package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack Hughes on 9/27/16.
 */
@Parcel
public class WalletEntitlementDetails extends GsonObject {

    @SerializedName("entitlementCount")
    Integer entitlementCount;

    @SerializedName("itemType")
    String itemType;

    @SerializedName("entitlementDescription")
    String entitlementDescription;

    @SerializedName("ticketName")
    String ticketName;

    @SerializedName("entitlements")
    ArrayList<WalletEntitlement> entitlements = new ArrayList<>();

    /**
     * Method to get the count of entitlements.
     *
     * @return a String containing the integer count of entitlements
     */
    public Integer getEntitlementCount() {
        return entitlementCount;
    }

    /**
     * Method to get the entitlement's item type. (i.e "UEP")
     *
     * @return a String containing the item type
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Method to get the description of the entitlement. Intentionally package protected.
     * Use {@link com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec#getTypeAlternativeHeaderLine1(WalletEntitlement)}
     *
     * @return a String containing the entitlement description
     */
    public String getEntitlementDescription() {
        return entitlementDescription;
    }

    /**
     * Method to get the entitlement's ticket name. (i.e "3-PARK UNIVERSAL EXPRESS℠ UNLIMITED")
     * Intentionally package protected.
     * Use {@link com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec#getTypeAlternativeHeaderLine2(WalletEntitlement)}
     *
     * @return a String containing the ticket name
     */
    public String getTicketName() {
        return ticketName;
    }

    /**
     * Method to get an {@link ArrayList<WalletEntitlement>} which holds a list of entitlements.
     *
     * @return a {@link ArrayList<WalletEntitlement>} holding entitlements
     */
    public ArrayList<WalletEntitlement> getEntitlements() {
        return entitlements;
    }

    @NonNull
    public List<String> getTcmIds() {
        List<String> tcmIds = new ArrayList<>();
        if (getEntitlements() != null) {
            for (WalletEntitlement walletEntitlement : getEntitlements()) {
                tcmIds.add(walletEntitlement.getTcmId1());
                tcmIds.add(walletEntitlement.getTcmId2());
            }
        }
        return tcmIds;
    }

}
