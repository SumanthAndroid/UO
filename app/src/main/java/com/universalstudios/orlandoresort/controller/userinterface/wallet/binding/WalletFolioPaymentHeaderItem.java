package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;
import android.support.annotation.IntDef;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author acampbell
 */
public class WalletFolioPaymentHeaderItem extends BaseObservableWithLayoutItem {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HEADER_TYPE_PAYMENTS,
            HEADER_TYPE_SPENDING_LIMITS,
            HEADER_TYPE_TRANSACTIONS})
    public @interface HeaderType {
    }

    // Declare the constants
    public static final int HEADER_TYPE_PAYMENTS = 0;
    public static final int HEADER_TYPE_SPENDING_LIMITS = 1;
    public static final int HEADER_TYPE_TRANSACTIONS = 2;

    @HeaderType
    private int type;
    @Bindable
    private String title;
    @Bindable
    private String buttonText;

    public WalletFolioPaymentHeaderItem(@HeaderType int type, String title, String buttonText) {
        this.type = type;
        setTitle(title);
        setButtonText(buttonText);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_wallet_folio_payment_header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
        notifyPropertyChanged(BR.buttonText);
    }

    public int getType() {
        return type;
    }
}
