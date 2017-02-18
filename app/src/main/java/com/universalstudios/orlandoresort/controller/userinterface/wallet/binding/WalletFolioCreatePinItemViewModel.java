package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

/**
 * @author Steven Byle
 */
public class WalletFolioCreatePinItemViewModel extends BaseObservableWithLayoutItem {

    protected String enteredPin;

    @Bindable
    protected boolean enteredPinValidLength;

    public WalletFolioCreatePinItemViewModel() {
        super();
        setEnteredPin(null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_wallet_folio_create_pin;
    }

    public TextWatcher pinTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            setEnteredPin(s.toString());
        }
    };

    public void setEnteredPin(String enteredPin) {
        this.enteredPin = enteredPin;
        setEnteredPinValidLength(enteredPin != null && enteredPin.length() == 4);
    }

    public String getEnteredPin() {
        return enteredPin;
    }

    public boolean isEnteredPinValidLength() {
        return enteredPinValidLength;
    }

    public void setEnteredPinValidLength(boolean enteredPinValidLength) {
        this.enteredPinValidLength = enteredPinValidLength;
        notifyPropertyChanged(BR.enteredPinValidLength);
    }
}
