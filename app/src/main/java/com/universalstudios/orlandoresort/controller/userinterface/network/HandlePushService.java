/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.AlertsUtils;
import com.universalstudios.orlandoresort.controller.userinterface.news.NewsUtils;
import com.universalstudios.orlandoresort.model.network.domain.push.NewsPushResponse;
import com.universalstudios.orlandoresort.model.network.domain.push.PushResponse;
import com.universalstudios.orlandoresort.model.network.domain.push.RideClosedAlertPushResponse;
import com.universalstudios.orlandoresort.model.network.domain.push.RideOpenAlertPushResponse;
import com.universalstudios.orlandoresort.model.network.domain.push.WaitTimeAlertPushResponse;

/**
 * Background service intended to handle GCM push notifications.
 * 
 * @author Steven Byle
 */
public class HandlePushService extends IntentService {
	private static final String TAG = HandlePushService.class.getSimpleName();

	private static final String KEY_ARG_NOTIFICATION_TYPE = "KEY_ARG_NOTIFICATION_TYPE";
	private static final String KEY_ARG_NOTIFICATION_JSON_OBJECT = "KEY_ARG_NOTIFICATION_JSON_OBJECT";

	/**
	 * 
	 */
	public HandlePushService() {
		super(HandlePushService.class.getSimpleName());
	}

	public static Bundle newInstanceBundle(String notificationType, String notificationJsonObject) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_NOTIFICATION_TYPE, notificationType);
		args.putString(KEY_ARG_NOTIFICATION_JSON_OBJECT, notificationJsonObject);
		return args;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onHandleIntent");
		}

		PushResponse pushResponse;

		// Default parameters
		Bundle args = intent.getExtras();
		if (args == null) {
			pushResponse = null;
		}
		// Otherwise, set incoming parameters
		else {
			String notificationType = args.getString(KEY_ARG_NOTIFICATION_TYPE);
			String notificationJsonObject = args.getString(KEY_ARG_NOTIFICATION_JSON_OBJECT);
			pushResponse = PushResponse.getPushResponse(notificationType, notificationJsonObject);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onHandleIntent: notificationType = " + notificationType);
				Log.d(TAG, "onHandleIntent: notificationJsonObject = " + notificationJsonObject);
			}
		}

		if (pushResponse != null) {
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "onHandleIntent: pushResponse = " + pushResponse);
			}

			// Handle all known push response types
			if (pushResponse instanceof WaitTimeAlertPushResponse) {
				AlertsUtils.handleWaitTimeAlertPushResponse((WaitTimeAlertPushResponse) pushResponse, this);
			}
			else if (pushResponse instanceof RideClosedAlertPushResponse) {
				AlertsUtils.handleRideClosedAlertPushResponse((RideClosedAlertPushResponse) pushResponse, this);
			}
			else if (pushResponse instanceof RideOpenAlertPushResponse) {
				AlertsUtils.handleRideOpenAlertPushResponse((RideOpenAlertPushResponse) pushResponse, this);
			}
			else if (pushResponse instanceof NewsPushResponse) {
				NewsUtils.handleNewsPushResponse((NewsPushResponse) pushResponse, this);
			}
			else {
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onHandleIntent: unhandled push response returned");
				}
			}
		}
		else {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onHandleIntent: empty or unknown push response returned");
			}
		}

		// Tell the broadcast receiver to release the wake lock for this intent
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
	}

}
