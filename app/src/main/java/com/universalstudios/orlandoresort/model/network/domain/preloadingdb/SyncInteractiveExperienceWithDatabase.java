package com.universalstudios.orlandoresort.model.network.domain.preloadingdb;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.domain.interactiveexperience.Beacon;
import com.universalstudios.orlandoresort.model.network.domain.interactiveexperience.GetInteractiveExperienceResponse;
import com.universalstudios.orlandoresort.model.network.domain.interactiveexperience.InteractiveMasterExperience;
import com.universalstudios.orlandoresort.model.network.domain.interactiveexperience.Trigger;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import java.util.ArrayList;
import java.util.List;


/**
 * Sync Interactive Experience data with Database
 * Created by GOKHAN on 6/21/2016.
 */
public class SyncInteractiveExperienceWithDatabase {

    private static final String TAG = SyncInteractiveExperienceWithDatabase.class.getSimpleName();
    private  GetInteractiveExperienceResponse getInteractiveExperienceResponse;

    public SyncInteractiveExperienceWithDatabase(GetInteractiveExperienceResponse getInteractiveExperienceResponse){
        this.getInteractiveExperienceResponse = getInteractiveExperienceResponse;
    }


    public void syncInteractiveExperience(List<InteractiveMasterExperience> interactiveMasterExperienceList) {
        if (interactiveMasterExperienceList == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "syncPoisWithDatabase: null interactiveMasterExperienceList ");
            }
            return;
        }

        // Query for POIs of this type
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        Cursor iExpCursor = contentResolver.query(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, null, null, null, null);

        // List to track POIs that need to be inserted

        deleteExperiences(contentResolver);
        // Insert any new interactive experiences
        insertInteractiveExperienceInDatabase(interactiveMasterExperienceList, contentResolver);

        // Delete any interactive experience that area
        deleteOldExperiencesFromDatabase(interactiveMasterExperienceList, contentResolver);

        // Close the cursor
        if (iExpCursor != null && !iExpCursor.isClosed()) {
            iExpCursor.close();
        }
    }

    private ContentValues createInteractiveExperienceContentValues(InteractiveMasterExperience experience) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID, experience.getId());
        contentValues.put(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_DISPLAY_NAME, experience.getId());
        contentValues.put(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_OBJECT_JSON, experience.toJson());

        return contentValues;
    }

    private void deleteExperiences(ContentResolver resolver) {
        resolver.delete(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, null, null);
        resolver.delete(UniversalOrlandoContentUris.INTERACTIVE_BEACONS, null, null);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void insertInteractiveExperienceInDatabase(List<InteractiveMasterExperience> experienceList, ContentResolver contentResolver) {
        if (experienceList == null || experienceList.size() == 0) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertInteractiveExperienceInDatabase: inserting new experiences = " + experienceList.size());
        }

        ContentValues contentValuesArray[] = new ContentValues[experienceList.size()];
        List<Trigger> triggers = new ArrayList<>();
        List<Beacon> beacons = new ArrayList<>();
        for (int i = 0; i < experienceList.size(); i++) {
            InteractiveMasterExperience newExp = experienceList.get(i);
            List<Trigger> expTriggers = newExp.getTriggers();
            if (null != expTriggers && !expTriggers.isEmpty()) {
                for (Trigger trigger : expTriggers) {
                    triggers.add(trigger);
                    List<Beacon> expBeacons = trigger.getBeacons();
                    if (null != expBeacons && !expBeacons.isEmpty()) {
                        for (Beacon beacon : expBeacons) {
                            if (beacon.getBeaconType().equalsIgnoreCase(Beacon.BeaconType.EDDYSTONE.toString())) {
                                beacon.triggerId = newExp.getId();
                                beacons.add(beacon);
                            }
                        }
                    }
                }
            }

            ContentValues contentValues = createInteractiveExperienceContentValues(newExp);
            contentValuesArray[i] = contentValues;
        }

        ContentValues[] beaconContentValues = new ContentValues[beacons.size()];
        for (int i = 0; i < beacons.size(); i++) {
            beaconContentValues[i] = beaconToContentValues(beacons.get(i));
        }

        try {
            contentResolver.bulkInsert(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, contentValuesArray);
            contentResolver.bulkInsert(UniversalOrlandoContentUris.INTERACTIVE_BEACONS, beaconContentValues);
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertPoisInDatabase: exception inserting point of interests into the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    /**
     * Creates ContentValues from a {@link Beacon}
     * @param beacon Beacon to create values from
     * @return ContentValues to use to store a Beacon
     */
    private ContentValues beaconToContentValues(Beacon beacon) {
        ContentValues values = new ContentValues();
        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_ASSOC_TRIGGER_ID, beacon.triggerId);
        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_BEACON_JSON, beacon.toJson());
        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_UUID, beacon.getUuid());
        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_MAJOR, beacon.getMajorId());
        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_MINOR, beacon.getMinorId());

        return values;
    }

