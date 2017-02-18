package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 * Created by Tyler Ritchie on 9/22/16.
 */
public abstract class NetworkDialogFragment extends DialogFragment implements NetworkRequestSender {
    private static final String TAG = NetworkDialogFragment.class.getSimpleName();

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register for response queue updates
        LocalBroadcastManager.getInstance(UniversalOrlandoApplication.getAppContext())
                .registerReceiver(mResponseQueueBroadcastReceiver, new ResponseQueueIntentFilter());

        // Check for any requests that completed when this sender was paused
        NetworkUtils.removeAndHandleResponsesFromQueue(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister for response queue updates
        LocalBroadcastManager.getInstance(UniversalOrlandoApplication.getAppContext())
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
