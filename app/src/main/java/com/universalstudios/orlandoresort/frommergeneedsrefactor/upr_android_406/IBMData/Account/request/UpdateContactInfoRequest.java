package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateContactInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/25/16.
 * Edited by Tyler Ritchie on 12/2/16.
 * Class: UpdateContactInfoRequest
 * Class Description: Request to update user's contact info
 */
public class UpdateContactInfoRequest extends IBMOrlandoServicesRequest implements Callback<UpdateContactInfoResponse> {
    private static final String TAG = UpdateContactInfoRequest.class.getSimpleName();

    public UpdateContactInfoRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType,
            UpdateContactInfoRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    private static class UpdateContactInfoRequestParams extends NetworkParams{

        private UpdateContactInfoRequestBodyParams bodyParams;

        public UpdateContactInfoRequestParams() {
            super();
            bodyParams = new UpdateContactInfoRequestBodyParams();
        }
    }

    public static class UpdateContactInfoRequestBodyParams extends GsonObject {

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

        public UpdateContactInfoRequestBodyParams() {
            super();
            sourceId = SourceId.EDIT_CONTACT_INFO;
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private UpdateContactInfoRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new UpdateContactInfoRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setEmail(String email) {
            requestParams.bodyParams.email = email;
            return getThis();
        }

        public Builder setFirstname(String firstname) {
            requestParams.bodyParams.firstname = firstname;
            return getThis();
        }

        public Builder setLastname(String lastname) {
            requestParams.bodyParams.lastname = lastname;
            return getThis();
        }

        public Builder setMobilePhone(String mobilePhone) {
            if (mobilePhone != null) {
                mobilePhone = mobilePhone.trim().replaceAll("[^\\d]", "");
            }
            requestParams.bodyParams.mobilePhone = mobilePhone;
            return getThis();
        }

        public Builder setNamePrefix(String namePrefix) {
            requestParams.bodyParams.namePrefix = namePrefix;
            return getThis();
        }

        public Builder setNameSuffix(String nameSuffix) {
            requestParams.bodyParams.nameSuffix = nameSuffix;
            return getThis();
        }

        public Builder setSourceId(@SourceId.SourceIdType String sourceId) {
            requestParams.bodyParams.sourceId = sourceId;
            return getThis();
        }

        public UpdateContactInfoRequest build() {
            return new UpdateContactInfoRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        UpdateContactInfoRequestParams requestParams = (UpdateContactInfoRequestParams) getNetworkParams();
        String guestId = AccountStateManager.getGuestId();
        String authString = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    UpdateContactInfoResponse response = services.updateContactInfo(
                            authString,
                            guestId,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.updateContactInfo(
                        authString,
                        guestId,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(UpdateContactInfoResponse updateContactInfoResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }

        if (updateContactInfoResponse == null) {
            updateContactInfoResponse = new UpdateContactInfoResponse();
        }
        super.handleSuccess(updateContactInfoResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }

        UpdateContactInfoResponse response = new UpdateContactInfoResponse();

        // TODO This block is handled when an ErrorResponse is used
        if (null != retrofitError && null != retrofitError.getResponse()) {
            response.setStatusCode(retrofitError.getResponse().getStatus());
        } else {
            response.setStatusCode(HttpResponseStatus.ERROR_INTERNAL_SERVER_ERROR);
        }
        super.handleFailure(response, retrofitError);
    }
}
