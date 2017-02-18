package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.ColorUtils;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickListener;
import com.universalstudios.orlandoresort.controller.userinterface.link.ClickableTextHelper;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.DisplayPricing;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.ViewItems.response.PaymentPlan;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.AddOnTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ParkTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/24/16.
 * Class: TridionConfig
 * Class Description: Model for Tridion configs
 */
@Parcel
public class TridionConfig extends GsonObject {
    public static final String TAG = "TridionConfig";
    private static final String WCSKEY_SAVINGS_KEY = "#WCSSavingsKey#";
    private static final String WCSKEY_SAVINGS_MESSAGE_KEY = "#WCSKEYSAVED#";
    private static final String COUNT_REPLACER = "#count#";
    private static final String WCSKEY_DOWN_PAYMENT = "#WCSKEYDOWNPAYMENT#";
    private static final String WCSKEY_FLEX_PAY_ITEM_TAXES = "#FLEXPAYITEMTAXES#";
    private static final String WCSKEY_FLEX_PAY_ITEM_TOTAL_FINANCED = "#FLEXPAYITEMTOTALFINANCED#";
    private static final String WCSKEY_MONTHS = "#WCSKEYMONTHS#";
    private static final String WCSKEY_PAYMENT = "#WCSKEYPAYMENT#";
    private static final String WCSKEY_CREDIT_CARD_LAST_FOUR = "#CREDITCARDLAST4DIGITS#";

    // This constants are intentionally hard-coded and private instead of in colors.xml.
    // Use the appropriate getter from TridionConfig to retrieve the desired color.
    private static final String DEFAULT_CALENDAR_VALUE_BACKGROUND_COLOR = "#DDEDFE";
    private static final String DEFAULT_CALENDAR_REGULAR_BACKGROUND_COLOR = "#A1CCFE";
    private static final String DEFAULT_CALENDAR_PEAK_BACKGROUND_COLOR = "#034FA5";
    private static final String DEFAULT_CALENDAR_MIXED_BACKGROUND_COLOR = "#FFFFFF";
    private static final String DEFAULT_CALENDAR_VALUE_TEXT_COLOR = "#333333";
    private static final String DEFAULT_CALENDAR_REGULAR_TEXT_COLOR = "#333333";
    private static final String DEFAULT_CALENDAR_PEAK_TEXT_COLOR = "#FFFFFF";
    private static final String DEFAULT_CALENDAR_MIXED_TEXT_COLOR = "#000000";
    private static final String COMPARE_AP_URL_FL_PARAM = "?flr=1";

    @SerializedName("BuyTicketsEmptyMessageTitle")
    String buyTicketsEmptyMessageTitle;

    @SerializedName("BuyTicketsEmptyMessageSubtitle")
    String buyTicketsEmptyMessageSubtitle;

    @SerializedName("BuyTicketsFilterNoOfDaysPopUp")
    String buyTicketsFilterNoOfDaysPopUp;

    @SerializedName("BuyTicketsFilterSectionDaysHeader")
    String buyTicketsFilterSectionDaysHeader;

    @SerializedName("BuyTicketsFilterFLPopUp")
    String buyTicketsFilterFLPopUp;

    @SerializedName("ShoppingCartEmptyMessageTitle")
    String shoppingCartEmptyMessageTitle;

    @SerializedName("ShoppingCartEmptyMessageSubTitle")
    String shoppingCartEmptyMessageSubTitle;

    @SerializedName("MyTicketsEmptyMessageTitle")
    String myTicketsEmptyMessageTitle;

    @SerializedName("ProductsListTicketType")
    String productsListTicketType;

    @SerializedName("ProductsListBPGError")
    String productsListBPGError;

    @SerializedName("RegisterAccountHeader")
    String registerAccountHeader;

    @SerializedName("RegisterAccountSubTitle")
    String registerAccountSubTitle;

    @SerializedName("AssignTicketsLabel")
    String assignTicketsLabel;

    @SerializedName("AssignNamesHeader")
    String assignNamesHeader;

    @SerializedName("PrimaryGuest")
    String primaryGuest;

    @SerializedName("MyTicketsEmptyMessageSubTitle")
    String myTicketsEmptyMessageSubTitle;

    @SerializedName("RegisterAccountBulletList")
    String registerAccountBulletList;

    @SerializedName("TicketsLabel")
    String ticketsLabel;

    @SerializedName("ExpressPassLabel")
    String expressPassLabel;

    @SerializedName("ExtrasLabel")
    String extrasLabel;

    @SerializedName("NoOfDaysLabel")
    String noOfDaysLabel;

    @SerializedName("NoOfDaysToolTip")
    String noOfDaysToolTip;

    @SerializedName("AdultsAgeLabel")
    String adultsAgeLabel;

    @SerializedName("ChildrenAgeLabel")
    String childrenAgeLabel;

    @SerializedName("NoOfGuestsTravelingLabel")
    String noOfGuestsTravelingLabel;

    @SerializedName("FloridaResidentLabel")
    String floridaResidentLabel;

    @SerializedName("FLResidentLabel")
    String flResidentLabel;

    @SerializedName("TicketsFloridaResidentLabel")
    String ticketsFloridaResidentLabel;

    @SerializedName("FloridaResidentToolTip")
    String floridaResidentToolTip;

    @SerializedName("TicketsNonFloridaResidentLabel")
    String ticketsNonFloridaResidentLabel;

    @SerializedName("SeeBlackoutDatesLabel")
    String seeBlackoutDatesLabel;

    @SerializedName("FloridaResidentBlockoutDates")
    String floridaResidentBlockoutDates;

    @SerializedName("FloridaResidentBlockoutDatesLabel")
    String floridaResidentBlockoutDatesLabel;

    @SerializedName("NonFloridaResidentBlockoutDatesLabel")
    String nonFloridaResidentBlockoutDatesLabel;

    @SerializedName("TicketingConfirmationText")
    String ticketingConfirmationText;

    @SerializedName("ErrMsgNoProducts")
    String errMsgNoProducts;

    @SerializedName("NoOfParksLabel")
    String noOfParksLabel;

    @SerializedName("SelectDatesLabel")
    String selectDatesLabel;

    @SerializedName("NoOfGuestsLabel")
    String noOfGuestsLabel;

    @SerializedName("OnlineSavingsLabel")
    String onlineSavingsLabel;

    @SerializedName("OnlineSavingsToolTip")
    String onlineSavingsToolTip;

    @SerializedName("GuestLabel")
    String guestLabel;

    @SerializedName("GuestsLabel")
    String guestsLabel;

    @SerializedName("All_SC")
    String allSC;

    @SerializedName("Dining_SC")
    String diningSC;

    @SerializedName("Experiences_SC")
    String experiencesSC;

    @SerializedName("Photos_SC")
    String photosSC;

    @SerializedName("Strollers_SC")
    String strollersSC;

    @SerializedName("ImgSliderTickets1")
    String imgSliderTickets1;

    @SerializedName("ImgSliderTickets1-Small")
    String imgSliderTickets1Small;

    @SerializedName("ImgSliderTickets2")
    String imgSliderTickets2;

    @SerializedName("ImgSliderTickets2-Small")
    String imgSliderTickets2Small;

    @SerializedName("ImgSliderTickets3")
    String imgSliderTickets3;

    @SerializedName("ImgSliderTickets3-Small")
    String imgSliderTickets3Small;

    @SerializedName("AOAllIcon")
    String aOAllIcon;

    @SerializedName("AODinningIcon")
    String aODinningIcon;

    @SerializedName("AOExperienceIcon")
    String aOExperienceIcon;

    @SerializedName("AOPhotosIcon")
    String aOPhotosIcon;

    @SerializedName("AOStrollersIcon")
    String aOStrollersIcon;

    @SerializedName("QuantityLabel")
    String quantityLabel;

    @SerializedName("WalletButtonLabel")
    String walletButtonLabel;

    @SerializedName("MyWalletDetailsLabel")
    String myWalletDetailsLabel;

    @SerializedName("UpgradeLabel")
    String upgradeLabel;

    @SerializedName("GiftLabel")
    String giftLabel;

    @SerializedName("ReassignLabel")
    String reassignLabel;

    @SerializedName("BuyAgainLabel")
    String buyAgainLabel;

    @SerializedName("DeliveryMethodLabel")
    String deliveryMethodLabel;

    @SerializedName("ContinueShoppingLabel")
    String continueShoppingLabel;

    @SerializedName("SCTicketsLabel")
    String sCTicketsLabel;

    @SerializedName("1TicketCount")
    String oneTicketCount;

    @SerializedName("SCExtrasLabel")
    String sCExtrasLabel;

    @SerializedName("SCExpressPassLabel")
    String sCExpressPassLabel;

    @SerializedName("SelectedSeatsLabel")
    String selectedSeatsLabel;

    @SerializedName("SeatsLabel")
    String seatsLabel;

    @SerializedName("RowsLabel")
    String rowsLabel;

    @SerializedName("OUSResident")
    String oUSResident;

    @SerializedName("SCSavingText")
    String sCSavingText;

    @SerializedName("PerTicketLabel")
    String perTicketLabel;

    @SerializedName("PerPassLabel")
    String perPassLabel;

    @SerializedName("ChildLabel")
    String childLabel;

    @SerializedName("AtLabel")
    String atLabel;

    @SerializedName("ShoppingCartLabel")
    String shoppingCartLabel;

    @SerializedName("EnterPromoOrUPCCodeLabel")
    String enterPromoOrUPCCodeLabel;

    @SerializedName("ApplyLabel")
    String applyLabel;

    @SerializedName("ValidCodeText")
    String validCodeText;

    @SerializedName("MobileDeliveryLabel")
    String mobileDeliveryLabel;

    @SerializedName("PickupKiosikLabel")
    String pickupKioskLabel;

    @SerializedName("TicketBlockoutDatesLabel")
    String ticketBlockoutDatesLabel;

    @SerializedName("TicketNumberLabel")
    String ticketNumberLabel;

    @SerializedName("ConfirmationLabel")
    String confirmationLabel;

    @SerializedName("PurchasedLabel")
    String purchasedLabel;

    @SerializedName("CreditCardLabel")
    String creditCardLabel;

    @SerializedName("OrderedByLabel")
    String orderedByLabel;

    @SerializedName("FilterLabel")
    String filterLabel;

    @SerializedName("AddPaymentMethod")
    String addPaymentMethod;

    @SerializedName("ByTimeLabel")
    String byTimeLabel;

    @SerializedName("ByLocationLabel")
    String byLocationLabel;

    @SerializedName("ByCategoryLabel")
    String byCategoryLabel;

    @SerializedName("ByPersonLabel")
    String byPersonLabel;

    @SerializedName("ByTimeOptions")
    String byTimeOptions;

    @SerializedName("ByLocationOptions")
    String byLocationOptions;

    @SerializedName("ByCategoryOptions")
    String byCategoryOptions;

    @SerializedName("transactionFilterBgColor")
    String transactionFilterBgColor;

    @SerializedName("PurchasesTabTitle")
    String purchasesTabTitle;

    @SerializedName("MediaTabTitle")
    String mediaTabTitle;

    @SerializedName("PaymentTabTitle")
    String paymentTabTitle;

    @SerializedName("NoitemsInCartMessage")
    String noItemsInCartMessage;

    @SerializedName("ShipToHomeLabel")
    String shipToHomeLabel;

    @SerializedName("POBoxNotAvailableLabel")
    String pOBoxNotAvailableLabel;

    @SerializedName("InternationalLabel")
    String internationalLabel;

    @SerializedName("TaxesLabel")
    String taxesLabel;

    @SerializedName("TotalLabel")
    String totalLabel;

    @SerializedName("CreatePINText")
    String createPINText;

    @SerializedName("CreatePINCopy")
    String createPINCopy;

    @SerializedName("CreatePINLabel")
    String createPINLabel;

    @SerializedName("PhotoIDInstructionalText")
    String photoIDInstructionalText;

    @SerializedName("DaysRemainingLabel")
    String daysRemainingLabel;

    @SerializedName("1DayRemainingLabel")
    String oneDayRemainingLabel;

    @SerializedName("ViewDetailsLabel")
    String viewDetailsLabel;

    @SerializedName("QRInstructionalText")
    String qRInstructionalText;

    @SerializedName("TicketHolderNameLabel")
    String ticketHolderNameLabel;

    @SerializedName("WalletDisclosureText")
    String walletDisclosureText;

    @SerializedName("TicketsIcon")
    String ticketsIcon;

    @SerializedName("ExtrasIcon")
    String extrasIcon;

    @SerializedName("DeliveryMethodIcon")
    String deliveryMethodIcon;

    @SerializedName("ExpressPassIcon")
    String expressPassIcon;

    @SerializedName("MonthJanuaryLabel")
    String monthJanuaryLabel;

    @SerializedName("MonthFebruaryLabel")
    String monthFebruaryLabel;

    @SerializedName("MonthMarchLabel")
    String monthMarchLabel;

    @SerializedName("MonthAprilLabel")
    String monthAprilLabel;

    @SerializedName("MonthMayLabel")
    String monthMayLabel;

    @SerializedName("MonthJuneLabel")
    String monthJuneLabel;

    @SerializedName("MonthJulyLabel")
    String monthJulyLabel;

    @SerializedName("MonthAugustLabel")
    String monthAugustLabel;

    @SerializedName("MonthSeptemberLabel")
    String monthSeptemberLabel;

    @SerializedName("MonthOctoberLabel")
    String monthOctoberLabel;

    @SerializedName("MonthNovemberLabel")
    String monthNovemberLabel;

    @SerializedName("MonthDecemberLabel")
    String monthDecemberLabel;

    @SerializedName("DaySunLabel")
    String daySunLabel;

    @SerializedName("DayMonLabel")
    String dayMonLabel;

    @SerializedName("DayTueLabel")
    String dayTueLabel;

    @SerializedName("DayWedLabel")
    String dayWedLabel;

    @SerializedName("DayThrLabel")
    String dayThrLabel;

    @SerializedName("DayFriLabel")
    String dayFriLabel;

    @SerializedName("DaySatLabel")
    String daySatLabel;

    @SerializedName("SCSubHeader")
    String sCSubHeader;

    @SerializedName("SCIntroText")
    String sCIntroText;

    @SerializedName("SCSeasonsLabel")
    String sCSeasonsLabel;

    @SerializedName("ParkLabel")
    String parkLabel;

    @SerializedName("ParksLabel")
    String parksLabel;

    @SerializedName("SCSeasonsTooltip")
    String sCSeasonsTooltip;

    @SerializedName("SCValueLabel")
    String sCValueLabel;

    @SerializedName("SCValueDescriptionLabel")
    String sCValueDescriptionLabel;

    @SerializedName("SCValueColor")
    String sCValueColor;

    @SerializedName("SCRegularLabel")
    String sCRegularLabel;

    @SerializedName("SCRegularDescriptionLabel")
    String sCRegularDescriptionLabel;

    @SerializedName("SCRegularColor")
    String sCRegularColor;

    @SerializedName("SCPeakLabel")
    String sCPeakLabel;

    @SerializedName("SCPeakDescriptionLabel")
    String sCPeakDescriptionLabel;

    @SerializedName("SCPeakColor")
    String sCPeakColor;

    @SerializedName("SCMixedLabel")
    String sCMixedLabel;

    @SerializedName("SCMixedDescriptionLabel")
    String sCMixedDescriptionLabel;

    @SerializedName("SCMixedColor")
    String sCMixedColor;

    @SerializedName("SCValueValidityDescription")
    String sCValueValidityDescription;

    @SerializedName("SCPeakValidityDescription")
    String sCPeakValidityDescription;

    @SerializedName("SCMixedValidityDescription")
    String sCMixedValidityDescription;

    @SerializedName("SCRegularValidityDescription")
    String sCRegularValidityDescription;

    @SerializedName("ViewFullCalendarLabel")
    String viewFullCalendarLabel;

    @SerializedName("CloseFullCalendarLabel")
    String closeFullCalendarLabel;

    @SerializedName("SCImage")
    String sCImage;

    @SerializedName("SCShippingLabel")
    String sCShippingLabel;

    @SerializedName("NameOnCardLabel")
    String nameOnCardLabel;

    @SerializedName("CreditCardNumberLabel")
    String creditCardNumberLabel;

    @SerializedName("ExpirationDateLabel")
    String expirationDateLabel;

    @SerializedName("SecurityCodeLabel")
    String securityCodeLabel;

    @SerializedName("SecurityCodeToolTip")
    String securityCodeToolTip;

    @SerializedName("BillingAddressLabel")
    String billingAddressLabel;

    @SerializedName("SelectAddressLabel")
    String selectAddressLabel;

    @SerializedName("AddAddressLabel")
    String addAddressLabel;

    @SerializedName("UpdateAddressLabel")
    String updateAddressLabel;

    @SerializedName("PrimaryLabel")
    String primaryLabel;

