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
 * Request for checking account and email status.
 *
 * @author Tyler Ritchie
 */
public class EditCardRequest extends IBMOrlandoServicesRequest implements Callback<EditCardResponse> {
    private static final String TAG = EditCardRequest.class.getSimpleName();
    private static final String CURRENCY_CODE_USD = "USD";

    private EditCardRequest(String senderTag, Priority priority,
                           ConcurrencyType concurrencyType, EditCardRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link EditCardRequest.Builder}.
     */
    private static class EditCardRequestParams extends NetworkParams {
        private EditCardRequestBodyParams bodyParams;

        public EditCardRequestParams() {
            super();
            bodyParams = new EditCardRequestBodyParams();
        }
    }

    public static class EditCardRequestBodyParams extends GsonObject {

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("addressId")
        private String addressId;

        @SerializedName("paymentAccountNumber")
        private String paymentAccountNumber;

        @SerializedName("expireYear")
        private String expireYear;

        @SerializedName("expireMonth")
        private String expireMonth;

        @SerializedName("cardSecurityCode")
        private String cardSecurityCode;

        @SerializedName("currencyCode")
        private String currencyCode;

        @SerializedName("primary")
        private boolean primary;

        @SerializedName("cardholderName")
        private String cardholderName;

        @SerializedName("sourceId")
        private String sourceId;

        @SerializedName("deleteCardId")
        private String deleteCardId;

        public EditCardRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_CARD;
            currencyCode = CURRENCY_CODE_USD;
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link EditCardRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<EditCardRequest.Builder> {
        private EditCardRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new EditCardRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setExternalGuestId(String externalGuestId) {
            this.requestParams.bodyParams.externalGuestId = externalGuestId;
            return this;
        }

        public Builder setAddressId(String addressId) {
            this.requestParams.bodyParams.addressId = addressId;
            return this;
        }

        public Builder setPaymentAccountNumber(String paymentAccountNumber) {
            this.requestParams.bodyParams.paymentAccountNumber = paymentAccountNumber;
            return this;
        }

        public Builder setExpireYear(String expireYear) {
            this.requestParams.bodyParams.expireYear = expireYear;
            return this;
        }

        public Builder setExpireMonth(String expireMonth) {
            this.requestParams.bodyParams.expireMonth = expireMonth;
            return this;
        }

        public Builder setCardSecurityCode(String cardSecurityCode) {
            this.requestParams.bodyParams.cardSecurityCode = cardSecurityCode;
            return this;
        }

        public Builder setCurrencyCode(String currencyCode) {
            this.requestParams.bodyParams.currencyCode = currencyCode;
            return this;
        }

        public Builder setPrimary(boolean primary) {
            this.requestParams.bodyParams.primary = primary;
            return this;
        }

        public Builder setCardHolderName(String cardholderName) {
            this.requestParams.bodyParams.cardholderName = cardholderName;
            return this;
        }

        public Builder setDeleteCardId(String deleteCardId) {
            this.requestParams.bodyParams.deleteCardId = deleteCardId;
            return this;
        }

        public EditCardRequest build() {
            return new EditCardRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        EditCardRequestParams requestParams = (EditCardRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    EditCardResponse response = services.editCardInWallet(
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.editCardInWallet(
                        basicAuth,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(EditCardResponse editCardResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (editCardResponse == null) {
            editCardResponse = new EditCardResponse();
        }
        super.handleSuccess(editCardResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new EditCardResponse(), retrofitError);
    }
}
