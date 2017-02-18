/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.controller.userinterface.video.DetailPageVideoPlayActivity;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;

import java.lang.ref.WeakReference;

/**
 * 
 * 
 * @author Steven Byle
 */
public class ImageDetailFragment extends Fragment {
	private static final String TAG = ImageDetailFragment.class.getSimpleName();

	private static final String KEY_ARG_DETAIL_IMAGE_URI = "KEY_ARG_DETAIL_IMAGE_URI";
	private static final String KEY_ARG_IMAGE_POSITION = "KEY_ARG_IMAGE_POSITION";
	private static final String KEY_ARG_TOTAL_IMAGE_COUNT = "KEY_ARG_TOTAL_IMAGE_COUNT";
	private static final String KEY_ARG_VIDEO_IMAGE_URL = "KEY_ARG_VIDEO_IMAGE_URL";
	private static final String KEY_ARG_VIDEO_URL = "KEY_ARG_VIDEO_URL";
	private static final String KEY_ARG_IS_VIDEO = "KEY_ARG_IS_VIDEO";

	private String mImageSizeParam;
	private Uri mDetailImageUri;
	private PicassoProvider mParentPicassoProvider;
	private ImageView mDetailImage;
	private ImageView mDetailImageNoImage;
	private FrameLayout videoImageLayout,rootImageDetailLayout;
	private int mImagePosition;
	private int mTotalImageCount;
	private boolean mIsVideo;
	private String mVideoUrl;
	private String mVideoImageUrl;
	private Uri mVideoImageUri;
	private ImageView videoPlayImage;


