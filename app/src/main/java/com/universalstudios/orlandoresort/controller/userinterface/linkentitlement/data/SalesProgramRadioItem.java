package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.binding.LayoutBinding;

/**
 * @author tjudkins
 * @since 1/23/17
 */

public class SalesProgramRadioItem extends SalesProgram implements LayoutBinding {

    @Bindable
    private boolean selected;

    public SalesProgramRadioItem(SalesProgram salesProgram) {
        this(salesProgram.getName(), salesProgram.getProgramCode());
    }

    public SalesProgramRadioItem(String name, String programCode) {
        super(name, programCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_select_channel_radio;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }
}
