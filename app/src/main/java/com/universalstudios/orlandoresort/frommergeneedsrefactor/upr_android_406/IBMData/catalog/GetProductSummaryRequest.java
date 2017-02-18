package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.BuildConfig;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by Tyler Ritchie on 10/12/16.
 */
public class GetProductSummaryRequest extends IBMOrlandoServicesRequest implements Callback<GetProductSummaryResponse> {

    // Logging Tag
    private static final String TAG = GetProductSummaryRequest.class.getSimpleName();

    protected GetProductSummaryRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetProductSummaryParams extends NetworkParams {
        private String categoryId;

        public GetProductSummaryParams() {
            super();
        }
    }

    /**
     * A Builder class for building a new {@link GetProductSummaryRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetProductSummaryParams getProductSummaryParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getProductSummaryParams = new GetProductSummaryParams();
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

        /**
         * [REQUIRED] Sets the category Id for looking up subcategories.
         *
         * @param categoryId
         *         the String for the category Id
         *
         * @return the {@link Builder}
         */
        public Builder setCategoryId(String categoryId) {
            this.getProductSummaryParams.categoryId = categoryId;
            return getThis();
        }

        public GetProductSummaryRequest build() {
            return new GetProductSummaryRequest(senderTag, priority, concurrencyType, getProductSummaryParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetProductSummaryParams params = (GetProductSummaryParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetProductSummaryResponse response = services.getProductSummaryByCategoryId(
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            params.categoryId);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getProductSummaryByCategoryId(
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        params.categoryId,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetProductSummaryResponse getProductSummaryResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        GetProductSummaryResponse gpsResponse = null != getProductSummaryResponse ?
                getProductSummaryResponse : new GetProductSummaryResponse();
        super.handleSuccess(gpsResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetProductSummaryResponse(), error);
    }
}