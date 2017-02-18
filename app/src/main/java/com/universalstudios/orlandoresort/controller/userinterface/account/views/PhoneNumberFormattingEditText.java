package com.universalstudios.orlandoresort.controller.userinterface.account.views;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/3/16.
 * Class: PhoneNumberFormattingEditText
 * Class Description: Custom EditText that follows Universal's phone number auto formatting
 */
public class PhoneNumberFormattingEditText extends EditText {
    public static final String TAG = "PhoneNumberFormattingEditText";

    public PhoneNumberFormattingEditText(Context context) {
        super(context);
        init();
    }

    public PhoneNumberFormattingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneNumberFormattingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setSingleLine();
        this.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.addTextChangedListener(mFormatTextWatcher);
        int maxLength = 15;
        this.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
    }
    
    private TextWatcher mFormatTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = s.toString().trim().replaceAll("[^\\d.]", "");

            //Do nothing to empty String
            if (TextUtils.isEmpty(phone)) {
                return;
            }
            removeTextChangedListener(this);

            //No formatting after 10 characters
            if (phone.length() >= 11) {
                setText(phone);
                setSelection(phone.length());
                addTextChangedListener(this);
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            if (phone.length() > 3) {
                sb.append("(")
                        .append(phone.substring(0, 3))
                        .append(") ");
                if (phone.length() > 6) {
                    sb.append(phone.substring(3, 6))
                            .append("-");
                    sb.append(phone.substring(6, phone.length()));
                } else {
                    sb.append(phone.substring(3, phone.length()));
                }
            } else {
                sb.append(phone);
            }

            setText(sb.toString());
            setSelection(sb.length());
            addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
