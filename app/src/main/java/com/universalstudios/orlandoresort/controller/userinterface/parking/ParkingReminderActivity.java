package com.universalstudios.orlandoresort.controller.userinterface.parking;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.state.parking.ParkingState;
import com.universalstudios.orlandoresort.model.state.parking.ParkingStateManager;

/**
 * @author acampbell
 */
public class ParkingReminderActivity extends NetworkRefreshActivity {

	private static final String TAG = ParkingReminderActivity.class.getSimpleName();

	private ViewGroup mParkingReminderFragmentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_parking_reminder);

		mParkingReminderFragmentContainer = (ViewGroup) findViewById(R.id.activity_parking_reminder_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the accessibility options fragment
			if (mParkingReminderFragmentContainer != null) {

				ParkingState parkingState = ParkingStateManager.getInstance();

				ParkingReminderFragment parkingReminderFragment =
						ParkingReminderFragment.newInstance(R.string.drawer_item_parking_reminder, parkingState.getSection());
				getSupportFragmentManager().beginTransaction()
						.replace(mParkingReminderFragmentContainer.getId(), parkingReminderFragment, ParkingReminderFragment.TAG)
						.commit();

			}
		}

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(R.string.drawer_item_parking_reminder);
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
