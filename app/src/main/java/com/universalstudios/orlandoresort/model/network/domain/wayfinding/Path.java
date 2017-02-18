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
public class Path extends GsonObject {

	@SerializedName("StartingWaypointId")
	Long startingWaypointId;

	@SerializedName("EndingWaypointId")
	Long endingWaypointId;

	@SerializedName("DestinationId")
	Long destinationId;

	@SerializedName("Instructions")
	String instructions;

	@SerializedName("Comment")
	String comment;

	@SerializedName("NavigationImage")
	String navigationImageUrl;

	@SerializedName("Url")
	String urlEndpoint;

	@SerializedName("Id")
	Long id;

	/**
	 * @return the startingWaypointId
	 */
	public Long getStartingWaypointId() {
		return startingWaypointId;
	}

	/**
	 * @return the endingWaypointId
	 */
	public Long getEndingWaypointId() {
		return endingWaypointId;
	}

	/**
	 * @return the destinationId
	 */
	public Long getDestinationId() {
		return destinationId;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return the navigationImageUrl
	 */
	public String getNavigationImageUrl() {
		return navigationImageUrl;
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

}
