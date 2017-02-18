package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

/**
 * Assignment footer item
 *
 * @author acampbell
 */
public class AssignNamesAssignmentFooterItem extends BaseObservableWithLayoutItem {

    @Bindable
    private boolean doneEnabled;

    public AssignNamesAssignmentFooterItem(boolean doneEnabled) {
        setDoneEnabled(doneEnabled);
    }

    @Override
    public int getLayoutId() {
        return R.layout.commerce_assignment_fragment_footer_item;
    }

    public boolean isDoneEnabled() {
        return doneEnabled;
    }

    public void setDoneEnabled(boolean doneEnabled) {
        this.doneEnabled = doneEnabled;
        notifyPropertyChanged(BR.doneEnabled);
    }
}
