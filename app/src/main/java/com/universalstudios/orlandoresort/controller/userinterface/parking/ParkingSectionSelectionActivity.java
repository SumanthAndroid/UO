package com.universalstudios.orlandoresort.controller.userinterface.parking;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;


public class ParkingSectionSelectionActivity extends NetworkRefreshActivity {

	private static final String TAG = ParkingSectionSelectionActivity.class.getSimpleName();

	private ViewGroup mParkingSectionSelectionFragmentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_parking_section_selection);

		mParkingSectionSelectionFragmentContainer = (ViewGroup) findViewById(R.id.activity_parking_section_selection_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the accessibility options fragment
			if (mParkingSectionSelectionFragmentContainer != null) {

				ParkingSectionSelectionFragment parkingSectionSelectionFragment = ParkingSectionSelectionFragment.newInstance();

				getSupportFragmentManager().beginTransaction()
						.replace(mParkingSectionSelectionFragmentContainer.getId(),
								parkingSectionSelectionFragment, ParkingSectionSelectionFragment.TAG)
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
