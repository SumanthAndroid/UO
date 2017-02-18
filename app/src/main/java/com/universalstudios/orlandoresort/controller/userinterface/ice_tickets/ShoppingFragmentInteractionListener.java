package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import com.universalstudios.orlandoresort.controller.userinterface.addons.items.AddOnsShoppingProductItem;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;

/**
 * @author tjudkins
 * @since 11/1/16
 */

public interface ShoppingFragmentInteractionListener {
    void onCheckoutClicked();
    void onContinueClicked();
    void onBackClicked();
    void onSelectAddOnClicked(AddOnsShoppingProductItem extrasProductItem);
    void onSeeAddOnDetailsClicked(AddOnsShoppingProductItem extrasProductItem);
    void onSelectComboTicketClicked(PersonalizationExtrasProduct extrasProduct);
}

