package com.universalstudios.orlandoresort.model.network.domain.account;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemErrorResponse;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponseWithErrors;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 1/13/2017.
 */
@Parcel
public class GetAccountStatusResponse extends NetworkResponseWithErrors<AddItemErrorResponse> {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
