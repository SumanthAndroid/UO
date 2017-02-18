package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Item;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;

import org.parceler.Parcel;

/**
 * @author jamestimberlake
 * @created 7/28/16.
 */
@Parcel
public class ExpressPassTicketGroups extends GsonObject {

    @SerializedName("numParks")
    String numParks;

    @SerializedName("totalPrice")
    BigDecimal totalPrice;

    @SerializedName("unitPrice")
    BigDecimal unitPrice;

    @SerializedName("quantity")
    Integer quantity;

    @SerializedName("expiration")
    List<String> expiration;

    @SerializedName("product")
    Product product;

    @SerializedName("orderItems")
    List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @SerializedName("item")
    Item item;

    @SerializedName("totalPriceSavings")
    BigDecimal totalPriceSavings;

    @SerializedName("parkName")
    String parkName;

    @SerializedName("useDate")
    String useDate;

    @SerializedName("usesAllowed")
    String usesAllowed;

    @SerializedName("description")
    String description;

    @SerializedName("groupingKey")
    String groupingKey;

    public String getNumParks() {
        return numParks;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<String> getExpiration() {
        return expiration;
    }

    public Product getProduct() {
        return product;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Item getItem() {
        return item;
    }

    public BigDecimal getTotalPriceSavings() {
        return totalPriceSavings;
    }

    public String getParkName() {
        return parkName;
    }

    public String getUseDate() {
        return useDate;
    }

    public String getUsesAllowed() {
        return usesAllowed;
    }

    public String getDescription() {
        return description;
    }

    public String getGroupingKey() {
        return groupingKey;
    }

    public List<CommerceAttribute> getOrderItemAttributes() {
        List<OrderItem> orderItems = getOrderItems();
        if (orderItems != null){
            OrderItem orderItem = orderItems.get(0);
            if (orderItem != null){
                return orderItem.getAttributes();
            }
        }
        return null;
    }

    public int getMaxQuantity() {
        int max = CommerceAttribute.DEFAULT_MAX_QUANTITY;
        if (null != getItem()) {
            max = getItem().getMaxQuantity();
        }
        return max;
    }

    public int getMinQuantity() {
        int max = CommerceAttribute.DEFAULT_MIN_QUANTITY;
        if (null != getItem()) {
            max = getItem().getMinQuantity();
        }
        return max;
    }

    /**
     * Returns true if this item's quantity can be updated
     */
    public boolean isQuantityChangeAllowed() {
        boolean isQuantityChangeAllowed = false;
        if (item != null && item.getAttributes() != null) {
            for (CommerceAttribute attribute : item.getAttributes()) {
                if (attribute != null && attribute.isQuantityChangeAllowed()) {
                    isQuantityChangeAllowed = true;
                    break;
                }
            }
        }

        return isQuantityChangeAllowed;
    }

    /**
     * Determine if ticket has any unassigned order items
     *
     * @return returns true if there is an unassigned order item
     */
    public boolean hasUnassignedOrderItem() {
        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                if (orderItem != null && !orderItem.isAssigned()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFlexPay() {
        boolean isFlexPay = false;
        if (null != orderItems) {
            for (OrderItem item : orderItems) {
                if (null != item && item.isFlexPay()) {
                    isFlexPay = true;
                    break;
                }
            }
        }
        return isFlexPay;
    }

    public boolean shouldShowSavingsMessage() {
        boolean showSavingsMessage = false;

        List<CommerceAttribute> attributes = new ArrayList<>();
        if (this.getOrderItemAttributes() != null) {
            attributes.addAll(this.getOrderItemAttributes());
        }

        for (CommerceAttribute attribute : attributes) {
            if (attribute != null && attribute.shouldShowSavingsMessage()) {
                showSavingsMessage = true;
                break;
            }
        }
        return showSavingsMessage && getPriceDifference().compareTo(BigDecimal.ZERO) >= 0;
    }

    @NonNull
    private BigDecimal getPriceDifference()
    {
        BigDecimal priceDifference = BigDecimal.ZERO;

        if(getOrderItems() != null) {
            for(OrderItem item : getOrderItems()) {
                priceDifference.add(item.getOfferPriceDifference());
            }
        }

        return priceDifference;
    }

}
