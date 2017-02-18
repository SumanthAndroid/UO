package com.universalstudios.orlandoresort.model.alerts;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;

/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public abstract class Alert extends GsonObject {

	@SerializedName("poiId")
	private Long poiId;

	@SerializedName("poiName")
	private String poiName;

	/**
	 * @param poiId
	 * @param poiName
	 */
	public Alert(Long poiId, String poiName) {
		this.poiId = poiId;
		this.poiName = poiName;
	}

	/**
	 * 
	 * @return a unique id for this alert
	 */
	public abstract String getIdString();

	/**
	 * Create a specific alert child class object from JSON, based
	 * on the alert type id.
	 * 
	 * @param alertObjectJson
	 * @param alertTypeId
	 * @return Properly parsed Alert object
	 */
	public static Alert fromJson(String alertObjectJson, Integer alertTypeId) {
		if (alertObjectJson == null || alertTypeId == null) {
			return null;
		}

		switch (alertTypeId) {
			case AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME:
				return GsonObject.fromJson(alertObjectJson, ShowTimeAlert.class);
			case AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME:
				return GsonObject.fromJson(alertObjectJson, WaitTimeAlert.class);
			case AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN:
				return GsonObject.fromJson(alertObjectJson, RideOpenAlert.class);
			default:
				return GsonObject.fromJson(alertObjectJson, Alert.class);
		}
	}

	/**
	 * @return the poiName
	 */
	public String getPoiName() {
		return poiName;
	}

	/**
	 * @param poiName the poiName to set
	 */
	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	/**
	 * @return the poiId
	 */
	public Long getPoiId() {
		return poiId;
	}

	/**
	 * @param poiId
	 *            the poiId to set
	 */
	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

}