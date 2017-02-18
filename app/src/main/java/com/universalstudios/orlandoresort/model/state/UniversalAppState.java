package com.universalstudios.orlandoresort.model.state;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.model.network.domain.global.LatLon;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.List;

/**
 * Note: Because this object is persisted, fields should not be changed, but can
 * be added.
 *
 * @author Steven Byle
 */
public class UniversalAppState extends GsonObject {

	// ************ PARSED FROM SERVICES STATE ************

	@SerializedName("token")
	private String token;

	@SerializedName("tokenExpirationString")
	private String tokenExpirationString;

	@SerializedName("tokenExpirationUnix")
	private Long tokenExpirationUnix;

	@SerializedName("orlandoActiveMapTileSetId")
	private Long activeMapTileSetId;

	@SerializedName("orlandoGeoFence")
	private List<LatLon> parkGeoFence;

	@SerializedName("servicesBaseUrl")
	private String servicesBaseUrl;

	@SerializedName("waitTimeUpdateIntervalInSec")
	private Long waitTimeUpdateIntervalInSec;

	@SerializedName("orlandoExpressPassUrl")
	private String expressPassUrl;

	@SerializedName("orlandoActiveMapTileSetBaseColor")
	private String activeMapTileSetBaseColor;

	@Deprecated
	@SerializedName("orlandoFeaturedPointsOfInterest")
	private List<Long> orlandoFeaturedPointsOfInterest;

	@SerializedName("orlandoFeaturedItems")
	private List<Long> featuredItems;

	@SerializedName("expressPassNotificationIntervalInMin")
	private Long expressPassNotificationIntervalInMin;

	@SerializedName("guestServicesPhoneNumber")
	private String guestServicesPhoneNumber;

	@SerializedName("guestServicesEmailAddress")
	private String guestServicesEmailAddress;

	@SerializedName("inParkWifiNetworkName")
	private String inParkWifiNetworkName;

	@SerializedName("riderGuidePdfUrl")
	private String riderGuidePdfUrl;

	@SerializedName("TicketWebstoreUrl")
	private String ticketWebstoreUrl;

	@SerializedName("GuestServicesDescription")
	private String guestServiceDescription;

	@SerializedName("GuestServicesDisplayEmail")
	private String guestServicesDisplayEmail;

	@SerializedName("GuestServicesTwitterUrl")
	private String guestServicesTwitterUrl;

	@SerializedName("GuestServicesDefaultShareText")
	private String guestServicesDefaultShareText;

	@SerializedName("faqPageUrl")
	private String faqPageUrl;

	@SerializedName("featuredOfferItems")
	private List<Long> featuredOfferItems;


	// ************ NOT PARSED, ADDED STATE ************

	@SerializedName("dateOfLastControlPropSyncInMillis")
	private Long dateOfLastControlPropSyncInMillis;

	@SerializedName("dateOfLastPoiSyncInMillis")
	private Long dateOfLastPoiSyncInMillis;

	@SerializedName("dateOfLastVenueSyncInMillis")
	private Long dateOfLastVenueSyncInMillis;

	@SerializedName("dateOfLastGeofenceCheckInMillis")
	private Long dateOfLastGeofenceCheckInMillis;

	@SerializedName("delayIntervalBeforeNextGeofenceCheckInMillis")
	private Long delayIntervalBeforeNextGeofenceCheckInMillis;

	@SerializedName("isLocationUnkown")
	private boolean isLocationUnkown;

	@SerializedName("isInUniversalOrlandoResortGeofence")
	private boolean isInUniversalOrlandoResortGeofence;

	@SerializedName("isInIslandsOfAdventureGeofence")
	private boolean isInIslandsOfAdventureGeofence;

	@SerializedName("isInUniversalStudiosFloridaGeofence")
	private boolean isInUniversalStudiosFloridaGeofence;

	@SerializedName("isInCityWalkGeofence")
	private boolean isInCityWalkOrlandoGeofence;

	@SerializedName("inInWetNWildGeofence")
	private boolean isInWetNWildGeofence;

	@SerializedName("hasViewedTutorial")
	private boolean hasViewedTutorial;

	@SerializedName("hasViewedWelcome")
	private boolean hasViewedWelcome;

