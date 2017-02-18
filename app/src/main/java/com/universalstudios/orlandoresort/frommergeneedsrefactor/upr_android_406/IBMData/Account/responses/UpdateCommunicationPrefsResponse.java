package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.CommunicationResult;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 *
 * Created by Tyler Ritchie on 11/15/16.
 */
@Parcel
public class UpdateCommunicationPrefsResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    CommunicationResult result;

    public CommunicationResult getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
