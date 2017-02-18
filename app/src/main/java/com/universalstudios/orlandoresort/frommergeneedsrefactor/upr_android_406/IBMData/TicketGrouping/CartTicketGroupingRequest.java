package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets.TicketAssignmentUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jamesblack on 6/8/16.
 * Edited by Tyler Ritchie on 11/4/16.
 *
 * This class calls to get all the tickets in the cart grouped so the logic is stored in one place.
 * So child/adult for related skus are grouped along with the quantity and other order information.
 *
 */
public class CartTicketGroupingRequest extends IBMOrlandoServicesRequest implements Callback<TicketGroupingResponse> {

    private static final String TAG = CartTicketGroupingRequest.class.getSimpleName();
    private static final String GROUP_METHOD_ICE_V2 = "ice_v2";
    private static final String CACHE_CONTROL = "no-cache";
    private static final String CONTRACT_ID_VALUE = "4000000000000000003";
    private static final String CONTRACT_ID_KEY = "contractId";
    private static final String GROUP_ID_KEY = "groupId";
    private static final String IP_VALUE = "ip_ShoppingCart_01";
    private static final String IC_VALUE = "IC_UO_MOBILE_APP";
    private static final String PERSONA_VALUE = "All";


    private CartTicketGroupingRequest(String senderTag, Priority priority,
                                      ConcurrencyType concurrencyType, CartTicketGroupingRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    private static class CartTicketGroupingRequestParams extends NetworkParams {
        private CartTicketGroupingRequestBodyParams cartTicketGroupingRequestBodyParams;

        public CartTicketGroupingRequestParams() {
            super();
            cartTicketGroupingRequestBodyParams = new CartTicketGroupingRequestBodyParams();
        }
    }

    public static class CartTicketGroupingRequestBodyParams extends GsonObject {
        @SerializedName("adobeTags")
        private List<String> adobeTags;

        @SerializedName("geoLocation")
        private String geoLocation;

        @SerializedName("externalGuestId")
        private String externalGuestId;

        @SerializedName("persona")
        private String persona;

        @SerializedName("ip")
        private String ip;

        @SerializedName("ic")
        private String ic;

        @SerializedName("attractionExperience")
        private String attractionExperience;

        @SerializedName("attractionInterest")
        private String attractionInterest;

        @SerializedName("attractionLocation")
        private String attractionLocation;

        @SerializedName("mapLocation")
        private String mapLocation;

        @SerializedName("pageBrowsed")
        private String pageBrowsed;

        @SerializedName("firstTimeVisitor")
        private String firstTimeVisitor;

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

        CartTicketGroupingRequestBodyParams() {
            super();
            externalGuestId = AccountStateManager.getGuestId();
            persona = PERSONA_VALUE;
            ip = IP_VALUE;
            ic = IC_VALUE;
            // TODO: There is a story to select these values
            geoLocation = "OUS";
            adobeTags = new ArrayList<>();
            // TODO: No info on these fields, but required
            attractionExperience = "";
            attractionInterest = "";
            attractionLocation = "";
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

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private CartTicketGroupingRequestParams cartTicketGroupingRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            cartTicketGroupingRequestParams = new CartTicketGroupingRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public CartTicketGroupingRequest build() {
            return new CartTicketGroupingRequest(senderTag, priority, concurrencyType, cartTicketGroupingRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);


        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(CONTRACT_ID_KEY, CONTRACT_ID_VALUE);
        queryMap.put(GROUP_ID_KEY, GROUP_METHOD_ICE_V2);

        CartTicketGroupingRequestParams networkParams = (CartTicketGroupingRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    TicketGroupingResponse response = services.getTicketingGroupsInCart(
                            CACHE_CONTROL,
                            wcToken,
                            wcTrustedToken,
                            queryMap,
                            networkParams.cartTicketGroupingRequestBodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getTicketingGroupsInCart(
                        CACHE_CONTROL,
                        wcToken,
                        wcTrustedToken,
                        queryMap,
                        networkParams.cartTicketGroupingRequestBodyParams,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(TicketGroupingResponse ticketGroupingResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (ticketGroupingResponse == null) {
            ticketGroupingResponse = new TicketGroupingResponse();
        }
        TicketAssignmentUtils.instance().setTicketGrouping(ticketGroupingResponse.getOrder());

        super.handleSuccess(ticketGroupingResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        TicketGroupingResponse ticketGroupingResponse = new TicketGroupingResponse();
        super.handleFailure(ticketGroupingResponse, retrofitError);
    }
}
