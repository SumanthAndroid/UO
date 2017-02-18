package com.universalstudios.orlandoresort.model.alerts;

import com.google.gson.annotations.SerializedName;

/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public class WaitTimeAlert extends Alert {

	private static final String ID_STRING_TAG_WAIT_TIME = "WAIT_TIME";

	@SerializedName("notifyThresholdInMin")
	private Integer notifyThresholdInMin;

	/**
	 * @param poiId
	 * @param poiName
	 * @param notifyThresholdInMin
	 */
	public WaitTimeAlert(Long poiId, String poiName, Integer notifyThresholdInMin) {
		super(poiId, poiName);
		this.notifyThresholdInMin = notifyThresholdInMin;
	}

	public static String computeIdString(Long poiId) {
		return new StringBuilder().append(poiId).append("_").append(ID_STRING_TAG_WAIT_TIME).toString();
	}

	@Override
	public String getIdString() {
		return computeIdString(getPoiId());
	}

	/**
	 * @return the notifyThresholdInMin
	 */
	public Integer getNotifyThresholdInMin() {
		return notifyThresholdInMin;
	}

	/**
	 * @param notifyThresholdInMin the notifyThresholdInMin to set
	 */
	public void setNotifyThresholdInMin(Integer notifyThresholdInMin) {
		this.notifyThresholdInMin = notifyThresholdInMin;
	}

}