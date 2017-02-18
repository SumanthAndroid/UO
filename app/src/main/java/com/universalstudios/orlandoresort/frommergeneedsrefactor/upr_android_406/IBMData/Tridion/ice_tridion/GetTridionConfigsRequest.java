package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/24/16.
 * Class: GetTridionConfigsRequest
 * Class Description: Request for getting tridion configs
 */
public class GetTridionConfigsRequest extends IBMOrlandoServicesRequest implements Callback<GetTridionConfigsResponse> {
    public static final String TAG = GetTridionConfigsRequest.class.getSimpleName();

    public GetTridionConfigsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder(NetworkRequestSender sender) {
            super(sender);
        }

        public GetTridionConfigsRequest build() {
            return new GetTridionConfigsRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetTridionConfigsResponse response = services.getTridionConfigs();
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getTridionConfigs(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetTridionConfigsResponse getTridionConfigsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        TridionConfigWrapper tridionConfigWrapper = TridionConfigStateManager.getInstance();
        if (getTridionConfigsResponse != null && getTridionConfigsResponse.getResult() != null) {
            tridionConfigWrapper.setTridionConfig(getTridionConfigsResponse.getResult());
            // Write Tridion JSON to manager
            TridionConfigStateManager.saveInstance();
        } else if (getTridionConfigsResponse == null) {
            getTridionConfigsResponse = new GetTridionConfigsResponse();
        }

        super.handleSuccess(getTridionConfigsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }

        super.handleFailure(new GetTridionConfigsResponse(), retrofitError);
    }
}
