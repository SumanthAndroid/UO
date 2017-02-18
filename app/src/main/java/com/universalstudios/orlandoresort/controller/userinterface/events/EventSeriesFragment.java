package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.filter.FilterOptions;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

/**
 * Container fragment for the two event views
 *
 * @author acampbell
 *
 */
public class EventSeriesFragment extends Fragment implements ActionBarTitleProvider {

    private static final String TAG = EventSeriesFragment.class.getSimpleName();
    private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
    private static final String KEY_ARG_IN_PARK = "KEY_ARG_IN_PARK";

    private int mActionBarTitleResId;
    private DrawerStateProvider mParentDrawerStateProvider;
    private View mFragmentContainerView;
    private boolean mInPark;

    public static EventSeriesFragment newInstance(int actionBarTitleResId, boolean inPark) {
        EventSeriesFragment fragment = new EventSeriesFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
        args.putBoolean(KEY_ARG_IN_PARK, inPark);
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
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
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
            mActionBarTitleResId = -1;
            mInPark = false;
        }
        // Otherwise, set incoming parameters
        else {
            mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
            mInPark = args.getBoolean(KEY_ARG_IN_PARK);
        }

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Set the action bar title, if the drawer isn't open
        if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                parentActivity.getActionBar().setTitle(provideTitle());
            }
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_event_series, container, false);

        // Load events carousel fragment
        mFragmentContainerView = fragmentView.findViewById(R.id.fragment_event_series_fragment_container);

        Fragment fragment;
        FragmentTransaction  fragmentTransaction = getChildFragmentManager().beginTransaction();

        UniversalAppState uoState = UniversalAppStateManager
                .getInstance();

        if (uoState.isDefaultEventsToEventSeries()) {
            fragment = EventSeriesCarouselFragment.newInstance();

            fragmentTransaction.replace(mFragmentContainerView.getId(), fragment, fragment.getClass()
                    .getName());

            fragmentTransaction.commit();
        } else {

            if (mFragmentContainerView != null) {

                DatabaseQuery timelineDatabaseQuery = DatabaseQueryUtils
                        .getAllUpcomingTimelineEvents(new FilterOptions(null, ExploreType.EVENTS));

                Cursor cursor = getActivity().getContentResolver().query(timelineDatabaseQuery.getContentUri(),
                        timelineDatabaseQuery.getProjection(),
                        timelineDatabaseQuery.getSelection(),
                        timelineDatabaseQuery.getSelectionArgs(),
                        timelineDatabaseQuery.getOrderBy());

                if (cursor.getCount() > 0) {
                    timelineDatabaseQuery = DatabaseQueryUtils
                            .getUpcomingTimelineEvents(new FilterOptions(null, ExploreType.EVENTS));
                    fragment = EventsUpcomingFragment.newInstance(timelineDatabaseQuery);
                } else {
                    fragment = EventSeriesCarouselFragment.newInstance();
                }
                fragmentTransaction.replace(mFragmentContainerView.getId(), fragment, fragment.getClass()
                        .getName());

                fragmentTransaction.commit();
            }
        }

        if(getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(mActionBarTitleResId);
        }
        return fragmentView;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_event_timeline:
                DatabaseQuery timelineDatabaseQuery = DatabaseQueryUtils
                        .getUpcomingTimelineEvents(new FilterOptions(null, ExploreType.UPCOMING_EVENTS));
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(mFragmentContainerView.getId(),
                                EventsUpcomingFragment.newInstance(timelineDatabaseQuery),
                                EventsUpcomingFragment.class.getSimpleName()).commit();
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_event_series_carousel:
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(mFragmentContainerView.getId(), EventSeriesCarouselFragment.newInstance(),
                                EventSeriesCarouselFragment.class.getSimpleName()).commit();
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String provideTitle() {
        return getString(mActionBarTitleResId);
    }

}