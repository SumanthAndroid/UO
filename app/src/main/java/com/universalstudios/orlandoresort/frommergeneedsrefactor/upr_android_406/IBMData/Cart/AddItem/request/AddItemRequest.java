package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServices;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices.IBMOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.request.NetworkParams;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Request for adding items to a guest's cart.
 *
 * @author Steven Byle
 * @author Tyler Ritchie
 * @author Antoine Campbell
 */
public class AddItemRequest extends IBMOrlandoServicesRequest implements Callback<AddItemResponse> {
    private static final String TAG = AddItemRequest.class.getSimpleName();

    private AddItemRequest(String senderTag,
                           NetworkRequest.Priority priority, NetworkRequest.ConcurrencyType concurrencyType, AddItemRequestParams requestParams) {

        super(senderTag, priority, concurrencyType, requestParams);
    }

    /**
     * Private internal Network Parameter class used by the {@link AddItemRequest.Builder}.
     */
    private static class AddItemRequestParams extends NetworkParams {
        private AddItemRequestBodyParams requestBody;

        public AddItemRequestParams() {
            super();
            requestBody = new AddItemRequestBodyParams();
        }
    }

    /**
     * Internal body parameters that are set individually using the {@link AddItemRequest.Builder}.
     */
    public static class AddItemRequestBodyParams extends GsonObject {
        @SerializedName("orderItem")
        private List<OrderItem> orderItem;

        public AddItemRequestBodyParams() {
            super();
            orderItem = new ArrayList<>();
        }
    }


    /**
     * Builder for setting parameter fields and generating the {@link AddItemRequest} object.
     */
    public static class Builder extends BaseNetworkRequestBuilder<Builder> {
        private AddItemRequestParams addItemRequestParams;

        public Builder(NetworkRequestSender sender) {
            super(sender);
            this.addItemRequestParams = new AddItemRequestParams();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setOrderItem(List<OrderItem> orderItem) {
            this.addItemRequestParams.requestBody.orderItem = orderItem;
            return getThis();
        }

        public Builder addOrderItemWithAttributes(String partNumber, List<CommerceAttribute> attributes) {
            List<CommerceAttribute> attributesToAdd = new ArrayList<>();
            if (attributes != null) {
                for (CommerceAttribute attribute : attributes) {
                    if (null != attribute) {
                        // Keep these attributes and throw away the rest when copying
                        if (attribute.isDate() ||
                            attribute.isInvEventId() ||
                            attribute.isInvResourceId()) {
                            attributesToAdd.add(attribute);
                        }
                    }
                }
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setPartNumber(partNumber);
            orderItem.setAttributes(attributesToAdd);
            orderItem.setQuantity(1);
            this.addItemRequestParams.requestBody.orderItem.add(orderItem);

            return getThis();
        }

        public AddItemRequest build() {
            return new AddItemRequest(senderTag, priority, concurrencyType, addItemRequestParams);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        IBMOrlandoServices services = restAdapter.create(IBMOrlandoServices.class);

        AddItemRequestParams requestParams = (AddItemRequestParams) getNetworkParams();

        String wcToken = AccountStateManager.getWcToken();
        String wcTrustedToken = AccountStateManager.getWcTrustedToken();

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    AddItemResponse response = services.addCartItem(
                            wcToken,
                            wcTrustedToken,
                            BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                            BuildConfig.COMMERCE_STORE_ID,
                            requestParams.requestBody);
                    success(response, null);
                } catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.addCartItem(
                        wcToken,
                        wcTrustedToken,
                        BuildConfig.COMMERCE_SERVICES_SHOP_JUNCTION,
                        BuildConfig.COMMERCE_STORE_ID,
                        requestParams.requestBody,
                        this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(AddItemResponse addItemResponse, Response response) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "success");
        }
        if (addItemResponse == null) {
            addItemResponse = new AddItemResponse();
        }
        super.handleSuccess(addItemResponse, response);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "failure");
        }
        super.handleFailure(new AddItemResponse(), retrofitError);
    }
}
