package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.alerts.Alert;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AlertsListCursorAdapter extends CursorAdapter implements StickyListHeadersAdapter {
	private static final String TAG = AlertsListCursorAdapter.class.getSimpleName();

	private static SparseIntArray sAlertHeaderStringResIdMap;
	private final String mImageSizeParam;
	private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private final Picasso mPicasso;

	public AlertsListCursorAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "AlertsCursorAdapter constructor");
		}

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
		mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_search_image_dpi_shift));

		mPicasso = new Picasso.Builder(context)
		.debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.list_alert_item, parent, false);

		AlertRowViewHolder holder = new AlertRowViewHolder(row);
		holder.displayNameText = (TextView) row.findViewById(R.id.list_alert_item_display_name_text);
		holder.intervalText = (TextView) row.findViewById(R.id.list_alert_item_interval_text);
		holder.thumbnailImageLayout = (ViewGroup) row.findViewById(R.id.list_alert_item_search_image_layout);
		holder.thumbnailImage = (ImageView) row.findViewById(R.id.list_alert_item_search_image);
		holder.thumbnailImageNoImage = (ImageView) row.findViewById(R.id.list_alert_item_no_image_logo);

		// Circle badge views
		holder.circleBadgeRootLayout = (LinearLayout) row.findViewById(R.id.poi_item_circle_badge_root_container);
		holder.waitTimeLayout = (LinearLayout) row.findViewById(R.id.poi_item_circle_badge_wait_time_layout);
		holder.waitTimeMinNumText = (TextView) row.findViewById(R.id.poi_item_circle_badge_wait_time_num_text);
		holder.showTimeLayout = (FrameLayout) row.findViewById(R.id.poi_item_circle_badge_show_time_layout);
		holder.showTimeBackgroundGray = row.findViewById(R.id.poi_item_circle_badge_show_time_background_gray);
		holder.showTimeBackgroundBlue = row.findViewById(R.id.poi_item_circle_badge_show_time_background_blue);
		holder.showTimeStartsTimeText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_starts_time_text);
		holder.showTimeStartsText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_starts_text);
		holder.showTimeOpensText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_opens_text);
		holder.showTimeAmPmText = (TextView) row.findViewById(R.id.poi_item_circle_badge_show_time_am_pm_text);
		holder.closedLayout = (LinearLayout) row.findViewById(R.id.poi_item_circle_badge_closed_layout);
		holder.closedText = (TextView) row.findViewById(R.id.poi_item_circle_badge_closed_text);
		holder.closedWeatherText = (TextView) row.findViewById(R.id.poi_item_circle_badge_weather_text);
		holder.closedTemporaryText = (TextView) row.findViewById(R.id.poi_item_circle_badge_temporary_text);
		holder.closedCapacityText = (TextView) row.findViewById(R.id.poi_item_circle_badge_closed_capacity_text);

		// Update the backgrounds to use a gray stroke, since the alerts page has a white background
		holder.waitTimeLayout.setBackgroundResource(R.drawable.shape_circle_badge_item_blue_with_gray_stroke);
		holder.showTimeBackgroundBlue.setBackgroundResource(R.drawable.shape_circle_badge_item_blue_with_gray_stroke);

		holder.closedLayout.setBackgroundResource(R.drawable.shape_circle_badge_item_gray_with_gray_stroke);
		holder.showTimeBackgroundGray.setBackgroundResource(R.drawable.shape_circle_badge_item_gray_with_gray_stroke);

		row.setTag(holder);
		return row;
	}

	@Override
	public void bindView(View row, final Context context, Cursor cursor) {

		// Get data from database row
		Integer alertTypeId = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
		String poiName = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_DISPLAY_NAME));
		String thumbnailImageUrl = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_THUMBNAIL_IMAGE_URL));

		// Apply data to view
		final AlertRowViewHolder holder = (AlertRowViewHolder) row.getTag();

		// Set the text
		holder.displayNameText.setText(poiName != null ? poiName : "");

		switch (alertTypeId) {
			case AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME:
			case AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN:
				Integer notifyThresholdInMin = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN));
				Integer waitTime = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
				String hoursListJson = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_HOURS_LIST_JSON));
				Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
				String poiObjectJson = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));

				// Only deviate to set the proper "alert set" text
				if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME) {
					notifyThresholdInMin = notifyThresholdInMin != null ? notifyThresholdInMin : 0;
					String waitTimeThreshold = context.getString(R.string.alerts_list_wait_time_threshold, notifyThresholdInMin);
					holder.intervalText.setText(waitTimeThreshold);
				}
				else if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN) {
					String whenRideOpens = context.getString(R.string.alerts_list_when_ride_opens);
					holder.intervalText.setText(whenRideOpens);
				}

				List<VenueHours> venueHoursList = null;
				if (hoursListJson != null) {
					try {
						venueHoursList = new Gson().fromJson(hoursListJson, new TypeToken<List<VenueHours>>() {}.getType());
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "bindView: exception trying to parse venue hours list from JSON", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}

				Ride ride = (Ride) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				String opensAt = ride.getOpensAt();

				PoiUtils.updatePoiCircleBadgeForRide(waitTime, opensAt, venueHoursList, holder);
				break;
			case AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME:
				String showTime = cursor.getString(cursor.getColumnIndex(AlertsTable.COL_SHOW_TIME));
				Integer notifyMinBefore = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_NOTIFY_MIN_BEFORE));

				String intervalText = AlertsUtils.convertNotifyMinBeforeToString(context.getResources(), notifyMinBefore);
				holder.intervalText.setText(intervalText != null ? intervalText.toUpperCase(Locale.US) : "");

				ArrayList<String> showTimesList = new ArrayList<String>();
				showTimesList.add(showTime);
				PoiUtils.updatePoiCircleBadgeForShow(showTimesList, holder, mContext);
				break;
			default:
				break;
		}

		// Set the visibility of the thumbnail image based on if the circle badge is visible
		boolean isCircleBadgeGone = holder.circleBadgeRootLayout.getVisibility() == View.GONE;
		holder.thumbnailImageLayout.setVisibility(isCircleBadgeGone ? View.VISIBLE : View.GONE);

		// Assume there is no image to start
		holder.thumbnailImage.setVisibility(View.GONE);
		holder.thumbnailImageNoImage.setVisibility(View.VISIBLE);

		// Load the search image
		if (thumbnailImageUrl != null && !thumbnailImageUrl.isEmpty()) {

			Uri thumbnailImageUri = null;
			try {
				thumbnailImageUri = Uri.parse(thumbnailImageUrl);
			}
			catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "bindView: invalid image URL: " + thumbnailImageUri, e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}

			if (thumbnailImageUri != null) {
				Uri imageUriToLoad = thumbnailImageUri.buildUpon()
						.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
						.build();

				if (BuildConfig.DEBUG) {
					Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
				}

				mPicasso.load(imageUriToLoad).into(holder.thumbnailImage, new AlertImageCallback(holder));
			}
		}
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_poi_section_header, parent, false);

			holder = new HeaderViewHolder();
			holder.headerTitleText = (TextView) convertView.findViewById(R.id.list_poi_section_header_title_text);
			convertView.setTag(holder);
		}
		else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		Cursor cursor = (Cursor) getItem(position);
		Integer alertTypeId = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));

		// Get the proper header title, fall back to rides if it can't find a title
		int alertHeaderStringResId = getAlertHeaderStringResId(alertTypeId);
		holder.headerTitleText.setText(alertHeaderStringResId);

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		Cursor cursor = (Cursor) getItem(position);
		Integer alertTypeId = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));

		// Get the proper header title, fall back to rides if it can't find a title
		int alertHeaderStringResId = getAlertHeaderStringResId(alertTypeId);
		return alertHeaderStringResId;
	}

	public void destroy() {
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
	}

	public static Alert getAlertFromCursor(Cursor cursor) {
		if (cursor == null || cursor.isClosed()) {
			return null;
		}

		Alert alert = null;

		// Get data from database row
		Integer alertTypeId = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));
		String poiName = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_DISPLAY_NAME));
		Long poiId = cursor.getLong(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_ID));

		// Build the proper alert
		switch (alertTypeId) {
			case AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME:
				Integer notifyThresholdInMin = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN));
				notifyThresholdInMin = notifyThresholdInMin != null ? notifyThresholdInMin : 0;
				alert = new WaitTimeAlert(poiId, poiName, notifyThresholdInMin);
				break;
			case AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN:
				alert = new RideOpenAlert(poiId, poiName);
				break;
			case AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME:
				String showTime = cursor.getString(cursor.getColumnIndex(AlertsTable.COL_SHOW_TIME));
				Integer notifyMinBefore = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_NOTIFY_MIN_BEFORE));
				alert = new ShowTimeAlert(poiId, poiName, notifyMinBefore, showTime);
				break;
			default:
				break;
		}

		return alert;
	}

	private static int getAlertHeaderStringResId(int alertTypeId) {
		if (sAlertHeaderStringResIdMap == null) {
			sAlertHeaderStringResIdMap = new SparseIntArray();
			sAlertHeaderStringResIdMap.put(AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME, R.string.alerts_list_header_wait_time);
			sAlertHeaderStringResIdMap.put(AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN, R.string.alerts_list_header_ride_opening);
			sAlertHeaderStringResIdMap.put(AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME, R.string.alerts_list_header_shows);
		}

		return sAlertHeaderStringResIdMap.get(alertTypeId, R.string.alerts_list_header_wait_time);
	}

	private static class HeaderViewHolder {
		TextView headerTitleText;
	}

	private static class AlertRowViewHolder extends PoiViewHolder {
		TextView intervalText;
		ViewGroup thumbnailImageLayout;
		ImageView thumbnailImage;
		ImageView thumbnailImageNoImage;

		public AlertRowViewHolder(View itemView) {
			super(itemView);
		}
	}

	// Private static class using weak references to prevent leaking a context
	private static class AlertImageCallback implements Callback {
		private final WeakReference<AlertRowViewHolder> mAlertRowViewHolder;

		public AlertImageCallback(AlertRowViewHolder alertRowViewHolder) {
			mAlertRowViewHolder = new WeakReference<AlertRowViewHolder>(alertRowViewHolder);
		}

		@Override
		public void onSuccess() {
			AlertRowViewHolder holder = mAlertRowViewHolder.get();
			if (holder != null) {
				if (holder.thumbnailImage != null) {
					holder.thumbnailImage.setVisibility(View.VISIBLE);
				}
				if (holder.thumbnailImageNoImage != null) {
					holder.thumbnailImageNoImage.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void onError() {
			AlertRowViewHolder holder = mAlertRowViewHolder.get();
			if (holder != null) {
				if (holder.thumbnailImage != null) {
					holder.thumbnailImage.setVisibility(View.GONE);
				}
				if (holder.thumbnailImageNoImage != null) {
					holder.thumbnailImageNoImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
