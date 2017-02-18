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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to add or update new addresses (one or more) to a guest's profile by guest Id.
 * Note - this request will add any {@link Address} in the array list if it does not contain the
 * {@link Address#addressId} attribute. If it contains the address Id, it will instead update that
 * address in the guest's profile. This request will take 1 or more Address objects in the array. If
 * a single address is to be added and not updated, simply use the {@link AddGuestAddressRequest}
 * instead. Also note that the return object is the same for this request for both adding and
 * updating, as well as being the same used in the {@link AddGuestAddressRequest} class.
 * <p/>
 * Created by Jack Hughes on 10/3/16.
 */
public class AddUpdateGuestAddressesRequest extends IBMOrlandoServicesRequest implements Callback<AddUpdateGuestAddressesResponse> {

    // Logging Tag
    private static final String TAG = AddUpdateGuestAddressesRequest.class.getSimpleName();

    protected AddUpdateGuestAddressesRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class AddGuestAddressParams extends NetworkParams {
        private List<Address> guestAddressesRequestBody;

        public AddGuestAddressParams() {
            super();
            guestAddressesRequestBody = new ArrayList<>();
        }
    }

    /**
     * A Builder class for building a new {@link AddUpdateGuestAddressesRequest}.
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
         * [REQUIRED] Sets the new addresses to add/update for the guest.
         *
         * @param addresses
         *         the {@link List<Address>} of addresses too add to the guest's profile
         *
         * @return the {@link Builder}
         */
        public Builder setAddresses(List<Address> addresses) {
            this.addGuestAddressParams.guestAddressesRequestBody = addresses;
            return getThis();
        }

        public AddUpdateGuestAddressesRequest build() {
            return new AddUpdateGuestAddressesRequest(senderTag, priority, concurrencyType, addGuestAddressParams);
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
                    AddUpdateGuestAddressesResponse response = services.addUpdateGuestAddresses(
                            authString,
                            guestId,
                            params.guestAddressesRequestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.addUpdateGuestAddresses(
                        authString,
                        guestId,
                        params.guestAddressesRequestBody,
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