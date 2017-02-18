
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventActivitiesActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wayfinding.WayfindingUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.util.List;

/**
 * @author Steven Byle
 */
public class DetailActivity extends DatabaseQueryActivity implements DatabaseQueryProvider, OnClickListener, OnFeatureListStateChangeListener, OnUniversalAppStateChangeListener {
	private static final String TAG = DetailActivity.class.getSimpleName();

	private static final String KEY_ARG_VENUE_OBJECT_JSON = "KEY_ARG_VENUE_OBJECT_JSON";
	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_SELECTED_POI_TYPE_ID = "KEY_ARG_SELECTED_POI_TYPE_ID";
	private static final String KEY_ARG_DETAIL_TYPE = "KEY_ARG_DETAIL_TYPE";
	private static final String KEY_ARG_CUSTOM_MAP_TILE_ID = "KEY_MAP_TILE";

	private DetailType mDetailType;
	private String mSelectedPoiObjectJson;
	private int mSelectedPoiTypeId;
	private String mVenueObjectJson;
	private String mSiteUrl, mSocialSharingText;
	private String mPoiName;
	private DatabaseQuery mVenueDiningListDatabaseQuery;
	private ViewGroup mImageAndTitleFragmentContainer;
	private ViewGroup mExploreMapParentContainer;
	private ViewGroup mExploreMapFragmentContainer;
	private View mExploreMapWrapperContainer;
	private ViewGroup mFeatureListFragmentContainer;
	private ViewGroup mFeatureListSecondaryFragmentContainer;
	private ViewGroup mAccessibilityOptionsFragmentContainer;
	private ViewGroup mShowTimeListFragmentContainer;
	private ViewGroup mHoursFragmentContainer;
	private ViewGroup mPerksFragmentContainer;
	private ViewGroup mAddressFragmentContainer;
	private ViewGroup mLockerPricingFragmentContainer;
	private ViewGroup mVenuePoiListFragmentContainer;
	private ViewGroup mRentalPricingFragmentContainer;
	private MenuItem mShareMenuItem;
	private MenuItem mGuideMeMenuItem;
	private boolean mIsRoutable;
	private String mPoiObjectJson;
	private Integer mPoiTypeId;
	private TextView mEventActivitesText;

	private Long mMapTileId = null;

	public static Bundle newInstanceBundle(DetailType detailType, String venueObjectJson, String selectedPoiObjectJson, int selectedPoiTypeId, Long mapTileId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		args.putSerializable(KEY_ARG_DETAIL_TYPE, detailType);
		args.putString(KEY_ARG_VENUE_OBJECT_JSON, venueObjectJson);
		args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		args.putInt(KEY_ARG_SELECTED_POI_TYPE_ID, selectedPoiTypeId);
		args.putSerializable(KEY_ARG_CUSTOM_MAP_TILE_ID, mapTileId);

		PointOfInterest poi = PointOfInterest.fromJson(selectedPoiObjectJson, selectedPoiTypeId);
		String detailDatabaseQueryJson = DatabaseQueryUtils.getDetailDatabaseQuery(poi.getId()).toJson();
		args.putString(KEY_ARG_DATABASE_QUERY_JSON, detailDatabaseQueryJson);

		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			mDetailType = null;
			mSelectedPoiObjectJson = null;
			mSelectedPoiTypeId = -1;
			mVenueObjectJson = null;
		}
		// Otherwise, set incoming parameters
		else {
			mDetailType = (DetailType) args.getSerializable(KEY_ARG_DETAIL_TYPE);
			mSelectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
			mVenueObjectJson = args.getString(KEY_ARG_VENUE_OBJECT_JSON);
			mSelectedPoiTypeId = args.getInt(KEY_ARG_SELECTED_POI_TYPE_ID);
			mMapTileId = (Long) args.getSerializable(KEY_ARG_CUSTOM_MAP_TILE_ID);
		}


