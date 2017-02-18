package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class ProductSummaryFacet extends GsonObject {

    @SerializedName("value")
    String value;

    @SerializedName("Entry")
    List<ProductSummaryFacetEntry> productSummaryFacetEntry;

    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }

    public List<ProductSummaryFacetEntry> getProductSummaryFacetEntry() {
        return productSummaryFacetEntry;
    }

    public String getValue() {
        return value;
    }

}
