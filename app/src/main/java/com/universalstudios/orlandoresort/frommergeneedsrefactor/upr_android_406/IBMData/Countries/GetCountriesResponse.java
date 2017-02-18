package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by IBM_ADMIN on 2/22/2016.
 */
@Parcel
public class GetCountriesResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    List<Country> countries;

    public int getStatusCode() {
        return statusCode;
    }

    public List<Country> getCountries() {
        return countries;
    }
}
