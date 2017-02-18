package com.universalstudios.orlandoresort.model.network.domain.wallet;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Nicholas Hanna on 1/13/2017.
 */
@Parcel
public class WalletTravelPartyMember extends GsonObject {

    private static String SPENDING_LIMIT_MAX = "99999999999999";

    @SerializedName("sequenceId")
    String sequenceId;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("suffix")
    String suffix;

    @SerializedName("spentAmount")
    BigDecimal spentAmount;

    @SerializedName("spendingLimit")
    BigInteger spendingLimit;

    @SerializedName("isUnlimited")
    Boolean isUnlimited;

    @SerializedName("isNearLimit")
    Boolean isNearLimit;


    public String getSequenceId() {
        return sequenceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public BigInteger getSpendingLimit() {
        return spendingLimit;
    }

    public Boolean getUnlimited() {
        // TODO: Service does not actually return this value at the moment so setting limit to max value means unlimited
        if (spendingLimit != null) {
            BigInteger max = new BigInteger(SPENDING_LIMIT_MAX);
            if (spendingLimit.compareTo(max) >= 0) {
                return true;
            }
        }
        return isUnlimited;
    }

    public Boolean getNearLimit() {
        return isNearLimit;
    }

    @NonNull
    public static BigInteger getMaxLimit() {
        return new BigInteger(SPENDING_LIMIT_MAX);
    }
}
