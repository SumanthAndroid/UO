/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.geofence;

import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class GeofenceUtils {
	private static final String TAG = GeofenceUtils.class.getSimpleName();

	private static final float LOCATION_ACCURACY_RADIUS_SCALER = 1.5f;
	private static final int RESPONSIVENESS_IN_MILLIS = 15 * 1000; // 15 seconds
	private static final int LOITERING_DELAY_IN_MILLIS = 1 * 60 * 1000; // 1 minute
	private static final long EXPIRATION_DURATION_IN_MILLIS = Geofence.NEVER_EXPIRE; // Never expire
	private static final int TRANSITION_TYPES =
			Geofence.GEOFENCE_TRANSITION_ENTER
			| Geofence.GEOFENCE_TRANSITION_DWELL
			| Geofence.GEOFENCE_TRANSITION_EXIT;

	// Orlando geofences
	public static final String GEOFENCE_ID_UNIVERSAL_ORLANDO_RESORT = "GEOFENCE_ID_UNIVERSAL_ORLANDO_RESORT";
	public static final double GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LAT = 28.472735;
	public static final double GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LON = -81.467505;
	public static final float GEOFENCE_UNIVERSAL_ORLANDO_RESORT_RADIUS_IN_METERS = 1400;

	public static final String GEOFENCE_ID_ISLANDS_OF_ADVENTURE = "GEOFENCE_ID_ISLANDS_OF_ADVENTURE";
	public static final double GEOFENCE_ISLANDS_OF_ADVENTURE_LAT = 28.471529;
	public static final double GEOFENCE_ISLANDS_OF_ADVENTURE_LON = -81.471792;
	public static final float GEOFENCE_ISLANDS_OF_ADVENTURE_RADIUS_IN_METERS = 400;

	public static final String GEOFENCE_ID_UNIVERSAL_STUDIOS_FLORIDA = "GEOFENCE_ID_UNIVERSAL_STUDIOS_FLORIDA";
	public static final double GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LAT = 28.477847;
	public static final double GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LON = -81.468230;
	public static final float GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_RADIUS_IN_METERS = 400;

	public static final String GEOFENCE_ID_CITYWALK = "GEOFENCE_ID_CITYWALK";
	public static final double GEOFENCE_CITYWALK_LAT = 28.473358;
	public static final double GEOFENCE_CITYWALK_LON = -81.466449;
	public static final float GEOFENCE_CITYWALK_RADIUS_IN_METERS = 200;
	
	public static final String GEOFENCE_ID_WET_N_WILD = "GEOFENCE_ID_WET_N_WILD";
	public static final double GEOFENCE_WET_N_WILD_LAT = 28.460495;
	public static final double GEOFENCE_WET_N_WILD_LON = -81.465322;
	public static final float GEOFENCE_WET_N_WILD_RADIUS_IN_METERS = 203;

	private static List<Geofence> GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT;
	public static List<Geofence> getGeofenceListForAllOfUniversalOrlando() {
		if (GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT == null) {
			GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT = new ArrayList<Geofence>();

			GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT.add(getGeofenceUniversalOrlandoResort());
			GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT.add(getGeofenceIslandsOfAdventure());
			GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT.add(getGeofenceUniversalStudiosFlorida());
			GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT.add(getGeofenceCityWalk());
			GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT.add(getGeofenceWetNWild());
		}

		return GEOFENCE_LIST_UNIVERSAL_ORLANDO_RESORT;
	}

	private static Geofence GEOFENCE_UNIVERSAL_ORLANDO_RESORT;
	public static Geofence getGeofenceUniversalOrlandoResort() {
		if (GEOFENCE_UNIVERSAL_ORLANDO_RESORT == null) {
			GEOFENCE_UNIVERSAL_ORLANDO_RESORT = new Geofence.Builder()
			.setCircularRegion(GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LAT,
					GEOFENCE_UNIVERSAL_ORLANDO_RESORT_LON,
					GEOFENCE_UNIVERSAL_ORLANDO_RESORT_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_UNIVERSAL_ORLANDO_RESORT)
					.build();
		}

		return GEOFENCE_UNIVERSAL_ORLANDO_RESORT;
	}

	private static Geofence GEOFENCE_ISLANDS_OF_ADVENTURE;
	public static Geofence getGeofenceIslandsOfAdventure() {
		if (GEOFENCE_ISLANDS_OF_ADVENTURE == null) {
			GEOFENCE_ISLANDS_OF_ADVENTURE = new Geofence.Builder()
			.setCircularRegion(GEOFENCE_ISLANDS_OF_ADVENTURE_LAT,
					GEOFENCE_ISLANDS_OF_ADVENTURE_LON,
					GEOFENCE_ISLANDS_OF_ADVENTURE_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_ISLANDS_OF_ADVENTURE)
					.build();
		}

		return GEOFENCE_ISLANDS_OF_ADVENTURE;
	}

	private static Geofence GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA;
	public static Geofence getGeofenceUniversalStudiosFlorida() {
		if (GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA == null) {
			GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA = new Geofence.Builder()
			.setCircularRegion(GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LAT,
					GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LON,
					GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_UNIVERSAL_STUDIOS_FLORIDA)
					.build();
		}

		return GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA;
	}

	private static Geofence GEOFENCE_CITYWALK;
	public static Geofence getGeofenceCityWalk() {
		if (GEOFENCE_CITYWALK == null) {
			GEOFENCE_CITYWALK = new Geofence.Builder()
			.setCircularRegion(GEOFENCE_CITYWALK_LAT,
					GEOFENCE_CITYWALK_LON,
					GEOFENCE_CITYWALK_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_CITYWALK)
					.build();
		}

		return GEOFENCE_CITYWALK;
	}
	
	private static Geofence GEOFENCE_WET_N_WILD;
	public static Geofence getGeofenceWetNWild() {
	    if(GEOFENCE_WET_N_WILD == null) {
	        GEOFENCE_WET_N_WILD = new Geofence.Builder()
            .setCircularRegion(GEOFENCE_WET_N_WILD_LAT,
                    GEOFENCE_WET_N_WILD_LON,
                    GEOFENCE_WET_N_WILD_RADIUS_IN_METERS)
                    .setTransitionTypes(TRANSITION_TYPES)
                    .setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
                    .setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
                    .setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
                    .setRequestId(GEOFENCE_ID_WET_N_WILD)
                    .build();
	    }
	    
	    return GEOFENCE_WET_N_WILD;
	}


	// Hollywood geofences
	public static final String GEOFENCE_ID_UNIVERSAL_HOLLYWOOD_RESORT = "GEOFENCE_ID_UNIVERSAL_HOLLYWOOD_RESORT";
	public static final double GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LAT = 34.138142;
	public static final double GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LON = -118.353955;
	public static final float GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_RADIUS_IN_METERS = 690;

	public static final String GEOFENCE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD = "GEOFENCE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD";
	public static final double GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LAT = 34.139627;
	public static final double GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LON = -118.355650;
	public static final float GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_RADIUS_IN_METERS = 400;

	public static final String GEOFENCE_ID_CITYWALK_HOLLYWOOD = "GEOFENCE_ID_CITYWALK_HOLLYWOOD";
	public static final double GEOFENCE_CITYWALK_HOLLYWOOD_LAT = 34.136731;
	public static final double GEOFENCE_CITYWALK_HOLLYWOOD_LON = -118.351866;
	public static final float GEOFENCE_CITYWALK_HOLLYWOOD_RADIUS_IN_METERS = 230;

	private static List<Geofence> GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT;
	public static List<Geofence> getGeofenceListForAllOfUniversalHollywood() {
		if (GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT == null) {
			GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT = new ArrayList<Geofence>();

			GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT.add(getGeofenceUniversalHollywoodResort());
			GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT.add(getGeofenceUniversalStudiosHollywood());
			GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT.add(getGeofenceCitywalkHollywood());
		}

		return GEOFENCE_LIST_UNIVERSAL_HOLLYWOOD_RESORT;
	}

	private static Geofence GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT;
	public static Geofence getGeofenceUniversalHollywoodResort() {
		if (GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT == null) {
			GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT = new Geofence.Builder()
					.setCircularRegion(GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LAT,
							GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_LON,
							GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_UNIVERSAL_HOLLYWOOD_RESORT)
					.build();
		}

		return GEOFENCE_UNIVERSAL_HOLLYWOOD_RESORT;
	}

	private static Geofence GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD;
	public static Geofence getGeofenceUniversalStudiosHollywood() {
		if (GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD == null) {
			GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD = new Geofence.Builder()
					.setCircularRegion(GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LAT,
							GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LON,
							GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD)
					.build();
		}

		return GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD;
	}

	private static Geofence GEOFENCE_CITYWALK_HOLLYWOOD;
	public static Geofence getGeofenceCitywalkHollywood() {
		if (GEOFENCE_CITYWALK_HOLLYWOOD == null) {
			GEOFENCE_CITYWALK_HOLLYWOOD = new Geofence.Builder()
					.setCircularRegion(GEOFENCE_CITYWALK_HOLLYWOOD_LAT,
							GEOFENCE_CITYWALK_HOLLYWOOD_LON,
							GEOFENCE_CITYWALK_HOLLYWOOD_RADIUS_IN_METERS)
					.setTransitionTypes(TRANSITION_TYPES)
					.setLoiteringDelay(LOITERING_DELAY_IN_MILLIS)
					.setExpirationDuration(EXPIRATION_DURATION_IN_MILLIS)
					.setNotificationResponsiveness(RESPONSIVENESS_IN_MILLIS)
					.setRequestId(GEOFENCE_ID_CITYWALK)
					.build();
		}

		return GEOFENCE_CITYWALK_HOLLYWOOD;
	}


	public static String getTransitionTypeString(int transitionType) {
		switch(transitionType) {
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				return "GEOFENCE_TRANSITION_ENTER";
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				return "GEOFENCE_TRANSITION_DWELL";
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				return "GEOFENCE_TRANSITION_EXIT";
			default:
				return "Unknown Transition";
		}
	}

	public static boolean isTransitionInGeofence(int transitionType) {
		switch(transitionType) {
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				return true;
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				return true;
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				return false;
			default:
				return false;
		}
	}

	public static void checkParkGeofences(Location location) {
		// Check the state before updating geofences
		UniversalAppState state = UniversalAppStateManager.getInstance();

		// Check Hollywood park geofences
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			boolean wasInUniversalStudiosHollywoodGeofence = state.isInUniversalStudiosHollywoodGeofence();
			boolean wasInCityWalkHollywoodGeofence = state.isInCityWalkHollywoodGeofence();

			state.setInUniversalStudiosHollywoodGeofence(isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LAT,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_LON,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_HOLLYWOOD_RADIUS_IN_METERS));

			state.setInCityWalkHollywoodGeofence(isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_LAT,
					GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_LON,
					GeofenceUtils.GEOFENCE_CITYWALK_HOLLYWOOD_RADIUS_IN_METERS));

			// Save changes only if a geofence has changed
			if (wasInUniversalStudiosHollywoodGeofence != state.isInUniversalStudiosHollywoodGeofence()
				|| wasInCityWalkHollywoodGeofence != state.isInCityWalkHollywoodGeofence()) {
				UniversalAppStateManager.saveInstance();
			}
		}
		// Otherwise, check Orlando park geofences
		else {
			boolean wasInIslandsOfAdventureGeofence = state.isInIslandsOfAdventureGeofence();
			boolean wasInUniversalStudiosFloridaGeofence = state.isInUniversalStudiosFloridaGeofence();
			boolean wasInCityWalkGeofence = state.isInCityWalkOrlandoGeofence();
			boolean wasInWetNWildGeofence = state.isInWetNWildGeofence();

			state.setInIslandsOfAdventureGeofence(isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_LAT,
					GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_LON,
					GeofenceUtils.GEOFENCE_ISLANDS_OF_ADVENTURE_RADIUS_IN_METERS));

			state.setInUniversalStudiosFloridaGeofence(isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LAT,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_LON,
					GeofenceUtils.GEOFENCE_UNIVERSAL_STUDIOS_FLORIDA_RADIUS_IN_METERS));

			state.setInCityWalkOrlandoGeofence(isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_CITYWALK_LAT,
					GeofenceUtils.GEOFENCE_CITYWALK_LON,
					GeofenceUtils.GEOFENCE_CITYWALK_RADIUS_IN_METERS));

			state.setInWetNWildGeofence(isLocationInsideGeofence(location,
					GeofenceUtils.GEOFENCE_WET_N_WILD_LAT,
					GeofenceUtils.GEOFENCE_WET_N_WILD_LON,
					GeofenceUtils.GEOFENCE_WET_N_WILD_RADIUS_IN_METERS));

			// Save changes only if a geofence has changed
			if (wasInIslandsOfAdventureGeofence != state.isInIslandsOfAdventureGeofence()
				|| wasInUniversalStudiosFloridaGeofence != state.isInUniversalStudiosFloridaGeofence()
				|| wasInCityWalkGeofence != state.isInCityWalkOrlandoGeofence()
				|| wasInWetNWildGeofence != state.isInWetNWildGeofence()) {
				UniversalAppStateManager.saveInstance();
			}
		}
	}

	public static boolean isLocationInsideGeofence(Location location, double geofenceLat,
			double geofenceLon, float geofenceRadiusInMeters) {

		if (location != null) {
			// Make sure the location accuracy is smaller than the geofence multiplied by a scaler
			if (location.getAccuracy() <= geofenceRadiusInMeters * LOCATION_ACCURACY_RADIUS_SCALER) {
				float[] distanceInMeters = new float[1];
				Location.distanceBetween(location.getLatitude(), location.getLongitude(),
						geofenceLat, geofenceLon, distanceInMeters);

				// If the location is within the radius of the geofence, return true
				if (distanceInMeters[0] < geofenceRadiusInMeters) {
					return true;
				}
			}
		}
		return false;
	}

	public static float getDistanceBetweenLocationAndGeofenceEdgeInMeters(Location location,
			double geofenceLat, double geofenceLon, float geofenceRadiusInMeters) {

		if (location != null) {
			// Make sure the location accuracy is smaller than the geofence multiplied by a scaler
			float[] distanceInMeters = new float[1];
			Location.distanceBetween(location.getLatitude(), location.getLongitude(),
					geofenceLat, geofenceLon, distanceInMeters);

			return (distanceInMeters[0] - geofenceRadiusInMeters);
		}
		return Float.MAX_VALUE;
	}
}
