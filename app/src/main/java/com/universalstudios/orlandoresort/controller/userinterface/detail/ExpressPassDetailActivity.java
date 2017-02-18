/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
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
public class ExpressPassDetailActivity extends NetworkRefreshActivity {
	private static final String TAG = ExpressPassDetailActivity.class.getSimpleName();

	private ViewGroup mExpressPassDetailFragmentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_express_pass_detail);

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
		}
		// Otherwise, set incoming parameters
		else {
		}

		mExpressPassDetailFragmentContainer = (ViewGroup) findViewById(R.id.activity_express_pass_detail_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the accessibility options fragment
			if (mExpressPassDetailFragmentContainer != null) {

				ExpressPassDetailFragment fragment = ExpressPassDetailFragment.newInstance();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mExpressPassDetailFragmentContainer.getId(), fragment,
						fragment.getClass().getName());

				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {

		}

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(Html.fromHtml(getResources().getString(R.string.action_title_express_pass)));
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
