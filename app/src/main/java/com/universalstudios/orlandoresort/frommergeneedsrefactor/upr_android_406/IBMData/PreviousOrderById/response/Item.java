package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IBM_ADMIN on 2/12/2016.
 */
@Parcel
public class Item extends GsonObject {

    @SerializedName("relatedPartNumber")
    String relatedPartNumber;

    @SerializedName("attributes")
    List<CommerceAttribute> attributes = new ArrayList<>();

    @SerializedName("name")
    String name;

    @SerializedName("id")
    String id;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("description")
    String description;

    @SerializedName("contractId")
    String contractId;

    /**
     *
     * @return
     * The relatedPartNumber
     */
    public String getRelatedPartNumber() {
        return relatedPartNumber;
    }

    /**
     *
     * @param relatedPartNumber
     * The relatedPartNumber
     */
    public void setRelatedPartNumber(String relatedPartNumber) {
        this.relatedPartNumber = relatedPartNumber;
    }

    /**
     *
     * @return
     * The attributes
     */
    public List<CommerceAttribute> getAttributes() {
        return attributes;
    }

    /**
     *
     * @param attributes
     * The attributes
     */
    public void setAttributes(List<CommerceAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The partNumber
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     *
     * @param partNumber
     * The partNumber
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The contractId
     */
    public String getContractId() {
        return contractId;
    }

    /**
     *
     * @param contractId
     * The contractId
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getTcmId1() {
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

    /**
     *
     * @return
     */
    public String getTcmId2() {
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

    // Helper methods

    @Nullable
    public String getAttributeParkNum() {
        String parkNum = null;
        if (attributes != null) {
            for(CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isParkNum()) {
                    parkNum = attribute.getValue();
                    break;
                }
            }
        }
        return parkNum;
    }

    @Nullable
    public String getAttributeAge() {
        String age = null;
        if (attributes != null) {
            for(CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isAge()) {
                    age = attribute.getValue();
                    break;
                }
            }
        }
        return age;
    }

    public int getMaxQuantity() {
        int max = CommerceAttribute.DEFAULT_MAX_QUANTITY;
        for (CommerceAttribute attribute : attributes) {
            if (attribute.isMaxQuantity()) {
                max = attribute.getMaxQuantity();
                break;
            }
        }

        return max;
    }

    public int getMinQuantity() {
        int min = CommerceAttribute.DEFAULT_MIN_QUANTITY;
        for (CommerceAttribute attribute : attributes) {
            if (attribute.isMinQuantity()) {
                min = attribute.getMinQuantity();
                break;
            }
        }

        return min;
    }

    /**
     * Method to get the item type from the {@link #attributes} list.
     * @return
     */
    public String getItemType() {
        String itemType = "";
        for (CommerceAttribute attribute : getAttributes()) {
            if (null != attribute && attribute.isItemType()) {
                itemType = attribute.getValue();
                break;
            }
        }
        return itemType;
    }

    public boolean isAnnualPass() {
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isItemType() && attribute.isItemTypeAnnualPass()) {
                    return true;
                }
            }
        }
        return false;
    }

}
