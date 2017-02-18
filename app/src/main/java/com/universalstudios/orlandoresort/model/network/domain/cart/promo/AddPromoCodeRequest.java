package com.universalstudios.orlandoresort.model.network.domain.cart.promo;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for adding a promo code to the cart
 *
 * @author acampbell
 */
public class AddPromoCodeRequest extends IBMOrlandoServicesRequest implements Callback<AddPromoCodeResponse> {
    private static final String TAG = AddPromoCodeRequest.class.getSimpleName();

    private AddPromoCodeRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, AddPromoCodeRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class AddPromoCodeRequestParams extends NetworkParams {
        private AddPromoCodeRequestBodyParams requestBody;

        public AddPromoCodeRequestParams() {
            super();
            requestBody = new AddPromoCodeRequestBodyParams();
        }
    }

    public static class AddPromoCodeRequestBodyParams extends GsonObject {
        @SerializedName("promoCode")
        private String promoCode;

        public AddPromoCodeRequestBodyParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private AddPromoCodeRequestParams params;

        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
            params = new AddPromoCodeRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setPromoCode(String promoCode) {
            params.requestBody.promoCode = promoCode;
            return getThis();
        }

        public AddPromoCodeRequest build() {
            return new AddPromoCodeRequest(senderTag, priority, concurrencyType, params);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        AddPromoCodeRequestParams requestParams = (AddPromoCodeRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    AddPromoCodeResponse response = services.addPromoCodeToCart(
                            wcToken,
                            wcTrustedToken,
                            BuildConfig.COMMERCE_STORE_ID,
                            requestParams.requestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.addPromoCodeToCart(
                        wcToken,
                        wcTrustedToken,
                        BuildConfig.COMMERCE_STORE_ID,
                        requestParams.requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(AddPromoCodeResponse addPromoCodeResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (addPromoCodeResponse == null) {
            addPromoCodeResponse = new AddPromoCodeResponse();
        }
        super.handleSuccess(addPromoCodeResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new AddPromoCodeResponse(), retrofitError);
    }
}