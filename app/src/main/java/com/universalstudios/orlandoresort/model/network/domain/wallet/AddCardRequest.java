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
public class AddCardRequest extends IBMOrlandoServicesRequest implements Callback<AddCardResponse> {
    private static final String TAG = AddCardRequest.class.getSimpleName();
    private static final String CURRENCY_CODE_USD = "USD";

    private AddCardRequest(String senderTag, Priority priority,
                           ConcurrencyType concurrencyType, AddCardRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link AddCardRequest.Builder}.
     */
    private static class AddCardRequestParams extends NetworkParams {
        private AddCardRequestBodyParams bodyParams;

        public AddCardRequestParams() {
            super();
            bodyParams = new AddCardRequestBodyParams();
        }
    }

    public static class AddCardRequestBodyParams extends GsonObject {

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

        public AddCardRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_CARD;
            currencyCode = CURRENCY_CODE_USD;
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link AddCardRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<AddCardRequest.Builder> {
        private AddCardRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new AddCardRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setExternalGuestId(String externalGuestId) {
            this.requestParams.bodyParams.externalGuestId = externalGuestId;
            return getThis();
        }

        public Builder setAddressId(String addressId) {
            this.requestParams.bodyParams.addressId = addressId;
            return getThis();
        }

        public Builder setPaymentAccountNumber(String paymentAccountNumber) {
            this.requestParams.bodyParams.paymentAccountNumber = paymentAccountNumber;
            return getThis();
        }

        public Builder setExpireYear(String expireYear) {
            this.requestParams.bodyParams.expireYear = expireYear;
            return getThis();
        }

        public Builder setExpireMonth(String expireMonth) {
            this.requestParams.bodyParams.expireMonth = expireMonth;
            return getThis();
        }

        public Builder setCardSecurityCode(String cardSecurityCode) {
            this.requestParams.bodyParams.cardSecurityCode = cardSecurityCode;
            return getThis();
        }

        public Builder setCurrencyCode(String currencyCode) {
            this.requestParams.bodyParams.currencyCode = currencyCode;
            return getThis();
        }

        public Builder setPrimary(boolean primary) {
            this.requestParams.bodyParams.primary = primary;
            return getThis();
        }

        public Builder setCardHolderName(String cardholderName) {
            this.requestParams.bodyParams.cardholderName = cardholderName;
            return getThis();
        }

        public AddCardRequest build() {
            return new AddCardRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        AddCardRequestParams requestParams = (AddCardRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    AddCardResponse response = services.addCardToWallet(
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.addCardToWallet(
                        basicAuth,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(AddCardResponse addCardResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (addCardResponse == null) {
            addCardResponse = new AddCardResponse();
        }
        super.handleSuccess(addCardResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new AddCardResponse(), retrofitError);
    }
}
