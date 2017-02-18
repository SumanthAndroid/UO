package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Update the shipping address ID on all cart items
 * @author tjudkins
 * @author Tyler Ritchie
 * @since 11/29/2016
 */
public class CartShippingInfoRequest extends IBMOrlandoServicesRequest implements Callback<CartShippingInfoResponse> {
    private static final String TAG = CartShippingInfoRequest.class.getSimpleName();

    private CartShippingInfoRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType, CartShippingInfoRequestParams requestParams) {
        super(senderTag, priority, concurrencyType, requestParams);
    }

    private static class CartShippingInfoRequestParams extends NetworkParams {
        private CartShippingInfoRequestBodyParams bodyParams;

        public CartShippingInfoRequestParams() {
            bodyParams = new CartShippingInfoRequestBodyParams();
        }
    }

    public static class CartShippingInfoRequestBodyParams extends GsonObject {
        @SerializedName("orderItem")
        private List<CartShippingRequestOrderItem> orderItems;

        public CartShippingInfoRequestBodyParams() {
            orderItems = new ArrayList<>();
        }

        public void addOrderItem(CartShippingRequestOrderItem.Builder builder) {
            orderItems.add(builder.build());
        }
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private CartShippingInfoRequestParams requestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.requestParams = new CartShippingInfoRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setAddressId(TicketGroupOrder order, String addressId) {
            if (null != order && null != order.getOrderItemGroups()) {
                for (ParkTicketGroups ticket : order.getOrderItemGroups().getParkTicketGroups()) {
                    if (null != ticket && null != ticket.getAdultTickets()) {
                        for (OrderItem orderItem : ticket.getAdultTickets().getOrderItems()) {

                            CartShippingRequestOrderItem.Builder builder = new CartShippingRequestOrderItem.Builder()
                                    .setAddressId(addressId)
                                    .setOrderItemId(orderItem.getOrderItemId());
                            requestParams.bodyParams.addOrderItem(builder);
                        }
                    }
                    if (null != ticket && null != ticket.getChildTickets()) {
                        for (OrderItem orderItem : ticket.getChildTickets().getOrderItems()) {

                            CartShippingRequestOrderItem.Builder builder = new CartShippingRequestOrderItem.Builder()
                                    .setAddressId(addressId)
                                    .setOrderItemId(orderItem.getOrderItemId());
                            requestParams.bodyParams.addOrderItem(builder);
                        }
                    }
                }
                for (ExpressPassTicketGroups expressPassTicketGroups : order.getOrderItemGroups().getExpressPassGroups()) {
                    if (null != expressPassTicketGroups) {
                        for (OrderItem orderItem : expressPassTicketGroups.getOrderItems()) {

                            CartShippingRequestOrderItem.Builder builder = new CartShippingRequestOrderItem.Builder()
                                    .setAddressId(addressId)
                                    .setOrderItemId(orderItem.getOrderItemId());
                            requestParams.bodyParams.addOrderItem(builder);
                        }
                    }
                }
            }

            return getThis();
        }

        public CartShippingInfoRequest build() {
            return new CartShippingInfoRequest(senderTag, priority, concurrencyType, requestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        CartShippingInfoRequestParams requestParams = (CartShippingInfoRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    CartShippingInfoResponse response = services.updateCartShippingInfo(
                            wcToken,
                            wcTrustedToken,
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            requestParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                    services.updateCartShippingInfo(
                            wcToken,
                            wcTrustedToken,
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            requestParams.bodyParams,
                            this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(CartShippingInfoResponse getResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (getResponse == null) {
            getResponse = new CartShippingInfoResponse();
        }
        super.handleSuccess(getResponse, response);
    }


    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new CartShippingInfoResponse(), retrofitError);
    }

}

