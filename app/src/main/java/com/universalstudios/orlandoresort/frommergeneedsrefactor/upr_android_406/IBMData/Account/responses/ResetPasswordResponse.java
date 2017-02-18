package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/15/16.
 * Class: ResetPasswordResponse
 * Class Description: Response to Reset password
 */
@Parcel
public class ResetPasswordResponse extends NetworkResponse {

    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }
}
