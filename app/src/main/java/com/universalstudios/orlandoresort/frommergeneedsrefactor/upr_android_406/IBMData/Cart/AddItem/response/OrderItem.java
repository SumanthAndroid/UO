package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by IBM_ADMIN on 1/26/2016.
 */
public class OrderItem extends GsonObject {

    @SerializedName("productId")
    private String productId;

    @SerializedName("itemAttributes")
    private Object itemAttributes;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("orderItemId")
    private String orderItemId;

    /**
     * @return The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return The itemAttributes
     */
    public Object getItemAttributes() {
        return itemAttributes;
    }

    /**
     * @return The quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @return The orderItemId
     */
    public String getOrderItemId() {
        return orderItemId;
    }

}
