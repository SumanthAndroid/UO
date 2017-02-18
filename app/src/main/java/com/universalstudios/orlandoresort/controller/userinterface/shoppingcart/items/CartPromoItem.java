package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Offer;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Price;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

/**
 * @author acampbell
 */
public class CartPromoItem extends BaseCartItem {

    @Bindable
    private Offer offer;
    @Bindable
    private String price;
    @Bindable
    private String childUnitPrice;
    @Bindable
    TridionLabelSpec labelSpecTcmId1;

    public CartPromoItem(Offer offer) {
        setOffer(offer);
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
        if (offer != null) {
            TridionLabelSpec labelSpec = TridionLabelSpecManager.getSpecForId(offer.getTcmId1());
            setLabelSpecTcmId1(labelSpec);
            if (offer.getSku() != null && offer.getSku().getPrice() != null) {
                for (Price price : offer.getSku().getPrice()) {
                    if (price != null && price.isDisplay()) {
                        setPrice(IceTicketUtils.getTridionConfig().getFormattedPrice(price.getValue()));
                    }
                }
            }
        }
        notifyPropertyChanged(BR.offer);
    }

    @Override
    public int getLayoutId() {
        return R.layout.shopping_cart_promo_item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    public String getChildUnitPrice() {
        return childUnitPrice;
    }

    public void setChildUnitPrice(String childUnitPrice) {
        this.childUnitPrice = childUnitPrice;
        notifyPropertyChanged(BR.childUnitPrice);
    }

    public TridionLabelSpec getLabelSpecTcmId1() {
        return labelSpecTcmId1;
    }

    public void setLabelSpecTcmId1(TridionLabelSpec labelSpecTcmId1) {
        this.labelSpecTcmId1 = labelSpecTcmId1;
        notifyPropertyChanged(BR.labelSpecTcmId1);
    }
}