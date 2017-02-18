package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Offer response object for tickets
 *
 * @author acampbell
 */
@Parcel
public class Offer extends GsonObject {

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("code")
    String code;

    @SerializedName("treatmentCode")
    String treatmentCode;

    @SerializedName("contentId")
    String contentId;

    @SerializedName("effectiveDate")
    String effectiveDate;

    @SerializedName("expirationDate")
    String expirationDate;

    @SerializedName("expirationDuration")
    Integer expirationDuration;

    @SerializedName("geolocation")
    String geolocation;

    @SerializedName("marketerScore")
    Integer marketerScore;

    @SerializedName("offerType")
    String offerType;

    @SerializedName("offerTypeCode")
    String offerTypeCode;

    @SerializedName("property")
    String property;

    @SerializedName("rtLearningMode")
    Integer rtLearningMode;

    @SerializedName("rtLearningModelId")
    Integer rtLearningModelId;

    @SerializedName("rtSelectionMethod")
    Integer rtSelectionMethod;

    @SerializedName("uacInteractionPointId")
    Integer uacInteractionPointId;

    @SerializedName("uacInteractionPointName")
    String uacInteractionPointName;

    @SerializedName("finalScore")
    Double finalScore;

    @SerializedName("confidencePct")
    Double confidencePct;

    @SerializedName("personalized")
    Boolean personalized;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getTreatmentCode() {
        return treatmentCode;
    }

    public String getContentId() {
        return contentId;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public Integer getExpirationDuration() {
        return expirationDuration;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public Integer getMarketerScore() {
        return marketerScore;
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

    public Integer getRtLearningMode() {
        return rtLearningMode;
    }

    public Integer getRtLearningModelId() {
        return rtLearningModelId;
    }

    public Integer getRtSelectionMethod() {
        return rtSelectionMethod;
    }

    public Integer getUacInteractionPointId() {
        return uacInteractionPointId;
    }

    public String getUacInteractionPointName() {
        return uacInteractionPointName;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public Double getConfidencePct() {
        return confidencePct;
    }

    public Boolean getPersonalized() {
        return personalized;
    }
}