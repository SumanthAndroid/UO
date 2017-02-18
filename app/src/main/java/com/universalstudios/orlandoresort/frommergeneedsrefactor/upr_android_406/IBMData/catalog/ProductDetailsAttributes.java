package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsAttributes extends GsonObject {

    private static final String IDENTIFIER_DAYS = "DAYS";
    private static final String IDENTIFIER_AGE = "AGE";
    private static final String IDENTIFIER_UCW_MOVIE_INCLUDED = "UCW_MOVIE_INCLUDED";
    private static final String IDENTIFIER_PARK = "PARK";
    private static final String IDENTIFIER_DINING_EVNT_TYPE = "DINING_EVNT_TYPE";
    private static final String IDENTIFIER_MIN_QUANTITY = "MIN_QUANTITY";

    @SerializedName("usage")
    String usage;

    @SerializedName("Values")
    List<ProductDetailsAttributesValues> productDetailsAttributesValues;

    @SerializedName("searchable")
    String searchable;

    @SerializedName("dataType")
    String dataType;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("comparable")
    String comparable;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("displayable")
    String displayable;

    @SerializedName("uniqueId")
    String uniqueId;

    public boolean getComparable() {
        return Boolean.parseBoolean(comparable);
    }

    public String getDataType() {
        return dataType;
    }

    public String getDescription() {
        return description;
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

    public List<ProductDetailsAttributesValues> getProductDetailsAttributesValues() {
        return productDetailsAttributesValues;
    }

    public boolean getSearchable() {
        return Boolean.parseBoolean(searchable);
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isDays() {
        return IDENTIFIER_DAYS.equalsIgnoreCase(identifier);
    }

    public boolean isAge() {
        return IDENTIFIER_AGE.equalsIgnoreCase(identifier);
    }

    public boolean isUcwMovieIncluded() {
        return IDENTIFIER_UCW_MOVIE_INCLUDED.equalsIgnoreCase(identifier);
    }

    public boolean isPark() {
        return IDENTIFIER_PARK.equalsIgnoreCase(identifier);
    }

    public boolean isDiningEventType() {
        return IDENTIFIER_DINING_EVNT_TYPE.equalsIgnoreCase(identifier);
    }

    public boolean isMinQuantity() {
        return IDENTIFIER_MIN_QUANTITY.equalsIgnoreCase(identifier);
    }
}
