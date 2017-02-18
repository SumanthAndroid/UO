package com.universalstudios.orlandoresort.controller.userinterface.network;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.controller.userinterface.tutorial.TutorialActivity;
import com.universalstudios.orlandoresort.controller.userinterface.welcome.WelcomeActivity;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.GetRegisteredAlertsRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.RegisterAlertsRequest;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.GetQueuesByPageRequest;
import com.universalstudios.orlandoresort.model.network.domain.control.GetControlPropertiesRequest;
import com.universalstudios.orlandoresort.model.network.domain.events.GetEventSeriesRequest;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsRequest;
import com.universalstudios.orlandoresort.model.network.domain.offers.GetOfferVendorsRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenueHoursRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenuesRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 * 
 */
public abstract class NetworkRefreshActivity extends NetworkActivity implements LoaderCallbacks<Cursor> {
	private static final String TAG = NetworkRefreshActivity.class.getSimpleName();

	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

	public static final int TOKEN_EXPIRES_IN_THE_NEXT_SEC = 15 * 60; // 15 minutes
	public static final int MINIMUM_WAIT_TIME_INTERVAL_IN_SEC = 1 * 60; // 1 minute
	public static final int DEFAULT_WAIT_TIME_UPDATE_INTERVAL_IN_SEC = 1 * 60; // 1 minute
	public static final int DATA_UPDATE_INTERVAL_IN_SEC = 8 * 60 * 60; // 8 hours

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID_VENUES = LoaderUtils.LOADER_ID_DATA_REFRESH_ACTIVITY;

