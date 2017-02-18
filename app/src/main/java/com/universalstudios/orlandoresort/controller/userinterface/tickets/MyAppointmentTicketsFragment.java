package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by GOKHAN on 8/3/2016.
 */
public class MyAppointmentTicketsFragment extends DatabaseQueryFragment implements AdapterView.OnItemClickListener, ActionBarTitleProvider {

    private static final String TAG = MyAppointmentTicketsFragment.class.getSimpleName();
    private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
    private static final String KEY_ARG_APPOINTMENT_TICKETS_DATABASE_QUERY_JSON = "KEY_ARG_APPOINTMENT_TICKETS_DATABASE_QUERY_JSON";
    private static final int LOADER_ID_APPOINTMENT_TICKETS = LoaderUtils.LOADER_ID_APPOINTMENT_TICKETS;

    private DrawerStateProvider mParentDrawerStateProvider;
    private Integer mActionBarTitleResId;
    private boolean mDoesAnyUnSeenTicketExist;
    private ViewGroup mNoResultsLayout;
    private StickyListHeadersListView mStickyListHeadersListView;

    private List<AppointmentTimes> appointmentTicketList;
    private AppointmentTicketListCursorAdapter mAppointmentTicketListCursorAdapter;
    private CreateAppointmentTimeResponse appointmentTimeTicket;


    public static MyAppointmentTicketsFragment newInstance(int actionBarTitleResId) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        MyAppointmentTicketsFragment fragment = new MyAppointmentTicketsFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
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
            mActionBarTitleResId = null;
        }
        // Otherwise, set incoming parameters
        else {
            mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
        }

        mDoesAnyUnSeenTicketExist = false;

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {

            // Track the page view
            AnalyticsUtils.trackPageView(
                    AnalyticsUtils.CONTENT_GROUP_PLANNING,
                    AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
                    null,
                    AnalyticsUtils.CONTENT_SUB_2_PARK_NOTIFICATIONS,
                    AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
                    null, null);
        }
        // Otherwise restore state, overwriting any passed in parameters
        else {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }
        Bundle AppointmentArgs = new Bundle();
        DatabaseQuery appointmentDatabaseQuery = DatabaseQueryUtils.getAllAppointmentTicketsDatabaseQuery();
        AppointmentArgs.putString(KEY_ARG_APPOINTMENT_TICKETS_DATABASE_QUERY_JSON, appointmentDatabaseQuery.toJson());
        getLoaderManager().initLoader(LOADER_ID_APPOINTMENT_TICKETS, AppointmentArgs, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Set the action bar title, if the drawer isn't open
        if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
            Activity parentActivity = getActivity();
            if (parentActivity != null) {
                parentActivity.getActionBar().setTitle(provideTitle());
            }
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_appointment_tickets, container, false);



        // Setup Views
        mStickyListHeadersListView = (StickyListHeadersListView) fragmentView.findViewById(R.id.fragment_appointment_ticket_list_listview);
        mNoResultsLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_appointment_ticket_list_no_results_layout);

        mAppointmentTicketListCursorAdapter = new AppointmentTicketListCursorAdapter(getActivity(),null);
        mStickyListHeadersListView.setAdapter(mAppointmentTicketListCursorAdapter);
        mStickyListHeadersListView.setOnItemClickListener(this);
       // mStickyListHeadersListView.setOnScrollListener(this);

        // If this is the first creation, default state
        if (savedInstanceState == null) {
            mStickyListHeadersListView.setVisibility(View.VISIBLE);
            mNoResultsLayout.setVisibility(View.GONE);
        }
        else {
        }

        return fragmentView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader: id = " + id);
        }
        switch (id) {

            case LOADER_ID_APPOINTMENT_TICKETS:
                String appointmentTicketDatabaseQueryJson = args.getString(KEY_ARG_APPOINTMENT_TICKETS_DATABASE_QUERY_JSON);
                DatabaseQuery queueDatabaseQuery = DatabaseQuery.fromJson(appointmentTicketDatabaseQueryJson, DatabaseQuery.class);
                return LoaderUtils.createCursorLoader(queueDatabaseQuery);
            default:
                // Otherwise, let the parent class handle it
                return super.onCreateLoader(id, args);
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished: loader.getId() = " + loader.getId());
        }

        switch (loader.getId()) {

            case LOADER_ID_APPOINTMENT_TICKETS:

                mAppointmentTicketListCursorAdapter.swapCursor(data);


                if (data != null && data.moveToFirst()) {
                    appointmentTicketList = new ArrayList<>();

                    do {
                        String appointmentTicketObjectJson = data.getString(data
                                .getColumnIndex(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_OBJECT_JSON));

                       String createdAppointmentTicketObjStr =  data.getString(data
                               .getColumnIndex(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON));

                        AppointmentTimes createdAppointmentTime = GsonObject.fromJson(appointmentTicketObjectJson, AppointmentTimes.class);

                        appointmentTimeTicket = GsonObject.fromJson(createdAppointmentTicketObjStr, CreateAppointmentTimeResponse.class);

                        if (createdAppointmentTime != null) {
                            appointmentTicketList.add(createdAppointmentTime);
                        }

                        Long appointmentId = appointmentTimeTicket.getTicketAppointmentId();
                        // If there is a notification active for this park news
                        // item, dismiss it since the user is viewing the detail
                        Activity parentActivity = getActivity();
                        if (appointmentId != null && parentActivity != null) {
                            NotificationManager notificationManager =
                                    (NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(appointmentId.toString(), NotificationUtils.NOTIFICATION_ID_APPOINTMENT_TICKET);
                        }

                    } while (data.moveToNext());

                    loader.stopLoading();
                }
                // Refresh the action bar items
                invalidateOptionsMenu();
                break;
            default:
                break;
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }

        switch (loader.getId()) {
            case LOADER_ID_DATABASE_QUERY:
                // Data is not available anymore, delete reference
                break;
            default:
                break;
        }

    }

    @Override
    public String provideTitle() {
        if (mActionBarTitleResId == null) {
            return "";
        }
        return getString(mActionBarTitleResId);
    }

    private void invalidateOptionsMenu() {
        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            parentActivity.invalidateOptionsMenu();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = mAppointmentTicketListCursorAdapter.getCursor();
        String appointmentTicket = cursor.getString(cursor.getColumnIndex(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_OBJECT_JSON));

        String createdAppointmentTicketObjStr = cursor.getString(cursor.getColumnIndex(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON));

        Bundle args = ReturnTimeTicketDoneActivity.newInstanceBundle(appointmentTicket,createdAppointmentTicketObjStr,true);
        startActivity(new Intent(getActivity(), ReturnTimeTicketDoneActivity.class).putExtras(args));

    }

    @Override
    public void onStop() {
        super.onStop();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStop");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateOptionsMenu");
        }
        inflater.inflate(R.menu.action_appointment_ticket_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPrepareOptionsMenu");
        }
        boolean isDrawerOpenAtAll = mParentDrawerStateProvider.isDrawerOpenAtAll();

        MenuItem menuItem = menu.findItem(R.id.action_mark_all_as_read);
        if (menuItem != null) {
            menuItem.setVisible(!isDrawerOpenAtAll && mDoesAnyUnSeenTicketExist)
                    .setEnabled(!isDrawerOpenAtAll && mDoesAnyUnSeenTicketExist);
        }

        super.onPrepareOptionsMenu(menu);
    }
}
