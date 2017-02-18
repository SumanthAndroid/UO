package com.universalstudios.orlandoresort.model.network.domain.wayfinding;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class GetWayfindingRouteResponse extends NetworkResponse {

	@SerializedName("Paths")
	List<Path> paths;

	@SerializedName("Waypoints")
	List<Waypoint> waypoints;

	/**
	 * @return the paths
	 */
	public List<Path> getPaths() {
		return paths;
	}

	/**
	 * @return the waypoints
	 */
	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

}