package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.ArrayList;
import java.util.List;

public class Item extends GsonObject {

    @SerializedName("CartItemAttributes")
    private List<CommerceAttribute> CartItemAttributes = new ArrayList<>();

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("partNumber")
    private String partNumber;

    @SerializedName("description")
    private String description;

    @SerializedName("contractId")
    private String contractId;

    @SerializedName("attributes")
    private List<CommerceAttribute> attributes = new ArrayList<>();

    /**
     * @return The CartItemAttributes
     */
    public List<CommerceAttribute> getCartItemAttributes() {
        return CartItemAttributes;
    }

    /**
     * @param CartItemAttributes The CartItemAttributes
     */
    public void setCartItemAttributes(List<CommerceAttribute> CartItemAttributes) {
        this.CartItemAttributes = CartItemAttributes;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The partNumber
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * @param partNumber The partNumber
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The contractId
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param contractId The contractId
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public List<CommerceAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CommerceAttribute> attributes) {
        this.attributes = attributes;
    }

}
