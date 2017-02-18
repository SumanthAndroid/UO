package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;

import java.util.Date;

/**
 * 
 * @author acampbell
 *
 */
public class EventSeriesCarouselFragment extends DatabaseQueryFragment implements OnPageChangeListener,
        OnEventSeriesCarouselChildClickListener {

    private static final String TAG = EventSeriesCarouselFragment.class.getSimpleName();

    private static final int VIEW_PAGER_OFF_SCREEN_PAGE_LIMIT = 2;
    private static final String KEY_STATE_PAGE = "KEY_STATE_PAGE";

    private JazzyViewPager mViewPager;
    private EventSeriesCarouselCursorPagerAdapter mAdapter;
    private TextView mPageTitleTextView;
    private DrawerStateProvider mParentDrawerStateProvider;
    private int mPage;
    private View mNoResultsLayout;

    public static EventSeriesCarouselFragment newInstance() {
        EventSeriesCarouselFragment fragment = new EventSeriesCarouselFragment();
        Bundle args = new Bundle();
        DatabaseQuery databaseQuery = DatabaseQueryUtils.getEventSeriesDatabaseQuery();
        args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        fragment.setArguments(args);
        return fragment;
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
            mPage = 0;
        }
        // Otherwise, set incoming parameters
        else {}

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            // Track the page view
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_EVENTS, AnalyticsUtils.CONTENT_SUB_1_EVENT_SERIES_CAROUSEL,
                    null, null, null, null);
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {
            mPage = savedInstanceState.getInt(KEY_STATE_PAGE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
            mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity != null && activity instanceof DrawerStateProvider) {
            mParentDrawerStateProvider = (DrawerStateProvider) activity;
        }
        // If neither implements the interface, log a warning
        else if (mParentDrawerStateProvider == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG,
                        "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_event_series_carousel, container, false);

        // Setup views
        mViewPager = (JazzyViewPager) fragmentView
                .findViewById(R.id.fragment_event_series_carousel_viewpager);
        mViewPager.setVisibility(View.GONE);
        mPageTitleTextView = (TextView) fragmentView
                .findViewById(R.id.fragment_event_series_carousel_page_title_textview);
        mPageTitleTextView.setVisibility(View.GONE);
        mNoResultsLayout = fragmentView.findViewById(R.id.fragment_event_series_carousel_no_results_layout);

        int itemHorizontalPadding = getResources().getDimensionPixelSize(
                R.dimen.events_carousel_item_horizontal_padding) * 2;
        mAdapter = new EventSeriesCarouselCursorPagerAdapter(this, getActivity(), null);
        mViewPager.setOffscreenPageLimit(VIEW_PAGER_OFF_SCREEN_PAGE_LIMIT);
        mViewPager.setPageMargin(-itemHorizontalPadding);
        mViewPager.setClipChildren(false);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }

        if (mViewPager != null) {
            outState.putInt(KEY_STATE_PAGE, mViewPager.getCurrentItem());
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onViewStateRestored");
        }

        // Enable action bar menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPrepareOptionsMenu");
        }

        // If the menu drawer is open, hide action items related to the main
        // content view, and show action items specific to the menu
        if (mParentDrawerStateProvider != null) {
            boolean isDrawerOpenAtAll = mParentDrawerStateProvider.isDrawerOpenAtAll();

            MenuItem menuItem;
            menuItem = menu.findItem(R.id.action_event_timeline);
            if (menuItem != null) {
                menuItem.setVisible(!isDrawerOpenAtAll).setEnabled(!isDrawerOpenAtAll);
            }
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }

        inflater.inflate(R.menu.action_event_series_carousel, menu);
        TintUtils.tintAllMenuItems(menu, getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished");
        }

        if (LOADER_ID_DATABASE_QUERY == loader.getId()) {
            mAdapter.swapCursor(data);
            // Find first event series that has not passed
            long timeNowUnix = new Date().getTime() / 1000;
            if (data != null && data.moveToFirst() && mPage == 0) {
                mPage = -1;
                do {
                    mPage++;
                    EventSeries eventSeries = GsonObject.fromJson(data.getString(data
                            .getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON)),
                            EventSeries.class);
                    EventDate endDate;
                    if (eventSeries.getEvents() != null && !eventSeries.getEvents().isEmpty()
                            && eventSeries.getEvents().size() == 1) {
                        endDate = EventUtils.getEndDate(eventSeries.getEvents().get(0).getEventDates());
                    } else {
                        endDate = EventUtils.getEndDate(eventSeries.getEventDates());
                    }
                    if (endDate != null) {
                        if (timeNowUnix <= endDate.getEndDateUnix()) {
                            break;
                        }
                    } else {
                        break;
                    }
                } while (data.moveToNext());
                
                mViewPager.setCurrentItem(mPage);
                onPageSelected(mPage);

                mViewPager.setVisibility(View.VISIBLE);
                mPageTitleTextView.setVisibility(View.VISIBLE);
                mNoResultsLayout.setVisibility(View.GONE);
            } else {
                mNoResultsLayout.setVisibility(View.VISIBLE);
                mPageTitleTextView.setVisibility(View.GONE);
                mViewPager.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }

        if (LOADER_ID_DATABASE_QUERY == loader.getId()) {
            mAdapter.swapCursor(null);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int position) {
        if (mAdapter != null) {
            mPageTitleTextView.setText(mAdapter.getPageTitle(position));
        }
    }

    @Override
    public void onEventCarouselChildClick(View v, String venueObjectJson, String eventSeriesObjectJson) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onEventCarouselChildClick");
        }

        switch (v.getId()) {
            case R.id.list_event_series_carousel_item_root_content_relative_layout:
                EventSeries series = EventSeries.fromJson(eventSeriesObjectJson, EventSeries.class);
                Long mapTileId = null;
                if (null != series) {
                    mapTileId = series.getCustomMapTileId();
                }
                boolean pageLoaded = DetailUtils.openEventSeriesDetailPage(v.getContext(), venueObjectJson,
                        eventSeriesObjectJson, mapTileId);
                if (!pageLoaded) {
                    UserInterfaceUtils.showToastFromForeground(
                            getString(R.string.event_series_unable_to_load_event_series), Toast.LENGTH_LONG,
                            getActivity());
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroyView");
        }

        if (mAdapter != null) {
            mAdapter.destroy();
        }
    }

}
