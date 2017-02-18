package com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * @author jamestimberlake
 * @created 12/1/15.
 */
public class FramedCamera extends SurfaceView implements SurfaceHolder.Callback {
    public static final String TAG = "framedCamera";

    SurfaceHolder holder;
    static Camera mCamera;
    private int currentCameraIndex = 0;
    private int totalCameraNum = 0;

    public FramedCamera(Context context, AttributeSet attrs) {
            super(context, attrs);

            holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            totalCameraNum = Camera.getNumberOfCameras();
            currentCameraIndex = 0;
    }

    public synchronized void switchCameraView(){
        currentCameraIndex += 1;
        if(currentCameraIndex >= totalCameraNum){
            currentCameraIndex = 0;
        }

        holder.removeCallback(this);

        try{
            mCamera.stopPreview();
            mCamera.release();
        } catch(Exception ex){

        }

        openCamera(currentCameraIndex);

        holder.addCallback(this);

        startCameraPreview();

    }

    public void reset(){
        mCamera.startPreview();
    }

    public void resetCamera(){
        switchCameraView();
    }

    /***
     *
     *  Take a picture and and convert it from bytes[] to Bitmap.
     *
     */
    public void takeAPicture(Camera.PictureCallback mPictureCallback){
        mCamera.takePicture(null, null, mPictureCallback);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        startCameraPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera(currentCameraIndex);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null) {
            this.getHolder().removeCallback(this);
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    private void openCamera(int cameraIndex){
        try {
            mCamera = Camera.open(cameraIndex);
            mCamera.enableShutterSound(true);
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            if(BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraPreview(){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.getSupportedPreviewSizes();
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }
}

