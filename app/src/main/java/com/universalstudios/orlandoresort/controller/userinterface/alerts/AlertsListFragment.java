package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.alerts.Alert;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.alerts.ShowTimeAlert;
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.DeleteRideOpenAlertRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.DeleteWaitTimeAlertRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.GetRegisteredAlertsRequest;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class AlertsListFragment extends DatabaseQueryFragment implements OnItemClickListener,
		OnUniversalAppStateChangeListener, MultiChoiceModeListener {
	private static final String TAG = AlertsListFragment.class.getSimpleName();

	private StickyListHeadersListView mAlertsListView;
	private AlertsListCursorAdapter mAlertsCursorAdapter;
	private ViewGroup mNoAlertsLayout;
	private ActionMode mActionMode;
	private boolean mDoAnyAlertsExist;

	public static AlertsListFragment newInstance() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		AlertsListFragment fragment = new AlertsListFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertsDatabaseQuery();
		args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
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

		mDoAnyAlertsExist = false;

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			GetRegisteredAlertsRequest getRegisteredAlertsRequest = new GetRegisteredAlertsRequest.Builder(null).build();
			NetworkUtils.queueNetworkRequest(getRegisteredAlertsRequest);
			NetworkUtils.startNetworkService();

			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_ALERTS,
					null,
					AnalyticsUtils.CONTENT_SUB_2_MY_ALERTS,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_alerts_list, container, false);

		// Setup Views
		mAlertsListView = (StickyListHeadersListView) fragmentView.findViewById(R.id.fragment_alerts_stickylistview);
		mNoAlertsLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_alerts_no_results_layout);

		// Create footer view, must be added before setting the adapter (old Android bug)
		View footerView = inflater.inflate(R.layout.list_alert_item_footer, null, false);
		mAlertsListView.addFooterView(footerView, null, false);

		mAlertsCursorAdapter = new AlertsListCursorAdapter(getActivity(), null);
		mAlertsListView.setAdapter(mAlertsCursorAdapter);
		mAlertsListView.setOnItemClickListener(this);

		// Allow the list to support multi edit
		mAlertsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
		mAlertsListView.setMultiChoiceModeListener(this);

		// If this is the first creation, default state
		if (savedInstanceState == null) {
			mAlertsListView.setVisibility(View.GONE);
			mNoAlertsLayout.setVisibility(View.GONE);
		}

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
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

		// Update the views based on the geofence
		updateViewsBasedOnGeofence();

		// Listen for state changes
		UniversalAppStateManager.registerStateChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}

		// Stop listening for state changes
		UniversalAppStateManager.unregisterStateChangeListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		// Release resources used to load images
		mAlertsCursorAdapter.destroy();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		inflater.inflate(R.menu.action_edit, menu);
		TintUtils.tintAllMenuItems(menu, getContext());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}

		MenuItem menuItem = menu.findItem(R.id.action_edit);
		if (menuItem != null) {
			menuItem.setVisible(mDoAnyAlertsExist).setEnabled(mDoAnyAlertsExist);
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case R.id.action_edit:
				// Start the multiselect by selecting the first item
				if (mAlertsListView != null && mAlertsListView.getAdapter().getCount() > 0) {
					mAlertsListView.setItemChecked(0, true);
					mAlertsListView.smoothScrollToPosition(0);
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
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateActionMode");
		}

		// Create the menu
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.action_delete, menu);
		TintUtils.tintAllMenuItems(menu, getContext());

		// Set the title
		updateActionModeTitle(mode);

		// Keep a reference to the action mode
		mActionMode = mode;

		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareActionMode");
		}

		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onItemCheckedStateChanged: id:" + id + " checked: " + checked);
		}

		// Update the title
		updateActionModeTitle(mode);
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onActionItemClicked");
		}

		switch (item.getItemId()) {
			case R.id.action_delete:
				// Get each checked alert
				ArrayList<Alert> alertsToDelete = new ArrayList<Alert>();
				SparseBooleanArray checkedAlerts = mAlertsListView.getCheckedItemPositions();
				for (int i = 0; i < checkedAlerts.size(); i++) {
					if (checkedAlerts.valueAt(i) == true) {
						Cursor cursor = (Cursor) mAlertsListView.getItemAtPosition(checkedAlerts.keyAt(i));
						Alert alertToDelete = AlertsListCursorAdapter.getAlertFromCursor(cursor);
						if (alertToDelete != null) {
							alertsToDelete.add(alertToDelete);
						}
					}
				}

				// Confirm before deleting alerts
				if (alertsToDelete.size() > 0) {
					FragmentManager fragmentManager = getChildFragmentManager();
					boolean isDialogFragmentShowing =
							fragmentManager.findFragmentByTag(ConfirmDeleteAlertsDialogFragment.class.getSimpleName()) != null;

					if (!isDialogFragmentShowing) {
						ConfirmDeleteAlertsDialogFragment dialogFragment =
								ConfirmDeleteAlertsDialogFragment.newInstance(alertsToDelete);
						dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
					}
				}

				return true;
			default:
				return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDestroyActionMode");
		}

		// Release the reference to the action mode
		mActionMode = null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onItemClick: position = " + position + " id = " + id);
		}

		// Open the set alert page
		Cursor cursor = (Cursor) mAlertsCursorAdapter.getItem(position);
		if (cursor != null && id != -1) {
			String alertObjectJson = cursor.getString(cursor.getColumnIndex(AlertsTable.COL_ALERT_OBJECT_JSON));
			Integer alertTypeId = cursor.getInt(cursor.getColumnIndex(AlertsTable.COL_ALERT_TYPE_ID));

			// Open the set alert page
			Bundle bundle = SetAlertActivity.newInstanceBundle(alertObjectJson, alertTypeId);
			Intent intent = new Intent(getActivity(), SetAlertActivity.class).putExtras(bundle);
			startActivity(intent);
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

				// Swap current cursor with a new up to date cursor
				mAlertsCursorAdapter.swapCursor(data);

				// Only show the alerts list if they exist
				mDoAnyAlertsExist = (data != null && data.getCount() > 0);
				mAlertsListView.setVisibility(mDoAnyAlertsExist ? View.VISIBLE : View.GONE);
				mNoAlertsLayout.setVisibility(mDoAnyAlertsExist ? View.GONE : View.VISIBLE);

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
				mAlertsCursorAdapter.swapCursor(null);
				break;
			default:
				break;
		}
	}

	@Override
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						// Update the views based on the geofence
						updateViewsBasedOnGeofence();
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}
			});
		}
	}

	public void finishActionMode() {
		if (mActionMode != null) {
			mActionMode.finish();
		}
	}

	private void invalidateOptionsMenu() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.invalidateOptionsMenu();
		}
	}

	private void updateViewsBasedOnGeofence() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {

			// Restart the loader to trigger a reload showing/hiding wait times
			// and guide me
			restartLoader();
		}
	}

	private void updateActionModeTitle(ActionMode mode) {
		int checkItemCount = 0;
		long[] checkedItemIds = mAlertsListView.getCheckedItemIds();
		if (checkedItemIds != null) {
			checkItemCount = checkedItemIds.length;
		}

		String title = getString(R.string.alerts_list_edit_title, checkItemCount);
		mode.setTitle(title);
	}

	private void deleteAlerts(List<Alert> alertsToDelete) {
		if (alertsToDelete == null || alertsToDelete.size() == 0) {
			return;
		}

		Activity parentActivity = getActivity();
		if (parentActivity != null) {

			List<Alert> showTimeAlertsToDelete = new ArrayList<Alert>();
			boolean waitTimeAlertsQueued = false;
			for (Alert alertToDelete : alertsToDelete) {
				if (alertToDelete == null) {
					continue;
				}

				// If the alert is a wait time alert, create a request to delete it
				if (alertToDelete instanceof WaitTimeAlert) {
					DeleteWaitTimeAlertRequest deleteWaitTimeAlertRequest = new DeleteWaitTimeAlertRequest.Builder(null)
					.setPriority(Priority.NORMAL)
					.setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
					.setWaitTimeAlertToDelete((WaitTimeAlert) alertToDelete)
					.build();
					NetworkUtils.queueNetworkRequest(deleteWaitTimeAlertRequest);

					waitTimeAlertsQueued = true;
				}
				// If the alert is a ride open alert, create a request to delete it
				else if (alertToDelete instanceof RideOpenAlert) {
					DeleteRideOpenAlertRequest deleteRideOpenAlertRequest = new DeleteRideOpenAlertRequest.Builder(null)
					.setPriority(Priority.NORMAL)
					.setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
					.setRideOpenAlertToDelete((RideOpenAlert) alertToDelete)
					.build();
					NetworkUtils.queueNetworkRequest(deleteRideOpenAlertRequest);

					waitTimeAlertsQueued = true;
				}
				// If the alert is a show time alert, queue it to be delete locally
				else if (alertToDelete instanceof ShowTimeAlert) {
					showTimeAlertsToDelete.add(alertToDelete);
				}
			}

			// Start the network service to process the wait time alert deletes
			if (waitTimeAlertsQueued) {
				NetworkUtils.startNetworkService();

				// Inform the user a delete is processing
				String toastMessage = getResources().getQuantityString(
						R.plurals.alerts_toast_deleting_alert_plural, alertsToDelete.size());
				UserInterfaceUtils.showToastFromForeground(
						toastMessage, Toast.LENGTH_SHORT, parentActivity);
			}
			// Delete show time alerts locally
			if (showTimeAlertsToDelete.size() > 0) {
				AlertsUtils.deleteAlertsFromDatabase(showTimeAlertsToDelete,
						parentActivity.getContentResolver(), true);
			}

		}
	}

	public static class OutOfParkAlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = OutOfParkAlertDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.alerts_list_out_of_park_title);
			alertDialogBuilder.setMessage(R.string.alerts_list_out_of_park_message);
			alertDialogBuilder.setNeutralButton(R.string.alerts_list_out_of_park_neutral_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_NEUTRAL:
					// Just let the dialog dismiss
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
		}
	}

	public static class ConfirmDeleteAlertsDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = ConfirmDeleteAlertsDialogFragment.class.getSimpleName();

		private static final String KEY_ARG_ALERT_OBJECT_JSON_LIST = "KEY_ARG_ALERT_OBJECT_JSON_LIST";
		private static final String KEY_ARG_ALERT_TYPE_LIST = "KEY_ARG_ALERT_TYPE_LIST";
		private List<Alert> mAlertsToDelete;

		public static ConfirmDeleteAlertsDialogFragment newInstance(List<Alert> alertsToDelete) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "newInstance" );
			}

			// Create a new fragment instance
			ConfirmDeleteAlertsDialogFragment fragment = new ConfirmDeleteAlertsDialogFragment();

			// Get arguments passed in, if any
			Bundle args = fragment.getArguments();
			if (args == null) {
				args = new Bundle();
			}

			// Convert the alert list a serializable form
			if (alertsToDelete != null && alertsToDelete.size() > 0) {

				String[] alertObjectJsonList = new String[alertsToDelete.size()];
				int[] alertTypeIdList = new int[alertsToDelete.size()];

				for (int i = 0; i < alertsToDelete.size(); i++) {
					Alert alert = alertsToDelete.get(i);
					alertObjectJsonList[i] = alert.toJson();
					if (alert instanceof WaitTimeAlert) {
						alertTypeIdList[i] = AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME;
					}
					else if (alert instanceof RideOpenAlert) {
						alertTypeIdList[i] = AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN;
					}
					else if (alert instanceof ShowTimeAlert) {
						alertTypeIdList[i] = AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME;
					}
				}

				// Add parameters to the argument bundle
				args.putStringArray(KEY_ARG_ALERT_OBJECT_JSON_LIST, alertObjectJsonList);
				args.putIntArray(KEY_ARG_ALERT_TYPE_LIST, alertTypeIdList);
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
				mAlertsToDelete = new ArrayList<Alert>();
			}
			// Otherwise, set incoming parameters
			else {
				String[] alertObjectJsonList = args.getStringArray(KEY_ARG_ALERT_OBJECT_JSON_LIST);
				int[] alertTypeIdList = args.getIntArray(KEY_ARG_ALERT_TYPE_LIST);
				mAlertsToDelete = new ArrayList<Alert>();

				// Build the alert list
				for (int i = 0; i < alertObjectJsonList.length; i++) {
					Alert alertToDelete = Alert.fromJson(alertObjectJsonList[i], alertTypeIdList[i]);
					mAlertsToDelete.add(alertToDelete);
				}
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.alerts_confirm_delete_title);
			String message = getResources().getQuantityString(
					R.plurals.alerts_confirm_delete_message_plural, mAlertsToDelete.size(), mAlertsToDelete.size());
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

					// Delete the alerts and finish the action mode
					Fragment parentFragment = getParentFragment();
					if (parentFragment != null && parentFragment instanceof AlertsListFragment) {
						AlertsListFragment alertsListFragment = (AlertsListFragment) parentFragment;
						alertsListFragment.deleteAlerts(mAlertsToDelete);
						alertsListFragment.finishActionMode();
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
}
