package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsSkuAttributes extends GsonObject {

    private static final String IDENTIFIER_DAYS = "DAYS";
    private static final String IDENTIFIER_AGE = "AGE";

    @SerializedName("usage")
    String usage;

    @SerializedName("Values")
    List<ProductDetailsSkuAttributesValues> productDetailsSkuAttributesValues;

    @SerializedName("searchable")
    String searchable;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("comparable")
    String comparable;

    @SerializedName("name")
    String name;

    @SerializedName("displayable")
    String displayable;

    @SerializedName("uniqueID")
    String uniqueID;

    public boolean getComparable() {
        return Boolean.parseBoolean(comparable);
    }

    public boolean getDisplayable() {
        return Boolean.parseBoolean(displayable);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public List<ProductDetailsSkuAttributesValues> getProductDetailsSkuAttributesValues() {
        return productDetailsSkuAttributesValues;
    }

    public boolean getSearchable() {
        return Boolean.parseBoolean(searchable);
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isDay() {
        return IDENTIFIER_DAYS.equalsIgnoreCase(identifier);
    }

    public boolean isAge() {
        return IDENTIFIER_AGE.equalsIgnoreCase(identifier);
    }
}
