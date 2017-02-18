package com.universalstudios.orlandoresort.model.state.wayfinding;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Path;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Waypoint;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WayfindingState extends GsonObject {

	@SerializedName("paths")
	private List<Path> paths;

	@SerializedName("waypoints")
	private List<Waypoint> waypoints;

	@SerializedName("onPathWaypoints")
	private List<Waypoint> onPathWaypoints;

	@SerializedName("offPathWaypoints")
	private List<Waypoint> offPathWaypoints;

	@SerializedName("currentStepIndex")
	private int currentStepIndex;

	@SerializedName("isServiceRunning")
	private Boolean isServiceRunning;

	@SerializedName("hasRequestedRoute")
	private boolean hasRequestedRoute;

	@SerializedName("didRouteRequestSucceed")
	private Boolean didRouteRequestSucceed;

	@SerializedName("routeRequestTag")
	private String routeRequestTag;

	@SerializedName("offPathUpdatesInARow")
	private int offPathUpdatesInARow;

	@SerializedName("hasFinishedRoute")
	private boolean hasFinishedRoute;

	@SerializedName("isGpsEnabled")
	private Boolean isGpsEnabled;

	@SerializedName("isLocationServicesConnected")
	private Boolean isLocationServicesConnected;

	@SerializedName("isWayfindingScreenShowing")
	private boolean isWayfindingScreenShowing;

	/**
	 * @return the offPathUpdatesInARow
	 */
	public synchronized int getOffPathUpdatesInARow() {
		return offPathUpdatesInARow;
	}

	/**
	 * @param offPathUpdatesInARow
	 *            the offPathUpdatesInARow to set
	 */
	public synchronized void setOffPathUpdatesInARow(int offPathUpdatesInARow) {
		this.offPathUpdatesInARow = offPathUpdatesInARow;
	}

	/**
	 * @return the routeRequestTag
	 */
	public synchronized String getRouteRequestTag() {
		return routeRequestTag;
	}

	/**
	 * @param routeRequestTag
	 *            the routeRequestTag to set
	 */
	public synchronized void setRouteRequestTag(String routeRequestTag) {
		this.routeRequestTag = routeRequestTag;
	}

	/**
	 * @return the paths
	 */
	public synchronized List<Path> getPaths() {
		return paths;
	}

	/**
	 * @param paths
	 *            the paths to set
	 */
	public synchronized void setPaths(List<Path> paths) {
		this.paths = paths;
	}

	/**
	 * @return the waypoints
	 */
	public synchronized List<Waypoint> getWaypoints() {
		return waypoints;
	}

	/**
	 * @param waypoints
	 *            the waypoints to set
	 */
	public synchronized void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	/**
	 * @return the onPathWaypoints
	 */
	public synchronized List<Waypoint> getOnPathWaypoints() {
		return onPathWaypoints;
	}

	/**
	 * @param onPathWaypoints
	 *            the onPathWaypoints to set
	 */
	public synchronized void setOnPathWaypoints(List<Waypoint> onPathWaypoints) {
		this.onPathWaypoints = onPathWaypoints;
	}

	/**
	 * @return the offPathWaypoints
	 */
	public synchronized List<Waypoint> getOffPathWaypoints() {
		return offPathWaypoints;
	}

	/**
	 * @param offPathWaypoints
	 *            the offPathWaypoints to set
	 */
	public synchronized void setOffPathWaypoints(List<Waypoint> offPathWaypoints) {
		this.offPathWaypoints = offPathWaypoints;
	}

	/**
	 * @return the currentStepIndex
	 */
	public synchronized int getCurrentStepIndex() {
		return currentStepIndex;
	}

	/**
	 * @param currentStepIndex
	 *            the currentStepIndex to set
	 */
	public synchronized void setCurrentStepIndex(int currentStepIndex) {
		this.currentStepIndex = currentStepIndex;
	}

	/**
	 * @return the isServiceRunning
	 */
	public synchronized Boolean getIsServiceRunning() {
		return isServiceRunning;
	}

	/**
	 * @param isServiceRunning
	 *            the isServiceRunning to set
	 */
	public synchronized void setIsServiceRunning(Boolean isServiceRunning) {
		this.isServiceRunning = isServiceRunning;
	}

	/**
	 * @return the hasRequestedRoute
	 */
	public synchronized boolean hasRequestedRoute() {
		return hasRequestedRoute;
	}

	/**
	 * @param hasRequestedRoute
	 *            the hasRequestedRoute to set
	 */
	public synchronized void setHasRequestedRoute(boolean hasRequestedRoute) {
		this.hasRequestedRoute = hasRequestedRoute;
	}

	/**
	 * @return the didRouteRequestSucceed
	 */
	public synchronized Boolean getDidRouteRequestSucceed() {
		return didRouteRequestSucceed;
	}

	/**
	 * @param didRouteRequestSucceed
	 *            the didRouteRequestSucceed to set
	 */
	public synchronized void setDidRouteRequestSucceed(Boolean didRouteRequestSucceed) {
		this.didRouteRequestSucceed = didRouteRequestSucceed;
	}

	/**
	 * @return the hasFinishedRoute
	 */
	public synchronized boolean hasFinishedRoute() {
		return hasFinishedRoute;
	}

	/**
	 * @param hasFinishedRoute
	 *            the hasFinishedRoute to set
	 */
	public synchronized void setHasFinishedRoute(boolean hasFinishedRoute) {
		this.hasFinishedRoute = hasFinishedRoute;
	}

	/**
	 * @return the isGpsEnabled
	 */
	public synchronized Boolean getIsGpsEnabled() {
		return isGpsEnabled;
	}

	/**
	 * @param isGpsEnabled
	 *            the isGpsEnabled to set
	 */
	public synchronized void setIsGpsEnabled(Boolean isGpsEnabled) {
		this.isGpsEnabled = isGpsEnabled;
	}

	/**
	 * @return the isLocationServicesConnected
	 */
	public synchronized Boolean getIsLocationServicesConnected() {
		return isLocationServicesConnected;
	}

	/**
	 * @param isLocationServicesConnected
	 *            the isLocationServicesConnected to set
	 */
	public synchronized void setIsLocationServicesConnected(Boolean isLocationServicesConnected) {
		this.isLocationServicesConnected = isLocationServicesConnected;
	}

	/**
	 * @return the isWayfindingScreenShowing
	 */
	public synchronized boolean isWayfindingScreenShowing() {
		return isWayfindingScreenShowing;
	}

	/**
	 * @param isWayfindingScreenShowing the isWayfindingScreenShowing to set
	 */
	public synchronized void setWayfindingScreenShowing(boolean isWayfindingScreenShowing) {
		this.isWayfindingScreenShowing = isWayfindingScreenShowing;
	}



}