package com.universalstudios.orlandoresort.controller.userinterface.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.R;

import java.util.Calendar;

/**
 * Util class to manage showing dialogs.
 *
 * @author Steven Byle
 */
public class DialogUtils {

    private static final String LOADING_VIEW_TAG = "LoadingViewSpinner";

    public static String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment) {
        return showDialogFragment(fragmentManager, dialogFragment, generateFragmentTag(dialogFragment));
    }

    public static String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment,
                                            String fragmentTag) {
        return showDialogFragment(fragmentManager, dialogFragment, fragmentTag, true);
    }

    public static String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment,
                                            boolean onlyIfNotDuplicate) {
        return showDialogFragment(fragmentManager, dialogFragment, generateFragmentTag(dialogFragment), onlyIfNotDuplicate);
    }

    public static String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment,
                                            String fragmentTag, boolean onlyIfNotDuplicate) {

        // If only showing non duplicates dialogs, make sure the fragment isn't already in the manager
        boolean doesFragmentExist = fragmentManager.findFragmentByTag(fragmentTag) != null;
        if (!(onlyIfNotDuplicate && doesFragmentExist)) {
            dialogFragment.show(fragmentManager, fragmentTag);
        }

        return fragmentTag;
    }

    public static void showLoadingView(FragmentManager fragmentManager) {
        boolean doesFragmentExist = fragmentManager.findFragmentByTag(LOADING_VIEW_TAG) != null;
        if (!doesFragmentExist) {
            FullScreenLoadingView fullScreenLoadingView = new FullScreenLoadingView();
            fullScreenLoadingView.show(fragmentManager, LOADING_VIEW_TAG);
        }
    }

    public static void cancelLoadingView(FragmentManager fragmentManager) {
        FullScreenLoadingView fragment = (FullScreenLoadingView) fragmentManager.findFragmentByTag(LOADING_VIEW_TAG);
        boolean doesFragmentExist = fragment != null;
        if (doesFragmentExist) {
            fragment.dismiss();
        }
    }

    private static String generateFragmentTag(Fragment fragment) {
        return fragment.getClass().getName();
    }


    // OLD DIALOG UTILS, TODO NEEDS TO BE REFACTORED

    /**
     * Shows the Date of Birth date picker dialog
     *
     * @param context         The context of the activity utilizing this date picker
     * @param dateSetListener The date listener
     */
    public static void showDatePicker(Context context, DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar today = Calendar.getInstance();
        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, today.get(Calendar.YEAR) - 13);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                R.style.AlertDialogStyle_DatePicker,
                dateSetListener,
                minCal.get(Calendar.YEAR),
                minCal.get(Calendar.MONTH),
                minCal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(minCal.getTimeInMillis());

        datePickerDialog.show();
    }

    /**
     * * use {@link DialogUtils#showOneButtonAlertDialogFragment(FragmentManager, String, String, String)}
     *
     * @param context
     * @param titleResId
     * @param messageResId
     * @param positiveButtonTextResId
     * @param positiveButtonListener
     */
    @Deprecated
    public static void showOneButtonMessageDialog(Context context, int titleResId, int messageResId,
                                                  int positiveButtonTextResId, @Nullable DialogInterface.OnClickListener positiveButtonListener) {

        showOneButtonMessageDialog(context,
                context.getString(titleResId),
                context.getString(messageResId),
                context.getString(positiveButtonTextResId),
                positiveButtonListener);
    }

    /**
     * use {@link #showOneButtonAlertDialogFragment(FragmentManager, String, String, String)}
     *
     * @param context
     * @param title
     * @param message
     * @param positiveButtonText
     * @param positiveButtonListener
     */
    @Deprecated
    public static void showOneButtonMessageDialog(Context context, String title, String message,
                                                  @Nullable String positiveButtonText, @Nullable DialogInterface.OnClickListener positiveButtonListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false);

        if (!TextUtils.isEmpty(positiveButtonText) && positiveButtonListener != null) {
            builder.setPositiveButton(positiveButtonText, positiveButtonListener);
        }

        builder.create().show();
    }

    /**
     * use {@link #showTwoButtonAlertDialogFragment(FragmentManager, String, String, String, String)}
     *
     * @param context
     * @param titleResId
     * @param messageResId
     * @param positiveButtonTextResId
     * @param positiveButtonListener
     * @param negativeButtonResId
     * @param negativeButtonListener
     */
    @Deprecated
    public static void showTwoButtonMessageDialog(Context context, int titleResId, int messageResId,
                                                  int positiveButtonTextResId, DialogInterface.OnClickListener positiveButtonListener, int negativeButtonResId, DialogInterface.OnClickListener negativeButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleResId)
                .setCancelable(false)
                .setMessage(messageResId)
                .setPositiveButton(positiveButtonTextResId, positiveButtonListener)
                .setNegativeButton(negativeButtonResId, negativeButtonListener)
                .create()
                .show();
    }

    /**
     * use {@link #showTwoButtonAlertDialogFragment(FragmentManager, String, String, String, String)}
     *
     * @param context
     * @param title
     * @param message
     * @param positiveButtonText
     * @param positiveButtonListener
     * @param negativeButton
     * @param negativeButtonListener
     */
    @Deprecated
    public static void showTwoButtonMessageDialog(Context context, String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveButtonListener, String negativeButton, DialogInterface.OnClickListener negativeButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setNegativeButton(negativeButton, negativeButtonListener)
                .create()
                .show();
    }

    public static void showOneButtonAlertDialogFragment(FragmentManager fragmentManager,
                                                        String title,
                                                        String message,
                                                        String positiveButtonText) {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message, positiveButtonText, null, null);
        showDialogFragment(fragmentManager, fragment);
    }

    public static void showTwoButtonAlertDialogFragment(FragmentManager fragmentManager,
                                                        String title,
                                                        String message,
                                                        String positiveButtonText,
                                                        String negativeButtonText) {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message, positiveButtonText, negativeButtonText, null);
        showDialogFragment(fragmentManager, fragment);
    }

    public static void showThreeButtonAlertDialogFragment(FragmentManager fragmentManager,
                                                          String title,
                                                          String message,
                                                          String positiveButtonText,
                                                          String negativeButtonText,
                                                          String neutralButtonText) {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(title, message, positiveButtonText, negativeButtonText, neutralButtonText);
        showDialogFragment(fragmentManager, fragment);
    }

}
