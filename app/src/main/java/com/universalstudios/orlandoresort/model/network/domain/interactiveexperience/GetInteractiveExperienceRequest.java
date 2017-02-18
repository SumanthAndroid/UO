package com.universalstudios.orlandoresort.model.network.domain.interactiveexperience;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.GetPoisResponse;
import com.universalstudios.orlandoresort.model.network.domain.preloadingdb.SyncInteractiveExperienceWithDatabase;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author jamestimberlake
 * @created 4/28/16.
 */
public class GetInteractiveExperienceRequest extends UniversalOrlandoServicesRequest implements Callback<GetInteractiveExperienceResponse> {
    private static final String TAG = GetInteractiveExperienceRequest.class.getSimpleName();

    private GetInteractiveExperienceRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        public Builder(NetworkRequestSender sender) {
            super(sender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetInteractiveExperienceRequest build() {
            return new GetInteractiveExperienceRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetInteractiveExperienceResponse response =
                            services.getInteractiveExperiences(ServiceEndpointUtils.getCity());
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getInteractiveExperiences(ServiceEndpointUtils.getCity(), this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetInteractiveExperienceResponse getInteractiveExperienceResponse, Response response) {
        super.handleSuccess(getInteractiveExperienceResponse, response);

        if(getInteractiveExperienceResponse != null){
//            syncInteractiveExperienceWithDatabase(getInteractiveExperienceResponse.getExperiences());
//
//            /**
//             * For now, we are fetching all interactive experience images on load.
//             * This will work for 340 only. We need to revisit our caching strategy for future sprints
//             */
//            for (InteractiveMasterExperience experience : getInteractiveExperienceResponse.getExperiences()) {
//                InteractiveExperienceManager.preFetchInteractiveExperiences(getAppContext(), experience);
//            }

            SyncInteractiveExperienceWithDatabase syncInteractiveExperienceWithDatabase = new
                    SyncInteractiveExperienceWithDatabase(getInteractiveExperienceResponse);

            syncInteractiveExperienceWithDatabase.successCaseInteractiveExperience();
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        super.handleFailure(new GetPoisResponse(), retrofitError);
    }

//    private void syncInteractiveExperienceWithDatabase(List<InteractiveMasterExperience> interactiveMasterExperienceList) {
//        if (interactiveMasterExperienceList == null) {
//            if (BuildConfig.DEBUG) {
//                Log.w(TAG, "syncPoisWithDatabase: null interactiveMasterExperienceList ");
//            }
//            return;
//        }
//
//        // Query for pois of this type
//        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
//        Cursor iExpCursor = contentResolver.query(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, null, null, null, null);
//
//        // List to track pois that need to be inserted
//        List<InteractiveMasterExperience> newExperienceList = new ArrayList<InteractiveMasterExperience>();
//
//        deleteExperiences(contentResolver);
//        // Insert any new interactive experiences
//        insertInteractiveExperienceInDatabase(interactiveMasterExperienceList, contentResolver);
//
//        // Delete any interactive experience that area
//        deleteOldExperiencesFromDatabase(interactiveMasterExperienceList, contentResolver);
//
//        // Close the cursor
//        if (iExpCursor != null && !iExpCursor.isClosed()) {
//            iExpCursor.close();
//        }
//    }
//
//    private static ContentValues createInteractiveExperienceContentValues(InteractiveMasterExperience experience) {
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID, experience.getId());
//        contentValues.put(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_DISPLAY_NAME, experience.getId());
//        contentValues.put(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_OBJECT_JSON, experience.toJson());
//
//        return contentValues;
//    }
//
//    private void deleteExperiences(ContentResolver resolver) {
//        resolver.delete(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, null, null);
//        resolver.delete(UniversalOrlandoContentUris.INTERACTIVE_BEACONS, null, null);
//    }
//
//    private static void insertInteractiveExperienceInDatabase(List<InteractiveMasterExperience> experienceList, ContentResolver contentResolver) {
//        if (experienceList == null || experienceList.size() == 0) {
//            return;
//        }
//
//        if (BuildConfig.DEBUG) {
//            Log.i(TAG, "insertInteractiveExperienceInDatabase: inserting new experiences = " + experienceList.size());
//        }
//
//        ContentValues contentValuesArray[] = new ContentValues[experienceList.size()];
//        List<Trigger> triggers = new ArrayList<>();
//        List<Beacon> beacons = new ArrayList<>();
//        for (int i = 0; i < experienceList.size(); i++) {
//            InteractiveMasterExperience newExp = experienceList.get(i);
//            List<Trigger> expTriggers = newExp.getTriggers();
//            if (null != expTriggers && !expTriggers.isEmpty()) {
//                for (Trigger trigger : expTriggers) {
//                    triggers.add(trigger);
//                    List<Beacon> expBeacons = trigger.getBeacons();
//                    if (null != expBeacons && !expBeacons.isEmpty()) {
//                        for (Beacon beacon : expBeacons) {
//                            if (beacon.getBeaconType().equalsIgnoreCase(Beacon.BeaconType.EDDYSTONE.toString())) {
//                                beacon.triggerId = newExp.getId();
//                                beacons.add(beacon);
//                            }
//                        }
//                    }
//                }
//            }
//
//            ContentValues contentValues = createInteractiveExperienceContentValues(newExp);
//            contentValuesArray[i] = contentValues;
//        }
//
//        ContentValues[] beaconContentValues = new ContentValues[beacons.size()];
//        for (int i = 0; i < beacons.size(); i++) {
//            beaconContentValues[i] = beaconToContentValues(beacons.get(i));
//        }
//
//        try {
//            contentResolver.bulkInsert(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, contentValuesArray);
//            contentResolver.bulkInsert(UniversalOrlandoContentUris.INTERACTIVE_BEACONS, beaconContentValues);
//        }
//        catch (Exception e) {
//            if (BuildConfig.DEBUG) {
//                Log.e(TAG, "insertPoisInDatabase: exception inserting point of interests into the database", e);
//            }
//
//            // Log the exception to crittercism
//            Crittercism.logHandledException(e);
//        }
//    }
//
//    /**
//     * Creates ContentValues from a {@link Beacon}
//     * @param beacon Beacon to create values from
//     * @return ContentValues to use to store a Beacon
//     */
//    private static ContentValues beaconToContentValues(Beacon beacon) {
//        ContentValues values = new ContentValues();
//        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_ASSOC_TRIGGER_ID, beacon.triggerId);
//        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_BEACON_JSON, beacon.toJson());
//        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_UUID, beacon.getUuid());
//        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_MAJOR, beacon.getMajorId());
//        values.put(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_MINOR, beacon.getMinorId());
//
//        return values;
//    }
//
//    private static void updateExperienceInDatabase(InteractiveMasterExperience expToUpdate, ContentResolver contentResolver) {
//        if (BuildConfig.DEBUG) {
//            Log.i(TAG, "updateExperienceInDatabase: updating experience = " + expToUpdate.getExperienceName());
//        }
//
//        ContentValues contentValues = createInteractiveExperienceContentValues(expToUpdate);
//        String selection = new StringBuilder(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID)
//                .append(" = '").append(expToUpdate.getId()).append("'")
//                .toString();
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
//                String beaconSelection = new StringBuilder(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_UUID)
//                        .append(" = '").append(values.get(UniversalOrlandoDatabaseTables.InteractiveBeaconsTable.COL_UUID))
//                        .append("'").toString();
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
//
//    private static void deleteOldExperiencesFromDatabase(List<InteractiveMasterExperience> experienceList, ContentResolver contentResolver) {
//        if (experienceList == null) {
//            return;
//        }
//
//        // Delete venue lands that don't match the latest id set, if the latest is empty, delete them all
//        String selection = null;
//        if (experienceList.size() > 0) {
//            StringBuilder selectionBuilder = new StringBuilder(UniversalOrlandoDatabaseTables.InteractiveExperiencesTable.COL_INTERACTIVE_EXPERIENCE_ID)
//                    .append(" NOT IN (");
//
//            for (int i = 0; i < experienceList.size(); i++) {
//                InteractiveMasterExperience interactiveMasterExperience = experienceList.get(i);
//                if (i > 0) {
//                    selectionBuilder.append(", ");
//                }
//                selectionBuilder.append("'").append(interactiveMasterExperience.getId()).append("'");
//            }
//            selectionBuilder.append(")");
//            selection = selectionBuilder.toString();
//        }
//
//        try {
//            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.INTERACTIVE_EXPERIENCES, selection, null);
//            if (BuildConfig.DEBUG) {
//                Log.i(TAG, "deleteOldInteractiveExperienceFromDatabase: old venue lands removed = " + rowsDeleted);
//            }
//        }
//        catch (Exception e) {
//            if (BuildConfig.DEBUG) {
//                Log.e(TAG, "deleteOldInteractiveExperienceFromDatabase: exception deleting old venue lands in the database", e);
//            }
//
//            // Log the exception to crittercism
//            Crittercism.logHandledException(e);
//        }
//    }
}
