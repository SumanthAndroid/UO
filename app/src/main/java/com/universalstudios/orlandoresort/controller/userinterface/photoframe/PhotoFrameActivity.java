package com.universalstudios.orlandoresort.controller.userinterface.photoframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrame;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrameExperience;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera.FramedCameraActivity;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/13/16.
 * Class: PhotoFrameActivity
 * Class Description: Activity to hold the Photo Frame selection and start the camera to
 * display the photo frame and image capture.
 */
public class PhotoFrameActivity extends ActionBarActivity {
    public static final String TAG = "Photofmeactiy";

    private static final String ARG_FRAME_LIST = "ARG_FRAME_LIST";

    private PhotoFrameExperience mExperience;
    private ViewGroup mContainer;

    public static Intent createIntent(Context context, PhotoFrameExperience experience) {
        Intent i = new Intent(context, PhotoFrameActivity.class);
        i.putExtra(ARG_FRAME_LIST, experience.toJson());
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_frames);
        mContainer = (ViewGroup) findViewById(R.id.container);

        String expString;
        if (null != savedInstanceState) {
            expString = savedInstanceState.getString(ARG_FRAME_LIST);
        } else {
            expString = getIntent().getStringExtra(ARG_FRAME_LIST);
        }

        if (!TextUtils.isEmpty(expString)) {
            mExperience = PhotoFrameExperience.fromJson(expString, PhotoFrameExperience.class);
           if(mExperience.getShortDescription() != null) {
               getActionBar().setTitle(mExperience.getShortDescription());
           }
            List<PhotoFrame> frames = mExperience.getPhotoFrames();
            if (null != frames && !frames.isEmpty()) {
                if (frames.size() == 1) {
                    startActivity(FramedCameraActivity.createIntent(this, frames.get(0).getDetailImageUrl()));
                    finish();
                } else {
                    showFramePicker(frames);
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_FRAME_LIST, mExperience.toJson());
        super.onSaveInstanceState(outState);
    }

    private void showFramePicker(List<PhotoFrame> frames) {
        FrameSelectionFragment fragment = (FrameSelectionFragment) getSupportFragmentManager().findFragmentByTag(FrameSelectionFragment.TAG);
        if (null == fragment) {
            fragment = FrameSelectionFragment.newInstance(frames);
            getSupportFragmentManager().beginTransaction()
                    .replace(mContainer.getId(), fragment, FrameSelectionFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
