package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.request_body;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: GetCommerceCardsRequestBody
 * Class Description: TODO: ALWAYS FILL OUT
 */
public class GetCommerceCardsRequestBody extends GsonObject {
    public static final String TAG = "GetCommerceCardsRequestBody";

    public GetCommerceCardsRequestBody(String salesCatalogId, List<String> dates, List<String> seasons) {
        this.salesCatalogId = salesCatalogId;
        this.dates = dates;
        this.seasons = seasons;
    }

    @SerializedName("salesCatalogId")
    public String salesCatalogId;

    @SerializedName("dates")
    public List<String> dates;

    @SerializedName("seasons")
    public List<String> seasons;
}
