package com.universalstudios.orlandoresort.model.state.parking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.ParkingStatePrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ParkingStateManager {
	private static final String TAG = ParkingStateManager.class.getSimpleName();

	private static ParkingState sInstance;
	private static List<OnParkingStateChangeListener> sStateChangeListeners;

	private ParkingStateManager() {
	}

	public static synchronized ParkingState getInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstance");
		}

		if (sInstance == null) {
			sInstance = getInstanceFromStorage();
		}

		return sInstance;
	}

	private static synchronized ParkingState getInstanceFromStorage() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstanceFromStorage");
		}

		// Get the shared preferences with the app state data
		SharedPreferences sharedPrefs = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				ParkingStatePrefs.NAME, Context.MODE_PRIVATE);

		// Retrieve the state JSON object from the shared preferences
		String jsonString = sharedPrefs.getString(ParkingStatePrefs.Keys.JSON_OBJECT, null);

		// Try to restore the state object from JSON, otherwise return a new instance
		if (jsonString != null) {
			try {
				return GsonObject.fromJson(jsonString, ParkingState.class);
			}
			catch (JsonSyntaxException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Exception trying to restore ParkingState from storage, returning a new state");
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
		return new ParkingState();
	}

	public static synchronized void saveInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstance");
		}

		saveInstanceToStorage(getInstance(), true);
	}

	private static synchronized void saveInstanceToStorage(ParkingState instance, boolean notifyListeners) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstanceToStorage: notifyListeners = " + notifyListeners);
		}

		// Get the shared preferences editor to save changes
		SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				ParkingStatePrefs.NAME, Context.MODE_PRIVATE).edit();

		// Store the state JSON object to the shared preferences
		sharedPrefsEditor.putString(ParkingStatePrefs.Keys.JSON_OBJECT, instance.toJson())
		.apply();

		// Inform all listeners that a new state was saved
		if (notifyListeners) {
			for (OnParkingStateChangeListener listener : getStateChangeListenersInstance()) {
				if (listener != null) {
					listener.onParkingStateChange(instance);
				}
			}
		}
	}

	private static synchronized List<OnParkingStateChangeListener> getStateChangeListenersInstance() {
		if (sStateChangeListeners == null) {
			sStateChangeListeners = new ArrayList<>();
		}
		return sStateChangeListeners;
	}

	public static synchronized boolean registerStateChangeListener(OnParkingStateChangeListener listener) {
		return getStateChangeListenersInstance().add(listener);
	}

	public static synchronized boolean unregisterStateChangeListener(OnParkingStateChangeListener listener) {
		return getStateChangeListenersInstance().remove(listener);
	}

}
