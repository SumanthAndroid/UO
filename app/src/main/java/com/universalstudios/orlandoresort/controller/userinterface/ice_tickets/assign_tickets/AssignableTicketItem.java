package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Item;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;
import com.universalstudios.orlandoresort.utils.CommerceTypeConstants.AgeType;
import com.universalstudios.orlandoresort.utils.CommerceTypeConstants.TicketType;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import java.util.ArrayList;
import java.util.List;

import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.AGE_TYPE_ADULT;
import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.AGE_TYPE_CHILD;
import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.AGE_TYPE_GENERAL;
import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.TICKET_TYPE_ANNUAL_PASS;
import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.TICKET_TYPE_EXPRESS;
import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.TICKET_TYPE_EXTRA;
import static com.universalstudios.orlandoresort.utils.CommerceTypeConstants.TICKET_TYPE_TICKET;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/11/16.
 * Modified heavily by tjudkins on 12/8/16
 * Class: AssignableTicketItem
 * Class Description:
 *
 * This class is just used to contain display information for an order item that has
 * not yet been assigned. DO NOT USE THIS CLASS FOR ANY OTHER PURPOSE
 */
public class AssignableTicketItem extends BaseObservableWithLayoutItem {
    public static final String TAG = AssignableTicketItem.class.getSimpleName();

    private ParkTicketGroups ticketGroup;
    private ExpressPassTicketGroups expressPassTicketGroup;
    private AddOnTicketGroups addOnTicketGroup;
    @Bindable
    private String assignNamesAlternativeHeaderLine1;
    @Bindable
    private String assignNamesAlternativeHeaderLine2;
    @Bindable
    private String assignNamesAlternativeHeaderLine3;
    @Bindable
    private boolean selected;
    private List<OrderItem> unAssignedOrderItems;
    @Bindable
    private @TicketType
    int ticketType;
    @Bindable
    private String dobText;

    /**
     * Private default constructor to enforce use of constructor with data
     */
    private AssignableTicketItem() {}

    @Override
    public int getLayoutId() {
        return R.layout.commerce_assign_names_ticket_view;
    }

    public static AssignableTicketItem createAdultAssignableTicketItem(AddOnTicketGroups addOnTicketGroup) {
        AssignableTicketItem assignableTicketItem = new AssignableTicketItem();
        assignableTicketItem.setAddOnTicketGroup(addOnTicketGroup, AGE_TYPE_ADULT);
        assignableTicketItem.setTicketType(TICKET_TYPE_EXTRA);
        return assignableTicketItem;
    }

    public static AssignableTicketItem createAdultAssignableTicketItem(ParkTicketGroups parkTicketGroups, @Nullable PartyMember guest) {
        AssignableTicketItem assignableTicketItem = new AssignableTicketItem();
        assignableTicketItem.setTicketGroup(parkTicketGroups, AGE_TYPE_ADULT, guest);
        return assignableTicketItem;
    }

    public static AssignableTicketItem createChildAssignableTicketItem(AddOnTicketGroups addOnTicketGroup) {
        AssignableTicketItem assignableTicketItem = new AssignableTicketItem();
        assignableTicketItem.setAddOnTicketGroup(addOnTicketGroup, AGE_TYPE_CHILD);
        assignableTicketItem.setTicketType(TICKET_TYPE_EXTRA);
        return assignableTicketItem;
    }

    public static AssignableTicketItem createChildAssignableTicketItem(ParkTicketGroups parkTicketGroups, @Nullable PartyMember guest) {
        AssignableTicketItem assignableTicketItem = new AssignableTicketItem();
        assignableTicketItem.setTicketGroup(parkTicketGroups, AGE_TYPE_CHILD, guest);
        return assignableTicketItem;
    }

    public static AssignableTicketItem createGeneralAssignableTicketItem(AddOnTicketGroups addOnTicketGroup) {
        AssignableTicketItem assignableTicketItem = new AssignableTicketItem();
        assignableTicketItem.setAddOnTicketGroup(addOnTicketGroup, AGE_TYPE_GENERAL);
        assignableTicketItem.setTicketType(TICKET_TYPE_EXTRA);
        return assignableTicketItem;
    }


    public AssignableTicketItem(ExpressPassTicketGroups expressPassTicketGroups) {
        setExpressPassTicketGroup(expressPassTicketGroups);
        setTicketType(TICKET_TYPE_EXPRESS);
    }

    public String getAssignNamesAlternativeHeaderLine1() {
        return assignNamesAlternativeHeaderLine1;
    }

    private void setAssignNamesAlternativeHeaderLine1(String assignNamesAlternativeHeaderLine1) {
        this.assignNamesAlternativeHeaderLine1 = assignNamesAlternativeHeaderLine1;
        notifyPropertyChanged(BR.assignNamesAlternativeHeaderLine1);
    }

    public String getAssignNamesAlternativeHeaderLine2() {
        return assignNamesAlternativeHeaderLine2;
    }

    private void setAssignNamesAlternativeHeaderLine2(String assignNamesAlternativeHeaderLine2) {
        this.assignNamesAlternativeHeaderLine2 = assignNamesAlternativeHeaderLine2;
        notifyPropertyChanged(BR.assignNamesAlternativeHeaderLine2);
    }

    public String getAssignNamesAlternativeHeaderLine3() {
        return assignNamesAlternativeHeaderLine3;
    }

    private void setAssignNamesAlternativeHeaderLine3(String assignNamesAlternativeHeaderLine3) {
        this.assignNamesAlternativeHeaderLine3 = assignNamesAlternativeHeaderLine3;
        notifyPropertyChanged(BR.assignNamesAlternativeHeaderLine3);
    }