	private NotificationManager mNotificationManager;
	private DatabaseQuery mVenueDatabaseQuery;
	private ArrayList<Venue> mVenues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the notification manager to update notifications
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Create a query for parks to use for hour verification
		mVenues = new ArrayList<Venue>();

		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			mVenueDatabaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD);
		} else {
			mVenueDatabaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE,
					VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA);
		}

		// Create loader
		Bundle loaderArgs = new Bundle();
		loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, mVenueDatabaseQuery.toJson());
		getSupportLoaderManager().initLoader(LOADER_ID_VENUES, loaderArgs, this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Only try to refresh data if there is a network connection
		if (NetworkUtils.isNetworkConnected()) {

			// If the token is expiring soon, get a new one
			boolean startNetworkService = false;
			if (UniversalAppStateManager.tokenExpiresInTheNext(TOKEN_EXPIRES_IN_THE_NEXT_SEC)) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onResume: token is expiring soon, attempting to get a new token");
				}

				GetControlPropertiesRequest getControlPropertiesRequest = new GetControlPropertiesRequest.Builder(null)
				.build();
				NetworkUtils.queueNetworkRequest(getControlPropertiesRequest);

				startNetworkService = true;
			}

			// Get the wait time interval
			UniversalAppState uoState = UniversalAppStateManager.getInstance();
			boolean isInPark = uoState.isInResortGeofence();
			Long waitTimeUpdateIntervalInSecLong = uoState.getWaitTimeUpdateIntervalInSec();
			long waitTimeUpdateIntervalInSec =
					(waitTimeUpdateIntervalInSecLong != null && waitTimeUpdateIntervalInSecLong >= MINIMUM_WAIT_TIME_INTERVAL_IN_SEC)
					? waitTimeUpdateIntervalInSecLong : DEFAULT_WAIT_TIME_UPDATE_INTERVAL_IN_SEC;

			// If the last POI sync is beyond the business set threshold, or it is stale, sync POIs
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastPoiSyncInMillis(), waitTimeUpdateIntervalInSec)
					|| !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastPoiSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onResume: the last POI sync was beyond the wait time sync threshold or was stale, syncing POIs...");
				}

				GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(null)
				.setPriority(Priority.HIGH)
				.build();
				NetworkUtils.queueNetworkRequest(getPoisRequest);

				startNetworkService = true;
			}

			// If the last Queues sync is beyond the business set threshold, or it is stale, sync Queues
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastQueuesSyncInMillis(), waitTimeUpdateIntervalInSec)
					|| !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastQueuesSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "onResume: the last Queue sync was beyond the wait time sync threshold or was stale, syncing Queues...");
				}

				GetQueuesByPageRequest getQueuesRequest = new GetQueuesByPageRequest.Builder(null)
						.setPriority(Priority.NORMAL)
						.build();
				NetworkUtils.queueNetworkRequest(getQueuesRequest);
				startNetworkService = true;
			}


			// If the last Event series sync is beyond the business set threshold, or it is stale, sync Event series
            if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastEventSeriesSyncInMillis(), waitTimeUpdateIntervalInSec)
                    || !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastEventSeriesSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "onResume: the last Event series sync was beyond the wait time sync threshold or was stale, syncing Event series...");
                }

                GetEventSeriesRequest getEventSeriesRequest = new GetEventSeriesRequest.Builder(null)
                .setPriority(Priority.HIGH)
                .build();
                NetworkUtils.queueNetworkRequest(getEventSeriesRequest);

                startNetworkService = true;
            }
            
            // If the last Offers sync is beyond the business set threshold, or it is stale, sync Offers
            if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastOffersSyncInMillis(), waitTimeUpdateIntervalInSec)
                    || !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastOffersSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "onResume: the last Offers sync was beyond the wait time sync threshold or was stale, syncing Offers...");
                }

                GetOfferVendorsRequest getOfferVendorsRequest = new GetOfferVendorsRequest.Builder(null)
                .setPriority(Priority.HIGH)
                .build();
                NetworkUtils.queueNetworkRequest(getOfferVendorsRequest);

                startNetworkService = true;
            }

			// If the last park news sync is beyond the business set threshold, or it is stale, sync park news
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastParkNewsSyncInMillis(), waitTimeUpdateIntervalInSec)
					|| !UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastParkNewsSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)) {

				GetNewsRequest getNewsRequest = new GetNewsRequest.Builder(null)
				.setPriority(Priority.LOW)
				.setPageSize(GetNewsRequest.PAGE_SIZE_ALL)
				.setPage(1)
				.build();
				NetworkUtils.queueNetworkRequest(getNewsRequest);

				startNetworkService = true;
			}

			// If the venue data is old, or has not synced today, sync
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastVenueSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)
					|| !UniversalAppStateManager.hasSyncedToday(uoState.getDateOfLastVenueSyncInMillis())) {
				GetVenuesRequest getVenuesRequest = new GetVenuesRequest.Builder(null)
				.setPriority(Priority.HIGH)
				.build();
				NetworkUtils.queueNetworkRequest(getVenuesRequest);

				startNetworkService = true;
			}

			// If the venue hours data is old, sync
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastVenueHoursSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)) {
				Calendar sixMonthsLater = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
				sixMonthsLater.add(Calendar.MONTH, 6);

				if (BuildConfigUtils.isLocationFlavorHollywood()) {
					GetVenueHoursRequest getVenueHoursRequestUsh = new GetVenueHoursRequest.Builder(null)
							.setPriority(Priority.LOW)
							.setVenueId(VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD)
							.setEndDate(sixMonthsLater.getTime())
							.build();
					NetworkUtils.queueNetworkRequest(getVenueHoursRequestUsh);

					GetVenueHoursRequest getVenueHoursRequestCwh = new GetVenueHoursRequest.Builder(null)
							.setPriority(Priority.LOW)
							.setVenueId(VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD)
							.setEndDate(sixMonthsLater.getTime())
							.build();
					NetworkUtils.queueNetworkRequest(getVenueHoursRequestCwh);
				} else {
					GetVenueHoursRequest getVenueHoursRequestIoa = new GetVenueHoursRequest.Builder(null)
							.setPriority(Priority.LOW)
							.setVenueId(VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE)
							.setEndDate(sixMonthsLater.getTime())
							.build();
					NetworkUtils.queueNetworkRequest(getVenueHoursRequestIoa);

					GetVenueHoursRequest getVenueHoursRequestUsf = new GetVenueHoursRequest.Builder(null)
							.setPriority(Priority.LOW)
							.setVenueId(VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA)
							.setEndDate(sixMonthsLater.getTime())
							.build();
					NetworkUtils.queueNetworkRequest(getVenueHoursRequestUsf);

					GetVenueHoursRequest getVenueHoursRequestCw = new GetVenueHoursRequest.Builder(null)
							.setPriority(Priority.LOW)
							.setVenueId(VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO)
							.setEndDate(sixMonthsLater.getTime())
							.build();
					NetworkUtils.queueNetworkRequest(getVenueHoursRequestCw);

					GetVenueHoursRequest getVenueHoursRequestWnw = new GetVenueHoursRequest.Builder(null)
							.setPriority(Priority.LOW)
							.setVenueId(VenuesTable.VAL_VENUE_ID_WET_N_WILD)
							.setEndDate(sixMonthsLater.getTime())
							.build();
					NetworkUtils.queueNetworkRequest(getVenueHoursRequestWnw);
				}

				startNetworkService = true;
			}

			// If the last push reg sync is old, or has not synced since 3am today, sync
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastPushRegSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)
					|| !UniversalAppStateManager.hasSyncedSinceHourOfToday(uoState.getDateOfLastPushRegSyncInMillis(), 3)) {

				RegisterAlertsRequest registerAlertsRequest = new RegisterAlertsRequest.Builder(null)
				.setPriority(Priority.HIGH)
				.setDeviceId(uoState.getGcmRegistrationId())
				.setInPark(uoState.isInResortGeofence())
				.build();
				NetworkUtils.queueNetworkRequest(registerAlertsRequest);

				startNetworkService = true;
			}

			// If the last alert sync is old, or has not synced since 3am today, sync
			if (!UniversalAppStateManager.hasSyncedInTheLast(uoState.getDateOfLastAlertSyncInMillis(), DATA_UPDATE_INTERVAL_IN_SEC)
					|| !UniversalAppStateManager.hasSyncedSinceHourOfToday(uoState.getDateOfLastAlertSyncInMillis(), 3)) {

				GetRegisteredAlertsRequest getRegisteredAlertsRequest = new GetRegisteredAlertsRequest.Builder(null)
				.setPriority(Priority.NORMAL)
				.build();
				NetworkUtils.queueNetworkRequest(getRegisteredAlertsRequest);

				startNetworkService = true;
			}

			// Only start the network service if requests were queued
			if (startNetworkService) {
				NetworkUtils.startNetworkService();
			}
		}

		// Check to see if the user has seen the tutorial, and show it if they haven't
		UniversalAppState state = UniversalAppStateManager.getInstance();
		if (!state.hasViewedTutorial()) {
			state.setHasViewedTutorial(true);
			UniversalAppStateManager.saveInstance(false);

			startActivity(new Intent(this, TutorialActivity.class));
		}
		// Orlando specific logic for welcome page and express pass reminders
		else if (BuildConfigUtils.isLocationFlavorOrlando()) {
			// Otherwise, check to see if the user has seen the welcome screen, and show it if they
			// are in Orlando and in the park
			if (!state.hasViewedWelcome()
				&& (state.isInIslandsOfAdventureGeofence()
					|| state.isInCityWalkOrlandoGeofence()
					|| state.isInUniversalStudiosFloridaGeofence())) {

				state.setHasViewedWelcome(true);
				UniversalAppStateManager.saveInstance(false);
				startActivity(new Intent(this, WelcomeActivity.class));
			}
			// Otherwise, check to see if the user hit remind later on the express pass notification, and show it if
			// they are in park and it is past the delay interval
			else {
				checkExpressPassNotification();
			}
		}
	}

	@Override
	public void handleNetworkResponse(NetworkResponse networkResponse) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleNetworkResponse");
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateLoader: id = " + id);
		}

		switch (id) {
			case LOADER_ID_VENUES:
				String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
				DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
				return LoaderUtils.createCursorLoader(databaseQuery);
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_VENUES:
				// Clear the currently held venues
				mVenues.clear();

				// Go through each new venue
				if (data != null && data.moveToFirst()) {
					do {
						String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
						Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);

						if (venue != null) {
							mVenues.add(venue);
						}
					} while(data.moveToNext());

					// After the load check to see if the express pass should fire
					checkExpressPassNotification();
				}
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_VENUES:
				// Data is not available anymore, delete reference
				break;
		}
	}

	private void checkExpressPassNotification() {
		UniversalAppState state = UniversalAppStateManager.getInstance();

		if (state.hasViewedWelcome()
				&& state.isRemindExpressPassLater()
				&& (state.isInIslandsOfAdventureGeofence()
						|| state.isInCityWalkOrlandoGeofence()
						|| state.isInUniversalStudiosFloridaGeofence())) {

			// Check that IOA or USF is open
			boolean isAParkOpen = false;
			long currentTimeInMs = new Date().getTime();
			if (mVenues != null && mVenues.size() > 0) {
				for (Venue venue : mVenues) {
					if (PoiUtils.isDuringVenueHours(currentTimeInMs, venue.getHours())) {
						isAParkOpen = true;
						break;
					}
				}
			}

			// If a park isn't open, don't go any further
			if (!isAParkOpen) {
				return;
			}

			// Check to see that it has been longer than the delay interval
			Long remindExpressPassLaterDateInMillis = state.getRemindExpressPassLaterDateInMillis();
			if (remindExpressPassLaterDateInMillis != null && currentTimeInMs > remindExpressPassLaterDateInMillis) {

				// Turn off the reminder
				state.setRemindExpressPassLater(false);
				state.setRemindExpressPassLaterDateInMillis(null);
				UniversalAppStateManager.saveInstance(false);

				// Send notification
				NotificationCompat.Builder builder = NotificationUtils.createExpressPassNotification(
						getString(R.string.ex_pass_stat_reminder_title),
						getString(R.string.ex_pass_stat_reminder_content),
						getString(R.string.ex_pass_stat_reminder_ticker),
						getString(R.string.ex_pass_stat_reminder_title),
						getString(R.string.ex_pass_stat_reminder_content));
				mNotificationManager.notify(NotificationUtils.NOTIFICATION_ID_EXPRESS_PASS, builder.build());
			}
		}
	}

	protected List<Venue> getParkVenues() {
		return mVenues;
	}

}
