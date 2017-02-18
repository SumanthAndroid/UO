package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CreditCardInfo;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.SubmitPurchaseResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.math.BigDecimal;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/14/16.
 * Class: SubmitPurchaseResponse
 * Class Description: Request class to submit purchase data
 */
public class SubmitPurchaseRequest extends IBMOrlandoServicesRequest implements Callback<SubmitPurchaseResponse> {
    private static final String TAG = SubmitPurchaseRequest.class.getSimpleName();
    private static final String CURRENCY_CODE_USD = "USD";

    public SubmitPurchaseRequest(String senderTag, Priority priority,
                                 ConcurrencyType concurrencyType, SubmitPurchaseRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    private static class SubmitPurchaseRequestParams extends NetworkParams {
        private SubmitPurchaseRequestBody requestBody;

        public SubmitPurchaseRequestParams() {
            requestBody = new SubmitPurchaseRequestBody();
        }
    }

    public static class SubmitPurchaseRequestBody extends GsonObject {

        @SerializedName("sourceId")
        public @SourceId.SourceIdType String sourceId;

        @SerializedName("amount")
        public BigDecimal amount;

        @SerializedName("orderId")
        public String orderId;

        @SerializedName("cardSecurityCode")
        public String cardSecurityCode;

        @SerializedName("expireYear")
        public String expireYear;

        @SerializedName("billingAddressId")
        public String billingAddressId;

        @SerializedName("paymentAccountNumber")
        public String accountNumber;

        @SerializedName("currencyCode")
        public String currencyCode;

        @SerializedName("expireMonth")
        public String expireMonth;

        public SubmitPurchaseRequestBody() {
            sourceId = SourceId.PAYMENT_BILLING_INFO;
            currencyCode = CURRENCY_CODE_USD;
        }

        public void setExpireMonth(int expireMonthIndex) {
            String expireMonth = null;
            if (CreditCardInfo.EXPIRATION_MONTHS.length > expireMonthIndex) {
                expireMonth = CreditCardInfo.EXPIRATION_MONTHS[expireMonthIndex];
            }
            this.expireMonth = expireMonth;
        }

        public void setExpireYear(int expireYearIndex) {
            String expireYear = null;
            if (CreditCardInfo.EXPIRATION_YEARS.length > expireYearIndex) {
                expireYear = CreditCardInfo.EXPIRATION_YEARS[expireYearIndex];
            }
            this.expireYear = expireYear;
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private SubmitPurchaseRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new SubmitPurchaseRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            requestParams.requestBody.amount = amount;
            return getThis();
        }

        public Builder setOrderId(String orderId) {
            requestParams.requestBody.orderId = orderId;
            return getThis();
        }

        public Builder setCardSecurityCode(String cardSecurityCode) {
            requestParams.requestBody.cardSecurityCode = cardSecurityCode;
            return getThis();
        }

        public Builder setBillingAddressId(String billingAddressId) {
            requestParams.requestBody.billingAddressId = billingAddressId;
            return getThis();
        }

        public Builder setAccountNumber(String accountNumber) {
            requestParams.requestBody.accountNumber = accountNumber;
            return getThis();
        }

        public Builder setExpireMonth(int expireMonth) {
            requestParams.requestBody.setExpireMonth(expireMonth);
            return getThis();
        }

        public Builder setExpireYear(int expireYear) {
            requestParams.requestBody.setExpireYear(expireYear);
            return getThis();
        }

        public SubmitPurchaseRequest build() {
            return new SubmitPurchaseRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        SubmitPurchaseRequestParams requestParams = (SubmitPurchaseRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try{
                    SubmitPurchaseResponse response = services.submitPurchase(
                            wcToken,
                            wcTrustedToken,
                            requestParams.requestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.submitPurchase(
                        wcToken,
                        wcTrustedToken,
                        requestParams.requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(SubmitPurchaseResponse submitPurchaseResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }

        if (submitPurchaseResponse == null) {
            submitPurchaseResponse = new SubmitPurchaseResponse();
        }
        super.handleSuccess(submitPurchaseResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }

        SubmitPurchaseResponse response = new SubmitPurchaseResponse();
        super.handleFailure(response, retrofitError);

        // Hack to get the HTTP status code for payment processing response
        // TODO Fix this when creating overarching failure method
        response.setError(retrofitError);
    }
}
