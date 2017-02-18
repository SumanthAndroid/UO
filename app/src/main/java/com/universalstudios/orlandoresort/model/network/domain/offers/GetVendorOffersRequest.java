package com.universalstudios.orlandoresort.model.network.domain.offers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author acampbell
 *
 */
public class GetVendorOffersRequest extends UniversalOrlandoServicesRequest implements Callback<OfferSeries> {

    private static final String TAG = GetVendorOffersRequest.class.getSimpleName();

    private final String mVendor;

    protected GetVendorOffersRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType,
            GetVendorOffersParams getVendorOffersParams) {
        super(senderTag, priority, concurrencyType, getVendorOffersParams);

        mVendor = getVendorOffersParams.vendor;
    }

    public static class GetVendorOffersParams extends NetworkParams {

        private String vendor;

        public GetVendorOffersParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private final GetVendorOffersParams getVendorOffersParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getVendorOffersParams = new GetVendorOffersParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setVendor(String vendor) {
            this.getVendorOffersParams.vendor = vendor;
            return getThis();
        }

        public GetVendorOffersRequest build() {
            return new GetVendorOffersRequest(senderTag, priority, concurrencyType, getVendorOffersParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        GetVendorOffersParams params = (GetVendorOffersParams) getNetworkParams();
        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    OfferSeries response = services.getVendorOffers(params.vendor);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getVendorOffers(params.vendor, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        // Delete all vendors offers if response is 404
        if (retrofitError != null && retrofitError.getResponse() != null
                && retrofitError.getResponse().getStatus() == 404) {
            deleteOldOffersFromDatabase(new ArrayList<Offer>(), UniversalOrlandoApplication.getAppContext().getContentResolver());
        }

        super.handleFailure(new OfferSeriesResponse(null), retrofitError);
    }

    @Override
    public void success(OfferSeries offerSeries, Response response) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "success");
        }

        // Sync offer series and offers to the database
        syncOfferSeriesWithDatabase(offerSeries);

        // Inform any listeners after saving the response
        super.handleSuccess(new OfferSeriesResponse(offerSeries), response);
    }

    private void syncOfferSeriesWithDatabase(OfferSeries offerSeries) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "syncOfferSeriesWithDatabase");
        }

        if (offerSeries == null) {
            return;
        }

        // Query for vendor's offer series
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        String selection = new StringBuilder(OfferSeriesTable.COL_VENDOR).append(" LIKE '").append(mVendor)
                .append("'").toString();
        Cursor cursor = contentResolver.query(UniversalOrlandoContentUris.OFFER_SERIES, null, selection,
                null, null);

        // Offer series found, update records
        if (cursor != null && cursor.moveToFirst()) {
            String offerSeriesObjectJson = cursor.getString(cursor
                    .getColumnIndex(OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON));
            if (!OfferSeries.areOfferSeriesEqual(offerSeries, offerSeriesObjectJson)) {
                updateOfferSeriesInDatabase(offerSeries, contentResolver);
            }
        }
        // Offer series not present insert new values
        else {
            insertOfferSeriesInDatabase(offerSeries, contentResolver);
        }

        // Sync vendor's offers
        if (offerSeries.getOffers() != null) {
            syncOffersWithDatabase(offerSeries.getOffers(), contentResolver);
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "syncOfferSeriesWithDatabase: no offers to sync");
            }
        }

        // Close cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }

    private void syncOffersWithDatabase(List<Offer> offers, ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "syncOffersWithDatabase");
        }

        // Query for vendor's offers
        String selection = new StringBuilder(OffersTable.COL_VENDOR).append(" LIKE '").append(mVendor)
                .append("'").toString();
        Cursor cursor = contentResolver
                .query(UniversalOrlandoContentUris.OFFERS, null, selection, null, null);

        // List to track offers that need to be inserted
        List<Offer> newOfferList = new ArrayList<Offer>();

        for (Offer offerFromResp : offers) {
            boolean offerFoundInDb = false;

            // Start the cursor at the first row
            if (cursor != null && cursor.moveToFirst()) {
                // Go through all vendor's offers
                do {
                    long offerId = cursor.getLong(cursor.getColumnIndex(OffersTable.COL_OFFER_ID));

                    // If the offer is found, check to see if it needs to be updated
                    if (offerId == offerFromResp.getId()) {
                        String offerObjectJson = cursor.getString(cursor
                                .getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));

                        // If the offer in the database is different than the new offer, update the database
                        if (!Offer.areOffersEqual(offerFromResp, offerObjectJson)) {
                            updateOfferInDatabase(offerFromResp, contentResolver);
                        }

                        // Stop looping after finding the offer
                        offerFoundInDb = true;
                        break;
                    }

                } while (cursor.moveToNext());
            }

            // If offer was not found, add it to the list to be inserted
            if (!offerFoundInDb) {
                newOfferList.add(offerFromResp);
            }
        }

        // Insert any new offers
        insertOffersInDatabase(newOfferList, contentResolver);

        // Delete any offers from vendor that are not in this list
        deleteOldOffersFromDatabase(offers, contentResolver);

        // Close cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }

    private void deleteOldOffersFromDatabase(List<Offer> latestOffers, ContentResolver contentResolver) {
        if (latestOffers == null) {
            return;
        }

        StringBuilder selection = new StringBuilder(OffersTable.COL_VENDOR).append(" LIKE '").append(mVendor)
                .append("'");
        if (!latestOffers.isEmpty()) {
            selection.append(" AND ").append(OffersTable.COL_OFFER_ID).append(" NOT IN (");

            for (int i = 0; i < latestOffers.size(); i++) {
                Offer offer = latestOffers.get(i);
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(offer.getId()).append("'");
            }
            selection.append(")");
        }

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.OFFERS,
                    selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteOldOffersFromDatabase: old offers removed = " + rowsDeleted + " vendor = "
                        + mVendor);
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG,
                        "deleteOldOffersFromDatabase: exception deleting old point of interests in the database, vendor = "
                                + mVendor, e);
            }
        }
    }

    private void insertOffersInDatabase(List<Offer> newOfferList, ContentResolver contentResolver) {
        if (newOfferList == null || newOfferList.isEmpty()) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertOffersInDatabase: inserting new offers = " + newOfferList.size() + " vendor = "
                    + mVendor);
        }

        ContentValues[] contentValuesArray = new ContentValues[newOfferList.size()];
        for (int i = 0; i < newOfferList.size(); i++) {
            Offer offer = newOfferList.get(i);

            ContentValues contentValues = createOfferContentValues(offer);
            contentValuesArray[i] = contentValues;
        }

        try {
            contentResolver.bulkInsert(UniversalOrlandoContentUris.OFFERS, contentValuesArray);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertOffersInDatabase: exception inserting offers into the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private void updateOfferInDatabase(Offer offerToUpdate, ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "updateOfferInDatabase: updating offer = " + offerToUpdate.getDisplayName()
                    + " vendor = " + mVendor);
        }

        ContentValues contentValues = createOfferContentValues(offerToUpdate);
        String selection = new StringBuilder(OffersTable.COL_OFFER_ID).append(" = '")
                .append(offerToUpdate.getId()).append("'").toString();

        try {
            contentResolver.update(UniversalOrlandoContentUris.OFFERS, contentValues, selection, null);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "updateOfferInDatabase: exception updating offer in the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private ContentValues createOfferContentValues(Offer offerToUpdate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OffersTable.COL_OFFER_ID, offerToUpdate.getId());
        contentValues.put(OffersTable.COL_VENDOR, mVendor);
        contentValues.put(OffersTable.COL_VENUE_ID, offerToUpdate.getVenueId());
        contentValues.put(OffersTable.COL_OFFER_TYPE, offerToUpdate.getOfferType());
        contentValues.put(OffersTable.COL_DISPLAY_NAME, offerToUpdate.getDisplayName());
        contentValues.put(OffersTable.COL_SHORT_DESCRIPTION, offerToUpdate.getShortDescription());
        contentValues.put(OffersTable.COL_THUMBNAIL_IMAGE_URL, offerToUpdate.getThumbnailImageUrl());
        contentValues.put(OffersTable.COL_END_DATE, offerToUpdate.getEndDateUnix());
        contentValues.put(OffersTable.COL_LATITUDE, offerToUpdate.getLatitude());
        contentValues.put(OffersTable.COL_LONGITUDE, offerToUpdate.getLongitude());
        contentValues.put(OffersTable.COL_OFFER_OBJECT_JSON, offerToUpdate.toJson());

        return contentValues;
    }

    private void updateOfferSeriesInDatabase(OfferSeries offerSeries, ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "updateOfferSeriesInDatabase");
        }

        ContentValues contentValues = createOfferSeriesContentValues(offerSeries);
        String selection = new StringBuilder(OfferSeriesTable.COL_VENDOR).append(" LIKE '").append(mVendor)
                .append("'").toString();

        try {
            contentResolver.update(UniversalOrlandoContentUris.OFFER_SERIES, contentValues, selection, null);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertOfferSeriesInDatabase: exception inserting offer series into the database",
                        e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private void insertOfferSeriesInDatabase(OfferSeries offerSeries, ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "insertOfferSeriesInDatabase: inserting offer series for vendor = " + mVendor);
        }

        ContentValues contentValues = createOfferSeriesContentValues(offerSeries);

        try {
            contentResolver.insert(UniversalOrlandoContentUris.OFFER_SERIES, contentValues);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertOfferSeriesInDatabase: exception inserting offer series into the database",
                        e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private ContentValues createOfferSeriesContentValues(OfferSeries offerSeries) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OfferSeriesTable.COL_OFFER_SERIES_ID, offerSeries.getId());
        contentValues.put(OfferSeriesTable.COL_VENDOR, mVendor);
        contentValues.put(OfferSeriesTable.COL_DISPLAY_NAME, offerSeries.getDisplayName());
        contentValues.put(OfferSeriesTable.COL_LATITUDE, offerSeries.getLatitude());
        contentValues.put(OfferSeriesTable.COL_LONGITUDE, offerSeries.getLongitude());
        contentValues.put(OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON, offerSeries.toJson());

        return contentValues;
    }

}
