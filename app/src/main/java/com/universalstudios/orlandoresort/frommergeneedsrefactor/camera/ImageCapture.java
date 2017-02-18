package com.universalstudios.orlandoresort.frommergeneedsrefactor.camera;

import android.Manifest;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsManager;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions.PermissionsRequestsListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

/**
 * Created by Kevin Haines on 6/1/16.
 * Project: Universal Orlando
 * Class Name: ImageCapture
 * Class Description:
 */
public class ImageCapture implements PermissionsRequestsListener, ImageCaptureListener {
    public static final String TAG = "ImageCapture";

    private String imageUrl;
    private ImageCaptureListener mListener;
    private WeakReference<FragmentActivity> mActivityRef;

    private ImageCapture(String url, ImageCaptureListener listener, FragmentActivity activity) {
        this.mListener = listener;
        this.imageUrl = url;
        mActivityRef = new WeakReference<>(activity);
    }

    public static void takePicture(FragmentActivity activity, @Nullable String url, @NonNull ImageCaptureListener listener) {
        if (null == activity) {
            return;
        }

        ImageCapture capture = new ImageCapture(url, listener, activity);
        capture.startCamera();
    }

    private void startCamera() {
        if (null == mActivityRef || null == mActivityRef.get()) {
            return;
        }

        PermissionsManager.getInstance().handlePermissionRequest(mActivityRef.get(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, this);
    }

    @Override
    public void onPermissionsUpdated(List<String> accepted, List<String> denied) {
        if (denied.isEmpty()) {
            if (null != mActivityRef && null != mActivityRef.get()) {
                ImageCaptureFragment fragment = ImageCaptureFragment.newInstance(imageUrl != null ? imageUrl
                        : Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID().toString() + ".jpg");
                FragmentTransaction transaction = mActivityRef.get().getSupportFragmentManager().beginTransaction();
                transaction.add(fragment, ImageCaptureFragment.TAG);
                transaction.commitAllowingStateLoss();
                fragment.setListener(this);
            }
        }
    }

    @Override
    public void onImageCaptured(Uri uri) {
        if (null != mListener) {
            mListener.onImageCaptured(uri);
        }
    }

    @Override
    public void onCaptureFailed() {
        if (null != mListener) {
            mListener.onCaptureFailed();
        }
    }
}
