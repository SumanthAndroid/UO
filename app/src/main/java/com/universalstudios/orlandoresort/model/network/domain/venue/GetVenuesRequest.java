package com.universalstudios.orlandoresort.model.network.domain.venue;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.domain.preloadingdb.SyncVenuesWithDatabase;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Steven Byle
 */
public class GetVenuesRequest extends UniversalOrlandoServicesRequest implements Callback<GetVenuesResponse> {
	private static final String TAG = GetVenuesRequest.class.getSimpleName();

	private GetVenuesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
		super(senderTag, priority, concurrencyType, null);
	}

	public static class Builder extends BaseNetworkRequestBuilder<Builder> {

		public Builder(NetworkRequestSender sender) {
			super(sender);
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public GetVenuesRequest build() {
			return new GetVenuesRequest(senderTag, priority, concurrencyType);
		}
	}

	@Override
	public void makeNetworkRequest(RestAdapter restAdapter) {
		super.makeNetworkRequest(restAdapter);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeNetworkRequest");
		}

		UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

		switch (getConcurrencyType()) {
			case SYNCHRONOUS:
				try {
					GetVenuesResponse response = services.getVenues(ServiceEndpointUtils.getCity());
					success(response, null);
				}
				catch (RetrofitError retrofitError) {
					failure(retrofitError);
				}
				break;
			case ASYNCHRONOUS:
				services.getVenues(ServiceEndpointUtils.getCity(), this);
				break;
			default:
				throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
		}

	}

	@Override
	public void success(GetVenuesResponse getVenuesResponse, Response response) {
		if (getVenuesResponse != null) {
//			// Sync the venues and lands with their respective database tables
//			List<Venue> venueList = getVenuesResponse.getVenues();
//			syncVenuesWithDatabase(venueList);
//			syncVenueLandsWithDatabase(venueList);
//
//			// Store the last time venues were synced
//			UniversalAppState universalOrlandoState = UniversalAppStateManager.getInstance(getAppContext());
//			universalOrlandoState.setDateOfLastVenueSyncInMillis(new Date().getTime());
//			UniversalAppStateManager.saveInstance(getAppContext());

			SyncVenuesWithDatabase syncVenuesWithDatabase = new SyncVenuesWithDatabase(getVenuesResponse);
			syncVenuesWithDatabase.successCaseVenues();
		}
		else {
			getVenuesResponse = new GetVenuesResponse();
		}

		// Inform any listeners after saving the response
		super.handleSuccess(getVenuesResponse, response);
	}

	@Override
	public void failure(RetrofitError retrofitError) {
		super.handleFailure(new GetVenuesResponse(), retrofitError);
	}

