package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickListener;
import com.universalstudios.orlandoresort.model.pricing.DisplayPricingModel;
import com.universalstudios.orlandoresort.model.state.account.AddressCache;

import org.parceler.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjudkins on 9/22/16.
 */

public class PaymentInfoViewModel extends BaseObservable {

    public final boolean isRegisteredUser;
    private boolean isShippingRequired;
    private boolean isDomesticShipping;
    boolean requiresFloridaBillingAddress;
    boolean shippingAddressSameAsBilling;
    boolean allRequiredFieldsComplete;
    public CreditCardInfo creditCardInfo = new CreditCardInfo();
    public final AddressInfo billingAddressInfo;
    public final AddressInfo shippingAddressInfo;
    public ArrayList<AddressInfo> savedAddresses = new ArrayList<>();
    @Bindable
    private DisplayPricingModel pricingModel;
    @Bindable
    ObservableField<Boolean> requiresFlexPayTnc = new ObservableField<>();
    @Bindable
    ObservableField<Boolean> flexPayTncChecked = new ObservableField<>();
    @Bindable
    private ClickListener tncLinkClickListener;

    public PaymentInfoViewModel(boolean isRegisteredUser, CountryStateArrays countryStateArrays) {
        this.isRegisteredUser = isRegisteredUser;
        billingAddressInfo = new AddressInfo(countryStateArrays);
        shippingAddressInfo = new AddressInfo(countryStateArrays);
        setRequiresFlexPayTnc(false);
        setFlexPayTncChecked(false);
        creditCardInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
        billingAddressInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
        shippingAddressInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
        requiresFlexPayTnc.addOnPropertyChangedCallback(onFieldsChangedCallback);
        flexPayTncChecked.addOnPropertyChangedCallback(onFieldsChangedCallback);
    }

    @Bindable
    public boolean getRequiresFloridaBillingAddress() {
        return requiresFloridaBillingAddress;
    }

    public void setRequiresFloridaBillingAddress(boolean requiresFloridaBillingAddress) {
        this.requiresFloridaBillingAddress = requiresFloridaBillingAddress;
        notifyPropertyChanged(BR.requiresFloridaBillingAddress);
    }

    @Bindable
    public boolean getAllRequiredFieldsComplete() {
        return allRequiredFieldsComplete;
    }

    public void setAllRequiredFieldsComplete(boolean allRequiredFieldsComplete) {
        this.allRequiredFieldsComplete = allRequiredFieldsComplete;
        notifyPropertyChanged(BR.allRequiredFieldsComplete);
    }

    public boolean isFlexPayTncChecked() {
        return flexPayTncChecked.get();
    }

    public void setFlexPayTncChecked(boolean flexPayTncChecked) {
        this.flexPayTncChecked.set(flexPayTncChecked);
        notifyPropertyChanged(BR.flexPayTncChecked);
    }

    public boolean isRequiresFlexPayTnc() {
        return requiresFlexPayTnc.get();
    }

    public void setRequiresFlexPayTnc(boolean requiresFlexPayTnc) {
        this.requiresFlexPayTnc.set(requiresFlexPayTnc);
        notifyPropertyChanged(BR.requiresFlexPayTnc);
    }

    @Bindable
    public boolean getShippingRequired() {
        return isShippingRequired;
    }

    @Bindable
    public boolean getDomesticShipping() {
        return isDomesticShipping;
    }

    @Bindable
    public boolean getInternationalShipping() {
        return !isDomesticShipping;
    }

    public void setShippingRequired(boolean shippingRequired) {
        isShippingRequired = shippingRequired;
        notifyPropertyChanged(BR.shippingRequired);
    }

    public void setDomesticShipping(boolean domesticShipping) {
        isDomesticShipping = domesticShipping;
        notifyPropertyChanged(BR.domesticShipping);
    }

    private void checkAllRequiredFieldsValid() {
        setAllRequiredFieldsComplete(creditCardInfo.areAllFieldsValid()
                && billingAddressInfo.areAllFieldsValid()
                && (!getShippingRequired() || shippingAddressInfo.areAllFieldsValid())
                && (!isRequiresFlexPayTnc() || isFlexPayTncChecked()));
    }

    public boolean showSavedShippingAddress() {
        return isRegisteredUser && getShippingRequired();
    }

    public boolean showSavedBillingAddress() {
        return isRegisteredUser;
    }

    public boolean showNewShippingAddress() {
        return !isRegisteredUser && getShippingRequired();
    }

    public boolean showNewBillingAddress() {
        return !isRegisteredUser;
    }

    @Bindable
    public boolean getShippingAddressSameAsBilling() {
        return shippingAddressSameAsBilling;
    }

    public void setShippingAddressSameAsBilling(boolean shippingAddressSameAsBilling) {
        this.shippingAddressSameAsBilling = shippingAddressSameAsBilling;
        if (shippingAddressSameAsBilling) {
            shippingAddressInfo.copy(billingAddressInfo);
        }
        notifyPropertyChanged(BR.shippingAddressSameAsBilling);
    }

    public DisplayPricingModel getPricingModel() {
        return pricingModel;
    }

    public void setPricingModel(DisplayPricingModel pricingModel) {
        this.pricingModel = pricingModel;
        notifyPropertyChanged(BR.pricingModel);
    }

    @Bindable
    public ArrayList<AddressInfo> getSavedAddresses() {
        return savedAddresses;
    }

    public void setSavedAddresses(List<AddressInfo> addresses) {
        savedAddresses.clear();
        if (null != addresses  && addresses.size() > 0) {
            for (AddressInfo a : addresses) {
                savedAddresses.add(a);
            }
        }

        notifyPropertyChanged(BR.savedAddresses);
    }

    public ClickListener getTncLinkClickListener() {
        return tncLinkClickListener;
    }

    public void setTncLinkClickListener(ClickListener tncLinkClickListener) {
        this.tncLinkClickListener = tncLinkClickListener;
        notifyPropertyChanged(BR.tncLinkClickListener);
    }

    @Transient
    public OnPropertyChangedCallback onFieldsChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            checkAllRequiredFieldsValid();
        }
    };


}
