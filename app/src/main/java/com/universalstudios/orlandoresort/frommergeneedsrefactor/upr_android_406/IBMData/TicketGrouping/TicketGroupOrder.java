package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.DisplayPricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Pricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Shipping;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceMessage;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhargavi on 6/6/16.
 *
 */
@Parcel
public class TicketGroupOrder extends GsonObject {

    @SerializedName("orderItemGroups")
    OrderItemGroups orderItemGroups;

    @SerializedName("externalUserId")
    String externalUserId;

    @SerializedName("status")
    String status;

    @SerializedName("userId")
    String userId;

    @SerializedName("pricing")
    Pricing pricing;

    @SerializedName("displayPricing")
    DisplayPricing displayPricing;

    @SerializedName("orderId")
    String orderId;

    @SerializedName("shipping")
    Shipping shipping;

    @SerializedName("lastUpdate")
    String lastUpdate;

    @SerializedName("messages")
    List<CommerceMessage> messages;

    @SerializedName("offer")
    Offer offer;

    public BigDecimal getPurchaseAmount() {
        BigDecimal amount = null;
        if (pricing != null) {
            amount = pricing.getGrandTotal();
        }
        return amount;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public DisplayPricing getDisplayPricing() { return displayPricing; }

    public String getExternalUserId() {
        return externalUserId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public OrderItemGroups getOrderItemGroups() {
        return orderItemGroups;
    }

    public List<CommerceMessage> getMessages() {
        return messages;
    }

    public Offer getOffer() {
        return offer;
    }

    public boolean isEmpty() {
        return orderItemGroups == null || orderItemGroups.isEmpty();
    }

    public List<ParkTicketGroups> getParkTicketGroups() {
        if (getOrderItemGroups() != null) {
            return getOrderItemGroups().getParkTicketGroups();
        } else {
            return null;
        }
    }

    // Helper methods

    public int getMaxNumberOfAdultTickets() {
        int current = 0;
        if (orderItemGroups != null) {
            if (orderItemGroups.getParkTicketGroups() != null) {
                for (ParkTicketGroups parkTicketGroups : getParkTicketGroups()) {
                    if (parkTicketGroups != null && parkTicketGroups.getAdultTickets() != null
                            && parkTicketGroups.getAdultTickets().getQuantity() > current) {
                        current = parkTicketGroups.getAdultTickets().getQuantity();
                    }
                }
            }

            // Adult addons
            for (AddOnTicketGroups addOnTicketGroup : orderItemGroups.getAddOnTicketGroups()) {
                if (addOnTicketGroup != null && addOnTicketGroup.getAdultAddOns() != null
                        && addOnTicketGroup.getAdultAddOns().getQuantity() > current) {
                    current = addOnTicketGroup.getAdultAddOns().getQuantity();
                }
            }

            // Adult annual passes
            if (orderItemGroups.getAnnualPassParkTicketGroups() != null) {
                for (ParkTicketGroups parkTicketGroups : orderItemGroups.getAnnualPassParkTicketGroups()) {
                    if (parkTicketGroups != null && parkTicketGroups.getAdultTickets() != null
                            && parkTicketGroups.getAdultTickets().getQuantity() > current) {
                        current = parkTicketGroups.getAdultTickets().getQuantity();
                    }
                }
            }
        }

        return current;
    }

    public int getMaxNumberOfChildTickets() {
        int current = 0;
        if (orderItemGroups != null) {
            if (orderItemGroups.getParkTicketGroups() != null) {
                for (ParkTicketGroups parkTicketGroups : getParkTicketGroups()) {
                    if (parkTicketGroups != null && parkTicketGroups.getChildTickets() != null
                            && parkTicketGroups.getChildTickets().getQuantity() > current) {
                        current = parkTicketGroups.getChildTickets().getQuantity();
                    }
                }
            }

            // Child addons
            for (AddOnTicketGroups addOnTicketGroup : orderItemGroups.getAddOnTicketGroups()) {
                if (addOnTicketGroup != null && addOnTicketGroup.getChildAddOns() != null
                        && addOnTicketGroup.getChildAddOns().getQuantity() > current) {
                    current = addOnTicketGroup.getChildAddOns().getQuantity();
                }
            }

            // Child annual passes
            if (orderItemGroups.getAnnualPassParkTicketGroups() != null) {
                for (ParkTicketGroups parkTicketGroups : orderItemGroups.getAnnualPassParkTicketGroups()) {
                    if (parkTicketGroups != null && parkTicketGroups.getChildTickets() != null
                            && parkTicketGroups.getChildTickets().getQuantity() > current) {
                        current = parkTicketGroups.getAdultTickets().getQuantity();
                    }
                }
            }
        }

        return current;
    }

    public int getMaxNumberOfUEPTickets() {
        int current = 0;

        if(orderItemGroups != null && orderItemGroups.getExpressPassGroups() != null) {
            for (ExpressPassTicketGroups expressPassTicketGroups : orderItemGroups.getExpressPassGroups()) {
                if (expressPassTicketGroups != null && expressPassTicketGroups.getQuantity() > current) {
                    current = expressPassTicketGroups.getQuantity();
                }
            }
        }

        return current;
    }

    public int getMaxNumberOfAllAgeAddOnTickets() {
        int current = 0;

        if(orderItemGroups != null) {
            for (AddOnTicketGroups addOnTicketGroup : orderItemGroups.getAddOnTicketGroups()) {
                if (addOnTicketGroup != null && addOnTicketGroup.getAllAddOns() != null
                        && addOnTicketGroup.getAllAddOns().getQuantity() > current) {
                    current = addOnTicketGroup.getAllAddOns().getQuantity();
                }
            }
        }

        return current;
    }

    @NonNull
    public Map<String, List<OrderItem>> getAssignedTicketAdults() {
        Map<String, List<OrderItem>> assignedOrderMap = new HashMap<>();
        if (getOrderItemGroups() != null && getOrderItemGroups().getParkTicketGroups() != null) {
            for (ParkTicketGroups parkTicketGroups : getOrderItemGroups().getParkTicketGroups()) {
                if (parkTicketGroups != null) {
                    Ticket t = parkTicketGroups.getAdultTickets();
                    if (t != null && t.getOrderItems() != null) {
                        for (OrderItem item : t.getOrderItems()) {
                            if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                                if (null == assignedOrderMap.get(item.getAssignedName())) {
                                    assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                                }
                                assignedOrderMap.get(item.getAssignedName()).add(item);
                            }
                        }
                    }
                }
            }
        }

        // Adult addons
        if (getOrderItemGroups() != null) {
            for (AddOnTicketGroups addOnTicketGroup : getOrderItemGroups().getAddOnTicketGroups()) {
                if (addOnTicketGroup.getAdultAddOns() != null
                        && addOnTicketGroup.getAdultAddOns().getOrderItems() != null) {
                    for (OrderItem item : addOnTicketGroup.getAdultAddOns().getOrderItems()) {
                        if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                            if (null == assignedOrderMap.get(item.getAssignedName())) {
                                assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                            }
                            assignedOrderMap.get(item.getAssignedName()).add(item);
                        }
                    }
                }
            }
        }

        // Adult annual passes
        if (getOrderItemGroups() != null && getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
            for (ParkTicketGroups parkTicketGroups : getOrderItemGroups().getAnnualPassParkTicketGroups()) {
                if (parkTicketGroups != null) {
                    Ticket t = parkTicketGroups.getAdultTickets();
                    if (t != null && t.getOrderItems() != null) {
                        for (OrderItem item : t.getOrderItems()) {
                            if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                                if (null == assignedOrderMap.get(item.getAssignedName())) {
                                    assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                                }
                                assignedOrderMap.get(item.getAssignedName()).add(item);
                            }
                        }
                    }
                }
            }
        }

