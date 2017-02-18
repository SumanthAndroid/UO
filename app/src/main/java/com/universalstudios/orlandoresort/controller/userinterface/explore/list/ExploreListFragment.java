package com.universalstudios.orlandoresort.controller.userinterface.explore.list;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.MapDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventCursorWrapper;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreFragment;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.PoiSelector;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.UniversalFloatingActionButton;
import com.universalstudios.orlandoresort.controller.userinterface.wayfinding.WayfindingUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.OnUniversalAppStateChangeListener;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ExploreListFragment extends DatabaseQueryFragment implements OnItemClickListener,
		OnUniversalAppStateChangeListener, OnExploreListChildClickListener {
	private static final String TAG = ExploreListFragment.class.getSimpleName();

	private static final String KEY_ARG_EXPLORE_TYPE = "KEY_ARG_EXPLORE_TYPE";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
	private static final String KEY_ARG_HIDE_STICKY_HEADER = "KEY_ARG_HIDE_STICKY_HEADER";
	private static final String KEY_ARG_JSON_OBJECT = "KEY_ARG_JSON_OBJECT";
    private static final String KEY_ARG_MAP_TILE_ID = "KEY_MAP_TILE_ID";
	private static final int LOADER_ID_EVENTS = LoaderUtils.LOADER_ID_EXPLORE_LIST_FRAGMENT_EVENTS;
	private static final int UPCOMING_EVENT_DAY_THRESHOLD = 3;

	private StickyListHeadersListView mStickyListHeadersListView;
	private ExploreListCursorAdapter mExploreListCursorAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ViewGroup mNoResultsLayout;
	private ViewGroup mNoFavoritesLayout;
	private ExploreType mExploreType;
	private PoiSelector mParentPoiSelector;
	private TextView mNoResultsPrimaryText;
	private TextView mNoResultsPrimarySubText;
	private Boolean hideStickyHeaders;
    private Long mMapTileId = null;
	private UniversalFloatingActionButton mCircleButtonFragment;

	public static ExploreListFragment newInstance(DatabaseQuery databaseQuery, ExploreType exploreType, Long mapTileId, Boolean... listArguments) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery + " exploreType = " + exploreType.getValue());
		}

		// Create a new fragment instance
		ExploreListFragment fragment = new ExploreListFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putSerializable(KEY_ARG_EXPLORE_TYPE, exploreType);
		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}

        args.putSerializable(KEY_ARG_MAP_TILE_ID, mapTileId);

		if(listArguments.length > 0) {
			Boolean hideStickyHeadersArgs = listArguments[0];

			if(hideStickyHeadersArgs != null) {
				args.putBoolean(KEY_ARG_HIDE_STICKY_HEADER, hideStickyHeadersArgs);
			}

		}
		fragment.setArguments(args);


		return fragment;
	}

	public static ExploreListFragment newInstance(DatabaseQuery databaseQuery, ExploreType exploreType, String jsonObject, Long mapTileId, Boolean... listArguments) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery + " exploreType = " + exploreType.getValue());
		}

		// Create a new fragment instance
		ExploreListFragment fragment = newInstance(databaseQuery, exploreType, mapTileId, listArguments);

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		args.putString(KEY_ARG_JSON_OBJECT, jsonObject);
        args.putSerializable(KEY_ARG_MAP_TILE_ID, mapTileId);

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
		if (parentFragment != null && parentFragment instanceof PoiSelector) {
			mParentPoiSelector = (PoiSelector) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof PoiSelector) {
			mParentPoiSelector = (PoiSelector) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentPoiSelector == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement PoiSelector");
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
			mExploreType = null;
		}
		// Otherwise, set incoming parameters
		else {
			mExploreType = (ExploreType) args.getSerializable(KEY_ARG_EXPLORE_TYPE);
            mMapTileId = (Long) args.getSerializable(KEY_ARG_MAP_TILE_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			// Track the page view
			ExploreFragment.trackPageView(mExploreType, AnalyticsUtils.CONTENT_SUB_2_LIST);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
		
		Bundle args = getArguments();
		boolean hideStickyHeader = args.getBoolean(KEY_ARG_HIDE_STICKY_HEADER);

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_explore_list, container, false);
		
		// Setup Views
		mStickyListHeadersListView = (StickyListHeadersListView) fragmentView.findViewById(R.id.fragment_explore_list_stickylistview);
		mNoResultsLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_explore_list_no_results_layout);
		mNoResultsPrimaryText = (TextView) fragmentView.findViewById(R.id.fragment_explore_list_no_results_primary_text);
		mNoResultsPrimarySubText = (TextView) fragmentView.findViewById(R.id.fragment_explore_list_no_results_primary_sub_text);
		mNoFavoritesLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_explore_list_no_favorites_layout);
		mSwipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.fragment_explore_list_swiperefreshview);

		mExploreListCursorAdapter = new ExploreListCursorAdapter(getActivity(), null, mExploreType, this, hideStickyHeader);
		if((mExploreType == ExploreType.UPCOMING_EVENTS || mExploreType == ExploreType.EVENT_SERIES_EVENTS))//&& mStickyListHeadersListView.getFooterViewsCount() == 0)
		{
			View footerView = getLayoutInflater(getArguments()).inflate(R.layout.view_explore_list_footer, null, false);
			com.universalstudios.orlandoresort.view.fonts.TextView textView = (com.universalstudios.orlandoresort.view.fonts.TextView)footerView.findViewById(R.id.view_explore_list_footer_textview);
			if(mExploreType == ExploreType.UPCOMING_EVENTS) {
				textView.setText(R.string.list_upcoming_events_footer_message);
			} else if(mExploreType == ExploreType.EVENT_SERIES_EVENTS){

				String jsonObject = getArguments().getString(KEY_ARG_JSON_OBJECT);

				if(jsonObject != null) {
					EventSeries eventSeries = GsonObject.fromJson(jsonObject, EventSeries.class);

					if(eventSeries != null) {
						textView.setText(eventSeries.getDisclaimer());
					}
				}
			}
			mStickyListHeadersListView.addFooterView(footerView);
		}
		mStickyListHeadersListView.setAdapter(mExploreListCursorAdapter);
		mStickyListHeadersListView.setOnItemClickListener(this);
		mStickyListHeadersListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (mStickyListHeadersListView == null || mStickyListHeadersListView.getChildCount() == 0) ? 0 : mStickyListHeadersListView.getChildAt(0).getTop();
                if(BuildConfig.DEBUG){
					Log.d(TAG, "firstItem: "+ firstVisibleItem +
							" \n visibleItemCount: " + visibleItemCount +
							" with the topRowVerticalPosition: " + topRowVerticalPosition +
							" scroll position: " + mStickyListHeadersListView.getScrollPosY());
				}
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0 && mStickyListHeadersListView.getScrollPosY() <= topRowVerticalPosition);
            }
        });

        mSwipeRefreshLayout.setEnabled(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
				if(NetworkUtils.isNetworkConnected()) {
					GetPoisRequest request = new GetPoisRequest.Builder(null).build();
					NetworkUtils.queueNetworkRequest(request);
					NetworkUtils.startNetworkService();

					//give the network operation 20 seconds before closing the activity
					new Handler().postDelayed(new SwipeRefreshRunnable(),20000);

					Log.d(TAG, "Network request made for getting pois");
				}
            }
        });
		// Always assume there is data until it loads
		mStickyListHeadersListView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
		mNoResultsLayout.setVisibility(View.GONE);
		mNoFavoritesLayout.setVisibility(View.GONE);


		if (mExploreType == ExploreType.UPCOMING_EVENTS) {
			// Track today's events page view
			AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
					AnalyticsUtils.CONTENT_FOCUS_EVENTS,
					AnalyticsUtils.CONTENT_SUB_1_EVENTS_TODAY, null, null, null, null);
		}

		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}

		// Update the views based on the geofence
		updateViewsBasedOnGeofence();

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
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		// Release resources used to load images
		if (mExploreListCursorAdapter != null) {
			mExploreListCursorAdapter.destroy();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onItemClick: position = " + position + " id = " + id);
		}

		// Open POI detail page
		Cursor cursor = mExploreListCursorAdapter.getCursor();
		if (cursor != null && cursor.moveToPosition(position)) {
			String poiObjectJson = cursor.getString(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
			Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
			String venueObjectJson = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
			Integer poiHashCode = cursor.getInt(cursor.getColumnIndex(PointsOfInterestTable.COL_POI_HASH_CODE));

			Long mapTileId = mMapTileId;
			if (mExploreType == ExploreType.EVENT_LIST) {
				EventSeries eventSeries = EventSeries.fromJson(poiObjectJson, EventSeries.class);
				if (null != eventSeries) {
					mapTileId = eventSeries.getCustomMapTileId();
				}
			}
			boolean loadedDetailPage = DetailUtils.openDetailPage(parent.getContext(), venueObjectJson, poiObjectJson, poiTypeId, false, mapTileId);

			// If selected POI doesn't have a detail page, click it on the map
			if (!loadedDetailPage && poiHashCode != null && mParentPoiSelector != null) {
				mParentPoiSelector.selectPoi(poiHashCode);
			}
		}
	}

	@Override
	public void onExploreListChildClick(View v, String poiObjectJson, int poiTypeId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onExploreListChildClick");
		}

		switch (v.getId()) {
			case R.id.list_poi_item_guide_me_button_layout:
				boolean didLoadWayfindingPage = WayfindingUtils.openWayfindingPage(v.getContext(), poiObjectJson, poiTypeId);
				if (!didLoadWayfindingPage && getActivity() != null) {
					UserInterfaceUtils.showToastFromForeground(
							getString(R.string.wayfinding_toast_error_trying_to_start_guiding), Toast.LENGTH_LONG, getActivity());
				}
				break;
			case R.id.list_poi_item_favorite_toggle_button:
				PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);

				// Update the favorite state off the main thread
				boolean isFavorite = poi.getIsFavorite() != null && poi.getIsFavorite().booleanValue();
				PoiUtils.updatePoiIsFavoriteInDatabase(
						getActivity(), poi, !isFavorite, true);
				break;
			case R.id.list_poi_item_locate_button:
				//open marker on map page
				PointOfInterest poiToMap = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
				if (mParentPoiSelector != null) {
					mParentPoiSelector.selectPoi(poiToMap.hashCode());
				} else {
					if (poiToMap instanceof Event) {
						// Track map clicked for Event
						AnalyticsUtils.trackEvent(poiToMap.getDisplayName(), AnalyticsUtils.EVENT_NAME_MAP, -1, null);
					}

					// Open a map page to the selected POI
					Bundle detailMapBundle = MapDetailActivity.newInstanceBundle(
							DatabaseQueryUtils.getDetailDatabaseQuery(poiToMap.getId()),
							ExploreType.SOLO_MAP_PAGE, poiObjectJson);
					startActivity(new Intent(getContext(), MapDetailActivity.class).putExtras(detailMapBundle));
				}
				break;
		}
	}
	
	@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader: id = " + id);
        }

        switch (id) {
            case LOADER_ID_EVENTS:
                String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
                DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
                return LoaderUtils.createCursorLoader(databaseQuery);
            default:
                // Otherwise, let the parent class handle it
                return super.onCreateLoader(id, args);
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
				mExploreListCursorAdapter.swapCursor(data);

				// If there are no results, show no results view
				if (data == null || data.getCount() == 0) {
					mStickyListHeadersListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);

					if (mExploreType == ExploreType.FAVORITES) {
						mNoFavoritesLayout.setVisibility(View.VISIBLE);
					}
					else if (mExploreType == ExploreType.EVENTS || mExploreType == ExploreType.EVENT_SERIES_EVENTS) {
                        mNoResultsLayout.setVisibility(View.VISIBLE);
                        mNoResultsPrimaryText.setText(R.string.event_timeline_no_results_primary);
                        mNoResultsPrimarySubText.setText(R.string.event_timeline_no_results_primary_sub);
                    }
					else if (mExploreType == ExploreType.UPCOMING_EVENTS) {
                        // Create loader to fetch all upcoming events
                        Bundle loaderArgs = new Bundle();
                        DatabaseQuery databaseQuery = DatabaseQueryUtils
                                .getAllUpcomingTimelineEvents(new FilterOptions(null,
                                        ExploreType.UPCOMING_EVENTS));
                        loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
                        getLoaderManager().initLoader(LOADER_ID_EVENTS, loaderArgs, this);
                    }
					else {
						mNoResultsLayout.setVisibility(View.VISIBLE);
					}
				} else if (mExploreType == ExploreType.UPCOMING_EVENTS) {
					// Create loader to fetch all upcoming events
					Bundle loaderArgs = new Bundle();
					DatabaseQuery databaseQuery = DatabaseQueryUtils
							.getAllUpcomingTimelineEvents(new FilterOptions(null,
									ExploreType.UPCOMING_EVENTS));
					loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
					getLoaderManager().initLoader(LOADER_ID_EVENTS, loaderArgs, this);
				}
				// Otherwise, show the list
				else {
					mStickyListHeadersListView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
					mNoResultsLayout.setVisibility(View.GONE);
					mNoFavoritesLayout.setVisibility(View.GONE);

				}
				break;
            case LOADER_ID_EVENTS:
                // Limit the returned data to 3 days of data after the first event
                int count = 0;
                if (data != null && data.moveToFirst()) {
                    long startDateUnix = data.getLong(data.getColumnIndex(EventTimesTable.COL_START_DATE));
                    Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
                    calendar.setTimeInMillis(startDateUnix * 1000);
                    int currentDate = calendar.get(Calendar.DATE);
                    int dayCount = 0;
                    do {
                        if (dayCount >= UPCOMING_EVENT_DAY_THRESHOLD) {
							count--;
                            break;
                        }
                        long eventDateUnix = data
                                .getLong(data.getColumnIndex(EventTimesTable.COL_START_DATE));
                        calendar.setTimeInMillis(eventDateUnix * 1000);
                        int eventDate = calendar.get(Calendar.DATE);
                        // Include event if it's before the cut off date and it has a valid date
                        if (currentDate != eventDate) {
                            currentDate = eventDate;
                            dayCount++;
                        }
                        count++;
                    } while (data.moveToNext());

                }

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "onLoadFinished: CursorWrapper count = " + count);
                }

                // Use a cursor wrapper to alter the cursors perceived count
                CursorWrapper wrappedCursor = new EventCursorWrapper(data, count);

                // Swap current cursor with a new up to date cursor
                mExploreListCursorAdapter.swapCursor(wrappedCursor);

                // If there are no results, show no results view
                if (wrappedCursor == null || wrappedCursor.getCount() == 0) {
                    mStickyListHeadersListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mNoResultsLayout.setVisibility(View.VISIBLE);
                    mNoResultsPrimaryText.setText(R.string.event_timeline_no_results_primary);
                    mNoResultsPrimarySubText.setText(R.string.event_timeline_no_results_primary_sub);
                    
                    // Track empty event page view
					SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy ", Locale.US);
					sdf.setTimeZone(DateTimeUtils.getParkTimeZone());
                    String dateString = sdf.format(new Date());
                    AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                            AnalyticsUtils.CONTENT_FOCUS_EVENTS, null,
                            String.format(AnalyticsUtils.CONTENT_SUB_2_EVENTS_TODAY_FORMAT , dateString), null, null, null);
                }
                // Otherwise, show the list
                else {
                    mStickyListHeadersListView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mNoResultsLayout.setVisibility(View.GONE);
                    mNoFavoritesLayout.setVisibility(View.GONE);

                }
                break;
			default:
				break;
		}

        //should give time for the refresh icon to disappear
        //otherwise if there are no changes in the data, then this will disappear quickly and
        //it doesn't look appealing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               ExploreListFragment.this.mSwipeRefreshLayout.setRefreshing(false);
            }
        },1000);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				mExploreListCursorAdapter.swapCursor(null);
				break;
			default:
				break;
		}
	}

	@Override
	public void onUniversalAppStateChange(UniversalAppState universalAppState) {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						// Update the views based on the geofence
						updateViewsBasedOnGeofence();
					}
					catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onUniversalAppStateChange: exception trying to refresh UI", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}
			});
		}
	}

	private void updateViewsBasedOnGeofence() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {

			// Restart the loader to trigger a reload showing/hiding wait times
			// and guide me
			restartLoader();
		}
	}

	private class SwipeRefreshRunnable implements Runnable {

		@Override
		public void run() {
			if(mSwipeRefreshLayout.isRefreshing()){
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}
	}
}
