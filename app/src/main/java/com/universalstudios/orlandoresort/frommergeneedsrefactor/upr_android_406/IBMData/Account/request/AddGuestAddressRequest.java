package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.AddUpdateGuestAddressesResponse;
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
 * Retrofit request to add a new address to a guest's profile by guest Id. This request is used for
 * adding (and not updating) a single address to a user's profile. If you need to add multiple
 * addresses or update one or more addresses, please use the {@link AddUpdateGuestAddressesRequest}
 * class instead.
 *
 * Created by Jack Hughes on 10/3/16.
 */
public class AddGuestAddressRequest extends IBMOrlandoServicesRequest implements Callback<AddUpdateGuestAddressesResponse> {

    // Logging Tag
    private static final String TAG = AddGuestAddressRequest.class.getSimpleName();

    protected AddGuestAddressRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class AddGuestAddressParams extends NetworkParams {
        private Address guestAddressRequestBody;

        public AddGuestAddressParams() {
            super();
            guestAddressRequestBody = new Address();
        }
    }

    /**
     * A Builder class for building a new {@link AddGuestAddressRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final AddGuestAddressParams addGuestAddressParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.addGuestAddressParams = new AddGuestAddressParams();
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
         * [REQUIRED] Sets the new address to add for the guest.
         *
         * @param address
         *         the {@link Address} too add to the guest's profile
         *
         * @return the {@link Builder}
         */
        public Builder setAddress(Address address) {
            this.addGuestAddressParams.guestAddressRequestBody = address;
            return getThis();
        }

        public AddGuestAddressRequest build() {
            return new AddGuestAddressRequest(senderTag, priority, concurrencyType, addGuestAddressParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        AddGuestAddressParams params = (AddGuestAddressParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String guestId = AccountStateManager.getGuestId();
        String authString = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    AddUpdateGuestAddressesResponse response = services.addGuestAddress(
                            authString,
                            guestId,
                            params.guestAddressRequestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.addGuestAddress(
                        authString,
                        guestId,
                        params.guestAddressRequestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(AddUpdateGuestAddressesResponse addUpdateGuestAddressesResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(addUpdateGuestAddressesResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new AddUpdateGuestAddressesResponse(), error);
    }
}