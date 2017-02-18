/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.wayfinding;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Path;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class PathFragment extends Fragment {
	private static final String TAG = PathFragment.class.getSimpleName();

	private static final String KEY_ARG_SELECTED_PATH_OBJECT_JSON = "KEY_ARG_SELECTED_PATH_OBJECT_JSON";
	private static final String KEY_ARG_STEP_NUMBER = "KEY_ARG_STEP_NUMBER";
	private static final String KEY_ARG_TOTAL_STEPS = "KEY_ARG_TOTAL_STEPS";

	private String mPathObjectJson;
	private int mStepNumber;
	private int mTotalSteps;
	private String mImageSizeParam;
	private PicassoProvider mParentPicassoProvider;
	private TextView mStepText;
	private TextView mInstructionsText;
	private TextView mCommentsText;
	private ImageView mPathImage;
	private ImageView mPathImageNoImage;
	private Path mPath;

	public static PathFragment newInstance(String pathObjectJson, int stepNumber, int totalSteps) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: pathObjectJson = " + pathObjectJson);
		}

		// Create a new fragment instance
		PathFragment fragment = new PathFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putString(KEY_ARG_SELECTED_PATH_OBJECT_JSON, pathObjectJson);
		args.putInt(KEY_ARG_STEP_NUMBER, stepNumber);
		args.putInt(KEY_ARG_TOTAL_STEPS, totalSteps);
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
			mPathObjectJson = null;
			mPath = null;
			mStepNumber = -1;
			mTotalSteps = -1;
		}
		// Otherwise, set incoming parameters
		else {
			mPathObjectJson = args.getString(KEY_ARG_SELECTED_PATH_OBJECT_JSON);
			mPath = GsonObject.fromJson(mPathObjectJson, Path.class);
			mStepNumber = args.getInt(KEY_ARG_STEP_NUMBER);
			mTotalSteps = args.getInt(KEY_ARG_TOTAL_STEPS);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		mImageSizeParam = ImageUtils.getPoiImageSizeString(getResources().getInteger(R.integer.poi_wayfinding_image_dpi_shift));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_path, container, false);

		// Setup Views
		mPathImage = (ImageView) fragmentView.findViewById(R.id.fragment_path_image);
		mPathImageNoImage = (ImageView) fragmentView.findViewById(R.id.fragment_path_image_no_image_logo);
		mInstructionsText = (TextView) fragmentView.findViewById(R.id.fragment_path_instructions_text);
		mCommentsText = (TextView) fragmentView.findViewById(R.id.fragment_path_comment_text);
		mStepText = (TextView) fragmentView.findViewById(R.id.fragment_path_step_text);


		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
		}

		// Set the text
		String instructions = mPath.getInstructions();
		String comments = mPath.getComment();
		String stepFormat = getString(R.string.wayfinding_step_x_of_x);
		String stepText = String.format(stepFormat, mStepNumber, mTotalSteps);

		if (instructions != null && !instructions.isEmpty()) {
			mInstructionsText.setText(instructions);
			mInstructionsText.setVisibility(View.VISIBLE);
			mCommentsText.setText(comments != null ? comments : "");
			mCommentsText.setVisibility(comments != null ? View.VISIBLE : View.GONE);
		}
		else {
			mInstructionsText.setVisibility(View.GONE);
			mCommentsText.setText(getString(R.string.wayfinding_step_instructions_not_available));
			mCommentsText.setVisibility(View.VISIBLE);
		}
		mStepText.setText(stepText != null ? stepText : "");

		// Assume there is no image
		mPathImage.setVisibility(View.GONE);
		mPathImageNoImage.setVisibility(View.VISIBLE);

		// Check to see if the image URI can be parsed
		String imageUrl = mPath.getNavigationImageUrl();
		Uri imageUriToLoad = null;
		try {
			imageUriToLoad = Uri.parse(imageUrl);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "onCreateView: invalid image URL: " + imageUrl, e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}

		// Load the path image
		if (imageUriToLoad != null && mParentPicassoProvider != null) {
			imageUriToLoad = imageUriToLoad.buildUpon()
					.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
					.build();

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "onCreateView: imageUriToLoad = " + imageUriToLoad);
			}

			Picasso picasso = mParentPicassoProvider.providePicasso();
			if (picasso != null) {
				picasso.load(imageUriToLoad).into(mPathImage, new PathImageCallback(mPathImage, mPathImageNoImage));
			}
		}

		return fragmentView;
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
	private static final class PathImageCallback implements Callback {
		private final WeakReference<ImageView> mImage;
		private final WeakReference<ImageView> mNoImage;

		public PathImageCallback(ImageView image, ImageView noImage) {
			mImage = new WeakReference<ImageView>(image);
			mNoImage = new WeakReference<ImageView>(noImage);
		}

		@Override
		public void onSuccess() {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "PathImageCallback: onSuccess");
			}

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
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "PathImageCallback: onError");
			}

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

}
