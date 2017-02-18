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

import java.math.BigInteger;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for setting primary card.
 *
 * @author Tyler Ritchie
 */
public class SetSpendingLimitRequest extends IBMOrlandoServicesRequest implements Callback<SetSpendingLimitResponse> {
    private static final String TAG = SetSpendingLimitRequest.class.getSimpleName();

    private SetSpendingLimitRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, SetSpendingLimitRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link SetSpendingLimitRequest.Builder}.
     */
    private static class SetSpendingLimitRequestParams extends NetworkParams {

        private SetSpendingLimitRequestBodyParams bodyParams;

        public SetSpendingLimitRequestParams() {
            super();
            bodyParams = new SetSpendingLimitRequestBodyParams();
        }

    }

    public static class SetSpendingLimitRequestBodyParams extends GsonObject {

        @SerializedName("sequenceId")
        private String sequenceId;

        @SerializedName("spendingLimit")
        private BigInteger spendingLimit;

        @SerializedName("isUnlimited")
        private boolean isUnlimited;

        @SerializedName("sourceId")
        private String sourceId;

        public SetSpendingLimitRequestBodyParams() {
            super();
            sourceId = SourceId.MODIFY_SPENDING_LIMIT;
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link SetSpendingLimitRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private SetSpendingLimitRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new SetSpendingLimitRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setSequenceId(String sequenceId) {
            this.requestParams.bodyParams.sequenceId = sequenceId;
            return getThis();
        }

        public Builder setSpendingLimit(BigInteger spendingLimit) {
            this.requestParams.bodyParams.spendingLimit = spendingLimit;
            return getThis();
        }

        public Builder setIsUnlimited(boolean isUnlimited) {
            this.requestParams.bodyParams.isUnlimited = isUnlimited;
            return getThis();
        }

        public SetSpendingLimitRequest build() {
            return new SetSpendingLimitRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        SetSpendingLimitRequestParams requestParams = (SetSpendingLimitRequestParams) getNetworkParams();
        String basicAuth = AccountStateManager.getBasicAuthString();
        String guestId = AccountStateManager.getGuestId();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    SetSpendingLimitResponse response = services.setSpendingLimit(
                            guestId,
                            basicAuth,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.setSpendingLimit(
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
    public void success(SetSpendingLimitResponse setSpendingLimitResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (setSpendingLimitResponse == null) {
            setSpendingLimitResponse = new SetSpendingLimitResponse();
        }
        super.handleSuccess(setSpendingLimitResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new SetSpendingLimitResponse(), retrofitError);
    }
}
