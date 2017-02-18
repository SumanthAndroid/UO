/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

/**
 * 
 * 
 * @author Steven Byle
 */
public class DiningPlanDetailActivity extends NetworkRefreshActivity {
	private static final String TAG = DiningPlanDetailActivity.class.getSimpleName();

	private ViewGroup mDiningPlanFragmentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_dining_plan_detail);

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		mDiningPlanFragmentContainer = (ViewGroup) findViewById(R.id.activity_dining_plan_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the accessibility options fragment
			if (mDiningPlanFragmentContainer != null) {

				DiningPlanDetailFragment fragment = DiningPlanDetailFragment.newInstance();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mDiningPlanFragmentContainer.getId(), fragment,
						fragment.getClass().getName());

				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {

		}

		// Set the action bar title
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.action_title_dining_plans);
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
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
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
