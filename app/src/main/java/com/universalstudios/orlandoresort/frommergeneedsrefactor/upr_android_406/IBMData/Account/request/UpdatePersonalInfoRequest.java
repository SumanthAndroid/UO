package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdatePersonalInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import org.parceler.Transient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/25/16.
 * Class: UpdatePersonalInfoRequest
 * Class Description: Request to update user's personal info
 */
public class UpdatePersonalInfoRequest extends IBMOrlandoServicesRequest implements Callback<UpdatePersonalInfoResponse> {
    // Logging Tag
    private static final String TAG = UpdatePersonalInfoRequest.class.getSimpleName();

    private UpdatePersonalInfoRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link UpdatePersonalInfoRequest.Builder}.
     */
    private static class UpdatePersonalInfoRequestParams extends NetworkParams {
        private UpdatePersonalInfoRequestBody requestBody;

        public UpdatePersonalInfoRequestParams() {
            super();
            requestBody = new UpdatePersonalInfoRequestBody();

        }
    }

    public static class UpdatePersonalInfoRequestBody extends GsonObject {

        @SerializedName("guestProfile")
        private UpdatePersonalInfoGuestProfile guestProfile;

        public UpdatePersonalInfoRequestBody() {
            super();
            guestProfile = new UpdatePersonalInfoGuestProfile();
        }

    }


    private static class UpdatePersonalInfoGuestProfile extends GsonObject {

        @Transient
        private boolean addressesPurged;

        @SerializedName("birthDate")
        private String birthDate;

        @SerializedName("contact")
        private UpdatePersonalInfoGuestProfileContact contact;

        @SerializedName("preferences")
        private UpdatePersonalInfoGuestProfilePreferences preference;

        @SerializedName("userId")
        private String userId;

        @SerializedName("sourceId")
        private @SourceId.SourceIdType String sourceId;

        public UpdatePersonalInfoGuestProfile() {
            super();
            contact = new UpdatePersonalInfoGuestProfileContact();
            preference = new UpdatePersonalInfoGuestProfilePreferences();
            sourceId = SourceId.EDIT_PERSONAL_INFO;
            userId = AccountStateManager.getUsername();
        }
    }

    private static class UpdatePersonalInfoGuestProfileContact extends GsonObject {

        @SerializedName("namePrefix")
        private String namePrefix;

        @SerializedName("nameSuffix")
        private String nameSuffix;

        @SerializedName("firstName")
        private String firstname;

        @SerializedName("lastName")
        private String lastname;

        @SerializedName("mobilePhone")
        private String mobilePhone;

        @SerializedName("emailAddress")
        private String email;

        @SerializedName("sourceId")
        private @SourceId.SourceIdType String sourceId;

        public UpdatePersonalInfoGuestProfileContact() {
            super();
            sourceId = SourceId.EDIT_PERSONAL_INFO;
        }
    }

    private static class UpdatePersonalInfoGuestProfilePreferences extends GsonObject {

        @SerializedName("receiveUpdates")
        private boolean receiveUpdates;

        @SerializedName("termsOfServices")
        private boolean termsAndServices;

    }

    public static class Builder extends BaseNetworkRequestBuilder<UpdatePersonalInfoRequest.Builder> {
        private final UpdatePersonalInfoRequestParams updatePersonalInfoRequestParams;

        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
            this.updatePersonalInfoRequestParams = new UpdatePersonalInfoRequestParams();
        }

        /**
         * Method required by {@link BaseNetworkRequestBuilder} to allow the proper builder pattern
         * with child classes.
         *
         * @return the {@link UpdatePersonalInfoRequest.Builder}
         */
        @Override
        protected Builder getThis() {
            return this;
        }

        public UpdatePersonalInfoRequest.Builder setBirthDate(String birthDate) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.birthDate = birthDate;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setNamePrefix(String namePrefix) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.contact.namePrefix = namePrefix;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setFirstname(String firstname) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.contact.firstname = firstname;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setLastname(String lastname) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.contact.lastname = lastname;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setNameSuffix(String nameSuffix) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.contact.nameSuffix = nameSuffix;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setEmail(String email) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.contact.email = email;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setPhone(String phone) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.contact.mobilePhone = phone;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setReceiveUpdates(boolean receiveUpdates) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.preference.receiveUpdates = receiveUpdates;
            return getThis();
        }

        public UpdatePersonalInfoRequest.Builder setTermsOfServices(boolean termsOfServices) {
            this.updatePersonalInfoRequestParams.requestBody.guestProfile.preference.termsAndServices = termsOfServices;
            return getThis();
        }

        public UpdatePersonalInfoRequest build() {
            return new UpdatePersonalInfoRequest(senderTag, priority, concurrencyType, updatePersonalInfoRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);
        // Set the params when the request is sent
        UpdatePersonalInfoRequest.UpdatePersonalInfoRequestParams params = (UpdatePersonalInfoRequest.UpdatePersonalInfoRequestParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String guestId = AccountStateManager.getGuestId();
        String authString = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    UpdatePersonalInfoResponse response = services.updatePersonalInfo(authString, guestId, params.requestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.updatePersonalInfo(authString, guestId, params.requestBody, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(UpdatePersonalInfoResponse updatePersonalInfoResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        UpdatePersonalInfoResponse upiResponse = updatePersonalInfoResponse;
        if (null == upiResponse) {
            upiResponse = new UpdatePersonalInfoResponse();
        }
        super.handleSuccess(upiResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        UpdatePersonalInfoResponse response = new UpdatePersonalInfoResponse();
        super.handleFailure(response, retrofitError);
    }
}
