/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.global;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.view.fonts.FontManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public class UserInterfaceUtils {
	private static final String TAG = UserInterfaceUtils.class.getSimpleName();

	public static void setActionBarCustomFont(Activity activity) {
		try {

			ActionBar actionBar = activity.getActionBar();
			if (actionBar == null) {
				return;
			}
			Resources r = activity.getResources();
			int actionBarTitleId = r.getIdentifier("action_bar_title", "id", "android");
			TextView actionBarTitleTextView = (TextView) activity.findViewById(actionBarTitleId);
			if (actionBarTitleTextView == null) {
				return;
			}

			Typeface customTypeface = FontManager.getInstance().getTypeface(activity, R.string.font_actionbar_title);
			actionBarTitleTextView.setTypeface(customTypeface);
			actionBarTitleTextView.setTextColor(r.getColor(R.color.text_white));
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "onCreate: unable to set custom font on action bar title", e);
			}

			// Log the exception to crittercism
			CrashAnalyticsUtils.logHandledException(e);
		}
	}

	public static void setActionBarIcon(ActionBar actionBar) {
		if (actionBar != null) {
			// Only use the action bar logo on Holo themed devices
			boolean useActionBarLogo = (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
			if (useActionBarLogo) {
				actionBar.setIcon(R.drawable.ic_action_universal_logo);
				actionBar.setLogo(R.drawable.ic_action_universal_logo);
				actionBar.setDisplayUseLogoEnabled(true);
			} else {
				actionBar.setIcon(null);
				actionBar.setLogo(null);
				actionBar.setDisplayUseLogoEnabled(false);
			}
		}
	}

	public static void enableActionBarHomeButton(ActionBar actionBar) {
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}
	}

	public static void setActionBarElevation(ActionBar actionBar) {
		if (actionBar != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				actionBar.setElevation(getDimenInPx(R.dimen.actionbar_elevation));
			}
		}
	}

	public static void closeKeyboard(View view) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "closeKeyboard: unable to close the keyboard", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}
	}

	/**
	 * Use {@link com.universalstudios.orlandoresort.controller.userinterface.utils.Toast}
     */
	@Deprecated
	public static void showToastFromForeground(String toastMessage, int toastLength, Context context) {
		if (context != null && toastMessage != null && !toastMessage.isEmpty()) {
			Toast.makeText(context, toastMessage, toastLength).show();
		}
	}

	/**
	 * Use {@link com.universalstudios.orlandoresort.controller.userinterface.utils.Toast}
	 */
	@Deprecated
	public static void showToastFromBackground(String toastMessage, int toastLength, Context context) {
		Bundle args = ShowToastBroadcastReceiver.newInstanceBundle(toastMessage, toastLength);
		Intent intent = new Intent(ShowToastBroadcastReceiver.ACTION_SHOW_TOAST);
		intent.putExtras(args);

		context.sendBroadcast(intent);
	}

	public static int getDimenInPx(@DimenRes int dimenResId) {
		return UniversalOrlandoApplication.getAppContext().getResources().getDimensionPixelSize(dimenResId);
	}


}
