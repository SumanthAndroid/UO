package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class DailyHours extends GsonObject {

	@SerializedName("Sunday")
	Hours sunday;

	@SerializedName("Monday")
	Hours monday;

	@SerializedName("Tuesday")
	Hours tuesday;

	@SerializedName("Wednesday")
	Hours wednesday;

	@SerializedName("Thursday")
	Hours thursday;

	@SerializedName("Friday")
	Hours friday;

	@SerializedName("Saturday")
	Hours saturday;

	/**
	 * @return the sunday
	 */
	public Hours getSunday() {
		return sunday;
	}

	/**
	 * @return the monday
	 */
	public Hours getMonday() {
		return monday;
	}

	/**
	 * @return the tuesday
	 */
	public Hours getTuesday() {
		return tuesday;
	}

	/**
	 * @return the wednesday
	 */
	public Hours getWednesday() {
		return wednesday;
	}

	/**
	 * @return the thursday
	 */
	public Hours getThursday() {
		return thursday;
	}

	/**
	 * @return the friday
	 */
	public Hours getFriday() {
		return friday;
	}

	/**
	 * @return the saturday
	 */
	public Hours getSaturday() {
		return saturday;
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
		result = prime * result + ((friday == null) ? 0 : friday.hashCode());
		result = prime * result + ((monday == null) ? 0 : monday.hashCode());
		result = prime * result + ((saturday == null) ? 0 : saturday.hashCode());
		result = prime * result + ((sunday == null) ? 0 : sunday.hashCode());
		result = prime * result + ((thursday == null) ? 0 : thursday.hashCode());
		result = prime * result + ((tuesday == null) ? 0 : tuesday.hashCode());
		result = prime * result + ((wednesday == null) ? 0 : wednesday.hashCode());
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
		DailyHours other = (DailyHours) obj;
		if (friday == null) {
			if (other.friday != null) {
				return false;
			}
		}
		else if (!friday.equals(other.friday)) {
			return false;
		}
		if (monday == null) {
			if (other.monday != null) {
				return false;
			}
		}
		else if (!monday.equals(other.monday)) {
			return false;
		}
		if (saturday == null) {
			if (other.saturday != null) {
				return false;
			}
		}
		else if (!saturday.equals(other.saturday)) {
			return false;
		}
		if (sunday == null) {
			if (other.sunday != null) {
				return false;
			}
		}
		else if (!sunday.equals(other.sunday)) {
			return false;
		}
		if (thursday == null) {
			if (other.thursday != null) {
				return false;
			}
		}
		else if (!thursday.equals(other.thursday)) {
			return false;
		}
		if (tuesday == null) {
			if (other.tuesday != null) {
				return false;
			}
		}
		else if (!tuesday.equals(other.tuesday)) {
			return false;
		}
		if (wednesday == null) {
			if (other.wednesday != null) {
				return false;
			}
		}
		else if (!wednesday.equals(other.wednesday)) {
			return false;
		}
		return true;
	}

}
