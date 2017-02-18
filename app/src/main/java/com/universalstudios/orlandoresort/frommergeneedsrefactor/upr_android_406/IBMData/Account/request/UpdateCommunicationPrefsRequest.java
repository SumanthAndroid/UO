package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.EmailFrequencyType;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateCommunicationPrefsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrofit request to update a user's communication preferences.
 *
 * Created by Tyler Ritchie on 11/15/16.
 */
public class UpdateCommunicationPrefsRequest extends IBMOrlandoServicesRequest implements Callback<UpdateCommunicationPrefsResponse> {

    // Logging Tag
    private static final String TAG = UpdateCommunicationPrefsRequest.class.getSimpleName();

    public UpdateCommunicationPrefsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class UpdateCommunicationPrefsRequestParams extends NetworkParams {
        private UpdateCommunicationPrefsRequestBodyParams updateCommunicationPrefsRequestBody;

        public UpdateCommunicationPrefsRequestParams() {
            super();
            updateCommunicationPrefsRequestBody = new UpdateCommunicationPrefsRequestBodyParams();
        }
    }

    public static class UpdateCommunicationPrefsRequestBodyParams extends GsonObject {
        @SerializedName("sourceId")
        private @SourceId.SourceIdType String sourceId;

        @SerializedName("emailFrequency")
        private String emailFrequency;

        @SerializedName("mobilePhone")
        private String mobilePhone;

        @SerializedName("contactPermissions")
        private UpdateCommunicationPrefsRequestContactPermissionsParams contactPermissions;

        @SerializedName("communicationInterests")
        private Map<String, String> interests;

        @SerializedName("typicalVacationSeasons")
        private Map<String, Boolean> vacationSeasonsResponse;


        public UpdateCommunicationPrefsRequestBodyParams() {
            super();
            sourceId = SourceId.EDIT_COMMUNICATIONS;
            contactPermissions = new UpdateCommunicationPrefsRequestContactPermissionsParams();
        }
    }

    private static class UpdateCommunicationPrefsRequestContactPermissionsParams extends GsonObject {
        @SerializedName("Direct Mail")
        private boolean directMail;

        @SerializedName("Email Permission")
        private boolean emailPermission;

        @SerializedName("Targeted Social Advertising")
        private boolean targetedSocialAdvertising;

        @SerializedName("Text Message")
        private boolean textMessage;

        public UpdateCommunicationPrefsRequestContactPermissionsParams() {
            super();
        }
    }

    /**
     * A Builder class for building a new {@link UpdateCommunicationPrefsRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final UpdateCommunicationPrefsRequestParams updateCommunicationPrefsRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.updateCommunicationPrefsRequestParams = new UpdateCommunicationPrefsRequestParams();
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
         * [REQUIRED] Sets the guest's email frequency type (such as "Weekly") for updating
         * communication preferences.
         *
         * @param emailFrequency
         *         the {@link EmailFrequencyType} of the guest's email frequency
         *
         * @return the {@link Builder}
         */
        public Builder setEmailFrequency(@EmailFrequencyType.StringValue String emailFrequency) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .emailFrequency = emailFrequency;
            return getThis();
        }

        /**
         * [REQUIRED] Sets the guest's mobile phone for updating communication preferences.
         *
         * @param mobilePhone
         *         the String of the guest's mobile phone
         *
         * @return the {@link Builder}
         */
        public Builder setMobilePhone(String mobilePhone) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .mobilePhone = mobilePhone;
            return getThis();
        }

        /**
         * [REQUIRED] Sets whether the guest should receive direct mail.
         *
         * @param directMail
         *         the boolean for whether the guest should receive direct mail
         *
         * @return the {@link Builder}
         */
        public Builder setDirectMail(boolean directMail) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .contactPermissions.directMail = directMail;
            return getThis();
        }

        /**
         * [REQUIRED] Sets whether the guest should receive emails.
         *
         * @param emailPermission
         *         the boolean for whether the guest should receive emails
         *
         * @return the {@link Builder}
         */
        public Builder setEmailPermission(boolean emailPermission) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .contactPermissions.emailPermission = emailPermission;
            return getThis();
        }

        /**
         * [REQUIRED] Sets whether the guest should receive targeted social ads.
         *
         * @param targetedSocialAdvertising
         *         the boolean for whether the guest should receive targeted social ads.
         *
         * @return the {@link Builder}
         */
        public Builder setTargetedSocialAdvertising(boolean targetedSocialAdvertising) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .contactPermissions.targetedSocialAdvertising = targetedSocialAdvertising;
            return getThis();
        }

        /**
         * [REQUIRED] Sets whether the guest should receive text messages.
         *
         * @param textMessage
         *         the boolean for whether the guest should receive text messages
         *
         * @return the {@link Builder}
         */
        public Builder setTextMessage(boolean textMessage) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .contactPermissions.textMessage = textMessage;
            return getThis();
        }

        public Builder setInterests(Map<String, String> interests) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .interests = interests;
            return getThis();
        }

        public Builder setVacationSeasons(Map<String, Boolean> vacationSeasonsResponse) {
            this.updateCommunicationPrefsRequestParams.updateCommunicationPrefsRequestBody
                    .vacationSeasonsResponse = vacationSeasonsResponse;
            return getThis();
        }

        public UpdateCommunicationPrefsRequest build() {
            return new UpdateCommunicationPrefsRequest(senderTag, priority, concurrencyType, updateCommunicationPrefsRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        UpdateCommunicationPrefsRequestParams params = (UpdateCommunicationPrefsRequestParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String guestId = AccountStateManager.getGuestId();
        String authString = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    UpdateCommunicationPrefsResponse response = services.updateCommunicationPrefs(
                            authString,
                            guestId,
                            params.updateCommunicationPrefsRequestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.updateCommunicationPrefs(
                        authString,
                        guestId,
                        params.updateCommunicationPrefsRequestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(UpdateCommunicationPrefsResponse updateCommunicationPrefsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(updateCommunicationPrefsResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new UpdateCommunicationPrefsResponse(), error);
    }
}