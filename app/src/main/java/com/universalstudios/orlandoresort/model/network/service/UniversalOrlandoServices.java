package com.universalstudios.orlandoresort.model.network.service;

import com.universalstudios.orlandoresort.model.network.domain.alert.DeleteRideOpenAlertResponse;
import com.universalstudios.orlandoresort.model.network.domain.alert.DeleteWaitTimeAlertResponse;
import com.universalstudios.orlandoresort.model.network.domain.alert.GetRegisteredAlertsResponse;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsRequest.RegisterAlertsBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsResponse;
import com.universalstudios.orlandoresort.model.network.domain.alert.SetRideOpenAlertRequest.SetRideOpenAlertBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.alert.SetRideOpenAlertResponse;
import com.universalstudios.orlandoresort.model.network.domain.alert.SetWaitTimeAlertRequest.SetWaitTimeAlertBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.alert.SetWaitTimeAlertResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeRequest;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.DeleteCreatedAppointmentTicketResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.GetAppointmentTimesResponse;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.GetQueuesByPageResponse;
import com.universalstudios.orlandoresort.model.network.domain.events.GetEventSeriesResponse;
import com.universalstudios.orlandoresort.model.network.domain.feedback.SendGuestFeedbackRequest.SendGuestFeedbackBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.feedback.SendGuestFeedbackResponse;
import com.universalstudios.orlandoresort.model.network.domain.interactiveexperience.GetInteractiveExperienceResponse;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsResponse;
import com.universalstudios.orlandoresort.model.network.domain.newsletter.NewsletterRegistrationRequest;
import com.universalstudios.orlandoresort.model.network.domain.newsletter.NewsletterRegistrationResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferCouponResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferVendorsResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferSeries;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.GetPhotoFramesExperienceResponse;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisResponse;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.category.GetPoiCategoriesResponse;
import com.universalstudios.orlandoresort.model.network.domain.tridion.GetMobilePagesResponse;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenuesResponse;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.GetWayfindingRouteResponse;
import com.universalstudios.orlandoresort.model.network.request.RequestHeaders;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Network API endpoints.
 *
 * @author Steven Byle
 */
@SuppressWarnings("javadoc")
public interface UniversalOrlandoServices {

	/**
	 *  Get venues
	 */
	@GET("/api/venues")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetVenuesResponse getVenues(
			@Query("city") String city);

	@GET("/api/venues")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getVenues(
			@Query("city") String city,
			Callback<GetVenuesResponse> cb);

	/**
	 *  Get points of interest categories
	 */
	@GET("/api/pointsOfInterest/categories")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetPoiCategoriesResponse getPoiCategories(
			@Query("city") String city);

	@GET("/api/pointsOfInterest/categories")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getPoiCategories(
			@Query("city") String city,
			Callback<GetPoiCategoriesResponse> cb);

	/**
	 *  Get points of interest
	 */
	@GET("/api/pointsOfInterest")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetPoisResponse getPois(
			@Query("city") String city);

	@GET("/api/pointsOfInterest")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getPois(
			@Query("city") String city,
			Callback<GetPoisResponse> cb);

	/**
	 *  Get wayfinding route
	 */
	@GET("/api/routing/{startLatitude}_{startLongitude}/{poiId}")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetWayfindingRouteResponse getWayfindingRoute(
			@Path("startLatitude") double startLatitude,
			@Path("startLongitude") double startLongitude,
			@Path("poiId") long poiId,
			@Query("waypointOption") String waypointOption);

	@GET("/api/routing/{startLatitude}_{startLongitude}/{poiId}")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getWayfindingRoute(
			@Path("startLatitude") double startLatitude,
			@Path("startLongitude") double startLongitude,
			@Path("poiId") long poiId,
			@Query("waypointOption") String waypointOption,
			Callback<GetWayfindingRouteResponse> cb);

	/**
	 *  Get park news
	 */
	@GET("/api/news/")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetNewsResponse getNews(
			@Query("city") String city,
			@Query("pageSize") String pageSize,
			@Query("page") int page);

	@GET("/api/news/")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getNews(
			@Query("city") String city,
			@Query("pageSize") String pageSize,
			@Query("page") int page,
			Callback<GetNewsResponse> cb);

	/**
	 *  Get venue hours
	 */
	@GET("/api/venues/{venueId}/hours")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public List<VenueHours> getVenueHours(
			@Path("venueId") long venueId,
			@Query("endDate") String endDate);

	@GET("/api/venues/{venueId}/hours")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getVenueHours(
			@Path("venueId") long venueId,
			@Query("endDate") String endDate,
			Callback<List<VenueHours>> cb);

