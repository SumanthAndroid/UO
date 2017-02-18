package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author tjudkins
 * @since 12/21/16
 */
@Parcel
public class WalletProductItemViewModel extends BaseObservableWithLayoutItem {

    @Bindable
    WalletEntitlementModel model;
    @Bindable
    boolean expanded;

    @ParcelConstructor
    public WalletProductItemViewModel(WalletEntitlementModel model) {
        setModel(model);
    }

    public WalletEntitlementModel getModel() {
        return model;
    }

    private void setModel(WalletEntitlementModel model) {
        this.model = model;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_product_wallet_entitlement;
    }

    public interface WalletProductItemCallback {
        void onExpandItemClicked(WalletProductItemViewModel walletProductItemViewModel);
        void onMoreInfoClicked(WalletEntitlementModel walletEntitlementModel);
    }

}
