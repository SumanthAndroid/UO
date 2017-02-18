package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.SKUData;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.io.Serializable;

/**
 * Created by ibm_admin on 1/18/2016.
 */
public class Guest extends GsonObject implements Serializable {

    @SerializedName("isAdult")
    private boolean isAdult;

    @SerializedName("sku")
    private String sku;

    @SerializedName("price")
    private String price;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }
}
