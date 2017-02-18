package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ValidateZip;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by jamesblack on 7/7/16.
 */
@Parcel
public class Result extends GsonObject {

    @SerializedName("status")
    Boolean status;

    /**
     *
     * @return
     * The status
     */
    public Boolean getStatus() {
        return status;
    }
}
