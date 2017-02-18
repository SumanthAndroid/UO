/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryActivity;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;

/**
 * 
 * 
 * @author Steven Byle
 */
public class MapDetailActivity extends DatabaseQueryActivity implements DatabaseQueryProvider {
	private static final String TAG = MapDetailActivity.class.getSimpleName();

	private static final String KEY_ARG_EXPLORE_TYPE = "KEY_ARG_EXPLORE_TYPE";
	private static final String KEY_ARG_SELECTED_POI_OBJECT_JSON = "KEY_ARG_SELECTED_POI_OBJECT_JSON";
	private static final String KEY_ARG_MAP_TILE_ID = "KEY_ARG_MAP_TILE_ID";

	private ViewGroup mExploreMapFragmentContainer;
	private Long mMapTileId = null;

	public static Bundle newInstanceBundle(DatabaseQuery databaseQuery, ExploreType exploreType, String selectedPoiObjectJson) {
		return newInstanceBundle(databaseQuery, exploreType, selectedPoiObjectJson, null);
	}

	public static Bundle newInstanceBundle(DatabaseQuery databaseQuery, ExploreType exploreType, String selectedPoiObjectJson, Long mapTileId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstanceBundle");
		}

		// Create a new bundle and put in the args
		Bundle args = new Bundle();

		// Add parameters to the argument bundle
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		args.putSerializable(KEY_ARG_MAP_TILE_ID, mapTileId);
		args.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);
		if (selectedPoiObjectJson != null) {
			args.putString(KEY_ARG_SELECTED_POI_OBJECT_JSON, selectedPoiObjectJson);
		}
		return args;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		setContentView(R.layout.activity_explore_map);

		ExploreType exploreType;
		String selectedPoiObjectJson;

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			exploreType = null;
			selectedPoiObjectJson = null;
		}
		// Otherwise, set incoming parameters
		else {
			exploreType = (ExploreType) args.getSerializable(KEY_ARG_EXPLORE_TYPE);
			selectedPoiObjectJson = args.getString(KEY_ARG_SELECTED_POI_OBJECT_JSON);
            mMapTileId = (Long) args.getSerializable(KEY_ARG_MAP_TILE_ID);
		}

		// If this is the first creation, add fragments
		if (savedInstanceState == null) {

			// Load the explore map fragment
			mExploreMapFragmentContainer = (ViewGroup) findViewById(R.id.activity_explore_map_fragment_container);
			if (mExploreMapFragmentContainer != null) {

				ExploreMapFragment exploreMapFragment = ExploreMapFragment.newInstance(getDatabaseQuery(), exploreType, selectedPoiObjectJson, mMapTileId);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mExploreMapFragmentContainer.getId(), exploreMapFragment,
						ExploreMapFragment.class.getName());

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

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Update UI based on the POI
				if (data != null && data.moveToFirst()) {
					String poiName = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));

					getActionBar().setTitle(poiName != null ? poiName : "");
				}
				break;
			default:
				// Otherwise, let the parent class handle it
				super.onLoadFinished(loader, data);
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				// Otherwise, let the parent class handle it
				super.onLoaderReset(loader);
				break;
		}
	}

	@Override
	public DatabaseQuery provideDatabaseQuery(Object requester) {
		return getDatabaseQuery();
	}
}
