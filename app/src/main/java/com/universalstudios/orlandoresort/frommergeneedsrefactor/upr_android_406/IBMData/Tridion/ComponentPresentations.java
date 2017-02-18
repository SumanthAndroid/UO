package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion;

/**
 * Created by jamesblack on 5/31/16.
 */
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by jamesblack on 4/20/16.
 */
public class ComponentPresentations extends GsonObject {
    @SerializedName("Id")
    private String id;
    @SerializedName("Title")
    private String title;
    @SerializedName("TemplateId")
    private String templateId;
    @SerializedName("TemplateTitle")
    private String templateTitle;
    @SerializedName("Fields")
    private Fields fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

}