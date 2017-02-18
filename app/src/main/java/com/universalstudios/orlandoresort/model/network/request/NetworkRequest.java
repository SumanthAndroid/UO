package com.universalstudios.orlandoresort.model.network.request;

import android.support.annotation.NonNull;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkErrorResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponseWithErrors;

import java.net.SocketTimeoutException;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The base class for all network request objects. This object defines the behavior needed to make a
 * request and handle its {@link NetworkResponse}.
 *
 * @author Steven Byle
 */
public abstract class NetworkRequest {
	private static final String TAG = NetworkRequest.class.getSimpleName();

	private final Priority mPriority;
	private final ConcurrencyType mConcurrencyType;
	private final String mSenderTag, mRequestTag;
	private Long mTimeOfRequest;
	private final NetworkParams mNetworkParams;

	/**
	 * Constructor to create a network request object.
	 *
	 * @param senderTag
	 *         the unique tag of the sender
	 * @param priority
	 *         the priority of the request
	 * @param concurrencyType
	 *         if the request should be processed asynchronously with other requests, otherwise it
	 *         will process synchronously and block subsequent requests until it finishes
	 * @param networkParams
	 *         optional set of parameters needed to make the request
	 */
	protected NetworkRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
		mSenderTag = senderTag;
		mRequestTag = NetworkUtils.generateUniqueTag(getClass());
		mPriority = (priority != null) ? priority : Priority.NORMAL;
		mConcurrencyType = (concurrencyType != null) ? concurrencyType : ConcurrencyType.SYNCHRONOUS;
		mNetworkParams = networkParams;
	}

	/**
	 * The base URL for the service APIs. This can be changed per environment,
	 * to allow for easy environment switching.
	 * 
	 * @return the base URL of the service APIs
	 */
	public abstract String getServicesBaseUrl();

	/**
	 * Call to actually make the network call.
	 *
	 * @param restAdapter
	 *         the rest adapter to make the call on
	 */
	public void makeNetworkRequest(RestAdapter restAdapter) {
		mTimeOfRequest = System.currentTimeMillis();
	}

	/**
	 * Base handler for a successful HTTP response, 2XX status code. It injects the HTTP response
	 * code onto the response object and saves it to the response queue.
	 */
	protected final void handleSuccess(NetworkResponse networkResponse, Response response) {
		if (response != null) {
			networkResponse.setHttpStatusCode(response.getStatus());
		}
		else {
			networkResponse.setHttpStatusCode(HttpResponseStatus.SUCCESS_OK);
		}

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "handleSuccess");
		}

		// Save to response queue
		saveToResponseQueue(networkResponse);
	}

	/**
	 * Base handler for an unsuccessful HTTP response due to network failure, non-2XX status code,
	 * or unexpected exception. It injects the HTTP response code onto the response object and saves
	 * it to the response queue.
	 */
	@SuppressWarnings("unchecked")
	protected final void handleFailure(@NonNull NetworkResponse networkResponse, @NonNull RetrofitError retrofitError) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "handleFailure: class = " + networkResponse.getClass().getSimpleName());
			Log.e(TAG, "handleFailure: error = " + retrofitError);
		}

		// Store the error and HTTP response code to the response object
		Response response = retrofitError.getResponse();
		if (response != null) {
			int httpStatusCode = response.getStatus();
			if (!HttpResponseStatus.isHttpStatusCodeSuccess(httpStatusCode)) {
				networkResponse.setHttpStatusCode(httpStatusCode);
			}
			else {
				// There was a successful HTTP code, but an error occurred during parsing
				networkResponse.setHttpStatusCode(HttpResponseStatus.ERROR_PARSING);
			}
		}
		else {
			if (retrofitError.getCause() instanceof SocketTimeoutException){
				networkResponse.setHttpStatusCode(HttpResponseStatus.ERROR_SOCKET_TIMEOUT);
			}
			else {
				networkResponse.setHttpStatusCode(HttpResponseStatus.ERROR_RETROFIT);
			}
		}

		// Parse out any error body if it's there and the network response supports errors
		if (networkResponse instanceof NetworkResponseWithErrors) {
			NetworkResponseWithErrors networkResponseWithErrors = (NetworkResponseWithErrors) networkResponse;
			try {
				Class errorResponseClass = networkResponseWithErrors.getNetworkErrorResponseClass();
				if (errorResponseClass != null && NetworkErrorResponse.class.isAssignableFrom(errorResponseClass)) {
					NetworkErrorResponse networkErrorResponse = (NetworkErrorResponse) retrofitError.getBodyAs(errorResponseClass);
					networkResponseWithErrors.setNetworkErrorResponse(networkErrorResponse);
				}
			} catch (Exception e) {
				// If the error response fails to parse, set the error response as null
				networkResponseWithErrors.setNetworkErrorResponse(null);
			}
		}

		// Save to response queue
		saveToResponseQueue(networkResponse);
	}

	/**
	 * Convenience method to save the response to the response queue database. It only saves to the
	 * response queue if there is a sender tag, otherwise there is no UI element that needs to
	 * handle the response directly.
	 *
	 * @param networkResponse
	 *         the network response to save
	 */
	private void saveToResponseQueue(@NonNull NetworkResponse networkResponse) {
		// Only save to the response queue if there is a sender tag, otherwise there is no UI element listening for the response
		String senderTag = getSenderTag();
		if (senderTag != null) {
			NetworkUtils.queueNetworkResponse(getNetworkParams(), networkResponse, senderTag, getRequestTag(), getTimeOfRequest());
		}
	}

	/**
	 * @return the RequestTag
	 */
	public String getRequestTag() {
		return mRequestTag;
	}

	/**
	 * @return the ConcurrencyType
	 */
	protected ConcurrencyType getConcurrencyType() {
		return mConcurrencyType;
	}

	/**
	 * @return the Priority
	 */
	protected Priority getPriority() {
		return mPriority;
	}

	/**
	 * @return the NetworkParams
	 */
	protected NetworkParams getNetworkParams() {
		return mNetworkParams;
	}

	/**
	 * @return the SenderTag
	 */
	protected String getSenderTag() {
		return mSenderTag;
	}

	/**
	 * @return the TimeOfRequest
	 */
	protected Long getTimeOfRequest() {
		return mTimeOfRequest;
	}

	/**
	 * Concurrency type to be used with the request. If synchronous, the request
	 * will block subsequent requests. If asynchronous, subsequent requests will
	 * be called at the same time as the request.
	 */
	public enum ConcurrencyType {
		SYNCHRONOUS(1), ASYNCHRONOUS(2);

		private int value;
		ConcurrencyType(int value) {
			this.value = value;
		}
	}

	/**
	 * Priority used to determine the order that a network request is processed.
	 */
	public enum Priority {
		VERY_HIGH(1), HIGH(2), NORMAL(3), LOW(4), VERY_LOW(5);

		private int value;
		Priority(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
