package com.universalstudios.orlandoresort.controller.userinterface.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsRequest;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.List;

/**
 * This class receives geofence transition events from Location Services, in the
 * form of an Intent containing the transition type and geofence id(s) that
 * triggered the event.
 */
public class GeofenceTransitionBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = GeofenceTransitionBroadcastReceiver.class.getSimpleName();

	public static final String ACTION_GEOFENCE_TRANSITION_RECEIVED = "com.universalstudios.orlandoresort.ACTION_GEOFENCE_TRANSITION_RECEIVED";

	/**
	 * Handles incoming intents
	 * 
	 * @param intent
	 *            The Intent sent by Location Services. This Intent is provided
	 *            to Location Services (inside a PendingIntent) when you call
	 *            addGeofences()
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onReceive");
		}

		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		// First check for errors
		if (geofencingEvent.hasError()) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "onHandleIntent: LocationClient.hasError(intent) = true, LocationClient.getErrorCode(intent) = "
						+ geofencingEvent.getErrorCode());
			}
		}
		else {
			int transitionType = geofencingEvent.getGeofenceTransition();
			List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

			// Check the state before updating geofences
			UniversalAppState state = UniversalAppStateManager.getInstance();
			boolean wasInUniversalOrlandoResortGeofence = state.isInUniversalOrlandoResortGeofence();
			boolean wasInIslandsOfAdventureGeofence = state.isInIslandsOfAdventureGeofence();
			boolean wasInUniversalStudiosFloridaGeofence = state.isInUniversalStudiosFloridaGeofence();
			boolean wasInCityWalkGeofence = state.isInCityWalkOrlandoGeofence();
			boolean wasInWetNWildGeofence = state.isInWetNWildGeofence();

			boolean wasInUniversalHollywoodResortGeofence = state.isInUniversalHollywoodResortGeofence();
			boolean wasInUniversalStudiosHollywoodGeofence = state.isInUniversalStudiosHollywoodGeofence();
			boolean wasInCityWalkHollywoodGeofence = state.isInCityWalkHollywoodGeofence();

			// Update geofence states
			if (triggeringGeofences != null && triggeringGeofences.size() > 0) {
				for (Geofence geofence : triggeringGeofences) {
					String geofenceId = geofence.getRequestId();
					if (geofenceId == null) {
						continue;
					}

					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onReceive: " + geofenceId
								+ " -> " + GeofenceUtils.getTransitionTypeString(transitionType));
					}

					boolean isInGeofence = GeofenceUtils.isTransitionInGeofence(transitionType);

					// Orlando geofences
					if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_UNIVERSAL_ORLANDO_RESORT)) {
						state.setInUniversalOrlandoResortGeofence(isInGeofence);
					}
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_ISLANDS_OF_ADVENTURE)) {
						state.setInIslandsOfAdventureGeofence(isInGeofence);
					}
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_UNIVERSAL_STUDIOS_FLORIDA)) {
						state.setInUniversalStudiosFloridaGeofence(isInGeofence);
					}
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_CITYWALK)) {
						state.setInCityWalkOrlandoGeofence(isInGeofence);
					}
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_WET_N_WILD)) {
					    state.setInWetNWildGeofence(isInGeofence);
					}
					// Hollywood geofences
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_UNIVERSAL_HOLLYWOOD_RESORT)) {
						state.setInUniversalHollywoodResortGeofence(isInGeofence);
					}
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD)) {
						state.setInUniversalStudiosHollywoodGeofence(isInGeofence);
					}
					else if (geofenceId.equals(GeofenceUtils.GEOFENCE_ID_CITYWALK_HOLLYWOOD)) {
						state.setInCityWalkHollywoodGeofence(isInGeofence);
					}
				}
			}

			// Save changes only if a geofence has changed
			if (wasInUniversalOrlandoResortGeofence != state.isInUniversalOrlandoResortGeofence()
				|| wasInIslandsOfAdventureGeofence != state.isInIslandsOfAdventureGeofence()
				|| wasInUniversalStudiosFloridaGeofence != state.isInUniversalStudiosFloridaGeofence()
				|| wasInCityWalkGeofence != state.isInCityWalkOrlandoGeofence()
				|| wasInWetNWildGeofence != state.isInWetNWildGeofence()
				|| wasInUniversalHollywoodResortGeofence != state.isInUniversalHollywoodResortGeofence()
				|| wasInUniversalStudiosHollywoodGeofence != state.isInUniversalStudiosHollywoodGeofence()
				|| wasInCityWalkHollywoodGeofence != state.isInCityWalkHollywoodGeofence()) {
				UniversalAppStateManager.saveInstance();
			}

			boolean hasEnteredOrExitedResortGeofence = false;
			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				hasEnteredOrExitedResortGeofence = wasInUniversalHollywoodResortGeofence != state.isInUniversalHollywoodResortGeofence();
			} else {
				hasEnteredOrExitedResortGeofence = wasInUniversalOrlandoResortGeofence != state.isInUniversalOrlandoResortGeofence();
			}

			// If going in or out of the resort geofence, update the service tier
			if (hasEnteredOrExitedResortGeofence) {

				RegisterAlertsRequest registerAlertsRequest = new RegisterAlertsRequest.Builder(null)
				.setDeviceId(state.getGcmRegistrationId())
				.setInPark(state.isInResortGeofence())
				.build();
				NetworkUtils.queueNetworkRequest(registerAlertsRequest);

				NetworkUtils.startNetworkService();
			}
		}
	}

}
