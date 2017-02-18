package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
public class DeleteCardResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    Object result;

    @SerializedName("message")
    String message;

    public int getStatusCode() {
        return statusCode;
    }

    public Object getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
