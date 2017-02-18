package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargavi on 6/7/16.
 */
@Parcel
public class AddOnTicketGroups extends GsonObject {

    @SerializedName("all")
    Ticket allAddOns;

    @SerializedName("child")
    Ticket childAddOns;

    @SerializedName("groupingKey")
    String groupingKey;

    @SerializedName("adult")
    Ticket adultAddOns;

    public Ticket getAllAddOns() {
        return allAddOns;
    }

    public void setAllAddOns(Ticket allAddOns) {
        this.allAddOns = allAddOns;
    }

    public Ticket getChildAddOns() {
        return childAddOns;
    }

    public void setChildAddOns(Ticket childAddOns) {
        this.childAddOns = childAddOns;
    }

    public Ticket getAdultAddOns() {
        return adultAddOns;
    }

    public void setAdultAddOns(Ticket adultAddOns) {
        this.adultAddOns = adultAddOns;
    }

    /**
     * Returns true if this item's quantity can be updated
     */
    public boolean isQuantityChangeAllowed() {
        boolean isQuantityChangeAllowed = false;
        List<CommerceAttribute> attributes = new ArrayList<>();
        if (allAddOns != null && allAddOns.getItem() != null && allAddOns.getItem().getAttributes() != null) {
            attributes.addAll(allAddOns.getItem().getAttributes());
        }
        if (adultAddOns != null && adultAddOns.getItem() != null && adultAddOns.getItem().getAttributes() != null) {
            attributes.addAll(adultAddOns.getItem().getAttributes());
        }
        if (childAddOns != null && childAddOns.getItem() != null && childAddOns.getItem().getAttributes() != null) {
            attributes.addAll(childAddOns.getItem().getAttributes());
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

        Ticket adult = getAdultAddOns();
        Ticket child = getChildAddOns();

        if(adult != null && adult.getTotalPriceSavings() != null) {
            totalSavings = totalSavings.add(adult.getTotalPriceSavings());
        }

        if(child != null && child.getTotalPriceSavings() != null) {
            totalSavings = totalSavings.add(child.getTotalPriceSavings());
        }
        return totalSavings;
    }

    public boolean isFlexPay() {
        Ticket adult = getAdultAddOns();
        Ticket child = getChildAddOns();
        Ticket all = getAllAddOns();
        return (adult != null && adult.isFlexPay()) || (child != null && child.isFlexPay())
                || (all != null && all.isFlexPay());
    }

    public boolean shouldShowSavingsMessage() {
        boolean showSavingsMessage = false;

        List<CommerceAttribute> attributes = new ArrayList<>();
        if (allAddOns != null && allAddOns.getItem() != null && allAddOns.getItem().getAttributes() != null) {
            attributes.addAll(allAddOns.getItem().getAttributes());
        }
        if (adultAddOns != null && adultAddOns.getItem() != null && adultAddOns.getItem().getAttributes() != null) {
            attributes.addAll(adultAddOns.getItem().getAttributes());
        }
        if (childAddOns != null && childAddOns.getItem() != null && childAddOns.getItem().getAttributes() != null) {
            attributes.addAll(childAddOns.getItem().getAttributes());
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

        if (allAddOns != null && allAddOns.getOrderItems() != null) {
            for(OrderItem item : allAddOns.getOrderItems()) {
                priceDifference.add(item.getOfferPriceDifference());
            }
        }

        if (adultAddOns != null && adultAddOns.getOrderItems() != null) {
            for(OrderItem item : adultAddOns.getOrderItems()) {
                priceDifference.add(item.getOfferPriceDifference());
            }
        }

        if (childAddOns != null && childAddOns.getOrderItems() != null) {
            for(OrderItem item : childAddOns.getOrderItems()) {
                priceDifference.add(item.getOfferPriceDifference());
            }
        }

        return priceDifference;
    }




}
