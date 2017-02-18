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
 * @author tjudkins
 * @since 1/27/17
 */

public class GetFolioTransactionsRequest extends IBMOrlandoServicesRequest implements Callback<GetFolioTransactionsResponse> {
    private static final String TAG = GetFolioTransactionsRequest.class.getSimpleName();

    private GetFolioTransactionsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<GetFolioTransactionsRequest.Builder> {
        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetFolioTransactionsRequest build() {
            return new GetFolioTransactionsRequest(senderTag, priority, concurrencyType);
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
                    GetFolioTransactionsResponse response = services.getFolioTransactions(basicAuth, guestId);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getFolioTransactions(basicAuth, guestId, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }


    // Callback interface methods
    @Override
    public void success(GetFolioTransactionsResponse getFolioTransactionsResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (null == getFolioTransactionsResponse) {
            getFolioTransactionsResponse = new GetFolioTransactionsResponse();
        }
        super.handleSuccess(getFolioTransactionsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetFolioTransactionsResponse response = new GetFolioTransactionsResponse();
        super.handleFailure(response, retrofitError);

    }
}
