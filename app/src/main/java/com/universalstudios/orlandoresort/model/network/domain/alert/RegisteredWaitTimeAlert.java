package com.universalstudios.orlandoresort.model.network.domain.alert;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class RegisteredWaitTimeAlert extends GsonObject {

	@SerializedName("RideId")
	Long rideId;

	@SerializedName("Threshold")
	Integer thresholdInMin;

	/**
	 * @return the rideId
	 */
	public Long getRideId() {
		return rideId;
	}

	/**
	 * @return the thresholdInMin
	 */
	public Integer getThresholdInMin() {
		return thresholdInMin;
	}
}