    @SerializedName("CountryLabel")
    String countryLabel;

    @SerializedName("Address1Label")
    String address1Label;

    @SerializedName("Address2Label")
    String address2Label;

    @SerializedName("OptionalLabel")
    String optionalLabel;

    @SerializedName("ZipCodeLabel")
    String zipCodeLabel;

    @SerializedName("CityLabel")
    String cityLabel;

    @SerializedName("StateProvinceLabel")
    String stateProvinceLabel;

    @SerializedName("PhoneNumberLabel")
    String phoneNumberLabel;

    @SerializedName("ShippingAddressLabel")
    String shippingAddressLabel;

    @SerializedName("SameasBillingAddressLabel")
    String sameAsBillingAddressLabel;

    @SerializedName("BILoggedOutShippingStateText")
    String bILoggedOutShippingStateText;

    @SerializedName("FirstNameLabel")
    String firstNameLabel;

    @SerializedName("LastNameLabel")
    String lastNameLabel;

    @SerializedName("DOBLabel")
    String dOBLabel;

    @SerializedName("DOBFormat")
    String dOBFormat;

    @SerializedName("EmailAddressLabel")
    String emailAddressLabel;

    @SerializedName("PasswordLabel")
    String passwordLabel;

    @SerializedName("ForgotPasswordLabel")
    String forgotPasswordLabel;

    @SerializedName("ForgotPasswordPageTitle")
    String forgotPasswordPageTitle;

    @SerializedName("ForgotPasswordHeader")
    String forgotPasswordHeader;

    @SerializedName("ResetPasswordPageTitle")
    String resetPasswordPageTitle;

    @SerializedName("ResetPasswordHeader")
    String resetPasswordHeader;

    @SerializedName("NewPasswordLabel")
    String newPasswordLabel;

    @SerializedName("SubmitButtonText")
    String submitButtonText;

    @SerializedName("DOBToolTip")
    String dOBToolTip;

    @SerializedName("PhoneNumberToolTip")
    String phoneNumberToolTip;

    @SerializedName("PasswordToolTip")
    String passwordToolTip;

    @SerializedName("RememberMeLabel")
    String rememberMeLabel;

    @SerializedName("NotRobotLabel")
    String notRobotLabel;

    @SerializedName("RequiredFieldsLabel")
    String requiredFieldsLabel;

    @SerializedName("EditLabel")
    String editLabel;

    @SerializedName("GuestLoginContinueLabel")
    String guestLoginContinueLabel;

    @SerializedName("GuestLoginIntroText")
    String guestLoginIntroText;

    @SerializedName("LoginFormLabel")
    String loginFormLabel;

    @SerializedName("LoginLabel")
    String loginLabel;

    @SerializedName("LoginPageTitle")
    String loginPageTitle;

    @SerializedName("SignInLabel")
    String signInLabel;

    @SerializedName("OrderConfirmationSignUpLabel")
    String orderConfirmationSignUpLabel;

    @SerializedName("CreateAccountLabel")
    String createAccountLabel;

    @SerializedName("CreateAccountPageTitle")
    String createAccountPageTitle;

    @SerializedName("CreateAccountNewLabel")
    String createAccountNewLabel;

    @SerializedName("CreateAccountLinkLabel")
    String createAccountLinkLabel;

    @SerializedName("PaymentSummaryHeading")
    String paymentSummaryHeading;

    @SerializedName("TodaysTotalLabel")
    String todaysTotalLabel;

    @SerializedName("AmountFinancedLabel")
    String amountFinancedLabel;

    @SerializedName("FinancedPaymentLabel")
    String financedPaymentLabel;

    @SerializedName("PaymentOfLabel")
    String paymentOfLabel;

    @SerializedName("TaxLabel")
    String taxLabel;

    @SerializedName("CreditCardChargedLabel")
    String creditCardChargedLabel;

    @SerializedName("CheckOutLabel")
    String checkOutLabel;

    @SerializedName("CardInformationLabel")
    String cardInformationLabel;

    @SerializedName("CardsAcceptedLabel")
    String cardsAcceptedLabel;

    @SerializedName("CompletePurchaseLabel")
    String completePurchaseLabel;

    @SerializedName("RemoveLabel")
    String removeLabel;

    @SerializedName("DoneLabel")
    String doneLabel;

    @SerializedName("SuffixLabel")
    String suffixLabel;

    @SerializedName("SelectLabel")
    String selectLabel;

    @SerializedName("SelectTicketQuantityLabel")
    String selectTicketQuantityLabel;

    @SerializedName("SelectParksLabel")
    String selectParksLabel;

    @SerializedName("SelectDateLabel")
    String selectDateLabel;

    @SerializedName("SelectTimeLabel")
    String selectTimeLabel;

    @SerializedName("SelectTierLabel")
    String selectTierLabel;

    @SerializedName("ShippingLabel")
    String shippingLabel;

    @SerializedName("BillingLabel")
    String billingLabel;

    @SerializedName("DownPaymentLabel")
    String downPaymentLabel;

    @SerializedName("SignUpTNC")
    String signUpTNC;

    @SerializedName("GuestLoginOptIn")
    String guestLoginOptIn;

    @SerializedName("TNC")
    String tnc;

    @SerializedName("pmt-card-card1")
    String pmtCard1;

    @SerializedName("pmt-card-card2")
    String pmtCard2;

    @SerializedName("pmt-card-card3")
    String pmtCard3;

    @SerializedName("pmt-card-card4")
    String pmtCard4;

    @SerializedName("certified-icon")
    String certifiedIcon;

    @SerializedName("ManageAccountLabel")
    String manageAccountLabel;

    @SerializedName("OrderConfirmationSignUpIntroText")
    String orderConfirmationSignUpIntroText;

    @SerializedName("MobileAppPromoTeaserText")
    String mobileAppPromoTeaserText;

    @SerializedName("ManageAccountPromoTeaserText")
    String manageAccountPromoTeaserText;

    @SerializedName("ShareHeader")
    String shareHeader;

    @SerializedName("ShareCopy")
    String shareCopy;

    @SerializedName("MobileAppPromoAppleLogo")
    String mobileAppPromoAppleLogo;

    @SerializedName("MobileAppPromoGooglePlayLogo")
    String mobileAppPromoGooglePlayLogo;

    @SerializedName("icon-share-facebook")
    String iconShareFacebook;

    @SerializedName("ER1")
    String er1;

    @SerializedName("ER2")
    String er2;

    @SerializedName("ER3")
    String er3;

    @SerializedName("ER4")
    String er4;

    @SerializedName("ER5")
    String er5;

    @SerializedName("ER6")
    String er6;

    @SerializedName("ER7")
    String er7;

    @SerializedName("ER8")
    String er8;

    @SerializedName("ER9")
    String er9;

    @SerializedName("ER10")
    String er10;

    @SerializedName("ER11")
    String er11;

    @SerializedName("ER12")
    String er12;

    @SerializedName("ER13")
    String er13;

    @SerializedName("ER14")
    String er14;

    @SerializedName("ER15")
    String er15;

    @SerializedName("ER16")
    String er16;

    @SerializedName("ER17")
    String er17;

    @SerializedName("ER18")
    String er18;

    @SerializedName("ER19")
    String er19;

    @SerializedName("ER20")
    String er20;

    @SerializedName("ER21")
    String er21;

    @SerializedName("ER22")
    String er22;

    @SerializedName("ER23")
    String er23;

    @SerializedName("ER24")
    String er24;

    @SerializedName("ER25")
    String er25;

    @SerializedName("ER26")
    String er26;

    @SerializedName("ER27")
    String er27;

    @SerializedName("ER28")
    String er28;

    @SerializedName("ER29")
    String er29;

    @SerializedName("ER30")
    String er30;

    @SerializedName("ER31")
    String er31;

    @SerializedName("ER32")
    String er32;

    @SerializedName("ER33")
    String er33;

    @SerializedName("ER34")
    String er34;

    @SerializedName("ER35")
    String er35;

    @SerializedName("ER36")
    String er36;

    @SerializedName("ER37")
    String er37;

    @SerializedName("ER38")
    String er38;

    @SerializedName("ER39")
    String er39;

    @SerializedName("ER40")
    String er40;

    @SerializedName("ER41")
    String er41;

    @SerializedName("ER42")
    String er42;

    @SerializedName("ER43")
    String er43;

    @SerializedName("ER44")
    String er44;

    @SerializedName("ER45")
    String er45;

    @SerializedName("ER46")
    String er46;

    @SerializedName("ER47")
    String er47;

    @SerializedName("ER48")
    String er48;

    @SerializedName("ER49")
    String er49;

    @SerializedName("ER50")
    String er50;

    @SerializedName("ER51")
    String er51;

    @SerializedName("ER52")
    String er52;

    @SerializedName("ER53")
    String er53;

    @SerializedName("ER54")
    String er54;

    @SerializedName("ER55")
    String er55;

    @SerializedName("ER56")
    String er56;

    @SerializedName("ER57")
    String er57;

    @SerializedName("ER58")
    String er58;

    @SerializedName("ER59")
    String er59;

    @SerializedName("ER60")
    String er60;

    @SerializedName("ER61")
    String er61;

    @SerializedName("ER62")
    String er62;

    @SerializedName("ER63")
    String er63;

    @SerializedName("ER64")
    String er64;

    @SerializedName("ER65")
    String er65;

    @SerializedName("ER66")
    String er66;

    @SerializedName("ER67")
    String er67;

    @SerializedName("ER68")
    String er68;

    @SerializedName("ER69")
    String er69;

    @SerializedName("ER70")
    String er70;

    @SerializedName("ER71")
    String er71;

    @SerializedName("ER72")
    String er72;

    @SerializedName("ER73")
    String er73;

    @SerializedName("ER74")
    String er74;

    @SerializedName("ER75")
    String er75;

    @SerializedName("ER76")
    String er76;

    @SerializedName("ER77")
    String er77;

    @SerializedName("ER78")
    String er78;

    @SerializedName("ER79")
    String er79;

    @SerializedName("ER80")
    String er80;

    @SerializedName("ER81")
    String er81;

    @SerializedName("ER82")
    String er82;

    @SerializedName("ER83")
    String er83;

    @SerializedName("ER84")
    String er84;

    @SerializedName("ER85")
    String er85;

    @SerializedName("ER86")
    String er86;

    @SerializedName("ER87")
    String er87;

    @SerializedName("ER88")
    String er88;

    @SerializedName("ER89")
    String er89;

    @SerializedName("ER90")
    String er90;

    @SerializedName("ER91")
    String er91;

    @SerializedName("ER92")
    String er92;

    @SerializedName("ER93")
    String er93;

    @SerializedName("ER94")
    String er94;

    @SerializedName("ER95")
    String er95;

    @SerializedName("ER96")
    String er96;

    @SerializedName("ER97")
    String er97;

    @SerializedName("ER98")
    String er98;

    @SerializedName("ER99")
    String er99;

    @SerializedName("ER100")
    String er100;

    @SerializedName("ER101")
    String er101;

    @SerializedName("ER102")
    String er102;

    @SerializedName("ER103")
    String er103;

    @SerializedName("W1")
    String w1;

    @SerializedName("W2")
    String w2;

    @SerializedName("W3")
    String w3;

    @SerializedName("W4")
    String w4;

    @SerializedName("W5")
    String w5;

    @SerializedName("W6")
    String w6;

    @SerializedName("W7")
    String w7;

    @SerializedName("W8")
    String w8;

    @SerializedName("W9")
    String w9;

    @SerializedName("W10")
    String w10;

    @SerializedName("W11")
    String w11;

    @SerializedName("W12")
    String w12;

    @SerializedName("SU1")
    String su1;

    @SerializedName("SU2")
    String su2;

    @SerializedName("SU3")
    String su3;

    @SerializedName("SU4")
    String su4;

    @SerializedName("SU5")
    String su5;

    @SerializedName("SU6")
    String su6;

    @SerializedName("SU7")
    String su7;

    @SerializedName("SU8")
    String su8;

    @SerializedName("SU9")
    String su9;

    @SerializedName("SU10")
    String su10;

    @SerializedName("SU11")
    String su11;

    @SerializedName("SU12")
    String su12;

    @SerializedName("SU13")
    String su13;

    @SerializedName("SU14")
    String su14;

    @SerializedName("SU15")
    String su15;

    @SerializedName("SU16")
    String su16;

    @SerializedName("SU17")
    String su17;

    @SerializedName("SU18")
    String su18;

    @SerializedName("SU20")
    String su20;

    @SerializedName("SU21")
    String su21;

    @SerializedName("OkToSendLabel")
    String okToSendLabel;

    @SerializedName("hhn")
    String hhn;

    @SerializedName("usf")
    String usf;

    @SerializedName("ioa")
    String ioa;

    @SerializedName("vb")
    String vb;

    @SerializedName("cw")
    String cw;

    @SerializedName("hotels")
    String hotels;

    @SerializedName("mg")
    String mg;

    @SerializedName("rtu")
    String rtu;

    @SerializedName("bmg")
    String bmg;

    @SerializedName("wwohp")
    String wwohp;

    @SerializedName("kingkong")
    String kingkong;

    @SerializedName("ErrAssignEntitlements")
    String errAssignEntitlements;

    @SerializedName("UnassignedAdultLabel")
    String unassignedAdultLabel;

    @SerializedName("UnassignedChildLabel")
    String unassignedChildtLabel;

    @SerializedName("PrimaryAccountLabel")
    String primaryAccountLabel;

    @SerializedName("SelectGuestNameLabel")
    String selectGuestNameLabel;

    @SerializedName("AddNewNameLabel")
    String addNewNameLabel;

    @SerializedName("ThisisMeLabel")
    String thisIsMeLabel;

    @SerializedName("AssignItemsLabel")
    String assignItemsLabel;

    @SerializedName("UnAssignedPurchasesLabel")
    String unassignedPurchasesLabel;

    @SerializedName("AddGuestLabel")
    String addGuestLabel;

    @SerializedName("PurchasesLeftToAssignLabel")
    String purchasesLeftToAssignLabel;

    @SerializedName("ValidLabel")
    String validLabel;

    @SerializedName("YesLabel")
    String yesLabel;

    @SerializedName("NoLabel")
    String noLabel;

    @SerializedName("ViewCartLabel")
    String viewCartLabel;

    @SerializedName("SubTotalLabel")
    String subTotalLabel;

    @SerializedName("MoreLabel")
    String moreLabel;

    @SerializedName("AddToCartLabel")
    String addToCartLabel;

    @SerializedName("AdultsLabel")
    String adultsLabel;

    @SerializedName("AdultLabel")
    String adultLabel;

    @SerializedName("PerAdultLabel")
    String perAdultLabel;

    @SerializedName("PerChildLabel")
    String perChildLabel;

    @SerializedName("ChildrenLabel")
    String childrenLabel;

    @SerializedName("BackLabel")
    String backLabel;

    @SerializedName("ContinueLabel")
    String continueLabel;

    @SerializedName("AllAgesLabel")
    String allAgesLabel;

    @SerializedName("SeeDetailsLabel")
    String seeDetailsLabel;

    @SerializedName("BuyTicketsLabel")
    String buyTicketsLabel;

    @SerializedName("ShopNowLabel")
    String shopNowLabel;

    @SerializedName("FreeLabel")
    String freeLabel;

    @SerializedName("MonthLabel")
    String monthLabel;

    @SerializedName("YearLabel")
    String yearLabel;

    @SerializedName("NextLabel")
    String nextLabel;

    @SerializedName("DayLabel")
    String dayLabel;

    @SerializedName("DaysLabel")
    String daysLabel;

    @SerializedName("SCAnnualPassLabel")
    String sCAnnualPassLabel;

    @SerializedName("UpdateLabel")
    String updateLabel;

    @SerializedName("CancelLabel")
    String cancelLabel;

    @SerializedName("SoldOutLabel")
    String soldOutLabel;

    @SerializedName("PromoBPImage")
    String promoBPImage;

    @SerializedName("PromoBPTitle")
    String promoBPTitle;

    @SerializedName("PromoBPTeaserText")
    String promoBPTeaserText;

    @SerializedName("PromoBPDetails")
    String promoBPDetails;

    @SerializedName("PageHeaderPHTitle")
    String pageHeaderPHTitle;

    @SerializedName("PageHeaderPHIntroText")
    String pageHeaderPHIntroText;

    @SerializedName("PageHeaderPHSEOTitle")
    String pageHeaderPHSEOTitle;

    @SerializedName("PageHeaderPHSEODescription")
    String pageHeaderPHSEODescription;

    @SerializedName("PageHeaderPHSEOKeywords")
    String pageHeaderPHSEOKeywords;

    @SerializedName("PageHeaderPHRobots")
    String pageHeaderPHRobots;

    @SerializedName("PageHeaderWPTitle")
    String pageHeaderWPTitle;

    @SerializedName("PageHeaderWPSEOTitle")
    String pageHeaderWPSEOTitle;

