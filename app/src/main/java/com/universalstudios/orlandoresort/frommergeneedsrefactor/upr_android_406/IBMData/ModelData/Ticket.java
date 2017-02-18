package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ModelData;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.SKUData.Guest;

import java.io.Serializable;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by ibm_admin on 1/18/2016.
 */
public class Ticket extends GsonObject implements Serializable {

    public enum TicketType {
        ADMISSION_TICKET,
        EXPRESS_PASS
    }

    @SerializedName("days")
    private String days;

    @SerializedName("guest")
    private Guest guest;

    @SerializedName("parkName")
    private String parkName;

    @SerializedName("marketingImage")
    private String marketingImage;

    @SerializedName("marketingText")
    private String marketingText;

    public TicketType mTicketType;

    private boolean isUnlimitedUse;

    public void setIsUnlimitedUse(boolean isUnlimitedUse) {
        this.isUnlimitedUse = isUnlimitedUse;
    }

    public boolean getIsUnlimitedUse() {
        return isUnlimitedUse;
    }

    public String getMarketingText() {
        return marketingText;
    }

    public void setMarketingText(String marketingText) {
        this.marketingText = marketingText;
    }

    public String getMarketingImage() {
        return marketingImage;
    }

    public void setMarketingImage(String marketingImage) {
        this.marketingImage = marketingImage;
    }

    public Ticket(Guest g) {
        this.guest = g;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }
}
