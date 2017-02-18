package com.universalstudios.orlandoresort.controller.userinterface.link;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

public class ClickableTextHelper extends ClickableSpan {
    public final String selectedText;
    private int lkColor;
    private ClickListener listener;
    private boolean underline = false;

    public ClickableTextHelper(String selectedText, ClickListener listener) {
        this.selectedText = selectedText;
        this.listener = listener;
    }

    public void setTextColor(int color) {
        lkColor = UniversalOrlandoApplication.getAppContext().getResources().getColor(color);
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    @Override
    public void onClick(View v) {
        if (null != listener) {
            listener.onLinkClicked();
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.linkColor = lkColor;
        ds.setTypeface(Typeface.createFromAsset(
                UniversalOrlandoApplication.getAppContext().getAssets(), "fonts/Gotham-Medium.otf"));
        super.updateDrawState(ds);
        ds.setUnderlineText(underline);
    }

    public static void setFontLink(SpannableString spannableString, ClickableTextHelper clickableSpan) {
        int start = spannableString.toString().indexOf(clickableSpan.selectedText);
        int end = start + clickableSpan.selectedText.length();
        if (start > -1) {
            spannableString.setSpan(clickableSpan, start, end, 0);
        }
    }

    public static SpannableString createEmbeddedSpannable(String wholeString, String linkSubstring,
                                       int textColor, boolean underline, ClickListener listener) {
        SpannableString text = new SpannableString(wholeString);

        ClickableTextHelper spanHelper = new ClickableTextHelper(linkSubstring, listener);
        spanHelper.setTextColor(textColor);
        spanHelper.setUnderline(underline);
        setFontLink(text, spanHelper);
        return text;
    }

    public static SpannableString createEmbeddedSpannable(SpannableString wholeString, String linkSubstring,
                                                          int textColor, boolean underline, ClickListener listener) {

        ClickableTextHelper spanHelper = new ClickableTextHelper(linkSubstring, listener);
        spanHelper.setTextColor(textColor);
        spanHelper.setUnderline(underline);
        setFontLink(wholeString, spanHelper);
        return wholeString;
    }
}