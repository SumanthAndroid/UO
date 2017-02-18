package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/11/16.
 * Class: TicketAssignmentUtils
 * Class Description: Utils class to help with ticket assignment
 */
public class TicketAssignmentUtils {

    private static TicketAssignmentUtils sInstance = null;
    private boolean primarySelected = false;
    private PartyMember primaryMember = null;

    private TicketAssignmentUtils() {
    }//To prevent manual instantiation

    private TicketGroupOrder mTicketGroupOrder;

    public static TicketAssignmentUtils instance() {
        synchronized (TicketAssignmentUtils.class) {
            if (null == sInstance) {
                sInstance = new TicketAssignmentUtils();
            }

            return sInstance;
        }
    }

    public TicketGroupOrder getTicketGroupOrder() {
        return mTicketGroupOrder;
    }

    public void setTicketGrouping(TicketGroupOrder order) {
        mTicketGroupOrder = order;
    }

    public List<AssignableTicketItem> getAdultTicketOptionsForGuest(PartyMember guest) {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        List<ParkTicketGroups> parkTicketGroupList = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            if (mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups());
            }
            if (mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups());
            }
        }
        for (ParkTicketGroups parkTicketGroups : parkTicketGroupList) {
            if (parkTicketGroups != null && parkTicketGroups.getAdultTickets() != null) {
                Ticket ticket = parkTicketGroups.getAdultTickets();
                // Check if ticket has an unassigned orderItem
                boolean unAssigned = ticket.hasUnassignedOrderItem();
                boolean assignedToGuest = isGroupAssignedToGuest(ticket, guest);
                if (assignedToGuest) {
                    AssignableTicketItem displayItem = AssignableTicketItem.createAdultAssignableTicketItem(parkTicketGroups, guest);
                    displayItem.setSelected(true);
                    unassignedTickets.add(displayItem);
                } else if (unAssigned) {
                    AssignableTicketItem displayItem = AssignableTicketItem.createAdultAssignableTicketItem(parkTicketGroups, guest);
                    unassignedTickets.add(displayItem);
                }
            }
        }

        return unassignedTickets;
    }

    public List<AssignableTicketItem> getChildTicketOptionsForGuest(PartyMember guest) {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        List<ParkTicketGroups> parkTicketGroupList = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            if (mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups());
            }
            if (mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups());
            }
        }
        for (ParkTicketGroups parkTicketGroups : parkTicketGroupList) {
            if (parkTicketGroups != null && parkTicketGroups.getChildTickets() != null) {
                Ticket ticket = parkTicketGroups.getChildTickets();
                // Check if ticket has an unassigned orderItem
                boolean unAssigned = ticket.hasUnassignedOrderItem();
                boolean assignedToGuest = isGroupAssignedToGuest(ticket, guest);
                if (assignedToGuest) {
                    AssignableTicketItem displayItem = AssignableTicketItem.createChildAssignableTicketItem(parkTicketGroups, guest);
                    displayItem.setSelected(true);
                    unassignedTickets.add(displayItem);
                } else if (unAssigned) {
                    AssignableTicketItem displayItem = AssignableTicketItem.createChildAssignableTicketItem(parkTicketGroups, guest);
                    unassignedTickets.add(displayItem);
                }
            }
        }

        return unassignedTickets;
    }

    public List<AssignableTicketItem> getUEPTicketOptionsForGuest(PartyMember guest) {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null
                && mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups() != null) {
            for (ExpressPassTicketGroups expressPassTicketGroup : mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups()) {
                boolean unAssigned = expressPassTicketGroup.hasUnassignedOrderItem();
                boolean assignedToGuest = isUEPGroupAssignedToGuest(expressPassTicketGroup, guest);
                if (assignedToGuest) {
                    AssignableTicketItem displayItem = new AssignableTicketItem(expressPassTicketGroup);
                    displayItem.setSelected(true);
                    unassignedTickets.add(displayItem);
                } else if (unAssigned) {
                    AssignableTicketItem displayItem = new AssignableTicketItem(expressPassTicketGroup);
                    unassignedTickets.add(displayItem);
                }
            }
        }

        return unassignedTickets;
    }

    public List<AssignableTicketItem> getAddOnOptionsForGuest(PartyMember guest) {
        List<AssignableTicketItem> assignableTicketItems = new ArrayList<>();
        if (mTicketGroupOrder == null || mTicketGroupOrder.getOrderItemGroups() == null) {
            return  assignableTicketItems;
        }
        List<AddOnTicketGroups> addOnTicketGroups = mTicketGroupOrder.getOrderItemGroups().getAddOnTicketGroups();
        for (AddOnTicketGroups addOnTicketGroup : addOnTicketGroups) {
            if (addOnTicketGroup != null) {
                boolean unAssigned;
                // Adult addons
                if (guest.ageType == PartyMember.AGE_TYPE_ADULT && addOnTicketGroup.getAdultAddOns() != null) {
                    unAssigned = addOnTicketGroup.getAdultAddOns().hasUnassignedOrderItem();
                    boolean assignedToGuest = isGroupAssignedToGuest(addOnTicketGroup.getAdultAddOns(), guest);
                    if (assignedToGuest) {
                        AssignableTicketItem displayItem = AssignableTicketItem.createAdultAssignableTicketItem(addOnTicketGroup);
                        displayItem.setSelected(true);
                        assignableTicketItems.add(displayItem);
                    } else if (unAssigned) {
                        AssignableTicketItem displayItem = AssignableTicketItem.createAdultAssignableTicketItem(addOnTicketGroup);
                        assignableTicketItems.add(displayItem);
                    }
                }

                // Child addons
                if (guest.ageType == PartyMember.AGE_TYPE_CHILD && addOnTicketGroup.getChildAddOns() != null) {
                    unAssigned = addOnTicketGroup.getChildAddOns().hasUnassignedOrderItem();
                    boolean assignedToGuest = isGroupAssignedToGuest(addOnTicketGroup.getChildAddOns(), guest);
                    if (assignedToGuest) {
                        AssignableTicketItem displayItem = AssignableTicketItem.createChildAssignableTicketItem(addOnTicketGroup);
                        displayItem.setSelected(true);
                        assignableTicketItems.add(displayItem);
                    } else if (unAssigned) {
                        AssignableTicketItem displayItem = AssignableTicketItem.createChildAssignableTicketItem(addOnTicketGroup);
                        assignableTicketItems.add(displayItem);
                    }
                }

                // All-age addons
                if (addOnTicketGroup.getAllAddOns() != null) {
                    unAssigned = addOnTicketGroup.getAllAddOns().hasUnassignedOrderItem();
                    boolean assignedToGuest = isGroupAssignedToGuest(addOnTicketGroup.getAllAddOns(), guest);
                    if (assignedToGuest) {
                        AssignableTicketItem displayItem = AssignableTicketItem.createGeneralAssignableTicketItem(addOnTicketGroup);
                        displayItem.setSelected(true);
                        assignableTicketItems.add(displayItem);
                    } else if (unAssigned) {
                        AssignableTicketItem displayItem = AssignableTicketItem.createGeneralAssignableTicketItem(addOnTicketGroup);
                        assignableTicketItems.add(displayItem);
                    }
                }
            }
        }

        return assignableTicketItems;
    }

    public List<OrderItem> getOrderItemsAssignedToGuest(PartyMember guest) {
        List<OrderItem> unassignedTickets = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            List<ParkTicketGroups> parkTicketGroupList = new ArrayList<>();
            if (mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups());
            }
            if (mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups());
            }
            // Park tickets and annual passes
            for (ParkTicketGroups parkTicketGroups : parkTicketGroupList) {
                Ticket ticket;
                if (guest.ageType == PartyMember.AGE_TYPE_ADULT) {
                    ticket = parkTicketGroups.getAdultTickets();
                } else {
                    ticket = parkTicketGroups.getChildTickets();
                }

                if (ticket != null && ticket.getOrderItems() != null && ticket.getOrderItems().size() > 0) {
                    for (OrderItem orderItem : ticket.getOrderItems()) {
                        if (isOrderItemAssignedToGuest(orderItem, guest)) {
                            unassignedTickets.add(orderItem);
                        }
                    }
                }
            }

            // express passes
            if (mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups().size() > 0) {
                for (ExpressPassTicketGroups expressPassTicketGroups : mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups()) {
                    if (expressPassTicketGroups != null && expressPassTicketGroups.getOrderItems() != null && expressPassTicketGroups.getOrderItems().size() > 0) {
                        for (OrderItem orderItem : expressPassTicketGroups.getOrderItems()) {
                            if (isOrderItemAssignedToGuest(orderItem, guest)) {
                                unassignedTickets.add(orderItem);
                            }
                        }
                    }
                }
            }

            // Add-ons
            List<AddOnTicketGroups> addOnTicketGroups = mTicketGroupOrder.getOrderItemGroups().getAddOnTicketGroups();
            for (AddOnTicketGroups addOnTicketGroup : addOnTicketGroups) {
                if (addOnTicketGroup != null) {
                    // Adult add-ons
                    if (addOnTicketGroup.getAdultAddOns() != null && addOnTicketGroup.getAdultAddOns().getOrderItems() != null) {
                        for (OrderItem orderItem : addOnTicketGroup.getAdultAddOns().getOrderItems()) {
                            if (isOrderItemAssignedToGuest(orderItem, guest)) {
                                unassignedTickets.add(orderItem);
                            }
                        }
                    }
                    // Child add-ons
                    if (addOnTicketGroup.getChildAddOns() != null && addOnTicketGroup.getChildAddOns().getOrderItems() != null) {
                        for (OrderItem orderItem : addOnTicketGroup.getChildAddOns().getOrderItems()) {
                            if (isOrderItemAssignedToGuest(orderItem, guest)) {
                                unassignedTickets.add(orderItem);
                            }
                        }
                    }
                    // General add-ons
                    if (addOnTicketGroup.getAllAddOns() != null && addOnTicketGroup.getAllAddOns().getOrderItems() != null) {
                        for (OrderItem orderItem : addOnTicketGroup.getAllAddOns().getOrderItems()) {
                            if (isOrderItemAssignedToGuest(orderItem, guest)) {
                                unassignedTickets.add(orderItem);
                            }
                        }
                    }
                }
            }
        }

        return unassignedTickets;
    }

    public static boolean isGroupAssignedToGuest(Ticket groupTicket, PartyMember guest) {
        boolean nameMatchFound = false;
        if (groupTicket != null) {
            for (OrderItem orderItem : groupTicket.getOrderItems()) {
                for (CommerceAttribute attribute : orderItem.getAttributes()) {
                    if (null != attribute &&
                            ((attribute.isAssignmentFirstName()
                                    && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.firstname))
                                    || (attribute.isAssignmentLastName()
                                    && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.lastname)))) {

                        if (nameMatchFound) {
                            return true;
                        } else {
                            nameMatchFound = true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isGroupAssignedToGuest(ExpressPassTicketGroups expressPassTicketGroup, PartyMember guest) {
        boolean nameMatchFound = false;
        for (OrderItem orderItem : expressPassTicketGroup.getOrderItems()) {
            for (CommerceAttribute attribute : orderItem.getAttributes()) {
                if (null != attribute &&
                        ((attribute.isAssignmentFirstName()
                                && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.firstname) )
                                || (attribute.isAssignmentLastName()
                                && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.lastname)))) {

                    if (nameMatchFound) {
                        return true;
                    } else {
                        nameMatchFound = true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isUEPGroupAssignedToGuest(ExpressPassTicketGroups t, PartyMember guest) {
        boolean nameMatchFound = false;
        for (OrderItem orderItem : t.getOrderItems()) {
            for (CommerceAttribute attribute : orderItem.getAttributes()) {
                if (null != attribute &&
                        ((attribute.isAssignmentFirstName()
                                && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.firstname) )
                                || (attribute.isAssignmentLastName()
                                && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.lastname)))) {

                    if (nameMatchFound) {
                        return true;
                    } else {
                        nameMatchFound = true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @deprecated use {@link TicketGroupOrder#getAssignedTicketAdults()}
     */
    @Deprecated
    @NonNull
    public Map<String, List<OrderItem>> getAssignedTicketAdults() {
        if (mTicketGroupOrder != null) {
            return mTicketGroupOrder.getAssignedTicketAdults();
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * @deprecated use {@link TicketGroupOrder#getAssignedTicketsChild()}
     */
    @Deprecated
    @NonNull
    public Map<String, List<OrderItem>> getAssignedTicketsChild() {
        if (mTicketGroupOrder != null) {
            return mTicketGroupOrder.getAssignedTicketsChild();
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * This clears the current user from the tickets thet are assigned to locally. Should only be used for clearing the item up to be re-assigned
     * @param guest
     */
    public void clearOrderItemsForGuest(PartyMember guest) {
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            //first clear the adult and child associated tickets
            List<ParkTicketGroups> parkTicketGroupList = new ArrayList<>();
            if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
                if (mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups() != null) {
                    parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups());
                }
                if (mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                    parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups());
                }
            }
            for (ParkTicketGroups group : parkTicketGroupList) {
                if (group.getAdultTickets() != null) {
                    if (isGroupAssignedToGuest(group.getAdultTickets(), guest)) {
                        //clear the ticket
                        clearOrderItemsOfGuest(group.getAdultTickets().getOrderItems(), guest);
                    }
                }

                if (group.getChildTickets() != null) {
                    if (isGroupAssignedToGuest(group.getChildTickets(), guest)) {
                        //clear the ticket
                        clearOrderItemsOfGuest(group.getChildTickets().getOrderItems(), guest);
                    }
                }
            }

            //now clear the express tickets
            if(mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups() != null){
                List<ExpressPassTicketGroups> expressPassTicketGroupsList= mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups();
                if(!expressPassTicketGroupsList.isEmpty()) {
                    for(ExpressPassTicketGroups group : expressPassTicketGroupsList) {
                        if(group != null) {
                            if (isGroupAssignedToGuest(group, guest)) {
                                //clear the ticket
                                clearOrderItemsOfGuest(group.getOrderItems(), guest);
                            }
                        }
                    }
                }

            }

            //TODO this also needs to be done for addons as well

        }

    }

    public void clearOrderItemsOfGuest(List<OrderItem> orderItems, PartyMember guest) {
        if(orderItems == null || orderItems.isEmpty()) {
            return;
        }

        for(OrderItem orderItem : orderItems) {
            for (CommerceAttribute attribute : orderItem.getAttributes()) {
                if (null != attribute) {
                    if (attribute.isAssignmentFirstName()
                            && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.firstname)) {
                        attribute.setValue("");
                    } else if (attribute.isAssignmentLastName()
                            && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.lastname)) {
                        attribute.setValue("");
                    }
                }
            }
        }
    }

    public static OrderItem getFirstUnassignedOrderItem(ParkTicketGroups group, PartyMember guest) {
        boolean isAdult = guest.ageType == PartyMember.AGE_TYPE_ADULT;

        Ticket ticket = isAdult ? group.getAdultTickets() : group.getChildTickets();
        if (isGroupAssignedToGuest(ticket, guest)) {
            return null;
        }

        for (OrderItem orderItem : ticket.getOrderItems()) {
            if (!isOrderItemAssigned(orderItem)) {
                return orderItem;
            }
        }

        return null;
    }

    public static OrderItem getFirstUnassignedOrderItem(ExpressPassTicketGroups group, PartyMember guest) {

        if (isGroupAssignedToGuest(group, guest)) {
            return null;
        }

        for (OrderItem orderItem : group.getOrderItems()) {
            if (!isOrderItemAssigned(orderItem)) {
                return orderItem;
            }
        }

        return null;
    }

    public static OrderItem getFirstUnassignedOrderItem(AddOnTicketGroups group, PartyMember guest) {
        if (group == null || guest == null) {
            return null;
        }

        if (guest.ageType == PartyMember.AGE_TYPE_ADULT && group.getAdultAddOns() != null
                && group.getAdultAddOns().getOrderItems() != null
                && !isGroupAssignedToGuest(group.getAdultAddOns(), guest)) {
            for (OrderItem orderItem : group.getAdultAddOns().getOrderItems()) {
                if (!isOrderItemAssigned(orderItem)) {
                    return orderItem;
                }
            }
        } else if (guest.ageType == PartyMember.AGE_TYPE_CHILD && group.getChildAddOns() != null
                && group.getChildAddOns().getOrderItems() != null
                && !isGroupAssignedToGuest(group.getChildAddOns(), guest)) {
            for (OrderItem orderItem : group.getChildAddOns().getOrderItems()) {
                if (!isOrderItemAssigned(orderItem)) {
                    return orderItem;
                }
            }
        }

        if (group.getAllAddOns() != null && group.getAllAddOns().getOrderItems() != null
                && !isGroupAssignedToGuest(group.getAllAddOns(), guest)) {
            for (OrderItem orderItem : group.getAllAddOns().getOrderItems()) {
                if (!isOrderItemAssigned(orderItem)) {
                    return orderItem;
                }
            }
        }

        return null;
    }

    public static OrderItem getFirstUnassignedOrderItem(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            if (!isOrderItemAssigned(item)) {
                return item;
            }
        }
        return null;
    }

    // TODO: replace names
    public static boolean isOrderItemAssignedToGuest(OrderItem orderItem, PartyMember guest) {
        boolean nameMatchFound = false;
        for (CommerceAttribute attribute : orderItem.getAttributes()) {
            if (null != attribute &&
                    ((attribute.isAssignmentFirstName()
                            && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.firstname))
                            || (attribute.isAssignmentLastName()
                            && !TextUtils.isEmpty(attribute.getValue()) && attribute.getValue().equals(guest.lastname)))) {
                if (nameMatchFound) {
                    return true;
                } else {
                    nameMatchFound = true;
                }
            }
        }
        return false;
    }

    public static boolean isOrderItemAssigned(OrderItem orderItem) {
        return orderItem != null && !TextUtils.isEmpty(orderItem.getAssignedName());
    }

    public List<AssignableTicketItem> getUnassignedAdultTickets() {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        List<ParkTicketGroups> parkTicketGroupList = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            if (mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups());
            }
            if (mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups());
            }
        }
        for (ParkTicketGroups parkTicketGroups : parkTicketGroupList) {
            if (parkTicketGroups != null) {
                List<OrderItem> unassignedOrderItems = new ArrayList<>();
                Ticket ticket = parkTicketGroups.getAdultTickets();
                if (ticket != null && ticket.getOrderItems() != null) {
                    for (OrderItem orderItem : ticket.getOrderItems()) {
                        if (orderItem != null && !orderItem.isAssigned()) {
                            unassignedOrderItems.add(orderItem);
                        }
                    }
                    if (!unassignedOrderItems.isEmpty()) {
                        AssignableTicketItem ticketItem = AssignableTicketItem.createAdultAssignableTicketItem(parkTicketGroups, null);
                        ticketItem.setUnAssignedOrderItems(unassignedOrderItems);
                        unassignedTickets.add(ticketItem);
                    }
                }
            }
        }

        return unassignedTickets;
    }

    public List<AssignableTicketItem> getUnassignedChildTickets() {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        List<ParkTicketGroups> parkTicketGroupList = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            if (mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getParkTicketGroups());
            }
            if (mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
                parkTicketGroupList.addAll(mTicketGroupOrder.getOrderItemGroups().getAnnualPassParkTicketGroups());
            }
        }
        for (ParkTicketGroups parkTicketGroups : parkTicketGroupList) {
            if (parkTicketGroups != null) {
                List<OrderItem> unassignedOrderItems = new ArrayList<>();
                Ticket ticket = parkTicketGroups.getChildTickets();
                if (ticket != null && ticket.getOrderItems() != null) {
                    for (OrderItem orderItem : ticket.getOrderItems()) {
                        if (orderItem != null && !orderItem.isAssigned()) {
                            unassignedOrderItems.add(orderItem);
                        }
                    }
                    if (!unassignedOrderItems.isEmpty()) {
                        AssignableTicketItem ticketItem = AssignableTicketItem.createChildAssignableTicketItem(parkTicketGroups, null);
                        ticketItem.setUnAssignedOrderItems(unassignedOrderItems);
                        unassignedTickets.add(ticketItem);
                    }
                }
            }
        }

        return unassignedTickets;
    }

    public List<AssignableTicketItem> getUnassignedUEPTickets() {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null
                && mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups() != null) {
            for (ExpressPassTicketGroups expressPassTicketGroup : mTicketGroupOrder.getOrderItemGroups().getExpressPassGroups()) {
                if (expressPassTicketGroup != null && expressPassTicketGroup.getOrderItems() != null) {
                    List<OrderItem> unassignedOrderItems = new ArrayList<>();
                    for (OrderItem orderItem : expressPassTicketGroup.getOrderItems()) {
                        if (orderItem != null && !orderItem.isAssigned()) {
                             unassignedOrderItems.add(orderItem);
                        }
                    }
                    if (!unassignedOrderItems.isEmpty()) {
                        AssignableTicketItem ticketItem = new AssignableTicketItem(expressPassTicketGroup);
                        ticketItem.setUnAssignedOrderItems(unassignedOrderItems);
                        unassignedTickets.add(ticketItem);
                    }
                }
            }
        }

        return unassignedTickets;
    }

    public List<AssignableTicketItem> getUnassignedAddOnTickets() {
        List<AssignableTicketItem> unassignedTickets = new ArrayList<>();
        if (mTicketGroupOrder != null && mTicketGroupOrder.getOrderItemGroups() != null) {
            List<AddOnTicketGroups> addOnTicketGroups = mTicketGroupOrder.getOrderItemGroups().getAddOnTicketGroups();
            for (AddOnTicketGroups addOnTicketGroup : addOnTicketGroups) {
                if (addOnTicketGroup != null) {
                    // Adult add-ons
                    List<OrderItem> unassignedOrderItems = new ArrayList<>();
                    if (addOnTicketGroup.getAdultAddOns() != null && addOnTicketGroup.getAdultAddOns().getOrderItems() != null) {
                        for (OrderItem orderItem : addOnTicketGroup.getAdultAddOns().getOrderItems()) {
                            if (orderItem != null && !orderItem.isAssigned()) {
                                unassignedOrderItems.add(orderItem);
                            }
                        }
                        if (!unassignedOrderItems.isEmpty()) {
                            AssignableTicketItem ticketItem = AssignableTicketItem.createAdultAssignableTicketItem(addOnTicketGroup);
                            ticketItem.setUnAssignedOrderItems(unassignedOrderItems);
                            unassignedTickets.add(ticketItem);
                        }
                    }
                    // Child add-ons
                    unassignedOrderItems = new ArrayList<>();
                    if (addOnTicketGroup.getChildAddOns() != null && addOnTicketGroup.getChildAddOns().getOrderItems() != null) {
                        for (OrderItem orderItem : addOnTicketGroup.getChildAddOns().getOrderItems()) {
                            if (orderItem != null && !orderItem.isAssigned()) {
                                unassignedOrderItems.add(orderItem);
                            }
                        }
                        if (!unassignedOrderItems.isEmpty()) {
                            AssignableTicketItem ticketItem = AssignableTicketItem.createChildAssignableTicketItem(addOnTicketGroup);
                            ticketItem.setUnAssignedOrderItems(unassignedOrderItems);
                            unassignedTickets.add(ticketItem);
                        }
                    }
                    // General add-ons
                    unassignedOrderItems = new ArrayList<>();
                    if (addOnTicketGroup.getAllAddOns() != null && addOnTicketGroup.getAllAddOns().getOrderItems() != null) {
                        for (OrderItem orderItem : addOnTicketGroup.getAllAddOns().getOrderItems()) {
                            if (orderItem != null && !orderItem.isAssigned()) {
                                unassignedOrderItems.add(orderItem);
                            }
                        }
                        if (!unassignedOrderItems.isEmpty()) {
                            AssignableTicketItem ticketItem = AssignableTicketItem.createGeneralAssignableTicketItem(addOnTicketGroup);
                            ticketItem.setUnAssignedOrderItems(unassignedOrderItems);
                            unassignedTickets.add(ticketItem);
                        }
                    }
                }
            }
        }

        return unassignedTickets;
    }

    public static boolean primarySelected() {
        return instance().primarySelected;
    }

    public static boolean primaryAlreadySelected() {
        return null != instance().primaryMember;
    }

    public static boolean primaryMemberSelected(PartyMember partyMember) {
        return null != instance().primaryMember &&
                instance().primaryMember.doNamesMatch(partyMember);
    }

    public static void setPrimarySelected(boolean primarySelected) {
        instance().primarySelected = primarySelected;
        if(!primarySelected){
            setPrimaryName(null);
        }
    }

    public static void setPrimaryName(PartyMember partyMember) {
        instance().primaryMember = partyMember;
    }

    public static PartyMember getPrimaryName(){
        return instance().primaryMember;
    }

    public static void clearPrimarySelected() {
        setPrimarySelected(false);
        setPrimaryName(null);
    }
}
