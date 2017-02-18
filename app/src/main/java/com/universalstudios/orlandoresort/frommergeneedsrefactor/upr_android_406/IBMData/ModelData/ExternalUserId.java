package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ModelData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;


/**
 * Created by IBM_ADMIN on 1/25/2016.
 */
public class ExternalUserId extends GsonObject {

    @SerializedName("externalUserId")
    @Expose
    private String externalUserId;

    /**
     *
     * @return
     * The externalUserId
     */
    public String getExternalUserId() {
        return externalUserId;
    }

    /**
     *
     * @param externalUserId
     * The externalUserId
     */
    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }
}
