/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import java.util.Date;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.model.alerts.Alert;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SetAlertsOnBootCompletedService extends IntentService {
	private static final String TAG = SetAlertsOnBootCompletedService.class.getSimpleName();

	/**
	 * 
	 */
	public SetAlertsOnBootCompletedService() {
		super(SetAlertsOnBootCompletedService.class.getSimpleName());
	}

	@Override
	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void onHandleIntent(Intent intent) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onHandleIntent");
		}

		// Default parameters
		Bundle args = intent.getExtras();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		Cursor data;
		try {
			DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertsDatabaseQuery();
			data = getContentResolver().query(
					databaseQuery.getContentUri(),
					databaseQuery.getProjection(),
					databaseQuery.getSelection(),
					databaseQuery.getSelectionArgs(),
					databaseQuery.getOrderBy());
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "onHandleIntent: exception while trying to query alerts");
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
			return;
		}

		// Find all show alerts that still exist, and set them again
		if (data != null && data.moveToFirst()) {
			do {
				String alertObjectJson = data.getString(data.getColumnIndex(AlertsTable.COL_ALERT_OBJECT_JSON));
				Integer alertTypeId = data.getInt(data.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
				Alert alert = Alert.fromJson(alertObjectJson, alertTypeId);

				// If a show time alert, set an alarm for it
				if (alert != null && alert instanceof ShowTimeAlert) {
					ShowTimeAlert showTimeAlert = (ShowTimeAlert) alert;

					// Set an alarm to send the trigger intent
					Date alertDateForToday = showTimeAlert.getAlertDateForToday();
					if (alertDateForToday != null) {
						// Create an intent to trigger the alert notification
						PendingIntent pendingIntent = AlertsUtils.createTriggerShowAlertPendingIntent(showTimeAlert.getIdString());

						// Get the alarm manager to handle setting alerts
						AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

						long triggerTimeInMillis = alertDateForToday.getTime();
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
							alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
						}
						else {
							alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
						}
					}
				}
			} while (data.moveToNext());
		}
	}

}
