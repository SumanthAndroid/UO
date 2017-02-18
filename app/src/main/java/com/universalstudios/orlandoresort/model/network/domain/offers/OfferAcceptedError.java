package com.universalstudios.orlandoresort.model.network.domain.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Offer accepted error message
 *
 * @author acampbell
 */
@Parcel
public class OfferAcceptedError extends GsonObject {

    @SerializedName("message")
    String message;

    @SerializedName("stack")
    String stack;

    public String getMessage() {
        return message;
    }

    public String getStack() {
        return stack;
    }
}