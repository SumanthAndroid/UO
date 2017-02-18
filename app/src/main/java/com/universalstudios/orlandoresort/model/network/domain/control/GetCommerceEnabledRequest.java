package com.universalstudios.orlandoresort.model.network.domain.control;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.state.commerce.CommerceStateManager;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request to get remote control properties for commerce. These can enable/disable commerce related
 * features.
 */
public class GetCommerceEnabledRequest extends IBMOrlandoServicesRequest implements Callback<GetCommerceEnabledResponse> {
    private static final String TAG = GetCommerceEnabledRequest.class.getSimpleName();

    private GetCommerceEnabledRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetCommerceEnabledParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class GetCommerceEnabledParams extends NetworkParams {
        private String versionName;

        public GetCommerceEnabledParams() {
            super();
            versionName = BuildConfigUtils.getRawVersionName();
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private GetCommerceEnabledParams params;

        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
            params = new GetCommerceEnabledParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetCommerceEnabledRequest build() {
            return new GetCommerceEnabledRequest(senderTag, priority, concurrencyType, params);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        GetCommerceEnabledParams requestParams = (GetCommerceEnabledParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetCommerceEnabledResponse response =
                            services.getCommerceEnabled(requestParams.versionName);
                    success(response, null);
                } catch (RetrofitError error) {
                    failure(error);
                }
                break;
            case ASYNCHRONOUS:
                services.getCommerceEnabled(requestParams.versionName, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetCommerceEnabledResponse networkResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }

        if (networkResponse != null) {
            // Copy over the commerce enabled properties
            CommerceEnabledProperties properties = networkResponse.getCommerceEnabledProperties();
            CommerceStateManager.setAppValidForCommerce(properties.getAppValidForCommerce());

            // Save this as the last successful sync time
            CommerceStateManager.setDateOfLastCommerceEnabledSyncInMillis(new Date().getTime());
        } else {
            networkResponse = new GetCommerceEnabledResponse();
        }
        super.handleSuccess(networkResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetCommerceEnabledResponse(), retrofitError);
    }
}