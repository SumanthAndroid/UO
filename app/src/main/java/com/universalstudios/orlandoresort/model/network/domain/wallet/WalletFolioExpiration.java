package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;

import org.parceler.Parcel;

/**
 * @author acampbell
 */
@Parcel
public class WalletFolioExpiration {

    @SerializedName("month")
    String month;

    @SerializedName("year")
    String year;

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public Integer getMonthInt() {
        try {
            return Integer.parseInt(month);
        } catch (NumberFormatException e) {
            CrashAnalyticsUtils.logHandledException(e);
            return null;
        }
    }

    public Integer getYearInt() {
        try {
            return Integer.parseInt(year);
        } catch (NumberFormatException e) {
            CrashAnalyticsUtils.logHandledException(e);
            return null;
        }
    }
}
