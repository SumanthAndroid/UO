package com.universalstudios.orlandoresort.model.state.debug;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public class UniversalOrlandoDebugState extends GsonObject {

	@SerializedName("showGeofencesOnMap")
	private boolean showGeofencesOnMap;

	@SerializedName("mockLocationString")
	private String mockLocationString;

	/**
	 * @return the mockLocationString
	 */
	public synchronized String getMockLocationString() {
		return mockLocationString;
	}

	/**
	 * @param mockLocationString
	 *            the mockLocationString to set
	 */
	public synchronized void setMockLocationString(String mockLocationString) {
		this.mockLocationString = mockLocationString;
	}

	/**
	 * @return the showGeofencesOnMap
	 */
	public synchronized boolean isShowGeofencesOnMap() {
		return showGeofencesOnMap;
	}

	/**
	 * @param showGeofencesOnMap
	 *            the showGeofencesOnMap to set
	 */
	public synchronized void setShowGeofencesOnMap(boolean showGeofencesOnMap) {
		this.showGeofencesOnMap = showGeofencesOnMap;
	}

}