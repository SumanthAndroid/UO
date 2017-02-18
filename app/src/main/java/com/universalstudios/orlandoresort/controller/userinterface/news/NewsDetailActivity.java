/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class NewsDetailActivity extends NetworkRefreshActivity {
	private static final String TAG = NewsDetailActivity.class.getSimpleName();

	private static final String KEY_ARG_NEWS_ID = "KEY_ARG_NEWS_ID";

	private ViewGroup mFragmentContainer;

	public static Bundle newInstanceBundle(long newsId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		args.putLong(KEY_ARG_NEWS_ID, newsId);
		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_news_detail);

		Long newsId;

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			newsId = null;
		}
		// Otherwise, set incoming parameters
		else {
			newsId = args.getLong(KEY_ARG_NEWS_ID);
		}

		mFragmentContainer = (ViewGroup) findViewById(R.id.activity_news_detail_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			if (mFragmentContainer != null && newsId != null) {

				// Create the right type of set alert fragment, depending on the alert type
				Fragment fragment = NewsDetailFragment.newInstance(newsId);

				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mFragmentContainer.getId(), fragment,
						fragment.getClass().getName());

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
