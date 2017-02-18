package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class GetVenueHoursResponse extends NetworkResponse {

	// Not parsed, added from raw response
	@SerializedName("VenueHours")
	List<VenueHours> venueHours;

	/**
	 *
	 */
	@ParcelConstructor
	public GetVenueHoursResponse() {
		super();
	}

	/**
	 * @param venueHours
	 */
	public GetVenueHoursResponse(List<VenueHours> venueHours) {
		super();
		this.venueHours = venueHours;
	}

	/**
	 * @return the venueHours
	 */
	public List<VenueHours> getVenueHours() {
		return venueHours;
	}

}
