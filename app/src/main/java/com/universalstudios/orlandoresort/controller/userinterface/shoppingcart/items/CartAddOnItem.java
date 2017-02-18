package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Item;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.state.network_config.TridionConfigStateManager;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Shopping cart item for add ons.
 * @author tjudkins
 * @since 10/18/16
 */

public class CartAddOnItem extends BaseCartItem {

    private static final String REGEX_QUANTITY_LABEL = "\\(.*\\)";

    @Bindable
    private AddOnTicketGroups addOnTicketGroups;
    @Bindable
    private String orderId;
    @Bindable
    private String alternativeHeaderLine1;
    @Bindable
    private String alternativeHeaderLine2;
    @Bindable
    private String alternativeHeaderLine3;
    @Bindable
    private boolean inventoryError;
    @Bindable
    private boolean showQuantityControls;
    @Bindable
    private Ticket generalAddOnTicket;
    @Bindable
    private Ticket adultAddOnTicket;
    @Bindable
    private Ticket childAddOnTicket;
    @Bindable
    private String generalTotal;
    @Bindable
    private String adultTotal;
    @Bindable
    private String childTotal;
    @Bindable
    private String generalLabel;
    @Bindable
    private String adultLabel;
    @Bindable
    private String childLabel;
    @Bindable
    private String pricePerGeneral;
    @Bindable
    private String pricePerAdult;
    @Bindable
    private String pricePerChild;
    @Bindable
    private int generalQty;
    @Bindable
    private int adultQty;
    @Bindable
    private int childQty;
    @Bindable
    private int generalMaxQty = Integer.MAX_VALUE;
    @Bindable
    private int adultMaxQty = Integer.MAX_VALUE;
    @Bindable
    private int childMaxQty = Integer.MAX_VALUE;
    @Bindable
    private int generalMinQty = 0;
    @Bindable
    private int adultMinQty = 0;
    @Bindable
    private int childMinQty = 0;
    @Bindable
    private boolean isGeneralAdmission;

    @Bindable
    private String addonSavingsMessage;

