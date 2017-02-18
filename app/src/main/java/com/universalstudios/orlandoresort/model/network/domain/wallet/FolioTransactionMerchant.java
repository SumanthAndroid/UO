package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

@Parcel
public class FolioTransactionMerchant extends GsonObject {

    @SerializedName("merchantName")
    String merchantName;

    @SerializedName("merchantCategory")
    String merchantCategory;

    @SerializedName("address")
    String address;

    @SerializedName("city")
    String city;

    @SerializedName("state")
    String state;

    @SerializedName("zip")
    String zip;

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}