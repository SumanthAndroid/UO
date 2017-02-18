package com.universalstudios.orlandoresort.view.segmentedcontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.universalstudios.orlandoresort.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class SegmentedGroup extends RadioGroup {

    private int oneDP;
    private Resources resources;
    private int mTintColor;
//    private int mCheckedTextColor = Color.WHITE;
    private ColorStateList mTextColorStateList;

    public SegmentedGroup(Context context) {
        super(context);
        resources = getResources();
        mTintColor = resources.getColor(R.color.radio_button_selected);
        oneDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
        mTextColorStateList = resources.getColorStateList(R.color.state_list_button_text);
    }

    public SegmentedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        resources = getResources();
        mTintColor = resources.getColor(R.color.radio_button_selected);
        oneDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SegmentedGroup);
        mTextColorStateList = resources.getColorStateList(typedArray.getResourceId(
                R.styleable.SegmentedGroup_text_color, R.color.state_list_button_text));
        typedArray.recycle();
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //Use holo light for default
        updateBackground();
    }

    public void setTintColor(int tintColor) {
        mTintColor = tintColor;
        updateBackground();
    }

    public void setTintColor(int tintColor, int checkedTextColor) {
        mTintColor = tintColor;
//        mCheckedTextColor = checkedTextColor;
        updateBackground();
    }

    public void updateBackground() {
        int count = super.getChildCount();
        // Get visible view indices to properly handle gone views
        List<Integer> indices = new ArrayList<Integer>(super.getChildCount());
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE) {
                indices.add(i);
            }
        }
        if (indices.size() > 1) {
            View child = getChildAt(indices.get(0));
            LayoutParams initParams = (LayoutParams) child.getLayoutParams();
            LayoutParams params = new LayoutParams(initParams.width, initParams.height, initParams.weight);
            params.setMargins(0, 0, -oneDP, 0);
            child.setLayoutParams(params);
            updateBackground(getChildAt(indices.get(0)), R.drawable.shape_radio_checked_left, R.drawable.shape_radio_unchecked_left);
            
            for (int i = 1; i < indices.size() - 1; i++) {
                updateBackground(getChildAt(indices.get(i)), R.drawable.shape_radio_checked_middle, R.drawable.shape_radio_unchecked_middle);
                View child2 = getChildAt(indices.get(i));
                initParams = (LayoutParams) child2.getLayoutParams();
                params = new LayoutParams(initParams.width, initParams.height, initParams.weight);
                params.setMargins(0, 0, -oneDP, 0);
                child2.setLayoutParams(params);
            }
            updateBackground(getChildAt(indices.get(indices.size()-1)), R.drawable.shape_radio_checked_right, R.drawable.shape_radio_unchecked_right);
        } else if (indices.size() == 1) {
            updateBackground(getChildAt(indices.get(0)), R.drawable.shape_radio_checked_default, R.drawable.shape_radio_unchecked_default);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi") 
    private void updateBackground(View view, int checked, int unchecked) {
        //Set text color
//        ColorStateList colorStateList = new ColorStateList(new int[][]{
//                {-android.R.attr.state_enabled},
//                {android.R.attr.state_pressed},
//                {-android.R.attr.state_pressed, -android.R.attr.state_checked},
//                {-android.R.attr.state_pressed, android.R.attr.state_checked}},
//                new int[]{Color.GRAY, Color.GRAY, mTintColor, mCheckedTextColor});
        
        // Setting text color via color state list instead of hard-coded values
        ((Button) view).setTextColor(mTextColorStateList);

        //Redraw with tint color
        Drawable checkedDrawable = resources.getDrawable(checked).mutate();
        Drawable uncheckedDrawable = resources.getDrawable(unchecked).mutate();
        ((GradientDrawable) checkedDrawable).setColor(mTintColor);
        ((GradientDrawable) checkedDrawable).setStroke(oneDP, mTintColor);

        //Create drawable
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, uncheckedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);

        //Set button background
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
    }
}
