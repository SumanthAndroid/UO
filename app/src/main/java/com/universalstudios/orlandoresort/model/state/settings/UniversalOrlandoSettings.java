package com.universalstudios.orlandoresort.model.state.settings;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public class UniversalOrlandoSettings extends GsonObject {

	private static final boolean DEFAULT_IS_GUIDE_ME_SOUND_ON = true;
	private static final boolean DEFAULT_IS_GUIDE_ME_VIBRATE_ON = true;
	private static final boolean DEFAULT_IS_SHOW_PARK_NEWS_NOTIFICATIONS_ON = true;

	@SerializedName("isGuideMeSoundOn")
	private Boolean isGuideMeSoundOn;

	@SerializedName("isGuideMeVibrateOn")
	private Boolean isGuideMeVibrateOn;

	@SerializedName("isShowParkNewsNotificationsOn")
	private Boolean isShowParkNewsNotificationsOn;

	/**
	 * @return the isGuideMeSoundOn
	 */
	public synchronized boolean getIsGuideMeSoundOn() {
		if (isGuideMeSoundOn == null) {
			isGuideMeSoundOn = DEFAULT_IS_GUIDE_ME_SOUND_ON;
		}
		return isGuideMeSoundOn;
	}

	/**
	 * @param isGuideMeSoundOn
	 *            the isGuideMeSoundOn to set
	 */
	public synchronized void setIsGuideMeSoundOn(Boolean isGuideMeSoundOn) {
		this.isGuideMeSoundOn = isGuideMeSoundOn;
	}

	/**
	 * @return the isGuideMeVibrateOn
	 */
	public synchronized boolean getIsGuideMeVibrateOn() {
		if (isGuideMeVibrateOn == null) {
			isGuideMeVibrateOn = DEFAULT_IS_GUIDE_ME_VIBRATE_ON;
		}
		return isGuideMeVibrateOn;
	}

	/**
	 * @param isGuideMeVibrateOn
	 *            the isGuideMeVibrateOn to set
	 */
	public synchronized void setIsGuideMeVibrateOn(Boolean isGuideMeVibrateOn) {
		this.isGuideMeVibrateOn = isGuideMeVibrateOn;
	}

	/**
	 * @return the isShowParkNewsNotificationsOn
	 */
	public synchronized boolean getIsShowParkNewsNotificationsOn() {
		if (isShowParkNewsNotificationsOn == null) {
			isShowParkNewsNotificationsOn = DEFAULT_IS_SHOW_PARK_NEWS_NOTIFICATIONS_ON;
		}
		return isShowParkNewsNotificationsOn;
	}

	/**
	 * @param isShowParkNewsNotificationsOn the isShowParkNewsNotificationsOn to set
	 */
	public synchronized void setIsShowParkNewsNotificationsOn(Boolean isShowParkNewsNotificationsOn) {
		this.isShowParkNewsNotificationsOn = isShowParkNewsNotificationsOn;
	}



}