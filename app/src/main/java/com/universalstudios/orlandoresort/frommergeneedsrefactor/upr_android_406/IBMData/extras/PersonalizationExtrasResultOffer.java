package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class PersonalizationExtrasResultOffer extends GsonObject {

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("code")
    String code;

    @SerializedName("contentId")
    String contentId;

    @SerializedName("effectiveDate")
    String effectiveDate;

    @SerializedName("expirationDate")
    String expirationDate;

    @SerializedName("expirationDuration")
    int expirationDuration;

    @SerializedName("geolocation")
    String geolocation;

    @SerializedName("marketerScore")
    String marketerScore;

    @SerializedName("offerType")
    String offerType;

    @SerializedName("offerTypeCode")
    String offerTypeCode;

    @SerializedName("property")
    String property;

    @SerializedName("rtLearningMode")
    int rtLearningMode;

    @SerializedName("rtLearningModelId")
    int rtLearningModelId;

    @SerializedName("rtSelectionMethod")
    int rtSelectionMethod;

    @SerializedName("uacInteractionPointId")
    int uacInteractionPointId;

    @SerializedName("uacInteractionPointName")
    String uacInteractionPointName;

    public String getCode() {
        return code;
    }

    public String getContentId() {
        return contentId;
    }

    public String getDescription() {
        return description;
    }

    public String getEffectiveDateString() {
        return effectiveDate;
    }

    public Date getEffectiveDate() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return df.parse(effectiveDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public String getExpirationDateString() {
        return expirationDate;
    }

    public Date getExpirationDate() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            return df.parse(expirationDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public int getExpirationDuration() {
        return expirationDuration;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public String getMarketerScore() {
        return marketerScore;
    }

    public String getName() {
        return name;
    }

    public String getOfferType() {
        return offerType;
    }

    public String getOfferTypeCode() {
        return offerTypeCode;
    }

    public String getProperty() {
        return property;
    }

    public int getRtLearningMode() {
        return rtLearningMode;
    }

    public int getRtLearningModelId() {
        return rtLearningModelId;
    }

    public int getRtSelectionMethod() {
        return rtSelectionMethod;
    }

    public int getUacInteractionPointId() {
        return uacInteractionPointId;
    }

    public String getUacInteractionPointName() {
        return uacInteractionPointName;
    }
}
