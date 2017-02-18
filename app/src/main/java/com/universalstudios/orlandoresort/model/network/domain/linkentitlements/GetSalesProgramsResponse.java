package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
@Parcel
public class GetSalesProgramsResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    HashMap<String, String> result;

    @SerializedName("message")
    String message;

    public int getStatusCode() {
        return statusCode;
    }

    public HashMap<String, String> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
