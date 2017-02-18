package com.universalstudios.orlandoresort.controller.userinterface.newsletter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;

public class NewsletterActivity extends NetworkRefreshActivity {
	private static final String TAG = NewsletterActivity.class.getSimpleName();

	private ViewGroup mFragmentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setContentView(R.layout.activity_newsletter);

		mFragmentContainer = (ViewGroup) findViewById(R.id.activity_newsletter_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			// Load the newsletter fragment
			if (mFragmentContainer != null) {

				NewsletterFragment fragment = NewsletterFragment.newInstance(R.string.action_title_newsletter);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mFragmentContainer.getId(), fragment, fragment.getClass().getName());

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
