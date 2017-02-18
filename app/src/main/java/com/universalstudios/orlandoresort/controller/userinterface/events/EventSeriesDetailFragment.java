package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.RadioButton;
import com.universalstudios.orlandoresort.view.scrollview.ScrollView;
import com.universalstudios.orlandoresort.view.segmentedcontrol.SegmentedGroup;

/**
 * @author acampbell
 *
 */
public class EventSeriesDetailFragment extends DatabaseQueryFragment implements OnCheckedChangeListener,
        PicassoProvider {

    private static final String TAG = EventSeriesDetailFragment.class.getSimpleName();

    private static final String KEY_ARG_EVENT_SERIES_OBJECT_JSON = "KEY_ARG_EVENT_SERIES_OBJECT_JSON";
    private static final String KEY_STATE_PAGE_INDEX = "KEY_STATE_PAGE_INDEX";

    private String mEventSeriesObjectJson;
    private Picasso mPicasso;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private RadioButton mAboutRadioButton;
    private RadioButton mActivitiesRadioButton;
    private RadioButton mAttractionsRadioButton;
    private EventSeriesDetailFragmentPagerAdapter mAdapter;
    private View mTabSegmentedControl;
    private View mImageContainer;
    private ScrollView mRootScrollView;
    private SegmentedGroup mSegmentedGroup;
    private int mPageIndex;


    public static EventSeriesDetailFragment newInstance(String eventSeriesObjectJson) {
        EventSeriesDetailFragment fragment = new EventSeriesDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_EVENT_SERIES_OBJECT_JSON, eventSeriesObjectJson);
        EventSeries eventSeries = GsonObject.fromJson(eventSeriesObjectJson, EventSeries.class);
        DatabaseQuery databaseQuery = DatabaseQueryUtils.getEventSeriesDetailDatabaseQuery(eventSeries
                .getId());
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

        } else {
            mEventSeriesObjectJson = args.getString(KEY_ARG_EVENT_SERIES_OBJECT_JSON);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

        }
        // Otherwise, restore state
        else {

        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Setup views
        View view = inflater.inflate(R.layout.fragment_event_series_detail, container, false);

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
        mPicasso = new Picasso.Builder(getActivity())
                .loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();

        // Setup views
        mRootScrollView = (ScrollView) view.findViewById(R.id.fragment_event_series_detail_root_scrollview);
        mImageContainer = view.findViewById(R.id.detail_images_and_title_container);
        mTabSegmentedControl = view.findViewById(R.id.fragment_event_series_detail_segmented_control);
        mAboutRadioButton = (RadioButton) view.findViewById(R.id.fragment_event_series_detail_about);
        mActivitiesRadioButton = (RadioButton) view
                .findViewById(R.id.fragment_event_series_detail_activities);
        mAttractionsRadioButton = (RadioButton) view
                .findViewById(R.id.fragment_event_series_detail_attractions);
        mSegmentedGroup = (SegmentedGroup) view
                .findViewById(R.id.fragment_event_series_detail_segmented_control);
        mSegmentedGroup.setTintColor(getResources().getColor(R.color.text_white));

        // Hide tabs
        mAboutRadioButton.setVisibility(View.GONE);
        mActivitiesRadioButton.setVisibility(View.GONE);
        mAttractionsRadioButton.setVisibility(View.GONE);

        mAboutRadioButton.setOnCheckedChangeListener(this);
        mActivitiesRadioButton.setOnCheckedChangeListener(this);
        mAttractionsRadioButton.setOnCheckedChangeListener(this);

        EventSeriesImagesAndTitleDetailFragment eventSeriesImagesAndTitleDetailFragment = EventSeriesImagesAndTitleDetailFragment
                .newInstance(getDatabaseQuery());
        getChildFragmentManager().beginTransaction()
                .replace(R.id.detail_images_and_title_container, eventSeriesImagesAndTitleDetailFragment)
                .commit();

        EventSeries eventSeries = GsonObject.fromJson(mEventSeriesObjectJson, EventSeries.class);

        mAdapter = new EventSeriesDetailFragmentPagerAdapter(getActivity(), getChildFragmentManager(),
                getDatabaseQuery(), eventSeries);

        // Adjust tabs based on event series data
        if (mAdapter.getCount() <= 1) {
            mTabSegmentedControl.setVisibility(View.GONE);
        }
        // Set tab names if tab is available
        else {
            SparseIntArray pageIds = mAdapter.getPageIds();
            for (int i = 0; i < pageIds.size(); i++) {
                int pageId = pageIds.get(i);
                switch (pageId) {
                    case EventSeriesDetailFragmentPagerAdapter.PAGE_ABOUT:
                        mAboutRadioButton.setVisibility(View.VISIBLE);
                        break;
                    case EventSeriesDetailFragmentPagerAdapter.PAGE_ACTIVITIES:
                        mActivitiesRadioButton.setText(mAdapter.getPageTitle(i));
                        mActivitiesRadioButton.setVisibility(View.VISIBLE);
                        break;
                    case EventSeriesDetailFragmentPagerAdapter.PAGE_ATTRACTIONS:
                        mAttractionsRadioButton.setText(mAdapter.getPageTitle(i));
                        mAttractionsRadioButton.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mPageIndex = 0;
        }
        // Otherwise, restore state
        else {
            mPageIndex = savedInstanceState.getInt(KEY_STATE_PAGE_INDEX);
        }

        getChildFragmentManager().beginTransaction()
                .replace(R.id.activity_event_series_fragment_container, mAdapter.getItem(mPageIndex))
                .commit();
        if (mPageIndex != 0) {
            toggleFullScreenScroll(true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        if(mAboutRadioButton.getText().toString().toLowerCase().equals(
                ((EventSeriesDetailActivity)getActivity()).selectedTabStr.toLowerCase())){
            mAboutRadioButton.setChecked(true);
            ((EventSeriesDetailActivity)getActivity()).selectedTabStr = "none";
        }

        if(mActivitiesRadioButton.getText().toString().toLowerCase().equals(
                ((EventSeriesDetailActivity)getActivity()).selectedTabStr.toLowerCase())){
            mActivitiesRadioButton.setChecked(true);
            ((EventSeriesDetailActivity)getActivity()).selectedTabStr = "none";
        }

        if(mAttractionsRadioButton.getText().toString().toLowerCase().equals(
                ((EventSeriesDetailActivity)getActivity()).selectedTabStr.toLowerCase())){
            mAttractionsRadioButton.setChecked(true);
            ((EventSeriesDetailActivity)getActivity()).selectedTabStr = "none";
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_event_series_detail, menu);
        TintUtils.tintAllMenuItems(menu, getContext());
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader,
     * java.lang.Object)
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onLoadFinished");
        }

        if (loader.getId() == LOADER_ID_DATABASE_QUERY) {
            // Update UI based on the Event Series
            if (data != null && data.moveToFirst()) {
                String title = data.getString(data.getColumnIndex(EventSeriesTable.COL_ALIAS_DISPLAY_NAME));
                getActivity().getActionBar().setTitle(title);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mAdapter == null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onCheckedChanged: Pager adapter is null");
            }
            return;
        }
        int selectedTabId;

        // Select page for tab selected
        if (isChecked) {
            int pagePosition = -1;
            switch (buttonView.getId()) {
                case R.id.fragment_event_series_detail_about:
                    pagePosition = mAdapter.getPagePosition(EventSeriesDetailFragmentPagerAdapter.PAGE_ABOUT);
                    toggleFullScreenScroll(false);
                    break;
                case R.id.fragment_event_series_detail_activities:
                    pagePosition = mAdapter
                            .getPagePosition(EventSeriesDetailFragmentPagerAdapter.PAGE_ACTIVITIES);
                    toggleFullScreenScroll(true);
                    break;
                case R.id.fragment_event_series_detail_attractions:
                    pagePosition = mAdapter
                            .getPagePosition(EventSeriesDetailFragmentPagerAdapter.PAGE_ATTRACTIONS);
                    toggleFullScreenScroll(true);
                    break;
                default:
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "onCheckedChanged: Unhandled compound button click");
                    }
                    break;
            }
            if (pagePosition >= 0) {
                mRootScrollView.scrollTo(0, 0);
                mPageIndex = pagePosition;
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_event_series_fragment_container,
                                mAdapter.getItem(pagePosition)).commit();
            } else {
                Log.w(TAG, "onCheckedChanged: Clicked page unavailable");
            }
        }

    }

    /**
     * Toggle the root ScrollView's ability to scroll, also hides the Images container when enabled
     * 
     * @param enabled
     *            if true the root ScrollView will not scroll allowing contained fragments to scroll
     */
    private void toggleFullScreenScroll(boolean enabled) {
        // Hide image fragment, giving scrolling fragments full use of screen
        mImageContainer.setVisibility(enabled ? View.GONE : View.VISIBLE);
        // Disable scrolling for root ScrollView, fragment scrolls
        mRootScrollView.setScrollingEnabled(!enabled);
    }

    @Override
    public Picasso providePicasso() {
        return mPicasso;
    }
}
