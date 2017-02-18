
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.DailyHours;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Hours;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.HoursProvider;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Steven Byle
 */
public class HoursDetailFragment extends DatabaseQueryFragment {
	private static final String TAG = HoursDetailFragment.class.getSimpleName();

	private ViewGroup mDailyHoursContainer;
	private TextView mHoursText, mNotAvailableText, mHoursVaryText;

	public static HoursDetailFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		HoursDetailFragment fragment = new HoursDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_detail_hours, container, false);

		// Setup Views
		mDailyHoursContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_detail_daily_hours_container);
		mHoursText = (TextView) fragmentView.findViewById(R.id.fragment_detail_hours_text);
		mNotAvailableText = (TextView) fragmentView.findViewById(R.id.fragment_detail_hours_not_available_text);
		mHoursVaryText = (TextView) fragmentView.findViewById(R.id.fragment_detail_hours_vary_text);

		// Hide the views until the data loads
		mHoursText.setVisibility(View.INVISIBLE);
		mNotAvailableText.setVisibility(View.GONE);

		return fragmentView;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Assume there are no hours
				mHoursText.setVisibility(View.GONE);
				mNotAvailableText.setVisibility(View.VISIBLE);
				mHoursVaryText.setVisibility(View.GONE);

				// Read the single POI
				if (data != null && data.moveToFirst()) {
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

					// Look at the venue for WnW hours
					if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
						String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

						Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);
						List<VenueHours> venueHoursList = venue.getHours();

						// Get the current date
						Date now = new Date();
						long currentTimeInMs = now.getTime();

						// Format the current date into the date
						// portion of the service format ("2014-05-06" of
						// "2014-05-06T09:00:00-04:00"), in local park time
						SimpleDateFormat uoHoursDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
						uoHoursDateFormat.setTimeZone(DateTimeUtils.getParkTimeZone());
						String uoHoursFormattedDate = uoHoursDateFormat.format(now);

						// Go through the hours to see if it is currently in between any
						boolean foundHoursForToday = false;
						if (venueHoursList != null) {
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

								// If current time is in between the open hours, use those hours
								if (openTimeUnix * 1000 <= currentTimeInMs
										&& currentTimeInMs < closeTimeUnix * 1000) {
									mHoursText.setText(getFormattedParkHours(openTimeUnix * 1000, closeTimeUnix * 1000));
									foundHoursForToday = true;
									break;
								}
							}
						}

						// Otherwise, cycle through the hours again and find hours that open today
						if (!foundHoursForToday && venueHoursList != null) {
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

								// If the open time is the same date as today, use those hours
								if (openTimeString.contains(uoHoursFormattedDate)) {
									mHoursText.setText(getFormattedParkHours(openTimeUnix * 1000, closeTimeUnix * 1000));
									foundHoursForToday = true;
									break;
								}
							}
						}

						// If no suitable hours were found for this venue, hide the hours text
						if (!foundHoursForToday) {
							mHoursText.setVisibility(View.GONE);
							mNotAvailableText.setVisibility(View.VISIBLE);
							mHoursVaryText.setVisibility(View.GONE);
						}
						// Otherwise, make the hours visible
						else {
							mHoursText.setVisibility(View.VISIBLE);
							mNotAvailableText.setVisibility(View.GONE);
							mHoursVaryText.setVisibility(View.GONE);
						}
					}
					// Otherwise, if the POI is a dining or entertainment
					else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_DINING
							|| poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT) {

						String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
						PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

						// Set hours if they are there
						if (poi != null && poi instanceof HoursProvider) {
							HoursProvider hoursProvider = (HoursProvider) poi;

							List<DailyHoursItem> dailyHoursItems = new ArrayList<DailyHoursItem>();
							DailyHours dailyHours = hoursProvider.getDailyHours();
							if (dailyHours != null) {
								dailyHoursItems.add(getDailyHoursItem(Calendar.MONDAY, dailyHours.getMonday()));
								dailyHoursItems.add(getDailyHoursItem(Calendar.TUESDAY, dailyHours.getTuesday()));
								dailyHoursItems.add(getDailyHoursItem(Calendar.WEDNESDAY, dailyHours.getWednesday()));
								dailyHoursItems.add(getDailyHoursItem(Calendar.THURSDAY, dailyHours.getThursday()));
								dailyHoursItems.add(getDailyHoursItem(Calendar.FRIDAY, dailyHours.getFriday()));
								dailyHoursItems.add(getDailyHoursItem(Calendar.SATURDAY, dailyHours.getSaturday()));
								dailyHoursItems.add(getDailyHoursItem(Calendar.SUNDAY, dailyHours.getSunday()));
							}
							else {
								String openTime = hoursProvider.getOpenTime();
								String closeTime = hoursProvider.getCloseTime();
								Hours generalHours = new Hours(openTime, closeTime);

								dailyHoursItems.add(getDailyHoursItem(Calendar.MONDAY, generalHours));
								dailyHoursItems.add(getDailyHoursItem(Calendar.TUESDAY, generalHours));
								dailyHoursItems.add(getDailyHoursItem(Calendar.WEDNESDAY, generalHours));
								dailyHoursItems.add(getDailyHoursItem(Calendar.THURSDAY, generalHours));
								dailyHoursItems.add(getDailyHoursItem(Calendar.FRIDAY, generalHours));
								dailyHoursItems.add(getDailyHoursItem(Calendar.SATURDAY, generalHours));
								dailyHoursItems.add(getDailyHoursItem(Calendar.SUNDAY, generalHours));
							}

							List<DailyHoursItem> dailyHoursItemsCondensed = new ArrayList<>();
							DailyHoursItem firstDailyHoursItem = dailyHoursItems.get(0);
							String startDay = firstDailyHoursItem.getDayText();
							String endDay = firstDailyHoursItem.getDayText();
							String time = firstDailyHoursItem.getTimeText();

							boolean addHoursToList, addLastHoursToList;
							for (int i = 0; i < dailyHoursItems.size(); i++) {
								DailyHoursItem curDailyHoursItem = dailyHoursItems.get(i);

								// If the time text is the same as the previous day, keep iterating to the next day
								if (time != null && time.equals(curDailyHoursItem.getTimeText())) {
									endDay = curDailyHoursItem.getDayText();

									// Only add hours to the list if it's the last day
									addHoursToList = (i == (dailyHoursItems.size() - 1));
									addLastHoursToList = false;
								}
								// Otherwise, add the group of hours to the list
								else {
									addHoursToList = true;

									// Handle the last day as the end of a group
									addLastHoursToList = (i == (dailyHoursItems.size() - 1));
								}

								// If at the end of a group, add it to the list
								if (addHoursToList) {
									dailyHoursItemsCondensed.add(getFormattedDailyHoursItem(startDay, endDay, time));

									// Copy over the new different day
									startDay = curDailyHoursItem.getDayText();
									endDay = curDailyHoursItem.getDayText();
									time = curDailyHoursItem.getTimeText();

									// If there is one final group, add it
									if (addLastHoursToList) {
										dailyHoursItemsCondensed.add(getFormattedDailyHoursItem(startDay, endDay, time));
									}
								}
							}

							// Remove any old hours views
							mDailyHoursContainer.removeAllViews();

							// Just show the hours vary text
							mHoursText.setVisibility(View.GONE);
							mNotAvailableText.setVisibility(View.GONE);
							mHoursVaryText.setVisibility(View.VISIBLE);

							// Add the condensed hours as views to the layout
							if (dailyHoursItemsCondensed.size() == 1
									&& dailyHoursItemsCondensed.get(0).getTimeText().equalsIgnoreCase(HoursProvider.HOURS_VARIES)) {
								mDailyHoursContainer.setVisibility(View.GONE);
							} else {
								for (DailyHoursItem dailyHoursItem : dailyHoursItemsCondensed) {
									View hoursView = createHoursItemView(mDailyHoursContainer,
											dailyHoursItem.getDayText().toUpperCase(Locale.US),
											dailyHoursItem.getTimeText());
									mDailyHoursContainer.addView(hoursView);
								}
								mDailyHoursContainer.setVisibility(View.VISIBLE);
							}
						}
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				break;
		}
	}

	private static String getFormattedPoiHours(String hours) {
		Date hoursDate = Hours.parseHoursToDate(hours);
		if (hoursDate == null) {
			return hours.equalsIgnoreCase(HoursProvider.HOURS_VARIES) ? hours : null;
		}

		SimpleDateFormat sdfOutTime;
		if (DateTimeUtils.is24HourFormat()) {
			sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
		} else {
			// Format the hours to 12-hour ("1:30 PM"), park time
			sdfOutTime = new SimpleDateFormat("h:mma", Locale.US);
        }
		sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());

		return sdfOutTime.format(hoursDate);
	}

	private String getFormattedParkHours(Long openTimeMs, Long closeTimeMs) {

		String hoursFormatted = null;
		if (openTimeMs != null && closeTimeMs != null) {

			SimpleDateFormat sdfOutTimeNoMinutes;
			SimpleDateFormat sdfOutTimeWithMinutes;
			if (DateTimeUtils.is24HourFormat()) {
				// 24 hour format always shows minutes
				sdfOutTimeNoMinutes = new SimpleDateFormat("HH:mm", Locale.US);
				sdfOutTimeWithMinutes = new SimpleDateFormat("HH:mm", Locale.US);
			} else {
				// Format the park hours to 12-hour ("6PM" or "6:30PM"), park time
				sdfOutTimeNoMinutes = new SimpleDateFormat("ha", Locale.US);
				sdfOutTimeWithMinutes = new SimpleDateFormat("h:mma", Locale.US);
			}

			sdfOutTimeNoMinutes.setTimeZone(DateTimeUtils.getParkTimeZone());
			sdfOutTimeWithMinutes.setTimeZone(DateTimeUtils.getParkTimeZone());

			String openTimeFormatted = null;
			if (!DateTimeUtils.is24HourFormat() && (openTimeMs / (60 * 1000)) % 60 == 0) {
				openTimeFormatted = sdfOutTimeNoMinutes.format(new Date(openTimeMs));
			}
			else {
				openTimeFormatted = sdfOutTimeWithMinutes.format(new Date(openTimeMs));
			}

			String closeTimeFormatted = null;
			if (!DateTimeUtils.is24HourFormat() && (closeTimeMs / (60 * 1000)) % 60 == 0) {
				closeTimeFormatted = sdfOutTimeNoMinutes.format(new Date(closeTimeMs));
			}
			else {
				closeTimeFormatted = sdfOutTimeWithMinutes.format(new Date(closeTimeMs));
			}
			hoursFormatted = getString(R.string.detail_hours_range_two, openTimeFormatted, closeTimeFormatted);
		}
		return hoursFormatted;
	}

	private DailyHoursItem getFormattedDailyHoursItem(String startDay, String endDay, String time) {
		String dayText;
		if (startDay != null && endDay!= null && !startDay.equals(endDay)) {
			dayText = getString(R.string.detail_days_range_two, startDay, endDay);
		}
		else {
			dayText = startDay;
		}
		return new DailyHoursItem(dayText, time);
	}

	private DailyHoursItem getDailyHoursItem(int calendarDayOfWeek, Hours hours) {
		if (hours == null) {
			hours = new Hours(HoursProvider.HOURS_VARIES, HoursProvider.HOURS_VARIES);
		}

		// Get the day of the week text
		Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

		// Format the day of the week ("Mon", "Sun", etc)
		SimpleDateFormat sdfOutDayOfWeek = new SimpleDateFormat("EEE", Locale.US);
		sdfOutDayOfWeek.setTimeZone(DateTimeUtils.getParkTimeZone());
		String dayFormatted = sdfOutDayOfWeek.format(calendar.getTime());

		// Assume the time is varies
		DailyHoursItem dailyHoursItem = new DailyHoursItem(dayFormatted, getString(R.string.detail_hours_varies));

		String openTime = hours.getOpenTime();
		if (openTime == null || openTime.isEmpty()) {
			openTime = HoursProvider.HOURS_VARIES;
		}
		String closeTime = hours.getCloseTime();
		if (closeTime == null || closeTime.isEmpty()) {
			closeTime = HoursProvider.HOURS_VARIES;
		}

		// Parse open and close times
		String formattedOpenTime = getFormattedPoiHours(openTime);
		String formattedCloseTime = getFormattedPoiHours(closeTime);

		if (openTime.equalsIgnoreCase(HoursProvider.HOURS_VARIES)
				&& closeTime.equalsIgnoreCase(HoursProvider.HOURS_VARIES)) {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_varies));
		} else if (openTime.equalsIgnoreCase(HoursProvider.HOURS_WITH_PARK)
				&& closeTime.equalsIgnoreCase(HoursProvider.HOURS_WITH_PARK)) {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_with_park));
		} else if (openTime.equalsIgnoreCase(HoursProvider.HOURS_SEASONAL)
				&& closeTime.equalsIgnoreCase(HoursProvider.HOURS_SEASONAL)) {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_seasonal));
		} else if (openTime.equalsIgnoreCase(HoursProvider.HOURS_CLOSED)
				|| closeTime.equalsIgnoreCase(HoursProvider.HOURS_CLOSED)) {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_closed));
		} else if (formattedOpenTime != null
				&& closeTime.equalsIgnoreCase(HoursProvider.HOURS_WITH_PARK)) {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_open_time_close_with_park, formattedOpenTime));
		} else if (openTime.equalsIgnoreCase(HoursProvider.HOURS_WITH_PARK)
				&& formattedCloseTime != null) {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_open_with_park_to_close_time, formattedCloseTime));
		} else if (formattedOpenTime != null && formattedCloseTime != null) {
			if (formattedOpenTime.equalsIgnoreCase(HoursProvider.HOURS_VARIES) && formattedCloseTime.equalsIgnoreCase(HoursProvider.HOURS_VARIES)) {
				dailyHoursItem.setTimeText(getString(R.string.detail_hours_varies));
			} else if (formattedOpenTime.equals(formattedCloseTime)) {
				dailyHoursItem.setTimeText(formattedOpenTime);
			} else {
				dailyHoursItem.setTimeText(getString(R.string.detail_hours_range_two, formattedOpenTime, formattedCloseTime));
			}
		} else {
			dailyHoursItem.setTimeText(getString(R.string.detail_hours_varies));
		}

		return dailyHoursItem;
	}

	private static View createHoursItemView(ViewGroup parentViewGroup, String daysTextString, String hoursTextString) {
		Context context = parentViewGroup.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		ViewGroup hoursView = (ViewGroup) inflater.inflate(R.layout.hours_item, parentViewGroup, false);
		TextView daysText = (TextView) hoursView.findViewById(R.id.hours_item_days_text);
		TextView hoursText = (TextView) hoursView.findViewById(R.id.hours_item_times_text);

		if (daysTextString != null) {
			daysText.setText(daysTextString);
		}
		daysText.setVisibility(daysTextString != null ? View.VISIBLE : View.GONE);

		if (hoursTextString != null) {
			hoursText.setText(hoursTextString);
		}
		hoursText.setVisibility(hoursTextString != null ? View.VISIBLE : View.GONE);

		return hoursView;
	}

	private static class DailyHoursItem {
		private String dayText;
		private String timeText;

		/**
		 * @param dayText
		 * @param timeText
		 */
		public DailyHoursItem(String dayText, String timeText) {
			this.dayText = dayText;
			this.timeText = timeText;
		}

		/**
		 * @return the dayText
		 */
		public String getDayText() {
			return dayText;
		}

		/**
		 * @param dayText
		 *            the dayText to set
		 */
		public void setDayText(String dayText) {
			this.dayText = dayText;
		}

		/**
		 * @return the timeText
		 */
		public String getTimeText() {
			return timeText;
		}

		/**
		 * @param timeText
		 *            the timeText to set
		 */
		public void setTimeText(String timeText) {
			this.timeText = timeText;
		}
	}
}
