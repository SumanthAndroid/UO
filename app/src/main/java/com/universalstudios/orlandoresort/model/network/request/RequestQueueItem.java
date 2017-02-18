package com.universalstudios.orlandoresort.model.network.request;

import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;

/**
 * Wrapper class to hold {@link NetworkRequest} objects. Should be used with the
 * {@link RequestQueue}.
 * 
 * @author Steven Byle
 */
public class RequestQueueItem implements Comparable<RequestQueueItem> {

	private final NetworkRequest mNetworkRequest;

	public RequestQueueItem(NetworkRequest networkRequest) {
		mNetworkRequest = networkRequest;
	}

	/**
	 * @return the networkRequest
	 */
	public NetworkRequest getNetworkRequest() {
		return mNetworkRequest;
	}

	/**
	 * @return the requestPriority
	 */
	public Priority getRequestPriority() {
		return mNetworkRequest.getPriority();
	}

	@Override
	public int compareTo(RequestQueueItem another) {
		return getRequestPriority().getValue() - another.getRequestPriority().getValue();
	}

}