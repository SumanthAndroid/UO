package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.request;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.GetTicketsByDateResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.UniversalOrlandoCommerceServices;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to get seasonal tickets by a date range.
 * <p/>
 * Created by Jack Hughes on 9/19/16.
 */
public class GetTicketsByDateRequest extends IBMOrlandoServicesRequest implements Callback<GetTicketsByDateResponse> {

    // Logging Tag
    private static final String TAG = GetTicketsByDateRequest.class.getSimpleName();

    protected GetTicketsByDateRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetTicketsByDateParams extends NetworkParams {
        private Date startDate;
        private Date endDate;

        public GetTicketsByDateParams() {
            super();
            this.startDate = new Date();
            this.endDate = new Date();
        }
    }

    /**
     * A Builder class for building a new {@link GetTicketsByDateRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetTicketsByDateParams getTicketsByDateParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getTicketsByDateParams = new GetTicketsByDateParams();
        }

        /**
         * Method required by {@link BaseNetworkRequestBuilder} to allow the proper builder pattern
         * with child classes.
         *
         * @return the {@link Builder}
         */
        @Override
        protected Builder getThis() {
            return this;
        }

        /**
         * [REQUIRED] Sets the start date for getting seasonal tickets.
         *
         * @param startDate
         *         the {@link java.util.Date} for setting the start date. This Date object is later
         *         formatted by the {@link GetTicketsByDateRequest#getStandardFormattedDate(Date)}
         *         method to an acceptable string.
         *
         * @return the {@link Builder}
         */
        public Builder setStartDate(Date startDate) {
            this.getTicketsByDateParams.startDate = startDate;
            return getThis();
        }

        /**
         * [REQUIRED] Sets the end date for getting seasonal tickets.
         *
         * @param endDate
         *         the {@link java.util.Date} for setting the start date. This Date object is later
         *         formatted by the {@link GetTicketsByDateRequest#getStandardFormattedDate(Date)}
         *         method to an acceptable string.
         *
         * @return the {@link Builder}
         */
        public Builder setEndDate(Date endDate) {
            this.getTicketsByDateParams.endDate = endDate;
            return getThis();
        }

        public GetTicketsByDateRequest build() {
            return new GetTicketsByDateRequest(senderTag, priority, concurrencyType, getTicketsByDateParams);
        }

    }

    /**
     * Static Method to get a String of the correctly formatted date for this call's path parameter
     *
     * @param dateOfRequest
     *         the {@link Date} object to format
     *
     * @return a String of the correctly formatted Date
     */
    private static String getStandardFormattedDate(Date dateOfRequest) {
        // Date format: 2016-09-19 00:00:01
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String formattedDate = sdf.format(dateOfRequest);
        return formattedDate;
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetTicketsByDateParams params = (GetTicketsByDateParams) getNetworkParams();
        String formattedStartDateOfRequest = params.startDate != null ? getStandardFormattedDate(params.startDate) : "";
        String formattedEndDateOfRequest = params.endDate != null ? getStandardFormattedDate(params.endDate) : "";

        UniversalOrlandoCommerceServices services = restAdapter.create(UniversalOrlandoCommerceServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetTicketsByDateResponse response = services.getTicketsByDate(
                            formattedStartDateOfRequest,
                            formattedEndDateOfRequest);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getTicketsByDate(
                        formattedStartDateOfRequest,
                        formattedEndDateOfRequest,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetTicketsByDateResponse getTicketsByDateResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(getTicketsByDateResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetTicketsByDateResponse(), error);
    }
}
