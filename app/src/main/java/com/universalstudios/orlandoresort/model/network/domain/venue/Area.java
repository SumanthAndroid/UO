package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.domain.global.LatLon;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class Area extends GsonObject {

	@SerializedName("MblDisplayName")
	String displayName;

	@SerializedName("MblLongDescription")
	String longDescription;

	@SerializedName("GpsBoundary")
	List<LatLon> gpsBoundary;

	@SerializedName("Url")
	String urlEndpoint;

	@SerializedName("Id")
	Long id;

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @return the gpsBoundary
	 */
	public List<LatLon> getGpsBoundary() {
		return gpsBoundary;
	}

	/**
	 * @return the urlEndpoint
	 */
	public String getUrlEndpoint() {
		return urlEndpoint;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((gpsBoundary == null) ? 0 : gpsBoundary.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((longDescription == null) ? 0 : longDescription.hashCode());
		result = prime * result + ((urlEndpoint == null) ? 0 : urlEndpoint.hashCode());
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
		Area other = (Area) obj;
		if (displayName == null) {
			if (other.displayName != null) {
				return false;
			}
		}
		else if (!displayName.equals(other.displayName)) {
			return false;
		}
		if (gpsBoundary == null) {
			if (other.gpsBoundary != null) {
				return false;
			}
		}
		else if (!gpsBoundary.equals(other.gpsBoundary)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		if (longDescription == null) {
			if (other.longDescription != null) {
				return false;
			}
		}
		else if (!longDescription.equals(other.longDescription)) {
			return false;
		}
		if (urlEndpoint == null) {
			if (other.urlEndpoint != null) {
				return false;
			}
		}
		else if (!urlEndpoint.equals(other.urlEndpoint)) {
			return false;
		}
		return true;
	}

}
