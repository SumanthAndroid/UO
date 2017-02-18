package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupOrder;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * This class models the messages coming back on particular orders and order items. It's used in
 * classes {@link OrderItem} and {@link TicketGroupOrder}.
 * This class also holds constants for commerce error messages for determining specific business
 * logic.
 * <p>
 * Created by Jack Hughes on 10/6/16.
 */
@Parcel
public class CommerceMessage extends GsonObject {

    /**
     * Error Keys - These key's will also act as the white-list for error messages. If the {@link
     * #key} is not one of these values, then the message should not be show.
     */
    public static final String KEY_ERR_INCOMPATIBLE_ITEM = "_ERR_INCOMPATIBLE_ITEM";
    public static final String KEY_ERR_COMPANION_ITEM = "_ERR_COMPANION_ITEM";
    public static final String KEY_ERR_INVALID_TICKET_DATE = "_ERR_INVALID_TICKET_DATE";
    public static final String KEY_ERR_INV_ALLOCATION = "_ERR_INV_ALLOCATION";
    public static final String KEY_ERR_MISSING_BILLING_ADDRESS = "_ERR_MISSING_BILLING_ADDRESS";
    public static final String KEY_ERR_SOAP_SERVICE_COMMUNICATION_ERROR = "_ERR_SOAP_SERVICE_COMMUNICATION_ERROR";
    public static final String KEY_ERR_SOAP_SERVICE_ERROR_RESPONSE = "_ERR_SOAP_SERVICE_ERROR_RESPONSE";
    public static final String KEY_ERR_SOAP_SERVICE_ERROR_BUILD_REQUEST = "_ERR_SOAP_SERVICE_ERROR_BUILD_REQUEST";
    public static final String KEY_ERR_SOAP_SERVICE_ERROR_PROCESS_RESPONSE = "_ERR_SOAP_SERVICE_ERROR_PROCESS_RESPONSE";
    public static final String KEY_ERR_SOAP_SERVICE_EMPTY_RESPONSE = "_ERR_SOAP_SERVICE_EMPTY_RESPONSE";
    public static final String KEY_ERR_PAYMENT_VALIDATION_NOT_SUCCESSFUL = "_ERR_PAYMENT_VALIDATION_NOT_SUCCESSFUL";
    public static final String KEY_ERR_FRAUD_RESPONSE_INVALID = "_ERR_FRAUD_RESPONSE_INVALID";
    public static final String KEY_ERR_PAYMENT_SESSION_CREATE_FAILED = "_ERR_PAYMENT_SESSION_CREATE_FAILED";
    public static final String KEY_ERR_NO_ISO_ALPHA_3_COUNTRY_CODE = "_ERR_NO_ISO_ALPHA_3_COUNTRY_CODE";
    public static final String KEY_ERR_NOT_FL_ZIPCODE = "_ERR_NOT_FL_ZIPCODE";
    public static final String KEY_ERR_NO_PENDING_ORDER = "_ERR_NO_PENDING_ORDER";
    public static final String KEY_ERR_ZIPCODE_NULL = "_ERR_ZIPCODE_NULL";
    public static final String KEY_ERR_ZIPCODE_LIST_NULL = "_ERR_ZIPCODE_LIST_NULL";
    public static final String KEY_ERR_CATENTRY_MAX_QTY_EXCEEDED = "_ERR_CATENTRY_MAX_QTY_EXCEEDED";
    public static final String KEY_ERR_ITEM_TYPE_MAX_QTY_EXCEEDED = "_ERR_ITEM_TYPE_MAX_QTY_EXCEEDED";
    public static final String KEY_ERR_ORDER_REQUEST_NULL = "_ERR_ORDER_REQUEST_NULL";
    public static final String KEY_ERR_UNIVERSAL_ORDER_NOT_FOUND = "_ERR_UNIVERSAL_ORDER_NOT_FOUND";
    public static final String KEY_ERR_ORDER_ITEM_NOT_BUYABLE = "_ERR_ORDER_ITEM_NOT_BUYABLE";
    public static final String KEY_ERR_UEP_NUMPARKS_MISMATCH = "_ERR_UEP_NUMPARKS_MISMATCH";
    public static final String KEY_ERR_UEP_NUMDAYS_MISMATCH = "_ERR_UEP_NUMDAYS_MISMATCH";
    public static final String KEY_ERR_UEP_QTY_MISMATCH = "_ERR_UEP_QTY_MISMATCH";
    public static final String KEY_ERR_EVENTID_NOT_FOUND = "_ERR_EVENTID_NOT_FOUND";
    public static final String KEY_ERR_INVENTORY_HOLD_NOT_SUCCESSFULL = "_ERR_INVENTORY_HOLD_NOT_SUCCESSFULL";
    public static final String KEY_ERR_INVENTORY_MISSING_DATA = "_ERR_INVENTORY_MISSING_DATA";
    public static final String KEY_ERR_INVENTORY_RESPONSE_INVALID = "_ERR_INVENTORY_RESPONSE_INVALID";
    public static final String KEY_ERR_BAD_RESPONSE_FROM_EXTERNAL_SYSTEM = "_ERR_BAD_RESPONSE_FROM_EXTERNAL_SYSTEM";


