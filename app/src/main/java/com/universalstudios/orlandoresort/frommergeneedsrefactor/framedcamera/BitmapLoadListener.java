package com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera;

import android.graphics.Bitmap;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/22/16.
 * Class: BitmapLoadListener
 * Class Description: Interface for listening to bitmap loading
 */
public interface BitmapLoadListener {
    void onBitmapFetched(Bitmap bitmap);
}
