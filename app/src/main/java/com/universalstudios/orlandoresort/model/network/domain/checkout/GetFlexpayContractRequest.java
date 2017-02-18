package com.universalstudios.orlandoresort.model.network.domain.checkout;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
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
 * Retrieves Flexpay contract data for a given order ID.
 *
 * @author Tyler Ritchie
 * @since 1/11/2017
 */
public class GetFlexpayContractRequest extends IBMOrlandoServicesRequest implements Callback<GetFlexpayContractResponse> {
    private static final String TAG = GetFlexpayContractRequest.class.getSimpleName();

    private GetFlexpayContractRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetFlexpayContractRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    private static class GetFlexpayContractRequestParams extends NetworkParams {
        private GetFlexpayContractRequestBodyParams bodyParams;

        public GetFlexpayContractRequestParams() {
            bodyParams = new GetFlexpayContractRequestBodyParams();
        }
    }

    public static class GetFlexpayContractRequestBodyParams extends GsonObject {

        @SerializedName("orderId")
        private String orderId;

    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private GetFlexpayContractRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new GetFlexpayContractRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setOrderId(String orderId) {
            requestParams.bodyParams.orderId = orderId;
            return getThis();
        }

        public GetFlexpayContractRequest build() {
            return new GetFlexpayContractRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        GetFlexpayContractRequestParams requestParams = (GetFlexpayContractRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();
        String basicAuth = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetFlexpayContractResponse response = services.getFlexpayContract(
                            basicAuth,
                            wcToken,
                            wcTrustedToken,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getFlexpayContract(
                        basicAuth,
                        wcToken,
                        wcTrustedToken,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetFlexpayContractResponse getResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getResponse == null) {
            getResponse = new GetFlexpayContractResponse();
        }
        super.handleSuccess(getResponse, response);
    }


    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetFlexpayContractResponse(), retrofitError);
    }

}

