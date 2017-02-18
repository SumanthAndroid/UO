package com.universalstudios.orlandoresort.controller.userinterface.events;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRefreshActivity;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventActivity;

/**
 * @author acampbell
 *
 */
public class EventActivitiesActivity extends NetworkRefreshActivity {

    private static final String TAG = EventActivitiesActivity.class.getSimpleName();

    private static final String KEY_ARG_EVENT_ACTIVITIES = "KEY_ARG_EVENT_ACTIVITIES";
    private static final String KEY_ARG_TITLE = "KEY_ARG_TITLE";

    private List<EventActivity> mActivities;
    private String mTitle;

    public static Bundle newInstanceBundle(String title, List<EventActivity> activities) {
        Bundle args = new Bundle();
        args.putString(KEY_ARG_EVENT_ACTIVITIES, new Gson().toJson(activities));
        args.putString(KEY_ARG_TITLE, title);

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }
        setContentView(R.layout.activity_event_activities);

        // Default parameters
        Bundle args = getIntent().getExtras();
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {
            Type type = new TypeToken<List<EventActivity>>() {}.getType();
            mActivities = new Gson().fromJson(args.getString(KEY_ARG_EVENT_ACTIVITIES), type);
            mTitle = args.getString(KEY_ARG_TITLE);
        }

        // If this is the first creation, add fragments
        if (savedInstanceState == null) {
            // Track page view
            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_EVENTS,
                    String.format(AnalyticsUtils.CONTENT_SUB_1_EVENTS_ACTIVITIES_FORMAT, mTitle), null, null,
                    null, null);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_event_activities_fragment_container,
                            EventActivitiesFragment.newInstance(mActivities)).commit();
        }
        // Otherwise, restore state
        else {

        }

        // Set action bar title
        getActionBar().setTitle(mTitle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onOptionsItemSelected");
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
