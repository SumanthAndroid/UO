package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommercePricingAndInventory
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class CommercePricingAndInventory extends GsonObject {

    @SerializedName("currency")
    String currency;

    @SerializedName("offerPricesAndInventory")
    HashMap<String, CommerceOfferPricingAndInventory> offerPricingAndInventory;

    @SerializedName("listPrice")
    BigDecimal listPrice;

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public HashMap<String, CommerceOfferPricingAndInventory> getOfferPricingAndInventory() {
        return offerPricingAndInventory;
    }

    public BigDecimal getOfferPrice() {
        BigDecimal offerPrice = BigDecimal.valueOf(-1);
        if (null != offerPricingAndInventory && !offerPricingAndInventory.isEmpty()) {
            CommerceOfferPricingAndInventory pricingAndInventory = (CommerceOfferPricingAndInventory) offerPricingAndInventory.values().toArray()[0];
            if (null != pricingAndInventory) {
                offerPrice = pricingAndInventory.getOfferPrice();
            }
        }
        return offerPrice;
    }

    @NonNull
    public String getOfferDateString() {
        String dateString = null;
        if (null != offerPricingAndInventory && !offerPricingAndInventory.isEmpty()) {
            dateString = (String) offerPricingAndInventory.keySet().toArray()[0];
        }
        if (null == dateString) {
            dateString = "";
        }
        return dateString;
    }

}
