package com.universalstudios.orlandoresort.model.network.domain.cart.promo;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author acampbell
 */
public class RemovePromoCodeRequest extends IBMOrlandoServicesRequest implements Callback<RemovePromoCodeResponse> {

    private static final String TAG = RemovePromoCodeRequest.class.getSimpleName();

    private RemovePromoCodeRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class RemovePromoCodeRequestParams extends NetworkParams {
        private String promoCode;

        public RemovePromoCodeRequestParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private RemovePromoCodeRequestParams params;

        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
            params = new RemovePromoCodeRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setPromoCode(String promoCode) {
            params.promoCode = promoCode;
            return getThis();
        }

        @Override
        public RemovePromoCodeRequest build() {
            return new RemovePromoCodeRequest(senderTag, priority, concurrencyType, params);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        RemovePromoCodeRequestParams requestParams = (RemovePromoCodeRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    RemovePromoCodeResponse response = services.removePromoCodeFromCart(
                            wcToken,
                            wcTrustedToken,
                            BuildConfig.COMMERCE_STORE_ID,
                            requestParams.promoCode);
                    handleSuccess(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.removePromoCodeFromCart(
                        wcToken,
                        wcTrustedToken,
                        BuildConfig.COMMERCE_STORE_ID,
                        requestParams.promoCode,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(RemovePromoCodeResponse removePromoCodeResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (removePromoCodeResponse == null) {
            removePromoCodeResponse = new RemovePromoCodeResponse();
        }
        super.handleSuccess(removePromoCodeResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new RemovePromoCodeResponse(), retrofitError);
    }
}
