package com.universalstudios.orlandoresort.model.network.request;

import java.util.concurrent.PriorityBlockingQueue;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkService;

/**
 * Priority request queue to hold {@link RequestQueueItem} objects. This is a
 * singleton implementation, and there is only one request queue for the life of
 * the application. {@link RequestQueueItem} objects are added to the queue, and
 * the {@link NetworkService} processes them once started.
 * 
 * @author Steven Byle
 */
public class RequestQueue extends PriorityBlockingQueue<RequestQueueItem> {
	private static final int INITIAL_QUEUE_CAPACITY = 50;

	private static RequestQueue sInstance;

	private RequestQueue() {
		super(INITIAL_QUEUE_CAPACITY);
	}

	private synchronized static RequestQueue getInstance() {
		if (sInstance == null) {
			sInstance = new RequestQueue();
		}
		return sInstance;
	}

	/**
	 * Convenience method to add network requests to the request queue.
	 *
	 * @param networkRequest
	 *         network request to add to the queue
	 *
	 * @return if the network request was successfully added to the queue
	 */
	public synchronized static boolean queueRequest(NetworkRequest networkRequest) {
		return getInstance().add(new RequestQueueItem(networkRequest));
	}

	/**
	 * Convenience method to remove and return the next request, based on priority.
	 *
	 * @return the next network request, or {@code null} if the queue is empty
	 */
	public synchronized static RequestQueueItem pollRequest() {
		return getInstance().poll();
	}

}