    public static final String[] ERROR_KEYS = new String[]{
            KEY_ERR_INCOMPATIBLE_ITEM,
            KEY_ERR_COMPANION_ITEM,
            KEY_ERR_INVALID_TICKET_DATE,
            KEY_ERR_INV_ALLOCATION,
            KEY_ERR_MISSING_BILLING_ADDRESS,
            KEY_ERR_SOAP_SERVICE_COMMUNICATION_ERROR,
            KEY_ERR_SOAP_SERVICE_ERROR_RESPONSE,
            KEY_ERR_SOAP_SERVICE_ERROR_BUILD_REQUEST,
            KEY_ERR_SOAP_SERVICE_ERROR_PROCESS_RESPONSE,
            KEY_ERR_SOAP_SERVICE_EMPTY_RESPONSE,
            KEY_ERR_PAYMENT_VALIDATION_NOT_SUCCESSFUL,
            KEY_ERR_FRAUD_RESPONSE_INVALID,
            KEY_ERR_PAYMENT_SESSION_CREATE_FAILED,
            KEY_ERR_NO_ISO_ALPHA_3_COUNTRY_CODE,
            KEY_ERR_NOT_FL_ZIPCODE,
            KEY_ERR_NO_PENDING_ORDER,
            KEY_ERR_ZIPCODE_NULL,
            KEY_ERR_ZIPCODE_LIST_NULL,
            KEY_ERR_CATENTRY_MAX_QTY_EXCEEDED,
            KEY_ERR_ITEM_TYPE_MAX_QTY_EXCEEDED,
            KEY_ERR_ORDER_REQUEST_NULL,
            KEY_ERR_UNIVERSAL_ORDER_NOT_FOUND,
            KEY_ERR_ORDER_ITEM_NOT_BUYABLE,
            KEY_ERR_UEP_NUMPARKS_MISMATCH,
            KEY_ERR_UEP_NUMDAYS_MISMATCH,
            KEY_ERR_UEP_QTY_MISMATCH,
            KEY_ERR_EVENTID_NOT_FOUND,
            KEY_ERR_INVENTORY_HOLD_NOT_SUCCESSFULL,
            KEY_ERR_INVENTORY_MISSING_DATA,
            KEY_ERR_INVENTORY_RESPONSE_INVALID,
            KEY_ERR_BAD_RESPONSE_FROM_EXTERNAL_SYSTEM
    };

    public static final String[] INVENTORY_ERROR_KEYS = new String[]{
            KEY_ERR_EVENTID_NOT_FOUND,
            KEY_ERR_INVALID_TICKET_DATE,
            KEY_ERR_INV_ALLOCATION,
            KEY_ERR_INVENTORY_HOLD_NOT_SUCCESSFULL,
            KEY_ERR_INVENTORY_MISSING_DATA,
            KEY_ERR_INVENTORY_RESPONSE_INVALID,
            KEY_ERR_BAD_RESPONSE_FROM_EXTERNAL_SYSTEM
    };

    @SerializedName("params")
    ArrayList<String> params;

    @SerializedName("message")
    String message;

    @SerializedName("severity")
    String severity;

    @SerializedName("key")
    String key;

    /**
     * Private helper method to prevent null Strings being consumed.
     *
     * @param input
     *         the String we want to null check
     *
     * @return the original String if it's not empty, or an empty string if it's null. If {@link
     * BuildConfig#DEBUG} is enabled, then this will return a "MISSING!!!" message
     */
    private String getNullSafeString(String input) {
        if (TextUtils.isEmpty(input)) {
            if (BuildConfig.DEBUG) {
                return "MISSING!!!";
            }
            else {
                return "";
            }
        }
        return input;
    }

    /**
     * Method to get the array list of params. This will tend to be the items mentioned/used in the
     * commerce message itself. For example, if the message is "No billing address was found for
     * user {0}", then the "{0}" portion would be included in the {@link ArrayList<String>} of
     * parameters.
     *
     * @return the {@link ArrayList<String>} of parameters used in the commerce message
     */
    public ArrayList<String> getParams() {
        return params;
    }

    /**
     * Method to get the commerce message's text. (i.e. "No billing addresswas found for user {0}")
     *
     * @return a String containing the commerce message's text
     */
    @NonNull
    public String getMessage() {
        return getNullSafeString(message);
    }

    /**
     * Method to get the severity of the commerce message. (i.e. "ERROR")
     *
     * @return a String containing the severity of the commerce message
     */
    @NonNull
    public String getSeverity() {
        return getNullSafeString(severity);
    }

    /**
     * Method to get the error key of the commerce message. (i.e. "_ERR_INVALID_TICKET_DATE")
     *
     * @return a String containing the error key of the commerce message
     */
    @NonNull
    public String getKey() {
        return getNullSafeString(key);
    }
}
