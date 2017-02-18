package com.universalstudios.orlandoresort.model.network.domain.control;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class GetCommerceEnabledResponse extends NetworkResponse {

	@SerializedName("statusCode")
	int statusCode;

	@SerializedName("result")
	CommerceEnabledProperties mCommerceEnabledProperties;

	public CommerceEnabledProperties getCommerceEnabledProperties() {
		return mCommerceEnabledProperties;
	}

	public int getStatusCode() {
		return statusCode;
	}
}