	@SerializedName("remindExpressPassLater")
	private boolean remindExpressPassLater;

	@SerializedName("remindExpressPassLaterDateInMillis")
	private Long remindExpressPassLaterDateInMillis;

	@SerializedName("dateOfLastVenueHoursSyncInMillis")
	private Long dateOfLastVenueHoursSyncInMillis;

	@SerializedName("dateOfLastParkNewsSyncInMillis")
	private Long dateOfLastParkNewsSyncInMillis;

	@SerializedName("gcmRegistrationId")
	private String gcmRegistrationId;

	@SerializedName("gcmRegistrationIdVersionName")
	private String gcmRegistrationIdVersionName;

	@SerializedName("dateOfLastPushRegSyncInMillis")
	private Long dateOfLastPushRegSyncInMillis;

	@SerializedName("dateOfLastAlertSyncInMillis")
	private Long dateOfLastAlertSyncInMillis;

	@SerializedName("dateOfLastEventSeriesSyncInMillis")
	private Long dateOfLastEventSeriesSyncInMillis;

	@SerializedName("dateOfLastOffersSyncInMillis")
	private Long dateOfLastOffersSyncInMillis;

	@SerializedName("isInUniversalHollywoodResortGeofence")
	private boolean isInUniversalHollywoodResortGeofence;

	@SerializedName("isInUniversalStudiosHollywoodGeofence")
	private boolean isInUniversalStudiosHollywoodGeofence;

	@SerializedName("isInCityWalkHollywoodGeofence")
	private boolean isInCityWalkHollywoodGeofence;

	@SerializedName("DefaultEventsToEventSeries")
	private boolean defaultEventsToEventSeries;

	private static Long dateOfLastQueuesSyncInMillis;

	private static Long dateOfLastAppointmentSyncInMillis;



	/**
	 * @return the token
	 */
	public synchronized String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public synchronized void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the tokenExpirationString
	 */
	public synchronized String getTokenExpirationString() {
		return tokenExpirationString;
	}

	/**
	 * @param tokenExpirationString
	 *            the tokenExpirationString to set
	 */
	public synchronized void setTokenExpirationString(String tokenExpirationString) {
		this.tokenExpirationString = tokenExpirationString;
	}

	/**
	 * @return the tokenExpirationUnix
	 */
	public synchronized Long getTokenExpirationUnix() {
		return tokenExpirationUnix;
	}

	/**
	 * @param tokenExpirationUnix
	 *            the tokenExpirationUnix to set
	 */
	public synchronized void setTokenExpirationUnix(Long tokenExpirationUnix) {
		this.tokenExpirationUnix = tokenExpirationUnix;
	}

	/**
	 * @return the activeMapTileSetId
	 */
	public synchronized Long getActiveMapTileSetId() {
		return activeMapTileSetId;
	}

	/**
	 * @param activeMapTileSetId
	 *            the activeMapTileSetId to set
	 */
	public synchronized void setActiveMapTileSetId(Long activeMapTileSetId) {
		this.activeMapTileSetId = activeMapTileSetId;
	}

	/**
	 * @return the parkGeoFence
	 */
	public synchronized List<LatLon> getParkGeoFence() {
		return parkGeoFence;
	}

	/**
	 * @param parkGeoFence
	 *            the parkGeoFence to set
	 */
	public synchronized void setParkGeoFence(List<LatLon> parkGeoFence) {
		this.parkGeoFence = parkGeoFence;
	}

	/**
	 * @return the servicesBaseUrl
	 */
	public synchronized String getServicesBaseUrl() {
		return servicesBaseUrl;
	}

	/**
	 * @param servicesBaseUrl
	 *            the servicesBaseUrl to set
	 */
	public synchronized void setServicesBaseUrl(String servicesBaseUrl) {
		this.servicesBaseUrl = servicesBaseUrl;
	}

	/**
	 * @return the waitTimeUpdateIntervalInSec
	 */
	public synchronized Long getWaitTimeUpdateIntervalInSec() {
		return waitTimeUpdateIntervalInSec;
	}

