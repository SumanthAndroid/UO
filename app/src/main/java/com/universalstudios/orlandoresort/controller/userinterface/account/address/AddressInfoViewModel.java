package com.universalstudios.orlandoresort.controller.userinterface.account.address;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;

import org.parceler.Transient;

/**
 * Created by tritchie on 9/30/16.
 */

public class AddressInfoViewModel extends BaseObservable {

    public interface OnAddressDeletedListener {
        void onAddressDeleted(AddressInfo addressInfo);
    }

    final AddressInfo addressInfo;
    boolean allRequiredFieldsComplete;
    boolean primaryAddressChecked;
    boolean floridaZipRequired;
    boolean validFloridaZip;
    boolean validGeorgiaZip;
    boolean georgiaZipRequired;
    final boolean newAddress;
    final boolean deleteAllowed;
    @Bindable
    boolean showLoading;
    OnAddressDeletedListener onAddressDeletedListener;

    public AddressInfoViewModel(CountryStateArrays countryStateArrays, boolean allowDelete, boolean newAddress) {
        addressInfo = new AddressInfo(countryStateArrays);
        addressInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
        this.deleteAllowed = allowDelete;
        this.newAddress = newAddress;
    }

    @Bindable
    public boolean getAllRequiredFieldsComplete() {
        return allRequiredFieldsComplete;
    }

    public void setAllRequiredFieldsComplete(boolean allRequiredFieldsComplete) {
        this.allRequiredFieldsComplete = allRequiredFieldsComplete;
        notifyPropertyChanged(BR.allRequiredFieldsComplete);
    }

    @Bindable
    public boolean isPrimaryAddressChecked() {
        return primaryAddressChecked;
    }

    public void setPrimaryAddressChecked(boolean primaryAddressChecked) {
        this.primaryAddressChecked = primaryAddressChecked;
        addressInfo.setPrimaryAddress(primaryAddressChecked);
        notifyPropertyChanged(BR.primaryAddressChecked);
    }

    private void checkAllRequiredFieldsValid() {
        setAllRequiredFieldsComplete(addressInfo.areAllFieldsValid() && isValidFloridaZip());
    }

    private void checkAllRequiredFieldsValidGeorgia(){
        setAllRequiredFieldsComplete(addressInfo.areAllFieldsValid() && isValidGeorgiaZip());

    }
    public void onPrimaryAddressChanged(View v) {
        if (v instanceof SwitchCompat) {
            setPrimaryAddressChecked(((SwitchCompat)v).isChecked());
        }
    }

    public boolean isFloridaZipRequired() {
        return floridaZipRequired;
    }

    public void setFloridaZipRequired(boolean floridaZipRequired) {
        this.floridaZipRequired = floridaZipRequired;
        checkAllRequiredFieldsValid();
    }

    public boolean isValidFloridaZip() {
        return !floridaZipRequired || validFloridaZip;
    }

    public void setValidFloridaZip(boolean validFloridaZip) {
        this.validFloridaZip = validFloridaZip;
        checkAllRequiredFieldsValid();
    }

    public boolean isGeorgiaZipRequired(){
        return georgiaZipRequired;
    }

    public void setGeorgiaZipRequired(boolean georgiaZipRequired) {
        this.georgiaZipRequired = georgiaZipRequired;
        checkAllRequiredFieldsValidGeorgia();
    }

    public boolean isValidGeorgiaZip() {
        return !georgiaZipRequired || validGeorgiaZip;
    }

    public void setValidGeorgiaZip(boolean validGeorgiaZip) {
        this.validGeorgiaZip = validGeorgiaZip;
        checkAllRequiredFieldsValidGeorgia();
    }

    public boolean isDeleteAllowed() {
        return deleteAllowed;
    }

    public boolean isNewAddress() {
        return newAddress;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyPropertyChanged(BR.showLoading);
    }

    public OnAddressDeletedListener getOnAddressDeletedListener() {
        return onAddressDeletedListener;
    }

    public void onDeleteClicked() {
        if (null != onAddressDeletedListener) {
            onAddressDeletedListener.onAddressDeleted(addressInfo);
        }
    }

    public void setOnAddressDeletedListener(OnAddressDeletedListener onAddressDeletedListener) {
        this.onAddressDeletedListener = onAddressDeletedListener;
    }

    @Transient
    public OnPropertyChangedCallback onFieldsChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            checkAllRequiredFieldsValid();
        }
    };

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

}
