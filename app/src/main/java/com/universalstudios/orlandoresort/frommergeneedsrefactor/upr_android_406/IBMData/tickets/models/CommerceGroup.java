package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.PaymentPlan;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceGroup
 * Class Description: Displayable ticket item to show the user
 */
@Parcel
public class CommerceGroup extends GsonObject {

    @SerializedName("items")
    List<CommerceCardItem> cardItems;

    @SerializedName("title")
    String title;

    @SerializedName("relationship")
    String relationship;

    @SerializedName("type")
    String type;

    @SerializedName("description")
    String description;

    @SerializedName("isFlexPayGroup")
    boolean flexPayGroup;

    @SerializedName("paymentPlans")
    List<PaymentPlan> paymentPlans;

    @SerializedName("startDate")
    String startDate;

    @SerializedName("endDate")
    String endDate;

    public boolean isFlexPayGroup() {
        return flexPayGroup;
    }

    public String getTitle() {
        return title;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @NonNull
    public List<CommerceCardItem> getCardItems() {
        List<CommerceCardItem> cardItems = this.cardItems;
        if (null == cardItems) {
            cardItems = new ArrayList<>();
        }
        return cardItems;
    }

    public CommerceCardItem getAdultItem() {
        CommerceCardItem cardItem = null;
        for (CommerceCardItem item : getCardItems()) {
            if (item.isAdult()) {
                cardItem = item;
                break;
            }
        }
        if (null == cardItem && !getCardItems().isEmpty()) {
            // If no adult found, assume the first one is an adult (for UEP)
            cardItem = getCardItems().get(0);
        }
        return cardItem;
    }

    public CommerceCardItem getChildItem() {
        CommerceCardItem cardItem = null;
        for (CommerceCardItem item : getCardItems()) {
            if (item.isChild()) {
                cardItem = item;
                break;
            }
        }
        return cardItem;
    }

    public CommercePricingAndInventory getAdultPricingAndInventory() {
        CommerceCardItem item = getAdultItem();
        if (null != item) {
           return item.getPricingAndInventory();
        }
        return null;
    }

    public CommercePricingAndInventory getChildPricingAndInventory() {
        CommerceCardItem item = getChildItem();
        if (null != item) {
            return item.getPricingAndInventory();
        }
        return null;
    }

    public BigDecimal getOnlineSavingsPrice(){
        if (null == cardItems || cardItems.isEmpty()) {
            return null;
        }
        CommerceCardItem item = cardItems.get(0);

        BigDecimal listPrice = null;
        BigDecimal offerPrice = null;

        if (null != item &&
                item.getPricingAndInventory() != null &&
                item.getPricingAndInventory().getOfferPricingAndInventory() != null &&
                !item.getPricingAndInventory().getOfferPricingAndInventory().isEmpty()) {
            
            listPrice = item.getPricingAndInventory().listPrice;

            int arraySize = item.getPricingAndInventory().getOfferPricingAndInventory().values().size();
            Collection<CommerceOfferPricingAndInventory> offerPricingAndInventoryCollection = item.getPricingAndInventory().getOfferPricingAndInventory().values();

            CommerceOfferPricingAndInventory[] pricingInventoryItems = offerPricingAndInventoryCollection.toArray(new CommerceOfferPricingAndInventory[arraySize]);

            if(pricingInventoryItems[0] != null){
                offerPrice = pricingInventoryItems[0].getOfferPrice();
            }
        }

        if(listPrice != null && offerPrice != null) {
            return listPrice.subtract(offerPrice).abs();
        }

        return BigDecimal.valueOf(-1f);
    }

    public int getNumParks() {
        int numParks = -1;
        for (CommerceCardItem cardItem : cardItems) {
            if (null != cardItem) {
                numParks = cardItem.getNumParks();
                if (numParks >= 0) break;
            }
        }
        return numParks;
    }

    public boolean isAnnualPass() {
        boolean isAnnualPass = false;
        if (cardItems != null && cardItems.size() > 0) {
            CommerceCardItem commerceCardItem = cardItems.get(0);
            if (commerceCardItem != null && commerceCardItem.getAttributes() != null) {
                List<CommerceAttribute> attributes = commerceCardItem.getAttributes();
                for (CommerceAttribute attribute : attributes) {
                    if (attribute != null && attribute.isItemType()) {
                        isAnnualPass = attribute.isItemTypeAnnualPass();
                        break;
                    }
                }
            }
        }
        return isAnnualPass;
    }

    public BigDecimal getDownPaymentValue() {
        if (paymentPlans != null && paymentPlans.size() >0){
            for (PaymentPlan paymentPlan : paymentPlans) {
                if (paymentPlan != null){
                    return paymentPlan.getDownPayment();
                }
            }
        }
        return null;
    }

    public BigDecimal recurringPaymentAmount() {
        if (paymentPlans != null && paymentPlans.size() >0){
            for (PaymentPlan paymentPlan : paymentPlans) {
                if (paymentPlan != null){
                    return paymentPlan.getRecurringPaymentAmount();
                }
            }
        }
        return null;
    }

    public BigDecimal getFinancedAmount() {
        if (paymentPlans != null && paymentPlans.size() >0){
            for (PaymentPlan paymentPlan : paymentPlans) {
                if (paymentPlan != null){
                    return paymentPlan.getFinancedAmount();
                }
            }
        }
        return null;
    }

    public int getRecurringPaymentCount() {
        if (paymentPlans != null && paymentPlans.size() >0){
            for (PaymentPlan paymentPlan : paymentPlans) {
                if (paymentPlan != null){
                    return paymentPlan.getRecurringPaymentCount();
                }
            }
        }
        return 0;
    }

    public boolean isComboGroup() {
        if (cardItems != null) {
            for (CommerceCardItem cardItem : cardItems) {
                if (cardItem != null && cardItem.getComponents() != null
                        && !cardItem.getComponents().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPaymentPlans() {
        return paymentPlans != null && paymentPlans.size() > 0;
    }
}
