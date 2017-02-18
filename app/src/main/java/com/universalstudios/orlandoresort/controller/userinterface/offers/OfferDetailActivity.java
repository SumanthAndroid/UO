package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.MapDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;

/**
 * @author acampbell
 *
 */
public class OfferDetailActivity extends DatabaseQueryActivity implements OnClickListener {

    private static final String TAG = OfferDetailActivity.class.getSimpleName();

    private static final String KEY_ARG_OFFER_ID = "KEY_ARG_OFFER_ID";

    private View mMapContainer;
    private View mExploreMapRootContainer;
    private View mExploreMapWrapperContainer;
    private View mCouponButtonContainer;
    private View mCouponButton;
    private long mOfferId;
    private boolean mPageTracked;

    public static Bundle newInstanceBundle(DatabaseQuery databaseQuery, long offerId) {
        Bundle args = new Bundle();
        args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        args.putLong(KEY_ARG_OFFER_ID, offerId);

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_detail_offer);

        mExploreMapRootContainer = findViewById(R.id.detail_explore_map_root_container);
        mMapContainer = findViewById(R.id.detail_explore_map_container);
        mExploreMapWrapperContainer = findViewById(R.id.detail_explore_map_wrapper_container);
        mExploreMapWrapperContainer.setOnClickListener(this);
        mCouponButtonContainer = findViewById(R.id.activity_offer_detail_bottom_action_bar);
        mCouponButtonContainer.setVisibility(View.GONE);
        mCouponButton = findViewById(R.id.activity_offer_detail_coupon_button);
        mCouponButton.setOnClickListener(this);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {}
        // Otherwise, set incoming parameters
        else {
            mOfferId = args.getLong(KEY_ARG_OFFER_ID);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

            DatabaseQuery databaseQuery = getDatabaseQuery();

            // Images and title fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_images_and_title_container,
                            OfferImagesAndDetailFragment.newInstance(databaseQuery)).commit();
            // Feature list fragment secondary
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_feature_list_secondary_container,
                            OfferFeatureListDetailFragment.newInstance(databaseQuery, true)).commit();
            // Feature list fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_feature_list_container,
                            OfferFeatureListDetailFragment.newInstance(databaseQuery, false)).commit();
            // Map fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mMapContainer.getId(),
                            ExploreMapFragment.newInstance(getDatabaseQuery(), ExploreType.OFFERS)).commit();
        }
        // Otherwise, restore state
        else {
            mPageTracked = true;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onOptionsItemSelected");
        }

        switch (loader.getId()) {
            case LOADER_ID_DATABASE_QUERY:
                // Set action bar title and add map fragment if needed
                if (data != null && data.moveToFirst()) {
                    String offerObjectJson = data.getString(data
                            .getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));
                    Offer offer = GsonObject.fromJson(offerObjectJson, Offer.class);
                    if (offer != null) {
                        // Set action bar title
                        getActionBar().setTitle(offer.getDisplayName());
                        // Show or hide coupon code button
                        if (offer.getRequiresCouponCode() != null && offer.getRequiresCouponCode()) {
                            mCouponButtonContainer.setVisibility(View.VISIBLE);
                        } else {
                            mCouponButtonContainer.setVisibility(View.GONE);
                        }

                        // Show or hide map
                        if (offer.getLatitude() != null || offer.getLongitude() != null) {
                            mExploreMapRootContainer.setVisibility(View.VISIBLE);
                        } else {
                            mExploreMapRootContainer.setVisibility(View.GONE);
                        }
                    } else {
                        mExploreMapRootContainer.setVisibility(View.GONE);
                    }

                    // Track page view
                    if (offer != null && !mPageTracked) {
                        String offerSeriesDisplayName = data.getString(data
                                .getColumnIndex(OfferSeriesTable.COL_ALIAS_DISPLAY_NAME));
                        mPageTracked = true;
                        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PROMOTIONS, null,
                                null, String.format(AnalyticsUtils.CONTENT_SUB_2_OFFER_DETAIL_FORMAT,
                                        offerSeriesDisplayName, offer.getDisplayName()), null, null, null);
                    }

                } else {
                    mExploreMapRootContainer.setVisibility(View.GONE);
                }
                break;
            default:
                super.onLoadFinished(loader, data);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onClick");
        }

        switch (v.getId()) {
            case R.id.detail_explore_map_wrapper_container:
                // Open a map page to the selected POI
                Bundle detailMapBundle = MapDetailActivity.newInstanceBundle(getDatabaseQuery(),
                        ExploreType.OFFERS, null, null);
                startActivity(new Intent(this, MapDetailActivity.class).putExtras(detailMapBundle));
                break;
            case R.id.activity_offer_detail_coupon_button:
                Bundle couponCodeArgs = OfferCouponCodeActivity.newInstanceBundle(mOfferId);
                startActivity(new Intent(this, OfferCouponCodeActivity.class).putExtras(couponCodeArgs));
                break;
        }
    }
}
