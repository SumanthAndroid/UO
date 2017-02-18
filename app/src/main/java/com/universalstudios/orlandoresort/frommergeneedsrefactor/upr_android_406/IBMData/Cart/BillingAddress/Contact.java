package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address.AddressType;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IBM_ADMIN on 6/1/2016.
 */
public class Contact extends GsonObject {
    private static final String IS_PRIMARY_TRUE = "true";
    private static final String IS_PRIMARY_FALSE = "false";

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("addressLine")
    private List<String> addressLine = new ArrayList<String>();

    @SerializedName("nickName")
    private String nickName;

    @SerializedName("addressType")
    private String addressType;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("city")
    private String city;

    @SerializedName("zipCode")
    private String zipCode;

    @SerializedName("phone1")
    private String phone1;

    @SerializedName("email1")
    private String email1;

    @SerializedName("phone1Type")
    private String phone1Type;

    @SerializedName("xaddr_addressField1")
    private String xaddrAddressField1;

    private Contact(@AddressType String addressType, String nickName, String email1, String firstName,
                    String lastName, List<String> addressLine, String city, String state, String zipCode,
                    String country, String phone, String primary) {
        this.addressType = addressType;
        this.nickName = nickName;
        this.email1 = email1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.phone1 = phone;
        this.primary = primary;
    }

    private Contact(@AddressType String addressType, String nickName, String email1, String firstName,
                    String lastName, List<String> addressLine, String city, String state, String zipCode,
                    String country, String phone, String primary, String xaddrAddressField1) {
        this(addressType, nickName, email1, firstName, lastName, addressLine, city, state, zipCode,
                country, phone, primary);
        this.xaddrAddressField1 = xaddrAddressField1;
    }


    public String getPhone1Type() {
        return phone1Type;
    }

    public void setPhone1Type(String phone1Type) {
        this.phone1Type = phone1Type;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        setPrimary(primary ? IS_PRIMARY_TRUE : IS_PRIMARY_FALSE);
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @SerializedName("primary")
    private String primary;

    /**
     *
     * @return
     * The phone1
     */
    public String getPhone1() {
        return phone1;
    }
    /**
     *
     * @param phone1
     * The lastName
     */
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    /**
     *
     * @return
     * The email1
     */
    public String getEmail1() {
        return email1;
    }
    /**
     *
     * @param email1
     * The lastName
     */
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    /**
     *
     * @return
     * The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     * The lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     * The addressLine
     */
    public List<String> getAddressLine() {
        return addressLine;
    }

    /**
     *
     * @param addressLine
     * The addressLine
     */
    public void setAddressLine(List<String> addressLine) {
        this.addressLine = addressLine;
    }

    /**
     *
     * @return
     * The nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @param nickName
     * The nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     *
     * @return
     * The addressType
     */
    public String getAddressType() {
        return addressType;
    }

    /**
     *
     * @param addressType
     * The addressType
     */
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    /**
     *
     * @return
     * The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     * The firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zipCode
     * The zipCode
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setXaddrAddressField1(String xaddrAddressField1) {
        this.xaddrAddressField1 = xaddrAddressField1;
    }

    public static class Builder {
        private String mLastName;
        private String mAddressLine1;
        private String mAddressLine2;
        private String mCity;
        private String mZipCode;
        private String mNickName;
        private String mPhone;
        private String mState;
        private String mEmail1;
        private String mCountryCode;
        private String mFirstName;
        private String mAddressType;
        private String mPrimary;
        private String mXaddrAddressField1;

        public Builder(){
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
         * @param country 2-letter country code
         * @return
         */
        public Builder setCountryCode(String country) {
            mCountryCode = country;
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

        public Builder setPrimaryString(String primary) {
            mPrimary = primary;
            return this;
        }

        public Builder setPrimary(boolean b) {
            setPrimaryString(b ? IS_PRIMARY_TRUE : IS_PRIMARY_FALSE);
            return this;
        }

        public Builder setXaddrAddressField1(String mXaddrAddressField1) {
            this.mXaddrAddressField1 = mXaddrAddressField1;
            return this;
        }

        public Contact build() {
            List<String> addressLine = new ArrayList<>();
            addressLine.add(mAddressLine1);
            if (!TextUtils.isEmpty(mAddressLine2))
                addressLine.add(mAddressLine2);
            return new Contact(mAddressType, mNickName, mEmail1, mFirstName, mLastName, addressLine,
                    mCity, mState, mZipCode, mCountryCode, mPhone, mPrimary, mXaddrAddressField1);
        }
    }
}
