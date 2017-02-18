package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.content.Intent;
import android.content.IntentFilter;

/**
 * Intent filter to catch local broadcasts for changes to the network response queue
 */
public class ResponseQueueIntentFilter extends IntentFilter {
    private static String ACTION_NEW_RESPONSE_ADDED_TO_QUEUE = "ACTION_NEW_RESPONSE_ADDED_TO_QUEUE";

    public ResponseQueueIntentFilter() {
        super(ACTION_NEW_RESPONSE_ADDED_TO_QUEUE);
    }

    public static Intent newResponseAddedToQueueIntent() {
        return new Intent(ACTION_NEW_RESPONSE_ADDED_TO_QUEUE);
    }
}
