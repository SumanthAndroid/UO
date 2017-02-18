package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Tyler Ritchie on 10/14/16.
 */

@Parcel
public class PriceInventoryEventAvailabilityDate extends GsonObject {

    @SerializedName("inventoryEvents")
    List<PriceInventoryEventAvailabilityInvEvent> priceInventoryEventAvailabilityInvEvents;

    @SerializedName("pricing")
    List<PriceInventoryEventAvailabilityPricing> priceInventoryEventAvailabilityPricings;

    public List<PriceInventoryEventAvailabilityInvEvent> getPriceInventoryEventAvailabilityInvEvents() {
        return priceInventoryEventAvailabilityInvEvents;
    }

    public List<PriceInventoryEventAvailabilityPricing> getPriceInventoryEventAvailabilityPricings() {
        return priceInventoryEventAvailabilityPricings;
    }

    public String getTime() {
        List<PriceInventoryEventAvailabilityInvEvent> events = getPriceInventoryEventAvailabilityInvEvents();
        if (events != null) {
            for (PriceInventoryEventAvailabilityInvEvent event : events) {
                if (event != null) {
                    String showTime = event.getShowTime();
                    if (!TextUtils.isEmpty(showTime)) {
                        return event.getShowTime();
                    }
                }
            }
        }
        return null;
    }

    public BigDecimal getPriceForTime(String time){
        List<PriceInventoryEventAvailabilityInvEvent> events = getPriceInventoryEventAvailabilityInvEvents();
        List<PriceInventoryEventAvailabilityPricing> prices = getPriceInventoryEventAvailabilityPricings();
        if (events != null && prices != null) {
            for (PriceInventoryEventAvailabilityInvEvent event : events) {
                if (time.equalsIgnoreCase(event.getShowTime())) {
                    for (PriceInventoryEventAvailabilityPricing price : prices) {
                        if (price != null) {
                            return price.getAmount();
                        }
                    }
                }
            }
        }
        return null;
    }

    public PriceInventoryEventAvailabilityInvEvent getEventForDate(Date date) {
        if (date != null && priceInventoryEventAvailabilityInvEvents != null) {
            for (PriceInventoryEventAvailabilityInvEvent invEvent : priceInventoryEventAvailabilityInvEvents) {
                if (invEvent != null && date.equals(invEvent.getStartDate())) {
                    return invEvent;
                }
            }
        }
        return null;
    }

    @Nullable
    public Date getDate() {
        Date date = null;
        if (priceInventoryEventAvailabilityInvEvents != null) {
            for (PriceInventoryEventAvailabilityInvEvent invEvent : priceInventoryEventAvailabilityInvEvents) {
                if (invEvent != null) {
                    date = invEvent.getStartDate();
                    break;
                }
            }
        }
        return date;
    }
}
