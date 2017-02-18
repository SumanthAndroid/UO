package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.TicketCounterView;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class AddOnQuantityControl extends LinearLayout implements TicketCounterView.OnTicketValueChangeListener {
    private static final String TAG = AddOnQuantityControl.class.getSimpleName();

    private List<TicketCounterView> ticketCounterViews = new ArrayList<>();
    private List<View> ticketCounterContainerViews = new ArrayList<>();

    private OnCountsChangedListener mOnCountsChangedListener;

    public AddOnQuantityControl(Context context) {
        super(context);
    }

    public AddOnQuantityControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddOnQuantityControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnCountChangedListener(OnCountsChangedListener onCountChangedListener) {
        this.mOnCountsChangedListener = onCountChangedListener;
    }

    public void addField(String id, String displayName, String unit, String abovePrice, int currentValue, BigDecimal price, int min, int max) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.add_on_quantity_control_view, null);

        TicketCounterView counterView = (TicketCounterView) view.findViewById(R.id.add_on_control_view_counter);
        TextView label = (TextView) view.findViewById(R.id.add_on_control_view_label);
        TextView startingFrom = (TextView) view.findViewById(R.id.add_on_control_view_starting_from);
        TextView priceText = (TextView) view.findViewById(R.id.add_on_control_view_price);
        TextView unitText = (TextView) view.findViewById(R.id.add_on_control_view_unit);

        label.setText(displayName);
        startingFrom.setText(abovePrice);
        priceText.setText(NumberFormat.getCurrencyInstance().format(price));
        unitText.setText(unit);

        counterView.setCurrentValue(currentValue);
        counterView.setMinimumValue(min);
        counterView.setMaximumValue(max);

        ticketCounterViews.add(counterView);
        ticketCounterContainerViews.add(view);

        counterView.setOnValueChangeListener(this);

        if (!TextUtils.isEmpty(id)) {
            counterView.setTag(id);
        }

        addView(view);
    }

    @Override
    public void onValueChange(TicketCounterView view, int value, int oldValue) {
        if (null != mOnCountsChangedListener) {
            mOnCountsChangedListener.onCountChanged(ticketCounterViews, view, oldValue, value, getTotalOfAllCounters());
        }
    }

    public int getTotalOfAllCounters() {
        int value = 0;
        for (TicketCounterView view : ticketCounterViews) {
            value += view.getCurrentValue();
        }
        return value;
    }

    public void updatePrice(@Nonnull String key, BigDecimal price) {
        for (View ticketCounterContainerView : ticketCounterContainerViews) {
            if (ticketCounterContainerView != null) {
                TicketCounterView ticketCounterView = (TicketCounterView) ticketCounterContainerView.findViewById(R.id.add_on_control_view_counter);
                if (ticketCounterView != null && key.equals(ticketCounterView.getTag())) {
                    TextView priceTextView = (TextView) ticketCounterContainerView.findViewById(R.id.add_on_control_view_price);
                    if (priceTextView != null) {
                        priceTextView.setText(NumberFormat.getCurrencyInstance().format(price));
                    }
                    // Hide starting from labels when price changes
                    TextView startingFrom = (TextView) ticketCounterContainerView.findViewById(R.id.add_on_control_view_starting_from);
                    if (startingFrom != null) {
                        startingFrom.setVisibility(GONE);
                    }
                }
            }
        }
    }

    public void toggleStartingFromLabel(@Nonnull String key, boolean visible) {
        for (View ticketCounterContainerView : ticketCounterContainerViews) {
            if (ticketCounterContainerView != null) {
                TicketCounterView ticketCounterView = (TicketCounterView) ticketCounterContainerView.findViewById(R.id.add_on_control_view_counter);
                if (ticketCounterView != null && key.equals(ticketCounterView.getTag())) {
                    TextView startingFrom = (TextView) ticketCounterContainerView.findViewById(R.id.add_on_control_view_starting_from);
                    if (startingFrom != null) {
                        startingFrom.setVisibility(visible ? VISIBLE : GONE);
                    }
                }
            }
        }
    }
}
