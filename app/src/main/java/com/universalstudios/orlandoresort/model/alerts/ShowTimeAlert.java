package com.universalstudios.orlandoresort.model.alerts;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public class ShowTimeAlert extends Alert {

	public static final int NOTIFY_INTERVAL_MIN_SHOW_START = 0;
	public static final int NOTIFY_INTERVAL_MIN_5_MIN = 5;
	public static final int NOTIFY_INTERVAL_MIN_15_MIN = 15;
	public static final int NOTIFY_INTERVAL_MIN_30_MIN = 30;
	public static final int NOTIFY_INTERVAL_MIN_1_HR = 1 * 60;
	public static final int NOTIFY_INTERVAL_MIN_2_HR = 2 * 60;

	@SerializedName("notifyMinBefore")
	private Integer notifyMinBefore;

	@SerializedName("showTime")
	private String showTime;

	/**
	 * @param poiId
	 * @param poiName
	 * @param notifyMinBefore
	 * @param showTime
	 */
	public ShowTimeAlert(Long poiId, String poiName, Integer notifyMinBefore, String showTime) {
		super(poiId, poiName);
		this.notifyMinBefore = notifyMinBefore;
		this.showTime = showTime;
	}

	public static String computeIdString(Long poiId, String showTime) {
		return new StringBuilder().append(poiId).append("_").append(showTime).toString();
	}

	@Override
	public String getIdString() {
		return computeIdString(getPoiId(), showTime);
	}

	/**
	 * @return the show time for today as a date, in the park's time zone
	 */
	public Date getShowTimeDateForToday() {
		// First, parse in the show time from 24 hour ("14:30:00"), park time
		SimpleDateFormat uoTwentyFourHourFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
		uoTwentyFourHourFormat.setTimeZone(DateTimeUtils.getParkTimeZone());

		Date showTimeDate = null;
		try {
			showTimeDate = uoTwentyFourHourFormat.parse(showTime);
		}
		catch (Exception e) {
			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}

		if (showTimeDate != null) {
			// Get the hours and minutes that the show is today, and show it in the park's timezone
			Calendar showTimeCal = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
			SimpleDateFormat sdfOutHourOfDay = new SimpleDateFormat("H", Locale.US);
			sdfOutHourOfDay.setTimeZone(DateTimeUtils.getParkTimeZone());
			showTimeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sdfOutHourOfDay.format(showTimeDate)));

			SimpleDateFormat sdfOutMinute = new SimpleDateFormat("m", Locale.US);
			sdfOutMinute.setTimeZone(DateTimeUtils.getParkTimeZone());
			showTimeCal.set(Calendar.MINUTE, Integer.parseInt(sdfOutMinute.format(showTimeDate)));

			showTimeCal.set(Calendar.SECOND, 0);
			showTimeCal.set(Calendar.MILLISECOND, 0);

			return showTimeCal.getTime();
		}
		else {
			// If the show time can't be formatted, return null
			return null;
		}
	}

	/**
	 * @return the alert time for today as a date, in the user's time zone
	 */
	public Date getAlertDateForToday() {
		Date showTimeDate = getShowTimeDateForToday();
		if (showTimeDate != null) {
			long alertTimeInMillis = showTimeDate.getTime() - (notifyMinBefore * 60 * 1000);
			return new Date(alertTimeInMillis);
		}
		else {
			return null;
		}
	}

	/**
	 * @return the notifyMinBefore
	 */
	public Integer getNotifyMinBefore() {
		return notifyMinBefore;
	}

	/**
	 * @param notifyMinBefore
	 *            the notifyMinBefore to set
	 */
	public void setNotifyMinBefore(Integer notifyMinBefore) {
		this.notifyMinBefore = notifyMinBefore;
	}

	/**
	 * @return the showTime
	 */
	public String getShowTime() {
		return showTime;
	}

	/**
	 * @param showTime
	 *            the showTime to set
	 */
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

}