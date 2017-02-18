package com.universalstudios.orlandoresort.controller.userinterface.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.universalstudios.orlandoresort.R;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/26/16.
 * Class: FullScreenLoadingView
 * Class Description: Fragment to show a loading view that doesn't allow user to hit any buttons
 */
public class FullScreenLoadingView extends DialogFragment {
    public static final String TAG = "FullScreenLoadingView";

    private static boolean isShowing = false;
    private static FullScreenLoadingView sInstance = null;


    public static synchronized FullScreenLoadingView show(FragmentManager manager) {
        if (!isShowing) {
            FullScreenLoadingView fragment = new FullScreenLoadingView();
            fragment.show(manager, "");
            isShowing = true;
            sInstance = fragment;
            return fragment;
        } else {
            isShowing = true;
            return sInstance;
        }
    }

    public static void showDialog(FragmentManager manager) {
        sInstance = new FullScreenLoadingView();
        sInstance.show(manager, "");
        isShowing = true;
    }
    public static void dismissDialog() {
        if (sInstance != null && isShowing) {
            sInstance.dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.fullscreen_loading_fragment, null);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
        isShowing = false;
    }
}
