/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.wayfinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WayfindingActivity extends NetworkRefreshActivity implements WayfindingStopper {
	private static final String TAG = WayfindingActivity.class.getSimpleName();

	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_SELECTED_POI_TYPE_ID = "KEY_ARG_SELECTED_POI_TYPE_ID";

	private ViewGroup mWayfindingContainer;

	public static Bundle newInstanceBundle(String selectedPoiObjectJson, int selectedPoiTypeId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		args.putInt(KEY_ARG_SELECTED_POI_TYPE_ID, selectedPoiTypeId);

		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setContentView(R.layout.activity_wayfinding);
		mWayfindingContainer = (ViewGroup) findViewById(R.id.activity_wayfinding_container);

		String selectedPoiObjectJson;
		int selectedPoiTypeId;

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			selectedPoiObjectJson = null;
			selectedPoiTypeId = -1;
		}
		// Otherwise, set incoming parameters
		else {
			selectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
			selectedPoiTypeId = args.getInt(KEY_ARG_SELECTED_POI_TYPE_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the wayfinding fragment
			if (mWayfindingContainer != null) {

				WayfindingFragment fragment = WayfindingFragment.newInstance(selectedPoiObjectJson, selectedPoiTypeId);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mWayfindingContainer.getId(), fragment, fragment.getClass().getName());

				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {
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
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}

		// If the activity is being finished, stop the wayfinding service
		if (isFinishing()) {
			stopWayfinding(false);
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

	@Override
	public void stopWayfinding(boolean finishActivity) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "stopWayfinding: finishActivity = " + finishActivity);
		}

		// Stop the service, clear the wayfinding image cache, and finish the activity
		stopService(new Intent(this, WayfindingService.class));
		CacheUtils.deleteCacheDir(CacheUtils.WAYFINDING_DISK_CACHE_NAME);

		if (finishActivity) {
			finish();
		}
	}

}
