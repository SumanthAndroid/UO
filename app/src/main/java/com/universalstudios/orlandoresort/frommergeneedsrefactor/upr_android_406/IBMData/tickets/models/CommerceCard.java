package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceCard
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class CommerceCard extends GsonObject {

    @SerializedName("id")
    String id;

    @SerializedName("groups")
    List<CommerceGroup> groups;

    @SerializedName("properties")
    CommerceProperty property;

    @SerializedName("title")
    String cardTitle;

    @SerializedName("description")
    String cardDescription;

    @SerializedName("header")
    String header;

    @SerializedName("isInjected")
    boolean isInjected;

    @SerializedName("offer")
    Offer offer;

    @SerializedName("CardGroupsUseSameSKU")
    String cardGroupsUseSameSKU;

    public String getId() {
        return id;
    }

    public List<CommerceGroup> getGroups() {
        return groups;
    }

    public CommerceProperty getProperty() {
        return property;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getHeader() {
        return header;
    }

    public boolean isInjected() {
        return isInjected;
    }

    public Offer getOffer() {
        return offer;
    }

    public boolean isCardGroupsUseSameSKU(){
        return Boolean.parseBoolean(cardGroupsUseSameSKU);
    }

    public boolean isAnnualPass(){
        boolean isAnnualPass = false;
        if (this.getGroups() != null && this.getGroups().size() > 0) {
            CommerceGroup commerceGroup = this.getGroups().get(0);
            if (commerceGroup != null) {
                if (commerceGroup.isAnnualPass()) {
                    isAnnualPass = true;
                }
            }
        }

        return isAnnualPass;
    }

    public boolean isFloridaPoo(){
        boolean isFloridaPoo = false;
        if (this.getGroups() != null && this.getGroups().size() > 0) {
            CommerceGroup commerceGroup = this.getGroups().get(0);
            if (commerceGroup != null && commerceGroup.getCardItems().size() > 0) {
                CommerceCardItem commerceCardItem = commerceGroup.getCardItems().get(0);
                isFloridaPoo = commerceCardItem.isFloridaPoo();
            }
        }

        return isFloridaPoo;
    }

    public List<String> getAnnualPassFooterStrings() {
        //use map and loop through twice to avoid duplicates
        Map<String, CommerceGroup> tridionCardMap = new HashMap<>();
        if (getGroups() != null) {
            for (CommerceGroup group : getGroups()) {
                if (group != null && group.hasPaymentPlans()) {
                    List<CommerceCardItem> cardItems = group.getCardItems();
                    for (CommerceCardItem cardItem : cardItems) {
                        if (cardItem != null) {
                            tridionCardMap.put(cardItem.getIdForTridion(), group);
                        }
                    }
                }
            }
        }

        List<String> footerItems = new ArrayList<>();
        for (String key : tridionCardMap.keySet()) {
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(key);
            footerItems.add(labelSpec.getPurchaseOptionLegal(tridionCardMap.get(key)));
        }

        return footerItems;
    }
}
