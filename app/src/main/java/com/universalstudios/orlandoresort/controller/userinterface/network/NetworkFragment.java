package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base class for {@link android.support.v4.app.Fragment} objects that need to perform network
 * operations and handle their responses.
 *
 * @author Steven Byle
 */
public abstract class NetworkFragment extends Fragment implements NetworkRequestSender {
	private static final String TAG = NetworkFragment.class.getSimpleName();

	private static final String KEY_STATE_SENDER_TAG = "KEY_STATE_SENDER_TAG";

	private String mSenderTag;
	private ResponseQueueBroadcastReceiver mResponseQueueBroadcastReceiver;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register for response queue updates
		LocalBroadcastManager.getInstance(getContext())
				.registerReceiver(mResponseQueueBroadcastReceiver, new ResponseQueueIntentFilter());

		// Check for any requests that completed when this sender was paused
		NetworkUtils.removeAndHandleResponsesFromQueue(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister for response queue updates
		LocalBroadcastManager.getInstance(getContext())
				.unregisterReceiver(mResponseQueueBroadcastReceiver);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(KEY_STATE_SENDER_TAG, mSenderTag);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public String getSenderTag() {
		return mSenderTag;
	}
}
