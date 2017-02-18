/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.home.HomeActivity;
import com.universalstudios.orlandoresort.controller.userinterface.launcher.LauncherActivity;
import com.universalstudios.orlandoresort.controller.userinterface.news.NewsDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.wayfinding.WayfindingService;

/**
 * 
 * 
 * @author Steven Byle
 */
public class NotificationUtils {
	private static final String TAG = NotificationUtils.class.getSimpleName();

	public static final int NOTIFICATION_ID_MOCK_LOCATION_PROVIDER = 1;
	public static final int NOTIFICATION_ID_WAYFINDING = 2;
	public static final int NOTIFICATION_ID_EXPRESS_PASS = 3;
	public static final int NOTIFICATION_ID_ALERT = 4;
	public static final int NOTIFICATION_ID_NEWS = 5;
	public static final int NOTIFICATION_ID_APPOINTMENT_TICKET = 6;

	/**
	 * @param contentTitle
	 * @param contentText
	 * @param tickerText
	 * @param bigContentTitle
	 * @param bigText
	 * @param largeIcon
	 * @param playSound
	 * @param vibratePattern
	 * @param showAsHeadsUp
	 * @return
	 */
	public static NotificationCompat.Builder createWayfindingNotification(String contentTitle, String contentText, String tickerText, String bigContentTitle, String bigText, Bitmap largeIcon,
			boolean playSound, long[] vibratePattern, boolean showAsHeadsUp) {

		Context context = UniversalOrlandoApplication.getAppContext();
		// If the notification is pressed, resume the app as if the launcher icon was pressed
		Intent resumeAppIntent = new Intent(context, LauncherActivity.class);
		resumeAppIntent.setAction(Intent.ACTION_MAIN);
		resumeAppIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent resumeAppPendingIntent = PendingIntent.getActivity(context, 0, resumeAppIntent, 0);

		// If the stop button is pressed, stop the service
		Intent stopServiceIntent = new Intent(context, WayfindingService.class).putExtras(WayfindingService.newInstanceBundle(true));
		PendingIntent selectStopServicePendingIntent = PendingIntent.getService(context, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Build the notification to show
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_stat_wayfinding)
		.setContentIntent(resumeAppPendingIntent)
		.setOngoing(true)
		.setOnlyAlertOnce(false)
		.setContentTitle(contentTitle)
		.setContentText(contentText)
		.setSubText(context.getString(R.string.app_stat_sub_text))
		.setTicker(tickerText)
		.setWhen(System.currentTimeMillis());

		// If neither sound or vibrate are set, the heads up notification won't show on 5.0+
		if (playSound) {
			notificationBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
		}
		if (vibratePattern != null && vibratePattern.length > 0) {
			notificationBuilder.setVibrate(vibratePattern);
		}

		// API 16 (4.1) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(notificationBuilder)
			.setBigContentTitle(bigContentTitle)
			.bigText(bigText);

			notificationBuilder
			.setPriority(NotificationCompat.PRIORITY_MAX)
			.setStyle(bigTextStyle)
			.addAction(R.drawable.ic_action_notif_cancel,
					context.getString(R.string.wayfinding_stat_action_stop_guiding),
					selectStopServicePendingIntent);

			if (largeIcon != null) {
				notificationBuilder.setLargeIcon(largeIcon);
			}
		}
		// API 21 (5.0) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			// Only show as a heads up notification if requested
			notificationBuilder.setPriority(showAsHeadsUp ? NotificationCompat.PRIORITY_MAX : NotificationCompat.PRIORITY_DEFAULT);
		}

		return notificationBuilder;
	}

	/**
	 * @param contentTitle
	 * @param contentText
	 * @param tickerText
	 * @param bigContentTitle
	 * @param bigText
	 * @return
	 */
	public static NotificationCompat.Builder createExpressPassNotification(String contentTitle, String contentText, String tickerText, String bigContentTitle, String bigText) {

		Context context = UniversalOrlandoApplication.getAppContext();
		// Build the notification to show
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_stat_express_pass)
		.setOngoing(false)
		.setOnlyAlertOnce(true)
		.setAutoCancel(true)
		.setContentTitle(contentTitle)
		.setContentText(contentText)
		.setSubText(context.getString(R.string.app_stat_sub_text))
		.setTicker(tickerText)
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
		.setWhen(System.currentTimeMillis());

		// If the notification is pressed, open the explore by shopping - sells express pass page
		Bundle openExpressPassLocationsBundle = ExploreActivity.newInstanceBundle(
				R.string.action_title_express_pass_locations,
				ExploreType.SHOPPING_EXPRESS_PASS);
		Intent openExpressPassLocationsIntent = new Intent(context, ExploreActivity.class).putExtras(openExpressPassLocationsBundle);

		Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_HOME);
		Intent homeActivityIntent = new Intent(context, HomeActivity.class).putExtras(homeActivityBundle);

		// Stack the detail page on top of a home page
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context)
				.addNextIntent(homeActivityIntent)
				.addNextIntent(openExpressPassLocationsIntent);
		PendingIntent openExpressPassLocationsPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
		notificationBuilder.setContentIntent(openExpressPassLocationsPendingIntent);

		// API 16 (4.1) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(notificationBuilder)
			.setBigContentTitle(bigContentTitle)
			.bigText(bigText);

			notificationBuilder
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setStyle(bigTextStyle);
		}
		// API 21 (5.0) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			notificationBuilder.setCategory(NotificationCompat.CATEGORY_PROMO);
		}

		return notificationBuilder;
	}

	/**
	 * @param contentTitle
	 * @param contentText
	 * @param tickerText
	 * @param bigContentTitle
	 * @param bigText
	 * @param venueObjectJson
	 * @param poiObjectJson
	 * @param poiTypeId
	 * @param alertIdString
	 * @return
	 */
	public static NotificationCompat.Builder createAlertNotification(String contentTitle, String contentText, String tickerText, String bigContentTitle, String bigText,
			String venueObjectJson, String poiObjectJson, Integer poiTypeId, String alertIdString) {

		Context context = UniversalOrlandoApplication.getAppContext();
		// Build the notification to show
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_stat_alarm)
		.setOngoing(false)
		.setAutoCancel(true)
		.setContentTitle(contentTitle)
		.setContentText(contentText)
		.setSubText(context.getString(R.string.app_stat_sub_text))
		.setTicker(tickerText)
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
		.setWhen(System.currentTimeMillis());

		// If the notification is pressed, open the detail page
		Bundle detailActivityBundle = DetailUtils.getDetailPageBundle(venueObjectJson, poiObjectJson, poiTypeId, null);
		if (detailActivityBundle != null) {
			Intent detailActivityIntent = new Intent(context, DetailActivity.class).putExtras(detailActivityBundle);

			// Trick to get Android to treat each notification intent as unique, and not overwrite them
			detailActivityIntent.setAction(alertIdString);
			Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_HOME);
			Intent homeActivityIntent = new Intent(context, HomeActivity.class).putExtras(homeActivityBundle);

			// Stack the detail page on top of a home page
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context)
					.addNextIntent(homeActivityIntent)
					.addNextIntent(detailActivityIntent);
			PendingIntent detailActivityPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

			notificationBuilder.setContentIntent(detailActivityPendingIntent);
		}

		// API 16 (4.1) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(notificationBuilder)
			.setBigContentTitle(bigContentTitle)
			.bigText(bigText);

			notificationBuilder
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setStyle(bigTextStyle);
		}
		// API 21 (5.0) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			notificationBuilder.setCategory(NotificationCompat.CATEGORY_ALARM);
		}

		return notificationBuilder;
	}

	/**
	 * @param contentTitle
	 * @param contentText
	 * @param tickerText
	 * @param bigContentTitle
	 * @param bigText
	 * @param newsId
	 * @return
	 */
	public static NotificationCompat.Builder createNewsNotification(String contentTitle, String contentText, String tickerText, String bigContentTitle, String bigText,
			Long newsId) {

		Context context = UniversalOrlandoApplication.getAppContext();
		// Build the notification to show
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_stat_announcement)
		.setOngoing(false)
		.setAutoCancel(true)
		.setOnlyAlertOnce(true)
		.setContentTitle(contentTitle)
		.setContentText(contentText)
		.setSubText(context.getString(R.string.app_stat_sub_text))
		.setTicker(tickerText)
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
		.setWhen(System.currentTimeMillis());

		// If the notification is pressed, open the detail page
		Bundle detailActivityBundle = NewsDetailActivity.newInstanceBundle(newsId);
		Intent detailActivityIntent = new Intent(context, NewsDetailActivity.class).putExtras(detailActivityBundle);

		// Trick to get Android to treat each notification intent as unique, and not overwrite them
		detailActivityIntent.setAction(newsId.toString());

		// Create a home activity page at the news list page
		Bundle homeActivityBundle = HomeActivity.newInstanceBundle(HomeActivity.START_PAGE_TYPE_NEWS_LIST);
		Intent homeActivityIntent = new Intent(context, HomeActivity.class).putExtras(homeActivityBundle);

		// Stack the detail page on top of a home page
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context)
				.addNextIntent(homeActivityIntent)
				.addNextIntent(detailActivityIntent);
		PendingIntent detailActivityPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
		notificationBuilder.setContentIntent(detailActivityPendingIntent);

		// API 16 (4.1) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(notificationBuilder)
			.setBigContentTitle(bigContentTitle)
			.bigText(bigText);

			notificationBuilder
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setStyle(bigTextStyle);
		}
		// API 21 (5.0) and up
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			notificationBuilder.setCategory(NotificationCompat.CATEGORY_RECOMMENDATION);
		}

		return notificationBuilder;
	}
}
