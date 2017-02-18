package com.universalstudios.orlandoresort.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.fonts.EditText;


/**
 * Created by rahul on 4/28/2016.
 */
public class TicketCounterView extends LinearLayout {

    /**
     * Binding adapter to use to set the value of {@link TicketCounterView} with data binding.
     * @param ticketCounterView the view
     * @param value the value to set
     */
    @BindingAdapter({"value"})
    public static void setValue(TicketCounterView ticketCounterView, int value) {
        ticketCounterView.setCurrentValue(value);
    }

    /**
     * Binding adapter to use to set the maximum of {@link TicketCounterView} with data binding.
     * @param ticketCounterView the view
     * @param max the maximum allowed value
     */
    @BindingAdapter({"max"})
    public static void setMax(TicketCounterView ticketCounterView, int max) {
        ticketCounterView.setMaximumValue(max);
    }

    /**
     * Binding adapter to use to set the minimum of {@link TicketCounterView} with data binding.
     * @param ticketCounterView the view
     * @param min the minimum allowed value
     */
    @BindingAdapter({"min"})
    public static void setMin(TicketCounterView ticketCounterView, int min) {
        ticketCounterView.setMinimumValue(min);
    }

    /**
     * Binding adapter used to callback when the count is incremented.
     * @param ticketCounterView the view
     * @param listener the {@link OnTicketCounterIncremented} listener
     */
    @BindingAdapter({"onIncremented"})
    public static void setOnIncremented(TicketCounterView ticketCounterView, OnTicketCounterIncremented listener) {
        ticketCounterView.setOnIncrementedListener(listener);
    }

    /**
     * Binding adapter used to callback when the count is decremented.
     * @param ticketCounterView the view
     * @param listener the {@link OnTicketCounterDecremented} listener
     */
    @BindingAdapter({"onDecremented"})
    public static void setOnDecremented(TicketCounterView ticketCounterView, OnTicketCounterDecremented listener) {
        ticketCounterView.setOnDecrementedListener(listener);
    }

    private Context mContext;
    private ImageView mIncrementImage, mDecrementImage;
    private int mMinValue = 0, mMaxValue = Integer.MAX_VALUE;
    private EditText mCounterText;
    private int mCurrentValue = 0;
    private int mPreviousValue = 0;

    private OnTicketValueChangeListener mOnValueChangeListener;
    private OnTicketCounterIncremented mOnIncrementedListener;
    private OnTicketCounterDecremented mOnDecrementedListener;

    public TicketCounterView(Context context) {
        this(context, null);
    }

    public TicketCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setView();
    }

    public void showButtons(boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.INVISIBLE;
        mIncrementImage.setVisibility(visibility);
        mDecrementImage.setVisibility(visibility);
    }

    private void setView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ticket_counter_view, this, true);
        mCounterText = (EditText) view.findViewById(R.id.ticket_counter_text);
        mIncrementImage = (ImageView) view.findViewById(R.id.increment_image);
        mDecrementImage = (ImageView) view.findViewById(R.id.decrement_image);

        mCounterText.setClickable(false);
        mCounterText.setEnabled(false);
        mIncrementImage.setOnClickListener(incrementListener);
        mDecrementImage.setOnClickListener(decrementListener);
        mCounterText.setText(String.valueOf(mCurrentValue));
    }

    /**
     * Use setOnIncrementedListener and setOnDecrementedListener instead.
     */
    @Deprecated
    public void setOnValueChangeListener(OnTicketValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setOnIncrementedListener(OnTicketCounterIncremented listener) {
        this.mOnIncrementedListener = listener;
    }

    public void setOnDecrementedListener(OnTicketCounterDecremented listener) {
        this.mOnDecrementedListener = listener;
    }

    public void setMinimumValue(int value) {
        this.mMinValue = value;
        if (mCurrentValue < mMinValue) {
            setCurrentValue(mMinValue);
        }
    }

    public void setMaximumValue(int value) {
        this.mMaxValue = value;
        if (mCurrentValue > mMaxValue) {
            setCurrentValue(mMaxValue);
        }
    }

    public int getCurrentValue() {
        return mCurrentValue;
    }

    public void setCurrentValue(int value) {
        if (value != mCurrentValue && value >= mMinValue && value <= mMaxValue) {
            mPreviousValue = mCurrentValue;
            this.mCurrentValue = value;
            mCounterText.setText(String.valueOf(mCurrentValue));
            if (mOnValueChangeListener != null)
                mOnValueChangeListener.onValueChange(TicketCounterView.this, mCurrentValue, getPreviousValue());
        }
    }

    private OnClickListener incrementListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mMaxValue == 0 && mMinValue == 0) {
                mCounterText.setText(String.valueOf(mMinValue));
                return;
            }
            if (mCurrentValue + 1 <= mMaxValue) {
                setCurrentValue(mCurrentValue + 1);
                if (mOnIncrementedListener != null) {
                    mOnIncrementedListener.onIncremented(TicketCounterView.this);
                }
            }

        }
    };

    private OnClickListener decrementListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            mCounterText.setError(null);
            if (mCurrentValue == mMinValue)
                return;
            if (mCurrentValue -1 >= mMinValue) {
                setCurrentValue(mCurrentValue - 1);

                if (mOnDecrementedListener != null) {
                    mOnDecrementedListener.onDecremented(TicketCounterView.this);
                }
            }
        }
    };

    /**
     * Use {@link OnTicketCounterIncremented} and {@link OnTicketCounterDecremented} instead.
     */
    @Deprecated
    public interface OnTicketValueChangeListener {
        void onValueChange(TicketCounterView view, int value, int oldValue);
    }

    public interface OnTicketCounterIncremented {
        void onIncremented(TicketCounterView view);
    }

    public interface OnTicketCounterDecremented {
        void onDecremented(TicketCounterView view);
    }

    public int getPreviousValue() {
        return this.mPreviousValue;
    }
}

