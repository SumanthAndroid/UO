package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/25/16.
 * Class: UpdateContactInfoResponse
 * Class Description: Response to update user's contact info
 */
// TODO This class should have an 'ErrorResponse' version
@Parcel
public class UpdateContactInfoResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
