package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jack Hughes on 9/27/16.
 */
public class WalletEntitlementAttributes extends GsonObject {

    // Logging Tag
    private static final String TAG = WalletEntitlementAttributes.class.getSimpleName();

    public static final String AGE_ADULT = "Adult";
    private static final String POO_FLORIDA = "Florida";

    @SerializedName("parkNum")
    private String parkNum;

    @SerializedName("parkName")
    private String parkName;

    @SerializedName("validDate")
    private String validDate;

    @SerializedName("useageAllowed")
    private String usageAllowed;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("usedAttractions")
    private ArrayList<String> usedAttractions = new ArrayList<>();

    @SerializedName("unUsedAttraction")
    private ArrayList<String> unusedAttractions = new ArrayList<>();

    @SerializedName("poo")
    private String poo;

    @SerializedName("age")
    private String age;

    @SerializedName("days")
    private Integer days;

    @SerializedName("daysRemaining")
    private Integer daysRemaining;

    @SerializedName("expirationDate")
    private String expirationDate;

    @SerializedName("invSeatNumLow")
    private String seatLow;

    @SerializedName("invSeatNumHigh")
    private String seatHigh;

    @SerializedName("invRow")
    private String row;

    private static SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    private static SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    private static SimpleDateFormat DATE_FORMAT3 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static SimpleDateFormat DATE_FORMAT4 = new SimpleDateFormat("MMM dd yyyy", Locale.US);

    private static SimpleDateFormat[] DATE_FORMATS = new SimpleDateFormat[] {
            DATE_FORMAT1, DATE_FORMAT2, DATE_FORMAT3, DATE_FORMAT4
    };

    /**
     * Try to get the {@link Date} object from the model's formatted date Strings.
     *
     * @param dateString
     *         the String to convert to a {@link Date} object
     *
     * @return the {@link Date} object from the formatted date String
     */
    private Date getFormattedDate(String dateString) {
        Date date = null;
        if (!TextUtils.isEmpty(dateString)) {
            for (SimpleDateFormat sdf : DATE_FORMATS) {
                try {
                    date = sdf.parse(dateString);
                    break;
                } catch (ParseException e) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "getFormattedDate: ", e);
                    }
                }
            }
        }
        return date;
    }

    /**
     * Method to get the {@link Date} valid date for the entitlement. Note this is different from
     * {@link #getValidDateString()} which returns the formatted date String.
     *
     * @return a {@link Date} object representing the entitlement's valid date
     */
    public Date getValidDate() {
        return getFormattedDate(this.validDate);
    }

    /**
     * Method to get the formatted date String of the entitlement's valid date. Note this is
     * different from {@link #getValidDate()} which returns the {@link Date} object.
     *
     * @return a formatted date String representing the entitlement's valid date
     */
    public String getValidDateString() {
        return validDate;
    }

    /**
     * Method to get the entitlement's park number. (i.e "3")
     *
     * @return a String containing the integer representing the entitlement's park number
     */
    public String getParkNum() {
        return parkNum;
    }

    /**
     * Method to get the name of the park for the entitlement. (i.e "Universal Studios Florida +
     * Universal's Island of Adventure + Universal's Volcano Bay")
     *
     * @return a String containing the entitlement's park name.
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * Method to get the usage allowed for the entitlement. (i.e "multipleUse")
     *
     * @return a String containing the entitlement's usage allowed.
     */
    public String getUsageAllowed() {
        return usageAllowed;
    }

    /**
     * Method to get the {@link Date} for the  last updated date for the entitlement. Note this is
     * different from {@link #getLastUpdatedString()} which returns the formatted date String.
     *
     * @return a {@link Date} object representing the entitlement's last updated date
     */
    public Date getLastUpdated() {
        return getFormattedDate(this.lastUpdated);
    }

    /**
     * Method to get the formatted date String of the entitlement's last updated date. Note this is
     * different from {@link #getLastUpdated()} which returns the {@link Date} object.
     *
     * @return a formatted date String representing the entitlement's valid date
     */
    public String getLastUpdatedString() {
        return lastUpdated;
    }

    /**
     * Method to get the list of used attractions for the entitlement.
     *
     * @return an {@link ArrayList<String>} representing a list of used attractions for the
     * entitlement
     */
    public ArrayList<String> getUsedAttractions() {
        return usedAttractions;
    }

    /**
     * Method to get the list of unused attractions for the entitlement.
     *
     * @return an {@link ArrayList<String>} representing a list of unused attractions for the
     * entitlement
     */
    public ArrayList<String> getUnusedAttractions() {
        return unusedAttractions;
    }

    private String getSeatLow() {
        return seatLow;
    }

    private String getSeatHigh() {
        return seatHigh;
    }

    public String getRow() {
        return row;
    }

    public String getPoo() {
        return poo;
    }

    public String getAge() {
        return age;
    }

    public Integer getDays() {
        return days;
    }

    public Integer getDaysRemaining() {
        return daysRemaining;
    }

    public boolean isFloridaPoo() {
        return POO_FLORIDA.equalsIgnoreCase(getPoo());
    }

    public String getExpirationDateString() {
        return expirationDate;
    }

    public Date getExpirationDate() {
        return getFormattedDate(expirationDate);
    }

    public String getSeats() {
        String seatLow = getSeatLow();
        String seatHigh = getSeatHigh();
        if (seatLow != null && seatHigh != null) {
            if (seatLow.equals(seatHigh)) {
                return seatLow;
            } else {
                return seatLow + "-" + seatHigh;
            }
        }
        return "";
    }

}
