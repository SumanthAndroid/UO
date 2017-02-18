package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Reversible enum for keeping custom calendar cells' state.
 * <p>
 * Created by Tyler Ritchie on 9/21/16.
 */
public class EmailFrequencyType {
    private static final String EMAIL_FREQ_WEEKLY = "Weekly";
    private static final String EMAIL_FREQ_BI_WEEKLY = "Bi-Weekly";
    private static final String EMAIL_FREQ_MONTHLY = "Monthly";

    private static final int EMAIL_FREQ_WEEKLY_VALUE = 2;
    private static final int EMAIL_FREQ_BI_WEEKLY_VALUE = 1;
    private static final int EMAIL_FREQ_MONTHLY_VALUE = 0;

    private final static Map<String, Integer> VALUE_LOOKUP_MAP;
    private final static Map<Integer, String> STRING_LOOKUP_MAP;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(EMAIL_FREQ_WEEKLY, EMAIL_FREQ_WEEKLY_VALUE);
        map.put(EMAIL_FREQ_BI_WEEKLY, EMAIL_FREQ_BI_WEEKLY_VALUE);
        map.put(EMAIL_FREQ_MONTHLY, EMAIL_FREQ_MONTHLY_VALUE);
        VALUE_LOOKUP_MAP = Collections.unmodifiableMap(map);
    }

    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(EMAIL_FREQ_WEEKLY_VALUE, EMAIL_FREQ_WEEKLY);
        map.put(EMAIL_FREQ_BI_WEEKLY_VALUE, EMAIL_FREQ_BI_WEEKLY);
        map.put(EMAIL_FREQ_MONTHLY_VALUE, EMAIL_FREQ_MONTHLY);
        STRING_LOOKUP_MAP = Collections.unmodifiableMap(map);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({EMAIL_FREQ_WEEKLY, EMAIL_FREQ_BI_WEEKLY, EMAIL_FREQ_MONTHLY})
    public @interface StringValue {
    }


    public static int fromValue(@StringValue String value) {
        if (!TextUtils.isEmpty(value)) {
            return VALUE_LOOKUP_MAP.get(value);
        } else {
            return VALUE_LOOKUP_MAP.get(EMAIL_FREQ_WEEKLY);
        }
    }

    @StringValue
    public static String fromValue(int value) {
        //noinspection WrongConstant
        return  STRING_LOOKUP_MAP.get(value);
    }
}
