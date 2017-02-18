package com.universalstudios.orlandoresort.model.security;

import android.util.Base64;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

import java.io.UnsupportedEncodingException;

/**
 * Util class to obfuscate and encrypt data.
 *
 * @author Steven Byle
 */
public class SecurityUtils {
    private static final String TAG = SecurityUtils.class.getSimpleName();
    private static final int BASE_64_FLAGS = Base64.NO_PADDING | Base64.NO_WRAP;

    /**
     * Use this method to obfuscate strings, and use the log output or return value to see the
     * result. This is not a secure way of hiding information, just a way of obscuring it.
     *
     * @param stringToObfuscate
     *
     * @return an obfuscated string
     */
    public static String obfuscateString(String stringToObfuscate) {
        String obfuscatedString = null;
        try {
            byte[] stringBytes = stringToObfuscate.getBytes("UTF-8");
            obfuscatedString = Base64.encodeToString(stringBytes, BASE_64_FLAGS);
        } catch (UnsupportedEncodingException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Exception while obfuscating", e);
            }
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "stringToObfuscate = " + stringToObfuscate);
            Log.i(TAG, "obfuscatedString = " + obfuscatedString);
        }
        return obfuscatedString;
    }

    public static String deobfuscateString(String stringToDeobfuscate) {
        String deobfuscatedString = new String(Base64.decode(stringToDeobfuscate, BASE_64_FLAGS));

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "stringToDeobfuscate = " + stringToDeobfuscate);
            Log.i(TAG, "deobfuscatedString = " + deobfuscatedString);
        }
        return deobfuscatedString;
    }
}
