package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ResetPasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/15/16.
 * Class: ResetPasswordRequest
 * Class Description: Request for resetting the user's password
 */
public class ResetPasswordRequest extends IBMOrlandoServicesRequest implements Callback<ResetPasswordResponse> {
    public static final String TAG = ResetPasswordRequest.class.getSimpleName();

    public ResetPasswordRequest(String senderTag, Priority priority,
                                ConcurrencyType concurrencyType, ResetPasswordRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    private static class ResetPasswordRequestParams extends NetworkParams {
        private String email;
        private String requestId;
        private ResetPasswordRequestBody requestBody;

        public ResetPasswordRequestParams() {
            super();
            requestBody = new ResetPasswordRequestBody();
        }
    }

    public static class ResetPasswordRequestBody extends GsonObject {
        @SerializedName("newPassword")
        private String newPassword;
    }


    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private ResetPasswordRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            requestParams = new ResetPasswordRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setEmail(String email) {
            requestParams.email = email;
            return getThis();
        }

        public Builder setNewPassword(String newPassword) {
            requestParams.requestBody.newPassword = newPassword;
            return getThis();
        }

        public Builder setRequestId(String requestId) {
            requestParams.requestId = requestId;
            return getThis();
        }

        public ResetPasswordRequest build() {
            return new ResetPasswordRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        ResetPasswordRequestParams requestParams = (ResetPasswordRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    ResetPasswordResponse response = services.resetPassword(
                            requestParams.email,
                            requestParams.requestId,
                            requestParams.requestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.resetPassword(
                        requestParams.email,
                        requestParams.requestId,
                        requestParams.requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(ResetPasswordResponse resetPasswordResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (resetPasswordResponse == null) {
            resetPasswordResponse = new ResetPasswordResponse();
        }
        handleSuccess(resetPasswordResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        handleFailure(new ResetPasswordResponse(), retrofitError);
    }
}
