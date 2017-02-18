package com.universalstudios.orlandoresort.model.network.domain.offers;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for tracking that a guest accepted an offer
 *
 * @author acampbell
 */
public class OfferAcceptedRequest extends IBMOrlandoServicesRequest implements Callback<OfferAcceptedResponse> {

    private static final String TAG = OfferAcceptedRequest.class.getSimpleName();

    private OfferAcceptedRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class OfferAcceptedRequestParams extends NetworkParams {
        private OfferAcceptedRequestBodyParams requestBody;

        public OfferAcceptedRequestParams() {
            super();
            requestBody = new OfferAcceptedRequestBodyParams();
        }
    }

    public static class OfferAcceptedRequestBodyParams extends GsonObject {
        @SerializedName("sessionId")
        private String sessionId;

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("offers")
        private List<Offer> offers;

        public OfferAcceptedRequestBodyParams() {
            super();
            externalGuestId = AccountStateManager.getGuestId();
            sessionId = AccountStateManager.getGuestId();
            offers = new ArrayList<>();
        }
    }

    private static class Offer extends GsonObject {
        @SerializedName("offerCode")
        private String offerCode;

        @SerializedName("treatmentCode")
        private String treatmentCode;
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private OfferAcceptedRequestParams offerAcceptedRequestParams;

        public Builder(NetworkRequestSender networkRequestSender) {
            super(networkRequestSender);
            offerAcceptedRequestParams = new OfferAcceptedRequestParams();
        }

        public Builder addOffer(String offerCode, String treatmentCode) {
            Offer offer = new Offer();
            offer.offerCode = offerCode;
            offer.treatmentCode = treatmentCode;
            offerAcceptedRequestParams.requestBody.offers.add(offer);
            return  this;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public OfferAcceptedRequest build() {
            return new OfferAcceptedRequest(senderTag, priority, concurrencyType, offerAcceptedRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        OfferAcceptedRequestParams requestParams = (OfferAcceptedRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    OfferAcceptedResponse response = services.getOffersAccepted(
                            requestParams.requestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getOffersAccepted(
                        requestParams.requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(OfferAcceptedResponse offerAcceptedResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (offerAcceptedResponse == null) {
            offerAcceptedResponse = new OfferAcceptedResponse();
        }
        super.handleSuccess(offerAcceptedResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new OfferAcceptedResponse(), retrofitError);
    }
}
