package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/16/16.
 * Class: GuestProfile
 * Class Description: Model for the Guest's profile
 */
@Parcel
public class GuestProfile extends GsonObject {
    public static final String TAG = "GuestProfile";
    public static final String GUEST_PROFILE_SOURCE_ID = "1000004";

    @SerializedName("sourceId")
    @SourceId.SourceIdType String sourceId;

    @SerializedName("userId")
    String email;

    @SerializedName("birthDate")
    String birthDate;

    @SerializedName("contact")
    Contact contact;

    @SerializedName("preferences")
    GuestPreference preference;

    @SerializedName("createdDate")
    String createdDate;

    @SerializedName("guestId")
    String guestId;

    @SerializedName("ldapId")
    String ldapId;

    @SerializedName("mdmId")
    String mdmId;

    @SerializedName("addresses")
    List<Address> addresses;

    @SerializedName("nameSuffix")
    String suffix;

    @Transient
    boolean addressesPurged;

    public @SourceId.SourceIdType String getSourceId() {
        return sourceId;
    }

    public void setSourceId(@SourceId.SourceIdType String sourceId) {
        this.sourceId = sourceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public @NonNull Contact getContact() {
        if (null == contact) {
            contact = new Contact();
        }
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public @NonNull GuestPreference getPreference() {
        if (null == preference) {
            preference = new GuestPreference();
        }
        return preference;
    }

    public void setPreference(GuestPreference preference) {
        this.preference = preference;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getLdapId() {
        return ldapId;
    }

    public void setLdapId(String ldapId) {
        this.ldapId = ldapId;
    }

    public String getMdmId() {
        return mdmId;
    }

    public void setMdmId(String mdmId) {
        this.mdmId = mdmId;
    }

    public List<Address> getAddresses() {
        // Per US17041, only use generic addresses from the user's profile. Addresses designated
        // as shipping and billing are for business tracking only.
        // Also, put primary address first
        if (null != addresses && addresses.size() > 0) {
            List<Address> genericAddresses = new ArrayList<>(addresses.size());
            for (Address address : addresses) {
                if (Address.ADDRESS_TYPE_GENERAL.equals(address.getAddressType())) {
                    if (address.isPrimary()) {
                        genericAddresses.add(0, address);
                    } else {
                        genericAddresses.add(address);
                    }
                }
            }
            addresses = genericAddresses;
            addressesPurged = true;
        }

        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        this.addressesPurged = false;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public boolean isContactNull() {
        return null == contact;
    }

    public boolean hasAddress() {
        return getAddresses() != null && getAddresses().size() > 0;
    }

    public Address getPrimaryAddress() {
        Address primary = null;
        if (null != addresses && addresses.size() > 0) {
            for (Address address : addresses) {
                if (Address.ADDRESS_TYPE_GENERAL.equals(address.getAddressType())) {
                    if (address.isPrimary()) {
                        primary = address;
                        break;
                    }
                }
            }
        }
        return primary;
    }
}
