/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.general;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryListener;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryTask;
import com.universalstudios.orlandoresort.controller.userinterface.detail.ImageDetailFragment;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.tridion.MobilePage;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class BasicInfoDetailFragment extends Fragment {
	private static final String TAG = BasicInfoDetailFragment.class.getSimpleName();

	static final String KEY_ARG_TITLE = "KEY_ARG_TITLE";
	static final String KEY_ARG_DESCRIPTION = "KEY_ARG_DESCRIPTION";
	private static final String COKE_PAGE_IDENTIFIER = "COKE_FREESTYLE";

	private String mTitle, mDescription;
	private TextView mTitleText, mDescText;

	private Picasso mPicasso;
	private PicassoProvider mParentPicassoProvider;
	private ImageDetailFragment imageDetailFragment;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private ImageView mImageView;

	public static BasicInfoDetailFragment newInstance(Integer titleResId, Integer descriptionResId) {

		// Add parameters to the argument bundle
		String title = null;
		String description = null;
		if (titleResId != null) {
			title = UniversalOrlandoApplication.getAppContext().getString(titleResId);
		}
		if (descriptionResId != null) {
			description = UniversalOrlandoApplication.getAppContext().getString(descriptionResId);
		}

		return newInstance(title, description);
	}

	public static BasicInfoDetailFragment newInstance(String title, String description) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance");
		}

		// Create a new fragment instance
		BasicInfoDetailFragment fragment = new BasicInfoDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		if (!TextUtils.isEmpty(title)) {
			args.putString(KEY_ARG_TITLE, title);
		}
		if (!TextUtils.isEmpty(description)) {
			args.putString(KEY_ARG_DESCRIPTION, description);
		}
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}


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
			mTitle = "";
			mDescription = "";
		}
		// Otherwise, set incoming parameters
		else {
			mTitle = args.getString(KEY_ARG_TITLE, "");
			mDescription = args.getString(KEY_ARG_DESCRIPTION, "");
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {

			if (null != mDescription) {
				if (mDescription.equals(getString(R.string.detail_basic_info_child_swap_description))) {
					// Track the page view
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
							AnalyticsUtils.CONTENT_SUB_1_FAQ,
							AnalyticsUtils.CONTENT_SUB_2_CHILD_SWAP,
							AnalyticsUtils.PROPERTY_NAME_PARKS,
							null, null);
				} else if (mDescription.equals(getString(R.string.detail_basic_info_single_rider_description))) {
					// Track the page view
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
							AnalyticsUtils.CONTENT_SUB_1_FAQ,
							AnalyticsUtils.CONTENT_SUB_2_SINGLE_RIDER,
							AnalyticsUtils.PROPERTY_NAME_PARKS,
							null, null);
				}
			}

		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(getContext()).loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
				.downloader(mUniversalOrlandoImageDownloader).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_detail_basic_info, container, false);

		// Setup Views
		mTitleText = (TextView) fragmentView.findViewById(R.id.fragment_detail_basic_info_title_text);
		mDescText = (TextView) fragmentView.findViewById(R.id.fragment_detail_basic_info_desc_text);
		mImageView = (ImageView) fragmentView.findViewById(R.id.fragment_detail_basic_info_desc_image_view);


		// Set text
		mTitleText.setText(mTitle);
		mTitleText.setVisibility((null != mTitle && !mTitle.isEmpty()) ? View.VISIBLE : View.GONE);

		mDescText.setText(Html.fromHtml(mDescription));
		mDescText.setVisibility((null != mDescription && !mDescription.isEmpty()) ? View.VISIBLE : View.GONE);

        if (null != mDescription && mDescription.equals(getString(R.string.detail_basic_info_coke_freestyle_description))) {
            DatabaseQuery databaseQuery = new DatabaseQuery(UniversalOrlandoContentUris.MOBILE_PAGES.toString(), null, UniversalOrlandoDatabaseTables.MobilePagesTable.COL_IDENTIFIER + " = ?", new String[]{COKE_PAGE_IDENTIFIER}, null);
            DatabaseQueryTask<MobilePage> task = new DatabaseQueryTask<>(databaseQuery, MobilePage.class, new DatabaseQueryListener<List<MobilePage>>() {
                @Override
                public void onQueryComplete(List<MobilePage> results) {
                    if (null != results && !results.isEmpty()) {
                        //mDescText.setText(Html.fromHtml(result.get(0).shortDescription));
						mTitleText.setVisibility(View.VISIBLE);
						MobilePage result = results.get(0);
						mTitleText.setText(result.getTitleName());
                        mDescText.setText(result.getShortDescription());
						mImageView.setVisibility(View.VISIBLE);
						String mImageSizeParam = ImageUtils.getPoiImageSizeString(getResources().getInteger(R.integer.poi_detail_image_dpi_shift));

						Uri imageUriToLoad = Uri.parse(result.getThumbnailImageUrl()).buildUpon()
								.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
								.build();

						mPicasso.load(imageUriToLoad)
								.placeholder(R.drawable.ic_no_image_logo_detail)
								.fit()
								.into(mImageView);
                    }
                }
            });
            task.execute();
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