    @SerializedName("PageHeaderWPSEODescription")
    String pageHeaderWPSEODescription;

    @SerializedName("PageHeaderWPSEOKeywords")
    String pageHeaderWPSEOKeywords;

    @SerializedName("PageHeaderWPRobots")
    String pageHeaderWPRobots;

    @SerializedName("PageHeaderOCTitle")
    String pageHeaderOCTitle;

    @SerializedName("PageHeaderOCIntroText")
    String pageHeaderOCIntroText;

    @SerializedName("PageHeaderOCUpsellText")
    String pageHeaderOCUpsellText;

    @SerializedName("PageHeaderOCHeroImage")
    String pageHeaderOCHeroImage;

    @SerializedName("PageHeaderOCBgColor")
    String pageHeaderOCBgColor;

    @SerializedName("PageHeaderOCSEOTitle")
    String pageHeaderOCSEOTitle;

    @SerializedName("PageHeaderOCSEODescription")
    String pageHeaderOCSEODescription;

    @SerializedName("PageHeaderOCSEOKeywords")
    String pageHeaderOCSEOKeywords;

    @SerializedName("PageHeaderOCRobots")
    String pageHeaderOCRobots;

    @SerializedName("PageHeaderPTTitle")
    String pageHeaderPTTitle;

    @SerializedName("PageHeaderPTIntroText")
    String pageHeaderPTIntroText;

    @SerializedName("PageHeaderPTSEOTitle")
    String pageHeaderPTSEOTitle;

    @SerializedName("PageHeaderPTSEODescription")
    String pageHeaderPTSEODescription;

    @SerializedName("PageHeaderPTSEOKeywords")
    String pageHeaderPTSEOKeywords;

    @SerializedName("PageHeaderPTRobots")
    String pageHeaderPTRobots;

    @SerializedName("PageHeaderMPTitle")
    String pageHeaderMPTitle;

    @SerializedName("PageHeaderMPSEOTitle")
    String pageHeaderMPSEOTitle;

    @SerializedName("PageHeaderMPSEODescription")
    String pageHeaderMPSEODescription;

    @SerializedName("PageHeaderMPSEOKeywords")
    String pageHeaderMPSEOKeywords;

    @SerializedName("PageHeaderMPRobots")
    String pageHeaderMPRobots;

    @SerializedName("PageHeaderEPTitle")
    String pageHeaderEPTitle;

    @SerializedName("PageHeaderEPAppTitle")
    String pageHeaderEPAppTitle;

    @SerializedName("PageHeaderEPIntroText")
    String pageHeaderEPIntroText;

    @SerializedName("PageHeaderEPUpsellText")
    String pageHeaderEPUpsellText;

    @SerializedName("PageHeaderEPSEOTitle")
    String pageHeaderEPSEOTitle;

    @SerializedName("PageHeaderEPSEODescription")
    String pageHeaderEPSEODescription;

    @SerializedName("PageHeaderEPSEOKeywords")
    String pageHeaderEPSEOKeywords;

    @SerializedName("PageHeaderEPRobots")
    String pageHeaderEPRobots;

    @SerializedName("PageHeaderCompareAPIntroText")
    String pageHeaderCompareAPIntroText;

    @SerializedName("PageHeaderCompareAPLinkLabel")
    String pageHeaderCompareAPLinkLabel;

    @SerializedName("PageHeaderCompareAPSEOTitle")
    String pageHeaderCompareAPSEOTitle;

    @SerializedName("PageHeaderCompareAPSEODescription")
    String pageHeaderCompareAPSEODescription;

    @SerializedName("PageHeaderCompareAPSEOKeywords")
    String pageHeaderCompareAPSEOKeywords;

    @SerializedName("PageHeaderCompareAPRobots")
    String pageHeaderCompareAPRobots;

    @SerializedName("PageHeaderCLTitle")
    String pageHeaderCLTitle;

    @SerializedName("PageHeaderCLUpsellText")
    String pageHeaderCLUpsellText;

    @SerializedName("PageHeaderCLHeroImage")
    String pageHeaderCLHeroImage;

    @SerializedName("PageHeaderCLSEOTitle")
    String pageHeaderCLSEOTitle;

    @SerializedName("PageHeaderCLSEODescription")
    String pageHeaderCLSEODescription;

    @SerializedName("PageHeaderCLSEOKeywords")
    String pageHeaderCLSEOKeywords;

    @SerializedName("PageHeaderCLRobots")
    String pageHeaderCLRobots;

    @SerializedName("PageHeaderAETitle")
    String pageHeaderAETitle;

    @SerializedName("PageHeaderAEAppTitle")
    String pageHeaderAEAppTitle;

    @SerializedName("PageHeaderAEIntroText")
    String pageHeaderAEIntroText;

    @SerializedName("PageHeaderAESEOTitle")
    String pageHeaderAESEOTitle;

    @SerializedName("PageHeaderAESEODescription")
    String pageHeaderAESEODescription;

    @SerializedName("PageHeaderAESEOKeywords")
    String pageHeaderAESEOKeywords;

    @SerializedName("PageHeaderAERobots")
    String pageHeaderAERobots;

    @SerializedName("PageHeaderAOTitle")
    String pageHeaderAOTitle;

    @SerializedName("PageHeaderAOIntroText")
    String pageHeaderAOIntroText;

    @SerializedName("PageHeaderAOSEOTitle")
    String pageHeaderAOSEOTitle;

    @SerializedName("PageHeaderAOSEODescription")
    String pageHeaderAOSEODescription;

    @SerializedName("PageHeaderAOSEOKeywords")
    String pageHeaderAOSEOKeywords;

    @SerializedName("PageHeaderAORobots")
    String pageHeaderAORobots;

    @SerializedName("PageHeaderAPTitle")
    String pageHeaderAPTitle;

    @SerializedName("PageHeaderAPIntroText")
    String pageHeaderAPIntroText;

    @SerializedName("PageHeaderAPUpsellText")
    String pageHeaderAPUpsellText;

    @SerializedName("PageHeaderAPLinkLabel")
    String pageHeaderAPLinkLabel;

    @SerializedName("PageHeaderAPSEOTitle")
    String pageHeaderAPSEOTitle;

    @SerializedName("PageHeaderAPSEODescription")
    String pageHeaderAPSEODescription;

    @SerializedName("PageHeaderAPSEOKeywords")
    String pageHeaderAPSEOKeywords;

    @SerializedName("PageHeaderAPRobots")
    String pageHeaderAPRobots;

    @SerializedName("PageHeaderAOCTitle")
    String pageHeaderAOCTitle;

    @SerializedName("PageHeaderAOCSEOTitle")
    String pageHeaderAOCSEOTitle;

    @SerializedName("PageHeaderAOCSEODescription")
    String pageHeaderAOCSEODescription;

    @SerializedName("PageHeaderAOCSEOKeywords")
    String pageHeaderAOCSEOKeywords;

    @SerializedName("PageHeaderAOCRobots")
    String pageHeaderAOCRobots;

    @SerializedName("APComparisionTable")
    String aPComparisionTable;

    @SerializedName("UEPHeaderDateFormat")
    String uepHeaderDateFormat;

    @SerializedName("UEP1ParkDescription")
    String uep1Parkdescription;

    @SerializedName("UEP2ParkDescription")
    String uep2Parkdescription;

    @SerializedName("UEP3ParkDescription")
    String uep3Parkdescription;

    @SerializedName("TNCTermsLinkLabel")
    String tncTermsLinkLabel;

    @SerializedName("TNCTermsLink")
    String tncTermsLink;

    @SerializedName("TNCAndLabel")
    String tncAndLabel;

    @SerializedName("TNCPrivacyLinkLabel")
    String tncPrivacyLinkLabel;

    @SerializedName("TNCPrivacyLink")
    String tncPrivacyLink;

    @SerializedName("PrivacyInformationText")
    String privacyInformationText;

    @SerializedName("PrivacyInformationLinkUrl")
    String privacyInformationLinkUrl;

    @SerializedName("PrivacyInformationLinkText")
    String privacyInformationLinkText;

    @SerializedName("TextBeforeTNC")
    String textBeforeTnc;

    @SerializedName("Value-Regular-Description")
    String valueRegularDescription;

    @SerializedName("Value-Peak-Description")
    String valuePeakDescription;

    @SerializedName("Regular-Value-Description")
    String regularValueDescription;

    @SerializedName("Regular-Peak-Description")
    String regularPeakDescription;

    @SerializedName("Peak-Value-Description")
    String peakValueDescription;

    @SerializedName("Peak-Regular-Description")
    String peakRegularDescription;

    @SerializedName("Value-Value-Description")
    String valueValueDescription;

    @SerializedName("Regular-Regular-Description")
    String regularRegularDescription;

    @SerializedName("Peak-Peak-Description")
    String peakPeakDescription;

    @SerializedName("AvailableTimesLabel")
    String availableTimesLabel;

    @SerializedName("UnavailableLabel")
    String unavailableLabel;

    @SerializedName("PartyMemberAssignSecondaryText")
    String partyMemberAssignSecondaryText;

    @SerializedName("TicketProcessingMsg")
    String ticketProcessingMsg;

    @SerializedName("EmptyWalletText")
    String emptyWalletText;

    @SerializedName("WalletEmptyHeroImage")
    String walletEmptyHeroImage;

    @SerializedName("EmailAddressToolTip")
    String emailAddressToolTip;

    @SerializedName("ContactInformationTitle")
    String contactInformationTitle;

    @SerializedName("AddressDeletingMessage")
    String addressDeletingMessage;

    @SerializedName("AddressListPageTitle")
    String addressListPageTitle;

    @SerializedName("PrimaryAddressSwitchLabel")
    String primaryAddressSwitchLabel;

    @SerializedName("ManageAccountAddAddressLabel")
    String manageAccountAddAddressLabel;

    @SerializedName("ContactInformationUpdateLabel")
    String contactInformationUpdateLabel;

    @SerializedName("AccountHoldingOptionLabel")
    String accountHoldingOptionLabel;

    @SerializedName("PersonalCardLabel")
    String personalCardLabel;

    @SerializedName("AddressCardLabel")
    String addressCardLabel;

    @SerializedName("MyProfilePageTitle")
    String myProfilePageTitle;

    @SerializedName("PrefixLabel")
    String prefixLabel;

    @SerializedName("PersonalInformationPageTitle")
    String personalInformationPageTitle;

    @SerializedName("NoneLabel")
    String noneLabel;

    @SerializedName("PrefixValues")
    String prefixValues;

    @SerializedName("CurrentPasswordLabel")
    String currentPasswordLabel;

    @SerializedName("UpdatePasswordPageTitle")
    String updatePasswordPageTitle;

    @SerializedName("BIPriceStringText")
    String bIPriceStringText;

    @SerializedName("PageHeaderPaymentTitle")
    String pageHeaderPaymentTitle;

    @SerializedName("TicketSelectionApplyLabel")
    String ticketSelectionApplyLabel;

    @SerializedName("PageHeaderSCTitle")
    String pageHeaderSCTitle;

    @SerializedName("SelectNumberOfDaysLabel")
    String selectNumberOfDaysLabel;

    @SerializedName("AddOnDetailsLabel")
    String addOnDetailsLabel;

    @SerializedName("SCPromoApplied")
    String scPromoApplied;

    @SerializedName("LearnMoreLabel")
    String learnMoreLabel;

    @SerializedName("HearMoreLabel")
    String hearMoreLabel;

    @SerializedName("AllFieldsOptionalLabel")
    String allFieldsOptionalLabel;

    @SerializedName("DontTellMeLabel")
    String dontTellMeLabel;

    @SerializedName("TellMeSomeLabel")
    String tellMeSomeLabel;

    @SerializedName("TellMeMoreLabel")
    String tellMeMoreLabel;

    @SerializedName("TravelSeasonPrefLabel")
    String travelSeasonPrefLabel;

    @SerializedName("CPTextMessageOptionLabel")
    String CPTextMessageOptionLabel;

    @SerializedName("CPEmailOptionLabel")
    String CPEmailOptionLabel;

    @SerializedName("CPSocialAdvertisingOptionLabel")
    String CPSocialAdvertisingOptionLabel;

    @SerializedName("CPSendMailOptionLabel")
    String CPSendMailOptionLabel;

    @SerializedName("CPSocialAdvertisingDescription")
    String CPSocialAdvertisingDescription;

    @SerializedName("CPEmailFrequencyOptions")
    String CPEmailFrequencyOptions;

    @SerializedName("CPHeadingLabel")
    String CPHeadingLabel;

    @SerializedName("MyInterestsPageTitle")
    String MyInterestsPageTitle;

    @SerializedName("ManageInterestsOrder")
    String ManageInterestsOrder;

    @SerializedName("CPTextMessageOkOptionLabel")
    String CPTextMessageOkOptionLabel;

    @SerializedName("CPTextMessageNotOkOptionLabel")
    String CPTextMessageNotOkOptionLabel;

    @SerializedName("CPEmailOkOptionLabel")
    String CPEmailOkOptionLabel;

    @SerializedName("CPEmailNotOkOptionLabel")
    String CPEmailNotOkOptionLabel;

    @SerializedName("CPSocialAdvertisingOkOptionLabel")
    String CPSocialAdvertisingOkOptionLabel;

    @SerializedName("CPSocialAdvertisingNotOkOptionLabel")
    String CPSocialAdvertisingNotOkOptionLabel;

    @SerializedName("CPSendMailOkOptionLabel")
    String CPSendMailOkOptionLabel;

    @SerializedName("CPSendMailNotOkOptionLabel")
    String CPSendMailNotOkOptionLabel;

    @SerializedName("CPInterestsUpdated")
    String CPInterestsUpdated;

    @SerializedName("SpecialOfferBannerText")
    String specialOfferBannerText;

    @SerializedName("SCValueTextColor")
    String SCValueTextColor;

    @SerializedName("SCRegularTextColor")
    String SCRegularTextColor;

    @SerializedName("SCPeakTextColor")
    String SCPeakTextColor;

    @SerializedName("SCMixedTextColor")
    String SCMixedTextColor;

    @SerializedName("TravelPreferencesOptions")
    String TravelPreferencesOptions;

    @SerializedName("TravelPreferencesLabels")
    String TravelPreferencesLabels;

    @SerializedName("ShoppingCartSpecialOfferBannerText")
    String shoppingCartSpecialOfferBannerText;

    @SerializedName("InterestsMinimumDisplayValue")
    String InterestsMinimumDisplayValue;

    @SerializedName("ProfileInterestParksLabel")
    String ProfileInterestParksLabel;

    @SerializedName("ProfileInterestSeasonsLabel")
    String ProfileInterestSeasonsLabel;

    @SerializedName("MinimumInterestLevel")
    Integer MinimumInterestLevel;

    @SerializedName("AnnualPassHeaderLabel")
    String annualPassHeaderLabel;

    @SerializedName("FlexPayDownPaymentCopy")
    String flexPayDownPaymentCopy;

    @SerializedName("FlexPayFinancedCopy")
    String flexPayFinancedCopy;

    @SerializedName("CompareAPUrl")
    String compareAPUrl;

    @SerializedName("TextBeforeSignUpTNC")
    String textBeforeSignUpTNC;

    @SerializedName("FlexPayLinkLabel")
    String flexPayLinkLabel;

    @SerializedName("PageHeaderUEPCTitle")
    String pageHeaderUEPCTitle;

    @SerializedName("StartingFromLabel")
    String startingFromLabel;

    @SerializedName("PasswordFlagColors")
    String passwordFlagColors;

    @SerializedName("PasswordFlagStrength")
    String passwordFlagStrength;

    @SerializedName("PaymentPromosImages")
    private String paymentPromoImages;

    @SerializedName("PaymentPromosCopy")
    private String paymentPromoCopy;

    @SerializedName("PaymentEmptyHeading")
    private String paymentEmptyHeading;

    @SerializedName("PaymentEmptyCopy")
    private String paymentEmptyCopy;

    @SerializedName("PaymentPartyMembersCopy")
    private String paymentPartyMembersCopy;

    @SerializedName("PaymentPartMembersImage")
    private String paymentPartMembersImage;

    @SerializedName("AddCardLabel")
    private String addCardLabel;

    @SerializedName("NoLimitLabel")
    String noLimitLabel;

    @SerializedName("DailySpendingLimitsLabel")
    String dailySpendingLimitsLabel;

    @SerializedName("FolioPaymentMethodsLabel")
    String folioPaymentMethodsLabel;

    @SerializedName("ManageAllLabel")
    String manageAllLabel;

    @SerializedName("DailySpendingLimitSelectionLabel")
    String dailySpendingLimitSelectionLabel;

    @SerializedName("DailySpendingLimitOption1Label")
    String dailySpendingLimitOption1Label;

    @SerializedName("DailySpendingLimitOption2Label")
    String dailySpendingLimitOption2Label;

