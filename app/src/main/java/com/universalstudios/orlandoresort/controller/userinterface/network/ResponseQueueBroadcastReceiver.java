package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

import java.lang.ref.WeakReference;

/**
 * Broadcast receiver that listens for new items added to the response queue, and returns the proper
 * responses to the {@link NetworkRequestSender}.
 *
 * @author Steven Byle
 */
public class ResponseQueueBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = ResponseQueueBroadcastReceiver.class.getSimpleName();

    private WeakReference<NetworkRequestSender> mNetworkRequestSenderRef;

    public ResponseQueueBroadcastReceiver(NetworkRequestSender networkRequestSender) {
        mNetworkRequestSenderRef = new WeakReference<NetworkRequestSender>(networkRequestSender);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onReceive");
        }

        // Check the response queue and handle and responses
        NetworkRequestSender networkRequestSender = mNetworkRequestSenderRef.get();
        if (networkRequestSender != null) {
            NetworkUtils.removeAndHandleResponsesFromQueue(networkRequestSender);
        }
    }
}
