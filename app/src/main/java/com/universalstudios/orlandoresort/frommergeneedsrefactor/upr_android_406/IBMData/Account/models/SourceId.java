package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tyler Ritchie on 11/22/16.
 */

public class SourceId {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ACCOUNT_CREATION,
            ASSIGN_ENTITLEMENTS,
            PAYMENT_BILLING_INFO,
            GUEST_CHECKOUT,
            PURCHASE_CONFIRMATION,
            EDIT_COMMUNICATIONS,
            EDIT_CONTACT_INFO,
            EDIT_PERSONAL_INFO,
            EDIT_ADDRESS,
            MODIFY_CARD,
            MODIFY_SPENDING_LIMIT,
            MODIFY_PIN,
            LEGACY_SOURCE_ID
    })
    public @interface SourceIdType {}
    // Declare the constants
    public static final String ACCOUNT_CREATION = "1006001";
    public static final String ASSIGN_ENTITLEMENTS = "1006002";
    public static final String PAYMENT_BILLING_INFO = "1006003";
    public static final String GUEST_CHECKOUT = "1006004";
    public static final String PURCHASE_CONFIRMATION = "1006005";
    public static final String EDIT_COMMUNICATIONS = "1006006";
    public static final String EDIT_CONTACT_INFO = "1006007";
    public static final String EDIT_PERSONAL_INFO = "1006008";
    public static final String EDIT_ADDRESS = "1006009";
    public static final String MODIFY_CARD = "1006011";
    public static final String MODIFY_SPENDING_LIMIT = "1006012";
    public static final String MODIFY_PIN = "1006013";
    public static final String LEGACY_SOURCE_ID = "1000004";

}
