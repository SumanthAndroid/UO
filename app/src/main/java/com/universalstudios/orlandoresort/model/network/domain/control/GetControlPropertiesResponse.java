package com.universalstudios.orlandoresort.model.network.domain.control;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.domain.global.LatLon;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class GetControlPropertiesResponse extends NetworkResponse {

	// Shared config values
	@SerializedName("Token")
	String token;

	@SerializedName("TokenExpirationString")
	String tokenExpirationString;

	@SerializedName("TokenExpirationUnix")
	Long tokenExpirationUnix;

	@SerializedName("ServicesBaseUrl")
	String servicesBaseUrl;

	// Orlando config values
	@SerializedName("OrlandoActiveMapTileSetId")
	Long orlandoActiveMapTileSetId;

	@SerializedName("OrlandoGeoFence")
	List<LatLon> orlandoGeoFence;

	@SerializedName("OrlandoExpressPassUrl")
	String orlandoExpressPassUrl;

	@SerializedName("OrlandoActiveMapTileSetBaseColor")
	String orlandoActiveMapTileSetBaseColor;

	@Deprecated
	@SerializedName("OrlandoFeaturedPointsOfInterest")
	List<Long> orlandoFeaturedPointsOfInterest;

	@SerializedName("WaitTimeUpdateIntervalInSec")
	Long waitTimeUpdateIntervalInSec;

	@SerializedName("ExPassNotificationInvervalInMin")
	Long exPassNotificationIntervalInMin;

	@SerializedName("OrlandoFeaturedList")
	List<Long> orlandoFeaturedList;

	@SerializedName("GuestServicesPhoneNumber")
	String guestServicesPhoneNumber;

	@SerializedName("GuestServicesEmailAddress")
	String guestServicesEmailAddress;

	@SerializedName("InParkWifiNetwork")
	String inParkWifiNetworkName;

	@SerializedName("RiderGuidePdfUrl")
	String riderGuidePdfUrl;

	@SerializedName("TicketWebstoreUrl")
	String ticketWebstoreUrl;

	@SerializedName("GuestServicesDescription")
	String guestServicesDescription;

	@SerializedName("GuestServicesDisplayEmail")
	String guestServicesDisplayEmail;

	@SerializedName("GuestServicesTwitterUrl")
	String guestServicesTwitterUrl;

	@SerializedName("GuestServicesDefaultShareText")
	String guestServicesDefaultShareText;

	@SerializedName("DefaultEventsToEventSeries")
	boolean defaultEventsToEventSeries;

	// Hollywood config values
	@SerializedName("USH_ActiveMapTileSetId")
	Long ushActiveMapTileSetId;

	@SerializedName("USH_ActiveMapTileSetBaseColor")
	String ushActiveMapTileSetBaseColor;

	@SerializedName("USH_GeoFence")
	List<LatLon> ushGeoFence;

	@SerializedName("USH_WaitTimeUpdateIntervalInSec")
	Long ushWaitTimeUpdateIntervalInSec;

	@SerializedName("USH_ExpressPassUrl")
	String ushExpressPassUrl;

	@SerializedName("USH_FeaturedList")
	List<Long> ushFeaturedList;

	@SerializedName("USH_FeaturedOffers")
	List<Long> ushFeaturedOffersList;

	@SerializedName("USH_GuestServicesPhoneNumber")
	String ushGuestServicesPhoneNumber;

	@SerializedName("USH_GuestServicesEmailAddress")
	String ushGuestServicesEmailAddress;

	@SerializedName("USH_InParkWifiNetwork")
	String ushInParkWifiNetworkName;

	@SerializedName("USH_RiderGuidePdfUrl")
	String ushRiderGuidePdfUrl;

	@SerializedName("USH_TicketWebstoreUrl")
	String ushTicketWebstoreUrl;

	@SerializedName("USH_GuestServicesDescription")
	String ushGuestServicesDescription;

	@SerializedName("USH_GuestServicesDisplayEmail")
	String ushGuestServicesDisplayEmail;

	@SerializedName("USH_GuestServicesTwitterUrl")
	String ushGuestServicesTwitterUrl;

	@SerializedName("USH_GuestServicesDefaultShareText")
	String ushGuestServicesDefaultShareText;

	@SerializedName("USH_FaqPageUrl")
	String ushFaqPageUrl;


	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the tokenExpirationString
	 */
	public String getTokenExpirationString() {
		return tokenExpirationString;
	}

	/**
	 * @return the tokenExpirationUnix
	 */
	public Long getTokenExpirationUnix() {
		return tokenExpirationUnix;
	}

	/**
	 * @return the orlandoGeoFence
	 */
	public List<LatLon> getOrlandoGeoFence() {
		return orlandoGeoFence;
	}

	/**
	 * @return the servicesBaseUrl
	 */
	public String getServicesBaseUrl() {
		return servicesBaseUrl;
	}

	/**
	 * @return the orlandoActiveMapTileSetId
	 */
	public Long getOrlandoActiveMapTileSetId() {
		return orlandoActiveMapTileSetId;
	}

	/**
	 * @return the waitTimeUpdateIntervalInSec
	 */
	public Long getWaitTimeUpdateIntervalInSec() {
		return waitTimeUpdateIntervalInSec;
	}

	/**
	 * @return the orlandoExpressPassUrl
	 */
	public String getOrlandoExpressPassUrl() {
		return orlandoExpressPassUrl;
	}

	/**
	 * @return the orlandoActiveMapTileSetBaseColor
	 */
	public String getOrlandoActiveMapTileSetBaseColor() {
		return orlandoActiveMapTileSetBaseColor;
	}

	/**
	 * @return the orlandoFeaturedPointsOfInterest
	 */
	@Deprecated
	public List<Long> getOrlandoFeaturedPointsOfInterest() {
		return orlandoFeaturedPointsOfInterest;
	}

	/**
	 * @return the exPassNotificationIntervalInMin
	 */
	public Long getExPassNotificationIntervalInMin() {
		return exPassNotificationIntervalInMin;
	}

	/**
	 * @return the orlandoFeaturedList
	 */
	public List<Long> getOrlandoFeaturedList() {
		return orlandoFeaturedList;
	}

	/**
	 * @return the guestServicesPhoneNumber
	 */
	public String getGuestServicesPhoneNumber() {
		return guestServicesPhoneNumber;
	}

	/**
	 * @return the guestServicesEmailAddress
	 */
	public String getGuestServicesEmailAddress() {
		return guestServicesEmailAddress;
	}

	/**
	 * @return the inParkWifiNetworkName
	 */
	public String getInParkWifiNetworkName() {
		return inParkWifiNetworkName;
	}

	/**
	 * @return the riderGuidePdfUrl
	 */
	public String getRiderGuidePdfUrl() { return riderGuidePdfUrl; }

	/**
	 * @return the ticketWebstoreUrl
	 */
	public String getTicketWebstoreUrl() { return ticketWebstoreUrl; }

	/**
	 * @return the default text for sharing
     */
	public String getGuestServicesDefaultShareText() {
		return guestServicesDefaultShareText;
	}

	public String getGuestServicesDescription() {
		return guestServicesDescription;
	}

	/**
	 * @return The email to display for guest services
     */
	public String getGuestServicesDisplayEmail() {
		return guestServicesDisplayEmail;
	}

	/**
	 * @return The guest services Twitter Url
     */
	public String getGuestServicesTwitterUrl() {
		return guestServicesTwitterUrl;
	}

	public String getUshActiveMapTileSetBaseColor() {
		return ushActiveMapTileSetBaseColor;
	}

	public Long getUshActiveMapTileSetId() {
		return ushActiveMapTileSetId;
	}

	public String getUshExpressPassUrl() {
		return ushExpressPassUrl;
	}

	public List<Long> getUshFeaturedList() {
		return ushFeaturedList;
	}

	public List<LatLon> getUshGeoFence() {
		return ushGeoFence;
	}

	public String getUshGuestServicesDefaultShareText() {
		return ushGuestServicesDefaultShareText;
	}

	public String getUshGuestServicesDescription() {
		return ushGuestServicesDescription;
	}

	public String getUshGuestServicesDisplayEmail() {
		return ushGuestServicesDisplayEmail;
	}

	public String getUshGuestServicesEmailAddress() {
		return ushGuestServicesEmailAddress;
	}

	public String getUshGuestServicesPhoneNumber() {
		return ushGuestServicesPhoneNumber;
	}

	public String getUshGuestServicesTwitterUrl() {
		return ushGuestServicesTwitterUrl;
	}

	public String getUshInParkWifiNetworkName() {
		return ushInParkWifiNetworkName;
	}

	public String getUshRiderGuidePdfUrl() {
		return ushRiderGuidePdfUrl;
	}

	public String getUshTicketWebstoreUrl() {
		return ushTicketWebstoreUrl;
	}

	public Long getUshWaitTimeUpdateIntervalInSec() {
		return ushWaitTimeUpdateIntervalInSec;
	}

	public String getUshFaqPageUrl() {
		return ushFaqPageUrl;
	}

	public List<Long> getUshFeaturedOffersList() {
		return ushFeaturedOffersList;
	}

	public boolean isDefaultEventsToEventSeries() {return defaultEventsToEventSeries;}
}