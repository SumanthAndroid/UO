package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountLoginActivity;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountSecurityFragment;
import com.universalstudios.orlandoresort.controller.userinterface.account.ViewProfileFragment;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.AlertsListActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.debug.DebugOptionsActivity;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerClickHandler;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerItem;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerItemExpandableListAdapter;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerSectionHeader;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventSeriesDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventSeriesFragment;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreFragment;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions.FilterSort;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.help.AboutWifiFragment;
import com.universalstudios.orlandoresort.controller.userinterface.help.GuestServicesActivity;
import com.universalstudios.orlandoresort.controller.userinterface.help.GuestServicesFragment;
import com.universalstudios.orlandoresort.controller.userinterface.help.HelpFragment;
import com.universalstudios.orlandoresort.controller.userinterface.hours.HoursAndDirectionsFragment;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ShoppingActivity;
import com.universalstudios.orlandoresort.controller.userinterface.legal.PrivacyAndLegalFragment;
import com.universalstudios.orlandoresort.controller.userinterface.news.NewsListFragment;
import com.universalstudios.orlandoresort.controller.userinterface.offers.OfferSeriesDetailFragment;
import com.universalstudios.orlandoresort.controller.userinterface.parking.ParkingReminderFragment;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.controller.userinterface.search.SearchActivity;
import com.universalstudios.orlandoresort.controller.userinterface.settings.SettingsAndAboutFragment;
import com.universalstudios.orlandoresort.controller.userinterface.tickets.MyAppointmentTicketsFragment;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.WalletFolioActivity;
import com.universalstudios.orlandoresort.controller.userinterface.web.BuyTicketsWebViewFragment;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewFragment;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.model.state.account.AccountLoginService;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.model.state.account.LoginBroadcastReceiver;
import com.universalstudios.orlandoresort.model.state.account.LoginResultIntentFilter;
import com.universalstudios.orlandoresort.model.state.commerce.CommerceStateManager;
import com.universalstudios.orlandoresort.model.state.parking.ParkingState;
import com.universalstudios.orlandoresort.model.state.parking.ParkingStateManager;
import com.universalstudios.orlandoresort.utils.WnWKillSwitchDateUtil;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.util.List;

import static com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerItem.BLANK_DRAWABLE_RESOURCE_ID;

/**
 * @author Steven Byle
 */
