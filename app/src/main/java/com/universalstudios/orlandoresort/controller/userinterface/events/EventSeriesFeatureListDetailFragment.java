/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.FeatureListUtils;
import com.universalstudios.orlandoresort.controller.userinterface.photoframe.PhotoFrameActivity;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrameExperience;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author acampbell
 *
 */
public class EventSeriesFeatureListDetailFragment extends DatabaseQueryFragment implements OnClickListener {

    private static final String TAG = EventSeriesFeatureListDetailFragment.class.getSimpleName();

    private static final String VIEW_TAG_EVENT_SERIES_DATES = "VIEW_TAG_EVENT_SERIES_DATES";
    private static final String VIEW_TAG_EVENT_SERIES_TIMES = "VIEW_TAG_EVENT_SERIES_TIMES";
    private static final String VIEW_TAG_EVENT_SERIES_TICKET = "VIEW_TAG_EVENT_TICKET";
    private static final String VIEW_TAG_EVENT_SERIES_TICKET_DISCLAIMER = "VIEW_TAG_EVENT_TICKET_DISCLAIMER";
    private static final String VIEW_TAG_EVENT_SERIES_DISCLAIMER = "VIEW_TAG_EVENT_DISCLAIMER";

    private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

    private LinearLayout mFeatureListLayout;
    private EventSeries mEventSeries;

