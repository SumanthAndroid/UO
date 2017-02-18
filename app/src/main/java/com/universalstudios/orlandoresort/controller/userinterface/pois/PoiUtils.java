/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.pois;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class PoiUtils {
	private static final String TAG = PoiUtils.class.getSimpleName();

	public final static int WAIT_TIME_STALE_THRESHOLD_IN_SEC = 1 * 60 * 60; // 1 hour
    public static final String DATE_FORMAT_SHOW_TIME = "HH:mm:ss";
    public static final String REGEX_SHOW_TIME = "^(\\d\\d:\\d\\d:\\d\\d)$";

	/**
	 * Shared method to apply circle badge UI logic for rides.
	 *
	 * @param waitTime
	 * @param opensAt
	 * @param venueHoursList
	 * @param poiViewHolder
	 */
	public static void updatePoiCircleBadgeForRide(Integer waitTime, String opensAt,
			List<VenueHours> venueHoursList, PoiViewHolder poiViewHolder) {

        // There must be a valid wait time
		if (waitTime == null) {
			poiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
			poiViewHolder.circleBadgeRootLayout.setContentDescription("");
			return;
		}

		// Build the content description as the views are shown
		StringBuilder contentDescriptionBuilder = new StringBuilder();

		// Set the wait time, if valid and was refreshed within the last hour
		Context context = poiViewHolder.circleBadgeRootLayout.getContext();
		UniversalAppState uoState = UniversalAppStateManager.getInstance();
		if (waitTime > 0 && UniversalAppStateManager.hasSyncedInTheLast(
				uoState.getDateOfLastPoiSyncInMillis(), WAIT_TIME_STALE_THRESHOLD_IN_SEC)) {
			poiViewHolder.waitTimeMinNumText.setText("" + waitTime);

			poiViewHolder.waitTimeLayout.setVisibility(View.VISIBLE);
			poiViewHolder.showTimeLayout.setVisibility(View.GONE);
			poiViewHolder.closedLayout.setVisibility(View.GONE);
			poiViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);

			// Set the content description based on the wait time
			contentDescriptionBuilder.append(
					context.getString(R.string.poi_circle_badge_min_wait_formatted_content_description, waitTime));
		}
		// Otherwise, set the status based on the special wait time value
		else {
			switch (waitTime) {
				case Ride.RIDE_WAIT_TIME_STATUS_OUT_OF_OPERATING_HOURS:
					// Parse in the opens at time, if it exists
					Long nextOpenTimeInMs = null;
					if (opensAt != null && !opensAt.isEmpty()) {
						SimpleDateFormat sdfInFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
						try {
							nextOpenTimeInMs = sdfInFormat.parse(opensAt).getTime();
						}
						catch (ParseException e) {
							// Log the exception to crittercism
							Crittercism.logHandledException(e);
						}
					}

					// If it is out of operating hours, and there is a next open
					// time, show the next time the ride will be open
					if (nextOpenTimeInMs != null) {
						Date nextOpenTimeDate = new Date(nextOpenTimeInMs);
						String formattedOpenTime;
						String formattedOpenTimeAmPm;

						if (DateTimeUtils.is24HourFormat()) {
							// Format the show time to 24-hour, park time
							SimpleDateFormat sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
							sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
							formattedOpenTime = sdfOutTime.format(nextOpenTimeDate);
							formattedOpenTimeAmPm = "";
						} else {
							// Format the show time to 12-hour, park time
							SimpleDateFormat sdfOutTime = new SimpleDateFormat("h:mm", Locale.US);
							sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
							formattedOpenTime = sdfOutTime.format(nextOpenTimeDate);

							// Last, format the AM/PM portion, park time
							SimpleDateFormat sdfOutTimeAmPm = new SimpleDateFormat("a", Locale.US);
							sdfOutTimeAmPm.setTimeZone(DateTimeUtils.getParkTimeZone());
							formattedOpenTimeAmPm = sdfOutTimeAmPm.format(nextOpenTimeDate);
						}

						// Apply the open time to the badge
						poiViewHolder.showTimeStartsTimeText.setText(formattedOpenTime);
						poiViewHolder.showTimeAmPmText.setText(formattedOpenTimeAmPm);
						poiViewHolder.showTimeAmPmText.setVisibility(!TextUtils.isEmpty(formattedOpenTimeAmPm) ? View.VISIBLE : View.GONE);

						// AM/PM text is reused for show times and opens at states
						poiViewHolder.showTimeAmPmText.setTextColor(ContextCompat.getColor(context, R.color.badge_closed_am_pm));

						// Toggle badge visibility
						poiViewHolder.showTimeOpensText.setVisibility(View.VISIBLE);
						poiViewHolder.showTimeStartsText.setVisibility(View.GONE);
						poiViewHolder.showTimeBackgroundGray.setVisibility(View.VISIBLE);
						poiViewHolder.showTimeBackgroundBlue.setVisibility(View.GONE);
						poiViewHolder.showTimeLayout.setVisibility(View.VISIBLE);
						poiViewHolder.waitTimeLayout.setVisibility(View.GONE);
						poiViewHolder.closedLayout.setVisibility(View.GONE);
						poiViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);

						contentDescriptionBuilder.append(poiViewHolder.showTimeOpensText.getText())
						.append(" ")
						.append(poiViewHolder.showTimeStartsTimeText.getText())
						.append(" ")
						.append(poiViewHolder.showTimeAmPmText.getText());
					}
					// If there is no next open time, hide the badge
					else {
						//TODO what should we show if an event is closed
						poiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
					}
					break;
                case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_LONG_TERM:
                    // Per the business, closed long term hides the bubble
//                    poiViewHolder.circleBadgeRootLayout.setVisibilitty(View.GONE);
                    //Intentional dropthrough
				case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_TEMP:
					poiViewHolder.closedText.setVisibility(View.VISIBLE);
					poiViewHolder.closedWeatherText.setVisibility(View.GONE);
					// Per the business, "closed temporary" just shows "closed" now
					poiViewHolder.closedTemporaryText.setVisibility(View.GONE);
					poiViewHolder.closedCapacityText.setVisibility(View.GONE);
					poiViewHolder.closedLayout.setVisibility(View.VISIBLE);
					poiViewHolder.waitTimeLayout.setVisibility(View.GONE);
					poiViewHolder.showTimeLayout.setVisibility(View.GONE);
					poiViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);

					contentDescriptionBuilder.append(poiViewHolder.closedText.getText());
					break;

				case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_INCLEMENT_WEATHER:
					poiViewHolder.closedText.setVisibility(View.VISIBLE);
					poiViewHolder.closedWeatherText.setVisibility(View.VISIBLE);
					poiViewHolder.closedTemporaryText.setVisibility(View.GONE);
					poiViewHolder.closedCapacityText.setVisibility(View.GONE);
					poiViewHolder.closedLayout.setVisibility(View.VISIBLE);
					poiViewHolder.waitTimeLayout.setVisibility(View.GONE);
					poiViewHolder.showTimeLayout.setVisibility(View.GONE);
					poiViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);

					contentDescriptionBuilder.append(poiViewHolder.closedText.getText())
					.append(" ")
					.append(poiViewHolder.closedWeatherText.getText());
					break;
				case Ride.RIDE_WAIT_TIME_STATUS_CLOSED_FOR_CAPACITY:
					poiViewHolder.closedText.setVisibility(View.GONE);
					poiViewHolder.closedWeatherText.setVisibility(View.GONE);
					poiViewHolder.closedTemporaryText.setVisibility(View.GONE);
					poiViewHolder.closedCapacityText.setVisibility(View.VISIBLE);
					poiViewHolder.closedLayout.setVisibility(View.VISIBLE);
					poiViewHolder.waitTimeLayout.setVisibility(View.GONE);
					poiViewHolder.showTimeLayout.setVisibility(View.GONE);
					poiViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);

					contentDescriptionBuilder.append(poiViewHolder.closedCapacityText.getText());
					break;
				case Ride.RIDE_WAIT_TIME_STATUS_NOT_AVAILABLE:
					poiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
					break;
				default:
					poiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);
					break;
			}
		}

		// Set the proper content description, if the badge is shown
		if (poiViewHolder.circleBadgeRootLayout.getVisibility() == View.VISIBLE) {
			poiViewHolder.circleBadgeRootLayout.setContentDescription(contentDescriptionBuilder.toString());
		}
		else {
			poiViewHolder.circleBadgeRootLayout.setContentDescription("");
		}
	}

	/**
	 * Shared method to apply circle badge UI logic for shows.
	 * 
	 * @param showTimesList
	 * @param poiViewHolder
	 */
	public static void updatePoiCircleBadgeForShow(List<String> showTimesList, PoiViewHolder poiViewHolder, Context context) {

		// There must be valid show times, and they must have been synced today
		UniversalAppState uoState = UniversalAppStateManager.getInstance();
		boolean hasSyncedPoisToday = UniversalAppStateManager.hasSyncedToday(uoState.getDateOfLastPoiSyncInMillis());
		if (showTimesList == null || showTimesList.size() == 0 || !hasSyncedPoisToday) {
			poiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

			// Wipe the content description
			poiViewHolder.circleBadgeRootLayout.setContentDescription("");
			return;
		}

		// Sort the time list to put the shows in ascending order (earlier to latest)
		Collections.sort(showTimesList);
		for(String showTime : showTimesList) {
			if (showTime == null) {
				continue;
			}

			// First, parse in the show time from 24 hour ("14:30:00"), park time
			SimpleDateFormat uoTwentyFourHourFormat = new SimpleDateFormat("HH:mm", Locale.US);
			uoTwentyFourHourFormat.setTimeZone(DateTimeUtils.getParkTimeZone());

			Date showTimeDate = null;
			try {
				showTimeDate = uoTwentyFourHourFormat.parse(showTime);
			}
			catch (ParseException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "updatePoiCircleBadgeForShow: exception trying to parse in show time", e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
				continue;
			}

			// Then, format out the current 24 hour time ("14:30:00")
			String now = uoTwentyFourHourFormat.format(new Date());

			// If the current local time is past the show time, try the next show time
			if (now.compareTo(showTime) > 0) {
				continue;
			}

			String formattedShowTime;
			String formattedShowTimeAmPm;
			if (DateTimeUtils.is24HourFormat()) {
				// Format the show time to 24-hour, park time
				SimpleDateFormat sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
				sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
				formattedShowTime = sdfOutTime.format(showTimeDate);
				formattedShowTimeAmPm = "";
			} else {
				// Format the show time to 12-hour, park time
				SimpleDateFormat sdfOutTime = new SimpleDateFormat("h:mm", Locale.US);
				sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
				formattedShowTime = sdfOutTime.format(showTimeDate);

				// Last, format the AM/PM portion, park time
				SimpleDateFormat sdfOutTimeAmPm = new SimpleDateFormat("a", Locale.US);
				sdfOutTimeAmPm.setTimeZone(DateTimeUtils.getParkTimeZone());
				formattedShowTimeAmPm = sdfOutTimeAmPm.format(showTimeDate);
			}

			// Apply the show time to the badge
			poiViewHolder.showTimeStartsTimeText.setText(formattedShowTime);
			poiViewHolder.showTimeAmPmText.setText(formattedShowTimeAmPm);
			poiViewHolder.showTimeAmPmText.setVisibility(!TextUtils.isEmpty(formattedShowTimeAmPm) ? View.VISIBLE : View.GONE);

			// AM/PM text is reused for show times and opens at states
			poiViewHolder.showTimeAmPmText.setTextColor(ContextCompat.getColor(context, R.color.show_time_badge_am_pm));

			// Toggle badge visibility
			poiViewHolder.waitTimeLayout.setVisibility(View.GONE);
			poiViewHolder.closedLayout.setVisibility(View.GONE);
			poiViewHolder.showTimeOpensText.setVisibility(View.GONE);
			poiViewHolder.showTimeStartsText.setVisibility(View.VISIBLE);
			poiViewHolder.showTimeBackgroundGray.setVisibility(View.GONE);
			poiViewHolder.showTimeBackgroundBlue.setVisibility(View.VISIBLE);
			poiViewHolder.showTimeLayout.setVisibility(View.VISIBLE);
			poiViewHolder.circleBadgeRootLayout.setVisibility(View.VISIBLE);

			// Set the content description
			StringBuilder contentDescriptionBuilder = new StringBuilder();
			contentDescriptionBuilder.append(poiViewHolder.showTimeStartsText.getText())
			.append(" ")
			.append(formattedShowTime)
			.append(" ")
			.append(formattedShowTimeAmPm);
			poiViewHolder.circleBadgeRootLayout.setContentDescription(contentDescriptionBuilder.toString());
			return;
		}

		// If there are no more show times today, hide the badge
		poiViewHolder.circleBadgeRootLayout.setVisibility(View.GONE);

		// Wipe the content description
		poiViewHolder.circleBadgeRootLayout.setContentDescription("");
	}
	
	public static void updateGuideMeButton(boolean isInPark, boolean isRoutable, PoiViewHolder poiViewHolder) {

		if (isInPark && isRoutable) {
			poiViewHolder.guideMeDivider.setVisibility(View.VISIBLE);
			poiViewHolder.guideMeLayout.setVisibility(View.VISIBLE);
		}
		else {
			poiViewHolder.guideMeDivider.setVisibility(View.GONE);
			poiViewHolder.guideMeLayout.setVisibility(View.GONE);
		}
	}

	public static boolean isDuringVenueHours(long timeInMs, List<VenueHours> venueHoursList) {
		if (venueHoursList == null) {
			return false;
		}

		// Go through the hours to see if it is currently in between any
		for (VenueHours venueHours : venueHoursList) {
			if (venueHours == null) {
				continue;
			}

			Long openTimeUnix = venueHours.getOpenTimeUnix();
			Long closeTimeUnix = venueHours.getCloseTimeUnix();
			if (openTimeUnix == null || openTimeUnix == 0
					|| closeTimeUnix == null || closeTimeUnix == 0) {
				continue;
			}

			// If time is in between the open hours, the venue is open
			if (openTimeUnix * 1000 <= timeInMs
					&& timeInMs < closeTimeUnix * 1000) {
				return true;
			}
		}
		return false;
	}

	public static Long getNextOpenTimeInMs(long currentTimeInMs, List<VenueHours> venueHoursList) {
		if (venueHoursList == null) {
			return null;
		}

		// Cycle through the hours and find the (closest) next open time
		Long curNextOpenTimeInMs = null;
		long curNextOpenTimeDiffInMs = Long.MAX_VALUE;
		for (VenueHours venueHours : venueHoursList) {
			if (venueHours == null) {
				continue;
			}

			Long openTimeUnix = venueHours.getOpenTimeUnix();
			Long closeTimeUnix = venueHours.getCloseTimeUnix();
			String openTimeString = venueHours.getOpenTimeString();
			if (openTimeUnix == null || openTimeUnix == 0
					|| closeTimeUnix == null || closeTimeUnix == 0
					|| openTimeString == null || openTimeString.isEmpty()) {
				continue;
			}

			// If the open time is later than now, and closer than the current next open time, copy that
			long opentTimeInMs = openTimeUnix * 1000;
			long opentTimeDiffInMs = opentTimeInMs - currentTimeInMs;
			if (opentTimeInMs > currentTimeInMs && opentTimeDiffInMs < curNextOpenTimeDiffInMs) {
				curNextOpenTimeInMs = opentTimeInMs;
				curNextOpenTimeDiffInMs = opentTimeDiffInMs;
			}
		}

		if (curNextOpenTimeInMs != null) {
			return curNextOpenTimeInMs;
		}
		else {
			return null;
		}
	}

	public static boolean isFavoriteEnabled(int poiTypeId) {
		switch (poiTypeId) {
			case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT:
			case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
				return true;
			default:
				return false;
		}
	}

	public static void updatePoiIsFavoriteInDatabase(final Context context, final PointOfInterest poi, final boolean isFavorite, boolean async) {
		if (context == null || poi == null) {
			return;
		}

		// Update the POI
		poi.setIsFavorite(isFavorite);

		final ContentValues contentValues = new ContentValues();
		contentValues.put(PointsOfInterestTable.COL_IS_FAVORITE, poi.getIsFavorite());
		contentValues.put(PointsOfInterestTable.COL_POI_HASH_CODE, poi.hashCode());
		contentValues.put(PointsOfInterestTable.COL_POI_OBJECT_JSON, poi.toJson());

		final String selection = new StringBuilder(PointsOfInterestTable.COL_POI_ID)
		.append(" = '").append(poi.getId()).append("'")
		.toString();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// Try to update the item
					int itemsUpdated = context.getContentResolver().update(UniversalOrlandoContentUris.POINTS_OF_INTEREST, contentValues, selection, null);

					// If the item doesn't exist, log it
					if (itemsUpdated == 0) {
						if (BuildConfig.DEBUG) {
							Log.w(TAG, "updateNewsHasBeenReadInDatabase: news item does no exist in the database");
						}
					}
					// Otherwise, track the update
					else {
						// Track the event
						String eventName = isFavorite ? AnalyticsUtils.EVENT_NAME_FAVORITE : AnalyticsUtils.EVENT_NAME_UNFAVORITE;
						Integer eventNum = isFavorite ? AnalyticsUtils.EVENT_NUM_FAVORITE : AnalyticsUtils.EVENT_NUM_UNFAVORITE;
						AnalyticsUtils.trackEvent(
								poi.getDisplayName(),
								eventName,
								eventNum,
								null);
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "updatePoiIsFavoriteInDatabase: exception saving to database", e);
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
	 *  Retrieve minutes before show time.
	 * @return
	 */
	public static int getMinutesBeforeShowTime(String showTimeString) {
	    // Initialize show time date formatter
        SimpleDateFormat showTimeDateFormat = new SimpleDateFormat(DATE_FORMAT_SHOW_TIME, Locale.US);
		showTimeDateFormat.setTimeZone(DateTimeUtils.getParkTimeZone());
	    
	    try {
            Date showTime = showTimeDateFormat.parse(showTimeString);
            Calendar showTimeCalendar = new GregorianCalendar(DateTimeUtils.getParkTimeZone(), Locale.US);
            Calendar nowCalendar = new GregorianCalendar(DateTimeUtils.getParkTimeZone(), Locale.US);
            showTimeCalendar.setTime(showTime);
            showTimeCalendar.set(nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH),
                    nowCalendar.get(Calendar.DAY_OF_MONTH));
            long millisUntilStart = showTimeCalendar.getTimeInMillis() - nowCalendar.getTimeInMillis();
            int minutesUntilStart = (int) (millisUntilStart / (1000L * 60L));
            if(BuildConfig.DEBUG) {
                Log.v(TAG, "getMinutesBeforeShowTime: minutesUntilStart = " + minutesUntilStart);
            }
            return minutesUntilStart;
        }
        catch (ParseException e) {
             // Show time not in the correct format
            if(BuildConfig.DEBUG) {
                Log.e(TAG, "getMinutesBeforeShowTime: showTime in the incorrect format = " + showTimeString, e);
            }
        }
	    
	    return -1;
	}
	
}