    @SerializedName("DailySpendingLimitOption3Label")
    String dailySpendingLimitOption3Label;

    @SerializedName("ConfirmLabel")
    String confirmLabel;

    @SerializedName("WalletCardDeleteMsg")
    String walletCardDeleteMsg;

    @SerializedName("DeleteFolioCardLabel")
    String deleteFolioCardLabel;

    @SerializedName("WalletCardDeletePrimaryMsg")
    String walletCardDeletePrimaryMsg;

    @SerializedName("PageHeaderWLPTitle")
    String pageHeaderWLPTitle;

    @SerializedName("WalletLinkPurchasesLinkLabel")
    String walletLinkPurchasesLinkLabel;

    @SerializedName("WalletLinkPurchaseOrderOrTicketNumberLabel")
    String walletLinkPurchaseOrderOrTicketNumberLabel;

    @SerializedName("WalletLinkPurchaseInfoToolTip")
    String walletLinkPurchaseInfoToolTip;

    @SerializedName("WalletLinkPurchaseLocatedHeader")
    String walletLinkPurchaseLocatedHeader;

    @SerializedName("WalletLinkPurchaseSelectLocationHeader")
    String walletLinkPurchaseSelectLocationHeader;

    @SerializedName("WalletLinkPurchaseFirstNameLabel")
    String walletLinkPurchaseFirstNameLabel;

    @SerializedName("WalletLinkPurchaseLastNameLabel")
    String walletLinkPurchaseLastNameLabel;

    @SerializedName("WalletLinkSelectChannelLabel")
    String walletLinkSelectChannelLabel;

    @SerializedName("AlertsHeaderLabel")
    String alertsHeaderLabel;

    @SerializedName("AlertsHeaderCopy")
    String alertsHeaderCopy;

    @SerializedName("SetAlertsLabel")
    String setAlertsLabel;

    @SerializedName("AlertsOptionEmailLabel")
    String alertsOptionEmailLabel;

    @SerializedName("AlertsOptionTextLabel")
    String alertsOptionTextLabel;

    @SerializedName("SaveLabel")
    String saveLabel;

    @SerializedName("AlertsOptionHeaderLinkLabel")
    String alertsOptionHeaderLinkLabel;

    @SerializedName("TransactionHistorySeeAllLabel")
    String transactionHistorySeeAllLabel;

    @SerializedName("PINHeaderLabel")
    String pinHeaderLabel;

    @SerializedName("PINHeaderCopy")
    String pinHeaderCopy;

    @SerializedName("EnterPINLabel")
    String enterPINLabel;

    @SerializedName("ConfirmationCreatePINHeader")
    String confirmationCreatePinHeader;

    @SerializedName("ConfirmationCreatePINSubHeader")
    String confirmationCreatePinSubHeader;

    @SerializedName("ConfirmationCreatePINMobileCopy")
    String confirmationCreatePinMobileCopy;

    @SerializedName("ConfirmationCreatePINPartyCopy")
    String confirmationCreatePinPartyCopy;

    @SerializedName("ConfirmationCreatePINTransactionCopy")
    String confirmationCreatePinTransactionCopy;

    @SerializedName("ConfirmationCreatePINCopyImages")
    String confirmationCreatePinCopyImages;

    @SerializedName("ConfirmationCreatePINCopy")
    String confirmationCreatePinCopy;

    @NonNull
    private static String getNonNullTridionString(String tridionString) {
        if (TextUtils.isEmpty(tridionString)) {
            if (BuildConfig.SHOW_MISSING_REMOTE_TEXT) {
                return "!!MISSING!!";
            }
            else {
                return "";
            }
        }
        else {
            return tridionString;
        }
    }

    private static ArrayList<String> getTridionStringAsArrayList(String tridionString) {
        String tridionStringArray = getNonNullTridionString(tridionString).trim().replace("[\"","").replace("\"]","").replace("\",\"",",");
        ArrayList<String> tridionStringArrayList = new ArrayList<>();
        tridionStringArrayList.addAll(Arrays.asList(tridionStringArray.split("\\s*,\\s*")));
        return tridionStringArrayList;
    }

    public String getAddCardLabel() {
        return getNonNullTridionString(addCardLabel);
    }

    public String getPaymentPartMembersImage() {
        return getNonNullTridionString(paymentPartMembersImage);
    }

    public String getPaymentPromoImages() {
        return getNonNullTridionString(paymentPromoImages);
    }

    public String getPaymentPromoCopy() {
        return getNonNullTridionString(paymentPromoCopy);
    }

    public String getPaymentPartyMembersCopy() {
        return getNonNullTridionString(paymentPartyMembersCopy);
    }

    public String getPaymentEmptyCopy() {
        return getNonNullTridionString(paymentEmptyCopy);
    }

    public String getPaymentEmptyHeading() {
        return getNonNullTridionString(paymentEmptyHeading);
    }

    public String getBuyTicketsEmptyMessageTitle() {
        return getNonNullTridionString(buyTicketsEmptyMessageTitle);
    }

    public String getBuyTicketsEmptyMessageSubtitle() {
        return getNonNullTridionString(buyTicketsEmptyMessageSubtitle);
    }

    public String getBuyTicketsFilterNoOfDaysPopUp() {
        return getNonNullTridionString(buyTicketsFilterNoOfDaysPopUp);
    }

    public String getBuyTicketsFilterSectionDaysHeader() {
        return getNonNullTridionString(buyTicketsFilterSectionDaysHeader);
    }

    public String getBuyTicketsFilterFLPopUp() {
        return getNonNullTridionString(buyTicketsFilterFLPopUp);
    }

    public String getShoppingCartEmptyMessageTitle() {
        return getNonNullTridionString(shoppingCartEmptyMessageTitle);
    }

    public String getShoppingCartEmptyMessageSubTitle() {
        return getNonNullTridionString(shoppingCartEmptyMessageSubTitle);
    }

    public String getMyTicketsEmptyMessageTitle() {
        return getNonNullTridionString(myTicketsEmptyMessageTitle);
    }

    public String getProductsListTicketType() {
        return getNonNullTridionString(productsListTicketType);
    }

    public String getProductsListBPGError() {
        return getNonNullTridionString(productsListBPGError);
    }

    public String getRegisterAccountHeader() {
        return getNonNullTridionString(registerAccountHeader);
    }

    public String getRegisterAccountSubTitle() {
        return getNonNullTridionString(registerAccountSubTitle);
    }

    public String getAssignTicketsLabel() {
        return getNonNullTridionString(assignTicketsLabel);
    }

    public String getAssignNamesHeader() {
        return getNonNullTridionString(assignNamesHeader);
    }

    public String getPrimaryGuest() {
        return getNonNullTridionString(primaryGuest);
    }

    public String getMyTicketsEmptyMessageSubTitle() {
        return getNonNullTridionString(myTicketsEmptyMessageSubTitle);
    }

    public String getRegisterAccountBulletList() {
        String bulletList = getNonNullTridionString(registerAccountBulletList);
        bulletList = bulletList.replaceAll("\\[\"", "<b><font color=#0574F0>&#8226;</b> ");
        bulletList = bulletList.replaceAll("\",\"", "<p><b><font color=#0574F0>&#8226;</b> ");
        bulletList = bulletList.replaceAll("\"]", "");
        return bulletList;
    }

    public String getTicketsLabel() {
        return getNonNullTridionString(ticketsLabel);
    }

    public String getExpressPassLabel() {
        return getNonNullTridionString(expressPassLabel);
    }

    public String getExtrasLabel() {
        return getNonNullTridionString(extrasLabel);
    }

    public String getNoOfDaysLabel() {
        return getNonNullTridionString(noOfDaysLabel);
    }

    public String getNoOfDaysToolTip() {
        return getNonNullTridionString(noOfDaysToolTip);
    }

    public String getAdultsAgeLabel() {
        return getNonNullTridionString(adultsAgeLabel);
    }

    public String getChildrenAgeLabel() {
        return getNonNullTridionString(childrenAgeLabel);
    }

    public String getNoOfGuestsTravelingLabel() {
        return getNonNullTridionString(noOfGuestsTravelingLabel);
    }

    public String getFloridaResidentLabel() {
        return getNonNullTridionString(floridaResidentLabel);
    }

    public String getFlResidentLabel() {
        return getNonNullTridionString(flResidentLabel);
    }

    public String getTicketsFloridaResidentLabel() {
        return getNonNullTridionString(ticketsFloridaResidentLabel);
    }

    public String getFloridaResidentToolTip() {
        return getNonNullTridionString(floridaResidentToolTip);
    }

    public String getTicketsNonFloridaResidentLabel() {
        return getNonNullTridionString(ticketsNonFloridaResidentLabel);
    }

    public String getSeeBlackoutDatesLabel() {
        return getNonNullTridionString(seeBlackoutDatesLabel);
    }

    public String getFloridaResidentBlockoutDates() {
        return getNonNullTridionString(floridaResidentBlockoutDates);
    }

    public String getFloridaResidentBlockoutDatesLabel() {
        return getNonNullTridionString(floridaResidentBlockoutDatesLabel);
    }

    public String getNonFloridaResidentBlockoutDatesLabel() {
        return getNonNullTridionString(nonFloridaResidentBlockoutDatesLabel);
    }

    public String getTicketingConfirmationText() {
        return getNonNullTridionString(ticketingConfirmationText);
    }

    public String getErrMsgNoProducts() {
        return getNonNullTridionString(errMsgNoProducts);
    }

    public String getNoOfParksLabel() {
        return getNonNullTridionString(noOfParksLabel);
    }

    public String getSelectDatesLabel() {
        return getNonNullTridionString(selectDatesLabel);
    }

    public String getNoOfGuestsLabel() {
        return getNonNullTridionString(noOfGuestsLabel);
    }

    public String getOnlineSavingsLabel() {
        return getNonNullTridionString(onlineSavingsLabel);
    }

    public String getOnlineSavingsToolTip() {
        return getNonNullTridionString(onlineSavingsToolTip);
    }

    public String getAllSC() {
        return getNonNullTridionString(allSC);
    }

    public String getDiningSC() {
        return getNonNullTridionString(diningSC);
    }

    public String getExperiencesSC() {
        return getNonNullTridionString(experiencesSC);
    }

    public String getPhotosSC() {
        return getNonNullTridionString(photosSC);
    }

    public String getStrollersSC() {
        return getNonNullTridionString(strollersSC);
    }

    public String getImgSliderTickets1() {
        return getNonNullTridionString(imgSliderTickets1);
    }

    public String getImgSliderTickets1Small() {
        return getNonNullTridionString(imgSliderTickets1Small);
    }

    public String getImgSliderTickets2() {
        return getNonNullTridionString(imgSliderTickets2);
    }

    public String getImgSliderTickets2Small() {
        return getNonNullTridionString(imgSliderTickets2Small);
    }

    public String getImgSliderTickets3() {
        return getNonNullTridionString(imgSliderTickets3);
    }

    public String getImgSliderTickets3Small() {
        return getNonNullTridionString(imgSliderTickets3Small);
    }

    public String getAOAllIcon() {
        return getNonNullTridionString(aOAllIcon);
    }

    public String getAODinningIcon() {
        return getNonNullTridionString(aODinningIcon);
    }

    public String getAOExperienceIcon() {
        return getNonNullTridionString(aOExperienceIcon);
    }

    public String getAOPhotosIcon() {
        return getNonNullTridionString(aOPhotosIcon);
    }

    public String getAOStrollersIcon() {
        return getNonNullTridionString(aOStrollersIcon);
    }

    public String getQuantityLabel() {
        return getNonNullTridionString(quantityLabel);
    }

    public String getWalletButtonLabel() {
        return getNonNullTridionString(walletButtonLabel);
    }

    public String getMyWalletDetailsLabel() {
        return getNonNullTridionString(myWalletDetailsLabel);
    }

    public String getUpgradeLabel() {
        return getNonNullTridionString(upgradeLabel);
    }

    public String getGiftLabel() {
        return getNonNullTridionString(giftLabel);
    }

    public String getReassignLabel() {
        return getNonNullTridionString(reassignLabel);
    }

    public String getBuyAgainLabel() {
        return getNonNullTridionString(buyAgainLabel);
    }

    public String getDeliveryMethodLabel() {
        return getNonNullTridionString(deliveryMethodLabel);
    }

    public String getContinueShoppingLabel() {
        return getNonNullTridionString(continueShoppingLabel);
    }

    public String getSCTicketsLabel() {
        return getNonNullTridionString(sCTicketsLabel);
    }

    public String getOneTicketCount() {
        return getNonNullTridionString(oneTicketCount);
    }

    public String getSCExtrasLabel() {
        return getNonNullTridionString(sCExtrasLabel);
    }

    public String getSCExpressPassLabel() {
        return getNonNullTridionString(sCExpressPassLabel);
    }

    public String getSelectedSeatsLabel() {
        return getNonNullTridionString(selectedSeatsLabel);
    }

    public String getSeatsLabel() {
        return getNonNullTridionString(seatsLabel);
    }

    public String getRowsLabel() {
        return getNonNullTridionString(rowsLabel);
    }

    public String getOUSResident() {
        return getNonNullTridionString(oUSResident);
    }

    public String getSCSavingText() {
        return getNonNullTridionString(sCSavingText);
    }

    public String getSCSavingsMessageText(@NonNull ParkTicketGroups group) {
        return getSCSavingText().replace(WCSKEY_SAVINGS_MESSAGE_KEY,
                getFormattedPrice(group.getTotalSavings()));
    }

    public String getSCSavingsMessageText(@NonNull DisplayPricing displayPricing) {
        return getSCSavingText().replace(WCSKEY_SAVINGS_MESSAGE_KEY,
                getFormattedPrice(displayPricing.getTotalPriceSavings(), true));
    }

    public String getSCSavingsMessageText(@NonNull ExpressPassTicketGroups expressPassTicketGroups) {
        return getSCSavingText().replace(WCSKEY_SAVINGS_MESSAGE_KEY,
                getFormattedPrice(expressPassTicketGroups.getTotalPriceSavings()));
    }

    public String getSCSavingsMessageText(@NonNull AddOnTicketGroups group) {
        return getSCSavingText().replace(WCSKEY_SAVINGS_MESSAGE_KEY,
                getFormattedPrice(group.getTotalSavings()));
    }

    public String getPerTicketLabel() {
        return getNonNullTridionString(perTicketLabel);
    }

    public String getPerPassLabel() {
        return getNonNullTridionString(perPassLabel);
    }

    public String getChildLabel() {
        return getNonNullTridionString(childLabel);
    }

    public String getAtLabel() {
        return getNonNullTridionString(atLabel);
    }

    public String getShoppingCartLabel() {
        return getNonNullTridionString(shoppingCartLabel);
    }

    public String getEnterPromoOrUPCCodeLabel() {
        return getNonNullTridionString(enterPromoOrUPCCodeLabel);
    }

    public String getApplyLabel() {
        return getNonNullTridionString(applyLabel);
    }

    public String getValidCodeText() {
        return getNonNullTridionString(validCodeText);
    }

    public String getMobileDeliveryLabel() {
        return getNonNullTridionString(mobileDeliveryLabel);
    }

    public String getPickupKioskLabel() {
        return getNonNullTridionString(pickupKioskLabel);
    }

    public String getTicketBlockoutDatesLabel() {
        return getNonNullTridionString(ticketBlockoutDatesLabel);
    }

    public String getTicketNumberLabel() {
        return getNonNullTridionString(ticketNumberLabel);
    }

    public String getConfirmationLabel() {
        return getNonNullTridionString(confirmationLabel);
    }

    public String getPurchasedLabel() {
        return getNonNullTridionString(purchasedLabel);
    }

    public String getCreditCardLabel() {
        return getNonNullTridionString(creditCardLabel);
    }

    public String getOrderedByLabel() {
        return getNonNullTridionString(orderedByLabel);
    }

    public String getFilterLabel() {
        return getNonNullTridionString(filterLabel);
    }

    public String getAddPaymentMethod() {
        return getNonNullTridionString(addPaymentMethod);
    }

    public String getByTimeLabel() {
        return getNonNullTridionString(byTimeLabel);
    }

    public String getByLocationLabel() {
        return getNonNullTridionString(byLocationLabel);
    }

    public String getByCategoryLabel() {
        return getNonNullTridionString(byCategoryLabel);
    }

    public String getByPersonLabel() {
        return getNonNullTridionString(byPersonLabel);
    }

    public String getByTimeOptions() {
        return getNonNullTridionString(byTimeOptions);
    }

    public String getByLocationOptions() {
        return getNonNullTridionString(byLocationOptions);
    }

    public String getByCategoryOptions() {
        return getNonNullTridionString(byCategoryOptions);
    }

    public String getTransactionFilterBgColor() {
        return getNonNullTridionString(transactionFilterBgColor);
    }

    public String getPurchasesTabTitle() {
        return getNonNullTridionString(purchasesTabTitle);
    }

