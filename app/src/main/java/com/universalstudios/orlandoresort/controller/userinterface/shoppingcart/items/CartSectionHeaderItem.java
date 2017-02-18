package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Section header for shopping cart.
 * @author tjudkins
 * @since 10/12/16
 */

public class CartSectionHeaderItem extends BaseCartItem {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            CART_HEADER_TICKETS,
            CART_HEADER_EXPRESS_PASSES,
            CART_HEADER_ADDONS,
            CART_HEADER_DELIVERY_METHOD
    })
    public @interface CartHeaderType {}
    public static final int CART_HEADER_TICKETS = 0;
    public static final int CART_HEADER_EXPRESS_PASSES = 1;
    public static final int CART_HEADER_ADDONS = 2;
    public static final int CART_HEADER_DELIVERY_METHOD = 3;

    private final @CartHeaderType int headerType;
    private final TridionConfig tridion;

    public CartSectionHeaderItem(@CartHeaderType int headerType, TridionConfig tridionConfig) {
        this.headerType = headerType;
        this.tridion = tridionConfig;
    }

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_header_view;
    }

    @Bindable
    public @DrawableRes int getResIcon() {
        @DrawableRes int resIcon;
        // TODO Use icons from Tridion (when the path is well formed)
        switch (headerType) {
            case CART_HEADER_TICKETS:
                resIcon = R.drawable.icon_ticket;
                break;
            case CART_HEADER_EXPRESS_PASSES:
                resIcon = R.drawable.ic_detail_feature_express_pass;
                break;
            case CART_HEADER_ADDONS:
                resIcon = R.drawable.icon_addon;
                break;
            case CART_HEADER_DELIVERY_METHOD:
                resIcon = R.drawable.delivery_icon;
                break;
            default:
                resIcon = R.drawable.icon_ticket;
                break;
        }
        return resIcon;
    }

    @Bindable
    public String getHeaderText() {
        String headerText = "";
        switch (headerType) {
            case CART_HEADER_TICKETS:
                headerText = tridion.getTicketsLabel();
                break;
            case CART_HEADER_EXPRESS_PASSES:
                headerText = tridion.getExpressPassLabel();
                break;
            case CART_HEADER_ADDONS:
                headerText = tridion.getExtrasLabel();
                break;
            case CART_HEADER_DELIVERY_METHOD:
                headerText = tridion.getDeliveryMethodLabel();
                break;
        }
        return headerText;
    }

}
