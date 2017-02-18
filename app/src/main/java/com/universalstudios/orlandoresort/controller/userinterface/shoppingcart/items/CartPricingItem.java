package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.DisplayPricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.pricing.DisplayPricingModel;

/**
 * Pricing item for shopping cart.
 * @author tjudkins
 * @since 10/12/16
 */

public class CartPricingItem extends BaseCartItem {

    @Bindable
    private DisplayPricingModel pricingModel;

    public CartPricingItem(@NonNull DisplayPricing displayPricing) {
        setDisplayPricing(displayPricing);
    }

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_pricing_view;
    }

    public DisplayPricingModel getPricingModel() {
        return pricingModel;
    }

    private void setPricingModel(DisplayPricingModel pricingModel) {
        this.pricingModel = pricingModel;
        notifyPropertyChanged(BR.pricingModel);
    }

    public void setDisplayPricing(DisplayPricing displayPricing) {
        setPricingModel(new DisplayPricingModel(displayPricing));

    }

}
