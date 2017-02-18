package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferCouponRequest;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferCouponResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;

import java.util.Arrays;

/**
 * @author acampbell
 *
 */
public class OfferCouponCodeFragment extends DatabaseQueryFragment {

    private static final String TAG = OfferCouponCodeFragment.class.getSimpleName();

    private static final String KEY_ARG_OFFER_ID = "KEY_ARG_OFFER_ID";

    private long mOfferId;
    private View mNoResultsLayout;
    private View mProgressBar;
    private View mCouponContainer;
    private TextView mCouponDescTextView;
    private TextView mCouponCodeTextView;
    private String mOfferSeriesName;
    private String mOfferName;

    public static OfferCouponCodeFragment newInstance(long offerId) {
        OfferCouponCodeFragment fragment = new OfferCouponCodeFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_ARG_OFFER_ID, offerId);

        DatabaseQuery databaseQuery = DatabaseQueryUtils.getOffersById(Arrays.asList(offerId));
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
        else {
            mOfferId = args.getLong(KEY_ARG_OFFER_ID);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {// If this is the first creation, default state variables

        }
        // Otherwise, restore state
        else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Setup views
        View view = inflater.inflate(R.layout.fragment_offer_coupon_code, container, false);
        mProgressBar = view.findViewById(R.id.fragment_offer_coupon_code_progressbar);
        mNoResultsLayout = view.findViewById(R.id.fragment_offer_coupon_code_no_results_layout);
        mCouponContainer = view.findViewById(R.id.fragment_offer_coupon_code_coupon_container);
        mCouponDescTextView = (TextView) view.findViewById(R.id.fragment_offer_coupon_code_desc_textview);
        mCouponCodeTextView = (TextView) view.findViewById(R.id.fragment_offer_coupon_code_code_textview);

        // Show loading progress bar, hide others
        mProgressBar.setVisibility(View.VISIBLE);
        mNoResultsLayout.setVisibility(View.GONE);
        mCouponContainer.setVisibility(View.GONE);

        // Request coupon code from service
        GetOfferCouponRequest request = new GetOfferCouponRequest.Builder(this)
                .setOfferId(mOfferId)
                .build();
        NetworkUtils.queueNetworkRequest(request);
        NetworkUtils.startNetworkService();

        return view;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished");
        }
        switch (loader.getId()) {
            case LOADER_ID_DATABASE_QUERY:
                if (data != null && data.moveToFirst()) {
                    // Get values need to track coupon success and failures
                    mOfferSeriesName = data.getString(data
                            .getColumnIndex(OfferSeriesTable.COL_ALIAS_DISPLAY_NAME));
                    mOfferName = data.getString(data.getColumnIndex(OffersTable.COL_ALIAS_DISPLAY_NAME));
                }
                break;
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {

        if (networkResponse instanceof GetOfferCouponResponse) {

            GetOfferCouponResponse couponResponse = (GetOfferCouponResponse) networkResponse;
            if (couponResponse.isHttpStatusCodeSuccess()) {
                handleSuccessOfferCouponResponse(couponResponse);
            } else {
                handleFailureOfferCouponResponse();
            }
        }

    }

    private void handleSuccessOfferCouponResponse(GetOfferCouponResponse couponResponse) {
        // Show coupon layout hide others
        mProgressBar.setVisibility(View.GONE);
        mNoResultsLayout.setVisibility(View.GONE);
        mCouponContainer.setVisibility(View.VISIBLE);

        // Set coupon text
        mCouponDescTextView.setText(couponResponse.getShortDescription());
        mCouponCodeTextView.setText(couponResponse.getCouponCode());

        // Track coupon success
        if (!TextUtils.isEmpty(mOfferName) && !TextUtils.isEmpty(mOfferSeriesName)) {
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PROMOTIONS, null,
                    null, String.format(AnalyticsUtils.CONTENT_SUB_2_OFFER_COUPON_SUCESS,
                            mOfferSeriesName, mOfferName), null, null, null);
        }
    }

    private void handleFailureOfferCouponResponse() {
        // Show no results layout, hide others
        mProgressBar.setVisibility(View.GONE);
        mNoResultsLayout.setVisibility(View.VISIBLE);
        mCouponContainer.setVisibility(View.GONE);

        // Track coupon failure
        if (!TextUtils.isEmpty(mOfferName) && !TextUtils.isEmpty(mOfferSeriesName)) {
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PROMOTIONS, null, null,
                    String.format(AnalyticsUtils.CONTENT_SUB_2_OFFER_COUPON_FAIL, mOfferSeriesName,
                            mOfferName), null, null, null);
        }
    }


}
