package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ForgotPasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/9/16.
 * Edited by Tyler Ritchie on 11/28/16.
 * Class: ForgotPasswordRequest
 * Class Description: Request to submit for a forgotten password
 */
public class ForgotPasswordRequest extends IBMOrlandoServicesRequest implements Callback<ForgotPasswordResponse> {
    public static final String TAG = "ForgotPasswordRequest";

    public ForgotPasswordRequest(String senderTag, Priority priority,
                                 ConcurrencyType concurrencyType, ForgotPasswordRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class ForgotPasswordRequestParams extends NetworkParams {
        private String userId;

        public ForgotPasswordRequestParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private ForgotPasswordRequestParams forgotPasswordRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.forgotPasswordRequestParams = new ForgotPasswordRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setUserId(String userId) {
            this.forgotPasswordRequestParams.userId = userId;
            return getThis();
        }

        public ForgotPasswordRequest build() {
            return new ForgotPasswordRequest(senderTag, priority, concurrencyType, forgotPasswordRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        ForgotPasswordRequestParams networkParams = (ForgotPasswordRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    ForgotPasswordResponse response = services.forgotPassword(networkParams.userId);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.forgotPassword(networkParams.userId, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(ForgotPasswordResponse forgotPasswordResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (forgotPasswordResponse == null) {
            forgotPasswordResponse = new ForgotPasswordResponse();
        }
        super.handleSuccess(forgotPasswordResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new ForgotPasswordResponse(), error);
    }


}
