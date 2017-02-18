package com.universalstudios.orlandoresort.model.network.domain.cart.promo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkErrorResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Error object returned for a remove promo code failure
 *
 * @author acampbell
 */
@Parcel
public class RemovePromoCodeErrorResponse extends NetworkErrorResponse {

    @SerializedName("errors")
    List<PromoCodeError> errors;

    public List<PromoCodeError> getErrors() {
        return errors;
    }

}