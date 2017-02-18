package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceCardsPage
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class CommerceCardsPage extends GsonObject {

    @SerializedName("eligibleUIControls")
    List<CommerceUIControl> controls;

    @SerializedName("cards")
    List<CommerceCard> cards;

    public List<CommerceUIControl> getControls() {
        return controls;
    }

    public List<CommerceCard> getCards() {
        return cards;
    }
}
