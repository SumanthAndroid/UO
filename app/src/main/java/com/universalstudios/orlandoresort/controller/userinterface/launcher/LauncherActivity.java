package com.universalstudios.orlandoresort.controller.userinterface.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.geofence.GeofenceActivity;

/**
 * 
 * 
 * @author Steven Byle
 */
public class LauncherActivity extends GeofenceActivity {
	private static final String TAG = LauncherActivity.class.getSimpleName();

	private ViewGroup mFragmentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Work around from being started by another app, prevent stacking
		// another instance on top
		final Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
				final String intentAction = intent.getAction();
				if (intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
					if (!isTaskRoot()) {
						if (BuildConfig.DEBUG) {
							Log.w(TAG, "onCreate: LauncherActivity is not the root, finishing LauncherActivity instead of starting a new one");
						}
						finish();
						return;
					}
					else {
						if (BuildConfig.DEBUG) {
							Log.i(TAG, "onCreate: LauncherActivity is the root, starting LauncherActivity");
						}
					}
				}
			}
		}

		setContentView(R.layout.activity_launcher);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// If there is a container, add the corresponding fragment to it
			mFragmentContainer = (ViewGroup) findViewById(R.id.activity_launcher_container);
			if (mFragmentContainer != null) {

				LauncherFragment launcherFragment = new LauncherFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mFragmentContainer.getId(), launcherFragment, LauncherFragment.class.getName());
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
	}

}
