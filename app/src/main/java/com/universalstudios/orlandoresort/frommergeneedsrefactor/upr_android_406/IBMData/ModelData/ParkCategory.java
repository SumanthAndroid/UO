package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ModelData;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ibm_admin on 1/19/2016.
 */
public class ParkCategory extends GsonObject implements Serializable {

    public enum ParkType {
        SINGLE_PARK,
        PARK_TO_PARK,
        THREE_PARK,
        EXPRESS
    }

    @SerializedName("parkType")
    private ParkType parkType;

    @SerializedName("adults")
    private List<Ticket> adults;

    @SerializedName("childs")
    private List<Ticket> childs;

    @SerializedName("express")
    private List<Ticket> expressTickets;

    public ParkCategory(ParkType parkType) {
        this.parkType = parkType;
        adults = new ArrayList<Ticket>();
        childs = new ArrayList<Ticket>();

    }

    public void addAdultTicket(Ticket t){
        adults.add(t);
    }
    public void addchildTicket(Ticket t){
        childs.add(t);
    }

    public ParkType getParkType() {
        return parkType;
    }

    public List<Ticket> getAdults() {
        return adults;
    }

    public List<Ticket> getChilds() {
        return childs;
    }
    public List<Ticket> getAdultTicket()
    {
        return adults;
    }

    public List<Ticket> getChildTicket()
    {
        return childs;
    }

    public void addExpressTicket(Ticket ticket) {
        if (null == expressTickets) {
            expressTickets = new ArrayList<>();
        }
        expressTickets.add(ticket);
    }

    public List<Ticket> getExpressTickets() {
        return expressTickets;
    }

    /*
    *
    * Sort Array on days attribute
    *
    * */
    public void sortArry() {

        Collections.sort(adults, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket s1, Ticket s2) {
                return Integer.parseInt(s2.getDays()) - Integer.parseInt(s1.getDays());
            }
        });
        Collections.sort(childs, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket s1, Ticket s2) {
                return Integer.parseInt(s2.getDays()) - Integer.parseInt(s1.getDays());
            }
        });
    }
}
