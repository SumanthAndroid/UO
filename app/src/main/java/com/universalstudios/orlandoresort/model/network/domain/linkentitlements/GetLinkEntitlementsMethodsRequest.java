package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

/**
 * Created by Tyler Ritchie on 1/13/17.
 */

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
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
 * Request for checking account and email status.
 *
 * @author Tyler Ritchie
 */
public class GetLinkEntitlementsMethodsRequest extends IBMOrlandoServicesRequest implements Callback<GetLinkEntitlementsMethodsResponse> {
    private static final String TAG = GetLinkEntitlementsMethodsRequest.class.getSimpleName();

    private GetLinkEntitlementsMethodsRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, GetLinkEntitlementsMethodsRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link GetLinkEntitlementsMethodsRequest.Builder}.
     */
    private static class GetLinkEntitlementsMethodsRequestParams extends NetworkParams {
        private GetLinkEntitlementsMethodsRequestBodyParams bodyParams;

        public GetLinkEntitlementsMethodsRequestParams() {
            super();
            bodyParams = new GetLinkEntitlementsMethodsRequestBodyParams();
        }
    }

    public static class GetLinkEntitlementsMethodsRequestBodyParams extends GsonObject {

        @SerializedName("mediaId")
        private String mediaId;
        
    }

    /**
     * Builder for setting parameter fields and generating the {@link GetLinkEntitlementsMethodsRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<GetLinkEntitlementsMethodsRequest.Builder> {
        private GetLinkEntitlementsMethodsRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new GetLinkEntitlementsMethodsRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setMediaId(String mediaId) {
            this.requestParams.bodyParams.mediaId = mediaId;
            return getThis();
        }

        public GetLinkEntitlementsMethodsRequest build() {
            return new GetLinkEntitlementsMethodsRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        GetLinkEntitlementsMethodsRequestParams requestParams = (GetLinkEntitlementsMethodsRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetLinkEntitlementsMethodsResponse response = services.getLinkEntitlementsMethods(
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getLinkEntitlementsMethods(
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetLinkEntitlementsMethodsResponse getLinkEntitlementsMethodsResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getLinkEntitlementsMethodsResponse == null) {
            getLinkEntitlementsMethodsResponse = new GetLinkEntitlementsMethodsResponse();
        }
        super.handleSuccess(getLinkEntitlementsMethodsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetLinkEntitlementsMethodsResponse(), retrofitError);
    }
}
