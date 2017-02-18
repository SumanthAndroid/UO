package com.universalstudios.orlandoresort.controller.userinterface.account.address;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;

public class AddressViewModel extends BaseObservable {

    @Bindable
    String name;

    @Bindable
    String addressLine1;

    @Bindable
    String addressLine2;

    @Bindable
    String cityStateZip;

    public AddressViewModel() {
    }

    public void setAddress(Address address) {
        if (address != null) {
            setName(AccountUtils.buildUsersName(address));
            setAddressLine1(address.getAddressLine1());
            setAddressLine2(address.getAddressLine2());
            setCityStateZip(AccountUtils.buildCityStateZipLine(address));
        } else {
            setName(null);
            setAddressLine1(null);
            setAddressLine2(null);
            setCityStateZip(null);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        notifyPropertyChanged(BR.addressLine1);
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        notifyPropertyChanged(BR.addressLine2);
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
        notifyPropertyChanged(BR.cityStateZip);
    }
}
