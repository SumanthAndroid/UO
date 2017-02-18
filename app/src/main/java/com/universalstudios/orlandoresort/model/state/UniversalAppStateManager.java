package com.universalstudios.orlandoresort.model.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.UniversalOrlandoStatePrefs;

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
public class UniversalAppStateManager {
	private static final String TAG = UniversalAppStateManager.class.getSimpleName();

	private static UniversalAppState sInstance;
	private static List<OnUniversalAppStateChangeListener> sStateChangeListeners;

	private UniversalAppStateManager() {
	}

	public static synchronized UniversalAppState getInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstance");
		}

		if (sInstance == null) {
			sInstance = getInstanceFromStorage();
		}

		return sInstance;
	}

	public static synchronized void saveInstance() {
		saveInstance(true);
	}

	public static synchronized void saveInstance(boolean notifyListeners) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstance");
		}

		saveInstanceToStorage(getInstance(), notifyListeners);
	}

	public static boolean hasSyncedInTheLast(Long lastSyncInMillis, long seconds) {
		if (lastSyncInMillis == null) {
			return false;
		}

		long lapseInMillis = seconds * 1000;
		long nowInMillis = new Date().getTime();

		// If data was synced within the time lapse, return true;
		if ((nowInMillis - lastSyncInMillis) <= lapseInMillis) {
			return true;
		}
		return false;
	}

	public static boolean hasSyncedToday(Long lastSyncInMillis) {
		if (lastSyncInMillis == null) {
			return false;
		}
		return hasSyncedSinceHourOfToday(lastSyncInMillis, 0);
	}

	public static boolean hasSyncedSinceHourOfToday(Long lastSyncInMillis, int sinceHourOfToday) {
		if (lastSyncInMillis == null) {
			return false;
		}

		// Get a calendar set to the beginning of the current day, in park time zone
		Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		calendar.set(Calendar.HOUR_OF_DAY, sinceHourOfToday);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// Return true if the last sync is after the beginning of the calendar day
		return (lastSyncInMillis >= calendar.getTime().getTime());
	}

	public static boolean tokenExpiresInTheNext(long seconds) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "tokenExpiresInTheNext");
		}

		UniversalAppState instance = getInstance();
		if (instance == null) {
			return true;
		}

		Long tokenExpirationInSec = instance.getTokenExpirationUnix();
		if (tokenExpirationInSec == null) {
			return true;
		}

		long tokenExpirationInMillis = tokenExpirationInSec * 1000;
		long lapseInMillis = seconds * 1000;
		long nowInMillis = new Date().getTime();

		// If token expires in the next {lapseInMillis}, return true
		if ((tokenExpirationInMillis - nowInMillis - lapseInMillis) <= 0) {
			return true;
		}
		return false;
	}

	public static boolean wipeLastSyncData() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "wipeLastSyncData");
		}

		UniversalAppState instance = getInstance();
		if (instance == null) {
			return false;
		}

		// Clear out the last sync dates and save
		instance.setDateOfLastControlPropSyncInMillis(null);
		instance.setDateOfLastVenueSyncInMillis(null);
		instance.setDateOfLastPoiSyncInMillis(null);
		instance.setDateOfLastParkNewsSyncInMillis(null);
		instance.setDateOfLastVenueHoursSyncInMillis(null);

		saveInstance();
		return true;
	}

	public static boolean wipeGeofenceState() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "wipeGeofenceState");
		}

		UniversalAppState instance = getInstance();
		if (instance == null) {
			return false;
		}

		// Clear out the geofence state and save
		instance.setInUniversalOrlandoResortGeofence(false);
		instance.setInIslandsOfAdventureGeofence(false);
		instance.setInUniversalStudiosFloridaGeofence(false);
		instance.setInCityWalkOrlandoGeofence(false);
		instance.setInWetNWildGeofence(false);

		instance.setInUniversalHollywoodResortGeofence(false);
		instance.setInUniversalStudiosHollywoodGeofence(false);
		instance.setInCityWalkHollywoodGeofence(false);

		instance.setIsLocationUnkown(true);

		saveInstance();
		return true;
	}

	public static synchronized boolean registerStateChangeListener(OnUniversalAppStateChangeListener listener) {
		return getStateChangeListenersInstance().add(listener);
	}

	public static synchronized boolean unregisterStateChangeListener(OnUniversalAppStateChangeListener listener) {
		return getStateChangeListenersInstance().remove(listener);
	}

	private static synchronized UniversalAppState getInstanceFromStorage() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstanceFromStorage");
		}

		Context context = UniversalOrlandoApplication.getAppContext();
		// Get the shared preferences with the app state data
		SharedPreferences sharedPrefs = context.getSharedPreferences(
				UniversalOrlandoStatePrefs.NAME, Context.MODE_PRIVATE);

		// Retrieve the state JSON object from the shared preferences
		String jsonString = sharedPrefs.getString(UniversalOrlandoStatePrefs.Keys.JSON_OBJECT, null);

		// Try to restore the state object from JSON, otherwise return a new instance
		if (jsonString != null) {
			try {
				UniversalAppState state = GsonObject.fromJson(jsonString, UniversalAppState.class);
				if (state != null) {
					return state;
				}
			}
			catch (JsonSyntaxException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Exception trying to restore UniversalAppState from storage, returning a new state");
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
		// If the loaded state is null, create a new instance
		return new UniversalAppState();
	}

	private static synchronized void saveInstanceToStorage(UniversalAppState instance, boolean notifyListeners) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstanceToStorage: notifyListeners = " + notifyListeners);
		}

		// Get the shared preferences editor to save changes
		SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				UniversalOrlandoStatePrefs.NAME, Context.MODE_PRIVATE).edit();

		// Store the state JSON object to the shared preferences
		sharedPrefsEditor.putString(UniversalOrlandoStatePrefs.Keys.JSON_OBJECT, instance.toJson())
		.apply();

		// Inform all listeners that a new state was saved
		if (notifyListeners) {
			for (OnUniversalAppStateChangeListener listener : getStateChangeListenersInstance()) {
				if (listener != null) {
					listener.onUniversalAppStateChange(instance);
				}
			}
		}
	}

	private static synchronized List<OnUniversalAppStateChangeListener> getStateChangeListenersInstance() {
		if (sStateChangeListeners == null) {
			sStateChangeListeners = new ArrayList<>();
		}
		return sStateChangeListeners;
	}

}
