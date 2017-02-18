package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */

public class GetPriceInventoryRequest extends IBMOrlandoServicesRequest implements Callback<GetPriceInventoryResponse> {

    // Logging Tag
    private static final String TAG = GetPriceInventoryRequest.class.getSimpleName();

    private static final String CONTRACT_ID = "4000000000000000003";
    private static final String CURRENCY_USD = "USD";

    private GetPriceInventoryRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetPriceInventoryParams extends NetworkParams {
        private GetPriceInventoryRequestBody requestBody;

        public GetPriceInventoryParams() {
            super();
            requestBody = new GetPriceInventoryRequestBody();
        }
    }

    public static class GetPriceInventoryRequestBody extends GsonObject {

        @SerializedName("contractId")
        private String contractId;

        @SerializedName("currency")
        private String currency;

        @SerializedName("events")
        private List<PriceInventoryEvent> events;

        public GetPriceInventoryRequestBody() {
            super();
            events = new ArrayList<>();
            contractId = CONTRACT_ID;
            currency = CURRENCY_USD;
        }

    }

    /**
     * A Builder class for building a new {@link GetPriceInventoryRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetPriceInventoryParams getPriceInventoryParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getPriceInventoryParams = new GetPriceInventoryParams();
        }

        /**
         * Method required by {@link BaseNetworkRequestBuilder} to allow the proper builder pattern
         * with child classes.
         *
         * @return the {@link Builder}
         */
        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder addPriceInventoryEvent(PriceInventoryEvent event) {
            this.getPriceInventoryParams.requestBody.events.add(event);
            return getThis();
        }

        public GetPriceInventoryRequest build() {
            return new GetPriceInventoryRequest(senderTag, priority, concurrencyType, getPriceInventoryParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetPriceInventoryParams params = (GetPriceInventoryParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetPriceInventoryResponse response = services.getPriceInventory(
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            params.requestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getPriceInventory(
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        params.requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetPriceInventoryResponse getPriceInventoryResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getPriceInventoryResponse == null) {
            getPriceInventoryResponse = new GetPriceInventoryResponse();
        }
        super.handleSuccess(getPriceInventoryResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetPriceInventoryResponse(), error);
    }
}