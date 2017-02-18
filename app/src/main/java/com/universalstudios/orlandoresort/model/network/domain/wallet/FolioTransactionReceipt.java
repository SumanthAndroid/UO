package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.List;

@Parcel
public class FolioTransactionReceipt extends GsonObject {

    @SerializedName("lineItems")
    List<FolioTransactionLineItem> lineItems;

    @SerializedName("amountTotal")
    BigDecimal amountTotal;

    @SerializedName("subtotal")
    BigDecimal subtotal;

    @SerializedName("taxes")
    BigDecimal taxes;

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public List<FolioTransactionLineItem> getLineItems() {
        return lineItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }
}