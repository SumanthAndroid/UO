package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util;

import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Provides utility methods for transforming Strings to number formats and has the potential
 * for methods which transform number format objects to Strings.
 *
 * Created by Tyler Ritchie on 10/13/16.
 */

public class NumberUtils {

    /**
     * Converts the given String to an Integer.  Returns
     * null if the passed String couldn't be parsed to an Integer
     * (if it throws NumberFormatException)
     *
     * @param num The String to convert
     * @return The resulting Integer, or null if the String
     *  couldn't be converted
     */
    public static Integer toInteger(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Converts the given String to an Double.  Returns
     * null if the passed String couldn't be parsed to an Double
     * (if it throws NumberFormatException)
     *
     * @param num The String to convert
     * @return The resulting Double, or null if the String
     *  couldn't be converted
     */
    public static Double toDouble(String num) {
        try {
            return Double.parseDouble(num);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Converts the given String to a BigDecimal  Returns
     * null if the passed String couldn't be parsed to an Double
     * (if it throws NumberFormatException)
     *
     * @param num The String to convert
     * @return The resulting BigDecimal, or null if the String
     * couldn't be converted
     */
    @Nullable
    public static BigDecimal toBigDecimal(String num) {
        try {
            return new BigDecimal(num);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converts the given String to a BigInteger  Returns
     * null if the passed String couldn't be parsed to an Double
     * (if it throws NumberFormatException or NullPointerException)
     *
     * @param num The String to convert
     * @return The resulting BigInteger, or null if the String
     * couldn't be converted
     */
    @Nullable
    public static BigInteger toBigInteger(String num) {
        try {
            return new BigInteger(num);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Determines if the decimal is a solid integer with no decimal places.
     *
     * @param bigDecimal
     *
     * @return If the decimal is an integer
     */
    public static boolean isIntegerValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return false;
        }
        // If 0, or does not have a positive scale (# of decimal places), then it is a solid integer
        else {
            return (bigDecimal.signum() == 0 || bigDecimal.scale() <= 0 || bigDecimal.stripTrailingZeros().scale() <= 0);
        }
    }
}