	public static ImageDetailFragment newInstance(Uri detailImageUri, int imagePosition, int totalImageCount) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: detailImageUri = " + detailImageUri);
		}

		// Create a new fragment instance
		ImageDetailFragment fragment = new ImageDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (detailImageUri != null) {
			args.putString(KEY_ARG_DETAIL_IMAGE_URI, detailImageUri.toString());
		}
		args.putInt(KEY_ARG_IMAGE_POSITION, imagePosition);
		args.putInt(KEY_ARG_TOTAL_IMAGE_COUNT, totalImageCount);
		fragment.setArguments(args);

		return fragment;
	}


	public static ImageDetailFragment newInstance(String videoImageUrl, String videoUrl, int imagePosition, boolean isThisVideo) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: videoImageUrl = " + videoImageUrl);
			Log.d(TAG, "newInstance: videoUrl = " + videoUrl);
			Log.d(TAG, "newInstance: KEY_ARG_IS_VIDEO = " + isThisVideo);

		}

		// Create a new fragment instance
		ImageDetailFragment fragment = new ImageDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		if (videoImageUrl != null) {
			args.putString(KEY_ARG_VIDEO_IMAGE_URL, videoImageUrl);
		}
		if(videoUrl !=null){
			args.putString(KEY_ARG_VIDEO_URL, videoUrl);
		}
		args.putInt(KEY_ARG_IMAGE_POSITION, imagePosition);
		args.putBoolean(KEY_ARG_IS_VIDEO,isThisVideo);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof PicassoProvider) {
			mParentPicassoProvider = (PicassoProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof PicassoProvider) {
			mParentPicassoProvider = (PicassoProvider) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mParentPicassoProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement PicassoProvider");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();



		if (args == null) {
			mDetailImageUri = null;
			mImagePosition = -1;
			mTotalImageCount = -1;
			mVideoImageUri = null;
			mVideoUrl = "";
		}
		// Otherwise, set incoming parameters
		else {

			mIsVideo = args.getBoolean(KEY_ARG_IS_VIDEO);

			if(mIsVideo){

				mVideoUrl = args.getString(KEY_ARG_VIDEO_URL);

				mVideoImageUri = null;
				mVideoImageUrl = args.getString(KEY_ARG_VIDEO_IMAGE_URL);
				if (mVideoImageUrl != null) {
					try {
						mDetailImageUri = Uri.parse(mVideoImageUrl);
					} catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onCreate: exception trying to parse video image URL", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}

			}else {

				mDetailImageUri = null;
				String detailImageUrl = args.getString(KEY_ARG_DETAIL_IMAGE_URI);
				if (detailImageUrl != null) {
					try {
						mDetailImageUri = Uri.parse(detailImageUrl);
					} catch (Exception e) {
						if (BuildConfig.DEBUG) {
							Log.e(TAG, "onCreate: exception trying to parse detail image URL", e);
						}

						// Log the exception to crittercism
						Crittercism.logHandledException(e);
					}
				}
				mTotalImageCount = args.getInt(KEY_ARG_TOTAL_IMAGE_COUNT);
			}

			mImagePosition = args.getInt(KEY_ARG_IMAGE_POSITION);

		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_detail_image, container, false);

		// Setup Views
		mDetailImage = (ImageView) fragmentView.findViewById(R.id.fragment_detail_image);
		mDetailImageNoImage = (ImageView) fragmentView.findViewById(R.id.fragment_detail_image_no_image_logo);
		videoImageLayout = (FrameLayout)fragmentView.findViewById(R.id.preview_layout);
		videoPlayImage = (ImageView) fragmentView.findViewById(R.id.VideoPreviewPlayButton);
		rootImageDetailLayout = (FrameLayout) fragmentView.findViewById(R.id.root_image_detail_framelayout);


		if(mIsVideo) {
			//set video layout visibility gone
			videoImageLayout.setVisibility(View.VISIBLE);

			mDetailImage.setVisibility(View.GONE);
			mDetailImageNoImage.setVisibility(View.GONE);

			videoPlayImage.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{

					Bundle args = DetailPageVideoPlayActivity.newInstanceBundle(mVideoImageUrl, mVideoUrl);
					startActivity(new Intent(getActivity(), DetailPageVideoPlayActivity.class).putExtras(args));

				}

			});

		}else{

			//videoView.setVisibility(View.GONE);
			videoImageLayout.setVisibility(View.GONE);

			// Assume there is no image
			mDetailImage.setVisibility(View.GONE);
			mDetailImageNoImage.setVisibility(View.VISIBLE);

			uploadDetailImage();

			// Set the content description for talkback
			String contentDescriptionString = getString(R.string.detail_image_x_of_x_formatted_content_description, mImagePosition + 1, mTotalImageCount);
			fragmentView.setContentDescription(contentDescriptionString);

		}

		return fragmentView;
	}

	private void uploadDetailImage(){

		// Try to load one if there is a single image
		if (mDetailImageUri != null && mParentPicassoProvider != null) {
			mImageSizeParam = ImageUtils.getPoiImageSizeString(getResources().getInteger(R.integer.poi_detail_image_dpi_shift));

			Uri imageUriToLoad = mDetailImageUri.buildUpon()
					.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
					.build();

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onCreateView: imageUriToLoad = " + imageUriToLoad);
			}

			Picasso picasso = mParentPicassoProvider.providePicasso();
			if (picasso != null) {
				picasso.load(imageUriToLoad).into(mDetailImage, new DetailImageCallback(mDetailImage, mDetailImageNoImage));
			}
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}

	// Private static class using weak references to prevent leaking a context
	private static final class DetailImageCallback implements Callback {
		private final WeakReference<ImageView> mImage;
		private final WeakReference<ImageView> mNoImage;

		public DetailImageCallback(ImageView image, ImageView noImage) {
			mImage = new WeakReference<ImageView>(image);
			mNoImage = new WeakReference<ImageView>(noImage);
		}

		@Override
		public void onSuccess() {
			ImageView image = mImage.get();
			ImageView noImage = mNoImage.get();

			if (image != null) {
				image.setVisibility(View.VISIBLE);
			}
			if (noImage != null) {
				noImage.setVisibility(View.GONE);
			}
		}

		@Override
		public void onError() {
			ImageView image = mImage.get();
			ImageView noImage = mNoImage.get();

			if (image != null) {
				image.setVisibility(View.GONE);
			}
			if (noImage != null) {
				noImage.setVisibility(View.VISIBLE);
			}
		}
	}


	// Private static class using weak references to prevent leaking a context
	private static final class DetailImageVideoCallback implements Callback {
		private final WeakReference<VideoView> mVideo;
		private final WeakReference<ImageView> mImage;

		public DetailImageVideoCallback(VideoView video, ImageView image) {
			mVideo = new WeakReference<VideoView>(video);
			mImage = new WeakReference<ImageView>(image);
		}

		@Override
		public void onSuccess() {
			VideoView video = mVideo.get();
			ImageView image = mImage.get();

			if (video != null) {
				video.setVisibility(View.VISIBLE);
			}
			if (image != null) {
				image.setVisibility(View.GONE);
			}
		}

		@Override
		public void onError() {
			VideoView video = mVideo.get();
			ImageView image = mImage.get();

			if (video != null) {
				video.setVisibility(View.GONE);
			}
			if (image != null) {
				image.setVisibility(View.VISIBLE);
			}
		}
	}
}
