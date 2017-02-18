package com.universalstudios.orlandoresort.model.network.domain.wallet;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Nicholas Hanna on 1/13/2017.
 */

public class GetWalletCardsRequest extends IBMOrlandoServicesRequest implements Callback<GetWalletCardsResponse> {
    private static final String TAG = GetWalletCardsRequest.class.getSimpleName();

    public GetWalletCardsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<GetWalletCardsRequest.Builder> {
        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetWalletCardsRequest build() {
            return new GetWalletCardsRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        String basicAuth = AccountStateManager.getBasicAuthString();
        String guestId = AccountStateManager.getGuestId();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetWalletCardsResponse response = services.getWalletCards(basicAuth, guestId);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getWalletCards(basicAuth, guestId, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }


    // Callback interface methods
    @Override
    public void success(GetWalletCardsResponse getWalletCardsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getWalletCardsResponse == null) {
            getWalletCardsResponse = new GetWalletCardsResponse();
        }
        super.handleSuccess(getWalletCardsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetWalletCardsResponse(), retrofitError);
    }
}
