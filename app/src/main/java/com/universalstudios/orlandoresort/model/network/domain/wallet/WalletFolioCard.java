package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by Nicholas Hanna on 1/13/2017.
 */
@Parcel
public class WalletFolioCard extends GsonObject {

    @SerializedName("cardId")
    String cardId;

    @SerializedName("name")
    String name;

    @SerializedName("type")
    String type;

    @SerializedName("expiration")
    WalletFolioExpiration expiration;

    @SerializedName("lastFour")
    String lastFour;

    @SerializedName("primary")
    Boolean primary;

    @SerializedName("addressId")
    String addressId;

    public String getCardId() {
        return cardId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public WalletFolioExpiration getExpiration() {
        return expiration;
    }

    public String getLastFour() {
        return lastFour;
    }

    public Boolean isPrimary() {
        return primary;
    }

    public String getAddressId() {
        return addressId;
    }
}
