package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferSeries;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;

/**
 * @author acampbell
 *
 */
public class OfferSeriesDetailFragment extends DatabaseQueryFragment implements OnClickListener {

    private static final String TAG = OfferSeriesDetailFragment.class.getSimpleName();

    private View mApplyButton;
    private View mApplybuttonContainer;
    private TextView mApplyButtonTextView;
    private boolean mPageTracked;
    private String mOfferUrl;
    private OfferSeries mOfferSeries;

    public static OfferSeriesDetailFragment newInstance(DatabaseQuery databaseQuery) {
        OfferSeriesDetailFragment fragment = new OfferSeriesDetailFragment();
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
            mPageTracked = true;
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
        View view = inflater.inflate(R.layout.fragment_detail_offer_series, container, false);
        mApplybuttonContainer = view.findViewById(R.id.fragment_offer_series_detail_bottom_action_bar);
        mApplybuttonContainer.setVisibility(View.GONE);
        //mApplybuttonContainer.setVisibility(View.GONE);
        mApplyButton = view.findViewById(R.id.fragment_offer_series_detail_apply_button);
        mApplyButton.setOnClickListener(this);
        mApplyButtonTextView = (TextView) view.findViewById(R.id.fragment_offer_series_detail_apply_button_text);

        DatabaseQuery databaseQuery = getDatabaseQuery();

        // Images and title fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_images_and_title_container,
                        OfferSeriesImagesAndDetailFragment.newInstance(databaseQuery)).commit();
        // Offer feature list fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_feature_list_container,
                        OfferSeriesFeatureListDetailFragment.newInstance(databaseQuery)).commit();

        return view;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> laoder) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onLoaderReset");
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
                    String name = data
                            .getString(data.getColumnIndex(OfferSeriesTable.COL_ALIAS_DISPLAY_NAME));
                    String offerSeriesObjectJson = data.getString(data
                            .getColumnIndex(OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON));
                    
                    mOfferSeries = GsonObject.fromJson(offerSeriesObjectJson, OfferSeries.class);
                    if (mOfferSeries == null || mOfferSeries.getLatitude() == null
                            || mOfferSeries.getLongitude() == null) {
                      // Keep Apply Button visible
                    } else {
                        mApplybuttonContainer.setVisibility(View.VISIBLE);
                    }
                    
                    if (!TextUtils.isEmpty(name)) {
                        getActivity().getActionBar().setTitle(name);
                    }

                    if (!TextUtils.isEmpty(mOfferSeries.getApplyButtonText()) && !TextUtils.isEmpty(mOfferSeries.getApplyButtonUrl())) {
                      mApplybuttonContainer.setVisibility(View.VISIBLE);
                      mApplyButtonTextView.setText(mOfferSeries.getApplyButtonText());
                      mOfferUrl = mOfferSeries.getApplyButtonUrl();
                    } else {
                      mApplybuttonContainer.setVisibility(View.GONE);
                    }

                    // Track page view
                    if (mOfferSeries != null && !mPageTracked) {
                        mPageTracked = true;
                        AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PROMOTIONS,
                                null, null, String.format(
                                        AnalyticsUtils.CONTENT_SUB_2_OFFER_SERIES_DETAIL_FORMAT,
                                        mOfferSeries.getDisplayName()), null, null, null);
                    }
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onClick");
        }

        switch (v.getId()) {
            case R.id.fragment_offer_series_detail_apply_button:
//                **OLD IMPLEMENTATION** Open a map page to the selected POI
//                Bundle detailMapBundle = MapDetailActivity.newInstanceBundle(getDatabaseQuery(),
//                        ExploreType.OFFER_SERIES, null);
//                startActivity(new Intent(getActivity(), MapDetailActivity.class).putExtras(detailMapBundle));
              
                if (mOfferUrl != null && !TextUtils.isEmpty(mOfferUrl)) {
                  
                  if (mOfferSeries != null) {
                    AnalyticsUtils.trackPageView(
                            AnalyticsUtils.CONTENT_GROUP_PROMOTIONS,
                        null,
                        null,
                        String.format(AnalyticsUtils.CONTENT_SUB_2_OFFER_SERIES_APPLY_CLICK, mOfferSeries.getDisplayName()),
                        null,
                        null,
                        null);
                  }
                  
                  Bundle bundle = WebViewActivity.newInstanceBundle(-1, mOfferUrl);
                  startActivity(new Intent(getActivity(), WebViewActivity.class).putExtras(bundle));
                }
                break;
        }
    }

}
