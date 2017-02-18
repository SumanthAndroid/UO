package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * 
 * 
 * @author Steven Byle
 */
public class Dimensions extends GsonObject {

	@SerializedName("HeightInInches")
	private Float heightInInches;

	@SerializedName("WidthInInches")
	private Float widthInInches;

	@SerializedName("LengthInInches")
	private Float lengthInInches;

	/**
	 * @return the heightInInches
	 */
	public synchronized Float getHeightInInches() {
		return heightInInches;
	}

	/**
	 * @return the widthInInches
	 */
	public synchronized Float getWidthInInches() {
		return widthInInches;
	}

	/**
	 * @return the lengthInInches
	 */
	public synchronized Float getLengthInInches() {
		return lengthInInches;
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
		result = prime * result + ((lengthInInches == null) ? 0 : lengthInInches.hashCode());
		result = prime * result + ((widthInInches == null) ? 0 : widthInInches.hashCode());
		result = prime * result + ((heightInInches == null) ? 0 : heightInInches.hashCode());
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
		Dimensions other = (Dimensions) obj;
		if (lengthInInches == null) {
			if (other.lengthInInches != null) {
				return false;
			}
		}
		else if (!lengthInInches.equals(other.lengthInInches)) {
			return false;
		}
		if (widthInInches == null) {
			if (other.widthInInches != null) {
				return false;
			}
		}
		else if (!widthInInches.equals(other.widthInInches)) {
			return false;
		}
		if (heightInInches == null) {
			if (other.heightInInches != null) {
				return false;
			}
		}
		else if (!heightInInches.equals(other.heightInInches)) {
			return false;
		}
		return true;
	}

}
