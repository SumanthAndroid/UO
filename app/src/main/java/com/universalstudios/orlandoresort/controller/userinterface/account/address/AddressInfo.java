package com.universalstudios.orlandoresort.controller.userinterface.account.address;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View.OnFocusChangeListener;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.TicketRequestUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address.Builder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.SourceId;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress.Contact;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.ProvState;
import com.universalstudios.orlandoresort.view.textview.PhoneEditText;

/**
 * Created by tjudkins on 9/23/16.
 */

public class AddressInfo extends BaseObservable {

    final CountryStateArrays countryStateArrays;
    String addressId;
    boolean primaryAddress;
    String firstName;
    String lastName;
    String addressLine1;
    String addressLine2;
    String city;
    String stateProvince;
    String zip;
    String country;
    String countryCode;
    String phone;
    int countryPosition = 0;
    int statePosition = -1;
    OnFocusChangeListener onZipFocusChangeListener;
    boolean showPhone = true;

    public AddressInfo(@NonNull CountryStateArrays countryStateArrays) {
        this.countryStateArrays = countryStateArrays;
    }

    public AddressInfo(@NonNull Address address, @NonNull CountryStateArrays countryStateArrays) {
        this.countryStateArrays = countryStateArrays;
        if (!TextUtils.isEmpty(address.getCountryCode())) {
            setCountryPosition(countryStateArrays.findCountryPosition(address.getCountryCode()));
        }
        if (countryPosition == 0 && !TextUtils.isEmpty(address.getState())) {
            setStatePosition(countryStateArrays.findStatePosition(address.getState()));
        }
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setCity(address.getCity());
        setStateProvince(address.getState());
        setZip(address.getZipCode());
        setCountry(address.getCountry());
        setPhone(address.getPhone());
        setAddressId(address.getAddressId());
        setPrimaryAddress(address.isPrimary());
    }

    public void copy(AddressInfo addressInfo) {
        setAddressId(addressInfo.getAddressId());
        setPrimaryAddress(addressInfo.isPrimaryAddress());
        setFirstName(addressInfo.getFirstName());
        setLastName(addressInfo.getLastName());
        setAddressLine1(addressInfo.getAddressLine1());
        setAddressLine2(addressInfo.getAddressLine2());
        setCity(addressInfo.getCity());
        setStateProvince(addressInfo.getStateProvince());
        setZip(addressInfo.getZip());
        setCountry(addressInfo.getCountry());
        setPhone(addressInfo.getPhone());
        setCountrPositionWithoutPropagating(addressInfo.getCountryPosition());
        setStatePosition(addressInfo.getStatePosition());
        setShowPhone(addressInfo.getShowPhone());
    }

    public Address toAddress(@SourceId.SourceIdType String sourceId) {
        Address address = new Builder(sourceId)
        .setFirstName(getFirstName())
        .setLastName(getLastName())
        .setAddressLine1(getAddressLine1())
        .setAddressLine2(getAddressLine2())
        .setCity(getCity())
        .setState(getStateProvince())
        .setZipCode(getZip())
        .setCountryCode(getCountryCode())
        .setPhone(getPhone())
        .setAddressId(getAddressId())
        .setPrimary(isPrimaryAddress())
        .build();
        return address;
    }

    public Contact toContact(@NonNull String orderId, @Address.AddressType String addressType, String email) {
        String nickName = TicketRequestUtils.createAddressNickname(addressType, orderId);
        Contact contact = new Contact.Builder()
                .setNickName(nickName)
                .setAddressType(addressType)
                .setFirstName(getFirstName())
                .setLastName(getLastName())
                .setAddressLine1(getAddressLine1())
                .setAddressLine2(getAddressLine2())
                .setCity(getCity())
                .setState(getStateProvince())
                .setZipCode(getZip())
                .setCountryCode(getCountryCode())
                .setPhone(getPhone())
                .setPrimary(isPrimaryAddress())
                .setEmail1(email)
                .setXaddrAddressField1(getAddressId())
                .build();
        return contact;
    }

    public CountryStateArrays getCountryStateArrays() {
        return countryStateArrays;
    }

