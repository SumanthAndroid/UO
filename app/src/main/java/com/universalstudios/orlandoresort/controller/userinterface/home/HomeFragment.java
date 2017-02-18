/**
 *
 */
package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.Manifest;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.accessibility.AccessibilityUtils;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerClickHandler;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerItem;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils.PagerDotColor;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.dialogs.NotificationDialog;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsRequestsListener;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 *
 * @author Steven Byle
 */
public class HomeFragment extends DatabaseQueryFragment implements ActionBarTitleProvider,
        OnPageChangeListener, PicassoProvider, OnClickListener, OnGlobalLayoutListener,
        OnTouchListener, OnUniversalAppStateChangeListener, PermissionsRequestsListener {
	private static final String TAG = HomeFragment.class.getSimpleName();

	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
	private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";
	private static final String KEY_STATE_HAS_USER_TOUCHED_PAGER = "KEY_STATE_HAS_USER_TOUCHED_PAGER";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID_FEATURED_NEWS_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT;
	private static final int LOADER_ID_FEATURED_POI_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_2;
	private static final int LOADER_ID_FEATURED_EVENT_SERIES_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_3;

	private static final long AUTO_SCROLL_PAGER_DELAY_IN_MS = 4 * 1000;
	private static final long CHOOSE_PARK_ANIM_DURATION_IN_MS = 600;

	private int mCurrentViewPagerTab;
	private int mCalculatedImageHeightDp;
	private boolean mIsChooseParkOpen;
	private boolean mHasUserTouchedPager;
	private RelativeLayout mViewPagerContainer;
	private JazzyViewPager mViewPager;
	private LinearLayout mPagerDotContainer;
	private ViewGroup mExploreMapParentContainer;
	private ViewGroup mExploreMapFragmentContainer;
	private View mExploreMapWrapperContainer;
	private ViewGroup mTabBarLayout;
	private View mTabBarShadow;
	private ViewGroup mChooseParkContentLayout;
	private ImageView mChooseParkCaret;
	private ImageView mExploreMapCircleClose;
	private ImageView mExploreMapCirclePin;
	private TextView mExploreMapExploreTheText;
	private TextView mExploreMapResortMapText;
	private ViewGroup mViewPagerAndTabBarMaskLayout;
	private View mTabBarItemRides;
	private TextView mTabBarItemRidesText;
	private View mTabBarItemShows;
	private View mTabBarItemDining;
	private View mTabBarItemBuyTicket;

	private ViewGroup mChooseParkContainer;
	private ViewGroup mChooseParkItemIoa;
	private ViewGroup mChooseParkItemUsf;
	private ViewGroup mChooseParkItemCw;
	private TextView mChooseParkIoaHoursText;
	private TextView mChooseParkUsfHoursText;
	private TextView mChooseParkCwHoursText;
	private FeaturedItemPagerAdapter mFeaturedItemPagerAdapter;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private Picasso mPicasso;
	private AnimatorSet mChooseParkAnimatorSet;
	private DrawerStateProvider mParentDrawerStateProvider;
	private DrawerClickHandler mParentDrawerClickHandler;
	private Handler mHandler;
	private ViewPagerAutoSwitchRunnable mViewPagerAutoSwitchRunnable;
	private ImageView mHeroImage;
	private List<Long> mValidNewsIdList;
	private List<Long> mValidPoiIdList;
	private List<Long> mValidEventSeriesIdList;
	private boolean mHasCheckedNewsIds;
	private boolean mHasCheckedPoiIds;
	private boolean mHasCheckedEventSeriesIds;

	public static HomeFragment newInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		HomeFragment fragment = new HomeFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(
				VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE,
				VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA,
				VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO);
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) activity;
		}
		// If neither implements the interface, log a warning
		else if (mParentDrawerStateProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
			}
		}

		// Check if parent fragment (if there is one) implements the interface
		if (parentFragment != null && parentFragment instanceof DrawerClickHandler) {
			mParentDrawerClickHandler = (DrawerClickHandler) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DrawerClickHandler) {
			mParentDrawerClickHandler = (DrawerClickHandler) activity;
		}
		// If neither implements the interface, log a warning
		else if (mParentDrawerClickHandler == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerClickHandler");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
					null, null,
					AnalyticsUtils.CONTENT_SUB_2_HOME,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);

			mHasUserTouchedPager = false;
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mHasUserTouchedPager = savedInstanceState.getBoolean(KEY_STATE_HAS_USER_TOUCHED_PAGER);
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
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Set the action bar title, if the drawer isn't open
		if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
			Activity parentActivity = getActivity();
			if (parentActivity != null) {
				parentActivity.getActionBar().setTitle(provideTitle());
			}
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

		// Setup Views
		mViewPagerContainer = (RelativeLayout) fragmentView.findViewById(R.id.fragment_home_viewpager_container);
		mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_home_viewpager);
		mHeroImage = (ImageView) fragmentView.findViewById(R.id.fragment_home_hero_image);
		mPagerDotContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_home_dot_layout);
		mExploreMapParentContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_explore_map_parent_container);
		mExploreMapFragmentContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_explore_map_container);
		mExploreMapWrapperContainer = fragmentView.findViewById(R.id.fragment_home_explore_map_wrapper_container);
		mTabBarLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_tab_bar);
		mTabBarShadow = fragmentView.findViewById(R.id.fragment_home_tab_bar_shadow);
		mChooseParkContentLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_map_choose_park_content_layout);
		mChooseParkCaret = (ImageView) fragmentView.findViewById(R.id.fragment_home_map_choose_park_caret);
		mExploreMapCircleClose = (ImageView) fragmentView.findViewById(R.id.fragment_home_explore_map_circle_close);
		mExploreMapCirclePin = (ImageView) fragmentView.findViewById(R.id.fragment_home_explore_map_circle_pin);
		mExploreMapExploreTheText = (TextView) fragmentView.findViewById(R.id.fragment_home_explore_map_explore_the_text);
		mExploreMapResortMapText = (TextView) fragmentView.findViewById(R.id.fragment_home_explore_map_resort_map_text);
		mViewPagerAndTabBarMaskLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_viewpager_and_tab_bar_mask);
		mTabBarItemRides = fragmentView.findViewById(R.id.fragment_home_tab_bar_rides);
		mTabBarItemRidesText = (TextView) fragmentView.findViewById(R.id.fragment_home_tab_bar_rides_text);
		mTabBarItemShows = fragmentView.findViewById(R.id.fragment_home_tab_bar_shows);
		mTabBarItemDining = fragmentView.findViewById(R.id.fragment_home_tab_bar_dining);
		mTabBarItemBuyTicket=fragmentView.findViewById(R.id.fragment_home_tab_bar_buy_ticket);
		mChooseParkContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_choose_park_container);
		mChooseParkItemIoa = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_choose_park_ioa);
		mChooseParkItemUsf = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_choose_park_usf);
		mChooseParkItemCw = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_choose_park_cw);
		mChooseParkIoaHoursText = (TextView) fragmentView.findViewById(R.id.fragment_home_choose_park_ioa_hours_text);
		mChooseParkUsfHoursText = (TextView) fragmentView.findViewById(R.id.fragment_home_choose_park_usf_hours_text);
		mChooseParkCwHoursText = (TextView) fragmentView.findViewById(R.id.fragment_home_choose_park_cw_hours_text);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mCurrentViewPagerTab = mViewPager.getCurrentItem();

			// Load the explore map fragment
			if (mExploreMapParentContainer != null && mExploreMapFragmentContainer != null && mExploreMapWrapperContainer != null) {

				mExploreMapWrapperContainer.setOnClickListener(this);
				ExploreMapFragment fragment = ExploreMapFragment.newInstance(null, ExploreType.HOME_PAGE_MAP_SNAPSHOT, null, null);
				FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
				fragmentTransaction.replace(mExploreMapFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

		}
		// Otherwise, restore state
		else {
			mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);

			if (mExploreMapParentContainer != null && mExploreMapFragmentContainer != null && mExploreMapWrapperContainer != null) {
				mExploreMapWrapperContainer.setOnClickListener(this);
			}
		}

		// Always assume the choose park menu is closed, since its state is not
		// restored
		mIsChooseParkOpen = false;
		mViewPagerAndTabBarMaskLayout.setVisibility(View.GONE);
		mViewPagerAndTabBarMaskLayout.setOnClickListener(this);
		mTabBarItemRides.setOnClickListener(this);
		mTabBarItemShows.setOnClickListener(this);
		mTabBarItemDining.setOnClickListener(this);
		mTabBarItemBuyTicket.setOnClickListener(this);
		mChooseParkItemIoa.setOnClickListener(this);
		mChooseParkItemUsf.setOnClickListener(this);
		mChooseParkItemCw.setOnClickListener(this);
		mChooseParkIoaHoursText.setVisibility(View.INVISIBLE);
		mChooseParkUsfHoursText.setVisibility(View.INVISIBLE);
		mChooseParkCwHoursText.setVisibility(View.INVISIBLE);

		// Set pager height based on detail image aspect ratio
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCalculatedImageHeightDp, displayMetrics));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, calculatedImageHeightPx);
		mViewPagerContainer.setLayoutParams(layoutParams);

		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(this);
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setFadeEnabled(false);

		mValidNewsIdList = new ArrayList<Long>();
		mValidPoiIdList = new ArrayList<Long>();
		mValidEventSeriesIdList = new ArrayList<Long>();
		mHasCheckedNewsIds = false;
		mHasCheckedPoiIds = false;
		mHasCheckedEventSeriesIds = false;

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(getActivity())
				.debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
				.downloader(mUniversalOrlandoImageDownloader).build();

		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Listen for the layout of the fragment, so the map container height
		// can be fixed
		view.getViewTreeObserver().addOnGlobalLayoutListener(this);

        PermissionsManager.getInstance().handlePermissionRequest(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this);
	}

    @Override
    public void onPermissionsUpdated(List<String> accepted, List<String> denied) {
        if (!denied.isEmpty()) {
            NotificationDialog.newInstance(getString(R.string.gps_permission_required_title), getString(R.string.gps_permission_message), null)
                    .show(getChildFragmentManager(), NotificationDialog.TAG);
        }
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Create loader to ensure featured POIs and park news items exist
		List<Long> featuredItemsIdList = UniversalAppStateManager.getInstance().getFeaturedItems();
		if (featuredItemsIdList != null) {
			Bundle loaderArgs = new Bundle();
			DatabaseQuery databaseQuery = DatabaseQueryUtils.getActiveNewsDatabaseQuery(featuredItemsIdList);
			loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
			LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_NEWS_ITEMS, loaderArgs);

			loaderArgs = new Bundle();
			databaseQuery = DatabaseQueryUtils.getDetailDatabaseQuery(featuredItemsIdList);
			loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
			LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_POI_ITEMS, loaderArgs);

			loaderArgs = new Bundle();
			databaseQuery = DatabaseQueryUtils.getEventSeriesByIdDatabaseQuery(featuredItemsIdList);
			loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
			LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_EVENT_SERIES_ITEMS, loaderArgs);
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}

		// Start the auto paging if the user has not touched the pager, and they do not have talk back enabled
		if (!mHasUserTouchedPager && !AccessibilityUtils.isTalkBackEnabled()) {
			mHandler = new Handler();
			mViewPagerAutoSwitchRunnable = new ViewPagerAutoSwitchRunnable(this);
			mHandler.postDelayed(mViewPagerAutoSwitchRunnable, AUTO_SCROLL_PAGER_DELAY_IN_MS);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

		// Update the views based on the geofence
		updateViewsBasedOnGeofence();

		// Listen for state changes
		UniversalAppStateManager.registerStateChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}

		// Stop listening for state changes
		UniversalAppStateManager.unregisterStateChangeListener(this);
		PermissionsManager.getInstance().removeListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}

		outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
		outState.putBoolean(KEY_STATE_HAS_USER_TOUCHED_PAGER, mHasUserTouchedPager);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}

		// Stop the auto pager
		if (mHandler != null && mViewPagerAutoSwitchRunnable != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		if (mFeaturedItemPagerAdapter != null) {
			mFeaturedItemPagerAdapter.destroy();
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
		if (mChooseParkAnimatorSet != null) {
			mChooseParkAnimatorSet.end();
			mChooseParkAnimatorSet.removeAllListeners();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onGlobalLayout() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onGlobalLayout");
		}

		if (mExploreMapParentContainer != null) {

			// Set the map container height, so it will not resize during the
			// animation
			int mapHeightInPx = mExploreMapParentContainer.getMeasuredHeight();

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mExploreMapParentContainer.getLayoutParams();
			params.height = mapHeightInPx;
			mExploreMapParentContainer.setLayoutParams(params);

			// Remove the listener aster setting the layout params
			View view = getView();
			if (view != null && view.getViewTreeObserver() != null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				else {
					// Use this method for older OS versions
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		switch (v.getId()) {
			case R.id.fragment_home_explore_map_wrapper_container:

				// Toggle the choose park layout
				AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
				if (!mIsChooseParkOpen) {
					openChooseParkLayout(CHOOSE_PARK_ANIM_DURATION_IN_MS, interpolator);
				}
				else if (mIsChooseParkOpen) {
					closeChooseParkLayout(CHOOSE_PARK_ANIM_DURATION_IN_MS, interpolator);
				}
				break;
			case R.id.fragment_home_viewpager_and_tab_bar_mask:

				// Close the choose park layout, since the user is tapping out
				// of it
				interpolator = new AccelerateDecelerateInterpolator();
				if (mIsChooseParkOpen) {
					closeChooseParkLayout(CHOOSE_PARK_ANIM_DURATION_IN_MS, interpolator);
				}
				break;
			case R.id.fragment_home_tab_bar_rides:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					CharSequence ridesCharSequence = mTabBarItemRidesText.getText();
					StringBuilder sb = new StringBuilder(ridesCharSequence.length());
					sb.append(ridesCharSequence);
					String ridesText = sb.toString();

					if (ridesText.equals(getString(R.string.home_button_rides))) {
						mParentDrawerClickHandler.handleDrawerClick(
								new DrawerItem(R.string.drawer_item_rides, null), false);
					}
					else if (ridesText.equals(getString(R.string.home_button_wait_times))) {
						mParentDrawerClickHandler.handleDrawerClick(
								new DrawerItem(R.string.drawer_item_wait_times, null), false);
					}
				}
				break;
			case R.id.fragment_home_tab_bar_shows:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					mParentDrawerClickHandler.handleDrawerClick(
							new DrawerItem(R.string.drawer_item_shows, null), false);
				}
				break;
			case R.id.fragment_home_tab_bar_dining:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					mParentDrawerClickHandler.handleDrawerClick(
							new DrawerItem(R.string.drawer_item_dining, null), false);
				}
				break;

			case R.id.fragment_home_tab_bar_buy_ticket:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					mParentDrawerClickHandler.handleDrawerClick(
							new DrawerItem(R.string.drawer_item_buy_tickets, null), false);
				}
				break;
			case R.id.fragment_home_choose_park_ioa:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					mParentDrawerClickHandler.handleDrawerClick(
							new DrawerItem(R.string.drawer_item_islands_of_adventure, null), false);
				}
				break;
			case R.id.fragment_home_choose_park_usf:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					mParentDrawerClickHandler.handleDrawerClick(
							new DrawerItem(R.string.drawer_item_universal_studios_florida, null), false);
				}
				break;
			case R.id.fragment_home_choose_park_cw:

				// Simulate drawer item clicks
				if (mParentDrawerClickHandler != null) {
					mParentDrawerClickHandler.handleDrawerClick(
							new DrawerItem(R.string.drawer_item_citywalk_orlando, null), false);
				}
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
			case R.id.fragment_home_viewpager:
				// If the user interacts with the view pager, stop the auto
				// pager
				mHasUserTouchedPager = true;
				if (mHandler != null && mViewPagerAutoSwitchRunnable != null) {
					mHandler.removeCallbacksAndMessages(null);
				}
				break;
		}
		return false;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPageSelected: " + position);
		}

		mCurrentViewPagerTab = position;

		// Set the proper dot on, and the others off
		int pageCount = mPagerDotContainer.getChildCount();
		if (pageCount > 1) {
			for (int i = 0; i < pageCount; i++) {
				View pagerDot = mPagerDotContainer.getChildAt(i);
				if (pagerDot != null) {
					pagerDot.setBackgroundResource(ImagePagerUtils.getPagerDotResId(i == mCurrentViewPagerTab, PagerDotColor.WHITE));
				}
			}
		}
	}

	@Override
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						// Update the views based on the geofence
						updateViewsBasedOnGeofence();
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}
			});
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateLoader: id = " + id);
		}

		switch (id) {
			case LOADER_ID_FEATURED_NEWS_ITEMS:
			case LOADER_ID_FEATURED_POI_ITEMS:
			case LOADER_ID_FEATURED_EVENT_SERIES_ITEMS:
				String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
				DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
				return LoaderUtils.createCursorLoader(databaseQuery);
			default:
				// Otherwise, let the parent class handle it
				return super.onCreateLoader(id, args);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_FEATURED_NEWS_ITEMS:
				// Pull out ids from the news items
				mValidNewsIdList.clear();
				if (data != null && data.moveToFirst()) {
					do {
						Long newsId = data.getLong(data.getColumnIndex(NewsTable.COL_NEWS_ID));
						String newsObjectJson = data.getString(data.getColumnIndex(NewsTable.COL_NEWS_OBJECT_JSON));

						// If the news item is valid, add it to the list
						if (newsId != null && newsId != 0 && newsObjectJson != null) {
							mValidNewsIdList.add(newsId);
						}
					}
					while (data.moveToNext());
				}
				// Update the featured pager
				mHasCheckedNewsIds = true;
				updateFeaturedItemPagerAdapter();
				break;
			case LOADER_ID_FEATURED_POI_ITEMS:
				// Pull out ids from the POI items
				mValidPoiIdList.clear();
				if (data != null && data.moveToFirst()) {
					do {
						Long poiId = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
						String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));

						// If the poi item is valid, add it to the list
						if (poiId != null && poiId != 0 && poiObjectJson != null) {
							mValidPoiIdList.add(poiId);
						}
					}
					while (data.moveToNext());
				}
				// Update the featured pager
				mHasCheckedPoiIds = true;
				updateFeaturedItemPagerAdapter();
				break;
			case LOADER_ID_FEATURED_EVENT_SERIES_ITEMS:
				// Pull out ids from the Event Series items
				mValidEventSeriesIdList.clear();
				if (data != null && data.moveToFirst()) {
					do {
						Long eventSeriesId = data.getLong(data.getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_ID));
						String eventSeriesObjectJson = data.getString(data.getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));

						// If the event series item is valid, add it to the list
						if (eventSeriesId != null && eventSeriesId != 0 && eventSeriesObjectJson != null) {
							mValidEventSeriesIdList.add(eventSeriesId);
						}
					}
					while (data.moveToNext());
				}
				// Update the featured pager
				mHasCheckedEventSeriesIds = true;
				updateFeaturedItemPagerAdapter();
				break;
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Get the current date
				Date now = new Date();
				long currentTimeInMs = now.getTime();

				// Get the beginning and end of today
				Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
				calendar.setTime(new Date(currentTimeInMs));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				long begTodayInMs = calendar.getTimeInMillis();

				calendar.add(Calendar.DAY_OF_YEAR, 1);
				long endTodayInMs = calendar.getTimeInMillis();

				// Pull out data from the venues
				if (data != null && data.moveToFirst()) {
					do {
						String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
						Long venueId = data.getLong(data.getColumnIndex(VenuesTable.COL_VENUE_ID));

						Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);
						List<VenueHours> venueHoursList = venue.getHours();

						// Go through the hours to see if it is currently in
						// between any
						boolean foundHoursForToday = false;
						if (venueHoursList != null) {
							for (VenueHours venueHours : venueHoursList) {
								if (venueHours == null) {
									continue;
								}

								Long openTimeUnix = venueHours.getOpenTimeUnix();
								Long closeTimeUnix = venueHours.getCloseTimeUnix();
								if (openTimeUnix == null || openTimeUnix == 0 || closeTimeUnix == null || closeTimeUnix == 0) {
									continue;
								}

								// If current time is in between the open hours,
								// use those hours
								if (openTimeUnix * 1000 <= currentTimeInMs && currentTimeInMs < closeTimeUnix * 1000) {
									updateParkHoursView(venueId, openTimeUnix * 1000, closeTimeUnix * 1000);
									foundHoursForToday = true;
									break;
								}
							}
						}

						// If today's hours were found, continue to the next
						// venue
						if (foundHoursForToday) {
							continue;
						}

						// Otherwise, cycle through the hours again and find
						// hours that open today
						if (venueHoursList != null) {
							for (VenueHours venueHours : venueHoursList) {
								if (venueHours == null) {
									continue;
								}

								Long openTimeUnix = venueHours.getOpenTimeUnix();
								Long closeTimeUnix = venueHours.getCloseTimeUnix();
								String openTimeString = venueHours.getOpenTimeString();
								if (openTimeUnix == null || openTimeUnix == 0 || closeTimeUnix == null || closeTimeUnix == 0
										|| openTimeString == null || openTimeString.isEmpty()) {
									continue;
								}

								// If the open time is between the beginning and end of today, use those hours
								if (begTodayInMs <= openTimeUnix * 1000 && openTimeUnix * 1000 < endTodayInMs) {
									updateParkHoursView(venueId, openTimeUnix * 1000, closeTimeUnix * 1000);
									foundHoursForToday = true;
									break;
								}
							}
						}

						// If no suitable hours we found for this venue, hide
						// the hours text
						if (!foundHoursForToday) {
							hideParkHoursView(venueId);
						}
					} while (data.moveToNext());
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_FEATURED_NEWS_ITEMS:
				mHasCheckedNewsIds = false;
				break;
			case LOADER_ID_FEATURED_POI_ITEMS:
				mHasCheckedPoiIds = false;
				break;
			case LOADER_ID_FEATURED_EVENT_SERIES_ITEMS:
				mHasCheckedEventSeriesIds = false;
				break;
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				break;
		}
	}

	@Override
	public String provideTitle() {
		return getString(R.string.action_title_home);
	}

	@Override
	public Picasso providePicasso() {
		return mPicasso;
	}

	private void updateFeaturedItemPagerAdapter() {
		// Only add the pager once, after the ids have been checked
		if (mViewPager.getAdapter() == null && mHasCheckedNewsIds && mHasCheckedPoiIds && mHasCheckedEventSeriesIds) {
			List<Long> featuredItemsIdList = UniversalAppStateManager.getInstance().getFeaturedItems();
			List<FeaturedItem> validFeaturedItemsIdList = new ArrayList<FeaturedItem>();

			// Go through the featured item IDs and only add valid ones
			if (featuredItemsIdList != null) {
				for (Long featuredItemId : featuredItemsIdList) {
					if (mValidNewsIdList.contains(featuredItemId)) {
						validFeaturedItemsIdList.add(new FeaturedItem(featuredItemId, FeaturedItem.Type.NEWS));
					}
					if (mValidPoiIdList.contains(featuredItemId)) {
						validFeaturedItemsIdList.add(new FeaturedItem(featuredItemId, FeaturedItem.Type.POI));
					}
					if (mValidEventSeriesIdList.contains(featuredItemId)) {
						validFeaturedItemsIdList.add(new FeaturedItem(featuredItemId, FeaturedItem.Type.EVENT_SERIES));
					}
				}
			}

			// Create the pager adapter to bind featured POIs, if any are there
			if (validFeaturedItemsIdList.size() > 0) {

				mFeaturedItemPagerAdapter = new FeaturedItemPagerAdapter(
						mViewPager, getChildFragmentManager(), validFeaturedItemsIdList);
				mViewPager.setAdapter(mFeaturedItemPagerAdapter);

				// If the pager only has 1 item, don't autopage
				if (validFeaturedItemsIdList.size() <= 1) {
					mHasUserTouchedPager = true;
				}

				// Clear out the dot pager indicator and add new ones
				mPagerDotContainer.removeAllViews();
				int pageCount = mFeaturedItemPagerAdapter.getCount();
				if (pageCount > 1) {
					for (int i = 0; i < pageCount; i++) {
						View pagerDot = ImagePagerUtils.createPagerDotView(
								mPagerDotContainer, i == mCurrentViewPagerTab, PagerDotColor.WHITE);
						mPagerDotContainer.addView(pagerDot);
					}
					mPagerDotContainer.setVisibility(View.VISIBLE);
				}
				// Only show pager dots if there is more than one page
				else {
					mPagerDotContainer.setVisibility(View.GONE);
				}

				// Hide the hero image and show the view pager
				mHeroImage.setVisibility(View.GONE);
				mViewPager.setVisibility(View.VISIBLE);
			}
			else {
				// No items in the pager, don't autopage
				mHasUserTouchedPager = true;

				// Since there are no items to show, hide the pager and show the hero image
				mViewPager.setVisibility(View.GONE);
				mPagerDotContainer.setVisibility(View.GONE);
				mHeroImage.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateViewsBasedOnGeofence() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();

			if (isInPark) {
				mTabBarItemRidesText.setText(R.string.home_button_wait_times);
				mTabBarItemRidesText.setContentDescription(getString(R.string.home_button_wait_times_text_content_description));
			}
			else {
				mTabBarItemRidesText.setText(R.string.home_button_rides);
				mTabBarItemRidesText.setContentDescription(getString(R.string.home_button_rides_text_content_description));
			}
		}
	}

	private void updateParkHoursView(Long venueId, Long openTimeMs, Long closeTimeMs) {
		if (venueId == null || openTimeMs == null || closeTimeMs == null) {
			return;
		}

		// Format the park hours to 12-hour ("6PM" or "6:30PM"), park time
		String openTimeFormatted;
		String closeTimeFormatted;
		SimpleDateFormat sdfOutTimeNoMinutes;
		SimpleDateFormat sdfOutTimeWithMinutes;

		if (DateTimeUtils.is24HourFormat()) {
			// 24 hour format always shows minutes
			sdfOutTimeNoMinutes = new SimpleDateFormat("HH:mm", Locale.US);
			sdfOutTimeWithMinutes = new SimpleDateFormat("HH:mm", Locale.US);
		} else {
			// Format the park hours to 12-hour ("6PM" or "6:30PM"), park time
			sdfOutTimeNoMinutes = new SimpleDateFormat("ha", Locale.US);
			sdfOutTimeWithMinutes = new SimpleDateFormat("h:mma", Locale.US);
		}

		sdfOutTimeNoMinutes.setTimeZone(DateTimeUtils.getParkTimeZone());
		sdfOutTimeWithMinutes.setTimeZone(DateTimeUtils.getParkTimeZone());

		if (!DateTimeUtils.is24HourFormat() && (openTimeMs / (60 * 1000)) % 60 == 0) {
			openTimeFormatted = sdfOutTimeNoMinutes.format(new Date(openTimeMs));
		} else {
			openTimeFormatted = sdfOutTimeWithMinutes.format(new Date(openTimeMs));
		}

		if (!DateTimeUtils.is24HourFormat() && (closeTimeMs / (60 * 1000)) % 60 == 0) {
			closeTimeFormatted = sdfOutTimeNoMinutes.format(new Date(closeTimeMs));
		} else {
			closeTimeFormatted = sdfOutTimeWithMinutes.format(new Date(closeTimeMs));
		}
		String hoursFormatted = openTimeFormatted + " - " + closeTimeFormatted;

		TextView textViewToSet = null;
		if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE) {
			textViewToSet = mChooseParkIoaHoursText;
		}
		else if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA) {
			textViewToSet = mChooseParkUsfHoursText;
		}
		else if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO) {
			textViewToSet = mChooseParkCwHoursText;
		}

		if (textViewToSet != null) {
			textViewToSet.setText(hoursFormatted);
			textViewToSet.setVisibility(View.VISIBLE);
		}
	}

	private void hideParkHoursView(Long venueId) {
		if (venueId == null) {
			return;
		}

		TextView textViewToHide = null;
		if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE) {
			textViewToHide = mChooseParkIoaHoursText;
		}
		else if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA) {
			textViewToHide = mChooseParkUsfHoursText;
		}
		else if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO) {
			textViewToHide = mChooseParkCwHoursText;
		}

		if (textViewToHide != null) {
			textViewToHide.setVisibility(View.INVISIBLE);
		}
	}

	private void openChooseParkLayout(long durationInMs, TimeInterpolator timeInterpolator) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "openChooseParkLayout");
		}

		int tabBarShadowHeightInPx = mTabBarShadow.getMeasuredHeight();
		int chooseParkContentLayoutHeightInPx = mChooseParkContentLayout.getMeasuredHeight();
		int chooseParkCaretHeightInPx = mChooseParkCaret.getMeasuredHeight();

		int tabBarBottomMargin = ((RelativeLayout.LayoutParams) mTabBarLayout.getLayoutParams()).bottomMargin;
		ValueAnimator tabBarMarginAnimator = ValueAnimator.ofInt(tabBarBottomMargin, chooseParkContentLayoutHeightInPx - tabBarShadowHeightInPx);
		tabBarMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer bottomMargin = (Integer) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTabBarLayout.getLayoutParams();
				params.bottomMargin = bottomMargin;
				mTabBarLayout.setLayoutParams(params);
			}
		});

		int viewPagerTopMargin = ((RelativeLayout.LayoutParams) mViewPagerContainer.getLayoutParams()).topMargin;
		ValueAnimator viewPagerMarginAnimator = ValueAnimator.ofInt(viewPagerTopMargin, -chooseParkContentLayoutHeightInPx);
		viewPagerMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer topMargin = (Integer) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewPagerContainer.getLayoutParams();
				params.topMargin = topMargin;
				mViewPagerContainer.setLayoutParams(params);
			}
		});

		int chooseParkCaretTopMargin = ((RelativeLayout.LayoutParams) mChooseParkCaret.getLayoutParams()).topMargin;
		ValueAnimator caretMarginAnimator = ValueAnimator.ofInt(chooseParkCaretTopMargin, 0);
		caretMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer topMargin = (Integer) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mChooseParkCaret.getLayoutParams();
				params.topMargin = topMargin;
				mChooseParkCaret.setLayoutParams(params);
			}
		});

		if (mChooseParkAnimatorSet != null) {
			mChooseParkAnimatorSet.removeAllListeners();
			mChooseParkAnimatorSet.cancel();
		}
		else {
			mChooseParkAnimatorSet = new AnimatorSet();
		}

		mChooseParkAnimatorSet.setInterpolator(timeInterpolator);
		mChooseParkAnimatorSet.setDuration(durationInMs);
		mChooseParkAnimatorSet.play(tabBarMarginAnimator).with(viewPagerMarginAnimator).with(caretMarginAnimator);
		mChooseParkAnimatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				mIsChooseParkOpen = true;
				mViewPagerAndTabBarMaskLayout.setVisibility(View.VISIBLE);
				mChooseParkContainer.setVisibility(View.VISIBLE);

				// Set choose park layout back to visible (so TalkBack can read it)
				mChooseParkContentLayout.setVisibility(View.VISIBLE);

				// Set the close button back to visible (so TalkBack can read it)
				mExploreMapCircleClose.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mViewPagerAndTabBarMaskLayout.setVisibility(View.VISIBLE);

				// Make the explore the park map views invisible (so TalkBack won't read them)
				mExploreMapCirclePin.setVisibility(View.INVISIBLE);
				mExploreMapExploreTheText.setVisibility(View.INVISIBLE);
				mExploreMapResortMapText.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});



		mExploreMapCircleClose.animate()
				.alpha(1f)
				.rotation(360f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mExploreMapCirclePin.animate()
				.alpha(0f)
				.rotation(360f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mExploreMapExploreTheText.animate()
				.alpha(0f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mExploreMapResortMapText.animate()
				.alpha(0f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mChooseParkAnimatorSet.start();
	}

	private void closeChooseParkLayout(long durationInMs, TimeInterpolator timeInterpolator) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "closeChooseParkLayout");
		}

		int tabBarShadowHeightInPx = mTabBarShadow.getMeasuredHeight();
		int chooseParkContentLayoutHeightInPx = mChooseParkContentLayout.getMeasuredHeight();
		int chooseParkCaretHeightInPx = mChooseParkCaret.getMeasuredHeight();

		int tabBarBottomMargin = ((RelativeLayout.LayoutParams) mTabBarLayout.getLayoutParams()).bottomMargin;
		ValueAnimator tabBarMarginAnimator = ValueAnimator.ofInt(tabBarBottomMargin, -tabBarShadowHeightInPx);
		tabBarMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer bottomMargin = (Integer) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTabBarLayout.getLayoutParams();
				params.bottomMargin = bottomMargin;
				mTabBarLayout.setLayoutParams(params);
			}
		});

		int viewPagerTopMargin = ((RelativeLayout.LayoutParams) mViewPagerContainer.getLayoutParams()).topMargin;
		ValueAnimator viewPagerMarginAnimator = ValueAnimator.ofInt(viewPagerTopMargin, 0);
		viewPagerMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer topMargin = (Integer) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewPagerContainer.getLayoutParams();
				params.topMargin = topMargin;
				mViewPagerContainer.setLayoutParams(params);
			}
		});

		int chooseParkCaretTopMargin = ((RelativeLayout.LayoutParams) mChooseParkCaret.getLayoutParams()).topMargin;
		ValueAnimator caretMarginAnimator = ValueAnimator.ofInt(chooseParkCaretTopMargin, -chooseParkCaretHeightInPx);
		caretMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer topMargin = (Integer) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mChooseParkCaret.getLayoutParams();
				params.topMargin = topMargin;
				mChooseParkCaret.setLayoutParams(params);
			}
		});

		if (mChooseParkAnimatorSet != null) {
			mChooseParkAnimatorSet.removeAllListeners();
			mChooseParkAnimatorSet.cancel();
		}
		else {
			mChooseParkAnimatorSet = new AnimatorSet();
		}
		mChooseParkAnimatorSet.setInterpolator(timeInterpolator);
		mChooseParkAnimatorSet.setDuration(durationInMs);
		mChooseParkAnimatorSet.play(tabBarMarginAnimator).with(viewPagerMarginAnimator).with(caretMarginAnimator);
		mChooseParkAnimatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				mIsChooseParkOpen = false;
				mViewPagerAndTabBarMaskLayout.setVisibility(View.VISIBLE);

				// Set the explore the park map views back to visible (so TalkBack will read them)
				mExploreMapCirclePin.setVisibility(View.VISIBLE);
				mExploreMapExploreTheText.setVisibility(View.VISIBLE);
				mExploreMapResortMapText.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mViewPagerAndTabBarMaskLayout.setVisibility(View.GONE);
				mChooseParkContainer.setVisibility(View.INVISIBLE);

				// Set choose park layout to invisible on close (so TalkBack does not read it)
				mChooseParkContentLayout.setVisibility(View.INVISIBLE);

				// Set close circle to invisible (so TalkBack does not read it)
				mExploreMapCircleClose.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		mExploreMapCircleClose.animate()
				.alpha(0f)
				.rotation(0f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mExploreMapCirclePin.animate()
				.alpha(1f)
				.rotation(0f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mExploreMapExploreTheText.animate()
				.alpha(1f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mExploreMapResortMapText.animate()
				.alpha(1f)
				.setDuration(durationInMs)
				.setInterpolator(timeInterpolator);

		mChooseParkAnimatorSet.start();
	}

	// Private static class using weak references to prevent leaking a context
	private static class ViewPagerAutoSwitchRunnable implements Runnable {
		private final WeakReference<HomeFragment> mHomeFragment;

		public ViewPagerAutoSwitchRunnable(HomeFragment homeFragment) {
			mHomeFragment = new WeakReference<HomeFragment>(homeFragment);
		}

		@Override
		public void run() {

			final HomeFragment homeFragment = mHomeFragment.get();
			if (homeFragment != null) {
				if (homeFragment.mViewPager != null && homeFragment.mFeaturedItemPagerAdapter != null) {
					int nextPageIndex = (homeFragment.mCurrentViewPagerTab + 1) % homeFragment.mFeaturedItemPagerAdapter.getCount();
					homeFragment.mViewPager.setCurrentItem(nextPageIndex, true);
					homeFragment.mHandler.postDelayed(homeFragment.mViewPagerAutoSwitchRunnable, AUTO_SCROLL_PAGER_DELAY_IN_MS);
				}
			}
		}
	}
}
