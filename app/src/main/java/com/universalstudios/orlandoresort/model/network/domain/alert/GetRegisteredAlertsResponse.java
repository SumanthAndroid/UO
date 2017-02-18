package com.universalstudios.orlandoresort.model.network.domain.alert;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class GetRegisteredAlertsResponse extends NetworkResponse {

	@SerializedName("DeviceId")
	String deviceId;

	@SerializedName("WaitTimeNotifications")
	Boolean waitTimeNotificationsOn;

	@SerializedName("InformationalNotifications")
	Boolean informationalNotificationsOn;

	@SerializedName("ParkNewsNotifications")
	Boolean parkNewsNotificationsOn;

	@SerializedName("EmergencyNotifications")
	Boolean emergencyNotificationsOn;

	@SerializedName("RideOpenNotifications")
	Boolean rideOpenNotificationsOn;

	@SerializedName("RideClosedNotifications")
	Boolean rideClosedNotificationsOn;

	@SerializedName("InPark")
	Boolean inPark;

	@SerializedName("WaitTimeAlerts")
	List<RegisteredWaitTimeAlert> waitTimeAlerts;

	@SerializedName("RideOpenAlerts")
	List<Long> rideOpenAlertPoiIds;

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the waitTimeNotificationsOn
	 */
	public Boolean getWaitTimeNotificationsOn() {
		return waitTimeNotificationsOn;
	}

	/**
	 * @return the informationalNotificationsOn
	 */
	public Boolean getInformationalNotificationsOn() {
		return informationalNotificationsOn;
	}

	/**
	 * @return the parkNewsNotificationsOn
	 */
	public Boolean getParkNewsNotificationsOn() {
		return parkNewsNotificationsOn;
	}

	/**
	 * @return the emergencyNotificationsOn
	 */
	public Boolean getEmergencyNotificationsOn() {
		return emergencyNotificationsOn;
	}

	/**
	 * @return the rideOpenNotificationsOn
	 */
	public Boolean getRideOpenNotificationsOn() {
		return rideOpenNotificationsOn;
	}

	/**
	 * @return the rideClosedNotificationsOn
	 */
	public Boolean getRideClosedNotificationsOn() {
		return rideClosedNotificationsOn;
	}

	/**
	 * @return the inPark
	 */
	public Boolean getInPark() {
		return inPark;
	}

	/**
	 * @return the waitTimeAlerts
	 */
	public List<RegisteredWaitTimeAlert> getWaitTimeAlerts() {
		return waitTimeAlerts;
	}

	/**
	 * @return the rideOpenAlertPoiIds
	 */
	public List<Long> getRideOpenAlertPoiIds() {
		return rideOpenAlertPoiIds;
	}


}