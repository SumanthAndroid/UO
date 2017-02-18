/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DismissableDialogFragment;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SetShowTimeAlertFragment extends DatabaseQueryFragment implements OnClickListener {
	private static final String TAG = SetShowTimeAlertFragment.class.getSimpleName();

	private static final String KEY_ARG_SHOW_ALERT_JSON = "KEY_ARG_SHOW_ALERT_JSON";

	private View mSaveButton;
	private View mCancelButton;
	private TextView mTimeText;
	private TextView mAmPmText;
	private TextView mSaveButtonText;
	private Spinner mNotifySpinner;
	private NotifyIntervalArrayAdapter mNotifyAdapter;
	private ShowTimeAlert mShowTimeAlert;
	private Date mShowTimeDate;
	private boolean mIsFirstCreation;
	private boolean mHasSetDefaultSpinnerState;
	private boolean mDoesAlertExist;
	private AlarmManager mAlarmManager;

	public static SetShowTimeAlertFragment newInstance(String showTimeAlertJson) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: showTimeAlertJson = " + showTimeAlertJson);
		}

		// Create a new fragment instance
		SetShowTimeAlertFragment fragment = new SetShowTimeAlertFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		if (showTimeAlertJson != null) {
			ShowTimeAlert showTimeAlert = GsonObject.fromJson(showTimeAlertJson, ShowTimeAlert.class);
			DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertDetailDatabaseQuery(showTimeAlert.getIdString());

			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
			args.putString(KEY_ARG_SHOW_ALERT_JSON, showTimeAlertJson);
		}
		fragment.setArguments(args);

		return fragment;
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
			mShowTimeAlert = null;
		}
		// Otherwise, set incoming parameters
		else {
			String showTimeAlertJson = args.getString(KEY_ARG_SHOW_ALERT_JSON);
			mShowTimeAlert = GsonObject.fromJson(showTimeAlertJson, ShowTimeAlert.class);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mIsFirstCreation = true;

			// Track the page view
			Map<String, Object> extraData = new AnalyticsUtils.Builder()
			.setEnvironmentVar(72, mShowTimeAlert.getShowTime())
			.build();
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_ALERTS,
					null,
					AnalyticsUtils.CONTENT_SUB_2_SET_SHOW_TIME_ALERT,
					null,
					mShowTimeAlert.getPoiName(),
					extraData);
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mIsFirstCreation = false;
		}

		// Default variables that don't save state
		mDoesAlertExist = false;
		mHasSetDefaultSpinnerState = false;

		// Get the alarm manager to handle setting alerts
		mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

		// Set the action bar to the POI name
		String poiName = mShowTimeAlert.getPoiName();
		poiName = poiName != null ? poiName : "";
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			ActionBar actionBar = parentActivity.getActionBar();
			actionBar.setTitle(poiName);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_set_show_time_alert, container, false);

		// Setup Views
		mSaveButton = fragmentView.findViewById(R.id.fragment_set_show_time_alert_save_button);
		mSaveButtonText = (TextView) fragmentView.findViewById(R.id.fragment_set_show_time_alert_save_button_text);
		mCancelButton = fragmentView.findViewById(R.id.fragment_set_show_time_alert_cancel_button);
		mNotifySpinner = (Spinner) fragmentView.findViewById(R.id.fragment_set_show_time_alert_reminder_notify_spinner);
		mTimeText = (TextView) fragmentView.findViewById(R.id.fragment_set_show_time_alert_circle_badge_show_time_starts_time_text);
		mAmPmText = (TextView) fragmentView.findViewById(R.id.fragment_set_show_time_alert_circle_badge_show_time_am_pm_text);

		mSaveButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		mShowTimeDate = mShowTimeAlert.getShowTimeDateForToday();
		if (mShowTimeDate == null) {
			// If the show time can't be formatted, just close the page
			finishActivity();
		}

		SimpleDateFormat sdfOutTime;
		String formattedShowTimeAmPm;
		if (DateTimeUtils.is24HourFormat()) {
			sdfOutTime = new SimpleDateFormat("HH:mm", Locale.US);

			// Don't show AM/PM for 24 hours format
			formattedShowTimeAmPm = "";
		} else {
			// Format the show time to 12 hour ("2:30"), park's time
			sdfOutTime = new SimpleDateFormat("h:mm", Locale.US);

			// Last, format the AM/PM portion, park's time
			SimpleDateFormat sdfOutTimeAmPm = new SimpleDateFormat("a", Locale.US);
			sdfOutTimeAmPm.setTimeZone(DateTimeUtils.getParkTimeZone());
			formattedShowTimeAmPm = sdfOutTimeAmPm.format(mShowTimeDate);
		}

		sdfOutTime.setTimeZone(DateTimeUtils.getParkTimeZone());
		String formattedShowTime = sdfOutTime.format(mShowTimeDate);

		mTimeText.setText(formattedShowTime);
		mAmPmText.setText(formattedShowTimeAmPm);

		// Create an ArrayAdapter using a string array
		String[] notifyIntervalOptions = getResources().getStringArray(R.array.show_alerts_notify_interval_array);
		int[] notifyBeforeMin = new int[notifyIntervalOptions.length];
		for (int i = 0; i < notifyIntervalOptions.length; i++) {
			notifyBeforeMin[i] = AlertsUtils.convertNotifyStringToMin(getResources(), notifyIntervalOptions[i]);
		}
		mNotifyAdapter = new NotifyIntervalArrayAdapter(getActivity(), R.layout.spinner_selected_item,
				notifyIntervalOptions, notifyBeforeMin, mShowTimeDate);
		mNotifyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		mNotifySpinner.setAdapter(mNotifyAdapter);

		mNotifySpinner.setOnItemSelectedListener(new NotifySpinnerOnItemSelectedListener());

		return fragmentView;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Enable action bar items
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		inflater.inflate(R.menu.action_delete, menu);
		TintUtils.tintAllMenuItems(menu, getContext());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}

		MenuItem menuItem = menu.findItem(R.id.action_delete);
		if (menuItem != null) {
			menuItem.setVisible(mDoesAlertExist).setEnabled(mDoesAlertExist);
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case R.id.action_delete:
				// Confirm before deleting alert
				FragmentManager fragmentManager = getChildFragmentManager();
				boolean isDialogFragmentShowing =
						fragmentManager.findFragmentByTag(ConfirmDeleteAlertDialogFragment.class.getSimpleName()) != null;

				if (!isDialogFragmentShowing) {
					ConfirmDeleteAlertDialogFragment dialogFragment = new ConfirmDeleteAlertDialogFragment();
					dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
				}
				return true;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
				}
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		switch (v.getId()) {
			case R.id.fragment_set_show_time_alert_cancel_button:
				// Just close the activity, don't save any changes
				finishActivity();
				break;
			case R.id.fragment_set_show_time_alert_save_button:
				// Save the alert to the database
				saveAlert();

				// Track the event
				Map<String, Object> extraData = new AnalyticsUtils.Builder()
				.setEnvironmentVar(72, mShowTimeAlert.getShowTime())
				.build();
				AnalyticsUtils.trackEvent(
						mShowTimeAlert.getPoiName(),
						AnalyticsUtils.EVENT_NAME_SET_ALERT,
						AnalyticsUtils.EVENT_NUM_SET_ALERT,
						extraData);

				finishActivity();
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
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

				// If the alert exists, set the "update" state
				if (data != null && data.moveToFirst()) {
					Integer notifyMinBefore = data.getInt(data.getColumnIndex(AlertsTable.COL_NOTIFY_MIN_BEFORE));

					mDoesAlertExist = true;
					mSaveButtonText.setText(R.string.alerts_button_save);

					// Default the spinner to the set alert interval
					if (!mHasSetDefaultSpinnerState && mIsFirstCreation) {
						mHasSetDefaultSpinnerState = true;

						String selectedItem = AlertsUtils.convertNotifyMinBeforeToString(getResources(), notifyMinBefore);
						int selectedItemPos = mNotifyAdapter.getPosition(selectedItem);
						if (selectedItemPos >= 0) {
							mNotifySpinner.setSelection(selectedItemPos);
						}
					}
				}
				// If the alert doesn't exist, set the "new" state
				else {
					mDoesAlertExist = false;
					mSaveButtonText.setText(R.string.alerts_button_set_alert);
				}

				// Refresh the action bar items
				invalidateOptionsMenu();

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

	private void finishActivity() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.finish();
		}
	}

	private void invalidateOptionsMenu() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.invalidateOptionsMenu();
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void saveAlert() {
		String notifyInterval = (String) mNotifySpinner.getSelectedItem();
		if (notifyInterval != null) {
			// Save show time to the database
			int notifyMinBefore = AlertsUtils.convertNotifyStringToMin(getResources(), notifyInterval);
			mShowTimeAlert.setNotifyMinBefore(notifyMinBefore);
			AlertsUtils.saveShowTimeAlertToDatabase(mShowTimeAlert, true);

			// Create an intent to trigger the alert notification
			PendingIntent pendingIntent = AlertsUtils.createTriggerShowAlertPendingIntent(mShowTimeAlert.getIdString());

			// Set an alarm to send the trigger intent
			long triggerTimeInMillis = mShowTimeDate.getTime() - (mShowTimeAlert.getNotifyMinBefore() * 60 * 1000);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
			}
			else {
				mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
			}
			
			// Track the event
			AnalyticsUtils.Builder extraData = new AnalyticsUtils.Builder();
			if (mShowTimeAlert != null) {
				extraData.setProperty(28, String.valueOf(mShowTimeAlert.getNotifyMinBefore()));
				extraData.setProperty(29, new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a", Locale.US).format(mShowTimeDate));
			}
			
			AnalyticsUtils.trackEvent(
					mShowTimeAlert.getPoiName(),
					AnalyticsUtils.EVENT_NAME_SET_ALERT,
					AnalyticsUtils.EVENT_NUM_SET_ALERT,
					extraData.build());
		}
	}

	private void deleteAlert() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			// Cancel any alarm set for this alert
			PendingIntent pendingIntent = AlertsUtils.createTriggerShowAlertPendingIntent(mShowTimeAlert.getIdString());
			mAlarmManager.cancel(pendingIntent);
			AlertsUtils.deleteAlertFromDatabase(mShowTimeAlert.getIdString(), true);
		}
	}

	public static class ConfirmDeleteAlertDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.alerts_confirm_delete_title);
			String message = getResources().getQuantityString(
					R.plurals.alerts_confirm_delete_message_plural, 1, 1);
			alertDialogBuilder.setMessage(message);
			alertDialogBuilder.setPositiveButton(R.string.alerts_confirm_delete_positive_button, this);
			alertDialogBuilder.setNegativeButton(R.string.alerts_confirm_delete_negative_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Fragment parentFragment = getParentFragment();
					if (parentFragment != null && parentFragment instanceof SetShowTimeAlertFragment) {
						SetShowTimeAlertFragment showTimeAlertsFragment = (SetShowTimeAlertFragment) parentFragment;
						showTimeAlertsFragment.deleteAlert();
						showTimeAlertsFragment.finishActivity();
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					// Do nothing, just dismiss the dialog
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate a cancel click
			onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}

	private static class NotifySpinnerOnItemSelectedListener implements OnItemSelectedListener {
		int mLastSelectedPos = 0;

		@Override
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			// Check to see if a disabled item is selected, and reset it back to the last set position if it isn't enabled
			NotifyIntervalArrayAdapter notifyIntervalArrayAdapter = (NotifyIntervalArrayAdapter) parentView.getAdapter();
			if (!notifyIntervalArrayAdapter.isEnabled(position)) {
				if (mLastSelectedPos >= 0 && mLastSelectedPos < notifyIntervalArrayAdapter.getCount()) {
					parentView.setSelection(mLastSelectedPos);
				}
			}
			else {
				mLastSelectedPos = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView) {
		}
	}

	private static class NotifyIntervalArrayAdapter extends ArrayAdapter<String> {

		private final Date mShowTimeDate;
		private final int[] mNotifyBeforeMin;

		public NotifyIntervalArrayAdapter(Context context, int resource, String[] objects, int[] notifyBeforeMin, Date showTimeDate) {
			super(context, resource, objects);
			mNotifyBeforeMin = notifyBeforeMin;
			mShowTimeDate = showTimeDate;
		}

		@Override
		public boolean isEnabled(int position) {
			if (position < 0 || position >= mNotifyBeforeMin.length) {
				return false;
			}

			int notifyBeforeMin = mNotifyBeforeMin[position];

			// Special case, always allow at show start to be enabled
			if (notifyBeforeMin == 0) {
				return true;
			}

			// Otherwise, only enable intervals that are in the future
			if (new Date().getTime() <= (mShowTimeDate.getTime() - (notifyBeforeMin * 60 * 1000))) {
				return true;
			}
			else {
				return false;
			}
		}

		@Override
		public boolean areAllItemsEnabled() {
			// Assume that all items aren't always enabled
			return false;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View v = super.getDropDownView(position, convertView, parent);
			v.setEnabled(isEnabled(position));
			return v;
		}
	}
}
