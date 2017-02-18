package com.universalstudios.orlandoresort.controller.userinterface.video;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.global.AnalyticsActivity;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;

/**
 *
 * Created by GOKHAN on 8/25/2016.
 */
public class DetailPageVideoPlayActivity extends AnalyticsActivity {

    private static final String TAG = DetailPageVideoPlayActivity.class.getSimpleName();
    private static final String KEY_ARG_VIDEO_URL = "KEY_ARG_VIDEO_URL";
    private static final String KEY_ARG_VIDEO_IMAGE_URL = "KEY_ARG_VIDEO_IMAGE_URL";
    private VideoView videoView;

    public static Bundle newInstanceBundle(String videoImageUrl, String videoUrl) {
        Bundle args = new Bundle();
        args.putString(KEY_ARG_VIDEO_URL, videoUrl);
        args.putString(KEY_ARG_VIDEO_IMAGE_URL,videoImageUrl);
        return args;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_detail_page_video_play);

        videoView = (VideoView)findViewById(R.id.videoView1);

        String videoPath = "";

        if(getIntent() != null && getIntent().getExtras() != null) {
            videoPath = getIntent().getExtras().getString(KEY_ARG_VIDEO_URL);
            savedInstanceState = new Bundle();
            savedInstanceState.putString(KEY_ARG_VIDEO_URL, videoPath);
        } else if (savedInstanceState != null && savedInstanceState.getString(KEY_ARG_VIDEO_URL) != null) {
            videoPath = savedInstanceState.getString(KEY_ARG_VIDEO_URL, "");
        }

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();
        videoView.bringToFront();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                UserInterfaceUtils.showToastFromForeground("Video Currently unavailable! Try it again later!", Toast.LENGTH_SHORT, getApplicationContext());
                finish();
                return false;
            }
        });

        videoView.start();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
