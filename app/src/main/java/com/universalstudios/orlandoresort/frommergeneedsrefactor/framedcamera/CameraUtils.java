package com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera;

import android.hardware.Camera;

/**
 * @author jamestimberlake
 * @created 12/2/15.
 */
public class CameraUtils {

    public static int numberOfCameras(){
        return Camera.getNumberOfCameras();
    }
}
