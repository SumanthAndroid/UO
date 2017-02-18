package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Nicholas Hanna on 1/13/2017.
 */
@Parcel
public class GetWalletFolioResponse extends NetworkResponse {

    @SerializedName("statusCode")
    Integer statusCode;

    @SerializedName("result")
    WalletFolioResult walletFolioResult;

    public Integer getStatusCode() {
        return statusCode;
    }

    public WalletFolioResult getWalletFolioResult() {
        return walletFolioResult;
    }
}
