package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class TravelPartyMember extends GsonObject {
    @SerializedName("sequenceId")
    Integer sequenceId;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("suffix")
    String suffix;

    @ParcelConstructor
    public TravelPartyMember(String firstName, String lastName, String suffix, Integer sequenceId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.suffix = suffix;
        this.sequenceId = sequenceId;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getFullName() {
        if (TextUtils.isEmpty(getSuffix())) {
            return getFirstName() + " " + getLastName();
        } else {
            return getFirstName() + " " + getLastName() + ", " + getSuffix();
        }
    }

    /**
     * treats null and empty string as equal
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TravelPartyMember that = (TravelPartyMember) o;
        if(getSequenceId() != null ? !getSequenceId().equals(that.getSequenceId()) : getSequenceId() == null) {
            return false;
        }
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : TextUtils.isEmpty(that.getLastName())) {
            return false;
        }
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : TextUtils.isEmpty(that.getFirstName())) {
            return false;
        }
        return getSuffix() != null ? getSuffix().equals(that.getSuffix()) : TextUtils.isEmpty(that.getSuffix());
    }
}
