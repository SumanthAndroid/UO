package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Jack Hughes on 10/3/16.
 */
@Parcel
public class AddUpdateGuestAddressesResponse extends NetworkResponse {

    @SerializedName("result")
    ArrayList<Address> response;

    @SerializedName("statusCode")
    int statusCode;

    public ArrayList<Address> getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
