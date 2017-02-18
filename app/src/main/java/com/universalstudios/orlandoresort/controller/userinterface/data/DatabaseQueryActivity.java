package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Base {@link FragmentActivity} that will monitor a {@link DatabaseQuery} and
 * update the query as it changes, along with a {@link CursorLoader} that will
 * load the data.
 * 
 * @author Steven Byle
 */
public abstract class DatabaseQueryActivity extends NetworkRefreshActivity implements LoaderCallbacks<Cursor>, OnDatabaseQueryChangeListener {
	private static final String TAG = DatabaseQueryActivity.class.getSimpleName();

	private static final String KEY_STATE_DATABASE_QUERY_JSON = "KEY_STATE_DATABASE_QUERY_JSON";
	protected static final String KEY_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_DATABASE_QUERY_JSON";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	protected static final int LOADER_ID_DATABASE_QUERY = LoaderUtils.LOADER_ID_DATABASE_QUERY_ACTIVITY;

	private DatabaseQuery mDatabaseQuery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Default parameters
		Bundle args = getIntent().getExtras();
		if (args == null) {
			mDatabaseQuery = null;
		}
		// Otherwise, set incoming parameters
		else {
			String databaseQueryJson = args.getString(KEY_ARG_DATABASE_QUERY_JSON);
			if (databaseQueryJson != null) {
				mDatabaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
			}
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
			String databaseQueryJson = savedInstanceState.getString(KEY_STATE_DATABASE_QUERY_JSON);
			if (databaseQueryJson != null) {
				mDatabaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
			}
		}

        // Create loader
        Bundle loaderArgs = new Bundle();
        if (mDatabaseQuery != null) {
            loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, mDatabaseQuery.toJson());
            getSupportLoaderManager().initLoader(LOADER_ID_DATABASE_QUERY, loaderArgs, this);
        }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mDatabaseQuery != null) {
			outState.putString(KEY_STATE_DATABASE_QUERY_JSON, mDatabaseQuery.toJson());
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateLoader: id = " + id);
		}

		switch (id) {
			case LOADER_ID_DATABASE_QUERY:
				String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
				DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
				return LoaderUtils.createCursorLoader(databaseQuery);
			default:
				// Otherwise, let the parent class handle it
				return super.onCreateLoader(id, args);
		}
	}

	@Override
	public void onDatabaseQueryChange(DatabaseQuery newDatabaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onNewDatabaseQuery");
		}

		if (newDatabaseQuery != null) {
			updateDatabaseQuery(newDatabaseQuery);
		}
	}

	private void updateDatabaseQuery(DatabaseQuery newDatabaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "updateDatabaseQuery");
		}

		mDatabaseQuery = newDatabaseQuery;

		// Restart the loader to show changes to the query
		Bundle args = new Bundle();
		args.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, mDatabaseQuery.toJson());
		getSupportLoaderManager().restartLoader(LOADER_ID_DATABASE_QUERY, args, this);
	}

	protected DatabaseQuery getDatabaseQuery() {
		return mDatabaseQuery;
	}
}
