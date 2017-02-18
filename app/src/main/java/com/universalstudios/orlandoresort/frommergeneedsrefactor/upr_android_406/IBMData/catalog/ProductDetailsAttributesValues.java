package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsAttributesValues extends GsonObject {

    @SerializedName("values")
    String values;

    @SerializedName("extendedValue")
    List<ProductDetailsAttributesValuesExtended> extendedValue;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("uniqueID")
    String uniqueId;

    public List<ProductDetailsAttributesValuesExtended> getExtendedValue() {
        return extendedValue;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getValues() {
        return values;
    }
}
