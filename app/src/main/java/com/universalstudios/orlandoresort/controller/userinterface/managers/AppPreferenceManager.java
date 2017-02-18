package com.universalstudios.orlandoresort.controller.userinterface.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 * Created by Kevin Haines on 5/4/16.
 * Package Name: com.universalstudios.orlandoresort.controller.userinterface.managers
 * Project: Universal Orlando
 * Class Name: AppPreferenceManager
 * Class Description: This class holds convenience and compliance methods for getting and setting preferences
 */
public class AppPreferenceManager {
    public static final String TAG = AppPreferenceManager.class.getSimpleName();

    public static SharedPreferences getSharedPreferences() {
        Context context = UniversalOrlandoApplication.getAppContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Tells us whether the Interactive Audio reasoning dialog has been displayed
     *
     * @return true if already shown
     */
    public static boolean hasShownAudioInteractionReasoning() {
        Context context = UniversalOrlandoApplication.getAppContext();
        String audioReasoning = context.getString(R.string.pref_key_show_interactive_audio_reasoning);
        return getSharedPreferences()
                .getBoolean(audioReasoning, false);
    }

    /**
     * Sets whether the user has seen the Interactive Audio reasoning dialog or not
     *
     * @param value
     *         true if dialog has been shown
     */
    public static void setHasShownAudioInteractionsReasoning(boolean value) {
        Context context = UniversalOrlandoApplication.getAppContext();
        String audioReasoning = context.getString(R.string.pref_key_show_interactive_audio_reasoning);
        getSharedPreferences().edit()
                .putBoolean(audioReasoning, value).apply();
    }

    /**
     * Sets the preference of whether we should show the restrooms on the map or not. This is for
     * persistence between different Explore types
     *
     * @param value
     *         boolean value
     */
    public static void setRestroomFiltersOn(boolean value) {
        Context context = UniversalOrlandoApplication.getAppContext();
        String restroomFilter = context.getString(R.string.pref_key_show_restroom_filter);
        getSharedPreferences().edit()
                .putBoolean(restroomFilter, value).apply();
    }

    /**
     * Gets the preference for whether we should show the restrooms on the map or not.
     *
     * @return true if we should show the restrooms
     */
    public static boolean getRestroomFiltersOn() {
        Context context = UniversalOrlandoApplication.getAppContext();
        String restroomFilter = context.getString(R.string.pref_key_show_restroom_filter);
        return getSharedPreferences().getBoolean(restroomFilter, false);
    }

    public static void setParkingReminderImageLocation(String uri) {
        Context context = UniversalOrlandoApplication.getAppContext();
        String parkingReminder = context.getString(R.string.pref_key_parking_reminder);
        getSharedPreferences().edit().putString(parkingReminder, uri).apply();
    }

    public static String getParkingReminderImageLocation() {
        Context context = UniversalOrlandoApplication.getAppContext();
        String parkingReminder = context.getString(R.string.pref_key_parking_reminder);
        return getSharedPreferences().getString(parkingReminder, null);
    }
}
