package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author tjudkins
 * @since 10/19/16
 */
@Parcel
public class PersonalizationOffer extends GsonObject {

    @SerializedName("name")
    String name;

    @SerializedName("contentId")
    String contentId;

    @SerializedName("code")
    String code;

    public String getName() {
        return name;
    }

    public String getContentId() {
        return contentId;
    }

    public String getCode() {
        return code;
    }
}