	/**
	 * @param waitTimeUpdateIntervalInSec
	 *            the waitTimeUpdateIntervalInSec to set
	 */
	public synchronized void setWaitTimeUpdateIntervalInSec(Long waitTimeUpdateIntervalInSec) {
		this.waitTimeUpdateIntervalInSec = waitTimeUpdateIntervalInSec;
	}

	/**
	 * @return the expressPassUrl
	 */
	public synchronized String getExpressPassUrl() {
		return expressPassUrl;
	}

	/**
	 * @param expressPassUrl
	 *            the expressPassUrl to set
	 */
	public synchronized void setExpressPassUrl(String expressPassUrl) {
		this.expressPassUrl = expressPassUrl;
	}

	/**
	 * @return the activeMapTileSetBaseColor
	 */
	public synchronized String getActiveMapTileSetBaseColor() {
		return activeMapTileSetBaseColor;
	}

	/**
	 * @param activeMapTileSetBaseColor
	 *            the activeMapTileSetBaseColor to set
	 */
	public synchronized void setActiveMapTileSetBaseColor(String activeMapTileSetBaseColor) {
		this.activeMapTileSetBaseColor = activeMapTileSetBaseColor;
	}

	/**
	 * @return the orlandoFeaturedPointsOfInterest
	 */
	@Deprecated
	public synchronized List<Long> getOrlandoFeaturedPointsOfInterest() {
		return orlandoFeaturedPointsOfInterest;
	}

	/**
	 * @param orlandoFeaturedPointsOfInterest
	 *            the orlandoFeaturedPointsOfInterest to set
	 */
	@Deprecated
	public synchronized void setOrlandoFeaturedPointsOfInterest(List<Long> orlandoFeaturedPointsOfInterest) {
		this.orlandoFeaturedPointsOfInterest = orlandoFeaturedPointsOfInterest;
	}

	/**
	 * @return the dateOfLastPoiSyncInMillis
	 */
	public synchronized Long getDateOfLastPoiSyncInMillis() {
		return dateOfLastPoiSyncInMillis;
	}

	/**
	 * @param dateOfLastPoiSyncInMillis
	 *            the dateOfLastPoiSyncInMillis to set
	 */
	public synchronized void setDateOfLastPoiSyncInMillis(Long dateOfLastPoiSyncInMillis) {
		this.dateOfLastPoiSyncInMillis = dateOfLastPoiSyncInMillis;
	}

	/**
	 * @return the dateOfLastVenueSyncInMillis
	 */
	public synchronized Long getDateOfLastVenueSyncInMillis() {
		return dateOfLastVenueSyncInMillis;
	}

	/**
	 * @param dateOfLastVenueSyncInMillis
	 *            the dateOfLastVenueSyncInMillis to set
	 */
	public synchronized void setDateOfLastVenueSyncInMillis(Long dateOfLastVenueSyncInMillis) {
		this.dateOfLastVenueSyncInMillis = dateOfLastVenueSyncInMillis;
	}

	/**
	 * @return the dateOfLastControlPropSyncInMillis
	 */
	public synchronized Long getDateOfLastControlPropSyncInMillis() {
		return dateOfLastControlPropSyncInMillis;
	}

	/**
	 * @param dateOfLastControlPropSyncInMillis
	 *            the dateOfLastControlPropSyncInMillis to set
	 */
	public synchronized void setDateOfLastControlPropSyncInMillis(Long dateOfLastControlPropSyncInMillis) {
		this.dateOfLastControlPropSyncInMillis = dateOfLastControlPropSyncInMillis;
	}

	/**
	 *
	 * @return
	 */
	public synchronized boolean isLocationUnkown() {
		return isLocationUnkown;
	}

	/**
	 *
	 * @param isLocationUnkown
	 */
	public synchronized void setIsLocationUnkown(boolean isLocationUnkown) {
		this.isLocationUnkown = isLocationUnkown;
	}

	/**
	 * @return the isInUniversalOrlandoResortGeofence
	 */
	public synchronized boolean isInUniversalOrlandoResortGeofence() {
		return isInUniversalOrlandoResortGeofence;
	}

	/**
	 * @param isInUniversalOrlandoResortGeofence
	 *            the isInUniversalOrlandoResortGeofence to set
	 */
	public synchronized void setInUniversalOrlandoResortGeofence(boolean isInUniversalOrlandoResortGeofence) {
		this.isInUniversalOrlandoResortGeofence = isInUniversalOrlandoResortGeofence;
	}

