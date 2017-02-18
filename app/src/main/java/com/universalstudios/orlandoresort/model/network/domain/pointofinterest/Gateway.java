package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class Gateway extends PointOfInterest {

	@SerializedName("GatewayType")
	String gatewayType;

	/**
	 * @return the gatewayType
	 */
	public String getGatewayType() {
		return gatewayType;
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
		result = prime * result + ((gatewayType == null) ? 0 : gatewayType.hashCode());
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
		Gateway other = (Gateway) obj;
		if (gatewayType == null) {
			if (other.gatewayType != null) {
				return false;
			}
		} else if (!gatewayType.equals(other.gatewayType)) {
			return false;
		}
		return true;
	}
}
