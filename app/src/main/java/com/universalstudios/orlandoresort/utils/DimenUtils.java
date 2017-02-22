package com.universalstudios.orlandoresort.utils;


import android.content.Context;
import android.util.DisplayMetrics;

public class DimenUtils {
    /**
     * GConverts Dp to Px
     *
     * @param context
     * @param dp
     * @return Returns Pixels corresponding to the given Dp.
     */
    public static int getPxFromDp(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Converts Px to Dp
     *
     * @param context
     * @param px
     * @return Returns Dp corresponding to the given Px.
     */
    public static int getDpFromPx(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
