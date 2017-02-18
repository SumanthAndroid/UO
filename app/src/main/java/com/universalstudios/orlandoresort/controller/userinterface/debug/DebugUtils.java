package com.universalstudios.orlandoresort.controller.userinterface.debug;

import com.facebook.stetho.Stetho;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 *
 */
public class DebugUtils {

    public static void initDebuggingUtilities() {
        if (BuildConfig.NETWORK_TRACING_ENABLED) {
            Stetho.initializeWithDefaults(UniversalOrlandoApplication.getAppContext());
        }
    }
}
