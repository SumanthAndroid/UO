package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author jamestimberlake
 * @created 7/28/16.
 */
@Parcel
public class OrderItemGroups extends GsonObject {

    @SerializedName("parkTickets")
    List<ParkTicketGroups> parkTicketGroups = new ArrayList<>();

    @SerializedName("expressPasses")
    List<ExpressPassTicketGroups> expressPassGroups = new ArrayList<>();

    @SerializedName("addOns")
    HashMap<String, List<AddOnTicketGroups>> addOnsMap = new HashMap<>();

    @SerializedName("annualPasses")
    List<ParkTicketGroups> annualPassParkTicketGroups = new ArrayList<>();

    public List<ParkTicketGroups> getParkTicketGroups() {
        return parkTicketGroups;
    }

    public List<ExpressPassTicketGroups> getExpressPassGroups() {
        return expressPassGroups;
    }

    public HashMap<String, List<AddOnTicketGroups>> getAddOnsMap() {
        return addOnsMap;
    }

    public List<ParkTicketGroups> getAnnualPassParkTicketGroups() {
        return annualPassParkTicketGroups;
    }

    public boolean isEmpty() {
        return (parkTicketGroups == null || parkTicketGroups.isEmpty())
                && (expressPassGroups == null || expressPassGroups.isEmpty())
                && (addOnsMap == null || addOnsMap.isEmpty())
                && (annualPassParkTicketGroups == null || annualPassParkTicketGroups.isEmpty());
    }

    @NonNull
    public List<AddOnTicketGroups> getAddOnTicketGroups() {
        List<AddOnTicketGroups> addOnTicketGroups = new ArrayList<>();
        if (addOnsMap != null) {
            for (List<AddOnTicketGroups> addOnTicketGroupsList : addOnsMap.values()) {
                if (addOnTicketGroupsList != null) {
                    for (AddOnTicketGroups addOnTicketGroup : addOnTicketGroupsList) {
                        if (addOnTicketGroup != null) {
                            addOnTicketGroups.add(addOnTicketGroup);
                        }
                    }
                }
            }
        }

        return addOnTicketGroups;
    }

    public boolean hasFlexPay() {
        boolean hasFlexPay = false;
        if (null != annualPassParkTicketGroups) {
            for (ParkTicketGroups groups : annualPassParkTicketGroups) {
                if (null != groups && groups.isFlexPay()) {
                    hasFlexPay = true;
                    break;
                }
            }
        }
        if (null != parkTicketGroups) {
            for (ParkTicketGroups groups : parkTicketGroups) {
                if (null != groups && groups.isFlexPay()) {
                    hasFlexPay = true;
                    break;
                }
            }
        }
        if (!hasFlexPay && null != expressPassGroups) {
            for (ExpressPassTicketGroups groups : expressPassGroups) {
                if (null != groups && groups.isFlexPay()) {
                    hasFlexPay = true;
                    break;
                }
            }
        }
        if (!hasFlexPay) {
            for (AddOnTicketGroups groups : getAddOnTicketGroups()) {
                if (null != groups && groups.isFlexPay()) {
                    hasFlexPay = true;
                    break;
                }
            }
        }
        return hasFlexPay;
    }
}
