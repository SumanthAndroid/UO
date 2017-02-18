package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import com.universalstudios.orlandoresort.view.TicketCounterView;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/31/16.
 * Class: OnCountsChangedListener
 * Class Description:  Listener for when the counter views values change
 */
public interface OnCountsChangedListener {

    void onCountChanged(List<TicketCounterView> counterViews, TicketCounterView viewChanged, int oldValue, int newValue, int totalOfAllViews);
}
