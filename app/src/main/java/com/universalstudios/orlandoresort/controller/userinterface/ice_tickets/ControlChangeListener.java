package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import java.util.Date;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: ControlChangeListener
 * Class Description: ControlChangeListener used for calendar operations
 *
 * Edited by Tyler Ritchie
 */
public interface ControlChangeListener {
    void onControlIdentifierChanged(String identifier, Date selectedDate);
}
