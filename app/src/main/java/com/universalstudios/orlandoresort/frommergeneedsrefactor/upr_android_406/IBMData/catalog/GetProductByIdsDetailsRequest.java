package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetProductByIdsDetailsRequest extends IBMOrlandoServicesRequest implements Callback<GetProductDetailsResponse> {

    // Logging Tag
    private static final String TAG = GetProductByIdsDetailsRequest.class.getSimpleName();

    protected GetProductByIdsDetailsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetProductDetailsParams extends NetworkParams {
        private List<String> idList;

        public GetProductDetailsParams() {
            super();
        }
    }

    /**
     * A Builder class for building a new {@link GetProductByIdsDetailsRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetProductDetailsParams getProductDetailsParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getProductDetailsParams = new GetProductDetailsParams();
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
         * @param categoryId the String for the category Id
         * @return the {@link Builder}
         */
        public Builder setSkuIds(List<String> categoryId) {
            this.getProductDetailsParams.idList = categoryId;
            return getThis();
        }

        public GetProductByIdsDetailsRequest build() {
            return new GetProductByIdsDetailsRequest(senderTag, priority, concurrencyType, getProductDetailsParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetProductDetailsParams params = (GetProductDetailsParams) getNetworkParams();
        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetProductDetailsResponse response = services.getProductDetailsByIds(
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            params.idList);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getProductDetailsByIds(
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        params.idList,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetProductDetailsResponse getProductDetailsByIdsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getProductDetailsByIdsResponse == null) {
            getProductDetailsByIdsResponse = new GetProductDetailsResponse();
        }

        super.handleSuccess(getProductDetailsByIdsResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetProductDetailsResponse(), error);
    }
}