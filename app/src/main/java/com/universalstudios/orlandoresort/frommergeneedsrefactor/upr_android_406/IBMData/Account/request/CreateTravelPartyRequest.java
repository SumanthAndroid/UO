package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateTravelPartyResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetTravelPartyResponse;
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
 * Retrofit request to get travel party.
 */
public class CreateTravelPartyRequest extends IBMOrlandoServicesRequest implements Callback<CreateTravelPartyResponse> {

    // Logging Tag
    private static final String TAG = CreateTravelPartyRequest.class.getSimpleName();

    protected CreateTravelPartyRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class CreateTravelPartyParams extends NetworkParams {
        private CreateTravelPartyBodyParams assignee;

        public CreateTravelPartyParams() {
            super();
            assignee = new CreateTravelPartyBodyParams();
        }
    }

    public static class CreateTravelPartyBodyParams extends GsonObject {
        @SerializedName("sourceId")
        private @SourceId.SourceIdType String sourceId;

        @SerializedName("partyMemberFirstName")
        private String partyMemberFirstName;

        @SerializedName("partyMemberLastName")
        private String partyMemberLastName;

        @SerializedName("partyMemberSuffix")
        private String partyMemberSuffix;

        @SerializedName("guestId")
        private String guestId;

        public CreateTravelPartyBodyParams() {
            super();
            sourceId = SourceId.ASSIGN_ENTITLEMENTS;
            guestId = AccountStateManager.getGuestId();
        }
    }

    /**
     * A Builder class for building a new {@link CreateTravelPartyRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final CreateTravelPartyParams createTravelPartyParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.createTravelPartyParams = new CreateTravelPartyParams();
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
         * [REQUIRED] Sets the guest's party's first name for getting their party.
         *
         * @param partyMemberFirstName the String of the guest's party's first name
         * @return the {@link Builder}
         */
        public Builder setPartyMemberFirstName(String partyMemberFirstName) {
            this.createTravelPartyParams.assignee.partyMemberFirstName = partyMemberFirstName;
            return getThis();
        }

        /**
         * [REQUIRED] Sets the guest's party's last name for getting their party.
         *
         * @param partyMemberLastName the String of the guest's party's last name
         * @return the {@link Builder}
         */
        public Builder setPartyMemberLastName(String partyMemberLastName) {
            this.createTravelPartyParams.assignee.partyMemberLastName = partyMemberLastName;
            return getThis();
        }

        /**
         * [REQUIRED] Sets the guest's party's suffix for getting their party.
         *
         * @param partyMemberSuffix the String of the guest's party's suffix
         * @return the {@link Builder}
         */
        public Builder setPartyMemberSuffix(String partyMemberSuffix) {
            this.createTravelPartyParams.assignee.partyMemberSuffix = partyMemberSuffix;
            return getThis();
        }

        public CreateTravelPartyRequest build() {
            return new CreateTravelPartyRequest(senderTag, priority, concurrencyType, createTravelPartyParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        CreateTravelPartyParams params = (CreateTravelPartyParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String basicAuth = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    CreateTravelPartyResponse response = services.createTravelPartyMember(
                            basicAuth,
                            params.assignee);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.createTravelPartyMember(
                        basicAuth,
                        params.assignee,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(CreateTravelPartyResponse getTravelPartyResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        super.handleSuccess(getTravelPartyResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetTravelPartyResponse(), error);
    }

}