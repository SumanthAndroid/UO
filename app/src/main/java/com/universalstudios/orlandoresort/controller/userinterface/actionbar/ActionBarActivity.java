/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.actionbar;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.MenuItem;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.geofence.GeofenceCheckerActivity;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;

/**
 * 
 * 
 * @author Steven Byle
 */
public abstract class ActionBarActivity extends GeofenceCheckerActivity {
	private static final String TAG = ActionBarActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		UserInterfaceUtils.setActionBarIcon(actionBar);
		UserInterfaceUtils.enableActionBarHomeButton(actionBar);
		UserInterfaceUtils.setActionBarCustomFont(this);
		UserInterfaceUtils.setActionBarElevation(actionBar);
	}

	protected void setActionbarTitle(String title) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	protected void setActionbarTitle(@StringRes int titleResId) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(titleResId);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}
		int count = getSupportFragmentManager().getBackStackEntryCount();

		switch (item.getItemId()) {
			case android.R.id.home:
				if(count > 0) {
					getSupportFragmentManager().popBackStack();
				} else {
					finish();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
