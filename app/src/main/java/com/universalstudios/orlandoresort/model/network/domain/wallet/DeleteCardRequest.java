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
public class DeleteCardRequest extends IBMOrlandoServicesRequest implements Callback<DeleteCardResponse> {
    private static final String TAG = DeleteCardRequest.class.getSimpleName();

    private DeleteCardRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, DeleteCardRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link DeleteCardRequest.Builder}.
     */
    private static class DeleteCardRequestParams extends NetworkParams {

        private DeleteCardRequestBodyParams bodyParams;

        public DeleteCardRequestParams() {
            super();
            bodyParams = new DeleteCardRequestBodyParams();
        }

    }

    public static class DeleteCardRequestBodyParams extends GsonObject {

        @SerializedName("cardId")
        private String cardId;

        @SerializedName("sourceId")
        private String sourceId;

        public DeleteCardRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_CARD;
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link DeleteCardRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<DeleteCardRequest.Builder> {

        private DeleteCardRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new DeleteCardRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setCardId(String cardId) {
            this.requestParams.bodyParams.cardId = cardId;
            return getThis();
        }

        public DeleteCardRequest build() {
            return new DeleteCardRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        DeleteCardRequestParams requestParams = (DeleteCardRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();
        String guestId = AccountStateManager.getGuestId();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    DeleteCardResponse response = services.deleteCardFromWallet(
                            guestId,
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.deleteCardFromWallet(
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
    public void success(DeleteCardResponse deleteCardResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (deleteCardResponse == null) {
            deleteCardResponse = new DeleteCardResponse();
        }
        super.handleSuccess(deleteCardResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new DeleteCardResponse(), retrofitError);
    }
}
