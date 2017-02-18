/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class TutorialPageFragment extends Fragment {
	private static final String TAG = TutorialPageFragment.class.getSimpleName();

	private static final String KEY_ARG_IMAGE_RES_ID = "KEY_ARG_IMAGE_RES_ID";
	private static final String KEY_ARG_TITLE_RES_ID = "KEY_ARG_TITLE_RES_ID";
	private static final String KEY_ARG_MESSAGE_RES_ID = "KEY_ARG_MESSAGE_RES_ID";

	private int mImageResId;
	private int mTitleResId;
	private int mMessageResId;
	private TextView mTitleText;
	private TextView mMessageText;
	private ImageView mImage;
	private int mCalculatedImageHeightDp;

	public static TutorialPageFragment newInstance(int imageResId, int titleResId, int messageResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		TutorialPageFragment fragment = new TutorialPageFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_IMAGE_RES_ID, imageResId);
		args.putInt(KEY_ARG_TITLE_RES_ID, titleResId);
		args.putInt(KEY_ARG_MESSAGE_RES_ID, messageResId);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
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
			mImageResId = -1;
			mTitleResId = -1;
			mMessageResId = -1;
		}
		// Otherwise, set incoming parameters
		else {
			mImageResId = args.getInt(KEY_ARG_IMAGE_RES_ID);
			mTitleResId = args.getInt(KEY_ARG_TITLE_RES_ID);
			mMessageResId = args.getInt(KEY_ARG_MESSAGE_RES_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		// Get the smallest (portrait) width in dp
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		float widthDp = displayMetrics.widthPixels / displayMetrics.density;
		float heightDp = displayMetrics.heightPixels / displayMetrics.density;
		float smallestWidthDp = Math.min(widthDp, heightDp);

		// Compute the height based on image aspect ratio 1080x1050 @ 480dpi
		mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (1050.0 / 1080.0));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_tutorial_page, container, false);

		// Setup Views
		mImage = (ImageView) fragmentView.findViewById(R.id.fragment_tutorial_page_image);
		mTitleText = (TextView) fragmentView.findViewById(R.id.fragment_tutorial_page_title_text);
		mMessageText = (TextView) fragmentView.findViewById(R.id.fragment_tutorial_page_message_text);

		// Set image height based on detail image aspect ratio
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCalculatedImageHeightDp, displayMetrics));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, calculatedImageHeightPx);
		mImage.setLayoutParams(layoutParams);

		if (mImageResId != -1) {
			mImage.setImageResource(mImageResId);
		}
		mImage.setVisibility(mImageResId != -1 ? View.VISIBLE : View.GONE);

		if (mTitleResId != -1) {
			mTitleText.setText(mTitleResId);
		}
		mTitleText.setVisibility(mTitleResId != -1 ? View.VISIBLE : View.GONE);

		if (mMessageResId != -1) {
			mMessageText.setText(mMessageResId);
		}
		mMessageText.setVisibility(mMessageResId != -1 ? View.VISIBLE : View.GONE);

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise, restore state
		else {
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

}
