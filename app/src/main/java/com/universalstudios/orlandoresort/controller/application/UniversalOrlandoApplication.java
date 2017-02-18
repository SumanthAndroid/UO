package com.universalstudios.orlandoresort.controller.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.debug.DebugUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.SecurityUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

/**
 * @author Steven Byle
 */
public class UniversalOrlandoApplication extends MultiDexApplication {
	private static final String TAG = UniversalOrlandoApplication.class.getSimpleName();

	private static UniversalOrlandoApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;

		// Initialize crash analytics
		CrashAnalyticsUtils.initCrittericism();

		// Initialize user analytics
		AnalyticsUtils.initAdobeAnalytics();

		// Initialize debugging utilities
		DebugUtils.initDebuggingUtilities();

		// Initialize threatmatrix
		SecurityUtils.initDefender();

		// Initialize the account state manager
		AccountStateManager.initialize();
	}

    /**
	 * @return PackageInfo
	 */
	public static PackageInfo getPackageInfo() {
		PackageInfo packageInfo = null;
		try {
			Context context = getAppContext();
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		}
		catch (NameNotFoundException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "getPackageInfo: exception trying to get package info");
			}

			// Log the exception to crittercism
			CrashAnalyticsUtils.logHandledException(e);
		}

		return packageInfo;
	}

	/**
	 * Gets the {@link Context} of the {@link android.app.Application}. NOTE: This should only ever
	 * be called after {@link #onCreate()}, and only used where an application context will suffice
	 * (do not use for anything that needs a theme, like inflating views or manipulating the user
	 * interface).
	 *
	 * @return The application context
	 */
	public static Context getAppContext() {
		if (sInstance == null) {
			throw new IllegalStateException("Attempting to access application context before the application has been created");
		}
		return sInstance.getApplicationContext();
	}
}
