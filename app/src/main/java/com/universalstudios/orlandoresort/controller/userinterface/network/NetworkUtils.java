package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Base64;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.RequestQueue;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.network.response.ResponseQueue;

/**
 * Utility class for commonly used network functions.
 *
 * @author Steven Byle
 */
public class NetworkUtils {

	/**
	 * Method to create a unique tag for an instance of a class, based on system nano time. This is
	 * typically used to bind a sender tag to a network request object.
	 *
	 * @param c
	 *         the class to generate the tag for
	 *
	 * @return the unique tag for the class instance
	 */
	public static String generateUniqueTag(Class<?> c) {
		return (c.getSimpleName() + "_" + System.nanoTime());
	}

	/**
	 * Convenience method to add a network request to the request queue.
	 *
	 * @param networkRequest
	 *         request to be queued
	 *
	 * @return if the request was successfully added to the queue
	 */
	public static synchronized boolean queueNetworkRequest(NetworkRequest networkRequest) {
		return RequestQueue.queueRequest(networkRequest);
	}

	/**
	 * Convenience method to add a network response to the response queue.
	 */
	public static synchronized boolean queueNetworkResponse(
			NetworkParams networkParams, NetworkResponse networkResponse,
			String senderTag, String requestTag, Long timeOfRequest) {
		return ResponseQueue.queueResponse(networkParams, networkResponse, senderTag, requestTag, timeOfRequest);
	}

	/**
	 * Convenience method to start the network service. If the service is already running, it will
	 * just continue to process.
	 *
	 */
	public static synchronized void startNetworkService() {
		Context context = UniversalOrlandoApplication.getAppContext();
		if (context != null) {
			context.startService(new Intent(context, NetworkService.class));
		}
	}

	/**
	 * Convenience method to handle and remove responses for a {@link NetworkRequestSender}
	 */
	public synchronized static void removeAndHandleResponsesFromQueue(NetworkRequestSender networkRequestSender) {
		ResponseQueue.removeAndHandleResponsesFromQueue(networkRequestSender);
	}

	/**
	 * Thread safe method to tell if there is a network connection.
	 *
	 * @return if there is a network connection
	 */
	public static synchronized boolean isNetworkConnected() {

		Context context = UniversalOrlandoApplication.getAppContext();
		// Check the network connectivity
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates a basic authentication string, using the username and password.  Returns null
	 * if either parameter is null or empty.
	 *
	 * @param username
	 *         The username string
	 * @param password
	 *         The password string
	 *
     * @return The basic authentication string
     */
	public static String createBasicAuthString(String username, String password) {
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return null;
		}
		return "Basic " + Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
	}
}
