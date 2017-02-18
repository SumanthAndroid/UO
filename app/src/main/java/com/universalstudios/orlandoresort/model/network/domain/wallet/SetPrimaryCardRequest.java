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
 * Request for setting primary card.
 *
 * @author Tyler Ritchie
 */
public class SetPrimaryCardRequest extends IBMOrlandoServicesRequest implements Callback<SetPrimaryCardResponse> {
    private static final String TAG = SetPrimaryCardRequest.class.getSimpleName();

    private SetPrimaryCardRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, SetPrimaryCardRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link SetPrimaryCardRequest.Builder}.
     */
    private static class SetPrimaryCardRequestParams extends NetworkParams {

        private SetPrimaryCardRequestBodyParams bodyParams;

        public SetPrimaryCardRequestParams() {
            super();
            bodyParams = new SetPrimaryCardRequestBodyParams();
        }

    }

    public static class SetPrimaryCardRequestBodyParams extends GsonObject {

        @SerializedName("cardId")
        private String cardId;

        @SerializedName("sourceId")
        private String sourceId;

        @SerializedName("externalGuestID")
        private String externalGuestId;

        public SetPrimaryCardRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_CARD;
            externalGuestId = AccountStateManager.getGuestId();
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link SetPrimaryCardRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<SetPrimaryCardRequest.Builder> {

        private SetPrimaryCardRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new SetPrimaryCardRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setCardId(String cardId) {
            this.requestParams.bodyParams.cardId = cardId;
            return getThis();
        }

        public SetPrimaryCardRequest build() {
            return new SetPrimaryCardRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        SetPrimaryCardRequestParams requestParams = (SetPrimaryCardRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    SetPrimaryCardResponse response = services.setPrimaryCard(
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.setPrimaryCard(
                        basicAuth,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(SetPrimaryCardResponse setPrimaryCardResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (setPrimaryCardResponse == null) {
            setPrimaryCardResponse = new SetPrimaryCardResponse();
        }
        super.handleSuccess(setPrimaryCardResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new SetPrimaryCardResponse(), retrofitError);
    }
}
