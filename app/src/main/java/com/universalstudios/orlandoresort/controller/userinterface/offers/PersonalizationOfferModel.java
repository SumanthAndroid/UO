package com.universalstudios.orlandoresort.controller.userinterface.offers;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;

/**
 * @author tjudkins
 * @since 10/19/16
 */

public class PersonalizationOfferModel extends BaseObservable {

    public interface OfferActionCallback {
        void onOfferCtaClicked(String url);
    }

    @Bindable
    private String imageUrl;
    @Bindable
    private String title;
    @Bindable
    private String teaserText;
    @Bindable
    private String buttonLabel;
    @Bindable
    private String buttonCtaUrl;
    @Bindable
    private String priceTextAbove;
    @Bindable
    private String priceTextBelowPrimary;
    @Bindable
    private OfferActionCallback callback;

    public PersonalizationOfferModel(String imageUrl, String title, String teaserText,
                                     String buttonLabel, String buttonCtaUrl, String priceTextAbove,
                                     String priceTextBelowPrimary, OfferActionCallback callback) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.teaserText = teaserText;
        this.buttonLabel = buttonLabel;
        this.buttonCtaUrl = buttonCtaUrl;
        this.priceTextAbove = priceTextAbove;
        this.priceTextBelowPrimary = priceTextBelowPrimary;
        this.callback = callback;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public String getTeaserText() {
        return teaserText;
    }

    public void setTeaserText(String teaserText) {
        this.teaserText = teaserText;
        notifyPropertyChanged(BR.teaserText);
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
        notifyPropertyChanged(BR.buttonLabel);
    }

    public String getButtonCtaUrl() {
        return buttonCtaUrl;
    }

    public void setButtonCtaUrl(String buttonCtaUrl) {
        this.buttonCtaUrl = buttonCtaUrl;
        notifyPropertyChanged(BR.buttonCtaUrl);
    }

    public String getPriceTextAbove() {
        return priceTextAbove;
    }

    public void setPriceTextAbove(String priceTextAbove) {
        this.priceTextAbove = priceTextAbove;
        notifyPropertyChanged(BR.priceTextAbove);
    }

    public String getPriceTextBelowPrimary() {
        return priceTextBelowPrimary;
    }

    public void setPriceTextBelowPrimary(String priceTextBelowPrimary) {
        this.priceTextBelowPrimary = priceTextBelowPrimary;
        notifyPropertyChanged(BR.priceTextBelowPrimary);
    }

    public OfferActionCallback getCallback() {
        return callback;
    }

    public void setCallback(OfferActionCallback callback) {
        this.callback = callback;
        notifyPropertyChanged(BR.callback);
    }
}
