package com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kevin Haines on 5/5/16.
 * Project: Universal Orlando
 * Class Name: PermissionsFragment
 * Class Description: Retained Fragment to handle requesting permissions
 */
public class PermissionsFragment extends Fragment {
    public static final String TAG = "PermissionsFragment";
    private static final String ARG_PERMISSION = "ARG_PERMISSION";

    private String[] mPermissions;
    private int mRequestCode = new Random().nextInt(254);

    private PermissionsRequestsListener mListener;

    public static PermissionsFragment newInstance(String[] permission) {
        PermissionsFragment fragment = new PermissionsFragment();
        Bundle b = new Bundle();
        b.putStringArray(ARG_PERMISSION, permission);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setListener(PermissionsRequestsListener listener) {
        mListener = listener;
    }

    public void startRequest() {
        requestPermissions(mPermissions, mRequestCode);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPermissions = getArguments().getStringArray(ARG_PERMISSION);
        startRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            List<String> accepted = new ArrayList<>();
            List<String> denied = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    accepted.add(permissions[i]);
                } else {
                    denied.add(permissions[i]);
                }
            }
            if (mListener != null) {
                mListener.onPermissionsUpdated(accepted, denied);
            }
        }
    }
}
