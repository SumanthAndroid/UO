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
import android.widget.ListAdapter;
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
import com.universalstudios.orlandoresort.model.alerts.WaitTimeAlert;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.alert.DeleteWaitTimeAlertRequest;
import com.universalstudios.orlandoresort.model.network.domain.alert.SetWaitTimeAlertRequest;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.flipview.FlipView;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.ArrayList;

/**
 * @author Steven Byle
 */
public class SetWaitTimeAlertFragment extends DatabaseQueryFragment implements OnClickListener {
	private static final String TAG = SetWaitTimeAlertFragment.class.getSimpleName();

	private static final String KEY_ARG_WAIT_ALERT_JSON = "KEY_ARG_WAIT_ALERT_JSON";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
	private static final String KEY_STATE_FLIPPER_POSITION = "KEY_STATE_FLIPPER_POSITION";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID_POI = LoaderUtils.LOADER_ID_SET_WAIT_TIME_ALERT_FRAGMENT;

	private View mSaveButton;
	private View mCancelButton;
	private TextView mSaveButtonText;
	private FlipView mFlipView;
	private WaitTimeAlert mWaitTimeAlert;
	private boolean mIsFirstCreation;
	private boolean mDoesAlertExist;
	private boolean mHasLoadedAlert;
	private boolean mHasLoadedPoi;
	private boolean mHasSetDefaultFlipperState;
	private int mLastSavedFlipperPosition;
	private Integer mNotifyThresholdInMin;
	private Integer mCurrentWaitTime;

