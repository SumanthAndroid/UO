package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;

/**
 * @author Steven Byle
 */
public class WalletFolioUpdatePinItemViewModel extends WalletFolioCreatePinItemViewModel {

    @Bindable
    private boolean expanded;

    public WalletFolioUpdatePinItemViewModel() {
        super();
        setExpanded(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_wallet_folio_update_pin;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

}
