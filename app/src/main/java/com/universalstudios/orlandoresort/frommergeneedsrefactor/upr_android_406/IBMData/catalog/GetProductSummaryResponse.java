package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 *
 * Created by Tyler Ritchie on 10/12/16.
 */
@Parcel
public class GetProductSummaryResponse extends NetworkResponse {
    @SerializedName("recordSetTotal")
    String recordSetTotal;

    @SerializedName("BreadCrumbTrailEntryView")
    List<ProductSummaryBreadCrumbTrailEntry> productSummaryBreadCrumbTrailEntry;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("resourceName")
    String resourceName;

    @SerializedName("recordSetStartNumber")
    String recordSetStartNumber;

    @SerializedName("recordSetComplete")
    String recordSetComplete;

    @SerializedName("FacetView")
    List<ProductSummaryFacet> productSummaryFacet;

    @SerializedName("CatalogEntryView")
    List<ProductSummaryCatalogEntry> productSummaryCatalogEntryView;

    @SerializedName("recordSetCount")
    String recordSetCount;

    @SerializedName("MetaData")
    List<ProductSummaryMetaData> productSummaryMetaData;

    public List<ProductSummaryBreadCrumbTrailEntry> getProductSummaryBreadCrumbTrailEntry() {
        return productSummaryBreadCrumbTrailEntry;
    }

    public List<ProductSummaryCatalogEntry> getProductSummaryCatalogEntryView() {
        return productSummaryCatalogEntryView;
    }

    public List<ProductSummaryFacet> getProductSummaryFacet() {
        return productSummaryFacet;
    }

    public List<ProductSummaryMetaData> getProductSummaryMetaData() {
        return productSummaryMetaData;
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