	/**
	 *  Register for push alert types
	 */
	@POST("/api/push")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public RegisterAlertsResponse registerAlerts(
			@Query("city") String city,
			@Body RegisterAlertsBodyParams bodyParams);

	@POST("/api/push")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public void registerAlerts(
			@Query("city") String city,
			@Body RegisterAlertsBodyParams bodyParams,
			Callback<RegisterAlertsResponse> cb);

	/**
	 *  Get registered push alert types
	 */
	@GET("/api/push")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetRegisteredAlertsResponse getRegisteredAlerts(
			@Query("city") String city,
			@Query("deviceId") String deviceId);

	@GET("/api/push")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getRegisteredAlerts(
			@Query("city") String city,
			@Query("deviceId") String deviceId,
			Callback<GetRegisteredAlertsResponse> cb);

	/**
	 *  Register or edit a wait time alert push
	 */
	@POST("/api/push/waitTimeAlert")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public SetWaitTimeAlertResponse setWaitTimeAlert(
			@Query("city") String city,
			@Body SetWaitTimeAlertBodyParams bodyParams);

	@POST("/api/push/waitTimeAlert")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public void setWaitTimeAlert(
			@Query("city") String city,
			@Body SetWaitTimeAlertBodyParams bodyParams,
			Callback<SetWaitTimeAlertResponse> cb);

	/**
	 *  Delete a wait time alert push
	 */
	@DELETE("/api/push/waitTimeAlert")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public DeleteWaitTimeAlertResponse deleteWaitTimeAlert(
			@Query("city") String city,
			@Query("deviceId") String deviceId,
			@Query("rideId") long rideId);

	@DELETE("/api/push/waitTimeAlert")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void deleteWaitTimeAlert(
			@Query("city") String city,
			@Query("deviceId") String deviceId,
			@Query("rideId") long rideId,
			Callback<DeleteWaitTimeAlertResponse> cb);

	/**
	 *  Register or edit a ride opening alert push
	 */
	@POST("/api/push/rideOpenAlert")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public SetRideOpenAlertResponse setRideOpenAlert(
			@Query("city") String city,
			@Body SetRideOpenAlertBodyParams bodyParams);

