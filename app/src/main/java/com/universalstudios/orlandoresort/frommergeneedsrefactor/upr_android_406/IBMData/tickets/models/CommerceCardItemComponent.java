package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Card item component
 *
 * @author acampbell
 */
@Parcel
public class CommerceCardItemComponent {

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("longDescription")
    String longDescription;

    @SerializedName("attributes")
    List<CommerceAttribute> attributes;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("partNumber")
    String partNumber;

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public List<CommerceAttribute> getAttributes() {
        return attributes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public boolean isItemTypeTicket () {
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isItemTypeTicket()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAgeAdult() {
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isAgeAdult()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAgeChild() {
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isAgeChild()) {
                    return true;
                }
            }
        }
        return false;
    }

}
