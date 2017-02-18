package com.universalstudios.orlandoresort.view.binding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.view.fonts.EditText;
import com.universalstudios.orlandoresort.view.fonts.FontManager;
import com.universalstudios.orlandoresort.view.fonts.RadioButton;
import com.universalstudios.orlandoresort.view.textview.ZipCodeEditText;

/**
 * Custom bindings for XML attributes using data binding.
 * (http://developer.android.com/tools/data-binding/guide.html)
 */
public class Bindings {

    @BindingAdapter({"font"})
    public static void setFont(TextView textView, @StringRes int resId) {
        String fontName = textView.getResources().getString(resId);
        if (!TextUtils.isEmpty(fontName)) {
            setFont(textView, fontName);
        }
    }

    @BindingAdapter({"font"})
    public static void setFont(RadioButton radioButton, @StringRes int resId) {
        String fontName = radioButton.getResources().getString(resId);
        if (!TextUtils.isEmpty(fontName)) {
            setFont(radioButton, fontName);
        }
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView textView, String fontName) {
        FontManager.getInstance().setFont(textView, fontName);
    }

    @BindingAdapter({"font"})
    public static void setFont(TabLayout tabLayout, @StringRes int resId) {
        String fontName = tabLayout.getResources().getString(resId);
        if (!TextUtils.isEmpty(fontName)) {
            setFont(tabLayout, fontName);
        }
    }

    @BindingAdapter({"font", "selectedFont"})
    public static void setFont(TabLayout tabLayout, @StringRes int defaultFontResId, @StringRes int selectedFontResId) {
        String fontName = tabLayout.getResources().getString(defaultFontResId);
        String selectedFontName = tabLayout.getResources().getString(selectedFontResId);
        if (!TextUtils.isEmpty(fontName)) {
            if (!TextUtils.isEmpty(selectedFontName)) {
                setFont(tabLayout, fontName, selectedFontName);
            } else {
                setFont(tabLayout, fontName);
            }
        }
    }

    @BindingAdapter({"font"})
    public static void setFont(TabLayout tabLayout, String fontName) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView)tabViewChild).setAllCaps(false);
                    FontManager.getInstance().setFont((TextView) tabViewChild, fontName);
                }
            }
        }
    }

    @BindingAdapter({"font", "selectedFont"})
    public static void setFont(TabLayout tabLayout, final String fontName, final String selectedFontName) {
        setFont(tabLayout, fontName);
        final ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabsCount = vg.getChildCount();
                for (int j = 0; j < tabsCount; j++) {
                    ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
                    int tabChildsCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildsCount; i++) {
                        View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof TextView) {
                            ((TextView) tabViewChild).setAllCaps(false);
                            if (j == tab.getPosition()) {
                                FontManager.getInstance().setFont((TextView) tabViewChild, selectedFontName);
                            } else {
                                FontManager.getInstance().setFont((TextView) tabViewChild, fontName);
                            }
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView view, @DrawableRes int resId) {
        view.setImageDrawable(view.getContext().getResources().getDrawable(resId));
    }

    @BindingAdapter("onFocusChange")
    public static void setOnLayoutChangeListener(EditText editText, View.OnFocusChangeListener focusChangeListener) {
        editText.setOnFocusChangeListener(focusChangeListener);
    }

    @BindingAdapter("onFocusChange")
    public static void setOnLayoutChangeListener(ZipCodeEditText editText, View.OnFocusChangeListener focusChangeListener) {
        editText.setOnFocusChangeListener(focusChangeListener);
    }

    @BindingAdapter("htmlText")
    public static void setText(TextView textView, String text) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(Html.fromHtml(text));
        } else {
            textView.setText(text);
        }
    }

    @BindingAdapter("progressColor")
    public static void setColor(ProgressBar progressBar, @ColorInt int color) {
        Drawable drawable = progressBar.getProgressDrawable();
        if (drawable != null && drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            Drawable layer = layerDrawable.findDrawableByLayerId(android.R.id.progress);
            if (layer != null) {
                layer.setColorFilter(color, Mode.SRC_IN);
            }
        }
    }

    @BindingConversion
    public static int convertToViewVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
}