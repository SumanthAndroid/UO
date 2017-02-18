package com.universalstudios.orlandoresort.model.state.debug;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.UniversalOrlandoDebugStatePrefs;

/**
 * 
 * 
 * @author Steven Byle
 */
public class UniversalOrlandoDebugStateManager {
	private static final String TAG = UniversalOrlandoDebugStateManager.class.getSimpleName();

	private static UniversalOrlandoDebugState sInstance;

	private UniversalOrlandoDebugStateManager() {
	}

	public static synchronized UniversalOrlandoDebugState getInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstance");
		}

		if (sInstance == null) {
			sInstance = getInstanceFromStorage();
		}

		return sInstance;
	}

	private static synchronized UniversalOrlandoDebugState getInstanceFromStorage() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstanceFromStorage");
		}

		// Get the shared preferences with the app debug state data
		SharedPreferences sharedPrefs = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				UniversalOrlandoDebugStatePrefs.NAME, Context.MODE_PRIVATE);

		// Retrieve the state JSON object from the shared preferences
		String jsonString = sharedPrefs.getString(UniversalOrlandoDebugStatePrefs.Keys.JSON_OBJECT, null);

		// Try to restore the debug state object from JSON, otherwise return a new instance
		if (jsonString != null) {
			try {
				return GsonObject.fromJson(jsonString, UniversalOrlandoDebugState.class);
			}
			catch (JsonSyntaxException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Exception trying to restore UniversalAppState from storage, returning an empty state");
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
		return new UniversalOrlandoDebugState();
	}

	public static synchronized void saveInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstance");
		}

		saveInstanceToStorage(getInstance());
	}

	private static synchronized void saveInstanceToStorage(UniversalOrlandoDebugState instance) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstanceToStorage");
		}

		// Get the shared preferences editor to save make changes
		SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				UniversalOrlandoDebugStatePrefs.NAME, Context.MODE_PRIVATE).edit();

		// Store the debug state JSON object to the shared preferences
		sharedPrefsEditor.putString(UniversalOrlandoDebugStatePrefs.Keys.JSON_OBJECT, instance.toJson())
		.apply();
	}

}
