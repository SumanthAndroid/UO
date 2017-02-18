package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/1/16.
 * Class: CommerceAttribute
 * Class Description: Attributes model
 */
@Parcel
public class CommerceAttribute extends GsonObject{
    public static final String TAG = "CommerceAttribute";

    public static final int DEFAULT_MAX_QUANTITY = Integer.MAX_VALUE;
    public static final int DEFAULT_MIN_QUANTITY = 0;

    private static final String NAME_TCMID1= "TCMID1";
    private static final String NAME_TCMID2= "TCMID2";
    private static final String NAME_DATE = "DATE";
    private static final String NAME_INV_EVENT_ID = "INV_EVENT_ID";
    private static final String NAME_INV_RESOURCE_ID = "INV_RESOURCE_ID";
    private static final String NAME_ITEM_TYPE = "ITEM_TYPE";
    private static final String NAME_AGE = "AGE";
    private static final String VALUE_AGE_ADULT = "Adult";
    private static final String VALUE_AGE_CHILD = "Child";
    private static final String NAME_ROW = "INV_ROW";
    private static final String NAME_SEAT_HIGH = "INV_SEAT_NUM_HIGH";
    private static final String NAME_SEAT_LOW = "INV_SEAT_NUM_LOW";
    private static final String NAME_PARK_NUM = "PARK_NUM";
    private static final String NAME_MONTHS = "MONTHS";
    private static final String NAME_ASSIGNMENT_FIRSTNAME = "PARTY_MEMBER_FNAME";
    private static final String NAME_ASSIGNMENT_LASTNAME = "PARTY_MEMBER_LNAME";
    private static final String NAME_ASSIGNMENT_SUFFIX = "PARTY_MEMBER_SUFFIX";
    private static final String NAME_ASSIGNMENT_MEMBER_ID = "PARTY_MEMBER_ID";
    private static final String NAME_PARTY_MEMBER_BIRTHDATE = "PARTY_MEMBER_BIRTHDATE";
    private static final String NAME_MAX_QUANTITY = "MAX_QUANTITY";
    private static final String NAME_MIN_QUANTITY = "MIN_QUANTITY";
    private static final String NAME_PARK = "PARK";
    private static final String NAME_POO = "POO";
    private static final String VALUE_FLORIDA = "Florida";
    private static final String NAME_NUM_DAYS = "DAYS";
    private static final String NAME_NUM_PARKS = "PARK_NUM";
    private static final String NAME_BEST_VALUE = "BEST_VALUE";
    private static final String VALUE_BEST_VALUE_YES = "YES";
    private static final String NAME_SC_WIDGET = "SC_WIDGET";
    private static final String VALUE_SC_WIDGET_QUANTITY_CONTROL = "QUANTITY_CONTROL";
    private static final String NAME_CARD = "CARD";
    private static final String NAME_FLEXPAY = "FLEXPAY";
    private static final String VALUE_FLEX_PAY = "Y";
    private static final String NAME_FLEXPAY_DOWNPAYMENT = "FLEXPAY_DOWNPAYMENT";
    private static final String NAME_FLEXPAY_FINANCED_AMOUNT = "FLEXPAY_FINANCED_AMOUNT";
    private static final String NAME_FLEXPAY_RECURRING_COUNT = "FLEXPAY_RECURRINGPAY_COUNT";
    private static final String NAME_FLEXYPAY_RECURRINGPAY_AMOUNT = "FLEXPAY_RECURRINGPAY_AMOUNT";
    private static final String VALUE_ITEM_TYPE_ANNUAL_PASS = "ANNUAL-PASS";
    private static final String VALUE_ITEM_TYPE_PASS = "PASS";
    private static final String VALUE_ITEM_TYPE_TICKET = "TICKET";
    private static final String NAME_SHOW_SAVINGS_MESSAGE = "SAVINGS_MESSAGE";
    private static final String VALUE_SHOW_SAVINGS_MESSAGE_YES_RESPONSE = "YES";


    private static final String DATE_FORMAT_NAME_DATE = "yyyy-MM-dd HH:mm:ss";

    @SerializedName("value")
    String value;

    @SerializedName("name")
    String name;

    @SerializedName("partNumber")
    String partNumber;

