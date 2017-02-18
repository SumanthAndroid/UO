package com.universalstudios.orlandoresort.controller.userinterface.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.SetAlertsOnBootCompletedService;

/**
 * 
 */
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = BootCompletedBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onReceive");
		}

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// Start the service the reset alerts after a boot
			context.startService(new Intent(context, SetAlertsOnBootCompletedService.class));
		}
	}

}
