package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.BaseCartItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartAddOnItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartDeliveryOptionItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartDividerItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartExpressPassTicketItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartFooterItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartPricingItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartPromoAppliedItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartPromoItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartSectionHeaderItem;
import com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items.CartTicketItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.DisplayPricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Pricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.DeliveryOption;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Offer;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.view.binding.DataBoundViewHolder;
import com.universalstudios.orlandoresort.view.binding.MultiTypeDataBoundAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * New and improved {@link ShoppingCartAdapter} that leverages data binding to simplify and
 * modularize the shopping cart. Each cart item is now a distinct class (see {@link CartTicketItem},
 * {@link CartPricingItem}, {@link CartDeliveryOptionItem} for examples) and layout that contain
 * their own view model for updating their own recycler view.
 * @author tjudkins
 * @since 10/11/16
 */

public final class ShoppingCartAdapter extends MultiTypeDataBoundAdapter {

    private TridionConfig mTridion;
    private ShoppingCartActionCallback mActionCallback;

    private CartPricingItem mPricingItem;
    private List<CartDeliveryOptionItem> mDeliveryOptionItems = new ArrayList<>();
    private CartFooterItem mFooterItem;
    private CartPromoAppliedItem mCartPromoAppliedItem;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TICKET_AGE_ADULT, TICKET_AGE_CHILD, TICKET_AGE_GENERAL})
    public @interface TicketAgeType {}
    public static final int TICKET_AGE_ADULT = 0;
    public static final int TICKET_AGE_CHILD = 1;
    public static final int TICKET_AGE_GENERAL = 2; // For add-ons

    public ShoppingCartAdapter(TridionConfig tridionConfig, ShoppingCartActionCallback actionCallback, Object... items) {
        super(items);
        mTridion = tridionConfig;
        mActionCallback = actionCallback;

    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        // this will work even if the layout does not have a callback parameter
        holder.binding.setVariable(BR.callback, mActionCallback);
        holder.binding.setVariable(BR.tridion, mTridion);
    }

    @Override
    public @LayoutRes int getItemLayoutId(int position) {
        // use layout ids as types
        Object item = getItem(position);

        if (item instanceof BaseCartItem) {
            return ((BaseCartItem) item).getLayoutId();
        }
        throw new IllegalArgumentException("unknown item type " + item);

    }

    @Override
    public void clear() {
        super.clear();
        mPricingItem = null;
        mFooterItem = null;
        if(mDeliveryOptionItems != null) {
            mDeliveryOptionItems.clear();
            mDeliveryOptionItems = null;
        }
        mCartPromoAppliedItem = null;
    }

    public void addParkTickets(List<ParkTicketGroups> parkTicketGroups, String orderId) {
        if (null != parkTicketGroups && !parkTicketGroups.isEmpty()) {
            addItem(new CartSectionHeaderItem(CartSectionHeaderItem.CART_HEADER_TICKETS, mTridion));

            for (int i = 0; i < parkTicketGroups.size(); i++) {
                ParkTicketGroups ticketGroups = parkTicketGroups.get(i);
                addItem(new CartTicketItem(orderId, ticketGroups));
                if (i < parkTicketGroups.size() - 1) {
                    addItem(new CartDividerItem());
                }
            }
        }
    }

    public void addExpressPassTickets(List<ExpressPassTicketGroups> expressPassTicketGroups, String orderId) {
        if (null != expressPassTicketGroups && !expressPassTicketGroups.isEmpty()) {
            addItem(new CartSectionHeaderItem(CartSectionHeaderItem.CART_HEADER_EXPRESS_PASSES, mTridion));

            for (int i = 0; i < expressPassTicketGroups.size(); i++) {
                ExpressPassTicketGroups ticketGroups = expressPassTicketGroups.get(i);
                addItem(new CartExpressPassTicketItem(orderId, ticketGroups));
                if (i < expressPassTicketGroups.size() - 1) {
                    addItem(new CartDividerItem());
                }
            }
        }
    }

    public void addAddOnTickets(HashMap<String, List<AddOnTicketGroups>> addOnsMap, String orderId) {
        List<AddOnTicketGroups> addOnTicketGroups = new ArrayList<>();
        for (List<AddOnTicketGroups> addOnsMapValues : addOnsMap.values()) {
            if (null != addOnsMapValues) {
                addOnTicketGroups.addAll(addOnsMapValues);
            }
        }

        if (!addOnTicketGroups.isEmpty()) {
            addItem(new CartSectionHeaderItem(CartSectionHeaderItem.CART_HEADER_ADDONS, mTridion));

            for (int i = 0; i < addOnTicketGroups.size(); i++) {
                AddOnTicketGroups ticketGroups = addOnTicketGroups.get(i);
                addItem(new CartAddOnItem(orderId, ticketGroups));
                if (i < addOnTicketGroups.size() - 1) {
                    addItem(new CartDividerItem());
                }
            }
        }
    }

    public void addPromoItem(Offer offer) {
        if (offer != null && offer.getSku() != null) {
            addItem(new CartPromoItem(offer));
        }
    }

    public void setPromoCodeEntry(Pricing pricing) {
        if (mCartPromoAppliedItem == null) {
            mCartPromoAppliedItem = new CartPromoAppliedItem(pricing);
            addItem(mCartPromoAppliedItem);
        } else {
            mCartPromoAppliedItem.setPricing(pricing);
        }
    }

    public void setPromoCodeValid(boolean valid) {
        if (mCartPromoAppliedItem != null) {
            mCartPromoAppliedItem.setValidCode(valid);
        }
    }

    public void setPricing(DisplayPricing displayPricing) {
        if (null == mPricingItem) {
            mPricingItem = new CartPricingItem(displayPricing);
            addItem(mPricingItem);
        } else {
            mPricingItem.setDisplayPricing(displayPricing);
        }
    }
    
    public void setFooter(boolean checkoutButtonEnabled) {
        if (null == mFooterItem) {
            mFooterItem = new CartFooterItem(checkoutButtonEnabled);
            addItem(mFooterItem);
        } else {
            mFooterItem.setCheckoutButtonEnabled(checkoutButtonEnabled);
        }
    }

    public void setDeliveryOptions(List<DeliveryOption> deliveryOptions, String selectedId) {
        if (null == deliveryOptions || deliveryOptions.size() <= 0 || TextUtils.isEmpty(selectedId)) return;
        if (null == mDeliveryOptionItems) {
            // No delivery options, so add them
            addItem(new CartSectionHeaderItem(CartSectionHeaderItem.CART_HEADER_DELIVERY_METHOD, mTridion));
            mDeliveryOptionItems = new ArrayList<>(deliveryOptions.size());
            for (DeliveryOption deliveryOption : deliveryOptions) {
                boolean isSelected = selectedId.equals(deliveryOption.getId());
                CartDeliveryOptionItem deliveryOptionItem = new CartDeliveryOptionItem(mTridion, deliveryOption, isSelected);
                mDeliveryOptionItems.add(deliveryOptionItem);
                addItem(deliveryOptionItem);
            }

        } else {
            // There are already delivery options, so just update the selected one
            setSelectedDeliveryOption(selectedId);
        }
    }

    public void setSelectedDeliveryOption(String selectedId) {
        for (CartDeliveryOptionItem optionItem : mDeliveryOptionItems) {
            boolean isSelected = selectedId.equals(optionItem.getDeliveryOption().getId());
            optionItem.setSelected(isSelected);
        }
    }

    /**
     * Callbacks that need to be implemented the fragment that leverages the {@link ShoppingCartAdapter}.
     * The adapter will automagically bind the callbacks to the recyclerviews to leverage the callbacks
     * using data binding.
     */
    public interface ShoppingCartActionCallback {
        void onRemoveClicked(Ticket adultTicket, Ticket childTicket, String orderId);
        void onRemoveSameItem(Ticket ticket, String orderId);
        void onAddSameItem(ParkTicketGroups parkTicketGroups, @TicketAgeType int ticketAgeType);
        void onAddSameItem(AddOnTicketGroups addOnTicketGroups, @TicketAgeType int ticketAgeType);
        void onRemoveClicked(ExpressPassTicketGroups expressTicket, String orderId);
        void onRemoveSameItem(ExpressPassTicketGroups expressTicket, String orderId);
        void onAddSameItem(ExpressPassTicketGroups expressTicket, String orderId);
        void onRemoveClicked(AddOnTicketGroups addOnTicket, String orderId);
        void onContinueShoppingClicked();
        void onCheckoutClicked();
        void onDeliveryOptionSelected(DeliveryOption deliveryOption);
        void onAddPromoCodeToCart(String promoCode);
        void onAddPromoItemToCart(Offer offer);
    }

}