	/**
	 * @return the isInIslandsOfAdventureGeofence
	 */
	public synchronized boolean isInIslandsOfAdventureGeofence() {
		return isInIslandsOfAdventureGeofence;
	}

	/**
	 * @param isInIslandsOfAdventureGeofence
	 *            the isInIslandsOfAdventureGeofence to set
	 */
	public synchronized void setInIslandsOfAdventureGeofence(boolean isInIslandsOfAdventureGeofence) {
		this.isInIslandsOfAdventureGeofence = isInIslandsOfAdventureGeofence;
	}

	/**
	 * @return the isInUniversalStudiosFloridaGeofence
	 */
	public synchronized boolean isInUniversalStudiosFloridaGeofence() {
		return isInUniversalStudiosFloridaGeofence;
	}

	/**
	 * @param isInUniversalStudiosFloridaGeofence
	 *            the isInUniversalStudiosFloridaGeofence to set
	 */
	public synchronized void setInUniversalStudiosFloridaGeofence(boolean isInUniversalStudiosFloridaGeofence) {
		this.isInUniversalStudiosFloridaGeofence = isInUniversalStudiosFloridaGeofence;
	}

	/**
	 * @return the isInCityWalkOrlandoGeofence
	 */
	public synchronized boolean isInCityWalkOrlandoGeofence() {
		return isInCityWalkOrlandoGeofence;
	}

	/**
	 * @param isInCityWalkGeofence
	 *            the isInCityWalkOrlandoGeofence to set
	 */
	public synchronized void setInCityWalkOrlandoGeofence(boolean isInCityWalkGeofence) {
		this.isInCityWalkOrlandoGeofence = isInCityWalkGeofence;
	}

	/**
	 * @return the dateOfLastGeofenceCheckInMillis
	 */
	public synchronized Long getDateOfLastGeofenceCheckInMillis() {
		return dateOfLastGeofenceCheckInMillis;
	}

	/**
	 * @param dateOfLastGeofenceCheckInMillis
	 *            the dateOfLastGeofenceCheckInMillis to set
	 */
	public synchronized void setDateOfLastGeofenceCheckInMillis(Long dateOfLastGeofenceCheckInMillis) {
		this.dateOfLastGeofenceCheckInMillis = dateOfLastGeofenceCheckInMillis;
	}

	/**
	 * @return the delayIntervalBeforeNextGeofenceCheckInMillis
	 */
	public synchronized Long getDelayIntervalBeforeNextGeofenceCheckInMillis() {
		return delayIntervalBeforeNextGeofenceCheckInMillis;
	}

	/**
	 * @param delayIntervalBeforeNextGeofenceCheckInMillis
	 *            the delayIntervalBeforeNextGeofenceCheckInMillis to set
	 */
	public synchronized void setDelayIntervalBeforeNextGeofenceCheckInMillis(Long delayIntervalBeforeNextGeofenceCheckInMillis) {
		this.delayIntervalBeforeNextGeofenceCheckInMillis = delayIntervalBeforeNextGeofenceCheckInMillis;
	}

	/**
	 * @return the hasViewedTutorial
	 */
	public synchronized boolean hasViewedTutorial() {
		return hasViewedTutorial;
	}

	/**
	 * @param hasViewedTutorial
	 *            the hasViewedTutorial to set
	 */
	public synchronized void setHasViewedTutorial(boolean hasViewedTutorial) {
		this.hasViewedTutorial = hasViewedTutorial;
	}

	/**
	 * @return the hasViewedWelcome
	 */
	public synchronized boolean hasViewedWelcome() {
		return hasViewedWelcome;
	}

	/**
	 * @param hasViewedWelcome
	 *            the hasViewedWelcome to set
	 */
	public synchronized void setHasViewedWelcome(boolean hasViewedWelcome) {
		this.hasViewedWelcome = hasViewedWelcome;
	}

	/**
	 * @return the expressPassNotificationIntervalInMin
	 */
	public synchronized Long getExpressPassNotificationIntervalInMin() {
		return expressPassNotificationIntervalInMin;
	}

