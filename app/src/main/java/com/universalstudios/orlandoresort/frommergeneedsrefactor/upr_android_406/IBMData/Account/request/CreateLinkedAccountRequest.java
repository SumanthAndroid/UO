package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request_params.CreateLinkedAccountRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.BuildConfig;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateLinkedAccountResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 *
 * Created by Tyler Ritchie on 10/13/16.
 */
public class CreateLinkedAccountRequest extends IBMOrlandoServicesRequest implements Callback<CreateLinkedAccountResponse> {

    // Logging Tag
    private static final String TAG = CreateLinkedAccountRequest.class.getSimpleName();
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    private CreateLinkedAccountRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link CreateLinkedAccountRequest.Builder}.
     */
    private static class CreateLinkedAccountRequestParams extends NetworkParams {
        private CreateLinkedAccountRequestBody requestBody;

        public CreateLinkedAccountRequestParams() {
            super();
            requestBody = new CreateLinkedAccountRequestBody();
        }
    }

    /**
     * A Builder class for building a new {@link CreateLinkedAccountRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<CreateLinkedAccountRequest.Builder> {
        private final CreateLinkedAccountRequest.CreateLinkedAccountRequestParams createLinkedAccountRequestParams;

        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
            this.createLinkedAccountRequestParams = new CreateLinkedAccountRequest.CreateLinkedAccountRequestParams();
            this.createLinkedAccountRequestParams.requestBody = new CreateLinkedAccountRequestBody();
        }

        /**
         * Method required by {@link BaseNetworkRequestBuilder} to allow the proper builder pattern
         * with child classes.
         *
         * @return the {@link CreateLinkedAccountRequest.Builder}
         */
        @Override
        protected CreateLinkedAccountRequest.Builder getThis() {
            return this;
        }

        public CreateLinkedAccountRequest.Builder setBirthDate(Date birthDate) {
            String formattedBirthDate = birthDate != null ? getStandardFormattedDate(birthDate) : "";
            this.createLinkedAccountRequestParams.requestBody.setBirthDate(formattedBirthDate);
            return getThis();
        }

        public CreateLinkedAccountRequest.Builder setBirthDate(String birthDateString) {
            this.createLinkedAccountRequestParams.requestBody.setBirthDate(birthDateString);
            return getThis();
        }

        public CreateLinkedAccountRequest.Builder setCustomerOrderId(String customerOrderId) {
            this.createLinkedAccountRequestParams.requestBody.setCustomerOrderId(customerOrderId);
            return getThis();
        }

        public CreateLinkedAccountRequest.Builder setGuestId(String guestId) {
            this.createLinkedAccountRequestParams.requestBody.setGuestId(guestId);
            return getThis();
        }

        public CreateLinkedAccountRequest.Builder setPassword(String password) {
            this.createLinkedAccountRequestParams.requestBody.setPassword(password);
            return getThis();
        }

        public CreateLinkedAccountRequest.Builder setSourceId(@SourceId.SourceIdType String sourceId) {
            this.createLinkedAccountRequestParams.requestBody.setSourceId(sourceId);
            return getThis();
        }

        public CreateLinkedAccountRequest build() {
            return new CreateLinkedAccountRequest(senderTag, priority, concurrencyType, createLinkedAccountRequestParams);
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
        // Date format: 9/19/2016
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return sdf.format(dateOfRequest);
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        CreateLinkedAccountRequest.CreateLinkedAccountRequestParams params = (CreateLinkedAccountRequest.CreateLinkedAccountRequestParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    CreateLinkedAccountResponse response = services.createLinkedAccount(params.requestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.createLinkedAccount(params.requestBody, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(CreateLinkedAccountResponse createLinkedAccountResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(createLinkedAccountResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new CreateLinkedAccountResponse(), error);
    }
}