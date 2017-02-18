/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * 
 * 
 * @author Steven Byle
 */
public class DismissableDialogFragment extends DialogFragment {
	private static final String TAG = DismissableDialogFragment.class.getSimpleName();

	private OnDialogDismissListener mParentOnDialogDismissListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof OnDialogDismissListener) {
			mParentOnDialogDismissListener = (OnDialogDismissListener) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof OnDialogDismissListener) {
			mParentOnDialogDismissListener = (OnDialogDismissListener) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentOnDialogDismissListener == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement OnDialogDismissListener");
			}
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		if (mParentOnDialogDismissListener != null) {
			mParentOnDialogDismissListener.onDialogDismiss(this);
		}
	}
}
