package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsSkuPrice extends GsonObject {

    @SerializedName("SKUPriceDescription")
    String skuPriceDescription;

    @SerializedName("SKUPriceValue")
    String skuPriceValue;

    @SerializedName("SKUPriceUsage")
    String skuPriceUsage;

    public String getSkuPriceDescription() {
        return skuPriceDescription;
    }

    public String getSkuPriceUsage() {
        return skuPriceUsage;
    }

    public Double getSkuPriceValue() {
        return NumberUtils.toDouble(skuPriceValue);
    }
}
