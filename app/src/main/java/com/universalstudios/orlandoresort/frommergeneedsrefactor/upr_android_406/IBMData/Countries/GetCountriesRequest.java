package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountriesState;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.country_state.CountryStateProvinceStateManager;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by IBM_ADMIN on 2/22/2016.
 * Edited by Tyler Ritchie on 11/28/2016.
 */
public class GetCountriesRequest extends IBMOrlandoServicesRequest implements Callback<GetCountriesResponse> {

    private static final String TAG = GetCountriesRequest.class.getSimpleName();

    private GetCountriesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
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

        public GetCountriesRequest build() {
            return new GetCountriesRequest(senderTag, priority, concurrencyType);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetCountriesResponse response = services.getCountries();
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getCountries(this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetCountriesResponse getCountriesResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        GetCountriesResponse cResponse = getCountriesResponse;
        if (cResponse == null) {
            cResponse = new GetCountriesResponse();
        } else if (cResponse.getCountries() != null && !cResponse.getCountries().isEmpty()) {
            CountriesState countriesState = CountryStateProvinceStateManager
                    .getCountriesInstance();
            countriesState.getCountries().clear();
            countriesState.getCountries().addAll(cResponse.getCountries());
            CountryStateProvinceStateManager.saveCountriesInstance();
        }

        super.handleSuccess(cResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetCountriesResponse getCountriesResponse = new GetCountriesResponse();
        super.handleFailure(getCountriesResponse, retrofitError);
    }
}
