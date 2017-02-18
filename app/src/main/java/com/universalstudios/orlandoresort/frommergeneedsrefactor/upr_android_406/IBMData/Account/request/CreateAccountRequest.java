package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Contact;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreference;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateAccountResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/10/16.
 * Edited by Tyler Ritchie on 11/16/16.
 * Class: CreateAccountRequest
 * Class Description: Request to create a user's account
 */
public class CreateAccountRequest extends IBMOrlandoServicesRequest implements Callback<CreateAccountResponse> {
    public static final String TAG = "CreateAccountRequest";
    public static final String CREATE_ACCOUNT_RECAPTCHA_RESPONSE = "bc1f106a-adb7-45e8-b314-0a61febd5555";

    public CreateAccountRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType,
                                CreateAccountRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link CreateAccountRequest.Builder}.
     */
    private static class CreateAccountRequestParams extends NetworkParams {
        private CreateAccountRequestBodyParams createAccountRequestBodyParams;

        public CreateAccountRequestParams() {
            super();
            createAccountRequestBodyParams = new CreateAccountRequestBodyParams();
        }
    }

    /**
     * Public request body class used by the {@link CreateAccountRequest.Builder}
     * and {@link IBMOrlandoServices}.
     */
    public static class CreateAccountRequestBodyParams extends GsonObject {

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("g-recaptcha-response")
        public String reCaptchaResponse;

        @SerializedName("password")
        public String password;

        @SerializedName("guestProfile")
        public GuestProfile guestProfile;

        public CreateAccountRequestBodyParams() {
            super();
            reCaptchaResponse = CREATE_ACCOUNT_RECAPTCHA_RESPONSE;
            guestProfile = new GuestProfile();
            guestProfile.setSourceId(SourceId.ACCOUNT_CREATION);
            guestProfile.setContact(new Contact());
            guestProfile.setPreference(new GuestPreference());
            externalGuestId = AccountStateManager.getGuestId();
        }
    }

    /**
     * Public builder class for creating the {@link CreateAccountRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private CreateAccountRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new CreateAccountRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setPassword(String password) {
            this.requestParams.createAccountRequestBodyParams.password = password;
            return getThis();
        }

        public Builder setBirthDate(String birthDate) {
            this.requestParams.createAccountRequestBodyParams.guestProfile.setBirthDate(birthDate);
            return getThis();
        }

        public Builder setEmail(String email) {
            this.requestParams.createAccountRequestBodyParams.guestProfile.setEmail(email);
            return getThis();
        }

        public Builder setFirstName(String firstName) {
            this.requestParams.createAccountRequestBodyParams.guestProfile.getContact().setFirstname(firstName);
            return getThis();
        }

        public Builder setLastName(String lastName) {
            this.requestParams.createAccountRequestBodyParams.guestProfile.getContact().setLastname(lastName);
            return getThis();
        }

        public Builder setReceiveUpdates(boolean receiveUpdates) {
            this.requestParams.createAccountRequestBodyParams.guestProfile.getPreference().setReceiveUpdates(receiveUpdates);
            return getThis();
        }

        public Builder setTermsOfService(boolean termsOfService) {
            this.requestParams.createAccountRequestBodyParams.guestProfile.getPreference().setTermsAndServices(termsOfService);
            return getThis();
        }

        public CreateAccountRequest build() {
            return new CreateAccountRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        CreateAccountRequestParams params = (CreateAccountRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try{
                    CreateAccountResponse response =
                            services.createAccount(params.createAccountRequestBodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.createAccount(params.createAccountRequestBodyParams, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(CreateAccountResponse createAccountResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (createAccountResponse == null) {
            createAccountResponse = new CreateAccountResponse();
        }
        super.handleSuccess(createAccountResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        // TODO Handle failure using a central class
        CreateAccountResponse createAccountResponse = new CreateAccountResponse();
        super.handleFailure(createAccountResponse, retrofitError);
    }
}
