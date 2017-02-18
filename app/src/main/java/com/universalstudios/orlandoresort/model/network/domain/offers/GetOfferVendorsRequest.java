package com.universalstudios.orlandoresort.model.network.domain.offers;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author acampbell
 *
 */
public class GetOfferVendorsRequest extends UniversalOrlandoServicesRequest implements
        Callback<GetOfferVendorsResponse> {

    private static final String TAG = GetOfferVendorsRequest.class.getSimpleName();

    protected GetOfferVendorsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
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

        public GetOfferVendorsRequest build() {
            return new GetOfferVendorsRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetOfferVendorsResponse response = services.getOfferVendors(ServiceEndpointUtils.getCity());
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getOfferVendors(ServiceEndpointUtils.getCity(), this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        super.handleFailure(new GetOfferVendorsResponse(), retrofitError);
    }

    @Override
    public void success(GetOfferVendorsResponse getOfferVendorsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "success");
        }

        if (getOfferVendorsResponse != null) {
            // Loop through vendor as create request for each to sync their offers
            if (getOfferVendorsResponse.getVendors() != null) {
                for (String vendor : getOfferVendorsResponse.getVendors()) {
                    GetVendorOffersRequest getVendorOffersRequest = new GetVendorOffersRequest.Builder(null)
                            .setVendor(vendor)
                            .setConcurrencyType(ConcurrencyType.SYNCHRONOUS).build();
                    NetworkUtils.queueNetworkRequest(getVendorOffersRequest);
                }
                NetworkUtils.startNetworkService();
            }

            // Store the last time offers were synced
            UniversalAppState universalAppState = UniversalAppStateManager
                    .getInstance();
            universalAppState.setDateOfLastOffersSyncInMillis(new Date().getTime());
            UniversalAppStateManager.saveInstance();
        } else {
            getOfferVendorsResponse = new GetOfferVendorsResponse();
        }

        // Inform any listeners after saving the response
        super.handleSuccess(getOfferVendorsResponse, response);
    }

}
