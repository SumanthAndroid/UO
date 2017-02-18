package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.Map;

/**
 * Response for setting spending limit alert preferences
 *
 * @author acampbell
 */
@Parcel
public class SetSpendingLimitAlertResponse extends NetworkResponse {

    @SerializedName("statusCode")
    Integer statusCode;

    /**
     * Empty object
     */
    @SerializedName("result")
    SetSpendingLimitAlertResult result;

    @SerializedName("message")
    String message;

    public Integer getStatusCode() {
        return statusCode;
    }

    public SetSpendingLimitAlertResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
