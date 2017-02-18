package com.universalstudios.orlandoresort.model.network.domain.appointments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.appointments.queues.QueuesResult;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by GOKHAN on 7/26/2016.
 */
public class CreateAppointmentTimeRequest extends UniversalOrlandoServicesRequest implements Callback<CreateAppointmentTimeResponse> {

    private static final String TAG = CreateAppointmentTimeRequest.class.getSimpleName();
    private static CreateAppointTimeParams createAppointTimeParams;
    private static QueuesResult queuesFromDb;
    private static AppointmentTimes createdAppointmentTime;
    private static boolean isCreatedOnce;
    private static long queueEntityId;

    private CreateAppointmentTimeRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, CreateAppointTimeParams createAppointTimeParams) {
        super(senderTag, priority, concurrencyType, createAppointTimeParams);
        this.createAppointTimeParams = createAppointTimeParams;
    }

    private static class CreateAppointTimeParams extends NetworkParams {
        private CreateAppointTimeBodyParams bodyParams;

        public CreateAppointTimeParams() {
            super();
            bodyParams = new CreateAppointTimeBodyParams();
        }
    }

    public static class CreateAppointTimeBodyParams extends GsonObject {

        @SerializedName("AppointmentTimeId")
        private Long appointmentTimeId;

        @SerializedName("Quantity")
        private int quantity;

        @SerializedName("DistributionPoint")
        private String distributionPoint;

        @SerializedName("QueueId")
        private long queueId;


        // FIELD NOT RETURNED FROM SERVICE
        @SerializedName("SelectedAppointmentTimeJson")
        private String selectedAppointmentTimeJson;

        @SerializedName("QueueResultJsonStr")
        private String queueResultJsonStr;

        @SerializedName("Imageurl")
        private String imageUrl;

        @SerializedName("QueueEntityId")
        private String queueEntityType;

        @SerializedName("ParkName")
        private String parkName;

    }

    @SuppressWarnings("javadoc")
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private CreateAppointTimeParams createAppointTimeParams;


        public Builder(NetworkRequestSender sender) {
            super(sender);
            createAppointTimeParams = new CreateAppointTimeParams();
            createAppointTimeParams.bodyParams.distributionPoint = "kiosk";
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setAppointmentTimeId(Long appointmentTimeId) {
            this.createAppointTimeParams.bodyParams.appointmentTimeId = appointmentTimeId;
            return getThis();
        }

        public Builder setQuantity(int quantity) {
            this.createAppointTimeParams.bodyParams.quantity = quantity;
            return getThis();
        }

        public Builder setQueueResultJsonStr(String queueResultJsonStr) {
            this.createAppointTimeParams.bodyParams.queueResultJsonStr = queueResultJsonStr;
            return getThis();
        }

        public Builder setQueueEntityType(String queueEntityType) {
            this.createAppointTimeParams.bodyParams.queueEntityType = queueEntityType;
            return getThis();
        }

        public Builder setDistributionPoint(String distributionPoint) {
            this.createAppointTimeParams.bodyParams.distributionPoint = distributionPoint;
            return getThis();
        }

        public Builder setQueueId(Long queueId){
            this.createAppointTimeParams.bodyParams.queueId = queueId;
            return getThis();
        }

        public Builder setSelectedAppointmentTimeJson(String selectedAppointmentTimeJson) {
            this.createAppointTimeParams.bodyParams.selectedAppointmentTimeJson = selectedAppointmentTimeJson;
            return getThis();
        }

        public Builder setImageUrl(String imageUrl){
            this.createAppointTimeParams.bodyParams.imageUrl = imageUrl;
            return getThis();
        }

        public Builder setParkName(String parkName){
            this.createAppointTimeParams.bodyParams.parkName = parkName;
            return getThis();
        }

        public CreateAppointmentTimeRequest build() {
            return new CreateAppointmentTimeRequest(senderTag, priority, concurrencyType, createAppointTimeParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        if(!isCreatedOnce) {
            isCreatedOnce=true;
            CreateAppointTimeParams params = (CreateAppointTimeParams) getNetworkParams();
            UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);
            String basicAuth = AccountStateManager.getBasicAuthString();
            switch (getConcurrencyType()) {
                case SYNCHRONOUS:
                    try {
                        CreateAppointmentTimeResponse response = services.createAppointmentTime(basicAuth, params.bodyParams.queueId,
                                params.bodyParams.appointmentTimeId, params.bodyParams);
                        success(response, null);
                    } catch (RetrofitError retrofitError) {
                        failure(retrofitError);
                    }
                    break;
                case ASYNCHRONOUS:
                    services.createAppointmentTime(basicAuth, params.bodyParams.queueId,
                            params.bodyParams.appointmentTimeId, params.bodyParams, this);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
            }
        }

    }


        @Override
    public void success(CreateAppointmentTimeResponse createAppointmentTimeResponse, Response response) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "success");
            }

            isCreatedOnce = false;
            if(createAppointmentTimeResponse != null){
                syncTicketWithDatabase(createAppointmentTimeResponse);
                handleSuccess(createAppointmentTimeResponse, response);
            }
            else{
                createAppointmentTimeResponse = new CreateAppointmentTimeResponse();
                handleSuccess(createAppointmentTimeResponse, response);
            }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        isCreatedOnce = false;
    }

    private void syncTicketWithDatabase(CreateAppointmentTimeResponse createAppointmentTimeResponse){

        // Query for all queues items
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        String projection[] = {
                UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_TICKET_APPOINTMENT_ID,
                UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TIME_ID,
                UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_FULL_APPOINTMENT_OBJECT_JSON,
                UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_FULL_APPOINTMENT_TICKET_OBJECT_JSON
        };

        Cursor queueCursor = contentResolver.query(UniversalOrlandoContentUris.TICKET_APPOINTMENTS, projection, null, null, null);

        insertTicketInDatabase(createAppointmentTimeResponse, contentResolver);
    }

    private static void insertTicketInDatabase(CreateAppointmentTimeResponse createAppointmentTimeResponse, ContentResolver contentResolver) {
        if (createAppointmentTimeResponse == null) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "insertTicketAppointmentsInDatabase: inserting new Ticket Appointment items = ");
        }
        queuesFromDb = GsonObject.fromJson(createAppointTimeParams.bodyParams.queueResultJsonStr , QueuesResult.class);
        createdAppointmentTime = GsonObject.fromJson(createAppointTimeParams.bodyParams.selectedAppointmentTimeJson, AppointmentTimes.class);

        createAppointmentTimeResponse.setImageUrl(createAppointTimeParams.bodyParams.imageUrl);
        createAppointmentTimeResponse.setParkName(createAppointTimeParams.bodyParams.parkName);
        createdAppointmentTime.setQueueEntityType(createAppointTimeParams.bodyParams.queueEntityType);
        createdAppointmentTime.setTicketAppointmentId(createAppointmentTimeResponse.getAppointmentTimeId());
        createdAppointmentTime.setTicketDisplayName(queuesFromDb.getName());
        createAppointmentTimeResponse.setQuantity(createAppointTimeParams.bodyParams.quantity);


            ContentValues contentValues = createAppointmentContentValues(createAppointmentTimeResponse);
        try {
            contentResolver.insert(UniversalOrlandoContentUris.TICKET_APPOINTMENTS, contentValues);
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "insertQueuesInDatabase: exception inserting new items into the database", e);
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }

    private static ContentValues createAppointmentContentValues(CreateAppointmentTimeResponse createAppointmentTimeResponse) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_TICKET_APPOINTMENT_ID, createAppointmentTimeResponse.getTicketAppointmentId());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TIME_ID, createAppointmentTimeResponse.getAppointmentTimeId());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_QUEUE_ENTITY_ID, queuesFromDb.getQueueEntityId());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_START_TIME, createdAppointmentTime.getStartTime());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_END_TIME, createdAppointmentTime.getEndTime());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_HAS_BEEN_READ, createAppointmentTimeResponse.isHasBeeenRead() );
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_OBJECT_JSON, createdAppointmentTime.toJson());
        contentValues.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON, createAppointmentTimeResponse.toJson());
        return contentValues;
    }

}
