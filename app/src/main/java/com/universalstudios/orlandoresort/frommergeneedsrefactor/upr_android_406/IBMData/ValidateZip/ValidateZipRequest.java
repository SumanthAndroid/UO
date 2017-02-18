package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ValidateZip;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.ErrorHandling.FLValidateZipDao;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Steven Byle
 */
public class ValidateZipRequest extends IBMOrlandoServicesRequest implements Callback<ValidateZipResponse> {
    private static final String TAG = ValidateZipRequest.class.getSimpleName();

    private ValidateZipRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, ValidateZipRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    private static class ValidateZipRequestParams extends NetworkParams {
        private String zip;
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private ValidateZipRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new ValidateZipRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setZip(String zip) {
            this.requestParams.zip = zip;
            return getThis();
        }


        public ValidateZipRequest build() {
            return new ValidateZipRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);


        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        ValidateZipRequestParams requestParams = (ValidateZipRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    ValidateZipResponse response = services.validateFLZIP(requestParams.zip);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.validateFLZIP(requestParams.zip, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(ValidateZipResponse getResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        FLValidateZipDao flValid = new FLValidateZipDao();
        if (getResponse != null) {
            flValid.setValid(getResponse.getResult().getStatus());
            flValid.setError(null);
        }
        super.handleSuccess(flValid, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        FLValidateZipDao flValid = new FLValidateZipDao();
        flValid.setValid(null);
        flValid.setError(retrofitError);
        super.handleFailure(flValid, retrofitError);
    }

}
