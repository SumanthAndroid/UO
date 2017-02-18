package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.LoginUserResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * DO NOT USE THIS REQUEST DIRECTLY. Use {@link com.universalstudios.orlandoresort.model.state.account.AccountLoginService}.
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/26/16.
 * Class: LoginUserRequest
 * Class Description: Request for logging in the user
 */
public class LoginUserRequest extends IBMOrlandoServicesRequest implements Callback<LoginUserResponse> {
    public static final String TAG = LoginUserRequest.class.getSimpleName();

    public LoginUserRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType,
            LoginUserRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class LoginUserRequestParams extends NetworkParams {
        private LoginRequestBody loginRequestBody;

        public LoginUserRequestParams() {
            super();
            loginRequestBody = new LoginRequestBody();
        }
    }

    public static class LoginRequestBody extends GsonObject {
        @SerializedName("username")
        private String username;

        @SerializedName("password")
        private String password;
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private LoginUserRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            requestParams = new LoginUserRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setUsername(String username) {
            this.requestParams.loginRequestBody.username = username;
            return getThis();
        }

        public Builder setPassword(String password) {
            this.requestParams.loginRequestBody.password = password;
            return getThis();
        }

        public LoginUserRequest build() {
            return new LoginUserRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        LoginUserRequestParams requestParams = (LoginUserRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try{
                    LoginUserResponse response =
                            services.loginUser(requestParams.loginRequestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.loginUser(requestParams.loginRequestBody, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(LoginUserResponse loginUserResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (loginUserResponse == null) {
            loginUserResponse = new LoginUserResponse();
        }
        super.handleSuccess(loginUserResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new LoginUserResponse(), retrofitError);
    }
}