    /**
     * This constructor is intentionally private. Use the appropriate getter or static
     * create methods to access CommerceAttribute.
     */
    @ParcelConstructor
    private CommerceAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * DO NOT USE FOR NEW DEVELOPMENT. This is a temporary setter so the Assign Names screen
     * works without refactoring.
     */
    @Deprecated
    public void setValue(String value) {
        this.value = value;
    }

    public static CommerceAttribute createDateAttribute(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_NAME_DATE, Locale.US);
        return new CommerceAttribute(NAME_DATE, dateFormat.format(date));
    }

    public static CommerceAttribute createDateAttribute(Date date, String partNumber) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_NAME_DATE, Locale.US);
        CommerceAttribute attribute = new CommerceAttribute(NAME_DATE, dateFormat.format(date));
        attribute.partNumber = partNumber;
        return attribute;
    }

    public static CommerceAttribute createInvEventIdAttribute(String eventId) {
        return new CommerceAttribute(NAME_INV_EVENT_ID, eventId);
    }

    public static CommerceAttribute createInvResourceIdAttribute(String eventId) {
        return new CommerceAttribute(NAME_INV_RESOURCE_ID, eventId);
    }

    public static CommerceAttribute createAssignmentFirstNameAttribute(String firstName) {
        return new CommerceAttribute(NAME_ASSIGNMENT_FIRSTNAME, firstName);
    }

    public static CommerceAttribute createAssignmentLastNameAttribute(String lastName) {
        return new CommerceAttribute(NAME_ASSIGNMENT_LASTNAME, lastName);
    }

    public static CommerceAttribute createAssignmentSuffixAttribute(String suffix) {
        return new CommerceAttribute(NAME_ASSIGNMENT_SUFFIX, suffix);
    }

    public static CommerceAttribute createAssignmentMemberIdAttribute(String memberId) {
        return new CommerceAttribute(NAME_ASSIGNMENT_MEMBER_ID, memberId);
    }

    public static CommerceAttribute createCardIdAttribute(String id) {
        return new CommerceAttribute(NAME_CARD, id);
    }

    public static CommerceAttribute createPartyMemberBirthdateAttribute(String dob) {
        return new CommerceAttribute(NAME_PARTY_MEMBER_BIRTHDATE, dob);
    }

    public static CommerceAttribute createFlexPayAttribute() {
        return new CommerceAttribute(NAME_FLEXPAY, VALUE_FLEX_PAY);
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    // Helper methods

    public boolean isTcmId1() {
        return NAME_TCMID1.equalsIgnoreCase(name);
    }

    public boolean isTcmId2() {
        return NAME_TCMID2.equalsIgnoreCase(name);
    }

    public boolean isAge() {
        return NAME_AGE.equalsIgnoreCase(name);
    }

    public boolean isAgeAdult() {
        return isAge() && VALUE_AGE_ADULT.equalsIgnoreCase(value);
    }

    public boolean isAgeChild() {
        return isAge() && VALUE_AGE_CHILD.equalsIgnoreCase(value);
    }

    public boolean isDate() {
        return NAME_DATE.equalsIgnoreCase(name);
    }

    public boolean isRow() {
        return NAME_ROW.equalsIgnoreCase(name);
    }

    public boolean isSeatLow() {
        return NAME_SEAT_LOW.equalsIgnoreCase(name);
    }

    public boolean isSeatHigh() {
        return NAME_SEAT_HIGH.equalsIgnoreCase(name);
    }

    public boolean isParkNum() {
        return NAME_PARK_NUM.equalsIgnoreCase(name);
    }

    public boolean isMonthsNum() {
        return NAME_MONTHS.equalsIgnoreCase(name);
    }

    public boolean isAssignmentFirstName() {
        return NAME_ASSIGNMENT_FIRSTNAME.equalsIgnoreCase(name);
    }

    public boolean isAssignmentLastName() {
        return NAME_ASSIGNMENT_LASTNAME.equalsIgnoreCase(name);
    }

    public boolean isAssignmentSuffix() {
        return NAME_ASSIGNMENT_SUFFIX.equalsIgnoreCase(name);
    }

    public boolean isAssignmentMemberId() {
        return NAME_ASSIGNMENT_MEMBER_ID.equalsIgnoreCase(name);
    }

    @Nullable
    public Date getDate() {
        if (!isDate()) {
            return null;
        }

        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_NAME_DATE, Locale.US);
        try {
            date = dateFormat.parse(value);
        } catch (ParseException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Error parsing date", e);
            }
        }

        return date;
    }

    public boolean isMaxQuantity() {
        return NAME_MAX_QUANTITY.equalsIgnoreCase(name);
    }

    public boolean isMinQuantity() {
        return NAME_MIN_QUANTITY.equalsIgnoreCase(name);
    }

    public int getMaxQuantity() {
        int max = Integer.MAX_VALUE;
        if (isMaxQuantity()) {
            max = Integer.parseInt(value);
        }
        return max;
    }

    public int getMinQuantity() {
        int min = CommerceAttribute.DEFAULT_MIN_QUANTITY;
        if (isMinQuantity()) {
            min = Integer.parseInt(value);
        }
        return min;
    }

    public boolean isPark() {
        return NAME_PARK.equalsIgnoreCase(name);
    }

    public boolean isItemType() {
        return NAME_ITEM_TYPE.equalsIgnoreCase(name);
    }

    public boolean isPoo() {
        return NAME_POO.equals(name);
    }

    public boolean isFloridaPoo() {
        return isPoo() && VALUE_FLORIDA.equalsIgnoreCase(value);
    }

    public boolean isNumDays() {
        return NAME_NUM_DAYS.equalsIgnoreCase(name);
    }

    public boolean isNumParks() {
        return NAME_NUM_PARKS.equalsIgnoreCase(name);
    }

    public boolean isInvEventId() {
        return NAME_INV_EVENT_ID.equalsIgnoreCase(name);
    }

    public boolean isInvResourceId() {
        return NAME_INV_RESOURCE_ID.equalsIgnoreCase(name);
    }

    public boolean isBestValue() {
        return NAME_BEST_VALUE.equalsIgnoreCase(name);
    }

    public boolean isBestValueYes() {
        return (isBestValue() && VALUE_BEST_VALUE_YES.equalsIgnoreCase(value));
    }

    public boolean isQuantityChangeAllowed() {
        return NAME_SC_WIDGET.equalsIgnoreCase(name)
                && VALUE_SC_WIDGET_QUANTITY_CONTROL.equalsIgnoreCase(value);
    }

    public boolean isFlexPay() {
        return NAME_FLEXPAY.equalsIgnoreCase(name) && VALUE_FLEX_PAY.equalsIgnoreCase(value);
    }

    public boolean isFlexPayDownPayment() {
        return NAME_FLEXPAY_DOWNPAYMENT.equalsIgnoreCase(name);
    }

    public boolean isFlexPayFinancedAmount() {
        return NAME_FLEXPAY_FINANCED_AMOUNT.equalsIgnoreCase(name);
    }

    public boolean isFlexPayRecurringCount() {
        return NAME_FLEXPAY_RECURRING_COUNT.equalsIgnoreCase(name);
    }

    public boolean isFlexPayRecurringPayAmount() {
        return NAME_FLEXYPAY_RECURRINGPAY_AMOUNT.equalsIgnoreCase(name);
    }

    public boolean isItemTypeAnnualPass() {
        if (getValue() != null) {
            return getValue().equalsIgnoreCase(VALUE_ITEM_TYPE_PASS) || getValue().equalsIgnoreCase(VALUE_ITEM_TYPE_ANNUAL_PASS);
        } else {
            return false;
        }
    }

    public boolean isPartyMemberBirthDate() {
        return NAME_PARTY_MEMBER_BIRTHDATE.equalsIgnoreCase(name);
    }

    public boolean isItemTypeTicket() {
        return isItemType() && VALUE_ITEM_TYPE_TICKET.equalsIgnoreCase(value);
    }

    public boolean shouldShowSavingsMessage() {
        return NAME_SHOW_SAVINGS_MESSAGE.equalsIgnoreCase(name)
                && VALUE_SHOW_SAVINGS_MESSAGE_YES_RESPONSE.equalsIgnoreCase(value);
    }

}
