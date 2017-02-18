package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.FeatureListUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferSeries;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;

import java.util.Collections;

/**
 * @author acampbell
 *
 */
public class OfferSeriesFeatureListDetailFragment extends DatabaseQueryFragment implements OnClickListener {

    private static final String TAG = OfferSeriesFeatureListDetailFragment.class.getSimpleName();

    private static final String VIEW_TAG_OFFER = "VIEW_TAG_OFFER";
    private static final String VIEW_TAG_NO_OFFERS = "VIEW_TAG_NO_OFFERS";

    private OfferSeries mOfferSeries;
    private LinearLayout mFeatureListLayout;
    private Picasso mPicasso;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;

    public static OfferSeriesFeatureListDetailFragment newInstance(DatabaseQuery databaseQuery) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "newInstance: databaseQuery = " + databaseQuery);
        }

        // Create a new fragment instance
        OfferSeriesFeatureListDetailFragment fragment = new OfferSeriesFeatureListDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {}
        // Otherwise, set incoming parameters
        else {}

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

        }
        // Otherwise restore state, overwriting any passed in parameters
        else {

        }

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(getActivity())
                .loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_offer_series_detail_feature_list, container,
                false);

        // Setup Views
        mFeatureListLayout = (LinearLayout) fragmentView
                .findViewById(R.id.fragment_offer_series_detail_feature_list_layout);

        return fragmentView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
     * .support.v4.content.Loader, java.lang.Object)
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onLoadFinished");
        }

        if (loader.getId() == LOADER_ID_DATABASE_QUERY) {
            // Clear the feature options and load new ones
            mFeatureListLayout.removeAllViews();

            if (data != null && data.moveToFirst()) {
                String offerSeriesObjectJson = data.getString(data
                        .getColumnIndex(OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON));
                mOfferSeries = GsonObject.fromJson(offerSeriesObjectJson, OfferSeries.class);
                addFeatures(mOfferSeries);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
     * .support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (tag.equals(VIEW_TAG_OFFER)) {
                Long offerId = (Long) v.getTag(R.id.key_view_tag_detail_feature_offer_id);
                Boolean showOfferDetail = (Boolean) v.getTag(R.id.key_view_tag_detail_feature_offer_has_details);
                boolean pageLoaded = false;
                // Load offer detail page
                if (offerId != null && (showOfferDetail != null && showOfferDetail)) {
                    pageLoaded = DetailUtils.openOfferDetailPage(v.getContext(), offerId);
                }
                // Display error message if offer detail page did not load
                if (!pageLoaded && (showOfferDetail != null && showOfferDetail)) {
                    UserInterfaceUtils.showToastFromForeground(getString(R.string.event_unable_to_load_page),
                            Toast.LENGTH_SHORT, getActivity());
                }
            }
        }
    }

    private void addFeatures(OfferSeries offerSeries) {
        View featureView;
        // Offers
        if (offerSeries.getOffers() != null) {
            // Sort offers
            Collections.sort(offerSeries.getOffers(), new Offer.AscendingComparator());

            for (Offer offer : offerSeries.getOffers()) {
                featureView = FeatureListUtils.createOfferFeatureItemView(mFeatureListLayout, mPicasso,
                        offer.getThumbnailImageUrl(), offer.getDisplayName(), offer.getShortDescription(),
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_OFFER, this);
                if(offer.getShowOfferDetailPage() != null && offer.getShowOfferDetailPage()) {
                    featureView.setClickable(true);
                }
                featureView.setTag(R.id.key_view_tag_detail_feature_offer_id, offer.getId());
                featureView.setTag(R.id.key_view_tag_detail_feature_offer_has_details, offer.getShowOfferDetailPage());
                mFeatureListLayout.addView(featureView);
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "No offer series present");
            }
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false,
                    R.string.offers_no_offers, null, null, (mFeatureListLayout.getChildCount() > 0),
                    VIEW_TAG_NO_OFFERS, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }
    }

}
