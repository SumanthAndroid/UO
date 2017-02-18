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
public class NewsPushResponse extends PushResponse {

	@SerializedName("AlertTitle")
	String alertTitle;

	@SerializedName("AlertMessage")
	String alertMessage;

	/**
	 * @return the alertTitle
	 */
	public String getAlertTitle() {
		return alertTitle;
	}

	/**
	 * @return the alertMessage
	 */
	public String getAlertMessage() {
		return alertMessage;
	}

}
