/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

import com.google.android.gms.maps.model.Marker;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueLand;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class PointOfInterestMarker {

	private Marker marker;
	private final Integer poiTypeId;
	private Integer poiHashCode;
	private String poiObjectJson;
	private String venueObjectJson;
	private String venueLandObjectJson;
	private final Long poiId;
	private PointOfInterest pointOfInterest;
	private Venue venue;
	private VenueLand venueLand;
	public Long subTypeFlags;
	private List<PointOfInterestMarker> sharedPoints;

	public PointOfInterestMarker(Marker marker, Long poiId, Integer poiTypeId, Integer poiHashCode,
			String poiObjectJson, String venueObjectJson, String venueLandObjectJson) {
		super();
		this.marker = marker;
		this.poiId = poiId;
		this.poiTypeId = poiTypeId;
		this.poiHashCode = poiHashCode;
		this.poiObjectJson = poiObjectJson;
		this.venueObjectJson = venueObjectJson;
		this.venueLandObjectJson = venueLandObjectJson;
		// Only parse the POI and venue objects when needed, improves
		// performance
		pointOfInterest = null;
		venue = null;
	}

	public PointOfInterestMarker(Marker marker, Long poiId, Integer poiTypeId, Integer poiHashCode,
			String poiObjectJson, String venueObjectJson, String venueLandObjectJson, Long subTypeFlags) {
		super();
		this.marker = marker;
		this.poiId = poiId;
		this.poiTypeId = poiTypeId;
		this.poiHashCode = poiHashCode;
		this.poiObjectJson = poiObjectJson;
		this.venueObjectJson = venueObjectJson;
		this.venueLandObjectJson = venueLandObjectJson;
		this.subTypeFlags = subTypeFlags;
		// Only parse the POI and venue objects when needed, improves
		// performance
		pointOfInterest = null;
		venue = null;
	}

	/**
	 * @return the poi
	 */
	public PointOfInterest getPointOfInterest() {
		if (pointOfInterest == null) {
			pointOfInterest = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
		}
		return pointOfInterest;
	}

	public void addSharedPoint(PointOfInterestMarker marker) {
		if (sharedPoints == null) {
			sharedPoints = new ArrayList<>();
		}
		sharedPoints.add(marker);
	}

	public List<PointOfInterestMarker> getSharedPoints() {
		return sharedPoints;
	}

	/**
	 * @return the venue
	 */
	public Venue getVenue() {
		if (venue == null) {
			venue = GsonObject.fromJson(venueObjectJson, Venue.class);
		}
		return venue;
	}

	/**
	 * @return the venueLand
	 */
	public VenueLand getVenueLand() {
		if (venueLand == null) {
			venueLand = GsonObject.fromJson(venueLandObjectJson, VenueLand.class);
		}
		return venueLand;
	}

	/**
	 * @return the poiId
	 */
	public Long getPoiId() {
		return poiId;
	}

	/**
	 * @return the poiHashCode
	 */
	public Integer getPoiHashCode() {
		return poiHashCode;
	}

	/**
	 * @return the marker
	 */
	public Marker getMarker() {
		return marker;
	}

	/**
	 * @return the poiTypeId
	 */
	public Integer getPoiTypeId() {
		return poiTypeId;
	}

	/**
	 * @return the poiObjectJson
	 */
	public String getPoiObjectJson() {
		return poiObjectJson;
	}

	/**
	 * @return the venueObjectJson
	 */
	public String getVenueObjectJson() {
		return venueObjectJson;
	}

	/**
	 * @param marker
	 *            the marker to set
	 */
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

}
