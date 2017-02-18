package com.universalstudios.orlandoresort.model.network.domain.preloadingdb;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.events.GetEventSeriesResponse;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Sync Event erices with Database for pre-loading
 *
 * Created by GOKHAN on 6/21/2016.
 */
@Deprecated
//FIXME MOVE THIS LOGIC BACK TO THE REQUEST CLASSES (STB)
public class SyncEventSeriesWithDatabase {

    private static final String TAG = SyncEventSeriesWithDatabase.class.getSimpleName();
    private GetEventSeriesResponse getEventSeriesResponse;

    public SyncEventSeriesWithDatabase(GetEventSeriesResponse getEventSeriesResponse){
        this.getEventSeriesResponse = getEventSeriesResponse;
    }

    public void syncEventSeries(List<EventSeries> eventSeriesList) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "syncEventSeriesWithDatabase:");
        }

        if (eventSeriesList == null) {
            return;
        }

        // Query for Event series of this type
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        Cursor cursor = contentResolver.query(UniversalOrlandoContentUris.EVENT_SERIES, null, null, null,
                null);

        // List to track Event series to be inserted
        List<EventSeries> newEventSeriesList = new ArrayList<>();

        int index = 0;
        for (EventSeries eventSeriesFromResp : eventSeriesList) {
            boolean eventSeriesfoundInDb = false;
            // Add server sort order to event series
            eventSeriesFromResp.setSortOrder(index);

            // Start the cursor at the first row
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long eventSeriesId = cursor
                            .getLong(cursor.getColumnIndex(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_ID));
                    if (eventSeriesId == eventSeriesFromResp.getId()) {

                        updateEventSeriesInDatabase(eventSeriesFromResp, contentResolver);

                        // Stop looping after finding the Event series
                        eventSeriesfoundInDb = true;
                        break;
                    }
                } while (cursor.moveToNext());
            }

            if (!eventSeriesfoundInDb) {
                newEventSeriesList.add(eventSeriesFromResp);
            }
            index++;
        }

        // Insert any new Event series
        insertEventSeriesInDatabase(newEventSeriesList, contentResolver);

        // Delete any Event series not in this list
        deleteOldEventSeriesFromDatabase(eventSeriesList, contentResolver);

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private void updateEventSeriesInDatabase(EventSeries eventSeriesToUpdate,
                                                    ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "updateEventSeriesInDatabase: updating event series = "
                    + eventSeriesToUpdate.getDisplayName());
        }

        ContentValues contentValues = createEventSeriesContentValues(eventSeriesToUpdate);
        String seelction = UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_ID + " = '" +
                eventSeriesToUpdate.getId() + "'";

        try {
            contentResolver.update(UniversalOrlandoContentUris.EVENT_SERIES, contentValues, seelction, null);
            // Delete event series times and insert new times
            deleteSingleEventSeriesTimesFromDatabase(eventSeriesToUpdate, contentResolver);
            insertEventSeriesTimesInDatabase(Collections.singletonList(eventSeriesToUpdate), contentResolver);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "updateEventSeriesInDatabase: exception updating event series in the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private void deleteOldEventSeriesFromDatabase(List<EventSeries> newEventSeriesList,
                                                         ContentResolver contentResolver) {
        if (newEventSeriesList == null) {
            return;
        }

        StringBuilder selection = new StringBuilder("1");

        if (!newEventSeriesList.isEmpty()) {
            selection = new StringBuilder(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_ID).append(" NOT IN (");
            for (int i = 0; i < newEventSeriesList.size(); i++) {
                EventSeries eventSeries = newEventSeriesList.get(i);
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(eventSeries.getId()).append("'");
            }
            selection.append(")");
        }
        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.EVENT_SERIES,
                    selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteOldEventSeriesFromDatabase: old event series removed = " + rowsDeleted);
            }

            // Delete event series times
            deleteEventSeriesTimesFromDatabase(newEventSeriesList, contentResolver);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG,
                        "deleteOldEventSeriesFromDatabase: exception deleting old event series in the database",
                        e);
            }
        }
    }

    private void insertEventSeriesInDatabase(List<EventSeries> newEventSeriesList,
                                                    ContentResolver contentResolver) {
        if (newEventSeriesList == null || newEventSeriesList.isEmpty()) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG,
                    "insertEventSeriesInDatabase: inserting new event series = " + newEventSeriesList.size());
        }

        ContentValues[] contentValuesArray = new ContentValues[newEventSeriesList.size()];
        for (int i = 0; i < newEventSeriesList.size(); i++) {
            EventSeries eventSeries = newEventSeriesList.get(i);
            contentValuesArray[i] = createEventSeriesContentValues(eventSeries);
        }

        try {
            contentResolver.bulkInsert(UniversalOrlandoContentUris.EVENT_SERIES, contentValuesArray);
            // Insert event series times
            insertEventSeriesTimesInDatabase(newEventSeriesList, contentResolver);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertEventSeriesInDatabase: exception inserting event series into the database",
                        e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private ContentValues createEventSeriesContentValues(EventSeries eventSeries) {
        ContentValues contentValues = new ContentValues();
        int flags = 0;
        flags = (eventSeries.isDisplayInNavigation() ? flags | EventSeries.FLAG_SHOW_IN_NAV : flags);
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_FLAGS, flags);
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_ID, eventSeries.getId());
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_DISPLAY_NAME, eventSeries.getDisplayName());
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_SORT_ORDER, eventSeries.getSortOrder());
        // Use event venue if there is only a single event
        if (eventSeries.getEvents() != null && eventSeries.getEvents().size() == 1
                && eventSeries.getEvents().get(0) != null
                && eventSeries.getEvents().get(0).getVenueId() != null) {
            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_VENUE_ID, eventSeries.getEvents().get(0).getVenueId());
        } else {
            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_VENUE_ID, eventSeries.getVenueId());
        }
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_LIST_IMAGE_URL, eventSeries.getHeroImageUrl());
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_THUMBNAIL_IMAGE_URL, eventSeries.getThumbnailImageUrl());
        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON, eventSeries.toJson());
        if (eventSeries.getEvents() != null && !eventSeries.getEvents().isEmpty()) {
            StringBuilder eventIds = new StringBuilder();
            for (int i = 0; i < eventSeries.getEvents().size(); i++) {
                Event event = eventSeries.getEvents().get(i);
                if (i > 0) {
                    eventIds.append(", ");
                }
                eventIds.append(event.getId());
            }
            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_IDS, eventIds.toString());
        }
        if (eventSeries.getAttractions() != null && !eventSeries.getAttractions().isEmpty()) {
            StringBuilder attractionIds = new StringBuilder();
            for (int i = 0; i < eventSeries.getAttractions().size(); i++) {
                PointOfInterest attraction = eventSeries.getAttractions().get(i);
                if (i > 0) {
                    attractionIds.append(", ");
                }
                attractionIds.append(attraction.getId());
            }
            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_ATTRACTION_IDS, attractionIds.toString());
        }

        return contentValues;
    }

    private ContentValues[] createEventSeriesTimeContentValues(List<EventSeries> eventSeriesList) {
        List<ContentValues> contentValuesList = new ArrayList<>();

        for (EventSeries eventSeries : eventSeriesList) {
            if (eventSeries.getEventDates() != null) {
                // Use event dates if this event series only has a single event
                if (eventSeries.getEvents() != null && eventSeries.getEvents().size() == 1) {
                    if (eventSeries.getEvents().get(0) != null
                            && eventSeries.getEvents().get(0).getEventDates() != null)
                        for (EventDate eventDate : eventSeries.getEvents().get(0).getEventDates()) {
                            ContentValues contentValues = new ContentValues();
                            int flags = 0;
                            flags = (eventSeries.isDisplayInNavigation() ? flags | EventSeries.FLAG_SHOW_IN_NAV : flags);
                            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_FLAGS, flags);
                            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_EVENT_SERIES_ID, eventSeries.getId());
                            if (eventDate.getStartDateUnix() != null && eventDate.getStartDateUnix() > 0) {
                                contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_START_DATE,
                                        eventDate.getStartDateUnix());
                            } else {
                                contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_START_DATE,
                                        9223372036854775806L);
                            }
                            if (eventDate.getEndDateUnix() != null) {
                                contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_END_DATE,
                                        eventDate.getEndDateUnix());
                            }
                            contentValuesList.add(contentValues);
                        }
                } else {
                    for (EventDate eventDate : eventSeries.getEventDates()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_EVENT_SERIES_ID, eventSeries.getId());
                        if (eventDate.getStartDateUnix() != null && eventDate.getStartDateUnix() > 0) {
                            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_START_DATE,
                                    eventDate.getStartDateUnix());
                        } else {
                            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_START_DATE,
                                    9223372036854775806L);
                        }
                        if (eventDate.getEndDateUnix() != null) {
                            contentValues.put(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_END_DATE, eventDate.getEndDateUnix());
                        }
                        contentValuesList.add(contentValues);
                    }
                }
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    private void insertEventSeriesTimesInDatabase(List<EventSeries> eventSeriesList,
                                                         ContentResolver contentResolver) {
        if (eventSeriesList == null || eventSeriesList.isEmpty()) {
            return;
        }

        ContentValues[] contentValues = createEventSeriesTimeContentValues(eventSeriesList);

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertEventSeriesTimesInDatabase: inserting new event series times = "
                    + contentValues.length);
        }

        if (contentValues.length > 0) {
            try {
                int rowsInserted = contentResolver.bulkInsert(UniversalOrlandoContentUris.EVENT_SERIES_TIMES,
                        contentValues);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "insertEventSeriesTimesInDatabase: content values count = "
                            + contentValues.length);
                    Log.d(TAG, "insertEventSeriesTimesInDatabase: " + rowsInserted
                            + " event series times inserted");
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG,
                            "insertEventTimesInDatabase: exception inserting event series times into the database",
                            e);
                }
                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
    }

    private void deleteSingleEventSeriesTimesFromDatabase(EventSeries eventSeries,
                                                                 ContentResolver contentResolver) {
        if (eventSeries == null) {
            return;
        }
        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_EVENT_SERIES_ID).append(" = '")
                .append(eventSeries.getId()).append("'");
        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.EVENT_SERIES_TIMES,
                    selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteSingleEventSeriesTimesFromDatabase: event series times removed = "
                        + rowsDeleted);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG,
                        "deleteSingleEventSeriesTimesFromDatabase: exception deleting event series times in the database",
                        e);
            }
        }
    }

    private void deleteEventSeriesTimesFromDatabase(List<EventSeries> eventSeriesList,
                                                           ContentResolver contentResolver) {
        if (eventSeriesList == null || eventSeriesList.isEmpty()) {
            return;
        }

        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.EventSeriesTimesTable.COL_EVENT_SERIES_ID)
                .append(" NOT IN (");
        for (int i = 0; i < eventSeriesList.size(); i++) {
            EventSeries eventSeries = eventSeriesList.get(i);
            if (i > 0) {
                selection.append(", ");
            }
            selection.append("'").append(eventSeries.getId()).append("'");
        }
        selection.append(")");

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.EVENT_SERIES_TIMES,
                    selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteEventSeriesTimesFromDatabase: event series times removed = " + rowsDeleted);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG,
                        "deleteEventSeriesTimesFromDatabase: exception deleting event series times in the database",
                        e);
            }
        }
    }

    public void successCaseEventSeries(){

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "success");
        }

        if (getEventSeriesResponse != null) {
            // Sync event series to the database
            syncEventSeries(getEventSeriesResponse.getEventSeries());

            // Store the last time event series were synced
            UniversalAppState universalOrlandoState = UniversalAppStateManager
                    .getInstance();
            universalOrlandoState.setDateOfLastEventSeriesSyncInMillis(new Date().getTime());
            UniversalAppStateManager.saveInstance();
        } else {
            getEventSeriesResponse = new GetEventSeriesResponse();
        }
    }
}
