package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

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
 * Created by Tyler Ritchie on 10/13/16.
 */
public class GetCatalogSubcategoryRequest extends IBMOrlandoServicesRequest implements Callback<GetCatalogSubcategoryResponse> {

    // Logging Tag
    private static final String TAG = GetCatalogSubcategoryRequest.class.getSimpleName();

    private GetCatalogSubcategoryRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetCatalogSubcategoryParams extends NetworkParams {
        private String categoryId;

        public GetCatalogSubcategoryParams() {
            super();
        }
    }

    /**
     * A Builder class for building a new {@link GetCatalogSubcategoryRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetCatalogSubcategoryParams getCatalogSubcategoryParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getCatalogSubcategoryParams = new GetCatalogSubcategoryParams();
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
         * [REQUIRED] Sets the category Id for looking up a subcategory.
         *
         * @param categoryId
         *         the String for the category Id
         *
         * @return the {@link Builder}
         */
        public Builder setCategoryId(String categoryId) {
            this.getCatalogSubcategoryParams.categoryId = categoryId;
            return getThis();
        }

        public GetCatalogSubcategoryRequest build() {
            return new GetCatalogSubcategoryRequest(senderTag, priority, concurrencyType, getCatalogSubcategoryParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetCatalogSubcategoryParams params = (GetCatalogSubcategoryParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetCatalogSubcategoryResponse response = services.getCatalogSubcategory(params.categoryId);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getCatalogSubcategory(params.categoryId, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetCatalogSubcategoryResponse getCatalogSubcategoryResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(getCatalogSubcategoryResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetCatalogSubcategoryResponse(), error);
    }
}