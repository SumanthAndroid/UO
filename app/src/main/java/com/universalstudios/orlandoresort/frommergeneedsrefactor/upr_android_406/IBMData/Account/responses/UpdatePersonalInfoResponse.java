package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/25/16.
 * Class: UpdatePersonalInfoResponse
 * Class Description: Response to updating user's personal info
 */
@Parcel
public class UpdatePersonalInfoResponse extends NetworkResponse {

    @SerializedName("statusCode")
    public int statusCode;

    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
