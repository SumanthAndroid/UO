package com.universalstudios.orlandoresort.controller.userinterface.events;

import java.lang.reflect.Type;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;

/**
 * @author acampbell
 *
 */
public class EventScheduleActivity extends NetworkRefreshActivity {

    private static final String TAG = EventScheduleActivity.class.getSimpleName();

    private static final String KEY_ARG_EVENT_DATES = "KEY_ARG_EVENT_DATES";
    private static final String KEY_ARG_EVENT_SERIES_NAME = "KEY_ARG_EVENT_SERIES_NAME";

    private List<EventDate> mEventDates;
    private String mEventSeriesName;

    public static Bundle newInstanceBundle(String eventSeriesName, List<EventDate> eventDates) {
        Bundle args = new Bundle();
        args.putString(KEY_ARG_EVENT_DATES, new Gson().toJson(eventDates));
        args.putString(KEY_ARG_EVENT_SERIES_NAME, eventSeriesName);

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        setContentView(R.layout.activity_event_schedule);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {
            Type type = new TypeToken<List<EventDate>>() {}.getType();
            mEventDates = new Gson().fromJson(args.getString(KEY_ARG_EVENT_DATES), type);
            mEventSeriesName = args.getString(KEY_ARG_EVENT_SERIES_NAME);
        }

        // If this is the first creation, add fragments
        if (savedInstanceState == null) {
            // Track page view
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_EVENTS, mEventSeriesName, String.format(
                            AnalyticsUtils.CONTENT_SUB_2_EVENT_SERIES_SCHEDULE_FORMAT, mEventSeriesName),
                    null, null, null);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_event_schedule_fragment_container,
                            EventScheduleFragment.newInstance(mEventDates)).commit();
        }
        // Otherwise, restore state
        else {

        }

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.event_full_schedule);
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
