package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.math.BigDecimal;

/**
 * @author acampbell
 */
public class Price extends GsonObject {

    private static final String USAGE_DISPLAY = "Display";

    @SerializedName("value")
    private BigDecimal value;

    @SerializedName("currency")
    private String currency;

    @SerializedName("usage")
    private String usage;

    @SerializedName("description")
    private String description;

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisplay() {
        return USAGE_DISPLAY.equalsIgnoreCase(usage);
    }
}