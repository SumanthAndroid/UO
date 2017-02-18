package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfigWrapper;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.TridionConfigPrefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * State manager for keeping track of the {@link TridionConfigWrapper} instance and
 * its internal {@link TridionConfig} member.  If the stored config JSON String in the SharedPreferences
 * returns null and the conversion to a {@link TridionConfig} object fails, then the instance will
 * be sourced from a stored default JSON file, and saved.
 * 
 * @author Tyler Ritchie
 */
public class TridionConfigStateManager {
	private static final String TRIDION_CONFIG_JSON_FILE_NAME = "TridionConfig.json";
	private static final String TRIDION_CONFIG_JSON_FILE_FORMAT = "UTF-8";
	private static final String TAG = TridionConfigStateManager.class.getSimpleName();

	private static TridionConfigWrapper sInstance;

	private TridionConfigStateManager() {
	}

	public static synchronized TridionConfigWrapper getInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstance");
		}

		if (sInstance == null) {
			sInstance = getInstanceFromStorage();
		}

		return sInstance;
	}

	private static synchronized TridionConfigWrapper getInstanceFromStorage() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getInstanceFromStorage");
		}

		Context context = UniversalOrlandoApplication.getAppContext();
		// Get the shared preferences with the app state data
		SharedPreferences sharedPrefs = context.getSharedPreferences(
				TridionConfigPrefs.NAME, Context.MODE_PRIVATE);

		// Retrieve the state JSON object from the shared preferences
		String jsonString = sharedPrefs.getString(TridionConfigPrefs.Keys.JSON_OBJECT, null);

		TridionConfigWrapper wrapper = new TridionConfigWrapper();
		// Try to restore the state object from JSON, otherwise return a new instance
		if (jsonString != null) {
			try {
				wrapper.setTridionConfig(GsonObject.fromJson(jsonString, TridionConfig.class));
				return wrapper;
			}
			catch (JsonSyntaxException ex) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "Exception trying to restore TridionConfig from storage; will read JSON file to populate config", ex);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(ex);
			}
		}
		// The JSON string from preferences was null therefore the transformation from JSON to
		// object failed; update it with config from JSON file
		if (wrapper.getTridionConfig() == null) {
			wrapper.setTridionConfig(getDefaultTridionConfigFromFile());
			saveInstanceToStorage(wrapper);
		}
		return wrapper;
	}

	public static synchronized void saveInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveInstance");
		}

		saveInstanceToStorage(getInstance());
	}

	private static synchronized void saveInstanceToStorage(TridionConfigWrapper instance) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveStateProvincesInstanceToStorage: ");
		}

		Context context = UniversalOrlandoApplication.getAppContext();
		// Get the shared preferences editor to save changes
		SharedPreferences.Editor sharedPrefsEditor = context.getSharedPreferences(
				TridionConfigPrefs.NAME, Context.MODE_PRIVATE).edit();

		// Store the state JSON object to the shared preferences
		sharedPrefsEditor.putString(TridionConfigPrefs.Keys.JSON_OBJECT, instance.getTridionConfig().toJson())
				.apply();
	}

	/**
	 * Uses a {@link BufferedReader} to read the complete TridionConfig object
	 * in the form of a JSON string from the Tridion config JSON file.
	 * Catches <code>IOException</code> if the {@link BufferedReader} fails.
	 *
	 * @return The {@link TridionConfig} object
	 */
	private static TridionConfig getDefaultTridionConfigFromFile() {

		StringBuilder jsonSb = null;
		try {
			Context context = UniversalOrlandoApplication.getAppContext();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(context.getAssets().open(TRIDION_CONFIG_JSON_FILE_NAME), TRIDION_CONFIG_JSON_FILE_FORMAT));
			String line;
			jsonSb = new StringBuilder();
			while ((line = in.readLine()) != null) {
				jsonSb.append(line);
			}
			in.close();
		}
		catch (IOException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "IOException when trying to read Tridion config file. Returning empty TridionConfig object.");
				return new TridionConfig();
			}
		}
		String jsonStr = "";
		if (jsonSb != null) {
			jsonStr = jsonSb.toString();
		}

		return TridionConfig.fromJson(jsonStr, TridionConfig.class);
	}
}
