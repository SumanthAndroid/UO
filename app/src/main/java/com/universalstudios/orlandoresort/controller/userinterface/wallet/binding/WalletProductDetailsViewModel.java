package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

/**
 * @author tjudkins
 * @since 12/21/16
 */
public class WalletProductDetailsViewModel extends BaseObservable {

    @Bindable
    WalletEntitlementModel model;

    public WalletProductDetailsViewModel(WalletEntitlementModel model) {
        setModel(model);
    }

    public WalletEntitlementModel getModel() {
        return model;
    }

    private void setModel(WalletEntitlementModel model) {
        this.model = model;
    }

    public interface WalletProductDetailsCallback {
        void onDetailsClicked(WalletEntitlementModel walletEntitlementModel);
    }

}
