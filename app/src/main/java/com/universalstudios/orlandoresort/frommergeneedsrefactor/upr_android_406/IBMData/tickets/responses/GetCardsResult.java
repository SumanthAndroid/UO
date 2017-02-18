package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardsPage;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author acampbell
 */
@Parcel
public class GetCardsResult extends GsonObject {

    @SerializedName("sessionId")
    String sessionId;

    @SerializedName("page")
    CommerceCardsPage page;

    public String getSessionId() {
        return sessionId;
    }

    public CommerceCardsPage getPage() {
        return page;
    }
}