		mIsRoutable = false;
		Venue venue = GsonObject.fromJson(mVenueObjectJson, Venue.class);
		mVenueDiningListDatabaseQuery = DatabaseQueryUtils.getExploreByVenueDatabaseQuery(venue.getId(), null, PointsOfInterestTable.VAL_POI_TYPE_ID_DINING);

		int layoutResId = -1;
		switch(mDetailType) {
			case RIDE:
				layoutResId = R.layout.activity_detail_ride;
				break;
			case SHOW:
				layoutResId = R.layout.activity_detail_show;
				break;
			case DINING:
				layoutResId = R.layout.activity_detail_dining;
				break;
			case HOTEL:
				layoutResId = R.layout.activity_detail_hotel;
				break;
			case ENTERTAINMENT:
				layoutResId = R.layout.activity_detail_entertainment;
				break;
			case PARK:
				layoutResId = R.layout.activity_detail_park;
				break;
			case LOCKER:
				layoutResId = R.layout.activity_detail_locker;
				break;
			case SHOPPING:
				layoutResId = R.layout.activity_detail_shopping;
				break;
			case RENTAL_SERVICES:
				layoutResId = R.layout.activity_detail_rental_services;
				break;
			case EVENT:
			    layoutResId = R.layout.activity_detail_event;
			    break;
			case GATEWAY:
				layoutResId = R.layout.activity_detail_gateway;
				break;
			case GENERAL_LOCATION:
				layoutResId = R.layout.activity_detail_general_location;
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onCreate: invalid detail type");
				}
				break;
		}

		// Set the proper layout depending on the detail type
		setContentView(layoutResId);

		// Get all of the fragment containers
		mImageAndTitleFragmentContainer = (ViewGroup) findViewById(R.id.detail_images_and_title_container);
		mExploreMapParentContainer = (ViewGroup) findViewById(R.id.detail_explore_map_parent_container);
		mExploreMapFragmentContainer = (ViewGroup) findViewById(R.id.detail_explore_map_container);
		mExploreMapWrapperContainer = findViewById(R.id.detail_explore_map_wrapper_container);
		mFeatureListFragmentContainer = (ViewGroup) findViewById(R.id.detail_feature_list_container);
		mFeatureListSecondaryFragmentContainer = (ViewGroup) findViewById(R.id.detail_feature_list_secondary_container);
		mAccessibilityOptionsFragmentContainer = (ViewGroup) findViewById(R.id.detail_accessibility_options_container);
		mShowTimeListFragmentContainer = (ViewGroup) findViewById(R.id.detail_show_time_list_container);
		mHoursFragmentContainer = (ViewGroup) findViewById(R.id.detail_hours_container);
		mPerksFragmentContainer = (ViewGroup) findViewById(R.id.detail_perks_container);
		mAddressFragmentContainer = (ViewGroup) findViewById(R.id.detail_address_container);
		mVenuePoiListFragmentContainer = (ViewGroup) findViewById(R.id.detail_venue_poi_list_container);
		mLockerPricingFragmentContainer = (ViewGroup) findViewById(R.id.detail_locker_pricing_container);
		mRentalPricingFragmentContainer = (ViewGroup) findViewById(R.id.detail_rental_pricing_container);
		mEventActivitesText = (TextView) findViewById(R.id.activity_event_detail_activities_text);
		
		// Properly size the map fragment container
		if (mExploreMapParentContainer != null && mExploreMapFragmentContainer != null && mExploreMapWrapperContainer != null) {

			// Get the smallest (portrait) width in dp
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			float widthDp = displayMetrics.widthPixels / displayMetrics.density;
			float heightDp = displayMetrics.heightPixels / displayMetrics.density;
			float smallestWidthDp = Math.min(widthDp, heightDp);

			// Compute the height based on map aspect ratio 360dp x 217dp
			int calculatedMapHeightDp = (int) Math.round(smallestWidthDp * (217.0 / 360.0));
			int calculatedMapHeightPx = Math.round(TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, calculatedMapHeightDp, displayMetrics));

			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mExploreMapParentContainer.getLayoutParams();
			layoutParams.width = LayoutParams.MATCH_PARENT;
			layoutParams.height = calculatedMapHeightPx;
			mExploreMapParentContainer.setLayoutParams(layoutParams);
			mExploreMapWrapperContainer.setOnClickListener(this);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Track the page view
			PointOfInterest poi = PointOfInterest.fromJson(mSelectedPoiObjectJson, mSelectedPoiTypeId);
			if (poi instanceof Event) {
			    // track event detail page view
                AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                        AnalyticsUtils.CONTENT_FOCUS_EVENTS, poi.getDisplayName(), null, null, null, null);
			}
			else if (poi != null) {
				String propertyName, contentGroup, contentFocus, contentSub1, attractionName;

				if (mDetailType == DetailType.PARK) {
					propertyName = AnalyticsUtils.getPropertyName(venue);
					contentGroup = AnalyticsUtils.CONTENT_GROUP_PLANNING;
					contentFocus = AnalyticsUtils.CONTENT_FOCUS_WNW;
					contentSub1 = null;
					attractionName = poi.getDisplayName();
				}
				else {
					propertyName = AnalyticsUtils.getPropertyName(venue);
					contentGroup = propertyName;
					contentFocus = propertyName;
					contentSub1 = AnalyticsUtils.getPoiTypeIdName(mSelectedPoiTypeId);
					attractionName = poi.getDisplayName();
				}
				AnalyticsUtils.trackPageView(
						contentGroup,
						contentFocus,
						contentSub1,
						"" + attractionName + " " + AnalyticsUtils.CONTENT_SUB_2_DETAIL,
						propertyName,
						attractionName,
						null);
			}

			// Load the image pager and title fragment
			if (mImageAndTitleFragmentContainer != null) {

				ImagesAndTitleDetailFragment fragment = ImagesAndTitleDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mImageAndTitleFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the map snapshot fragment
			if (mExploreMapParentContainer != null && mExploreMapFragmentContainer != null && mExploreMapWrapperContainer != null) {

				ExploreMapFragment fragment = ExploreMapFragment.newInstance(getDatabaseQuery(), ExploreType.DETAIL_PAGE_MAP_SNAPSHOT, mSelectedPoiObjectJson, mMapTileId);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mExploreMapFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the feature list fragment
			if (mFeatureListFragmentContainer != null) {

				FeatureListDetailFragment fragment = FeatureListDetailFragment.newInstance(getDatabaseQuery(), poi.getId(), false);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mFeatureListFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the secondary feature list fragment
			if (mFeatureListSecondaryFragmentContainer != null) {

				FeatureListDetailFragment fragment = FeatureListDetailFragment.newInstance(getDatabaseQuery(), poi.getId(), true);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mFeatureListSecondaryFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the accessibility options fragment
			if (mAccessibilityOptionsFragmentContainer != null) {

				AccessibilityOptionsDetailFragment fragment = AccessibilityOptionsDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mAccessibilityOptionsFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the show time list fragment
			if (mShowTimeListFragmentContainer != null) {
				if (poi != null) {
					ShowTimeListDetailFragment fragment = ShowTimeListDetailFragment.newInstance(getDatabaseQuery(), poi.getId());
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(mShowTimeListFragmentContainer.getId(), fragment, fragment.getClass().getName());

					fragmentTransaction.commit();
				}
			}

			// Load the hours fragment
			if (mHoursFragmentContainer != null) {

				HoursDetailFragment fragment = HoursDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mHoursFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the perks fragment
			if (mPerksFragmentContainer != null) {

				PerksDetailFragment fragment = PerksDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mPerksFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the address fragment
			if (mAddressFragmentContainer != null) {

				AddressDetailFragment fragment = AddressDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mAddressFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load venue POI list fragment
			if (mVenuePoiListFragmentContainer != null) {

				VenuePoiListDetailFragment fragment = VenuePoiListDetailFragment.newInstance(mVenueDiningListDatabaseQuery);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mVenuePoiListFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the locker pricing fragment
			if (mLockerPricingFragmentContainer != null) {

				LockerPricingDetailFragment fragment = LockerPricingDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mLockerPricingFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			// Load the Rental pricing and Terms-Conditions fragment
			if (mRentalPricingFragmentContainer != null  ) {

				LockerPricingDetailFragment fragment = LockerPricingDetailFragment.newInstance(getDatabaseQuery());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mRentalPricingFragmentContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}

			
			// Handle event activities view
            if (mDetailType == DetailType.EVENT && mEventActivitesText != null) {
                if (poi instanceof Event) {
                    Event event = (Event) poi;
                    if (event.getEventActivities() != null && !event.getEventActivities().isEmpty()) {
                        mEventActivitesText.setOnClickListener(this);
                    } else {
                        mEventActivitesText.setVisibility(View.GONE);
                    }
                } else {
                    mEventActivitesText.setVisibility(View.GONE);
                }
            }
		}
		// Otherwise, restore state
		else {
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

		// Update the guide me button
		updateGuideMeMenuItemVisibility();

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
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		// Only add share items for certain detail types
		switch (mDetailType) {
			case DINING:
			case ENTERTAINMENT:
			case RENTAL_SERVICES:
			case HOTEL:
			case RIDE:
			case SHOW:
			case PARK:
			case SHOPPING:
				// Add items to the action bar
				MenuInflater menuInflater = getMenuInflater();
				menuInflater.inflate(R.menu.action_share, menu);
				mShareMenuItem = menu.findItem(R.id.action_share);
				break;
			default:
				break;
		}

		// Add items to the action bar
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.action_guide_me, menu);
		mGuideMeMenuItem = menu.findItem(R.id.action_guide_me);
		TintUtils.tintAllMenuItems(menu, this);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}

		// Only show the share action if there is valid data to share
		if (mShareMenuItem != null) {
			boolean showShareAction;
			if (!hasSocialShareData()) {
				showShareAction = false;
			}
			// The share intent must resolve to at least one app
			else {
				showShareAction = createSocialShareIntent().resolveActivity(getPackageManager()) != null;
			}

			mShareMenuItem.setVisible(showShareAction).setEnabled(showShareAction);
		}

		// Update the guide me button
		updateGuideMeMenuItemVisibility();

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_share:
				// Only send the share intent if there is valid data to share
				if (hasSocialShareData()) {
					Intent shareIntent = createSocialShareIntent();
					String chooserTitleFormatter = getString(R.string.detail_social_sharing_chooser_title);
					String chooserTitleFormatted = String.format(chooserTitleFormatter,
							mPoiName != null ? mPoiName : "");

					startActivity(Intent.createChooser(shareIntent, chooserTitleFormatted));

					// Tag the event
					AnalyticsUtils.trackEvent(
							mPoiName,
							AnalyticsUtils.EVENT_NAME_SOCIAL_SHARE,
							AnalyticsUtils.EVENT_NUM_SOCIAL_SHARE,
							null);
				}
				return true;
			case R.id.action_guide_me:
				// Try to start wayfinding
				boolean didLoadWayfindingPage = WayfindingUtils.openWayfindingPage(this, mPoiObjectJson, mPoiTypeId);
				if (!didLoadWayfindingPage) {
					UserInterfaceUtils.showToastFromForeground(
							getString(R.string.wayfinding_toast_error_trying_to_start_guiding), Toast.LENGTH_LONG, this);
				}
				return true;
			default:
				return (super.onOptionsItemSelected(item));
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		switch (v.getId()) {
			case R.id.detail_explore_map_wrapper_container: {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onClick: map fragment clicked");
				}
				
				PointOfInterest poi = PointOfInterest.fromJson(mSelectedPoiObjectJson, mPoiTypeId);
				if (poi instanceof Event) {
				    // Track map clicked for Event
				    AnalyticsUtils.trackEvent(poi.getDisplayName(), AnalyticsUtils.EVENT_NAME_MAP, -1, null);
				}

				// Open a map page to the selected POI
				Bundle detailMapBundle = MapDetailActivity.newInstanceBundle(getDatabaseQuery(),
						ExploreType.SOLO_MAP_PAGE, mSelectedPoiObjectJson, mMapTileId);
				startActivity(new Intent(this, MapDetailActivity.class).putExtras(detailMapBundle));

				break;
			}
			case R.id.activity_event_detail_activities_text:
			    PointOfInterest poi = PointOfInterest.fromJson(mSelectedPoiObjectJson, mPoiTypeId);
			    if (poi instanceof Event) {
			        Event event = (Event) poi;
			        String title = String.valueOf(getActionBar().getTitle());
			        Bundle args = EventActivitiesActivity.newInstanceBundle(title, event.getEventActivities());
			        startActivity(new Intent(this, EventActivitiesActivity.class).putExtras(args));
			    } else {
			        if (BuildConfig.DEBUG) {
	                    Log.w(TAG, "onClick: POI was not an event");
	                }
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
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					// Refresh the options menu
					invalidateOptionsMenu();
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

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Update UI based on the POI
				if (data != null && data.moveToFirst()) {
					mPoiName = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));
					mPoiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					mPoiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
					mIsRoutable = (data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_IS_ROUTABLE)) != 0);

					// Set the action bar title to the POI name
					getActionBar().setTitle(mPoiName != null ? mPoiName : "");

					// Pull out social sharing data
					PointOfInterest poi = PointOfInterest.fromJson(mPoiObjectJson, mPoiTypeId);
					mSiteUrl = poi.getSiteUrl();
					mSocialSharingText = poi.getSocialSharingText();

					// Refresh the options menu
					invalidateOptionsMenu();
				}
				else {
					mPoiName = null;
					mPoiObjectJson = null;
					mPoiTypeId = null;
					mIsRoutable = false;
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
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				// Otherwise, let the parent class handle it
				super.onLoaderReset(loader);
				break;
		}
	}

	@Override
	public DatabaseQuery provideDatabaseQuery(Object requester) {
		if (requester instanceof VenuePoiListDetailFragment) {
			return mVenueDiningListDatabaseQuery;
		}
		else {
			return getDatabaseQuery();
		}
	}

	@Override
	public void onFeatureListStateChange(boolean isEmpty) {
		// Pass the event down to child Fragments
		List<Fragment> childFragments = getSupportFragmentManager().getFragments();
		if (childFragments != null) {
			for (Fragment fragment : childFragments) {
				if (fragment != null && fragment instanceof OnFeatureListStateChangeListener) {
					((OnFeatureListStateChangeListener) fragment).onFeatureListStateChange(isEmpty);
				}
			}
		}
	}

	private void updateGuideMeMenuItemVisibility() {
		// Only show the share action if there is valid data to share
		if (mGuideMeMenuItem != null) {
			boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
			boolean showGuideMe = isInPark && mIsRoutable;
			mGuideMeMenuItem.setVisible(showGuideMe).setEnabled(showGuideMe);
		}
	}

	private boolean hasSocialShareData() {
		boolean hasSiteUrl = mSiteUrl != null && !mSiteUrl.isEmpty();
		boolean hasSocialSharingText = mSocialSharingText != null && !mSocialSharingText.isEmpty();
		return (hasSiteUrl || hasSocialSharingText);
	}

	private Intent createSocialShareIntent() {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");

		String subjectFormatter = getString(R.string.detail_social_sharing_subject);
		String subjectFormatted = String.format(subjectFormatter,
				mPoiName != null ? mPoiName : getString(R.string.detail_social_sharing_subject_check_this_out));

		String socialSharingText = mSocialSharingText != null ? mSocialSharingText : "";
		String siteUrl = mSiteUrl != null ? mSiteUrl : "";

		// Add social data to the share intent
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, subjectFormatted);
		shareIntent.putExtra(Intent.EXTRA_TEXT, socialSharingText + "\n\n" + siteUrl);

		return shareIntent;
	}
}
