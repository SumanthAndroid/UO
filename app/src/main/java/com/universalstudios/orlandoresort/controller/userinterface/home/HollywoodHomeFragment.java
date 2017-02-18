/**
 *
 */
package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.MapDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerClickHandler;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerItem;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreFragment;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions.FilterSort;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.home.FeaturedItem.Type;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomePoiRecyclerAdapter.ViewAllType;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils.PagerDotColor;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.controller.userinterface.list.DividerItemDecoration;
import com.universalstudios.orlandoresort.controller.userinterface.list.HorizontalSpaceItemDecoration;
import com.universalstudios.orlandoresort.controller.userinterface.news.NewsDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.newsletter.NewsletterActivity;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wayfinding.WayfindingUtils;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.dialogs.NotificationDialog;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsRequestsListener;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
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
 * @author Steven Byle
 */
public class HollywoodHomeFragment extends DatabaseQueryFragment implements ActionBarTitleProvider,
        OnPageChangeListener, PicassoProvider, OnClickListener, HomeOffersRecyclerAdapter.OnItemClickListener,
        View.OnTouchListener, OnUniversalAppStateChangeListener, PermissionsRequestsListener,
        HomeNewsRecyclerAdapter.OnItemClickListener, HomePoiRecyclerAdapter.OnItemClickListener {
    private static final String TAG = HollywoodHomeFragment.class.getSimpleName();

    private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
    private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";
    private static final String KEY_STATE_HAS_USER_TOUCHED_PAGER = "KEY_STATE_HAS_USER_TOUCHED_PAGER";

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID_FEATURED_UNREAD_NEWS_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT;
    private static final int LOADER_ID_FEATURED_POI_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_2;
    private static final int LOADER_ID_FEATURED_EVENT_SERIES_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_3;
    private static final int LOADER_ID_LATEST_NEWS_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_4;
    private static final int LOADER_ID_SHORTEST_WAIT_TIMES_POI_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_5;
    private static final int LOADER_ID_UPCOMING_EVENT_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_6;
    private static final int LOADER_ID_OFFERS_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_7;
    private static final int LOADER_ID_FEATURED_OFFERS_ITEMS = LoaderUtils.LOADER_ID_HOME_FRAGMENT_8;

    private static final long AUTO_SCROLL_PAGER_DELAY_IN_MS = 4 * 1000;
    private static final long CHOOSE_PARK_ANIM_DURATION_IN_MS = 600;
    private static final int MAX_RECENT_NEWS_ITEMS = 2;
    private static final int MAX_WAIT_TIMES_ITEMS = 5;
    private static final int MAX_UPCOMING_EVENT_ITEMS = 5;

    private int mCurrentViewPagerTab;
    private int mCalculatedImageHeightDp;
    private boolean mHasUserTouchedPager;
    private ViewGroup mParkHoursContainer, mParkUpdatesContainer, mShortWaitTimesContainer, mUpcomingEventsContainer;
    private TextView mParkHoursText;
    private RelativeLayout mViewPagerContainer;
    private JazzyViewPager mViewPager;
    private LinearLayout mPagerDotContainer;
    private ViewGroup mBrowseAttractionsButton, mBrowseDiningButton, mBrowseShoppingButton;
    private ImageView mBrowseMapIcon, mBrowseAttractionsIcon, mBrowseDiningIcon, mBrowseShoppingIcon;
    private View mNewsletterSignupButton, mBuyTicketsButton;
    private RecyclerView mNewsRecyclerView, mShortWaitTimesRecyclerView, mUpcomingEventsRecyclerView, mOffersRecyclerView;

    private FeaturedItemPagerAdapter mFeaturedItemPagerAdapter;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private Picasso mPicasso;
    private HomeFragmentManager mParentHomeFragmentManager;
    private DrawerStateProvider mParentDrawerStateProvider;
    private DrawerClickHandler mParentDrawerClickHandler;
    private Handler mHandler;
    private ViewPagerAutoSwitchRunnable mViewPagerAutoSwitchRunnable;
    private ImageView mHeroImage;
    private List<Long> mValidNewsIdList;
    private List<Long> mValidPoiIdList;
    private List<Long> mValidEventSeriesIdList;
    private List<Long> mValidOfferIdList;
    private boolean mHasCheckedNewsIds;
    private boolean mHasCheckedPoiIds;
    private boolean mHasCheckedEventSeriesIds;
    private boolean mHasCheckedOfferIds;
    private HomeNewsRecyclerAdapter mNewsRecyclerAdapter;
    private HomeOffersRecyclerAdapter mOffersRecyclerAdapter;
    private HomePoiRecyclerAdapter mShortWaitTimesRecyclerAdapter, mUpcomingEventsRecyclerAdapter;
    private List<VenueHours> mUshVenueHoursList;

    public static HollywoodHomeFragment newInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        HollywoodHomeFragment fragment = new HollywoodHomeFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        DatabaseQuery databaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(
                VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD,
                VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD);
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

        // Check if parent fragment (if there is one) implements the interface
        if (parentFragment != null && parentFragment instanceof HomeFragmentManager) {
            mParentHomeFragmentManager = (HomeFragmentManager) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof HomeFragmentManager) {
            mParentHomeFragmentManager = (HomeFragmentManager) activity;
        }
        // If neither implements the interface, log a warning
        else if (mParentHomeFragmentManager == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement HomeFragmentManager");
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
        View fragmentView = inflater.inflate(R.layout.fragment_home_hollywood, container, false);

        // Setup Views
        mParkHoursContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_park_hours_container);
        mParkHoursText = (TextView) fragmentView.findViewById(R.id.fragment_home_park_hours_text);
        mViewPagerContainer = (RelativeLayout) fragmentView.findViewById(R.id.fragment_home_viewpager_container);
        mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_home_viewpager);
        mHeroImage = (ImageView) fragmentView.findViewById(R.id.fragment_home_hero_image);
        mPagerDotContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_home_dot_layout);
        mBrowseMapIcon = (ImageView) fragmentView.findViewById(R.id.fragment_home_explore_map_image);
        mBrowseAttractionsButton = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_explore_tab_attractions);
        mBrowseAttractionsIcon = (ImageView) fragmentView.findViewById(R.id.fragment_home_explore_tab_attractions_image);
        mBrowseDiningButton = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_explore_tab_dining);
        mBrowseDiningIcon = (ImageView) fragmentView.findViewById(R.id.fragment_home_explore_tab_dining_image);
        mBrowseShoppingButton = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_explore_tab_shopping);
        mBrowseShoppingIcon = (ImageView) fragmentView.findViewById(R.id.fragment_home_explore_tab_shopping_image);
        mNewsletterSignupButton = fragmentView.findViewById(R.id.fragment_home_newsletter_clickable_layout);
        mBuyTicketsButton = fragmentView.findViewById(R.id.fragment_home_buy_tickets_clickable_layout);
        mParkUpdatesContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_park_updates_layout);
        mNewsRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.fragment_home_park_updates_recycler_view);
        mOffersRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.fragment_home_offers_recycler_view);
        mShortWaitTimesContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_short_wait_times_layout);
        mShortWaitTimesRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.fragment_home_short_wait_times_recycler_view);
        mUpcomingEventsContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_home_upcoming_events_layout);
        mUpcomingEventsRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.fragment_home_upcoming_events_recycler_view);

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mCurrentViewPagerTab = mViewPager.getCurrentItem();
        }
        // Otherwise, restore state
        else {
            mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);
        }

        // Set pager height based on detail image aspect ratio
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCalculatedImageHeightDp, displayMetrics));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, calculatedImageHeightPx);
        mViewPagerContainer.setLayoutParams(layoutParams);

        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOnTouchListener(this);
        mViewPager.setTransitionEffect(TransitionEffect.Standard);
        mViewPager.setFadeEnabled(false);
        mViewPager.setFocusable(false);

        // Tint the icons
        TintUtils.tintImageView(ContextCompat.getColor(getContext(), R.color.purple_light), mBrowseMapIcon);
        TintUtils.tintImageView(ContextCompat.getColor(getContext(), R.color.text_white),
                mBrowseAttractionsIcon, mBrowseDiningIcon, mBrowseShoppingIcon);

        // Set click listeners
        mBrowseAttractionsButton.setOnClickListener(this);
        mBrowseDiningButton.setOnClickListener(this);
        mBrowseShoppingButton.setOnClickListener(this);
        mNewsletterSignupButton.setOnClickListener(this);
        mBuyTicketsButton.setOnClickListener(this);
        mParkHoursContainer.setOnClickListener(this);

        mValidNewsIdList = new ArrayList<Long>();
        mValidPoiIdList = new ArrayList<Long>();
        mValidEventSeriesIdList = new ArrayList<Long>();
        mValidOfferIdList = new ArrayList<Long>();
        mHasCheckedNewsIds = false;
        mHasCheckedPoiIds = false;
        mHasCheckedEventSeriesIds = false;
        mHasCheckedOfferIds = false;

        // Bind the adapters
        mNewsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.shape_divider_gray));
        mNewsRecyclerAdapter = new HomeNewsRecyclerAdapter(null, this);
        mNewsRecyclerView.setAdapter(mNewsRecyclerAdapter);

        mShortWaitTimesRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(R.dimen.explore_list_horizontal_space));
        mShortWaitTimesRecyclerAdapter = new HomePoiRecyclerAdapter(getContext(), null, ExploreType.WAIT_TIMES, ViewAllType.POI, this);
        mShortWaitTimesRecyclerView.setAdapter(mShortWaitTimesRecyclerAdapter);

        mUpcomingEventsRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(R.dimen.explore_list_horizontal_space));
        mUpcomingEventsRecyclerAdapter = new HomePoiRecyclerAdapter(getContext(), null, ExploreType.EVENT_LIST, ViewAllType.EVENT, this);
        mUpcomingEventsRecyclerView.setAdapter(mUpcomingEventsRecyclerAdapter);

        mOffersRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(R.dimen.explore_list_horizontal_space));
        mOffersRecyclerAdapter = new HomeOffersRecyclerAdapter(getContext(), null, this);
        mOffersRecyclerView.setAdapter(mOffersRecyclerAdapter);

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

        PermissionsManager.getInstance().handlePermissionRequest(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, this);
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
        UniversalAppState appState = UniversalAppStateManager.getInstance();
        List<Long> featuredItemsIdList = appState.getFeaturedItems();
        if (featuredItemsIdList != null) {
            Bundle loaderArgs = new Bundle();
            DatabaseQuery databaseQuery = DatabaseQueryUtils.getActiveNewsDatabaseQuery(featuredItemsIdList);
            loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
            LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_UNREAD_NEWS_ITEMS, loaderArgs);

            loaderArgs = new Bundle();
            databaseQuery = DatabaseQueryUtils.getDetailDatabaseQuery(featuredItemsIdList);
            loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
            LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_POI_ITEMS, loaderArgs);

            loaderArgs = new Bundle();
            databaseQuery = DatabaseQueryUtils.getEventSeriesByIdDatabaseQuery(featuredItemsIdList);
            loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
            LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_EVENT_SERIES_ITEMS, loaderArgs);

            loaderArgs = new Bundle();
            databaseQuery = DatabaseQueryUtils.getOffersById(featuredItemsIdList);
            loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
            LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_FEATURED_OFFERS_ITEMS, loaderArgs);
        }

        // Create loader to get the latest news items
        Bundle loaderArgs = new Bundle();
        DatabaseQuery databaseQuery = DatabaseQueryUtils.getAllActiveNewsDatabaseQuery(MAX_RECENT_NEWS_ITEMS);
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_LATEST_NEWS_ITEMS, loaderArgs);

        // Create loader to get the shortest wait times
        loaderArgs = new Bundle();
        databaseQuery = DatabaseQueryUtils.getExploreByShortestWaitTimesDatabaseQuery(MAX_WAIT_TIMES_ITEMS,
                PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE);
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_SHORTEST_WAIT_TIMES_POI_ITEMS, loaderArgs);

        // Create loader to get upcoming events
        loaderArgs = new Bundle();
        databaseQuery = DatabaseQueryUtils.getAllUpcomingTimelineEvents(
                new FilterOptions(null, ExploreType.UPCOMING_EVENTS), true, MAX_UPCOMING_EVENT_ITEMS);
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_UPCOMING_EVENT_ITEMS, loaderArgs);

        // Create loader to get the featured offer items
        List<Long> featuredOffersIdList = appState.getFeaturedOfferItems();
        if (featuredOffersIdList == null) {
            featuredOffersIdList = new ArrayList<>();
        }
        loaderArgs = new Bundle();
        databaseQuery = DatabaseQueryUtils.getOffersById(featuredOffersIdList);
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_OFFERS_ITEMS, loaderArgs);
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
        // Release resources used to load images
        if (mShortWaitTimesRecyclerAdapter != null) {
            mShortWaitTimesRecyclerAdapter.destroy();
        }
        // Release resources used to load images
        if (mUpcomingEventsRecyclerAdapter != null) {
            mUpcomingEventsRecyclerAdapter.destroy();
        }
        // Release resources used to load images
        if (mOffersRecyclerAdapter != null) {
            mOffersRecyclerAdapter.destroy();
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

        Fragment newFragment = null;

        switch (v.getId()) {
            case R.id.fragment_home_park_hours_container:
                // Simulate park hours drawer item click
                if (mParentDrawerClickHandler != null) {
                    mParentDrawerClickHandler.handleDrawerClick(
                            new DrawerItem(R.string.drawer_item_hours_and_directions, null), false);
                }
                break;
            case R.id.fragment_home_explore_tab_attractions:
                // If USH is open, sort attractions by wait time
                FilterOptions ushFilterOptions = null;
                boolean isDuringUshVenueHours = PoiUtils.isDuringVenueHours(System.currentTimeMillis(), mUshVenueHoursList);
                if (isDuringUshVenueHours) {
                    ushFilterOptions = new FilterOptions(null, ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS);
                    ushFilterOptions.setFilterSort(FilterSort.WAIT_TIMES);
                }
                newFragment = ExploreFragment.newInstance(
                        R.string.drawer_item_universal_studios_hollywood,
                        ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS,
                        ushFilterOptions);
                mParentHomeFragmentManager.replaceMainFragment(newFragment, false);
                break;
            case R.id.fragment_home_explore_tab_dining:
                newFragment = ExploreFragment.newInstance(
                        R.string.drawer_item_universal_studios_hollywood,
                        ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_DINING);
                mParentHomeFragmentManager.replaceMainFragment(newFragment, false);
                break;
            case R.id.fragment_home_explore_tab_shopping:
                newFragment = ExploreFragment.newInstance(
                        R.string.drawer_item_universal_studios_hollywood,
                        ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING);
                mParentHomeFragmentManager.replaceMainFragment(newFragment, false);
                break;
            case R.id.fragment_home_newsletter_clickable_layout:
                startActivity(new Intent(getContext(), NewsletterActivity.class));
                break;
            case R.id.fragment_home_buy_tickets_clickable_layout:
                UniversalAppState uoState = UniversalAppStateManager.getInstance();
                String ticketsUrl = uoState.getTicketWebStoreUrl();
                if (!TextUtils.isEmpty(ticketsUrl)) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    Bundle bundle = WebViewActivity.newInstanceBundle(
                            R.string.drawer_item_buy_tickets, ticketsUrl, true);
                    intent.putExtras(bundle);
                    startActivity(intent);
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
    public void onNewsItemClicked(News news) {
        // Open the news detail page
        if (news != null) {
            Long newsId = news.getId();

            Activity parentActivity = getActivity();
            if (parentActivity != null && newsId != null) {
                parentActivity.startActivity(new Intent(parentActivity, NewsDetailActivity.class)
                        .putExtras(NewsDetailActivity.newInstanceBundle(newsId)));
            }
        }
    }

    @Override
    public void onNewsViewAllClicked() {
        // Simulate park updates drawer item click
        if (mParentDrawerClickHandler != null) {
            mParentDrawerClickHandler.handleDrawerClick(
                    new DrawerItem(R.string.drawer_item_park_news, null), false);
        }
    }

    @Override
    public void onOfferItemClicked(Offer offer) {
        if (offer != null) {
            Long offerId = offer.getId();
            if (offerId != null) {
                DetailUtils.openOfferDetailPage(getContext(), offerId);
            }
        }
    }

    @Override
    public void onPoiItemClicked(String poiObjectJson, int poiTypeId, String venueObjectJson) {
        DetailUtils.openDetailPage(getContext(), venueObjectJson, poiObjectJson, poiTypeId, false);
    }

    @Override
    public void onPoiItemGuideMeClicked(String poiObjectJson, int poiTypeId) {
        boolean didLoadWayfindingPage = WayfindingUtils.openWayfindingPage(getContext(), poiObjectJson, poiTypeId);
        if (!didLoadWayfindingPage && getActivity() != null) {
            UserInterfaceUtils.showToastFromForeground(
                    getString(R.string.wayfinding_toast_error_trying_to_start_guiding), Toast.LENGTH_LONG, getActivity());
        }
    }

    @Override
    public void onPoiItemLocateClicked(String poiObjectJson, int poiTypeId, long poiId) {
        // Open a map page to the selected POI
        Bundle detailMapBundle = MapDetailActivity.newInstanceBundle(
                DatabaseQueryUtils.getDetailDatabaseQuery(poiId),
                ExploreType.SOLO_MAP_PAGE, poiObjectJson);
        Intent intent = new Intent(getContext(), MapDetailActivity.class)
                .putExtras(detailMapBundle);
        startActivity(intent);
    }

    @Override
    public void onViewAllClicked(ViewAllType viewAllType) {
        switch (viewAllType) {
            case POI:
                Fragment newFragment = ExploreFragment.newInstance(
                        R.string.drawer_item_universal_studios_hollywood,
                        ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS);
                mParentHomeFragmentManager.replaceMainFragment(newFragment, false);
                break;
            case EVENT:
                // Simulate events drawer item click
                if (mParentDrawerClickHandler != null) {
                    mParentDrawerClickHandler.handleDrawerClick(
                            new DrawerItem(R.string.drawer_item_events, null), false);
                }
                break;
            default:
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
                    } catch (Exception e) {
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
            case LOADER_ID_FEATURED_UNREAD_NEWS_ITEMS:
            case LOADER_ID_FEATURED_POI_ITEMS:
            case LOADER_ID_FEATURED_EVENT_SERIES_ITEMS:
            case LOADER_ID_LATEST_NEWS_ITEMS:
            case LOADER_ID_SHORTEST_WAIT_TIMES_POI_ITEMS:
            case LOADER_ID_UPCOMING_EVENT_ITEMS:
            case LOADER_ID_OFFERS_ITEMS:
            case LOADER_ID_FEATURED_OFFERS_ITEMS:
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
            case LOADER_ID_FEATURED_UNREAD_NEWS_ITEMS:
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
            case LOADER_ID_FEATURED_OFFERS_ITEMS:
                // Pull out ids from the offers items
                mValidOfferIdList.clear();
                if (data != null && data.moveToFirst()) {
                    do {
                        Long offerId = data.getLong(data.getColumnIndex(OffersTable.COL_OFFER_ID));
                        String offerObjectJson = data.getString(data.getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));

                        // If the offer item is valid, add it to the list
                        if (offerId != null && offerId != 0 && offerObjectJson != null) {
                            mValidOfferIdList.add(offerId);
                        }
                    }
                    while (data.moveToNext());
                }
                // Update the featured pager
                mHasCheckedOfferIds = true;
                updateFeaturedItemPagerAdapter();
                break;
            case LOADER_ID_LATEST_NEWS_ITEMS:
                // Swap current cursor with a new up to date cursor
                mNewsRecyclerAdapter.swapCursor(data);

                // Show/hide the news layout
                boolean doesAnyNewsExist = (data != null && data.getCount() > 0);
                mParkUpdatesContainer.setVisibility(doesAnyNewsExist ? View.VISIBLE : View.GONE);
                break;
            case LOADER_ID_OFFERS_ITEMS:
                // Swap current cursor with a new up to date cursor
                mOffersRecyclerAdapter.swapCursor(data);

                // Show/hide the offers layout
                boolean doOffersExist = (data != null && data.getCount() > 0);
                mOffersRecyclerView.setVisibility(doOffersExist ? View.VISIBLE : View.GONE);
                break;
            case LOADER_ID_SHORTEST_WAIT_TIMES_POI_ITEMS:
                // Swap current cursor with a new up to date cursor
                mShortWaitTimesRecyclerAdapter.swapCursor(data);

                // Check if any POIs had wait times
                boolean doAnyWaitTimesExist = (data != null && data.getCount() > 0);

                // Check to see if the POI data has been synced recently and is not stale
                UniversalAppState appState = UniversalAppStateManager.getInstance();
                boolean isPoiDataFresh = UniversalAppStateManager.hasSyncedInTheLast(
                        appState.getDateOfLastPoiSyncInMillis(), PoiUtils.WAIT_TIME_STALE_THRESHOLD_IN_SEC);

                // Only show the shortest wait time section if the data exists, and is fresh
                mShortWaitTimesContainer.setVisibility(doAnyWaitTimesExist && isPoiDataFresh ? View.VISIBLE : View.GONE);
                break;
            case LOADER_ID_UPCOMING_EVENT_ITEMS:
                // Swap current cursor with a new up to date cursor
                mUpcomingEventsRecyclerAdapter.swapCursor(data);

                // Show/hide the events layout
                boolean doAnyEventsExist = (data != null && data.getCount() > 0);
                mUpcomingEventsContainer.setVisibility(doAnyEventsExist ? View.VISIBLE : View.GONE);
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
                        long venueId = data.getLong(data.getColumnIndex(VenuesTable.COL_VENUE_ID));

                        Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);
                        List<VenueHours> venueHoursList = venue.getHours();

                        if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
                            mUshVenueHoursList = venueHoursList;
                        }

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
            case LOADER_ID_FEATURED_UNREAD_NEWS_ITEMS:
                mHasCheckedNewsIds = false;
                break;
            case LOADER_ID_FEATURED_POI_ITEMS:
                mHasCheckedPoiIds = false;
                break;
            case LOADER_ID_FEATURED_EVENT_SERIES_ITEMS:
                mHasCheckedEventSeriesIds = false;
                break;
            case LOADER_ID_FEATURED_OFFERS_ITEMS:
                mHasCheckedOfferIds = false;
                break;
            case LOADER_ID_LATEST_NEWS_ITEMS:
                mNewsRecyclerAdapter.swapCursor(null);
                break;
            case LOADER_ID_SHORTEST_WAIT_TIMES_POI_ITEMS:
                mShortWaitTimesRecyclerAdapter.swapCursor(null);
                break;
            case LOADER_ID_OFFERS_ITEMS:
                mOffersRecyclerAdapter.swapCursor(null);
                break;
            case LOADER_ID_UPCOMING_EVENT_ITEMS:
                mUpcomingEventsRecyclerAdapter.swapCursor(null);
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
        if (mViewPager.getAdapter() == null && mHasCheckedNewsIds && mHasCheckedPoiIds
            && mHasCheckedEventSeriesIds && mHasCheckedOfferIds) {
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
                    if (mValidOfferIdList.contains(featuredItemId)) {
                        validFeaturedItemsIdList.add(new FeaturedItem(featuredItemId, Type.OFFER));
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
            } else {
                // No items in the pager, don't autopage
                mHasUserTouchedPager = true;

                // Since there are no items to show, hide the pager and show the hero image
                mViewPager.setVisibility(View.GONE);
                mPagerDotContainer.setVisibility(View.GONE);
                mHeroImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateParkHoursView(Long venueId, Long openTimeMs, Long closeTimeMs) {
        if (venueId == null || openTimeMs == null || closeTimeMs == null) {
            return;
        }

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

        ViewGroup parentViewToShow = null;
        TextView textViewToSet = null;
        if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
            parentViewToShow = mParkHoursContainer;
            textViewToSet = mParkHoursText;
        }

        if (parentViewToShow != null && textViewToSet != null) {
            textViewToSet.setText(hoursFormatted);
            textViewToSet.setVisibility(View.VISIBLE);
            parentViewToShow.setVisibility(View.VISIBLE);
        }
    }

    private void hideParkHoursView(Long venueId) {
        if (venueId == null) {
            return;
        }

        View viewToHide = null;
        if (venueId.longValue() == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
            viewToHide = mParkHoursContainer;
        }

        if (viewToHide != null) {
            viewToHide.setVisibility(View.GONE);
        }
    }

    // Private static class using weak references to prevent leaking a context
    private static class ViewPagerAutoSwitchRunnable implements Runnable {
        private final WeakReference<HollywoodHomeFragment> mHomeFragment;

        public ViewPagerAutoSwitchRunnable(HollywoodHomeFragment homeFragment) {
            mHomeFragment = new WeakReference<HollywoodHomeFragment>(homeFragment);
        }

        @Override
        public void run() {

            final HollywoodHomeFragment homeFragment = mHomeFragment.get();
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
