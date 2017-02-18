package com.universalstudios.orlandoresort.controller.userinterface.network;

import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.domain.push.PushResponse;

/**
 * Broadcast receiver that listens for GCM pushes and starts the
 * {@link HandlePushService} to handle the responses.
 * 
 * 
 * @author Steven Byle
 */
public class PushBroadcastReceiver extends WakefulBroadcastReceiver {
	private static final String TAG = PushBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onReceive");
		}

		Bundle args = intent.getExtras();
		if (args == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onReceive: args == null");
			}
			return;
		}

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);

		if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR)) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onReceive: send error... " + args.toString());

				Set<String> keySet = args.keySet();
				for (String key : keySet) {
					Log.w(TAG, "onReceive: " + key + " = " + args.getString(key));
				}
			}
		}
		else if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_DELETED)) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onReceive: messages deleted on server... " + args.toString());

				Set<String> keySet = args.keySet();
				for (String key : keySet) {
					Log.w(TAG, "onReceive: " + key + " = " + args.getString(key));
				}
			}
		}
		else if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
			if (BuildConfig.DEBUG) {
				Log.i(TAG, "onReceive: received message... " + args.toString());

				Set<String> keySet = args.keySet();
				for (String key : keySet) {
					Log.i(TAG, "onReceive: " + key + " = " + args.getString(key));
				}
			}

			// Parse out the response object
			String notificationType = args.getString(PushResponse.KEY_NOTIFICATION_TYPE);
			String notificationJsonObject = args.getString(PushResponse.KEY_NOTIFICATION_JSON_OBJECT);

			// Start a background service to process the response
			Bundle bundle = HandlePushService.newInstanceBundle(notificationType, notificationJsonObject);
			Intent handlePushServiceIntent = new Intent(context, HandlePushService.class);
			handlePushServiceIntent.putExtras(bundle);

			// The the service with info about the wake lock, the service will
			// tell this receiver when to release the wake lock
			startWakefulService(context, handlePushServiceIntent);
		}
	}
}
