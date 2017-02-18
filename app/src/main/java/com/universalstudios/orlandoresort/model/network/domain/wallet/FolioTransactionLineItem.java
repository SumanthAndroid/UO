package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;

@Parcel
public class FolioTransactionLineItem extends GsonObject {

    @SerializedName("item")
    String item;

    @SerializedName("amount")
    BigDecimal amount;

    public String getItem() {
        return item;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}