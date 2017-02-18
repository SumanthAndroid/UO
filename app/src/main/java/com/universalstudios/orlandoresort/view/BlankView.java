package com.universalstudios.orlandoresort.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author acampbell
 *
 */
public class BlankView extends View {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BlankView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BlankView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlankView(Context context) {
        super(context);
    }

}
