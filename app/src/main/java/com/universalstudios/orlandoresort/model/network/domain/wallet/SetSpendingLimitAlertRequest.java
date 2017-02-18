package com.universalstudios.orlandoresort.model.network.domain.wallet;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
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
 * Request for setting spending limit alert preferences.
 *
 * @author acampbell
 */
public class SetSpendingLimitAlertRequest extends IBMOrlandoServicesRequest implements Callback<SetSpendingLimitAlertResponse> {
    private static final String TAG = SetSpendingLimitAlertRequest.class.getSimpleName();

    private SetSpendingLimitAlertRequest(String senderTag, Priority priority,
                                         ConcurrencyType concurrencyType, SetSpendingLimitAlertRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link SetSpendingLimitAlertRequest.Builder}.
     */
    private static class SetSpendingLimitAlertRequestParams extends NetworkParams {

        private SetSpendingLimitAlertRequestBodyParams bodyParams;

        public SetSpendingLimitAlertRequestParams() {
            super();
            bodyParams = new SetSpendingLimitAlertRequestBodyParams();
        }

    }

    public static class SetSpendingLimitAlertRequestBodyParams extends GsonObject {

        @SerializedName("email")
        private boolean email;

        @SerializedName("text")
        private boolean text;

        @SerializedName("sourceId")
        private String sourceId;

        public SetSpendingLimitAlertRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_SPENDING_LIMIT;
        }
    }

    /**
     * Builder for setting parameter fields and generating the {@link SetSpendingLimitAlertRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private SetSpendingLimitAlertRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new SetSpendingLimitAlertRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }


        public Builder setEmail(boolean email) {
            requestParams.bodyParams.email = email;
            return this;
        }

        public Builder setText(boolean text) {
            requestParams.bodyParams.text = text;
            return this;
        }

        public SetSpendingLimitAlertRequest build() {
            return new SetSpendingLimitAlertRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        SetSpendingLimitAlertRequestParams requestParams = (SetSpendingLimitAlertRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();
        String guestId = AccountStateManager.getGuestId();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    SetSpendingLimitAlertResponse response = services.setSpendingLimitAlert(
                            guestId,
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.setSpendingLimitAlert(
                        guestId,
                        basicAuth,
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(SetSpendingLimitAlertResponse setSpendingLimitAlertResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (setSpendingLimitAlertResponse == null) {
            setSpendingLimitAlertResponse = new SetSpendingLimitAlertResponse();
        }
        super.handleSuccess(setSpendingLimitAlertResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new SetSpendingLimitAlertResponse(), retrofitError);
    }
}
