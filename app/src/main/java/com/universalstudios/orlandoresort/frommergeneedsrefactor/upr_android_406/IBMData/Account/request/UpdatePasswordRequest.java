package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdatePasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/25/16.
 * Class: UpdatePasswordRequest
 * Class Description: Request ot update user's password
 */
public class UpdatePasswordRequest extends IBMOrlandoServicesRequest implements Callback<UpdatePasswordResponse> {
    public static final String TAG = UpdatePasswordRequest.class.getSimpleName();

    public UpdatePasswordRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, UpdatePasswordRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class UpdatePasswordRequestParams extends NetworkParams {

        private UpdatePasswordRequestBodyParams bodyParams;

        public UpdatePasswordRequestParams() {
            super();
            bodyParams = new UpdatePasswordRequestBodyParams();
        }
    }

    public static class UpdatePasswordRequestBodyParams extends GsonObject {

        @SerializedName("oldPassword")
        public String oldPassword;

        @SerializedName("newPassword")
        public String newPassword;

    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private UpdatePasswordRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new UpdatePasswordRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setNewPassword(String newPassword) {
            this.requestParams.bodyParams.newPassword = newPassword;
            return getThis();
        }

        public Builder setOldPassword(String oldPassword) {
            this.requestParams.bodyParams.oldPassword = oldPassword;
            return getThis();
        }

        public UpdatePasswordRequest build() {
            return new UpdatePasswordRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        UpdatePasswordRequestParams requestParams = (UpdatePasswordRequestParams) getNetworkParams();
        String userId = AccountStateManager.getUsername();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try{
                    UpdatePasswordResponse response =
                            services.updatePassword(userId, requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.updatePassword(userId, requestParams.bodyParams, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(UpdatePasswordResponse updatePasswordResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (updatePasswordResponse == null) {
            updatePasswordResponse = new UpdatePasswordResponse();
        }
        super.handleSuccess(updatePasswordResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        UpdatePasswordResponse response = new UpdatePasswordResponse();

        response.setStatusCode(retrofitError.getResponse().getStatus());
        super.handleFailure(response, retrofitError);
    }
}
