package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponseWithErrors;

import org.parceler.Parcel;

/**
 * Created by IBM_ADMIN on 1/25/2016.
 */
@Parcel
public class AddItemResponse extends NetworkResponseWithErrors<AddItemErrorResponse> {

    @SerializedName("order")
    Order order;

    /**
     * @return The order
     */
    public Order getOrder() {
        return order;
    }

}
