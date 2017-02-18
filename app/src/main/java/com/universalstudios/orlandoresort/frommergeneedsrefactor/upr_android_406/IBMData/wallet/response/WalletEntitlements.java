package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Jack Hughes on 9/27/16.
 */
@Parcel
public class WalletEntitlements extends GsonObject {

    @SerializedName("activeEntitlements")
    ArrayList<WalletEntitlementDetails> activeEntitlements;

    @SerializedName("inactiveEntitlements")
    ArrayList<WalletEntitlementDetails> inactiveEntitlements;

    /**
     * Method to get a list of {@link WalletEntitlementDetails} for active entitlements. TODO - this
     * schema is currently an array of an array, unlike the schema for inactive entitlements. It's
     * assumed they are to be the same schema, so either active or inactive entitlements will change
     * once hearing back from services.
     *
     * @return a {@link ArrayList<ArrayList<WalletEntitlements>>} for active entitlements
     */
    public ArrayList<WalletEntitlementDetails> getActiveEntitlements() {
        return activeEntitlements;
    }

    /**
     * Method to get a list of {@link WalletEntitlementDetails} for inactive entitlements. TODO -
     * this schema is currently an array, unlike the schema for active entitlements. It's assumed
     * they are to be the same schema, so either active or inactive entitlements will change once
     * hearing back from services.
     *
     * @return a {@link ArrayList<WalletEntitlements>} for inactive entitlements
     */
    public ArrayList<WalletEntitlementDetails> getInactiveEntitlements() {
        return inactiveEntitlements;
    }
}
