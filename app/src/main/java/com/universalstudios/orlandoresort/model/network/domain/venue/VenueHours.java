package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author Steven Byle
 */
@Parcel
public class VenueHours extends GsonObject {

	@SerializedName("OpenTimeString")
	String openTimeString;

	@SerializedName("OpenTimeUnix")
	Long openTimeUnix;

	@SerializedName("CloseTimeString")
	String closeTimeString;

	@SerializedName("CloseTimeUnix")
	Long closeTimeUnix;

	/**
	 * @param openTimeString
	 * @param openTimeUnix
	 * @param closeTimeString
	 * @param closeTimeUnix
	 */
	@ParcelConstructor
	public VenueHours(String openTimeString, Long openTimeUnix, String closeTimeString, Long closeTimeUnix) {
		super();
		this.openTimeString = openTimeString;
		this.openTimeUnix = openTimeUnix;
		this.closeTimeString = closeTimeString;
		this.closeTimeUnix = closeTimeUnix;
	}

	/**
	 * @return the openTimeString
	 */
	public String getOpenTimeString() {
		return openTimeString;
	}

	/**
	 * @return the openTimeUnix
	 */
	public Long getOpenTimeUnix() {
		return openTimeUnix;
	}

	/**
	 * @return the closeTimeString
	 */
	public String getCloseTimeString() {
		return closeTimeString;
	}

	/**
	 * @return the closeTimeUnix
	 */
	public Long getCloseTimeUnix() {
		return closeTimeUnix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closeTimeString == null) ? 0 : closeTimeString.hashCode());
		result = prime * result + ((closeTimeUnix == null) ? 0 : closeTimeUnix.hashCode());
		result = prime * result + ((openTimeString == null) ? 0 : openTimeString.hashCode());
		result = prime * result + ((openTimeUnix == null) ? 0 : openTimeUnix.hashCode());
		return result;
	}

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
		VenueHours other = (VenueHours) obj;
		if (closeTimeString == null) {
			if (other.closeTimeString != null) {
				return false;
			}
		}
		else if (!closeTimeString.equals(other.closeTimeString)) {
			return false;
		}
		if (closeTimeUnix == null) {
			if (other.closeTimeUnix != null) {
				return false;
			}
		}
		else if (!closeTimeUnix.equals(other.closeTimeUnix)) {
			return false;
		}
		if (openTimeString == null) {
			if (other.openTimeString != null) {
				return false;
			}
		}
		else if (!openTimeString.equals(other.openTimeString)) {
			return false;
		}
		if (openTimeUnix == null) {
			if (other.openTimeUnix != null) {
				return false;
			}
		}
		else if (!openTimeUnix.equals(other.openTimeUnix)) {
			return false;
		}
		return true;
	}


}
