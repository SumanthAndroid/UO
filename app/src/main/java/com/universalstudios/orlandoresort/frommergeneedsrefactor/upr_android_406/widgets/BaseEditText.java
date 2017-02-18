package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.widgets;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by IBM_ADMIN on 5/26/2016.
 */
public abstract class BaseEditText extends EditText {

    protected Handler mHandler = new Handler();
    protected static final int WAITING_TIME = 5 * 1000;

    private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            BaseEditText.this.setError(null);
        }
    };

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            BaseEditText.this.setError(null);
            return false;
        }
    };

    protected void postMessage(Runnable runnable) {
        mHandler.postDelayed(runnable, WAITING_TIME);
    }

    protected void cancelMessage(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    public BaseEditText(Context context) {
        super(context);
        setTextWatcher();
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextWatcher();
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextWatcher();
    }

    public abstract boolean isDataValid(String inputValue);

    protected void setTextWatcher() {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        this.setOnFocusChangeListener(focusChangeListener);
        this.setOnTouchListener(touchListener);
    }

}
