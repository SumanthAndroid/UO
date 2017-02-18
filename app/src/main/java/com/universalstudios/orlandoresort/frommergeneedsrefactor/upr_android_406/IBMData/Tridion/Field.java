package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion;

/**
 * Created by jamesblack on 5/31/16.
 */
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by jamesblack on 4/20/16.
 */
public class Field extends GsonObject {

    @SerializedName("XPath")
    private String xPath;

    @SerializedName("Value")
    private String value;

    public String getXPath() {
        return xPath;
    }

    public void setXPath(String xPath) {
        this.xPath = xPath;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}