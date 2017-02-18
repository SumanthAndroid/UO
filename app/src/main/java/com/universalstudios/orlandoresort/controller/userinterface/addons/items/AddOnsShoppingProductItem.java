package com.universalstudios.orlandoresort.controller.userinterface.addons.items;

import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasProduct;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.PersonalizationExtrasResultAttribute;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;

/**
 * Footer that contains the continue and back buttons for Add-ons Shopping.
 * @author tjudkins
 * @since 10/12/16
 */
@Parcel
public class AddOnsShoppingProductItem extends BaseObservableWithLayoutItem {

    final TridionConfig tridionConfig;
    PersonalizationExtrasProduct extrasProduct;
    String tcmId1;
    String tcmId2;
    float lowestChildPrice = Float.POSITIVE_INFINITY;

    @Bindable
    TridionLabelSpec labelSpecTcmId1;
    @Bindable
    TridionLabelSpec labelSpecTcmId2;
    @Bindable
    String textPrice;
    @Bindable
    String textFormattedPriceBelowSecondary;

    @ParcelConstructor
    public AddOnsShoppingProductItem(TridionConfig tridionConfig, @NonNull PersonalizationExtrasProduct extrasProduct) {
        this.tridionConfig = tridionConfig;
        setExtrasProduct(extrasProduct);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_addon_shopping;
    }

    public TridionLabelSpec getLabelSpecTcmId1() {
        return labelSpecTcmId1;
    }

    public void setLabelSpecTcmId1(@NonNull TridionLabelSpec labelSpecTcmId1) {
        this.labelSpecTcmId1 = labelSpecTcmId1;
        if (lowestChildPrice != Float.POSITIVE_INFINITY) {
            setTextFormattedPriceBelowSecondary(String.format("($%.2f %s)", lowestChildPrice, labelSpecTcmId1.getPriceTextBelowSecondary()));
        }
        notifyPropertyChanged(BR.labelSpecTcmId1);
    }

    public TridionLabelSpec getLabelSpecTcmId2() {
        return labelSpecTcmId2;
    }

    public void setLabelSpecTcmId2(@NonNull TridionLabelSpec labelSpecTcmId2) {
        this.labelSpecTcmId2 = labelSpecTcmId2;
        notifyPropertyChanged(BR.labelSpecTcmId2);
    }

    public PersonalizationExtrasProduct getExtrasProduct() {
        return extrasProduct;
    }

    public void setExtrasProduct(@NonNull PersonalizationExtrasProduct extrasProduct) {
        this.extrasProduct = extrasProduct;
        for (PersonalizationExtrasResultAttribute attribute : extrasProduct.getPersonalizationExtrasResultAttributes()) {
            if (null != attribute) {
               if (attribute.isTcmId1()) {
                   tcmId1 = attribute.getValues();
               } else if (attribute.isTcmId2()) {
                   tcmId2 = attribute.getValues();
               }
            }
        }
        float priceValue = IceTicketUtils.findAddOnsLowestAdultPrice(extrasProduct);
        if (priceValue == Float.POSITIVE_INFINITY) {
            // if no adult price found, get the lowest overall price
            priceValue = IceTicketUtils.findAddOnsLowestPrice(extrasProduct);
        }
        textPrice = String.format("$%.2f",priceValue);
        lowestChildPrice = IceTicketUtils.findAddOnsLowestChildPrice(extrasProduct);
    }

    public String getTcmId1() {
        return tcmId1;
    }

    public String getTcmId2() {
        return tcmId2;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice = textPrice;
        notifyPropertyChanged(BR.textPrice);
    }

    public String getTextFormattedPriceBelowSecondary() {
        return textFormattedPriceBelowSecondary;
    }

    public void setTextFormattedPriceBelowSecondary(String textFormattedPriceBelowSecondary) {
        this.textFormattedPriceBelowSecondary = textFormattedPriceBelowSecondary;
        notifyPropertyChanged(BR.textFormattedPriceBelowSecondary);
    }
}
