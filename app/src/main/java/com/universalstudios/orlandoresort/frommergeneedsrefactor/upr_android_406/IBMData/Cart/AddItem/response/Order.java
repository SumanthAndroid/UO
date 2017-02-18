package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.List;

/**
 * Created by IBM_ADMIN on 1/26/2016.
 */
public class Order extends GsonObject {

    @SerializedName("orderItem")
    private List<OrderItem> orderItem;

    @SerializedName("orderId")
    private String orderId;

    /**
     * @return The orderItem
     */
    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    /**
     * @param orderItem The orderItem
     */
    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    /**
     * @return The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId The orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}

