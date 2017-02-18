package com.universalstudios.orlandoresort.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Commonly used constants
 *
 * @author acampbell
 */
public final class CommerceTypeConstants {

    private CommerceTypeConstants() {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({AGE_TYPE_ADULT, AGE_TYPE_CHILD, AGE_TYPE_GENERAL})
    public @interface AgeType {
    }

    public static final int AGE_TYPE_ADULT = 1;
    public static final int AGE_TYPE_CHILD = 2;
    public static final int AGE_TYPE_GENERAL = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TICKET_TYPE_TICKET, TICKET_TYPE_EXPRESS, TICKET_TYPE_EXTRA, TICKET_TYPE_ANNUAL_PASS})
    public @interface TicketType {
    }

    public static final int TICKET_TYPE_TICKET = 1;
    public static final int TICKET_TYPE_EXPRESS = 2;
    public static final int TICKET_TYPE_EXTRA = 3;
    public static final int TICKET_TYPE_ANNUAL_PASS = 4;
}
