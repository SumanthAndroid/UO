package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/23/17.
 */
@Parcel
public class SetPrimaryCardResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    // Unsure what this object type is, don't try to parse it
    //@SerializedName("result")
    //Object result;

    @SerializedName("message")
    String message;

    public int getStatusCode() {
        return statusCode;
    }

    //public Object getResult() {
    //    return result;
    //}

    public String getMessage() {
        return message;
    }
}
