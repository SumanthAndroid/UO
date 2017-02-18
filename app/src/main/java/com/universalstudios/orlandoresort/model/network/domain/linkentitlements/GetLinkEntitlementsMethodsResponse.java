package com.universalstudios.orlandoresort.model.network.domain.linkentitlements;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
@Parcel
public class GetLinkEntitlementsMethodsResponse extends NetworkResponse {
    @SerializedName("statusCode")
    int statusCode;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    GetLinkEntitlementsMethodsResult result;

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public GetLinkEntitlementsMethodsResult getResult() {
        return result;
    }
}
