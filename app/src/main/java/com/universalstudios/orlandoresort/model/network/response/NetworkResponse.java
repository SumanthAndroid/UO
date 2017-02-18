package com.universalstudios.orlandoresort.model.network.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;

/**
 * Base class to hold response data from network calls made by
 * {@link NetworkRequest} objects. All response objects should extend from this
 * class.
 * 
 * @author Steven Byle
 */
public abstract class NetworkResponse extends GsonObject {

	// Added to the response after parsing
	@SerializedName("HttpStatusCode")
	Integer httpStatusCode;

	/**
	 * @return the httpStatusCode
	 */
	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	/**
	 * @param httpStatusCode
	 *            the httpStatusCode to set
	 */
	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	/**
	 * @return if the status code was successful
	 */
	public boolean isHttpStatusCodeSuccess() {
		return HttpResponseStatus.isHttpStatusCodeSuccess(httpStatusCode);
	}
}