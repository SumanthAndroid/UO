package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by Tyler Ritchie on 10/12/16.
 */
public class GetSubcategoryRequest extends IBMOrlandoServicesRequest implements Callback<GetSubcategoryResponse> {

    // Logging Tag
    private static final String TAG = GetSubcategoryRequest.class.getSimpleName();

    protected GetSubcategoryRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetSubcategoriesParams extends NetworkParams {
        private String categoryId;

        public GetSubcategoriesParams() {
            super();
        }
    }

    /**
     * A Builder class for building a new {@link GetSubcategoryRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetSubcategoriesParams getSubcategoriesParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getSubcategoriesParams = new GetSubcategoriesParams();
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
            this.getSubcategoriesParams.categoryId = categoryId;
            return getThis();
        }

        public GetSubcategoryRequest build() {
            return new GetSubcategoryRequest(senderTag, priority, concurrencyType, getSubcategoriesParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetSubcategoriesParams params = (GetSubcategoriesParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetSubcategoryResponse response = services.getSubCategoriesByCategoryId(
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
                services.getSubCategoriesByCategoryId(
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
    public void success(GetSubcategoryResponse getSubcategoryResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(getSubcategoryResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetSubcategoryResponse(), error);
    }
}