package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

@Parcel
public class GetFolioTransactionsResult extends GsonObject {

    @SerializedName("merchantId")
    String merchantId;

    @SerializedName("dateTime")
    String dateTime;

    @SerializedName("guestId")
    String guestId;

    @SerializedName("receipt")
    FolioTransactionReceipt receipt;

    @SerializedName("merchant")
    FolioTransactionMerchant merchant;

    @SerializedName("transactionType")
    String transactionType;

    public String getDateTime() {
        return dateTime;
    }

    public String getGuestId() {
        return guestId;
    }

    public FolioTransactionMerchant getMerchant() {
        return merchant;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public FolioTransactionReceipt getReceipt() {
        return receipt;
    }

    public String getTransactionType() {
        return transactionType;
    }
}