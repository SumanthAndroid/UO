package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlement;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementAttributes;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementDetails;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpec;
import com.universalstudios.orlandoresort.model.state.content.TridionLabelSpecManager;

import org.parceler.Parcel;

import java.util.Locale;

/**
 * @author tjudkins
 * @since 12/21/16
 */

@Parcel
public class WalletEntitlementModel extends BaseObservable {

    private static final String ENTITLEMENT_TYPE_EXPRESS_PASS = "UEP";
    private static final String ENTITLEMENT_TYPE_PARK_TICKET = "TICKET";
    private static final String ENTITLEMENT_TYPE_DINING_PLAN = "DINING_PLAN";
    private static final String ENTITLEMENT_TYPE_PHOTO = "PHOTO";
    private static final String ENTITLEMENT_TYPE_STROLLER = "STROLLER";
    private static final String ENTITLEMENT_TYPE_CW_PASS = "CW_PASS";
    private static final String ENTITLEMENT_TYPE_CUP = "CUP";
    private static final String ENTITLEMENT_TYPE_CW_MEAL = "CW_MEAL";
    private static final String ENTITLEMENT_TYPE_VIP = "VIP";
    private static final String ENTITLEMENT_TYPE_BMG = "BMG";
    private static final String ENTITLEMENT_ANNUAL_PASS = "ANNUALPASS";

    @Bindable
    @WalletEntitlement.EntitlementStatus int entitlementStatus;
    @Bindable
    String productName;
    @Bindable
    String itemType;
    @Bindable
    String description;
    @Bindable
    String tertiaryDescription;
    @Bindable
    String visualId;
    @Bindable
    String assignedFirstName;
    @Bindable
    String assignedLastName;
    @Bindable
    String assignedSuffix;
    @Bindable
    String restrictions;
    @Bindable
    String invalidReason;
    @Bindable
    String expirationDate;
    @Bindable
    String checkinLocation;
    @Bindable
    String wheelchairAccessible;
    @Bindable
    String orderedBy;
    @Bindable
    String confirmationNumber;
    @Bindable
    String creditCardNumber;
    @Bindable
    String purchasedDate;
    String tcmId1;
    String tcmId2;

    /**
     * Intentionally protected constructor
     */
    WalletEntitlementModel() {}

    public WalletEntitlementModel(@NonNull WalletEntitlementDetails entitlementDetails, @NonNull WalletEntitlement entitlement) {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
        setEntitlementStatus(entitlement.getEntitlementStatus());
        setProductName(entitlementDetails.getTicketName());
        setItemType(entitlementDetails.getItemType());
        setTcmId1(entitlement.getTcmId1());
        setTcmId2(entitlement.getTcmId2());
        setDescription(entitlement.getEntitlementDetailDescription());
        WalletEntitlementAttributes attributes = entitlement.getAttributes();
        if (null != attributes) {
            Integer daysRemaining = attributes.getDaysRemaining();
            if (daysRemaining != null && daysRemaining >= 0) {
                String daysRemainingText = daysRemaining == 1 ? tridionConfig.getOneDayRemainingLabel() :
                        String.format(Locale.US, "%d %s", daysRemaining, tridionConfig.getDaysRemainingLabel());
                setTertiaryDescription(daysRemainingText);
            }
        }
        setAssignedFirstName(entitlement.getEntitlementFirstName());
        setAssignedLastName(entitlement.getEntitlementLastName());
        setAssignedSuffix(entitlement.getEntitlementSuffix());
        setVisualId(entitlement.getVisualId());
        setRestrictions(tridionConfig.getFloridaResidentBlockoutDates());
        setInvalidReason(entitlement.getInvalidReason());
        setExpirationDate(entitlement.getExpirationDate());
        setCheckinLocation(entitlement.getCheckInLocation());
        setWheelchairAccessible(entitlement.getWheelchairAccessible());
        setOrderedBy(entitlement.getOrderedBy());
        setConfirmationNumber(entitlement.getConfirmation());
        setCreditCardNumber(entitlement.getCreditCardUsed());
        setPurchasedDate(entitlement.getPurchasedDate());
    }

