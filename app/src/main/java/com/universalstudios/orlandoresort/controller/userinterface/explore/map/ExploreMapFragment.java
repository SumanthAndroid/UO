package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.crittercism.app.Crittercism;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryMapFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.OnDatabaseQueryChangeListener;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventTimelineActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ClusteredExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreFragment;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.geofence.GeofenceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.managers.AppPreferenceManager;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.UniversalFloatingActionButton;
import com.universalstudios.orlandoresort.controller.userinterface.wayfinding.WayfindingUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.model.state.debug.UniversalOrlandoDebugState;
import com.universalstudios.orlandoresort.model.state.debug.UniversalOrlandoDebugStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.map.MapWrapperLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Steven Byle
 */
public class ExploreMapFragment extends DatabaseQueryMapFragment implements OnCameraChangeListener, OnDatabaseQueryChangeListener,
OnMarkerClickListener, OnInfoWindowClickListener, OnGlobalLayoutListener, OnMapClickListener, OnInfoWindowChildViewClickListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnClickListener, OnUniversalAppStateChangeListener, PoiSelector {
	private static final String TAG = ExploreMapFragment.class.getSimpleName();

	private static final String KEY_ARG_EXPLORE_TYPE = "KEY_ARG_EXPLORE_TYPE";
	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_STATE_HAS_MOVED_MAP_CAMERA_TO_START = "KEY_STATE_HAS_MOVED_MAP_CAMERA_TO_START";
	private static final String KEY_STATE_HAS_ANIMATED_MAP_CAMERA_TO_USER_LOCATION_TO_START = "KEY_STATE_HAS_ANIMATED_MAP_CAMERA_TO_USER_LOCATION_TO_START";
	private static final String KEY_STATE_HAS_CLICKED_DEFAULT_MARKER = "KEY_STATE_HAS_CLICKED_DEFAULT_MARKER";
	private static final String KEY_STATE_MARKER_WITH_INFO_WINDOW_OPEN_POI_HASH_CODE = "KEY_STATE_MARKER_WITH_INFO_WINDOW_OPEN_POI_HASH_CODE";
	private static final String KEY_STATE_MAP_TILE_ID = "KEY_MAP_TILE_ID";

	private static final int MAP_BOUNDS_CORRECTION_ANIMATION_DURATION_IN_MS = 900;
	private static final int MAP_ZOOM_CORRECTION_ANIMATION_DURATION_IN_MS = 400;
	public static final int MAP_CENTER_ANIMATION_DURATION_IN_MS = 600;
	private static final int MAP_ANIMATION_DELAY_IN_MS = 600;
	private static final int MAP_PIN_CLICK_DELAY = 300;
	private static final float EVENT_CLUSTERING_DISTANCE_THRESHOLD_METERS = 10;

	private static SparseIntArray sPoiPinResIdMap;
	private boolean mHasMovedMapCameraToStart;
	private boolean mHasAnimatedMapCameraToUserLocationToStart;
	private boolean mHasClickedDefaultMarker;
	private boolean mIsMarkerInfoWindowOpen;
	private String mSelectedPoiObjectJson;
	private PointOfInterest mSelectedPoi;
	private SparseArray<PointOfInterestMarker> mPoiMarkerSparseArray;
	private LatLng mMarkerWithInfoWindowOpenLatLng;
	private int mMarkerWithInfoWindowOpenPoiHashCode;
	private String mMarkerWithInfoWindowOpenId;
	private GoogleMap mGoogleMap;
	private ExploreType mExploreType;
	private TileOverlay mTileOverlay;
	private ExploreMapInfoWindowAdapter mExploreMapInfoWindowAdapter;
	private MapTileProvider mMapTileProvider;
	private MapWrapperLayout mMapWrapperLayout;
	private OnCheckedChangeListener mParentOnCheckedChangeListener;
	private GoogleApiClient mGoogleApiClient;
	private int mDistanceThreshold = 10;
	private UniversalFloatingActionButton mWalletButton;
	private ToggleButton mRestroomToggle;
	private UniversalFloatingActionButton mLocationButton;
    public boolean mQueryReturned = false; //Used to prevent onLoadFinished fully running twice
	private Long mMapTileId = null;
    private boolean mHasLoadedFavorites = false;

	public static ExploreMapFragment newInstance(DatabaseQuery databaseQuery, ExploreType exploreType) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery
					+ " exploreType = " + exploreType);
		}

		// Create a new fragment instance
		ExploreMapFragment fragment = new ExploreMapFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (exploreType != null) {
			args.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);
		}
		if (databaseQuery != null) {
			args.putString(DatabaseQueryMapFragment.KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		fragment.setArguments(args);

		return fragment;
	}

	public static ExploreMapFragment newInstance(DatabaseQuery databaseQuery, ExploreType exploreType, String selectedPoiObjectJson, Long mapTileId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery
					+ " exploreType = " + exploreType.getValue()
					+ " poiObjectJson = " + selectedPoiObjectJson);
		}

		// Create a new fragment instance
		ExploreMapFragment fragment = newInstance(databaseQuery, exploreType);

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		if (selectedPoiObjectJson != null) {
			args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		}
		args.putSerializable(KEY_STATE_MAP_TILE_ID, mapTileId);
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
		if (parentFragment != null && parentFragment instanceof OnCheckedChangeListener) {
			mParentOnCheckedChangeListener = (OnCheckedChangeListener) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof OnCheckedChangeListener) {
			mParentOnCheckedChangeListener = (OnCheckedChangeListener) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentOnCheckedChangeListener == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnCheckedChangeListener");
			}
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
		if (args == null) {
			mExploreType = null;
			mSelectedPoiObjectJson = null;
		}
		// Otherwise, set incoming parameters
		else {
			mExploreType = (ExploreType) args.getSerializable(KEY_ARG_EXPLORE_TYPE);
			mSelectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
			mMapTileId = (Long) args.getSerializable(KEY_STATE_MAP_TILE_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			// Track the page view
			ExploreFragment.trackPageView(mExploreType, AnalyticsUtils.CONTENT_SUB_2_MAP);

			mHasMovedMapCameraToStart = false;
			setHasAnimatedMapCameraToUserLocationToStart(false);
			mHasClickedDefaultMarker = false;
			mMarkerWithInfoWindowOpenPoiHashCode = -1;
			mIsMarkerInfoWindowOpen = false;
			mMarkerWithInfoWindowOpenId = "";
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mHasMovedMapCameraToStart = savedInstanceState.getBoolean(KEY_STATE_HAS_MOVED_MAP_CAMERA_TO_START);
			setHasAnimatedMapCameraToUserLocationToStart(savedInstanceState.getBoolean(KEY_STATE_HAS_ANIMATED_MAP_CAMERA_TO_USER_LOCATION_TO_START));
			mHasClickedDefaultMarker = savedInstanceState.getBoolean(KEY_STATE_HAS_CLICKED_DEFAULT_MARKER);
			mMarkerWithInfoWindowOpenPoiHashCode = savedInstanceState.getInt(KEY_STATE_MARKER_WITH_INFO_WINDOW_OPEN_POI_HASH_CODE);
			mIsMarkerInfoWindowOpen = false; // Assume window is closed, let that state restore itself
			mMarkerWithInfoWindowOpenId = ""; // Assume window is closed, let that state restore itself
		}

		if (mSelectedPoiObjectJson != null) {
			mSelectedPoi = GsonObject.fromJson(mSelectedPoiObjectJson, PointOfInterest.class);
		}

		mPoiMarkerSparseArray = new SparseArray<PointOfInterestMarker>();
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

        // Enable menu if explore type is event map
        if (mExploreType == ExploreType.EVENT_MAP) {
            setHasOptionsMenu(true);
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Create the map layout
		View rootMapView = super.onCreateView(inflater, container, savedInstanceState);

		// Inject a map wrapper layout on top of the map
		View rootLayout = inflater.inflate(R.layout.map_wrapper_container, (ViewGroup) rootMapView, true);
		mMapWrapperLayout = (MapWrapperLayout) rootLayout.findViewById(R.id.map_wrapper_container);
		mWalletButton = (UniversalFloatingActionButton) rootLayout.findViewById(R.id.view_ufab_wallet_map);
		mLocationButton = (UniversalFloatingActionButton) rootLayout.findViewById(R.id.view_ufab_location_map);
		mRestroomToggle = (ToggleButton) rootLayout.findViewById(R.id.view_floating_action_toggle_restroom_map);
		// Set the parent to listen to the restroom toggle changes
		if (mParentOnCheckedChangeListener != null) {
			mRestroomToggle.setOnCheckedChangeListener(mParentOnCheckedChangeListener);
		}

		// Set up map buttons
		boolean isRestroomToggleVisible = isRestroomToggleVisible(mExploreType);
		mRestroomToggle.setVisibility(isRestroomToggleVisible ? View.VISIBLE : View.GONE);

		if (isRestroomToggleVisible) {
			mRestroomToggle.setChecked(AppPreferenceManager.getRestroomFiltersOn());
		}

		boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
		boolean isLocateMeButtonVisible = isLocateMeButtonVisible(mExploreType);
		mLocationButton.setVisibility((isLocateMeButtonVisible && isInPark) ? View.VISIBLE : View.GONE);
		mLocationButton.setOnClickListener(this);
		if(mExploreType == ExploreType.HOME_PAGE_MAP_SNAPSHOT || mExploreType == ExploreType.DETAIL_PAGE_MAP_SNAPSHOT) {
            mWalletButton.setVisibility(View.GONE);
        }
		return rootMapView;
	}

	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Create options menu if explore type is event map
        if (mExploreType == ExploreType.EVENT_MAP) {
            inflater.inflate(R.menu.action_event_series_map, menu);
        }
		TintUtils.tintAllMenuItems(menu, getContext());

        super.onCreateOptionsMenu(menu, inflater);
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Listen for the layout of the fragment, if the map hasn't been set to a default view
		if (!mHasMovedMapCameraToStart) {
			view.getViewTreeObserver().addOnGlobalLayoutListener(this);
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Only work with the map it has initialized
		mGoogleMap = getMap();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onViewStateRestored: getMap() " + (mGoogleMap == null ? "==" : "!=") + " null");
		}

		if (mGoogleMap != null) {

			// Compute the height of the map pin
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			float pinHeightInDp = 144 / 3.0f; // 144px @ 480dpi
			int bottomOffsetInPx = Math.round(
					TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pinHeightInDp, displayMetrics));

			// Init the map wrapper layout to handle clicks for the pin info
			// window
			mMapWrapperLayout.init(mGoogleMap, bottomOffsetInPx);
			mExploreMapInfoWindowAdapter = new ExploreMapInfoWindowAdapter(getActivity(), mMapWrapperLayout, mPoiMarkerSparseArray, this);
			mGoogleMap.setInfoWindowAdapter(mExploreMapInfoWindowAdapter);

			// Add custom map tile overlays for the tiles and background
			mMapTileProvider = new MapTileProvider(mMapTileId);
			mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mMapTileProvider)
					.zIndex(0));

			if (BuildConfig.DEBUG) {
				UniversalOrlandoDebugState debugState = UniversalOrlandoDebugStateManager.getInstance();
				boolean showGeofencesOnMap = debugState.isShowGeofencesOnMap();

				if (showGeofencesOnMap) {
					if (BuildConfigUtils.isLocationFlavorHollywood()) {
						CircleOptions ushr = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LAT, GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LON))
								.radius(GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#88FF0000"))
								.fillColor(Color.parseColor("#33FF0000"))
								.zIndex(1);
						mGoogleMap.addCircle(ushr);
						mGoogleMap.addCircle(ushr.radius(5));

						CircleOptions ush = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LAT, GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LON))
								.radius(GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#880000FF"))
								.fillColor(Color.parseColor("#330000FF"))
								.zIndex(1);
						mGoogleMap.addCircle(ush);
						mGoogleMap.addCircle(ush.radius(5));

						CircleOptions cw = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_LAT, GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_LON))
								.radius(GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#8800FFFF"))
								.fillColor(Color.parseColor("#3300FFFF"))
								.zIndex(1);
						mGoogleMap.addCircle(cw);
						mGoogleMap.addCircle(cw.radius(5));

					} else {
						CircleOptions uo = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LAT, GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LON))
								.radius(GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#88FF0000"))
								.fillColor(Color.parseColor("#33FF0000"))
								.zIndex(1);
						mGoogleMap.addCircle(uo);
						mGoogleMap.addCircle(uo.radius(5));

						CircleOptions ioa = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_LAT, GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_LON))
								.radius(GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#8800FF00"))
								.fillColor(Color.parseColor("#3300FF00"))
								.zIndex(1);
						mGoogleMap.addCircle(ioa);
						mGoogleMap.addCircle(ioa.radius(5));

						CircleOptions usf = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LAT, GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LON))
								.radius(GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#880000FF"))
								.fillColor(Color.parseColor("#330000FF"))
								.zIndex(1);
						mGoogleMap.addCircle(usf);
						mGoogleMap.addCircle(usf.radius(5));

						CircleOptions cw = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_CITYWALK_LAT, GeofenceUtils.GEOFENCE_CITYWALK_LON))
								.radius(GeofenceUtils.GEOFENCE_CITYWALK_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#8800FFFF"))
								.fillColor(Color.parseColor("#3300FFFF"))
								.zIndex(1);
						mGoogleMap.addCircle(cw);
						mGoogleMap.addCircle(cw.radius(5));

						CircleOptions wnw = new CircleOptions()
								.center(new LatLng(GeofenceUtils.GEOFENCE_WET_N_WILD_LAT, GeofenceUtils.GEOFENCE_WET_N_WILD_LON))
								.radius(GeofenceUtils.GEOFENCE_WET_N_WILD_RADIUS_IN_METERS)
								.strokeColor(Color.parseColor("#8853DCCA"))
								.fillColor(Color.parseColor("#3300FFFF"))
								.zIndex(1);
						mGoogleMap.addCircle(wnw);
						mGoogleMap.addCircle(wnw.radius(5));
					}
				}
			}

			// Other map settings
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			mGoogleMap.setIndoorEnabled(false);
			mGoogleMap.setOnCameraChangeListener(this);
			mGoogleMap.setOnMarkerClickListener(this);
			mGoogleMap.setOnInfoWindowClickListener(this);
			mGoogleMap.setOnMapClickListener(this);

			// Only show the user location on the map if the user is within the geofence
			boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
			try {
				mGoogleMap.setMyLocationEnabled(isMyLocationAllowed(mExploreType, isInPark));
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			// Enabled map controls
			UiSettings uiSettings = mGoogleMap.getUiSettings();
			uiSettings.setZoomGesturesEnabled(true);
			uiSettings.setScrollGesturesEnabled(true);

			// Disabled map controls
			uiSettings.setCompassEnabled(false);
			uiSettings.setMyLocationButtonEnabled(false);
			uiSettings.setRotateGesturesEnabled(false);
			uiSettings.setTiltGesturesEnabled(false);
			uiSettings.setZoomControlsEnabled(false);
			uiSettings.setMapToolbarEnabled(false);
			uiSettings.setIndoorLevelPickerEnabled(false);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}

		// Get a connection to user location
		mGoogleApiClient.connect();
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
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}

		outState.putBoolean(KEY_STATE_HAS_MOVED_MAP_CAMERA_TO_START, mHasMovedMapCameraToStart);
		outState.putBoolean(KEY_STATE_HAS_ANIMATED_MAP_CAMERA_TO_USER_LOCATION_TO_START, getHasAnimatedMapCameraToUserLocationToStart());
		outState.putBoolean(KEY_STATE_HAS_CLICKED_DEFAULT_MARKER, mHasClickedDefaultMarker);
		outState.putInt(KEY_STATE_MARKER_WITH_INFO_WINDOW_OPEN_POI_HASH_CODE, mMarkerWithInfoWindowOpenPoiHashCode);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}

		// Stop listening to user location
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		// Clear any references from loading UI on the map
		if (mExploreMapInfoWindowAdapter != null) {
			mExploreMapInfoWindowAdapter.destroy();
		}
		if (mMapTileProvider != null) {
			mMapTileProvider.destroy();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}

		// Clear any references to markers to prevent leaks
		if (mPoiMarkerSparseArray != null) {
			mPoiMarkerSparseArray.clear();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onGlobalLayout() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onGlobalLayout");
		}

		// Only move the map camera if it hasn't yet, and the Fragment has been
		// added to the Activity
		if (!mHasMovedMapCameraToStart && isAdded()) {
			// Move the map camera to its starting position
			moveMapCameraToStart();

			// If the map camera was successfully set, stop listening for changes
			if (mHasMovedMapCameraToStart) {
				View view = getView();
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
	public void onCameraChange(CameraPosition pos) {
		if (pos == null || pos.target == null) {
			return;
		}

		if (BuildConfig.DEBUG && false) {
			Log.d(TAG, "onCameraChange:" + "\nZoom: " + pos.zoom
					+ "\nLat (Y): " + pos.target.latitude
					+ "\nLon (X): " + pos.target.longitude
					+ "\nBearing: " + pos.bearing
					+ "\nTilt: " + pos.tilt);
		}

		// Only start limiting bounds after the default camera position has been set
		if (mHasMovedMapCameraToStart && getHasAnimatedMapCameraToUserLocationToStart() && mHasClickedDefaultMarker) {
			// Limit the zoom and reset it exceeds the bounds
			if (pos.zoom < MapUtils.MIN_ZOOM_LEVEL) {
				mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(MapUtils.MIN_ZOOM_LEVEL), MAP_ZOOM_CORRECTION_ANIMATION_DURATION_IN_MS, null);
			}
			else if (pos.zoom > MapUtils.MAX_ZOOM_LEVEL) {
				mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(MapUtils.MAX_ZOOM_LEVEL), MAP_ZOOM_CORRECTION_ANIMATION_DURATION_IN_MS, null);
			}
			// If a marker window is open, keep the pin on screen
			else if (mIsMarkerInfoWindowOpen) {
				boolean markerOutsideEast = false;
				boolean markerOutsideWest = false;
				boolean markerOutsideNorth = false;
				boolean markerOutsideSouth = false;

				LatLngBounds screenLatLngBounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;

				if (mMarkerWithInfoWindowOpenLatLng.longitude > screenLatLngBounds.northeast.longitude) {
					markerOutsideEast = true;
				}
				if (mMarkerWithInfoWindowOpenLatLng.latitude > screenLatLngBounds.northeast.latitude) {
					markerOutsideNorth = true;
				}
				if (mMarkerWithInfoWindowOpenLatLng.longitude < screenLatLngBounds.southwest.longitude) {
					markerOutsideWest = true;
				}
				if (mMarkerWithInfoWindowOpenLatLng.latitude < screenLatLngBounds.southwest.latitude) {
					markerOutsideSouth = true;
				}

				if (markerOutsideEast || markerOutsideWest || markerOutsideNorth || markerOutsideSouth) {
					// Since the user has scrolled away, close the marker window, map bounds logic will resume
					PointOfInterestMarker poiMarker = mPoiMarkerSparseArray.get(mMarkerWithInfoWindowOpenPoiHashCode);
					if (poiMarker != null) {
						Marker marker = poiMarker.getMarker();
						if (marker != null) {
							marker.hideInfoWindow();
						}
					}
					mIsMarkerInfoWindowOpen = false;
					mMarkerWithInfoWindowOpenId = "";
					mMarkerWithInfoWindowOpenPoiHashCode = -1;
				}
			}
			// Otherwise, keep the map screen within the illustrated maps bounds
			else  {

				boolean outsideEast = false;
				boolean outsideWest = false;
				boolean outsideNorth = false;
				boolean outsideSouth = false;

				LatLngBounds screenLatLngBounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;

				if (BuildConfig.DEBUG && false) {
					Log.d(TAG, "onCameraChange current screen:" + "\nZoom: " + pos.zoom
							+ "\nNortheast Lat (Y): " + screenLatLngBounds.northeast.latitude
							+ "\nNortheast Lon (X): " + screenLatLngBounds.northeast.longitude
							+ "\nSouthwest Lat (Y): " + screenLatLngBounds.southwest.latitude
							+ "\nSouthwest Lon (X): " + screenLatLngBounds.southwest.longitude);
				}

				if (screenLatLngBounds.northeast.longitude > MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.longitude) {
					outsideEast = true;
				}
				if (screenLatLngBounds.northeast.latitude > MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.latitude) {
					outsideNorth = true;
				}
				if (screenLatLngBounds.southwest.longitude < MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.longitude) {
					outsideWest = true;
				}
				if (screenLatLngBounds.southwest.latitude < MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.latitude) {
					outsideSouth = true;
				}

				if (BuildConfig.DEBUG && false) {
					Log.d(TAG, "onCameraChange:"
							+ "\n outsideEast = " + outsideEast
							+ "\n outsideWest = " + outsideWest
							+ "\n outsideNorth = " + outsideNorth
							+ "\n outsideSouth = " + outsideSouth);
				}

				if (outsideEast || outsideNorth || outsideWest || outsideSouth) {

					double lonBeyondEast = outsideEast ? screenLatLngBounds.northeast.longitude - MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.longitude : 0;
					double latBeyondNorth = outsideNorth ? screenLatLngBounds.northeast.latitude - MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.latitude : 0;
					double lonBeyondWest = outsideWest ? MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.longitude - screenLatLngBounds.southwest.longitude : 0;
					double latBeyondSouth = outsideSouth ? MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.latitude - screenLatLngBounds.southwest.latitude : 0;

					double newLatBoundEast = screenLatLngBounds.northeast.longitude - lonBeyondEast + lonBeyondWest;
					double newLatBoundNorth = screenLatLngBounds.northeast.latitude - latBeyondNorth + latBeyondSouth;
					double newLatBoundWest = screenLatLngBounds.southwest.longitude - lonBeyondEast + lonBeyondWest;
					double newLatBoundSouth = screenLatLngBounds.southwest.latitude - latBeyondNorth + latBeyondSouth;

					if (newLatBoundEast > MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.longitude) {
						newLatBoundEast = MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.longitude;
					}
					if (newLatBoundNorth > MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.latitude) {
						newLatBoundNorth = MapUtils.ILLUSTRATED_LATLNG_NORTHEAST.latitude;
					}
					if (newLatBoundWest < MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.longitude) {
						newLatBoundWest = MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.longitude;
					}
					if (newLatBoundSouth < MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.latitude) {
						newLatBoundSouth = MapUtils.ILLUSTRATED_LATLNG_SOUTHWEST.latitude;
					}

					LatLngBounds.Builder newLatLngBoundsBuilder = new LatLngBounds.Builder();
					newLatLngBoundsBuilder.include(new LatLng(
							newLatBoundNorth,
							newLatBoundEast));
					newLatLngBoundsBuilder.include(new LatLng(
							newLatBoundSouth,
							newLatBoundWest));
					LatLngBounds newLatLngBounds = newLatLngBoundsBuilder.build();

					if (BuildConfig.DEBUG && false) {
						Log.d(TAG, "onCameraChange corrected screen:" + "\nZoom: " + pos.zoom
								+ "\nNortheast Lat (Y): " + newLatLngBounds.northeast.latitude
								+ "\nNortheast Lon (X): " + newLatLngBounds.northeast.longitude
								+ "\nSouthwest Lat (Y): " + newLatLngBounds.southwest.latitude
								+ "\nSouthwest Lon (X): " + newLatLngBounds.southwest.longitude);
					}

					mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLngBounds.getCenter()), MAP_BOUNDS_CORRECTION_ANIMATION_DURATION_IN_MS, null);
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
			case R.id.view_ufab_location_map:
				if (mGoogleMap != null && mGoogleMap.isMyLocationEnabled()) {
					animateMapCameraToLocation(mGoogleMap.getMyLocation());
				}
				break;
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onMapClick");
		}

		// If a marker info window was open, it will now close
		if (mIsMarkerInfoWindowOpen) {
			mIsMarkerInfoWindowOpen = false;
			mMarkerWithInfoWindowOpenId = "";
			mMarkerWithInfoWindowOpenPoiHashCode = -1;
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onMarkerClick");
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "marker.isVisible() = " + marker.isVisible()
					+ " marker.isInfoWindowShown() = " + marker.isInfoWindowShown()
					+ " marker.getId() = " + marker.getId()
					+ " mMarkerWithInfoWindowOpenId = " + mMarkerWithInfoWindowOpenId);
		}

        PointOfInterestMarker pointOfInterestMarker = mPoiMarkerSparseArray.get(Integer.parseInt(marker
                .getSnippet()));

		// Load event list for clicked event marker
        if (mExploreType == ExploreType.EVENT_MAP) {
            if (pointOfInterestMarker != null) {
                PointOfInterest poi = pointOfInterestMarker.getPointOfInterest();
                if (poi instanceof Event) {
                    Event event = (Event) poi;
                    // Load timeline for grouped events
                    if (event.getGroupedEventIds() != null && event.getGroupedEventIds().size() > 1) {
                        DatabaseQuery databaseQuery = DatabaseQueryUtils.getTimelineEventsById(
                                event.getGroupedEventIds(), new FilterOptions(null, ExploreType.EVENT_LIST));
                        Bundle args = EventTimelineActivity
                                .newInstanceBundle(getString(R.string.drawer_item_events), databaseQuery,
                                        ExploreType.EVENT_LIST);
                        startActivity(new Intent(getActivity(), EventTimelineActivity.class).putExtras(args));
                        // Timeline loaded exit function
                        return true;
                    }
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "onMarkerClick: non-event marker clicked on the event map");
                    }
                }
            }
        } else if (mExploreType == ExploreType.OFFERS || mExploreType == ExploreType.OFFER_SERIES) {
            // Do not open an info window for offers
            return true;
        }

		// Show and center the marker window
		if (marker.isVisible()
                && !marker.isInfoWindowShown()
                && !marker.getId().equals(mMarkerWithInfoWindowOpenId)) {
            if (null != pointOfInterestMarker && null != pointOfInterestMarker.getSharedPoints()
                    && !pointOfInterestMarker.getSharedPoints().isEmpty()) {
                processGroupClick(pointOfInterestMarker);
                return true;
            }
			marker.showInfoWindow();
			mIsMarkerInfoWindowOpen = true;
			mMarkerWithInfoWindowOpenId = marker.getId();
			mMarkerWithInfoWindowOpenPoiHashCode = Integer.parseInt(marker.getSnippet());
			mMarkerWithInfoWindowOpenLatLng = marker.getPosition();

			Point point = mGoogleMap.getProjection().toScreenLocation(marker.getPosition());
			point.y -= mMapWrapperLayout.getPinAndInfoWindowOffsetCenterInPx();
			LatLng markerInfoWindowCenter = mGoogleMap.getProjection().fromScreenLocation(point);
			mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(markerInfoWindowCenter), MAP_CENTER_ANIMATION_DURATION_IN_MS, null);

			// Extract the attraction name for analytics
			if (pointOfInterestMarker != null) {
				PointOfInterest poi = pointOfInterestMarker.getPointOfInterest();
				if (poi != null) {
					String attractionName = poi.getDisplayName();

					// Track the page view
					String propertyName = null;
					Venue venue = pointOfInterestMarker.getVenue();
					if (venue != null) {
						propertyName = AnalyticsUtils.getPropertyName(venue);
					}
					String contentFocus = AnalyticsUtils.getExploreTypeName(mExploreType);
					String contentSub1 = AnalyticsUtils.getGuestServicesName(mExploreType);

					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							contentFocus,
							contentSub1,
							attractionName + " " + AnalyticsUtils.CONTENT_SUB_2_POPOVER,
							propertyName,
							attractionName,
							null);
				}
			}

		}
		else {
			marker.hideInfoWindow();
			mIsMarkerInfoWindowOpen = false;
			mMarkerWithInfoWindowOpenId = "";
			mMarkerWithInfoWindowOpenPoiHashCode = -1;
		}

		return true;
	}

    private void processGroupClick(PointOfInterestMarker marker) {
        List<PointOfInterestMarker> markers = marker.getSharedPoints();
        markers.add(marker);//add itself
        List<Long> poiIdList = new ArrayList<>();

        for (PointOfInterestMarker m : markers) {
            poiIdList.add(m.getPointOfInterest().getId());
        }
        DatabaseQuery query = DatabaseQueryUtils.getPointsOfInterestById(poiIdList, new FilterOptions(null, mExploreType));
        startActivity(ClusteredExploreActivity.createIntent(getActivity(), query.toJson(), mExploreType));
    }

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onInfoWindowClick");
		}

		String poiHashCode = marker.getSnippet();
		if (poiHashCode != null) {

			// If this is a detail view of the map, go back to the detail page
			if (mExploreType == ExploreType.SOLO_MAP_PAGE) {
				Activity parentActivity = getActivity();
				if (parentActivity != null) {
					parentActivity.finish();
				}
			}
			// Otherwise, open POI detail page
			else {
				try {
					PointOfInterestMarker pointOfInterestMarker = mPoiMarkerSparseArray.get(Integer.parseInt(poiHashCode));
					String poiObjectJson = pointOfInterestMarker.getPoiObjectJson();
					Integer poiTypeId = pointOfInterestMarker.getPoiTypeId();
					String venueObjectJson = pointOfInterestMarker.getVenueObjectJson();

					DetailUtils.openDetailPage(getContext(), venueObjectJson, poiObjectJson, poiTypeId, false, null);
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onInfoWindowClick: error converting poiHashCode = " + poiHashCode, e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}

		}
	}

	@Override
	public void onInfoWindowChildViewClicked(View v, Marker marker) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onInfoWindowChildViewClicked");
		}

		switch(v.getId()) {
			case R.id.map_poi_window_favorite_toggle_button:
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onInfoWindowChildViewClicked: favorite button clicked");
				}

				String poiHashCode = marker.getSnippet();
				if (poiHashCode != null) {
					try {
						PointOfInterestMarker pointOfInterestMarker = mPoiMarkerSparseArray.get(Integer.parseInt(poiHashCode));
						String poiObjectJson = pointOfInterestMarker.getPoiObjectJson();
						Integer poiTypeId = pointOfInterestMarker.getPoiTypeId();

						PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

						// Update the favorite state off the main thread
						boolean isFavorite = poi.getIsFavorite() != null && poi.getIsFavorite().booleanValue();
						PoiUtils.updatePoiIsFavoriteInDatabase(
								getActivity(), poi, !isFavorite, true);
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onInfoWindowChildViewClicked: error converting poiHashCode = " + poiHashCode, e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}

				break;
			case R.id.map_poi_window_guide_me_button_layout:
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onInfoWindowChildViewClicked: guide me button clicked");
				}

				poiHashCode = marker.getSnippet();
				boolean didLoadWayfindingPage = false;
				if (poiHashCode != null) {
					try {
						PointOfInterestMarker pointOfInterestMarker = mPoiMarkerSparseArray.get(Integer.parseInt(poiHashCode));
						String poiObjectJson = pointOfInterestMarker.getPoiObjectJson();
						Integer poiTypeId = pointOfInterestMarker.getPoiTypeId();

						didLoadWayfindingPage = WayfindingUtils.openWayfindingPage(v.getContext(), poiObjectJson, poiTypeId);
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onInfoWindowChildViewClicked: error converting poiHashCode = " + poiHashCode, e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}
				if (!didLoadWayfindingPage && getActivity() != null) {
					UserInterfaceUtils.showToastFromForeground(
							getString(R.string.wayfinding_toast_error_trying_to_start_guiding), Toast.LENGTH_LONG, getActivity());
				}
				break;
			case R.id.map_poi_window_root_container:
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onInfoWindowChildViewClicked: info window clicked, passing click to normal handler");
				}
				// Pass click to the normal info window click handler
				onInfoWindowClick(marker);
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onInfoWindowChildViewClicked: unhandled info window child view clicked");
				}
				break;
		}
	}

	@Override
	public void onDatabaseQueryChange(DatabaseQuery databaseQuery) {
        mQueryReturned = false;
		super.onDatabaseQueryChange(databaseQuery);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Check the currently open marker
				PointOfInterestMarker poiMarkerWithInfoWindowOpen = mPoiMarkerSparseArray.get(mMarkerWithInfoWindowOpenPoiHashCode);
				PointOfInterestMarker updatedPoiMarkerWithInfoWindowOpen = null;
				Long markerWithInfoWindowOpenPoiId = null;
				if (poiMarkerWithInfoWindowOpen != null) {
					markerWithInfoWindowOpenPoiId = poiMarkerWithInfoWindowOpen.getPoiId();
				}

				// Track new POIs
				SparseArray<String> latestPoiList = new SparseArray<String>();

				// Go through each new POI
				if (data != null && data.moveToFirst()) {
				    // Offer pins
                    if (mExploreType == ExploreType.OFFERS || mExploreType == ExploreType.OFFER_SERIES) {
                        do {
                            String poiObjectJson = null;
                            Double latitude = null;
                            Double longitude = null;
                            String venueObjectJson = null;
							String venueLandObjectJson;

							try {
								venueLandObjectJson = data.getString(data.getColumnIndex(VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON));
							} catch (Exception e) {
								venueLandObjectJson = null;
							}
							switch(mExploreType) {
                                case OFFERS:
                                    poiObjectJson = data.getString(data.getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));
                                    latitude = data.getDouble(data.getColumnIndex(OffersTable.COL_LATITUDE));
                                    longitude = data.getDouble(data.getColumnIndex(OffersTable.COL_LONGITUDE));
                                    venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
                                    break;
                                case OFFER_SERIES:
                                    poiObjectJson = data.getString(data.getColumnIndex(OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON));
                                    latitude = data.getDouble(data.getColumnIndex(OfferSeriesTable.COL_LATITUDE));
                                    longitude = data.getDouble(data.getColumnIndex(OfferSeriesTable.COL_LONGITUDE));
                                    venueObjectJson = Venue.DEFAULT_EVENT_VENUE.toJson();
                                    break;
                                default:
                                    break;
                            }

                            if (TextUtils.isEmpty(venueObjectJson)) {
                                venueObjectJson = Venue.DEFAULT_EVENT_VENUE.toJson();
                            }
                            PointOfInterest poi = GsonObject.fromJson(poiObjectJson, PointOfInterest.class);
                            if (poi != null && poiObjectJson != null && !poiObjectJson.isEmpty()
                                    && venueObjectJson != null && !venueObjectJson.isEmpty()
                                    && latitude != null && longitude != null
                                    && MapUtils.ILLUSTRATED_LATLNG_BOUNDS.contains(new LatLng(latitude, longitude))) {

                                // Center the map on the offer's coordinates
                                LatLng poiLatLng = new LatLng(poi.getLatitude(), poi.getLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(poiLatLng)
                                .zoom(17.5f)
                                .build();

                                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                Long poiId = poi.getId();
                                Integer poiHashCode = poi.hashCode();
                                Integer poiTypeId = PointsOfInterestTable.VAL_POI_TYPE_ID_OFFER;
                                // Create pin
                                latestPoiList.put(poiHashCode, poiObjectJson);

                                // If the POI is new, or has changed, add it to the map
                                if (mPoiMarkerSparseArray.get(poiHashCode) == null) {
                                    // Get the proper pin image
                                    int pinImageResId = R.drawable.ic_map_pin_amenity_atm;

                                    // If a new POI is an updated version of the open marker, just update the marker
                                    if (poiId.equals(markerWithInfoWindowOpenPoiId)) {
                                        updatedPoiMarkerWithInfoWindowOpen = new PointOfInterestMarker(
												null, poiId, poiTypeId, poiHashCode, poiObjectJson, venueObjectJson, venueLandObjectJson);
                                    }
                                    // Otherwise, create a new marker
                                    else {
                                        // Create the map marker
                                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(latitude, longitude))
                                            .anchor(56.0f / 144.0f, 1.0f) // Shift the anchor point to the bottom point of the pin
                                            .infoWindowAnchor(56.0f / 144.0f, 0.0f) // Shift the info window anchor to match the bottom point of the pin
                                            .snippet(poiHashCode.toString()) // Very important, links marker back to the POI sparse array
                                            .icon(BitmapDescriptorFactory.fromResource(pinImageResId)));

                                        // Keep track of the poi and the marker
                                        mPoiMarkerSparseArray.put(poiHashCode, new PointOfInterestMarker(
												marker, poiId, poiTypeId, poiHashCode, poiObjectJson, venueObjectJson, venueLandObjectJson));
                                    }
                                }
                            }
                        } while (data.moveToNext());
                    }
    			    // Grouped map for event-series events
				    else if (mExploreType == ExploreType.EVENT_MAP) {
						HashMap<Long, String> poiIdToVenueObjectJson = new HashMap<>();
						HashMap<Long, String> poiIdToVenueLandObjectJson = new HashMap<>();
    			        List<Event> events = new ArrayList<>();
    			        do {
    			            String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
                            Long poiId = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
                            Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
                            Integer poiHashCode = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_HASH_CODE));
                            Double latitude = data.getDouble(data.getColumnIndex(PointsOfInterestTable.COL_LATITUDE));
                            Double longitude = data.getDouble(data.getColumnIndex(PointsOfInterestTable.COL_LONGITUDE));
                            Long subTypeFlags = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_SUB_TYPE_FLAGS));
                            String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
							String venueLandObjectJson = data.getString(data.getColumnIndex(VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON));

							if (poiObjectJson != null && !poiObjectJson.isEmpty()
                                    && poiTypeId != null && poiHashCode != null
                                    && venueObjectJson != null && !venueObjectJson.isEmpty()
                                    && latitude != null && longitude != null
                                    && subTypeFlags != null && poiId != null
                                    && MapUtils.ILLUSTRATED_LATLNG_BOUNDS.contains(new LatLng(latitude, longitude))) {
                                PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                                // Gather events
                                if (poi instanceof Event) {
                                    Event event = (Event) poi;
                                    events.add(event);
									poiIdToVenueObjectJson.put(poi.getId(), venueObjectJson);
									poiIdToVenueLandObjectJson.put(poi.getId(), venueLandObjectJson);
                                }
                            }
    			        } while (data.moveToNext());

    			        List<Event> processedEvents = new ArrayList<Event>();
    			        // Group events based on distance
    			        if (!events.isEmpty()) {
    			            do {
    			                // Process event
    			                Event processedEvent = events.remove(0);
    			                processedEvents.add(processedEvent);
    			                // Initialize grouped event id list
    			                processedEvent.setGroupedEventIds(new ArrayList<Long>());
    			                processedEvent.getGroupedEventIds().add(processedEvent.getId());

    			                Location processedLocation = new Location(LocationManager.GPS_PROVIDER);
    			                processedLocation.setLatitude(processedEvent.getLatitude());
    			                processedLocation.setLongitude(processedEvent.getLongitude());

    			                // Add other nearby events if any
    			                Iterator<Event> iterator = events.iterator();
    			                while (iterator.hasNext()) {
    			                    Event event = iterator.next();
    			                    Location eventLocation = new Location(LocationManager.GPS_PROVIDER);
    			                    eventLocation.setLatitude(event.getLatitude());
    			                    eventLocation.setLongitude(event.getLongitude());
    			                    if (processedLocation.distanceTo(eventLocation) <= EVENT_CLUSTERING_DISTANCE_THRESHOLD_METERS) {
    			                        processedEvent.getGroupedEventIds().add(event.getId());
    			                        iterator.remove();
    			                    }
    			                }
    			            } while(!events.isEmpty());

    			            // Create markers for grouped events
    			            for (Event processedEvent : processedEvents) {
    			                Integer poiHashCode = processedEvent.hashCode();
    			                latestPoiList.put(poiHashCode, processedEvent.toJson());
    			                // If the POI is new, or has changed, add it to the map
                                if (mPoiMarkerSparseArray.get(poiHashCode) == null) {
                                    // Get the proper pin image
                                    int pinImageResId = getPoiPinResId(mExploreType,
                                            PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT,
                                            processedEvent.getSubTypeFlags());

                                    // If a new POI is an updated version of the open marker, just update the marker
                                    if (processedEvent.getId().equals(markerWithInfoWindowOpenPoiId)) {
                                        updatedPoiMarkerWithInfoWindowOpen = new PointOfInterestMarker(null,
                                                processedEvent.getId(),
                                                PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT, poiHashCode,
                                                processedEvent.toJson(),
												poiIdToVenueObjectJson.get(processedEvent.getId()),
												poiIdToVenueLandObjectJson.get(processedEvent.getId()));
                                    }
                                    // Otherwise, create a new marker
                                    else {
                                        // Create the map marker
                                        MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng(processedEvent.getLatitude(), processedEvent.getLongitude()))
                                        .anchor(56.0f / 144.0f, 1.0f) // Shift the anchor point to the bottom point of the pin
                                        .infoWindowAnchor(56.0f / 144.0f, 0.0f) // Shift the info window anchor to match the bottom point of the pin
                                        .snippet(poiHashCode.toString()); // Very important, links marker back to the POI sparse array
                                        if (processedEvent.getGroupedEventIds() != null && processedEvent.getGroupedEventIds().size() > 1) {
                                            markerOptions.icon(
                                                    BitmapDescriptorFactory.fromBitmap(
                                                            writeTextOnDrawable(pinImageResId,
                                                                    String.valueOf(processedEvent.getGroupedEventIds().size()))));
                                        } else {
                                            markerOptions.icon(BitmapDescriptorFactory.fromResource(pinImageResId));
                                        }
                                        Marker marker = mGoogleMap.addMarker(markerOptions);
                                        // Keep track of the poi and the marker
                                        mPoiMarkerSparseArray.put(poiHashCode,
                                                new PointOfInterestMarker(marker, processedEvent.getId(),
                                                        PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT,
                                                        poiHashCode, processedEvent.toJson(),
														poiIdToVenueObjectJson.get(processedEvent.getId()),
														poiIdToVenueLandObjectJson.get(processedEvent.getId())
												));
                                    }
                                }
    			            }
    			        }
    			    } else if (mExploreType == ExploreType.FAVORITES
							   && BuildConfigUtils.isLocationFlavorOrlando() /* Only group for UO, this doesn't function correctly*/) {
                        List<PointOfInterestMarker> pointOfInterestMarkers = new ArrayList<>();

    					do {
    						String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
    						Long poiId = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
    						Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
    						Integer poiHashCode = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_HASH_CODE));
    						Double latitude = data.getDouble(data.getColumnIndex(PointsOfInterestTable.COL_LATITUDE));
    						Double longitude = data.getDouble(data.getColumnIndex(PointsOfInterestTable.COL_LONGITUDE));
    						Long subTypeFlags = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_SUB_TYPE_FLAGS));
    						String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
							String venueLandObjectJson = data.getString(data.getColumnIndex(VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON));

							if (poiObjectJson != null && !poiObjectJson.isEmpty()
    								&& poiTypeId != null && poiHashCode != null
    								&& venueObjectJson != null && !venueObjectJson.isEmpty()
    								&& venueLandObjectJson != null && !venueLandObjectJson.isEmpty()
    								&& latitude != null && longitude != null
    								&& subTypeFlags != null && poiId != null
    								&& MapUtils.ILLUSTRATED_LATLNG_BOUNDS.contains(new LatLng(latitude, longitude))) {

    							latestPoiList.put(poiHashCode, poiObjectJson);

    							// If the POI is new, or has changed, add it to the map
    							if (mPoiMarkerSparseArray.get(poiHashCode) == null) {
                                    PointOfInterestMarker poiMarker = new PointOfInterestMarker(
											null, poiId, poiTypeId, poiHashCode,
											poiObjectJson, venueObjectJson, venueLandObjectJson, subTypeFlags);
                                    pointOfInterestMarkers.add(poiMarker);
    							}
    						}
    					} while (data.moveToNext());

                        Iterator<PointOfInterestMarker> poiMarkersInterator = pointOfInterestMarkers.iterator();
                        List<PointOfInterestMarker> processedPointsOfInterest = new ArrayList<>();
                        while (poiMarkersInterator.hasNext()) {
                            PointOfInterestMarker poiMarker = poiMarkersInterator.next();
                            // If a new POI is an updated version of the open marker, just update the marker
                            if (poiMarker.getPoiId().equals(markerWithInfoWindowOpenPoiId)) {
                                updatedPoiMarkerWithInfoWindowOpen = poiMarker;
                            } else {
                                Location processedLocation = new Location(LocationManager.GPS_PROVIDER);
                                processedLocation.setLatitude(poiMarker.getPointOfInterest().getLatitude());
                                processedLocation.setLongitude(poiMarker.getPointOfInterest().getLongitude());

                                boolean found = false;
                                for (PointOfInterestMarker marker : processedPointsOfInterest) {
                                    Location currentMarkerLoc = new Location(LocationManager.GPS_PROVIDER);
                                    currentMarkerLoc.setLatitude(marker.getPointOfInterest().getLatitude());
                                    currentMarkerLoc.setLongitude(marker.getPointOfInterest().getLongitude());
                                    if (processedLocation.distanceTo(currentMarkerLoc) <= mDistanceThreshold) {
                                        marker.addSharedPoint(poiMarker);
                                        found = true;
                                        break;
                                    }
                                }

                                if (!found) {
                                    processedPointsOfInterest.add(poiMarker);
                                }
                            }
                        }
                        for (PointOfInterestMarker poiMarker : processedPointsOfInterest) {
                            int pinImageResId = getPoiPinResId(mExploreType, poiMarker.getPoiTypeId(), poiMarker.subTypeFlags);

                            // Create the map marker
                            MarkerOptions options = new MarkerOptions();
                            options

                                    .position(new LatLng(poiMarker.getPointOfInterest().getLatitude(), poiMarker.getPointOfInterest().getLongitude()))
                                    .anchor(56.0f / 144.0f, 1.0f) // Shift the anchor point to the bottom point of the pin
                                    .infoWindowAnchor(56.0f / 144.0f, 0.0f) // Shift the info window anchor to match the bottom point of the pin
                                    .snippet(poiMarker.getPoiHashCode().toString()); // Very important, links marker back to the POI sparse array

                            if (null == poiMarker.getSharedPoints() || poiMarker.getSharedPoints().isEmpty()) {
                                options.icon(BitmapDescriptorFactory.fromResource(pinImageResId));
                            } else if (null != poiMarker.getSharedPoints()) {
                                int startType = poiMarker.getPoiTypeId();
                                for (PointOfInterestMarker sub : poiMarker.getSharedPoints()) {
                                    if (sub.getPoiTypeId() != startType) {
                                        pinImageResId = R.drawable.ic_map_pin_event;
                                        break;
                                    }
                                }
                                options.icon(
                                        BitmapDescriptorFactory.fromBitmap(
                                                writeTextOnDrawable(pinImageResId,
                                                        String.valueOf(poiMarker.getSharedPoints().size() + 1))));
                            }
                            Marker marker = mGoogleMap.addMarker(options);

                            // Keep track of the poi and the marker
                            poiMarker.setMarker(marker);
                            mPoiMarkerSparseArray.put(poiMarker.getPoiHashCode(), poiMarker);
                        }
    			    }
					// Normal un-grouped map
					else {
						do {
							String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
							Long poiId = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
							Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
							Integer poiHashCode = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_HASH_CODE));
							Double latitude = data.getDouble(data.getColumnIndex(PointsOfInterestTable.COL_LATITUDE));
							Double longitude = data.getDouble(data.getColumnIndex(PointsOfInterestTable.COL_LONGITUDE));
							Long subTypeFlags = data.getLong(data.getColumnIndex(PointsOfInterestTable.COL_SUB_TYPE_FLAGS));
							String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
							String venueLandObjectJson = data.getString(data.getColumnIndex(VenueLandsTable.COL_VENUE_LAND_OBJECT_JSON));

							if (poiObjectJson != null && !poiObjectJson.isEmpty()
									&& poiTypeId != null && poiHashCode != null
									&& venueObjectJson != null && !venueObjectJson.isEmpty()
									&& venueLandObjectJson != null && !venueLandObjectJson.isEmpty()
									&& latitude != null && longitude != null
									&& subTypeFlags != null && poiId != null
									&& MapUtils.ILLUSTRATED_LATLNG_BOUNDS.contains(new LatLng(latitude, longitude))) {

								latestPoiList.put(poiHashCode, poiObjectJson);

								// If the POI is new, or has changed, add it to the map
								if (mPoiMarkerSparseArray.get(poiHashCode) == null) {
									// Get the proper pin image
									int pinImageResId = getPoiPinResId(mExploreType, poiTypeId, subTypeFlags);

									// If a new POI is an updated version of the open marker, just update the marker
									if (poiId.equals(markerWithInfoWindowOpenPoiId)) {
										updatedPoiMarkerWithInfoWindowOpen = new PointOfInterestMarker(
												null, poiId, poiTypeId, poiHashCode, poiObjectJson, venueObjectJson, venueLandObjectJson);
									}
									// Otherwise, create a new marker
									else {
										// Create the map marker
										Marker marker = mGoogleMap.addMarker(new MarkerOptions()
												.position(new LatLng(latitude, longitude))
												.anchor(56.0f / 144.0f, 1.0f) // Shift the anchor point to the bottom point of the pin
												.infoWindowAnchor(56.0f / 144.0f, 0.0f) // Shift the info window anchor to match the bottom point of the pin
												.snippet(poiHashCode.toString()) // Very important, links marker back to the POI sparse array
												.icon(BitmapDescriptorFactory.fromResource(pinImageResId)));

										// Keep track of the poi and the marker
										mPoiMarkerSparseArray.put(poiHashCode, new PointOfInterestMarker(
												marker, poiId, poiTypeId, poiHashCode, poiObjectJson, venueObjectJson, venueLandObjectJson));
									}
								}
							}
						} while (data.moveToNext());
					}
    		    }

				// Find old markers that aren't in the latest POI list
				List<PointOfInterestMarker> poiMarkersToRemove = new ArrayList<>();
				for (int i = 0; i < mPoiMarkerSparseArray.size(); i++) {
					PointOfInterestMarker poiMarker = mPoiMarkerSparseArray.valueAt(i);

					// Track any markers that aren't in the latest POI list
					if (latestPoiList.get(poiMarker.getPoiHashCode()) == null) {
						poiMarkersToRemove.add(poiMarker);
					}
				}
				// Remove old markers that aren't in the latest POI list
				for (PointOfInterestMarker poiMarkerToRemove : poiMarkersToRemove) {
					Marker markerToRemove = poiMarkerToRemove.getMarker();

					// If the new version of the open marker exists, just update it
					if (updatedPoiMarkerWithInfoWindowOpen != null
							&& poiMarkerToRemove.getPoiId().equals(markerWithInfoWindowOpenPoiId)) {

						int updatedPoiWithInfoWindowOpenPoiHashCode = updatedPoiMarkerWithInfoWindowOpen.getPoiHashCode();
						markerToRemove.setSnippet("" + updatedPoiWithInfoWindowOpenPoiHashCode);
						updatedPoiMarkerWithInfoWindowOpen.setMarker(markerToRemove);

						// Add the new marker and remove the old one
						mPoiMarkerSparseArray.put(updatedPoiWithInfoWindowOpenPoiHashCode, updatedPoiMarkerWithInfoWindowOpen);
						mPoiMarkerSparseArray.remove(poiMarkerToRemove.getPoiHashCode());

						// Update the marker UI
						markerToRemove.showInfoWindow();
						mIsMarkerInfoWindowOpen = true;
						mMarkerWithInfoWindowOpenId = markerToRemove.getId();
						mMarkerWithInfoWindowOpenPoiHashCode = updatedPoiWithInfoWindowOpenPoiHashCode;
						mMarkerWithInfoWindowOpenLatLng = markerToRemove.getPosition();

					}
					// Otherwise, remove the marker
					else {
						if (markerToRemove.isInfoWindowShown()) {
							mIsMarkerInfoWindowOpen = false;
							mMarkerWithInfoWindowOpenId = "";
							mMarkerWithInfoWindowOpenPoiHashCode = -1;
						}
						mPoiMarkerSparseArray.remove(poiMarkerToRemove.getPoiHashCode());
						markerToRemove.remove();
					}
				}

				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onLoadFinished: mPoiMarkerSparseArray.size() = " + mPoiMarkerSparseArray.size()
							+ " latestPoiList.size() = " + latestPoiList.size()
							+ " poiMarkersToRemove.size() = " + poiMarkersToRemove.size());
				}

				// If a POI was selected to start the map, select it
				if (!mHasClickedDefaultMarker && mMarkerWithInfoWindowOpenId.equals("")) {
					// If there is a POI to select, and this isn't a detail page, click it
					if (mSelectedPoi != null && mExploreType != ExploreType.SOLO_MAP_PAGE
							&& mExploreType != ExploreType.DETAIL_PAGE_MAP_SNAPSHOT) {
						clickMarker(mSelectedPoi.getId());
					}
					mHasClickedDefaultMarker = true;
				}
				// Otherwise, if a pin was selected and the state was saved, and no pin window is open, click it
				else if (!mIsMarkerInfoWindowOpen && mMarkerWithInfoWindowOpenPoiHashCode != -1 && mMarkerWithInfoWindowOpenId.equals("")) {
					// Set the last marker clicked to a special invalid value to prevent double clicks on double loader calls
					mMarkerWithInfoWindowOpenId = "-1";
					clickMarker(mMarkerWithInfoWindowOpenPoiHashCode);
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
			case LOADER_ID_DATABASE_QUERY:
				break;
			default:
				break;
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
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

		// After connecting to location client, try to zoom in on user location (if needed)
		if (!getHasAnimatedMapCameraToUserLocationToStart()) {
			animateMapCameraToUserLocationToStart();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onConnectionFailed: connectionResult.getErrorCode() = " + connectionResult.getErrorCode());
		}
	}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
	public void selectPoi(int poiHashCode) {
		// Only click the marker if it isn't currently clicked
		if (poiHashCode != mMarkerWithInfoWindowOpenPoiHashCode) {
			clickMarker(poiHashCode);
		}
	}

	private synchronized void setHasAnimatedMapCameraToUserLocationToStart(boolean hasAnimatedMapCameraToUserLocationToStart) {
		mHasAnimatedMapCameraToUserLocationToStart = hasAnimatedMapCameraToUserLocationToStart;
	}

	private synchronized boolean getHasAnimatedMapCameraToUserLocationToStart() {
		return mHasAnimatedMapCameraToUserLocationToStart;
	}

	private void updateViewsBasedOnGeofence() {
		Activity parentActivity = getActivity();
		if (parentActivity != null && mGoogleMap != null && mLocationButton != null) {
			boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
			boolean isLocateMeButtonVisible = isLocateMeButtonVisible(mExploreType);
			mLocationButton.setVisibility((isLocateMeButtonVisible && isInPark) ? View.VISIBLE : View.GONE);
			mLocationButton.setOnClickListener(this);
            try {
                mGoogleMap.setMyLocationEnabled(isMyLocationAllowed(mExploreType, isInPark));
            } catch (SecurityException e) {
                e.printStackTrace();
            }

			// Restart the loader to trigger a reload showing/hiding wait times
			// and guide me
			restartLoader();
		}
	}

	private void moveMapCameraToStart() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "moveMapCameraToStart");
		}

		// Move camera to a default position
		if (mGoogleMap != null) {

			// If there is a POI that was selected, zoom to it
			if (mSelectedPoi != null
					&& mSelectedPoi.getLatitude() != null
					&& mSelectedPoi.getLongitude() != null
					&& MapUtils.ILLUSTRATED_LATLNG_BOUNDS.contains(new LatLng(mSelectedPoi.getLatitude(), mSelectedPoi.getLongitude()))) {

				float defaultZoom;
				switch (mExploreType) {
					case SOLO_MAP_PAGE:
					case DETAIL_PAGE_MAP_SNAPSHOT:
						defaultZoom = 17.5f;
						break;
					default:
						defaultZoom = 17.75f;
						break;
				}

				// Center the map on the POI's coordinates
				LatLng poiLatLng = new LatLng(mSelectedPoi.getLatitude(), mSelectedPoi.getLongitude());
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(poiLatLng)
				.zoom(defaultZoom)
				.bearing(MapUtils.MAP_BEARING_DEGREES)
				.tilt(0)
				.build();

				mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

				// Then, get the center of the POI pin, to even better center it (map has to have camera already set for this to work)
				Point point = mGoogleMap.getProjection().toScreenLocation(poiLatLng);
				point.y -= mMapWrapperLayout.getPinOffsetCenterInPx();
				LatLng markerInfoWindowCenter = mGoogleMap.getProjection().fromScreenLocation(point);

				cameraPosition = new CameraPosition.Builder(mGoogleMap.getCameraPosition())
				.target(markerInfoWindowCenter)
				.build();

				mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
			// Otherwise, use default zooms
			else {
				LatLngBounds.Builder builder = new LatLngBounds.Builder();

				switch (mExploreType) {
					case ISLANDS_OF_ADVENTURE:
						for (LatLng latLng : MapUtils.getLatLngBoundsIslandsOfAdventure()) {
							builder.include(latLng);
						}
						break;
					case UNIVERSAL_STUDIOS_FLORIDA:
						for (LatLng latLng : MapUtils.getLatLngBoundsUniversalStudiosFlorida()) {
							builder.include(latLng);
						}
						break;
					case CITY_WALK_ORLANDO:
						for (LatLng latLng : MapUtils.getLatLngBoundsCityWalkOrlando()) {
							builder.include(latLng);
						}
						break;
					case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
					case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
					case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
						for (LatLng latLng : MapUtils.getLatLngBoundsUniversalStudiosHollywood()) {
							builder.include(latLng);
						}
						break;
					case CITY_WALK_HOLLYWOOD:
						for (LatLng latLng : MapUtils.getLatLngBoundsCityWalkHollywood()) {
							builder.include(latLng);
						}
						break;
					case WET_N_WILD:
					    for (LatLng latLng : MapUtils.getLatLngBoundsWetNWild()) {
					        builder.include(latLng);
					    }
					    break;
                    case VOLCANO_BAY:
                        for (LatLng latLng : MapUtils.getLatlngBoundsParkVolcanoBay()) {
                            builder.include(latLng);
                        }
                        break;
					case FAVORITES:
						for (LatLng latLng : MapUtils.getLatLngBoundsAreaEntirePark()) {
							builder.include(latLng);
						}
						break;
					case HOTELS:
						for (LatLng latLng : MapUtils.getLatLngBoundsAreaHotels()) {
							builder.include(latLng);
						}
						break;
					case HOME_PAGE_MAP_SNAPSHOT:
						for (LatLng latLng : MapUtils.getLatLngBoundsHomePageStaticMap()) {
							builder.include(latLng);
						}
						break;
					case EVENT_MAP:
					    // Move map to bounds to include entire UO area including WNW
					    for (LatLng latLng : MapUtils.getLatLngBoundsAreaEntirePark()) {
                            builder.include(latLng);
                        }
					    for (LatLng latLng : MapUtils.getLatLngBoundsWetNWild()) {
                            builder.include(latLng);
                        }
                        break;
					default:
						for (LatLng latLng : MapUtils.getLatLngBoundsAreaParks()) {
							builder.include(latLng);
						}
						break;
				}

				LatLngBounds latLngBounds = builder.build();
				int mapWidthInPx = mMapWrapperLayout.getWidth();
				int mapHeightInPx = mMapWrapperLayout.getHeight();

				int paddingInPx = getResources().getDimensionPixelSize(R.dimen.map_zoom_padding);

				// Move the map to the proper zoom that fits the area
				CameraUpdate cameraUpdate;

				// If west is up, intentionally flip width/height
				if (MapUtils.MAP_BEARING_DEGREES == 270.0f) {
					cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, mapHeightInPx, mapWidthInPx, paddingInPx);
				}
				// Otherwise, assume north is up
				else {
					cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, mapWidthInPx, mapHeightInPx, paddingInPx);
				}
				mGoogleMap.moveCamera(cameraUpdate);

				// Rotate the camera (if necessary)
				CameraPosition cameraPosition = new CameraPosition.Builder(mGoogleMap.getCameraPosition())
				.bearing(MapUtils.MAP_BEARING_DEGREES)
				.tilt(0)
				.build();
				mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}

			mHasMovedMapCameraToStart = true;
		}
	}

	private void animateMapCameraToUserLocationToStart() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "animateMapCameraToUserLocationToStart");
		}

		// If a POI has been selected, do not try to animate to the user
		if (mSelectedPoi != null) {
			setHasAnimatedMapCameraToUserLocationToStart(true);
			return;
		}

		// Find out if the the user's location needs to be zoomed in on for the map
		boolean animateToUserLocation;
		float defaultZoom = 0f;
		UniversalAppState state = UniversalAppStateManager.getInstance();
		switch (mExploreType) {
			case ISLANDS_OF_ADVENTURE:
				animateToUserLocation = state.isInIslandsOfAdventureGeofence();
				defaultZoom = 17.5f;
				break;
			case UNIVERSAL_STUDIOS_FLORIDA:
				animateToUserLocation = state.isInUniversalStudiosFloridaGeofence();
				defaultZoom = 17.5f;
				break;
			case CITY_WALK_ORLANDO:
				animateToUserLocation = state.isInCityWalkOrlandoGeofence();
				defaultZoom = 17.5f;
				break;
			case WET_N_WILD:
			    animateToUserLocation = state.isInWetNWildGeofence();
			    defaultZoom = 17.5f;
			    break;
			case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
				animateToUserLocation = state.isInUniversalStudiosHollywoodGeofence();
				defaultZoom = 18.5f;
				break;
			case CITY_WALK_HOLLYWOOD:
				animateToUserLocation = state.isInCityWalkHollywoodGeofence();
				defaultZoom = 18.5f;
				break;
			case ATMS:
			case CHARGING_STATIONS:
			case DINING:
			case FIRST_AID:
			case GUEST_SERVICES_BOOTHS:
			case LOCKERS:
			case LOST_AND_FOUND:
			case RESTROOMS:
			case SERVICE_ANIMAL_REST_AREAS:
			case SHOPPING:
			case RENTAL_SERVICES:
			case SHOPPING_EXPRESS_PASS:
			case SHOWS:
			case SMOKING_AREAS:
				if (BuildConfigUtils.isLocationFlavorHollywood()) {
					animateToUserLocation = state.isInUniversalStudiosHollywoodGeofence()
											|| state.isInCityWalkHollywoodGeofence();
					defaultZoom = 18.0f;
				} else {
					animateToUserLocation = state.isInIslandsOfAdventureGeofence()
											|| state.isInUniversalStudiosFloridaGeofence()
											|| state.isInCityWalkOrlandoGeofence();
					defaultZoom = 17.0f;
				}
				break;
			case RIDES:
			case WAIT_TIMES:
				if (BuildConfigUtils.isLocationFlavorHollywood()) {
					animateToUserLocation = state.isInUniversalStudiosHollywoodGeofence();
					defaultZoom = 18.0f;
				} else {
					animateToUserLocation = state.isInIslandsOfAdventureGeofence()
											|| state.isInUniversalStudiosFloridaGeofence();
					defaultZoom = 17.0f;
				}
				break;
			case SOLO_MAP_PAGE:
			case DETAIL_PAGE_MAP_SNAPSHOT:
			case HOME_PAGE_MAP_SNAPSHOT:
			case HOTELS:
			case FAVORITES:
				animateToUserLocation = false;
				break;
			default:
				animateToUserLocation = false;
				break;
		}

		// If the user's location shouldn't be zoomed in on, return
		if (!animateToUserLocation) {
			setHasAnimatedMapCameraToUserLocationToStart(true);
			return;
		}

		// If user location isn't available don't animate
		if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
			setHasAnimatedMapCameraToUserLocationToStart(true);
			return;
		}
        try {
            final Location userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (userLocation == null) {
                setHasAnimatedMapCameraToUserLocationToStart(true);
                return;
            }

            // Update the park geofences with the user's latest location
            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                GeofenceUtils.checkParkGeofences(userLocation);
            }

            // Animate the camera to the user location
            AnimateCameraToUserLocationThread thread = new AnimateCameraToUserLocationThread(
                    mGoogleMap, userLocation, defaultZoom, this);
            thread.start();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
	}

	private void animateMapCameraToLocation(Location location) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "animateMapCameraToLocation");
		}

		if (location != null && mGoogleMap != null && mGoogleMap.isMyLocationEnabled()) {
			LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

			CameraPosition cameraPosition = new CameraPosition.Builder(mGoogleMap.getCameraPosition())
			.target(userLatLng)
			.build();

			CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
			mGoogleMap.animateCamera(cameraUpdate, MAP_BOUNDS_CORRECTION_ANIMATION_DURATION_IN_MS, null);
		}
	}

	private void clickMarker(Long selectedPoiId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "clickMarker");
		}

		Integer poiHashCode = getSelectedPoiHashCode(mPoiMarkerSparseArray, selectedPoiId);
		if (poiHashCode != null) {
			clickMarker(poiHashCode);
		}
	}

	private void clickMarker(final int poiHashCode) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "clickMarker");
		}

		ClickMarkerThread thread = new ClickMarkerThread(this, poiHashCode);
		thread.start();
	}

    private Bitmap writeTextOnDrawable(int drawableId, String text) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_gotham_bold));

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(convertToPixels(11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        // If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4)) // the padding on either sides is considered as 4, so
                                                         // as to appropriately fit in the text
            paint.setTextSize(convertToPixels(7)); // Scaling needs to be used for different dpi's

        // Calculate the positions
        int xPos = canvas.getWidth() / 2 + textRect.width();
        int yPos = textRect.height() + (textRect.height() + 2);

        int circleColor = getResources().getColor(R.color.text_primary);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(convertToPixels(2));
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(xPos, yPos - (textRect.height() / 2), (textRect.height()) + 2, paint);
        paint.setStyle(Style.FILL);
        paint.setColor(circleColor);
        canvas.drawCircle(xPos, yPos - (textRect.height() / 2), (textRect.height()) + 1, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.FILL);
        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }

    private static int convertToPixels(int nDP) {
		Context context = UniversalOrlandoApplication.getAppContext();
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

	private static int getPoiPinResId(ExploreType exploreType, int poiTypeId, long subTypeFlags) {
		if (sPoiPinResIdMap == null) {
			sPoiPinResIdMap = new SparseIntArray();
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE, R.drawable.ic_map_pin_ride);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_DINING, R.drawable.ic_map_pin_dining);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW, R.drawable.ic_map_pin_show);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE, R.drawable.ic_map_pin_show);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP, R.drawable.ic_map_pin_shop);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS, R.drawable.ic_map_pin_amenity_rental_service);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT, R.drawable.ic_map_pin_entertainment);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL, R.drawable.ic_map_pin_hotel);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM, R.drawable.ic_map_pin_amenity_restroom);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_ATM, R.drawable.ic_map_pin_amenity_atm);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_CHARGING_STATION, R.drawable.ic_map_pin_amenity_charger);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_FIRST_AID_STATION, R.drawable.ic_map_pin_amenity_first_aid);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH, R.drawable.ic_map_pin_amenity_guest_service_booth);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS, R.drawable.ic_map_pin_amenity_locker_ride);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION, R.drawable.ic_map_pin_amenity_lost_and_found);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA, R.drawable.ic_map_pin_amenity_service_animal);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_SMOKING_AREA, R.drawable.ic_map_pin_amenity_smoking);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK, R.drawable.ic_map_pin_ride_water);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT, R.drawable.ic_map_pin_event);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY, R.drawable.ic_map_pin_amen_keyarea);
			sPoiPinResIdMap.put(PointsOfInterestTable.VAL_POI_TYPE_ID_GEN_LOCATION, R.drawable.ic_map_pin_amen_keyarea);
		}

		switch (exploreType) {

			case RIDES:
			case WAIT_TIMES:
				// Explore by activity - ride pins
			    // Checking water secondary sub-types first as they are also water sub-types
			    if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_SUPER_THRILL)) {
                    return R.drawable.ic_map_pin_super_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_GROUP_THRILL)) {
                    return R.drawable.ic_map_pin_group_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_FAMILY_THRILL)) {
                    return R.drawable.ic_map_pin_family_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_EXTRAS)) {
                    return R.drawable.ic_map_pin_water_extras;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_THRILL)) {
					return R.drawable.ic_map_pin_ride;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER)) {
					return R.drawable.ic_map_pin_ride_water;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_VIDEO_3D_4D)) {
					return R.drawable.ic_map_pin_ride_3d;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_KID_FRIENDLY)) {
					return R.drawable.ic_map_pin_ride_kids;
				}
				else {
                    return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_ride);
                }

			case SHOWS:
				// Explore by activity - show pins
				if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_ACTION)) {
					return R.drawable.ic_map_pin_show_action;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_COMEDY)) {
					return R.drawable.ic_map_pin_show_comedy;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_MUSIC)) {
					return R.drawable.ic_map_pin_show_music;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_PARADE)) {
					return R.drawable.ic_map_pin_show_parade;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_SHOW_TYPE_CHARACTER)) {
					return R.drawable.ic_map_pin_show_character;
				}
				else {
					return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_show);
				}

			case DINING:
				// Explore by activity - dining pins
				if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_FINE_DINING)) {
					return R.drawable.ic_map_pin_dining_fine;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_CASUAL_DINING)) {
					return R.drawable.ic_map_pin_dining_casual;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_DINING_TYPE_QUICK_SERVICE)) {
					return R.drawable.ic_map_pin_dining_quick;
				}
				else {
					return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_dining);
				}

			case SHOPPING:
				// Explore by activity - show pins
				return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_shop);

			case RESTROOMS:
				// Explore by activity - restroom pins
				if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_STANDARD)) {
					return R.drawable.ic_map_pin_amenity_restroom;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_FAMILY)) {
					return R.drawable.ic_map_pin_amenity_restroom_family;
				}
				else {
					return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_amenity_restroom);
				}

			case LOCKERS:
				// Explore by activity - locker pins
				if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RENTAL)) {
					return R.drawable.ic_map_pin_amenity_locker_rental;
				}
				else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RIDE)) {
					return R.drawable.ic_map_pin_amenity_locker_ride;
				}
				else {
					return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_amenity_locker_ride);
				}

			case WET_N_WILD:
			    // Checking water secondary sub-types first as they are also water sub-types
                if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_SUPER_THRILL)) {
                    return R.drawable.ic_map_pin_super_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_GROUP_THRILL)) {
                    return R.drawable.ic_map_pin_group_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_FAMILY_THRILL)) {
                    return R.drawable.ic_map_pin_family_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_EXTRAS)) {
                    return R.drawable.ic_map_pin_water_extras;
                } else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_TYPE_WATER)) {
                    return R.drawable.ic_map_pin_ride_water;
                } else {
                    return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_ride);
                }
			case SOLO_MAP_PAGE:
			case DETAIL_PAGE_MAP_SNAPSHOT:
			    // Checking water secondary sub-types first as they are also water sub-types
                if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_SUPER_THRILL)) {
                    return R.drawable.ic_map_pin_super_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_GROUP_THRILL)) {
                    return R.drawable.ic_map_pin_group_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_FAMILY_THRILL)) {
                    return R.drawable.ic_map_pin_family_thrill;
                }
                else if (PointOfInterest.isSubType(subTypeFlags, PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RIDE_WATER_EXTRAS)) {
                    return R.drawable.ic_map_pin_water_extras;
                } else {
                    return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_ride);
                }

			default:
				// Otherwise, get the pin based on the POI type
				return sPoiPinResIdMap.get(poiTypeId, R.drawable.ic_map_pin_ride);
		}
	}

	private static boolean isRestroomToggleVisible(ExploreType exploreType) {
		switch (exploreType) {
			case ISLANDS_OF_ADVENTURE:
			case UNIVERSAL_STUDIOS_FLORIDA:
			case CITY_WALK_ORLANDO:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
			case CITY_WALK_HOLLYWOOD:
				return true;
			default:
				return false;
		}
	}

	private static boolean isLocateMeButtonVisible(ExploreType exploreType) {
		switch (exploreType) {
			case DETAIL_PAGE_MAP_SNAPSHOT:
			case HOME_PAGE_MAP_SNAPSHOT:
				return false;
			default:
				return true;
		}
	}

	private static boolean isMyLocationAllowed(ExploreType exploreType, boolean isInPark) {
		switch (exploreType) {
			case HOME_PAGE_MAP_SNAPSHOT:
			case DETAIL_PAGE_MAP_SNAPSHOT:
				return false;
			default:
				return isInPark;
		}
	}

	private static PointOfInterestMarker getSelectedPoiMarker(SparseArray<PointOfInterestMarker> poiMarkerSparseArray, Long selectedPoiId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getSelectedPoiMarker");
		}

		if (selectedPoiId != null) {
			for (int i = 0; i < poiMarkerSparseArray.size(); i++) {
				PointOfInterestMarker poiMarker = poiMarkerSparseArray.valueAt(i);

				// Track any markers that aren't in the latest POI list
				if (selectedPoiId.equals(poiMarker.getPoiId())) {
					return poiMarker;
				}
			}
		}
		return null;
	}

	private static Marker getSelectedMarker(SparseArray<PointOfInterestMarker> poiMarkerSparseArray, Long selectedPoiId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getSelectedMarker");
		}

		PointOfInterestMarker poiMarker = getSelectedPoiMarker(poiMarkerSparseArray, selectedPoiId);
		if (poiMarker != null) {
			return poiMarker.getMarker();
		}
		return null;
	}

	private static PointOfInterest getSelectedPoi(SparseArray<PointOfInterestMarker> poiMarkerSparseArray, Long selectedPoiId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getSelectedPoi");
		}

		PointOfInterestMarker poiMarker = getSelectedPoiMarker(poiMarkerSparseArray, selectedPoiId);
		if (poiMarker != null) {
			return poiMarker.getPointOfInterest();
		}
		return null;
	}

	private static Integer getSelectedPoiHashCode(SparseArray<PointOfInterestMarker> poiMarkerSparseArray, Long selectedPoiId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getSelectedPoiHashCode");
		}

		PointOfInterestMarker poiMarker = getSelectedPoiMarker(poiMarkerSparseArray, selectedPoiId);
		if (poiMarker != null) {
			return poiMarker.getPoiHashCode();
		}
		return null;
	}


	// Private class using weak references to prevent leaking a context
	private static final class ClickMarkerThread extends Thread {
		private final WeakReference<ExploreMapFragment> mExploreMapFragment;
		private final int mPoiHashCode;

		public ClickMarkerThread(ExploreMapFragment exploreMapFragment, int poiHashCode) {
			mExploreMapFragment = new WeakReference<ExploreMapFragment>(exploreMapFragment);
			mPoiHashCode = poiHashCode;
		}

		@Override
		public void run() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "ClickMarkerThread:run()");
			}

			// Wait for the map camera to settle off the main thread
			try {
				Thread.sleep(MAP_PIN_CLICK_DELAY);
			}
			catch (InterruptedException e) {
			}

			final ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();

			// Check if references are still alive
			if (exploreMapFragment != null) {
				Activity parentActivity = exploreMapFragment.getActivity();

				if (parentActivity != null) {
					// Then select the marker on the UI thread
					ClickMarkerRunnable runnable = new ClickMarkerRunnable(exploreMapFragment, mPoiHashCode);
					parentActivity.runOnUiThread(runnable);
				}
			}
		}
	}

	// Private class using weak references to prevent leaking a context
	private static final class ClickMarkerRunnable implements Runnable {
		private final WeakReference<ExploreMapFragment> mExploreMapFragment;
		private final int mPoiHashCode;

		public ClickMarkerRunnable(ExploreMapFragment exploreMapFragment, int poiHashCode) {
			mExploreMapFragment = new WeakReference<ExploreMapFragment>(exploreMapFragment);
			mPoiHashCode = poiHashCode;
		}

		@Override
		public void run() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "ClickMarkerRunnable:run()");
			}

			final ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();

			// Check if references are still alive
			if (exploreMapFragment != null && exploreMapFragment.mPoiMarkerSparseArray != null) {
				PointOfInterestMarker poiMarker = exploreMapFragment.mPoiMarkerSparseArray.get(mPoiHashCode);
				if (poiMarker != null) {
					Marker marker = poiMarker.getMarker();
					// Fake a marker click to center on the marker and open its
					// window
					if (marker != null) {
						exploreMapFragment.onMarkerClick(marker);
					}
				}
			}
		}
	}

	// Private class using weak references to prevent leaking a context
	private static final class AnimateCameraToUserLocationThread extends Thread {
		private final WeakReference<GoogleMap> mGoogleMap;
		private final Location mUserLocation;
		private final float mUserLocationZoom;
		private final WeakReference<ExploreMapFragment> mExploreMapFragment;

		public AnimateCameraToUserLocationThread(GoogleMap googleMap, Location userLocation,
				float userLocationZoom, ExploreMapFragment exploreMapFragment) {
			mGoogleMap = new WeakReference<GoogleMap>(googleMap);
			mUserLocation = userLocation;
			mUserLocationZoom = userLocationZoom;
			mExploreMapFragment = new WeakReference<ExploreMapFragment>(exploreMapFragment);
		}

		@Override
		public void run() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "AnimateCameraToUserLocationThread:run()");
			}

			// Wait for the map camera to settle off the main thread
			try {
				Thread.sleep(MAP_ANIMATION_DELAY_IN_MS);
			}
			catch (InterruptedException e) {
				ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();
				if (exploreMapFragment != null) {
					exploreMapFragment.setHasAnimatedMapCameraToUserLocationToStart(true);
				}
				return;
			}

			final GoogleMap googleMap = mGoogleMap.get();
			final ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();

			// Check to see the references are still alive
			if (googleMap != null && mUserLocation != null && exploreMapFragment != null) {
				Activity parentActivity = exploreMapFragment.getActivity();
				if (parentActivity == null) {
					exploreMapFragment.setHasAnimatedMapCameraToUserLocationToStart(true);
					return;
				}

				// Then animate to the user location
				AnimateCameraToUserLocationRunnable runnable = new AnimateCameraToUserLocationRunnable(
						googleMap, mUserLocation, mUserLocationZoom, exploreMapFragment);
				parentActivity.runOnUiThread(runnable);
			}
		}
	}

	// Private class using weak references to prevent leaking a context
	private static final class AnimateCameraToUserLocationRunnable implements Runnable {
		private final WeakReference<GoogleMap> mGoogleMap;
		private final Location mUserLocation;
		private final float mUserLocationZoom;
		private final WeakReference<ExploreMapFragment> mExploreMapFragment;

		public AnimateCameraToUserLocationRunnable(GoogleMap googleMap, Location userLocation,
				float userLocationZoom, ExploreMapFragment exploreMapFragment) {
			mGoogleMap = new WeakReference<GoogleMap>(googleMap);
			mUserLocation = userLocation;
			mUserLocationZoom = userLocationZoom;
			mExploreMapFragment = new WeakReference<ExploreMapFragment>(exploreMapFragment);
		}

		@Override
		public void run() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "AnimateCameraToUserLocationRunnable:run()");
			}

			final GoogleMap googleMap = mGoogleMap.get();
			final ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();

			// Check to see the references are still alive
			if (googleMap != null && mUserLocation != null && exploreMapFragment != null) {
				LatLng userLatLng = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());

				CameraPosition cameraPosition = new CameraPosition.Builder(googleMap.getCameraPosition())
				.target(userLatLng)
				.zoom(mUserLocationZoom)
				.bearing(MapUtils.MAP_BEARING_DEGREES)
				.tilt(0)
				.build();

				CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
				AnimateCameraToUserLocationCallback callback = new AnimateCameraToUserLocationCallback(exploreMapFragment);
				googleMap.animateCamera(cameraUpdate, MAP_BOUNDS_CORRECTION_ANIMATION_DURATION_IN_MS, callback);
			}
		}
	}

	// Private class using weak references to prevent leaking a context
	private static final class AnimateCameraToUserLocationCallback implements CancelableCallback {
		private final WeakReference<ExploreMapFragment> mExploreMapFragment;

		public AnimateCameraToUserLocationCallback(ExploreMapFragment exploreMapFragment) {
			mExploreMapFragment = new WeakReference<ExploreMapFragment>(exploreMapFragment);
		}

		@Override
		public void onCancel() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "AnimateCameraToUserLocationCallback:onCancel()");
			}

			final ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();

			// Check to see the references are still alive
			if (exploreMapFragment != null) {
				exploreMapFragment.setHasAnimatedMapCameraToUserLocationToStart(true);
			}
		}

		@Override
		public void onFinish() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "AnimateCameraToUserLocationCallback:onFinish()");
			}

			final ExploreMapFragment exploreMapFragment = mExploreMapFragment.get();

			// Check to see the references are still alive
			if (exploreMapFragment != null) {
				exploreMapFragment.setHasAnimatedMapCameraToUserLocationToStart(true);
			}
		}
	}
}
