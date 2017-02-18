/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.SetAlertActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ShowTimeListDetailFragment extends DatabaseQueryFragment implements OnClickListener {
	private static final String TAG = ShowTimeListDetailFragment.class.getSimpleName();

	private static final String KEY_ARG_POI_ID = "KEY_ARG_POI_ID";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID_ALERTS = LoaderUtils.LOADER_ID_FEATURE_LIST_DETAIL_FRAGMENT;

	private LinearLayout mShowTimeLayout;
	private HorizontalScrollView mHorizontalScrollView;
	private TextView mBottomText;
	private View mBottomDivider;
	private Long mPoiId;
	private String mPoiName;
	private HashMap<String, ShowTimeAlert> mShowTimeAlerts;
	private HashMap<String, View> mShowTimeButtons;

	public static ShowTimeListDetailFragment newInstance(DatabaseQuery databaseQuery, long poiId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		ShowTimeListDetailFragment fragment = new ShowTimeListDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		args.putLong(KEY_ARG_POI_ID, poiId);
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
			mPoiId = null;
		}
		// Otherwise, set incoming parameters
		else {
			mPoiId = args.getLong(KEY_ARG_POI_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		mShowTimeAlerts = new HashMap<String, ShowTimeAlert>();
		mShowTimeButtons = new HashMap<String, View>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_detail_show_time_list, container, false);

		// Setup Views
		mShowTimeLayout = (LinearLayout) fragmentView.findViewById(R.id.fragment_detail_show_time_layout);
		mHorizontalScrollView = (HorizontalScrollView) fragmentView.findViewById(R.id.fragment_detail_show_time_scrollview);
		mBottomText = (TextView) fragmentView.findViewById(R.id.fragment_detail_show_time_list_bottom_text);
		mBottomDivider = fragmentView.findViewById(R.id.fragment_detail_show_time_list_bottom_divider);

		mHorizontalScrollView.setSmoothScrollingEnabled(true);

		// Hide the text until data loads
		mBottomText.setVisibility(View.GONE);

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

		// Create loader
		Bundle loaderArgs = new Bundle();
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertsForPoiDatabaseQuery(mPoiId, AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME);
		loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		getLoaderManager().initLoader(LOADER_ID_ALERTS, loaderArgs, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateLoader: id = " + id);
		}

		switch (id) {
			case LOADER_ID_ALERTS:
				String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
				DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
				return LoaderUtils.createCursorLoader(databaseQuery);
			default:
				// Otherwise, let the parent class handle it
				return super.onCreateLoader(id, args);
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

				// Clear the show times and load new ones
				mShowTimeLayout.removeAllViews();
				mShowTimeButtons.clear();

				// Go to the POI
				if (data != null && data.moveToFirst()) {
					String poiObjectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
					Integer poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));

					PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

					// Get the POI name for later
					mPoiName = poi.getDisplayName();

					// Add show times
					if (poi instanceof Show) {
						Show show = (Show) poi;

						int showTimesInView = 0;
						View nextOrCurrentShowButton = null;

						int sidePaddingInDp = 16;
						int showTimePaddingInDp = 3;
						Resources r = getResources();
						final float sidePaddingInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sidePaddingInDp, r.getDisplayMetrics());
						final float showTimePaddingInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, showTimePaddingInDp, r.getDisplayMetrics());

						// Only add show times if they have been synced today
						UniversalAppState uoState = UniversalAppStateManager.getInstance();
						boolean hasSyncedPoisToday = UniversalAppStateManager.hasSyncedToday(uoState.getDateOfLastPoiSyncInMillis());
						List<String> startTimesList = show.getStartTimes();
						if (hasSyncedPoisToday && startTimesList != null) {

							// Add padding to the left of the scroll view
							View leftPadding = new View(getActivity());
							leftPadding.setLayoutParams(new LinearLayout.LayoutParams(Math.round(sidePaddingInPx - showTimePaddingInPx),
									LayoutParams.WRAP_CONTENT));
							mShowTimeLayout.addView(leftPadding);

							// Show the time list in the order it is returned
							List<String> endTimesList = show.getEndTimes();
							for (int i = 0; i < startTimesList.size(); i++) {
								String startTime = startTimesList.get(i);
								if (startTime == null) {
									continue;
								}

								// First, parse in the start time from 24 hour ("14:30:00"), park time
								SimpleDateFormat uoTwentyFourHourFormat = new SimpleDateFormat("HH:mm", Locale.US);
								uoTwentyFourHourFormat.setTimeZone(DateTimeUtils.getParkTimeZone());

								Date startTimeDate = null;
								try {
									startTimeDate = uoTwentyFourHourFormat.parse(startTime);
								}
								catch (ParseException e) {
									if (BuildConfig.DEBUG) {
										Log.e(TAG, "onLoadFinished: exception trying to parse in start time", e);
									}

									// Log the exception to crittercism
									Crittercism.logHandledException(e);

									continue;
								}

								SimpleDateFormat sdfOutTime;
								if (DateTimeUtils.is24HourFormat()) {
									sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);
								} else {
									// Format the show time to 12 hour ("2:30"), park time
									sdfOutTime = new SimpleDateFormat("h:mm", Locale.US);
								}
								sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
								String formattedStartTime = sdfOutTime.format(startTimeDate);

								// Last, format the AM/PM portion, park time
								SimpleDateFormat sdfOutTimeAmPm = new SimpleDateFormat("a", Locale.US);
								sdfOutTimeAmPm.setTimeZone(DateTimeUtils.getParkTimeZone());

								String formattedStartTimeAmPm;
								if (DateTimeUtils.is24HourFormat()) {
									// Don't show AM/PM for 24 hour
									formattedStartTimeAmPm = "";
								} else {
									formattedStartTimeAmPm = sdfOutTimeAmPm.format(startTimeDate);
								}

								// Next, get the end time if it exists
								String endTime = null;
								Date endTimeDate = null;
								String formattedEndTime = null;
								String formattedEndTimeAmPm = null;
								if (endTimesList != null && i < endTimesList.size()) {
									endTime = endTimesList.get(i);

									// Then, try to parse in the end time from 24 hour ("14:30:00"), Eastern time
									if (endTime != null && !endTime.isEmpty()) {
										try {
											endTimeDate = uoTwentyFourHourFormat.parse(endTime);
											formattedEndTime = sdfOutTime.format(endTimeDate);
											formattedEndTime = getString(R.string.detail_show_time_hours_end_range, formattedEndTime);
											if (DateTimeUtils.is24HourFormat()) {
												// Don't show AM/PM for 24 hour
												formattedEndTimeAmPm = "";
											} else {
												formattedEndTimeAmPm = sdfOutTimeAmPm.format(endTimeDate);
											}
										}
										catch (Exception e) {
											if (BuildConfig.DEBUG) {
												Log.e(TAG, "onLoadFinished: exception trying to parse in end time", e);
											}

											// Log the exception to crittercism
											Crittercism.logHandledException(e);
										}
									}
								}

								// Then, format out the current 24 hour time ("14:30:00")
								String now = uoTwentyFourHourFormat.format(new Date());

								// If the current local time is past the show time, disable the button
								boolean isBeforeShowStart = now.compareTo(startTime) <= 0;
								boolean isBeforeShowEnd = (endTime != null) && now.compareTo(endTime) <= 0;

								// Create the show time button and add it to the list
								View showTimeButton = createShowTimeButton(mShowTimeLayout,
										formattedStartTime, formattedStartTimeAmPm,
										formattedEndTime, formattedEndTimeAmPm,
										isBeforeShowStart, startTime);

								mShowTimeLayout.addView(showTimeButton);
								showTimesInView++;
								mShowTimeButtons.put(startTime, showTimeButton);

								// Store the next/current show time to reference later
								if ((isBeforeShowStart || isBeforeShowEnd) && nextOrCurrentShowButton == null) {
									nextOrCurrentShowButton = showTimeButton;
								}
							}

							// Add padding to the right of the scroll view
							View rightPadding = new View(getActivity());
							rightPadding.setLayoutParams(new LinearLayout.LayoutParams(Math.round(sidePaddingInPx - showTimePaddingInPx),
									LayoutParams.WRAP_CONTENT));
							mShowTimeLayout.addView(rightPadding);

						}

						// Update the text based on what's in the view
						if (showTimesInView > 0) {
							if (nextOrCurrentShowButton != null) {
								mBottomText.setText(R.string.detail_show_time_list_set_reminder);
								mBottomText.setContentDescription(getString(R.string.detail_show_time_list_set_reminder_content_description));
								mBottomText.setVisibility(View.VISIBLE);
								mHorizontalScrollView.setVisibility(View.VISIBLE);
								mHorizontalScrollView.setFocusable(true);

								// Scroll to the next show time
								final View nextShowButtonCopy = nextOrCurrentShowButton;
								mHorizontalScrollView.post(new Runnable() {
									@Override
									public void run() {
										mHorizontalScrollView.smoothScrollTo(Math.round(nextShowButtonCopy.getLeft() - sidePaddingInPx),
												Math.round(nextShowButtonCopy.getTop()));
									}
								});
							}
							else {
								mBottomText.setText(R.string.detail_show_time_list_no_more_shows);
								mBottomText.setContentDescription(getString(R.string.detail_show_time_list_no_more_shows));
								mBottomText.setVisibility(View.VISIBLE);
								mHorizontalScrollView.setVisibility(View.VISIBLE);

								// Scroll to the last show time
								mHorizontalScrollView.post(new Runnable() {
									@Override
									public void run() {
										mHorizontalScrollView.smoothScrollTo(Math.round(mShowTimeLayout.getRight()),
												Math.round(mHorizontalScrollView.getTop()));
									}
								});
							}
						}
						else {
							mBottomText.setText(R.string.detail_show_time_list_shows_unavailable);
							mBottomText.setContentDescription(getString(R.string.detail_show_time_list_shows_unavailable));
							mBottomText.setVisibility(View.VISIBLE);
							mHorizontalScrollView.setVisibility(View.GONE);
						}
					}
				}

				// Update the state of the show time buttons
				updateShowTimeButtonsState();
				break;
			case LOADER_ID_ALERTS:
				mShowTimeAlerts.clear();

				// Load in the alerts for the POI
				if (data != null && data.moveToFirst()) {
					do {
						String showTime = data.getString(data.getColumnIndex(AlertsTable.COL_SHOW_TIME));
						String showTimeAlertJson = data.getString(data.getColumnIndex(AlertsTable.COL_ALERT_OBJECT_JSON));

						if (showTime != null && showTimeAlertJson != null) {
							ShowTimeAlert showTimeAlert = GsonObject.fromJson(showTimeAlertJson, ShowTimeAlert.class);
							mShowTimeAlerts.put(showTime, showTimeAlert);
						}
					}
					while (data.moveToNext());
				}

				// Update the state of the show time buttons
				updateShowTimeButtonsState();
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
			case LOADER_ID_ALERTS:
				// Data is not available anymore, delete reference
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			String showTime = (String) v.getTag();
			ShowTimeAlert showTimeAlert = new ShowTimeAlert(mPoiId, mPoiName, ShowTimeAlert.NOTIFY_INTERVAL_MIN_SHOW_START, showTime);
			Bundle bundle = SetAlertActivity.newInstanceBundle(showTimeAlert.toJson(), AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME);
			startActivity(new Intent(parentActivity, SetAlertActivity.class).putExtras(bundle));
		}
	}

	private String createShowTimeText(String startTimeString, String startAmPmString,
			String endTimeString, String endAmPmString) {

		StringBuilder timeBuilder = new StringBuilder();

		if (startTimeString != null) {
			timeBuilder.append(startTimeString);

			if (startAmPmString != null) {
				timeBuilder.append(" ");
				timeBuilder.append(startAmPmString);
			}

			// End time is only relevant with a start time
			if (endTimeString != null) {
				timeBuilder.append(" to ");
				timeBuilder.append(endTimeString);

				if (endAmPmString != null) {
					timeBuilder.append(" ");
					timeBuilder.append(endAmPmString);
				}
			}
		}

		return timeBuilder.toString();
	}

	private View createShowTimeButton(ViewGroup parentViewGroup, String startTimeString, String startAmPmString,
			String endTimeString, String endAmPmString, boolean enabled, String showTimeString) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		ViewGroup showTimeButton = (ViewGroup) inflater.inflate(R.layout.button_show_time, parentViewGroup, false);
		ImageView iconImage = (ImageView) showTimeButton.findViewById(R.id.button_show_time_icon_image);
		TextView startTimeText = (TextView) showTimeButton.findViewById(R.id.button_show_time_text);
		TextView startAmPmText = (TextView) showTimeButton.findViewById(R.id.button_show_time_am_pm_text);
		TextView endTimeText = (TextView) showTimeButton.findViewById(R.id.button_show_time_end_text);
		TextView endAmPmText = (TextView) showTimeButton.findViewById(R.id.button_show_time_end_am_pm_text);

		showTimeButton.setEnabled(enabled);
		showTimeButton.setOnClickListener(this);
		iconImage.setVisibility(View.GONE);

		if (startTimeString != null) {
			startTimeText.setText(startTimeString);
		}
		startTimeText.setVisibility(!TextUtils.isEmpty(startTimeString) ? View.VISIBLE : View.GONE);

		if (startAmPmString != null) {
			startAmPmText.setText(startAmPmString);
		}
		startAmPmText.setVisibility(!TextUtils.isEmpty(startAmPmString) ? View.VISIBLE : View.GONE);

		if (endTimeString != null) {
			endTimeText.setText(endTimeString);
		}
		endTimeText.setVisibility(!TextUtils.isEmpty(endTimeString) ? View.VISIBLE : View.GONE);

		if (endAmPmString != null) {
			endAmPmText.setText(endAmPmString);

			// If AM/PM text are the same, hide the start time AM/PM
			if (startAmPmString != null && startAmPmString.equals(endAmPmString)) {
				startAmPmText.setVisibility(View.GONE);
			}
		}
		endAmPmText.setVisibility(!TextUtils.isEmpty(endAmPmString) ? View.VISIBLE : View.GONE);

		showTimeButton.setTag(showTimeString);

		// Add a content description
		String timeText = createShowTimeText(startTimeString, startAmPmString, endTimeString, endAmPmString);
		String contentDescription = enabled ? getString(R.string.detail_show_time_button_prompt_to_set_reminder_formatted_content_description, timeText) : timeText;
		showTimeButton.setContentDescription(contentDescription);

		return showTimeButton;
	}

	private void updateShowTimeButtonsState() {
		Set<String> showTimes = mShowTimeButtons.keySet();
		for (String showTime : showTimes) {
			View showTimeButton = mShowTimeButtons.get(showTime);
			ImageView iconImage = (ImageView) showTimeButton.findViewById(R.id.button_show_time_icon_image);

			// Update the button background based on the if the alert exists
			if (mShowTimeAlerts.containsKey(showTime)) {
				showTimeButton.setBackgroundResource(R.drawable.state_list_show_time_button_blue_with_fade);
				iconImage.setVisibility(View.VISIBLE);
				TintUtils.tintImageView(ContextCompat.getColor(getContext(), R.color.show_time_icon),
						iconImage);
			} else {
				showTimeButton.setBackgroundResource(R.drawable.state_list_show_time_button_gray_with_fade);
				iconImage.setVisibility(View.GONE);
			}

			// Hack to flip enabled back and forth to force Android to draw the new state
			// Without this, the View always appears disabled until it is touched
			showTimeButton.setEnabled(!showTimeButton.isEnabled());
			showTimeButton.setEnabled(!showTimeButton.isEnabled());
		}
	}
}
