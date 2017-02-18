package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;

/**
 * @author acampbell
 */
public class CommerceAssignmentViewModel extends BaseObservable {

    @Bindable
    private boolean showLoading;

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyPropertyChanged(BR.showLoading);
    }
}