    public String getMediaTabTitle() {
        return getNonNullTridionString(mediaTabTitle);
    }

    public String getPaymentTabTitle() {
        return getNonNullTridionString(paymentTabTitle);
    }

    public String getNoItemsInCartMessage() {
        return getNonNullTridionString(noItemsInCartMessage);
    }

    public String getShipToHomeLabel() {
        return getNonNullTridionString(shipToHomeLabel);
    }

    public String getPOBoxNotAvailableLabel() {
        return getNonNullTridionString(pOBoxNotAvailableLabel);
    }

    public String getInternationalLabel() {
        return getNonNullTridionString(internationalLabel);
    }

    public String getTaxesLabel() {
        return getNonNullTridionString(taxesLabel);
    }

    public String getTotalLabel() {
        return getNonNullTridionString(totalLabel);
    }

    public String getCreatePINText() {
        return getNonNullTridionString(createPINText);
    }

    public String getCreatePINCopy() {
        return getNonNullTridionString(createPINCopy);
    }

    public String getCreatePINLabel() {
        return getNonNullTridionString(createPINLabel);
    }

    public String getPhotoIDInstructionalText() {
        return getNonNullTridionString(photoIDInstructionalText);
    }

    public String getDaysRemainingLabel() {
        return getNonNullTridionString(daysRemainingLabel);
    }

    public String getOneDayRemainingLabel() {
        return getNonNullTridionString(oneDayRemainingLabel);
    }

    public String getViewDetailsLabel() {
        return getNonNullTridionString(viewDetailsLabel);
    }

    public String getQRInstructionalText() {
        return getNonNullTridionString(qRInstructionalText);
    }

    public String getTicketHolderNameLabel() {
        return getNonNullTridionString(ticketHolderNameLabel);
    }

    public String getWalletDisclosureText() {
        return getNonNullTridionString(walletDisclosureText);
    }

    public String getTicketsIcon() {
        return getNonNullTridionString(ticketsIcon);
    }

    public String getExtrasIcon() {
        return getNonNullTridionString(extrasIcon);
    }

    public String getDeliveryMethodIcon() {
        return getNonNullTridionString(deliveryMethodIcon);
    }

    public String getExpressPassIcon() {
        return getNonNullTridionString(expressPassIcon);
    }

    public String getMonthJanuaryLabel() {
        return getNonNullTridionString(monthJanuaryLabel);
    }

    public String getMonthFebruaryLabel() {
        return getNonNullTridionString(monthFebruaryLabel);
    }

    public String getMonthMarchLabel() {
        return getNonNullTridionString(monthMarchLabel);
    }

    public String getMonthAprilLabel() {
        return getNonNullTridionString(monthAprilLabel);
    }

    public String getMonthMayLabel() {
        return getNonNullTridionString(monthMayLabel);
    }

    public String getMonthJuneLabel() {
        return getNonNullTridionString(monthJuneLabel);
    }

    public String getMonthJulyLabel() {
        return getNonNullTridionString(monthJulyLabel);
    }

    public String getMonthAugustLabel() {
        return getNonNullTridionString(monthAugustLabel);
    }

    public String getMonthSeptemberLabel() {
        return getNonNullTridionString(monthSeptemberLabel);
    }

    public String getMonthOctoberLabel() {
        return getNonNullTridionString(monthOctoberLabel);
    }

    public String getMonthNovemberLabel() {
        return getNonNullTridionString(monthNovemberLabel);
    }

    public String getMonthDecemberLabel() {
        return getNonNullTridionString(monthDecemberLabel);
    }

    public String getDaySunLabel() {
        return getNonNullTridionString(daySunLabel);
    }

    public String getDayMonLabel() {
        return getNonNullTridionString(dayMonLabel);
    }

    public String getDayTueLabel() {
        return getNonNullTridionString(dayTueLabel);
    }

    public String getDayWedLabel() {
        return getNonNullTridionString(dayWedLabel);
    }

    public String getDayThrLabel() {
        return getNonNullTridionString(dayThrLabel);
    }

    public String getDayFriLabel() {
        return getNonNullTridionString(dayFriLabel);
    }

    public String getDaySatLabel() {
        return getNonNullTridionString(daySatLabel);
    }

    public String getSCSubHeader() {
        return getNonNullTridionString(sCSubHeader);
    }

    public String getSCIntroText() {
        return getNonNullTridionString(sCIntroText);
    }

    public String getSCSeasonsLabel() {
        return getNonNullTridionString(sCSeasonsLabel);
    }

    public String getSCSeasonsTooltip() {
        return getNonNullTridionString(sCSeasonsTooltip);
    }

    public String getSCValueLabel() {
        return getNonNullTridionString(sCValueLabel);
    }

    public String getSCValueDescriptionLabel() {
        return getNonNullTridionString(sCValueDescriptionLabel);
    }

    public int getSCValueColor() {
        String colorString = getNonNullTridionString(sCValueColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_VALUE_BACKGROUND_COLOR);
        return color;
    }

    public String getSCRegularLabel() {
        return getNonNullTridionString(sCRegularLabel);
    }

    public String getSCRegularDescriptionLabel() {
        return getNonNullTridionString(sCRegularDescriptionLabel);
    }

    public int getSCRegularColor() {
        String colorString = getNonNullTridionString(sCRegularColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_REGULAR_BACKGROUND_COLOR);
        return color;
    }

    public String getSCPeakLabel() {
        return getNonNullTridionString(sCPeakLabel);
    }

    public String getSCPeakDescriptionLabel() {
        return getNonNullTridionString(sCPeakDescriptionLabel);
    }

    public int getSCPeakColor() {
        String colorString = getNonNullTridionString(sCPeakColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_PEAK_BACKGROUND_COLOR);
        return color;
    }

    public String getSCMixedLabel() {
        return getNonNullTridionString(sCMixedLabel);
    }

    public String getSCMixedDescriptionLabel() {
        return getNonNullTridionString(sCMixedDescriptionLabel);
    }

    public int getSCMixedColor() {
        String colorString = getNonNullTridionString(sCMixedColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_MIXED_BACKGROUND_COLOR);
        return color;
    }

    public String getSCValueValidityDescription() {
        return getNonNullTridionString(sCValueValidityDescription);
    }

    public String getSCPeakValidityDescription() {
        return getNonNullTridionString(sCPeakValidityDescription);
    }

    public String getSCMixedValidityDescription() {
        return getNonNullTridionString(sCMixedValidityDescription);
    }

    public String getSCRegularValidityDescription() {
        return getNonNullTridionString(sCRegularValidityDescription);
    }

    public String getViewFullCalendarLabel() {
        return getNonNullTridionString(viewFullCalendarLabel);
    }

    public String getCloseFullCalendarLabel() {
        return getNonNullTridionString(closeFullCalendarLabel);
    }

    public String getSCImage() {
        return getNonNullTridionString(sCImage);
    }

    public String getNameOnCardLabel() {
        return getNonNullTridionString(nameOnCardLabel);
    }

    public String getCreditCardNumberLabel() {
        return getNonNullTridionString(creditCardNumberLabel);
    }

    public String getExpirationDateLabel() {
        return getNonNullTridionString(expirationDateLabel);
    }

    public String getSecurityCodeLabel() {
        return getNonNullTridionString(securityCodeLabel);
    }

    public String getSecurityCodeToolTip() {
        return getNonNullTridionString(securityCodeToolTip);
    }

    public String getBillingAddressLabel() {
        return getNonNullTridionString(billingAddressLabel);
    }

    public String getSelectAddressLabel() {
        return getNonNullTridionString(selectAddressLabel);
    }

    public String getAddAddressLabel() {
        return getNonNullTridionString(addAddressLabel);
    }

    public String getUpdateAddressLabel() {
        return getNonNullTridionString(updateAddressLabel);
    }

    public String getPrimaryLabel() {
        return getNonNullTridionString(primaryLabel);
    }

    public String getCountryLabel() {
        return getNonNullTridionString(countryLabel);
    }

    public String getAddress1Label() {
        return getNonNullTridionString(address1Label);
    }

    public String getAddress2Label() {
        return getNonNullTridionString(address2Label);
    }

    public String getOptionalLabel() {
        return getNonNullTridionString(optionalLabel);
    }

    public String getZipCodeLabel() {
        return getNonNullTridionString(zipCodeLabel);
    }

    public String getCityLabel() {
        return getNonNullTridionString(cityLabel);
    }

    public String getStateProvinceLabel() {
        return getNonNullTridionString(stateProvinceLabel);
    }

    public String getPhoneNumberLabel() {
        return getNonNullTridionString(phoneNumberLabel);
    }

    public String getShippingAddressLabel() {
        return getNonNullTridionString(shippingAddressLabel);
    }

    public String getSameAsBillingAddressLabel() {
        return getNonNullTridionString(sameAsBillingAddressLabel);
    }

    public String getBILoggedOutShippingStateText() {
        return getNonNullTridionString(bILoggedOutShippingStateText);
    }

    public String getFirstNameLabel() {
        return getNonNullTridionString(firstNameLabel);
    }

    public String getLastNameLabel() {
        return getNonNullTridionString(lastNameLabel);
    }

    public String getDOBLabel() {
        return getNonNullTridionString(dOBLabel);
    }

    public String getDOBFormat() {
        return getNonNullTridionString(dOBFormat);
    }

    public String getEmailAddressLabel() {
        return getNonNullTridionString(emailAddressLabel);
    }

    public String getPasswordLabel() {
        return getNonNullTridionString(passwordLabel);
    }

    public String getForgotPasswordLabel() {
        return getNonNullTridionString(forgotPasswordLabel);
    }

    public String getForgotPasswordPageTitle() {
        return getNonNullTridionString(forgotPasswordPageTitle);
    }

    public String getForgotPasswordHeader() {
        return getNonNullTridionString(forgotPasswordHeader);
    }

    public String getResetPasswordPageTitle() {
        return getNonNullTridionString(resetPasswordPageTitle);
    }

    public String getResetPasswordHeader() {
        return getNonNullTridionString(resetPasswordHeader);
    }

    public String getNewPasswordLabel() {
        return getNonNullTridionString(newPasswordLabel);
    }

    public String getSubmitButtonText() {
        return getNonNullTridionString(submitButtonText);
    }

    public String getDOBToolTip() {
        return getNonNullTridionString(dOBToolTip);
    }

    public String getPhoneNumberToolTip() {
        return getNonNullTridionString(phoneNumberToolTip);
    }

    public String getPasswordToolTip() {
        return getNonNullTridionString(passwordToolTip);
    }

    public String getRememberMeLabel() {
        return getNonNullTridionString(rememberMeLabel);
    }

    public String getNotRobotLabel() {
        return getNonNullTridionString(notRobotLabel);
    }

    public String getRequiredFieldsLabel() {
        return getNonNullTridionString(requiredFieldsLabel);
    }

    public String getEditLabel() {
        return getNonNullTridionString(editLabel);
    }

    public String getGuestLoginContinueLabel() {
        return getNonNullTridionString(guestLoginContinueLabel);
    }

    public String getGuestLoginIntroText() {
        return getNonNullTridionString(guestLoginIntroText);
    }

    public String getLoginFormLabel() {
        return getNonNullTridionString(loginFormLabel);
    }

    public String getLoginLabel() {
        return getNonNullTridionString(loginLabel);
    }

    public String getLoginPageTitle() {
        return getNonNullTridionString(loginPageTitle);
    }

    public String getSignInLabel() {
        return getNonNullTridionString(signInLabel);
    }

    public String getOrderConfirmationSignUpLabel() {
        return getNonNullTridionString(orderConfirmationSignUpLabel);
    }

    public String getCreateAccountLabel() {
        return getNonNullTridionString(createAccountLabel);
    }

    public String getCreateAccountPageTitle() {
        return getNonNullTridionString(createAccountPageTitle);
    }

    public String getCreateAccountNewLabel() {
        return getNonNullTridionString(createAccountNewLabel);
    }

    public String getCreateAccountLinkLabel() {
        return getNonNullTridionString(createAccountLinkLabel);
    }

    public String getPaymentSummaryHeading() {
        return getNonNullTridionString(paymentSummaryHeading);
    }

    public String getTodaysTotalLabel() {
        return getNonNullTridionString(todaysTotalLabel);
    }

    public String getAmountFinancedLabel() {
        return getNonNullTridionString(amountFinancedLabel);
    }

    public String getFinancedPaymentLabel() {
        return getNonNullTridionString(financedPaymentLabel);
    }

    public String getPaymentOfLabel() {
        return getNonNullTridionString(paymentOfLabel);
    }

    public String getTaxLabel() {
        return getNonNullTridionString(taxLabel);
    }

    public String getCreditCardChargedLabel() {
        return getNonNullTridionString(creditCardChargedLabel);
    }

    public String getCheckOutLabel() {
        return getNonNullTridionString(checkOutLabel);
    }

    public String getCardInformationLabel() {
        return getNonNullTridionString(cardInformationLabel);
    }

    public String getCardsAcceptedLabel() {
        return getNonNullTridionString(cardsAcceptedLabel);
    }

    public String getCompletePurchaseLabel() {
        return getNonNullTridionString(completePurchaseLabel);
    }

    public String getRemoveLabel() {
        return getNonNullTridionString(removeLabel);
    }

    public String getDoneLabel() {
        return getNonNullTridionString(doneLabel);
    }

    public String getSuffixLabel() {
        return getNonNullTridionString(suffixLabel);
    }

    public String getSelectLabel() {
        return getNonNullTridionString(selectLabel);
    }

    public String getSelectTicketQuantityLabel() {
        return getNonNullTridionString(selectTicketQuantityLabel);
    }

    public String getSelectParksLabel() {
        return getNonNullTridionString(selectParksLabel);
    }

    public String getSelectDateLabel() {
        return getNonNullTridionString(selectDateLabel);
    }

    public String getSelectTimeLabel() {
        return getNonNullTridionString(selectTimeLabel);
    }

    public String getSelectTierLabel() {
        return getNonNullTridionString(selectTierLabel);
    }

    public String getShippingLabel() {
        return getNonNullTridionString(shippingLabel);
    }

    public String getSCShippingLabel() {
        return getNonNullTridionString(sCShippingLabel);
    }

    public String getBillingLabel() {
        return getNonNullTridionString(billingLabel);
    }

    public String getDownPaymentLabel() {
        return getNonNullTridionString(downPaymentLabel);
    }

    public String getSignUpTNC() {
        return getNonNullTridionString(signUpTNC);
    }

    public String getGuestLoginOptIn() {
        return getNonNullTridionString(guestLoginOptIn);
    }

    public String getTnc() {
        return getNonNullTridionString(tnc);
    }

    public String getPmtCard1() {
        return getNonNullTridionString(pmtCard1);
    }

    public String getPmtCard2() {
        return getNonNullTridionString(pmtCard2);
    }

    public String getPmtCard3() {
        return getNonNullTridionString(pmtCard3);
    }

    public String getPmtCard4() {
        return getNonNullTridionString(pmtCard4);
    }

    public String getCertifiedIcon() {
        return getNonNullTridionString(certifiedIcon);
    }

    public String getManageAccountLabel() {
        return getNonNullTridionString(manageAccountLabel);
    }

    public String getOrderConfirmationSignUpIntroText() {
        return getNonNullTridionString(orderConfirmationSignUpIntroText);
    }

    public String getMobileAppPromoTeaserText() {
        return getNonNullTridionString(mobileAppPromoTeaserText);
    }

    public String getManageAccountPromoTeaserText() {
        return getNonNullTridionString(manageAccountPromoTeaserText);
    }

    public String getShareHeader() {
        return getNonNullTridionString(shareHeader);
    }

    public String getShareCopy() {
        return getNonNullTridionString(shareCopy);
    }

    public String getMobileAppPromoAppleLogo() {
        return getNonNullTridionString(mobileAppPromoAppleLogo);
    }

    public String getMobileAppPromoGooglePlayLogo() {
        return getNonNullTridionString(mobileAppPromoGooglePlayLogo);
    }

    public String getIconShareFacebook() {
        return getNonNullTridionString(iconShareFacebook);
    }

    public String getEr1() {
        return getNonNullTridionString(er1);
    }

    public String getEr2() {
        return getNonNullTridionString(er2);
    }

    public String getEr3() {
        return getNonNullTridionString(er3);
    }

    public String getEr4() {
        return getNonNullTridionString(er4);
    }

    public String getEr5() {
        return getNonNullTridionString(er5);
    }

    public String getEr6() {
        return getNonNullTridionString(er6);
    }

    public String getEr7() {
        return getNonNullTridionString(er7);
    }

    public String getEr8() {
        return getNonNullTridionString(er8);
    }

    public String getEr9() {
        return getNonNullTridionString(er9);
    }

    public String getEr10() {
        return getNonNullTridionString(er10);
    }

    public String getEr11() {
        return getNonNullTridionString(er11);
    }