//	private void syncVenuesWithDatabase(List<Venue> venueList) {
//		if (venueList == null) {
//			return;
//		}
//
//		// Query for all venues
//		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
//		Cursor venuesCursor = contentResolver.query(UniversalOrlandoContentUris.VENUES, null, null, null, null);
//
//		// List to track venues that need to be inserted
//		List<Venue> newVenueList = new ArrayList<Venue>();
//
//		for (Venue venue : venueList) {
//			boolean venueFoundInDb = false;
//
//			// Start the cursor at the first row
//			if (venuesCursor != null && venuesCursor.moveToFirst()) {
//				// Go through every venue in the database
//				do {
//					int venueId = venuesCursor.getInt(venuesCursor.getColumnIndex(VenuesTable.COL_VENUE_ID));
//
//					// If the venue is found, check to see if it needs to be updated
//					if (venueId == venue.getId()) {
//						String venueObjectJson = venuesCursor.getString(venuesCursor.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
//						Venue venueFromJson = GsonObject.fromJson(venueObjectJson, Venue.class);
//
//						// If the venue in the database is different than the new venue, update the database
//						if (!venue.equals(venueFromJson)) {
//							updateVenueInDatabase(venue, contentResolver);
//						}
//
//						// Stop looping after finding the venue
//						venueFoundInDb = true;
//						break;
//					}
//				} while (venuesCursor.moveToNext());
//			}
//
//			// If the venue wasn't found, add it to the list to be inserted
//			if (!venueFoundInDb) {
//				newVenueList.add(venue);
//			}
//		}
//
//		// Insert any new venues
//		insertVenuesInDatabase(newVenueList, contentResolver);
//
//		// Delete any venues that are not in the latest venue set
//		deleteOldVenuesFromDatabase(venueList, contentResolver);
//
//		// Close the cursor
//		if (venuesCursor != null && !venuesCursor.isClosed()) {
//			venuesCursor.close();
//		}
//	}
//
//	private void syncVenueLandsWithDatabase(List<Venue> venueList) {
//		if (venueList == null) {
//			return;
//		}
//
//		// Get all of the venue lands from each venue
//		List<VenueLand> venueLandList = new ArrayList<VenueLand>();
//		for (Venue venue : venueList) {
//			List<VenueLand> venueLandSet = venue.getContainedLands();
//
//			if (venueLandSet != null) {
//				for (VenueLand venueLand : venueLandSet) {
//					venueLandList.add(venueLand);
//				}
//			}
//		}
//
//		// Query for all venue lands
//		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
//		Cursor venueLandsCursor = contentResolver.query(UniversalOrlandoContentUris.VENUE_LANDS, null, null, null, null);
//
//		// List to track venue lands that need to be inserted
//		List<VenueLand> newVenueLandList = new ArrayList<VenueLand>();
//
//		for (VenueLand venueLand : venueLandList) {
//			boolean venueLandFoundInDb = false;
//
//			// Start the cursor at the first row
//			if (venueLandsCursor != null && venueLandsCursor.moveToFirst()) {
//				// Go through every venue land in the database
//				do {
//					int venueLandId = venueLandsCursor.getInt(venueLandsCursor.getColumnIndex(VenueLandsTable.COL_VENUE_LAND_ID));
//
//					// If the venue land is found, check to see if it needs to be updated
//					if (venueLandId == venueLand.getId()) {
//						String venueLandObjectJson = venueLandsCursor.getString(venueLandsCursor.getColumnIndex(VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON));
//						VenueLand venueLandFromJson = GsonObject.fromJson(venueLandObjectJson, VenueLand.class);
//
//						// If the venue land in the database is different than the new venue land, update the database
//						if (!venueLand.equals(venueLandFromJson)) {
//							updateVenueLandInDatabase(venueLand, contentResolver);
//						}
//
//						// Stop looping after finding the venue
//						venueLandFoundInDb = true;
//						break;
//					}
//				} while (venueLandsCursor.moveToNext());
//			}
//
//			// If the venue wasn't found, add it to the list to be inserted
//			if (!venueLandFoundInDb) {
//				newVenueLandList.add(venueLand);
//			}
//		}
//
//		// Insert any new venue lands into the database
//		insertVenueLandsInDatabase(newVenueLandList, contentResolver);
//
//		// Delete any venue lands that are not in the latest venue set
//		deleteOldVenueLandsFromDatabase(venueLandList, contentResolver);
//
//		// Close the cursor
//		if (venueLandsCursor != null && !venueLandsCursor.isClosed()) {
//			venueLandsCursor.close();
//		}
//	}
//
//	private static ContentValues createVenueContentValues(Venue venue) {
//
//		ContentValues contentValues = new ContentValues();
//		contentValues.put(VenuesTable.COL_VENUE_ID, venue.getId());
//		contentValues.put(VenuesTable.COL_DISPLAY_NAME, venue.getDisplayName());
//		contentValues.put(VenuesTable.COL_LONG_DESCRIPTION, venue.getLongDescription());
//		contentValues.put(VenuesTable.COL_VENUE_OBJECT_JSON, venue.toJson());
//
//		List<VenueHours> venueHoursList = venue.getHours();
//		if (venueHoursList != null && venueHoursList.size() > 0) {
//			contentValues.put(VenuesTable.COL_HOURS_LIST_JSON, new Gson().toJson(venueHoursList));
//		}
//		else {
//			contentValues.put(VenuesTable.COL_HOURS_LIST_JSON, new Gson().toJson(new ArrayList<VenueHours>()));
//		}
//
//		return contentValues;
//	}
//
//	private static ContentValues createVenueLandContentValues(VenueLand venueLand) {
//
//		ContentValues contentValues = new ContentValues();
//		contentValues.put(VenueLandsTable.COL_VENUE_LAND_ID, venueLand.getId());
//		contentValues.put(VenueLandsTable.COL_CONTAINING_VENUE_ID, venueLand.getContainingVenueId());
//		contentValues.put(VenueLandsTable.COL_DISPLAY_NAME, venueLand.getDisplayName());
//		contentValues.put(VenueLandsTable.COL_LONG_DESCRIPTION, venueLand.getLongDescription());
//		contentValues.put(VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON, venueLand.toJson());
//
//		return contentValues;
//	}
//
//	private static void updateVenueInDatabase(Venue venueToUpdate, ContentResolver contentResolver) {
//		if (BuildConfig.DEBUG) {
//			Log.i(TAG, "updateVenueInDatabase: updating venue = " + venueToUpdate.getDisplayName());
//		}
//
//		ContentValues contentValues = createVenueContentValues(venueToUpdate);
//		String selection = new StringBuilder(VenuesTable.COL_VENUE_ID)
//		.append(" = '").append(venueToUpdate.getId()).append("'")
//		.toString();
//
//		try {
//			contentResolver.update(UniversalOrlandoContentUris.VENUES, contentValues, selection, null);
//		}
//		catch (Exception e) {
//			if (BuildConfig.DEBUG) {
//				Log.e(TAG, "updateVenueInDatabase: exception updating venue in the database", e);
//			}
//
//			// Log the exception to crittercism
//			Crittercism.logHandledException(e);
//		}
//	}
//
//	private static void updateVenueLandInDatabase(VenueLand venueLandToUpdate, ContentResolver contentResolver) {
//		if (BuildConfig.DEBUG) {
//			Log.i(TAG, "updateVenueLandInDatabase: updating venue land = " + venueLandToUpdate.getDisplayName());
//		}
//
//		ContentValues contentValues = createVenueLandContentValues(venueLandToUpdate);
//		String selection = new StringBuilder(VenueLandsTable.COL_VENUE_LAND_ID)
//		.append(" = '").append(venueLandToUpdate.getId()).append("'")
//		.toString();
//
//		try {
//			contentResolver.update(UniversalOrlandoContentUris.VENUE_LANDS, contentValues, selection, null);
//		}
//		catch (Exception e) {
//			if (BuildConfig.DEBUG) {
//				Log.e(TAG, "updateVenueLandInDatabase: exception updating venue land in the database", e);
//			}
//
//			// Log the exception to crittercism
//			Crittercism.logHandledException(e);
//		}
//	}
//
//	private static void insertVenuesInDatabase(List<Venue> newVenueList, ContentResolver contentResolver) {
//		if (newVenueList == null || newVenueList.size() == 0) {
//			return;
//		}
//
//		if (BuildConfig.DEBUG) {
//			Log.i(TAG, "insertVenuesInDatabase: inserting new venues = " + newVenueList.size());
//		}
//
//		ContentValues contentValuesArray[] = new ContentValues[newVenueList.size()];
//		for (int i = 0; i < newVenueList.size(); i++) {
//			Venue newVenue = newVenueList.get(i);
//
//			ContentValues contentValues = createVenueContentValues(newVenue);
//			contentValuesArray[i] = contentValues;
//		}
//
//		try {
//			contentResolver.bulkInsert(UniversalOrlandoContentUris.VENUES, contentValuesArray);
//		}
//		catch (Exception e) {
//			if (BuildConfig.DEBUG) {
//				Log.e(TAG, "insertVenuesInDatabase: exception inserting venue into the database", e);
//			}
//
//			// Log the exception to crittercism
//			Crittercism.logHandledException(e);
//		}
//	}
//
//	private static void insertVenueLandsInDatabase(List<VenueLand> newVenueLandList, ContentResolver contentResolver) {
//		if (newVenueLandList == null || newVenueLandList.size() == 0) {
//			return;
//		}
//
//		if (BuildConfig.DEBUG) {
//			Log.i(TAG, "insertVenueLandsInDatabase: inserting new venue lands = " + newVenueLandList.size());
//		}
//
//		ContentValues contentValuesArray[] = new ContentValues[newVenueLandList.size()];
//		for (int i = 0; i < newVenueLandList.size(); i++) {
//			VenueLand newVenueLand = newVenueLandList.get(i);
//
//			ContentValues contentValues = createVenueLandContentValues(newVenueLand);
//			contentValuesArray[i] = contentValues;
//		}
//
//		try {
//			contentResolver.bulkInsert(UniversalOrlandoContentUris.VENUE_LANDS, contentValuesArray);
//		}
//		catch (Exception e) {
//			if (BuildConfig.DEBUG) {
//				Log.e(TAG, "insertVenueLandsInDatabase: exception inserting venue land into the database", e);
//			}
//
//			// Log the exception to crittercism
//			Crittercism.logHandledException(e);
//		}
//	}
//
//	private static void deleteOldVenuesFromDatabase(List<Venue> latestVenueList, ContentResolver contentResolver) {
//		if (latestVenueList == null) {
//			return;
//		}
//
//		// Delete venues that don't match the latest id set, if the latest is empty, delete them all
//		String selection = null;
//		if (latestVenueList.size() > 0) {
//			StringBuilder selectionBuilder = new StringBuilder(VenuesTable.COL_VENUE_ID)
//			.append(" NOT IN (");
//
//			for (int i = 0; i < latestVenueList.size(); i++) {
//				Venue venue = latestVenueList.get(i);
//				if (i > 0) {
//					selectionBuilder.append(", ");
//				}
//				selectionBuilder.append("'").append(venue.getId()).append("'");
//			}
//			selectionBuilder.append(")");
//			selection = selectionBuilder.toString();
//		}
//
//		try {
//			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.VENUES, selection, null);
//			if (BuildConfig.DEBUG) {
//				Log.i(TAG, "deleteOldVenuesFromDatabase: old venues removed = " + rowsDeleted);
//			}
//		}
//		catch (Exception e) {
//			if (BuildConfig.DEBUG) {
//				Log.e(TAG, "deleteOldVenuesFromDatabase: exception deleting old venues in the database", e);
//			}
//
//			// Log the exception to crittercism
//			Crittercism.logHandledException(e);
//		}
//	}
//
//	private static void deleteOldVenueLandsFromDatabase(List<VenueLand> latestVenueLandList, ContentResolver contentResolver) {
//		if (latestVenueLandList == null) {
//			return;
//		}
//
//		// Delete venue lands that don't match the latest id set, if the latest is empty, delete them all
//		String selection = null;
//		if (latestVenueLandList.size() > 0) {
//			StringBuilder selectionBuilder = new StringBuilder(VenueLandsTable.COL_VENUE_LAND_ID)
//			.append(" NOT IN (");
//
//			for (int i = 0; i < latestVenueLandList.size(); i++) {
//				VenueLand venueLand = latestVenueLandList.get(i);
//				if (i > 0) {
//					selectionBuilder.append(", ");
//				}
//				selectionBuilder.append("'").append(venueLand.getId()).append("'");
//			}
//			selectionBuilder.append(")");
//			selection = selectionBuilder.toString();
//		}
//
//		try {
//			int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.VENUE_LANDS, selection, null);
//			if (BuildConfig.DEBUG) {
//				Log.i(TAG, "deleteOldVenueLandsFromDatabase: old venue lands removed = " + rowsDeleted);
//			}
//		}
//		catch (Exception e) {
//			if (BuildConfig.DEBUG) {
//				Log.e(TAG, "deleteOldVenueLandsFromDatabase: exception deleting old venue lands in the database", e);
//			}
//
//			// Log the exception to crittercism
//			Crittercism.logHandledException(e);
//		}
//	}

}