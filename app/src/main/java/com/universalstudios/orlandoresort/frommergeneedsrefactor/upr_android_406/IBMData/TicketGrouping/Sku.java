package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.List;

/**
 * @author acampbell
 */
public class Sku extends GsonObject {

    @SerializedName("buyable")
    private String buyable;

    @SerializedName("price")
    private List<Price> price;

    @SerializedName("name")
    private String name;

    @SerializedName("uniqueID")
    private String uniqueID;

    @SerializedName("partNumber")
    private String partNumber;

    @SerializedName("attributes")
    private List<Attribute> attributes;

    public String getBuyable() {
        return buyable;
    }

    public List<Price> getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}