    public static EventSeriesFeatureListDetailFragment newInstance(DatabaseQuery databaseQuery) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "newInstance: databaseQuery = " + databaseQuery);
        }

        // Create a new fragment instance
        EventSeriesFeatureListDetailFragment fragment = new EventSeriesFeatureListDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_event_series_detail_feature_list, container,
                false);

        // Setup Views
        mFeatureListLayout = (LinearLayout) fragmentView
                .findViewById(R.id.fragment_event_series_detail_feature_list_layout);

        return fragmentView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
     * .support.v4.content.Loader, java.lang.Object)
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onLoadFinished");
        }

        if (loader.getId() == LOADER_ID_DATABASE_QUERY) {
            // Clear the feature options and load new ones
            mFeatureListLayout.removeAllViews();

            if (data != null && data.moveToFirst()) {
                String eventSeriesObjectJson = data.getString(data
                        .getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
                mEventSeries = GsonObject.fromJson(eventSeriesObjectJson, EventSeries.class);
                addFeatures(mEventSeries);
            }

            if (null != mEventSeries) {
                Bundle photoFrameArgs = new Bundle();

                List<Long> tempIds = mEventSeries.getPhotoFrameExperienceIds();
                if (null != tempIds && !tempIds.isEmpty()) {
                    DatabaseQuery photoFrameExperienceDatabaseQuery = DatabaseQueryUtils.getPhotoFrameExperienceDatabaseQuery(tempIds);
                    photoFrameArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, photoFrameExperienceDatabaseQuery.toJson());

                    getLoaderManager().initLoader(LoaderUtils.LOADER_ID_PHOTOFRAME_EXPERIENCE, photoFrameArgs, this);
                }
            }
        } else if (loader.getId() == LoaderUtils.LOADER_ID_PHOTOFRAME_EXPERIENCE) {
            List<PhotoFrameExperience> photoFrameExperiences = new ArrayList<>();
            if (null != data && data.moveToFirst()) {
                do {
                    String expJson = data.getString(data.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_PHOTOFRAME_EXPERIENCE_JSON));
                    if (!TextUtils.isEmpty(expJson)) {
                        PhotoFrameExperience experience = PhotoFrameExperience.fromJson(expJson, PhotoFrameExperience.class);
                        photoFrameExperiences.add(experience);
                    }
                } while (data.moveToNext());
            }

            addPhotoFrameExperienceFeatures(photoFrameExperiences);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_DATABASE_QUERY:
            case LoaderUtils.LOADER_ID_PHOTOFRAME_EXPERIENCE:
                String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
                DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
                return LoaderUtils.createCursorLoader(databaseQuery);
            default:
                return null;
        }
    }

    /*
         * (non-Javadoc)
         *
         * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
         * .support.v4.content.Loader)
         */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onClick(View v) {
        if (VIEW_TAG_EVENT_SERIES_DATES.equals(v.getTag()) && mEventSeries != null) {
            Bundle args = EventScheduleActivity.newInstanceBundle(mEventSeries.getDisplayName(),
                    mEventSeries.getEventDates());
            startActivity(new Intent(getActivity(), EventScheduleActivity.class).putExtras(args));
        } else if (VIEW_TAG_EVENT_SERIES_TICKET.equals(v.getTag()) && mEventSeries != null) {
            Bundle args = WebViewActivity.newInstanceBundle(R.string.event_ticket_buy_now_web_view_title,
                    mEventSeries.getBuyNowUrl());
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtras(args));
        } else if (v.getTag() instanceof PhotoFrameExperience) {
            PhotoFrameExperience experience = (PhotoFrameExperience) v.getTag();
            startActivity(PhotoFrameActivity.createIntent(getActivity(), experience));
        }
    }

    private void addFeatures(EventSeries eventSeries) {
        View featureView;

        if (eventSeries.getEventDates() != null && !eventSeries.getEventDates().isEmpty()) {
            // Show dates and times for the event series if less than 3
            if (eventSeries.getEventDates().size() <= 3) {
                // Show dates and times for the event
                if (eventSeries.getEventDates().size() == 1) {
                    // Event dates
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_calendar, false, EventUtils.getEventFullDay(
                                    eventSeries.getEventDates().get(0), false,
                                    getString(R.string.event_series_dates_tba)), null, null,
                            (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_SERIES_DATES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                    // Event times
                    featureView = FeatureListUtils.createFeatureItemView(
                            mFeatureListLayout,
                            R.drawable.ic_detail_feature_clock,
                            false,
                            EventUtils.getEventTimeStartSpanLines(eventSeries.getEventDates(),
                                    eventSeries.getDatePlaceholder()), null, null,
                            (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_SERIES_TIMES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                } else {
                    // Event dates
                    boolean useShortStyle = EventUtils
                            .eventDatesSpanMultipleDays(eventSeries.getEventDates());
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_calendar, false, EventUtils.getEventDateSpan(
                                    eventSeries.getEventDates(), useShortStyle,
                                    eventSeries.getDatePlaceholder(), true), null, null, (mFeatureListLayout
                                    .getChildCount() > 0), VIEW_TAG_EVENT_SERIES_DATES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                    // Event times
                    featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                            R.drawable.ic_detail_feature_clock, false, EventUtils.getEventDateStartSpanLines(
                                    eventSeries.getEventDates(),
                                    eventSeries.getDatePlaceholder()), null, EventUtils
                                    .getEventTimeStartSpanLines(eventSeries.getEventDates(),
                                            eventSeries.getDatePlaceholder()), (mFeatureListLayout
                                    .getChildCount() > 0), VIEW_TAG_EVENT_SERIES_TIMES, this);
                    featureView.setClickable(false);
                    mFeatureListLayout.addView(featureView);
                }

            }
            // Show link to full schedule
            else {
                // Event Dates
                featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                        R.drawable.ic_detail_feature_calendar, false,
                        getString(R.string.event_detail_view_full_schedule), EventUtils.getEventDateSpan(
                                eventSeries.getEventDates(), true,
                                eventSeries.getDatePlaceholder()), null,
                        (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_SERIES_DATES, this);
                featureView.setClickable(true);
                mFeatureListLayout.addView(featureView);
            }
        }

        // Event ticket info
        String ticketPrimary = eventSeries.getTicketedEventDetails();
        String ticketSecondary = null;
        if (ticketPrimary != null) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    R.drawable.ic_detail_feature_ticket_gray, false, ticketPrimary, ticketSecondary, null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_SERIES_TICKET, this);
            featureView.setClickable(!TextUtils.isEmpty(ticketSecondary));
            mFeatureListLayout.addView(featureView);
        }

        if(mEventSeries.getDisclaimer() != null && !TextUtils.isEmpty(mEventSeries.getDisclaimer())) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout,
                    null, false, null, mEventSeries.getDisclaimer(), null,
                    (mFeatureListLayout.getChildCount() > 0), VIEW_TAG_EVENT_SERIES_TICKET_DISCLAIMER, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }
    }

    private void addPhotoFrameExperienceFeatures(List<PhotoFrameExperience> experiences) {
        View featureView;

        for (PhotoFrameExperience experience : experiences) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, R.drawable.ic_detail_photoframe, false,
                    experience.getShortDescription(), null, null,
                    (mFeatureListLayout.getChildCount() > 0), experience, this);
            featureView.setClickable(true);
            mFeatureListLayout.addView(featureView);
        }

        // Event disclaimer
        if (!TextUtils.isEmpty(mEventSeries.getDisclaimer())) {
            featureView = FeatureListUtils.createFeatureItemView(mFeatureListLayout, null, false, null,
                    mEventSeries.getDisclaimer(), null, (mFeatureListLayout.getChildCount() > 0),
                    VIEW_TAG_EVENT_SERIES_DISCLAIMER, this);
            featureView.setClickable(false);
            mFeatureListLayout.addView(featureView);
        }
    }
}
