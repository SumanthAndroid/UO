package com.universalstudios.orlandoresort.frommergeneedsrefactor.camera;

import android.net.Uri;

/**
 * Created by Kevin Haines on 6/1/16.
 * Project: Universal Orlando
 * Class Name: ImageCaptureListener
 * Class Description:
 */
public interface ImageCaptureListener {
    void onImageCaptured(Uri uri);
    void onCaptureFailed();
}