    @Bindable
    public boolean isProcessing() {
        return getEntitlementStatus() == WalletEntitlement.PROCESSING;
    }

    public @WalletEntitlement.EntitlementStatus int getEntitlementStatus() {
        return entitlementStatus;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setEntitlementStatus(@WalletEntitlement.EntitlementStatus int entitlementStatus) {
        this.entitlementStatus = entitlementStatus;
    }

    public @DrawableRes int getIconResId() {
        @DrawableRes int iconResId;
        switch (getItemType().toUpperCase()) {
            case ENTITLEMENT_TYPE_VIP:
                iconResId = R.drawable.ic_wallet_vip;
                break;
            case ENTITLEMENT_TYPE_BMG:
                iconResId = R.drawable.ic_wallet_bmg;
                break;
            case ENTITLEMENT_TYPE_STROLLER:
                iconResId = R.drawable.ic_wallet_stroller;
                break;
            case ENTITLEMENT_TYPE_DINING_PLAN:
                iconResId = R.drawable.ic_wallet_dining;
                break;
            case ENTITLEMENT_ANNUAL_PASS:
                iconResId = R.drawable.ic_wallet_park_ticket;
                break;
            case ENTITLEMENT_TYPE_PARK_TICKET:
                iconResId = R.drawable.ic_wallet_park_ticket;
                break;
            case ENTITLEMENT_TYPE_EXPRESS_PASS:
                iconResId = R.drawable.ic_wallet_uep;
                break;
            case ENTITLEMENT_TYPE_CW_MEAL:
            case ENTITLEMENT_TYPE_CUP:
            case ENTITLEMENT_TYPE_CW_PASS:
            case ENTITLEMENT_TYPE_PHOTO:
            default:
                iconResId = R.drawable.ic_wallet_extras;
        }
        return iconResId;
    }

    public String getProductName() {
        return productName;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Intentionally private. Use getters that return data based on item type.
     */
    private String getItemType() {
        return itemType;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setDescription(String description) {
        this.description = description;
    }

    public String getTertiaryDescription() {
        return tertiaryDescription;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setTertiaryDescription(String tertiaryDescription) {
        this.tertiaryDescription = tertiaryDescription;
    }

    public String getVisualId() {
        return visualId;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setVisualId(String visualId) {
        this.visualId = visualId;
    }

    public String getAssignedFirstName() {
        return assignedFirstName;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setAssignedFirstName(String assignedFirstName) {
        this.assignedFirstName = assignedFirstName;
    }

    public String getAssignedLastName() {
        return assignedLastName;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setAssignedLastName(String assignedLastName) {
        this.assignedLastName = assignedLastName;
    }

    public String getAssignedSuffix() {
        return assignedSuffix;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setAssignedSuffix(String assignedSuffix) {
        this.assignedSuffix = assignedSuffix;
    }

    public String getName() {
        StringBuilder sb = new StringBuilder(getAssignedFirstName())
                .append(" ")
                .append(getAssignedLastName());

        if (!TextUtils.isEmpty(getAssignedSuffix())) {
            sb.append(", ")
                    .append(getAssignedSuffix());
        }
        return sb.toString();
    }

    public String getRestrictions() {
        return restrictions;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCheckinLocation() {
        return checkinLocation;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setCheckinLocation(String checkinLocation) {
        this.checkinLocation = checkinLocation;
    }

    public String getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setWheelchairAccessible(String wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public TridionLabelSpec getTcmId1Spec() {
        return TridionLabelSpecManager.getSpecForId(getTcmId1());
    }

    private String getTcmId1() {
        return tcmId1;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setTcmId1(String tcmId1) {
        this.tcmId1 = tcmId1;
    }

    public TridionLabelSpec getTcmId2Spec() {
        return TridionLabelSpecManager.getSpecForId(getTcmId2());
    }

    private String getTcmId2() {
        return tcmId2;
    }

    /**
     * Intentionally private. Set by the constructor only.
     */
    private void setTcmId2(String tcmId2) {
        this.tcmId2 = tcmId2;
    }

}
