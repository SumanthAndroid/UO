package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargavi on 6/7/16.
 */
@Parcel
public class ParkTicketGroups extends GsonObject {

    @SerializedName("numParks")
    String numParks;

    @SerializedName("parkName")
    String parkName;

    @SerializedName("child")
    Ticket childTickets;

    @SerializedName("childTickets")
    Ticket childTicketsLegacy;

    @SerializedName("ptp")
    String ptp;

    @SerializedName("groupingKey")
    String groupingKey;

    @SerializedName("description")
    String description;

    @SerializedName("pointOfOrigin")
    String pointOfOrigin;

    @SerializedName("numDays")
    String numDays;

    @SerializedName("itemType")
    String itemType;

    @SerializedName("adult")
    Ticket adultTickets;

    @SerializedName("adultTickets")
    Ticket adultTicketsLegacy;

    public String getNumParks() {
        return numParks;
    }

    public String getParkName() {
        return parkName;
    }

    public String getPtp() {
        return ptp;
    }

    public String getGroupingKey() {
        return groupingKey;
    }

    public String getDescription() {
        return description;
    }

    public String getPointOfOrigin() {
        return pointOfOrigin;
    }

    public String getNumDays() {
        return numDays;
    }

    public String getItemType() {
        return itemType;
    }

    public Ticket getChildTickets() {
        // Try to use the new child tickets key first
        if (childTickets != null) {
            return childTickets;
        }
        // Otherwise, use the legacy child tickets key
        else {
            return childTicketsLegacy;
        }
    }

    public Ticket getAdultTickets() {
        // Try to use the new adult tickets key first
        if (adultTickets != null) {
            return adultTickets;
        }
        // Otherwise, use the legacy adult tickets key
        else {
            return adultTicketsLegacy;
        }
    }

    /**
     * Returns true if this item's quantity can be updated
     */
    public boolean isQuantityChangeAllowed() {
        boolean isQuantityChangeAllowed = false;
        List<CommerceAttribute> attributes = new ArrayList<>();
        if (getAdultTickets() != null && getAdultTickets().getItem() != null && getAdultTickets().getItem().getAttributes() != null) {
            attributes.addAll(getAdultTickets().getItem().getAttributes());
        }
        if (getChildTickets() != null && getChildTickets().getItem() != null && getChildTickets().getItem().getAttributes() != null) {
            attributes.addAll(getChildTickets().getItem().getAttributes());
        }
        for (CommerceAttribute attribute : attributes) {
            if (attribute != null && attribute.isQuantityChangeAllowed()) {
                isQuantityChangeAllowed = true;
                break;
            }
        }
        return isQuantityChangeAllowed;
    }

    /**
     * Gets the total savings for both the adult and child tickets
     * @return 0 if there are no savings
     */
    public BigDecimal getTotalSavings()
    {
        BigDecimal totalSavings = BigDecimal.ZERO;

        if(adultTickets != null && adultTickets.getTotalPriceSavings() != null) {
            totalSavings = totalSavings.add(adultTickets.getTotalPriceSavings());
        }

        if(childTickets != null && childTickets.getTotalPriceSavings() != null) {
            totalSavings = totalSavings.add(childTickets.getTotalPriceSavings());
        }
        return totalSavings;
    }

    public boolean isFlexPay() {
        Ticket adult = getAdultTickets();
        Ticket child = getChildTickets();
        return (adult != null && adult.isFlexPay()) || (child != null && child.isFlexPay());
    }

    @NonNull
    public List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        Ticket adult = getAdultTickets();
        Ticket child = getChildTickets();
        if (adult != null && adult.getOrderItems() != null) {
            orderItems.addAll(adult.getOrderItems());
        }
        if (child != null && child.getOrderItems() != null) {
            orderItems.addAll(child.getOrderItems());
        }
        return orderItems;
    }

    @NonNull
    public BigDecimal getTaxes() {
        BigDecimal tax = BigDecimal.ZERO;
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem != null && orderItem.getPricing() != null) {
                BigDecimal salesTax = orderItem.getPricing().getSalesTax();
                if (salesTax != null) {
                    tax = tax.add(salesTax);
                }
            }
        }
        return tax;
    }

    @NonNull
    public BigDecimal getFlexPayTotalDownPaymentAmount() {
        BigDecimal downPaymentAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem != null && orderItem.isFlexPay()) {
                for (CommerceAttribute attribute : orderItem.getAttributes()) {
                    if (attribute != null && attribute.isFlexPayDownPayment()) {
                        BigDecimal amount = NumberUtils.toBigDecimal(attribute.getValue());
                        downPaymentAmount = downPaymentAmount.add(amount);
                    }
                }
            }
        }
        return downPaymentAmount;
    }

    @NonNull
    public BigDecimal getFlexPayTotalFinancedAmount() {
        BigDecimal financedAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem != null && orderItem.isFlexPay()) {
                for (CommerceAttribute attribute : orderItem.getAttributes()) {
                    if (attribute != null && attribute.isFlexPayFinancedAmount()) {
                        BigDecimal amount = NumberUtils.toBigDecimal(attribute.getValue());
                        financedAmount = financedAmount.add(amount);
                    }
                }
            }
        }
        return financedAmount;
    }

    @NonNull
    public BigDecimal getFlexPayTotalPaymentAmount() {
        BigDecimal paymentAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem != null && orderItem.isFlexPay()) {
                for (CommerceAttribute attribute : orderItem.getAttributes()) {
                    if (attribute != null && attribute.isFlexPayRecurringPayAmount()) {
                        BigDecimal amount = NumberUtils.toBigDecimal(attribute.getValue());
                        paymentAmount = paymentAmount.add(amount);
                    }
                }
            }
        }
        return paymentAmount;
    }

    public boolean isAnnualPass() {
        Ticket adult = getAdultTickets();
        Ticket child = getChildTickets();
        if (adult != null) {
            return adult.isAnnualPass();
        }
        return child != null && child.isAnnualPass();
    }

    public boolean shouldShowSavingsMessage() {
        boolean showSavingsMessage = false;

        List<CommerceAttribute> attributes = new ArrayList<>();
        if (this.getAdultTickets() != null && this.getAdultTickets().getOrderItemAttributes() != null) {
            attributes.addAll(this.getAdultTickets().getOrderItemAttributes());
        }

        if (this.getChildTickets() != null && this.getChildTickets().getOrderItemAttributes() != null) {
            attributes.addAll(this.getChildTickets().getOrderItemAttributes());
        }

        for (CommerceAttribute attribute : attributes) {
            if (attribute != null && attribute.shouldShowSavingsMessage()) {
                showSavingsMessage = true;
                break;
            }
        }
        return showSavingsMessage && getPriceDifference().compareTo(BigDecimal.ZERO) >= 0;
    }

    @NonNull
    private BigDecimal getPriceDifference()
    {
        BigDecimal priceDifference = BigDecimal.ZERO;

        if(getAdultTickets() != null && getAdultTickets().getOrderItems() != null) {
            for(OrderItem item : getAdultTickets().getOrderItems()) {
                priceDifference.add(item.getOfferPriceDifference());
            }
        }

        if(getChildTickets() != null && getChildTickets().getOrderItems() != null) {
            for(OrderItem item : getChildTickets().getOrderItems()) {
                priceDifference.add(item.getOfferPriceDifference());
            }
        }

        return priceDifference;
    }
}
