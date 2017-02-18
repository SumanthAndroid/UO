package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
public class PriceInventoryEvent extends GsonObject {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SerializedName("partNumber")
    private String partNumber;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    public void setEndDate(Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        this.endDate = endDate != null ? sdf.format(endDate) : null;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStartDate(Date startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        this.startDate = startDate != null ? sdf.format(startDate) : null;
    }
}
