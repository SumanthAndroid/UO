package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class GetCatalogCategoryResponse extends NetworkResponse {

    @SerializedName("statusCode")
    int statusCode;

    @SerializedName("result")
    List<CatalogCategoryResult> catalogCategoryResult;

    public List<CatalogCategoryResult> getCatalogCategoryResult() {
        return catalogCategoryResult;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
