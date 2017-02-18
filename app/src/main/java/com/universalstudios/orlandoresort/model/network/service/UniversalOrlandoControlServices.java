package com.universalstudios.orlandoresort.model.network.service;

import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesRequest.GetControlPropertiesBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesResponse;
import com.universalstudios.orlandoresort.model.network.request.RequestHeaders;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author Steven Byle
 */
@SuppressWarnings("javadoc")
public interface UniversalOrlandoControlServices {

	/**
	 *  Authenticate with the services and get the control properties
	 */
	@POST("/api")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	GetControlPropertiesResponse getControlProperties(
			@Header(RequestHeaders.Keys.DATE) String formattedDate,
			@Body GetControlPropertiesBodyParams bodyParams);

	@POST("/api")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	void getControlProperties(
			@Header(RequestHeaders.Keys.DATE) String formattedDate,
			@Body GetControlPropertiesBodyParams bodyParams,
			Callback<GetControlPropertiesResponse> cb);
}
