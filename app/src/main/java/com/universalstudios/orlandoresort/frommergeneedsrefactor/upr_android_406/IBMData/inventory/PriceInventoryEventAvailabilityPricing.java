package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */

@Parcel
public class PriceInventoryEventAvailabilityPricing extends GsonObject {

    @SerializedName("currency")
    String currency;

    @SerializedName("amount")
    BigDecimal amount;

    @SerializedName("quantity")
    double quantity;

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public double getQuantity() {
        return quantity;
    }
}
