/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.FilterTabOption;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions.FilterSort;
import com.universalstudios.orlandoresort.controller.userinterface.filter.MultiChoiceFilterDialogFragment.MultiChoiceOptionsType;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.Switch;
import com.universalstudios.orlandoresort.view.segmentedcontrol.SegmentedGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Fragment for the advanced filter page accessible from the Explore fragments
 * 
 * @author acampbell
 *
 */
public class AdvancedFilterFragment extends Fragment implements OnClickListener, OnCheckedChangeListener,
        OnSeekBarChangeListener {

    private static final String TAG = AdvancedFilterFragment.class.getSimpleName();
    private static final String KEY_ARG_FILTER_OPTIONS = "KEY_ARG_FILTER_OPTIONS";
    private static final String KEY_STATE_FILTER_OPTIONS = "KEY_STATE_FILTER_OPTIONS";
    private static final String KEY_ARG_EXPLORE_TYPE = "KEY_EXPLORE_TYPE";

    private FilterOptions mFilterOptions;
    private SegmentedGroup mSegmentedGroup;
    private RadioButton mSortAlphabeticalRadioButton;
    private RadioButton mSortDistanceRadioButton;
    private RadioButton mSortWaitTimesRadioButton;
    private RadioButton mSortTypeRadioButton;
    private RadioButton mSortShowTimesRadioButton;
    private Switch mExpressLineSwitch;
    private Switch mKidFriendlySwitch;
    private RelativeLayout mUpperLotRelativeLayout;
    private Switch mUpperLotSwitch;
    private RelativeLayout mLowerLotRelativeLayout;
    private Switch mLowerLotSwitch;
    private RelativeLayout mAccessibilityRelativeLayout;
    private TextView mAccessibilitySelectedTextView;
    private LinearLayout mOtherRootLinearLayout;
    private TextView mSortDescriptionTextView;
    // Rides
    private SeekBar mRideMaxWaitSeekBar;
    private TextView mRidesMaxWaitTextView;
    private Switch mRideFilterSwitch;
    private LinearLayout mRideFilterContentsLinearLayout;
    private LinearLayout mRideFilterRootLinearLayout;
    private SeekBar mRideRiderHeightSeekBar;
    private TextView mRideRiderHeightTextView;
    private Switch mRideSingleRiderLineSwitch;
    private Switch mRideChildSwapSwitch;
    private RelativeLayout mRideVenuesRelativeLayout;
    private TextView mRideVenuesSelectedTextView;
    private RelativeLayout mRideTypeRelativeLayout;
    private TextView mRideTypeSelectedTextView;
    private int mRideMaxWaitStep;
    private int mRideMinRiderHeight;
    private Switch mRideParentalSupervisionSwitch;
    private Switch mRideLifeJacketRequiredSwitch;
    private ViewGroup mRideLifeJacketRequiredViewGroup;

    // Shows
    private LinearLayout mShowFilterContentsLinearLayout;
    private LinearLayout mShowFilterRootLinearLayout;
    private Switch mShowFilterSwitch;
    private RelativeLayout mShowVenuesRelativeLayout;
    private TextView mShowVenuesSelectedTextView;
    private RelativeLayout mShowTypeRelativeLayout;
    private TextView mShowTypeSelectedTextView;
    private SeekBar mShowNextShowSeekBar;
    private TextView mShowNextShowTextView;
    private int mShowNextShowStep;

    // Dining
    private LinearLayout mDiningFilterRootLinearLayout;
    private LinearLayout mDiningFilterContentsLinearLayout;
    private Switch mDiningFilterSwitch;
    private RelativeLayout mDiningVenuesRelativeLayout;
    private TextView mDiningVenuesSelectedTextView;
    private RelativeLayout mDiningTypeRelativeLayout;
    private TextView mDiningTypeSelectedTextView;
    private ViewGroup mDiningStartingPriceViewGroup;
    private SeekBar mDiningStartingPriceSeekBar;
    private TextView mDiningStartingPriceSelectedTextView;
    private int mDiningMinStartingPrice;
    private RelativeLayout mDiningPlanRelativeLayout;
    private TextView mDiningPlanSelectedTextView;
    private Switch mDiningCharacterDiningSwitch;
    private Switch mDiningVegetarianHealthySwitch;
    private Switch mDiningAmexSwitch;
    private ViewGroup mDiningAmexViewGroup;

    // Shopping
    private LinearLayout mShoppingFilterRootLinearLayout;
    private LinearLayout mShoppingFilterContentsLinearLayout;
    private Switch mShoppingFilterSwitch;
    private RelativeLayout mShoppingVenuesRelativeLayout;
    private TextView mShoppingVenuesSelectedTextView;
    private RelativeLayout mShoppingTypeRelativeLayout;
    private TextView mShoppingTypeSelectedTextView;
    private ViewGroup mShoppingExpressPassViewGroup;
    private Switch mShoppingExpressPassSwitch;
    private ViewGroup mShoppingAmexViewGroup;
    private Switch mShoppingAmexSwitch;
    private ViewGroup mShoppingPackagePickupViewGroup;
    private Switch mShoppingPackagePickupSwitch;

    //Rental Services
    private LinearLayout mRentalServicesFilterRootLinearLayout;
    private LinearLayout mRentalServicesFilterContentsLinearLayout;
    private Switch mRentalServicesFilterSwitch;
    private RelativeLayout mRentalServicesVenuesRelativeLayout;
    private TextView mRentalServicesVenuesSelectedTextView;

    //Rental Types
    private Switch mRentalTypeEcvSwitch;
    private Switch mRentalTypeStrollerSwitch;
    private Switch mRentalTypeWheelChairSwitch;

    // Entertainment
    private LinearLayout mEntertainmentFilterRootLinearLayout;
    private LinearLayout mEntertainmentFilterContentsLinearLayout;
    private Switch mEntertainmentFilterSwitch;
    private RelativeLayout mEntertainmentTypeRelativeLayout;
    private TextView mEntertainmentTypeSelectedTextView;
    private SeekBar mEntertainmentStartingPriceSeekBar;
    private TextView mEntertainmentStartingPriceSelectedTextView;
    private int mEntertainmentMinStartingPrice;
    private ExploreType mExploreType;

    private Switch mCocaColaFreestyleSwitch;
    private ViewGroup cocaColaContianer;

    public static AdvancedFilterFragment newInstance(FilterOptions filterOptions, ExploreType exploreType) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        AdvancedFilterFragment fragment = new AdvancedFilterFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);
        args.putString(KEY_ARG_FILTER_OPTIONS, filterOptions.toJson());
        fragment.setArguments(args);

        return fragment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Enable menu
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            mFilterOptions = GsonObject.fromJson(args.getString(KEY_ARG_FILTER_OPTIONS), FilterOptions.class);
            mExploreType = (ExploreType) args.getSerializable(KEY_ARG_EXPLORE_TYPE);
        }
        if (savedInstanceState != null) {
            // Track the page view
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_SITE_NAV, null, null,
                    AnalyticsUtils.CONTENT_SUB_2_APPLY_FILTER, AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
                    null, null);
            mFilterOptions = GsonObject.fromJson(savedInstanceState.getString(KEY_STATE_FILTER_OPTIONS),
                    FilterOptions.class);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onResume");
        }

        // Update UI based on current filter options
        updateViews();

        // Have to call for the segmented group's background to update after
        // some radio buttons are hidden from the layout
        mSegmentedGroup.updateBackground();

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        outState.putString(KEY_STATE_FILTER_OPTIONS, mFilterOptions.toJson());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
     * android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View view = inflater.inflate(R.layout.fragment_advanced_filter, container, false);

        mSegmentedGroup = (SegmentedGroup) view.findViewById(R.id.advand_filter_sort_segmented_control);

        // Setup click handler for sort radio buttons
        mSortAlphabeticalRadioButton = (RadioButton) view
                .findViewById(R.id.advanced_filter_sort_alphabetical);
        mSortAlphabeticalRadioButton.setOnClickListener(this);
        mSortDistanceRadioButton = (RadioButton) view.findViewById(R.id.advanced_filter_sort_distance);
        mSortDistanceRadioButton.setOnClickListener(this);
        mSortTypeRadioButton = (RadioButton) view.findViewById(R.id.advanced_filter_sort_type);
        mSortTypeRadioButton.setOnClickListener(this);
        mSortWaitTimesRadioButton = (RadioButton) view.findViewById(R.id.advanced_filter_sort_wait_times);
        mSortWaitTimesRadioButton.setOnClickListener(this);
        mSortShowTimesRadioButton = (RadioButton) view.findViewById(R.id.advanced_filter_sort_show_times);
        mSortDescriptionTextView = (TextView) view.findViewById(R.id.advanced_filter_sort_description);
        mSortShowTimesRadioButton.setOnClickListener(this);
        // Setup accessibility, kid-friendly, and express pass line options
        mUpperLotRelativeLayout = (RelativeLayout) view.findViewById(R.id.advanced_filter_upper_lot_relative_layout);
        mUpperLotSwitch = (Switch) view.findViewById(R.id.advanced_filter_upper_lot_switch);
        mUpperLotSwitch.setOnCheckedChangeListener(this);
        mLowerLotRelativeLayout = (RelativeLayout) view.findViewById(R.id.advanced_filter_lower_lot_relative_layout);
        mLowerLotSwitch = (Switch) view.findViewById(R.id.advanced_filter_lower_lot_switch);
        mLowerLotSwitch.setOnCheckedChangeListener(this);
        mExpressLineSwitch = (Switch) view.findViewById(R.id.advanced_filter_express_line_switch);
        mExpressLineSwitch.setOnCheckedChangeListener(this);
        mKidFriendlySwitch = (Switch) view.findViewById(R.id.advanced_filter_kid_friendly_switch);
        mKidFriendlySwitch.setOnCheckedChangeListener(this);
        mAccessibilityRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_accessibility_relative_layout);
        mAccessibilityRelativeLayout.setOnClickListener(this);
        mAccessibilitySelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_accessibility_selected_textview);
        mOtherRootLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_other_root_linear_layout);
        // Setup ride options
        mRideMaxWaitSeekBar = (SeekBar) view.findViewById(R.id.advanced_filter_ride_max_wait_seekbar);
        mRideMaxWaitSeekBar.setOnSeekBarChangeListener(this);
        mRidesMaxWaitTextView = (TextView) view.findViewById(R.id.advanced_filter_rides_max_wait_textview);
        mRideMaxWaitStep = getResources().getInteger(R.integer.advanced_filter_ride_wait_step);
        mRideFilterSwitch = (Switch) view.findViewById(R.id.advanced_filter_ride_filter_switch);
        mRideFilterSwitch.setOnCheckedChangeListener(this);
        mRideFilterContentsLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_ride_filter_contents_linear_layout);
        mRideFilterRootLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_rides_root_linear_layout);

        mRideMinRiderHeight = getResources().getInteger(R.integer.advanced_filter_ride_rider_height_min);
        mRideRiderHeightSeekBar = (SeekBar) view.findViewById(R.id.advanced_filter_ride_rider_height_seekbar);
        mRideRiderHeightSeekBar.setOnSeekBarChangeListener(this);
        mRideRiderHeightTextView = (TextView) view
                .findViewById(R.id.advanced_filter_rides_rider_height_textview);
        mRideChildSwapSwitch = (Switch) view.findViewById(R.id.advanced_filter_ride_child_swap_switch);
        mRideChildSwapSwitch.setOnCheckedChangeListener(this);
        mRideSingleRiderLineSwitch = (Switch) view
                .findViewById(R.id.advanced_filter_ride_single_rider_line_switch);
        mRideSingleRiderLineSwitch.setOnCheckedChangeListener(this);
        mRideVenuesRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_ride_venues_relative_layout);
        mRideVenuesRelativeLayout.setOnClickListener(this);
        mRideVenuesSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_ride_venues_selected_textview);
        mRideTypeRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_ride_type_relative_layout);
        mRideTypeRelativeLayout.setOnClickListener(this);
        mRideTypeSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_ride_type_selected_textview);
        mRideParentalSupervisionSwitch = (Switch) view
                .findViewById(R.id.advanced_filter_ride_parental_supervision_switch);
        mRideParentalSupervisionSwitch.setOnCheckedChangeListener(this);
        mRideLifeJacketRequiredSwitch = (Switch) view
                .findViewById(R.id.advanced_filter_ride_life_jacket_required_switch);
        mRideLifeJacketRequiredSwitch.setOnCheckedChangeListener(this);
        mRideLifeJacketRequiredViewGroup = (ViewGroup) view
                .findViewById(R.id.advanced_filter_ride_life_jacket_required_relative_layout);
        // Setup show options
        mShowFilterContentsLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_show_filter_contents_linear_layout);
        mShowFilterRootLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_shows_root_linear_layout);
        mShowFilterSwitch = (Switch) view.findViewById(R.id.advanced_filter_show_filter_switch);
        mShowFilterSwitch.setOnCheckedChangeListener(this);
        mShowVenuesRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_show_venues_relative_layout);
        mShowVenuesRelativeLayout.setOnClickListener(this);
        mShowVenuesSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_show_venues_selected_textview);
        mShowTypeRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_show_type_relative_layout);
        mShowTypeRelativeLayout.setOnClickListener(this);
        mShowTypeSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_show_type_selected_textview);
        mShowNextShowSeekBar = (SeekBar) view.findViewById(R.id.advanced_filter_show_next_show_time_seekbar);
        mShowNextShowSeekBar.setOnSeekBarChangeListener(this);
        mShowNextShowTextView = (TextView) view
                .findViewById(R.id.advanced_filter_show_next_show_time_textview);
        mShowNextShowStep = getResources().getInteger(R.integer.advanced_filter_show_next_show_time_step);
        // Setup dining options
        mDiningFilterRootLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_dining_root_linear_layout);
        mDiningFilterContentsLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_dining_filter_contents_linear_layout);
        mDiningFilterSwitch = (Switch) view.findViewById(R.id.advanced_filter_dining_filter_switch);
        mDiningFilterSwitch.setOnCheckedChangeListener(this);
        mDiningVenuesRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_dining_venues_relative_layout);
        mDiningVenuesRelativeLayout.setOnClickListener(this);
        mDiningVenuesSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_dining_venues_selected_textview);
        mDiningTypeRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_dining_type_relative_layout);
        mDiningTypeRelativeLayout.setOnClickListener(this);
        mDiningTypeSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_dining_type_selected_textview);
        mDiningStartingPriceViewGroup = (ViewGroup) view
                .findViewById(R.id.advanced_filter_dining_starting_price_linear_layout);
        mDiningStartingPriceSeekBar = (SeekBar) view
                .findViewById(R.id.advanced_filter_dining_starting_price_seekbar);
        mDiningStartingPriceSeekBar.setOnSeekBarChangeListener(this);
        mDiningStartingPriceSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_dining_starting_price_selected_textview);
        mDiningMinStartingPrice = getResources().getInteger(
                R.integer.advanced_filter_dining_starting_price_min);
        mDiningPlanRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_dining_plan_relative_layout);
        mDiningPlanRelativeLayout.setOnClickListener(this);
        mDiningPlanSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_dining_plan_selected_textview);
        mDiningCharacterDiningSwitch = (Switch) view
                .findViewById(R.id.advanced_filter_dining_character_dining_switch);
        mDiningCharacterDiningSwitch.setOnCheckedChangeListener(this);
        mDiningVegetarianHealthySwitch = (Switch) view
                .findViewById(R.id.advanced_filter_dining_vegetarian_healthy_switch);
        mDiningVegetarianHealthySwitch.setOnCheckedChangeListener(this);
        mDiningAmexSwitch = (Switch) view.findViewById(R.id.advanced_filter_dining_amex_switch);
        mDiningAmexSwitch.setOnCheckedChangeListener(this);
        mDiningAmexViewGroup = (ViewGroup) view.findViewById(R.id.advanced_filter_dining_amex_viewgroup);

        mCocaColaFreestyleSwitch = (Switch) view.findViewById(R.id.advanced_filter_coca_cola_freestyle_switch);
        mCocaColaFreestyleSwitch.setOnCheckedChangeListener(this);
        cocaColaContianer = (ViewGroup) view.findViewById(R.id.cocaColaContainer);

        // Setup shopping options
        mShoppingFilterRootLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_shopping_root_linear_layout);
        mShoppingTypeRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_shopping_type_relative_layout);
        mShoppingTypeRelativeLayout.setOnClickListener(this);
        mShoppingTypeSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_shopping_type_selected_textview);

        //rental services options
        mRentalServicesFilterRootLinearLayout = (LinearLayout) view.findViewById(R.id.advanced_filter_rental_services_root_linear_layout);

        mShoppingFilterContentsLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_shopping_filter_contents_linear_layout);
        mShoppingFilterSwitch = (Switch) view.findViewById(R.id.advanced_filter_shopping_filter_switch);
        mShoppingFilterSwitch.setOnCheckedChangeListener(this);

        mRentalServicesFilterContentsLinearLayout = (LinearLayout) view.findViewById(R.id.advanced_filter_rental_services_filter_contents_linear_layout);
        //rental services filter switch
        mRentalServicesFilterSwitch = (Switch) view.findViewById(R.id.advanced_filter_rental_services_filter_switch);
        mRentalServicesFilterSwitch.setOnCheckedChangeListener(this);

        mShoppingVenuesRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_shopping_venues_relative_layout);
        mShoppingVenuesRelativeLayout.setOnClickListener(this);

        //rental service filter layout
        mRentalServicesVenuesRelativeLayout = (RelativeLayout)view.findViewById(R.id.advanced_filter_rental_services_venues_relative_layout);
        mRentalServicesVenuesRelativeLayout.setOnClickListener(this);

        mRentalServicesVenuesSelectedTextView = (TextView) view.findViewById(R.id.advanced_filter_rental_services_venues_selected_textview);

        mRentalTypeEcvSwitch = (Switch) view.findViewById(R.id.advanced_filter_rental_type_testing_switch);
        mRentalTypeEcvSwitch.setOnCheckedChangeListener(this);

        mRentalTypeStrollerSwitch = (Switch) view.findViewById(R.id.advanced_filter_rental_type_stroller_switch);
        mRentalTypeStrollerSwitch.setOnCheckedChangeListener(this);

        mRentalTypeWheelChairSwitch = (Switch) view.findViewById(R.id.advanced_filter_rental_type_wheel_chair_pickup_switch);
        mRentalTypeWheelChairSwitch.setOnCheckedChangeListener(this);

        if (null != mExploreType && mExploreType != ExploreType.RENTAL_SERVICES) {
            mRentalServicesFilterRootLinearLayout.setVisibility(View.GONE);
        }

        mShoppingVenuesSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_shopping_venues_selected_textview);
        mShoppingExpressPassViewGroup = (ViewGroup) view
                .findViewById(R.id.advanced_filter_shopping_express_pass_viewgroup);
        mShoppingExpressPassSwitch = (Switch) view
                .findViewById(R.id.advanced_filter_shopping_express_pass_switch);
        mShoppingExpressPassSwitch.setOnCheckedChangeListener(this);
        mShoppingAmexViewGroup = (ViewGroup) view.findViewById(R.id.advanced_filter_shopping_amex_viewgroup);
        mShoppingAmexSwitch = (Switch) view.findViewById(R.id.advanced_filter_shopping_amex_switch);
        mShoppingAmexSwitch.setOnCheckedChangeListener(this);
        mShoppingPackagePickupViewGroup = (ViewGroup) view.findViewById(R.id.advanced_filter_shopping_package_pickup_viewgroup);
        mShoppingPackagePickupSwitch = (Switch) view.findViewById(R.id.advanced_filter_shopping_package_pickup_switch);
        mShoppingPackagePickupSwitch.setOnCheckedChangeListener(this);
        // Setup entertainment options
        mEntertainmentFilterRootLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_entertainment_root_linear_layout);
        mEntertainmentFilterContentsLinearLayout = (LinearLayout) view
                .findViewById(R.id.advanced_filter_entertainment_filter_contents_linear_layout);
        mEntertainmentFilterSwitch = (Switch) view
                .findViewById(R.id.advanced_filter_entertainment_filter_switch);
        mEntertainmentFilterSwitch.setOnCheckedChangeListener(this);
        mEntertainmentTypeRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.advanced_filter_entertainment_type_relative_layout);
        mEntertainmentTypeRelativeLayout.setOnClickListener(this);
        mEntertainmentTypeSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_entertainment_type_selected_textview);
        mEntertainmentStartingPriceSeekBar = (SeekBar) view
                .findViewById(R.id.advanced_filter_entertainment_starting_price_seekbar);
        mEntertainmentStartingPriceSeekBar.setOnSeekBarChangeListener(this);
        mEntertainmentStartingPriceSelectedTextView = (TextView) view
                .findViewById(R.id.advanced_filter_entertainment_starting_price_selected_textview);
        mEntertainmentMinStartingPrice = getResources().getInteger(
                R.integer.advanced_filter_entertainment_starting_price_min);


        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        inflater.inflate(R.menu.action_advanced_filter, menu);
        TintUtils.tintAllMenuItems(menu, getContext());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem )
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case R.id.action_advanced_filter_apply:
                Intent intent = new Intent();
                Bundle data = new Bundle();
                data.putString(AdvancedFilterActivity.KEY_ARG_RESULT_FILTER_OPTIONS, mFilterOptions.toJson());
                intent.putExtras(data);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                return true;
            case R.id.action_advanced_filter_reset:
                resetAll();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onClick");
        }

        switch (v.getId()) {
            case R.id.advanced_filter_sort_type:
                mFilterOptions.setFilterSort(FilterSort.TYPE);
                break;
            case R.id.advanced_filter_sort_alphabetical:
                mFilterOptions.setFilterSort(FilterSort.ALPHABETICAL);
                break;
            case R.id.advanced_filter_sort_distance:
                mFilterOptions.setFilterSort(FilterSort.DISTANCE);
                break;
            case R.id.advanced_filter_sort_wait_times:
                mFilterOptions.setFilterSort(FilterSort.WAIT_TIMES);
                break;
            case R.id.advanced_filter_sort_show_times:
                mFilterOptions.setFilterSort(FilterSort.SHOW_TIMES);
                break;
            case R.id.advanced_filter_accessibility_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_accessibility_options_array),
                        MultiChoiceOptionsType.ACCESSIBILITY).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_ride_venues_relative_layout:
                MultiChoiceFilterDialogFragment
                        .newInstance(mFilterOptions, MultiChoiceOptionsType.RIDE_VENUE).show(
                                getFragmentManager(), MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_ride_type_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_ride_sub_types),
                        MultiChoiceOptionsType.RIDE_TYPE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_show_venues_relative_layout:
                MultiChoiceFilterDialogFragment
                        .newInstance(mFilterOptions, MultiChoiceOptionsType.SHOW_VENUE).show(
                                getFragmentManager(), MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_show_type_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_show_sub_types),
                        MultiChoiceOptionsType.SHOW_TYPE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_dining_venues_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        MultiChoiceOptionsType.DINING_VENUE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_dining_type_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_dining_sub_types),
                        MultiChoiceOptionsType.DINING_TYPE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_dining_plan_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_dining_plans),
                        MultiChoiceOptionsType.DINING_PLAN).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_shopping_venues_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        MultiChoiceOptionsType.SHOPPING_VENUE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_shopping_type_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_shopping_sub_types),
                        MultiChoiceOptionsType.SHOPPING_TYPE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_rental_services_venues_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        MultiChoiceOptionsType.RENTAL_SERVICES_VENUE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            case R.id.advanced_filter_entertainment_type_relative_layout:
                MultiChoiceFilterDialogFragment.newInstance(mFilterOptions,
                        getResources().getStringArray(R.array.advanced_filter_entertainment_sub_types),
                        MultiChoiceOptionsType.ENTERTAINMENT_TYPE).show(getFragmentManager(),
                        MultiChoiceFilterDialogFragment.class.getSimpleName());
                break;
            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "onClick: unknown view clicked");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCheckedChanged");
        }

        switch (buttonView.getId()) {
            case R.id.advanced_filter_upper_lot_switch:
                mFilterOptions.setUpperLot(isChecked);
                break;
            case R.id.advanced_filter_lower_lot_switch:
                mFilterOptions.setLowerLot(isChecked);
                break;
            case R.id.advanced_filter_express_line_switch:
                mFilterOptions.setExpressPassLine(isChecked);
                break;
            case R.id.advanced_filter_kid_friendly_switch:
                mFilterOptions.setKidFriendly(isChecked);
                break;
            case R.id.advanced_filter_ride_filter_switch:
                toggleFilterTab(isChecked, PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
                updateRideOptions();
                break;
            case R.id.advanced_filter_ride_child_swap_switch:
                mFilterOptions.setChildSwap(isChecked);
                break;
            case R.id.advanced_filter_ride_single_rider_line_switch:
                mFilterOptions.setSingleRiderLine(isChecked);
                break;
            case R.id.advanced_filter_show_filter_switch:
                toggleFilterTab(isChecked, PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
                updateShowOptions();
                break;
            case R.id.advanced_filter_dining_filter_switch:
                toggleFilterTab(isChecked, PointsOfInterestTable.VAL_POI_TYPE_ID_DINING,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
                updateDiningOptions();
                break;
            case R.id.advanced_filter_dining_character_dining_switch:
                mFilterOptions.setCharacterDining(isChecked);
                break;
            case R.id.advanced_filter_dining_vegetarian_healthy_switch:
                mFilterOptions.setVegetarianHealthy(isChecked);
                break;
            case R.id.advanced_filter_shopping_filter_switch:
                toggleFilterTab(isChecked, PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
                updateShoppingOptions();
                break;
            case R.id.advanced_filter_rental_services_filter_switch:
                toggleFilterTab(isChecked, PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
                updateRentalServiceOptions();
                break;
            case R.id.advanced_filter_shopping_express_pass_switch:
                mFilterOptions.setExpressPassSold(isChecked);
                break;
            case R.id.advanced_filter_rental_type_testing_switch:
                mFilterOptions.setRentalTypeEcv(isChecked);
                break;
            case R.id.advanced_filter_entertainment_filter_switch:
                toggleFilterTab(isChecked, PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT,
                        PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
                updateEntertainmentOptions();
                break;
            case R.id.advanced_filter_ride_parental_supervision_switch:
                mFilterOptions.setParentalSupervisionRequired(isChecked);
                break;
            case R.id.advanced_filter_ride_life_jacket_required_switch:
                mFilterOptions.setLifeJacketRequired(isChecked);
                break;
            case R.id.advanced_filter_shopping_amex_switch:
                mFilterOptions.setAmexShopping(isChecked);
                break;
            case R.id.advanced_filter_rental_type_stroller_switch:
                mFilterOptions.setRentalTypeStroller(isChecked);
                break;
            case R.id.advanced_filter_dining_amex_switch:
                mFilterOptions.setAmexDining(isChecked);
                break;
            case R.id.advanced_filter_coca_cola_freestyle_switch:
                mFilterOptions.hasCocaCola = isChecked;
                setDiningSubTypes(FilterOptions.getDiningSubTypeFlags());
                updateDiningOptions();
                break;
            case R.id.advanced_filter_shopping_package_pickup_switch:
                mFilterOptions.setShopPackagePickup(isChecked);
                break;

            case R.id.advanced_filter_rental_type_wheel_chair_pickup_switch:
                mFilterOptions.setRentalTypeWheelChair(isChecked);
                break;

            default:
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "onCheckedChanged: unknown view checked");
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onProgressChanged");
        }
        switch (seekBar.getId()) {
            case R.id.advanced_filter_ride_max_wait_seekbar: {
                // Round seekbar value
                int value = (int) (Math.ceil(((double) progress) / mRideMaxWaitStep) * mRideMaxWaitStep);
                switch (progress) {
                    case 0:
                        mRidesMaxWaitTextView.setText(R.string.advanced_filter_option_off);
                        break;
                    default:
                        mRidesMaxWaitTextView.setText(getString(
                                R.string.advanced_filter_ride_max_wait_format, value));
                        break;
                }
                mFilterOptions.setRideFilterMaxWaitTime(value);
                break;
            }
            case R.id.advanced_filter_ride_rider_height_seekbar: {
                // Format rider height
                int value = progress + mRideMinRiderHeight;
                String riderHeight = formatRiderHeight(value, false);
                if (progress <= 0) {
                    mRideRiderHeightTextView.setText(R.string.advanced_filter_option_off);
                    mFilterOptions.setRiderHeight(0);
                } else {
                    mRideRiderHeightTextView.setText(riderHeight);
                    mFilterOptions.setRiderHeight(value);
                }
                break;
            }
            case R.id.advanced_filter_show_next_show_time_seekbar: {
                // Round seekbar value
                int value = (int) (Math.ceil(((double) progress) / mShowNextShowStep) * mShowNextShowStep);
                switch (value) {
                    case 0:
                        mShowNextShowTextView.setText(R.string.advanced_filter_option_off);
                        break;
                    default:
                        mShowNextShowTextView.setText(getString(
                                R.string.advanced_filter_show_next_show_times_format, value));
                        break;
                }
                mFilterOptions.setNextShowTime(value);
                break;
            }
            case R.id.advanced_filter_dining_starting_price_seekbar: {
                int value = progress + mDiningMinStartingPrice;
                if (progress <= 0) {
                    mDiningStartingPriceSelectedTextView.setText(R.string.advanced_filter_option_off);
                    mFilterOptions.setDiningMinPrice(0);
                } else {
                    mDiningStartingPriceSelectedTextView.setText(getString(
                            R.string.advanced_filter_dining_starting_price_format, value));
                    mFilterOptions.setDiningMinPrice(value);
                }
                break;
            }
            case R.id.advanced_filter_entertainment_starting_price_seekbar: {
                int value = progress + mEntertainmentMinStartingPrice;
                if (progress <= 0) {
                    mEntertainmentStartingPriceSelectedTextView.setText(R.string.advanced_filter_option_off);
                    mFilterOptions.setEntertainmentMinPrice(0);
                } else {
                    mEntertainmentStartingPriceSelectedTextView.setText(getString(
                            R.string.advanced_filter_entertainment_starting_price_format, value));
                    mFilterOptions.setEntertainmentMinPrice(value);
                }
                break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    /**
     * Update display based on filter options
     */
    private void updateViews() {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "updateViews");
        }

        updateSortOptions();
        updateOtherOptions();
        updateRideOptions();
        updateShowOptions();
        updateDiningOptions();
        updateShoppingOptions();
        updateRentalServiceOptions();
        updateEntertainmentOptions();
    }

    /**
     * Update sort options based on filter and ExploreType
     */
    private void updateSortOptions() {

        // A to Z and Distance sorts enabled by default in fragment layout
        switch (mFilterOptions.getExploreType()) {
            case CITY_WALK_ORLANDO:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case DINING:
            case ISLANDS_OF_ADVENTURE:
            case WET_N_WILD:
            case CITY_WALK_HOLLYWOOD:
                mSortTypeRadioButton.setVisibility(View.VISIBLE);
                break;
            case WAIT_TIMES:
            case RIDES:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
                mSortTypeRadioButton.setVisibility(View.VISIBLE);
                mSortWaitTimesRadioButton.setVisibility(View.VISIBLE);
                break;
            case SHOPPING:
                // Only the defaults needed
                break;
            case RENTAL_SERVICES:
                //
                break;
            case SHOWS:
                mSortTypeRadioButton.setVisibility(View.VISIBLE);
                mSortShowTimesRadioButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }

        // Disable distance if not in park
        if (mFilterOptions.getUserLocation() == null) {
            mSortDistanceRadioButton.setEnabled(false);
            mSortDescriptionTextView.setVisibility(View.VISIBLE);
        } else {
            mSortDistanceRadioButton.setEnabled(true);
            mSortDescriptionTextView.setVisibility(View.GONE);
        }

        // Update selected sort
        switch (mFilterOptions.getFilterSort()) {
            case ALPHABETICAL:
                mSortAlphabeticalRadioButton.setChecked(true);
                break;
            case DISTANCE:
                mSortDistanceRadioButton.setChecked(true);
                break;
            case TYPE:
                mSortTypeRadioButton.setChecked(true);
                break;
            case WAIT_TIMES:
                mSortWaitTimesRadioButton.setChecked(true);
                break;
            case SHOW_TIMES:
                mSortShowTimesRadioButton.setChecked(true);
                break;
            default:
                break;
        }
    }

    /**
     * Update accessibility, kid-friendly, and express pass line options
     */
    private void updateOtherOptions() {

        switch (mFilterOptions.getExploreType()) {
            case RIDES:
            case WAIT_TIMES:
            case SHOWS:
            case ISLANDS_OF_ADVENTURE:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case CITY_WALK_ORLANDO:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
            case CITY_WALK_HOLLYWOOD:
                mOtherRootLinearLayout.setVisibility(View.VISIBLE);
                break;
            default:
                mOtherRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Show upper/lower lot switches for Universal Studios Hollywood
        boolean showUpperLowerLotFilters = false;
        if (BuildConfigUtils.isLocationFlavorHollywood()
            && (mFilterOptions.getExploreType() == ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS
            || mFilterOptions.getExploreType() == ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_DINING
            || mFilterOptions.getExploreType() == ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING)) {
            showUpperLowerLotFilters = true;
        }
        mUpperLotRelativeLayout.setVisibility(showUpperLowerLotFilters ? View.VISIBLE : View.GONE);
        mLowerLotRelativeLayout.setVisibility(showUpperLowerLotFilters ? View.VISIBLE : View.GONE);
        mUpperLotSwitch.setChecked(mFilterOptions.isUpperLot());
        mLowerLotSwitch.setChecked(mFilterOptions.isLowerLot());

        mKidFriendlySwitch.setChecked(mFilterOptions.isKidFriendly());
        mExpressLineSwitch.setChecked(mFilterOptions.isExpressPassLine());

        if (!mFilterOptions.getAccessibilityOptions().isEmpty()) {
            List<String> accessibilityFlags = FilterOptions.getAccessibilityFlags();
            if (mFilterOptions.getAccessibilityOptions().size() == accessibilityFlags.size()) {
                mAccessibilitySelectedTextView.setText(R.string.advanced_filter_option_all);
            } else {
                if(mFilterOptions.getAccessibilityOptions().size()==1){
                    mAccessibilitySelectedTextView.setText(FilterOptions.getPropNameForAccessibilityFlag(mFilterOptions.getAccessibilityOptions().get(0)));
                    mAccessibilitySelectedTextView.setTextColor(getResources().getColor(R.color.text_gray_light));
                }else {
                    mAccessibilitySelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                            mFilterOptions.getAccessibilityOptions().size()));
                    mAccessibilitySelectedTextView.setTextColor(getResources().getColor(R.color.text_primary));
                }
            }
        } else {
            mAccessibilitySelectedTextView.setTextColor(getResources().getColor(R.color.text_primary));
            mAccessibilitySelectedTextView.setText(R.string.advanced_filter_option_off);
        }
    }

    private void updateRideOptions() {
        switch (mFilterOptions.getExploreType()) {
            case RIDES:
            case WAIT_TIMES:
                cocaColaContianer.setVisibility(View.GONE);
                break;
            case ISLANDS_OF_ADVENTURE:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case WET_N_WILD:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
                // Hide ride venues since a venue was chosen
                mRideVenuesRelativeLayout.setVisibility(View.GONE);
                cocaColaContianer.setVisibility(View.VISIBLE);
                break;
            default:
                // Hide the all ride filters since this explore type is not
                // listed above
                mRideFilterRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Update ride contents based on the state of the ride filter switch
        mRideFilterContentsLinearLayout.setVisibility(mRideFilterSwitch.isChecked() ? View.VISIBLE
                : View.GONE);

        // Update venues filter, select all when none are selected
        if (!mFilterOptions.getRideFilterVenues().isEmpty()) {

            if(mFilterOptions.getRideFilterVenues().size()==1){

                mRideVenuesSelectedTextView.setText(FilterOptions.getPropNameForVenueFlag(mFilterOptions.getRideFilterVenues().get(0)));
                mRideVenuesSelectedTextView.setTextColor(getResources().getColor(R.color.text_gray_light));
            }else {
                mRideVenuesSelectedTextView.setTextColor(getResources().getColor(R.color.text_primary));
                mRideVenuesSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        mFilterOptions.getRideFilterVenues().size()));
            }
        } else {
            mRideVenuesSelectedTextView.setTextColor(getResources().getColor(R.color.text_primary));
            mRideVenuesSelectedTextView.setText(R.string.advanced_filter_option_all);
        }

        // Get selected ride sub-types from tabs when ride or wait times explore
        // type selected
        if (mFilterOptions.getExploreType() == ExploreType.WAIT_TIMES
                || mFilterOptions.getExploreType() == ExploreType.RIDES) {
            mFilterOptions.setRideSubTypes(getSubTypes(mFilterOptions.getFilterTabOptions()));
        }

        // Update the number of selected ride sub-types
        if (!mFilterOptions.getRideSubTypes().isEmpty()) {
            int checkedCount = mFilterOptions.getRideSubTypes().size();
            String[] rideSubTypes = getResources().getStringArray(R.array.advanced_filter_ride_sub_types);
            if (checkedCount == rideSubTypes.length) {
                mRideTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else if (checkedCount == 0) {
                mRideTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else {
                mRideTypeSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        checkedCount));
            }
        } else {
            // venues default to having no subtypes, selected but all should be
            // considered selected, ride and wait times are based on tabs
            switch (mFilterOptions.getExploreType()) {
                case RIDES:
                case WAIT_TIMES:
                    mRideTypeSelectedTextView.setText(R.string.advanced_filter_option_off);
                    break;
                default:
                    mRideTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
                    break;
            }
        }

        mRideMaxWaitSeekBar.setProgress(mFilterOptions.getRideFilterMaxWaitTime());
        mRideChildSwapSwitch.setChecked(mFilterOptions.isChildSwap());
        mRideSingleRiderLineSwitch.setChecked(mFilterOptions.isSingleRiderLine());
        mRideParentalSupervisionSwitch.setChecked(mFilterOptions.isParentalSupervisionRequired());
        mRideLifeJacketRequiredSwitch.setChecked(mFilterOptions.isLifeJacketRequired());
        updateFilterSwitch(mRideFilterSwitch, PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        // Update rider height slider, offset by min height as it does not begin
        // at zero
        if (mFilterOptions.getRiderHeight() > 0) {
            mRideRiderHeightSeekBar.setProgress(mFilterOptions.getRiderHeight() - mRideMinRiderHeight);
        } else {
            mRideRiderHeightSeekBar.setProgress(0);
        }

        // Hide life jacket option for Hollywood (since it doesn't exist there)
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            mRideLifeJacketRequiredViewGroup.setVisibility(View.GONE);
        }

    }

    private void updateShowOptions() {
        switch (mFilterOptions.getExploreType()) {
            case SHOWS:
                break;
            case CITY_WALK_ORLANDO:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case ISLANDS_OF_ADVENTURE:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
                // Hide show venues since a venue was chosen
                mShowVenuesRelativeLayout.setVisibility(View.GONE);
                break;
            default:
                mShowFilterRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Update show filter switch based on it's filter tab
        updateFilterSwitch(mShowFilterSwitch, PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        // Update show contents based on the state of the show filter switch
        mShowFilterContentsLinearLayout.setVisibility(mShowFilterSwitch.isChecked() ? View.VISIBLE
                : View.GONE);

        // Update venues filter, select all when none are selected
        if (!mFilterOptions.getShowFilterVenues().isEmpty()) {
            mShowVenuesSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                    mFilterOptions.getShowFilterVenues().size()));
        } else {
            mShowVenuesSelectedTextView.setText(R.string.advanced_filter_option_all);
        }

        // Get selected show sub-types from tabs when shows explore type
        // selected
        if (mFilterOptions.getExploreType() == ExploreType.SHOWS) {
            mFilterOptions.setShowSubTypes(getSubTypes(mFilterOptions.getFilterTabOptions()));
        }

        // Update the number of selected show sub-types
        if (!mFilterOptions.getShowSubTypes().isEmpty()) {
            int checkedCount = mFilterOptions.getShowSubTypes().size();
            String[] showSubTypes = getResources().getStringArray(R.array.advanced_filter_show_sub_types);
            if (checkedCount == showSubTypes.length) {
                mShowTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else if (checkedCount == 0) {
                mShowTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            }else if(checkedCount == 1){
                mShowTypeSelectedTextView.setText(mFilterOptions.getShowSubTypes().get(0));
              //  mShowTypeSelectedTextView.setText(FilterOptions.getPropNameForAccessibilityFlag(mFilterOptions.getRideSubTypes().get(0)));
                mShowTypeSelectedTextView.setTextColor(getResources().getColor(R.color.text_gray_light));
            } else {
                mRideTypeSelectedTextView.setTextColor(getResources().getColor(R.color.text_primary));
                mRideTypeSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        checkedCount));


                mShowTypeSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        checkedCount));
            }
        } else {
            // venues default to having no subtypes, selected but all should be
            // considered selected, shows are based on tabs
            switch (mFilterOptions.getExploreType()) {
                case SHOWS:
                    mShowTypeSelectedTextView.setText(R.string.advanced_filter_option_off);
                    break;
                default:
                    mShowTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
                    break;
            }
        }
        // Update next show time seekbar
        mShowNextShowSeekBar.setProgress(mFilterOptions.getNextShowTime());

    }

    private void updateDiningOptions() {
        switch (mFilterOptions.getExploreType()) {
            case DINING:
                break;
            case ISLANDS_OF_ADVENTURE:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case CITY_WALK_ORLANDO:
            case WET_N_WILD:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
            case CITY_WALK_HOLLYWOOD:
                // Hide show venues since a venue was chosen
                mDiningVenuesRelativeLayout.setVisibility(View.GONE);
                break;
            default:
                mDiningFilterRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Update dining filter switch based on it's filter tab
        updateFilterSwitch(mDiningFilterSwitch, PointsOfInterestTable.VAL_POI_TYPE_ID_DINING,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        // Update dining contents based on the state of the dining filter switch
        mDiningFilterContentsLinearLayout.setVisibility(mDiningFilterSwitch.isChecked() ? View.VISIBLE
                : View.GONE);

        // Update venues filter, select all when none are selected
        if (!mFilterOptions.getDiningFilterVenues().isEmpty()) {
            mDiningVenuesSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                    mFilterOptions.getDiningFilterVenues().size()));
        } else {
            mDiningVenuesSelectedTextView.setText(R.string.advanced_filter_option_all);
        }

        // Get selected dining sub-types from tabs when dining explore type
        // selected
        if (mFilterOptions.getExploreType() == ExploreType.DINING) {
            mFilterOptions.setDiningSubTypes(getSubTypes(mFilterOptions.getFilterTabOptions()));
        }

        // Update the number of selected dining sub-types
        if (!mFilterOptions.getDiningSubTypes().isEmpty()) {
            int checkedCount = mFilterOptions.getDiningSubTypes().size();
            String[] diningSubTypes = getResources().getStringArray(R.array.advanced_filter_dining_sub_types);
            if (checkedCount == diningSubTypes.length) {
                mDiningTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else if (checkedCount == 0) {
                mDiningTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else {
                mDiningTypeSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        checkedCount));
            }
        } else {
            // venues default to having no subtypes, selected but all should be
            // considered selected, dining is based on tabs
            switch (mFilterOptions.getExploreType()) {
                case DINING:
                    mDiningTypeSelectedTextView.setText(R.string.advanced_filter_option_off);
                    break;
                default:
                    mDiningTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
                    break;
            }
        }

        // Update starting price slider
        if (mFilterOptions.getDiningMinPrice() > 0) {
            mDiningStartingPriceSeekBar.setProgress(mFilterOptions.getDiningMinPrice()
                    - mDiningMinStartingPrice);
        } else {
            mDiningStartingPriceSeekBar.setProgress(0);
        }

        // Update dining plans
        if (!mFilterOptions.getDiningPlans().isEmpty()) {
            List<String> diningPlanConstants = FilterOptions.getDiningPlanFlags();
            if (mFilterOptions.getDiningPlans().size() == diningPlanConstants.size()) {
                mDiningPlanSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else {
                mDiningPlanSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        mFilterOptions.getDiningPlans().size()));
            }
        } else {
            mDiningPlanSelectedTextView.setText(R.string.advanced_filter_option_off);
        }

        mDiningCharacterDiningSwitch.setChecked(mFilterOptions.isCharacterDining());
        mDiningVegetarianHealthySwitch.setChecked(mFilterOptions.isVegetarianHealthy());
        mDiningAmexSwitch.setChecked(mFilterOptions.isAmexDining());
    }

    private void updateShoppingOptions() {
        switch (mFilterOptions.getExploreType()) {
            case SHOPPING:
                break;
            case ISLANDS_OF_ADVENTURE:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case CITY_WALK_ORLANDO:
            case WET_N_WILD:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
            case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
            case CITY_WALK_HOLLYWOOD:
                // Hide show venues since a venue was chosen
                mShoppingVenuesRelativeLayout.setVisibility(View.GONE);
                break;
            default:
                mShoppingFilterRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Update shopping filter switch based on it's filter tab
        updateFilterSwitch(mShoppingFilterSwitch, PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        // Update shopping contents based on the state of the shopping filter
        // switch
        mShoppingFilterContentsLinearLayout.setVisibility(mShoppingFilterSwitch.isChecked() ? View.VISIBLE
                : View.GONE);

        // Update venues filter, select all when none are selected
        if (!mFilterOptions.getShoppingFilterVenues().isEmpty()) {
            mShoppingVenuesSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                    mFilterOptions.getShoppingFilterVenues().size()));
        } else {
            mShoppingVenuesSelectedTextView.setText(R.string.advanced_filter_option_all);
        }

        // Get selected shopping sub-types from tabs when shopping explore type
        // selected
        if (mFilterOptions.getExploreType() == ExploreType.SHOPPING) {
            mFilterOptions.setShoppingSubTypes(getSubTypes(mFilterOptions.getFilterTabOptions()));
        }

        // Update the number of selected shopping sub-types
        if (!mFilterOptions.getShoppingSubTypes().isEmpty()) {
            int checkedCount = mFilterOptions.getShoppingSubTypes().size();
            String[] shoppingSubTypes = getResources().getStringArray(R.array.advanced_filter_shopping_sub_types);
            if (checkedCount == shoppingSubTypes.length) {
                mShoppingTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else if (checkedCount == 0) {
                mShoppingTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else {
                mShoppingTypeSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        checkedCount));
            }
        } else {
            // Venues default to having no subtypes, selected but all should be
            // considered selected, shopping is based on tabs
            switch (mFilterOptions.getExploreType()) {
                case SHOPPING:
                    mShoppingTypeSelectedTextView.setText(R.string.advanced_filter_option_off);
                    break;
                default:
                    mShoppingTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
                    break;
            }
        }

        mShoppingExpressPassSwitch.setChecked(mFilterOptions.isExpressPassSold());
        mShoppingAmexSwitch.setChecked(mFilterOptions.isAmexShopping());
        mShoppingPackagePickupSwitch.setChecked(mFilterOptions.isShopPackagePickup());

        // Orlando does not have shopping sub types
        if (BuildConfigUtils.isLocationFlavorOrlando()) {
            mShoppingTypeRelativeLayout.setVisibility(View.GONE);
        }

        // Hollywood only
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            // Business wants to hide express pass and not advertise it
            mShoppingExpressPassViewGroup.setVisibility(View.GONE);

            // Does not support AMEX savings
            mShoppingAmexViewGroup.setVisibility(View.GONE);

            // Does not support package pickup
            mShoppingPackagePickupViewGroup.setVisibility(View.GONE);
        }
    }


    private void updateRentalServiceOptions() {
        switch (mFilterOptions.getExploreType()) {
            case RENTAL_SERVICES:
                break;
            case ISLANDS_OF_ADVENTURE:
            case UNIVERSAL_STUDIOS_FLORIDA:
            case CITY_WALK_ORLANDO:
            case WET_N_WILD:
                // Hide show venues since a venue was chosen
                mRentalServicesVenuesRelativeLayout.setVisibility(View.GONE);
                break;
            default:
                mRentalServicesFilterRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Update Rental Services filter switch based on it's filter tab
        updateFilterSwitch(mRentalServicesFilterSwitch, PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        // Update Rental Services contents based on the state of the Rental Services filter
        // switch
        mRentalServicesFilterContentsLinearLayout.setVisibility(mRentalServicesFilterSwitch.isChecked() ? View.VISIBLE
                : View.GONE);

        // Update venues filter, select all when none are selected
        if (!mFilterOptions.getRentalServicesFilterVenues().isEmpty()) {
            mRentalServicesVenuesSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                    mFilterOptions.getRentalServicesFilterVenues().size()));
        } else {
            mRentalServicesVenuesSelectedTextView.setText(R.string.advanced_filter_option_all);
        }

        mRentalTypeEcvSwitch.setChecked(mFilterOptions.isRentalTypeEcv());
        mRentalTypeStrollerSwitch.setChecked(mFilterOptions.isRentalTypeStroller());
        mRentalTypeWheelChairSwitch.setChecked(mFilterOptions.isRentalTypeWheelChair());
    }

    private void updateEntertainmentOptions() {
        switch (mFilterOptions.getExploreType()) {
            case CITY_WALK_ORLANDO:
            case CITY_WALK_HOLLYWOOD:
                // Entertainment filter only available for city walk
                break;
            default:
                mEntertainmentFilterRootLinearLayout.setVisibility(View.GONE);
                break;
        }

        // Update entertainment filter switch based on it's filter tab
        updateFilterSwitch(mEntertainmentFilterSwitch, PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT,
                PointsOfInterestTable.COL_FULL_POI_TYPE_ID);
        // Update entertainment contents based on the state of the entertainment
        // filter switch
        mEntertainmentFilterContentsLinearLayout
                .setVisibility(mEntertainmentFilterSwitch.isChecked() ? View.VISIBLE : View.GONE);

        // Update the number of selected entertainment sub-types
        if (!mFilterOptions.getEntertainmentSubTypes().isEmpty()) {
            int checkedCount = mFilterOptions.getEntertainmentSubTypes().size();
            String[] entertainmentSubTypes = getResources().getStringArray(R.array.advanced_filter_entertainment_sub_types);
            if (checkedCount == entertainmentSubTypes.length) {
                mEntertainmentTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else if (checkedCount == 0) {
                mEntertainmentTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
            } else {
                mEntertainmentTypeSelectedTextView.setText(getString(R.string.advanced_filter_selected_format,
                        checkedCount));
            }
        } else {
            // Venues default to having no subtypes, selected but all should be
            // considered selected, entertainment is based on tabs
            switch (mFilterOptions.getExploreType()) {
                default:
                    mEntertainmentTypeSelectedTextView.setText(R.string.advanced_filter_option_all);
                    break;
            }
        }

        // Update starting price slider
        if (mFilterOptions.getEntertainmentMinPrice() > 0) {
            mEntertainmentStartingPriceSeekBar.setProgress(mFilterOptions.getEntertainmentMinPrice()
                    - mEntertainmentMinStartingPrice);
        } else {
            mEntertainmentStartingPriceSeekBar.setProgress(0);
        }

        // Orlando does not have entertainment sub types
        if (BuildConfigUtils.isLocationFlavorOrlando()) {
            mEntertainmentTypeRelativeLayout.setVisibility(View.GONE);
        }
    }

    public void setAccessibiltyOptions(List<String> accessibilityOptions) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setAccessibiltyOptions");
        }
        mFilterOptions.setAccessibilityOptions(accessibilityOptions);
        updateOtherOptions();
    }

    public void setRideFilterVenues(List<String> rideFilterVenues) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setRideFilterVenues");
        }
        mFilterOptions.setRideFilterVenues(rideFilterVenues);
        updateRideOptions();
    }

    public void setShowFilterVenues(List<String> showFilterVenues) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setShowFilterVenues");
        }
        mFilterOptions.setShowFilterVenues(showFilterVenues);
        updateShowOptions();
    }

    public void setShowSubTypes(List<String> showSubTypes) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setShowSubTypes");
        }
        mFilterOptions.setShowSubTypes(showSubTypes);
        switch (mFilterOptions.getExploreType()) {
        // Tabs will only match show sub-types for shows,
        // otherwise do not update filter tabs
            case SHOWS:
                // If all show sub-types were unchecked select them all
                if (showSubTypes.isEmpty()) {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), true);
                } else {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), false);
                    for (String showSubType : showSubTypes) {
                        toggleFilterTab(true, Integer.valueOf(showSubType),
                                PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS);
                    }
                }
                break;
            default:
                break;
        }

        updateShowOptions();
    }

    public void setRideSubTypes(List<String> rideSubTypes) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setRideSubTypes");
        }
        mFilterOptions.setRideSubTypes(rideSubTypes);
        switch (mFilterOptions.getExploreType()) {
        // Tabs will only match ride sub-types for rides and wait times,
        // otherwise do not update filter tabs
            case RIDES:
            case WAIT_TIMES:
                // If all ride sub-types were unchecked select them all
                if (rideSubTypes.isEmpty()) {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), true);
                } else {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), false);
                    for (String rideSubType : rideSubTypes) {
                        toggleFilterTab(true, Integer.valueOf(rideSubType),
                                PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS);
                    }
                }
                break;
            default:
                break;
        }

        updateRideOptions();
    }

    public void setDiningFilterVenues(List<String> diningFilterVenues) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setDiningFilterVenues");
        }
        mFilterOptions.setDiningFilterVenues(diningFilterVenues);
        updateDiningOptions();
    }

    public void setDiningSubTypes(List<String> diningSubTypes) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setDiningSubTypes");
        }
        mFilterOptions.setDiningSubTypes(diningSubTypes);
        switch (mFilterOptions.getExploreType()) {
        // Tabs will only match dining sub-types for dining,
        // otherwise do not update filter tabs
            case DINING:
                // If all dining sub-types were unchecked select them all
                if (diningSubTypes.isEmpty()) {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), true);
                } else {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), false);
                    for (String diningSubType : diningSubTypes) {
                        toggleFilterTab(true, Integer.valueOf(diningSubType),
                                PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS);
                    }
                }
                break;
            default:
                break;
        }

        updateDiningOptions();
    }

    public void setDiningPlans(List<String> diningPlans) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setDiningPlans");
        }
        mFilterOptions.setDiningPlans(diningPlans);
        updateDiningOptions();
    }

    public void setShoppingSubTypes(List<String> shoppingSubTypes) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setShoppingSubTypes");
        }
        mFilterOptions.setShoppingSubTypes(shoppingSubTypes);
        switch (mFilterOptions.getExploreType()) {
            // Tabs will only match shopping sub-types for shopping,
            // otherwise do not update filter tabs
            case SHOPPING:
                // If all shopping sub-types were unchecked select them all
                if (shoppingSubTypes.isEmpty()) {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), true);
                } else {
                    resetFilterTabOptions(mFilterOptions.getFilterTabOptions(), false);
                    for (String shoppingSubType : shoppingSubTypes) {
                        toggleFilterTab(true, Integer.valueOf(shoppingSubType),
                                PointsOfInterestTable.COL_FULL_SUB_TYPE_FLAGS);
                    }
                }
                break;
            default:
                break;
        }

        updateShoppingOptions();
    }

    public void setShoppingFilterVenues(List<String> shoppingFilterVenues) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setShoppingFilterVenues");
        }
        mFilterOptions.setShoppingFilterVenues(shoppingFilterVenues);
        updateShoppingOptions();
    }

    public void setEntertainmentSubTypes(List<String> entertainmentSubTypes) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setEntertainmentSubTypes");
        }
        mFilterOptions.setEntertainmentSubTypes(entertainmentSubTypes);
        switch (mFilterOptions.getExploreType()) {
            // Tabs will only match entertainment sub-types for entertainment,
            // otherwise do not update filter tabs
            // Explore by entertainment does not currently exist
            default:
                break;
        }

        updateEntertainmentOptions();
    }

    public void setRentalServicesFilterVenues(List<String> rentalServicesFilterVenues) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "setRentalServiceFilterVenues");
        }
        mFilterOptions.setRentalServicesFilterVenues(rentalServicesFilterVenues);
        updateRentalServiceOptions();
    }


    private List<String> getSubTypes(List<FilterTabOption> filterTabOptions) {
        List<String> subTypes = new ArrayList<String>();
        for (FilterTabOption filterTabOption : filterTabOptions) {
            if (filterTabOption.isChecked() && filterTabOption.getColumnValues().length > 0) {
                Collections.addAll(subTypes, filterTabOption.getColumnValues());
            }
        }

        return subTypes;
    }

    private void resetAll() {
        mFilterOptions = FilterOptions.resetFilter(mFilterOptions);
        updateViews();
    }

    /**
     * Update the associated FilterTabOption when a switch has been checked
     * 
     * @param isChecked
     * @param poiTypeId
     */
    private void toggleFilterTab(boolean isChecked, int poiTypeId, String columnName) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "toggleFilterTab");
        }

        if (mFilterOptions.getFilterTabOptions() != null && !mFilterOptions.getFilterTabOptions().isEmpty()) {
            for (FilterTabOption filterTabOption : mFilterOptions.getFilterTabOptions()) {
                List<String> columnValues = Arrays.asList(filterTabOption.getColumnValues());
                if (columnName.equals(filterTabOption.getColumnName())
                        && columnValues.contains(String.valueOf(poiTypeId))) {
                    filterTabOption.setChecked(isChecked);
                    return;
                }
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "toggleFilterTab: No matching filter tab found");
        }
    }

    /**
     * Set all filter tabs to passed in checked value
     * 
     * @param filterTabOptions
     */
    private static void resetFilterTabOptions(List<FilterTabOption> filterTabOptions, boolean isChecked) {
        if (filterTabOptions == null || filterTabOptions.isEmpty()) {
            return;
        }
        for (FilterTabOption filterTabOption : filterTabOptions) {
            filterTabOption.setChecked(isChecked);
        }
    }

    /**
     * Update the associated switch from the associated FilterTabOption
     * 
     * @param filterSwitch
     * @param poiTypeId
     */
    private void updateFilterSwitch(Switch filterSwitch, int poiTypeId, String columnName) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "updateFilterSwitch");
        }

        if (mFilterOptions.getFilterTabOptions() != null && !mFilterOptions.getFilterTabOptions().isEmpty()) {
            for (FilterTabOption filterTabOption : mFilterOptions.getFilterTabOptions()) {
                List<String> columns = Arrays.asList(filterTabOption.getColumnValues());
                if (columnName.equals(filterTabOption.getColumnName())
                        && columns.contains(String.valueOf(poiTypeId))) {
                    filterSwitch.setVisibility(View.VISIBLE);
                    filterSwitch.setChecked(filterTabOption.isChecked());
                    return;
                }
            }
        }
        // Hide switch if associated tab is not present
        filterSwitch.setVisibility(View.GONE);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "updateFilterSwitch: No matching filter tab found");
        }
    }

    private String formatRiderHeight(int riderHeight, boolean showFeet) {
        if (riderHeight > 0 && showFeet) {
            int feet = riderHeight / 12;
            int inches = riderHeight % 12;

            if (feet > 0) {
                return feet + "' " + inches + "\"";
            } else {
                return inches + "\"";
            }
        } else {
            return riderHeight + "\"";
        }
    }

}
