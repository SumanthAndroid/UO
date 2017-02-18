package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Pricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Address;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/1/16.
 * Class: OrderItem
 * Class Description: Data to send to server for an order item
 */
@Parcel
public class OrderItem extends GsonObject {

    // Logging Tag
    private static final String TAG = OrderItem.class.getSimpleName();

    // Date Formatter for a date like 2016-10-05 09:15:41.056
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    private static SimpleDateFormat ORDER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 00:00:01", Locale.US);

    @SerializedName("attributes")
    List<CommerceAttribute> attributes = new ArrayList<>();

    @SerializedName("status")
    String status;

    @SerializedName("isFreeGift")
    String isFreeGift;

    @SerializedName("pricing")
    Pricing pricing;

    @SerializedName("createDate")
    String createDate;

    @SerializedName("quantity")
    Integer quantity;

    @SerializedName("orderItemId")
    String orderItemId;

    @SerializedName("lastUpdate")
    String lastUpdate;

    @SerializedName("deliveryMethod")
    String deliveryMethod;

    @SerializedName("inventoryStatus")
    String inventoryStatus;

    @SerializedName("messages")
    List<CommerceMessage> messages;

    @SerializedName("address")
    Address address;

    @SerializedName("productId")
    String productId;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("eventId")
    String eventId;

    @SerializedName("addressId")
    String addressId;

    /**
     * Method to get the {@link Date} object from the model's formatted date Strings.
     *
     * @param dateString
     *         the String to convert to a {@link Date} object
     *
     * @return the {@link Date} object from the formatted date String
     */
    private Date getFormattedDate(String dateString) {
        if (!TextUtils.isEmpty(dateString)) {
            try {
                return sdf.parse(dateString);
            }
            catch (ParseException e) {
                Log.e(TAG, "getFormattedDate: ", e);

            }
            return null;
        }
        return null;
    }

    /**
     * Method to get the assigned name from the {@link #getAssignedFirstName()} method and {@link
     * #getAssignedLastName()} method.
     *
     * @return a String containing the combined first and last name retrieved from the {@link
     * #attributes}.
     */
    public String getAssignedName() {
        StringBuilder nameBuilder = new StringBuilder(getAssignedFirstName())
                .append(" ")
                .append(getAssignedLastName());
        String suffix = getSuffix();
        if(!TextUtils.isEmpty(suffix)) {
            nameBuilder.append(" ")
                    .append(suffix);
        }

        return nameBuilder.toString().trim();
    }

    /**
     * Method to get the first name from the {@link #attributes} list.
     *
     * @return a String containing the first name of the party member.
     */
    public String getAssignedFirstName() {
        for (CommerceAttribute attribute : attributes) {
            if (attribute != null && attribute.isAssignmentFirstName()) {
                return attribute.getValue();
            }
        }

        return "";
    }

    /**
     * Method to get the last name from the {@link #attributes} list.
     *
     * @return a String containing the last name of the party member.
     */
    public String getAssignedLastName() {
        for (CommerceAttribute attribute : attributes) {
            if (attribute != null && attribute.isAssignmentLastName()) {
                return attribute.getValue();
            }
        }

        return "";
    }

    /**
     * Method to get the suffix of the party member from the {@link #attributes} list.
     *
     * @return a String containing the suffix of the party member.
     */
    public String getSuffix() {
        for (CommerceAttribute attribute : attributes) {
            if (null != attribute && attribute.isAssignmentSuffix()) {
                return attribute.getValue();
            }
        }

        return "";
    }

    /**
     * Method to get the DOB of the party member from the {@link #attributes} list.
     *
     * @return a String containing the DOB of the party member.
     */
    public String getPartyMemberBirthDate() {
        for (CommerceAttribute attribute : attributes) {
            if (null != attribute && attribute.isPartyMemberBirthDate()) {
                return attribute.getValue();
            }
        }
        return "";
    }

