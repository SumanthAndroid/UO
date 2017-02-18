package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * @author jamestimberlake
 * @created 1/2/17.
 */
@Parcel
public class Taxes extends GsonObject {

    @SerializedName("amount")
    BigDecimal amount;

    @SerializedName("description")
    String description;

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

}
