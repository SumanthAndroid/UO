package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestProfileResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.GetGuestProfileRequest;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/17/16.
 * Class: GetGuestProfileResponse
 * Class Description: Response for the {@link GetGuestProfileRequest}
 */
@Parcel
public class GetGuestProfileResponse extends NetworkResponse {

    // FIXME These fields need to be default, and have getters rather than being directly used
    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    GuestProfileResult result;

    public GuestProfileResult getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
