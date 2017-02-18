/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ExploreActivity extends NetworkRefreshActivity {
	private static final String TAG = ExploreActivity.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static final String KEY_ARG_EXPLORE_TYPE = "KEY_ARG_EXPLORE_TYPE";
	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_FILTER_OPTIONS = "KEY_ARG_FILTER_OPTIONS";

	private ViewGroup mExploreFragmentContainer;

    public static Bundle newInstanceBundle(int actionBarTitleResId, ExploreType exploreType,
            FilterOptions... filterOptions) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();
		args.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		// Add filter options if present
        if(filterOptions.length > 0) {
            args.putString(KEY_ARG_FILTER_OPTIONS, filterOptions[0].toJson());
        }

		return args;
	}

    public static Bundle newInstanceBundle(int actionBarTitleResId, ExploreType exploreType,
            String selectedPoiObjectJson, FilterOptions... filterOptions) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = newInstanceBundle(actionBarTitleResId, exploreType);
		if (selectedPoiObjectJson != null) {
			args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		}
		// Add filter options if present
        if(filterOptions.length > 0) {
            args.putString(KEY_ARG_FILTER_OPTIONS, filterOptions[0].toJson());
        }

		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setContentView(R.layout.activity_explore);

		int actionBarTitleResId;
		String selectedPoiObjectJson;
		ExploreType exploreType;
		FilterOptions filterOptions;

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			exploreType = null;
			actionBarTitleResId = -1;
			selectedPoiObjectJson = null;
			filterOptions = null;
		}
		// Otherwise, set incoming parameters
		else {
			exploreType = (ExploreType) args.getSerializable(KEY_ARG_EXPLORE_TYPE);
			actionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
			selectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
			filterOptions = GsonObject.fromJson(args.getString(KEY_ARG_FILTER_OPTIONS), FilterOptions.class);
		}

		// If this is the first creation, add fragments
		if (savedInstanceState == null) {

			// Load the explore fragment
			mExploreFragmentContainer = (ViewGroup) findViewById(R.id.activity_explore_fragment_container);
			if (mExploreFragmentContainer != null) {

				ExploreFragment fragment; 
                if (filterOptions != null) {
                    fragment = ExploreFragment.newInstance(actionBarTitleResId, exploreType,
                            selectedPoiObjectJson, filterOptions);
                } else {
                    fragment = ExploreFragment.newInstance(actionBarTitleResId, exploreType,
                            selectedPoiObjectJson);
                }
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mExploreFragmentContainer.getId(), fragment,
						fragment.getClass().getName());

				fragmentTransaction.commit();
			}
		}
		// Otherwise, restore state
		else {

		}

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(actionBarTitleResId);
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
