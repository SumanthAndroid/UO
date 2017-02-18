package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfile;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfileResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/17/16.
 * Edited by Tyler Ritchie on 8/10/16.
 *
 * Class: GetGuestProfileRequest
 * Class Description: Request For Getting the User's Profile
 */
public class GetGuestProfileRequest extends IBMOrlandoServicesRequest implements Callback<GetGuestProfileResponse> {
    public static final String TAG = GetGuestProfileRequest.class.getSimpleName();

    public GetGuestProfileRequest(String senderTag,
                                  Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        public Builder(NetworkRequestSender sender) {
            super(sender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetGuestProfileRequest build() {
            return new GetGuestProfileRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        String userId = AccountStateManager.getUsername();
        String authString = AccountStateManager.getBasicAuthString();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetGuestProfileResponse response =
                            services.getGuestProfile(authString, userId);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getGuestProfile(authString, userId, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetGuestProfileResponse getGuestProfileResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getGuestProfileResponse != null) {
            GuestProfileResult guestProfileResult = getGuestProfileResponse.getResult();
            if (guestProfileResult != null) {
                GuestProfile guestProfile = guestProfileResult.getGuestProfile();
                if (guestProfile != null) {
                    guestProfile.setSourceId(SourceId.LEGACY_SOURCE_ID);
                    AccountStateManager.setGuest(guestProfile);
                }
            }
        } else {
            getGuestProfileResponse = new GetGuestProfileResponse();
        }
        super.handleSuccess(getGuestProfileResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        GetGuestProfileResponse response = new GetGuestProfileResponse();
        super.handleFailure(response, retrofitError);
    }
}
