package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * @author tjudkins
 * @since 10/19/16
 */
@Parcel
public class GetPersonalizationOffersResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    PersonalizationOffersResult personalizationOffersResult;

    public PersonalizationOffersResult getPersonalizationOffersResult() {
        return personalizationOffersResult;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