	/**
	 * @param expressPassNotificationIntervalInMin
	 *            the expressPassNotificationIntervalInMin to set
	 */
	public synchronized void setExpressPassNotificationIntervalInMin(Long expressPassNotificationIntervalInMin) {
		this.expressPassNotificationIntervalInMin = expressPassNotificationIntervalInMin;
	}

	/**
	 * @return the remindExpressPassLater
	 */
	public synchronized boolean isRemindExpressPassLater() {
		return remindExpressPassLater;
	}

	/**
	 * @param remindExpressPassLater
	 *            the remindExpressPassLater to set
	 */
	public synchronized void setRemindExpressPassLater(boolean remindExpressPassLater) {
		this.remindExpressPassLater = remindExpressPassLater;
	}

	/**
	 * @return the remindExpressPassLaterDateInMillis
	 */
	public synchronized Long getRemindExpressPassLaterDateInMillis() {
		return remindExpressPassLaterDateInMillis;
	}

	/**
	 * @param remindExpressPassLaterDateInMillis
	 *            the remindExpressPassLaterDateInMillis to set
	 */
	public synchronized void setRemindExpressPassLaterDateInMillis(Long remindExpressPassLaterDateInMillis) {
		this.remindExpressPassLaterDateInMillis = remindExpressPassLaterDateInMillis;
	}

	/**
	 * @return the featuredItems
	 */
	public synchronized List<Long> getFeaturedItems() {
		return featuredItems;
	}

	/**
	 * @param featuredItems
	 *            the featuredItems to set
	 */
	public synchronized void setFeaturedItems(List<Long> featuredItems) {
		this.featuredItems = featuredItems;
	}

	/**
	 * @return the guestServicesPhoneNumber
	 */
	public synchronized String getGuestServicesPhoneNumber() {
		return guestServicesPhoneNumber;
	}

	/**
	 * @param guestServicesPhoneNumber
	 *            the guestServicesPhoneNumber to set
	 */
	public synchronized void setGuestServicesPhoneNumber(String guestServicesPhoneNumber) {
		this.guestServicesPhoneNumber = guestServicesPhoneNumber;
	}

	/**
	 * @return the guestServicesEmailAddress
	 */
	public synchronized String getGuestServicesEmailAddress() {
		return guestServicesEmailAddress;
	}

	/**
	 * @param guestServicesEmailAddress
	 *            the guestServicesEmailAddress to set
	 */
	public synchronized void setGuestServicesEmailAddress(String guestServicesEmailAddress) {
		this.guestServicesEmailAddress = guestServicesEmailAddress;
	}

	public String getGuestServiceDescription() {
		return guestServiceDescription;
	}

	public void setGuestServiceDescription(String guestServiceDescription) {
		this.guestServiceDescription = guestServiceDescription;
	}

	public String getGuestServicesDisplayEmail() {
		return guestServicesDisplayEmail;
	}

	public void setGuestServicesDisplayEmail(String guestServicesDisplayEmail) {
		this.guestServicesDisplayEmail = guestServicesDisplayEmail;
	}

	public String getGuestServicesTwitterUrl() {
		return guestServicesTwitterUrl;
	}

	public void setGuestServicesTwitterUrl(String guestServicesTwitterUrl) {
		this.guestServicesTwitterUrl = guestServicesTwitterUrl;
	}

	public String getGuestServicesDefaultShareText() {
		return guestServicesDefaultShareText;
	}

	public void setGuestServicesDefaultShareText(String guestServicesDefaultShareText) {
		this.guestServicesDefaultShareText = guestServicesDefaultShareText;
	}

	public String getFaqPageUrl() {
		return faqPageUrl;
	}

	public void setFaqPageUrl(String faqPageUrl) {
		this.faqPageUrl = faqPageUrl;
	}

	/**
	 * @return the inParkWifiNetworkName
	 */
	public synchronized String getInParkWifiNetworkName() {
		return inParkWifiNetworkName;
	}

	/**
	 * @param inParkWifiNetworkName
	 *            the inParkWifiNetworkName to set
	 */
	public synchronized void setInParkWifiNetworkName(String inParkWifiNetworkName) {
		this.inParkWifiNetworkName = inParkWifiNetworkName;
	}

