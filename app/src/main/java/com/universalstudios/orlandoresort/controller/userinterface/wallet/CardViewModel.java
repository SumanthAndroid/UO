package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.text.TextUtils;

import com.android.databinding.library.baseAdapters.BR;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CountryStateArrays;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.CreditCardInfo;

import org.parceler.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjudkins on 9/22/16.
 */

public class CardViewModel extends BaseObservable {
    private static final String TAG = CardViewModel.class.getSimpleName();

    @Bindable
    private boolean enableAddButton;

    @Bindable
    private boolean enableUpdateButton;

    @Bindable
    public CreditCardInfo creditCardInfo;

    @Bindable
    private boolean newCard;

    public final AddressInfo billingAddressInfo;
    public ArrayList<AddressInfo> savedAddresses = new ArrayList<>();

    private OnPrimaryCheckChangeListener mListener;

    public CardViewModel(OnPrimaryCheckChangeListener listener, CountryStateArrays countryStateArrays, CreditCardInfo creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
        this.mListener = listener;
        setNewCard(true);
        this.billingAddressInfo = new AddressInfo(countryStateArrays);
        this.creditCardInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
        this.billingAddressInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
    }

    public void setCreditCardInfo(CreditCardInfo creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
        this.creditCardInfo.addOnPropertyChangedCallback(onFieldsChangedCallback);
        notifyPropertyChanged(BR.creditCardInfo);
    }

    public void setSavedAddresses(List<AddressInfo> addresses) {
        savedAddresses.clear();
        if (null != addresses && addresses.size() > 0) {
            for (AddressInfo a : addresses) {
                savedAddresses.add(a);
            }
        }
        notifyPropertyChanged(BR.savedAddresses);
    }

    @Bindable
    public ArrayList<AddressInfo> getSavedAddresses() {
        return savedAddresses;
    }

    public AddressInfo getBillingAddressInfo() {
        return billingAddressInfo;
    }

    public boolean isNewCard() {
        return newCard;
    }

    public void setNewCard(boolean newCard) {
        this.newCard = newCard;
        notifyPropertyChanged(BR.newCard);
    }

    public boolean getEnableAddButton() {
        return enableAddButton;
    }

    public void setEnableAddButton(boolean enableAddButton) {
        this.enableAddButton = enableAddButton;
        notifyPropertyChanged(BR.enableAddButton);
    }

    public boolean getEnableUpdateButton() {
        return enableUpdateButton;
    }

    public void setEnableUpdateButton(boolean enableUpdateButton) {
        this.enableUpdateButton = enableUpdateButton;
        notifyPropertyChanged(BR.enableUpdateButton);
    }

    private void checkAllRequiredFieldsValid() {
        if (newCard) {
            setEnableAddButton(creditCardInfo.areAllFieldsValid()
                    && !TextUtils.isEmpty(billingAddressInfo.getAddressId()));
        } else {
            setEnableUpdateButton(creditCardInfo.areAllFieldsValid()
                    && !TextUtils.isEmpty(billingAddressInfo.getAddressId()));
        }
    }

    public void onPrimaryClicked(boolean newCard, boolean checked) {
        if (mListener != null) {
            mListener.onPrimaryCheckChange(newCard, checked);
        }
    }

    @Transient
    private OnPropertyChangedCallback onFieldsChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            checkAllRequiredFieldsValid();
        }
    };

    interface OnPrimaryCheckChangeListener {
        void onPrimaryCheckChange(boolean newCard, boolean checked);
    }
}
