package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.BuildConfig;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by Tyler Ritchie on 10/13/16.
 */
public class GetCatalogCategoryRequest extends IBMOrlandoServicesRequest implements Callback<GetCatalogCategoryResponse> {

    // Logging Tag
    private static final String TAG = GetCatalogCategoryRequest.class.getSimpleName();

    private GetCatalogCategoryRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    /**
     * A Builder class for building a new {@link GetCatalogCategoryRequest}.
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

        public GetCatalogCategoryRequest build() {
            return new GetCatalogCategoryRequest(senderTag, priority, concurrencyType);
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
                    GetCatalogCategoryResponse response = services.getCatalogCategories();
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getCatalogCategories(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetCatalogCategoryResponse getCatalogCategoryResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }

        GetCatalogCategoryResponse gccResponse = null != getCatalogCategoryResponse ?
                getCatalogCategoryResponse : new GetCatalogCategoryResponse();
        super.handleSuccess(gccResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetCatalogCategoryResponse(), error);
    }
}