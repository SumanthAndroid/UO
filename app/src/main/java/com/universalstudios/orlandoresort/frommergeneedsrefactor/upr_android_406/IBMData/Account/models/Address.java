package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jamesblack on 7/5/16.
 */
@Parcel
public class Address extends GsonObject {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ADDRESS_TYPE_BILLING, ADDRESS_TYPE_SHIPPING, ADDRESS_TYPE_GENERAL})
    public @interface AddressType {}
    // Declare the constants
    public static final String ADDRESS_TYPE_BILLING = "B";
    public static final String ADDRESS_TYPE_SHIPPING = "S";
    public static final String ADDRESS_TYPE_GENERAL = "Generic Address";

    private static final String IS_PRIMARY_TRUE = "Y";
    private static final String IS_PRIMARY_FALSE = "N";

    public Address() {
        // default constructor
        //hardcoded source id for Android
        this.sourceId = GuestProfile.GUEST_PROFILE_SOURCE_ID;
    }

    private Address(String middleName, String lastName, String addressLine1, String addressLine2, String city, String zipCode, String nickName, String phone, String addressId, String state, String email1, String country, String email2, String firstName, String addressType, String createdDate, String primary, String sourceId) {
        this.middleName = middleName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.zipCode = zipCode;
        this.nickName = nickName;
        this.phone = phone;
        this.addressId = addressId;
        this.state = state;
        this.email1 = email1;
        this.country = country;
        this.email2 = email2;
        this.firstName = firstName;
        this.addressType = addressType;
        this.createdDate = createdDate;
        this.primary = primary;
        this.sourceId = sourceId;
    }

    @SerializedName("middleName")
    String middleName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("addressLine1")
    String addressLine1;

    @SerializedName("addressLine2")
    String addressLine2;

    @SerializedName("city")
    String city;

    @SerializedName("postalCode")
    String zipCode;

    @SerializedName("nickName")
    String nickName;

    @SerializedName("phoneNumber")
    String phone;

    @SerializedName("addressId")
    String addressId;

    @SerializedName("state")
    String state;

    @SerializedName("email1")
    String email1;

    @SerializedName("countryCode")
    String countryCode;

    @SerializedName("country")
    String country;

    @SerializedName("email2")
    String email2;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("addressType")
    String addressType;

    @SerializedName("createdDate")
    String createdDate;

    @SerializedName("primary")
    String primary;

    @SerializedName("sourceId")
    String sourceId;

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String setPrimaryString(String primary) {
        return primary;
    }


    public String getPrimaryString() {
        return primary;
    }

    /**
     * Returns true if primary is not null and is equal to "Y".  Returns
     * false otherwise.
     * @return String primary is not null and is equal to "Y"
     */
    public void setPrimary(boolean b)
    {
        primary = b ? IS_PRIMARY_TRUE : IS_PRIMARY_FALSE;
    }

    /**
     * Returns true if primary is not null and is equal to "Y".  Returns
     * false otherwise.
     * @return String primary is not null and is equal to "Y"
     */
    public boolean isPrimary()
    {
        return primary != null && IS_PRIMARY_TRUE.equalsIgnoreCase(primary);
    }

    public String getSourceId() {
        return sourceId;
    }

    public static class Builder {
        private String mMiddleName;
        private String mLastName;
        private String mAddressLine1;
        private String mAddressLine2;
        private String mCity;
        private String mZipCode;
        private String mNickName;
        private String mPhone;
        private String mAddressId;
        private String mState;
        private String mEmail1;
        private String mCountryCode;
        private String mEmail2;
        private String mFirstName;
        private String mAddressType;
        private String mCreatedDate;
        private String mPrimary;
        private String mSourceId;

        public Builder(@SourceId.SourceIdType String sourceId){
            if (TextUtils.isEmpty(sourceId)) {
                this.mSourceId = SourceId.EDIT_ADDRESS;
            } else {
                this.mSourceId = sourceId;
            }
        }

        public Builder setMiddleName(String middleName) {
            mMiddleName = middleName;
            return this;
        }

        public Builder setLastName(String lastName) {
            mLastName = lastName;
            return this;
        }

        public Builder setAddressLine1(String addressLine1) {
            mAddressLine1 = addressLine1;
            return this;
        }

        public Builder setAddressLine2(String addressLine2) {
            mAddressLine2 = addressLine2;
            return this;
        }

        public Builder setCity(String city) {
            mCity = city;
            return this;
        }

        public Builder setZipCode(String zipCode) {
            mZipCode = zipCode;
            return this;
        }

        public Builder setNickName(String nickName) {
            mNickName = nickName;
            return this;
        }

        public Builder setPhone(String phone) {
            mPhone = phone;
            return this;
        }

        public Builder setAddressId(String addressId) {
            mAddressId = addressId;
            return this;
        }

        public Builder setState(String state) {
            mState = state;
            return this;
        }

        public Builder setEmail1(String email1) {
            mEmail1 = email1;
            return this;
        }

        /**
         * This should be the 2-letter country code, not the country name
         * @param countryCode 2-letter country code
         * @return
         */
        public Builder setCountryCode(String countryCode) {
            // This is not straight-forward, but the services expect countryCode in the country
            // field of the request when adding/updating addresses.
            mCountryCode = countryCode;
            return this;
        }

        public Builder setEmail2(String email2) {
            mEmail2 = email2;
            return this;
        }

        public Builder setFirstName(String firstName) {
            mFirstName = firstName;
            return this;
        }

        public Builder setAddressType(String addressType) {
            mAddressType = addressType;
            return this;
        }

        public Builder setCreatedDate(String createdDate) {
            mCreatedDate = createdDate;
            return this;
        }

        public Builder setPrimaryString(String primary) {
            mPrimary = primary;
            return this;
        }

        public Builder setPrimary(boolean b) {
            mPrimary = b ? IS_PRIMARY_TRUE : IS_PRIMARY_FALSE;
            return this;
        }

        public Address build() {
            // This is not straight-forward, but the services expect countryCode in the country
            // field of the request when adding/updating addresses.
            return new Address(mMiddleName, mLastName, mAddressLine1, mAddressLine2, mCity, mZipCode, mNickName, mPhone, mAddressId, mState, mEmail1, mCountryCode, mEmail2, mFirstName, mAddressType, mCreatedDate, mPrimary, mSourceId);
        }
    }
}