    public String getEr12() {
        return getNonNullTridionString(er12);
    }

    public String getEr13() {
        return getNonNullTridionString(er13);
    }

    public String getEr14() {
        return getNonNullTridionString(er14);
    }

    public String getEr15() {
        return getNonNullTridionString(er15);
    }

    public String getEr16() {
        return getNonNullTridionString(er16);
    }

    public String getEr17() {
        return getNonNullTridionString(er17);
    }

    public String getEr18() {
        return getNonNullTridionString(er18);
    }

    public String getEr19() {
        return getNonNullTridionString(er19);
    }

    public String getEr20() {
        return getNonNullTridionString(er20);
    }

    public String getEr21() {
        return getNonNullTridionString(er21);
    }

    public String getEr22() {
        return getNonNullTridionString(er22);
    }

    public String getEr23() {
        return getNonNullTridionString(er23);
    }

    public String getEr24() {
        return getNonNullTridionString(er24);
    }

    public String getEr25() {
        return getNonNullTridionString(er25);
    }

    public String getEr26() {
        return getNonNullTridionString(er26);
    }

    public String getEr27() {
        return getNonNullTridionString(er27);
    }

    public String getEr28() {
        return getNonNullTridionString(er28);
    }

    public String getEr29() {
        return getNonNullTridionString(er29);
    }

    public String getEr30() {
        return getNonNullTridionString(er30);
    }

    public String getEr31() {
        return getNonNullTridionString(er31);
    }

    public String getEr32() {
        return getNonNullTridionString(er32);
    }

    public String getEr33() {
        return getNonNullTridionString(er33);
    }

    public String getEr34() {
        return getNonNullTridionString(er34);
    }

    public String getEr35() {
        return getNonNullTridionString(er35);
    }

    public String getEr36() {
        return getNonNullTridionString(er36);
    }

    public String getEr37() {
        return getNonNullTridionString(er37);
    }

    public String getEr38() {
        return getNonNullTridionString(er38);
    }

    public String getEr39() {
        return getNonNullTridionString(er39);
    }

    public String getEr40() {
        return getNonNullTridionString(er40);
    }

    public String getEr41() {
        return getNonNullTridionString(er41);
    }

    public String getEr42() {
        return getNonNullTridionString(er42);
    }

    public String getEr43() {
        return getNonNullTridionString(er43);
    }

    public String getEr44() {
        return getNonNullTridionString(er44);
    }

    public String getEr45() {
        return getNonNullTridionString(er45);
    }

    public String getEr46() {
        return getNonNullTridionString(er46);
    }

    public String getEr47() {
        return getNonNullTridionString(er47);
    }

    public String getEr48() {
        return getNonNullTridionString(er48);
    }

    public String getEr49() {
        return getNonNullTridionString(er49);
    }

    public String getEr50() {
        return getNonNullTridionString(er50);
    }

    public String getEr51() {
        return getNonNullTridionString(er51);
    }

    public String getEr52() {
        return getNonNullTridionString(er52);
    }

    public String getEr53() {
        return getNonNullTridionString(er53);
    }

    public String getEr54() {
        return getNonNullTridionString(er54);
    }

    public String getEr55() {
        return getNonNullTridionString(er55);
    }

    public String getEr56() {
        return getNonNullTridionString(er56);
    }

    public String getEr57() {
        return getNonNullTridionString(er57);
    }

    public String getEr58() {
        return getNonNullTridionString(er58);
    }

    public String getEr59() {
        return getNonNullTridionString(er59);
    }

    public String getEr60() {
        return getNonNullTridionString(er60);
    }

    public String getEr61() {
        return getNonNullTridionString(er61);
    }

    public String getEr62() {
        return getNonNullTridionString(er62);
    }

    public String getEr63() {
        return getNonNullTridionString(er63);
    }

    public String getEr64() {
        return getNonNullTridionString(er64);
    }

    public String getEr65() {
        return getNonNullTridionString(er65);
    }

    public String getEr66() {
        return getNonNullTridionString(er66);
    }

    public String getEr67() {
        return getNonNullTridionString(er67);
    }

    public String getEr68() {
        return getNonNullTridionString(er68);
    }

    public String getEr69() {
        return getNonNullTridionString(er69);
    }

    public String getEr70() {
        return getNonNullTridionString(er70);
    }

    public String getEr71() {
        return getNonNullTridionString(er71);
    }

    public String getEr72() {
        return getNonNullTridionString(er72);
    }

    public String getEr73() {
        return getNonNullTridionString(er73);
    }

    public String getEr74() {
        return getNonNullTridionString(er74);
    }
    public String getEr75() {
        return getNonNullTridionString(er75);
    }
    public String getEr76() {
        return getNonNullTridionString(er76);
    }
    public String getEr77() {
        return getNonNullTridionString(er77);
    }
    public String getEr78() {
        return getNonNullTridionString(er78);
    }
    public String getEr79() {
        return getNonNullTridionString(er79);
    }
    public String getEr80() {
        return getNonNullTridionString(er80);
    }
    public String getEr81() {
        return getNonNullTridionString(er81);
    }
    public String getEr82() {
        return getNonNullTridionString(er82);
    }
    public String getEr83() {
        return getNonNullTridionString(er83);
    }
    public String getEr84() {
        return getNonNullTridionString(er84);
    }
    public String getEr85() {
        return getNonNullTridionString(er85);
    }
    public String getEr86() {
        return getNonNullTridionString(er86);
    }
    public String getEr87() {
        return getNonNullTridionString(er87);
    }
    public String getEr88() {
        return getNonNullTridionString(er88);
    }
    public String getEr89() {
        return getNonNullTridionString(er89);
    }
    public String getEr90() {
        return getNonNullTridionString(er90);
    }
    public String getEr91() {
        return getNonNullTridionString(er91);
    }
    public String getEr92() {
        return getNonNullTridionString(er92);
    }

    public String getEr93() {
        return getNonNullTridionString(er93);
    }

    public String getEr94() {
        return getNonNullTridionString(er94);
    }

    public String getEr95() {
        return getNonNullTridionString(er95);
    }

    public String getEr96() {
        return getNonNullTridionString(er96);
    }

    public String getEr97() {
        return getNonNullTridionString(er97);
    }

    public String getEr98() {
        return getNonNullTridionString(er98);
    }

    public String getEr99() {
        return getNonNullTridionString(er99);
    }

    public String getEr100() {
        return getNonNullTridionString(er100);
    }

    public String getEr101() {
        return getNonNullTridionString(er101);
    }

    public String getEr102() {
        return getNonNullTridionString(er102);
    }

    public String getEr103() {
        return getNonNullTridionString(er103);
    }

    public String getSu1() {
        return getNonNullTridionString(su1);
    }

    public String getSu2() {
        return getNonNullTridionString(su2);
    }

    public String getSu3() {
        return getNonNullTridionString(su3);
    }

    public String getSu4() {
        return getNonNullTridionString(su4);
    }

    public String getSu5() {
        return getNonNullTridionString(su5);
    }

    public String getSu6() {
        return getNonNullTridionString(su6);
    }

    public String getSu7() {
        return getNonNullTridionString(su7);
    }

    public String getSu8() {
        return getNonNullTridionString(su8);
    }

    public String getSu9() {
        return getNonNullTridionString(su9);
    }

    public String getSu10() {
        return getNonNullTridionString(su10);
    }

    public String getSu11() {
        return getNonNullTridionString(su11);
    }

    public String getSu12() {
        return getNonNullTridionString(su12);
    }

    public String getSu13() {
        return getNonNullTridionString(su13);
    }

    public String getSu14() {
        return getNonNullTridionString(su14);
    }

    public String getSu15() {
        return getNonNullTridionString(su15);
    }

    public String getSu16() {
        return getNonNullTridionString(su16);
    }

    public String getSu17() {
        return getNonNullTridionString(su17);
    }

    public String getSu18() {
        return getNonNullTridionString(su18);
    }

    public String getSu20(String lastFour) {
        return getNonNullTridionString(su20).replace(WCSKEY_CREDIT_CARD_LAST_FOUR, lastFour);
    }

    public String getSu21() {
        return getNonNullTridionString(su21);
    }

    public String getOkToSendLabel() {
        return getNonNullTridionString(okToSendLabel);
    }

    public String getHhn() {
        return getNonNullTridionString(hhn);
    }

    public String getUsf() {
        return getNonNullTridionString(usf);
    }

    public String getIoa() {
        return getNonNullTridionString(ioa);
    }

    public String getVb() {
        return getNonNullTridionString(vb);
    }

    public String getCw() {
        return getNonNullTridionString(cw);
    }

    public String getHotels() {
        return getNonNullTridionString(hotels);
    }

    public String getMg() {
        return getNonNullTridionString(mg);
    }

    public String getRtu() {
        return getNonNullTridionString(rtu);
    }

    public String getBmg() {
        return getNonNullTridionString(bmg);
    }

    public String getWwohp() {
        return getNonNullTridionString(wwohp);
    }

    public String getKingkong() {
        return getNonNullTridionString(kingkong);
    }

    public String getErrAssignEntitlements() {
        return getNonNullTridionString(errAssignEntitlements);
    }

    public String getUnassignedAdultLabel() {
        return getNonNullTridionString(unassignedAdultLabel);
    }

    public String getUnassignedChildLabel() {
        return getNonNullTridionString(unassignedChildtLabel);
    }

    public String getPrimaryAccountLabel() {
        return getNonNullTridionString(primaryAccountLabel);
    }

    public String getSelectGuestNameLabel() {
        return getNonNullTridionString(selectGuestNameLabel);
    }

    public String getAddNewNameLabel() {
        return getNonNullTridionString(addNewNameLabel);
    }

    public String getThisIsMeLabel() {
        return getNonNullTridionString(thisIsMeLabel);
    }

    public String getAssignItemsLabel() {
        return getNonNullTridionString(assignItemsLabel);
    }

    public String getUnassignedPurchasesLabel() {
        return getNonNullTridionString(unassignedPurchasesLabel);
    }

    public String getAddGuestLabel() {
        return getNonNullTridionString(addGuestLabel);
    }

    public String getPurchasesLeftToAssignLabel() {
        return getNonNullTridionString(purchasesLeftToAssignLabel);
    }

    public String getValidLabel() {
        return getNonNullTridionString(validLabel);
    }

    public String getYesLabel() {
        return getNonNullTridionString(yesLabel);
    }

    public String getNoLabel() {
        return getNonNullTridionString(noLabel);
    }

    public String getViewCartLabel() {
        return getNonNullTridionString(viewCartLabel);
    }

    public String getSubTotalLabel() {
        return getNonNullTridionString(subTotalLabel);
    }

    public String getMoreLabel() {
        return getNonNullTridionString(moreLabel);
    }

    public String getAddToCartLabel() {
        return getNonNullTridionString(addToCartLabel);
    }

    public String getAdultsLabel() {
        return getNonNullTridionString(adultsLabel);
    }

    public String getAdultLabel() {
        return getNonNullTridionString(adultLabel);
    }

    public String getPerAdultLabel() {
        return getNonNullTridionString(perAdultLabel);
    }

    public String getPerChildLabel() {
        return getNonNullTridionString(perChildLabel);
    }

    public String getChildrenLabel() {
        return getNonNullTridionString(childrenLabel);
    }

    public String getBackLabel() {
        return getNonNullTridionString(backLabel);
    }

    public String getContinueLabel() {
        return getNonNullTridionString(continueLabel);
    }

    public String getAllAgesLabel() {
        return getNonNullTridionString(allAgesLabel);
    }

    public String getSeeDetailsLabel() {
        return getNonNullTridionString(seeDetailsLabel);
    }

    public String getBuyTicketsLabel() {
        return getNonNullTridionString(buyTicketsLabel);
    }

    public String getShopNowLabel() {
        return getNonNullTridionString(shopNowLabel);
    }

    public String getFreeLabel() {
        return getNonNullTridionString(freeLabel);
    }

    public String getMonthLabel() {
        return getNonNullTridionString(monthLabel);
    }

    public String getYearLabel() {
        return getNonNullTridionString(yearLabel);
    }

    public String getNextLabel() {
        return getNonNullTridionString(nextLabel);
    }

    public String getDayLabel() {
        return getNonNullTridionString(dayLabel);
    }

    public String getDaysLabel() {
        return getNonNullTridionString(daysLabel);
    }

    public String getSCAnnualPassLabel() {
        return getNonNullTridionString(sCAnnualPassLabel);
    }

    public String getUpdateLabel() {
        return getNonNullTridionString(updateLabel);
    }

    public String getCancelLabel() {
        return getNonNullTridionString(cancelLabel);
    }

    public String getSoldOutLabel() {
        return getNonNullTridionString(soldOutLabel);
    }

    public String getPromoBPImage() {
        return getNonNullTridionString(promoBPImage);
    }

    public String getPromoBPTitle() {
        return getNonNullTridionString(promoBPTitle);
    }

    public String getPromoBPTeaserText() {
        return getNonNullTridionString(promoBPTeaserText);
    }

    public String getPromoBPDetails() {
        return getNonNullTridionString(promoBPDetails);
    }

    public String getPageHeaderPHTitle() {
        return getNonNullTridionString(pageHeaderPHTitle);
    }

    public String getPageHeaderPHIntroText() {
        return getNonNullTridionString(pageHeaderPHIntroText);
    }

    public String getPageHeaderPHSEOTitle() {
        return getNonNullTridionString(pageHeaderPHSEOTitle);
    }

    public String getPageHeaderPHSEODescription() {
        return getNonNullTridionString(pageHeaderPHSEODescription);
    }

    public String getPageHeaderPHSEOKeywords() {
        return getNonNullTridionString(pageHeaderPHSEOKeywords);
    }

    public String getPageHeaderPHRobots() {
        return getNonNullTridionString(pageHeaderPHRobots);
    }

    public String getPageHeaderWPTitle() {
        return getNonNullTridionString(pageHeaderWPTitle);
    }

    public String getPageHeaderWPSEOTitle() {
        return getNonNullTridionString(pageHeaderWPSEOTitle);
    }

    public String getPageHeaderWPSEODescription() {
        return getNonNullTridionString(pageHeaderWPSEODescription);
    }

    public String getPageHeaderWPSEOKeywords() {
        return getNonNullTridionString(pageHeaderWPSEOKeywords);
    }

    public String getPageHeaderWPRobots() {
        return getNonNullTridionString(pageHeaderWPRobots);
    }

    public String getPageHeaderOCTitle() {
        return getNonNullTridionString(pageHeaderOCTitle);
    }

    public String getPageHeaderOCIntroText() {
        return getNonNullTridionString(pageHeaderOCIntroText);
    }

    public String getPageHeaderOCUpsellText() {
        return getNonNullTridionString(pageHeaderOCUpsellText);
    }

    public String getPageHeaderOCHeroImage() {
        return getNonNullTridionString(pageHeaderOCHeroImage);
    }

    public String getPageHeaderOCBgColor() {
        return getNonNullTridionString(pageHeaderOCBgColor);
    }

    public String getPageHeaderOCSEOTitle() {
        return getNonNullTridionString(pageHeaderOCSEOTitle);
    }

    public String getPageHeaderOCSEODescription() {
        return getNonNullTridionString(pageHeaderOCSEODescription);
    }

    public String getPageHeaderOCSEOKeywords() {
        return getNonNullTridionString(pageHeaderOCSEOKeywords);
    }

    public String getPageHeaderOCRobots() {
        return getNonNullTridionString(pageHeaderOCRobots);
    }

    public String getPageHeaderPTTitle() {
        return getNonNullTridionString(pageHeaderPTTitle);
    }

    public String getPageHeaderPTIntroText(@NonNull CommerceGroup group) {
        return getPageHeaderPTIntroText().replace(WCSKEY_SAVINGS_KEY,
                getFormattedPrice(group.getOnlineSavingsPrice()));
    }

    /**
     * This method is intentially private. Use {@link #getPageHeaderPTIntroText(CommerceGroup)}.
     * @return
     */
    private String getPageHeaderPTIntroText() {
        return getNonNullTridionString(pageHeaderPTIntroText);
    }

    public String getPageHeaderPTSEOTitle() {
        return getNonNullTridionString(pageHeaderPTSEOTitle);
    }

    public String getPageHeaderPTSEODescription() {
        return getNonNullTridionString(pageHeaderPTSEODescription);
    }

    public String getPageHeaderPTSEOKeywords() {
        return getNonNullTridionString(pageHeaderPTSEOKeywords);
    }

    public String getPageHeaderPTRobots() {
        return getNonNullTridionString(pageHeaderPTRobots);
    }

    public String getPageHeaderMPTitle() {
        return getNonNullTridionString(pageHeaderMPTitle);
    }

    public String getPageHeaderMPSEOTitle() {
        return getNonNullTridionString(pageHeaderMPSEOTitle);
    }

    public String getPageHeaderMPSEODescription() {
        return getNonNullTridionString(pageHeaderMPSEODescription);
    }

