/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.events;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.GsonBuilder;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.FilterTabOption;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

/**
 * @author acampbell
 *
 */
public class EventSeriesDetailActivity extends NetworkRefreshActivity {

    private static final String TAG = EventSeriesActivity.class.getSimpleName();

    private static final String KEY_ARG_EVENT_SERIES_OBJECT_JSON = "KEY_ARG_EVENT_SERIES_OBJECT_JSON";

    private String mEventSeriesObjectJson;
    public String selectedTabStr = "none";

    public static Bundle newInstanceBundle(String eventSeriesObjectJson) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "newInstanceBundle");
        }

        // Create new arguments bundle
        Bundle args = new Bundle();
        args.putString(KEY_ARG_EVENT_SERIES_OBJECT_JSON, eventSeriesObjectJson);

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_event_series_detail);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {
            mEventSeriesObjectJson = args.getString(KEY_ARG_EVENT_SERIES_OBJECT_JSON);
        }

        // If this is the first creation, add fragments
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_event_series_detail_fragment_container,
                            EventSeriesDetailFragment.newInstance(mEventSeriesObjectJson)).commit();
        }
        // Otherwise, restore state
        else {

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
            case R.id.action_map:
                EventSeries eventSeries = GsonObject.fromJson(mEventSeriesObjectJson, EventSeries.class);
                List<Long> poiIdList = new ArrayList<Long>();
                List<String> evSeriesList = new ArrayList<String>();
                List<String> evAttrList = new ArrayList<String>();

                // If the is a filter tab bar, filter by the tabs that are on
                List<FilterTabOption> filterBarTabOptionsOn = new ArrayList<FilterTabOption>();

                if (eventSeries != null) {

                    if (eventSeries.getEvents() != null) {
                        for (Event event : eventSeries.getEvents()) {
                            poiIdList.add(event.getId());
                            evSeriesList.add(event.getId().toString());
                        }
                    }

                    if (eventSeries.getAttractions() != null) {
                        for (PointOfInterest poi : eventSeries.getAttractions()) {
                            poiIdList.add(poi.getId());
                            evAttrList.add(poi.getId().toString());
                        }
                    }

                    if(evSeriesList.size() == 0 && evAttrList.size() == 0){
                        filterBarTabOptionsOn = null;
                    } else {
                        String optionTitle1 = eventSeries.getEventListDisplayName() != null ? eventSeries.getEventListDisplayName().toUpperCase() : "";
                        String optionTitle2 = eventSeries.getAttractionListDisplayName() != null ? eventSeries.getAttractionListDisplayName().toUpperCase() : "";

                        FilterTabOption option1 = new FilterTabOption(optionTitle1, R.drawable.ic_tab_filter_event_series_events, true, UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_ALIAS_POI_ID, (String[]) evSeriesList.toArray(new String[evSeriesList.size()]));
                        FilterTabOption option2 = new FilterTabOption(optionTitle2, R.drawable.ic_tab_filter_event_series_keyarea, true, UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_ALIAS_POI_ID, (String[]) evAttrList.toArray(new String[evAttrList.size()]));

                        filterBarTabOptionsOn.add(option1);
                        filterBarTabOptionsOn.add(option2);
                    }
                }

                DatabaseQuery databaseQuery = DatabaseQueryUtils.getPointsOfInterestById(poiIdList,
                        new FilterOptions(filterBarTabOptionsOn, ExploreType.EVENT_MAP));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_event_series_detail_fragment_container,
                                EventSeriesMapFragment.newInstance(databaseQuery, ExploreType.EVENT_MAP, filterBarTabOptionsOn, eventSeries.getCustomMapTileId()))
                        .commit();

                return true;
            case R.id.action_list:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_event_series_detail_fragment_container,
                                EventSeriesDetailFragment.newInstance(mEventSeriesObjectJson)).commit();
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

    public void setSelectedTabId(String tabStr){
        selectedTabStr = tabStr;

    }
}