        return assignedOrderMap;
    }

    @NonNull
    public Map<String, List<OrderItem>> getAssignedTicketsChild() {
        Map<String, List<OrderItem>> assignedOrderMap = new HashMap<>();
        if (getOrderItemGroups() != null && getOrderItemGroups().getParkTicketGroups() != null) {
            for (ParkTicketGroups parkTicketGroups : getOrderItemGroups().getParkTicketGroups()) {
                if (parkTicketGroups != null) {
                    Ticket t = parkTicketGroups.getChildTickets();
                    if (t != null && t.getOrderItems() != null) {
                        for (OrderItem item : t.getOrderItems()) {
                            if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                                if (null == assignedOrderMap.get(item.getAssignedName())) {
                                    assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                                }
                                assignedOrderMap.get(item.getAssignedName()).add(item);
                            }
                        }
                    }
                }
            }
        }

        // Child addons
        if (getOrderItemGroups() != null) {
            for (AddOnTicketGroups addOnTicketGroup : getOrderItemGroups().getAddOnTicketGroups()) {
                if (addOnTicketGroup.getChildAddOns() != null
                        && addOnTicketGroup.getChildAddOns().getOrderItems() != null) {
                    for (OrderItem item : addOnTicketGroup.getChildAddOns().getOrderItems()) {
                        if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                            if (null == assignedOrderMap.get(item.getAssignedName())) {
                                assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                            }
                            assignedOrderMap.get(item.getAssignedName()).add(item);
                        }
                    }
                }
            }
        }

        // Child annual passes
        if (getOrderItemGroups() != null && getOrderItemGroups().getAnnualPassParkTicketGroups() != null) {
            for (ParkTicketGroups parkTicketGroups : getOrderItemGroups().getAnnualPassParkTicketGroups()) {
                if (parkTicketGroups != null) {
                    Ticket t = parkTicketGroups.getChildTickets();
                    if (t != null && t.getOrderItems() != null) {
                        for (OrderItem item : t.getOrderItems()) {
                            if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                                if (null == assignedOrderMap.get(item.getAssignedName())) {
                                    assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                                }
                                assignedOrderMap.get(item.getAssignedName()).add(item);
                            }
                        }
                    }
                }
            }
        }

        return assignedOrderMap;
    }

    @NonNull
    public Map<String, List<OrderItem>> getAssignedAllAgeAddOnTickets() {
        Map<String, List<OrderItem>> assignedOrderMap = new HashMap<>();
        if (getOrderItemGroups() != null) {
            for (AddOnTicketGroups addOnTicketGroup : getOrderItemGroups().getAddOnTicketGroups()) {
                if (addOnTicketGroup != null && addOnTicketGroup.getAllAddOns() != null
                        && addOnTicketGroup.getAllAddOns().getOrderItems() != null) {
                    for (OrderItem item : addOnTicketGroup.getAllAddOns().getOrderItems()) {
                        if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                            if (null == assignedOrderMap.get(item.getAssignedName())) {
                                assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                            }
                            assignedOrderMap.get(item.getAssignedName()).add(item);
                        }
                    }
                }
            }
        }

        return assignedOrderMap;
    }

    @NonNull
    public Map<String, List<OrderItem>> getAssignedUEPTickets() {
        Map<String, List<OrderItem>> assignedOrderMap = new HashMap<>();
        if (getOrderItemGroups() != null && getOrderItemGroups().getExpressPassGroups() != null) {
            for (ExpressPassTicketGroups expressPassTicketGroup : getOrderItemGroups().getExpressPassGroups()) {
                if (expressPassTicketGroup != null && expressPassTicketGroup.getOrderItems() != null) {
                    for (OrderItem item : expressPassTicketGroup.getOrderItems()) {
                        if (item != null && !TextUtils.isEmpty(item.getAssignedName())) {
                            if (null == assignedOrderMap.get(item.getAssignedName())) {
                                assignedOrderMap.put(item.getAssignedName(), new ArrayList<OrderItem>());
                            }
                            assignedOrderMap.get(item.getAssignedName()).add(item);
                        }
                    }
                }
            }
        }

        return assignedOrderMap;
    }

    @NonNull
    public List<String> getIdsForTridion() {
        List<String> ids = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();

        if (orderItemGroups != null) {
            if (orderItemGroups.getParkTicketGroups() != null) {
                for (ParkTicketGroups group : orderItemGroups.getParkTicketGroups()) {
                    if (null != group) {
                        tickets.add(group.getAdultTickets());
                        tickets.add(group.getChildTickets());
                    }
                }
            }
            List<AddOnTicketGroups> addOnTicketGroups = orderItemGroups.getAddOnTicketGroups();
            for (AddOnTicketGroups group : addOnTicketGroups) {
                if (null != group) {
                    tickets.add(group.getAllAddOns());
                    tickets.add(group.getAdultAddOns());
                    tickets.add(group.getChildAddOns());
                }
            }
            if (orderItemGroups.getAnnualPassParkTicketGroups() != null) {
                for (ParkTicketGroups group : orderItemGroups.getAnnualPassParkTicketGroups()) {
                    if (null != group) {
                        tickets.add(group.getAdultTickets());
                        tickets.add(group.getChildTickets());
                    }
                }
            }
            for (Ticket ticket : tickets) {
                if (null != ticket && ticket.getItem() != null) {
                    String id = ticket.getItem().getTcmId1();
                    if (!TextUtils.isEmpty(id)) {
                        ids.add(id);
                    }
                    String detailId = ticket.getItem().getTcmId2();
                    if (!TextUtils.isEmpty(detailId)) {
                        ids.add(detailId);
                    }
                }
            }

            for (ExpressPassTicketGroups expressGroup : orderItemGroups.getExpressPassGroups()) {
                if (null != expressGroup && null != expressGroup.getItem()) {
                    String id = expressGroup.getItem().getTcmId1();
                    if (!TextUtils.isEmpty(id)) {
                        ids.add(id);
                    }
                    String detailId = expressGroup.getItem().getTcmId2();
                    if (!TextUtils.isEmpty(detailId)) {
                        ids.add(detailId);
                    }
                }
            }
        }

        // Offer ids
        if (offer != null) {
            String id = offer.getTcmId1();
            if (!TextUtils.isEmpty(id)) {
                ids.add(id);
            }
            id = offer.getTcmId2();
            if (!TextUtils.isEmpty(id)) {
                ids.add(id);
            }
        }

        return ids;
    }

    public boolean hasFlexPay() {
        return null != orderItemGroups && orderItemGroups.hasFlexPay();
    }
}
