/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.news;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.news.GetNewsRequest;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.domain.push.NewsPushResponse;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.state.settings.UniversalOrlandoSettings;
import com.universalstudios.orlandoresort.model.state.settings.UniversalOrlandoSettingsManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class NewsUtils {
	private static final String TAG = NewsUtils.class.getSimpleName();

	public static String getFormattedDate(Long timeInMillis, Resources r) {
		if (timeInMillis == null) {
			return null;
		}

		Date dateToFormat = new Date(timeInMillis);

		// Get a calendar set to the beginning of the current day, in the park's timezone
		Calendar calendarToday = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		calendarToday.set(Calendar.HOUR_OF_DAY, 0);
		calendarToday.set(Calendar.MINUTE, 0);
		calendarToday.set(Calendar.SECOND, 0);
		calendarToday.set(Calendar.MILLISECOND, 0);

		Calendar calendarYesterday = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		calendarYesterday.add(Calendar.DATE, -1);
		calendarYesterday.set(Calendar.HOUR_OF_DAY, 0);
		calendarYesterday.set(Calendar.MINUTE, 0);
		calendarYesterday.set(Calendar.SECOND, 0);
		calendarYesterday.set(Calendar.MILLISECOND, 0);

		String formattedDate;
		if (dateToFormat.getTime() > calendarToday.getTimeInMillis()) {
			SimpleDateFormat sdfOutTime;
			if (DateTimeUtils.is24HourFormat()) {
				sdfOutTime = new SimpleDateFormat("HH:mm z", Locale.US);
			} else {
				sdfOutTime = new SimpleDateFormat("h:mm a z", Locale.US);
			}
			sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
			formattedDate = sdfOutTime.format(dateToFormat);
		}
		else if (dateToFormat.getTime()  > calendarYesterday.getTimeInMillis()) {
			formattedDate = r.getString(R.string.news_date_yesterday);
		}
		else {
			SimpleDateFormat sdfOutTime = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
			formattedDate = sdfOutTime.format(dateToFormat);
		}

		return formattedDate;
	}


	public static void updateNewsHasBeenReadInDatabase(final List<News> newsList, boolean hasBeenRead,
			final ContentResolver contentResolver, boolean async) {
		if (contentResolver == null || newsList == null) {
			return;
		}

		for (News news : newsList) {
			if (news.getHasBeenRead() == null || !news.getHasBeenRead().booleanValue()) {
				updateNewsHasBeenReadInDatabase(news, hasBeenRead, contentResolver, async);
			}
		}
	}

	public static void updateNewsHasBeenReadInDatabase(final News news, boolean hasBeenRead,
			final ContentResolver contentResolver, boolean async) {
		if (contentResolver == null || news == null) {
			return;
		}

		// Update the news object
		news.setHasBeenRead(hasBeenRead);

		final ContentValues contentValues = new ContentValues();
		contentValues.put(NewsTable.COL_HAS_BEEN_READ, news.getHasBeenRead());
		contentValues.put(NewsTable.COL_NEWS_OBJECT_JSON, news.toJson());

		final String where = new StringBuilder(NewsTable.COL_NEWS_ID)
		.append(" = ").append(news.getId())
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// Try to update the item
					int itemsUpdated = contentResolver.update(UniversalOrlandoContentUris.NEWS, contentValues, where, null);

					// If the item doesn't exist, log it
					if (itemsUpdated == 0) {
						if (BuildConfig.DEBUG) {
							Log.w(TAG, "updateNewsHasBeenReadInDatabase: news item does no exist in the database");
						}
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "updateNewsHasBeenReadInDatabase: exception saving to database", e);
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

	/**
	 * Note: This should only be called from a background thread, since it does database queries and long operations.
	 * 
	 * @param parkNewsPushResponse
	 * @param context
	 */
	public static void handleNewsPushResponse(NewsPushResponse parkNewsPushResponse, Context context) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "handleParkNewsPushResponse");
		}

		// Sync the latest park news
		GetNewsRequest getNewsRequest = new GetNewsRequest.Builder(null)
		.setPageSize(GetNewsRequest.PAGE_SIZE_ALL)
		.setPage(1)
		.build();

		NetworkUtils.queueNetworkRequest(getNewsRequest);
		NetworkUtils.startNetworkService();

		// Show notification only if the show park news setting is enabled
		UniversalOrlandoSettings settings = UniversalOrlandoSettingsManager.getInstance();
		if (settings.getIsShowParkNewsNotificationsOn()) {

			String title = parkNewsPushResponse.getAlertTitle();
			title = (title == null) ? "" : title;
			String message = parkNewsPushResponse.getAlertMessage();
			message = (message == null) ? "" : message;
			Long parkNewsId = parkNewsPushResponse.getId();

			if (parkNewsId != null) {
				NotificationCompat.Builder notificationBuilder = NotificationUtils.createNewsNotification(
						title, message, title, title, message, parkNewsId);
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(parkNewsId.toString(), NotificationUtils.NOTIFICATION_ID_NEWS,
						notificationBuilder.build());
			}
		}

	}
}
