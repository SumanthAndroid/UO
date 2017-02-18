package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Model class for {@link ProductSummaryCatalogEntry}.
 *
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class ProductSummaryCatalogEntry extends GsonObject {

    @SerializedName("longDescription")
    String longDescription;

    @SerializedName("buyable")
    String buyable;

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("parentCategoryID")
    String parentCategoryId;

    @SerializedName("keyword")
    String keyword;

    @SerializedName("productType")
    String productType;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("name")
    String name;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("uniqueID")
    String uniqueId;

    @SerializedName("storeID")
    String storeId;

    public String getBuyable() {
        return buyable;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getName() {
        return name;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getProductType() {
        return productType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
