package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets;

import android.graphics.Color;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * Color utility class for handling various operations with colors.
 *
 * Created by Tyler Ritchie on 11/30/16.
 */

public class ColorUtils {
    private static final String TAG = ColorUtils.class.getSimpleName();

    /**
     * Attempts to parse the passed color hex String into a color value.  If that
     * parsing fails, the passed default color resource ID is evaluated and the color value is returned.
     * Ultimately returns the color value.
     *
     * @param colorHexValue The hex code for the desired color
     * @param defaultColor The default color to use if the colorHexValue parameter String
     *                          cannot be parsed
     * @throws IllegalArgumentException if the passed in colorHexValue cannot be parsed
     * @return The color value sourced from the passed colorHexValue, or sourced from the defaultColorResId
     *              if the hex value could not be parsed
     */
    public static int parseColor(String colorHexValue, String defaultColor) {
        int colorValue;
        try {
            // Attempt to parse the passed color hex value
            colorValue = Color.parseColor(colorHexValue);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Parsing of color failed, using default color");
            }
            // Parsing failed, fall back on the passed default color resource ID
            try {
                colorValue = Color.parseColor(defaultColor);
            } catch (Exception exception) {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Parsing of default color failed :/");
                }
                // Someone did something really dumb, so just set it to white
                colorValue = Color.WHITE;
            }
        }
        return colorValue;
    }
}
