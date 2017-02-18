package com.universalstudios.orlandoresort.controller.userinterface.search;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersListView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SearchFragment extends DatabaseQueryFragment implements OnItemClickListener, OnScrollListener {
	private static final String TAG = SearchFragment.class.getSimpleName();

	private StickyListHeadersListView mStickyListHeadersListView;
	private SearchListCursorAdapter mSearchListCursorAdapter;
	private ViewGroup mNoResultsLayout;
	private SearchQueryProvider mSearchQueryProvider;

	public static SearchFragment newInstance(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
		}

		// Create a new fragment instance
		SearchFragment fragment = new SearchFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof SearchQueryProvider) {
			mSearchQueryProvider = (SearchQueryProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof SearchQueryProvider) {
			mSearchQueryProvider = (SearchQueryProvider) activity;
		}
		// If neither implements the interface, log a warning
		else if (mSearchQueryProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement SearchQueryProvider");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {

		}
		// Otherwise, set incoming parameters
		else {

		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

		}
		// Otherwise restore state, overwriting any passed in parameters
		else {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

		// Setup Views
		mStickyListHeadersListView = (StickyListHeadersListView) fragmentView.findViewById(R.id.fragment_search_stickylistview);
		mNoResultsLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_search_no_results_layout);

		mSearchListCursorAdapter = new SearchListCursorAdapter(getActivity(), null);
		mStickyListHeadersListView.setAdapter(mSearchListCursorAdapter);
		mStickyListHeadersListView.setOnItemClickListener(this);
		mStickyListHeadersListView.setOnScrollListener(this);

		// If this is the first creation, default state
		if (savedInstanceState == null) {
			mStickyListHeadersListView.setVisibility(View.VISIBLE);
			mNoResultsLayout.setVisibility(View.GONE);
		}
		else {

		}
		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
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
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		// Release resources used to load images
		mSearchListCursorAdapter.destroy();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onItemClick: position = " + position + " id = " + id);
		}

		// Open POI detail page
		Cursor cursor = mSearchListCursorAdapter.getCursor();
		if (cursor != null && cursor.moveToPosition(position)) {
			String poiObjectJson = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
			Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
			String venueObjectJson = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

			DetailUtils.openDetailPage(view.getContext(), venueObjectJson, poiObjectJson, poiTypeId, true, null);

			// Track the page view
			if (poiObjectJson != null && poiTypeId != null) {
				PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				if (poi != null) {
					String searchQuery = null;
					if (mSearchQueryProvider != null) {
						searchQuery = mSearchQueryProvider.provideSearchQuery();
					}
					AnalyticsUtils.Builder builder = new AnalyticsUtils.Builder();
					builder.setProperty(25, searchQuery);

					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_SITE_NAV,
							null, null,
							AnalyticsUtils.CONTENT_SUB_2_SEARCH,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							poi.getDisplayName(),
							builder.build());
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onScrollStateChanged");
		}

		// Close the keyboard when the list is scrolled
		if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onScrollStateChanged: scrollState = SCROLL_STATE_TOUCH_SCROLL, closing keyboard");
			}
			closeKeyboard();
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Swap current cursor with a new up to date cursor
				mSearchListCursorAdapter.swapCursor(data);

				// If there are no results, but there is a search query, show no results
				if (data == null || data.getCount() == 0
						&& mSearchQueryProvider != null && mSearchQueryProvider.provideSearchQuery().length() > 0) {
					mStickyListHeadersListView.setVisibility(View.GONE);
					mNoResultsLayout.setVisibility(View.VISIBLE);
				}
				// Otherwise, show the list
				else {
					mStickyListHeadersListView.setVisibility(View.VISIBLE);
					mNoResultsLayout.setVisibility(View.GONE);
				}
				break;
			default:
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
				mSearchListCursorAdapter.swapCursor(null);
				break;
			default:
				break;
		}
	}

	private void closeKeyboard() {
		UserInterfaceUtils.closeKeyboard(mStickyListHeadersListView);
	}
}
