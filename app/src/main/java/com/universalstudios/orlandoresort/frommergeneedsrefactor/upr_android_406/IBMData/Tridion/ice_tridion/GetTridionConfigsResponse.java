package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/24/16.
 * Class: GetTridionConfigsResponse
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class GetTridionConfigsResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    TridionConfig result;

    public int getStatusCode() {
        return statusCode;
    }

    public TridionConfig getResult() {
        return result;
    }
}
