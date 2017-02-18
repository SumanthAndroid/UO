package com.universalstudios.orlandoresort.controller.userinterface.events;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventActivity;

/**
 * @author acampbell
 *
 */
public class EventActivitiesFragment extends Fragment {

    private static final String TAG = EventActivitiesFragment.class.getSimpleName();

    private static final String KEY_ARG_EVENT_ACTIVITIES = "KEY_ARG_EVENT_ACTIVITIES";

    private ListView mListView;
    private EventActivitiesAdapter mAdapter;
    private List<EventActivity> mActivities;

    public static EventActivitiesFragment newInstance(List<EventActivity> activities) {
        EventActivitiesFragment fragment = new EventActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_EVENT_ACTIVITIES, new Gson().toJson(activities));
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
            Type type = new TypeToken<List<EventActivity>>() {}.getType();
            mActivities = new Gson().fromJson(args.getString(KEY_ARG_EVENT_ACTIVITIES), type);
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
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        View view = inflater.inflate(R.layout.fragment_event_activities, container, false);
        mListView = (ListView) view.findViewById(R.id.fragment_event_activities_listview);
        mAdapter = new EventActivitiesAdapter(getActivity(), mActivities);
        mListView.setAdapter(mAdapter);

        return view;
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
