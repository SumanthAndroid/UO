/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryListener;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryTask;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.tridion.MobilePage;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import java.util.List;

/**
 * 
 * 
 * @author Matthew Velie
 */
public class PackagePickupDetailFragment extends Fragment implements OnClickListener {

    private static final String TAG = PackagePickupDetailFragment.class.getSimpleName();
    private static final String VIEW_TAG_ATTRACTIONS = "VIEW_TAG_ATTRACTIONS";
    private static final String PAGE_IDENTIFIER = "PACKAGE_PICKUP";

    private LinearLayout mButtonRootLinearLayout;
    private TextView mDescriptionTextView;
    private int mCalculatedImageHeightDp;
    private ImageView mHeroImage;

    public static PackagePickupDetailFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        PackagePickupDetailFragment fragment = new PackagePickupDetailFragment();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            // Track the page view
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES, AnalyticsUtils.CONTENT_SUB_1_FAQ,
                    AnalyticsUtils.CONTENT_SUB_2_PACKAGE_PICKUP_INFO, AnalyticsUtils.PROPERTY_NAME_PARKS, null,
                    null);
        }

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
        View fragmentView = inflater.inflate(R.layout.fragment_detail_package_pickup, container, false);

        // Setup Views
        mDescriptionTextView = (TextView)fragmentView.findViewById(R.id.fragment_detail_package_pickup_desc_text);
        mDescriptionTextView.setText(Html.fromHtml(getActivity().getResources().getString(R.string.detail_basic_info_package_pickup_description)));

        mButtonRootLinearLayout = (LinearLayout) fragmentView
                .findViewById(R.id.fragment_detail_package_pickup_buton_root_linear_layout);
        mHeroImage = (ImageView) fragmentView.findViewById(R.id.fragment_detail_package_pickup_hero_image);
        
        // Set image height based on detail image aspect ratio
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mCalculatedImageHeightDp, displayMetrics));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mHeroImage.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = calculatedImageHeightPx;
        mHeroImage.setLayoutParams(layoutParams);

        View view = FeatureListUtils.createFeatureItemView(mButtonRootLinearLayout,
                R.drawable.ic_detail_feature_package_pickup, false,
                R.string.detail_feature_package_pickup_participating_stores, null, null, true,
                VIEW_TAG_ATTRACTIONS, this);
        view.setOnClickListener(this);
        mButtonRootLinearLayout.addView(view);


        DatabaseQuery databaseQuery = new DatabaseQuery(UniversalOrlandoContentUris.MOBILE_PAGES.toString(), null, UniversalOrlandoDatabaseTables.MobilePagesTable.COL_IDENTIFIER + " = ?", new String[]{PAGE_IDENTIFIER}, null);
        DatabaseQueryTask<MobilePage> task = new DatabaseQueryTask<>(databaseQuery, MobilePage.class, new DatabaseQueryListener<List<MobilePage>>() {
            @Override
            public void onQueryComplete(List<MobilePage> result) {
                if (null != result && !result.isEmpty()) {
                    mDescriptionTextView.setText(Html.fromHtml(result.get(0).getShortDescription()));
                }
            }
        });
        task.execute();

        return fragmentView;
    }


    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        if (VIEW_TAG_ATTRACTIONS.equals(v.getTag())) {
            // Open the explore by shops that have package pickup
            FilterOptions filterOptions = new FilterOptions(null, ExploreType.ATTRACTIONS_PACKAGE_PICKUP);
            filterOptions.setShopPackagePickup(true);
            Bundle bundle = ExploreActivity.newInstanceBundle(
                    R.string.detail_feature_package_pickup_participating_stores,
                    ExploreType.ATTRACTIONS_PACKAGE_PICKUP, filterOptions);
            startActivity(new Intent(getActivity(), ExploreActivity.class).putExtras(bundle));
        }
    }
}
