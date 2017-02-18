/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.general.BasicInfoDetailActivity;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ExpressPassDetailFragment extends Fragment implements OnClickListener {

    private static final String TAG = ExpressPassDetailFragment.class.getSimpleName();
    private static final String VIEW_TAG_ATTRACTIONS = "VIEW_TAG_ATTRACTIONS";
    private static final String VIEW_TAG_BUY_IN_PARK = "VIEW_TAG_BUY";

    private LinearLayout mButtonRootLinearLayout;
    private TextView mRestrictionsTextView;
    private ViewGroup mRestrictionsTextViewGroup;
    private int mCalculatedImageHeightDp;
    private ImageView mHeroImage;

    public static ExpressPassDetailFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        ExpressPassDetailFragment fragment = new ExpressPassDetailFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        // Add parameters to the argument bundle
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
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
            // Track the page view
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES, AnalyticsUtils.CONTENT_SUB_1_FAQ,
                    AnalyticsUtils.CONTENT_SUB_2_EXPRESS_PASS_INFO, AnalyticsUtils.PROPERTY_NAME_PARKS, null,
                    null);
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {}

        // Get the smallest (portrait) width in dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float smallestWidthDp = Math.min(widthDp, heightDp);

        // Compute the height based on image aspect ratio 1080x760 @ 480dpi
        mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_detail_express_pass, container, false);

        // Setup Views
        mButtonRootLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_detail_express_buton_root_linear_layout);
        mRestrictionsTextView = (TextView) fragmentView.findViewById(R.id.fragment_detail_express_pass_restrictions_textview);
        mHeroImage = (ImageView) fragmentView.findViewById(R.id.fragment_detail_express_pass_hero_image);
        mRestrictionsTextViewGroup = (ViewGroup) fragmentView.findViewById(R.id.fragment_detail_express_pass_restrictions_viewgroup);

        mRestrictionsTextView.setOnClickListener(this);

        // For USH, hide the restrictions
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            mRestrictionsTextViewGroup.setVisibility(View.GONE);
        }
        // Otherwise, show them
        else {
            mRestrictionsTextViewGroup.setVisibility(View.VISIBLE);
        }

        // Set image height based on detail image aspect ratio
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mCalculatedImageHeightDp, displayMetrics));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mHeroImage.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = calculatedImageHeightPx;
        mHeroImage.setLayoutParams(layoutParams);

        View view = FeatureListUtils.createFeatureItemView(mButtonRootLinearLayout,
                R.drawable.ic_detail_feature_express_pass, false,
                R.string.detail_feature_express_pass_participating_attractions, null, null, true,
                VIEW_TAG_ATTRACTIONS, this);
        view.setOnClickListener(this);
        mButtonRootLinearLayout.addView(view);

        view = FeatureListUtils.createFeatureItemView(mButtonRootLinearLayout,
                R.drawable.ic_detail_feature_price_gray, false,
                R.string.detail_basic_info_express_pass_line_buy_in_park, null, null, true,
                VIEW_TAG_BUY_IN_PARK, this);
        view.setOnClickListener(this);
        mButtonRootLinearLayout.addView(view);

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewStateRestored: savedInstanceState "
                    + (savedInstanceState == null ? "==" : "!=") + " null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPause");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        if (VIEW_TAG_ATTRACTIONS.equals(v.getTag())) {
            // Open the explore by express pass attractions - has express pass line
            FilterOptions filterOptions = new FilterOptions(null, ExploreType.ATTRACTIONS_EXPRESS_PASS);
            filterOptions.setExpressPassLine(true);
            Bundle bundle = ExploreActivity.newInstanceBundle(
                    R.string.detail_feature_express_pass_participating_attractions,
                    ExploreType.ATTRACTIONS_EXPRESS_PASS, filterOptions);
            startActivity(new Intent(getActivity(), ExploreActivity.class).putExtras(bundle));
        } else if (VIEW_TAG_BUY_IN_PARK.equals(v.getTag())) {
            // Open the explore by shopping - sells express pass page
            Bundle bundle = ExploreActivity.newInstanceBundle(R.string.action_title_express_pass_locations,
                    ExploreType.SHOPPING_EXPRESS_PASS);
            startActivity(new Intent(getActivity(), ExploreActivity.class).putExtras(bundle));
        } else if (v.equals(mRestrictionsTextView)) {
            Bundle budle = BasicInfoDetailActivity.newInstanceBundle(R.string.action_title_express_pass,
                    null, R.string.detail_basic_info_express_pass_restrictions_description);
            startActivity(new Intent(getActivity(), BasicInfoDetailActivity.class).putExtras(budle));
        }
    }

}
