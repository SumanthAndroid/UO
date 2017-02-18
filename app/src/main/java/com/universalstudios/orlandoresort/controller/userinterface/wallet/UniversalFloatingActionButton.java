package com.universalstudios.orlandoresort.controller.userinterface.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;

/**
 *
 * Created by Jason.Andujar && Nikhil on 1/20/2017.
 * Resource type suppression of styleable
 */
public class UniversalFloatingActionButton extends FloatingActionButton implements View.OnClickListener {

    private enum UniversalFloatingActionButtonType {
        WALLET,
        LOCATION
    }

    final String TAG = getClass().getSimpleName();
    private UniversalFloatingActionButtonType mType;

    public UniversalFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UniversalFloatingActionButton, 0, 0);
        String iconKey = typedArray.getString(R.styleable.UniversalFloatingActionButton_ufab_icon);
        setUpButton(context, iconKey);
        typedArray.recycle();
    }

    public UniversalFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UniversalFloatingActionButton, 0, 0);
        String iconKey = typedArray.getString(R.styleable.UniversalFloatingActionButton_ufab_icon);
        setUpButton(context, iconKey);
        typedArray.recycle();
    }


    public void setUpButton(Context context, String iconKey) {
        setImageDrawable(getIcon(iconKey));
        setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue_color)));
        setClickable(true);
        setFocusable(false);
        setScaleType(ScaleType.CENTER);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //handling location buttons in the layout itself, not here
        if(mType == UniversalFloatingActionButtonType.WALLET) {
            getContext().startActivity(new Intent(getContext(), WalletActivity.class));
        }
    }

    /**
     * getting the icon for the FAB
     * @param iconKey look for (or create) key_ufab_your_icon_name in string resources
     * @return a icon as a drawable based on the param iconKey
     */
    private Drawable getIcon(@NonNull String iconKey) {
        Resources resources = getResources();
        String wallet = resources.getString(R.string.key_ufab_wallet);
        String location = resources.getString(R.string.key_ufab_location);
        Drawable walletDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bitmap_ic_menu_wallet_white);
        Drawable locationDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_map_locate_me);
        //returns wallet by default
        mType = UniversalFloatingActionButtonType.WALLET;
        //can still be null with annotation
        if(iconKey == null) {
            return walletDrawable;
        } else if(iconKey.equals(wallet)) {
            return walletDrawable;
        } else if(iconKey.equals(location)) {
            mType = UniversalFloatingActionButtonType.LOCATION;
            return locationDrawable;
        } else {
            //just in case this attribute is forgotten, fall back on the wallet as default
            return walletDrawable;
        }
    }
}
