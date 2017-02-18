package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ValidateZip;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author Steven Byle
 */
@Parcel
public class ValidateZipResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    Result result;

    public int getStatusCode() {
        return statusCode;
    }

    public Result getResult() {
        return result;
    }
}