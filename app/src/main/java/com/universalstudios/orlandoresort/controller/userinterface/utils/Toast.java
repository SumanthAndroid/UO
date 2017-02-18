package com.universalstudios.orlandoresort.controller.userinterface.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.databinding.CustomToastBinding;
import com.universalstudios.orlandoresort.view.fonts.FontManager;

/**
 * A custom UO toast. Cheers!
 * @author tjudkins
 * @since 12/19/16
 */
public class Toast extends android.widget.Toast {

    private final CustomToastBinding mBinding;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public Toast(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.custom_toast, null, false);
        setView(mBinding.getRoot());
        setGravity(Gravity.FILL, 0, 0);
        setTextBold(false);
        mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.this.cancel();
            }
        });
    }

    /**
     * Make a custom toast that just contains a text view with icon.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);

        result.mBinding.text.setText(text);
        result.setDuration(duration);

        return result;
    }

    /**
     * Make a custom toast that just contains a text view with icon with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }


    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     * @param resId The new text for the Toast.
     */
    @Override
    public void setText(@StringRes int resId) {
        if (resId != -1) {
            mBinding.text.setText(resId);
        }
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     * @param s The new text for the Toast.
     */
    @Override
    public void setText(CharSequence s) {
        mBinding.text.setText(s);
    }

    /**
     * Update the icon in a Toast that was previously created using one of the makeText() methods.
     * @param resId The new icon for the Toast.
     */
    public Toast setIcon(@DrawableRes int resId) {
        if (resId != -1) {
            mBinding.icon.setImageResource(resId);
        }
        return this;
    }

    /**
     * Update the icon color in a Toast that was previously created using one of the makeText() methods.
     * @param color The new color for the icon.
     */
    public Toast setIconColor(int color) {
        mBinding.icon.setColorFilter(color);
        return this;
    }

    /**
     * Update if the text is bold in a Toast that was previously created using one of the makeText() methods.
     * @param isBold true if the text is bold for the Toast.
     */
    public Toast setTextBold(boolean isBold) {
        if (isBold) {
            FontManager.getInstance().setFont(mBinding.text, "fonts/Gotham-Bold.otf");
        } else {
            FontManager.getInstance().setFont(mBinding.text, "fonts/Gotham-Light.otf");
        }
        return this;
    }

}
