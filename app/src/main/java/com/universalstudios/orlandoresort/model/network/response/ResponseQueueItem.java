package com.universalstudios.orlandoresort.model.network.response;

import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.request.RequestQueue;

/**
 * Wrapper class to hold {@link NetworkRequest} objects. Should be used with the
 * {@link RequestQueue}.
 * 
 * @author Steven Byle
 */
public class ResponseQueueItem {

	private final NetworkParams mNetworkParams;
	private final NetworkResponse mNetworkResponse;
	private final String mSenderTag, mRequestTag;
	private final Long mTimeOfRequest;

	public ResponseQueueItem(NetworkParams networkParams, NetworkResponse networkResponse, String senderTag, String requestTag, Long timeOfRequest) {
		mNetworkParams = networkParams;
		mNetworkResponse = networkResponse;
		mSenderTag = senderTag;
		mRequestTag = requestTag;
		mTimeOfRequest = timeOfRequest;
	}

	public NetworkResponse getNetworkResponse() {
		return mNetworkResponse;
	}

	public NetworkParams getNetworkParams() {
		return mNetworkParams;
	}

	public String getRequestTag() {
		return mRequestTag;
	}

	public String getSenderTag() {
		return mSenderTag;
	}

	public Long getTimeOfRequest() {
		return mTimeOfRequest;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ResponseQueueItem that = (ResponseQueueItem) o;

		if (mNetworkParams != null ? !mNetworkParams.equals(that.mNetworkParams) : that.mNetworkParams != null) {
			return false;
		}
		if (mNetworkResponse != null ? !mNetworkResponse.equals(that.mNetworkResponse) : that.mNetworkResponse != null) {
			return false;
		}
		if (mSenderTag != null ? !mSenderTag.equals(that.mSenderTag) : that.mSenderTag != null) {
			return false;
		}
		if (mRequestTag != null ? !mRequestTag.equals(that.mRequestTag) : that.mRequestTag != null) {
			return false;
		}
		return mTimeOfRequest != null ? mTimeOfRequest.equals(that.mTimeOfRequest) : that.mTimeOfRequest == null;
	}

	@Override
	public int hashCode() {
		int result = mNetworkParams != null ? mNetworkParams.hashCode() : 0;
		result = 31 * result + (mNetworkResponse != null ? mNetworkResponse.hashCode() : 0);
		result = 31 * result + (mSenderTag != null ? mSenderTag.hashCode() : 0);
		result = 31 * result + (mRequestTag != null ? mRequestTag.hashCode() : 0);
		result = 31 * result + (mTimeOfRequest != null ? mTimeOfRequest.hashCode() : 0);
		return result;
	}

	//FIXME REMOVE AFTER TESTING
	@Override
	public String toString() {
		return "ResponseQueueItem{" +
			   "mTimeOfRequest=" + mTimeOfRequest +
			   ", mSenderTag='" + mSenderTag + '\'' +
			   ", mRequestTag='" + mRequestTag + '\'' +
			   '}';
	}
}