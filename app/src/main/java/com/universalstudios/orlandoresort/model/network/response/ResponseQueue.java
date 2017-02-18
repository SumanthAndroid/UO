package com.universalstudios.orlandoresort.model.network.response;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.ResponseQueueIntentFilter;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorted response queue to hold {@link ResponseQueueItem} objects. This is a singleton
 * implementation, and there is only one response queue for the life of the application. {@link
 * ResponseQueueItem} objects are added to the queue, and events are fired to inform controller
 * classes that are interested in these responses.
 *
 * @author Steven Byle
 */
public class ResponseQueue {

    private static final ArrayList<ResponseQueueItem> sResponseQueueItems = new ArrayList<>();

    private ResponseQueue() {
        super();
    }

    private static void sortResponseQueueByTimeOfRequest() {
        // Synchronize the access to the response queue, so other modifications cannot occur during this method
        synchronized (sResponseQueueItems) {
            // Sorts response queue by time of request in ascending order
            Collections.sort(sResponseQueueItems, new Comparator<ResponseQueueItem>() {
                @Override
                public int compare(ResponseQueueItem item1, ResponseQueueItem item2) {
                    return (int) (item1.getTimeOfRequest() - item2.getTimeOfRequest());
                }
            });
        }
    }

    public static boolean queueResponse(
            NetworkParams networkParams, NetworkResponse networkResponse,
            String senderTag, String requestTag, Long timeOfRequest) {

        // Synchronize the access to the response queue, so other modifications cannot occur concurrently
        boolean addedToQueue;
        synchronized (sResponseQueueItems) {
            // Add response to queue
            addedToQueue = sResponseQueueItems.add(new ResponseQueueItem(networkParams, networkResponse, senderTag, requestTag, timeOfRequest));
        }

        // Sort the response queue (this method is already synchronized)
        sortResponseQueueByTimeOfRequest();

        // If added to the response queue, send a broadcast to let the controllers know to check it
        if (addedToQueue) {
            Context appContext = UniversalOrlandoApplication.getAppContext();
            LocalBroadcastManager.getInstance(appContext)
                    .sendBroadcast(ResponseQueueIntentFilter.newResponseAddedToQueueIntent());
        }
        return addedToQueue;
    }

    public static void removeAndHandleResponsesFromQueue(NetworkRequestSender networkRequestSender) {
        // Synchronize the access to the response queue, so other modifications cannot occur concurrently
        synchronized (sResponseQueueItems) {
            // Find the network request sender's responses via its tag and handle the response
            ArrayList<ResponseQueueItem> responseQueueItemsToRemove = new ArrayList<>();
            for (ResponseQueueItem responseQueueItem : sResponseQueueItems) {
                if (networkRequestSender.getSenderTag().equals(responseQueueItem.getSenderTag())) {
                    networkRequestSender.handleNetworkResponse(responseQueueItem.getNetworkResponse());
                    responseQueueItemsToRemove.add(responseQueueItem);
                }
            }
            // Remove all the responses that were handled
            if (!responseQueueItemsToRemove.isEmpty()) {
                sResponseQueueItems.removeAll(responseQueueItemsToRemove);
            }
        }
    }
}
