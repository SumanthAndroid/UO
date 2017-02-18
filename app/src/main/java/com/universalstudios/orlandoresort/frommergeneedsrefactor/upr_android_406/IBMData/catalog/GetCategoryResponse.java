package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;

/**
 * Model class for a response containing all categories
 * ({@link CategoryCatalogGroup} <code>List</code>).
 *
 * Created by Tyler Ritchie on 10/12/16.
 */

public class GetCategoryResponse extends NetworkResponse {
    @SerializedName("recordSetTotal")
    private String recordSetTotal;

    @SerializedName("resourceId")
    private String resourceId;

    @SerializedName("resourceName")
    private String resourceName;

    @SerializedName("recordSetStartNumber")
    private String recordSetStartNumber;

    @SerializedName("recordSetComplete")
    private String recordSetComplete;

    @SerializedName("CatalogGroupView")
    private List<CategoryCatalogGroup> categoryCatalogGroup;

    @SerializedName("recordSetCount")
    private String recordSetCount;

    @SerializedName("MetaData")
    private List<CategoryMetaData> categoryMetaData;

    public List<CategoryCatalogGroup> getCategoryCatalogGroup() {
        return categoryCatalogGroup;
    }

    public List<CategoryMetaData> getCategoryMetaData() {
        return categoryMetaData;
    }

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
}
