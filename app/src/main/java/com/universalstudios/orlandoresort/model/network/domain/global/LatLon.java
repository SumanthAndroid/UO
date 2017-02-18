package com.universalstudios.orlandoresort.model.network.domain.global;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class LatLon extends GsonObject {

	@SerializedName("Latitude")
	Double latitude;

	@SerializedName("Longitude")
	Double longitude;

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
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
		LatLon other = (LatLon) obj;
		if (latitude == null) {
			if (other.latitude != null) {
				return false;
			}
		}
		else if (!latitude.equals(other.latitude)) {
			return false;
		}
		if (longitude == null) {
			if (other.longitude != null) {
				return false;
			}
		}
		else if (!longitude.equals(other.longitude)) {
			return false;
		}
		return true;
	}
}
