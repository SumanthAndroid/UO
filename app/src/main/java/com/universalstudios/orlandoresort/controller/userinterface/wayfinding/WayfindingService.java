package com.universalstudios.orlandoresort.controller.userinterface.wayfinding;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkSenderService;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.GetWayfindingRouteRequest;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.GetWayfindingRouteResponse;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Path;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Waypoint;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.settings.UniversalOrlandoSettings;
import com.universalstudios.orlandoresort.model.state.settings.UniversalOrlandoSettingsManager;
import com.universalstudios.orlandoresort.model.state.wayfinding.WayfindingState;
import com.universalstudios.orlandoresort.model.state.wayfinding.WayfindingStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WayfindingService extends NetworkSenderService implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener, Target {
	private static final String TAG = WayfindingService.class.getSimpleName();

	private static final long LOCATION_UPDATE_INTERVAL_MS = 2 * 1000;
	private static final long LOCATION_FASTEST_UPDATE_INTERVAL_MS = LOCATION_UPDATE_INTERVAL_MS;
	private static final long LOCATION_SMALLEST_DISPLACEMENT_METERS = 0;
	private static final float LOCATION_ACCURACY_RADIUS_SCALER = 5.0f;
	private static final float WAYPOINT_ACCURACY_RADIUS_SCALER = 1.4f;
	private static final int CONSECUTIVE_LOCATION_UPDATES_OFF_PATH_BEFORE_REROUTE = 6;
	private static final long VIBRATE_ON_MS = 350;
	private static final long VIBRATE_OFF_MS = VIBRATE_ON_MS / 2;

	private static final String KEY_ARG_STOP_SERVICE = "KEY_ARG_STOP_SERVICE";
	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_SELECTED_POI_TYPE_ID = "KEY_ARG_SELECTED_POI_TYPE_ID";
	private static final String KEY_ARG_RELOAD_ROUTE = "KEY_ARG_RELOAD_ROUTE";

	private boolean mReloadRoute;
	private int mSelectedPoiTypeId;
	private NotificationManager mNotificationManager;
	private GoogleApiClient mGoogleApiClient;
	private String mSelectedPoiObjectJson;
	private PointOfInterest mSelectedPoi;
	private LocationRequest mLocationRequest;
	private NotificationCompat.Builder mNotificationBuilder;
	private String mImageSizeParam;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private Picasso mPicasso;
	private Bitmap mLargeIconBitmap;

	public static Bundle newInstanceBundle(String selectedPoiObjectJson, int selectedPoiTypeId, boolean reloadRoute) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();
		args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		args.putInt(KEY_ARG_SELECTED_POI_TYPE_ID, selectedPoiTypeId);
		args.putBoolean(KEY_ARG_RELOAD_ROUTE, reloadRoute);

		return args;
	}

	public static Bundle newInstanceBundle(boolean stopService) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();
		args.putBoolean(KEY_ARG_STOP_SERVICE, stopService);

		return args;
	}

	public WayfindingService() {
		super();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "WayfindingService constructor");
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate");
		}

		// Create location client
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		mLocationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(LOCATION_UPDATE_INTERVAL_MS)
				.setFastestInterval(LOCATION_FASTEST_UPDATE_INTERVAL_MS)
				.setSmallestDisplacement(LOCATION_SMALLEST_DISPLACEMENT_METERS);

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(this)
		.debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();

		mImageSizeParam = ImageUtils.getPoiImageSizeString(getResources().getInteger(R.integer.poi_search_image_dpi_shift));
		mLargeIconBitmap = null;

		// Default wayfinding state
		WayfindingState state = WayfindingStateManager.getInstance();
		state.setPaths(null);
		state.setWaypoints(null);
		state.setOnPathWaypoints(null);
		state.setOffPathWaypoints(null);
		state.setHasRequestedRoute(false);
		state.setDidRouteRequestSucceed(null);
		state.setCurrentStepIndex(0);
		state.setIsServiceRunning(true);
		state.setRouteRequestTag(null);
		state.setOffPathUpdatesInARow(0);
		state.setHasFinishedRoute(false);
		state.setIsLocationServicesConnected(null);
		state.setIsGpsEnabled(null);

		// Sync UI with latest state
		WayfindingStateManager.notifyStateChangeListeners();

		// Get the notification manager to update notifications
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Set the notification text
		mNotificationBuilder = NotificationUtils.createWayfindingNotification(
				getString(R.string.wayfinding_stat_starting_title), getString(R.string.wayfinding_stat_starting_content),
				getString(R.string.wayfinding_stat_starting_ticker),
				getString(R.string.wayfinding_stat_starting_title), getString(R.string.wayfinding_stat_starting_content),
				mLargeIconBitmap, getPlaySoundBasedOnSettings(false), getVibratePatternBasedOnSettings(0),
				!state.isWayfindingScreenShowing());

		// Start service in foreground
		startForeground(NotificationUtils.NOTIFICATION_ID_WAYFINDING, mNotificationBuilder.build());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStartCommand");
		}

		// Get incoming args
		boolean stopService;
		Bundle args = intent.getExtras();
		if (args == null) {
			mSelectedPoiObjectJson = null;
			mSelectedPoiTypeId = -1;
			mSelectedPoi = null;
			mReloadRoute = false;
			stopService = false;
		}
		// Otherwise, set incoming parameters
		else {
			mSelectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
			mSelectedPoiTypeId = args.getInt(KEY_ARG_SELECTED_POI_TYPE_ID);
			mSelectedPoi = PointOfInterest.fromJson(mSelectedPoiObjectJson, mSelectedPoiTypeId);
			mReloadRoute = args.getBoolean(KEY_ARG_RELOAD_ROUTE, false);
			stopService = args.getBoolean(KEY_ARG_STOP_SERVICE, false);
		}

		// If the stop message was sent, stop the service
		if (stopService) {
			stopSelf();
		}
		// Otherwise start the wayfinding logic
		else {
			// First, connect to the location client, if it isn't already connected
			if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}

			// Load thumbnail image
			if (mSelectedPoi != null) {
				Uri thumbNailImageUri = null;
				try {
					thumbNailImageUri = Uri.parse(mSelectedPoi.getThumbnailImageUrl());
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onStartCommand: invalid image URL: " + thumbNailImageUri, e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}

				if (thumbNailImageUri != null) {
					Uri imageUriToLoad = thumbNailImageUri.buildUpon()
							.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
							.build();

					if (BuildConfig.DEBUG) {
						Log.d(TAG, "onStartCommand: imageUriToLoad = " + imageUriToLoad);
					}

					mPicasso.load(imageUriToLoad)
					.resizeDimen(android.R.dimen.notification_large_icon_width, android.R.dimen.notification_large_icon_height)
					.into(this);
				}
			}
		}

		// Do not restart the service if it is killed
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onBind");
		}

		// This is a started service, don't bind it
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}

		// Track the event
		String attractionName = mSelectedPoi != null ? mSelectedPoi.getDisplayName() : null;
		AnalyticsUtils.trackEvent(
				attractionName,
				AnalyticsUtils.EVENT_NAME_GUIDE_ME_EXIT,
				null,
				null);

		WayfindingState state = WayfindingStateManager.getInstance();
		state.setIsServiceRunning(false);
		WayfindingStateManager.notifyStateChangeListeners();

		// Disconnect the current location client
		if (mGoogleApiClient != null && (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting())) {
			mGoogleApiClient.disconnect();
		}

		// Remove the ongoing notification
		if (mNotificationManager != null) {
			mNotificationManager.cancel(NotificationUtils.NOTIFICATION_ID_WAYFINDING);
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

		WayfindingState state = WayfindingStateManager.getInstance();
		state.setIsLocationServicesConnected(true);
		state.setIsGpsEnabled(checkIfGpsIsEnabled());

		if (state.getIsGpsEnabled() != null
				&& !state.getIsGpsEnabled().booleanValue()) {
			// Update the notification that GPS is disabled
			updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_no_gps_content),
					getString(R.string.wayfinding_stat_no_gps_ticker), true, 2);
			WayfindingStateManager.notifyStateChangeListeners();
		}
		else {
			// Register for location updates
			try {
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onConnectionFailed: connectionResult.getErrorCode() = " + connectionResult.getErrorCode());
		}


		WayfindingState state = WayfindingStateManager.getInstance();

		// Update the notification that location can't be acquired
		Boolean isLocationServicesConnected = state.getIsLocationServicesConnected();
		if (isLocationServicesConnected == null || isLocationServicesConnected.booleanValue()) {
			state.setIsLocationServicesConnected(false);
			updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_no_location_services_content),
					getString(R.string.wayfinding_stat_no_location_services_ticker), true, 2);

			WayfindingStateManager.notifyStateChangeListeners();
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDisconnected");
		}

		WayfindingState state = WayfindingStateManager.getInstance();

		Boolean isLocationServicesConnected = state.getIsLocationServicesConnected();
		if (isLocationServicesConnected == null || isLocationServicesConnected.booleanValue()) {
			state.setIsLocationServicesConnected(false);

			// Update the notification that GPS is disabled
			updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_no_location_services_content),
					getString(R.string.wayfinding_stat_no_location_services_ticker), true, 2);

			WayfindingStateManager.notifyStateChangeListeners();
		}

		// Unregister for location updates
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLocationChanged: lat = " + location.getLatitude()
                    + " lon = " + location.getLongitude()
                    + " accuracy = " + location.getAccuracy());
		}

		WayfindingState state = WayfindingStateManager.getInstance();

		// Check the GPS state, and short circuit if it isn't on
		Boolean wasGpsEnabled = state.getIsGpsEnabled();
		boolean isGpsEnabled = checkIfGpsIsEnabled();
		state.setIsGpsEnabled(isGpsEnabled);
		if (!isGpsEnabled) {
			// Update the notification that GPS is disabled only if it wasn't already updated
			if (wasGpsEnabled == null || wasGpsEnabled.booleanValue()) {
				updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_no_gps_content),
						getString(R.string.wayfinding_stat_no_gps_ticker), true, 2);
				WayfindingStateManager.notifyStateChangeListeners();
			}
			return;
		}

		// If the route hasn't been requested, or a retry was requested, make the request
		if (!state.hasRequestedRoute() || mReloadRoute) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onLocationChanged: making network request for a route");
			}
			// Only reload a route once
			mReloadRoute = false;

			// Make a network request to get a route
			String routeRequestTag = makeRouteRequest(location);

			// Sync the state to the UI
			state.setHasRequestedRoute(true);
			state.setDidRouteRequestSucceed(null);
			state.setRouteRequestTag(routeRequestTag);
			state.setPaths(null);
			state.setWaypoints(null);
			state.setOnPathWaypoints(null);
			state.setOffPathWaypoints(null);
			state.setCurrentStepIndex(0);
			state.setOffPathUpdatesInARow(0);
			state.setHasFinishedRoute(false);
			state.setIsLocationServicesConnected(null);
			state.setIsGpsEnabled(null);
			WayfindingStateManager.notifyStateChangeListeners();

			// Update the notification
			updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_starting_content),
					getString(R.string.wayfinding_stat_starting_ticker), false, 0);
		}
		// If there is a valid route, and the user hasn't finished it, check the user's status on it
		else if (state.hasRequestedRoute() && state.getDidRouteRequestSucceed() != null
				&& state.getDidRouteRequestSucceed().booleanValue()
				&& state.getPaths() != null && state.getPaths().size() > 0
				&& !state.hasFinishedRoute()) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onLocationChanged: checking if user is inside a waypoint");
			}

			// Check to see if the user is in a waypoint on the route,
			// preferring the furthest along waypoint
			int onPathWaypointIndex = findLastWaypointIndexInIfLocationIsInside(location, state.getOnPathWaypoints());
			if (onPathWaypointIndex >= 0 && onPathWaypointIndex != state.getCurrentStepIndex()) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onLocationChanged: user is in a new waypoint on path, updating UI, onPathWaypointIndex = " + onPathWaypointIndex);
				}

				// If the current step is the last step, track that the user has finished and disconnect from any more location updates
				if (state.getPaths() != null && onPathWaypointIndex == state.getPaths().size() - 1) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLocationChanged: user has reached the end of the route, onPathWaypointIndex = " + onPathWaypointIndex);
					}
					state.setHasFinishedRoute(true);
					if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
						LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
						mGoogleApiClient.disconnect();
					}

					// Track the page view
					String attractionName = mSelectedPoi != null ? mSelectedPoi.getDisplayName() : null;
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_GUIDE_ME,
							null,
							"" + attractionName + " " + AnalyticsUtils.CONTENT_SUB_2_GUIDE_ME_ARRIVAL,
							null,
							attractionName,
							null);
				}

				// Update the state and the notification
				state.setOffPathUpdatesInARow(0);
				state.setCurrentStepIndex(onPathWaypointIndex);
				updateNotificationWithCurrentStep(state.getCurrentStepIndex(), state.getPaths(), true, 1);

				// Sync the state to the UI
				WayfindingStateManager.notifyStateChangeListeners();
			}
			else {
				// Check to see if the user is in a waypoint off the route
				int offPathWaypointIndex = findFirstWaypointIndexInIfLocationIsInside(location, state.getOffPathWaypoints());
				if (offPathWaypointIndex >= 0) {
					// Increment the off path updates
					state.setOffPathUpdatesInARow(state.getOffPathUpdatesInARow() + 1);

					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLocationChanged: user is in a waypoint off path, off path updates = " + state.getOffPathUpdatesInARow()
								+ " offPathWaypointIndex = " + offPathWaypointIndex);
					}

					// Check to see if the user has been off path long enough to trigger reroute
					if (state.getOffPathUpdatesInARow() >= CONSECUTIVE_LOCATION_UPDATES_OFF_PATH_BEFORE_REROUTE) {
						if (BuildConfig.DEBUG) {
							Log.w(TAG, "onLocationChanged: off path threshold reached, triggering a reroute");
						}

						// Make a network request to get a new route
						String routeRequestTag = makeRouteRequest(location);

						// Sync the state to the UI
						state.setHasRequestedRoute(true);
						state.setDidRouteRequestSucceed(null);
						state.setRouteRequestTag(routeRequestTag);
						state.setPaths(null);
						state.setWaypoints(null);
						state.setOnPathWaypoints(null);
						state.setOffPathWaypoints(null);
						state.setCurrentStepIndex(0);
						state.setOffPathUpdatesInARow(0);
						state.setHasFinishedRoute(false);
						state.setIsLocationServicesConnected(null);
						state.setIsGpsEnabled(null);
						WayfindingStateManager.notifyStateChangeListeners();

						// Update the notification
						updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_rerouting_content),
								getString(R.string.wayfinding_stat_rerouting_ticker), true, 2);
					}
				}
			}
		}
	}

	@Override
	public void handleNetworkResponse(NetworkResponse networkResponse) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleNetworkResponse");
		}

		if (networkResponse instanceof GetWayfindingRouteResponse) {
			GetWayfindingRouteResponse response = (GetWayfindingRouteResponse) networkResponse;

			WayfindingState state = WayfindingStateManager.getInstance();

			if (response.isHttpStatusCodeSuccess()) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "handleNetworkResponse: GetWayfindingRouteResponse = success");
				}

                /*
				List<Waypoint> waypoints = response.getWaypoints();
				Waypoint startingWaypoint = waypoints.get(0);
				Waypoint finalWaypoint = waypoints.get(waypoints.size() - 1);
				long startingVenueId = startingWaypoint.getVenueId();
				long endingVenueId = finalWaypoint.getVenueId();

				DatabaseQuery mVenueDatabaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(startingVenueId);
				mVenueDatabaseQuery.toJson();
                String venueObjectJson = data.getString(data.getColumnIndex(UniversalOrlandoDatabaseTables.VenuesTable.COL_VENUE_OBJECT_JSON));
                Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);

                //MVELIE US1148

                Venue startingVenue;
                Venue endingVenue;

                //if neither requires, don't do anything
                if(!startingVenue.getAdmissionRequired() && !endingVenue.getAdmissionRequired()) {

                }
                //if both require admission, require park-to-park admission
                else if(startingVenue.getAdmissionRequired() && endingVenue.getAdmissionRequired()) {
                    //Park-to-Park Admission
                    //Your walking directions require a Park-to-Park ticket.
                    //continue if ok, quit if not
                }
                //if destination require admission, show one park ticket
                else if(!startingVenue.getAdmissionRequired() && endingVenue.getAdmissionRequired()) {
                    String parkName = endingVenue.getDisplayName();
                    //Single Park Admission
                    //Your walking directions require a ticket to {destination park name}.
                    //continue if ok, quit if not

                }
*/

				// Protect against null data
				List<Path> newPaths = response.getPaths();
				if (newPaths == null) {
					newPaths = new ArrayList<Path>();
				}
				List<Waypoint> newWaypoints = response.getWaypoints();
				if (newWaypoints == null) {
					newWaypoints = new ArrayList<Waypoint>();
				}

				// Set up the route state
				state.setPaths(newPaths);
				state.setWaypoints(newWaypoints);
				state.setOnPathWaypoints(new ArrayList<Waypoint>(newPaths.size()));
				for (int i = 0; i < newPaths.size(); i++) {
					state.getOnPathWaypoints().add(null);
				}
				state.setOffPathWaypoints(new ArrayList<Waypoint>());
				state.setDidRouteRequestSucceed(true);
				state.setCurrentStepIndex(0);
				state.setOffPathUpdatesInARow(0);

				// Go through all of the waypoints and group them into on and off path lists
				for (Waypoint waypoint : state.getWaypoints()) {
					if (waypoint != null) {
						int waypointIndexInPaths = findWaypointIndexInPaths(waypoint.getId(), state.getPaths());
						if (waypointIndexInPaths >= 0) {
							state.getOnPathWaypoints().set(waypointIndexInPaths, waypoint);
						}
						else {
							state.getOffPathWaypoints().add(waypoint);
						}
					}
				}

				// If the current step is the last step, track that the user has
				// finished and disconnect from any more location updates
				state.setHasFinishedRoute(state.getCurrentStepIndex() == (state.getPaths().size() - 1));
				if (state.hasFinishedRoute()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "handleNetworkResponse: route is a single step");
					}

					if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
						LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
						mGoogleApiClient.disconnect();
					}
				}

				if (BuildConfig.DEBUG) {
					Log.d(TAG, "handleNetworkResponse: paths = " + state.getPaths().size()
							+ " waypoints = " + state.getWaypoints().size()
							+ " onPathWaypoints = " + state.getOnPathWaypoints().size()
							+ " offPathWaypoints = " + state.getOffPathWaypoints().size());

					for (int i = 0; i < state.getOnPathWaypoints().size(); i++) {
						Waypoint waypointOnPath = state.getOnPathWaypoints().get(i);
						String latLonText = "null";
						if (waypointOnPath != null) {
							latLonText = waypointOnPath.getLatitude() + ", " + waypointOnPath.getLongitude();
						}
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "handleNetworkResponse: waypointOnPath " + i + " = " + latLonText);
						}
					}
				}

				// Update the notification and inform the UI
				if (state.getPaths().size() > 0) {
					updateNotificationWithCurrentStep(state.getCurrentStepIndex(), state.getPaths(), false, 0);

					// Track the page view
					String attractionName = mSelectedPoi != null ? mSelectedPoi.getDisplayName() : null;
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_GUIDE_ME,
							null,
							"" + attractionName + " " + AnalyticsUtils.CONTENT_SUB_2_GUIDE_ME_DEPARTURE,
							null,
							attractionName,
							null);
				}
				// If the route is empty, notify the user
				else {
					updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_no_route_content),
							getString(R.string.wayfinding_stat_no_route_ticker), false, 0);
				}

				// Sync the state to the UI
				WayfindingStateManager.notifyStateChangeListeners();
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "handleNetworkResponse: GetWayfindingRouteResponse = failure");
				}

				state.setDidRouteRequestSucceed(false);

				// Update the notification that loading the route failed
				updateNotificationWithLoadingInfo(getString(R.string.wayfinding_stat_get_route_failed_content),
						getString(R.string.wayfinding_stat_get_route_failed_ticker), false, 0);

				// Sync the state to the UI
				WayfindingStateManager.notifyStateChangeListeners();
			}
		}
	}

	@Override
	public void onBitmapFailed(Drawable errorDrawable) {
		mLargeIconBitmap = null;
	}

	@Override
	public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
		mLargeIconBitmap = bitmap;
	}

	@Override
	public void onPrepareLoad(Drawable placeHolderDrawable) {
		mLargeIconBitmap = null;
	}

	private boolean checkIfGpsIsEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isGpsEnabled = false;
		if (locationManager != null) {
			try {
				isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "checkIfGpsIsEnabled: unable to detect if GPS is on", e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
		return isGpsEnabled;
	}

	private String makeRouteRequest(Location location) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "makeRouteRequest");
		}

		// Make a wayfinding request
		GetWayfindingRouteRequest getWayfindingRouteRequest = new GetWayfindingRouteRequest.Builder(this)
		.setStartLatitude(location.getLatitude())
		.setStartLongitude(location.getLongitude())
		.setPoiId(mSelectedPoi.getId())
		.setWaypointOption(GetWayfindingRouteRequest.WAYPOINT_OPTION_ALL)
		.build();

		NetworkUtils.queueNetworkRequest(getWayfindingRouteRequest);
		NetworkUtils.startNetworkService();

		return getWayfindingRouteRequest.getRequestTag();
	}

	private void updateNotificationWithLoadingInfo(String contentText, String tickerText, boolean playSound, int vibrates) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "updateNotificationWithLoadingInfo: contentText = " + contentText
					+ " tickerText = " + tickerText
					+ " playSound = " + playSound);
		}

		WayfindingState state = WayfindingStateManager.getInstance();
		String contentTitle = getNotificationContentTitle(getString(R.string.wayfinding_stat_starting_title));

		// Set the text that will change
		mNotificationBuilder = NotificationUtils.createWayfindingNotification(
				contentTitle, contentText, tickerText,
				contentTitle, contentText,
				mLargeIconBitmap, getPlaySoundBasedOnSettings(playSound), getVibratePatternBasedOnSettings(vibrates),
				!state.isWayfindingScreenShowing());
		mNotificationManager.notify(NotificationUtils.NOTIFICATION_ID_WAYFINDING, mNotificationBuilder.build());
	}

	private void updateNotificationWithCurrentStep(int currentStepIndex, List<Path> paths, boolean playSound, int vibrates) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "updateNotificationWithCurrentStep: currentStepIndex = " + currentStepIndex
					+ " playSound = " + playSound);
		}

		String contentTitle = getNotificationContentTitle(getString(R.string.wayfinding_stat_starting_title));
		String contentText = "";

		if (paths != null && currentStepIndex < paths.size() && currentStepIndex >= 0) {
			Path currentPath = paths.get(currentStepIndex);
			if (currentPath != null) {
				String instructions = currentPath.getInstructions();
				if (instructions == null || instructions.isEmpty()) {
					instructions = getString(R.string.wayfinding_step_instructions_not_available);
				}

				String stepFormat = getString(R.string.wayfinding_step_x_of_x_notification);
				String stepText = String.format(stepFormat, currentStepIndex + 1, paths.size());
				contentText = stepText + ": " + instructions;
			}
		}
		String tickerText = contentText;

		WayfindingState state = WayfindingStateManager.getInstance();

		// Set the text that will change
		mNotificationBuilder = NotificationUtils.createWayfindingNotification(
				contentTitle, contentText, tickerText,
				contentTitle, contentText,
				mLargeIconBitmap, getPlaySoundBasedOnSettings(playSound), getVibratePatternBasedOnSettings(vibrates),
				!state.isWayfindingScreenShowing());
		mNotificationManager.notify(NotificationUtils.NOTIFICATION_ID_WAYFINDING, mNotificationBuilder.build());
	}

	private String getNotificationContentTitle(String fallbackContentTitle) {

		String contentTitle = fallbackContentTitle;
		if (mSelectedPoi != null) {
			String destination = mSelectedPoi.getDisplayName();
			contentTitle = destination != null ? destination : fallbackContentTitle;
		}
		return contentTitle;
	}

	private boolean getPlaySoundBasedOnSettings(boolean playSound) {

		// Only play a sound if the user has the setting on
		UniversalOrlandoSettings settings = UniversalOrlandoSettingsManager.getInstance();
		if (settings.getIsGuideMeSoundOn()) {
			return playSound;
		}
		return false;
	}

	private long[] getVibratePatternBasedOnSettings(int repeats) {

		// Only vibrate if the user has the setting on
		UniversalOrlandoSettings settings = UniversalOrlandoSettingsManager.getInstance();
		if (repeats > 0 && settings.getIsGuideMeVibrateOn()) {
			long[] pattern = new long [repeats * 2];
			for (int i = 0; i < pattern.length; i+=2) {
				pattern[i] = VIBRATE_OFF_MS;
				pattern[i+1] = VIBRATE_ON_MS;
			}
			return pattern;
		}
		return null;
	}

	private static int findWaypointIndexInPaths(long waypointId, List<Path> paths) {
		for (int i = 0; i < paths.size(); i++) {
			Path path = paths.get(i);
			if (path != null && path.getStartingWaypointId() == waypointId) {
				return i;
			}
		}
		return -1;
	}


	private static int findFirstWaypointIndexInIfLocationIsInside(Location location, List<Waypoint> waypoints) {
		for (int i = 0; i < waypoints.size(); i++) {
			Waypoint waypoint = waypoints.get(i);
			if (waypoint != null) {
				if (isLocationInsideWaypoint(location, waypoint)) {
					return i;
				}
			}
		}
		return -1;
	}

	private static int findLastWaypointIndexInIfLocationIsInside(Location location, List<Waypoint> waypoints) {
		for (int i = waypoints.size()-1; i >= 0; i--) {
			Waypoint waypoint = waypoints.get(i);
			if (waypoint != null) {
				if (isLocationInsideWaypoint(location, waypoint)) {
					return i;
				}
			}
		}
		return -1;
	}

	private static boolean isLocationInsideWaypoint(Location location, Waypoint waypoint) {
		if (waypoint != null && location != null) {
			Integer waypointRadiusInMeters = waypoint.getRadiusInMeters();

			// Make sure the location accuracy is smaller than the waypoint multiplied by a scaler
			if (waypointRadiusInMeters != null && location.getAccuracy() <= waypointRadiusInMeters * LOCATION_ACCURACY_RADIUS_SCALER) {
				float[] distanceInMeters = new float[1];
				Location.distanceBetween(location.getLatitude(), location.getLongitude(),
						waypoint.getLatitude(), waypoint.getLongitude(), distanceInMeters);

				// If the location is within the radius of the waypoint multiplied by a scaler, return true
				if (distanceInMeters[0] < waypointRadiusInMeters * WAYPOINT_ACCURACY_RADIUS_SCALER) {
					return true;
				}
			}
		}
		return false;
	}
}