    public String getPageHeaderMPSEOKeywords() {
        return getNonNullTridionString(pageHeaderMPSEOKeywords);
    }

    public String getPageHeaderMPRobots() {
        return getNonNullTridionString(pageHeaderMPRobots);
    }

    public String getPageHeaderEPTitle() {
        return getNonNullTridionString(pageHeaderEPTitle);
    }

    public String getPageHeaderEPAppTitle() {
        return getNonNullTridionString(pageHeaderEPAppTitle);
    }

    public String getPageHeaderEPIntroText() {
        return getNonNullTridionString(pageHeaderEPIntroText);
    }

    public String getPageHeaderEPUpsellText() {
        return getNonNullTridionString(pageHeaderEPUpsellText);
    }

    public String getPageHeaderEPSEOTitle() {
        return getNonNullTridionString(pageHeaderEPSEOTitle);
    }

    public String getPageHeaderEPSEODescription() {
        return getNonNullTridionString(pageHeaderEPSEODescription);
    }

    public String getPageHeaderEPSEOKeywords() {
        return getNonNullTridionString(pageHeaderEPSEOKeywords);
    }

    public String getPageHeaderEPRobots() {
        return getNonNullTridionString(pageHeaderEPRobots);
    }

    public String getPageHeaderCompareAPIntroText() {
        return getNonNullTridionString(pageHeaderCompareAPIntroText);
    }

    public String getPageHeaderCompareAPLinkLabel() {
        return getNonNullTridionString(pageHeaderCompareAPLinkLabel);
    }

    public String getPageHeaderCompareAPSEOTitle() {
        return getNonNullTridionString(pageHeaderCompareAPSEOTitle);
    }

    public String getPageHeaderCompareAPSEODescription() {
        return getNonNullTridionString(pageHeaderCompareAPSEODescription);
    }

    public String getPageHeaderCompareAPSEOKeywords() {
        return getNonNullTridionString(pageHeaderCompareAPSEOKeywords);
    }

    public String getPageHeaderCompareAPRobots() {
        return getNonNullTridionString(pageHeaderCompareAPRobots);
    }

    public String getPageHeaderCLTitle() {
        return getNonNullTridionString(pageHeaderCLTitle);
    }

    public String getPageHeaderCLUpsellText() {
        return getNonNullTridionString(pageHeaderCLUpsellText);
    }

    public String getPageHeaderCLHeroImage() {
        return getNonNullTridionString(pageHeaderCLHeroImage);
    }

    public String getPageHeaderCLSEOTitle() {
        return getNonNullTridionString(pageHeaderCLSEOTitle);
    }

    public String getPageHeaderCLSEODescription() {
        return getNonNullTridionString(pageHeaderCLSEODescription);
    }

    public String getPageHeaderCLSEOKeywords() {
        return getNonNullTridionString(pageHeaderCLSEOKeywords);
    }

    public String getPageHeaderCLRobots() {
        return getNonNullTridionString(pageHeaderCLRobots);
    }

    public String getPageHeaderAETitle() {
        return getNonNullTridionString(pageHeaderAETitle);
    }

    public String getPageHeaderAEAppTitle() {
        return getNonNullTridionString(pageHeaderAEAppTitle);
    }

    public String getPageHeaderAEIntroText() {
        return getNonNullTridionString(pageHeaderAEIntroText);
    }

    public String getPageHeaderAESEOTitle() {
        return getNonNullTridionString(pageHeaderAESEOTitle);
    }

    public String getPageHeaderAESEODescription() {
        return getNonNullTridionString(pageHeaderAESEODescription);
    }

    public String getPageHeaderAESEOKeywords() {
        return getNonNullTridionString(pageHeaderAESEOKeywords);
    }

    public String getPageHeaderAERobots() {
        return getNonNullTridionString(pageHeaderAERobots);
    }

    public String getPageHeaderAOTitle() {
        return getNonNullTridionString(pageHeaderAOTitle);
    }

    public String getPageHeaderAOIntroText() {
        return getNonNullTridionString(pageHeaderAOIntroText);
    }

    public String getPageHeaderAOSEOTitle() {
        return getNonNullTridionString(pageHeaderAOSEOTitle);
    }

    public String getPageHeaderAOSEODescription() {
        return getNonNullTridionString(pageHeaderAOSEODescription);
    }

    public String getPageHeaderAOSEOKeywords() {
        return getNonNullTridionString(pageHeaderAOSEOKeywords);
    }

    public String getPageHeaderAORobots() {
        return getNonNullTridionString(pageHeaderAORobots);
    }

    public String getPageHeaderAPTitle() {
        return getNonNullTridionString(pageHeaderAPTitle);
    }

    public String getPageHeaderAPIntroText() {
        return getNonNullTridionString(pageHeaderAPIntroText);
    }

    public String getPageHeaderAPUpsellText() {
        return getNonNullTridionString(pageHeaderAPUpsellText);
    }

    public String getPageHeaderAPLinkLabel() {
        return getNonNullTridionString(pageHeaderAPLinkLabel);
    }

    public String getPageHeaderAPSEOTitle() {
        return getNonNullTridionString(pageHeaderAPSEOTitle);
    }

    public String getPageHeaderAPSEODescription() {
        return getNonNullTridionString(pageHeaderAPSEODescription);
    }

    public String getPageHeaderAPSEOKeywords() {
        return getNonNullTridionString(pageHeaderAPSEOKeywords);
    }

    public String getPageHeaderAPRobots() {
        return getNonNullTridionString(pageHeaderAPRobots);
    }

    public String getPageHeaderAOCTitle() {
        return getNonNullTridionString(pageHeaderAOCTitle);
    }

    public String getPageHeaderAOCSEOTitle() {
        return getNonNullTridionString(pageHeaderAOCSEOTitle);
    }

    public String getPageHeaderAOCSEODescription() {
        return getNonNullTridionString(pageHeaderAOCSEODescription);
    }

    public String getPageHeaderAOCSEOKeywords() {
        return getNonNullTridionString(pageHeaderAOCSEOKeywords);
    }

    public String getPageHeaderAOCRobots() {
        return getNonNullTridionString(pageHeaderAOCRobots);
    }

    public String getAPComparisionTable() {
        return getNonNullTridionString(aPComparisionTable);
    }

    public String getTncAndLabel() {
        return getNonNullTridionString(tncAndLabel);
    }

    public String getTncPrivacyLink() {
        return getNonNullTridionString(tncPrivacyLink);
    }

    public String getTncPrivacyLinkLabel() {
        return getNonNullTridionString(tncPrivacyLinkLabel);
    }

    public String getPrivacyInformationText() {
        return getNonNullTridionString(privacyInformationText);
    }

    public String getPrivacyInformationLinkUrl() {
        return getNonNullTridionString(privacyInformationLinkUrl);
    }

    public String getPrivacyInformationLinkText() {
        return getNonNullTridionString(privacyInformationLinkText);
    }

    public String getTncTermsLink() {
        return getNonNullTridionString(tncTermsLink);
    }

    public String getTncTermsLinkLabel() {
        return getNonNullTridionString(tncTermsLinkLabel);
    }

    public String getTextBeforeTnc() {
        return getNonNullTridionString(textBeforeTnc);
    }

    public String getUepHeaderDateFormat() {
        return getNonNullTridionString(uepHeaderDateFormat);
    }

    public String getPeakPeakDescription() {
        return getNonNullTridionString(peakPeakDescription);
    }

    public String getPeakRegularDescription() {
        return getNonNullTridionString(peakRegularDescription);
    }

    public String getPeakValueDescription() {
        return getNonNullTridionString(peakValueDescription);
    }

    public String getRegularPeakDescription() {
        return getNonNullTridionString(regularPeakDescription);
    }

    public String getRegularValueDescription() {
        return getNonNullTridionString(regularValueDescription);
    }

    public String getRegularRegularDescription() {
        return getNonNullTridionString(regularRegularDescription);
    }

    public String getValuePeakDescription() {
        return getNonNullTridionString(valuePeakDescription);
    }

    public String getValueRegularDescription() {
        return getNonNullTridionString(valueRegularDescription);
    }

    public String getValueValueDescription() {
        return getNonNullTridionString(valueValueDescription);
    }

    public String getParkLabel() {
        return getNonNullTridionString(parkLabel);
    }

    public String getParksLabel() {
        return getNonNullTridionString(parksLabel);
    }

    public String getGuestLabel() {
        return getNonNullTridionString(guestLabel);
    }

    public String getGuestsLabel() {
        return getNonNullTridionString(guestsLabel);
    }

    public String getUEP1ParkDescription() {
        return getNonNullTridionString(uep1Parkdescription);
    }

    public String getUEP2ParkDescription() {
        return getNonNullTridionString(uep2Parkdescription);
    }

    public String getUEP3ParkDescription() {
        return getNonNullTridionString(uep3Parkdescription);
    }
    public String getTicketProcessingMsg() { return getNonNullTridionString(ticketProcessingMsg); }

    public String getAvailableTimesLabel() {
        return getNonNullTridionString(availableTimesLabel);
    }

    public String getUnavailableLabel() {
        return getNonNullTridionString(unavailableLabel);
    }

    public String getTransactionHistorySeeAllLabel() {
        return getNonNullTridionString(transactionHistorySeeAllLabel);
    }

    /**
     * This method is intentionally private. Use {@link #getPartyMemberAssignSecondaryText(int)}.
     */
    private String getPartyMemberAssignSecondaryText() {
        return partyMemberAssignSecondaryText;
    }

    public String getPartyMemberAssignSecondaryText(int count) {
        return StringUtils.replace(getPartyMemberAssignSecondaryText(), COUNT_REPLACER, Integer.toString(count));
    }

    public String getContactInformationTitle() {
        return getNonNullTridionString(contactInformationTitle);
    }
    public String getEmptyWalletText() { return getNonNullTridionString(emptyWalletText); }

    public String getWalletEmptyHeroImage() { return getNonNullTridionString(walletEmptyHeroImage); }

    public String getEmailAddressToolTip() { return getNonNullTridionString(emailAddressToolTip); }

    public String getAddressDeletingMessage() { return getNonNullTridionString(addressDeletingMessage); }

    public String getAddressListPageTitle() { return getNonNullTridionString(addressListPageTitle); }

    public String getPrimaryAddressSwitchLabel() { return getNonNullTridionString(primaryAddressSwitchLabel); }

    public String getManageAccountAddAddressLabel() { return getNonNullTridionString(manageAccountAddAddressLabel); }

    public String getContactInformationUpdateLabel() { return getNonNullTridionString(contactInformationUpdateLabel); }

    public String getAccountHoldingOptionLabel() { return getNonNullTridionString(accountHoldingOptionLabel); }

    public String getPersonalCardLabel() { return getNonNullTridionString(personalCardLabel); }

    public String getAddressCardLabel() { return getNonNullTridionString(addressCardLabel); }

    public String getMyProfilePageTitle() { return getNonNullTridionString(myProfilePageTitle); }

    public String getPrefixLabel() { return getNonNullTridionString(prefixLabel); }

    public String getPersonalInformationPageTitle() { return getNonNullTridionString(personalInformationPageTitle); }

    public String getNoneLabel() { return getNonNullTridionString(noneLabel); }

    public ArrayList<String> getPrefixValues() {
        return getTridionStringAsArrayList(prefixValues);
    }

    public String getCurrentPasswordLabel() { return getNonNullTridionString(currentPasswordLabel); }

    public String getUpdatePasswordPageTitle() { return getNonNullTridionString(updatePasswordPageTitle); }

    /**
     * This method is intentionally private. Use {@link #getFormattedPrice(BigDecimal)}.
     */
    private String getBIPriceStringText() { return getNonNullTridionString(bIPriceStringText); }

    public String getPageHeaderPaymentTitle() { return getNonNullTridionString(pageHeaderPaymentTitle); }

    public String getTicketSelectionApplyLabel() { return getNonNullTridionString(ticketSelectionApplyLabel); }

    public String getPageHeaderSCTitle() { return getNonNullTridionString(pageHeaderSCTitle); }

    public String getSelectNumberOfDaysLabel() {
        return getNonNullTridionString(selectNumberOfDaysLabel);
    }

    public String getAddOnDetailsLabel() {
        return getNonNullTridionString(addOnDetailsLabel);
	}

    public String getScPromoApplied() {
        return getNonNullTridionString(scPromoApplied);
    }

    public String getLearnMoreLabel() {
        return getNonNullTridionString(learnMoreLabel);
    }

    public String getHearMoreLabel() {
        return getNonNullTridionString(hearMoreLabel);
    }

    public String getAllFieldsOptionalLabel() {
        return getNonNullTridionString(allFieldsOptionalLabel);
    }

    public String getDontTellMeLabel() {
        return getNonNullTridionString(dontTellMeLabel);
    }

    public String getTellMeSomeLabel() {
        return getNonNullTridionString(tellMeSomeLabel);
    }

    public String getTellMeMoreLabel() {
        return getNonNullTridionString(tellMeMoreLabel);
    }
    public String getTravelSeasonPrefLabel() {
        return getNonNullTridionString(travelSeasonPrefLabel);
    }

    public String getCPSendMailOptionLabel() {
        return getNonNullTridionString(CPSendMailOptionLabel);
    }

    public String getCPSocialAdvertisingOptionLabel() {
        return getNonNullTridionString(CPSocialAdvertisingOptionLabel);
    }

    public String getCPEmailOptionLabel() {
        return getNonNullTridionString(CPEmailOptionLabel);
    }

    public String getCPTextMessageOptionLabel() {
        return getNonNullTridionString(CPTextMessageOptionLabel);
    }

    public String getCPSocialAdvertisingDescription() {
        return getNonNullTridionString(CPSocialAdvertisingDescription);
    }

    public String getCPEmailFrequencyOptions() {
        return getNonNullTridionString(CPEmailFrequencyOptions).trim().replace("[\"","").replace("\"]","").replace("\",\"",",");
    }

    public String getCPHeadingLabel(){
        return getNonNullTridionString(CPHeadingLabel);
    }

    public String getMyInterestsPageTitle(){
        return getNonNullTridionString(MyInterestsPageTitle);
    }

    public String getManageInterestsOrder(){
        return getNonNullTridionString(ManageInterestsOrder).trim().replace("[\"","").replace("\"]","").replace("\",\"",",");
    }

    public String getCPSendMailNotOkOptionLabel() {
        return getNonNullTridionString(CPSendMailNotOkOptionLabel);
    }

    public String getCPSendMailOkOptionLabel() {
        return getNonNullTridionString(CPSendMailOkOptionLabel);
    }

    public String getCPSocialAdvertisingNotOkOptionLabel() {
        return getNonNullTridionString(CPSocialAdvertisingNotOkOptionLabel);
    }

    public String getCPSocialAdvertisingOkOptionLabel() {
        return getNonNullTridionString(CPSocialAdvertisingOkOptionLabel);
    }

    public String getCPEmailNotOkOptionLabel() {
        return getNonNullTridionString(CPEmailNotOkOptionLabel);
    }

    public String getCPEmailOkOptionLabel() {
        return getNonNullTridionString(CPEmailOkOptionLabel);
    }

    public String getCPTextMessageNotOkOptionLabel() {
        return getNonNullTridionString(CPTextMessageNotOkOptionLabel);
    }

    public String getCPTextMessageOkOptionLabel() {
        return getNonNullTridionString(CPTextMessageOkOptionLabel);
    }

    public String getCPInterestsUpdated() {
        return getNonNullTridionString(CPInterestsUpdated);
    }

    public String getSpecialOfferBannerText() {
        return getNonNullTridionString(specialOfferBannerText);
    }

    public String getShoppingCartSpecialOfferBannerText() {
        return getNonNullTridionString(shoppingCartSpecialOfferBannerText);
    }

    public String getNoLimitLabel() {
        return getNonNullTridionString(noLimitLabel);
    }

    public String getDailySpendingLimitsLabel() {
        return getNonNullTridionString(dailySpendingLimitsLabel);
    }

    public String getFolioPaymentMethodsLabel() {
        return getNonNullTridionString(folioPaymentMethodsLabel);
    }

    public String getManageAllLabel() {
        return getNonNullTridionString(manageAllLabel);
    }

    public String getDailySpendingLimitSelectionLabel() {
        return getNonNullTridionString(dailySpendingLimitSelectionLabel);
    }

    public String getDailySpendingLimitOption1Label() {
        return getNonNullTridionString(dailySpendingLimitOption1Label);
    }

    public String getDailySpendingLimitOption2Label() {
        return getNonNullTridionString(dailySpendingLimitOption2Label);
    }

    public String getDailySpendingLimitOption3Label() {
        return getNonNullTridionString(dailySpendingLimitOption3Label);
    }

    public String getConfirmLabel() {
        return getNonNullTridionString(confirmLabel);
    }

    public String getWalletCardDeleteMsg() {
        return getNonNullTridionString(walletCardDeleteMsg);
    }

