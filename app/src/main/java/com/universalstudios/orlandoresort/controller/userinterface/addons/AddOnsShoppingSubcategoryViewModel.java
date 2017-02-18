package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.universalstudios.orlandoresort.BR;

/**
 * @author tjudkins
 * @since 11/1/16
 */

public class AddOnsShoppingSubcategoryViewModel extends BaseObservable {

    @Bindable
    private boolean loading;
    @Bindable
    private boolean empty;

    public boolean getLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public boolean getEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
        notifyPropertyChanged(BR.empty);
    }
}
