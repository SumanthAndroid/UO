package com.universalstudios.orlandoresort.controller.userinterface.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.compat.BuildConfig;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;


public class AlertDialogFragment extends DialogFragment {
    private static final String TAG = AlertDialogFragment.class.getSimpleName();

    private static final String KEY_ARGS_TITLE = "KEY_ARGS_TITLE";
    private static final String KEY_ARGS_MESSAGE = "KEY_ARGS_MESSAGE";
    private static final String KEY_ARGS_POSITIVE = "KEY_ARGS_POSITIVE";
    private static final String KEY_ARGS_NEGATIVE = "KEY_ARGS_NEGATIVE";
    private static final String KEY_ARGS_NEUTRAL = "KEY_ARGS_NEUTRAL";

    private OnAlertDialogFragmentClickListener mListener;


    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance(String title, String message, String positiveButtonText, String negativeButtonText, String neutralButtonText) {
        AlertDialogFragment fragment = new AlertDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARGS_TITLE, title);
        bundle.putString(KEY_ARGS_MESSAGE, message);
        bundle.putString(KEY_ARGS_POSITIVE, positiveButtonText);
        bundle.putString(KEY_ARGS_NEGATIVE, negativeButtonText);
        bundle.putString(KEY_ARGS_NEUTRAL, neutralButtonText);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if parent fragment (if there is one) implements the interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnAlertDialogFragmentClickListener) {
            mListener = (OnAlertDialogFragmentClickListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the interface
        else if (activity instanceof OnDialogDismissListener) {
            mListener = (OnAlertDialogFragmentClickListener) activity;
        }
        // If neither implements the image selection callback, warn that
        // selections are being missed
        else if (mListener == null) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnAlertDialogFragmentClickListener");
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title;
        String message;
        String positiveText;
        String negativeText;
        String neutralText;

        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(KEY_ARGS_TITLE, null);
            message = args.getString(KEY_ARGS_MESSAGE, null);
            positiveText = args.getString(KEY_ARGS_POSITIVE, null);
            negativeText = args.getString(KEY_ARGS_NEGATIVE, null);
            neutralText = args.getString(KEY_ARGS_NEUTRAL, null);
        } else {
            title = null;
            message = null;
            positiveText = null;
            negativeText = null;
            neutralText = null;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(positiveText)) {
            alertDialogBuilder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onClick() called with: dialog = [" + dialog + "], which = [" + which + "]");
                    }

                    if (mListener != null) {
                        mListener.onPositiveClick(dialog);
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(negativeText)) {
            alertDialogBuilder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onClick() called with: dialog = [" + dialog + "], which = [" + which + "]");
                    }

                    if (mListener != null) {
                        mListener.onNegativeClick(dialog);
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(neutralText)) {
            alertDialogBuilder.setNeutralButton(neutralText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onClick() called with: dialog = [" + dialog + "], which = [" + which + "]");
                    }

                    if (mListener != null) {
                        mListener.onNeutralTextClick(dialog);
                    }
                }
            });
        }

        return alertDialogBuilder.create();
    }

    public interface OnAlertDialogFragmentClickListener {
        void onPositiveClick(DialogInterface dialog);

        void onNegativeClick(DialogInterface dialog);

        void onNeutralTextClick(DialogInterface dialog);
    }
}
