package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers;

import android.content.Context;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Item;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.OrderItemGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author tjudkins
 * @author Tyler Ritchie
 * @since 10/19/16
 */
public class GetPersonalizationOffersRequest extends IBMOrlandoServicesRequest implements Callback<GetPersonalizationOffersResponse> {

    // Logging Tag
    private static final String TAG = GetPersonalizationOffersRequest.class.getSimpleName();
    private static final String IP_VALUE = "ip_OrderConfirmation_01";
    private static final String IC_VALUE = "IC_UO_MOBILE_APP";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            GEOLOCATION_TYPE_FLORIDA,
            GEOLOCATION_TYPE_OUTER_US,
            GEOLOCATION_TYPE_INTERNATIONAL,
            GEOLOCATION_TYPE_IN_PARK,
            GEOLOCATION_TYPE_NEAR_PARK
    })
    public @interface GeoLocationType {}

    public static final String GEOLOCATION_TYPE_FLORIDA = "FL";
    public static final String GEOLOCATION_TYPE_OUTER_US = "OUS";
    public static final String GEOLOCATION_TYPE_INTERNATIONAL = "INT";
    public static final String GEOLOCATION_TYPE_IN_PARK = "INP";
    public static final String GEOLOCATION_TYPE_NEAR_PARK = "NEAR";


    private GetPersonalizationOffersRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, NetworkParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link Builder}.
     */
    private static class GetPersonalizationOffersParams extends NetworkParams {
        private GetPersonalizationOffersRequestBody requestBody;

        public GetPersonalizationOffersParams() {
            super();
            requestBody = new GetPersonalizationOffersRequestBody();
        }
    }

    public static class GetPersonalizationOffersRequestBody extends GsonObject {

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("sessionId")
        private String sessionId;

        @SerializedName("adobeTags")
        private List<String> adobeTags;

        @SerializedName("ip")
        private String ip;

        @SerializedName("ic")
        private String ic;

        @SerializedName("numberRequested")
        private int numberRequested;

        @SerializedName("partNumbers")
        private List<PersonalizationOffersPartNumber> partNumbers;

        @SerializedName("geoLocation")
        private String geoLocation;

        public GetPersonalizationOffersRequestBody() {
            super();
            partNumbers = new ArrayList<>();
            ip = IP_VALUE;
            ic = IC_VALUE;
        }

        public void addPersonalizationOffersPartNumber(PersonalizationOffersPartNumber.Builder builder) {
            partNumbers.add(builder.build());
        }
    }

    /**
     * A Builder class for building a new {@link GetPersonalizationOffersRequest}.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private final GetPersonalizationOffersParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new GetPersonalizationOffersParams();
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

        public Builder setAdobeTags(List<String> adobeTags) {
            this.requestParams.requestBody.adobeTags = adobeTags;
            return getThis();
        }

        public Builder setExternalGuestId(String externalGuestId) {
            this.requestParams.requestBody.externalGuestId = externalGuestId;
            // DE12097 - SessionId is the same as externalGuestId (for now)
            this.requestParams.requestBody.sessionId = externalGuestId;
            return getThis();
        }

        public Builder setNumberRequested(int numberRequested) {
            this.requestParams.requestBody.numberRequested = numberRequested;
            return getThis();
        }

        public Builder setPartNumbers(TicketGroupOrder ticketGroupOrder) {
            List<Item> items = new ArrayList<>();
            if (null != ticketGroupOrder && null != ticketGroupOrder.getOrderItemGroups()) {
                OrderItemGroups orderItemGroups = ticketGroupOrder.getOrderItemGroups();
                if (null != orderItemGroups.getParkTicketGroups()) {
                    for (ParkTicketGroups p : orderItemGroups.getParkTicketGroups()) {
                        if (null != p) {
                            if (null != p.getAdultTickets() && p.getAdultTickets().getQuantity() > 0
                                    && null != p.getAdultTickets().getItem()) {
                                items.add(p.getAdultTickets().getItem());
                            }
                            if (null != p.getChildTickets() && p.getChildTickets().getQuantity() > 0
                                    && null != p.getChildTickets().getItem()) {
                                items.add(p.getChildTickets().getItem());
                            }
                        }
                    }
                }
                if (null != orderItemGroups.getExpressPassGroups()) {
                    for (ExpressPassTicketGroups e : orderItemGroups.getExpressPassGroups()) {
                        if (null != e && e.getQuantity() > 0 && null != e.getItem()) {
                            items.add(e.getItem());
                        }
                    }
                }
                if (null != orderItemGroups.getAddOnTicketGroups()) {
                    for (AddOnTicketGroups a : orderItemGroups.getAddOnTicketGroups()) {
                        if (null != a) {
                            if (null != a.getAdultAddOns() && a.getAdultAddOns().getQuantity() > 0
                                    && null != a.getAdultAddOns().getItem()) {
                                items.add(a.getAdultAddOns().getItem());
                            }
                            if (null != a.getChildAddOns() && a.getChildAddOns().getQuantity() > 0
                                    && null != a.getChildAddOns().getItem()) {
                                items.add(a.getChildAddOns().getItem());
                            }
                            if (null != a.getAllAddOns() && a.getAllAddOns().getQuantity() > 0
                                    && null != a.getAllAddOns().getItem()) {
                                items.add(a.getAllAddOns().getItem());
                            }
                        }
                    }
                }
            }
            for (Item item : items) {
                PersonalizationOffersPartNumber.Builder builder = new PersonalizationOffersPartNumber.Builder()
                        .setSkuPartNumber(item.getPartNumber())
                        .setProductItemType(item.getItemType())
                        .setProductPartNumber(item.getName());
                requestParams.requestBody.addPersonalizationOffersPartNumber(builder);
            }
            return getThis();
        }

        public Builder setGeoLocation(@GeoLocationType String geoLocation) {
            this.requestParams.requestBody.geoLocation = geoLocation;
            return getThis();
        }

        public GetPersonalizationOffersRequest build() {
            return new GetPersonalizationOffersRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        // Set the params when the request is sent
        GetPersonalizationOffersParams params = (GetPersonalizationOffersParams) getNetworkParams();

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetPersonalizationOffersResponse response = services.getPersonalizationOffers(params.requestBody);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getPersonalizationOffers(params.requestBody, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }

    }

    @Override
    public void success(GetPersonalizationOffersResponse getPersonalizationOffersResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getPersonalizationOffersResponse == null) {
            getPersonalizationOffersResponse = new GetPersonalizationOffersResponse();
        }
        super.handleSuccess(getPersonalizationOffersResponse, response);
    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new GetPersonalizationOffersResponse(), error);
    }
}