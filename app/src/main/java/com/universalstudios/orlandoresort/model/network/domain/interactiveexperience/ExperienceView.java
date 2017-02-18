package com.universalstudios.orlandoresort.model.network.domain.interactiveexperience;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author jamestimberlake
 * @created 4/28/16.
 */
@Parcel
public class ExperienceView extends GsonObject {

    @SerializedName("Id")
    Long Id;

    @SerializedName("BaseFrame")
    String baseFrame;

    @SerializedName("LargeBottom")
    String largeBottom;

    @SerializedName("MediumBottom")
    String mediumBottom;

    @SerializedName("SmallBottom")
    String smallBotton;

    @SerializedName("BackgroundColor")
    String backgroundColor;

    @SerializedName("TitleBarColor")
    String titleBarColor;

    @SerializedName("DisplayImage")
    String defaultDisplayImage;

    @SerializedName("TextColor")
    String textColor;

    @SerializedName("TextPosition")
    String textPosition;

    public Long getId() {
        return Id;
    }

    public String getBaseFrame() {
        return baseFrame;
    }

    public String getLargeBottom() {
        return largeBottom;
    }

    public String getMediumBottom() {
        return mediumBottom;
    }

    public String getSmallBotton() {
        return smallBotton;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTitleBarColor() {
        return titleBarColor;
    }

    public String getDefaultDisplayImage() {
        return defaultDisplayImage;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getTextPosition() {
        return textPosition;
    }

}
