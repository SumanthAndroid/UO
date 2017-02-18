package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.CountryPrefs;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.StateProvincePrefs;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.BuildConfig;

/**
 * @author Jack Hughes on 9/28/2016
 */
public class CountryStateProvinceStateManager {
    private static final String TAG = CountryStateProvinceStateManager.class.getSimpleName();

    private static CountriesState sCountryInstance;
    private static StateProvincesState sStateProvincesInstance;

    private CountryStateProvinceStateManager() {
    }

    public static synchronized @NonNull CountriesState getCountriesInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getInstance");
        }

        if (sCountryInstance == null) {
            sCountryInstance = getCountriesInstanceFromStorage();
        }

        return sCountryInstance;
    }

    private static synchronized @NonNull CountriesState getCountriesInstanceFromStorage() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getCountriesInstanceFromStorage");
        }

        // Get the shared preferences with the app state data
        SharedPreferences sharedPrefs = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
                CountryPrefs.NAME, Context.MODE_PRIVATE);

        // Retrieve the state JSON object from the shared preferences
        String jsonString = sharedPrefs.getString(CountryPrefs.Keys.JSON_OBJECT, null);

        // Try to restore the state object from JSON, otherwise return a new instance
        if (jsonString != null) {
            try {
                return GsonObject.fromJson(jsonString, CountriesState.class);
            }
            catch (JsonSyntaxException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Exception trying to restore CountryPrefs from storage, returning a new state");
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
        return new CountriesState();
    }

    public static synchronized void saveCountriesInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveCountriesInstance");
        }

        saveCountriesInstanceToStorage(getCountriesInstance());
    }

    private static synchronized void saveCountriesInstanceToStorage(CountriesState instance) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveCountriesInstanceToStorage:= ");
        }

        // Get the shared preferences editor to save changes
        SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
                CountryPrefs.NAME, Context.MODE_PRIVATE).edit();

        // Store the state JSON object to the shared preferences
        sharedPrefsEditor.putString(CountryPrefs.Keys.JSON_OBJECT, instance.toJson())
                .apply();
    }

    public static synchronized @NonNull StateProvincesState getStateProvincesInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getInstance");
        }

        if (sStateProvincesInstance == null) {
            sStateProvincesInstance = getStateProvincesInstanceFromStorage();
        }

        return sStateProvincesInstance;
    }

    private static synchronized @NonNull StateProvincesState getStateProvincesInstanceFromStorage() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getStateProvincesInstanceFromStorage");
        }

        // Get the shared preferences with the app state data
        SharedPreferences sharedPrefs = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
                StateProvincePrefs.NAME, Context.MODE_PRIVATE);

        // Retrieve the state JSON object from the shared preferences
        String jsonString = sharedPrefs.getString(StateProvincePrefs.Keys.JSON_OBJECT, null);

        // Try to restore the state object from JSON, otherwise return a new instance
        if (jsonString != null) {
            try {
                return GsonObject.fromJson(jsonString, StateProvincesState.class);
            }
            catch (JsonSyntaxException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Exception trying to restore StateProvincePrefs from storage, returning a new state");
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
        return new StateProvincesState();
    }

    public static synchronized void saveStateProvincesInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveStateProvincesInstance");
        }

        saveStateProvincesInstanceToStorage(getStateProvincesInstance());
    }

    private static synchronized void saveStateProvincesInstanceToStorage(StateProvincesState instance) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveStateProvincesInstanceToStorage:= ");
        }

        // Get the shared preferences editor to save changes
        SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
                StateProvincePrefs.NAME, Context.MODE_PRIVATE).edit();

        // Store the state JSON object to the shared preferences
        sharedPrefsEditor.putString(StateProvincePrefs.Keys.JSON_OBJECT, instance.toJson())
                .apply();
    }

}
