package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Nicholas Hanna on 1/13/2017.
 */
@Parcel
public class WalletFolioResult extends GsonObject {

    @SerializedName("cards")
    List<WalletFolioCard> cards;

    @SerializedName("travelParty")
    List<WalletTravelPartyMember> travelParty;

    @SerializedName("alert")
    WalletFolioAlert alert;

    @SerializedName("hasPin")
    Boolean hasPin;

    public WalletFolioAlert getAlert() {
        return alert;
    }

    public List<WalletFolioCard> getCards() {
        return cards;
    }

    public List<WalletTravelPartyMember> getTravelParty() {
        return travelParty;
    }

    public Boolean getHasPin() {
        return hasPin;
    }
}
