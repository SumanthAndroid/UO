package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for retrieving eligible shipping modes.
 *
 * @author Steven Byle
 * @authoer Tyler Ritchie 11/7/16
 */
public class GetEligibleShipModesRequest extends IBMOrlandoServicesRequest implements Callback<GetEligibleShipModesResponse> {
    private static final String TAG = GetEligibleShipModesRequest.class.getSimpleName();
    private static final String CACHE_CONTROL = "no-cache";
    private static final String LANG_ID = "-1";
    private static final String CONTRACT_ID = "4000000000000000003";

    private GetEligibleShipModesRequest(String senderTag,
                                        Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        public Builder(NetworkRequestSender sender) {
            super(sender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetEligibleShipModesRequest build() {
            return new GetEligibleShipModesRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                services.getEligibleShipModes(
                        CACHE_CONTROL,
                        wcToken,
                        wcTrustedToken,
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        LANG_ID,
                        CONTRACT_ID);
                break;
            case ASYNCHRONOUS:
                services.getEligibleShipModes(
                        CACHE_CONTROL,
                        wcToken,
                        wcTrustedToken,
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        LANG_ID,
                        CONTRACT_ID,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetEligibleShipModesResponse getResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getResponse == null) {
            getResponse = new GetEligibleShipModesResponse();
        }
        super.handleSuccess(getResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }

        GetEligibleShipModesResponse response = new GetEligibleShipModesResponse();
        super.handleFailure(response, retrofitError);
    }

}
