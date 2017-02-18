package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class SubcategoryCatalogGroup extends GsonObject {

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("name")
    String name;

    @SerializedName("parentCatalogGroupID")
    List<String> parentCatalogGroupId;

    @SerializedName("productsURL")
    String productsURL;

    @SerializedName("uniqueID")
    String uniqueID;

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public List<String> getParentCatalogGroupId() {
        return parentCatalogGroupId;
    }

    public String getProductsURL() {
        return productsURL;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getUniqueID() {
        return uniqueID;
    }
}
