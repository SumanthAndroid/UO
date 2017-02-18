package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceOfferPricingAndInventory
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class CommerceOfferPricingAndInventory extends GsonObject {

    @SerializedName("isInventoryControlled")
    boolean isInventoryControlled;

    @SerializedName("inventoryEvents")
    List<CommerceInventoryItem> inventoyItems;

    @SerializedName("offerPrice")
    BigDecimal offerPrice;

    public boolean isInventoryControlled() {
        return isInventoryControlled;
    }

    public List<CommerceInventoryItem> getInventoyItems() {
        return inventoyItems;
    }

    public BigDecimal getOfferPrice() {
        return offerPrice;
    }
}
