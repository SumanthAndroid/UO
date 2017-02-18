package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ValidateEmailAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to check the validity of an email address.
 * <p/>
 * Created by Jack Hughes on 9/19/16.
 */
public class ValidateEmailAddressRequest extends IBMOrlandoServicesRequest implements Callback<ValidateEmailAddressResponse> {

    // Logging Tag
    private static final String TAG = ValidateEmailAddressRequest.class.getSimpleName();

    protected ValidateEmailAddressRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class ValidateEmailAddressParams extends NetworkParams {
        private ValidateEmailAddressBodyParams validateEmailAddressBodyParams;

        public ValidateEmailAddressParams() {
            super();
            validateEmailAddressBodyParams = new ValidateEmailAddressBodyParams();
        }
    }

    /**
     * Body parameters class used by the {@link ValidateEmailAddressRequest}.
     */
    public static class ValidateEmailAddressBodyParams extends GsonObject {

        @SerializedName("marketingId")
        private String marketingId;

        @SerializedName("email")
        private String email;

    }

    /**
     * A Builder class for building a new {@link ValidateEmailAddressRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final ValidateEmailAddressParams validateEmailAddressParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.validateEmailAddressParams = new ValidateEmailAddressParams();
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
         * [REQUIRED] Sets the email to validate in the request.
         *
         * @param email
         *         the String containing the email to validate
         *
         * @return the {@link Builder}
         */
        public Builder setEmail(String email) {
            this.validateEmailAddressParams.validateEmailAddressBodyParams.email = email;
            return getThis();
        }

        /**
         * [REQUIRED] Sets the marketingId of the email to validate in the request.
         *
         * @param marketingId
         *         the String containing the marketingId of the email to validate
         *
         * @return the {@link Builder}
         */
        public Builder setMarketingId(String marketingId) {
            this.validateEmailAddressParams.validateEmailAddressBodyParams.marketingId = marketingId;
            return getThis();
        }

        public ValidateEmailAddressRequest build() {
            return new ValidateEmailAddressRequest(senderTag, priority, concurrencyType, validateEmailAddressParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        ValidateEmailAddressParams params = (ValidateEmailAddressParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    ValidateEmailAddressResponse response = services.validateEmailAddress(
                            params.validateEmailAddressBodyParams);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.validateEmailAddress(
                        params.validateEmailAddressBodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(ValidateEmailAddressResponse validateEmailAddressResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(validateEmailAddressResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new ValidateEmailAddressResponse(), error);
    }
}
