package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.AndroidUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.UIControlField;
import com.universalstudios.orlandoresort.view.TicketCounterView;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: PartyMembersControl
 * Class Description: TODO: ALWAYS FILL OUT
 */
public class PartyMembersControl extends LinearLayout implements TicketCounterView.OnTicketValueChangeListener {
    public static final String TAG = "PartyMembersControl";
    // FIXME Service does not return max for filter
    public static final int MAX_PARTY_MEMBERS = 10;
    private static final String DISPLAY_VALUE_ADULT = "ADULT";
    private static final String DISPLAY_VALUE_CHILD = "CHILD";

    private OnCountsChangedListener onCountsChangedListener;

    private List<TicketCounterView> ticketCounterViews = new ArrayList<>();

    public PartyMembersControl(Context context) {
        super(context);
    }

    public PartyMembersControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PartyMembersControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnCountChangedListener(OnCountsChangedListener onCountChangedListener) {
        this.onCountsChangedListener = onCountChangedListener;
    }

    public void setUIControlFields(List<UIControlField> fields, int... values) {
        if (null == fields || fields.isEmpty()) {
            return;
        }
        this.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < fields.size(); i++) {
            UIControlField field = fields.get(i);

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(HORIZONTAL);
            LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutParams.setMargins(0, (int) AndroidUtils.convertDpToPixel(10, getContext()), 0, 0);
            linearLayout.setLayoutParams(linearLayoutParams);

            //Label
            TextView textView = new TextView(getContext());
            String label = field.getDisplayValue();
            if (DISPLAY_VALUE_ADULT.equalsIgnoreCase(label)) {
                label = IceTicketUtils.getTridionConfig().getAdultsAgeLabel();
            } else if (DISPLAY_VALUE_CHILD.equalsIgnoreCase(label)) {
                label = IceTicketUtils.getTridionConfig().getChildrenAgeLabel();
            }
            textView.setText(label);
            LayoutParams tvParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvParams.weight = 1;
            tvParams.gravity = Gravity.CENTER_VERTICAL;
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(tvParams);

            //Counter view
            TicketCounterView counterView = new TicketCounterView(getContext());
            counterView.setMaximumValue(MAX_PARTY_MEMBERS);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.END;
            layoutParams.weight = 1;
            counterView.setLayoutParams(layoutParams);

            counterView.setGravity(Gravity.END);

            ticketCounterViews.add(counterView);

            counterView.setOnValueChangeListener(this);

            //Add views
            linearLayout.addView(textView);
            linearLayout.addView(counterView);

            this.addView(linearLayout);
        }

        for (int i = 0; i < ticketCounterViews.size(); i++) {
            TicketCounterView counterView = ticketCounterViews.get(i);
            if (null != values && values.length >= i - 1) {
                counterView.setCurrentValue(values[i]);
            }
        }
    }

    private int getTotalOfAllCounters() {
        int value = 0;
        for (TicketCounterView view : ticketCounterViews) {
            value += view.getCurrentValue();
        }
        return value;
    }

    @Override
    public void onValueChange(TicketCounterView view, int value, int oldValue) {
        if (null != onCountsChangedListener) {
            onCountsChangedListener.onCountChanged(ticketCounterViews, view, oldValue, value, getTotalOfAllCounters());
        }
    }
}
