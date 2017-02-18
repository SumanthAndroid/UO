package com.universalstudios.orlandoresort.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

/**
 * Created by Nicholas Hanna on 12/5/2016.
 */
public class ClipBoardUtils {

    public static void copyToClipboard(String label, String textToCopy) {
        Context context = UniversalOrlandoApplication.getAppContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, textToCopy);
        clipboard.setPrimaryClip(clip);
    }

    public static void copyToClipboard(int labelResId, String textToCopy) {
        Context context = UniversalOrlandoApplication.getAppContext();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(labelResId), textToCopy);
        clipboard.setPrimaryClip(clip);
    }
}
