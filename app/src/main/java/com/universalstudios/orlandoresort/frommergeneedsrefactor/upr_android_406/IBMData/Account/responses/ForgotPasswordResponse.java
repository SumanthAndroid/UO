package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/11/16.
 * Class: ForgotPasswordResponse
 * Class Description: Response from the Forgotten password request
 */
@Parcel
public class ForgotPasswordResponse extends NetworkResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
