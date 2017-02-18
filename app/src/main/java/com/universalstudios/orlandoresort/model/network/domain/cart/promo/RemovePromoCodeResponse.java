package com.universalstudios.orlandoresort.model.network.domain.cart.promo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponseWithErrors;

import org.parceler.Parcel;

/**
 * Remove promo code response
 *
 * @author acampbell
 */
@Parcel
public class RemovePromoCodeResponse extends NetworkResponseWithErrors<RemovePromoCodeErrorResponse> {

    @SerializedName("orderId")
    String orderId;

    public String getOrderId() {
        return orderId;
    }
}