/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.AlertsTable;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SetAlertActivity extends NetworkRefreshActivity {
	private static final String TAG = SetAlertActivity.class.getSimpleName();

	private static final String KEY_ARG_ALERT_OBJECT_JSON = "KEY_ARG_ALERT_OBJECT_JSON";
	private static final String KEY_ARG_ALERT_TYPE_ID = "KEY_ARG_ALERT_TYPE_ID";

	private ViewGroup mFragmentContainer;

	public static Bundle newInstanceBundle(String alertObjectJson, int alertTypeId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_ALERT_OBJECT_JSON, alertObjectJson);
		args.putInt(KEY_ARG_ALERT_TYPE_ID, alertTypeId);
		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_set_alert);

		String alertObjectJson;
		Integer alertTypeId;

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			alertObjectJson = null;
			alertTypeId = null;
		}
		// Otherwise, set incoming parameters
		else {
			alertObjectJson = args.getString(KEY_ARG_ALERT_OBJECT_JSON);
			alertTypeId = args.getInt(KEY_ARG_ALERT_TYPE_ID);
		}

		mFragmentContainer = (ViewGroup) findViewById(R.id.activity_set_alert_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			if (mFragmentContainer != null) {

				// Create the right type of set alert fragment, depending on the alert type
				Fragment fragment = null;
				if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_WAIT_TIME) {
					fragment = SetWaitTimeAlertFragment.newInstance(alertObjectJson);
				}
				else if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_SHOW_TIME) {
					fragment = SetShowTimeAlertFragment.newInstance(alertObjectJson);
				}
				else if (alertTypeId == AlertsTable.VAL_ALERT_TYPE_ID_RIDE_OPEN) {
					fragment = SetRideOpenAlertFragment.newInstance(alertObjectJson);
				}

				if (fragment != null) {
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.replace(mFragmentContainer.getId(), fragment,
							fragment.getClass().getName());

					fragmentTransaction.commit();
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return (super.onOptionsItemSelected(item));
		}
	}

}
