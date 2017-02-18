package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.DeleteGuestAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to delete an address in a guest's profile by guest Id and address Id.
 * <p/>
 * @author tjudkins
 * @since 10/23/16
 */
public class DeleteGuestAddressRequest extends IBMOrlandoServicesRequest implements Callback<DeleteGuestAddressResponse> {

    // Logging Tag
    private static final String TAG = DeleteGuestAddressRequest.class.getSimpleName();

    protected DeleteGuestAddressRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class DeleteGuestAddressParams extends NetworkParams {

        private String addressId;
        public DeleteGuestAddressParams() {
            super();
        }

    }
    /**
     * A Builder class for building a new {@link DeleteGuestAddressRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private final DeleteGuestAddressParams deleteGuestAddressParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.deleteGuestAddressParams = new DeleteGuestAddressParams();
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
         * [REQUIRED] Sets the guest's Id for adding a new address.
         *
         * @param addressId
         *         the String of the guest's Id
         *
         * @return the {@link Builder}
         */
        public Builder setAddressId(String addressId) {
            this.deleteGuestAddressParams.addressId = addressId;
            return getThis();
        }

        public DeleteGuestAddressRequest build() {
            return new DeleteGuestAddressRequest(senderTag, priority, concurrencyType, deleteGuestAddressParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        DeleteGuestAddressParams params = (DeleteGuestAddressParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String guestId = AccountStateManager.getGuestId();
        String authString = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    DeleteGuestAddressResponse response = services.deleteGuestAddress(
                            authString,
                            guestId,
                            params.addressId);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.deleteGuestAddress(
                        authString,
                        guestId,
                        params.addressId,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(DeleteGuestAddressResponse deleteGuestAddressResponse, Response response) {
        DeleteGuestAddressResponse dgaResponse = deleteGuestAddressResponse;
        if (null == dgaResponse) dgaResponse = new DeleteGuestAddressResponse();
        super.handleSuccess(deleteGuestAddressResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        super.handleFailure(new DeleteGuestAddressResponse(), error);
    }

}