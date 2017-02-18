package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.request;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.response.UpdateItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.DeliveryOption;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Steven Byle
 * @author Tyler Ritchie 11/7/16
 */
public class UpdateItemRequest extends IBMOrlandoServicesRequest implements Callback<UpdateItemResponse> {
    private static final String TAG = UpdateItemRequest.class.getSimpleName();

    private UpdateItemRequest(String senderTag, Priority priority,
            ConcurrencyType concurrencyType, UpdateItemRequestParams networkParams) {
        super(senderTag, priority, concurrencyType, networkParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link UpdateItemRequest.Builder}.
     */
    private static class UpdateItemRequestParams extends NetworkParams {
        private UpdateItemRequestBodyParams bodyParams;
        public UpdateItemRequestParams() {
            super();
            bodyParams = new UpdateItemRequestBodyParams();
        }
    }

    public static class UpdateItemRequestBodyParams extends GsonObject {
        @SerializedName("orderItem")
        private List<UpdateItemRequestOrderItem> updateItemRequestOrderItems;

        public UpdateItemRequestBodyParams() {
            super();
            updateItemRequestOrderItems = new ArrayList<>();
        }

        public UpdateItemRequestOrderItem getOrAddNewOrderItem(String orderItemId) {
            // If there already exists the order item in this list, then return it
            for (UpdateItemRequestOrderItem orderItem : updateItemRequestOrderItems) {
                if (TextUtils.equals(orderItem.orderItemId, orderItemId)) {
                    return orderItem;
                }
            }
            // Create a new order item with the given ID and return it
            UpdateItemRequestOrderItem newOrderItem = new UpdateItemRequestOrderItem();
            newOrderItem.orderItemId = orderItemId;
            updateItemRequestOrderItems.add(newOrderItem);

            return newOrderItem;
        }


    }

    public static class UpdateItemRequestOrderItem extends GsonObject {

        @SerializedName("attributes")
        private List<CommerceAttribute> attributes;

        @SerializedName("orderItemId")
        private String orderItemId;

        @SerializedName("quantity")
        private Integer quantity;

        @SerializedName("deliveryMethod")
        private String deliveryMethod;

        public UpdateItemRequestOrderItem() {
            attributes = new ArrayList<>();
        }

        public void setFirstName(String firstName) {
            CommerceAttribute attribute = getFirstNameAttribute(firstName);
            attributes.add(attribute);
        }

        public void setLastName(String lastName) {
            CommerceAttribute attribute = getLastNameAttribute(lastName);
            attributes.add(attribute);
        }

        public void setSuffix(String suffix) {
            CommerceAttribute attribute = getSuffixAttribute(suffix);
            attributes.add(attribute);
        }

        public void setMemberId(String memberId) {
            CommerceAttribute attribute = getMemberIdAttribute(memberId);
            attributes.add(attribute);
        }

        public void setDob(String dob) {
            CommerceAttribute attribute = getDobAttribute(dob);
            attributes.add(attribute);
        }

        private CommerceAttribute getDobAttribute(String dob) {
            // If there already exists the dob commerce attribute list, then return it
            for (CommerceAttribute commerceAttribute : attributes) {
                if (commerceAttribute.isPartyMemberBirthDate()) {
                    return commerceAttribute;
                }
            }
            // Create a new CommerceAttribute for the dob
            return CommerceAttribute.createPartyMemberBirthdateAttribute(dob);
        }

        private CommerceAttribute getFirstNameAttribute(String firstName) {

            // If there already exists the first name commerce attribute list, then return it
            for (CommerceAttribute commerceAttribute : attributes) {
                if (commerceAttribute.isAssignmentFirstName()) {
                    return commerceAttribute;
                }
            }
            // Create a new CommerceAttribute for the first name
            return CommerceAttribute.createAssignmentFirstNameAttribute(firstName);
        }

        private CommerceAttribute getLastNameAttribute(String lastName) {

            // If there already exists the first name commerce attribute list, then return it
            for (CommerceAttribute commerceAttribute : attributes) {
                if (commerceAttribute.isAssignmentLastName()) {
                    return commerceAttribute;
                }
            }
            // Create a new CommerceAttribute for the first name
            return CommerceAttribute.createAssignmentLastNameAttribute(lastName);
        }

        private CommerceAttribute getSuffixAttribute(String suffix) {

            // If there already exists the first name commerce attribute list, then return it
            for (CommerceAttribute commerceAttribute : attributes) {
                if (commerceAttribute.isAssignmentSuffix()) {
                    return commerceAttribute;
                }
            }
            // Create a new CommerceAttribute for the first name
            return CommerceAttribute.createAssignmentSuffixAttribute(suffix);
        }

        private CommerceAttribute getMemberIdAttribute(String memberId) {

            // If there already exists the first name commerce attribute list, then return it
            for (CommerceAttribute commerceAttribute : attributes) {
                if (commerceAttribute.isAssignmentMemberId()) {
                    return commerceAttribute;
                }
            }
            // Create a new CommerceAttribute for the first name
            return CommerceAttribute.createAssignmentMemberIdAttribute(memberId);
        }
    }


    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private UpdateItemRequestParams updateItemRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.updateItemRequestParams = new UpdateItemRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder addOrderItemToUpdate(String orderItemId, String firstName, String lastName, String memberSuffix, Integer memberId, String dob) {
            UpdateItemRequestOrderItem updateOrderItem = updateItemRequestParams.bodyParams.getOrAddNewOrderItem(orderItemId);
            updateOrderItem.setFirstName(firstName);
            updateOrderItem.setLastName(lastName);
            updateOrderItem.setSuffix(memberSuffix);
            updateOrderItem.setDob(dob);
            if (null != memberId) {
                updateOrderItem.setMemberId(memberId.toString());
            }

            return getThis();
        }

        public Builder addOrderItemsToRemove(List<String> orderItemIds) {
            // Search the existing list of order items and update as necessary
            for (String orderItemId : orderItemIds) {
                UpdateItemRequestOrderItem updateOrderItem = updateItemRequestParams.bodyParams.getOrAddNewOrderItem(orderItemId);
                updateOrderItem.quantity = 0;
            }
            return getThis();
        }

        public Builder setDeliveryOption(TicketGroupOrder ticketGroupOrder, DeliveryOption deliveryOption) {

            if (null != ticketGroupOrder && null != ticketGroupOrder.getOrderItemGroups()) {
                for (ParkTicketGroups parkTicketGroups : ticketGroupOrder.getOrderItemGroups().getParkTicketGroups()) {
                    if (null != parkTicketGroups && null != parkTicketGroups.getAdultTickets()) {
                        for (OrderItem item : parkTicketGroups.getAdultTickets().getOrderItems()) {
                            setDeliveryOption(item.getOrderItemId(), deliveryOption.getId());
                        }
                    }
                    if (null != parkTicketGroups && null != parkTicketGroups.getChildTickets()) {
                        for (OrderItem item : parkTicketGroups.getChildTickets().getOrderItems()) {
                            setDeliveryOption(item.getOrderItemId(), deliveryOption.getId());
                        }
                    }
                }
                for (ExpressPassTicketGroups expressParkTicketGroups : ticketGroupOrder.getOrderItemGroups().getExpressPassGroups()) {
                    for (OrderItem item : expressParkTicketGroups.getOrderItems()) {
                        setDeliveryOption(item.getOrderItemId(), deliveryOption.getId());
                    }
                }
                Map<String, List<AddOnTicketGroups>> addOnsMap = ticketGroupOrder.getOrderItemGroups().getAddOnsMap();
                if (null != addOnsMap) {
                    for (String addOnKey : addOnsMap.keySet()) {
                        for (AddOnTicketGroups addOnTicketGroups : addOnsMap.get(addOnKey)) {
                            if (null != addOnTicketGroups && null != addOnTicketGroups.getAllAddOns()) {
                                for (OrderItem item : addOnTicketGroups.getAllAddOns().getOrderItems()) {
                                    setDeliveryOption(item.getOrderItemId(), deliveryOption.getId());
                                }
                            }
                            if (null != addOnTicketGroups && null != addOnTicketGroups.getAdultAddOns()) {
                                for (OrderItem item : addOnTicketGroups.getAdultAddOns().getOrderItems()) {
                                    setDeliveryOption(item.getOrderItemId(), deliveryOption.getId());
                                }
                            }
                            if (null != addOnTicketGroups && null != addOnTicketGroups.getChildAddOns()) {
                                for (OrderItem item : addOnTicketGroups.getChildAddOns().getOrderItems()) {
                                    setDeliveryOption(item.getOrderItemId(), deliveryOption.getId());
                                }
                            }
                        }
                    }
                }
            }

            return getThis();
        }

        private void setDeliveryOption(String orderItemId, String deliveryOptionId) {
            UpdateItemRequestOrderItem orderItem = updateItemRequestParams.bodyParams.getOrAddNewOrderItem(orderItemId);
            orderItem.deliveryMethod = deliveryOptionId;
        }

        public UpdateItemRequest build() {
            return new UpdateItemRequest(senderTag, priority, concurrencyType, updateItemRequestParams);
        }

    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        UpdateItemRequestParams networkParams = (UpdateItemRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcRefreshToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    UpdateItemResponse response = services.updateCartItem(
                            wcToken,
                            wcRefreshToken,
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            networkParams.bodyParams);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                    services.updateCartItem(
                            wcToken,
                            wcRefreshToken,
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            networkParams.bodyParams,
                            this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(UpdateItemResponse updateItemResponse, Response response) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }

        if (updateItemResponse == null) {
            updateItemResponse = new UpdateItemResponse();
        }

        super.handleSuccess(updateItemResponse, response);
    }


    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }

        UpdateItemResponse response = new UpdateItemResponse();
        super.handleFailure(response, retrofitError);
    }

}

