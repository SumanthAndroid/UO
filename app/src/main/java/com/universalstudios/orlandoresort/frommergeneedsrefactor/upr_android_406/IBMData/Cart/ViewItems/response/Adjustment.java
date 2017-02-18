package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author acampbell
 */
@Parcel
public class Adjustment extends GsonObject {

    @SerializedName("usage")
    String usage;

    @SerializedName("amount")
    String amount;

    @SerializedName("description")
    String description;

    @SerializedName("code")
    String code;

    public String getUsage() {
        return usage;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}