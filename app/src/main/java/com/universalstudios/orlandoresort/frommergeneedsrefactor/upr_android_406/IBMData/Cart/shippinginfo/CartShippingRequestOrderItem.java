package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Ritchie on 11/30/16.
 */
public class CartShippingRequestOrderItem extends GsonObject {

    @SerializedName("attributes")
    private List<CartShippingRequestCommerceAttribute> attributes;

    @SerializedName("orderItemId")
    private String orderItemId;

    @SerializedName("addressId")
    private String addressId;

    public static class Builder {
        private CartShippingRequestOrderItem model;

        public Builder() {
            super();
            model = new CartShippingRequestOrderItem();
            model.attributes = new ArrayList<>();
        }

        public Builder addAttribute(CartShippingRequestCommerceAttribute.Builder builder) {
            model.attributes.add(builder.build());
            return this;
        }

        public Builder setOrderItemId(String orderItemId) {
            model.orderItemId = orderItemId;
            return this;
        }

        public Builder setAddressId(String addressId) {
            model.addressId = addressId;
            return this;
        }

        public CartShippingRequestOrderItem build() {
            return model;
        }
    }
}