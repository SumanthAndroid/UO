package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jamesblack on 5/25/16.
 * Edited by Tyler Ritchie 11/14/16.
 */

public class AddBillingAddressRequest extends IBMOrlandoServicesRequest implements Callback<AddBillingAddressResponse> {

    private static final String TAG = AddBillingAddressRequest.class.getSimpleName();
    private static final String NOT_APPLICABLE = "n/a";

    private AddBillingAddressRequest(String senderTag,
                                     Priority priority, ConcurrencyType concurrencyType, AddBillingAddressRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link AddBillingAddressRequest.Builder}.
     */
    private static class AddBillingAddressRequestParams extends NetworkParams {
        private AddBillingAddressBody addBillingAddressBody;

        public AddBillingAddressRequestParams() {
            super();
            addBillingAddressBody = new AddBillingAddressRequest.AddBillingAddressBody();
        }
    }

    public static class AddBillingAddressBody extends GsonObject {
        @SerializedName("contact")
        private List<Contact> contacts;

        public AddBillingAddressBody() {
            super();
            contacts = new ArrayList<>();
        }

        public void addContact(Contact contact) {
            contacts.add(contact);
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private AddBillingAddressRequestParams addBillingAddressRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.addBillingAddressRequestParams = new AddBillingAddressRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder addContact(Contact contact) {
            addBillingAddressRequestParams.addBillingAddressBody.addContact(contact);
            return getThis();
        }

        public AddBillingAddressRequest build() {
            return new AddBillingAddressRequest(senderTag, priority, concurrencyType, addBillingAddressRequestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        AddBillingAddressRequestParams networkParams = (AddBillingAddressRequestParams) getNetworkParams();
        AddBillingAddressBody requestBody = networkParams.addBillingAddressBody;

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        for(Contact contact : requestBody.contacts) {
            if(contact.getState() == null || contact.getState().length() == 0) {
                contact.setState(NOT_APPLICABLE);
            }
            if(contact.getZipCode() == null || contact.getZipCode().length() == 0) {
                contact.setZipCode(NOT_APPLICABLE);
            } else {
                contact.setZipCode(contact.getZipCode().replace("-", ""));
            }
        }

        if(BuildConfig.DEBUG) {
            Log.d(TAG, requestBody.toJson());
        }

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try{
                    AddBillingAddressResponse response = services.addBillingAddress(
                            wcToken,
                            wcTrustedToken,
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            requestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.addBillingAddress(
                        wcToken,
                        wcTrustedToken,
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(AddBillingAddressResponse addBillingAddressResponse, Response response) {
        super.handleSuccess(addBillingAddressResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        // TODO Handle failure using a central class
        AddBillingAddressResponse addBillingAddressResponse = new AddBillingAddressResponse();
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "Error after sending request");
        }
        super.handleFailure(addBillingAddressResponse, retrofitError);
    }

}
