package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request_params;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 10/24/16.
 */

public class CreateLinkedAccountRequestBody extends GsonObject {

    @SerializedName("sourceId")
    private @SourceId.SourceIdType String sourceId;

    @SerializedName("customerOrderId")
    private String customerOrderId;

    @SerializedName("guestId")
    private String guestId;

    @SerializedName("password")
    private String password;

    @SerializedName("birthDate")
    private String birthDate;

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSourceId(@SourceId.SourceIdType String sourceId) {
        this.sourceId = sourceId;
    }

}