    public String getDeleteFolioCardLabel() {
        return getNonNullTridionString(deleteFolioCardLabel);
    }

    public String getWalletCardDeletePrimaryMsg() {
        return getNonNullTridionString(walletCardDeletePrimaryMsg);
    }

    public String getAlertsHeaderLabel() {
        return getNonNullTridionString(alertsHeaderLabel);
    }

    public String getAlertsHeaderCopy() {
        return getNonNullTridionString(alertsHeaderCopy);
    }

    public String getSetAlertsLabel() {
        return getNonNullTridionString(setAlertsLabel);
    }

    public String getAlertsOptionEmailLabel() {
        return getNonNullTridionString(alertsOptionEmailLabel);
    }

    public String getAlertsOptionTextLabel() {
        return getNonNullTridionString(alertsOptionTextLabel);
    }

    public String getAlertsOptionHeaderLinkLabel() {
        return getNonNullTridionString(alertsOptionHeaderLinkLabel);
    }

    public String getSaveLabel() {
        return getNonNullTridionString(saveLabel);
    }

    public String getW10() {
        return getNonNullTridionString(w10);
    }

    public String getW11() {
        return getNonNullTridionString(w11);
    }

    public String getW12() {
        return getNonNullTridionString(w12);
    }

    public String getW1() {
        return getNonNullTridionString(w1);
    }

    public String getW2() {
        return getNonNullTridionString(w2);
    }

    public String getW3() {
        return getNonNullTridionString(w3);
    }

    public String getW4() {
        return getNonNullTridionString(w4);
    }

    public String getW5() {
        return getNonNullTridionString(w5);
    }

    public String getW6() {
        return getNonNullTridionString(w6);
    }

    public String getW7() {
        return getNonNullTridionString(w7);
    }

    public String getW8() {
        return getNonNullTridionString(w8);
    }

    public String getW9() {
        return getNonNullTridionString(w9);
    }

    public String getPinHeaderCopy() {
        return getNonNullTridionString(pinHeaderCopy);
    }

    public String getPinHeaderLabel() {
        return getNonNullTridionString(pinHeaderLabel);
    }

    public String getEnterPINLabel() {
        return getNonNullTridionString(enterPINLabel);
    }

    public String getFlexPayDownPaymentCopy(ParkTicketGroups parkTicketGroups) {
        String output = getNonNullTridionString(flexPayDownPaymentCopy);
        if (parkTicketGroups != null) {
            BigDecimal totalDownPaymentAmount = parkTicketGroups.getFlexPayTotalDownPaymentAmount();
            output = StringUtils.replace(output, WCSKEY_DOWN_PAYMENT, getFormattedPrice(totalDownPaymentAmount));
            output = StringUtils.replace(output, WCSKEY_FLEX_PAY_ITEM_TAXES, getFormattedPrice(parkTicketGroups.getTaxes()));
        }
        return output;
    }

    public String getFlexPayFinancedCopy(ParkTicketGroups parkTicketGroups) {
        String output = getNonNullTridionString(flexPayFinancedCopy);
        if (parkTicketGroups != null) {
            List<OrderItem> orderItems = parkTicketGroups.getOrderItems();
            BigDecimal totalFinancedAmount = parkTicketGroups.getFlexPayTotalFinancedAmount();
            BigDecimal totalPaymentAmount = parkTicketGroups.getFlexPayTotalPaymentAmount();
            OUTER_LOOP: for (OrderItem orderItem : orderItems) {
                if (orderItem != null && orderItem.isFlexPay()) {
                    for (CommerceAttribute attribute : orderItem.getAttributes()) {
                        if (attribute != null && attribute.isFlexPayRecurringCount()) {
                            output = StringUtils.replace(output, WCSKEY_MONTHS, attribute.getValue());
                            break OUTER_LOOP;
                        }
                    }
                }
            }
            output = StringUtils.replace(output, WCSKEY_FLEX_PAY_ITEM_TOTAL_FINANCED, getFormattedPrice(totalFinancedAmount));
            output = StringUtils.replace(output, WCSKEY_PAYMENT, getFormattedPrice(totalPaymentAmount));
        }
        return output;
    }

    public String getFlexPayTotalDescription(DisplayPricing displayPricing) {
        String output = getNonNullTridionString(flexPayDownPaymentCopy) + "\n" + getNonNullTridionString(flexPayFinancedCopy);
        if (displayPricing != null) {
            output = StringUtils.replace(output, WCSKEY_DOWN_PAYMENT, getFormattedPrice(displayPricing.getTotalDownPayment()));
            output = StringUtils.replace(output, WCSKEY_FLEX_PAY_ITEM_TAXES, getFormattedPrice(displayPricing.getTotalDownPaymentTax()));
            output = StringUtils.replace(output, WCSKEY_FLEX_PAY_ITEM_TOTAL_FINANCED, getFormattedPrice(displayPricing.getTotalFinanced()));
            output = StringUtils.replace(output, WCSKEY_PAYMENT, getFormattedPrice(displayPricing.getPaymentPlanTotalPaymentAmount()));
            PaymentPlan paymentPlan = displayPricing.getFirstPaymentPlan();
            if (paymentPlan != null && paymentPlan.getRecurringPaymentAmount() != null) {
                output = StringUtils.replace(output, WCSKEY_MONTHS, String.valueOf(paymentPlan.getRecurringPaymentCount()));
            }
        }
        return output;
    }

    public int getSCValueTextColor() {
        String colorString = getNonNullTridionString(SCValueTextColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_VALUE_TEXT_COLOR);
        return color;
    }

    public int getSCRegularTextColor() {
        String colorString = getNonNullTridionString(SCRegularTextColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_REGULAR_TEXT_COLOR);
        return color;
    }

    public int getSCPeakTextColor() {
        String colorString = getNonNullTridionString(SCPeakTextColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_PEAK_TEXT_COLOR);
        return color;
    }

    public int getSCMixedTextColor() {
        String colorString = getNonNullTridionString(SCMixedTextColor);
        int color = ColorUtils.parseColor(colorString, DEFAULT_CALENDAR_MIXED_TEXT_COLOR);
        return color;
    }

    public ArrayList<String> getSeasonKeyValues() {
        return getTridionStringAsArrayList(TravelPreferencesOptions);
    }

    public ArrayList<String> getSeasonDisplayValues() {
        return getTridionStringAsArrayList(TravelPreferencesLabels);
    }

    public String getProfileInterestParksLabel() {
        return getNonNullTridionString(ProfileInterestParksLabel);
    }

    public String getProfileInterestSeasonsLabel() {
        return getNonNullTridionString(ProfileInterestSeasonsLabel);
    }

    public String getAnnualPassHeaderLabel() {
        return getNonNullTridionString(annualPassHeaderLabel);
    }

    public String getPageHeaderUEPCTitle() {
        return getNonNullTridionString(pageHeaderUEPCTitle);
    }

    public String getStartingFromLabel() {
        return getNonNullTridionString(startingFromLabel);
    }

    public String getPageHeaderWLPTitle() {
        return getNonNullTridionString(pageHeaderWLPTitle);
    }

    public String getWalletLinkPurchasesLinkLabel() {
        return getNonNullTridionString(walletLinkPurchasesLinkLabel);
    }

    public String getWalletLinkPurchaseOrderOrTicketNumberLabel() {
        return getNonNullTridionString(walletLinkPurchaseOrderOrTicketNumberLabel);
    }

    public String getWalletLinkPurchaseInfoToolTip() {
        return getNonNullTridionString(walletLinkPurchaseInfoToolTip);
    }

    public String getWalletLinkPurchaseLocatedHeader() {
        return getNonNullTridionString(walletLinkPurchaseLocatedHeader);
    }

    public String getWalletLinkPurchaseSelectLocationHeader() {
        return getNonNullTridionString(walletLinkPurchaseSelectLocationHeader);
    }

    public String getWalletLinkPurchaseFirstNameLabel() {
        return getNonNullTridionString(walletLinkPurchaseFirstNameLabel);
    }

    public String getWalletLinkPurchaseLastNameLabel() {
        return getNonNullTridionString(walletLinkPurchaseLastNameLabel);
    }

    public String getWalletLinkSelectChannelLabel() {
        return getNonNullTridionString(walletLinkSelectChannelLabel);
    }

    public String getConfirmationCreatePinHeader() {
        return getNonNullTridionString(confirmationCreatePinHeader);
    }

    public String getConfirmationCreatePinSubHeader() {
        return getNonNullTridionString(confirmationCreatePinSubHeader);
    }

    public String getConfirmationCreatePinMobileCopy() {
        ArrayList<String> tridionList = getTridionStringAsArrayList(confirmationCreatePinCopy);
        return getNonNullStringFromTridionArrayList(tridionList, 0);
    }

    public String getConfirmationCreatePinPartyCopy() {
        ArrayList<String> tridionList = getTridionStringAsArrayList(confirmationCreatePinCopy);
        return getNonNullStringFromTridionArrayList(tridionList, 1);
    }

    public String getConfirmationCreatePinTransactionCopy() {
        ArrayList<String> tridionList = getTridionStringAsArrayList(confirmationCreatePinCopy);
        return getNonNullStringFromTridionArrayList(tridionList, 2);
    }

    public String getOrderConfirmationPinMobileImageView() {
        ArrayList<String> tridionList = getTridionStringAsArrayList(confirmationCreatePinCopyImages);
        return getNonNullStringFromTridionArrayList(tridionList, 0);
    }

    public String getOrderConfirmationPinCardImageView() {
        ArrayList<String> tridionList = getTridionStringAsArrayList(confirmationCreatePinCopyImages);
        return getNonNullStringFromTridionArrayList(tridionList, 1);
    }

    public String getOrderConfirmationPinLetterImageView() {
        ArrayList<String> tridionList = getTridionStringAsArrayList(confirmationCreatePinCopyImages);
        return getNonNullStringFromTridionArrayList(tridionList, 2);
    }

    /**
     * Performs a safe retrieval from the passed in ArrayList.  Returns !!MISSING!! or empty string
     * if the index is out of bounds.
     *
     * @param stringList The ArrayList
     * @param index The index of the element to retrieve
     * @return The String value of the ArrayList at the index
     */
    private String getNonNullStringFromTridionArrayList(ArrayList<String> stringList, int index) {
        if (stringList.size() > index) {
            return stringList.get(index);
        } else {
            return getNonNullTridionString(null);
        }
    }

    private String getCompareAPUrl() {
        return compareAPUrl;
    }

    public String getCompareAPUrl(boolean isFloridaResident) {
        String url = getCompareAPUrl();
        if (isFloridaResident && !TextUtils.isEmpty(url)) {
            url += COMPARE_AP_URL_FL_PARAM;
        }
        return url;
    }

    private String getTextBeforeSignUpTNC() {
        return textBeforeSignUpTNC;
    }

    public String getFlexPayLinkLabel() {
        return flexPayLinkLabel;
    }

    public SpannableString getFlexPaySignupTNC(ClickListener linkClickListener) {
        String tncString = String.format(Locale.US, "%s %s", getTextBeforeSignUpTNC(), getFlexPayLinkLabel());
        SpannableString spanString = ClickableTextHelper.createEmbeddedSpannable(tncString,
                getFlexPayLinkLabel(), R.color.ticket_text_color, false, linkClickListener);
        return spanString;
    }


    public Integer getMinimumInterestLevel() {
        Integer i = null;
        if (InterestsMinimumDisplayValue != null) {
            try {
                i = Integer.valueOf(InterestsMinimumDisplayValue);
            } catch (NumberFormatException e) {
                CrashAnalyticsUtils.logHandledException(e);
            }
        }
        return i == null ? 4 : i;
    }

    public ArrayList<Integer> getPasswordStrengthColorList() {
        ArrayList<String> strengthColorStringList = getTridionStringAsArrayList(passwordFlagColors);

        ArrayList<Integer> strengthColorList = new ArrayList<>();
        for (int i = 0; i < strengthColorStringList.size(); i++) {
            String strengthColorString = strengthColorStringList.get(i);
            if (!TextUtils.isEmpty(strengthColorString) && strengthColorString.trim().startsWith("#")) {
                strengthColorString = strengthColorString.trim().replaceAll("#", "");
                StringBuilder properColorString = new StringBuilder("#");

                // Sanitize #RGB to #RRGGBB
                if (strengthColorString.length() == 3) {
                    properColorString
                            .append(strengthColorString.charAt(0)).append(strengthColorString.charAt(0))
                            .append(strengthColorString.charAt(1)).append(strengthColorString.charAt(1))
                            .append(strengthColorString.charAt(2)).append(strengthColorString.charAt(2));
                }
                // Sanitize #ARGB to #AARRGGBB
                else if (strengthColorString.length() == 4) {
                    properColorString
                            .append(strengthColorString.charAt(0)).append(strengthColorString.charAt(0))
                            .append(strengthColorString.charAt(1)).append(strengthColorString.charAt(1))
                            .append(strengthColorString.charAt(2)).append(strengthColorString.charAt(2))
                            .append(strengthColorString.charAt(3)).append(strengthColorString.charAt(3));
                } else if (strengthColorString.length() == 6 || strengthColorString.length() == 8) {
                    properColorString.append(strengthColorString);
                }

                // Parse the color and add it to the list
                try {
                    int color = Color.parseColor(properColorString.toString());
                    strengthColorList.add(color);
                } catch (Exception e) {
                    // Ignore if the parsing fails
                }
            }
        }

        return strengthColorList;
    }

    public ArrayList<String> getPasswordStrengthTextList() {
        return getTridionStringAsArrayList(passwordFlagStrength);
    }

    /**********************************************************************************************
     * The following methods are helper methods that combine data from Tridion into Strings or
     * other objects that can be directly used by the app
     **********************************************************************************************/

    /**
     * Builds a terms and conditions policy string, complete with links, to be parsed
     * by HTML parser.
     *
     * @return The terms and conditions/policy string
     */
    public Spanned getTermsAndPolicy() {
        StringBuilder sb = new StringBuilder();
        // Append the terms and conditions beginning <a href="..."> tag
        sb.append(getTextBeforeTnc());
        sb.append(" <a href=\"");
        sb.append(getTncTermsLink());
        sb.append("\">");
        // Append the link label
        sb.append(getTncTermsLinkLabel());
        // Append the closing </a>
        sb.append("</a> ");
        sb.append(getTncAndLabel());
        // Append the privacy policy beginning <a href="..."> tag
        sb.append(" <a href=\"");
        sb.append(getTncPrivacyLink());
        sb.append("\">");
        // Append the link label
        sb.append(getTncPrivacyLinkLabel());
        // Append the closing </a>
        sb.append("</a>");
        return Html.fromHtml(sb.toString());
    }

    /**
     * Builds a privacy information string, complete with links, to be parsed
     * by HTML parser.
     *
     * @return The privacy information string
     */
    public Spanned getPrivacyInformation() {
        String privacyInfoString = getPrivacyInformationText().replace(getPrivacyInformationLinkText(),
                String.format(Locale.US,"<a href=\"%s\">%s</a>", getPrivacyInformationLinkUrl(),
                        getPrivacyInformationLinkText()));
        return Html.fromHtml(privacyInfoString);
    }

    /**
     * Builds a terms and conditions policy string, complete with links, to be parsed
     * by HTML parser.
     *
     * @return The terms and conditions/policy string
     */
    public Spanned getTermsAndPolicyLinks() {
        StringBuilder sb = new StringBuilder();
        sb.append(" <a href=\"");
        sb.append(getTncTermsLink());
        sb.append("\">");
        // Append the link label
        sb.append(getTncTermsLinkLabel());
        // Append the closing </a>
        sb.append("</a> ");
        sb.append(getTncAndLabel());
        // Append the privacy policy beginning <a href="..."> tag
        sb.append(" <a href=\"");
        sb.append(getTncPrivacyLink());
        sb.append("\">");
        // Append the link label
        sb.append(getTncPrivacyLinkLabel());
        // Append the closing </a>
        sb.append("</a>");
        return Html.fromHtml(sb.toString());
    }

    String mPriceDenomination;
    public String getPriceDenomination() {
        if (null == mPriceDenomination) {
            mPriceDenomination = getBIPriceStringText().replaceAll("[\\d\\.,]","");
        }
        return mPriceDenomination;
    }

    public String getFormattedPrice(BigDecimal price) {
        return getFormattedPrice(price, false);
    }

    public String getFormattedPrice(BigDecimal price, boolean hideDecimalPlacesIfInteger) {
        String priceFormat;

        // If requested, and price has no decimal places, don't show them
        if (hideDecimalPlacesIfInteger && NumberUtils.isIntegerValue(price)) {
            priceFormat = "%s%.0f";
        }
        // Otherwise show 2 decimal places
        else {
            priceFormat = "%s%.2f";
        }

        return String.format(Locale.US, priceFormat, getPriceDenomination(), price);
    }
}
