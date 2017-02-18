package com.universalstudios.orlandoresort.model.network.domain.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponseWithErrors;

import org.parceler.Parcel;

/**
 * Accepted offer response
 *
 * @author acampbell
 */
@Parcel
public class OfferAcceptedResponse extends NetworkResponseWithErrors<OfferAcceptedErrorResponse> {

    @SerializedName("statusCode")
    Integer statusCode;

    // Empty object
    @SerializedName("result")
    OfferAcceptedResult result;

    @SerializedName("message")
    String message;

    public Integer getStatusCode() {
        return statusCode;
    }

    public OfferAcceptedResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}