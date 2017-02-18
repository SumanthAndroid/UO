package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.utils.AndroidUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: DaysButton
 * Class Description: Button control for shopping filter
 */
public class DaysButton extends LinearLayout implements View.OnClickListener {
    public static final String TAG = "DaysButton";

    private TextView textView;
    private OnSelectionChangedListener onSelectionChangedListener;
    private boolean isSelected;

    public DaysButton(Context context, OnSelectionChangedListener onSelectionChangedListener) {
        super(context);
        this.onSelectionChangedListener = onSelectionChangedListener;
        init();
    }

    public DaysButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DaysButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);
        textView = new TextView(getContext());
        LinearLayout.LayoutParams tvParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = (int) AndroidUtils.convertDpToPixel(5, getContext());
        tvParams.setMargins(margin, margin, margin, margin);
        tvParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setPadding(16, 16 ,16, 16);
        textView.setMinimumWidth((int) AndroidUtils.convertDpToPixel(50, getContext()));
        textView.setMinimumHeight((int) AndroidUtils.convertDpToPixel(50, getContext()));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        textView.setText("X");
        textView.setBackgroundResource(R.drawable.shape_ticket_count_unselected);
        textView.setLayoutParams(tvParams);
        addView(textView);

        this.setOnClickListener(this);
    }

    /**
     * Sets this button as selected or not
     *
     * @param isSelected true if selected
     */
    public void setSelected(boolean isSelected) {
        if (null == this.textView) {
            return;
        }
        this.isSelected = isSelected;

        if (isSelected) {
            textView.setBackgroundResource(R.drawable.shape_ticket_count_selected);
            textView.setTextColor(getResources().getColor(R.color.text_white));
        } else {
            textView.setBackgroundResource(R.drawable.shape_ticket_count_unselected);
            textView.setTextColor(getResources().getColor(R.color.text_shadow_black));
        }

        if (isSelected) {
            if (null != onSelectionChangedListener) {
                onSelectionChangedListener.onSelectionChanged(this);
            }
        }
    }

    public String getText() {
        return textView.getText().toString();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setText(String text) {
        text = text.replaceAll(" \\\\n ", "\n").trim();
        this.textView.setText(text);
    }

    @Override
    public void onClick(View v) {
        setSelected(true);
    }
}
