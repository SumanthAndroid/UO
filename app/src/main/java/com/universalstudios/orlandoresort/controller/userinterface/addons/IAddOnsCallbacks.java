package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.annotation.NonNull;

/**
 * Created by kbojarski on 10/26/16.
 */
public interface IAddOnsCallbacks {

    void buttonEnabled(boolean enabled);

    void updateSubtotal();

    @NonNull
    AddOnsState getAddOnState();
}
