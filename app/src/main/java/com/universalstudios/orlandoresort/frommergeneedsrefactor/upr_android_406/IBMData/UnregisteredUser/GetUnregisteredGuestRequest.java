package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.UnregisteredUser;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetPersonalizationExtrasRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * DO NOT USE THIS REQUEST DIRECTLY. Use {@link com.universalstudios.orlandoresort.model.state.account.AccountLoginService}
 * Created by IBM_ADMIN on 5/4/2016.
 * Edited by Tyler Ritchie on 12/1/2016.
 */
public class GetUnregisteredGuestRequest extends IBMOrlandoServicesRequest implements Callback<GetUnregisteredGuestResponse> {

    private static final String TAG = GetUnregisteredGuestRequest.class.getSimpleName();

    private GetUnregisteredGuestRequest(String senderTag, NetworkRequest.Priority priority,
                                        NetworkRequest.ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }
    /**
     * Private internal Network Parameter class used by the {@link GetPersonalizationExtrasRequest.Builder}.
     */
    private static class GetUnregisteredGuestParams extends NetworkParams {

        private GetUnregisteredGuestBodyParams mGetUnregisteredGuestBodyParams;

        public GetUnregisteredGuestParams() {
            super();
            mGetUnregisteredGuestBodyParams = new GetUnregisteredGuestBodyParams();
        }
    }

    public static class GetUnregisteredGuestBodyParams extends GsonObject {

        @SerializedName("guestProfile")
        private GetUnregisteredGuestProfileParams mGetUnregisteredGuestProfileParams;

        @SerializedName("externalGuestId")
        private String externalGuestId;

        public GetUnregisteredGuestBodyParams() {
            super();
            mGetUnregisteredGuestProfileParams = new GetUnregisteredGuestProfileParams();
            externalGuestId = AccountStateManager.getGuestId();
        }
    }

    private static class GetUnregisteredGuestProfileParams extends GsonObject {

        @SerializedName("sourceId")
        private String sourceId;

        public GetUnregisteredGuestProfileParams() {
            super();
            sourceId = GuestProfile.GUEST_PROFILE_SOURCE_ID;
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private GetUnregisteredGuestParams getUnregisteredGuestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getUnregisteredGuestParams = new GetUnregisteredGuestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetUnregisteredGuestRequest build() {
            return new GetUnregisteredGuestRequest(senderTag, priority, concurrencyType, getUnregisteredGuestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);
        GetUnregisteredGuestParams params = (GetUnregisteredGuestParams)getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetUnregisteredGuestResponse response = services.createUnregisterGuest(params.mGetUnregisteredGuestBodyParams);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.createUnregisterGuest(params.mGetUnregisteredGuestBodyParams, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetUnregisteredGuestResponse getUnregisteredGuestResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getUnregisteredGuestResponse == null) {
            getUnregisteredGuestResponse = new GetUnregisteredGuestResponse();
        }
        super.handleSuccess(getUnregisteredGuestResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetUnregisteredGuestResponse getUnregisteredGuestResponse = new GetUnregisteredGuestResponse();
        super.handleFailure(getUnregisteredGuestResponse, retrofitError);
    }
}
