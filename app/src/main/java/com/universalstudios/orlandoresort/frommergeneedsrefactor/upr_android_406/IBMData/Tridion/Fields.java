package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion;

/**
 * Created by jamesblack on 5/31/16.
 */
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by jamesblack on 4/20/16.
 */
public class Fields extends GsonObject {

    @SerializedName("Header1Label")
    private Field header1Label;

    @SerializedName("SubheaderLabel")
    private Field subheaderLabel;

    @SerializedName("CreateAccountLinkLabel")
    private Field createAccountLinkLabel;

    @SerializedName("EmailTextFieldLabel")
    private Field emailTextFieldLabel;

    @SerializedName("PasswordTextFieldLabel")
    private Field passwordTextFieldLabel;

    @SerializedName("ForgotLinkLabel")
    private Field forgotLinkLabel;

    @SerializedName("RememberMeCheckboxLink")
    private Field rememberMeCheckboxLink;

    @SerializedName("SignInButtonLabel")
    private Field signInButtonLabel;

    @SerializedName("HeroImage")
    private Field heroImage;

    public Field getHeader1Label() {
        return header1Label;
    }

    public void setHeader1Label(Field header1Label) {
        this.header1Label = header1Label;
    }

    public Field getSubheaderLabel() {
        return subheaderLabel;
    }

    public void setSubheaderLabel(Field subheaderLabel) {
        this.subheaderLabel = subheaderLabel;
    }

    public Field getCreateAccountLinkLabel() {
        return createAccountLinkLabel;
    }

    public void setCreateAccountLinkLabel(Field createAccountLinkLabel) {
        this.createAccountLinkLabel = createAccountLinkLabel;
    }

    public Field getEmailTextFieldLabel() {
        return emailTextFieldLabel;
    }

    public void setEmailTextFieldLabel(Field emailTextFieldLabel) {
        this.emailTextFieldLabel = emailTextFieldLabel;
    }

    public Field getPasswordTextFieldLabel() {
        return passwordTextFieldLabel;
    }

    public void setPasswordTextFieldLabel(Field passwordTextFieldLabel) {
        this.passwordTextFieldLabel = passwordTextFieldLabel;
    }

    public Field getForgotLinkLabel() {
        return forgotLinkLabel;
    }

    public void setForgotLinkLabel(Field forgotLinkLabel) {
        this.forgotLinkLabel = forgotLinkLabel;
    }

    public Field getRememberMeCheckboxLink() {
        return rememberMeCheckboxLink;
    }

    public void setRememberMeCheckboxLink(Field rememberMeCheckboxLink) {
        this.rememberMeCheckboxLink = rememberMeCheckboxLink;
    }

    public Field getSignInButtonLabel() {
        return signInButtonLabel;
    }

    public void setSignInButtonLabel(Field signInButtonLabel) {
        this.signInButtonLabel = signInButtonLabel;
    }

    public Field getHeroImage() {
        return heroImage;
    }

    public void setHeroImage(Field heroImage) {
        this.heroImage = heroImage;
    }
}