package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Adjustment;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Pricing;

/**
 * @author acampbell
 */
public class CartPromoAppliedItem extends BaseCartItem {

    private Pricing pricing;

    @Bindable
    private String promoCode;
    /**
     * Determines which message is shown, null = none, true = valid, false = invalid
     */
    @Bindable
    private Boolean validCode;
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setPromoCode(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public CartPromoAppliedItem(Pricing pricing) {
        setPricing(pricing);
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;

        if (pricing != null) {
            Adjustment adjustment = pricing.getFirstAdjustment();
            if (adjustment != null) {
                setPromoCode(adjustment.getCode());
                setValidCode(true);
            } else {
                setPromoCode(null);
                setValidCode(null);
            }
        } else {
            setPromoCode(null);
            setValidCode(null);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_promo_code_view;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
        notifyPropertyChanged(BR.promoCode);
    }

    public Boolean getValidCode() {
        return validCode;
    }

    public void setValidCode(Boolean validCode) {
        this.validCode = validCode;
        notifyPropertyChanged(BR.validCode);
    }
}