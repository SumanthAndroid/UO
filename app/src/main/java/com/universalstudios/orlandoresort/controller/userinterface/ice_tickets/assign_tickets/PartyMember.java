package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.text.TextUtils;

import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/10/16.
 * Class: PartyMember
 * Class Description: TODO: ALWAYS FILL OUT
 */
public class PartyMember extends GsonObject {
    public static final String TAG = "PartyMember";
    public static final int AGE_TYPE_ADULT = 0;
    public static final int AGE_TYPE_CHILD = 1;

    public PartyMember(int ageType) {
        this.ageType = ageType;
    }

    public int ageType = -1;
    public String firstname;
    public String lastname;
    public String suffix;
    public List<OrderItem> orderItems;


    public boolean doNamesMatch(Object partyMemberObject) {
        if (partyMemberObject == null){
            return false;
        }

        PartyMember fellowPartyMember = (PartyMember) partyMemberObject;
        if(null != firstname && null != lastname){
            return firstname.equals(fellowPartyMember.firstname) &&
                    lastname.equals(fellowPartyMember.lastname) &&
                    ((null == suffix && null == fellowPartyMember.suffix) || suffix.equals(fellowPartyMember.suffix));
        }

       return false;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(firstname) && TextUtils.isEmpty(lastname) && TextUtils.isEmpty(suffix);
    }
}
