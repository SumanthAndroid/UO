package com.universalstudios.orlandoresort.model.network.domain.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkErrorResponse;

import org.parceler.Parcel;

/**
 * Offer accepted error response
 *
 * @author acampbell
 */
@Parcel
public class OfferAcceptedErrorResponse extends NetworkErrorResponse {

    @SerializedName("statusCode")
    Integer statusCode;

    // Empty object
    @SerializedName("result")
    OfferAcceptedResult result;

    @SerializedName("message")
    String message;

    @SerializedName("isOperational")
    Boolean isOperational;

    @SerializedName("rawError")
    OfferAcceptedError rawError;

    @SerializedName("cause")
    OfferAcceptedError cause;

    public Integer getStatusCode() {
        return statusCode;
    }

    public OfferAcceptedResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getOperational() {
        return isOperational;
    }

    public OfferAcceptedError getRawError() {
        return rawError;
    }

    public OfferAcceptedError getCause() {
        return cause;
    }
}