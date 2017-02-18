package com.universalstudios.orlandoresort.model.network.domain.wayfinding;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class Waypoint extends GsonObject {

	@SerializedName("Longitude")
	Double longitude;

	@SerializedName("Latitude")
	Double latitude;

	@SerializedName("RadiusInMeters")
	Integer radiusInMeters;

	@SerializedName("LandId")
	Long landId;

	@SerializedName("VenueId")
	Long venueId;

	@SerializedName("Id")
	Long id;

	@SerializedName("Url")
	String urlEndpoint;

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @return the radiusInMeters
	 */
	public Integer getRadiusInMeters() {
		return radiusInMeters;
	}

	/**
	 * @return the landId
	 */
	public Long getLandId() {
		return landId;
	}

	/**
	 * @return the venueId
	 */
	public Long getVenueId() {
		return venueId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the urlEndpoint
	 */
	public String getUrlEndpoint() {
		return urlEndpoint;
	}
}
