package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;

/**
 * @author tjudkins
 * @since 12/21/16
 */
public class WalletViewModel extends BaseObservable {

    @Bindable
    boolean showLoading;
    @Bindable
    boolean showEntitlements;
    @Bindable
    boolean showEmptyView;
    @Bindable
    boolean showExpiredEntitlements;

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyPropertyChanged(BR.showLoading);
    }

    public boolean isShowEntitlements() {
        return showEntitlements;
    }

    public void setShowEntitlements(boolean showEntitlements) {
        this.showEntitlements = showEntitlements;
        notifyPropertyChanged(BR.showEntitlements);
    }

    public boolean isShowEmptyView() {
        return showEmptyView;
    }

    public void setShowEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
        notifyPropertyChanged(BR.showEmptyView);
    }

    public boolean isShowExpiredEntitlements() {
        return showExpiredEntitlements;
    }

    public void setShowExpiredEntitlements(boolean showExpiredEntitlements) {
        this.showExpiredEntitlements = showExpiredEntitlements;
        notifyPropertyChanged(BR.showExpiredEntitlements);
    }

    public interface WalletCallback {
        void onLinkTicketClicked();
        void onViewExpiredClicked();
        void onShopNowClicked();
    }
}
