package com.universalstudios.orlandoresort.model.network.domain.checkout;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/11/17.
 */
@Parcel
public class GetFlexpayContractResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    String result;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