	@POST("/api/push/rideOpenAlert")
	@Headers({
		RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
		RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public void setRideOpenAlert(
			@Query("city") String city,
			@Body SetRideOpenAlertBodyParams bodyParams,
			Callback<SetRideOpenAlertResponse> cb);

	/**
	 *  Delete a ride opening alert push
	 */
	@DELETE("/api/push/rideOpenAlert")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public DeleteRideOpenAlertResponse deleteRideOpenAlert(
			@Query("city") String city,
			@Query("deviceId") String deviceId,
			@Query("rideId") long rideId);

	@DELETE("/api/push/rideOpenAlert")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void deleteRideOpenAlert(
			@Query("city") String city,
			@Query("deviceId") String deviceId,
			@Query("rideId") long rideId,
			Callback<DeleteRideOpenAlertResponse> cb);

	/**
	 * Event series
	 */
	@GET("/api/eventSeries?pageSize=all")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public void getEventSeries(
			@Query("city") String city,
			Callback<GetEventSeriesResponse> cb);

	@GET("/api/eventSeries?pageSize=all")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public GetEventSeriesResponse getEventSeries(@Query("city") String city);

	/**
	 * Offers
	 */
	@GET("/api/Offers/Vendors")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getOfferVendors(
			@Query("city") String city,
			Callback<GetOfferVendorsResponse> cb);

	@GET("/api/Offers/Vendors")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public GetOfferVendorsResponse getOfferVendors(@Query("city") String city);

	@GET("/api/Offers/{vendor}")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public OfferSeries getVendorOffers(
	        @Path("vendor") String vendor);

	@GET("/api/Offers/{vendor}")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public void getVendorOffers(
            @Path("vendor") String vendor,
            Callback<OfferSeries> cb);

	@GET("/api/Offers/{offerId}/CouponCode")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public GetOfferCouponResponse getOfferCoupon(
            @Path("offerId") long offerId);

	@GET("/api/Offers/{offerId}/CouponCode")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public void getOfferCoupon(
            @Path("offerId") long offerId,
            Callback<GetOfferCouponResponse> cb);

	/**
	 * Interactive Experiences
	 */
	@GET("/api/interactiveexperiences")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetInteractiveExperienceResponse getInteractiveExperiences(
			@Query("city") String city);

	@GET("/api/interactiveexperiences")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getInteractiveExperiences(
			@Query("city") String city,
			Callback<GetInteractiveExperienceResponse> cb);

	@GET("/api/photoframeexperiences")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetPhotoFramesExperienceResponse getPhotoFramesExperiences(
			@Query("city") String city,
			@Query("page") int page,
			@Query("pageSize") String pageSize);

	@GET("/api/PhotoFrameExperiences")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetPhotoFramesExperienceResponse getPhotoFramesExperiences(
			@Query("city") String city,
			@Query("page") int page,
			@Query("pageSize") String pageSize,
			Callback<GetPhotoFramesExperienceResponse> cb);

	/**
	 *  Submit guest feedback
	 */
	@POST("/api/Forms/GuestFeedback")
	@Headers({
			RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
			RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public SendGuestFeedbackResponse sendGuestFeedback(
			@Query("city") String city,
			@Body SendGuestFeedbackBodyParams bodyParams);

	@POST("/api/Forms/GuestFeedback")
	@Headers({
			RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
			RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public void sendGuestFeedback(
			@Query("city") String city,
			@Body SendGuestFeedbackBodyParams bodyParams,
			Callback<SendGuestFeedbackResponse> cb);

	/**
	 *  newsletter registrations
	 */
	@POST("/api/Forms/NewsletterRegistration")
	@Headers({
			RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
			RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public NewsletterRegistrationResponse sendNewsletterRegistration(
			@Query("city") String city,
			@Body NewsletterRegistrationRequest.NewsletterRegistrationBodyParams bodyParams);

	@POST("/api/Forms/NewsletterRegistration")
	@Headers({
			RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
			RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public void sendNewsletterRegistration(
			@Query("city") String city,
			@Body NewsletterRegistrationRequest.NewsletterRegistrationBodyParams bodyParams,
			Callback<NewsletterRegistrationResponse> cb);

	/**
	 *  Get Queues
	 */
	@GET("/api/Queues?page=1&pageSize=all")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public GetQueuesByPageResponse getQueuesByPage();

	@GET("/api/Queues?page=1&pageSize=all")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void getQueuesByPage(Callback<GetQueuesByPageResponse> cb);

    /**
     *  Get Appointment Times
     */
    @GET("/api/Queues/{queueId}/AppointmentTimes")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public GetAppointmentTimesResponse getAppointmentTimesByQueue(
            @Path("queueId") long queueId,
            @Query("date") String date);

    @GET("/api/Queues/{queueId}/AppointmentTimes")
    @Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
    public void getAppointmentTimesByQueue(
            @Path("queueId") long queueId,
            @Query("date") String date,
            Callback<GetAppointmentTimesResponse> cb);


	/**
	 *  Create Appointment Ticket
	 */
	@POST("/api/Queues/{queueId}/AppointmentTimes/{AppointmentTimeId}/Appointments/v1.6/Create?blnReturnObjectModel=true")
	@Headers({
			RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
			RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public CreateAppointmentTimeResponse createAppointmentTime(@Header("Authorization") String auth, @Path("queueId") long queueId,
			@Path("AppointmentTimeId") long appointmentTimeId,
			@Body CreateAppointmentTimeRequest.CreateAppointTimeBodyParams bodyParams);

	@POST("/api/Queues/{queueId}/AppointmentTimes/{AppointmentTimeId}/Appointments/v1.6/Create?blnReturnObjectModel=true")
	@Headers({
			RequestHeaders.HEADER_CONTENT_TYPE_APPLICATION_JSON,
			RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1
	})
	public void createAppointmentTime(@Header("Authorization") String auth, @Path("queueId") long queueId,
			@Path("AppointmentTimeId") long appointmentTimeId,
			@Body CreateAppointmentTimeRequest.CreateAppointTimeBodyParams bodyParams,
			Callback<CreateAppointmentTimeResponse> cb);



	/**
	 *  Delete Create Appointment Ticket
	 */
	@DELETE("/api/Queues/{queueId}/AppointmentTimes/{appointmentTimeId}/Appointments/v1.6/{appointmentTicketId}")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public DeleteCreatedAppointmentTicketResponse deleteAppointmentTicket(
			@Header("Authorization") String auth,
			@Path("queueId") long queueId,
			@Path("appointmentTimeId") long appointmentTimeId,
			@Path("appointmentTicketId") long appointmentTicketId);

	@DELETE("/api/Queues/{queueId}/AppointmentTimes/{appointmentTimeId}/Appointments/v1.6/{appointmentTicketId}")
	@Headers(RequestHeaders.HEADER_X_UNIWEBSERVICE_SERVICE_VERSION_1)
	public void deleteAppointmentTicket(
			@Header("Authorization") String auth,
			@Path("queueId") long queueId,
			@Path("appointmentTimeId") long appointmentTimeId,
			@Path("appointmentTicketId") long appointmentTicketId,
			Callback<DeleteCreatedAppointmentTicketResponse> cb);

	@GET("/api/MobilePages")
	void getMobilePages(@Query("page") String page, @Query("pageSize") String pageSize, Callback<GetMobilePagesResponse> cb);

}
