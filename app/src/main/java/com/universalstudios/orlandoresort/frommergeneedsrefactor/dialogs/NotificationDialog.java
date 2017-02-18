package com.universalstudios.orlandoresort.frommergeneedsrefactor.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.api_compatibility.CompatibilityManager;

import java.io.Serializable;

/**
 * Created by Kevin Haines on 5/5/16.
 * Project: Universal Orlando
 * Class Name: NotificationDialog
 * Class Description: Dialog for displaying simple notifications to the user
 */
public class NotificationDialog extends AppCompatDialogFragment implements AlertDialog.OnClickListener {
    public static final String TAG = "NotificationDialog";

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_POSITIVE = "ARG_POSITIVE";
    private static final String ARG_NEGATIVE = "ARG_NEGATIVE";
    private static final String ARG_TAG = "ARG_TAG";

    private String mTitle;
    private String mMessage;
    private String mPositiveText;
    private String mNegativeText;
    private Serializable mTag; //Tag object to reference onDismiss

    private OnDialogDismiss mParentOnDialogDismissListener;

    /**
     * Factory method to create a new instance of NotificationDialog
     *
     * @param title Dialog's title
     * @param message Dialog's message
     * @param tag Custom Tag object used in reference when dialog is dismissed
     * @return new instance of NotificationDialog
     */
    public static NotificationDialog newInstance(String title, String message, Serializable tag) {
        NotificationDialog dialog = new NotificationDialog();
        Bundle b = new Bundle();
        b.putString(ARG_MESSAGE, message);
        b.putString(ARG_TITLE, title);
        b.putSerializable(ARG_TAG, tag);
        dialog.setArguments(b);
        return dialog;
    }

    /**
     * Factory method to create a new instance of NotificationDialog
     *
     * @param title Dialog's title
     * @param message Dialog's message
     * @param tag Custom Tag object used in reference when dialog is dismissed
     * @return new instance of NotificationDialog
     */
    public static NotificationDialog newInstance(String title, String message, String positive, String negative, Serializable tag) {
        NotificationDialog dialog = new NotificationDialog();
        dialog.setCancelable(false);
        Bundle b = new Bundle();
        b.putString(ARG_MESSAGE, message);
        b.putString(ARG_TITLE, title);
        b.putString(ARG_POSITIVE, positive);
        b.putString(ARG_NEGATIVE, negative);
        b.putSerializable(ARG_TAG, tag);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(ARG_TITLE);
            mMessage = savedInstanceState.getString(ARG_MESSAGE);
            mPositiveText = savedInstanceState.getString(ARG_POSITIVE);
            mNegativeText = savedInstanceState.getString(ARG_NEGATIVE);
            mTag = savedInstanceState.getSerializable(ARG_TAG);
        } else if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mMessage = getArguments().getString(ARG_MESSAGE);
            mPositiveText = getArguments().getString(ARG_POSITIVE);
            mNegativeText = getArguments().getString(ARG_NEGATIVE);
            mTag = getArguments().getSerializable(ARG_TAG);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof OnDialogDismiss) {
            mParentOnDialogDismissListener = (OnDialogDismiss) parentFragment;
        } else if (activity != null && activity instanceof DialogInterface.OnDismissListener) {
            mParentOnDialogDismissListener = (OnDialogDismiss) activity;
        } else if (mParentOnDialogDismissListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnDialogDismissListener");
            }
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setCancelable(false);

        if (CompatibilityManager.isAboveApi(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            builder.setPositiveButton(mPositiveText == null ? getString(R.string.ok) : mPositiveText, this);
            if (null != mNegativeText) {
                builder.setNegativeButton(mNegativeText, this);
            }
        } else {
            builder.setPositiveButton(mPositiveText == null ? getString(R.string.ok) : mPositiveText, this);
            if (null != mNegativeText) {
                builder.setNegativeButton(mNegativeText, this);
            }
        }

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_TAG, mTag);
        outState.putString(ARG_TITLE, mTitle);
        outState.putString(ARG_POSITIVE, mPositiveText);
        outState.putString(ARG_NEGATIVE, mNegativeText);
        outState.putString(ARG_MESSAGE, mMessage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mParentOnDialogDismissListener != null) {
            mParentOnDialogDismissListener.onButtonClicked(mTag, which);
        }
    }
}
