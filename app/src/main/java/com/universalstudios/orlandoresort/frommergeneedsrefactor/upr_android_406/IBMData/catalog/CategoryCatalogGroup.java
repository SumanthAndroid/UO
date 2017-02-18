package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Model class for CatalogGroupView.
 *
 * Created by Tyler Ritchie on 10/12/16.
 */

public class CategoryCatalogGroup extends GsonObject {

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("resourceId")
    private String resourceId;

    @SerializedName("name")
    private String name;

    @SerializedName("productsURL")
    private String productsURL;

    @SerializedName("uniqueID")
    private String uniqueID;

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
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
