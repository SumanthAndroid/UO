package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: GetCardsResponse
 * Class Description: Response for getting the cards request
 */
@Parcel
public class GetCardsResponse extends NetworkResponse {

    @SerializedName("result")
    GetCardsResult result;

    @SerializedName("statusCode")
    int statusCode;

    public GetCardsResult getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
