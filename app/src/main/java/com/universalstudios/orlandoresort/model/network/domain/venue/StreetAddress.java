package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class StreetAddress extends GsonObject {

	@SerializedName("AddressLine1")
	String addressLine1;

	@SerializedName("AddressLine2")
	String addressLine2;

	@SerializedName("City")
	String city;

	@SerializedName("State")
	String stateAbbrev;

	@SerializedName("ZipCode")
	String zipCode;

	public String getFormattedAddress(boolean useSpaceNotNewLine) {
		// Required fields
		if (addressLine1 != null && !addressLine1.isEmpty()
				&& city != null && !city.isEmpty()
				&& stateAbbrev != null && !stateAbbrev.isEmpty()) {

			String spaceOrNewLine = useSpaceNotNewLine ? " " : "\n";

			StringBuilder formattedAddress = new StringBuilder(addressLine1);

			// Address line 2 optional
			if (addressLine2 != null && !addressLine2.isEmpty()) {
				formattedAddress.append(spaceOrNewLine).append(addressLine2);
			}

			formattedAddress.append(spaceOrNewLine)
			.append(city).append(", ").append(stateAbbrev);

			// Zip code optional
			if (zipCode != null && !zipCode.isEmpty()) {
				formattedAddress.append(" ").append(zipCode);
			}

			return (formattedAddress.toString());
		}
		return null;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the stateAbbrev
	 */
	public String getStateAbbrev() {
		return stateAbbrev;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
		result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((stateAbbrev == null) ? 0 : stateAbbrev.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		StreetAddress other = (StreetAddress) obj;
		if (addressLine1 == null) {
			if (other.addressLine1 != null) {
				return false;
			}
		}
		else if (!addressLine1.equals(other.addressLine1)) {
			return false;
		}
		if (addressLine2 == null) {
			if (other.addressLine2 != null) {
				return false;
			}
		}
		else if (!addressLine2.equals(other.addressLine2)) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		}
		else if (!city.equals(other.city)) {
			return false;
		}
		if (stateAbbrev == null) {
			if (other.stateAbbrev != null) {
				return false;
			}
		}
		else if (!stateAbbrev.equals(other.stateAbbrev)) {
			return false;
		}
		if (zipCode == null) {
			if (other.zipCode != null) {
				return false;
			}
		}
		else if (!zipCode.equals(other.zipCode)) {
			return false;
		}
		return true;
	}

}
