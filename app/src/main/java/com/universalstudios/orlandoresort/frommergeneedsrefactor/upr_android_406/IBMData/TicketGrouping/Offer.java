package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.List;

/**
 * @author acampbell
 */
public class Offer extends GsonObject {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("code")
    private String code;

    @SerializedName("treatmentCode")
    private String treatmentCode;

    @SerializedName("contentId")
    private String contentId;

    @SerializedName("effectiveDate")
    private String effectiveDate;

    @SerializedName("expirationDate")
    private String expirationDate;

    @SerializedName("expirationDuration")
    private Integer expirationDuration;

    @SerializedName("geolocation")
    private String geolocation;

    @SerializedName("marketerScore")
    private Integer marketerScore;

    @SerializedName("offerType")
    private String offerType;

    @SerializedName("offerTypeCode")
    private String offerTypeCode;

    @SerializedName("property")
    private String property;

    @SerializedName("rtLearningMode")
    private Integer rtLearningMode;

    @SerializedName("rtLearningModelId")
    private Integer rtLearningModelId;

    @SerializedName("rtSelectionMethod")
    private Integer rtSelectionMethod;

    @SerializedName("uacInteractionPointId")
    private Integer uacInteractionPointId;

    @SerializedName("uacInteractionPointName")
    private String uacInteractionPointName;

    @SerializedName("finalScore")
    private Double finalScore;

    @SerializedName("confidencePct")
    private Double confidencePct;

    @SerializedName("personalized")
    private Boolean personalized;

    @SerializedName("sku")
    private Sku sku;

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

    public Sku getSku() {
        return sku;
    }

    // Helper methods

    @Nullable
    public String getTcmId1() {
        if (sku != null && sku.getAttributes() != null) {
            List<Attribute> attributes = sku.getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute != null && attribute.isTcmId1()
                        && !TextUtils.isEmpty(attribute.getValues())) {
                    return attribute.getValues();
                }
            }
        }

        return null;
    }

    @Nullable
    public String getTcmId2() {
        if (sku != null && sku.getAttributes() != null) {
            List<Attribute> attributes = sku.getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute != null && attribute.isTcmId2()
                        && !TextUtils.isEmpty(attribute.getValues())) {
                    return attribute.getValues();
                }
            }
        }

        return null;
    }
}