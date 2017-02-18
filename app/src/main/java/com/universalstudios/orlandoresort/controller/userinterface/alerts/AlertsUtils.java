package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.model.alerts.Alert;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.push.RideClosedAlertPushResponse;
import com.universalstudios.orlandoresort.model.network.domain.push.RideOpenAlertPushResponse;
import com.universalstudios.orlandoresort.model.network.domain.push.WaitTimeAlertPushResponse;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AlertsUtils {
	private static final String TAG = AlertsUtils.class.getSimpleName();

	private static HashMap<String, Integer> sShowAlertIntervalMinsMap;
	private static HashMap<Integer, String> sShowAlertIntervalStringsMap;

	public static void saveShowTimeAlertToDatabase(final ShowTimeAlert showTimeAlert, boolean async) {
		final ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		if (contentResolver == null || showTimeAlert == null) {
			return;
		}

		final ContentValues contentValues = new ContentValues();
		contentValues.put(AlertsTable.COL_ALERT_ID_STRING, showTimeAlert.getIdString());
		contentValues.put(AlertsTable.COL_ALERT_TYPE_ID, AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME);
		contentValues.put(AlertsTable.COL_POI_ID, showTimeAlert.getPoiId());
		contentValues.put(AlertsTable.COL_POI_NAME, showTimeAlert.getPoiName());
		contentValues.put(AlertsTable.COL_SHOW_TIME, showTimeAlert.getShowTime());
		contentValues.put(AlertsTable.COL_NOTIFY_MIN_BEFORE, showTimeAlert.getNotifyMinBefore());
		contentValues.put(AlertsTable.COL_CREATED_DATE_IN_MILLIS, System.currentTimeMillis());
		contentValues.put(AlertsTable.COL_ALERT_OBJECT_JSON, showTimeAlert.toJson());

		final String where = new StringBuilder(AlertsTable.COL_ALERT_ID_STRING)
		.append(" = '").append(showTimeAlert.getIdString()).append("'")
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// Try to update the alert first
					int itemsUpdated = contentResolver.update(UniversalOrlandoContentUris.ALERTS, contentValues, where, null);

					// If the alert doesn't exist, insert it and track when it was created
					if (itemsUpdated == 0) {
						contentResolver.insert(UniversalOrlandoContentUris.ALERTS, contentValues);
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "saveShowTimeAlertToDatabase: exception saving alert to database", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		};

		// If requested, run asynchronously
		if (async) {
			new Thread(runnable).start();
		}
		// Otherwise, run synchronously
		else {
			runnable.run();
		}
	}

	public static void saveWaitTimeAlertToDatabase(final WaitTimeAlert waitTimeAlert, boolean async) {
		final ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		if (contentResolver == null || waitTimeAlert == null) {
			return;
		}

		final ContentValues contentValues = new ContentValues();
		contentValues.put(AlertsTable.COL_ALERT_ID_STRING, waitTimeAlert.getIdString());
		contentValues.put(AlertsTable.COL_ALERT_TYPE_ID, AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME);
		contentValues.put(AlertsTable.COL_POI_ID, waitTimeAlert.getPoiId());
		contentValues.put(AlertsTable.COL_POI_NAME, waitTimeAlert.getPoiName());
		contentValues.put(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN, waitTimeAlert.getNotifyThresholdInMin());
		contentValues.put(AlertsTable.COL_CREATED_DATE_IN_MILLIS, System.currentTimeMillis());
		contentValues.put(AlertsTable.COL_ALERT_OBJECT_JSON, waitTimeAlert.toJson());

		final String where = new StringBuilder(AlertsTable.COL_ALERT_ID_STRING)
		.append(" = '").append(waitTimeAlert.getIdString()).append("'")
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// Try to update the alert first
					int itemsUpdated = contentResolver.update(UniversalOrlandoContentUris.ALERTS, contentValues, where, null);

					// If the alert doesn't exist, insert it and track when it was created
					if (itemsUpdated == 0) {
						contentResolver.insert(UniversalOrlandoContentUris.ALERTS, contentValues);
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "saveWaitTimeAlertToDatabase: exception saving alert to database", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		};

		// If requested, run asynchronously
		if (async) {
			new Thread(runnable).start();
		}
		// Otherwise, run synchronously
		else {
			runnable.run();
		}
	}

	public static void saveRideOpenAlertToDatabase(final RideOpenAlert rideOpenAlert, boolean async) {
		final ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		if (contentResolver == null || rideOpenAlert == null) {
			return;
		}

		final ContentValues contentValues = new ContentValues();
		contentValues.put(AlertsTable.COL_ALERT_ID_STRING, rideOpenAlert.getIdString());
		contentValues.put(AlertsTable.COL_ALERT_TYPE_ID, AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN);
		contentValues.put(AlertsTable.COL_POI_ID, rideOpenAlert.getPoiId());
		contentValues.put(AlertsTable.COL_POI_NAME, rideOpenAlert.getPoiName());
		contentValues.put(AlertsTable.COL_CREATED_DATE_IN_MILLIS, System.currentTimeMillis());
		contentValues.put(AlertsTable.COL_ALERT_OBJECT_JSON, rideOpenAlert.toJson());

		final String where = new StringBuilder(AlertsTable.COL_ALERT_ID_STRING)
		.append(" = '").append(rideOpenAlert.getIdString()).append("'")
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// Try to update the alert first
					int itemsUpdated = contentResolver.update(UniversalOrlandoContentUris.ALERTS, contentValues, where, null);

					// If the alert doesn't exist, insert it and track when it was created
					if (itemsUpdated == 0) {
						contentResolver.insert(UniversalOrlandoContentUris.ALERTS, contentValues);
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "saveWaitTimeAlertToDatabase: exception saving alert to database", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		};

		// If requested, run asynchronously
		if (async) {
			new Thread(runnable).start();
		}
		// Otherwise, run synchronously
		else {
			runnable.run();
		}
	}

	public static void deleteAlertFromDatabase(final String alertIdString, boolean async) {
		final ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		if (contentResolver == null || alertIdString == null) {
			return;
		}

		final String where = new StringBuilder(AlertsTable.COL_ALERT_ID_STRING)
		.append(" = '").append(alertIdString).append("'")
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.ALERTS, where, null);
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "deleteAlertFromDatabase: exception deleting alert from database", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		};

		// If requested, run asynchronously
		if (async) {
			new Thread(runnable).start();
		}
		// Otherwise, run synchronously
		else {
			runnable.run();
		}
	}

	public static void deleteAlertsFromDatabase(List<Alert> alertsToDelete, final ContentResolver contentResolver, boolean async) {
		if (contentResolver == null || alertsToDelete == null || alertsToDelete.size() == 0) {
			return;
		}

		final StringBuilder whereBuilder = new StringBuilder();
		for(int i = 0; i < alertsToDelete.size(); i++) {
			if (i > 0) {
				whereBuilder.append(" OR ");
			}

			Alert alert = alertsToDelete.get(i);
			whereBuilder.append(AlertsTable.COL_ALERT_ID_STRING)
			.append(" = '").append(alert.getIdString()).append("'");
		}


		final String where = whereBuilder.toString();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.ALERTS, where, null);
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "deleteAlertsFromDatabase: exception deleting alerts from database", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		};

		// If requested, run asynchronously
		if (async) {
			new Thread(runnable).start();
		}
		// Otherwise, run synchronously
		else {
			runnable.run();
		}
	}

	public static void deleteOldShowTimeAlertsFromDatabase(final ContentResolver contentResolver, boolean async) {
		if (contentResolver == null) {
			return;
		}

		// Get a calendar set to the beginning of the current day, in the parks's timezone
		Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// Delete any alerts older than the current day
		final String where = new StringBuilder(AlertsTable.COL_FULL_ALERT_TYPE_ID)
		.append(" = ").append(AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME)
		.append(" AND ")
		.append(AlertsTable.COL_FULL_CREATED_DATE_IN_MILLIS)
		.append(" < ").append(calendar.getTimeInMillis())
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.ALERTS, where, null);
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "deleteOldShowTimeAlertsFromDatabase: exception deleting alert from database", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		};

		// If requested, run asynchronously
		if (async) {
			new Thread(runnable).start();
		}
		// Otherwise, run synchronously
		else {
			runnable.run();
		}
	}

	public static int convertNotifyStringToMin(Resources r, String notifyInterval) {
		if (notifyInterval == null) {
			return ShowTimeAlert.NOTIFY_INTERVAL_MIN_SHOW_START;
		}

		if (sShowAlertIntervalMinsMap == null) {
			sShowAlertIntervalMinsMap = new HashMap<String, Integer>();
			sShowAlertIntervalMinsMap.put(r.getString(R.string.show_alerts_notify_interval_item_show_start), ShowTimeAlert.NOTIFY_INTERVAL_MIN_SHOW_START);
			sShowAlertIntervalMinsMap.put(r.getString(R.string.show_alerts_notify_interval_item_5_min), ShowTimeAlert.NOTIFY_INTERVAL_MIN_5_MIN);
			sShowAlertIntervalMinsMap.put(r.getString(R.string.show_alerts_notify_interval_item_15_min), ShowTimeAlert.NOTIFY_INTERVAL_MIN_15_MIN);
			sShowAlertIntervalMinsMap.put(r.getString(R.string.show_alerts_notify_interval_item_30_min), ShowTimeAlert.NOTIFY_INTERVAL_MIN_30_MIN);
			sShowAlertIntervalMinsMap.put(r.getString(R.string.show_alerts_notify_interval_item_1_hr), ShowTimeAlert.NOTIFY_INTERVAL_MIN_1_HR);
			sShowAlertIntervalMinsMap.put(r.getString(R.string.show_alerts_notify_interval_item_2_hr), ShowTimeAlert.NOTIFY_INTERVAL_MIN_2_HR);
		}

		Integer minutes = sShowAlertIntervalMinsMap.get(notifyInterval);
		return minutes != null ? minutes : ShowTimeAlert.NOTIFY_INTERVAL_MIN_SHOW_START;
	}

	public static String convertNotifyMinBeforeToString(Resources r, int notifyMinBefore) {
		if (sShowAlertIntervalStringsMap == null) {
			sShowAlertIntervalStringsMap = new HashMap<Integer, String>();
			sShowAlertIntervalStringsMap.put(ShowTimeAlert.NOTIFY_INTERVAL_MIN_SHOW_START, r.getString(R.string.show_alerts_notify_interval_item_show_start));
			sShowAlertIntervalStringsMap.put(ShowTimeAlert.NOTIFY_INTERVAL_MIN_5_MIN, r.getString(R.string.show_alerts_notify_interval_item_5_min));
			sShowAlertIntervalStringsMap.put(ShowTimeAlert.NOTIFY_INTERVAL_MIN_15_MIN, r.getString(R.string.show_alerts_notify_interval_item_15_min));
			sShowAlertIntervalStringsMap.put(ShowTimeAlert.NOTIFY_INTERVAL_MIN_30_MIN, r.getString(R.string.show_alerts_notify_interval_item_30_min));
			sShowAlertIntervalStringsMap.put(ShowTimeAlert.NOTIFY_INTERVAL_MIN_1_HR, r.getString(R.string.show_alerts_notify_interval_item_1_hr));
			sShowAlertIntervalStringsMap.put(ShowTimeAlert.NOTIFY_INTERVAL_MIN_2_HR, r.getString(R.string.show_alerts_notify_interval_item_2_hr));
		}

		String notifyString = sShowAlertIntervalStringsMap.get(notifyMinBefore);
		return notifyString != null ? notifyString : r.getString(R.string.show_alerts_notify_interval_item_show_start);
	}

	public static PendingIntent createTriggerShowAlertPendingIntent(String showTimeAlertIdString) {
		Context context = UniversalOrlandoApplication.getAppContext();
		Intent intent = new Intent(context, TriggerShowAlertService.class);
		// Make each intent unique so Android doesn't overwrite any of them
		intent.setAction(TriggerShowAlertService.ACTION_TRIGGER_SHOW_ALERT + "_" + showTimeAlertIdString);
		intent.putExtras(TriggerShowAlertService.newInstanceBundle(showTimeAlertIdString));
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		return pendingIntent;
	}

	/**
	 * Note: This should only be called from a background thread, since it does database queries and long operations.
	 * 
	 * @param waitTimeAlertPushResponse
	 * @param context
	 */
	public static void handleWaitTimeAlertPushResponse(WaitTimeAlertPushResponse waitTimeAlertPushResponse, Context context) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleWaitTimeAlertPushResponse");
		}

		// Start a POI sync to load in the latest wait times
		GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(null)
		.setPriority(Priority.HIGH)
		.build();

		NetworkUtils.queueNetworkRequest(getPoisRequest);
		NetworkUtils.startNetworkService();

		// Query for the alert
		Long poiId = waitTimeAlertPushResponse.getId();
		DatabaseQuery waitTimeAlertQuery = DatabaseQueryUtils.getAlertDetailDatabaseQuery(WaitTimeAlert.computeIdString(poiId));

		// OK to block since this is in a background thread
		ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
		Cursor data = contentResolver.query(waitTimeAlertQuery.getContentUri(),
				waitTimeAlertQuery.getProjection(),
				waitTimeAlertQuery.getSelection(),
				waitTimeAlertQuery.getSelectionArgs(),
				waitTimeAlertQuery.getOrderBy());

		if (data != null && data.moveToFirst()) {
			Integer alertTypeId = data.getInt(data.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
			if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME) {
				// Pull out the alert data from the DB
				String waitTimeAlertIdString = data.getString(data.getColumnIndex(AlertsTable.COL_ALERT_ID_STRING));
				String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
				String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
				Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

				// Pull out the data from the push
				Integer waitTime = waitTimeAlertPushResponse.getCurrentWaitTimeInMin();
				waitTime = waitTime != null ? waitTime : -1;
				String poiName = waitTimeAlertPushResponse.getRideName();

				if (waitTime >= 0) {
					// Show the notification for the alert
					String title = poiName;
					String text = context.getString(R.string.wait_time_alerts_content, waitTime);
					String textExpanded = context.getString(R.string.wait_time_alerts_content_expanded,
							poiName, waitTime);

					NotificationCompat.Builder notificationBuilder = NotificationUtils.createAlertNotification(
							title, text, textExpanded, title, textExpanded,
							venueObjectJson, poiObjectJson, poiTypeId, waitTimeAlertIdString);
					NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.notify(waitTimeAlertIdString, NotificationUtils.NOTIFICATION_ID_ALERT,
							notificationBuilder.build());

					// Track the event
					Map<String, Object> extraData = new AnalyticsUtils.Builder()
					.setEnvironmentVar(72, "" + waitTime)
					.build();
					AnalyticsUtils.trackEvent(
							poiName,
							AnalyticsUtils.EVENT_NAME_RECEIVE_ALERT,
							AnalyticsUtils.EVENT_NUM_RECEIVE_ALERT,
							extraData);
				}

				// Delete the alert from the database since it has triggered
				AlertsUtils.deleteAlertFromDatabase(waitTimeAlertIdString, false);
			}
		}
	}

	/**
	 * Note: This should only be called from a background thread, since it does database queries and long operations.
	 * 
	 * @param rideClosedAlertPushResponse
	 * @param context
	 */
	public static void handleRideClosedAlertPushResponse(RideClosedAlertPushResponse rideClosedAlertPushResponse, Context context) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleRideClosedAlertPushResponse");
		}

		// Start a POI sync to load in the latest wait times
		GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(null)
		.setPriority(Priority.HIGH)
		.build();

		NetworkUtils.queueNetworkRequest(getPoisRequest);
		NetworkUtils.startNetworkService();

		// Query for the ride
		Long poiId = rideClosedAlertPushResponse.getId();
		DatabaseQuery poiQuery = DatabaseQueryUtils.getDetailDatabaseQuery(poiId);

		// OK to block since this is in a background thread
		ContentResolver contentResolver = context.getContentResolver();
		Cursor data = contentResolver.query(poiQuery.getContentUri(),
				poiQuery.getProjection(),
				poiQuery.getSelection(),
				poiQuery.getSelectionArgs(),
				poiQuery.getOrderBy());

		if (data != null && data.moveToFirst()) {
			// Pull out the POI data from the DB
			String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
			String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
			Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

			// Pull out the data from the push
			String poiName = rideClosedAlertPushResponse.getRideName();
			poiName = poiName != null ? poiName : "";

			// Show the notification for the alert
			String title = poiName;
			String text = context.getString(R.string.closed_alert_content);
			String textExpanded = context.getString(R.string.closed_alert_content_expanded, poiName);

			// Make a unique id string for this ride's closed alert
			String alertIdString = new StringBuilder().append(poiId).append("_").append("RIDE_CLOSED").toString();

			NotificationCompat.Builder notificationBuilder = NotificationUtils.createAlertNotification(
					title, text, textExpanded, title, textExpanded,
					venueObjectJson, poiObjectJson, poiTypeId, alertIdString);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(alertIdString, NotificationUtils.NOTIFICATION_ID_ALERT,
					notificationBuilder.build());
		}
	}

	/**
	 * Note: This should only be called from a background thread, since it does database queries and long operations.
	 * 
	 * @param rideOpenAlertPushResponse
	 * @param context
	 */
	public static void handleRideOpenAlertPushResponse(RideOpenAlertPushResponse rideOpenAlertPushResponse, Context context) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleRidOpenAlertPushResponse");
		}

		// Start a POI sync to load in the latest wait times
		GetPoisRequest getPoisRequest = new GetPoisRequest.Builder(null)
		.setPriority(Priority.HIGH)
		.build();

		NetworkUtils.queueNetworkRequest(getPoisRequest);
		NetworkUtils.startNetworkService();

		// Query for the alert
		Long poiId = rideOpenAlertPushResponse.getId();
		DatabaseQuery rideOpenAlertQuery = DatabaseQueryUtils.getAlertDetailDatabaseQuery(RideOpenAlert.computeIdString(poiId));

		// OK to block since this is in a background thread
		ContentResolver contentResolver = context.getContentResolver();
		Cursor data = contentResolver.query(rideOpenAlertQuery.getContentUri(),
				rideOpenAlertQuery.getProjection(),
				rideOpenAlertQuery.getSelection(),
				rideOpenAlertQuery.getSelectionArgs(),
				rideOpenAlertQuery.getOrderBy());

		if (data != null && data.moveToFirst()) {
			Integer alertTypeId = data.getInt(data.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
			if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN) {
				// Pull out the alert data from the DB
				String rideOpenAlertIdString = data.getString(data.getColumnIndex(AlertsTable.COL_ALERT_ID_STRING));
				String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
				String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
				Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

				// Pull out the data from the push
				String poiName = rideOpenAlertPushResponse.getRideName();

				// Show the notification for the alert
				String title = poiName;
				String text = context.getString(R.string.open_alert_content);
				String textExpanded = context.getString(R.string.open_alert_content_expanded, poiName);

				NotificationCompat.Builder notificationBuilder = NotificationUtils.createAlertNotification(
						title, text, textExpanded, title, textExpanded,
						venueObjectJson, poiObjectJson, poiTypeId, rideOpenAlertIdString);
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(rideOpenAlertIdString, NotificationUtils.NOTIFICATION_ID_ALERT,
						notificationBuilder.build());

				// Delete the alert from the database since it has triggered
				AlertsUtils.deleteAlertFromDatabase(rideOpenAlertIdString, false);
			}
		}
	}

	/**
	 * Util method to figure out if a wait time status is valid to show a ride open alert for.
	 * 
	 * @param waitTime
	 * @return
	 */
	public static boolean showSetRideOpenAlert(Integer waitTime) {
		// Do not show for closed long term, since the ride will probably not reopen that day
		return waitTime != null
				&& (waitTime == Ride.RIDE_WAIT_TIME_STATUS_CLOSED_FOR_CAPACITY
				|| waitTime == Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INCLEMENT_WEATHER
				|| waitTime == Ride.RIDE_WAIT_TIME_STATUS_CLOSED_TEMP
				|| waitTime == Ride.RIDE_WAIT_TIME_STATUS_OUT_OF_OPERATING_HOURS
				|| waitTime == Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INSIDE_OF_OPERATING_HOURS);
	}

}