	/**
	 * @return the RiderGuidePdfUrl
	 */
	public synchronized String getRiderGuidePdfUrl() { return riderGuidePdfUrl; }

	/**
	 * @param riderGuidePdfUrl the RiderGuidePdfUrl to set
	 */
	public synchronized void setRiderGuidePdfUrl(String riderGuidePdfUrl) {
		this.riderGuidePdfUrl = riderGuidePdfUrl;
	}

	/**
	 * @return the TicketWebStoreUrl
	 */
	public synchronized String getTicketWebStoreUrl() { return ticketWebstoreUrl; }

	/**
	 * @param ticketWebStoreUrl the TicketWebStoreUrl to set
	 */
	public synchronized void setTicketWebStoreUrl(String ticketWebStoreUrl) {
		this.ticketWebstoreUrl = ticketWebStoreUrl;
	}

	/**
	 * @return the dateOfLastVenueHoursSyncInMillis
	 */
	public synchronized Long getDateOfLastVenueHoursSyncInMillis() {
		return dateOfLastVenueHoursSyncInMillis;
	}

	/**
	 * @param dateOfLastVenueHoursSyncInMillis
	 *            the dateOfLastVenueHoursSyncInMillis to set
	 */
	public synchronized void setDateOfLastVenueHoursSyncInMillis(Long dateOfLastVenueHoursSyncInMillis) {
		this.dateOfLastVenueHoursSyncInMillis = dateOfLastVenueHoursSyncInMillis;
	}

	/**
	 * @return the dateOfLastParkNewsSyncInMillis
	 */
	public synchronized Long getDateOfLastParkNewsSyncInMillis() {
		return dateOfLastParkNewsSyncInMillis;
	}

	/**
	 * @param dateOfLastParkNewsSyncInMillis
	 *            the dateOfLastParkNewsSyncInMillis to set
	 */
	public synchronized void setDateOfLastParkNewsSyncInMillis(Long dateOfLastParkNewsSyncInMillis) {
		this.dateOfLastParkNewsSyncInMillis = dateOfLastParkNewsSyncInMillis;
	}

	/**
	 * @return the gcmRegistrationId
	 */
	public synchronized String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	/**
	 * @param gcmRegistrationId
	 *            the gcmRegistrationId to set
	 */
	public synchronized void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	/**
	 * @return the gcmRegistrationIdVersionName
	 */
	public synchronized String getGcmRegistrationIdVersionName() {
		return gcmRegistrationIdVersionName;
	}

	/**
	 * @param gcmRegistrationIdVersionName
	 *            the gcmRegistrationIdVersionName to set
	 */
	public synchronized void setGcmRegistrationIdVersionName(String gcmRegistrationIdVersionName) {
		this.gcmRegistrationIdVersionName = gcmRegistrationIdVersionName;
	}

	/**
	 * @return the dateOfLastPushRegSyncInMillis
	 */
	public Long getDateOfLastPushRegSyncInMillis() {
		return dateOfLastPushRegSyncInMillis;
	}

	/**
	 * @param dateOfLastPushRegSyncInMillis
	 *            the dateOfLastPushRegSyncInMillis to set
	 */
	public void setDateOfLastPushRegSyncInMillis(Long dateOfLastPushRegSyncInMillis) {
		this.dateOfLastPushRegSyncInMillis = dateOfLastPushRegSyncInMillis;
	}

	/**
	 * @return the dateOfLastAlertSyncInMillis
	 */
	public Long getDateOfLastAlertSyncInMillis() {
		return dateOfLastAlertSyncInMillis;
	}

	/**
	 * @param dateOfLastAlertSyncInMillis
	 *            the dateOfLastAlertSyncInMillis to set
	 */
	public void setDateOfLastAlertSyncInMillis(Long dateOfLastAlertSyncInMillis) {
		this.dateOfLastAlertSyncInMillis = dateOfLastAlertSyncInMillis;
	}


	/**
	 * @return the isInWetNWildGeofence
	 */
	public synchronized boolean isInWetNWildGeofence() {
		return isInWetNWildGeofence;
	}

