
package com.universalstudios.orlandoresort.controller.userinterface.hours;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.accessibility.AccessibilityUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.venue.GetVenueHoursRequest;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueHoursTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.calendar.CalendarPickerView;
import com.universalstudios.orlandoresort.view.calendar.CalendarPickerView.OnDateSelectedListener;
import com.universalstudios.orlandoresort.view.calendar.CalendarPickerView.SelectionMode;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class HoursCalendarFragment extends DatabaseQueryFragment implements OnDateSelectedListener {
	private static final String TAG = HoursCalendarFragment.class.getSimpleName();

	private TextView mIoaTimeText;
	private TextView mUsfTimeText;
	private TextView mCwoTimeText;
	private TextView mWnwTimeText;
	private TextView mUshTimeText;
	private TextView mCwhTimeText;
	private ViewGroup mIoaTimeContainer;
	private ViewGroup mUsfTimeContainer;
	private ViewGroup mCwTimeContainer;
	private ViewGroup mWnwTimeContainer;
	private ViewGroup mOrlandoLocationsContainer;
	private ViewGroup mHollywoodLocationsContainer;
	private Date mSelectedDate;
	private CalendarPickerView mCalendar;
	private HashMap<String, VenueHours> mVenueHoursMap;

	public static HoursCalendarFragment newInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		HoursCalendarFragment fragment = new HoursCalendarFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Get the venue hours for the parks
		DatabaseQuery databaseQuery;
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			databaseQuery = DatabaseQueryUtils.getVenueHoursDatabaseQuery(
					VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD,
					VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD);
		} else {
			databaseQuery = DatabaseQueryUtils.getVenueHoursDatabaseQuery(
					VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE,
					VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA,
					VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO,
					VenuesTable.VAL_VENUE_ID_WET_N_WILD);
		}

		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}

		// Add parameters to the argument bundle
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		mVenueHoursMap = new HashMap<String, VenueHours>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_hours_calendar, container, false);

		// Setup Views
		mCalendar = (CalendarPickerView) fragmentView.findViewById(R.id.fragment_hours_calendar_picker);
		mIoaTimeText = (TextView) fragmentView.findViewById(R.id.fragment_hours_calendar_ioa_time_text);
		mUsfTimeText = (TextView) fragmentView.findViewById(R.id.fragment_hours_calendar_usf_time_text);
		mCwoTimeText = (TextView) fragmentView.findViewById(R.id.fragment_hours_calendar_cw_time_text);
		mWnwTimeText = (TextView) fragmentView.findViewById(R.id.fragment_hours_calendar_wnw_time_text);
		mUshTimeText = (TextView) fragmentView.findViewById(R.id.fragment_hours_calendar_ush_time_text);
		mCwhTimeText = (TextView) fragmentView.findViewById(R.id.fragment_hours_calendar_citywalk_hollywood_time_text);
		mIoaTimeContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_calendar_ioa_time_container);
		mUsfTimeContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_calendar_usf_time_container);
		mCwTimeContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_calendar_cw_time_container);
		mWnwTimeContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_calendar_wnw_time_container);

		mOrlandoLocationsContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_calendar_orlando_locations_container);
		mHollywoodLocationsContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_calendar_hollywood_locations_container);

		if (BuildConfigUtils.isLocationFlavorHollywood()){
			mOrlandoLocationsContainer.setVisibility(View.GONE);
			mHollywoodLocationsContainer.setVisibility(View.VISIBLE);
		}else {
			mOrlandoLocationsContainer.setVisibility(View.VISIBLE);
			mHollywoodLocationsContainer.setVisibility(View.GONE);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

		// Setup the calendar
		mCalendar.setOnDateSelectedListener(this);

		Calendar now = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		Calendar sixMonthsLater = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
		sixMonthsLater.add(Calendar.MONTH, 6);

		mCalendar.init(now.getTime(), sixMonthsLater.getTime(), Locale.US)
		.inMode(SelectionMode.SINGLE)
		.withSelectedDate(now.getTime());

		// Default the hours to the current days selection
		mSelectedDate = mCalendar.getSelectedDate();
		updateParkHoursViews();

		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public void onDateSelected(Date date) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDateSelected: " + date.toString());
		}

		// Update the hours views
		mSelectedDate = date;
		updateParkHoursViews();

		// If TalkBack is on, focus to the park hours views after selecting a new date to call out the
		if (AccessibilityUtils.isTalkBackEnabled()) {
			mIoaTimeContainer.requestFocus();

			AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_VIEW_FOCUSED);
			event.setSource(mIoaTimeContainer);
			event.setClassName(mIoaTimeContainer.getClass().getName());
			event.setPackageName(mIoaTimeContainer.getContext().getPackageName());
			event.setEnabled(true);
			event.setContentDescription(getString(R.string.splash_loading_content_description));
			mIoaTimeContainer.sendAccessibilityEventUnchecked(event);
		}
	}

	@Override
	public void onDateUnselected(Date date) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDateUnselected: " + date.toString());
		}
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

				// Wipe the map for new data
				mVenueHoursMap.clear();

				// Put the venue hours into a map
				if (data != null && data.moveToFirst()) {
					do {
						Long venueId = data.getLong(data.getColumnIndex(VenueHoursTable.COL_VENUE_ID));
						Long dateInMillis = data.getLong(data.getColumnIndex(VenueHoursTable.COL_DATE_IN_MILLIS));
						Long openDateInMillis = data.getLong(data.getColumnIndex(VenueHoursTable.COL_OPEN_DATE_IN_MILLIS));
						Long closeDateInMillis = data.getLong(data.getColumnIndex(VenueHoursTable.COL_CLOSE_DATE_IN_MILLIS));

						if (venueId != null && dateInMillis != null
								&& openDateInMillis != null	&& closeDateInMillis != null) {
							// Create a temp venue hours object to put in the map
							VenueHours venueHours = new VenueHours(null, openDateInMillis / 1000, null, closeDateInMillis / 1000);

							mVenueHoursMap.put(getVenueHoursMapKey(dateInMillis, venueId), venueHours);
						}
					}
					while (data.moveToNext());
				}

				// Update the hours views in case the data has changed
				updateParkHoursViews();
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

	private static String getVenueHoursMapKey(Long startOfDayInMillis, Long venueId) {
		return new StringBuilder().append(startOfDayInMillis).append("_").append(venueId).toString();
	}

	private void updateParkHoursViews() {
		if (mSelectedDate == null) {
			return;
		}

		long startOfDayInMillis = GetVenueHoursRequest.getStartOfDayInMillis(mSelectedDate.getTime());

		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			updateParkHoursTextView(startOfDayInMillis, VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD, mUshTimeText);
			updateParkHoursTextView(startOfDayInMillis, VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD, mCwhTimeText);
		} else {
			updateParkHoursTextView(startOfDayInMillis, VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE, mIoaTimeText);
			updateParkHoursTextView(startOfDayInMillis, VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA, mUsfTimeText);
			updateParkHoursTextView(startOfDayInMillis, VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO, mCwoTimeText);
			updateParkHoursTextView(startOfDayInMillis, VenuesTable.VAL_VENUE_ID_WET_N_WILD, mWnwTimeText);
		}

	}

	private void updateParkHoursTextView(Long startOfDayInMillis, Long venueId, TextView timeText) {
		if (startOfDayInMillis == null || venueId == null) {
			timeText.setText(getString(R.string.hours_calendar_not_available));
			return;
		}

		VenueHours venueHours = mVenueHoursMap.get(getVenueHoursMapKey(startOfDayInMillis, venueId));
		if (venueHours == null) {
			timeText.setText(getString(R.string.hours_calendar_not_available));
			return;
		}

		Long openTimeMs = venueHours.getOpenTimeUnix() * 1000;
		Long closeTimeMs = venueHours.getCloseTimeUnix() * 1000;

		SimpleDateFormat sdfOutTimeNoMinutes;
		SimpleDateFormat sdfOutTimeWithMinutes;
		String closeTimeFormatted;
		String openTimeFormatted;

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

		if (!DateTimeUtils.is24HourFormat() && (openTimeMs / (60 * 1000)) % 60 == 0) {
			openTimeFormatted = sdfOutTimeNoMinutes.format(new Date(openTimeMs));
		} else {
			openTimeFormatted = sdfOutTimeWithMinutes.format(new Date(openTimeMs));
		}

		if (!DateTimeUtils.is24HourFormat() && (closeTimeMs / (60 * 1000)) % 60 == 0) {
			closeTimeFormatted = sdfOutTimeNoMinutes.format(new Date(closeTimeMs));
		} else {
			closeTimeFormatted = sdfOutTimeWithMinutes.format(new Date(closeTimeMs));
		}
		String hoursFormatted = openTimeFormatted + " - " + closeTimeFormatted;

		// Set the formatted time text
		timeText.setText(hoursFormatted);
	}
}
