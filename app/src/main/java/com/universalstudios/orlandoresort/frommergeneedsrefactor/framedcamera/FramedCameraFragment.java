package com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FramedCameraFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = FramedCameraFragment.class.getSimpleName();

    private static final String ARG_BITMAP = "ARG_BITMAP";

    private FramedCamera mCamera = null;
    private ImageButton cameraSwitchButton = null;
    private ImageButton takePictureButton = null;
    private LinearLayout overlayView = null;
    private ImageView overlayImage = null;
    String url;

    public static FramedCameraFragment newInstance(String url) {
        FramedCameraFragment framedCameraFragment = new FramedCameraFragment();
        Bundle b = new Bundle();
        b.putString(ARG_BITMAP, url);
        framedCameraFragment.setArguments(b);
        return framedCameraFragment;
    }

    private boolean didOpenFirstTime = false;

    private Camera.PictureCallback pictureCallback = null;

    private boolean isTakingPicture = false;

    public FramedCameraFragment() {
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                saveImage(data);
                mCamera.reset();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            url = getArguments().getString(ARG_BITMAP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.framed_camera, container, false);

        mCamera = (FramedCamera) layout.findViewById(R.id.parade_camera);
        cameraSwitchButton = (ImageButton) layout.findViewById(R.id.parade_btn_switch_camera);
        takePictureButton = (ImageButton) layout.findViewById(R.id.parade_btn_take_picture);
        overlayImage = (ImageView) layout.findViewById(R.id.parade_view_overlay_image);

        overlayView = (LinearLayout) layout.findViewById(R.id.parade_view_overlay_container);
        overlayView.setDrawingCacheEnabled(true);

        takePictureButton.setTag(R.id.parade_btn_take_picture);
        takePictureButton.setOnClickListener(this);

        if(CameraUtils.numberOfCameras() > 1) {
            cameraSwitchButton.setTag(R.id.parade_btn_switch_camera);
            cameraSwitchButton.setOnClickListener(this);
        } else {
            cameraSwitchButton.setVisibility(View.GONE);
        }

        Picasso.with(getActivity())
                .load(url)
                .into(overlayImage);

        return layout;
    }

    @Override
    public void onClick(View v) {

        if(!isTakingPicture) {
            Integer tag = (Integer) v.getTag();

            if (tag == R.id.parade_btn_switch_camera) {
                mCamera.switchCameraView();
            } else if (tag == R.id.parade_btn_take_picture) {
                isTakingPicture = true;
                overlayView.buildDrawingCache(true);
                mCamera.takeAPicture(pictureCallback);
                overlayView.destroyDrawingCache();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (didOpenFirstTime) {
            FrameLayout parent = ((FrameLayout) mCamera.getParent());
            parent.removeView(mCamera);
            mCamera = new FramedCamera(getActivity(), null);
            parent.addView(mCamera);
        } else {
            didOpenFirstTime = true;
        }
    }

    private void saveImage(final byte[] cameraData) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap mBitmap = BitmapFactory.decodeByteArray(cameraData, 0, cameraData.length, options);
                    Bitmap mergedBitmap = BitmapUtils.mergeBitmaps(mBitmap, overlayView.getDrawingCache(true), overlayView.getWidth(), overlayView.getHeight());
                    PhotoUtils.insertImage(getActivity().getContentResolver(), mergedBitmap, "Framed Camera", "Framed Pic");

                } catch(Exception ex){
                    if(BuildConfig.DEBUG) {
                        ex.printStackTrace();
                    }
                } finally {
                    FramedCameraFragment.this.isTakingPicture = false;
                }
            }
        }, 200);

        Toast.makeText(this.getActivity(), "Saved to gallery!", Toast.LENGTH_SHORT).show();
    }
}
