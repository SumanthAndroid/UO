package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author acampbell
 */
@Parcel
public class TicketGroupingResult extends GsonObject {

    @SerializedName("sessionId")
    String sessionId;

    @SerializedName("order")
    TicketGroupOrder order;

    public String getSessionId() {
        return sessionId;
    }

    public TicketGroupOrder getOrder() {
        return order;
    }
}
