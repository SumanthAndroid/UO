package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.domain.wallet.WalletFolioCard;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

/**
 * @author acampbell
 */
public class PaymentMethodItemViewModel extends BaseObservableWithLayoutItem {

    private WalletFolioCard walletFolioCard;

    @Bindable
    private String name;
    @Bindable
    private String lastFour;
    @Bindable
    @DrawableRes
    int imageRes;


    public PaymentMethodItemViewModel(@NonNull WalletFolioCard card) {
        setWalletFolioCard(card);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_manage_card;
    }

    public WalletFolioCard getWalletFolioCard() {
        return walletFolioCard;
    }

    public void setWalletFolioCard(@NonNull WalletFolioCard card) {
        this.walletFolioCard = card;
        setName(this.walletFolioCard.getName());
        setLastFour(this.walletFolioCard.getLastFour());
        setImageRes(getCreditCardDrawable(this.walletFolioCard.getType()));
    }

    @DrawableRes
    private int getCreditCardDrawable(String type) {
        if (type.equalsIgnoreCase("visa")) {
            return R.drawable.cc_visa;
        } else if (type.equalsIgnoreCase("amex")) {
            return R.drawable.cc_amex;
        } else if (type.equalsIgnoreCase("master")) {
            return R.drawable.cc_mastercard;
        } else if (type.equalsIgnoreCase("discover")) {
            return R.drawable.cc_discover;
        } else {
            return R.drawable.cc_amex;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getLastFour() {
        return lastFour;
    }

    public void setLastFour(String number) {
        this.lastFour = number;
        notifyPropertyChanged(BR.lastFour);
    }

    @DrawableRes
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
    }
}
