package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import java.math.BigDecimal;

/**
 * Express pass item for Shopping Cart.
 * @author tjudkins
 * @since 10/12/16
 */

public class CartExpressPassTicketItem extends BaseCartItem {

    @Bindable
    private ExpressPassTicketGroups expressPassTicketGroups;

    @Bindable
    private String alternativeHeaderLine1;
    @Bindable
    private String alternativeHeaderLine2;
    @Bindable
    private String alternativeHeaderLine3;
    @Bindable
    private String orderId;
    @Bindable
    private boolean inventoryError;
    @Bindable
    private String total;
    @Bindable
    private String pricePerTicket;
    @Bindable
    private int ticketQty;
    @Bindable
    private int maxQty = Integer.MAX_VALUE;
    @Bindable
    private int minQty = 1;
    @Bindable
    private boolean showQuantityControls;

    @Bindable
    private String expressSavingsMessage;

    @Bindable
    private boolean isExpressSavingsMessageShown;

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_express_pass_item;
    }

    public CartExpressPassTicketItem(String orderId, ExpressPassTicketGroups expressPassTicketGroups) {
        setOrderId(orderId);
        setExpressPassTicketGroups(expressPassTicketGroups);
    }

    public ExpressPassTicketGroups getExpressPassTicketGroups() {
        return expressPassTicketGroups;
    }

    public void setExpressPassTicketGroups(ExpressPassTicketGroups expressPassTicketGroups) {
        this.expressPassTicketGroups = expressPassTicketGroups;

        if (null != expressPassTicketGroups && null != expressPassTicketGroups.getItem()) {
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(expressPassTicketGroups.getItem().getTcmId1());
            setAlternativeHeaderLine1(labelSpec.getTypeAlternativeHeaderLine1(expressPassTicketGroups));
            setAlternativeHeaderLine2(labelSpec.getTypeAlternativeHeaderLine2(expressPassTicketGroups));
            setAlternativeHeaderLine3(labelSpec.getTypeAlternativeHeaderLine3(expressPassTicketGroups));

            boolean isInventoryError = IceTicketUtils.hasInventoryError(expressPassTicketGroups.getOrderItems());
            setTotal(IceTicketUtils.getTridionConfig().getFormattedPrice(expressPassTicketGroups.getTotalPrice()));
            setPricePerTicket(labelSpec.getPricePerUnitPrimaryString(expressPassTicketGroups));
            setShowQuantityControls(!isInventoryError && expressPassTicketGroups.isQuantityChangeAllowed());
            setInventoryError(isInventoryError);
            setTicketQty(expressPassTicketGroups.getQuantity());
            setMaxQty(expressPassTicketGroups.getMaxQuantity());
            int min = expressPassTicketGroups.getMinQuantity();
            setMinQty(min > 1 ? min : 1);

            BigDecimal totalSavings = expressPassTicketGroups.getTotalPriceSavings();

            setExpressSavingsMessageShown(expressPassTicketGroups.shouldShowSavingsMessage());
            if(totalSavings != null && totalSavings.compareTo(BigDecimal.ZERO) > 0)
            {
                setExpressSavingsMessage(TridionConfigStateManager
                        .getInstance()
                        .getTridionConfig()
                        .getSCSavingsMessageText(expressPassTicketGroups));
            }
        }
    }

    public String getAlternativeHeaderLine1() {
        return alternativeHeaderLine1;
    }

    public void setAlternativeHeaderLine1(String alternativeHeaderLine1) {
        this.alternativeHeaderLine1 = alternativeHeaderLine1;
        notifyPropertyChanged(BR.alternativeHeaderLine1);
    }

    public String getAlternativeHeaderLine2() {
        return alternativeHeaderLine2;
    }

    public void setAlternativeHeaderLine2(String alternativeHeaderLine2) {
        this.alternativeHeaderLine2 = alternativeHeaderLine2;
        notifyPropertyChanged(BR.alternativeHeaderLine2);
    }

    public String getAlternativeHeaderLine3() {
        return alternativeHeaderLine3;
    }

    public void setAlternativeHeaderLine3(String alternativeHeaderLine3) {
        this.alternativeHeaderLine3 = alternativeHeaderLine3;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
        notifyPropertyChanged(BR.orderId);
    }

    public boolean isInventoryError() {
        return inventoryError;
    }

    public void setInventoryError(boolean inventoryError) {
        this.inventoryError = inventoryError;
        notifyPropertyChanged(BR.inventoryError);
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
        notifyPropertyChanged(BR.adultTotal);
    }

    public String getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(String pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
        notifyPropertyChanged(BR.pricePerTicketAdult);
    }

    public int getTicketQty() {
        return ticketQty;
    }

    public void setTicketQty(int ticketQty) {
        this.ticketQty = ticketQty;
        notifyPropertyChanged(BR.adultTicketQty);
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
        notifyPropertyChanged(BR.maxQty);
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
        notifyPropertyChanged(BR.minQty);
    }

    public boolean isShowQuantityControls() {
        return showQuantityControls;
    }

    public void setShowQuantityControls(boolean showQuantityControls) {
        this.showQuantityControls = showQuantityControls;
        notifyPropertyChanged(BR.showQuantityControls);
    }

    public String getExpressSavingsMessage() {
        return expressSavingsMessage;
    }

    public void setExpressSavingsMessage(String expressSavingsMessage) {
        this.expressSavingsMessage = expressSavingsMessage;
        notifyPropertyChanged(BR.expressSavingsMessage);
    }

    public boolean isExpressSavingsMessageShown() {
        return isExpressSavingsMessageShown;
    }

    public void setExpressSavingsMessageShown(boolean isSavingsMessageShown) {
        isExpressSavingsMessageShown = isSavingsMessageShown;
        notifyPropertyChanged(BR.isExpressSavingsMessageShown);
    }
}
