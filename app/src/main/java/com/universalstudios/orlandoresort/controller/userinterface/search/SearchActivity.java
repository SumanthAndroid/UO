package com.universalstudios.orlandoresort.controller.userinterface.search;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crittercism.app.Crittercism;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.OnDatabaseQueryChangeListener;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.EditText;

import java.lang.ref.WeakReference;

/**
 * @author Steven Byle
 */
public class SearchActivity extends NetworkRefreshActivity implements TextWatcher, OnFocusChangeListener, OnClickListener,
DatabaseQueryProvider, SearchQueryProvider, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnUniversalAppStateChangeListener {
	private static final String TAG = SearchActivity.class.getSimpleName();

	private static final String KEY_STATE_SEARCH_QUERY_TEXT = "KEY_STATE_SEARCH_QUERY_TEXT";

	private String mSearchQueryText;
	private ViewGroup mFragmentContainer;
	private EditText mSearchActionViewEditText;
	private MenuItem mSearchMenuItem;
	private ImageView mSearchClearImage;
	private RelativeLayout mSearchActionView;
	private GoogleApiClient mGoogleApiClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		setContentView(R.layout.activity_search);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.action_title_search);

		mFragmentContainer = (ViewGroup) findViewById(R.id.activity_search_container);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mSearchQueryText = "";

			if (mFragmentContainer != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				SearchFragment searchFragment = SearchFragment.newInstance(provideDatabaseQuery(this));
				fragmentTransaction.replace(mFragmentContainer.getId(), searchFragment, searchFragment.getClass().getName());
				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {
			mSearchQueryText = savedInstanceState.getString(KEY_STATE_SEARCH_QUERY_TEXT);
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}

		// Get a connection to user location
		mGoogleApiClient.connect();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

		// Listen for state changes
		UniversalAppStateManager.registerStateChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}

		// Stop listening for state changes
		UniversalAppStateManager.unregisterStateChangeListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}

		outState.putString(KEY_STATE_SEARCH_QUERY_TEXT, mSearchQueryText);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}

		// Stop listening to user location
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		// Adds items to the action bar
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.action_search_view, menu);

		mSearchMenuItem = menu.findItem(R.id.action_search_view);
		mSearchActionView = (RelativeLayout) mSearchMenuItem.getActionView();
		mSearchActionViewEditText = (EditText) mSearchActionView.findViewById(R.id.action_search_edit_text);
		mSearchClearImage = (ImageView) mSearchActionView.findViewById(R.id.action_search_clear_image);

		mSearchActionViewEditText.addTextChangedListener(this);
		mSearchActionViewEditText.setOnFocusChangeListener(this);
		mSearchClearImage.setOnClickListener(this);

		// Restore last search state
		mSearchActionViewEditText.setText(mSearchQueryText);

		// Set the cursor at the end of the search box
		mSearchActionViewEditText.setSelection(mSearchActionViewEditText.length());
		mSearchActionViewEditText.requestFocus();

		TintUtils.tintAllMenuItems(menu, this);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return (super.onOptionsItemSelected(item));
		}
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		switch (v.getId()) {
			case R.id.action_search_clear_image:
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "onClick: search clear button clicked");
				}

				// Clear the search text, if there is any
				if (mSearchActionViewEditText.length() > 0) {
					mSearchActionViewEditText.getText().clear();
					openKeyboardAndFocusSearch();
				}
				break;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "afterTextChanged");
		}

		// Toggle the clear button state based on the search box
		if (mSearchActionViewEditText.length() == 0) {
			mSearchClearImage.setVisibility(View.GONE);
		} else {
			mSearchClearImage.setVisibility(View.VISIBLE);
		}

		// Only update the search if the text changes
		String newSearchQueryText = mSearchActionViewEditText.getText().toString();
		if (mSearchQueryText != null && !mSearchQueryText.equalsIgnoreCase(newSearchQueryText)) {
			mSearchQueryText = newSearchQueryText;
			updateSearchFragmentBasedOnGeofence();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "beforeTextChanged");
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onTextChanged");
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onFocusChange: hasFocus = " + hasFocus);
		}

		switch (v.getId()) {
			case R.id.action_search_edit_text:
				break;
			default:
				break;
		}
	}

	@Override
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onUniversalAppStateChange");
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					// Update search results sorting based on geofence
					updateSearchFragmentBasedOnGeofence();
				} catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		});
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onConnected");
		}

		// Update search results sorting based on geofence
		updateSearchFragmentBasedOnGeofence();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (BuildConfig.DEBUG) {
			Log.e(TAG, "onConnectionFailed: connectionResult.getErrorCode() = " + connectionResult.getErrorCode());
		}
	}

	@Override
	public String provideSearchQuery() {
		return mSearchQueryText != null ? mSearchQueryText : "";
	}

	@Override
	public DatabaseQuery provideDatabaseQuery(Object requester) {
		return createSearchQueryBasedOnGeofence();
	}

	private void updateSearchFragmentBasedOnGeofence() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "updateSearchFragmentBasedOnGeofence");
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		SearchFragment searchFragment = (SearchFragment) fragmentManager.findFragmentByTag(
				SearchFragment.class.getName());

		// Update the search results
		if (searchFragment != null) {
			DatabaseQuery newSearchQuery = createSearchQueryBasedOnGeofence();
			((OnDatabaseQueryChangeListener) searchFragment).onDatabaseQueryChange(newSearchQuery);
		}
	}

	private DatabaseQuery createSearchQueryBasedOnGeofence() {
		// Get latest location for sorting, if inside the geofence
		Location userLocation = getUserLocationIfInResortGeofence();
		return DatabaseQueryUtils.getSearchDatabaseQuery(mSearchQueryText, userLocation);
	}

	private Location getUserLocationIfInResortGeofence() {
		if (UniversalAppStateManager.getInstance().isInResortGeofence()) {
			if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
				try {
					Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					if (lastLocation != null) {
						return lastLocation;
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private void openKeyboardAndFocusSearch() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "openKeyboardAndFocusSearch");
		}

		OpenKeyboardAndFocusSearchRunnable openKeyboardAndFocusSearchRunnable =
				new OpenKeyboardAndFocusSearchRunnable(mSearchActionViewEditText, this);
		mSearchActionViewEditText.post(openKeyboardAndFocusSearchRunnable);
	}

	private void openSearchView() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "openSearchView");
		}

		if (!mSearchMenuItem.isActionViewExpanded()) {
			mSearchMenuItem.expandActionView();
		}
	}

	private void closeSearchView() {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "closeSearchView");
		}

		if (mSearchMenuItem.isActionViewExpanded()) {
			mSearchMenuItem.collapseActionView();
		}
	}

	private static final class OpenKeyboardAndFocusSearchRunnable implements Runnable {
		private final WeakReference<EditText> mSearchActionViewEditText;
		private final WeakReference<Activity> mActivity;

		public OpenKeyboardAndFocusSearchRunnable(EditText searchActionViewEditText, Activity activity) {
			mSearchActionViewEditText = new WeakReference<EditText>(searchActionViewEditText);
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void run() {
			final EditText searchActionViewEditText = mSearchActionViewEditText.get();
			final Activity activity = mActivity.get();

			// Check to see the references are still alive
			if (searchActionViewEditText != null && activity != null) {
				try {
					// Set the cursor at the end of the search box
					searchActionViewEditText.setSelection(searchActionViewEditText.length());
					searchActionViewEditText.requestFocus();

					// Open the keyboard
					InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.showSoftInput(searchActionViewEditText, 0);
				} catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "OpenKeyboardAndFocusSearchRunnable: unable to open the keyboard", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		}
	}
}
