package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model class for a response containing all categories
 * ({@link SubcategoryCatalogGroup} <code>List</code>).
 *
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class GetSubcategoryResponse extends NetworkResponse {

    @SerializedName("recordSetTotal")
    String recordSetTotal;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("resourceName")
    String resourceName;

    @SerializedName("recordSetStartNumber")
    String recordSetStartNumber;

    @SerializedName("recordSetComplete")
    String recordSetComplete;

    @SerializedName("CatalogGroupView")
    List<SubcategoryCatalogGroup> subcategoryCatalogGroup;

    @SerializedName("recordSetCount")
    String recordSetCount;

    @SerializedName("MetaData")
    List<SubcategoryMetaData> subcategoryMetaData;

    public boolean getRecordSetComplete() {
        return Boolean.parseBoolean(recordSetComplete);
    }

    public Integer getRecordSetCount() {
        return NumberUtils.toInteger(recordSetCount);
    }

    public Integer getRecordSetStartNumber() {
        return NumberUtils.toInteger(recordSetStartNumber);
    }

    public Integer getRecordSetTotal() {
        return NumberUtils.toInteger(recordSetTotal);
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public List<SubcategoryCatalogGroup> getSubcategoryCatalogGroup() {
        return subcategoryCatalogGroup;
    }

    public List<SubcategoryMetaData> getSubcategoryMetaData() {
        return subcategoryMetaData;
    }
}
