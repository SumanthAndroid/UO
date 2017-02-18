package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.DeliveryOption;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

/**
 * Delivery option item for Shopping Cart.
 * @author tjudkins
 * @since 10/12/16
 */

public class CartDeliveryOptionItem extends BaseCartItem {

    private final TridionConfig tridion;

    @Bindable
    private boolean selected;
    @Bindable
    private DeliveryOption deliveryOption;

    public CartDeliveryOptionItem(TridionConfig tridion, DeliveryOption deliveryOption, boolean isSelected) {
        this.tridion = tridion;
        setDeliveryOption(deliveryOption);
        setSelected(isSelected);
    }

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_delivery_method_item;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public String getDeliveryLabelText() {
        return deliveryOption.getDescription();
    }

    public String getDeliveryAdditionalText() {
        return deliveryOption.getAdditionalDescription();
    }

    @Bindable
    public String getDeliveryCostText() {
        StringBuilder sb = new StringBuilder();
        if (deliveryOption.getAmount().doubleValue() > 0) {
            sb.append(IceTicketUtils.getTridionConfig().getFormattedPrice(deliveryOption.getAmount()));
        } else {
            sb.append(tridion.getFreeLabel());
        }
        return sb.toString();
    }

    public DeliveryOption getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOption deliveryOption) {
        this.deliveryOption = deliveryOption;
        notifyPropertyChanged(BR.deliveryOption);
    }
}
