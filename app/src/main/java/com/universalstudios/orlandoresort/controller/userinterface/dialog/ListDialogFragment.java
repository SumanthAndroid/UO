package com.universalstudios.orlandoresort.controller.userinterface.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.universalstudios.orlandoresort.R;

import java.util.ArrayList;
import java.util.List;

public class ListDialogFragment extends DialogFragment {

    private static final String KEY_ARG_LIST = "KEY_ARG_LIST";
    private static final String KEY_ARG_MESSAGE = "KEY_ARG_MESSAGE";

    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnCancelListener onCancelListener;
    private String title;
    private ArrayList<String> list;

    public static ListDialogFragment getInstance(List<String> list, String message) {
        Bundle args = new Bundle();
        ArrayList<String> newList = new ArrayList<>();
        newList.addAll(list);
        args.putStringArrayList(KEY_ARG_LIST, newList);
        args.putString(KEY_ARG_MESSAGE, message);

        ListDialogFragment dialogFragment = new ListDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (null != args) {
            list = args.getStringArrayList(KEY_ARG_LIST);
            title = args.getString(KEY_ARG_MESSAGE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(list.toArray(new String[0]), onClickListener);
        if (null != onCancelListener) {
            builder.setCancelable(true)
                    .setNegativeButton(R.string.alerts_button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onCancelListener.onCancel(dialog);
                        }
                    })
                    .setOnCancelListener(onCancelListener);
        } else {
            builder.setCancelable(false);
        }

        return builder.create();

    }

    public void setOnClickListener(DialogInterface.OnClickListener listener) {
        this.onClickListener = listener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        this.onCancelListener = listener;
    }

}