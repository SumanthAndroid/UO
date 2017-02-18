package com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsRequestsListener;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/13/16.
 * Class: FramedCameraActivity
 * Class Description: Activity for containing the {@link FramedCameraFragment}
 */
public class FramedCameraActivity extends FragmentActivity implements PermissionsRequestsListener {
    public static final String TAG = "FramedCameraActivity";
    private static final String ARG_BITMAP = "ARG_BITMAP";
    private String mUrl;

    public static Intent createIntent(Context context, String url) {
        Intent i = new Intent(context, FramedCameraActivity.class);
        i.putExtra(ARG_BITMAP, url);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.framed_camera_activity);
        mUrl = getIntent().getStringExtra(ARG_BITMAP);

        PermissionsManager.getInstance().handlePermissionRequest(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPermissionsUpdated(List<String> accepted, List<String> denied) {
        if (!denied.isEmpty()) {
            Toast.makeText(this,
                    "You must allow all required permissions to use this feature.",
                    Toast.LENGTH_LONG)
                        .show();
            finish();
        } else if (!this.isDestroyed()) {
            FramedCameraFragment framedCameraFragment = FramedCameraFragment.newInstance(mUrl);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, framedCameraFragment, FramedCameraFragment.TAG)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }
}
