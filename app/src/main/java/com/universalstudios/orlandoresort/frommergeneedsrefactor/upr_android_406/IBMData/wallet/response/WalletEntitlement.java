package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response;

import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Jack Hughes on 9/27/16.
 */
@Parcel
public class WalletEntitlement extends GsonObject {

    // Logging Tag
    private static final String TAG = WalletEntitlement.class.getSimpleName();

    // Date Formatter for a date like 2016-09-26
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UNKNOWN, PROCESSING, WAITING_FOR_KIOSK, READY})
    public @interface EntitlementStatus {}

    public static final int UNKNOWN = -1;
    public static final int PROCESSING = 0;
    public static final int WAITING_FOR_KIOSK = 1;
    public static final int READY = 2;

    private static final String STATUS_PROCESSING = "PROCESSING";
    private static final String STATUS_KIOSK = "KIOSK";
    private static final String STATUS_AVAILABLE = "READY";


    @SerializedName("itemType")
    String itemType;

    @SerializedName("visualId")
    String visualId;

    @SerializedName("purchasedDate")
    String purchasedDate;

    @SerializedName("confirmation")
    String confirmation;

    @SerializedName("issuedDate")
    String issuedDate;

    @SerializedName("orderedBy")
    String orderedBy;

    @SerializedName("entitlementFirstName")
    String entitlementFirstName;

    @SerializedName("entitlementLastName")
    String entitlementLastName;

    @SerializedName("entitlementSuffix")
    String entitlementSuffix;

    @SerializedName("creditCardUsed")
    String creditCardUsed;

    @SerializedName("status")
    String status;

    @SerializedName("entitlementDetailDescription")
    String entitlementDetailDescription;

    @SerializedName("tcmId1")
    String tcmId1;

    @SerializedName("tcmId2")
    String tcmId2;

    @SerializedName("productDescription")
    String productDescription;

    @SerializedName("attributes")
    WalletEntitlementAttributes attributes;

    @SerializedName("ticketInvalidReason")
    String invalidReason;

    @SerializedName("expirationDate")
    String expirationDate;

    @SerializedName("checkInLocation")
    String checkInLocation;

    @SerializedName("wheelchairAccessible")
    String wheelchairAccessible;

    /**
     * Method to get the item type of the entitlement. (i.e "uep")
     *
     * @return a String containing the entitlement's item type
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Method to get the visual Id of the entitlement. (i.e "677651927108429128273")
     *
     * @return a String containing the entitlement's visual Id
     */
    public String getVisualId() {
        return visualId;
    }

    /**
     * Method to get the date String of the entitlement's purchase date.
     *
     * @return a date String representing the entitlement's purchase date
     */
    public String getPurchasedDate() {
        return purchasedDate;
    }

    /**
     * Method to get the confirmation code of the entitlement. (i.e "U3298402480")
     *
     * @return a String containing the entitlement's confirmation code
     */
    public String getConfirmation() {
        return confirmation;
    }

    /**
     * Method to get the date String of the entitlement's issued date.
     *
     * @return a date String representing the entitlement's issued date
     */
    public String getIssuedDate() {
        return issuedDate;
    }

    /**
     * Method to get the name of the person who ordered the tickets. (i.e "Mary Worthington")
     *
     * @return a String containing the ticket purchaser's name
     */
    public String getOrderedBy() {
        return orderedBy;
    }

    /**
     * Method to get the first name of the person to whom the entitlement belongs. (i.e "Mary")
     *
     * @return a String containing the first name of the entitlement's owner
     */
    public String getEntitlementFirstName() {
        return entitlementFirstName;
    }

    /**
     * Method to get the last name of the person to whom the entitlement belongs. (i.e
     * "Worthington")
     *
     * @return a String containing the last name of the entitlement's owner
     */
    public String getEntitlementLastName() {
        return entitlementLastName;
    }

    /**
     * Method to get the suffix of the person to whom the entitlement belongs. (i.e
     * "Jr.")
     *
     * @return a String containing the suffix of the entitlement's owner
     */
    public String getEntitlementSuffix() {
        return entitlementSuffix;
    }

    /**
     * Method to get the (obfuscated) account number of the purchasing credit card. (i.e
     * "XXXXXXXXXXXX1234")
     *
     * @return a String containing the (obfuscated) account number of the purchasing credit card
     */
    public String getCreditCardUsed() {
        return creditCardUsed;
    }

    /**
     * Method to get the status of the entitlement. (i.e. "Available")
     *
     * @return a String containing the status of the entitlement
     */
    public String getStatus() {
        return status;
    }

    /**
     * Method to get the second detail description of the entitlement.
     *
     * @return a String containing the second detail  description of the entitlement
     */
    public String getEntitlementDetailDescription() {
        return entitlementDetailDescription;
    }

    /**
     * Method to get the tcmid that gives info on the entitlement
     * @return
     */
    public String getTcmId1() {
        return tcmId1;
    }

    /**
     * Needed to show the details for the view details button on the product details page
     * @return
     */
    public String getTcmId2() {
        return tcmId2;
    }

    /**
     * Method to get the product description of the entitlement.
     *
     * @return a String containing the product description of the entitlement
     */
    public String getProductDescription() {
        return productDescription;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public String getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    /**
     * Method to get the entitlement attributes.
     *
     * @return a {@link WalletEntitlementAttributes} object representing the entitlement's
     * attributes
     */
    public WalletEntitlementAttributes getAttributes() {
        return attributes;
    }

    public @EntitlementStatus int getEntitlementStatus() {
        @EntitlementStatus int status;
        switch (getStatus().toUpperCase()) {
            case STATUS_AVAILABLE:
                status = READY;
                break;
            case STATUS_KIOSK:
                status = WAITING_FOR_KIOSK;
                break;
            case STATUS_PROCESSING:
                status = PROCESSING;
                break;
            default:
                status = UNKNOWN;
                break;
        }
        return status;
    }
}
