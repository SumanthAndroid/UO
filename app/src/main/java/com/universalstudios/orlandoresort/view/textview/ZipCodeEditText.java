package com.universalstudios.orlandoresort.view.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.universalstudios.orlandoresort.R;

/**
 * @author tjudkins
 * @since 9/29/16
 */

public class ZipCodeEditText extends EditText {

    public static final int MIN_CHARS_DOMESTIC = 5;
    public static final int MAX_CHARS_DOMESTIC = 10;
    public static final int MAX_CHARS_INTERNATIONAL = 12;
    public boolean domesticZip;

    public ZipCodeEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ZipCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZipCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZipCodeEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        setFilters(new InputFilter[]{
                InputFilters.allowedChars("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ -")
        });
        addTextChangedListener(textWatcher);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ZipCodeEditText,
                    0, 0);
            try {
                setDomesticZip(a.getBoolean(R.styleable.ZipCodeEditText_domesticZip, true));
            } finally {
                a.recycle();
            }
        }
    }

    public boolean getDomesticZip() {
        return domesticZip;
    }

    public void setDomesticZip(boolean domesticZip) {
        boolean changed = this.domesticZip != domesticZip;
        this.domesticZip = domesticZip;
        if (changed) {
            setText("");
        }
        if (domesticZip) {
            setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        } else {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        }
    }

    //Text watcher for zipCode
    TextWatcher textWatcher = new TextWatcher() {

        boolean mFormatting = false;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mFormatting) {
                mFormatting = true;
                if (domesticZip) {
                    if (s.length() > MAX_CHARS_DOMESTIC) {
                        setText(s.subSequence(0, MAX_CHARS_DOMESTIC));
                        setSelection(MAX_CHARS_DOMESTIC);
                    } else if (s.length() > 5) {
                        String zipString = formatZipString(s.toString());
                        setText(zipString);
                        setSelection(zipString.length());
                    }
                } else {
                    setError(null);
                    if (s.length() > MAX_CHARS_INTERNATIONAL) {
                        setText(s.subSequence(0, MAX_CHARS_INTERNATIONAL));
                        setSelection(MAX_CHARS_INTERNATIONAL);
                    }
                }
                mFormatting = false;
            }
        }

        private String formatZipString(CharSequence s) {
            String m = s.toString().replaceAll("[^0-9]", "");
            return m.length() > 5 ? m.substring(0, 5) + "-" + m.substring(5) : m;
        }

    };

}
