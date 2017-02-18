package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest;
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
 *
 * Created by Tyler Ritchie on 10/13/16.
 */
public class GetPersonalizationExtrasRequest extends IBMOrlandoServicesRequest implements Callback<GetPersonalizationExtrasResponse> {

    // Logging Tag
    private static final String TAG = GetPersonalizationExtrasRequest.class.getSimpleName();
    private static final String IP_VALUE = "ip_AddOnsProducts_01";
    private static final String IC_VALUE = "IC_UO_MOBILE_APP";
    private static final String PERSONA_VALUE = "All";

    private GetPersonalizationExtrasRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetPersonalizationExtrasParams extends NetworkParams {
        private GetPersonalizationExtrasRequestBodyParams requestBody;

        public GetPersonalizationExtrasParams() {
            super();
            requestBody = new GetPersonalizationExtrasRequestBodyParams();
        }
    }

    public static class GetPersonalizationExtrasRequestBodyParams extends GsonObject {

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("subcategoryid")
        private String subcategoryId;

        @SerializedName("adobeTags")
        private List<String> adobeTags;

        @SerializedName("ip")
        private String ip;

        @SerializedName("ic")
        private String ic;

        @SerializedName("numberRequested")
        private int numberRequested;

        @SerializedName("geoLocation")
        private String geoLocation;

        @SerializedName("persona")
        private String persona;

        @SerializedName("ticketsPageNumberOfDays")
        private String ticketsPageNumberOfDays;

        @SerializedName("ticketsPageNumberOfAdults")
        private String ticketsPageNumberOfAdults;

        @SerializedName("ticketsPageNumberOfChildren")
        private String ticketsPageNumberOfChildren;

        @SerializedName("ticketsPageFloridaResidentFlag")
        private String ticketsPageFloridaResidentFlag;

        @SerializedName("ticketsPageSelectedTicketDate")
        private String ticketsPageSelectedTicketDate;

        @SerializedName("attractionExperience")
        private String attractionExperience;

        @SerializedName("attractionLocation")
        private String attractionLocation;

        @SerializedName("attractionInterest")
        private String attractionInterest;

        @SerializedName("mapLocation")
        private String mapLocation;

        @SerializedName("pageBrowsed")
        private String pageBrowsed;

        @SerializedName("firstTimeVisitor")
        private String firstTimeVisitor;

        public GetPersonalizationExtrasRequestBodyParams() {
            super();
            ip = IP_VALUE;
            ic = IC_VALUE;
            geoLocation = AccountUtils.getGeoLocationType();
            // TODO: story to set this value
            adobeTags = new ArrayList<>();
            // TODO: No info on these fields, but required
            persona = PERSONA_VALUE;
            attractionExperience = "";
            attractionLocation = "";
            attractionInterest = "";
            mapLocation = "";
            pageBrowsed = "";
            firstTimeVisitor = "";
            ticketsPageNumberOfDays = "";
            ticketsPageNumberOfAdults = "";
            ticketsPageNumberOfChildren = "";
            ticketsPageFloridaResidentFlag = "";
            ticketsPageSelectedTicketDate = "";
        }
    }

    /**
     * A Builder class for building a new {@link GetPersonalizationExtrasRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetPersonalizationExtrasParams getPersonalizationExtrasParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getPersonalizationExtrasParams = new GetPersonalizationExtrasParams();
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

        public Builder setExternalGuestId(String externalGuestId) {
            this.getPersonalizationExtrasParams.requestBody.externalGuestId = externalGuestId;
            return getThis();
        }

        public Builder setNumberRequested(int numberRequested) {
            this.getPersonalizationExtrasParams.requestBody.numberRequested = numberRequested;
            return getThis();
        }

        public Builder setSubcategoryId(String subcategoryId) {
            this.getPersonalizationExtrasParams.requestBody.subcategoryId = subcategoryId;
            return getThis();
        }

        public Builder setGeolocation(@GetPersonalizationOffersRequest.GeoLocationType String geolocation){
            this.getPersonalizationExtrasParams.requestBody.geoLocation = geolocation;
            return getThis();
        }

        public GetPersonalizationExtrasRequest build() {
            return new GetPersonalizationExtrasRequest(senderTag, priority, concurrencyType, getPersonalizationExtrasParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetPersonalizationExtrasParams params = (GetPersonalizationExtrasParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetPersonalizationExtrasResponse response = services.getPersonalizationExtras(params.requestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getPersonalizationExtras(params.requestBody, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetPersonalizationExtrasResponse getPersonalizationExtrasResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getPersonalizationExtrasResponse == null) {
            getPersonalizationExtrasResponse = new GetPersonalizationExtrasResponse();
        }
        super.handleSuccess(getPersonalizationExtrasResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetPersonalizationExtrasResponse(), error);
    }
}