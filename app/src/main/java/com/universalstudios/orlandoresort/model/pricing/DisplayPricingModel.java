package com.universalstudios.orlandoresort.model.pricing;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.Adjustment;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.DisplayPricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * Pricing item for shopping cart.
 * @author tjudkins
 * @since 10/12/16
 */
@Parcel
public class DisplayPricingModel extends BaseObservable {

    TridionConfig tridion;

    @Bindable
    String subtotalValueText;
    @Bindable
    String shippingValueText;
    @Bindable
    String taxesValueText;
    @Bindable
    String totalValueText;
    @Bindable
    String adjustmentTotalValue;
    @Bindable
    String adjustmentDescription;
    @Bindable
    String totalSavingsMessage;
    @Bindable
    String totalAmountFinanced;
    @Bindable
    String totalFinancedDescription;
    @Bindable
    String todaysTotalText;

    /**
     * Package-protected constructor for {@link Parcel}
     */
    DisplayPricingModel() {}

    public DisplayPricingModel(@NonNull DisplayPricing displayPricing) {
        this.tridion = IceTicketUtils.getTridionConfig();
        setDisplayPricing(displayPricing);
    }

    private void setDisplayPricing(DisplayPricing displayPricing) {
        if (null != displayPricing) {
            setSubtotalValueText(tridion.getFormattedPrice(displayPricing.getTotalProductPrice()));
            setShippingValueText(tridion.getFormattedPrice(displayPricing.getShippingCharge()));
            setTotalValueText(tridion.getFormattedPrice(displayPricing.getGrandTotal()));
            BigDecimal taxes = BigDecimal.ZERO;
            if (displayPricing.getSalesTax() != null) {
                taxes = taxes.add(displayPricing.getSalesTax());
            }
            if (displayPricing.getShippingTax() != null) {
                taxes = taxes.add(displayPricing.getShippingTax());
            }
            setTaxesValueText(tridion.getFormattedPrice(taxes));
            Adjustment adjustment = displayPricing.getFirstAdjustment();
            if (adjustment != null) {
                setAdjustmentTotalValue(tridion.getFormattedPrice(displayPricing.getAdjustmentTotal()));
                setAdjustmentDescription(adjustment.getDescription());
            } else {
                setAdjustmentTotalValue(null);
                setAdjustmentDescription(null);
            }

            BigDecimal totalSavings = displayPricing.getTotalPriceSavings();

            if (totalSavings != null && totalSavings.compareTo(BigDecimal.ZERO) > 0) {
                setTotalSavingsMessage(TridionConfigStateManager
                        .getInstance()
                        .getTridionConfig()
                        .getSCSavingsMessageText(displayPricing));
            } else {
                setTotalSavingsMessage(null);
            }

            // Flex pay values
            BigDecimal totalAmountFinanced = displayPricing.getTotalFinanced();
            if (totalAmountFinanced != null && totalAmountFinanced.compareTo(BigDecimal.ZERO) > 0) {
                setTotalAmountFinanced(tridion.getFormattedPrice(totalAmountFinanced));
                setTotalFinancedDescription(tridion.getFlexPayTotalDescription(displayPricing));
                setTodaysTotalText(tridion.getFormattedPrice(displayPricing.getTotalCharged()));
            } else {
                setTotalAmountFinanced(null);
                setTotalFinancedDescription(null);
            }
        } else {
            setTotalAmountFinanced(null);
            setTotalFinancedDescription(null);
            setTotalSavingsMessage(null);
        }
    }

    public String getSubtotalValueText() {
        return subtotalValueText;
    }

    private void setSubtotalValueText(String subtotalValueText) {
        this.subtotalValueText = subtotalValueText;
        notifyPropertyChanged(BR.subtotalValueText);
    }

    public String getShippingValueText() {
        return shippingValueText;
    }

    private void setShippingValueText(String shippingValueText) {
        this.shippingValueText = shippingValueText;
        notifyPropertyChanged(BR.shippingValueText);
    }

    public String getTaxesValueText() {
        return taxesValueText;
    }

    private void setTaxesValueText(String taxesValueText) {
        this.taxesValueText = taxesValueText;
        notifyPropertyChanged(BR.taxesValueText);
    }

    public String getTotalValueText() {
        return totalValueText;
    }

    private void setTotalValueText(String totalValueText) {
        this.totalValueText = totalValueText;
        notifyPropertyChanged(BR.totalValueText);
    }

    public String getAdjustmentTotalValue() {
        return adjustmentTotalValue;
    }

    public void setAdjustmentTotalValue(String adjustmentTotalValue) {
        this.adjustmentTotalValue = adjustmentTotalValue;
        notifyPropertyChanged(BR.adjustmentTotalValue);
    }

    public String getAdjustmentDescription() {
        return adjustmentDescription;
    }

    public void setAdjustmentDescription(String adjustmentDescription) {
        this.adjustmentDescription = adjustmentDescription;
        notifyPropertyChanged(BR.adjustmentDescription);
    }

    public String getTotalSavingsMessage() {
        return totalSavingsMessage;
    }

    public void setTotalSavingsMessage(String totalSavingsMessage) {
        this.totalSavingsMessage = totalSavingsMessage;
        notifyPropertyChanged(BR.totalSavingsMessage);
    }

    public String getTotalAmountFinanced() {
        return totalAmountFinanced;
    }

    public void setTotalAmountFinanced(String totalAmountFinanced) {
        this.totalAmountFinanced = totalAmountFinanced;
        notifyPropertyChanged(BR.totalAmountFinanced);
    }

    public String getTotalFinancedDescription() {
        return totalFinancedDescription;
    }

    public void setTotalFinancedDescription(String totalFinancedDescription) {
        this.totalFinancedDescription = totalFinancedDescription;
        notifyPropertyChanged(BR.totalFinancedDescription);
    }

    public String getTodaysTotalText() {
        return todaysTotalText;
    }

    public void setTodaysTotalText(String todaysTotalText) {
        this.todaysTotalText = todaysTotalText;
        notifyPropertyChanged(BR.todaysTotalText);
    }
}
