package com.universalstudios.orlandoresort.view.custom_calendar;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEventAvailabilityDate;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.PriceInventoryEventAvailabilityInvEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventCalendarCell {
    private List<PriceInventoryEventAvailabilityDate> priceInventoryEventAvailabilityInvEvents;
    private Date date;
    private boolean isOutOfMonth;

    public EventCalendarCell(Date date, boolean isOutOfMonth) {
        this.date = date;
        this.isOutOfMonth = isOutOfMonth;
    }

    public void addPriceInventoryEvents(PriceInventoryEventAvailabilityDate priceInventoryEventAvailabilityInvEvent) {
        if (priceInventoryEventAvailabilityInvEvents == null) {
            priceInventoryEventAvailabilityInvEvents = new ArrayList<>();
        }

        priceInventoryEventAvailabilityInvEvents.add(priceInventoryEventAvailabilityInvEvent);
    }

    public void addPriceInventoryEvents(List<PriceInventoryEventAvailabilityDate> list) {
        if (priceInventoryEventAvailabilityInvEvents == null) {
            priceInventoryEventAvailabilityInvEvents = new ArrayList<>();
        }

        priceInventoryEventAvailabilityInvEvents.addAll(list);
    }

    public boolean isAvailable() {
        if (priceInventoryEventAvailabilityInvEvents != null) {
            for (PriceInventoryEventAvailabilityDate priceInventoryEventAvailabilityInvEvent : priceInventoryEventAvailabilityInvEvents) {
                if (priceInventoryEventAvailabilityInvEvent != null) {
                    List<PriceInventoryEventAvailabilityInvEvent> list = priceInventoryEventAvailabilityInvEvent.getPriceInventoryEventAvailabilityInvEvents();
                    if (list != null) {
                        for (PriceInventoryEventAvailabilityInvEvent priceInventoryEventAvailabilityInvEvent1 : list) {
                            String available = priceInventoryEventAvailabilityInvEvent1.getAvailable();
                            if (available != null) {
                                try {
                                    int i = Integer.parseInt(available);
                                    if (i > 0) {
                                        return true;
                                    }
                                } catch (NumberFormatException nfe) {
                                    Crittercism.logHandledException(nfe);
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<String> getAvailableTimes() {
        List<String> result = new ArrayList<>();
        if (priceInventoryEventAvailabilityInvEvents != null) {
            for (PriceInventoryEventAvailabilityDate priceInventoryEventAvailabilityInvEvent : priceInventoryEventAvailabilityInvEvents) {
                if (priceInventoryEventAvailabilityInvEvent != null) {
                    List<PriceInventoryEventAvailabilityInvEvent> list = priceInventoryEventAvailabilityInvEvent.getPriceInventoryEventAvailabilityInvEvents();
                    if (list != null) {
                        for (PriceInventoryEventAvailabilityInvEvent priceInventoryEventAvailabilityInvEvent1 : list) {
                            if (priceInventoryEventAvailabilityInvEvent1 != null) {
                                String time = null;
                                // Shows have a showTime while other events only have a start date
                                if (!TextUtils.isEmpty(priceInventoryEventAvailabilityInvEvent1.getShowTime())) {
                                    time = priceInventoryEventAvailabilityInvEvent1.getShowTime();
                                } else if (priceInventoryEventAvailabilityInvEvent1.getStartDate() != null) {
                                    time = priceInventoryEventAvailabilityInvEvent1.getStartTime();
                                }
                                if (!TextUtils.isEmpty(time) && !result.contains(time)) {
                                    result.add(time);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Nullable
    public Date getDate() {
        return date;
    }

    public int getDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public boolean isOutOfMonth() {
        return isOutOfMonth;
    }
}
