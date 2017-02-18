package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import java.math.BigDecimal;

/**
 * Shopping cart item for park tickets and annual passes.
 * @author tjudkins
 * @author acampbell
 * @since 10/12/16
 */

public class CartTicketItem extends BaseCartItem {

    @Bindable
    private ParkTicketGroups parkTicketGroups;
    @Bindable
    private String alternativeHeaderLine1;
    @Bindable
    private String alternativeHeaderLine2;
    @Bindable
    private String alternativeHeaderLine3;
    @Bindable
    private Ticket adultTicket;
    @Bindable
    private Ticket childTicket;
    @Bindable
    private String orderId;
    @Bindable
    private boolean inventoryError;
    @Bindable
    private String adultTotal;
    @Bindable
    private String childTotal;
    @Bindable
    private String pricePerTicketAdult;
    @Bindable
    private String pricePerTicketChild;
    @Bindable
    private int adultTicketQty;
    @Bindable
    private int childTicketQty;
    @Bindable
    private int adultMaxQty = Integer.MAX_VALUE;
    @Bindable
    private int childMaxQty = Integer.MAX_VALUE;
    @Bindable
    private int adultMinQty = 0;
    @Bindable
    private int childMinQty = 0;
    @Bindable
    private boolean showQuantityControls;
    @Bindable
    private String ticketSavingsMessage;
    @Bindable
    private boolean isTicketSavingsMessageShown;
    @Bindable
    private String downPaymentText;
    @Bindable
    private String financeText;

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_ticket_item;
    }

    public CartTicketItem(String orderId, ParkTicketGroups parkTicketGroups) {
        setOrderId(orderId);
        setParkTicketGroups(parkTicketGroups);
    }

    public ParkTicketGroups getParkTicketGroups() {
        return parkTicketGroups;
    }

    public void setParkTicketGroups(ParkTicketGroups parkTicketGroups) {
        this.parkTicketGroups = parkTicketGroups;

        if (null != parkTicketGroups) {
            setAdultTicket(parkTicketGroups.getAdultTickets());
            setChildTicket(parkTicketGroups.getChildTickets());
            boolean isInventoryError = false;
            int adultMin = 0;
            int childMin = 0;
            TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
            if (null != adultTicket && null != adultTicket.getItem()) {
                TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(adultTicket.getItem().getTcmId1());
                adultMin = adultTicket.getMinQuantity();
                if (adultTicket.getQuantity() > 0) {
                    setAlternativeHeaderLine1(labelSpec.getTypeAlternativeHeaderLine1(adultTicket));
                    setAlternativeHeaderLine2(labelSpec.getTypeAlternativeHeaderLine2(adultTicket));
                    setAlternativeHeaderLine3(labelSpec.getTypeAlternativeHeaderLine3(adultTicket));
                }
                isInventoryError = IceTicketUtils.hasInventoryError(adultTicket.getOrderItems());
                setAdultTotal(IceTicketUtils.getTridionConfig().getFormattedPrice(adultTicket.getTotalPrice()));
                setPricePerTicketAdult(labelSpec.getPricePerUnitPrimaryString(adultTicket));
                setAdultTicketQty(adultTicket.getQuantity());
                setAdultMaxQty(adultTicket.getMaxQuantity());
            }
            if (null != childTicket && null != childTicket.getItem()) {
                TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(childTicket.getItem().getTcmId1());
                childMin = childTicket.getMinQuantity();
                if (childTicket.getQuantity() > 0 && null != adultTicket && adultTicket.getQuantity() <= 0) {
                    setAlternativeHeaderLine1(labelSpec.getTypeAlternativeHeaderLine1(childTicket));
                    setAlternativeHeaderLine2(labelSpec.getTypeAlternativeHeaderLine2(childTicket));
                    setAlternativeHeaderLine3(labelSpec.getTypeAlternativeHeaderLine3(childTicket));
                }
                if (!isInventoryError) {
                    isInventoryError = IceTicketUtils.hasInventoryError(parkTicketGroups.getChildTickets().getOrderItems());
                }
                setChildTotal(IceTicketUtils.getTridionConfig().getFormattedPrice(childTicket.getTotalPrice()));
                setPricePerTicketChild(labelSpec.getPricePerUnitSecondaryString(childTicket));
                setChildTicketQty(childTicket.getQuantity());
                setChildMaxQty(childTicket.getMaxQuantity());
            }
            if (getAdultTicketQty() > 0 && getChildTicketQty() > 0) {
                setAdultMinQty(0);
                setChildMinQty(0);
            } else if (getAdultTicketQty() <= 0) {
                setAdultMinQty(0);
                setChildMinQty(childMin > 1 ? childMin : 1);
            } else if (getChildTicketQty() <= 0) {
                setAdultMinQty(adultMin > 1 ? adultMin : 1);
                setChildMinQty(0);
            }
            // Down payment
            if (parkTicketGroups.isFlexPay()) {
                setDownPaymentText(tridionConfig.getFlexPayDownPaymentCopy(parkTicketGroups));
                setFinanceText(tridionConfig.getFlexPayFinancedCopy(parkTicketGroups));
            } else {
                setDownPaymentText(null);
            }

            BigDecimal totalSavings = parkTicketGroups.getTotalSavings();

            setTicketSavingsMessageShown(parkTicketGroups.shouldShowSavingsMessage());
            if(totalSavings != null && totalSavings.compareTo(BigDecimal.ZERO) > 0) {
                setTicketSavingsMessage(TridionConfigStateManager.getInstance()
                        .getTridionConfig()
                        .getSCSavingsMessageText(parkTicketGroups));
            }

            setShowQuantityControls(!isInventoryError && parkTicketGroups.isQuantityChangeAllowed());
            setInventoryError(isInventoryError);
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
        notifyPropertyChanged(BR.alternativeHeaderLine3);
    }

    public Ticket getAdultTicket() {
        return adultTicket;
    }

    public void setAdultTicket(Ticket adultTicket) {
        this.adultTicket = adultTicket;
        notifyPropertyChanged(BR.adultTicket);
    }

    public Ticket getChildTicket() {
        return childTicket;
    }

    public void setChildTicket(Ticket childTicket) {
        this.childTicket = childTicket;
        notifyPropertyChanged(BR.childTicket);
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

    public String getAdultTotal() {
        return adultTotal;
    }

    public void setAdultTotal(String adultTotal) {
        this.adultTotal = adultTotal;
        notifyPropertyChanged(BR.adultTotal);
    }

    public String getChildTotal() {
        return childTotal;
    }

    public void setChildTotal(String childTotal) {
        this.childTotal = childTotal;
        notifyPropertyChanged(BR.childTotal);
    }

    public String getPricePerTicketAdult() {
        return pricePerTicketAdult;
    }

    public void setPricePerTicketAdult(String pricePerTicketAdult) {
        this.pricePerTicketAdult = pricePerTicketAdult;
        notifyPropertyChanged(BR.pricePerTicketAdult);
    }

    public String getPricePerTicketChild() {
        return pricePerTicketChild;
    }

    public void setPricePerTicketChild(String pricePerTicketChild) {
        this.pricePerTicketChild = pricePerTicketChild;
        notifyPropertyChanged(BR.pricePerTicketChild);
    }

    public int getAdultTicketQty() {
        return adultTicketQty;
    }

    public void setAdultTicketQty(int adultTicketQty) {
        this.adultTicketQty = adultTicketQty;
        notifyPropertyChanged(BR.adultTicketQty);
    }

    public int getChildTicketQty() {
        return childTicketQty;
    }

    public void setChildTicketQty(int childTicketQty) {
        this.childTicketQty = childTicketQty;
        notifyPropertyChanged(BR.childTicketQty);
    }

    public int getAdultMaxQty() {
        return adultMaxQty;
    }

    public void setAdultMaxQty(int adultMaxQty) {
        this.adultMaxQty = adultMaxQty;
        notifyPropertyChanged(BR.adultMaxQty);
    }

    public int getChildMaxQty() {
        return childMaxQty;
    }

    public void setChildMaxQty(int childMaxQty) {
        this.childMaxQty = childMaxQty;
        notifyPropertyChanged(BR.childMaxQty);
    }

    public int getAdultMinQty() {
        return adultMinQty;
    }

    public void setAdultMinQty(int adultMinQty) {
        this.adultMinQty = adultMinQty;
        notifyPropertyChanged(BR.adultMinQty);
    }

    public int getChildMinQty() {
        return childMinQty;
    }

    public void setChildMinQty(int childMinQty) {
        this.childMinQty = childMinQty;
        notifyPropertyChanged(BR.childMinQty);
    }

    public boolean isShowQuantityControls() {
        return showQuantityControls;
    }

    public void setShowQuantityControls(boolean showQuantityControls) {
        this.showQuantityControls = showQuantityControls;
        notifyPropertyChanged(BR.showQuantityControls);
    }

    public String getTicketSavingsMessage() {
        return ticketSavingsMessage;
    }

    public void setTicketSavingsMessage(String ticketSavingsMessage) {
        this.ticketSavingsMessage = ticketSavingsMessage;
        notifyPropertyChanged(BR.ticketSavingsMessage);
    }

    public String getDownPaymentText() {
        return downPaymentText;
    }

    public void setDownPaymentText(String downPaymentText) {
        this.downPaymentText = downPaymentText;
        notifyPropertyChanged(BR.downPaymentText);
    }

    public String getFinanceText() {
        return financeText;
    }

    public void setFinanceText(String financeText) {
        this.financeText = financeText;
        notifyPropertyChanged(BR.financeText);
    }

    public boolean isTicketSavingsMessageShown() {
        return isTicketSavingsMessageShown;
    }

    public void setTicketSavingsMessageShown(boolean isSavingsMessageShown) {
        isTicketSavingsMessageShown = isSavingsMessageShown;
        notifyPropertyChanged(BR.isTicketSavingsMessageShown);
    }
}
