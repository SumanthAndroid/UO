/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * 
 * @author Steven Byle
 */
public class TriggerShowAlertService extends IntentService {
	private static final String TAG = TriggerShowAlertService.class.getSimpleName();

	public static final String ACTION_TRIGGER_SHOW_ALERT = "com.universalstudios.orlandoresort.ACTION_TRIGGER_SHOW_ALERT";
	private static final String KEY_ARG_SHOW_ALERT_ID_STRING = "KEY_ARG_SHOW_ALERT_ID_STRING";

	private NotificationCompat.Builder mNotificationBuilder;
	private NotificationManager mNotificationManager;

	/**
	 * 
	 */
	public TriggerShowAlertService() {
		super(TriggerShowAlertService.class.getSimpleName());
	}

	public static Bundle newInstanceBundle(String alertIdString) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_SHOW_ALERT_ID_STRING, alertIdString);
		return args;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onHandleIntent");
		}

		String alertIdString;

		// Default parameters
		Bundle args = intent.getExtras();
		if (args == null) {
			alertIdString = null;
		}
		// Otherwise, set incoming parameters
		else {
			alertIdString = args.getString(KEY_ARG_SHOW_ALERT_ID_STRING);
		}

		if (alertIdString != null) {
			Cursor data;
			try {
				DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertDetailDatabaseQuery(alertIdString);
				data = getContentResolver().query(
						databaseQuery.getContentUri(),
						databaseQuery.getProjection(),
						databaseQuery.getSelection(),
						databaseQuery.getSelectionArgs(),
						databaseQuery.getOrderBy());
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "onHandleIntent: exception while trying to query alert");
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
				return;
			}

			// Check that the alert still exists
			if (data != null && data.moveToFirst()) {
				String showTimeAlertJson = data.getString(data.getColumnIndex(AlertsTable.COL_ALERT_OBJECT_JSON));
				String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
				Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
				String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

				ShowTimeAlert showTimeAlert = GsonObject.fromJson(showTimeAlertJson, ShowTimeAlert.class);

				if (showTimeAlert != null) {
					// Next, check to make sure the alert has really gone off
					Date showTimeDate = showTimeAlert.getShowTimeDateForToday();
					Date alertDate = showTimeAlert.getAlertDateForToday();
					if (showTimeDate != null && alertDate != null && System.currentTimeMillis() >= alertDate.getTime()) {

						SimpleDateFormat sdfOutTime;
						if (DateTimeUtils.is24HourFormat()) {
							sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
						} else {
							// Format the show time to 12 hour ("2:30 PM"), park's time
							sdfOutTime = new SimpleDateFormat("h:mm a", Locale.US);
						}
						sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
						String formattedShowTime = sdfOutTime.format(showTimeDate);

						// Show the notification
						String title = showTimeAlert.getPoiName();
						String text = getString(R.string.show_alerts_content,
								formattedShowTime);
						String textExpanded = getString(R.string.show_alerts_content_expanded,
								showTimeAlert.getPoiName(), formattedShowTime);

						mNotificationBuilder = NotificationUtils.createAlertNotification(
								title, text, textExpanded, title, textExpanded,
								venueObjectJson, poiObjectJson, poiTypeId, showTimeAlert.getIdString());
						mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						mNotificationManager.notify(showTimeAlert.getIdString(), NotificationUtils.NOTIFICATION_ID_ALERT,
								mNotificationBuilder.build());

						// Delete the alert from the database since it has triggered
						AlertsUtils.deleteAlertFromDatabase(showTimeAlert.getIdString(), false);

						// Track the event
						Map<String, Object> extraData = new AnalyticsUtils.Builder()
						.setEnvironmentVar(72, showTimeAlert.getShowTime())
						.build();
						AnalyticsUtils.trackEvent(
								showTimeAlert.getPoiName(),
								AnalyticsUtils.EVENT_NAME_RECEIVE_ALERT,
								AnalyticsUtils.EVENT_NUM_RECEIVE_ALERT,
								extraData);
					}
				}
			}
		}
	}

}
