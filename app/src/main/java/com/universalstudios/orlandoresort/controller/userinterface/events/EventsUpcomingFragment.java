package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.list.ExploreListFragment;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.view.TintUtils;

/**
 * @author acampbell
 *
 */
public class EventsUpcomingFragment extends Fragment {

    private static final String TAG = EventsUpcomingFragment.class.getSimpleName();

    private static final String KEY_ARG_DATABASE_QUERY_JSON = "KEY_ARG_DATABASE_QUERY_JSON";

    private DatabaseQuery mDatabaseQuery;
    private View mFragmentContainerView;
    private DrawerStateProvider mParentDrawerStateProvider;

    public static EventsUpcomingFragment newInstance(DatabaseQuery databaseQuery) {
        EventsUpcomingFragment fragment = new EventsUpcomingFragment();
        Bundle args = new Bundle();
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
            mDatabaseQuery = null;
        }
        // Otherwise, set incoming parameters
        else {
            String databaseQueryJson = args.getString(KEY_ARG_DATABASE_QUERY_JSON);
            if (databaseQueryJson != null) {
                mDatabaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
            }
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}
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
        View view = inflater.inflate(R.layout.fragment_events_upcoming, container, false);

        // Load explore list fragment
        mFragmentContainerView = view.findViewById(R.id.fragment_events_upcoming_fragment_container);
        if (mFragmentContainerView != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(mFragmentContainerView.getId(),
                            ExploreListFragment.newInstance(mDatabaseQuery, ExploreType.UPCOMING_EVENTS, (Long) null))
                    .commit();
        }

        return view;
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
            menuItem = menu.findItem(R.id.action_event_series_carousel);
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

        inflater.inflate(R.menu.action_events_upcoming, menu);
        TintUtils.tintAllMenuItems(menu, getContext());
    }

}
