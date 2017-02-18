package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/3/16.
 * Edited by Tyler Ritchie on 11/29/16.
 * Class: GetTridionSpecsRequest
 * Class Description: TODO: ALWAYS FILL OUT
 */
public class GetTridionSpecsRequest extends IBMOrlandoServicesRequest implements Callback<GetTridionSpecsResponse> {
    public static final String TAG = GetTridionSpecsRequest.class.getSimpleName();

    public GetTridionSpecsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class GetTridionSpecsRequestParams extends NetworkParams {
        private List<String> ids = new ArrayList<>();

        public GetTridionSpecsRequestParams() {
            super();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private GetTridionSpecsRequestParams networkParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            networkParams = new GetTridionSpecsRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        // FIXME Change this to addId(String id)
        public Builder setIds(List<String> ids) {
            this.networkParams.ids = ids;
            return getThis();
        }

        public GetTridionSpecsRequest build() {
            return new GetTridionSpecsRequest(senderTag, priority, concurrencyType, networkParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        GetTridionSpecsRequestParams requestParams = (GetTridionSpecsRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetTridionSpecsResponse response = services.getTridionTicketSpecs(requestParams.ids);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getTridionTicketSpecs(requestParams.ids, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetTridionSpecsResponse getTridionSpecsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getTridionSpecsResponse == null) {
            getTridionSpecsResponse = new GetTridionSpecsResponse();
        }
        else if (getTridionSpecsResponse.getResults() != null) {
            TridionLabelSpecManager.addToMap(getTridionSpecsResponse.getResults());
        }

        super.handleSuccess(getTridionSpecsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetTridionSpecsResponse response = new GetTridionSpecsResponse();
        super.handleFailure(response, retrofitError);
    }
}
