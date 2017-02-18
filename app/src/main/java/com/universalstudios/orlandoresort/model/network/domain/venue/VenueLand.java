package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class VenueLand extends Area {

	@SerializedName("ContainingVenueId")
	Long containingVenueId;

	/**
	 * @return the containingVenueId
	 */
	public Long getContainingVenueId() {
		return containingVenueId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((containingVenueId == null) ? 0 : containingVenueId.hashCode());
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
		VenueLand other = (VenueLand) obj;
		if (containingVenueId == null) {
			if (other.containingVenueId != null) {
				return false;
			}
		}
		else if (!containingVenueId.equals(other.containingVenueId)) {
			return false;
		}
		return true;
	}

}
