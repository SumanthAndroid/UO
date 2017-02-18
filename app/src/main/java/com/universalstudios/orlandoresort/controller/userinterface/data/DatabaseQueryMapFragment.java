package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crittercism.app.Crittercism;
import com.google.android.gms.maps.SupportMapFragment;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Base {@link SupportMapFragment} that will monitor a {@link DatabaseQuery} and
 * update the query as it changes, along with a {@link CursorLoader} that will
 * load the data.
 * 
 * @author Steven Byle
 */
public abstract class DatabaseQueryMapFragment extends SupportMapFragment implements LoaderCallbacks<Cursor>, OnDatabaseQueryChangeListener {
	private static final String TAG = DatabaseQueryMapFragment.class.getSimpleName();

	private static final String KEY_STATE_DATABASE_QUERY_JSON = "KEY_STATE_DATABASE_QUERY_JSON";
	protected static final String KEY_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_DATABASE_QUERY_JSON";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	protected static final int LOADER_ID_DATABASE_QUERY = LoaderUtils.LOADER_ID_DATABASE_QUERY_MAP_FRAGMENT;

	private DatabaseQuery mDatabaseQuery;
	private DatabaseQueryProvider mParentDatabaseQueryProvider;
	private long mFirstOnLoadFinishedInMs;
	private boolean mIsDoubleOnLoadFinished;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DatabaseQueryProvider) {
			mParentDatabaseQueryProvider = (DatabaseQueryProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DatabaseQueryProvider) {
			mParentDatabaseQueryProvider = (DatabaseQueryProvider) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentDatabaseQueryProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DatabaseQueryProvider");
			}
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Default parameters
		Bundle args = getArguments();
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

		mFirstOnLoadFinishedInMs = -1;
		mIsDoubleOnLoadFinished = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Create loader, if there is a query
		if (mDatabaseQuery != null) {
			Bundle loaderArgs = new Bundle();
			loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, mDatabaseQuery.toJson());
			LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_DATABASE_QUERY, loaderArgs);
		}

		// Check parent to see if the query has changed while the fragment was not active
		if (mParentDatabaseQueryProvider != null) {
			DatabaseQuery databaseQuery = mParentDatabaseQueryProvider.provideDatabaseQuery(this);
			if (databaseQuery != null && !databaseQuery.equals(mDatabaseQuery)) {
				mDatabaseQuery = databaseQuery;
				updateDatabaseQuery(databaseQuery);
			}
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
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
	public void onDestroyView() {
		super.onDestroyView();
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
				return null;
		}
	}

	@Override
	public void onDatabaseQueryChange(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDatabaseQueryChange");
		}

		if (databaseQuery != null) {
			updateDatabaseQuery(databaseQuery);
		}
	}

	protected DatabaseQuery getDatabaseQuery() {
		return mDatabaseQuery;
	}

	protected boolean checkIfDoubleOnLoadFinished() {
		// Track whether this is a double load
		long currentTimeInMs = System.currentTimeMillis();
		mIsDoubleOnLoadFinished = (currentTimeInMs - mFirstOnLoadFinishedInMs) < LoaderUtils.DOUBLE_LOAD_THRESHOLD_MAX_IN_MS;
		if (mFirstOnLoadFinishedInMs <= 0) {
			mFirstOnLoadFinishedInMs = currentTimeInMs;
		}

		return mIsDoubleOnLoadFinished;
	}

	protected void restartLoader() {
		// Restart the loader with the same query to trigger a reload
		if (mDatabaseQuery != null) {
			Bundle args = new Bundle();
			args.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, mDatabaseQuery.toJson());
			LoaderManager loaderManager = getLoaderManager();
			if (loaderManager != null) {
				try {
					loaderManager.restartLoader(LOADER_ID_DATABASE_QUERY, args, this);
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "restartLoader: exception trying to restart loader", e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
			}
		}
	}

	private void updateDatabaseQuery(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "updateDatabaseQuery");
		}

		mDatabaseQuery = databaseQuery;

		// Restart the loader to show changes to the query
		restartLoader();
	}
}
