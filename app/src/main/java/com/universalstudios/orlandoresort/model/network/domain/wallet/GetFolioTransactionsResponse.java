package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class GetFolioTransactionsResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    List<GetFolioTransactionsResult> result;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public List<GetFolioTransactionsResult> getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }
}