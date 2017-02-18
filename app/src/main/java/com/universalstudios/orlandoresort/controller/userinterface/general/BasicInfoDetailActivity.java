/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.general;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 * 
 * 
 * @author Steven Byle
 */
public class BasicInfoDetailActivity extends AppCompatActivity {
	private static final String TAG = BasicInfoDetailActivity.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE = "KEY_ARG_ACTION_BAR_TITLE";

	public static Bundle newInstanceBundle(Integer actionBarTitleResId, Integer titleResId, Integer descriptionResId) {
		String actionBarTitle = null;
		String title = null;
		String description = null;
		if (null != actionBarTitleResId) {
			actionBarTitle = UniversalOrlandoApplication.getAppContext().getString(actionBarTitleResId);
		}
		if (null != titleResId) {
			title = UniversalOrlandoApplication.getAppContext().getString(titleResId);
		}
		if (null != descriptionResId) {
			description = UniversalOrlandoApplication.getAppContext().getString(descriptionResId);
		}

		return newInstanceBundle(actionBarTitle, title, description);

	}

	public static Bundle newInstanceBundle(String actionBarTitle, String title, String description) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		if (!TextUtils.isEmpty(actionBarTitle)) {
			args.putString(KEY_ARG_ACTION_BAR_TITLE, actionBarTitle);
		}
		if (!TextUtils.isEmpty(title)) {
			args.putString(BasicInfoDetailFragment.KEY_ARG_TITLE, title);
		}
		if (!TextUtils.isEmpty(description)) {
			args.putString(BasicInfoDetailFragment.KEY_ARG_DESCRIPTION, description);
		}

		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_basic_info_detail);

		String title, description;

		// Default parameters
		Bundle args = getIntent().getExtras();
		String actionBarTitle;
		if (args == null) {
			actionBarTitle = "";
			title = "";
			description = "";
		}
		// Otherwise, set incoming parameters
		else {
			actionBarTitle = args.getString(KEY_ARG_ACTION_BAR_TITLE, "");
			title = args.getString(BasicInfoDetailFragment.KEY_ARG_TITLE, "");
			description = args.getString(BasicInfoDetailFragment.KEY_ARG_DESCRIPTION, "");
		}

		ViewGroup mBasicInfoFragmentContainer = (ViewGroup) findViewById(R.id.activity_basic_info_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the accessibility options fragment
			if (mBasicInfoFragmentContainer != null) {

				BasicInfoDetailFragment fragment = BasicInfoDetailFragment.newInstance(
						title, description);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mBasicInfoFragmentContainer.getId(), fragment,
						fragment.getClass().getName());

				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {

		}

		ActionBar actionBar = getSupportActionBar();
		if (!TextUtils.isEmpty(actionBarTitle)) {
			actionBar.setTitle(Html.fromHtml(actionBarTitle));
		}
		actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
		actionBar.setDisplayShowHomeEnabled(true);
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
