package com.universalstudios.orlandoresort.controller.userinterface.global;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.LocaleList;
import android.provider.Settings;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.util.Locale;

/**
 *
 */

public class LocationUtils {

    public static boolean isLocaleSet(Resources resources, Locale locale) {
        if (resources != null) {
            Configuration configuration = resources.getConfiguration();
            if (configuration != null) {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
                    LocaleList localeList = configuration.getLocales();
                    for (int i = 0; i < localeList.size(); i++) {
                        if (locale.equals(localeList.get(i))) {
                            return true;
                        }
                    }
                } else {
                    if (locale.equals(configuration.locale)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isLocaleCountrySet(Resources resources, Locale locale) {
        if (resources != null) {
            Configuration configuration = resources.getConfiguration();
            if (configuration != null) {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
                    LocaleList localeList = configuration.getLocales();
                    for (int i = 0; i < localeList.size(); i++) {
                        if (locale.getISO3Country().equals(localeList.get(i).getISO3Country())) {
                            return true;
                        }
                    }
                } else {
                    if (locale.getISO3Country().equals(configuration.locale.getISO3Country())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        Context context = UniversalOrlandoApplication.getAppContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
