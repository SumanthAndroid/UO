package com.universalstudios.orlandoresort.model.network.domain.appointments;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by GOKHAN on 7/19/2016.
 */
public class GetAppointmentTimesRequest extends UniversalOrlandoServicesRequest implements
        Callback<GetAppointmentTimesResponse> {

    private static final String TAG = GetAppointmentTimesRequest.class.getSimpleName();
    private static final SimpleDateFormat APPT_TIME_FORMAT =  new SimpleDateFormat("MM/dd/yyyy");
    private Date today;



    protected GetAppointmentTimesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetAppointmentParams queueIdParam ) {
        super(senderTag, priority, concurrencyType, queueIdParam);
    }

    private static class GetAppointmentParams extends NetworkParams {
        private long queueId;

        public GetAppointmentParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private final GetAppointmentParams getAppointmentParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            getAppointmentParams = new GetAppointmentParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setQueueId(long queueId) {
            this.getAppointmentParams.queueId = queueId;
            return getThis();
        }


        public GetAppointmentTimesRequest build() {
            return new GetAppointmentTimesRequest(senderTag, priority, concurrencyType,getAppointmentParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        GetAppointmentParams params = (GetAppointmentParams) getNetworkParams();
        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        this.today = new Date();
        SimpleDateFormat dateFormat = APPT_TIME_FORMAT;
        String formattedDate = dateFormat.format(today);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetAppointmentTimesResponse response = services.getAppointmentTimesByQueue(params.queueId, formattedDate);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getAppointmentTimesByQueue(params.queueId, formattedDate, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetAppointmentTimesResponse getAppointmentTimesResponse, Response response) {

        // Only sync if response is not null
        if (getAppointmentTimesResponse != null) {

            // Store the last time appointment was synced
            UniversalAppState appState = UniversalAppStateManager.getInstance();
            appState.setDateOfLastAppointmentSyncInMillis(new Date().getTime());
            UniversalAppStateManager.saveInstance();

            //only return appointment times that are available past the current time
            getAppointmentTimesResponse.setAppointmentTimesResults(getValidAppointmentTimesAfterCurrentTime(getAppointmentTimesResponse.getAppointmentTimesResults()));
        } else {
            getAppointmentTimesResponse = new GetAppointmentTimesResponse();
            getAppointmentTimesResponse.setTotalCount(0);
            getAppointmentTimesResponse.setAppointmentTimesResults(new ArrayList<AppointmentTimes>());
        }

        handleSuccess(getAppointmentTimesResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure: " + retrofitError );
        }

        GetAppointmentTimesResponse emptyAppointmentTimesResponse = new GetAppointmentTimesResponse();
        emptyAppointmentTimesResponse.setTotalCount(0);
        emptyAppointmentTimesResponse.setAppointmentTimesResults(new ArrayList<AppointmentTimes>());
        handleSuccess(emptyAppointmentTimesResponse, retrofitError.getResponse());
    }

    private List<AppointmentTimes> getValidAppointmentTimesAfterCurrentTime(List<AppointmentTimes> appointmentTimes) {

        SimpleDateFormat dateFormat = APPT_TIME_FORMAT;
        String formattedDate = dateFormat.format(today);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        for (int i = appointmentTimes.size() -1; i > 0; i--) {
            Date appointmentDate = getTimeForAppointment(formattedDate + " " + appointmentTimes.get(i).getStartTime(), dateFormatter);
            if(appointmentDate != null && today.compareTo(appointmentDate) > 0) {
                appointmentTimes.remove(i);
            }
        }

        return appointmentTimes;
    }

    private Date getTimeForAppointment(String time, SimpleDateFormat formatter) {
        try {
            return formatter.parse(time);
        } catch (ParseException ex) {
            return null;
        }
    }
}
