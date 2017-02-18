package com.universalstudios.orlandoresort.model.state.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.UniversalOrlandoSettingsPrefs;

/**
 * 
 * 
 * @author Steven Byle
 */
public class UniversalOrlandoSettingsManager {
	private static final String TAG = UniversalOrlandoSettingsManager.class.getSimpleName();

	private static UniversalOrlandoSettings sInstance;

	private UniversalOrlandoSettingsManager() {
	}

	public static synchronized UniversalOrlandoSettings getInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstance");
		}

		if (sInstance == null) {
			sInstance = getInstanceFromStorage();
		}

		return sInstance;
	}

	private static synchronized UniversalOrlandoSettings getInstanceFromStorage() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstanceFromStorage");
		}

		// Get the shared preferences with the app settings data
		SharedPreferences sharedPrefs = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				UniversalOrlandoSettingsPrefs.NAME, Context.MODE_PRIVATE);

		// Retrieve the settings JSON object from the shared preferences
		String jsonString = sharedPrefs.getString(UniversalOrlandoSettingsPrefs.Keys.JSON_OBJECT, null);

		// Try to restore the settings object from JSON, otherwise return a new instance
		if (jsonString != null) {
			try {
				return GsonObject.fromJson(jsonString, UniversalOrlandoSettings.class);
			}
			catch (JsonSyntaxException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Exception trying to restore UniversalOrlandoSettings from storage, returning a new settings");
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
		return new UniversalOrlandoSettings();
	}

	public static synchronized void saveInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstance");
		}

		saveInstanceToStorage(getInstance());
	}

	private static synchronized void saveInstanceToStorage(UniversalOrlandoSettings instance) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstanceToStorage");
		}

		// Get the shared preferences editor to save changes
		SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
				UniversalOrlandoSettingsPrefs.NAME, Context.MODE_PRIVATE).edit();

		// Store the settings JSON object to the shared preferences
		sharedPrefsEditor.putString(UniversalOrlandoSettingsPrefs.Keys.JSON_OBJECT, instance.toJson())
		.apply();
	}

}
