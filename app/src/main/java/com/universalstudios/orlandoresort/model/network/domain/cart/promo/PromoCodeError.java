package com.universalstudios.orlandoresort.model.network.domain.cart.promo;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Error object returned for an promo code failure
 *
 * @author acampbell
 */
@Parcel
public class PromoCodeError extends GsonObject {

    @SerializedName("errorParameters")
    String errorParameters;

    @SerializedName("errorCode")
    String errorCode;

    @SerializedName("errorKey")
    String errorKey;

    @SerializedName("errorLevel")
    String errorLevel;

    @SerializedName("errorMessage")
    String errorMessage;

    public String getErrorParameters() {
        return errorParameters;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}