package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by IBM_ADMIN on 2/22/2016.
 */
@Parcel
public class GetStateResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    List<ProvState> states;

    /**
     * @return The states
     */
    public List<ProvState> getStates() {
        return states;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
