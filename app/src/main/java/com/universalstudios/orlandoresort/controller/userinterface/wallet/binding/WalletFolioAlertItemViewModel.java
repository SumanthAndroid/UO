package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioAlert;
import com.universalstudios.orlandoresort.model.state.account.AccountStateManager;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

/**
 * @author acampbell
 */
public class WalletFolioAlertItemViewModel extends BaseObservableWithLayoutItem {

    private final WalletFolioAlert walletFolioAlert;
    @Bindable
    private boolean expanded;
    @Bindable
    private String email;
    @Bindable
    private String phone;
    @Bindable
    private boolean emailChecked;
    @Bindable
    private boolean phoneChecked;

    public WalletFolioAlertItemViewModel(WalletFolioAlert walletFolioAlert) {
        this.walletFolioAlert = walletFolioAlert;
        setEmail(AccountStateManager.getUsername());
        Address primaryAddress = AccountStateManager.getPrimaryAddress();
        if (primaryAddress != null && !TextUtils.isEmpty(primaryAddress.getPhone())) {
            setPhone(primaryAddress.getPhone());
        }
        if (walletFolioAlert != null) {
            if (walletFolioAlert.getEmail() != null) {
                setEmailChecked(walletFolioAlert.getEmail());
            }
            if (walletFolioAlert.getText() != null) {
                setPhoneChecked(walletFolioAlert.getText());
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_wallet_folio_spending_alert;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public boolean isEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(boolean emailChecked) {
        this.emailChecked = emailChecked;
        notifyPropertyChanged(BR.emailChecked);
    }

    public boolean isPhoneChecked() {
        return phoneChecked;
    }

    public void setPhoneChecked(boolean phoneChecked) {
        this.phoneChecked = phoneChecked;
        notifyPropertyChanged(BR.phoneChecked);
    }
}
