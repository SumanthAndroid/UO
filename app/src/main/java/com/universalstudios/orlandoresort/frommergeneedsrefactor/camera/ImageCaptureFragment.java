package com.universalstudios.orlandoresort.frommergeneedsrefactor.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by Kevin Haines on 6/1/16.
 * Project: Universal Orlando
 * Class Name: ImageCaptureFragment
 * Class Description:
 */
public class ImageCaptureFragment extends Fragment {
    public static final String TAG = "ImageCaptureFragment";
    public static final String ARG_URL = "ARG_URL";
    private static final int IMAGE_REQUEST_CODE = 0;

    private String mUrl;
    private ImageCaptureListener mListener;

    public static ImageCaptureFragment newInstance(String url) {
        ImageCaptureFragment fragment = new ImageCaptureFragment();
        Bundle b = new Bundle();
        b.putString(ARG_URL, url);
        fragment.setArguments(b);
        return fragment;
    }

    public void setListener(ImageCaptureListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUrl = getArguments().getString(ARG_URL);

        File destination = new File(mUrl);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(destination));
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && null != mListener) {
                mListener.onImageCaptured(Uri.parse(mUrl));
            }
        }
    }
}
