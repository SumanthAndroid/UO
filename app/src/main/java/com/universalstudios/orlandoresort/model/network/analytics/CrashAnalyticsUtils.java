/**
 *
 */
package com.universalstudios.orlandoresort.model.network.analytics;

import com.crittercism.app.CritterCallback;
import com.crittercism.app.CritterUserData;
import com.crittercism.app.CritterUserDataRequest;
import com.crittercism.app.Crittercism;
import com.crittercism.app.CrittercismConfig;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 *
 *
 * @author Steven Byle
 */
public class CrashAnalyticsUtils {

	public static void initCrittericism() {
        // Create the CrittercismConfig instance.
        CrittercismConfig crittercismConfig = new CrittercismConfig();
        crittercismConfig.setVersionCodeToBeIncludedInVersionString(true);

        Crittercism.initialize(UniversalOrlandoApplication.getAppContext(),
				getCrashAnalyticsAppId(), crittercismConfig);

        // Only report data if enabled
        Crittercism.setOptOutStatus(!BuildConfig.CRASH_ANALYTICS_ENABLED);
    }

	public static void logHandledException(Exception exception) {
		Crittercism.logHandledException(exception);
	}

	public static void getUserUuid() {
		// Instantiate callback object.
		CritterCallback cb = new CritterCallback() {
			// CritterCallback is an interface that requires you to implement
			// onCritterDataReceived(CritterUserData).
			@Override
			public void onCritterDataReceived(CritterUserData userData) {
				String userUUID = userData.getUserUUID();
				// ...do something with userUUID
			}
		};

		// Instantiate data request object, and specify that it should include the
		// user UUID.
		CritterUserDataRequest request = new CritterUserDataRequest(cb)
		.requestUserUUID();

		// Fire off the request.
		request.makeRequest();
	}

	public static String getCrashAnalyticsAppId() {
		return BuildConfig.CRASH_ANALYTICS_APP_ID;
	}

}
