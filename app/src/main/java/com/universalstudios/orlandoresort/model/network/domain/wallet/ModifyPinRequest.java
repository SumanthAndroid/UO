package com.universalstudios.orlandoresort.model.network.domain.wallet;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
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
 * Request for modifying a guest's PIN (or adding it if it doesn't exist)
 *
 * @author Tyler Ritchie
 */
public class ModifyPinRequest extends IBMOrlandoServicesRequest implements Callback<ModifyPinResponse> {
    private static final String TAG = ModifyPinRequest.class.getSimpleName();

    private ModifyPinRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, ModifyPinRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link ModifyPinRequest.Builder}.
     */
    private static class ModifyPinRequestParams extends NetworkParams {
        private ModifyPinRequestBodyParams bodyParams;

        public ModifyPinRequestParams() {
            super();
            bodyParams = new ModifyPinRequestBodyParams();
        }
    }

    public static class ModifyPinRequestBodyParams extends GsonObject {

        @SerializedName("pin")
        private String pin;

        @SerializedName("sourceId")
        private String sourceId;

        public ModifyPinRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_PIN;
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link ModifyPinRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<ModifyPinRequest.Builder> {
        private ModifyPinRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new ModifyPinRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setPin(String pin) {
            this.requestParams.bodyParams.pin = pin;
            return this;
        }

        public ModifyPinRequest build() {
            return new ModifyPinRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        ModifyPinRequestParams requestParams = (ModifyPinRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();
        String guestId = AccountStateManager.getGuestId();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    ModifyPinResponse response = services.modifyCardPin(
                            guestId,
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.modifyCardPin(
                        guestId,
                        basicAuth,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(ModifyPinResponse modifyPinResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (modifyPinResponse == null) {
            modifyPinResponse = new ModifyPinResponse();
        }
        super.handleSuccess(modifyPinResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new ModifyPinResponse(), retrofitError);
    }
}
