package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import android.support.annotation.NonNull;
import android.support.multidex.BuildConfig;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceCardItem
 * Class Description: Card item reference for each group
 */
@Parcel
public class CommerceCardItem extends GsonObject {
    public static final String TAG = CommerceCardItem.class.getSimpleName();

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("longDescription")
    String longDescription;

    @SerializedName("buyable")
    boolean isBuyable;

    @SerializedName("attributes")
    List<CommerceAttribute> attributes;

    @SerializedName("pricingAndInventory")
    CommercePricingAndInventory pricingAndInventory;

    @SerializedName("id")
    long id;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("components")
    List<CommerceCardItemComponent> components;

    public long getId() {
        return id;
    }

    public boolean isAdult() {
        if (components != null) {
            for (CommerceCardItemComponent component : components) {
                if (component != null) {
                    if (component.isAgeAdult()) {
                        return true;
                    }
                }
            }
        }
        for (CommerceAttribute attribute : getAttributes()) {
            if (attribute.isAgeAdult()) {
                return true;
            }
        }
        return false;
    }

    public boolean isChild() {
        if (components != null) {
            for (CommerceCardItemComponent component : components) {
                if (component != null) {
                    if (component.isAgeChild()) {
                        return true;
                    }
                }
            }
        }
        for (CommerceAttribute attribute : getAttributes()) {
            if (attribute.isAgeChild()) {
                return true;
            }
        }
        return false;
    }

    public String getIdForTridion() {
        if (null == attributes || attributes.isEmpty()) {
            return null;
        }

        for (CommerceAttribute attribute : attributes) {
            if (null != attribute && attribute.isTcmId1()) {
                return attribute.getValue();
            }
        }
        return null;
    }

    public String getDetailIdForTridion() {
        if (null == attributes || attributes.isEmpty()) {
            return null;
        }

        for (CommerceAttribute attribute : attributes) {
            if (null != attribute && attribute.isTcmId2()) {
                return attribute.getValue();
            }
        }
        return null;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public boolean isBuyable() {
        return isBuyable;
    }

    @NonNull
    public List<CommerceAttribute> getAttributes() {
        if (null == attributes) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    @NonNull
    public List<CommerceAttribute> getComponentAttributes() {
        List<CommerceAttribute> componentAttributes = new ArrayList<>();
        if (components != null) {
            for (CommerceCardItemComponent component : components) {
                if (component != null && component.getAttributes() != null) {
                    componentAttributes.addAll(component.getAttributes());
                }
            }
        }
        return componentAttributes;
    }

    public CommercePricingAndInventory getPricingAndInventory() {
        return pricingAndInventory;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public List<CommerceCardItemComponent> getComponents() {
        return components;
    }

    public boolean isFloridaPoo() {
        boolean floridaPoo = false;
        for (CommerceAttribute attribute : getAttributes()) {
            if (null != attribute && attribute.isFloridaPoo()) {
                floridaPoo = true;
                break;
            }
        }
        return floridaPoo;
    }

    public int getNumDays() {
        int numDays = -1;
        for (CommerceAttribute attribute : getAttributes()) {
            if (null != attribute && attribute.isNumDays()) {
                try {
                    numDays = Integer.parseInt(attribute.getValue());
                    break;
                } catch(NumberFormatException ex){
                    if(BuildConfig.DEBUG) {
                        Log.e(TAG, "The number of days is not giving a string we can parse into an integer");
                    }
                }
            }
        }
        return numDays;
    }

    public int getNumParks() {
        int numParks = -1;
        for (CommerceAttribute attribute : getAttributes()) {
            if (null != attribute && attribute.isNumParks()) {
                numParks = Integer.parseInt(attribute.getValue());
                break;
            }
        }
        return numParks;
    }

    public boolean hasBestOffer() {
        boolean isBestOffer = false;
        if (null != getAttributes()) {
            for (CommerceAttribute attribute : getAttributes()) {
                if (null != attribute && attribute.isBestValueYes()) {
                    isBestOffer = true;
                    break;
                }
            }
        }
        return isBestOffer;
    }
}
