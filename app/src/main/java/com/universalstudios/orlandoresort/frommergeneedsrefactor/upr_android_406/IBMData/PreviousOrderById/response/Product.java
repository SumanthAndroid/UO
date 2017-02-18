package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by IBM_ADMIN on 2/12/2016.
 */
@Parcel
public class Product extends GsonObject {

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("longDescription")
    String longDescription;

    @SerializedName("name")
    String name;

    @SerializedName("id")
    String id;

    @SerializedName("partNumber")
    String partNumber;

    /**
     *
     * @return
     * The shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     *
     * @param shortDescription
     * The shortDescription
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     *
     * @return
     * The longDescription
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     *
     * @param longDescription
     * The longDescription
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The partNumber
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     *
     * @param partNumber
     * The partNumber
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
}
