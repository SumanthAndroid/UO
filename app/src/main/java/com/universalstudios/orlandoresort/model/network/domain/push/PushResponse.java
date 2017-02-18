/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.push;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class PushResponse extends GsonObject {
	public static final String KEY_NOTIFICATION_TYPE = "NotificationType";
	public static final String KEY_NOTIFICATION_JSON_OBJECT = "Notification";

	public static final String NOTIFICATION_TYPE_WAIT_TIME = "WaitTimeNotification";
	public static final String NOTIFICATION_TYPE_PARK_NEWS = "ParkNewsNotification";
	public static final String NOTIFICATION_TYPE_RIDE_CLOSED = "RideClosedNotification";
	public static final String NOTIFICATION_TYPE_RIDE_OPEN = "RideOpenNotification";

	@SerializedName("Id")
	Long id;

	@SerializedName("TimeStampUnix")
	Long timeStampUnix;

	@SerializedName("ExpirationUnix")
	Long expirationUnix;

	public static PushResponse getPushResponse(String notificationType, String notificationJsonObject) {
		if (notificationType == null || notificationType.isEmpty()
				|| notificationJsonObject == null || notificationJsonObject.isEmpty()) {
			return null;
		}

		// Parse the proper object based on the type
		if (notificationType.equals(NOTIFICATION_TYPE_WAIT_TIME)) {
			return GsonObject.fromJson(notificationJsonObject, WaitTimeAlertPushResponse.class);
		}
		else if (notificationType.equals(NOTIFICATION_TYPE_PARK_NEWS)) {
			return GsonObject.fromJson(notificationJsonObject, NewsPushResponse.class);
		}
		else if (notificationType.equals(NOTIFICATION_TYPE_RIDE_OPEN)) {
			return GsonObject.fromJson(notificationJsonObject, RideOpenAlertPushResponse.class);
		}
		else if (notificationType.equals(NOTIFICATION_TYPE_RIDE_CLOSED)) {
			return GsonObject.fromJson(notificationJsonObject, RideClosedAlertPushResponse.class);
		}
		else {
			return null;
		}
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the timeStampUnix
	 */
	public Long getTimeStampUnix() {
		return timeStampUnix;
	}

	/**
	 * @return the expirationUnix
	 */
	public Long getExpirationUnix() {
		return expirationUnix;
	}

}
