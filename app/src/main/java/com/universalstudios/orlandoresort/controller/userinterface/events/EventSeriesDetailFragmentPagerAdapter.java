package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.list.ExploreListFragment;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author acampbell
 *
 */
public class EventSeriesDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = EventSeriesDetailFragmentPagerAdapter.class.getSimpleName();

    public static final int PAGE_ABOUT = 1000;
    public static final int PAGE_ACTIVITIES = 1001;
    public static final int PAGE_ATTRACTIONS = 1002;
    private static final int PAGE_SIZE_MAX = 3;

    private final SparseIntArray mPageIds;
    private final SparseArray<String> mPageTitles;
    private DatabaseQuery mDatabaseQuery;
    private DatabaseQuery mEventDatabaseQuery;
    private DatabaseQuery mAttractionDatabaseQuery;
    private EventSeries mEventSeries;
    private Context mContext;

    public EventSeriesDetailFragmentPagerAdapter(Context context, FragmentManager fm,
            DatabaseQuery databaseQuery, EventSeries eventSeries) {
        super(fm);
        mDatabaseQuery = databaseQuery;
        mEventSeries = eventSeries;
        mContext = context;

        mPageIds = new SparseIntArray(PAGE_SIZE_MAX);
        mPageTitles = new SparseArray<String>(PAGE_SIZE_MAX);
        int page = 0;
        // Add about page, always present
        mPageIds.append(page, PAGE_ABOUT);
        mPageTitles.append(page, context.getString(R.string.event_series_detail_tab_about));
        page++;

        if (mEventSeries != null) {
            // Add additional pages and titles if data is present
            if (mEventSeries.getEvents() != null && !mEventSeries.getEvents().isEmpty()) {
                mPageIds.append(page, PAGE_ACTIVITIES);
                if (!TextUtils.isEmpty(mEventSeries.getEventListDisplayName())) {
                    mPageTitles.append(page, mEventSeries.getEventListDisplayName());
                } else {
                    mPageTitles.append(page, context.getString(R.string.event_series_detail_tab_activities));
                }
                page++;

                // Create events database queries
                List<Long> poiIdList = new ArrayList<Long>();
                for (Event event : eventSeries.getEvents()) {
                    poiIdList.add(event.getId());
                }
                mEventDatabaseQuery = DatabaseQueryUtils.getTimelineEventsById(poiIdList, new FilterOptions(
                        null, ExploreType.EVENTS));
            }
            if (mEventSeries.getAttractions() != null && !mEventSeries.getAttractions().isEmpty()) {
                mPageIds.append(page, PAGE_ATTRACTIONS);
                if (!TextUtils.isEmpty(mEventSeries.getAttractionListDisplayName())) {
                    mPageTitles.append(page, mEventSeries.getAttractionListDisplayName());
                } else {
                    mPageTitles.append(page, context.getString(R.string.event_series_detail_tab_attractions));
                }
                page++;

                // Create attractions database queries
                List<Long> poiIdList = new ArrayList<Long>();
                for (PointOfInterest attraction : eventSeries.getAttractions()) {
                    poiIdList.add(attraction.getId());
                }
                mAttractionDatabaseQuery = DatabaseQueryUtils.getPointsOfInterestById(poiIdList,
                        new FilterOptions(null, ExploreType.ATTRACTIONS));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {
        switch (mPageIds.get(position)) {
            case PAGE_ABOUT:
                trackPageView(position);
                return EventSeriesAboutFragment.newInstance(mDatabaseQuery);
            case PAGE_ACTIVITIES:
                trackPageView(position);
                return ExploreListFragment.newInstance(mEventDatabaseQuery, ExploreType.EVENT_SERIES_EVENTS, mEventSeries.toJson(), mEventSeries.getCustomMapTileId(), mEventSeries.getHideStickyHeader());
            case PAGE_ATTRACTIONS:
                trackPageView(position);
                return ExploreListFragment.newInstance(mAttractionDatabaseQuery, ExploreType.EVENT_SERIES_ATTRACTIONS, mEventSeries.getCustomMapTileId(), mEventSeries.getHideStickyHeader());
            default:
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "getItem: No fragment available for position = " + position);
                }
                return null;
        }

    }

    private void trackPageView(int position) {
        if (mEventSeries != null) {
            String name = mEventSeries.getDisplayName();
            CharSequence tabName = getPageTitle(position);

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(tabName)) {
                AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                        AnalyticsUtils.CONTENT_FOCUS_EVENTS, name, name + " " + tabName, null, null, null);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mPageIds.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitles.get(position);
    }

    public int getPagePosition(int pageId) {
        return mPageIds.indexOfValue(pageId);
    }

    public SparseIntArray getPageIds() {
        return mPageIds;
    }

}
