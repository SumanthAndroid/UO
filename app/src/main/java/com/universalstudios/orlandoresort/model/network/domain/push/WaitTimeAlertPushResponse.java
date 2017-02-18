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
public class WaitTimeAlertPushResponse extends PushResponse {

	@SerializedName("RideName")
	String rideName;

	@SerializedName("AlertMessage")
	String alertMessage;

	@SerializedName("CurrentWaitTimeInMin")
	Integer currentWaitTimeInMin;

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

	/**
	 * @return the currentWaitTimeInMin
	 */
	public Integer getCurrentWaitTimeInMin() {
		return currentWaitTimeInMin;
	}

}
