package com.universalstudios.orlandoresort.model.state.commerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.CommerceStatePrefs;
import com.universalstudios.orlandoresort.model.persistence.SharedPreferencesConstants.UniversalOrlandoStatePrefs;
import com.universalstudios.orlandoresort.model.state.account.AccountLoginService;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Steven Byle
 */
public class CommerceStateManager {
    private static final String TAG = CommerceStateManager.class.getSimpleName();

    private static CommerceState sInstance;
    private static List<OnCommerceStateChangeListener> sStateChangeListeners;

    private CommerceStateManager() {
    }

    private static synchronized CommerceState getInstance() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getInstance");
        }

        if (sInstance == null) {
            sInstance = getInstanceFromStorage();
        }
        return sInstance;
    }

    private static synchronized void saveInstance() {
        saveInstance(true);
    }

    private static synchronized void saveInstance(boolean notifyListeners) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveInstance");
        }
        saveInstanceToStorage(getInstance(), notifyListeners);
    }

    private static synchronized CommerceState getInstanceFromStorage() {
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
                CommerceState state = GsonObject.fromJson(jsonString, CommerceState.class);
                if (state != null) {
                    return state;
                }
            } catch (JsonSyntaxException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Exception trying to restore CommerceState from storage, returning a new state");
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
        // If the loaded state is null, create a new instance
        return new CommerceState();
    }

    private static synchronized void saveInstanceToStorage(CommerceState instance, boolean notifyListeners) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveInstanceToStorage: notifyListeners = " + notifyListeners);
        }

        // Get the shared preferences editor to save changes
        SharedPreferences.Editor sharedPrefsEditor = UniversalOrlandoApplication.getAppContext().getSharedPreferences(
                CommerceStatePrefs.NAME, Context.MODE_PRIVATE).edit();

        // Store the state JSON object to the shared preferences
        sharedPrefsEditor.putString(CommerceStatePrefs.Keys.JSON_OBJECT, instance.toJson())
                .apply();

        // Inform all listeners that a new state was saved
        if (notifyListeners) {
            for (OnCommerceStateChangeListener listener : getStateChangeListenersInstance()) {
                if (listener != null) {
                    listener.onCommerceStateChange(instance);
                }
            }
        }
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

    public static synchronized boolean registerStateChangeListener(OnCommerceStateChangeListener listener) {
        return getStateChangeListenersInstance().add(listener);
    }

    public static synchronized boolean unregisterStateChangeListener(OnCommerceStateChangeListener listener) {
        return getStateChangeListenersInstance().remove(listener);
    }

    private static synchronized List<OnCommerceStateChangeListener> getStateChangeListenersInstance() {
        if (sStateChangeListeners == null) {
            sStateChangeListeners = new ArrayList<>();
        }
        return sStateChangeListeners;
    }

    // ****** DELEGATED METHODS FOR THE COMMERCE STATE ******

    public static void setDateOfLastCommerceEnabledSyncInMillis(Long dateOfLastCommerceEnabledSyncInMillis) {
        getInstance().setDateOfLastCommerceEnabledSyncInMillis(dateOfLastCommerceEnabledSyncInMillis);
        saveInstance();
    }

    public static void setAppValidForCommerce(Boolean appValidForCommerce) {
        getInstance().setAppValidForCommerce(appValidForCommerce);
        saveInstance();
    }

    public static Long getDateOfLastCommerceEnabledSyncInMillis() {
        return getInstance().getDateOfLastCommerceEnabledSyncInMillis();
    }

    public static Date getDateOfLastCommerceEnabledSync() {
        return getInstance().getDateOfLastCommerceEnabledSync();
    }

    public static Boolean getAppValidForCommerce() {
        return getInstance().getAppValidForCommerce();
    }

    public static boolean isAppValidForCommerce() {
        Boolean isAppValidForCommerce = getInstance().getAppValidForCommerce();
        // Check the commerce kill switch, and only disable commerce if it has been returned, and is set to false
        if (isAppValidForCommerce != null && !isAppValidForCommerce) {
            return false;
        }
        // Otherwise, assume commerce is enabled
        else {
            return true;
        }
    }

    /**
     * Utility method to check if commerce features are enabled for this version of the app.
     *
     * @param showErrorIfCommerceDisabled
     *         show an error message if commerce is not enabled
     * @param logUserOutIfCommerceDisabled
     *         log the user out (if they are signed in) and show a message if commerce is not
     *         enabled
     *
     * @return If commerce features are enabled for this this version of the app.
     */
    public static boolean isAppValidForCommerce(boolean showErrorIfCommerceDisabled, boolean logUserOutIfCommerceDisabled) {
        boolean isAppValidForCommerce = isAppValidForCommerce();
        if (!isAppValidForCommerce) {
            Context appContext = UniversalOrlandoApplication.getAppContext();
            if (showErrorIfCommerceDisabled) {
                String errorMessage = TridionConfigStateManager.getInstance().getTridionConfig().getEr96();
                Toast.makeText(appContext, errorMessage, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
            if (logUserOutIfCommerceDisabled && AccountStateManager.isUserLoggedIn()) {
                String errorMessage = TridionConfigStateManager.getInstance().getTridionConfig().getEr97();
                Toast.makeText(appContext, errorMessage, Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();

                AccountLoginService.startActionLogout(appContext);
            }
        }
        return isAppValidForCommerce;
    }
}
