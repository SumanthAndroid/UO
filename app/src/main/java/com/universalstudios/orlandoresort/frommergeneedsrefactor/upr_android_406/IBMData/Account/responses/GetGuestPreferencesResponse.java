package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreferencesResult;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

/**
 * @author Tyler Ritchie on 11/15/16
 */
public class GetGuestPreferencesResponse extends NetworkResponse {

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("result")
    private GuestPreferencesResult result;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public GuestPreferencesResult getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
