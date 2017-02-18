package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

/**
 * Created by Tyler Ritchie on 1/13/17.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for checking account and email status.
 *
 * @author Tyler Ritchie
 */
public class LinkEntitlementsRequest extends IBMOrlandoServicesRequest implements Callback<LinkEntitlementsResponse> {
    private static final String TAG = LinkEntitlementsRequest.class.getSimpleName();

    private LinkEntitlementsRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, LinkEntitlementsRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link LinkEntitlementsRequest.Builder}.
     */
    private static class LinkEntitlementsRequestParams extends NetworkParams {
        private LinkEntitlementsRequestBodyParams bodyParams;

        public LinkEntitlementsRequestParams() {
            super();
            bodyParams = new LinkEntitlementsRequestBodyParams();
        }
    }

    public static class LinkEntitlementsRequestBodyParams extends GsonObject {

        public static class Validation extends GsonObject {
            @SerializedName("firstName")
            private String firstName;

            @SerializedName("lastName")
            private String lastName;

            @SerializedName("salesProgramId")
            private String salesProgramId;

        }

        @SerializedName("mediaIds")
        private List<String> mediaIds;

        @SerializedName("sequenceId")
        private String sequendId;

        @SerializedName("validation")
        private Validation validation;

        public @NonNull Validation getValidation() {
            // Lazy load validation so it gets created only when needed
            if (null == validation) {
                validation = new Validation();
            }
            return validation;
        }

    }

    /**
     * Builder for setting parameter fields and generating the {@link LinkEntitlementsRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<LinkEntitlementsRequest.Builder> {
        private LinkEntitlementsRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new LinkEntitlementsRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder addMediaId(String mediaId) {
            if (null == this.requestParams.bodyParams.mediaIds) {
                this.requestParams.bodyParams.mediaIds = new ArrayList<>();
            }
            this.requestParams.bodyParams.mediaIds.add(mediaId);
            return getThis();
        }

        public Builder setSequencedId(String sequencedId) {
            this.requestParams.bodyParams.sequendId = sequencedId;
            return getThis();
        }

        public Builder setFirstName(String firstName) {
            this.requestParams.bodyParams.getValidation().firstName = firstName;
            return getThis();
        }

        public Builder setLastName(String lastName) {
            this.requestParams.bodyParams.getValidation().lastName = lastName;
            return getThis();
        }

        public Builder setSalesProgramId(String salesProgramId) {
            this.requestParams.bodyParams.getValidation().salesProgramId = salesProgramId;
            return getThis();
        }

        public LinkEntitlementsRequest build() {
            return new LinkEntitlementsRequest(senderTag, priority, concurrencyType, requestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        LinkEntitlementsRequestParams requestParams = (LinkEntitlementsRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    LinkEntitlementsResponse response = services.linkEntitlements(
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.linkEntitlements(
                        requestParams.bodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(LinkEntitlementsResponse LinkEntitlementsResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (LinkEntitlementsResponse == null) {
            LinkEntitlementsResponse = new LinkEntitlementsResponse();
        }
        super.handleSuccess(LinkEntitlementsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new LinkEntitlementsResponse(), retrofitError);
    }
}
