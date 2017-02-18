package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.StateProvincesState;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by IBM_ADMIN on 2/22/2016.
 * Edited by Tyler Ritchie on 11/29/16.
 */
public class GetStateRequest extends IBMOrlandoServicesRequest implements Callback<GetStateResponse> {

    private static final String TAG = GetStateRequest.class.getSimpleName();

    private GetStateRequest(String senderTag,
            Priority priority, ConcurrencyType concurrencyType) {
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

        public GetStateRequest build() {
            return new GetStateRequest(senderTag, priority, concurrencyType);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetStateResponse response = services.getStates();
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getStates(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetStateResponse getResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        GetStateResponse sResponse = getResponse;
        if (sResponse == null) {
            sResponse = new GetStateResponse();
        } else if (sResponse.getStates() != null && !sResponse.getStates().isEmpty()) {
            StateProvincesState stateProvincesState = CountryStateProvinceStateManager
                    .getStateProvincesInstance();
            stateProvincesState.getStateProvinces().clear();
            stateProvincesState.getStateProvinces().addAll(sResponse.getStates());
            CountryStateProvinceStateManager.saveStateProvincesInstance();
        }
        super.handleSuccess(sResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetStateResponse getStateResponse = new GetStateResponse();
        super.handleFailure(getStateResponse, retrofitError);
    }
}