	public static SetWaitTimeAlertFragment newInstance(String waitTimeAlertJson) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: waitTimeAlertJson = " + waitTimeAlertJson);
		}

		// Create a new fragment instance
		SetWaitTimeAlertFragment fragment = new SetWaitTimeAlertFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		if (waitTimeAlertJson != null) {
			WaitTimeAlert waitTimeAlert = GsonObject.fromJson(waitTimeAlertJson, WaitTimeAlert.class);
			DatabaseQuery databaseQuery = DatabaseQueryUtils.getAlertDetailDatabaseQuery(waitTimeAlert.getIdString());

			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
			args.putString(KEY_ARG_WAIT_ALERT_JSON, waitTimeAlertJson);
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
			mWaitTimeAlert = null;
		}
		// Otherwise, set incoming parameters
		else {
			String waitTimeAlertJson = args.getString(KEY_ARG_WAIT_ALERT_JSON);
			mWaitTimeAlert = GsonObject.fromJson(waitTimeAlertJson, WaitTimeAlert.class);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mIsFirstCreation = true;
			mLastSavedFlipperPosition = -1;

			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_ALERTS,
					null,
					AnalyticsUtils.CONTENT_SUB_2_SET_WAIT_TIME_ALERT,
					null,
					mWaitTimeAlert.getPoiName(),
					null);
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			mIsFirstCreation = false;
			mLastSavedFlipperPosition = savedInstanceState.getInt(KEY_STATE_FLIPPER_POSITION, -1);
		}

		// Default variables that don't store state
		mDoesAlertExist = false;
		mHasLoadedAlert = false;
		mHasLoadedPoi = false;
		mHasSetDefaultFlipperState = false;
		mNotifyThresholdInMin = null;
		mCurrentWaitTime = null;

		// Set the action bar to the POI name
		String poiName = mWaitTimeAlert.getPoiName();
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
		View fragmentView = inflater.inflate(R.layout.fragment_set_wait_time_alert, container, false);

		// Setup Views
		mSaveButton = fragmentView.findViewById(R.id.fragment_set_wait_time_alert_save_button);
		mSaveButtonText = (TextView) fragmentView.findViewById(R.id.fragment_set_wait_time_alert_save_button_text);
		mCancelButton = fragmentView.findViewById(R.id.fragment_set_wait_time_alert_cancel_button);
		mFlipView = (FlipView) fragmentView.findViewById(R.id.fragment_set_wait_time_alert_flipview);

		mSaveButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		return fragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Create loader
		Bundle loaderArgs = new Bundle();
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getDetailDatabaseQuery(mWaitTimeAlert.getPoiId());
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}

		outState.putInt(KEY_STATE_FLIPPER_POSITION, mFlipView.getCurrentPage());
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
			case R.id.fragment_set_wait_time_alert_cancel_button:
				// Just close the activity, don't save any changes
				finishActivity();
				break;
			case R.id.fragment_set_wait_time_alert_save_button:
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
				mHasLoadedAlert = true;

				// If the alert exists, set the "update" state
				if (data != null && data.moveToFirst()) {
					mNotifyThresholdInMin = data.getInt(data.getColumnIndex(AlertsTable.COL_NOTIFY_THRESHOLD_IN_MIN));

					mDoesAlertExist = true;
					mSaveButtonText.setText(R.string.alerts_button_save);
				}
				// If the alert doesn't exist, set the "new" state
				else {
					mDoesAlertExist = false;
					mSaveButtonText.setText(R.string.alerts_button_set_alert);
				}

				// Try to set the default flipper state
				updateDefaultFlipperState();

				// Refresh the action bar items
				invalidateOptionsMenu();

				break;
			case LOADER_ID_POI:
				mHasLoadedPoi = true;

				// Get the wait time for the POI
				if (data != null && data.moveToFirst()) {
					mCurrentWaitTime = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
				}

				// Set the flipper adapter based on the current wait time
				if (mCurrentWaitTime != null && mFlipView != null) {
					int currentPage = mFlipView.getCurrentPage();

					ArrayList<Integer> waitTimes = createWaitTimeFlipperOptions(mCurrentWaitTime);
					WaitTimeListAdapter waitTimeListAdapter = new WaitTimeListAdapter(waitTimes);
					mFlipView.setAdapter(waitTimeListAdapter);

					// Set the flipper back to the same page since the adapter was updated
					setFlipperToPage(currentPage);
				}

				// Try to set the default flipper state
				updateDefaultFlipperState();

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

	private void updateDefaultFlipperState() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "updateDefaultFlipperState");
		}

		if (!mHasSetDefaultFlipperState && mFlipView != null && mHasLoadedAlert && mHasLoadedPoi) {
			if (mIsFirstCreation) {
				mHasSetDefaultFlipperState = true;

				// Default the flipper to the saved value
				if (mNotifyThresholdInMin != null) {
					ListAdapter flipViewAdapter = mFlipView.getAdapter();
					if (flipViewAdapter != null) {
						int defaultPosition = 0;
						for (int i = flipViewAdapter.getCount() - 1; i >= 0; i--) {
							Integer waitTime = (Integer) flipViewAdapter.getItem(i);
							if (mNotifyThresholdInMin.compareTo(waitTime) >= 0) {
								defaultPosition = i;
								break;
							}
						}
						setFlipperToPage(defaultPosition);
					}
				}
				// If there is no saved value, default the flipper to last item
				else if (mCurrentWaitTime != null) {
					setFlipperToPage(mFlipView.getPageCount() - 1);
				}

				// Peek the prev/next page, to let the user know the flipper can be swiped
				if (mFlipView.getCurrentPage() + 1 < mFlipView.getPageCount()) {
					mFlipView.peekNext(true);
				} else if (mFlipView.getCurrentPage() - 1 >= 0) {
					mFlipView.peekPrevious(true);
				}
			}
			// Otherwise, if the state is being restored, set the last position
			else {
				mHasSetDefaultFlipperState = true;
				setFlipperToPage(mLastSavedFlipperPosition);
			}
		}
	}

	private void setFlipperToPage(int pageIndex) {
		if (mFlipView == null) {
			return;
		}

		// Fit the desired page within the flipper's range
		int pageCount = mFlipView.getPageCount();
		if (pageIndex >= pageCount) {
			pageIndex = pageCount - 1;
		} else if (pageIndex < 0) {
			pageIndex = 0;
		}

		if (pageIndex >= 0 && pageIndex < pageCount) {
			mFlipView.flipTo(pageIndex);
		}
	}

	private ArrayList<Integer> createWaitTimeFlipperOptions(Integer currentWaitTime) {
		ArrayList<Integer> waitTimes = new ArrayList<Integer>();

		// Add all 5 minute increments up to the current wait time
		if (currentWaitTime != null && currentWaitTime >= 0) {
			for (int min = 5; min < currentWaitTime; min += 5) {
				waitTimes.add(min);
			}
		}

		// If there is no wait time, or it is <= 5 minutes, make 5 the only option
		if (waitTimes.size() == 0) {
			waitTimes.add(5);
		}

		return waitTimes;
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
			// Save the alert to the database
			ListAdapter flipViewAdapter = mFlipView.getAdapter();
			if (flipViewAdapter != null) {

				// Get the selected wait time and save it
				Integer selectedWaitTime = (Integer) flipViewAdapter.getItem(mFlipView.getCurrentPage());
				if (selectedWaitTime != null) {
					mWaitTimeAlert.setNotifyThresholdInMin(selectedWaitTime);

					// Send a request to the server to create/update the wait time alert
					SetWaitTimeAlertRequest setWaitTimeAlertRequest = new SetWaitTimeAlertRequest.Builder(null)
							.setPriority(Priority.NORMAL)
							.setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
							.setWaitTimeAlert(mWaitTimeAlert)
							.build();
					NetworkUtils.queueNetworkRequest(setWaitTimeAlertRequest);
					NetworkUtils.startNetworkService();

					// Inform the user a save is processing
					UserInterfaceUtils.showToastFromForeground(
							getString(R.string.wait_time_alerts_toast_saving_alert), Toast.LENGTH_SHORT, parentActivity);

					// Track the event
					AnalyticsUtils.Builder extraData = new AnalyticsUtils.Builder();
					if (mCurrentWaitTime != null) {
						extraData.setProperty(28, String.valueOf(mWaitTimeAlert.getNotifyThresholdInMin()));
						extraData.setProperty(29, String.valueOf(mCurrentWaitTime));

					}

					AnalyticsUtils.trackEvent(
							mWaitTimeAlert.getPoiName(),
							AnalyticsUtils.EVENT_NAME_SET_ALERT,
							AnalyticsUtils.EVENT_NUM_SET_ALERT,
							extraData.build());
				}
			}
		}
	}

	private void deleteAlert() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			// Send a request to the server to delete the wait time alert
			DeleteWaitTimeAlertRequest deleteWaitTimeAlertRequest = new DeleteWaitTimeAlertRequest.Builder(null)
					.setPriority(Priority.NORMAL)
					.setConcurrencyType(ConcurrencyType.SYNCHRONOUS)
					.setWaitTimeAlertToDelete(mWaitTimeAlert)
					.build();
			NetworkUtils.queueNetworkRequest(deleteWaitTimeAlertRequest);
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
					if (parentFragment != null && parentFragment instanceof SetWaitTimeAlertFragment) {
						SetWaitTimeAlertFragment fragment = (SetWaitTimeAlertFragment) parentFragment;
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
