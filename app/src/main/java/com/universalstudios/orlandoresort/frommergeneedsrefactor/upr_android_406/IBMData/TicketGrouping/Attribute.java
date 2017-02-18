package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * @author acampbell
 */
public class Attribute extends GsonObject {

    private static final String NAME_TCMID1= "TCMID1";
    private static final String NAME_TCMID2= "TCMID2";

    @SerializedName("name")
    private String name;

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("values")
    private String values;

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValues() {
        return values;
    }

    // Helper methods

    public boolean isTcmId1() {
        return NAME_TCMID1.equalsIgnoreCase(identifier);
    }

    public boolean isTcmId2() {
        return NAME_TCMID2.equalsIgnoreCase(identifier);
    }
}