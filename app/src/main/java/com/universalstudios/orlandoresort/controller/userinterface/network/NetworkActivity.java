package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;

/**
 * Base class for {@link FragmentActivity} objects that need to perform network
 * operations and handle their responses.
 * 
 * @author Steven Byle
 */
public abstract class NetworkActivity extends ActionBarActivity implements NetworkRequestSender {
	private static final String TAG = NetworkActivity.class.getSimpleName();

	private static final String KEY_STATE_SENDER_TAG = "KEY_STATE_SENDER_TAG";

	private String mSenderTag;
	private ResponseQueueBroadcastReceiver mResponseQueueBroadcastReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mSenderTag = NetworkUtils.generateUniqueTag(getClass());
		}
		// Otherwise, restore state
		else {
			mSenderTag = savedInstanceState.getString(KEY_STATE_SENDER_TAG);
		}

		// Create the listener to handle when responses are added to the queue
		mResponseQueueBroadcastReceiver = new ResponseQueueBroadcastReceiver(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register for response queue updates
		LocalBroadcastManager.getInstance(this)
				.registerReceiver(mResponseQueueBroadcastReceiver, new ResponseQueueIntentFilter());

		// Check for any requests that completed when this sender was paused
		NetworkUtils.removeAndHandleResponsesFromQueue(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister for response queue updates
		LocalBroadcastManager.getInstance(this)
				.unregisterReceiver(mResponseQueueBroadcastReceiver);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(KEY_STATE_SENDER_TAG, mSenderTag);
	}

	@Override
	public String getSenderTag() {
		return mSenderTag;
	}

}
