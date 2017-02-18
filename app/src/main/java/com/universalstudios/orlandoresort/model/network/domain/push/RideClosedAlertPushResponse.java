/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.push;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class RideClosedAlertPushResponse extends PushResponse {

	@SerializedName("RideName")
	String rideName;

	@SerializedName("AlertMessage")
	String alertMessage;

	/**
	 * @return the rideName
	 */
	public String getRideName() {
		return rideName;
	}

	/**
	 * @return the alertMessage
	 */
	public String getAlertMessage() {
		return alertMessage;
	}

}
