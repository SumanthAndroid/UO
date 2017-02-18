package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * DO NOT USE THIS REQUEST DIRECTLY. Use {@link com.universalstudios.orlandoresort.model.state.account.AccountLoginService}.
 * Created by IBM_ADMIN on 1/25/2016.
 * Edited by Tyler Ritchie on 11/28/2016.
 */
public class GetAuthNRequest extends IBMOrlandoServicesRequest implements Callback<GetAuthNResponse> {

    private static final String TAG = GetAuthNRequest.class.getSimpleName();

    private GetAuthNRequest(String senderTag, Priority priority,
                            ConcurrencyType concurrencyType, GetGuestIdentityRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link GetAuthNRequest.Builder}.
     */
    private static class GetGuestIdentityRequestParams extends NetworkParams {
        private GetGuestIdentityBodyParams guestIdentityRequestParams;
        public GetGuestIdentityRequestParams() {
            super();
            guestIdentityRequestParams = new GetGuestIdentityBodyParams();
        }
    }

    public static class GetGuestIdentityBodyParams extends GsonObject {

        @SerializedName("externalGuestId")
        private String externalGuestId;

        public GetGuestIdentityBodyParams() {
            super();
            externalGuestId = AccountStateManager.getGuestId();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private GetGuestIdentityRequestParams networkParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.networkParams = new GetGuestIdentityRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetAuthNRequest build() {
            return new GetAuthNRequest(senderTag, priority, concurrencyType, networkParams);
        }
    }


    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        GetGuestIdentityRequestParams params = (GetGuestIdentityRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetAuthNResponse response =
                            services.postAuthN(
                                    params.guestIdentityRequestParams);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.postAuthN(params.guestIdentityRequestParams, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetAuthNResponse getAuthNResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        NetworkResponse networkResponse = null;
        if (null == getAuthNResponse || null == getAuthNResponse.getResult()
                || TextUtils.isEmpty(getAuthNResponse.getResult().getWCToken())
                || TextUtils.isEmpty(getAuthNResponse.getResult().getWCTrustedToken())) {
            networkResponse = new GetAuthNFailureResponse();
        } else {
            networkResponse = getAuthNResponse;
        }
        super.handleSuccess(networkResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetAuthNFailureResponse(), retrofitError);
    }
}
