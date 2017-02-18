package com.universalstudios.orlandoresort.model.state.content;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.PreviousOrderById.response.Item;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.ExpressPassTicketGroups;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.Ticket;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.OrderItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlement;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementAttributes;

import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * TridionLabelSpec that contains #WCSKEY# within the strings that need to be replaced by appropriate
 * attributes from ticket, UEP, and add-ons.
 *
 * @author tjudkins
 * @since 12/7/16
 */
@Parcel
public class TridionLabelSpec {
    public static final String TAG = "TridionLabelSpec";

    private static final SimpleDateFormat SERVICE_PRICING_DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.US);
    private static final SimpleDateFormat PRESENTATION_DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private static final SimpleDateFormat PRESENTATION_TIME_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);

    private static final String WCS_NO_KEY = "#NOKEY#";
    private static final String WCSKEY_PRICE_PER_DAY = "#WCSKEYPRICEPERDAY#";
    private static final String WCSKEY_DAYS = "#WCSKEYDAYS#";
    private static final String WCSKEY_NUM_PARKS = "#WCSKEYPARKS#";
    private static final String WCSKEY_PARK_NAME = "#WCSKEYPARKNAME#";
    private static final String WCSKEY_PER_CHILD = "#WCSKEYPERCHILD#";
    private static final String WCSKEY_DOWN_PAYMENT = "#WCSKEYDOWNPAYMENT#";
    private static final String WCSKEY_PER_MONTH = "#WCSKEYMONTHLYPAYMENT#";
    private static final String WCSKEY_PAYMENT = "#WCSKEYPAYMENT#";
    private static final String WCSKEY_USES_ALLOWED = "#WCSKEYUSESALLOWED#";
    private static final String WCSKEY_TICKET_DATE = "#WCSKEYTICKETDATE#";
    private static final String WCSKEY_POINT_OF_ORIGIN = "#WCSKEYPOO#";
    private static final String WCSKEY_TICKET_OPTION = "#WCSKEYTICKETOPTION#";
    private static final String SELECTED_SKU_TITLE = "#SELECTEDSKUTITLE#";
    private static final String WCSKEY_TICKET_TIME = "#WCSKEYTICKETTIME#";
    private static final String WCSKEY_TICKET_AGE = "#WCSTICKETAGE#";
    private static final String WCSKEYS_ROWS = "#WCSROWS#";
    private static final String WCSKEY_SEATS = "#WCSSEATS#";
    private static final String WCSKEY_FLEXPAY = "#ISFLEXPAY#";
    private static final String WCSKEY_MONTHS = "#WCSKEYMONTHS#";
    private static final String WCSKEY_PASS = "#WCSKEYPASS#";
    private static final String WCSKEY_WCSKEYUUPARKS = "#WCSKEYUUPARKS#";
    public static final String DEFAULT_HEADER_COLOR = "#0e51d3";

    /**
     * Using a hashmap to contain the strings allows us to easily extend the set of labels without
     * updating the GSON object. Then, getters for each label retrieve the key from the map and
     * the appropriate #WCSKEY# variables instead of delegating to a utils class that needs to be
     * called. This provides safety so the consumer does not need to worry about #WCSKEY# variables.
     */
    HashMap<String, String> mTridionLabelMap;

    public TridionLabelSpec() {
        mTridionLabelMap = new HashMap<>();
    }

    public TridionLabelSpec(HashMap<String, String> tridionLabelMap) {
        if (null != tridionLabelMap) {
            mTridionLabelMap = tridionLabelMap;
        } else {
            mTridionLabelMap = new HashMap<>();
        }
    }

    private HashMap<String, String> getTridionLabelMap() {
        return mTridionLabelMap;
    }

    @NonNull
    private String getNonNullTridionLabel(String key) {
        String label = null;
        if (null != key) {
            label = getTridionLabelMap().get(key);
        }
        if (null == label) {
            label = "";
        }
        return label;
    }

    public void setItem(Item item) {

    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getBanner(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getBannerWCSKey() {
        return getNonNullTridionLabel("BannerWCSKey");
    }

    public String getBanner(CommerceCardItem cardItem) {
        return replaceWcsKeys(getBannerWCSKey(), cardItem);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getBanner(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getBannerLabelSpec() {
        return getNonNullTridionLabel("BannerLabel");
    }

    private String getBannerLabel(CommerceCardItem cardItem) {
        return replaceWcsKeys(getBannerLabelSpec(), cardItem);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getBanner(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getBannerLabelSecondarySpec() {
        return getNonNullTridionLabel("BannerLabelSecondary");
    }

    @NonNull
    public String getBannerBackgroundImage() {
        return getNonNullTridionLabel("BannerBgImage");
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getHeaderLine1(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeHeaderLine1() {
        return getNonNullTridionLabel("TypeHeaderLine1");
    }

    @NonNull
    public String getTypeHeaderLine1(CommerceCardItem cardItem) {
        return replaceWcsKeys(getTypeHeaderLine1(), cardItem);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getHeaderLine2(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeHeaderLine2() {
        return getNonNullTridionLabel("TypeHeaderLine2");
    }

    @NonNull
    public String getTypeHeaderLine2(CommerceCardItem cardItem) {
        return replaceWcsKeys(getTypeHeaderLine2(), cardItem);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getHeaderLine3(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeHeaderLine3() {
        return getNonNullTridionLabel("TypeHeaderLine3");
    }

    @NonNull
    public String getTypeHeaderLine3(CommerceCardItem cardItem) {
        return replaceWcsKeys(getTypeHeaderLine3(), cardItem);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getTypeAlternativeHeaderLine1(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeAlternativeHeaderLine1() {
        return getNonNullTridionLabel("TypeAlternativeHeaderLine1");
    }

    @NonNull
    public String getTypeAlternativeHeaderLine1(Ticket ticket) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine1(), ticket);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine1(ExpressPassTicketGroups expressPassTicketGroups) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine1(), expressPassTicketGroups);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine1(CommerceCardItem cardItem) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine1(), cardItem);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine1(WalletEntitlement walletEntitlement) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine1(), walletEntitlement);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getTypeAlternativeHeaderLine2(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeAlternativeHeaderLine2() {
        return getNonNullTridionLabel("TypeAlternativeHeaderLine2");
    }

    @NonNull
    public String getTypeAlternativeHeaderLine2(Ticket ticket) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine2(), ticket);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine2(ExpressPassTicketGroups expressPassTicketGroups) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine2(), expressPassTicketGroups);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine2(CommerceCardItem cardItem) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine2(), cardItem);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine2(WalletEntitlement walletEntitlement) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine2(), walletEntitlement);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getTypeAlternativeHeaderLine3(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeAlternativeHeaderLine3() {
        return getNonNullTridionLabel("TypeAlternativeHeaderLine3");
    }

    @NonNull
    public String getTypeAlternativeHeaderLine3(Ticket ticket) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine3(), ticket);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine3(ExpressPassTicketGroups expressPassTicketGroups) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine3(), expressPassTicketGroups);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine3(CommerceCardItem cardItem) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine3(), cardItem);
    }

    @NonNull
    public String getTypeAlternativeHeaderLine3(WalletEntitlement walletEntitlement) {
        return replaceWcsKeys(getTypeAlternativeHeaderLine3(), walletEntitlement);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getTypeAssignNamesAlternativeHeaderLine1(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeAssignNamesAlternativeHeaderLine1() {
        return getNonNullTridionLabel("TypeAssignNamesAlternativeHeaderLine1");
    }

    @NonNull
    public String getTypeAssignNamesAlternativeHeaderLine1(Ticket ticket) {
        return replaceWcsKeys(getTypeAssignNamesAlternativeHeaderLine1(), ticket);
    }

    @NonNull
    public String getTypeAssignNamesAlternativeHeaderLine1(ExpressPassTicketGroups expressPassTicketGroups) {
        return replaceWcsKeys(getTypeAssignNamesAlternativeHeaderLine1(), expressPassTicketGroups);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getTypeAssignNamesAlternativeHeaderLine2(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeAssignNamesAlternativeHeaderLine2() {
        return getNonNullTridionLabel("TypeAssignNamesAlternativeHeaderLine2");
    }

    @NonNull
    public String getTypeAssignNamesAlternativeHeaderLine2(Ticket ticket) {
        return replaceWcsKeys(getTypeAssignNamesAlternativeHeaderLine2(), ticket);
    }

    @NonNull
    public String getTypeAssignNamesAlternativeHeaderLine2(ExpressPassTicketGroups expressPassTicketGroups) {
        return replaceWcsKeys(getTypeAssignNamesAlternativeHeaderLine2(), expressPassTicketGroups);
    }

    /**
     * This method is purposefully private. This field uses #WCSKEY# variables to insert values
     * into the string. Use the associated getTypeAssignNamesAlternativeHeaderLine3(...) method with a field that extracts
     * these variables
     */
    @NonNull
    private String getTypeAssignNamesAlternativeHeaderLine3() {
        return getNonNullTridionLabel("TypeAssignNamesAlternativeHeaderLine3");
    }

    @NonNull
    public String getTypeAssignNamesAlternativeHeaderLine3(Ticket ticket) {
        return replaceWcsKeys(getTypeAssignNamesAlternativeHeaderLine3(), ticket);
    }

    @NonNull
    public String getTypeAssignNamesAlternativeHeaderLine3(ExpressPassTicketGroups expressPassTicketGroups) {
        return replaceWcsKeys(getTypeAssignNamesAlternativeHeaderLine3(), expressPassTicketGroups);
    }

    /**
     * This method is purposefully private. Use {@link #getHeaderBackgroundColor()}.
     */
    @NonNull
    private String getHeaderBackgroundColorString() {
        return getNonNullTridionLabel("TypeHeaderBgColor");
    }

    public int getHeaderBackgroundColor() {
        String colorString = getHeaderBackgroundColorString();
        if (TextUtils.isEmpty(colorString)) {
            colorString = DEFAULT_HEADER_COLOR;
        }
        return Color.parseColor(colorString);
    }

    @NonNull
    public String getBadgeBackgroundImageUrl() {
        return getNonNullTridionLabel("BadgeBgImage");
    }

    @NonNull
    public String getBadgeLabel1() {
        return getNonNullTridionLabel("BadgeLabel1");
    }

    @NonNull
    public String getBadgeLabel2() {
        return getNonNullTridionLabel("BadgeLabel2");
    }

     @NonNull
    public String getPurchaseOptionTitle() {
        return getNonNullTridionLabel("PurchaseOptionTitle");
    }

    @NonNull
    public String getPurchaseOptionAlternativeTitle() {
        return getNonNullTridionLabel("PurchaseOptionAlternativeTitle");
    }

    @NonNull
    public String getPurchaseOptionTeaser(CommerceCardItem commerceCardItem) {
        return replaceWcsKeys(getNonNullTridionLabel("PurchaseOptionTeaser"), commerceCardItem);
    }

    @NonNull
    public String getPurchaseOptionPrimaryPriceLabel() {
        return getNonNullTridionLabel("PurchaseOptionPrimaryPriceLabel");
    }

    @NonNull
    public String getPurchaseOptionSecondaryPriceLabel(CommerceGroup commerceGroup) {
        String tridionString = getNonNullTridionLabel("PurchaseOptionSecondaryPriceLabel");
        if (null != commerceGroup && null != commerceGroup.getChildPricingAndInventory()) {
            if (tridionString.contains(WCSKEY_PER_CHILD)) {
                BigDecimal childPrice = commerceGroup.getChildPricingAndInventory().getOfferPrice();
                tridionString = StringUtils.replace(tridionString, WCSKEY_PER_CHILD,
                        IceTicketUtils.getTridionConfig().getFormattedPrice(childPrice));
            } else if (tridionString.contains(WCSKEY_PER_MONTH)){
                BigDecimal childPrice = commerceGroup.recurringPaymentAmount();
                tridionString = StringUtils.replace(tridionString, WCSKEY_PER_MONTH,
                        IceTicketUtils.getTridionConfig().getFormattedPrice(childPrice));
            }
        }

        return tridionString;
    }

    @NonNull
    public String getUpsellTeaserText() {
        return getNonNullTridionLabel("UpsellTeaserText");
    }

    @NonNull
    public String getUpsellImageURL() {
        return getNonNullTridionLabel("UpsellImage");
    }

    @NonNull
    public String getDetails() {
        return getNonNullTridionLabel("Details");
    }

    @NonNull
    private String getCommonDetailsRTFDesc() {
        return getNonNullTridionLabel("CommonDetailsRTFDesc");
    }

    @NonNull
    public String getRestrictions() {
        return getNonNullTridionLabel("RestrictionsRTFDesc");
    }

    @NonNull
    private String getOptionsRTFDesc(CommerceGroup group, CommerceCardItem cardItem) {
        TridionLabelSpec tcmId1Spec = TridionLabelSpecManager.getSpecForId(cardItem.getIdForTridion());
        String replacedTridionString = getNonNullTridionLabel("OptionsRTFDesc");
        if (replacedTridionString.contains(WCSKEY_PASS)) {
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_PASS, tcmId1Spec.getTypeHeaderLine1(cardItem));
        }
        replacedTridionString = replaceWcsKeys(replacedTridionString, group);
        return replacedTridionString;
    }

    @NonNull
    public String getTitle() {
        return getNonNullTridionLabel("Title");
    }

    @NonNull
    public String getImage() {
        return getNonNullTridionLabel("Image");
    }

    @NonNull
    public String getDescription() {
        return getNonNullTridionLabel("Description");
    }

    @NonNull
    public String getAboutSectionSummary() {
        return getNonNullTridionLabel("AboutSectionSummary");
    }

    @NonNull
    public String getQuantitySelectorHeading() {
        return getNonNullTridionLabel("QuantitySelectorHeading");
    }

    @NonNull
    public String getDateSectionSummary() {
        return getNonNullTridionLabel("DateSectionSummary");
    }

    @NonNull
    public String getDateSelectorHeading() {
        return getNonNullTridionLabel("DateSelectorHeading");
    }

    @NonNull
    public String getTimeSectionSummary() {
        return getNonNullTridionLabel("TimeSectionSummary");
    }

    @NonNull
    public String getTimeSelectorHeading() {
        return getNonNullTridionLabel("TimeSelectorHeading");
    }

    @NonNull
    public String getTypeSectionSummary() {
        return getNonNullTridionLabel("TypeSectionSummary");
    }

    @NonNull
    public String getTypeSelectorHeading() {
        return getNonNullTridionLabel("TypeSelectorHeading");
    }

    @NonNull
    public String getTypeSelectorImage() {
        return getNonNullTridionLabel("TypeSelectorImage");
    }

    @NonNull
    public String getProductSummarySectionInstructionalText() {
        return getNonNullTridionLabel("ProductSummarySectionInstructionalText");
    }

    @NonNull
    public String getSummaryImage() {
        return getNonNullTridionLabel("SummaryImage");
    }

    @NonNull
    public String getSummarySubHeading() {
        return getNonNullTridionLabel("SummarySubHeading");
    }

    @NonNull
    public String getSelectionSummary() {
        return getNonNullTridionLabel("SelectionSummary");
    }

    /**
     * The image for cards
     */
    @NonNull
    public String getTileImage() {
        return getNonNullTridionLabel("TileImage");
    }

    @NonNull
    public String getButtonCtaUrl() {
        return getNonNullTridionLabel("ButtonCTA");
    }

    @NonNull
    public String getPriceTextAbove() {
        return getNonNullTridionLabel("PriceTextAbove");
    }

    @NonNull
    public String getTeaserText() {
        return getNonNullTridionLabel("TeaserText");
    }

    @NonNull
    public String getTeaser() {
        return getNonNullTridionLabel("Teaser");
    }

    /**
     * Use {@link #getPricePerUnitPrimaryString(Ticket)} if you want "($0.00 per unit)"
     * @return PriceTextBelowPrimary
     */
    @NonNull
    public String getPriceTextBelowPrimary() {
        return getNonNullTridionLabel("PriceTextBelowPrimary");
    }

    /**
     * Use {@link #getPricePerUnitSecondaryString(Ticket)} if you want "($0.00 per unit)"
     * @return PriceTextBelowSecondary
     */
    @NonNull
    public String getPriceTextBelowSecondary() {
        return getNonNullTridionLabel("PriceTextBelowSecondary");
    }

    @NonNull
    public String getButtonLabel() {
        return getNonNullTridionLabel("ButtonLabel");
    }

    @NonNull
    public String getQuantitySelectorBelowLabel1() {
        return getNonNullTridionLabel("QuantitySelectorBelowLabel1");
    }

    @NonNull
    public String getQuantitySelectorBelowLabel2() {
        return getNonNullTridionLabel("QuantitySelectorBelowLabel2");
    }

    @NonNull
    public String getItemName() {
        return getNonNullTridionLabel("ItemName");
    }

    @NonNull
    public String getSkuSelectorHeading() {
        return getNonNullTridionLabel("SKUSelectorHeading");
    }


    public String getTypeHeaderLogo() {
      return  getNonNullTridionLabel("TypeHeaderLogo");
    }

    public String getPurchaseOption1Title() {
        return  getNonNullTridionLabel("PurchaseOption1Title");
    }

    public String getPurchaseOption1Teaser() {
        return  getNonNullTridionLabel("PurchaseOption1Teaser");
    }

    public String getPurchaseOption1PrimaryPriceLabel() {
        return getNonNullTridionLabel("PurchaseOption1PrimaryPriceLabel");
    }

    public String getPurchaseOptionLegal(CommerceGroup cardItem){
        return replaceWcsKeys(getNonNullTridionLabel("PurchaseOptionLegal"), cardItem);
    }

    /**********************************************************************************************
     * These are helper methods for replacing #WCSKEY# strings in the Tridion strings
     * ********************************************************************************************/

    @NonNull
    private String replaceWcsKeys(String tridionString, Ticket ticket) {
        String replacedTridionString = tridionString;
        if (null != ticket) {
            replacedTridionString = replaceWcsKeys(replacedTridionString, ticket.getItem());
            if (ticket.isFlexPay()) {
                replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_FLEXPAY, getPurchaseOptionTitle());
            } else {
                replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_FLEXPAY, "");
            }
            if (null != ticket.getOrderItems()) {
                for (OrderItem orderItem : ticket.getOrderItems()) {
                    replacedTridionString = replaceWcsKeys(replacedTridionString, orderItem);
                }
            }
        }
        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, ExpressPassTicketGroups expressPassTicketGroups) {
        String replacedTridionString = tridionString;
        if (null != expressPassTicketGroups) {
            replacedTridionString = replaceWcsKeys(replacedTridionString, expressPassTicketGroups.getItem());
            if (null != expressPassTicketGroups.getOrderItems()) {
                for (OrderItem orderItem : expressPassTicketGroups.getOrderItems()) {
                    replacedTridionString = replaceWcsKeys(replacedTridionString, orderItem);
                }
            }
        }
        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, OrderItem orderItem) {
        String replacedTridionString = tridionString;

        if (null != orderItem) {
            replacedTridionString = replaceWcsKeys(replacedTridionString, orderItem.getAttributes());
        }

        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, Item item) {
        String replacedTridionString = tridionString;
        if (null != item) {
            TridionLabelSpec tcmId2TridionSpec = TridionLabelSpecManager.getSpecForId(item.getTcmId2());

            replacedTridionString = replaceWcsKeys(replacedTridionString, item.getAttributes());
            replacedTridionString = StringUtils.replace(replacedTridionString, SELECTED_SKU_TITLE, tcmId2TridionSpec.getItemName());
        }

        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, CommerceGroup group) {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        String replacedTridionString = tridionString;
        if (null != group) {
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_DOWN_PAYMENT,
                    tridionConfig.getFormattedPrice(group.getDownPaymentValue()));
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_PER_MONTH,
                    tridionConfig.getFormattedPrice(group.recurringPaymentAmount()));
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_PAYMENT,
                    tridionConfig.getFormattedPrice(group.getFinancedAmount()));
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_MONTHS,
                    Integer.toString(group.getRecurringPaymentCount()));
        }
        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, CommerceCardItem cardItem) {
        String replacedTridionString = tridionString;
        if (null != cardItem) {
            if (replacedTridionString.contains(WCS_NO_KEY)) {
                replacedTridionString = StringUtils.replace(replacedTridionString, WCS_NO_KEY, getBannerLabel(cardItem));
            }
            if (replacedTridionString.contains(WCSKEY_FLEXPAY)) {
                replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_FLEXPAY, "");
            }
            if (null != cardItem.getPricingAndInventory()) {
                BigDecimal offerPrice = cardItem.getPricingAndInventory().getOfferPrice();
                if (replacedTridionString.contains(WCSKEY_PRICE_PER_DAY)) {
                    String bestOfferLabel = cardItem.hasBestOffer() ? getBannerLabel(cardItem) : "";
                    int numDays = cardItem.getNumDays();
                    String pricePerDay = IceTicketUtils.getTridionConfig().getFormattedPrice(
                            offerPrice.divide(BigDecimal.valueOf(numDays), BigDecimal.ROUND_CEILING));
                    String bannerLabelSecondary = getBannerLabelSecondarySpec();

                    String bannerWcsKeyString = String.format(Locale.US, "%s %s %s", bestOfferLabel,
                            pricePerDay, bannerLabelSecondary);
                    replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_PRICE_PER_DAY, bannerWcsKeyString);
                }
                if (replacedTridionString.contains(WCSKEY_TICKET_DATE)) {
                    // For CommerceCardItem's, get the date from the offer pricing and inventory key
                    String ticketDate = getDateValueFromFormat(cardItem.getPricingAndInventory().getOfferDateString(),
                            SERVICE_PRICING_DATE_FORMAT, PRESENTATION_DATE_FORMAT);
                    replacedTridionString = StringUtils.replace(replacedTridionString,
                            WCSKEY_TICKET_DATE, ticketDate);
                }
            }
            List<CommerceAttribute> attributes = new ArrayList<>();
            attributes.addAll(cardItem.getAttributes());
            attributes.addAll(cardItem.getComponentAttributes());
            replacedTridionString = replaceWcsKeys(replacedTridionString, attributes);
        }

        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, WalletEntitlement walletEntitlement) {
        String replacedTridionString = tridionString;
        if (null != walletEntitlement) {
            TridionLabelSpec tcmId2TridionSpec = TridionLabelSpecManager.getSpecForId(walletEntitlement.getTcmId2());

            replacedTridionString = StringUtils.replace(replacedTridionString, SELECTED_SKU_TITLE, tcmId2TridionSpec.getItemName());
            replacedTridionString = replaceWcsKeys(replacedTridionString, walletEntitlement.getAttributes());
        }

        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @Nullable
    private String replaceWcsKeys(String tridionString, List<CommerceAttribute> attributes) {
        String replacedTridionString = tridionString;
        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_USES_ALLOWED, getPurchaseOptionAlternativeTitle());
        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_OPTION, getPurchaseOptionTitle());
        if (null != attributes) {
            for (CommerceAttribute attribute : attributes) {
                if (null != attribute) {
                    if (attribute.isParkNum()) {
                        String numParks = attribute.getValue();
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_NUM_PARKS, numParks);
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_WCSKEYUUPARKS, numParks);
                    } else if (attribute.isNumDays()) {
                        String numDays = attribute.getValue();
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_DAYS, numDays);
                    } else if (attribute.isPark()) {
                        String parkName = attribute.getValue();
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_PARK_NAME, parkName);
                    } else if (attribute.isDate()) {
                        String ticketDate = getDateValueFromFormat(attribute.getValue(),
                                SERVICE_PRICING_DATE_FORMAT, PRESENTATION_DATE_FORMAT);
                        String ticketTime = getDateValueFromFormat(attribute.getValue(),
                                SERVICE_PRICING_DATE_FORMAT, PRESENTATION_TIME_FORMAT);
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_DATE, ticketDate);
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_TIME, ticketTime);
                    } else if (attribute.isPoo()) {
                        String poo = attribute.isFloridaPoo() ? IceTicketUtils.getTridionConfig().getTicketsFloridaResidentLabel() : "";
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_POINT_OF_ORIGIN, poo);
                    } else if (attribute.isAge()) {
                        String age = attribute.getValue();
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_AGE, age);
                    } else if (attribute.isRow()) {
                        String row = attribute.getValue();
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEYS_ROWS, row);
                    } else if (attribute.isSeatLow()) {
                        // This one is special. The low AND high seat are needed, but we don't want
                        // to replace the WCSKEY unless the low seat is in the attributes since
                        // attributes can come from multiple places
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_SEATS, getSeatReplacer(attributes));
                    }  else if (attribute.isMonthsNum()) {
                        String numMonths = attribute.getValue();
                        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_MONTHS, numMonths);
                    }
                }
            }
        }


        return replacedTridionString;
    }

    @NonNull
    private String replaceWcsKeys(String tridionString, WalletEntitlementAttributes attributes) {
        String replacedTridionString = tridionString;
        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_USES_ALLOWED, getPurchaseOptionAlternativeTitle());
        replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_OPTION, getPurchaseOptionTitle());
        if (null != attributes) {
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_NUM_PARKS, attributes.getParkNum());
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_DAYS,
                    String.format(Locale.US, "%d", attributes.getDays()));
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_PARK_NAME, attributes.getParkName());
            String ticketDate = getFormattedDate(attributes.getValidDate(),
                    PRESENTATION_DATE_FORMAT);
            String ticketTime = getFormattedDate(attributes.getValidDate(),
                    PRESENTATION_TIME_FORMAT);
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_DATE, ticketDate);
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_TIME, ticketTime);
            String poo = attributes.isFloridaPoo() ? IceTicketUtils.getTridionConfig().getTicketsFloridaResidentLabel() : "";
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_POINT_OF_ORIGIN, poo);
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_TICKET_AGE, attributes.getAge());
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEYS_ROWS, attributes.getRow());
            replacedTridionString = StringUtils.replace(replacedTridionString, WCSKEY_SEATS, attributes.getSeats());
        }
        if (null == replacedTridionString) {
            replacedTridionString = "";
        }
        return replacedTridionString;
    }

    @NonNull
    private String getSeatReplacer(List<CommerceAttribute> attributes) {
        String seatLow = null;
        String seatHigh = null;
        if (null != attributes) {
            for (CommerceAttribute attribute : attributes) {
                if (attribute.isSeatLow()) {
                    seatLow = attribute.getValue();
                }
                if (attribute.isSeatHigh()) {
                    seatHigh = attribute.getValue();
                }
            }
        }
        if (seatLow != null && seatHigh != null) {
            if (seatLow.equals(seatHigh)) {
                return seatLow;
            } else {
                return seatLow + "-" + seatHigh;
            }
        }
        return "";
    }

    /**
     * Returns the ($x.xx per day/ticket/etc) string for a ticket
     * This is used for Shopping Cart Pages only
     * @param ticket the ticket
     * @return the formatted price per unit string
     */
    public String getPricePerUnitPrimaryString(Ticket ticket) {
        String priceString = null;
        if (null != ticket) {
            String unit = getPriceTextBelowPrimary();
            if (TextUtils.isEmpty(unit)) {
                unit = IceTicketUtils.getTridionConfig().getPerTicketLabel();
            }
            String price = IceTicketUtils.getTridionConfig().getFormattedPrice(ticket.getUnitPrice());
            priceString = String.format(Locale.US, "(%s %s)", price, unit);
        } else {
            priceString = "";
        }

        return priceString;
    }

    /**
     * Returns the ($x.xx per day/ticket/etc) string for a ticket
     * This is used for Shopping Cart Pages only
     * @param ticket the ticket
     * @return the formatted price per unit string
     */
    public String getPricePerUnitSecondaryString(Ticket ticket) {
        String priceString = null;
        if (null != ticket) {
            String unit = getPriceTextBelowSecondary();
            if (TextUtils.isEmpty(unit)) {
                unit = IceTicketUtils.getTridionConfig().getPerTicketLabel();
            }
            String price = IceTicketUtils.getTridionConfig().getFormattedPrice(ticket.getUnitPrice());
            priceString = String.format(Locale.US, "(%s %s)", price, unit);
        } else {
            priceString = "";
        }

        return priceString;
    }

    /**
     * Returns the ($x.xx per day/ticket/etc) string for an express pass
     * This is used for Shopping Cart Pages only
     * @param expressPassTicketGroups the express pass
     * @return the formatted price per unit string
     */
    public String getPricePerUnitPrimaryString(ExpressPassTicketGroups expressPassTicketGroups) {
        String priceString = null;
        if (null != expressPassTicketGroups) {
            String unit = getPriceTextBelowPrimary();
            if (TextUtils.isEmpty(unit)) {
                unit = IceTicketUtils.getTridionConfig().getPerTicketLabel();
            }
            String price = IceTicketUtils.getTridionConfig().getFormattedPrice(expressPassTicketGroups.getUnitPrice());
            priceString = String.format(Locale.US, "(%s %s)", price, unit);
        } else {
            priceString = "";
        }

        return priceString;
    }

    /**
     * Returns the ($x.xx per day/ticket/etc) string for an express pass
     * This is used for Shopping Cart Pages only
     * @param expressPassTicketGroups the express pass
     * @return the formatted price per unit string
     */
    public String getPricePerUnitSecondaryString(ExpressPassTicketGroups expressPassTicketGroups) {
        String priceString = null;
        if (null != expressPassTicketGroups) {
            String unit = getPriceTextBelowSecondary();
            if (TextUtils.isEmpty(unit)) {
                unit = IceTicketUtils.getTridionConfig().getPerTicketLabel();
            }
            String price = IceTicketUtils.getTridionConfig().getFormattedPrice(expressPassTicketGroups.getUnitPrice());
            priceString = String.format(Locale.US, "(%s %s)", price, unit);
        } else {
            priceString = "";
        }

        return priceString;
    }

    public String getCommerceCardDetailsHtmlString(CommerceGroup group) {
        String htmlString = "";
        if (null != group && !group.getCardItems().isEmpty()) {
            CommerceCardItem cardItem = group.getCardItems().get(0);
            if (null != cardItem) {
                TridionLabelSpec tcmId2Spec = TridionLabelSpecManager.getSpecForId(cardItem.getDetailIdForTridion());
                String details = tcmId2Spec.getDetails();
                String commonDetailsRtfDesc = tcmId2Spec.getCommonDetailsRTFDesc();
                String restrictions = tcmId2Spec.getRestrictions();
                String optionsRtfDesc = tcmId2Spec.getOptionsRTFDesc(group, cardItem);
                String primaryHtml = String.format(Locale.US, "%s %s %s %s", details, commonDetailsRtfDesc, restrictions, optionsRtfDesc);

                String topLine = getTypeAlternativeHeaderLine1(cardItem);
                String secondLine = getTypeAlternativeHeaderLine2(cardItem);
                String thirdLine = getTypeAlternativeHeaderLine3(cardItem);
                String topFont = "17sp";
                String middleFont = "16sp";
                String bottomFont = "16sp";

                String fontName = UniversalOrlandoApplication.getAppContext().getString(R.string.font_gotham_medium);

                StringBuilder sb = new StringBuilder();
                sb.append("<html><head><style type=\"text/css\">")
                        .append("\nbody { font-family: '")
                        .append("file:///android_asset/")
                        .append(fontName)
                        .append("'; }</style></head><body><center><span style=\"font-weight: bold; font-size: ")
                        .append(topFont)
                        .append("\">")
                        .append(topLine)
                        .append("</span><br /><span style=\"font-size: ")
                        .append(middleFont)
                        .append("\">")
                        .append(secondLine)
                        .append("</span><br /><span style=\"font-size: ")
                        .append(bottomFont)
                        .append("\">")
                        .append(thirdLine)
                        .append("</span></center><br /><br />")
                        .append(primaryHtml)
                        .append("</body></html>");

                htmlString = sb.toString();

            }
        }

        return htmlString;
    }

    /**
     * This is a string replace method that will return an empty string ONLY if the
     * item that needs replacing is empty or null
     * @param label
     * @param replacerText
     * @param replacementText
     * @return
     */
    public String replace(String label, String replacerText, String replacementText) {
        if (null == label) {
            return "";
        }

        if(null == replacerText) {
            replacerText = "";
        }

        if(null == replacementText){
            replacementText = "";
        }

        return label.replace(replacerText, replacementText);
    }

    @NonNull
    private String getDateValueFromFormat(String date, SimpleDateFormat currentFormat,  SimpleDateFormat formatToReturn) {
        if (null == date ) {
            return "";
        }

        Date formattedDate = null;
        try {
            formattedDate = currentFormat.parse(date);
        } catch (ParseException ex) {

        }

        return getFormattedDate(formattedDate, formatToReturn);
    }

    private String getFormattedDate(Date formattedDate, SimpleDateFormat formatToReturn) {
        if (null != formattedDate) {
            return formatToReturn.format(formattedDate);
        } else {
            return "";
        }
    }
}
