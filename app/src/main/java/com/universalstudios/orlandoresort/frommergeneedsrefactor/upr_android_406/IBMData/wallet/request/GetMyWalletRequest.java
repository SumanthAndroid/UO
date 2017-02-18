package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.request;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.GetMyWalletResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.UniversalOrlandoCommerceServices;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to get "My Wallet" by external guest Id.
 * <p/>
 * Created by Jack Hughes on 9/27/16.
 */
public class GetMyWalletRequest extends IBMOrlandoServicesRequest implements Callback<GetMyWalletResponse> {

    // Logging Tag
    private static final String TAG = GetMyWalletRequest.class.getSimpleName();

    protected GetMyWalletRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetMyWalletParams extends NetworkParams {
        private String userName;
        private String userPassword;

        public GetMyWalletParams() {
            super();
        }
    }

    /**
     * A Builder class for building a new {@link GetMyWalletRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetMyWalletParams getMyWalletParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getMyWalletParams = new GetMyWalletParams();
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

        /**
         * [REQUIRED] Sets the guest's external Id for getting their wallet.
         *
         * @param userName
         *         the String of the guest's email/password
         *
         * @return the {@link Builder}
         */
        public Builder setUserName(String userName) {
            this.getMyWalletParams.userName = userName;
            return getThis();
        }

        /**
         * [REQUIRED] Sets the guest's external Id for getting their wallet.
         *
         * @param password
         *         the String of the guest's password
         *
         * @return the {@link Builder}
         */
        public Builder setUserPassword(String password) {
            this.getMyWalletParams.userPassword = password;
            return getThis();
        }

        public GetMyWalletRequest build() {
            return new GetMyWalletRequest(senderTag, priority, concurrencyType, getMyWalletParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetMyWalletParams params = (GetMyWalletParams) getNetworkParams();

        UniversalOrlandoCommerceServices services = restAdapter.create(UniversalOrlandoCommerceServices.class);
        String basicAuth = NetworkUtils.createBasicAuthString(params.userName, params.userPassword);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetMyWalletResponse response = services.getMyWallet(
                            basicAuth);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getMyWallet(
                        basicAuth,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetMyWalletResponse getMyWalletResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(getMyWalletResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetMyWalletResponse(), error);
    }
}