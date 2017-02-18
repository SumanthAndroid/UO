package com.universalstudios.orlandoresort.model.network.domain.account;

/**
 * Created by Tyler Ritchie on 1/13/17.
 */

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for checking account and email status.
 *
 * @author Tyler Ritchie
 */
public class GetAccountStatusRequest extends IBMOrlandoServicesRequest implements Callback<GetAccountStatusResponse> {
    private static final String TAG = GetAccountStatusRequest.class.getSimpleName();

    private GetAccountStatusRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, GetAccountStatusRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link GetAccountStatusRequest.Builder}.
     */
    private static class GetAccountStatusRequestParams extends NetworkParams {

        private String email;

    }

    /**
     * Builder for setting parameter fields and generating the {@link GetAccountStatusRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<GetAccountStatusRequest.Builder> {
        private GetAccountStatusRequestParams getAccountStatusRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getAccountStatusRequestParams = new GetAccountStatusRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setEmail(String email) {
            this.getAccountStatusRequestParams.email = email;
            return getThis();
        }

        public GetAccountStatusRequest build() {
            return new GetAccountStatusRequest(senderTag, priority, concurrencyType, getAccountStatusRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        GetAccountStatusRequestParams requestParams = (GetAccountStatusRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetAccountStatusResponse response = services.getAccountStatus(
                            requestParams.email);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getAccountStatus(
                        requestParams.email,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetAccountStatusResponse getAccountStatusResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getAccountStatusResponse == null) {
            getAccountStatusResponse = new GetAccountStatusResponse();
        }
        super.handleSuccess(getAccountStatusResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetAccountStatusResponse(), retrofitError);
    }
}
