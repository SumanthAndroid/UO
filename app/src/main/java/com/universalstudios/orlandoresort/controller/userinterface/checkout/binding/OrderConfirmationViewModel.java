package com.universalstudios.orlandoresort.controller.userinterface.checkout.binding;

import android.databinding.Bindable;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.checkout.OrderConfirmationInfo;
import com.universalstudios.orlandoresort.controller.userinterface.offers.PersonalizationOfferModel;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.binding.WalletFolioCreatePinItemViewModel;

/**
 * @author tjudkins
 * @author tritchie
 * @since 10/10/16
 */

public class OrderConfirmationViewModel extends WalletFolioCreatePinItemViewModel {

    @Bindable
    private boolean registeredUser;

    @Bindable
    private boolean showPinCreation;

    @Bindable
    private String pageTitle;

    @Bindable
    private boolean showOrderConfirmation;

    @Bindable
    private boolean showEmailConfirmation;

    @Bindable
    private String dob;

    @Bindable
    private boolean showDobError;

    @Bindable
    private String password;

    @Bindable
    private String passwordError;

    @Bindable
    private boolean showPasswordError;

    @Bindable
    private boolean tosChecked;

    @Bindable
    private boolean createAccountButtonEnabled;

    @Bindable
    private PersonalizationOfferModel personalizationOffer;

    public OrderConfirmationInfo orderConfirmationInfo;

    public OrderConfirmationViewModel(boolean isRegisteredUser, String pageTitle) {
        super();
        this.registeredUser = isRegisteredUser;
        this.pageTitle = pageTitle;
        this.showOrderConfirmation = true;
        this.showEmailConfirmation = true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_confirmation;
    }

    public boolean getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        this.registeredUser = registeredUser;
        notifyPropertyChanged(BR.registeredUser);
    }

    public boolean getShowPinCreation() {
        return showPinCreation;
    }

    public void setShowPinCreation(boolean showPinCreation) {
        this.showPinCreation = showPinCreation;
        notifyPropertyChanged(BR.showPinCreation);
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
        notifyPropertyChanged(BR.pageTitle);
    }

    public boolean isShowOrderConfirmation() {
        return showOrderConfirmation;
    }

    public void setShowOrderConfirmation(boolean showOrderConfirmation) {
        this.showOrderConfirmation = showOrderConfirmation;
        notifyPropertyChanged(BR.showOrderConfirmation);
    }

    public boolean isShowEmailConfirmation() {
        return showEmailConfirmation;
    }

    public void setShowEmailConfirmation(boolean showEmailConfirmation) {
        this.showEmailConfirmation = showEmailConfirmation;
        notifyPropertyChanged(BR.showEmailConfirmation);
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
        notifyPropertyChanged(BR.dob);
    }

    public boolean isShowDobError() {
        return showDobError;
    }

    public void setShowDobError(boolean showDobError) {
        this.showDobError = showDobError;
        notifyPropertyChanged(BR.showDobError);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
        notifyPropertyChanged(BR.passwordError);
        setShowPasswordError(!TextUtils.isEmpty(passwordError));
    }

    public boolean getShowPasswordError() {
        return showPasswordError;
    }

    public void setShowPasswordError(boolean showPasswordError) {
        this.showPasswordError = showPasswordError;
        notifyPropertyChanged(BR.showPasswordError);
    }

    public boolean getTosChecked() {
        return tosChecked;
    }

    public void setTosChecked(boolean tosChecked) {
        this.tosChecked = tosChecked;
        notifyPropertyChanged(BR.tosChecked);
        setCreateAccountButtonEnabled(tosChecked);
    }

    public boolean getCreateAccountButtonEnabled() {
        return createAccountButtonEnabled;
    }

    public void setCreateAccountButtonEnabled(boolean createAccountButtonEnabled) {
        this.createAccountButtonEnabled = createAccountButtonEnabled;
        notifyPropertyChanged(BR.createAccountButtonEnabled);
    }

    public PersonalizationOfferModel getPersonalizationOffer() {
        return personalizationOffer;
    }

    public void setPersonalizationOffer(PersonalizationOfferModel personalizationOffer) {
        this.personalizationOffer = personalizationOffer;
        notifyPropertyChanged(BR.personalizationOffer);
    }
}
