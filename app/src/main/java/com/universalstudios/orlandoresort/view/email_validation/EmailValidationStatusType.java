package com.universalstudios.orlandoresort.view.email_validation;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Reversible enum for checking email validation verbose outputs.
 *
 * Created by Tyler Ritchie on 10/6/16.
 */
public enum EmailValidationStatusType {
    VERIFIED("verified"),
    MAILBOX_DISABLED("mailboxDisabled"),
    MAILBOX_DNE("mailboxDoesNotExist"),
    MAILBOX_FULL("mailboxFull"),
    SYNTAX_FAILURE("syntaxFailure"),
    UNREACHABLE("unreachable"),
    UNRESOLVABLE("unresolvable"),
    ILLEGITIMATE("illegitimate"),
    ROLE_ACCOUNT("roleAccount"),
    TYPE_DOMAIN("typoDomain"),
    DISPOSABLE("disposable"),
    UNKNOWN("unknown"),
    TIMEOUT("timeout"),
    ACCEPT_ALL("acceptAll");

    private static final Map<String, EmailValidationStatusType> stringToEnumMap;
    private String keyString;

    static {
        stringToEnumMap = new HashMap<>();
        for (EmailValidationStatusType state : EnumSet.allOf(EmailValidationStatusType.class)) {
            stringToEnumMap.put(state.getKeyString(), state);
        }
    }

    EmailValidationStatusType(String keyString) {
        this.keyString = keyString;
    }

    public String getKeyString() {
        return keyString;
    }

    public static EmailValidationStatusType fromStatusString(String value) {
        EmailValidationStatusType ret = stringToEnumMap.get(value);
        if (ret == null) {
            throw new IllegalArgumentException();
        }
        return ret;
    }
}
