
package com.universalstudios.orlandoresort.controller.userinterface.geofence;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.OrientationLockActivity;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsRequest;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * @author Steven Byle
 */
public abstract class GeofenceCheckerActivity extends OrientationLockActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	private static final String TAG = GeofenceCheckerActivity.class.getSimpleName();

	private static final long LOCATION_UPDATE_INTERVAL_MS = 4 * 1000;
	private static final int LOCATION_NUM_UPDATES = 3;
	private static final long LOCATION_FASTEST_UPDATE_INTERVAL_MS = LOCATION_UPDATE_INTERVAL_MS;
	private static final long LOCATION_SMALLEST_DISPLACEMENT_METERS = 0;
	private static final float DELAY_MILLIS_PER_METER_OUT_OF_GEOFENCE = 37.28f; // Assumes moving at 60mph
	private static final long DELAY_MILLIS_MAX = 4 * 60 * 60 * 1000; // 4 hours

	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private int mLocationUpdates;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate");
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		mLocationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(LOCATION_UPDATE_INTERVAL_MS)
				.setFastestInterval(LOCATION_FASTEST_UPDATE_INTERVAL_MS)
				.setSmallestDisplacement(LOCATION_SMALLEST_DISPLACEMENT_METERS)
				.setNumUpdates(LOCATION_NUM_UPDATES);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

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

		UniversalAppState state = UniversalAppStateManager.getInstance();
		Long lastGeofenceCheckInMillisLong = state.getDateOfLastGeofenceCheckInMillis();
		long lastGeofenceCheckInMillis = lastGeofenceCheckInMillisLong != null ? lastGeofenceCheckInMillisLong : 0;
		Long delayIntervalBeforeNextGeofenceCheckInMillisLong = state.getDelayIntervalBeforeNextGeofenceCheckInMillis();
		long delayIntervalBeforeNextGeofenceCheckInMillis = delayIntervalBeforeNextGeofenceCheckInMillisLong != null ? delayIntervalBeforeNextGeofenceCheckInMillisLong : 0;

		long currentTimeInMillis = System.currentTimeMillis();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onStart: currentTimeInMillis = " + currentTimeInMillis
					+ " lastGeofenceCheckInMillis = " + lastGeofenceCheckInMillis
					+ " delayIntervalBeforeNextGeofenceCheckInMillis = " + delayIntervalBeforeNextGeofenceCheckInMillis
					+ " lastGeofenceCheckInMillis + delayIntervalBeforeNextGeofenceCheckInMillis = " + (lastGeofenceCheckInMillis + delayIntervalBeforeNextGeofenceCheckInMillis));
		}

		// If the app is not in the geofence
		if (!state.isInResortGeofence()) {
			// Double check the geofence if the app has waited long enough since the last check
			if (currentTimeInMillis > (lastGeofenceCheckInMillis + delayIntervalBeforeNextGeofenceCheckInMillis)) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onStart: delay period reached, checking geofences");
				}

				// Connect to the location client, if it isn't already connected
				if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
				}
			}
		}
		else {
			state.setDelayIntervalBeforeNextGeofenceCheckInMillis(0l);
			UniversalAppStateManager.saveInstance(false);
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

		// Disconnect the location client
		if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.disconnect();
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
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

		// Register for location updates
		mLocationUpdates = 0;
		try {
			LocationServices.FusedLocationApi.requestLocationUpdates(
					mGoogleApiClient, mLocationRequest, this);
		} catch (SecurityException e) {
			e.printStackTrace();
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
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "Location services disconected");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLocationChanged: lat = " + location.getLatitude()
					+ " lon = " + location.getLongitude()
					+ " accuracy = " + location.getAccuracy());
		}

		UniversalAppState state = UniversalAppStateManager.getInstance();
		state.setDateOfLastGeofenceCheckInMillis(System.currentTimeMillis());
		state.setIsLocationUnkown(false);
		Long delayIntervalBeforeNextGeofenceCheckInMillis = null;
		boolean hasEnteredOrExitedResortGeofence = false;

		// Check Hollywood geofences
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			// Check the state before updating geofences
			boolean wasInUniversalHollywoodResortGeofence = state.isInUniversalHollywoodResortGeofence();
			boolean wasInUniversalStudiosHollywoodGeofence = state.isInUniversalStudiosHollywoodGeofence();
			boolean wasInCityWalkHollywoodGeofence = state.isInCityWalkHollywoodGeofence();

			// If inside the resort
			if (GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LAT,
					GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LON,
					GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_RADIUS_IN_METERS)) {
				state.setInUniversalHollywoodResortGeofence(true);
			}
			// If outside the resort fence, figure out how long to wait before checking again
			else {
				float distanceBetweenLocationAndGeofenceEdgeInMeters = GeofenceUtils.getDistanceBetweenLocationAndGeofenceEdgeInMeters(location,
						GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LAT,
						GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LON,
						GeofenceUtils.GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_RADIUS_IN_METERS);

				// Delay based on how far the user is outside of the geofence before the next check
				if (distanceBetweenLocationAndGeofenceEdgeInMeters > 0) {
					delayIntervalBeforeNextGeofenceCheckInMillis =
							(long) (distanceBetweenLocationAndGeofenceEdgeInMeters * DELAY_MILLIS_PER_METER_OUT_OF_GEOFENCE);

					// Limit the max delay
					if (delayIntervalBeforeNextGeofenceCheckInMillis > DELAY_MILLIS_MAX) {
						delayIntervalBeforeNextGeofenceCheckInMillis = DELAY_MILLIS_MAX;
					}
					state.setDelayIntervalBeforeNextGeofenceCheckInMillis(delayIntervalBeforeNextGeofenceCheckInMillis);
				}
			}

			state.setInUniversalStudiosHollywoodGeofence(GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LAT,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LON,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_RADIUS_IN_METERS));

			state.setInCityWalkHollywoodGeofence(GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_LAT,
					GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_LON,
					GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_RADIUS_IN_METERS));

			// Save changes only if a geofence has changed or the delay interval was updated
			if (wasInUniversalHollywoodResortGeofence != state.isInUniversalHollywoodResortGeofence()
				|| wasInUniversalStudiosHollywoodGeofence != state.isInUniversalStudiosHollywoodGeofence()
				|| wasInCityWalkHollywoodGeofence != state.isInCityWalkHollywoodGeofence()) {
				UniversalAppStateManager.saveInstance();
			}
			else if (delayIntervalBeforeNextGeofenceCheckInMillis != null) {
				UniversalAppStateManager.saveInstance(false);
			}

			hasEnteredOrExitedResortGeofence = wasInUniversalHollywoodResortGeofence != state.isInUniversalHollywoodResortGeofence();
		}
		// Otherwise, check Orlando geofences
		else {
			// Check the state before updating geofences
			boolean wasInUniversalOrlandoResortGeofence = state.isInUniversalOrlandoResortGeofence();
			boolean wasInIslandsOfAdventureGeofence = state.isInIslandsOfAdventureGeofence();
			boolean wasInUniversalStudiosFloridaGeofence = state.isInUniversalStudiosFloridaGeofence();
			boolean wasInCityWalkGeofence = state.isInCityWalkOrlandoGeofence();
			boolean wasInWetNWildGeofence = state.isInWetNWildGeofence();

			// If inside the resort
			if (GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LAT,
					GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LON,
					GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_RADIUS_IN_METERS)) {
				state.setInUniversalOrlandoResortGeofence(true);
			}
			// If outside the resort fence, figure out how long to wait before checking again
			else {
				float distanceBetweenLocationAndGeofenceEdgeInMeters = GeofenceUtils.getDistanceBetweenLocationAndGeofenceEdgeInMeters(location,
						GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LAT,
						GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LON,
						GeofenceUtils.GEOFENCE_UNIVERSAL_ORLANDO_RESORT_RADIUS_IN_METERS);

				// Delay based on how far the user is outside of the geofence before the next check
				if (distanceBetweenLocationAndGeofenceEdgeInMeters > 0) {
					delayIntervalBeforeNextGeofenceCheckInMillis =
							(long) (distanceBetweenLocationAndGeofenceEdgeInMeters * DELAY_MILLIS_PER_METER_OUT_OF_GEOFENCE);

					// Limit the max delay
					if (delayIntervalBeforeNextGeofenceCheckInMillis > DELAY_MILLIS_MAX) {
						delayIntervalBeforeNextGeofenceCheckInMillis = DELAY_MILLIS_MAX;
					}
					state.setDelayIntervalBeforeNextGeofenceCheckInMillis(delayIntervalBeforeNextGeofenceCheckInMillis);
				}
			}

			if (GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_LAT,
					GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_LON,
					GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_RADIUS_IN_METERS)) {
				state.setInIslandsOfAdventureGeofence(true);
			}
			if (GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LAT,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LON,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_RADIUS_IN_METERS)) {
				state.setInUniversalStudiosFloridaGeofence(true);
			}
			if (GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_CITYWALK_LAT,
					GeofenceUtils.GEOFENCE_CITYWALK_LON,
					GeofenceUtils.GEOFENCE_CITYWALK_RADIUS_IN_METERS)) {
				state.setInCityWalkOrlandoGeofence(true);
			}

			if (GeofenceUtils.isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_WET_N_WILD_LAT,
					GeofenceUtils.GEOFENCE_WET_N_WILD_LON,
					GeofenceUtils.GEOFENCE_WET_N_WILD_RADIUS_IN_METERS)) {
				state.setInWetNWildGeofence(true);
			}

			// Save changes only if a geofence has changed or the delay interval was updated
			if (wasInUniversalOrlandoResortGeofence != state.isInUniversalOrlandoResortGeofence()
				|| wasInIslandsOfAdventureGeofence != state.isInIslandsOfAdventureGeofence()
				|| wasInUniversalStudiosFloridaGeofence != state.isInUniversalStudiosFloridaGeofence()
				|| wasInCityWalkGeofence != state.isInCityWalkOrlandoGeofence()
				|| wasInWetNWildGeofence != state.isInWetNWildGeofence()) {
				UniversalAppStateManager.saveInstance();
			}
			else if (delayIntervalBeforeNextGeofenceCheckInMillis != null) {
				UniversalAppStateManager.saveInstance(false);
			}

			hasEnteredOrExitedResortGeofence = wasInUniversalOrlandoResortGeofence != state.isInUniversalOrlandoResortGeofence();
		}


		// If going in or out of the resort geofence, update the service
		if (hasEnteredOrExitedResortGeofence) {

			RegisterAlertsRequest registerAlertsRequest = new RegisterAlertsRequest.Builder(null)
			.setDeviceId(state.getGcmRegistrationId())
			.setInPark(state.isInResortGeofence())
			.build();
			NetworkUtils.queueNetworkRequest(registerAlertsRequest);

			NetworkUtils.startNetworkService();
		}

		// If the max updates has ben reached, disconnect
		mLocationUpdates++;
		if (mLocationUpdates >= LOCATION_NUM_UPDATES) {
            mGoogleApiClient.disconnect();
		}
	}
}
