package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.OrderItemGroups;

/**
 * Footer that contains the checkout and continue buttons for Shopping Cart.
 * @author tjudkins
 * @since 10/12/16
 */

public class CartFooterItem extends BaseCartItem {

    @Bindable
    private boolean checkoutButtonEnabled;

    public CartFooterItem(boolean checkoutButtonEnabled) {
        setCheckoutButtonEnabled(checkoutButtonEnabled);
    }

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_footer_view;
    }

    public boolean isCheckoutButtonEnabled() {
        return checkoutButtonEnabled;
    }

    public void setCheckoutButtonEnabled(boolean checkoutButtonEnabled) {
        this.checkoutButtonEnabled = checkoutButtonEnabled;
    }
}
