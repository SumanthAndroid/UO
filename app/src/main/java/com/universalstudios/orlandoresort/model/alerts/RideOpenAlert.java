package com.universalstudios.orlandoresort.model.alerts;


/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public class RideOpenAlert extends Alert {

	private static final String ID_STRING_TAG_RIDE_OPEN = "RIDE_OPEN";

	/**
	 * @param poiId
	 * @param poiName
	 */
	public RideOpenAlert(Long poiId, String poiName) {
		super(poiId, poiName);
	}

	/**
	 * @param poiId
	 * @return
	 */
	public static String computeIdString(Long poiId) {
		return new StringBuilder().append(poiId).append("_").append(ID_STRING_TAG_RIDE_OPEN).toString();
	}

	@Override
	public String getIdString() {
		return computeIdString(getPoiId());
	}

}