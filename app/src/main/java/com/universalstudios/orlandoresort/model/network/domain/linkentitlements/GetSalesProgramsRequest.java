package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

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
 * Created by Tyler Ritchie on 1/23/17.
 *
 * This request class calls to retrieve all sales programs (names and sales program ID for linking).
 *
 */
public class GetSalesProgramsRequest extends IBMOrlandoServicesRequest implements Callback<GetSalesProgramsResponse> {

    private static final String TAG = GetSalesProgramsRequest.class.getSimpleName();

    private GetSalesProgramsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
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

        public GetSalesProgramsRequest build() {
            return new GetSalesProgramsRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetSalesProgramsResponse response = services.getSalesPrograms();
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getSalesPrograms(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetSalesProgramsResponse getSalesProgramsResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getSalesProgramsResponse == null) {
            getSalesProgramsResponse = new GetSalesProgramsResponse();
        }

        super.handleSuccess(getSalesProgramsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetSalesProgramsResponse GetSalesProgramsResponse = new GetSalesProgramsResponse();
        super.handleFailure(GetSalesProgramsResponse, retrofitError);
    }
}
