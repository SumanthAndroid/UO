package com.universalstudios.orlandoresort.model.network.domain.wallet;

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

public class GetWalletFolioRequest extends IBMOrlandoServicesRequest implements Callback<GetWalletFolioResponse> {

    public GetWalletFolioRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<GetWalletFolioRequest.Builder> {
        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetWalletFolioRequest build() {
            return new GetWalletFolioRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        String guestId = AccountStateManager.getGuestId();
        String basicAuth = AccountStateManager.getBasicAuthString();
        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetWalletFolioResponse response = services.getWalletFolio(basicAuth, guestId);
                    success(response, null);
                } catch(RetrofitError error) {
                    failure(error);
                }
                break;
            case ASYNCHRONOUS:
                services.getWalletFolio(basicAuth, guestId, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void success(GetWalletFolioResponse getWalletFolioResponse, Response response) {
        super.handleSuccess(getWalletFolioResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        super.handleFailure(new GetWalletFolioResponse(), error);
    }
}
