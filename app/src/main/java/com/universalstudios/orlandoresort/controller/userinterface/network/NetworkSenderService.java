package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.app.Service;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 
 * 
 * @author Steven Byle
 */
public abstract class NetworkSenderService extends Service implements NetworkRequestSender {
	private static final String TAG = NetworkSenderService.class.getSimpleName();

	private String mSenderTag;
	private ResponseQueueBroadcastReceiver mResponseQueueBroadcastReceiver;

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize variables
		mSenderTag = NetworkUtils.generateUniqueTag(getClass());
		mResponseQueueBroadcastReceiver = new ResponseQueueBroadcastReceiver(this);

		// Register for response queue updates
		LocalBroadcastManager.getInstance(this)
				.registerReceiver(mResponseQueueBroadcastReceiver, new ResponseQueueIntentFilter());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Unregister for response queue updates
		LocalBroadcastManager.getInstance(this)
				.unregisterReceiver(mResponseQueueBroadcastReceiver);

	}

	@Override
	public String getSenderTag() {
		return mSenderTag;
	}
}
