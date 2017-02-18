package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetCardsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Edited by Tyler Ritchie on 11/7/16.
 *
 * Class: GetCardsRequest
 * Class Description: Request for getting the cards to display
 */
public class GetCardsRequest extends IBMOrlandoServicesRequest implements Callback<GetCardsResponse> {
    public static final String TAG = GetCardsRequest.class.getSimpleName();
    public static final String DEFAULT_TICKETS_PATH = "Tickets_SC_Web"; // TODO: switch back to "Tickets_SC" when services are ready
    public static final String DEFAULT_UEP_PATH = "Express_Passes_SC_Web"; // TODO: switch back to "Express_Passes_SC" when services are ready
    public static final String DEFAULT_TICKET_BMG_PATH = "Tickets_BMG_Kit_SC_Web";
    public static final String DEFAULT_TICKET_UEP_PATH = "Tickets_UEP_Kit_SC_Web";
    public static final String ONE_DAY_VISIT_STR_IDENTIFIER = "1";
    public static final String UEP_IDENTIFIER = "EXPRESS";
    private static final String IP_VALUE = "ip_Tickets_01";
    private static final String IC_VALUE = "IC_UO_MOBILE_APP";
    private static final String PERSONA_VALUE = "All";
    private static final String VALUE_FLORIDA_RESIDENT_TRUE = "Y";
    private static final String VALUE_FLORIDA_RESIDENT_FALSE = "N";
    private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

    public GetCardsRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, GetCardsRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link GetCardsRequest.Builder}.
     */
    private static class GetCardsRequestParams extends NetworkParams {
        private GetCardsBodyParams getCardsBodyParams;
        public GetCardsRequestParams() {
            super();
            getCardsBodyParams = new GetCardsRequest.GetCardsBodyParams();
        }
    }

    public static class GetCardsBodyParams extends GsonObject {
        @SerializedName("cards")
        private String cards;

        @SerializedName("skipInventory")
        private String skipInventory;

        @SerializedName("catalogId")
        private String catalogId;

        @SerializedName("dates")
        private List<String> dates;

        @SerializedName("seasons")
        private List<String> seasons;

        @SerializedName("geoLocation")
        private String geoLocation;

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("adobeTags")
        private List<String> adobeTags;

        @SerializedName("persona")
        private String persona;

        @SerializedName("ip")
        private String ip;

        @SerializedName("ic")
        private String ic;

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

        public GetCardsBodyParams() {
            super();
            catalogId = BuildConfig.COMMERCE_SALES_CATALOG_ID;
            externalGuestId = AccountStateManager.getGuestId();
            persona = PERSONA_VALUE;
            ip = IP_VALUE;
            ic = IC_VALUE;
            // TODO: There is a story to select these values
            skipInventory = "false";
            geoLocation = "OUS";
            adobeTags = new ArrayList<>();
            // TODO: No info on these fields, not currently used
            attractionExperience = "";
            attractionLocation = "";
            attractionInterest = "";
            mapLocation = "";
            pageBrowsed = "";
            firstTimeVisitor = "";
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private GetCardsRequestParams getCardsRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.getCardsRequestParams = new GetCardsRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setCards(String cards) {
            getCardsRequestParams.getCardsBodyParams.cards = cards;
            return getThis();
        }

        public Builder setDates(Date... dates) {
            getCardsRequestParams.getCardsBodyParams.dates = new ArrayList<>();
            // Date format: 2016-09-19 00:00:01
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:01", Locale.US);
            for (Date date : dates) {
                getCardsRequestParams.getCardsBodyParams.dates.add(sdf.format(date));
            }
            return getThis();
        }

        public Builder setSeasons(List<String> seasons) {
            getCardsRequestParams.getCardsBodyParams.seasons = seasons;
            return getThis();
        }

        public Builder setGeolocation(@GetPersonalizationOffersRequest.GeoLocationType String geolocation) {
            getCardsRequestParams.getCardsBodyParams.geoLocation = geolocation;
            return getThis();
        }

        public Builder setTicketsPageNumberOfDays(int ticketsPageNumberOfDays) {
            if (ticketsPageNumberOfDays > 0) {
                getCardsRequestParams.getCardsBodyParams.ticketsPageNumberOfDays =
                        String.valueOf(ticketsPageNumberOfDays);
            } else {
                getCardsRequestParams.getCardsBodyParams.ticketsPageNumberOfDays = "";
            }
            return getThis();
        }

        public Builder setTicketsPageNumberOfAdults(int ticketsPageNumberOfAdults) {
            if (ticketsPageNumberOfAdults > 0) {
                getCardsRequestParams.getCardsBodyParams.ticketsPageNumberOfAdults =
                        String.valueOf(ticketsPageNumberOfAdults);
            } else {
                getCardsRequestParams.getCardsBodyParams.ticketsPageNumberOfAdults = "";
            }
            return getThis();
        }

        public Builder setTicketsPageNumberOfChildren(int ticketsPageNumberOfChildren) {
            if (ticketsPageNumberOfChildren > 0) {
                getCardsRequestParams.getCardsBodyParams.ticketsPageNumberOfChildren =
                        String.valueOf(ticketsPageNumberOfChildren);
            } else {
                getCardsRequestParams.getCardsBodyParams.ticketsPageNumberOfChildren = "";
            }
            return getThis();
        }

        public Builder setTicketsPageFloridaResidentFlag(boolean ticketsPageFloridaResidentFlag) {
            getCardsRequestParams.getCardsBodyParams.ticketsPageFloridaResidentFlag =
                    ticketsPageFloridaResidentFlag ? VALUE_FLORIDA_RESIDENT_TRUE : VALUE_FLORIDA_RESIDENT_FALSE;
            return getThis();
        }

        public Builder setTicketsPageSelectedTicketDate(Date ticketsPageSelectedTicketDate) {
            if (ticketsPageSelectedTicketDate != null) {
                Date date = DateUtils.truncate(ticketsPageSelectedTicketDate, Calendar.DATE);
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
                getCardsRequestParams.getCardsBodyParams.ticketsPageSelectedTicketDate =
                        dateFormat.format(date);
            }
            return getThis();
        }

        public Builder setSkipInventory(String skipInventory) {
            this.getCardsRequestParams.getCardsBodyParams.skipInventory = skipInventory;
            return getThis();
        }

        public GetCardsRequest build() {
            return new GetCardsRequest(senderTag, priority, concurrencyType, getCardsRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        GetCardsRequestParams networkParams = (GetCardsRequestParams) getNetworkParams();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetCardsResponse response =
                            services.getCommerceCards(
                                    networkParams.getCardsBodyParams);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;

            case ASYNCHRONOUS:
                services.getCommerceCards(
                        networkParams.getCardsBodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetCardsResponse getCardsResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }

        if (getCardsResponse == null) {
            getCardsResponse = new GetCardsResponse();
        }
        super.handleSuccess(getCardsResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }

        GetCardsResponse response = new GetCardsResponse();
        super.handleFailure(response, retrofitError);
    }
}