    /**
     * Method to get the suffix of the party member id from the {@link #attributes} list.
     *
     * @return a String containing the party_member_id/sequence_id of the party member.
     */
    public Integer getPartyMemberId() {
        for (CommerceAttribute attribute : attributes) {
            try {
                if (null != attribute && attribute.isAssignmentMemberId()) {
                    return Integer.parseInt(attribute.getValue());
                }
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }

    /**
     * Method to set the product id of the order item.
     *
     * @param productId
     *         a String containing the product id of the order item
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Method to get the quantity
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = new Integer(quantity);
    }

    /**
     * Method to get a {@link List<CommerceAttribute>} list of attributes for the order item.
     *
     * @return a {@link List<CommerceAttribute>} list of attributes for the order item
     */
    public List<CommerceAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Method to set the {@link List<CommerceAttribute>} list of attributes for the order item.
     *
     * @param attributes
     *         a {@link List<CommerceAttribute>} list of attributes for the order item
     */
    public void setAttributes(List<CommerceAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Method to get the status of the order item. (i.e. "P")
     *
     * @return a String containing the status of the order item
     */
    public String getStatus() {
        return status;
    }

    /**
     * Method to get whether or not the order item is a free gift. (i.e. "false")
     *
     * @return a String representing a boolean of whether or not the order item is a free gift
     */
    public String getIsFreeGift() {
        return isFreeGift;
    }

    /**
     * Method to get the {@link Pricing} of the order item.
     *
     * @return the {@link Pricing} object for the pricing of the order item
     */
    public Pricing getPricing() {
        return pricing;
    }

    /**
     * Method to get the {@link Date} create date of the order item. Note this is different from
     * {@link #getCreateDateString()} which returns the formatted date String.
     *
     * @return a {@link Date} object representing the order item's create date
     */
    public Date getCreatedDate() {
        return getFormattedDate(this.createDate);
    }

    /**
     * Method to get the formatted date String of the order item's created date.
     * <p/>
     * (i.e. "2016-10-05 09:15:41.056") Note this is different from {@link #getCreatedDate()} which
     * returns the {@link Date} object.
     *
     * @return a formatted date String representing the order item's created date
     */
    public String getCreateDateString() {
        return createDate;
    }

    /**
     * Method to get the quantity for this order item.
     *
     * @return the int representing the quantity of this order item
     */
    public int getQuantity() {
        return quantity != null ? quantity.intValue() : 0;
    }

    /**
     * Method to get the order item's id.
     *
     * @return a String containing the order items id
     */
    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * Method to get the {@link Date} last update date of the order item. Note this is different
     * from {@link #getLastUpdateString()} which returns the formatted date String.
     *
     * @return a {@link Date} object representing the order item's last update date
     */
    public Date getLastUpdate() {
        return getFormattedDate(this.lastUpdate);
    }

    /**
     * Method to get the formatted date String of the order item's last update date.
     * <p/>
     * (i.e. "2016-10-05 09:15:41.056") Note this is different from {@link #getLastUpdate()} which
     * returns the {@link Date} object.
     *
     * @return a formatted date String representing the order item's last update date
     */
    public String getLastUpdateString() {
        return lastUpdate;
    }

    /**
     * Method to get the delivery method for the order item.
     *
     * @return a String containing the delivery method for the order item
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Method to set the delivery method for the order item.
     *
     * @param deliveryMethod
     *         a String containing the delivery method for the order item
     */
    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    /**
     * Method to get the inventory status for the order item (i.e. "AVL")
     *
     * @return a String containing the order item's inventory status
     */
    public String getInventoryStatus() {
        return inventoryStatus;
    }

    /**
     * Method to get the commerce messages for the order item. These will generally represent
     * inventory errors, such as an order item no longer existing in inventory.
     *
     * @return a {@link ArrayList<CommerceMessage>} list of commerce messages for the order item
     */
    public List<CommerceMessage> getMessages() {
        return messages;
    }

    /**
     * Method to get the {@link Address} for the order item.
     *
     * @return a {@link Address} object representing the address for the order item
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Method to get the product id of the order item.
     *
     * @return a String containing the order item's product id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Method to get the part number for the order item. (i.e. "TPA-12M_SEA_2P_AD_AP_FL")
     *
     * @return a String containing the part number of the order item
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * Method to set the part number for the order item. (i.e. "TPA-12M_SEA_2P_AD_AP_FL")
     *
     * @param partNumber
     *         a String containing the part number of the order item
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setEventId(String eventId) { this.eventId = eventId; }

    /**
     * Sets the date for date based ordwer items. Without this attribute set, we cannot
     * get the correct use date for uep or seasonal tickets. Dates ned to be saved in
     * yyyy-MM-dd 00:00:01 format
     * @param date
     */
    public void setOrderDate(Date date){
        if(date == null){
            return;
        }

        List<CommerceAttribute> attributes;
        attributes = getAttributes() != null ? getAttributes() : new ArrayList<CommerceAttribute>();
        attributes.add(CommerceAttribute.createDateAttribute(date));
        setAttributes(attributes);
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    // Helper methods

    @Nullable
    public Date getAttributeDate() {
        Date date = null;
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isDate()) {
                    date = attribute.getDate();
                    break;
                }
            }
        }
        return date;
    }

    @Nullable
    public String getAttributeAge() {
        String age = null;
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isAge()) {
                    age = attribute.getValue();
                    break;
                }
            }
        }
        return age;
    }

    @Nullable
    public String getAttributeRow() {
        String row = null;
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isRow()) {
                    row = attribute.getValue();
                    break;
                }
            }
        }
        return row;
    }

    @Nullable
    public String getAttributeSeatLow() {
        String seat = null;
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isSeatLow()) {
                    seat = attribute.getValue();
                    break;
                }
            }
        }
        return seat;
    }

    @Nullable
    public String getAttributeSeatHigh() {
        String seat = null;
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isSeatHigh()) {
                    seat = attribute.getValue();
                    break;
                }
            }
        }
        return seat;
    }

    public boolean isFlexPay() {
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute != null && attribute.isFlexPay()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAssigned() {
        if (attributes != null) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute.isAssignmentFirstName() && !TextUtils.isEmpty(attribute.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public BigDecimal getOfferPriceDifference() {
        if(pricing == null ||
                pricing.getListPrice() == null ||
                pricing.getOrderItemPrice() == null) {
            return BigDecimal.ZERO;
        }

        return pricing.getListPrice().subtract(pricing.getOrderItemPrice());
    }

}