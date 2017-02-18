package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

@Parcel
public class CreateTravelPartyResponse extends NetworkResponse {

	@SerializedName("statusCode")
	String statusCode;

	@SerializedName("result")
	CreatedTravelPartyMember createdTravelPartyMember;

	@SerializedName("message")
	String message;

	/**
	 * Method to get the HTTP Status Code for the response.
	 *
	 * @return a String for the HTTP Status code
	 */
	public String getStatusCode() {
		return statusCode;
	}


	public CreatedTravelPartyMember getCreatedTravelPartyMember() {
		return createdTravelPartyMember;
	}

	public String getMessage() {
		return message;
	}
}
