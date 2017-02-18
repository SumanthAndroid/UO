package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import java.util.List;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/1/16.
 * Class: CommerceGroupListener
 * Class Description: Listener for when Add to cart is clicked
 */
public interface CommerceGroupListener {
    void onAddToCartClicked(List<OrderItem> orderItems);

    void onMoreClicked(CommerceGroup group);

    void onSelectClicked(CommerceGroup group);
}
