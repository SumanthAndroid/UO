package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/10/16.
 * Class: CreateAccountResponse
 * Class Description: Response from Creating account
 */
@Parcel
public class CreateAccountResponse extends NetworkResponse {

    @SerializedName("result")
    CreateAccountResponseProfile response;

    @SerializedName("message")
    String message;

    @SerializedName("statusCode")
    int statusCode;

    public CreateAccountResponseProfile getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
