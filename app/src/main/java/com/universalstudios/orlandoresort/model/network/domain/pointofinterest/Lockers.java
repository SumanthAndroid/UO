package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class Lockers extends PointOfInterest {

	@SerializedName("IsFree")
	Boolean isFree;

	@SerializedName("LockerSizes")
	List<Dimensions> lockerSizes;

	@SerializedName("LockerPrices")
	List<String> lockerPrices;

	@Override
	public long getSubTypeFlags() {
		long subTypeFlags = 0l;

		if (isFree != null) {
			if (isFree) {
				subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RIDE;
			}
			else {
				subTypeFlags |= PointsOfInterestTable.VAL_SUB_TYPE_FLAG_LOCKER_TYPE_RENTAL;
			}
		}
		return super.getSubTypeFlags() | subTypeFlags;
	}

	/**
	 * @return the isFree
	 */
	public Boolean getIsFree() {
		return isFree;
	}

	/**
	 * @return the lockerSizes
	 */
	public synchronized List<Dimensions> getLockerSizes() {
		return lockerSizes;
	}

	/**
	 * @return the lockerPrices
	 */
	public synchronized List<String> getLockerPrices() {
		return lockerPrices;
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
		result = prime * result + ((lockerPrices == null) ? 0 : lockerPrices.hashCode());
		result = prime * result + ((isFree == null) ? 0 : isFree.hashCode());
		result = prime * result + ((lockerSizes == null) ? 0 : lockerSizes.hashCode());
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
		Lockers other = (Lockers) obj;
		if (lockerPrices == null) {
			if (other.lockerPrices != null) {
				return false;
			}
		}
		else if (!lockerPrices.equals(other.lockerPrices)) {
			return false;
		}
		if (isFree == null) {
			if (other.isFree != null) {
				return false;
			}
		}
		else if (!isFree.equals(other.isFree)) {
			return false;
		}
		if (lockerSizes == null) {
			if (other.lockerSizes != null) {
				return false;
			}
		}
		else if (!lockerSizes.equals(other.lockerSizes)) {
			return false;
		}
		return true;
	}

}
