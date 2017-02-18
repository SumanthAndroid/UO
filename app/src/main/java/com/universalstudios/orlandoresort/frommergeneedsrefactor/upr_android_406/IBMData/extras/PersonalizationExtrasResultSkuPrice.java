package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class PersonalizationExtrasResultSkuPrice extends GsonObject {

    public static final String USAGE_DISPLAY = "Display";

    @SerializedName("value")
    String value;

    @SerializedName("currency")
    String currency;

    @SerializedName("usage")
    String usage;

    @SerializedName("description")
    String description;

    @NonNull
    public static PersonalizationExtrasResultSkuPrice fromCommerceCardItem(@NonNull CommerceCardItem commerceCardItem) {
        PersonalizationExtrasResultSkuPrice price = new PersonalizationExtrasResultSkuPrice();
        price.usage = USAGE_DISPLAY;
        if (commerceCardItem.getPricingAndInventory() != null) {
            BigDecimal offerPrice = commerceCardItem.getPricingAndInventory().getOfferPrice();
            if (offerPrice != null) {
                price.value = offerPrice.toString();
            }
        }
        return price;
    }

    public Double getCurrency() {
        return NumberUtils.toDouble(currency);
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getValue() {
        return value;
    }

    public boolean isDisplay() {
        return USAGE_DISPLAY.equalsIgnoreCase(usage);
    }
}
