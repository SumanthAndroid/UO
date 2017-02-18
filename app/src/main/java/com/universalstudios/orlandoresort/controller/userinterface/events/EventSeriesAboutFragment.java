package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

public class EventSeriesAboutFragment extends DatabaseQueryFragment {

    private static final String TAG = EventSeriesAboutFragment.class.getSimpleName();

    private TextView mVenueNameText;
    private TextView mNameText;
    private TextView mDescText;
    private TextView mDateText;

    public static EventSeriesAboutFragment newInstance(DatabaseQuery databaseQuery) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance");
        }

        // Create a new fragment instance
        EventSeriesAboutFragment fragment = new EventSeriesAboutFragment();

        // Get arguments passed in, if any
        Bundle args = new Bundle();
        args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {

        }
        // Otherwise, set incoming parameters
        else {

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
        // Inflate the fragment layout into the container
        View view = inflater.inflate(R.layout.fragment_event_series_about, container, false);

        mVenueNameText = (TextView) view.findViewById(R.id.fragment_event_series_about_venue_text);
        mNameText = (TextView) view.findViewById(R.id.fragment_event_series_about_name_text);
        mDescText = (TextView) view.findViewById(R.id.fragment_event_series_about_desc_text);
        mDateText = (TextView) view.findViewById(R.id.fragment_event_series_about_date_text);

        mVenueNameText.setVisibility(View.INVISIBLE);
        mNameText.setVisibility(View.INVISIBLE);
        mDescText.setVisibility(View.INVISIBLE);
        mDateText.setVisibility(View.INVISIBLE);

        EventSeriesFeatureListDetailFragment eventSeriesFetureListDetailFragment = EventSeriesFeatureListDetailFragment
                .newInstance(getDatabaseQuery());
        getFragmentManager().beginTransaction()
                .replace(R.id.detail_feature_list_container, eventSeriesFetureListDetailFragment).commit();

        return view;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onLoadFinished");
        }

        if (loader.getId() == LOADER_ID_DATABASE_QUERY) {

            if (data != null && data.moveToFirst()) {
                String eventSeriesObjectJson = data.getString(data
                        .getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
                String venueName = data.getString(data.getColumnIndex(VenuesTable.COL_ALIAS_DISPLAY_NAME));
                // Default venue name if null
                venueName = venueName == null ? getString(R.string.event_venue_name_default) : venueName;
                EventSeries eventSeries = GsonObject.fromJson(eventSeriesObjectJson, EventSeries.class);

                mVenueNameText.setText(venueName);
                mNameText.setText(eventSeries.getDisplayName() == null ? "" : eventSeries.getDisplayName());
                mDescText.setText(eventSeries.getShortDescription());
                mDateText.setText(EventUtils.getEventDateSpan(eventSeries.getEventDates(),
                        true, eventSeries.getDatePlaceholder(), true));

                mVenueNameText.setVisibility(View.VISIBLE);
                mNameText.setVisibility(View.VISIBLE);
                mDescText.setVisibility(View.VISIBLE);
                mDateText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}
