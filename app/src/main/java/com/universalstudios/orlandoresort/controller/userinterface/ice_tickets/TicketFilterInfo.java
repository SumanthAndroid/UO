package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views.PooControl;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCard;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.view.custom_calendar.CalendarCellSeasonState;

import java.util.Date;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/31/16.
 * Class: TicketFilterInfo
 * Class Description: Filter information from user selection
 */
public class TicketFilterInfo extends GsonObject {
    public static final String TAG = TicketFilterInfo.class.getSimpleName();

    public static final int FLAG_ANNUAL_PASS = -2;

    //Shared
    public int numberOfAdultTickets = -1;
    public int numberOfChildTickets = -1;

    //Tickets
    public boolean isFloridaResident = false;
    public int numberOfDays = -1; // For annual pass set to FLAG_ANNUAL_PASS

    //Express
    public int numberOfParks = -1;
    public List<Date> dates;
    public boolean hasExpressBeenChangedManually = false;
    public int numExpressTickets = -1;

    public PooControl.CheckedState checkedState = PooControl.CheckedState.USE_DEFAULT;

    //Calendar
    public CalendarCellSeasonState calendarCellSeasonState;

    //Ui
    public List<CommerceCard> cards;
    public String currentServiceIdentifier;//String sent to server to determine the UI controls and cards to display

    public TicketFilterInfo() {
    }

    public void resetAllFilters() {
        isFloridaResident = false;
        numberOfDays = -1;
        numberOfAdultTickets = -1;
        numberOfChildTickets = -1;
        numberOfParks = -1;
        numExpressTickets = -1;
        hasExpressBeenChangedManually = false;
        dates = null;
        calendarCellSeasonState = CalendarCellSeasonState.NONE;
    }

    public void setFilterForTickets(int days, int numAdultTickets, int numChildTickets, boolean isFloridaResident) {
        resetAllFilters();
        this.numberOfDays = days;
        this.numberOfAdultTickets = numAdultTickets;
        this.numberOfChildTickets = numChildTickets;
        this.isFloridaResident = isFloridaResident;
    }

    public void setFilterForExpress(int numParks, int numTickets, List<Date> dates) {
        resetAllFilters();
        this.numberOfParks = numParks;
        this.numExpressTickets = numTickets;
        this.dates = dates;
    }

    //TODO address with TA23460
    public String getTicketFilterString() {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();

        StringBuilder sb = new StringBuilder();
        if (numberOfDays != FLAG_ANNUAL_PASS) {
            sb.append(numberOfDays);
            if (numberOfDays > 1) {
                sb.append(" ")
                        .append(tridionConfig.getDaysLabel())
                        .append(", ");
            } else {
                sb.append(" ")
                        .append(tridionConfig.getDayLabel())
                        .append(", ");
            }

        } else {
            sb.append(tridionConfig.getSCAnnualPassLabel())
            .append(", ");
        }
        if (numberOfAdultTickets != -1) {
            sb.append(numberOfAdultTickets)
                    .append(" ")
                    .append(numberOfAdultTickets == 1 ? tridionConfig.getAdultLabel() : tridionConfig.getAdultsLabel())
                    .append(", ");
        }
                sb.append(numberOfChildTickets)
                    .append(" ")
                    .append(numberOfChildTickets == 1 ? tridionConfig.getChildLabel() : tridionConfig.getChildrenLabel());

                if (isFloridaResident) {
                    sb.append(", ")
                        .append(tridionConfig.getFlResidentLabel());
                }
        return sb.toString();
    }

    //TODO address with TA23460
    public String getUEPFilterString() {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        StringBuilder sb = new StringBuilder();

        sb.append(numberOfParks);
        if (numberOfParks > 1) {
            sb.append(" ")
                    .append(tridionConfig.getParksLabel())
                    .append(", ");
        } else {
            sb.append(" ")
                    .append(tridionConfig.getParkLabel())
                    .append(", ");
        }

        //TODO change this logic once we have multiple day passes for UEP
        int days = 1;
        sb.append(days);
        if (days > 1) {
            sb.append(" ")
                    .append(tridionConfig.getDaysLabel())
                    .append(", ");
        } else {
            sb.append(" ")
                    .append(tridionConfig.getDayLabel())
                    .append(", ");
        }

        sb.append(numExpressTickets);
        if (numExpressTickets > 1) {
            sb.append(" ")
                    .append(tridionConfig.getGuestsLabel());
        } else {
            sb.append(" ")
                    .append(tridionConfig.getGuestLabel());
        }

        return sb.toString();
    }

    public int getNumberOfAdultTickets() {
        return numberOfAdultTickets;
    }

    public int getNumberOfChildTickets() {
        return numberOfChildTickets;
    }

    public int getNumExpressTickets() { return numExpressTickets; }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public boolean isFloridaResident() {
        return isFloridaResident;
    }

    public void clearTicketsSettings(){
        numberOfDays = -1;
        numberOfAdultTickets = -1;
        numberOfChildTickets = -1;
        calendarCellSeasonState = CalendarCellSeasonState.NONE;
        isFloridaResident = false;
        CommerceUiBuilder.setCurrentIdentifier(null);
    }

    public void clearExpressPassSettings(){
        numberOfParks = -1;
        numExpressTickets = -1;
        hasExpressBeenChangedManually = false;
        //dates = null;
        CommerceUiBuilder.setCurrentIdentifier(null);
    }
}