    public ParkTicketGroups getTicketGroup() {
        return ticketGroup;
    }

    public void setTicketGroup(ParkTicketGroups parkTicketGroups, @AgeType int ageType, @Nullable PartyMember guest) {
        this.ticketGroup = parkTicketGroups;
        if (null != parkTicketGroups) {
            if (parkTicketGroups.isAnnualPass()) {
                setTicketType(TICKET_TYPE_ANNUAL_PASS);
            } else {
                setTicketType(TICKET_TYPE_TICKET);
            }
            Ticket ticket = null;
            switch (ageType) {
                case AGE_TYPE_ADULT:
                    ticket = parkTicketGroups.getAdultTickets();
                    break;
                case AGE_TYPE_CHILD:
                    ticket = parkTicketGroups.getChildTickets();
                    break;
                case AGE_TYPE_GENERAL:
                    break;
                default:
                    break;
            }
            if (null != ticket) {
                // Retrieve existing birth date of guest if present
                if (guest != null && isAnnualPass()) {
                    if (ticket.getOrderItems() != null
                            && TicketAssignmentUtils.isGroupAssignedToGuest(ticket, guest)) {
                        OUTER_LOOP : for (OrderItem orderItem : ticket.getOrderItems()) {
                            if (orderItem != null && TicketAssignmentUtils.isOrderItemAssignedToGuest(orderItem, guest)) {
                                for (CommerceAttribute attribute : orderItem.getAttributes()) {
                                    if (attribute != null && attribute.isPartyMemberBirthDate()) {
                                        setDobText(attribute.getValue());
                                        break OUTER_LOOP;
                                    }
                                }
                            }
                        }
                    }
                }
                if (ticket.getItem() != null) {
                    TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(ticket.getItem().getTcmId1());
                    if (ticket.getQuantity() > 0) {
                        setAssignNamesAlternativeHeaderLine1(labelSpec.getTypeAssignNamesAlternativeHeaderLine1(ticket));
                        setAssignNamesAlternativeHeaderLine2(labelSpec.getTypeAssignNamesAlternativeHeaderLine2(ticket));
                        setAssignNamesAlternativeHeaderLine3(labelSpec.getTypeAssignNamesAlternativeHeaderLine3(ticket));
                    }
                }
            }
        }
    }

    public ExpressPassTicketGroups getExpressPassTicketGroup() {
        return expressPassTicketGroup;
    }

    public void setExpressPassTicketGroup(ExpressPassTicketGroups expressPassTicketGroup) {
        this.expressPassTicketGroup = expressPassTicketGroup;
        if (null != expressPassTicketGroup && null != expressPassTicketGroup.getItem()) {
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(expressPassTicketGroup.getItem().getTcmId1());
            setAssignNamesAlternativeHeaderLine1(labelSpec.getTypeAssignNamesAlternativeHeaderLine1(expressPassTicketGroup));
            setAssignNamesAlternativeHeaderLine2(labelSpec.getTypeAssignNamesAlternativeHeaderLine2(expressPassTicketGroup));
            setAssignNamesAlternativeHeaderLine3(labelSpec.getTypeAssignNamesAlternativeHeaderLine3(expressPassTicketGroup));
        }
    }

    public AddOnTicketGroups getAddOnTicketGroup() {
        return addOnTicketGroup;
    }

    public void setAddOnTicketGroup(AddOnTicketGroups addOnTicketGroups, @AgeType int ageType) {
        this.addOnTicketGroup = addOnTicketGroups;
        if (null != addOnTicketGroups) {
            Ticket ticket = null;
            switch (ageType) {
                case AGE_TYPE_ADULT:
                    ticket = addOnTicketGroups.getAdultAddOns();
                    break;
                case AGE_TYPE_CHILD:
                    ticket = addOnTicketGroups.getChildAddOns();
                    break;
                case AGE_TYPE_GENERAL:
                    ticket = addOnTicketGroups.getAllAddOns();
                    break;
            }
            if (null != ticket) {
                Item item = ticket.getItem();
                if (null != item && ticket.getQuantity() > 0) {
                    TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(item.getTcmId1());
                    setAssignNamesAlternativeHeaderLine1(labelSpec.getTypeAssignNamesAlternativeHeaderLine1(ticket));
                    setAssignNamesAlternativeHeaderLine2(labelSpec.getTypeAssignNamesAlternativeHeaderLine2(ticket));
                    setAssignNamesAlternativeHeaderLine3(labelSpec.getTypeAssignNamesAlternativeHeaderLine3(ticket));
                }
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(@TicketType int ticketType) {
        this.ticketType = ticketType;
        notifyPropertyChanged(BR.ticketType);
    }

    public String getDobText() {
        return dobText;
    }

    public void setDobText(String dobText) {
        this.dobText = dobText;
        notifyPropertyChanged(BR.dobText);
    }

    public boolean isTicket() {
        return TICKET_TYPE_TICKET == ticketType;
    }

    public boolean isExpress() {
        return TICKET_TYPE_EXPRESS == ticketType;
    }

    public boolean isExtra() {
        return TICKET_TYPE_EXTRA == ticketType;
    }

    public boolean isAnnualPass() {
        return TICKET_TYPE_ANNUAL_PASS == ticketType;
    }

    @NonNull
    public List<OrderItem> getUnAssignedOrderItems() {
        if (null == unAssignedOrderItems) {
            unAssignedOrderItems = new ArrayList<>();
        }
        return unAssignedOrderItems;
    }

    public void setUnAssignedOrderItems(List<OrderItem> unAssignedOrderItems) {
        this.unAssignedOrderItems = unAssignedOrderItems;
    }

}
