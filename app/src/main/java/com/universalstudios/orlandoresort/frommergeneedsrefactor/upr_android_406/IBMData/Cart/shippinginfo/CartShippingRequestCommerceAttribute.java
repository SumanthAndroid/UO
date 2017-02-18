package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 11/30/16.
 */
public class CartShippingRequestCommerceAttribute extends GsonObject {

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    public static class Builder {
        private CartShippingRequestCommerceAttribute model;

        public Builder() {
            super();
            model = new CartShippingRequestCommerceAttribute();
        }

        public Builder setName(String name) {
            model.name = name;
            return this;
        }

        public Builder setValue(String value) {
            model.value = value;
            return this;
        }

        public CartShippingRequestCommerceAttribute build() {
            return model;
        }
    }
}
