package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersListView;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author acampbell
 *
 */
public class EventScheduleFragment extends Fragment {

    private static final String TAG = EventScheduleFragment.class.getSimpleName();

    private static final String KEY_ARG_EVENT_DATES = "KEY_ARG_EVENT_DATES";

    private List<EventDate> mEventDates;
    private StickyListHeadersListView mListView;

    public static EventScheduleFragment newInstance(List<EventDate> eventDates) {
        EventScheduleFragment fragment = new EventScheduleFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_EVENT_DATES, new Gson().toJson(eventDates));
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

        }
        // Otherwise, set incoming parameters
        else {
            Type type = new TypeToken<List<EventDate>>() {}.getType();
            mEventDates = new Gson().fromJson(args.getString(KEY_ARG_EVENT_DATES), type);
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
        View view = inflater.inflate(R.layout.fragment_event_schedule, container, false);

        // Setup views
        mListView = (StickyListHeadersListView) view
                .findViewById(R.id.fragment_event_schedule_stickylistview);
        mListView.setAdapter(new EventScheduleAdapter(mEventDates));

        return view;
    }
}
