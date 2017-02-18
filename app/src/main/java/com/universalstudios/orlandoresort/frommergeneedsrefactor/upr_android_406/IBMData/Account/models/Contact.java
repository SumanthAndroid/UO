package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

@Parcel
public class Contact extends GsonObject {

    @SerializedName("namePrefix")
    String namePrefix;

    @SerializedName("nameSuffix")
    String nameSuffix;

    @SerializedName("firstName")
    String firstname;

    @SerializedName("lastName")
    String lastname;

    @SerializedName("mobilePhone")
    String mobilePhone;

    @SerializedName("emailAddress")
    String email;

    @SerializedName("sourceId")
    @SourceId.SourceIdType String sourceId;

    public String getNamePrefix() {
        return namePrefix;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSourceId(@SourceId.SourceIdType String sourceIdType) {
        this.sourceId = sourceIdType;
    }
}
