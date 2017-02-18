package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Venue extends Area {
    
    public static final Venue DEFAULT_EVENT_VENUE;
    static{
        DEFAULT_EVENT_VENUE = new Venue("Universal Orlando Resort", 0);
    }

	@SerializedName("ContainedLands")
	List<VenueLand> containedLands;

	@SerializedName("StreetAddress")
	StreetAddress streetAddress;

	@SerializedName("Hours")
	List<VenueHours> hours;

	@SerializedName("PhoneNumber")
	String phoneNumber;

	@SerializedName("PhoneNumberCopy")
	String phoneNumberCopy;

	@SerializedName("AdmissionRequired")
	Boolean admissionRequired;

	@ParcelConstructor
    public Venue() {}
    
    public Venue(String displayName, long venueId) {
        this.displayName = displayName;
        this.id = venueId;
    }

	/**
	 * @return the containedLands
	 */
	public List<VenueLand> getContainedLands() {
		return containedLands;
	}

	/**
	 * @return the streetAddress
	 */
	public StreetAddress getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @return the hours
	 */
	public List<VenueHours> getHours() {
		return hours;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 *
	 * @return phoneNumberCopy
	 */
	public String getPhoneNumberCopy() {
		return phoneNumberCopy;
	}

	/**
	 * @return the admissionRequired
	 */
	public Boolean getAdmissionRequired() {
		return admissionRequired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((admissionRequired == null) ? 0 : admissionRequired.hashCode());
		result = prime * result + ((containedLands == null) ? 0 : containedLands.hashCode());
		result = prime * result + ((hours == null) ? 0 : hours.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((streetAddress == null) ? 0 : streetAddress.hashCode());
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Venue other = (Venue) obj;
		if (admissionRequired == null) {
			if (other.admissionRequired != null) {
				return false;
			}
		}
		else if (!admissionRequired.equals(other.admissionRequired)) {
			return false;
		}
		if (containedLands == null) {
			if (other.containedLands != null) {
				return false;
			}
		}
		else if (!containedLands.equals(other.containedLands)) {
			return false;
		}
		if (hours == null) {
			if (other.hours != null) {
				return false;
			}
		}
		else if (!hours.equals(other.hours)) {
			return false;
		}
		if (phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		}
		else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		if (streetAddress == null) {
			if (other.streetAddress != null) {
				return false;
			}
		}
		else if (!streetAddress.equals(other.streetAddress)) {
			return false;
		}
		return true;
	}

}
