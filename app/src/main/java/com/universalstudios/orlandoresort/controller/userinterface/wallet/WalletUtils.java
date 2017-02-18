package com.universalstudios.orlandoresort.controller.userinterface.wallet;

/**
 * Created by sbyle on 1/31/17.
 */

public class WalletUtils {

    public static boolean isWalletFolioPinCodeValid(String pinCode) {
        // Must have a length of 4
        if (pinCode == null || pinCode.length() != 4) {
            return false;
        }

        // Must be all digits
        for (int i = 0; i < pinCode.length(); i++) {
            if (!Character.isDigit(pinCode.charAt(i))) {
                return false;
            }
        }

        // Must not be ascending or descending
        boolean isAscending = true;
        boolean isDescending = true;
        for (int i = 0; i < pinCode.length() - 1; i++) {
            int curDigitIndex = i;
            int nextDigitIndex = curDigitIndex + 1;

            int curDigit = Integer.parseInt(pinCode.substring(curDigitIndex, curDigitIndex + 1));
            int nextDigit = Integer.parseInt(pinCode.substring(nextDigitIndex, nextDigitIndex + 1));
            if (curDigit + 1 != nextDigit) {
                isAscending = false;
            }
            if (curDigit - 1 != nextDigit) {
                isDescending = false;
            }
        }
        if (isAscending || isDescending) {
            return false;
        }

        // Must not be all same digit
        boolean isAllSameDigit = true;
        for (int i = 0; i < pinCode.length() - 1; i++) {
            int curDigitIndex = i;
            int nextDigitIndex = curDigitIndex + 1;

            String curDigit = pinCode.substring(curDigitIndex, curDigitIndex + 1);
            String nextDigit = pinCode.substring(nextDigitIndex, nextDigitIndex + 1);
            if (!curDigit.equals(nextDigit)) {
                isAllSameDigit = false;
                break;
            }
        }
        if (isAllSameDigit) {
            return false;
        }

        // Otherwise, consider the PIN valid
        return true;
    }
}
