package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

@Parcel
public class CatalogEntryPrice extends GsonObject {

    @SerializedName("priceDescription")
    String description;

    @SerializedName("priceUsage")
    String usage;

    @SerializedName("priceValue")
    String value;

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getValue() {
        return value;
    }

    public Double getDoubleValue() {
        return NumberUtils.toDouble(value);
    }
}