public class HomeActivity extends DatabaseQueryActivity implements ActionBarTitleProvider, DrawerStateProvider,
        OnGroupClickListener, OnChildClickListener, DrawerClickHandler, HomeFragmentManager {

    private static final String TAG = HomeActivity.class.getSimpleName();

    public static final int START_PAGE_TYPE_HOME = 0;
    public static final int START_PAGE_TYPE_NEWS_LIST = 1;
    public static final int START_PAGE_TYPE_VIEW_PROFILE = 6;

    private static final String KEY_ARG_START_PAGE_TYPE = "KEY_ARG_START_PAGE_TYPE";
    private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
    private static final String KEY_STATE_CUR_ACTION_BAR_TITLE = "KEY_STATE_CUR_ACTION_BAR_TITLE";
    private static final String KEY_STATE_IS_DRAWER_MENU_OPEN_AT_ALL = "KEY_STATE_IS_DRAWER_MENU_OPEN_AT_ALL";
    private static final String KEY_STATE_IS_DRAWER_SHOWING_LOGGED_IN = "KEY_STATE_IS_DRAWER_SHOWING_LOGGED_IN";

    private static final int ACTIVITY_LOGIN_NEXT_DESTINATION_USER_PROFILE = 1000;
    private static final int ACTIVITY_LOGIN_NEXT_DESTINATION_MY_WALLET = 1001;

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID_UNREAD_ACTIVE_NEWS = LoaderUtils.LOADER_ID_HOME_ACTIVITY;
    private static final int LOADER_ID_AMEX_OFFER_COUNT = LoaderUtils.LOADER_ID_HOME_ACTIVITY_2;
    private static final int LOADER_ID_UNREAD_ACTIVE_MYTICKETS = LoaderUtils.LOADER_ID_APPOINMENT_TICKETS_UNREAD;

    private boolean mIsDrawerOpenAtAll;
    private ViewGroup mFragmentContainer;
    private DrawerLayout mDrawerLayout;
    private HomeActionBarDrawerToggle mActionBarDrawerToggle;
    private View mLoadingLayout;
    private DrawerItem mParkNewsDrawerItem;
    private CharSequence mDrawerTitle;
    private CharSequence mCurrentActionBarTitle;
    private ExpandableListView mDrawerExpandableListView;
    private DrawerItemExpandableListAdapter mDrawerItemExpandableListAdapter;
    private LinearLayout mDrawerRootLayout;
    private MenuItem mSearchMenuItem, mAlertsMenuItem;
    private Integer mStartPageType;
    private DrawerItem mAmexOffersDrawerItem;
    private String fragmentTag;
    private TridionConfig mTridionConfig;
    private boolean mIsDrawerShowingLoggedInState;
    private String mLoggedOutEmail;

    private LoginBroadcastReceiver mLoginBroadcastReceiver;

    private LoginBroadcastReceiver.LoginResultCallback mLoginResultCallback = new LoginBroadcastReceiver.LoginResultCallback() {
        @Override
        public void onLoginResult(@LoginBroadcastReceiver.LoginResult int result) {
            hideLoadingView();
            switch (result) {
                case LoginBroadcastReceiver.SUCCESS_LOGOUT:
                    final String msg = mTridionConfig.getSu1().replace("{0}", mLoggedOutEmail);
                    mLoggedOutEmail = null;
                    Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG)
                            .show();
                    Fragment fragment = getSupportFragmentManager().findFragmentById(mFragmentContainer.getId());
                    if (fragment instanceof AccountSecurityFragment) {
                        ((AccountSecurityFragment) fragment).lockOut();
                    }
                    Fragment newFragment = newHomeFragmentInstance();
                    replaceMainFragment(newFragment, true);
                    break;
                case LoginBroadcastReceiver.SUCCESS_REGISTERED:
                    break;
                case LoginBroadcastReceiver.SUCCESS_UNREGISTERED:
                    // Intentionally falls through since an error is treated like logged out
                case LoginBroadcastReceiver.ERROR_ACCOUNT_LOCKED:
                case LoginBroadcastReceiver.ERROR_LOGIN_FAILED:
                case LoginBroadcastReceiver.ERROR_UNKNOWN:
                default:
            }
            checkAndUpdateMenuDrawerItems(false);
        }
    };

    public static Intent newInstanceIntent(Context context, Integer startPageType) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstanceBundle");
        }

        // Create a new bundle and put in the args
        Bundle args = newInstanceBundle(startPageType);

        // Attach the arguments to the intent
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtras(args);
        return intent;
    }

    public static Bundle newInstanceBundle(Integer startPageType) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstanceBundle");
        }

        // Create a new bundle and put in the args
        Bundle args = new Bundle();

        // Add parameters to the argument bundle
        if (startPageType != null) {
            args.putInt(KEY_ARG_START_PAGE_TYPE, startPageType);
        }

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        mTridionConfig = IceTicketUtils.getTridionConfig();

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {
            mStartPageType = null;
        }
        // Otherwise, set incoming parameters
        else {
            mStartPageType = args.getInt(KEY_ARG_START_PAGE_TYPE, -1);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mCurrentActionBarTitle = provideTitle();
            mIsDrawerOpenAtAll = false;
            mIsDrawerShowingLoggedInState = false;
        }
        // Otherwise, restore state
        else {
            mCurrentActionBarTitle = savedInstanceState.getString(KEY_STATE_CUR_ACTION_BAR_TITLE);
            mIsDrawerOpenAtAll = savedInstanceState.getBoolean(KEY_STATE_IS_DRAWER_MENU_OPEN_AT_ALL);
            mIsDrawerShowingLoggedInState = savedInstanceState.getBoolean(KEY_STATE_IS_DRAWER_SHOWING_LOGGED_IN);
        }

        setContentView(R.layout.activity_home);

        mFragmentContainer = (ViewGroup) findViewById(R.id.activity_home_container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer_layout);
        mDrawerRootLayout = (LinearLayout) findViewById(R.id.activity_home_drawer_root);
        mDrawerExpandableListView = (ExpandableListView) findViewById(R.id.activity_home_drawer_list);
        mLoadingLayout = findViewById(R.id.activity_home_loading);

        // Setup action bar
        mDrawerTitle = getResources().getString(R.string.action_title_drawer);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(mCurrentActionBarTitle);
        actionBar.setDisplayShowHomeEnabled(false);

        // Setup menu drawer
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        mActionBarDrawerToggle = new HomeActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawer_open_drawer_content_description, R.string.drawer_close_drawer_content_description);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        // Create an expandable list adapter for the menu drawer
        mDrawerItemExpandableListAdapter = new DrawerItemExpandableListAdapter();
        checkAndUpdateMenuDrawerItems(true);

        mDrawerExpandableListView.setAdapter(mDrawerItemExpandableListAdapter);
        mDrawerExpandableListView.setOnGroupClickListener(this);
        mDrawerExpandableListView.setOnChildClickListener(this);

        // If this is the first creation, add the default fragment
        if (savedInstanceState == null) {
            // Assume the home page
            Fragment startingFragment = newHomeFragmentInstance();
            if (mStartPageType != null) {
                switch (mStartPageType) {
                    case START_PAGE_TYPE_HOME:
                        startingFragment = newHomeFragmentInstance();
                        break;
                    case START_PAGE_TYPE_NEWS_LIST:
                        startingFragment = NewsListFragment.newInstance(
                                R.string.drawer_item_park_news);
                        break;
                    case START_PAGE_TYPE_VIEW_PROFILE:
                        startingFragment = ViewProfileFragment.newInstance();
                        break;
                    default:
                        break;
                }
            }
            replaceMainFragment(startingFragment, false);
        }

        Bundle AppointmentTicketArgs = new Bundle();
        DatabaseQuery appointmentDatabaseQuery = DatabaseQueryUtils.getUnReadAppointmentTicketsDatabaseQuery();
        AppointmentTicketArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, appointmentDatabaseQuery.toJson());
        getSupportLoaderManager().initLoader(LOADER_ID_UNREAD_ACTIVE_MYTICKETS, AppointmentTicketArgs, this);

        Bundle amexLoaderArgs = new Bundle();
        DatabaseQuery amexDatabaseQuery = DatabaseQueryUtils.getOfferCount(OffersTable.VAL_VENDOR_AMEX);
        amexLoaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, amexDatabaseQuery.toJson());
        getSupportLoaderManager().initLoader(LOADER_ID_AMEX_OFFER_COUNT, amexLoaderArgs, this);

        // Create loader to track the # of unread park news items
        Bundle loaderArgs = new Bundle();
        DatabaseQuery databaseQuery = DatabaseQueryUtils.getAllActiveUnreadNewsDatabaseQuery();
        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        getSupportLoaderManager().initLoader(LOADER_ID_UNREAD_ACTIVE_NEWS, loaderArgs, this);


    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPostCreate");
        }

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mActionBarDrawerToggle.syncState();
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

        registerLoginBroadcastReceiver();
        boolean hasGuestId = false;
        boolean hasWcTokens = false;

        // Check if commerce is enabled before refreshing tokens. If not, sign the user out.
        if (CommerceStateManager.isAppValidForCommerce(false, true)) {
            if (!TextUtils.isEmpty(AccountStateManager.getGuestId())) {
                hasGuestId = true;
            }
            if (!TextUtils.isEmpty(AccountStateManager.getWcToken()) && !TextUtils.isEmpty(AccountStateManager.getWcTrustedToken())) {
                hasWcTokens = true;
            }
            if (!hasGuestId || !hasWcTokens) {
                showLoadingView();

                if (AccountStateManager.isUserLoggedIn()) {
                    // Only re-login if there is no guest ID
                    String username = AccountStateManager.getUsername();
                    String password = AccountStateManager.getPassword();
                    AccountLoginService.startActionLoginWithUsernamePassword(this, username, password);
                } else {
                    String guestId = AccountStateManager.getGuestId();
                    // The service will get a new guest ID for an unregistered user if there isn't one
                    // so calling this works whether the guest ID is set or not.
                    AccountLoginService.startActionLoginWithGuestId(this, guestId);
                }
            }
        }

        // Update menu drawer in case login state has changed
        checkAndUpdateMenuDrawerItems(false);
    }

    private void registerLoginBroadcastReceiver() {
        if (null == mLoginBroadcastReceiver) {
            mLoginBroadcastReceiver = new LoginBroadcastReceiver(mLoginResultCallback);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mLoginBroadcastReceiver, new LoginResultIntentFilter()
            );
        }
    }

    @Override
    public void onPause() {
        unregisterLoginBroadcastReceiver();
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onPause");
        }
    }

    private void unregisterLoginBroadcastReceiver() {
        if (null != mLoginBroadcastReceiver) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLoginBroadcastReceiver);
            mLoginBroadcastReceiver = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        outState.putString(KEY_STATE_CUR_ACTION_BAR_TITLE, mCurrentActionBarTitle.toString());
        outState.putBoolean(KEY_STATE_IS_DRAWER_MENU_OPEN_AT_ALL, mIsDrawerOpenAtAll);
        outState.putBoolean(KEY_STATE_IS_DRAWER_SHOWING_LOGGED_IN, mIsDrawerShowingLoggedInState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onConfigurationChanged");
        }

        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        // Adds items to the action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_search, menu);
        menuInflater.inflate(R.menu.action_alerts, menu);

        mSearchMenuItem = menu.findItem(R.id.action_search);
        mAlertsMenuItem = menu.findItem(R.id.action_alerts);

        TintUtils.tintAllMenuItems(menu, this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPrepareOptionsMenu");
        }

        // If the menu drawer is open, hide action items related to the main
        // content view, and show action items specific to the menu
        boolean isDrawerOpenAtAll = isDrawerOpenAtAll();

        // Update the search action view based on the drawer
        mSearchMenuItem.setVisible(isDrawerOpenAtAll).setEnabled(isDrawerOpenAtAll);
        mAlertsMenuItem.setVisible(isDrawerOpenAtAll).setEnabled(isDrawerOpenAtAll);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onOptionsItemSelected");
        }

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle other action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.action_alerts:
                startActivity(new Intent(this, AlertsListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onGroupClick: groupPosition = " + groupPosition + " id = " + id);
        }

        Object object = parent.getExpandableListAdapter().getGroup(groupPosition);
        if (object != null && object instanceof DrawerItem) {
            DrawerItem drawerItem = (DrawerItem) object;
            return handleDrawerClick(drawerItem, true);
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onChildClick: groupPosition = " + groupPosition + " childPosition = " + childPosition + " id = " + id);
        }

        Object object = parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
        if (object != null && object instanceof DrawerItem) {
            DrawerItem drawerItem = (DrawerItem) object;
            return handleDrawerClick(drawerItem, true);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        // Toggle the drawer when the menu button is pressed
        // Remove this if there is ever an overflow actionbar menu item (right now there isn't)
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (isDrawerOpenAtAll()) {
                closeDrawer();
            } else {
                openDrawer();
            }
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    @Override
    public String provideTitle() {
        return getString(R.string.action_title_home);
    }

    @Override
    public boolean isDrawerOpenAtAll() {
        return mIsDrawerOpenAtAll;
    }

    @Override
    public void onBackPressed() {
        // If the activity is finishing, ignore the back press
        if (isFinishing()) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(mFragmentContainer.getId());

        int count = getSupportFragmentManager().getBackStackEntryCount();

        // If the drawer is open, close it
        if (mIsDrawerOpenAtAll) {
            mDrawerLayout.closeDrawer(mDrawerRootLayout);
        } else if (count != 0) {
            super.onBackPressed();
        }
        // If back is being pressed on any fragment other than the home
        // fragment, load the home fragment before exiting the app
        else if (fragment != null && fragment.isResumed() &&
                !(fragment instanceof HomeFragment) &&
                !(fragment instanceof HollywoodHomeFragment)) {
            Fragment homeFragment = newHomeFragmentInstance();
            replaceMainFragment(homeFragment, false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean handleDrawerClick(DrawerItem drawerItem, boolean closeDrawer) {
        if (drawerItem == null) {
            return false;
        }

        UniversalAppState uoState = UniversalAppStateManager.getInstance();
        Integer titleStringResId = drawerItem.getTitleStringResId();
        Fragment newFragment = null;

        // Don't handle clicks for groups that have children items, just let them expand/collapse
        //The only exception is the buy button
        List<DrawerItem> childDrawerItemList = drawerItem.getChildDrawerItemList();
        if (childDrawerItemList != null && childDrawerItemList.size() > 0) {
            if (titleStringResId != null && titleStringResId.equals(R.string.drawer_header_buy)) {
                sendBuyClickAnalytics();
            }
            return false;
        }

        if (null != drawerItem.tag && drawerItem.tag instanceof EventSeries) {
            Bundle b = EventSeriesDetailActivity.newInstanceBundle(((EventSeries) drawerItem.tag).toJson());
            Intent i = new Intent(this, EventSeriesDetailActivity.class);
            i.putExtras(b);
            startActivity(i);
        } else if (titleStringResId != null) {
            switch (titleStringResId) {
                case R.string.drawer_item_home:
                    newFragment = newHomeFragmentInstance();
                    break;
                // Explore by venue
                case R.string.drawer_item_islands_of_adventure:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.ISLANDS_OF_ADVENTURE);
                    break;
                case R.string.drawer_item_universal_studios_florida:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.UNIVERSAL_STUDIOS_FLORIDA);
                    break;
                case R.string.drawer_item_volcano_bay:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.VOLCANO_BAY);
                    break;
                case R.string.drawer_item_citywalk_orlando:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.CITY_WALK_ORLANDO);
                    break;
                case R.string.drawer_item_universal_studios_hollywood:
                    // Get the park venues from the parent activity
                    List<Venue> parkVenues = getParkVenues();
                    Venue ushVenue = null;
                    for (Venue venue : parkVenues) {
                        if (venue != null) {
                            Long venueId = venue.getId();
                            if (venueId != null && venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
                                ushVenue = venue;
                            }
                        }
                    }
                    // If USH is open, sort attractions by wait time
                    FilterOptions ushFilterOptions = null;
                    boolean isDuringUshVenueHours = ushVenue != null && PoiUtils.isDuringVenueHours(System.currentTimeMillis(), ushVenue.getHours());
                    if (isDuringUshVenueHours) {
                        ushFilterOptions = new FilterOptions(null, ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS);
                        ushFilterOptions.setFilterSort(FilterSort.WAIT_TIMES);
                    }
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS,
                            ushFilterOptions);
                    break;
                case R.string.drawer_item_citywalk_hollywood:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.CITY_WALK_HOLLYWOOD);
                    break;
                // Explore by activity
                case R.string.drawer_item_rides:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.RIDES);
                    break;
                case R.string.drawer_item_wait_times:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.WAIT_TIMES);
                    break;
                case R.string.drawer_item_shows:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.SHOWS);
                    break;
                case R.string.drawer_item_dining:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.DINING);
                    break;
                case R.string.drawer_item_shopping:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.SHOPPING);
                    break;
                case R.string.drawer_item_restrooms:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.RESTROOMS);
                    break;
                case R.string.drawer_item_lockers:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.LOCKERS);
                    break;
                case R.string.drawer_item_rental_services:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.RENTAL_SERVICES);
                    break;
                case R.string.drawer_item_guest_services_locations:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.GUEST_SERVICES_BOOTHS);
                    break;
                case R.string.drawer_item_atms:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.ATMS);
                    break;
                case R.string.drawer_item_charging_stations:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.CHARGING_STATIONS);
                    break;
                case R.string.drawer_item_lost_and_found:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.LOST_AND_FOUND);
                    break;
                case R.string.drawer_item_first_aid:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.FIRST_AID);
                    break;
                case R.string.drawer_item_service_animal_rest_areas:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.SERVICE_ANIMAL_REST_AREAS);
                    break;
                case R.string.drawer_item_smoking_areas:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.SMOKING_AREAS);
                    break;
                case R.string.drawer_item_contact_guest_services:
                    startActivity(new Intent(this, GuestServicesActivity.class));
                    break;
                case R.string.drawer_item_hotels:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.HOTELS);
                    break;
                case R.string.drawer_item_hours_and_directions:
                    newFragment = HoursAndDirectionsFragment.newInstance(
                            titleStringResId);
                    break;
                case R.string.drawer_item_settings_and_about:
                    newFragment = SettingsAndAboutFragment.newInstance(
                            titleStringResId);
                    break;
                case R.string.drawer_item_privacy_and_legal:
                    newFragment = PrivacyAndLegalFragment.newInstance(
                            titleStringResId);
                    break;

                case R.string.drawer_item_my_appointment_tickets:
                    newFragment = MyAppointmentTicketsFragment.newInstance(
                            titleStringResId);
                    break;

                case R.string.drawer_item_park_news:
                    newFragment = NewsListFragment.newInstance(
                            titleStringResId);
                    break;
                case R.string.drawer_item_help:
                    newFragment = HelpFragment.newInstance(
                            titleStringResId);
                    break;
                case R.string.drawer_item_favorites:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.FAVORITES);
                    break;
                case R.string.drawer_item_parking_reminder:
                    ParkingState parkingState = ParkingStateManager.getInstance();
                    newFragment = ParkingReminderFragment.newInstance(
                            titleStringResId, parkingState.getSection());
                    break;
                case R.string.drawer_item_web_tickets:
                    newFragment = BuyTicketsWebViewFragment.newInstance(R.string.drawer_item_buy_tickets,
                            uoState.getTicketWebStoreUrl());
                    break;
                case R.string.drawer_item_wet_n_wild:
                    newFragment = ExploreFragment.newInstance(
                            titleStringResId,
                            ExploreType.WET_N_WILD);
                    break;
                case R.string.drawer_item_events:
                    newFragment = EventSeriesFragment.newInstance(titleStringResId,
                            UniversalAppStateManager.getInstance().isInResortGeofence());
                    break;
                case R.string.drawer_item_amex_special_offers:
                    DatabaseQuery amexDatabaseQuery = DatabaseQueryUtils.getOfferSeries(OffersTable.VAL_VENDOR_AMEX);
                    newFragment = OfferSeriesDetailFragment.newInstance(amexDatabaseQuery);
                    break;
                case R.string.drawer_item_view_profile:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        newFragment = ViewProfileFragment.newInstance();
                    }
                    break;
                case R.string.drawer_item_log_out:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        showLoadingView();
                        mLoggedOutEmail = AccountStateManager.getUsername();
                        AccountLoginService.startActionLogout(this);
                    }
                    break;
                case R.string.drawer_item_wallet:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        if (AccountStateManager.isUserLoggedIn()) {
                            closeDrawer();
                            startActivity(WalletFolioActivity.newInstanceIntent(this));
                        } else {
                            closeDrawer();
                            startActivityForResult(AccountLoginActivity.newInstanceIntent(this),
                                    ACTIVITY_LOGIN_NEXT_DESTINATION_MY_WALLET);
                        }
                    }
                    break;
                case R.string.drawer_item_login:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        closeDrawer();
                        startActivityForResult(AccountLoginActivity.newInstanceIntent(this),
                                ACTIVITY_LOGIN_NEXT_DESTINATION_USER_PROFILE);
                    }
                    break;
                case R.string.drawer_item_hh:
                    // Hollywood uses a generic web view for tickets
                    if (BuildConfigUtils.isLocationFlavorHollywood()) {
                        String ticketsUrl = uoState.getTicketWebStoreUrl();

                        if (!TextUtils.isEmpty(ticketsUrl)) {
                            // Track the page view
                            AnalyticsUtils.trackPageView(
                                    AnalyticsUtils.CONTENT_GROUP_TICKETS,
                                    null, null,
                                    AnalyticsUtils.CONTENT_SUB_2_HOME,
                                    AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
                                    null, null);

                            Intent intent = new Intent(this, WebViewActivity.class);
                            Bundle bundle = WebViewActivity.newInstanceBundle(
                                    R.string.drawer_item_buy_tickets, ticketsUrl, true);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            if (closeDrawer) {
                                closeDrawer();
                            }
                        }
                    }
                    // Orlando has native commerce
                    else {
                        if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                            CommerceUiBuilder.getCurrentFilter().resetAllFilters();
                            startActivity(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKETS, true));
                            if (closeDrawer) {
                                closeDrawer();
                            }
                        }
                    }
                    break;
                case R.string.drawer_item_buy_tickets:
                    //Intentional dropthrough
                case R.string.drawer_item_tickets:
                    // Hollywood uses a generic web view for tickets
                    if (BuildConfigUtils.isLocationFlavorHollywood()) {
                        String ticketsUrl = uoState.getTicketWebStoreUrl();

                        if (!TextUtils.isEmpty(ticketsUrl)) {
                            // Track the page view
                            AnalyticsUtils.trackPageView(
                                    AnalyticsUtils.CONTENT_GROUP_TICKETS,
                                    null, null,
                                    AnalyticsUtils.CONTENT_SUB_2_HOME,
                                    AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
                                    null, null);

                            Intent intent = new Intent(this, WebViewActivity.class);
                            Bundle bundle = WebViewActivity.newInstanceBundle(
                                    R.string.drawer_item_buy_tickets, ticketsUrl, true);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            if (closeDrawer) {
                                closeDrawer();
                            }
                        }
                    }
                    // Orlando has native commerce
                    else {
                        if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                            CommerceUiBuilder.getCurrentFilter().resetAllFilters();
                            startActivity(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKETS));
                            if (closeDrawer) {
                                closeDrawer();
                            }
                        }
                    }
                    break;
                case R.string.drawer_item_express_pass:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        // Create a synthetic stack since this is a deep link into the shopping process
                        CommerceUiBuilder.getCurrentFilter().resetAllFilters();
                        TaskStackBuilder expressPassStackBuilder = TaskStackBuilder.create(this)
                                .addNextIntent(HomeActivity.newInstanceIntent(this, START_PAGE_TYPE_HOME))
                                .addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKETS))
                                .addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_EXPRESS_PASS));
                        expressPassStackBuilder.startActivities();
                    }
                    break;
                case R.string.drawer_item_extras:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        // Create a synthetic stack since this is a deep link into the shopping process
                        CommerceUiBuilder.getCurrentFilter().resetAllFilters();
                        TaskStackBuilder extrasStackBuilder = TaskStackBuilder.create(this)
                                .addNextIntent(HomeActivity.newInstanceIntent(this, START_PAGE_TYPE_HOME))
                                .addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKETS))
                                .addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_EXPRESS_PASS))
                                .addNextIntent(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_ADDONS));
                        extrasStackBuilder.startActivities();
                    }
                    break;
                case R.string.drawer_item_ticket_bmg_bundle:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        CommerceUiBuilder.getCurrentFilter().resetAllFilters();
                        startActivity(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKET_BMG_BUNDLE));
                        if (closeDrawer) {
                            closeDrawer();
                        }
                    }
                    break;
                case R.string.drawer_item_ticket_uep_bundle:
                    if (CommerceStateManager.isAppValidForCommerce(true, false)) {
                        CommerceUiBuilder.getCurrentFilter().resetAllFilters();
                        startActivity(ShoppingActivity.newInstanceIntent(this, ShoppingActivity.SELECT_TICKET_UEP_BUNDLE));
                        if (closeDrawer) {
                            closeDrawer();
                        }
                    }
                    break;
                case R.string.drawer_item_wifi:
                    newFragment = AboutWifiFragment.newInstance(R.string.about_wifi_title);
                    break;
                case R.string.drawer_item_contact_us:
                    newFragment = GuestServicesFragment.newInstance(titleStringResId);
                    break;
                case R.string.drawer_item_faqs:
                    if (BuildConfigUtils.isLocationFlavorHollywood()) {
                        UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
                        String faqUrl = universalAppState.getFaqPageUrl();
                        if (!TextUtils.isEmpty(faqUrl)) {
                            newFragment = WebViewFragment.newInstance(
                                    R.string.title_faq, faqUrl);
                        }
                    } else {
                        newFragment = HelpFragment.newInstance(
                                titleStringResId);
                    }
                    break;
                case R.string.drawer_item_settings:
                    newFragment = SettingsAndAboutFragment.newInstance(
                            titleStringResId);
                    break;
                default:
                    Toast.makeText(this, "In Development", Toast.LENGTH_SHORT)
                            .show();
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "handleDrawerClick: Unknown drawerItem clicked - titleStringResId = " + titleStringResId);
                    }
                    break;
            }
            if (newFragment != null) {
                replaceMainFragment(newFragment, closeDrawer);
                return true;
            }
        }
        // Handle hidden dev options
        else if (BuildConfig.DEBUG_DRAWER_ENABLED) {
            String title = drawerItem.getTitle();
            if (title != null && title.equals(DebugOptionsActivity.TITLE)) {
                if (closeDrawer) {
                    closeDrawer();
                }
                startActivity(new Intent(this, DebugOptionsActivity.class));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_LOGIN_NEXT_DESTINATION_MY_WALLET:
                if (resultCode == RESULT_OK) {
                    startActivity(WalletFolioActivity.newInstanceIntent(this));
                }
                break;
            case ACTIVITY_LOGIN_NEXT_DESTINATION_USER_PROFILE:
                if (resultCode == RESULT_OK) {
                    replaceMainFragment(ViewProfileFragment.newInstance(), true);
                }
                break;
            default:
                Log.w(TAG, "Activity received unexpected result " +
                        "from finished activity");
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader: id = " + id);
        }

        switch (id) {
            case LOADER_ID_AMEX_OFFER_COUNT:
            case LOADER_ID_UNREAD_ACTIVE_NEWS:
            case LOADER_ID_UNREAD_ACTIVE_MYTICKETS:
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
            Log.d(TAG, "onLoadFinished: loader.getId() = " + loader.getId());
        }

        switch (loader.getId()) {
            case LOADER_ID_UNREAD_ACTIVE_NEWS:
                // Set the badge to the number of unread park news items
                if (mParkNewsDrawerItem != null) {
                    int unreadNewsItemCount = data != null ? data.getCount() : 0;
                    mParkNewsDrawerItem.setBadgeNumber(unreadNewsItemCount > 0 ? unreadNewsItemCount : null);
                    mDrawerItemExpandableListAdapter.notifyDataSetChanged();
                }
                break;
            case LOADER_ID_AMEX_OFFER_COUNT:
                // Hide the AMEX drawer menu item if there are no offers
                if (data != null && data.moveToFirst()) {
                    int count = data.getInt(data.getColumnIndex(BaseColumns._COUNT));
                    if (count > 0) {
                        // Show drawer item
                        if (mAmexOffersDrawerItem != null) {
                            mAmexOffersDrawerItem.setVisible(true);
                        }
                    } else {
                        // Hide drawer item
                        if (mAmexOffersDrawerItem != null) {
                            mAmexOffersDrawerItem.setVisible(false);
                        }
                    }
                } else {
                    // Hide drawer item
                    if (mAmexOffersDrawerItem != null) {
                        mAmexOffersDrawerItem.setVisible(false);
                    }
                }
                break;
            default:
                // Otherwise, let the parent class handle it
                super.onLoadFinished(loader, data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }

        switch (loader.getId()) {
            case LOADER_ID_UNREAD_ACTIVE_NEWS:
                // Data is not available anymore, delete reference
                break;
            case LOADER_ID_UNREAD_ACTIVE_MYTICKETS:
                break;
            case LOADER_ID_DATABASE_QUERY:
                // Data is not available anymore, delete reference
                break;
            default:
                // Otherwise, let the parent class handle it
                super.onLoaderReset(loader);
                break;
        }
    }

    private void closeKeyboard() {
        UserInterfaceUtils.closeKeyboard(mFragmentContainer);
    }

    private String getCurrentActionBarTitle() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(mFragmentContainer.getId());

        String currentActionBarTitle;
        if (fragment != null && fragment instanceof ActionBarTitleProvider) {
            currentActionBarTitle = ((ActionBarTitleProvider) fragment).provideTitle();
        } else {
            currentActionBarTitle = (String) mCurrentActionBarTitle;
        }

        return currentActionBarTitle;
    }

    private void addEventToNav(EventSeries event, DrawerItemExpandableListAdapter adapter) {
        adapter.addDrawerObject(new DrawerItem(event.getDisplayName(), event.getNavigationIconLink(), this, event, adapter));
    }

    private void checkAndUpdateMenuDrawerItems(boolean forceUpdate) {
        // If account state is the same as the drawer is showing, nothing to update
        boolean isUserLoggedIn = AccountStateManager.isUserLoggedIn();
        if (!forceUpdate && isUserLoggedIn == mIsDrawerShowingLoggedInState) {
            return;
        } else {
            mIsDrawerShowingLoggedInState = isUserLoggedIn;
        }

        // Clear the drawer, and get any events that need to show in the drawer
        mDrawerItemExpandableListAdapter.clearAll();
        List<EventSeries> eventsToShow = EventUtils.getEventsForNavigation();

        // Hollywood nav drawer
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            // Main section
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_home, R.drawable.ic_menu_home));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_buy_tickets, R.drawable.ic_menu_buytickets));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_favorites, R.drawable.ic_menu_recommended));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_parking_reminder, R.drawable.ic_menu_parking_reminder));

            // Parks & Events
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_explore_parks_and_events));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_universal_studios_hollywood, R.drawable.ic_menu_ush_logo));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_citywalk_hollywood, R.drawable.ic_menu_citywalk_hollywood));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_events, R.drawable.ic_menu_events));
            mParkNewsDrawerItem = new DrawerItem(R.string.drawer_item_park_news, R.drawable.ic_menu_park_updates);
            mDrawerItemExpandableListAdapter.addDrawerObject(mParkNewsDrawerItem);

            // Guest Amenities
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_guest_amenities));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_restrooms, R.drawable.ic_menu_restrooms));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_lockers, R.drawable.ic_menu_lockers));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_rental_services, R.drawable.ic_menu_stroller));

            // Guest Services
            DrawerItem guestServicesDrawerItem = new DrawerItem(R.string.drawer_item_guest_services, R.drawable.ic_menu_guest_services);
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_guest_services_locations, R.drawable.ic_menu_guest_services_booths));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_atms, R.drawable.ic_menu_credit_card));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_lost_and_found, R.drawable.ic_menu_lost_and_found));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_service_animal_rest_areas, R.drawable.ic_menu_service_animal));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_first_aid, R.drawable.ic_menu_first_aid));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_smoking_areas, R.drawable.ic_menu_smoking));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_wifi, R.drawable.ic_network_wifi));
            mDrawerItemExpandableListAdapter.addDrawerObject(guestServicesDrawerItem);

            // Help
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_help_and_support));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_contact_us, R.drawable.ic_menu_contact_us));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_hours_and_directions, R.drawable.ic_menu_hours_and_directions));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_faqs, R.drawable.ic_menu_faq));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_settings, R.drawable.ic_menu_settings));
        }
        // Orlando nav drawer
        else {
            // Main section
            if (mIsDrawerShowingLoggedInState) {
                mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_view_profile, R.drawable.ic_menu_account));
            } else {
                mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_login, R.drawable.ic_menu_account));
            }
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_home, R.drawable.ic_menu_home));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_wallet, R.drawable.ic_menu_mytickets));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_favorites, R.drawable.ic_menu_favorites));
            mParkNewsDrawerItem = new DrawerItem(R.string.drawer_item_park_news, R.drawable.ic_menu_park_news);
            mDrawerItemExpandableListAdapter.addDrawerObject(mParkNewsDrawerItem);
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_parking_reminder, R.drawable.ic_menu_parking_reminder));

            if (null != eventsToShow) {
                for (EventSeries event : eventsToShow) {
                    addEventToNav(event, mDrawerItemExpandableListAdapter);
                }
            }

            // Shop
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_shop));
            DrawerItem buyDrawerItem = new DrawerItem(R.string.drawer_header_buy, R.drawable.ic_menu_buytickets);
            buyDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_tickets, R.drawable.ic_menu_tickets));
            buyDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_hh, R.drawable.ic_menu_tickets));
            buyDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_extras, R.drawable.ic_wallet_extras));

            buyDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_ticket_bmg_bundle, R.drawable.ic_menu_express_pass));
            buyDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_ticket_uep_bundle, R.drawable.ic_menu_express_pass));

            mDrawerItemExpandableListAdapter.addDrawerObject(buyDrawerItem);

            // Explore parks
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_explore_parks_and_hotels));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_islands_of_adventure, R.drawable.ic_menu_islands_of_adventure));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_universal_studios_florida, R.drawable.ic_menu_universal_studios_florida));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_volcano_bay, R.drawable.menu_item_volcano_bay));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_citywalk_orlando, R.drawable.ic_menu_citywalk));
            if (WnWKillSwitchDateUtil.shouldDisplayContent()) {
                mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_wet_n_wild, R.drawable.ic_menu_wet_n_wild));
            }
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_hotels, R.drawable.ic_menu_hotels));

            // Explore activities
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_explore_all_activities));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_rides, R.drawable.ic_menu_rides));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_shows, R.drawable.ic_menu_shows));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_dining, R.drawable.ic_menu_dining));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_events, R.drawable.ic_menu_events));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_shopping, R.drawable.ic_menu_shopping));

            // Guest amenities
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader(R.string.drawer_header_guest_amenities));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_restrooms, R.drawable.ic_menu_restrooms));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_lockers, R.drawable.ic_menu_lockers));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_rental_services, R.drawable.ic_menu_rentals));

            DrawerItem guestServicesDrawerItem = new DrawerItem(R.string.drawer_item_guest_services, R.drawable.ic_menu_guest_services);
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_guest_services_locations, R.drawable.ic_menu_guest_services_booths));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_atms, R.drawable.ic_menu_credit_card));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_lost_and_found, R.drawable.ic_menu_lost_and_found));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_service_animal_rest_areas, R.drawable.ic_menu_service_animal));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_first_aid, R.drawable.ic_menu_first_aid));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_smoking_areas, R.drawable.ic_menu_smoking));
            guestServicesDrawerItem.addChildDrawerItem(new DrawerItem(R.string.drawer_item_contact_guest_services, R.drawable.ic_detail_feature_phone));
            mDrawerItemExpandableListAdapter.addDrawerObject(guestServicesDrawerItem);

            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_hours_and_directions, R.drawable.ic_menu_hours_and_directions));
            mAmexOffersDrawerItem = new DrawerItem(R.string.drawer_item_amex_special_offers, R.drawable.ic_menu_credit_card);
            mDrawerItemExpandableListAdapter.addDrawerObject(mAmexOffersDrawerItem);
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_help, R.drawable.ic_menu_faq));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_settings_and_about, R.drawable.ic_menu_settings));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_privacy_and_legal, R.drawable.ic_menu_legal));

            if (mIsDrawerShowingLoggedInState) {
                mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(R.string.drawer_item_log_out, BLANK_DRAWABLE_RESOURCE_ID));
            }
        }

        // Hidden dev menu
        if (BuildConfig.DEBUG_DRAWER_ENABLED) {
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerSectionHeader("DEVELOPER CONTROLS"));
            mDrawerItemExpandableListAdapter.addDrawerObject(new DrawerItem(DebugOptionsActivity.TITLE, R.drawable.ic_menu_settings));
        }
    }

    // TODO THIS NEEDS TO BE MADE PRIVATE TO STOP ALL THE HACKS TRYING TO CONTROL NAVIGATION (STB)
    @Override
    public String replaceMainFragment(Fragment fragment, boolean closeDrawer) {
        return replaceMainFragment(fragment, closeDrawer, false);
    }

    public String replaceMainFragment(Fragment fragment, boolean closeDrawer, boolean clearBackStack) {
        // Add the new fragment to the back stack and replace the current main fragment
        fragmentTag = fragment.getClass().getName() + " " + System.currentTimeMillis();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearBackStack) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(mFragmentContainer.getId(), fragment, fragmentTag);

        if (closeDrawer) {
            // If the drawer needs to close, then listen for it to finish closing first,
            // THEN commit to the fragment change
            mDrawerLayout.addDrawerListener(new DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    // Once the drawer has finished closing, commit the fragment transaction
                    fragmentTransaction.commit();
                    mDrawerLayout.removeDrawerListener(this);
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                }
            });
            closeDrawer();
        } else {
            fragmentTransaction.commit();
        }

        return fragmentTag;
    }

    private void closeDrawer() {
        if (mIsDrawerOpenAtAll) {
            mDrawerLayout.closeDrawer(mDrawerRootLayout);
        }
    }

    private void openDrawer() {
        if (!mIsDrawerOpenAtAll) {
            mDrawerLayout.openDrawer(mDrawerRootLayout);
        }
    }

    private static Fragment newHomeFragmentInstance() {
        // Hollywood home fragment
        if (BuildConfigUtils.isLocationFlavorHollywood()) {
            return HollywoodHomeFragment.newInstance();
        }
        // Orlando
        else {
            return HomeFragment.newInstance();
        }
    }

    private class HomeActionBarDrawerToggle extends ActionBarDrawerToggle {

        public HomeActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes,
                                         int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);

            boolean wasDrawerOpenAtAll = mIsDrawerOpenAtAll;
            mIsDrawerOpenAtAll = slideOffset > 0;

            if (wasDrawerOpenAtAll != mIsDrawerOpenAtAll) {
                // Drawer has just started to open
                if (mIsDrawerOpenAtAll) {
                    mCurrentActionBarTitle = mDrawerTitle;
                    invalidateOptionsMenu();
                    closeKeyboard();
                }
                // Drawer is completely closed
                else {
                    mCurrentActionBarTitle = getCurrentActionBarTitle();
                    invalidateOptionsMenu();
                }
                getActionBar().setTitle(mCurrentActionBarTitle);
            }
        }
    }

    private void showLoadingView() {
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void sendBuyClickAnalytics() {

        AnalyticsUtils.trackTicketsPageView(
                AnalyticsUtils.CONTENT_SUB_2_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_TICKETS,
                AnalyticsUtils.CONTENT_SUB_2_TICKETS,
                AnalyticsUtils.CONTENT_GROUP_SITE_TICKETS,
                AnalyticsUtils.PROPERTY_NAME_PRODUCT_SELECTION,
                AnalyticsUtils.PROPERTY_NAME_SHOPPING,
                null,
                null,
                "event2, event5, event38",
                false,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
