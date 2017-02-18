package com.universalstudios.orlandoresort.model.network.domain.appointments;

import android.content.ContentResolver;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
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
 * Created by GOKHAN on 8/3/2016.
 */
public class DeleteCreatedAppointmentTicketRequest extends UniversalOrlandoServicesRequest implements
        Callback<DeleteCreatedAppointmentTicketResponse> {

    private static final String TAG = DeleteCreatedAppointmentTicketRequest.class.getSimpleName();
    DeleteCreatedAppointmentTicketResponse deleteCreatedAppointmentTicketResponse;

    private static DeleteCreatedAppointmentTicketParams params;

    protected DeleteCreatedAppointmentTicketRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, DeleteCreatedAppointmentTicketParams params ) {
        super(senderTag, priority, concurrencyType, params);
    }

    private static class DeleteCreatedAppointmentTicketParams extends NetworkParams {
        private long appointmentTimeId;
        private long appointmentTicketId;
        private long queueId;

        public DeleteCreatedAppointmentTicketParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private final DeleteCreatedAppointmentTicketParams deleteCreatedAppointmentTicketParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            deleteCreatedAppointmentTicketParams = new DeleteCreatedAppointmentTicketParams();
            //this.getAppointmentParams.queueId = queueId;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setAppointmentTimeId(long appointmentTimeId) {
            this.deleteCreatedAppointmentTicketParams.appointmentTimeId = appointmentTimeId;
            return getThis();
        }

        public Builder setAppointmentTicketId(long appointmentTicketId){
            this.deleteCreatedAppointmentTicketParams.appointmentTicketId = appointmentTicketId;
            return getThis();
        }

        public Builder setQueueId(long queueId){
            this.deleteCreatedAppointmentTicketParams.queueId = queueId;
            return getThis();
        }


        public DeleteCreatedAppointmentTicketRequest build() {
            return new DeleteCreatedAppointmentTicketRequest(senderTag, priority, concurrencyType,deleteCreatedAppointmentTicketParams);
        }
    }


    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        DeleteCreatedAppointmentTicketParams params = (DeleteCreatedAppointmentTicketParams) getNetworkParams();
        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);
        DeleteCreatedAppointmentTicketResponse response = new DeleteCreatedAppointmentTicketResponse();
        String basicAuth = AccountStateManager.getBasicAuthString();
        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                     response = services.deleteAppointmentTicket(basicAuth, params.queueId,
                            params.appointmentTimeId, params.appointmentTicketId);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    deleteCreatedAppointmentTicketResponse = response;
                    failure(retrofitError);
                   // deleteOldQueuesFromDatabase();
                }
                break;
            case ASYNCHRONOUS:
                services.deleteAppointmentTicket(basicAuth, params.queueId,
                        params.appointmentTimeId, params.appointmentTicketId);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    private void deleteOldQueuesFromDatabase() {

        DeleteCreatedAppointmentTicketParams params = (DeleteCreatedAppointmentTicketParams)getNetworkParams();
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();

        // Delete queue that don't match the latest id set, if the latest set is empty, delete them all
        String selection = null;

            StringBuilder selectionBuilder = new StringBuilder(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_TICKET_APPOINTMENT_ID)
                    .append(" IN (");

                selectionBuilder.append("'").append(params.appointmentTicketId).append("'");

            selectionBuilder.append(")");
            selection = selectionBuilder.toString();

        try {
            int rowsDeleted = contentResolver.delete(UniversalOrlandoContentUris.TICKET_APPOINTMENTS, selection, null);
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "Appointment Ticket is removed = " + rowsDeleted);
            }
        }
        catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "deleteAppointmentTicketFromDatabase: exception deleting appointment item from the database", e);
                deleteOldQueuesFromDatabase();
            }

            // Log the exception to crittercism
            Crittercism.logHandledException(e);
        }
    }


    @Override
    public void success(DeleteCreatedAppointmentTicketResponse deleteCreatedAppointmentTicketResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Appointment Ticket delete success:  = " + response);
        }
if(deleteCreatedAppointmentTicketResponse == null){

    deleteCreatedAppointmentTicketResponse = new DeleteCreatedAppointmentTicketResponse();
    deleteCreatedAppointmentTicketResponse.setStatus(200);
}

        // Only sync if response is not null
        if (deleteCreatedAppointmentTicketResponse != null) {
            deleteOldQueuesFromDatabase();
            handleSuccess(deleteCreatedAppointmentTicketResponse, response);
        }

    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Appointment Ticket delete error-retrofit:  = " + retrofitError);
            handleFailure(deleteCreatedAppointmentTicketResponse, retrofitError);
        }
    }
}
