package com.universalstudios.orlandoresort.controller.userinterface.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;

public class ShowToastBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = ShowToastBroadcastReceiver.class.getSimpleName();

	public static final String ACTION_SHOW_TOAST = "com.universalstudios.orlandoresort.ACTION_SHOW_TOAST";
	private static final String KEY_ARG_TOAST_MESSAGE = "KEY_ARG_TOAST_MESSAGE";
	private static final String KEY_ARG_TOAST_LENGTH = "KEY_ARG_TOAST_LENGTH";


	public static Bundle newInstanceBundle(String toastMessage, int toastLength) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_TOAST_MESSAGE, toastMessage);
		args.putInt(KEY_ARG_TOAST_LENGTH, toastLength);
		return args;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onReceive");
		}

		Bundle args = intent.getExtras();
		if (args != null) {
			// Get the toast params
			String toastMessage = args.getString(KEY_ARG_TOAST_MESSAGE);
			int toastLength = args.getInt(KEY_ARG_TOAST_LENGTH, Toast.LENGTH_SHORT);

			// Show the toast
			UserInterfaceUtils.showToastFromForeground(toastMessage, toastLength, context);
		}
	}

}
