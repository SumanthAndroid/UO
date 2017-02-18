package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 *
 * @author Steven Byle
 */
@Parcel
public class Hours extends GsonObject {

	/**
	 *
	 */
	@ParcelConstructor
	public Hours() {
		super();
	}

	/**
	 * @param openTime
	 * @param closeTime
	 */
	public Hours(String openTime, String closeTime) {
		super();
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

	@SerializedName("OpenTime")
	String openTime;

	@SerializedName("CloseTime")
	String closeTime;

	public static Date parseHoursToDate(String hours) {
		SimpleDateFormat hoursFormatNoSpace;
		SimpleDateFormat hoursFormatSpace;


		hoursFormatNoSpace = new SimpleDateFormat("h:mma", Locale.US);
		hoursFormatNoSpace.setTimeZone(DateTimeUtils.getParkTimeZone());
		hoursFormatSpace = new SimpleDateFormat("h:mm a", Locale.US);
		hoursFormatSpace.setTimeZone(DateTimeUtils.getParkTimeZone());

		try {
			return hoursFormatNoSpace.parse(hours);
		}
		catch (ParseException e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}

		try {
			return hoursFormatSpace.parse(hours);
		}
		catch (ParseException e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * @return the openTime
	 */
	public String getOpenTime() {
		return openTime;
	}

	/**
	 * @return the closeTime
	 */
	public String getCloseTime() {
		return closeTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closeTime == null) ? 0 : closeTime.hashCode());
		result = prime * result + ((openTime == null) ? 0 : openTime.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Hours other = (Hours) obj;
		if (closeTime == null) {
			if (other.closeTime != null) {
				return false;
			}
		}
		else if (!closeTime.equals(other.closeTime)) {
			return false;
		}
		if (openTime == null) {
			if (other.openTime != null) {
				return false;
			}
		}
		else if (!openTime.equals(other.openTime)) {
			return false;
		}
		return true;
	}

}
