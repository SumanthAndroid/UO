package com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.dialogs.NotificationDialog;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.dialogs.OnDialogDismiss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kevin Haines on 5/5/16.
 * Project: Universal Orlando
 * Class Name: PermissionsManager
 * Class Description: Class to handle permissions reasoning and handling for Marshmallow and above
 */
public class PermissionsManager implements PermissionsRequestsListener {
    public static final String TAG = "PermissionsManager";

    private static PermissionsManager sInstance = null;
    private CopyOnWriteArrayList<PermissionsRequestsListener> mListeners = new CopyOnWriteArrayList<>();

    public synchronized static PermissionsManager getInstance() {
        if (sInstance == null) {
            sInstance = new PermissionsManager();
        }
        return sInstance;
    }

    /**
     * DO NOT CALL THIS IN onResume() it will recursively call it forever
     *
     * This method requests the needed permissions for us and gives us a callback
     * on the result
     *
     * @param activity Calling Activity
     * @param permissions Permissions to request @see {@link android.Manifest.permission}
     * @param listener Listener to handle our custom callback when the permissions are accepted/denied
     */
    public void handlePermissionRequest(FragmentActivity activity, String[] permissions, PermissionsRequestsListener listener) {
        if (activity == null || permissions == null || listener == null) {
            if (BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Cant request permissions -> activity is null: " + (activity == null ? "true" : "false") +
                "\n permissions array is null: " + (permissions == null ? " true " : " false") +
                "\n PermissionsRequestListener is null: " + (listener == null ? " true " :  " false"));
            }
            return;
        }

        if (!isAboveMarshmallow()) {
            listener.onPermissionsUpdated(Arrays.asList(permissions), new ArrayList<String>());
            return;
        }

        List<String> permissionsList = new ArrayList<>();
        for (String perm : permissions) {
            if (!isPermissionAllowed(activity, perm)) {
                permissionsList.add(perm);
            }
        }

        String[] finalPermissionsList = new String[permissionsList.size()];
        permissionsList.toArray(finalPermissionsList);

        if (finalPermissionsList.length == 0) {
            listener.onPermissionsUpdated(permissionsList, new ArrayList<String>());
        } else {
            mListeners.addIfAbsent(listener);
            PermissionsFragment permissionsFragment = PermissionsFragment.newInstance(finalPermissionsList);
            activity.getSupportFragmentManager().beginTransaction()
                    .add(permissionsFragment, PermissionsFragment.TAG)
                    .commit();
            permissionsFragment.setListener(this);
        }
    }

//    public void handlePermissionsRequest(FragmentActivity activity, String[] permissions, PermissionsRequestsListener listener,
//                                         String reasoningTitle, String reasoningMessage, boolean showReasoningAlways) {
//        if (!isAboveMarshmallow()) {
//            listener.onPermissionsUpdated(Arrays.asList(permissions), new ArrayList<String>());
//            return;
//        }
//
//        List<String> permissionsList = new ArrayList<>();
//        for (String perm : permissions) {
//            if (!isPermissionAllowed(activity, perm)) {
//                permissionsList.add(perm);
//            }
//        }
//
//        String[] finalPermissionsList = new String[permissionsList.size()];
//        permissionsList.toArray(finalPermissionsList);
//
//        if (finalPermissionsList.length == 0) {
//            listener.onPermissionsUpdated(permissionsList, new ArrayList<String>());
//        } else {
//            mListeners.addIfAbsent(listener);
//            PermissionsFragment permissionsFragment = PermissionsFragment.newInstance(finalPermissionsList);
//            activity.getSupportFragmentManager().beginTransaction()
//                    .add(permissionsFragment, PermissionsFragment.TAG)
//                    .commit();
//            permissionsFragment.setListener(this);
//        }
//    }

    /**
     * Removes a listener from the list of Permission Listeners
     *
     * Note: Do not call in onPause() because this will remove the
     * listener when the PermissionsDialog shows and no callbacks will
     * be handled.
     *
     * @param listener listener to remove
     */
    public void removeListener(PermissionsRequestsListener listener) {
        mListeners.remove(listener);
    }

    public static void showReasoningDialog(String title, String message, @Nullable String positive, @Nullable String negative, OnDialogDismiss listener) {
        NotificationDialog dialog = NotificationDialog.newInstance(title, message, positive, negative, null);
        FragmentManager manager;
        if (listener == null) {
            throw new IllegalArgumentException("Your listener can't be null to show reasoning, must be an instance" +
                    "of FragmentActivity or v4.Fragment AND implement OnDialogDismiss");
        } else if (listener instanceof FragmentActivity) {
            manager = ((FragmentActivity) listener).getSupportFragmentManager();
        } else if (listener instanceof Fragment) {
            manager = ((Fragment) listener).getChildFragmentManager();
        } else {
            throw new IllegalArgumentException("Your listener must be an instance of FragmentActivity or v4.Fragment" +
                    " AND implement OnDialogDismiss");
        }
        dialog.show(manager, NotificationDialog.TAG);
    }

    @SuppressLint("NewApi")
    public static boolean isPermissionAllowed(Activity activity, String permission) {
        return !isAboveMarshmallow() || activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isAboveMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @Override
    public void onPermissionsUpdated(List<String> accepted, List<String> denied) {
        /**
         * if you get the following crash
         * java.lang.RuntimeException: Failure delivering result ResultInfo
         * its because we failed to remove the listener when a listener was no longer
         * valid
         * IE: We switch fragments, we should remove the listener in onPause/onDetach/onStop
         *
         * The listener automatically gets added to a list of listeners and should be removed when no longer
         * valid.
         */
       for (PermissionsRequestsListener l : mListeners) {
           l.onPermissionsUpdated(accepted, denied);
       }
    }
}