    @Bindable
    private boolean isAddonSavingsMessageShown;

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_add_on_item;
    }

    public CartAddOnItem(String orderId, AddOnTicketGroups addOnTicketGroupsTicketGroups) {
        setOrderId(orderId);
        setAddOnTicketGroups(addOnTicketGroupsTicketGroups);
    }

    public AddOnTicketGroups getAddOnTicketGroups() {
        return addOnTicketGroups;
    }

    public void setAddOnTicketGroups(AddOnTicketGroups addOnTicketGroups) {
        this.addOnTicketGroups = addOnTicketGroups;

        if (null != addOnTicketGroups) {
            setGeneralAddOnTicket(addOnTicketGroups.getAllAddOns());
            setAdultAddOnTicket(addOnTicketGroups.getAdultAddOns());
            setChildAddOnTicket(addOnTicketGroups.getChildAddOns());
            int adultMin = 0;
            int childMin = 0;
            int generalMin = 0;
            if (null != generalAddOnTicket) {
                setGeneralQty(generalAddOnTicket.getQuantity());
                setGeneralMaxQty(generalAddOnTicket.getMaxQuantity());
                generalMin = generalAddOnTicket.getMinQuantity();
                Item item = generalAddOnTicket.getItem();
                if (null != item && generalAddOnTicket.getQuantity() > 0) {
                    TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(generalAddOnTicket.getItem().getTcmId1());
                    setGeneralTotal(IceTicketUtils.getTridionConfig().getFormattedPrice(generalAddOnTicket.getTotalPrice()));
                    setAlternativeHeaderLine1(labelSpec.getTypeAlternativeHeaderLine1(generalAddOnTicket));
                    setAlternativeHeaderLine2(labelSpec.getTypeAlternativeHeaderLine2(generalAddOnTicket));
                    setAlternativeHeaderLine3(labelSpec.getTypeAlternativeHeaderLine3(generalAddOnTicket));
                    setPricePerGeneral(labelSpec.getPricePerUnitPrimaryString(generalAddOnTicket));
                    setGeneralLabel(StringUtils.replaceAll(labelSpec.getQuantitySelectorBelowLabel1(), REGEX_QUANTITY_LABEL, "").trim());
                }
            }
            if (null != adultAddOnTicket) {
                setAdultQty(adultAddOnTicket.getQuantity());
                setAdultMaxQty(adultAddOnTicket.getMaxQuantity());
                adultMin = adultAddOnTicket.getMinQuantity();
                Item item = adultAddOnTicket.getItem();
                if (null != item) {
                    TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(adultAddOnTicket.getItem().getTcmId1());
                    setAdultTotal(IceTicketUtils.getTridionConfig().getFormattedPrice(adultAddOnTicket.getTotalPrice()));
                    if (adultAddOnTicket.getQuantity() > 0) {
                        setAlternativeHeaderLine1(labelSpec.getTypeAlternativeHeaderLine1(adultAddOnTicket));
                        setAlternativeHeaderLine2(labelSpec.getTypeAlternativeHeaderLine2(adultAddOnTicket));
                        setAlternativeHeaderLine3(labelSpec.getTypeAlternativeHeaderLine3(adultAddOnTicket));
                        setPricePerAdult(labelSpec.getPricePerUnitPrimaryString(adultAddOnTicket));
                        // This is purposeful to get the labels correct for per unit prices
                        setPricePerChild(labelSpec.getPricePerUnitSecondaryString(childAddOnTicket));
                        setAdultLabel(StringUtils.replaceAll(labelSpec.getQuantitySelectorBelowLabel1(), REGEX_QUANTITY_LABEL, "").trim());
                        setChildLabel(StringUtils.replaceAll(labelSpec.getQuantitySelectorBelowLabel2(), REGEX_QUANTITY_LABEL, "").trim());
                    }
                }
            }
            if (null != childAddOnTicket) {
                setChildQty(childAddOnTicket.getQuantity());
                setChildMaxQty(childAddOnTicket.getMaxQuantity());
                childMin = childAddOnTicket.getMinQuantity();
                Item item = childAddOnTicket.getItem();
                if (null != item) {
                    TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(childAddOnTicket.getItem().getTcmId1());
                    setChildTotal(IceTicketUtils.getTridionConfig().getFormattedPrice(childAddOnTicket.getTotalPrice()));
                    if (childAddOnTicket.getQuantity() > 0 && null != adultAddOnTicket && adultAddOnTicket.getQuantity() <= 0) {
                        // If it gets here, it means the adult ticket did not have a alternativeHeaderLine1, so get it from the child
                        setAlternativeHeaderLine1(labelSpec.getTypeAlternativeHeaderLine1(childAddOnTicket));
                        setAlternativeHeaderLine2(labelSpec.getTypeAlternativeHeaderLine2(childAddOnTicket));
                        setAlternativeHeaderLine3(labelSpec.getTypeAlternativeHeaderLine3(childAddOnTicket));
                        // This is purposeful to get the labels correct for per unit prices
                        setPricePerAdult(labelSpec.getPricePerUnitPrimaryString(adultAddOnTicket));
                        setPricePerChild(labelSpec.getPricePerUnitSecondaryString(childAddOnTicket));
                        setAdultLabel(StringUtils.replaceAll(labelSpec.getQuantitySelectorBelowLabel1(), REGEX_QUANTITY_LABEL, "").trim());
                        setChildLabel(StringUtils.replaceAll(labelSpec.getQuantitySelectorBelowLabel2(), REGEX_QUANTITY_LABEL, "").trim());
                    }
                }
            }

            if (getGeneralQty() > 0) {
                setGeneralMinQty(generalMin > 1 ? generalMin : 1);
            } else {
                if (getAdultQty() > 0 && getChildQty() > 0) {
                    setAdultMinQty(0);
                    setChildMinQty(0);
                } else if (getAdultQty() <= 0) {
                    setAdultMinQty(0);
                    setChildMinQty(childMin > 1 ? childMin : 1);
                } else if (getChildQty() <= 0) {
                    setAdultMinQty(adultMin > 1 ? adultMin : 1);
                    setChildMinQty(0);
                }
            }

            setAddonSavingsMessageShown(addOnTicketGroups.shouldShowSavingsMessage());
            if(addOnTicketGroups.getTotalSavings().compareTo(BigDecimal.ZERO) > 0) {
                setAddonSavingsMessage(TridionConfigStateManager.getInstance()
                        .getTridionConfig()
                        .getSCSavingsMessageText(addOnTicketGroups));
            }

            boolean isInventoryError = IceTicketUtils.hasInventoryError(addOnTicketGroups);
            setShowQuantityControls(!isInventoryError && addOnTicketGroups.isQuantityChangeAllowed());
            setInventoryError(isInventoryError);
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
        notifyPropertyChanged(BR.orderId);
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

    public boolean getInventoryError() {
        return inventoryError;
    }

    public void setInventoryError(boolean inventoryError) {
        this.inventoryError = inventoryError;
        notifyPropertyChanged(BR.inventoryError);
    }

    public boolean getShowQuantityControls() {
        return showQuantityControls;
    }

    public void setShowQuantityControls(boolean showQuantityControls) {
        this.showQuantityControls = showQuantityControls;
        notifyPropertyChanged(BR.showQuantityControls);
    }

    public Ticket getGeneralAddOnTicket() {
        return generalAddOnTicket;
    }

    public void setGeneralAddOnTicket(Ticket generalAddOnTicket) {
        this.generalAddOnTicket = generalAddOnTicket;
        notifyPropertyChanged(BR.generalAddOnTicket);
    }

    public Ticket getAdultAddOnTicket() {
        return adultAddOnTicket;
    }

    public void setAdultAddOnTicket(Ticket adultAddOnTicket) {
        this.adultAddOnTicket = adultAddOnTicket;
        notifyPropertyChanged(BR.adultAddOnTicket);
    }

    public Ticket getChildAddOnTicket() {
        return childAddOnTicket;
    }

    public void setChildAddOnTicket(Ticket childAddOnTicket) {
        this.childAddOnTicket = childAddOnTicket;
        notifyPropertyChanged(BR.childAddOnTicket);
    }

    public String getGeneralTotal() {
        return generalTotal;
    }

    public void setGeneralTotal(String generalTotal) {
        this.generalTotal = generalTotal;
        notifyPropertyChanged(BR.generalTotal);
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

    public String getGeneralLabel() {
        return generalLabel;
    }

    public void setGeneralLabel(String generalLabel) {
        this.generalLabel = generalLabel;
    }

    public String getAdultLabel() {
        return adultLabel;
    }

    public void setAdultLabel(String adultLabel) {
        this.adultLabel = adultLabel;
    }

    public String getChildLabel() {
        return childLabel;
    }

    public void setChildLabel(String childLabel) {
        this.childLabel = childLabel;
    }

    public String getPricePerGeneral() {
        return pricePerGeneral;
    }

    public void setPricePerGeneral(String pricePerGeneral) {
        this.pricePerGeneral = pricePerGeneral;
        notifyPropertyChanged(BR.pricePerGeneral);
    }

    public String getPricePerAdult() {
        return pricePerAdult;
    }

    public void setPricePerAdult(String pricePerAdult) {
        this.pricePerAdult = pricePerAdult;
        notifyPropertyChanged(BR.pricePerAdult);
    }

    public String getPricePerChild() {
        return pricePerChild;
    }

    public void setPricePerChild(String pricePerChild) {
        this.pricePerChild = pricePerChild;
        notifyPropertyChanged(BR.pricePerChild);
    }

    public int getGeneralQty() {
        return generalQty;
    }

    public void setGeneralQty(int generalQty) {
        this.generalQty = generalQty;
        setIsGeneralAdmission(generalQty > 0);
        notifyPropertyChanged(BR.generalQty);
    }

    public int getAdultQty() {
        return adultQty;
    }

    public void setAdultQty(int adultQty) {
        this.adultQty = adultQty;
        notifyPropertyChanged(BR.adultQty);
        setNewMinimumQty();
    }

    public int getChildQty() {
        return childQty;
    }

    public void setChildQty(int childQty) {
        this.childQty = childQty;
        notifyPropertyChanged(BR.childQty);
        setNewMinimumQty();
    }

    public int getGeneralMaxQty() {
        return generalMaxQty;
    }

    public void setGeneralMaxQty(int generalMaxQty) {
        this.generalMaxQty = generalMaxQty;
        notifyPropertyChanged(BR.generalMaxQty);
    }

    public int getGeneralMinQty() {
        return generalMinQty;
    }

    public void setGeneralMinQty(int generalMinQty) {
        this.generalMinQty = generalMinQty;
        notifyPropertyChanged(BR.generalMinQty);
    }

    public int getAdultMaxQty() {
        return adultMaxQty;
    }

    public void setAdultMaxQty(int adultMaxQty) {
        this.adultMaxQty = adultMaxQty;
        notifyPropertyChanged(BR.adultMaxQty);
    }

    public int getAdultMinQty() {
        return adultMinQty;
    }

    public void setAdultMinQty(int adultMinQty) {
        this.adultMinQty = adultMinQty;
        notifyPropertyChanged(BR.adultMinQty);
        setNewMinimumQty();
    }

    public int getChildMaxQty() {
        return childMaxQty;
    }

    public void setChildMaxQty(int childMaxQty) {
        this.childMaxQty = childMaxQty;
        notifyPropertyChanged(BR.childMaxQty);
    }

    public int getChildMinQty() {
        return childMinQty;
    }

    public void setChildMinQty(int childMinQty) {
        this.childMinQty = childMinQty;
        notifyPropertyChanged(BR.childMinQty);
        setNewMinimumQty();
    }

    public boolean getIsGeneralAdmission() {
        return isGeneralAdmission;
    }

    public void setIsGeneralAdmission(boolean generalAdmission) {
        isGeneralAdmission = generalAdmission;
        notifyPropertyChanged(BR.isGeneralAdmission);
    }

    public String getAddonSavingsMessage() {
        return addonSavingsMessage;
    }

    public void setAddonSavingsMessage(String addonSavingsMessage) {
        this.addonSavingsMessage = addonSavingsMessage;
        notifyPropertyChanged(BR.addonSavingsMessage);
    }

    public boolean isAddonSavingsMessageShown() {
        return isAddonSavingsMessageShown;
    }

    public void setAddonSavingsMessageShown(boolean isSavingsMessageShown) {
        isAddonSavingsMessageShown = isSavingsMessageShown;
        notifyPropertyChanged(BR.isAddonSavingsMessageShown);
    }

    /**
     * This adjusts the minimum adult and child minimum quantity relative
     * to the changes made to the adult and child quantity changes
     */
    private void setNewMinimumQty() {
        if(this.childQty == childMinQty && adultQty < (adultMinQty + 1)) {
            setAdultMinQty(adultMinQty + 1);
        } else if (this.adultQty == adultMinQty && childQty < (childMinQty + 1)) {
            setChildMinQty(childMinQty + 1);
        }
    }
}