	/**
	 * @param isInWetNWildGeofence
	 *            the isInWetNWildGeofence to set
	 */
	public synchronized void setInWetNWildGeofence(boolean isInWetNWildGeofence) {
		this.isInWetNWildGeofence = isInWetNWildGeofence;
	}

	/**
	 * @return the dateOfLastEvenSeriesSyncInMillis
	 */
	public Long getDateOfLastEventSeriesSyncInMillis() {
		return dateOfLastEventSeriesSyncInMillis;
	}

	/**
	 * @param dateOfLastEvenSeriesSyncInMillis
	 *            the dateOfLastEvenSeriesSyncInMillis to set
	 */
	public void setDateOfLastEventSeriesSyncInMillis(Long dateOfLastEvenSeriesSyncInMillis) {
		this.dateOfLastEventSeriesSyncInMillis = dateOfLastEvenSeriesSyncInMillis;
	}

	/**
	 * @return the dateOfLastOffersSyncInMillis
	 */
	public Long getDateOfLastOffersSyncInMillis() {
		return dateOfLastOffersSyncInMillis;
	}

	/**
	 * @param dateOfLastOffersSyncInMillis
	 *            the dateOfLastOffersSyncInMillis to set
	 */
	public void setDateOfLastOffersSyncInMillis(Long dateOfLastOffersSyncInMillis) {
		this.dateOfLastOffersSyncInMillis = dateOfLastOffersSyncInMillis;
	}

	public synchronized boolean isInCityWalkHollywoodGeofence() {
		return isInCityWalkHollywoodGeofence;
	}

	public synchronized void setInCityWalkHollywoodGeofence(boolean inCityWalkHollywoodGeofence) {
		isInCityWalkHollywoodGeofence = inCityWalkHollywoodGeofence;
	}

	public synchronized boolean isInUniversalHollywoodResortGeofence() {
		return isInUniversalHollywoodResortGeofence;
	}

	public synchronized void setInUniversalHollywoodResortGeofence(boolean inUniversalHollywoodResortGeofence) {
		isInUniversalHollywoodResortGeofence = inUniversalHollywoodResortGeofence;
	}

	public synchronized boolean isInUniversalStudiosHollywoodGeofence() {
		return isInUniversalStudiosHollywoodGeofence;
	}

	public synchronized void setInUniversalStudiosHollywoodGeofence(boolean inUniversalStudiosHollywoodGeofence) {
		isInUniversalStudiosHollywoodGeofence = inUniversalStudiosHollywoodGeofence;
	}

	public synchronized List<Long> getFeaturedOfferItems() {
		return featuredOfferItems;
	}

	public synchronized void setFeaturedOfferItems(List<Long> featuredOfferItems) {
		this.featuredOfferItems = featuredOfferItems;
	}

	/**
	 * @return if the user is in the entire resort geofence for the specific location flavor of the app
	 */
	public synchronized boolean isInResortGeofence() {
		if(BuildConfigUtils.isLocationFlavorHollywood()) {
			return isInUniversalStudiosHollywoodGeofence();
		} else {
			return isInUniversalOrlandoResortGeofence();
		}
	}
	public boolean isDefaultEventsToEventSeries() {
		return defaultEventsToEventSeries;
	}

	public void setDefaultEventsToEventSeries(boolean defaultEventsToEventSeries) {
		this.defaultEventsToEventSeries = defaultEventsToEventSeries;
	}

	/**
	 * @return the dateOfLastQueuesSyncInMillis
	 */
	public Long getDateOfLastQueuesSyncInMillis() {return dateOfLastQueuesSyncInMillis;}

	/**
	 * @param dateOfLastQueuesSyncInMillis
	 *            the dateOfLastQueuesSyncInMillis to set
	 */
	public void setDateOfLastQueuesSyncInMillis(Long dateOfLastQueuesSyncInMillis) {
		this.dateOfLastQueuesSyncInMillis = dateOfLastQueuesSyncInMillis;
	}

	public Long getDateOfLastAppointmentSyncInMillis() {
		return dateOfLastAppointmentSyncInMillis;
	}

	public void setDateOfLastAppointmentSyncInMillis(Long dateOfLastAppointmentSyncInMillis) {
		UniversalAppState.dateOfLastAppointmentSyncInMillis = dateOfLastAppointmentSyncInMillis;
	}
}