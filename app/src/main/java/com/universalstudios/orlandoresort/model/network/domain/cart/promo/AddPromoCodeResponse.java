package com.universalstudios.orlandoresort.model.network.domain.cart.promo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponseWithErrors;

import org.parceler.Parcel;

/**
 * @author acampbell
 */
@Parcel
public class AddPromoCodeResponse extends NetworkResponseWithErrors<AddPromoCodeErrorResponse> {

    @SerializedName("orderId")
    String orderId;

    @SerializedName("promoCode")
    String promoCode;

    public String getOrderId() {
        return orderId;
    }

    public String getPromoCode() {
        return promoCode;
    }
}