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
import android.view.View;
import android.widget.EditText;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;

/**
 * @author tjudkins
 * @since 9/29/16
 */

public class PhoneEditText extends EditText {

    public static final int MAX_CHARS_MINUS_FORMATTING = 10;
    public static final int MIN_CHARS_DOMESTIC = 14;
    public static final int MAX_CHARS_DOMESTIC = 14;
    public static final int MAX_CHARS_DOMESTIC_WITH_COUNTRY_CODE = 16;
    public static final int MAX_CHARS_INTERNATIONAL = 15;
    private static final String COUNTRY_CODE_NUM_PREFIX = "1";
    public boolean domesticPhone;

    public PhoneEditText(Context context) {
        super(context);
        init(context, null);
    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PhoneEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhoneEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        setFilters(new InputFilter[]{
                InputFilters.allowedChars("1234567890()- ")
        });
        setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    PhoneEditText editText = (PhoneEditText) view;
                    String text = editText.getText().toString();
                    if (domesticPhone && text.length() > 0 && text.length() < MIN_CHARS_DOMESTIC) {
                        setError(IceTicketUtils.getTridionConfig().getEr41());
                    } else {
                        setError(null);
                    }
                }
            }
        });
        addTextChangedListener(textWatcher);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PhoneEditText,
                    0, 0);
            try {
                setDomesticPhone(a.getBoolean(R.styleable.PhoneEditText_domesticPhone, true));
            } finally {
                a.recycle();
            }
        }
    }

    public boolean getDomesticPhone() {
        return domesticPhone;
    }

    public void setDomesticPhone(boolean domesticPhone) {
        boolean changed = this.domesticPhone != domesticPhone;
        this.domesticPhone = domesticPhone;
        if (changed) {
            setText("");
        }
    }

    //Text watcher for zipCode
    TextWatcher textWatcher = new TextWatcher() {

        boolean mFormatting = false;
        private boolean clearFlag;
        private String previousText;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            previousText = s.toString();
            if(domesticPhone) {
                if (after == 0 && s.toString().equals("1 ")) {
                    clearFlag = true;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mFormatting) {
                mFormatting = true;
                if (domesticPhone && s != null) {
                    int maxChars = s.toString().startsWith(COUNTRY_CODE_NUM_PREFIX) ? MAX_CHARS_DOMESTIC_WITH_COUNTRY_CODE : MAX_CHARS_DOMESTIC;
                    if (s.length() > maxChars) {
                        setText(previousText);
                        setSelection(maxChars);
                    } else {
                        String phoneString = formatUsNumber(s.toString());
                        setText(phoneString);
                        setSelection(phoneString.length());
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

        private String formatUsNumber(CharSequence text) {
            StringBuilder formattedString = new StringBuilder();
            // Now only digits are remaining
            String allDigitString = text.toString().replaceAll("[^0-9]", "");
            int totalDigitCount = allDigitString.length();

            if (totalDigitCount == 0 || totalDigitCount > MAX_CHARS_MINUS_FORMATTING) {
                return allDigitString;
            }
            int alreadyPlacedDigitCount = 0;
            // Only '1' is remaining and user pressed backspace and so we clear
            // the edit text.
            if (allDigitString.equals(COUNTRY_CODE_NUM_PREFIX) && clearFlag) {
                clearFlag = false;
                return "";
            }
            // The first 3 numbers beyond '1' must be enclosed in brackets "()"
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append("("
                        + allDigitString.substring(alreadyPlacedDigitCount,
                        alreadyPlacedDigitCount + 3) + ") ");
                alreadyPlacedDigitCount += 3;
            }
            // There must be a '-' inserted after the next 3 numbers
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append(allDigitString.substring(
                        alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3)
                        + "-");
                alreadyPlacedDigitCount += 3;
            }
            // All the required formatting is done so we'll just copy the
            // remaining digits.
            if (totalDigitCount > alreadyPlacedDigitCount) {
                formattedString.append(allDigitString
                        .substring(alreadyPlacedDigitCount));
            }

            return formattedString.toString();
        }

    };

}
