package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.BR;

import java.util.Calendar;

/**
 * Created by tjudkins on 9/23/16.
 */

public class CreditCardInfo extends BaseObservable {
    private static final int NUM_YEARS = 10;

    public static String[] EXPIRATION_MONTHS = new String[12];
    public static String[] EXPIRATION_YEARS = new String[NUM_YEARS];

    @Bindable
    private String creditCardNumber;

    @Bindable
    private int expirationMonth;

    @Bindable
    private int expirationYear;

    @Bindable
    private String securityCode;

    @Bindable
    private boolean primary;

    static {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        for (int i = 0; i < NUM_YEARS; i++) {
            EXPIRATION_YEARS[i] = Integer.toString(year);
            year++;
        }
        for (int i = 0; i < EXPIRATION_MONTHS.length; i++) {
            EXPIRATION_MONTHS[i] = String.format("%02d", i + 1);
        }

    }

    public CreditCardInfo() {
        expirationMonth = -1;
        expirationYear = -1;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        notifyPropertyChanged(BR.creditCardNumber);
    }

    public int getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth = expirationMonth;
        notifyPropertyChanged(BR.expirationMonth);
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
        notifyPropertyChanged(BR.expirationYear);
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
        notifyPropertyChanged(BR.securityCode);
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
        notifyPropertyChanged(BR.primary);
    }

    /**
     * Checks that all of the values of credit card info are complete and valid
     *
     * @return true if all are valid
     */
    public boolean areAllFieldsValid() {
        return !TextUtils.isEmpty(creditCardNumber) && !TextUtils.isEmpty(securityCode)
                && expirationMonth >= 0 && expirationYear >= 0;
    }

}
