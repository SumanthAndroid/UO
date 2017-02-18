package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by Tyler Ritchie on 10/12/16.
 */
public class GetCategoryRequest extends IBMOrlandoServicesRequest implements Callback<GetCategoryResponse> {

    // Logging Tag
    private static final String TAG = GetCategoryRequest.class.getSimpleName();

    private GetCategoryRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    /**
     * A Builder class for building a new {@link GetCategoryRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        public Builder(NetworkRequestSender sender) {
            super(sender);
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

        public GetCategoryRequest build() {
            return new GetCategoryRequest(senderTag, priority, concurrencyType);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetCategoryResponse response = services.getCategories(BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION, BuildConfig.COMMERCE_STORE_ID);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getCategories(BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION, BuildConfig.COMMERCE_STORE_ID, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetCategoryResponse getCategoryResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(getCategoryResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetCategoryResponse(), error);
    }
}