package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/3/16.
 * Class: GetTridionSpecsResponse
 * Class Description: Response for getting the Tridion display specs for Tickets
 */
@Parcel
public class GetTridionSpecsResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    HashMap<String, HashMap<String, String>> results;

    public int getStatusCode() {
        return statusCode;
    }

    public HashMap<String, HashMap<String, String>> getResults() {
        return results;
    }
}
