package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Pricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Item;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Product;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargavi on 6/8/16.
 */
@Parcel
public class Ticket extends GsonObject {

    @SerializedName("orderItems")
    List<OrderItem> orderItems = new ArrayList<>();

    @SerializedName("item")
    Item item;

    @SerializedName("totalPriceSavings")
    BigDecimal totalPriceSavings;

    @SerializedName("unitPrice")
    BigDecimal unitPrice;

    @SerializedName("totalPrice")
    BigDecimal totalPrice;

    @SerializedName("quantity")
    Integer quantity;

    @SerializedName("product")
    Product product;

    public BigDecimal getTotalPriceSavings() {
        if (totalPriceSavings != null) {
            return totalPriceSavings;
        }
        return new BigDecimal(0.0);
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        if (totalPrice != null) {
            return totalPrice;
        }
        return new BigDecimal(0.0);
    }

    public int getQuantity() {
        if (quantity != null) {
            return quantity;
        }
        return 0;
    }

    public Product getProduct() {
        return product;
    }

    public Item getItem() {
        return item;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public List<CommerceAttribute> getOrderItemAttributes() {
        List<OrderItem> orderItems = getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()){
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

    public boolean isFlexPay() {
        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                if (orderItem != null && orderItem.isFlexPay()) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getMinQuantity() {
        int min = CommerceAttribute.DEFAULT_MIN_QUANTITY;
        if (null != getItem()) {
            min = getItem().getMinQuantity();
        }
        return min;
    }

    public boolean isAnnualPass() {
        if (item != null) {
            return item.isAnnualPass();
        }
        return false;
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



}