//    private void updateExperienceInDatabase(InteractiveMasterExperience expToUpdate, ContentResolver contentResolver) {
//        if (BuildConfig.DEBUG) {
//            Log.i(TAG, "updateExperienceInDatabase: updating experience = " + expToUpdate.getExperienceName());
//        }
//
//        ContentValues contentValues = createInteractiveExperienceContentValues(expToUpdate);
//        String selection = UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID +
//                " = '" + expToUpdate.getId() + "'";
//
//        List<Trigger> triggersToUpdate = expToUpdate.getTriggers();
//        List<Beacon> beaconsToUpdate = new ArrayList<>();
//        for (int i = 0; i < triggersToUpdate.size(); i++) {
//            Trigger trigger = triggersToUpdate.get(i);
//            for (Beacon beacon : trigger.getBeacons()) {
//                if (beacon.getBeaconType().equalsIgnoreCase(Beacon.BeaconType.EDDYSTONE.toString())) {
//                    beacon.triggerId = expToUpdate.getId();
//                    beaconsToUpdate.add(beacon);
//                }
//            }
//        }
//
//        ContentValues[] beaconValues = new ContentValues[beaconsToUpdate.size()];
//        for (int i = 0; i < beaconsToUpdate.size(); i++) {
//            beaconValues[i] = beaconToContentValues(beaconsToUpdate.get(i));
//        }
//
//        try {
//
//            contentResolver.update(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, contentValues, selection, null);
//
//            for (ContentValues values : beaconValues) {
//                String beaconSelection = UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_UUID +
//                        " = '" + values.get(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_UUID) +
//                        "'";
//                contentResolver.update(UniversalOrlandoContentUris.INTERACTIVE_BEACONS, values, beaconSelection, null);
//            }
//
//        }
//        catch (Exception e) {
//            if (BuildConfig.DEBUG) {
//                Log.e(TAG, "updatePoiInDatabase: exception updating point of interest in the database", e);
//            }
//
//            // Log the exception to crittercism
//            Crittercism.logHandledException(e);
//        }
//    }

    private void deleteOldExperiencesFromDatabase(List<InteractiveMasterExperience> experienceList, ContentResolver contentResolver) {
        if (experienceList == null) {
            return;
        }

        // Delete venue lands that don't match the latest id set, if the latest is empty, delete them all
        String selection = null;
        if (experienceList.size() > 0) {
            StringBuilder selectionBuilder = new StringBuilder(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID)
                    .append(" NOT IN (");

            for (int i = 0; i < experienceList.size(); i++) {
                InteractiveMasterExperience interactiveMasterExperience = experienceList.get(i);
                if (i > 0) {
                    selectionBuilder.append(", ");
                }
                selectionBuilder.append("'").append(interactiveMasterExperience.getId()).append("'");
            }
            selectionBuilder.append(")");
            selection = selectionBuilder.toString();
        }

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, selection, null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteOldInteractiveExperienceFromDatabase: old venue lands removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteOldInteractiveExperienceFromDatabase: exception deleting old venue lands in the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    public void successCaseInteractiveExperience(){

        if(getInteractiveExperienceResponse != null){
            syncInteractiveExperience(getInteractiveExperienceResponse.getExperiences());
        }

    }
}
