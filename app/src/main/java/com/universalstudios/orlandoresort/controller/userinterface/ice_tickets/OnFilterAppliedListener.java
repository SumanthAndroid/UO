package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import java.util.List;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/30/16.
 * Class: OnFilterAppliedListener
 * Class Description: Listener for when a filter gets applied
 */
public interface OnFilterAppliedListener {
    void onFilterApplied(List<CommerceCard> cards, String mCurrentServiceIdentifier);
}
