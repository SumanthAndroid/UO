package com.universalstudios.orlandoresort.model.network.domain.tridion;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/19/16.
 * Class: GetMobilePagesResponse
 * Class Description: Response for getting mobile pages
 */
@Parcel
public class GetMobilePagesResponse extends NetworkResponse {

    @SerializedName("Results")
    List<MobilePage> pages;

    public List<MobilePage> getPages() {
        return pages;
    }
}
