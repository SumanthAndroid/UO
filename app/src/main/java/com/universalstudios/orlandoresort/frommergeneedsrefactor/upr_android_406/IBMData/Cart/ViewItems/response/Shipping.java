package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.io.Serializable;

/**
 * Created by IBM_ADMIN on 1/25/2016.
 */

public class Shipping extends GsonObject implements Serializable {

    @SerializedName("expectedShipDate")
    private String expectedShipDate;

    @SerializedName("carrier")
    private String carrier;

    @SerializedName("shipModeId")
    private String shipModeId;

    @SerializedName("description")
    private String description;

    /**
     * @return The expectedShipDate
     */
    public String getExpectedShipDate() {
        return expectedShipDate;
    }

    /**
     * @param expectedShipDate The expectedShipDate
     */
    public void setExpectedShipDate(String expectedShipDate) {
        this.expectedShipDate = expectedShipDate;
    }

    /**
     * @return The carrier
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * @param carrier The carrier
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    /**
     * @return The shipModeId
     */
    public String getShipModeId() {
        return shipModeId;
    }

    /**
     * @param shipModeId The shipModeId
     */
    public void setShipModeId(String shipModeId) {
        this.shipModeId = shipModeId;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
