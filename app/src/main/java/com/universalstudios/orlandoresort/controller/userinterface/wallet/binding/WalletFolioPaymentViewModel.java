package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;

/**
 * @author acampbell
 */
public class WalletFolioPaymentViewModel extends BaseObservable {

    @Bindable
    private boolean showLoading;
    @Bindable
    private boolean showEmpty;

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyPropertyChanged(BR.showLoading);
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    public void setShowEmpty(boolean showEmpty) {
        this.showEmpty = showEmpty;
        notifyPropertyChanged(BR.showEmpty);
    }
}
