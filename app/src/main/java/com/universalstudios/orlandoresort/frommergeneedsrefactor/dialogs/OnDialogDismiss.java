package com.universalstudios.orlandoresort.frommergeneedsrefactor.dialogs;

import android.content.DialogInterface;

/**
 * Created by Kevin Haines on 5/5/16.
 * Project: Universal Orlando
 * Class Name: OnDialogDismiss
 * Class Description:
 */
public interface OnDialogDismiss {
    int POSITIVE = DialogInterface.BUTTON_POSITIVE;
    int NEGATIVE = DialogInterface.BUTTON_NEGATIVE;

    void onDismiss(Object tag);
    void onButtonClicked(Object tag, int which);
}
