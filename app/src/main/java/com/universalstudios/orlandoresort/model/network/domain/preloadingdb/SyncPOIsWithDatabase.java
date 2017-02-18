package com.universalstudios.orlandoresort.model.network.domain.preloadingdb;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.Gson;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dining;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Entertainment;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisResponse;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Show;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit.client.Response;

/**
 * Sync POIs data with Database for pre loading
 * Created by GOKHAN on 6/21/2016.
 */
public class SyncPOIsWithDatabase {

    private final String TAG = SyncPOIsWithDatabase.class.getSimpleName();

    public void syncPoisWithDatabase(List<? extends PointOfInterest> poiList, int poiTypeId) {
        if (poiList == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "syncPoisWithDatabase: null poiList for poiTypeId = " + poiTypeId);
            }
            return;
        }

        // Query for POIs of this type
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        String selection = UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_TYPE_ID +
                " = '" + poiTypeId + "'";
        Cursor poisCursor = contentResolver.query(UniversalOrlandoContentUris.POINTS_OF_INTEREST, null, selection, null, null);

        // List to track POIs that need to be inserted
        List<PointOfInterest> newPoisList = new ArrayList<>();

        int index = 0;
        for (PointOfInterest poiFromResp : poiList) {
            boolean poiFoundInDb = false;
            // Add server sort order to event series
            poiFromResp.setSortOrder(index);

            // Start the cursor at the first row
            if (poisCursor != null && poisCursor.moveToFirst()) {
                // Go through every POI in the database
                do {
                    long poiId = poisCursor.getLong(poisCursor.getColumnIndex(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_ID));

                    // If the POI is found, check to see if it needs to be updated
                    if (poiId == poiFromResp.getId()) {
                        String poiObjectJson = poisCursor.getString(poisCursor.getColumnIndex(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_OBJECT_JSON));

                        // If the POI in the database is different than the new POI, update the database
                        if (!PointOfInterest.arePoisEqual(poiFromResp, poiTypeId, poiObjectJson)) {

                            // Copy over the transient state so it doesn't get overwritten
                            PointOfInterest poiFromDb = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            poiFromResp.setIsFavorite(poiFromDb.getIsFavorite());
                            updatePoiInDatabase(poiFromResp, poiTypeId, contentResolver);
                        }

                        // Stop looping after finding the POI
                        poiFoundInDb = true;
                        break;
                    }
                } while (poisCursor.moveToNext());
            }

            // If the POI wasn't found, add it to the list to be inserted
            if (!poiFoundInDb) {
                newPoisList.add(poiFromResp);
            }
            index++;
        }

        // Insert any new POIs
        insertPoisInDatabase(newPoisList, poiTypeId, contentResolver);

        // Delete any POIs that are of the same type, but not in the latest POI set
        deleteOldPoisFromDatabase(poiList, poiTypeId, contentResolver);

        // Close the cursor
        if (poisCursor != null && !poisCursor.isClosed()) {
            poisCursor.close();
        }
    }

    private ContentValues createPoiContentValues(PointOfInterest poi, int poiTypeId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_ID, poi.getId());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_TYPE_ID, poiTypeId);
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_DISPLAY_NAME, poi.getDisplayName());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_VENUE_ID, poi.getVenueId());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_VENUE_LAND_ID, poi.getLandId());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_SUB_TYPE_FLAGS, poi.getSubTypeFlags());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_LIST_IMAGE_URL, poi.getListImageUrl());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_THUMBNAIL_IMAGE_URL, poi.getThumbnailImageUrl());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_LATITUDE, poi.getLatitude());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_LONGITUDE, poi.getLongitude());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_IS_ROUTABLE, poi.getIsRoutable());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_HASH_CODE, poi.hashCode());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_OBJECT_JSON, poi.toJson());
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_SORT_ORDER, poi.getSortOrder());
        if(poi.getTags() != null) {
            // Add pipe delimiters to tags, removing non-characters
            // e.g. ["food"] becomes |food| and ["food","dinner"] becomes |food|dinner|
            String pipedTags = new Gson().toJson(poi.getTags()).replaceAll("[^\\w]+", "|");
            contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_TAGS, pipedTags);
        }
        contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_OPTION_FLAGS, poi.getOptionFlags());
        if (poi.getOfferIds() != null) {
            contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_OFFER_IDS, new Gson().toJson(poi.getOfferIds()));
        }

        if (poi instanceof Ride) {
            Ride ride = (Ride) poi;
            contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_WAIT_TIME, ride.getWaitTime());
            if (ride.getMinHeightInInches() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_MIN_RIDE_HEIGHT, ride.getMinHeightInInches());
            }
            if (ride.getMaxHeightInInches() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_MAX_RIDE_HEIGHT, ride.getMaxHeightInInches());
            }
        }
        else if (poi instanceof Show) {
            List<String> startTimesList = ((Show) poi).getStartTimes();
            if (startTimesList != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_SHOW_TIMES_JSON, new Gson().toJson(startTimesList));
            }
            else {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_SHOW_TIMES_JSON, new Gson().toJson(new ArrayList<String>()));
            }

            // Add the special wait time for some shows
            contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_WAIT_TIME, ((Show) poi).getWaitTime());
        } else if(poi instanceof Dining) {
            Dining dining = (Dining) poi;
            if (dining.getMinPrice() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_MIN_PRICE, dining.getMinPrice());
            }
            if (dining.getMaxPrice() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_MAX_PRICE, dining.getMaxPrice());
            }
        } else if(poi instanceof Entertainment) {
            Entertainment entertainment = (Entertainment) poi;
            if (entertainment.getMinPrice() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_MIN_PRICE, entertainment.getMinPrice());
            }
            if (entertainment.getMaxPrice() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_MAX_PRICE, entertainment.getMaxPrice());
            }
        } else if (poi instanceof Event) {
            Event event = (Event) poi;
            if (event.getWaitTime() != null) {
                contentValues.put(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_WAIT_TIME, event.getWaitTime());
            }
        }
        return contentValues;
    }

    private void insertPoisInDatabase(List<? extends PointOfInterest> newPoiList, int poiTypeId, ContentResolver contentResolver) {
        if (newPoiList == null || newPoiList.size() == 0) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertPoisInDatabase: inserting new pois = " + newPoiList.size()
                    + " poiTypeId = " + poiTypeId);
        }

        ContentValues contentValuesArray[] = new ContentValues[newPoiList.size()];
        for (int i = 0; i < newPoiList.size(); i++) {
            PointOfInterest newPoi = newPoiList.get(i);

            ContentValues contentValues = createPoiContentValues(newPoi, poiTypeId);
            contentValuesArray[i] = contentValues;
        }

        try {
            contentResolver.bulkInsert(UniversalOrlandoContentUris.POINTS_OF_INTEREST, contentValuesArray);
            // Insert show times
            insertShowTimesInDatabase(newPoiList, poiTypeId, contentResolver);
            // Insert event times
            insertEventTimesInDatabase(newPoiList, poiTypeId, contentResolver);
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertPoisInDatabase: exception inserting point of interests into the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private void updatePoiInDatabase(PointOfInterest poiToUpdate, int poiTypeId, ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "updatePoiInDatabase: updating poi = " + poiToUpdate.getDisplayName()
                    + " poiTypeId = " + poiTypeId);
        }

        ContentValues contentValues = createPoiContentValues(poiToUpdate, poiTypeId);
        String selection = UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_ID + " = '" +
                poiToUpdate.getId() +
                "'";

        try {

            contentResolver.update(UniversalOrlandoContentUris.POINTS_OF_INTEREST, contentValues, selection, null);
            // Delete show times if POI is a show then insert new show times
            if(poiToUpdate instanceof Show) {
                deleteSinglePoiShowTimesFromDatabase(poiToUpdate, contentResolver);
                insertShowTimesInDatabase(Collections.singletonList(poiToUpdate), poiTypeId, contentResolver);
            } else if(poiToUpdate instanceof Event) {
                deleteSingleEventTimesFromDatabase((Event) poiToUpdate, contentResolver);
                insertEventTimesInDatabase(Collections.singletonList(poiToUpdate), poiTypeId, contentResolver);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "updatePoiInDatabase: exception updating point of interest in the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private void deleteOldPoisFromDatabase(List<? extends PointOfInterest> latestPoiList, int poiTypeId, ContentResolver contentResolver) {
        if (latestPoiList == null) {
            return;
        }

        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_TYPE_ID);
        selection.append(" = '");
        selection.append(poiTypeId);
        selection.append("'");

        // Delete pois that don't match the latest id set, if the latest is empty, delete them all
        if (latestPoiList.size() > 0) {
            selection.append(" AND ")
                    .append(UniversalOrlandoDatabaseTables.PointsOfInterestTable.COL_POI_ID)
                    .append(" NOT IN (");

            for (int i = 0; i < latestPoiList.size(); i++) {
                PointOfInterest poi = latestPoiList.get(i);
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(poi.getId()).append("'");
            }
            selection.append(")");
        }

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.POINTS_OF_INTEREST, selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteOldPoisFromDatabase: old pois removed = " + rowsDeleted
                        + " poiTypeId = " + poiTypeId);
            }

            // Delete show times from database
            deleteShowTimesFromDatabase(latestPoiList, poiTypeId, contentResolver);

            // Delete event times from database
            deleteEventTimesFromDatabase(latestPoiList, poiTypeId, contentResolver);

        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteOldPoisFromDatabase: exception deleting old point of interests in the database, poiTypeId = " + poiTypeId, e);
            }
        }
    }

    private void deleteSinglePoiShowTimesFromDatabase(PointOfInterest poi, ContentResolver contentResolver) {
        if(poi == null) {
            return;
        }
        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_ID)
                .append(" = '").append(poi.getId()).append("'");
        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.SHOW_TIMES, selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteSinglePoiShowTimesFromDatabase: show times removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteSinglePoiShowTimesFromDatabase: exception deleting show times in the database", e);
            }
        }

    }

    private void deleteShowTimesFromDatabase(List<? extends PointOfInterest> poiList, int poiTypeId, ContentResolver contentResolver) {
        if(poiList == null) {
            return;
        }

        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_TYPE_ID)
                .append(" = '").append(poiTypeId).append("'");

        if (!poiList.isEmpty()) {
            selection.append(" AND ");
            selection.append(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_ID);
            selection.append(" NOT IN (");
            for (int i = 0; i < poiList.size(); i++) {
                PointOfInterest poi = poiList.get(i);
                if (i > 0) {
                    selection.append(", ");
                }
                selection.append("'").append(poi.getId()).append("'");
            }
            selection.append(")");
        }
        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.SHOW_TIMES, selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteShowTimes: show times removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteShowTimes: exception deleting show times in the database", e);
            }
        }
    }

    private void insertShowTimesInDatabase(List<? extends PointOfInterest> poiList, int poiTypeId, ContentResolver contentResolver) {
        if(poiList == null || poiList.isEmpty()) {
            return;
        }

        ContentValues[] contentValues = createShowTimeContentValues(poiList, poiTypeId);

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertShowTimesInDatabase: inserting new show times = " + contentValues.length);
        }

        if(contentValues.length > 0) {
            try {
                int rowsInserted = contentResolver.bulkInsert(UniversalOrlandoContentUris.SHOW_TIMES, contentValues);
                if(BuildConfig.DEBUG) {
                    Log.d(TAG, "insertShowTimesInDatabase: content values count = " + contentValues.length);
                    Log.d(TAG, "insertShowTimesInDatabase: " + rowsInserted + " show times inserted" );
                }
            }
            catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "insertShowTimesInDatabase: exception inserting show times into the database", e);
                }
                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
    }

    private ContentValues[] createShowTimeContentValues(List<? extends PointOfInterest> poiList, int poiTypeId) {
        List<ContentValues> contentValuesList = new ArrayList<>();

        for (PointOfInterest poi : poiList) {
            if (poi instanceof Show) {
                Show show = (Show) poi;
                // Create content values for start times
                if (show.getStartTimes() != null && !show.getStartTimes().isEmpty()) {
                    for (String startTime : show.getStartTimes()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_TIME_TYPE, UniversalOrlandoDatabaseTables.ShowTimesTable.TIME_TYPE_START_TIME);
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_ID, show.getId());
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_SHOW_TIME, startTime);
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_TYPE_ID, poiTypeId);
                        contentValuesList.add(contentValues);
                    }
                }

                // Create content values for end times
                if (show.getEndTimes() != null && !show.getEndTimes().isEmpty()) {
                    for (String endTime : show.getStartTimes()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_TIME_TYPE, UniversalOrlandoDatabaseTables.ShowTimesTable.TIME_TYPE_END_TIME);
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_ID, show.getId());
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_SHOW_TIME, endTime);
                        contentValues.put(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_TYPE_ID, poiTypeId);
                        contentValuesList.add(contentValues);
                    }
                }
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    private ContentValues[] createEventTimeContentValues(List<? extends PointOfInterest> poiList,
                                                                int poiTypeId) {
        List<ContentValues> contentValuesList = new ArrayList<>();

        for (PointOfInterest poi : poiList) {
            if (poi instanceof Event) {
                Event event = (Event) poi;
                int flags = 0;
                flags = (event.shouldShowInDisplayList() ? flags | Event.FLAG_SHOW_IN_TODAY_VIEW : flags);
                if (event.getEventDates() != null && !event.getEventDates().isEmpty()) {
                    for (EventDate eventDate : event.getEventDates()) {
                        if (eventDate.getStartDateUnix() != null) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_POI_TYPE_ID, poiTypeId);
                            contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_POI_ID, poi.getId());
                            contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_FLAGS, flags);
                            if (eventDate.getStartDateUnix() != null && eventDate.getStartDateUnix() > 0) {
                                contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_START_DATE, eventDate.getStartDateUnix());
                            } else {
                                contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_START_DATE, 9223372036854775806L);
                            }
                            if (eventDate.getEndDateUnix() != null) {
                                contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_END_DATE, eventDate.getEndDateUnix());
                            }
                            contentValuesList.add(contentValues);
                        }
                    }
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_POI_TYPE_ID, poiTypeId);
                    contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_POI_ID, poi.getId());
                    contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_START_DATE, 9223372036854775806L);
                    contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_END_DATE, 9223372036854775806L);
                    contentValues.put(UniversalOrlandoDatabaseTables.EventTimesTable.COL_FLAGS, flags);
                    contentValuesList.add(contentValues);
                }
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    private void insertEventTimesInDatabase(List<? extends PointOfInterest> poiList, int poiTypeId, ContentResolver contentResolver) {
        if(poiList == null || poiList.isEmpty()) {
            return;
        }

        ContentValues[] contentValues = createEventTimeContentValues(poiList, poiTypeId);

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertEventTimesInDatabase: inserting new event times = " + contentValues.length);
        }

        if(contentValues.length > 0) {
            try {
                int rowsInserted = contentResolver.bulkInsert(UniversalOrlandoContentUris.EVENT_TIMES, contentValues);
                if(BuildConfig.DEBUG) {
                    Log.d(TAG, "insertEventTimesInDatabase: content values count = " + contentValues.length);
                    Log.d(TAG, "insertEventTimesInDatabase: " + rowsInserted + " event times inserted" );
                }
            }
            catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "insertEventTimesInDatabase: exception inserting event times into the database", e);
                }
                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }
        }
    }

    private void deleteSingleEventTimesFromDatabase(Event event, ContentResolver contentResolver) {
        if(event == null) {
            return;
        }

        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.EventTimesTable.COL_POI_ID)
                .append(" = '").append(event.getId()).append("'");
        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.EVENT_TIMES, selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteSingleEventTimesFromDatabase: event times removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteSingleEventTimesFromDatabase: exception deleting event times in the database", e);
            }
        }
    }

    private void deleteEventTimesFromDatabase(List<? extends PointOfInterest> poiList, int poiTypeId, ContentResolver contentResolver) {
        if(poiList == null || poiList.isEmpty()) {
            return;
        }

        StringBuilder selection = new StringBuilder(UniversalOrlandoDatabaseTables.ShowTimesTable.COL_POI_TYPE_ID)
                .append(" = '").append(poiTypeId).append("'");

        selection.append(" AND ").append(UniversalOrlandoDatabaseTables.EventTimesTable.COL_POI_ID);
        selection.append(" NOT IN (");
        for (int i = 0; i < poiList.size(); i++) {
            PointOfInterest poi = poiList.get(i);
            if (i > 0) {
                selection.append(", ");
            }
            selection.append("'").append(poi.getId()).append("'");
        }
        selection.append(")");

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.EVENT_TIMES, selection.toString(), null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteEventTimesFromDatabase: event times removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteEventTimesFromDatabase: exception deleting event times in the database", e);
            }
        }
    }


    public void successCase(GetPoisResponse getPoisResponse, Response response) {

        //Only sync if response in not null
        if (getPoisResponse != null) {
            Log.e(TAG, "get pois request: " + new Gson().toJson(response));
            // Sync the points of interest to the POI database
            syncPoisWithDatabase(getPoisResponse.getRides(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE);
            syncPoisWithDatabase(getPoisResponse.getDining(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_DINING);
            syncPoisWithDatabase(getPoisResponse.getShows(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW);
            syncPoisWithDatabase(getPoisResponse.getHotels(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL);
            syncPoisWithDatabase(getPoisResponse.getParades(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE);
            syncPoisWithDatabase(getPoisResponse.getRestrooms(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_RESTROOM);
            syncPoisWithDatabase(getPoisResponse.getAtms(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_ATM);
            syncPoisWithDatabase(getPoisResponse.getParking(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_PARKING);
            syncPoisWithDatabase(getPoisResponse.getShops(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP);
            syncPoisWithDatabase(getPoisResponse.getRentalServices(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS);
            syncPoisWithDatabase(getPoisResponse.getLockers(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS);
            syncPoisWithDatabase(getPoisResponse.getServiceAnimalRestAreas(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_SERVICE_ANIMAL_REST_AREA);
            syncPoisWithDatabase(getPoisResponse.getSmokingAreas(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_SMOKING_AREA);
            syncPoisWithDatabase(getPoisResponse.getPackagePickups(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_PACKAGE_PICKUP);
            syncPoisWithDatabase(getPoisResponse.getLostAndFoundStations(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_LOST_AND_FOUND_STATION);
            syncPoisWithDatabase(getPoisResponse.getGuestServices(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_GUEST_SERVICES_BOOTH);
            syncPoisWithDatabase(getPoisResponse.getFirstAidStations(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_FIRST_AID_STATION);
            syncPoisWithDatabase(getPoisResponse.getChargingStations(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_CHARGING_STATION);
            syncPoisWithDatabase(getPoisResponse.getPhoneCardDispensers(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_PHONE_CARD_DISPENSER);
            syncPoisWithDatabase(getPoisResponse.getEntertainment(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_ENTERTAINMENT);
            syncPoisWithDatabase(getPoisResponse.getWaterParks(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK);
            syncPoisWithDatabase(getPoisResponse.getEvents(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT);
            syncPoisWithDatabase(getPoisResponse.getGeneralLocations(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_GEN_LOCATION);
            syncPoisWithDatabase(getPoisResponse.getGateways(), UniversalOrlandoDatabaseTables.PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY);

            // Store the last time pois were synced
            UniversalAppState universalOrlandoState = UniversalAppStateManager.getInstance();
            universalOrlandoState.setDateOfLastPoiSyncInMillis(new Date().getTime());
            UniversalAppStateManager.saveInstance();

        }

    }
}
