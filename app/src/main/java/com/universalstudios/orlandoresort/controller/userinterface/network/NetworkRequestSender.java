/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.network;

import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

/**
 * Interface for controller elements that send {@link NetworkRequest} objects
 * and handle {@link NetworkResponse} objects. Usually these are UI controllers
 * that intend on interacting with responses directly.
 * 
 * @author Steven Byle
 */
public interface NetworkRequestSender {

	/**
	 * The unique sender tag to identify the sender of the
	 * {@link NetworkRequest}
	 * 
	 * @return the unique sender tag
	 */
	public String getSenderTag();

	/**
	 * Handler for a network response object.
	 * 
	 * @param networkResponse
	 *            the network response to handle
	 */
	public void handleNetworkResponse(NetworkResponse networkResponse);

}
