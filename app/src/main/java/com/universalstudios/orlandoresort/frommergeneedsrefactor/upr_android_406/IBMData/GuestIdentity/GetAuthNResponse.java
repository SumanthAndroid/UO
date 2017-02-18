package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by IBM_ADMIN on 1/25/2016.
 */
@Parcel
public class GetAuthNResponse extends NetworkResponse {
    @SerializedName("result")
    GetAuthNResult result;

    @SerializedName("statusCode")
    int statusCode;

    public GetAuthNResult getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
