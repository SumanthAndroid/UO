package com.universalstudios.orlandoresort.model.network.domain.offers;

import android.content.Context;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for retrieving offer coupons.
 *
 * @author acampbell
 * @author tritchie
 */
public class GetOfferCouponRequest extends UniversalOrlandoServicesRequest implements Callback<GetOfferCouponResponse> {

    private static final String TAG = GetOfferCouponRequest.class.getSimpleName();

    private GetOfferCouponRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType,
            NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class GetOfferCouponParams extends NetworkParams {

        private long offerId;

    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        private final GetOfferCouponParams getOfferCouponParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getOfferCouponParams = new GetOfferCouponParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setOfferId(long offerId) {
            this.getOfferCouponParams.offerId = offerId;
            return getThis();
        }

        public GetOfferCouponRequest build() {
            return new GetOfferCouponRequest(senderTag, priority, concurrencyType, getOfferCouponParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        GetOfferCouponParams params = (GetOfferCouponParams) getNetworkParams();
        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);
        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetOfferCouponResponse response = services.getOfferCoupon(params.offerId);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getOfferCoupon(params.offerId, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetOfferCouponResponse getResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getResponse == null) {
            getResponse = new GetOfferCouponResponse();
        }
        super.handleSuccess(getResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetOfferCouponResponse(), retrofitError);
    }

}
