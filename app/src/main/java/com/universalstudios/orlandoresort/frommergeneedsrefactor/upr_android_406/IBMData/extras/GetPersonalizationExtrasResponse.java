package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class GetPersonalizationExtrasResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    PersonalizationExtrasResult personalizationExtrasResult;

    public PersonalizationExtrasResult getPersonalizationExtrasResult() {
        return personalizationExtrasResult;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
