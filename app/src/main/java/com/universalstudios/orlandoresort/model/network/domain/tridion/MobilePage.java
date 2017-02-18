package com.universalstudios.orlandoresort.model.network.domain.tridion;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseUnwrapper;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/19/16.
 * Class: MobilePage
 * Class Description: Modal for containing mobile page
 */
@Parcel
public class MobilePage extends GsonObject implements DatabaseUnwrapper<MobilePage> {

    @SerializedName("TridionID")
    public String tridionId;

    @SerializedName("TitleName")
    public String titleName;

    @SerializedName("MobileDisplayName")
    public String mobileDisplayName;

    @SerializedName("ThumbnailImage")
    public String thumbnailImageUrl;

    @SerializedName("VenueName")
    public String venueName;

    @SerializedName("LandName")
    public String landName;

    @SerializedName("MobileShortDescription")
    public String shortDescription;

    @SerializedName("PageIdentifier")
    public String pageIdentifier;

    @SerializedName("Url")
    public String url;

    @SerializedName("Id")
    public Long id;

    @SerializedName("PhoneNumber")
    public String phoneNumber;

    @Override
    public MobilePage unwrap(Cursor cursor) {
        return MobilePage
                .fromJson(cursor.getString(cursor.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.MobilePagesTable.COL_JSON)),
                        MobilePage.class);
    }

    public Long getId() {
        return id;
    }

    public String getLandName() {
        return landName;
    }

    public String getMobileDisplayName() {
        return mobileDisplayName;
    }

    public String getPageIdentifier() {
        return pageIdentifier;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getTridionId() {
        return tridionId;
    }

    public String getUrl() {
        return url;
    }

    public String getVenueName() {
        return venueName;
    }
}
