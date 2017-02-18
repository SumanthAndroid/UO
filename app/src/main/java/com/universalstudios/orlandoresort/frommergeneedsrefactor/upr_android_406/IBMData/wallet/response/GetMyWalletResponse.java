package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

/**
 * Created by Jack Hughes on 9/27/16.
 */
@Parcel
public class GetMyWalletResponse extends NetworkResponse {

    @SerializedName("statusCode")
    String statusCode;

    @SerializedName("result")
    WalletEntitlements walletEntitlements;

    /**
     * Method to get the HTTP Status Code for the response.
     *
     * @return a String for the HTTP Status code
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Method to get the Wallet Entitlements (for both active and inactive entitlements).
     *
     * @return a {@ling WalletEntitlements} containing both active and inactive entitlements
     */
    public WalletEntitlements getWalletEntitlements() {
        return walletEntitlements;
    }
}