    @Bindable
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
        notifyPropertyChanged(BR.addressId);

    }

    @Bindable
    public boolean isPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(boolean primaryAddress) {
        this.primaryAddress = primaryAddress;
    }
    @Bindable
    public boolean getDomesticAddress() {
        // Assume US is the first item in the list because that is what is expected.
        return (countryPosition == 0);
    }

    @Bindable
    public boolean getFloridaState() {
        if(getDomesticAddress() && getCountryStateArrays() != null
                && getCountryStateArrays().getStates() != null
                && !getCountryStateArrays().getStates().isEmpty()
                && getCountryStateArrays().getStates().get(statePosition) != null) {
            return ProvState.STATE_CODE_FLORIDA.equals(getCountryStateArrays().getStates().get(statePosition).getCode());
        }
        return false;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    @Bindable
    public String getAddressLine1() {
        return addressLine1;
    }

    @Bindable
    public String getAddressLine2() {
        return addressLine2;
    }

    @Bindable
    public String getCity() {
        return city;
    }

    @Bindable
    public String getStateProvince() {
        if (getDomesticAddress() && countryStateArrays.getStates().size() > statePosition
                && statePosition >=0) {
            return countryStateArrays.getStates().get(statePosition).getCode();
        }
        return stateProvince;
    }

    @Bindable
    public String getZip() {
        return zip;
    }

    @Bindable
    public String getCountry() {
        if (countryStateArrays.getCountries().size() > countryPosition
                && countryPosition >=0) {
            return countryStateArrays.getCountries().get(countryPosition).getDisplayName();
        }
        return country;
    }

    public String getCountryCode() {
        if (countryStateArrays.getCountries().size() > countryPosition
                && countryPosition >=0) {
            return countryStateArrays.getCountries().get(countryPosition).getCode();
        }
        return country;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    @Bindable
    public int getCountryPosition() {
        return countryPosition;
    }

    @Bindable
    public int getStatePosition() {
        return statePosition;
    }

    @Bindable
    public OnFocusChangeListener getOnZipFocusChangeListener() {
        return onZipFocusChangeListener;
    }

    public void setOnZipFocusChangeListener(OnFocusChangeListener onZipFocusChangeListener) {
        this.onZipFocusChangeListener = onZipFocusChangeListener;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        notifyPropertyChanged(BR.addressLine1);
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        notifyPropertyChanged(BR.addressLine2);
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        notifyPropertyChanged(BR.stateProvince);
    }

    public void setZip(String zip) {
        this.zip = zip;
        notifyPropertyChanged(BR.zip);
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public void setCountryPosition(int countryPosition) {
        this.countryPosition = countryPosition;
        notifyPropertyChanged(BR.countryPosition);
        notifyPropertyChanged(BR.states);
        notifyPropertyChanged(BR.domesticAddress);
    }

    public void setCountrPositionWithoutPropagating(int countryPosition) {
        this.countryPosition = countryPosition;
        notifyPropertyChanged(BR.countryPosition);
    }

    public void setStatePosition(int statePosition) {
        this.statePosition = statePosition;
        notifyPropertyChanged(BR.statePosition);
    }

    public void setShowPhone(boolean show){
        this.showPhone = show;
        notifyChange();
    }

    public boolean getShowPhone(){
        return this.showPhone;
    }

    /**
     * Checks that all of the values of address info are complete and valid
     * @return true if all are valid
     */
    public boolean areAllFieldsValid() {
        // TODO in the future, it would be better to return a IntDef for a reason why the fields are invalid
        return !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                && countryPosition >= 0 && !TextUtils.isEmpty(addressLine1)
                && !TextUtils.isEmpty(city) && (!getDomesticAddress() ||
                (!TextUtils.isEmpty(city) && statePosition >= 0 && isValidZip()))
                && isValidPhone();
    }

    private boolean isValidZip() {
        if (TextUtils.isEmpty(getZip())) return false;
        String allDigitString = getZip().replaceAll("[^0-9]", "");
        int totalDigitCount = allDigitString.length();
        if (getDomesticAddress()) {
            return totalDigitCount >= 5;
        } else {
            return totalDigitCount > 0;
        }
    }
    private boolean isValidPhone() {
        if (TextUtils.isEmpty(getPhone())) return false;
        String allDigitString = getPhone().replaceAll("[^0-9]", "");
        int totalDigitCount = allDigitString.length();
        if (getDomesticAddress()) {
            return totalDigitCount >= PhoneEditText.MAX_CHARS_MINUS_FORMATTING;
        } else {
            return totalDigitCount > 0;
        }
    }

    public String toFormattedString() {
        StringBuilder sb = new StringBuilder().append(firstName); // avoids null pointer this way
        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) {
            sb.append(" ");
        }
        sb.append(lastName).append("\n")
                .append(addressLine1).append("\n");
        if (!TextUtils.isEmpty(addressLine2)) {
            sb.append(addressLine2).append("\n");
        }
        sb.append(city);
        if (!TextUtils.isEmpty(stateProvince)) {
            sb.append(", ").append(stateProvince);
        }
        if (!TextUtils.isEmpty(zip)) {
            sb.append(" ").append(zip);
        }
        sb.append("\n").append(phone);
        return sb.toString();
    }

    public String getFullName(){
        return String.format("%s %s", getFirstName(), getLastName());
    }

    @Override
    public String toString() {
        return getAddressLine1();
    }

}
