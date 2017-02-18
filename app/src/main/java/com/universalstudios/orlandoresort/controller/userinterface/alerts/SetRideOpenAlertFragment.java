/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
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
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DismissableDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.alerts.RideOpenAlert;
import com.universalstudios.orlandoresort.model.network.domain.alert.DeleteRideOpenAlertRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.SetRideOpenAlertRequest;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SetRideOpenAlertFragment extends DatabaseQueryFragment implements OnClickListener {
	private static final String TAG = SetRideOpenAlertFragment.class.getSimpleName();

	private static final String KEY_ARG_RIDE_OPEN_ALERT_JSON = "KEY_ARG_RIDE_OPEN_ALERT_JSON";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID_POI = LoaderUtils.LOADER_ID_SET_RIDE_OPEN_ALERT_FRAGMENT;

	private View mSaveButton;
	private View mCancelButton;
	private View mBottomActionBarButtonDivider;
	private TextView mSaveButtonText;
	private RideOpenAlert mRideOpenAlert;
	private boolean mDoesAlertExist;
	private Integer mCurrentWaitTime;

	public static SetRideOpenAlertFragment newInstance(String rideOpenAlertJson) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: rideOpenAlertJson = " + rideOpenAlertJson);
		}

		// Create a new fragment instance
		SetRideOpenAlertFragment fragment = new SetRideOpenAlertFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		if (rideOpenAlertJson != null) {
			RideOpenAlert rideOpenAlert = GsonObject.fromJson(rideOpenAlertJson, RideOpenAlert.class);
			DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertDetailDatabaseQuery(rideOpenAlert.getIdString());

			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
			args.putString(KEY_ARG_RIDE_OPEN_ALERT_JSON, rideOpenAlertJson);
		}
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
			mRideOpenAlert = null;
		}
		// Otherwise, set incoming parameters
		else {
			String rideOpenAlertJson = args.getString(KEY_ARG_RIDE_OPEN_ALERT_JSON);
			mRideOpenAlert = GsonObject.fromJson(rideOpenAlertJson, RideOpenAlert.class);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		// Default variables that don't store state
		mDoesAlertExist = false;
		mCurrentWaitTime = null;

		// Set the action bar to the POI name
		String poiName = mRideOpenAlert.getPoiName();
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
		View fragmentView = inflater.inflate(R.layout.fragment_set_ride_open_alert, container, false);

		// Setup Views
		mSaveButton = fragmentView.findViewById(R.id.fragment_set_ride_open_alert_save_button);
		mSaveButtonText = (TextView) fragmentView.findViewById(R.id.fragment_set_ride_open_alert_save_button_text);
		mCancelButton = fragmentView.findViewById(R.id.fragment_set_ride_open_alert_cancel_button);
		mBottomActionBarButtonDivider = fragmentView.findViewById(R.id.fragment_set_ride_open_alert_bottom_action_bar_button_divider);

		mSaveButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

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
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getDetailDatabaseQuery(mRideOpenAlert.getPoiId());
		loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		getLoaderManager().initLoader(LOADER_ID_POI, loaderArgs, this);
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
			case R.id.fragment_set_ride_open_alert_cancel_button:
				// Just close the activity, don't save any changes
				finishActivity();
				break;
			case R.id.fragment_set_ride_open_alert_save_button:
				// Save the alert
				saveAlert();

				// Close the activity
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
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateLoader: id = " + id);
		}

		switch (id) {
			case LOADER_ID_POI:
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
				// If the alert exists, set the "update" state
				if (data != null && data.moveToFirst()) {
					mDoesAlertExist = true;
					mSaveButtonText.setText(R.string.alerts_button_save);
				}
				// If the alert doesn't exist, set the "new" state
				else {
					mDoesAlertExist = false;
					mSaveButtonText.setText(R.string.alerts_button_set_alert);
				}

				// Hide the save button if the alert already exists (since it can't be updated)
				mSaveButton.setVisibility(mDoesAlertExist ? View.GONE : View.VISIBLE);
				mBottomActionBarButtonDivider.setVisibility(mDoesAlertExist ? View.GONE : View.VISIBLE);

				// Refresh the action bar items
				invalidateOptionsMenu();

				break;
			case LOADER_ID_POI:
				// Get the wait time for the POI
				if (data != null && data.moveToFirst()) {
					mCurrentWaitTime = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
				}

				// If the current wait time is not a valid closed state (where
				// ride open alerts can be set), close the page
				boolean rideIsInValidClosedState = AlertsUtils.showSetRideOpenAlert(mCurrentWaitTime);
				if (!rideIsInValidClosedState) {
					finishActivity();
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
			case LOADER_ID_POI:
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

	private void saveAlert() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			// Send a request to the server to create/update the ride open alert
			SetRideOpenAlertRequest setRideOpenAlertRequest = new SetRideOpenAlertRequest.Builder(null)
			.setPriority(Priority.NORMAL)
			.setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
			.setRideOpenAlert(mRideOpenAlert)
			.build();
			NetworkUtils.queueNetworkRequest(setRideOpenAlertRequest);
			NetworkUtils.startNetworkService();

			// Inform the user a save is processing
			UserInterfaceUtils.showToastFromForeground(
					getString(R.string.ride_open_alerts_toast_saving_alert), Toast.LENGTH_SHORT, parentActivity);
		}
	}

	private void deleteAlert() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			// Send a request to the server to delete the ride open alert
			DeleteRideOpenAlertRequest deleteRideOpenAlertRequest = new DeleteRideOpenAlertRequest.Builder(null)
			.setPriority(Priority.NORMAL)
			.setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
			.setRideOpenAlertToDelete(mRideOpenAlert)
			.build();
			NetworkUtils.queueNetworkRequest(deleteRideOpenAlertRequest);
			NetworkUtils.startNetworkService();

			// Inform the user a delete is processing
			String toastMessage = getResources().getQuantityString(
					R.plurals.alerts_toast_deleting_alert_plural, 1);
			UserInterfaceUtils.showToastFromForeground(
					toastMessage, Toast.LENGTH_SHORT, parentActivity);
		}
	}

	public static class ConfirmDeleteAlertDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = ConfirmDeleteAlertDialogFragment.class.getSimpleName();

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
					if (parentFragment != null && parentFragment instanceof SetRideOpenAlertFragment) {
						SetRideOpenAlertFragment fragment = (SetRideOpenAlertFragment) parentFragment;
						fragment.deleteAlert();
						fragment.finishActivity();
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
