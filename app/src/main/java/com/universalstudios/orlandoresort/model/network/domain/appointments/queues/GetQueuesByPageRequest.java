package com.universalstudios.orlandoresort.model.network.domain.appointments.queues;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.QueuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by GOKHAN on 7/12/2016.
 */
public class GetQueuesByPageRequest extends UniversalOrlandoServicesRequest implements
        Callback<GetQueuesByPageResponse> {

    private static final String TAG = GetQueuesByPageRequest.class.getSimpleName();
    public static final String PAGE_SIZE_ALL = "All";

    protected GetQueuesByPageRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType,GetQueuesParams getQueuesParams ) {
        super(senderTag, priority, concurrencyType, getQueuesParams);
    }

    private static class GetQueuesParams extends NetworkParams {
        private Integer page;
        private String pageSize;

        public GetQueuesParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private final GetQueuesParams getQueuesParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            getQueuesParams = new GetQueuesParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setPage(Integer page) {
            getQueuesParams.page = page;
            return getThis();
        }

        public Builder setPageSize(String pageSize) {
            getQueuesParams.pageSize = pageSize;
            return getThis();
        }

        public GetQueuesByPageRequest build() {
            return new GetQueuesByPageRequest(senderTag, priority, concurrencyType,getQueuesParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        GetQueuesParams params = (GetQueuesParams) getNetworkParams();
        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetQueuesByPageResponse response = services.getQueuesByPage();
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getQueuesByPage(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }


    @Override
    public void success(GetQueuesByPageResponse getQueuesByPageResponse, Response response) {

        // Only sync if response is not null
        if (getQueuesByPageResponse != null) {
            // Sync the Queue items to the database
            syncQueuesWithDatabase(getQueuesByPageResponse.getResults());

            // Store the last time park queue was synced
            UniversalAppState appState = UniversalAppStateManager.getInstance();
            appState.setDateOfLastQueuesSyncInMillis(new Date().getTime());
            UniversalAppStateManager.saveInstance();
        }
    }

    private void syncQueuesWithDatabase(List<QueuesResult> queuesList) {
        // If null, treat the list as if it were empty
        if (queuesList == null) {
            queuesList = new ArrayList<QueuesResult>();
        }

        // Query for all queues items
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        String projection[] = {
                QueuesTable.COL_QUEUE_ID,
                QueuesTable.COL_QUEUE_OBJECT_JSON
        };
        Cursor queueCursor = contentResolver.query(UniversalOrlandoContentUris.QUEUES, projection, null, null, null);

        // List to track items that need to be inserted
        List<QueuesResult> queuesResultList = new ArrayList<QueuesResult>();

        for (QueuesResult queueFromResp : queuesList) {
            if (queueFromResp == null) {
                continue;
            }
            boolean queuesFoundInDb = false;

            // Parse the timestamps into dates
          //  queuesResult.setDatesInMillis();

            // Start the cursor at the first row
            if (queueCursor != null && queueCursor.moveToFirst()) {
                // Go through every queue item in the database
                do {
                    int queueId = queueCursor.getInt(queueCursor.getColumnIndex(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_ID));

                    // If the queue item is found, check to see if it needs to be updated
                    if (queueId == queueFromResp.getId()) {
                        String queueObjectJson = queueCursor.getString(queueCursor.getColumnIndex(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_OBJECT_JSON));
                        QueuesResult queuesFromDb = GsonObject.fromJson(queueObjectJson, QueuesResult.class);

                        // If the queue item in the database is different than the new queue item, update the database
                        if (!queueFromResp.equals(queuesFromDb, true)) {
                            // Copy over any transient state that was set after syncing
                            queueFromResp.setHasBeenRead(queuesFromDb.getHasBeenRead());
                            // Update the item in the DB
                            updateQueuesInDatabase(queueFromResp, contentResolver);
                        }

                        // Stop looping after finding the queues item
                        queuesFoundInDb = true;
                        break;
                    }
                } while (queueCursor.moveToNext());
            }

            // If the queue item wasn't found, add it to the list to be inserted
            if (!queuesFoundInDb) {
                queuesResultList.add(queueFromResp);
            }
        }

        // Insert any new queue items
        insertQueuesInDatabase(queuesResultList, contentResolver);

        // Delete any queue items that are not in the latest queues set
        deleteOldQueuesFromDatabase(queuesList, contentResolver);

        // Close the cursor
        if (queueCursor != null && !queueCursor.isClosed()) {
            queueCursor.close();
        }
    }

    private static void deleteOldQueuesFromDatabase(List<QueuesResult> latestQueuesList, ContentResolver contentResolver) {
        if (latestQueuesList == null) {
            latestQueuesList = new ArrayList<QueuesResult>();
        }

        // Delete queue that don't match the latest id set, if the latest set is empty, delete them all
        String selection = null;
        if (latestQueuesList.size() > 0) {
            StringBuilder selectionBuilder = new StringBuilder(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_ID)
                    .append(" NOT IN (");

            for (int i = 0; i < latestQueuesList.size(); i++) {
                QueuesResult queues = latestQueuesList.get(i);
                if (i > 0) {
                    selectionBuilder.append(", ");
                }
                selectionBuilder.append("'").append(queues.getId()).append("'");
            }
            selectionBuilder.append(")");
            selection = selectionBuilder.toString();
        }

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.QUEUES, selection, null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "deleteOldQueuesFromDatabase: old Queue items removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteOldQueuesFromDatabase: exception deleting old items from the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private static void insertQueuesInDatabase(List<QueuesResult> newQueueList, ContentResolver contentResolver) {
        if (newQueueList == null || newQueueList.size() == 0) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertQueuesInDatabase: inserting new Queues items = " + newQueueList.size());
        }

        ContentValues contentValuesArray[] = new ContentValues[newQueueList.size()];
        for (int i = 0; i < newQueueList.size(); i++) {
            QueuesResult newQueue = newQueueList.get(i);

            ContentValues contentValues = createQueuesContentValues(newQueue);
            contentValuesArray[i] = contentValues;
        }

        try {
            contentResolver.bulkInsert(UniversalOrlandoContentUris.QUEUES, contentValuesArray);
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertQueuesInDatabase: exception inserting new items into the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }


    private static void updateQueuesInDatabase(QueuesResult queuesToUpdate, ContentResolver contentResolver) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "updateQueuesInDatabase: updating Queues = " + queuesToUpdate.getName());
        }

        ContentValues contentValues = createQueuesContentValues(queuesToUpdate);
        String selection = new StringBuilder(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_ID)
                .append(" = '").append(queuesToUpdate.getId()).append("'")
                .toString();

        try {
            contentResolver.update(UniversalOrlandoContentUris.QUEUES, contentValues, selection, null);
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "updateQueuesInDatabase: exception updating item in the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private static ContentValues createQueuesContentValues(QueuesResult queues) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_ID, queues.getId());
        contentValues.put(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_ENTITY_ID, queues.getQueueEntityId());
        contentValues.put(UniversalOrlandoDatabaseTables.QueuesTable.COL_QUEUE_OBJECT_JSON, queues.toJson());
        return contentValues;
    }


    @Override
    public void failure(RetrofitError error) {

    }
}
