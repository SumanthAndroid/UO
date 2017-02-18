package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class Restroom extends PointOfInterest {

	@SerializedName("IsFamilyFriendly")
	Boolean isFamilyFriendly;

	@Override
	public long getSubTypeFlags() {
		long subTypeFlags = 0l;

		if (isFamilyFriendly != null) {
			if (isFamilyFriendly == true) {
				subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_FAMILY;
			}
			else {
				subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_RESTROOM_TYPE_STANDARD;
			}
		}
		return super.getSubTypeFlags() | subTypeFlags;
	}

	/**
	 * @return the isFamilyFriendly
	 */
	public Boolean getIsFamilyFriendly() {
		return isFamilyFriendly;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((isFamilyFriendly == null) ? 0 : isFamilyFriendly.hashCode());
		return result;
	}

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
		Restroom other = (Restroom) obj;
		if (isFamilyFriendly == null) {
			if (other.isFamilyFriendly != null) {
				return false;
			}
		}
		else if (!isFamilyFriendly.equals(other.isFamilyFriendly)) {
			return false;
		}
		return true;
	}

}
