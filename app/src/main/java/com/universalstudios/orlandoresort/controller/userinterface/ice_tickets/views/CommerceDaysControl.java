package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.CommerceUiBuilder;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.UIControlField;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/29/16.
 * Class: CommerceDaysControl
 * Class Description: Control for selecting days
 */
//TODO this needs to be refactored to be more of a generic control
public class CommerceDaysControl extends HorizontalScrollView implements OnSelectionChangedListener {
    public static final String TAG = "CommerceDaysControl";

    private LinearLayout layoutContainer;

    public enum DaysControlOrderingType {
        NORMAL,
        REVERSE
    }

    private OnSelectionChangedListener onSelectionChangedListener;
    private int selectedIndex = -1;
    private int filterDays = -1;
    private List<UIControlField> fields;
    private DaysControlOrderingType daysControlOrderingType;

    public CommerceDaysControl(Context context) {
        super(context);
        initSubViews();
    }

    public CommerceDaysControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSubViews();
    }

    public CommerceDaysControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSubViews();
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.onSelectionChangedListener = listener;
    }

    private void initSubViews() {
        layoutContainer = new LinearLayout(getContext());
        layoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutContainer.setLayoutParams(layoutParams);
        this.addView(layoutContainer);
    }

    public void setUIControlFields(@NonNull DaysControlOrderingType orderingType, List<UIControlField> fields) {
        if (null == fields || fields.isEmpty()) {
            return;
        }

        this.daysControlOrderingType = orderingType;

        this.fields = fields;
        setControls();
    }

    public void setControls() {
        if (null == fields) {
            return;
        }
        layoutContainer.removeAllViews();
        setSelectedIndex();

        for (int i = 0; i < fields.size(); i++) {
            UIControlField field = fields.get(i);
            DaysButton dayButton = new DaysButton(getContext(), this);
            dayButton.setText(field.getDisplayValue());
            dayButton.setTag(field);
            if (selectedIndex == -1 && field.isDefault()) {
                dayButton.setSelected(true);
                //Setting the filter this way avoids triggering the onClickListener
                //and forcing the serviceIdentifier change
                CommerceUiBuilder.setFilterInfoWithoutUIFromDaysControl(dayButton);
            } else if (i == selectedIndex){
                dayButton.setSelected(true);
            }
            layoutContainer.addView(dayButton);
        }
    }

    public void setSelectedIndex() {
        if (filterDays == -1 || null == fields) {
            return;
        }
        if (filterDays == -2) {
            selectedIndex = 0;
        } else {
            switch(daysControlOrderingType) {
                case REVERSE:
                    // Cannot always assume the size of the fields list - must loop through and match on value
                    for (int i = 0; i < fields.size(); i++) {
                        UIControlField field = fields.get(i);
                        if (TextUtils.equals(field.getDisplayValue(), Integer.toString(filterDays))) {
                            selectedIndex = i;
                            break;
                        }
                    }
                    break;
                case NORMAL:
                default:
                    selectedIndex = (filterDays - 1);
                    break;
            }
        }
    }

    public void setSelectedFromFilterInfo(int numDaysFromFilterInfo) {
        filterDays = numDaysFromFilterInfo;
    }

    /**
     * Gets the UIControlField object associated with the selected view
     * Set by calling DaysButton.setTag(UIControlField) in {@link this#setUIControlFields(DaysControlOrderingType, List)}
     *
     * @return selected buttons UIControlField or null if not selected and there was no default
     */
    public UIControlField getSelectedControl() {
        int childCount = layoutContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DaysButton button = (DaysButton) layoutContainer.getChildAt(i);
            if (button.isSelected()) {
                return (UIControlField) button.getTag();
            }
        }
        return null;
    }

    @Override
    public void onSelectionChanged(DaysButton selectedButton) {
        int childCount = layoutContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DaysButton button = (DaysButton) layoutContainer.getChildAt(i);
            if (button != selectedButton) {
                button.setSelected(false);
            }
        }

        if (null != onSelectionChangedListener) {
            onSelectionChangedListener.onSelectionChanged(selectedButton);
        }
    }
}
