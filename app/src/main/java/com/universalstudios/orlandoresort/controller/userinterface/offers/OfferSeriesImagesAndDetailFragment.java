package com.universalstudios.orlandoresort.controller.userinterface.offers;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.detail.ImageDetailFragmentPagerAdapter;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils.PagerDotColor;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferSeries;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OfferSeriesTable;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;

/**
 * @author acampbell
 *
 */
public class OfferSeriesImagesAndDetailFragment extends DatabaseQueryFragment implements
        OnPageChangeListener, PicassoProvider {

    private static final String TAG = OfferSeriesImagesAndDetailFragment.class.getSimpleName();

    private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";

    private int mCurrentViewPagerTab;
    private int mCalculatedImageHeightDp;
    private TextView mNameText;
    private TextView mDescText;
    private RelativeLayout mViewPagerContainer;
    private JazzyViewPager mViewPager;
    private LinearLayout mPagerDotContainer;
    private View mBottomGradient;
    private ImageDetailFragmentPagerAdapter mImageDetailFragmentPagerAdapter;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private Picasso mPicasso;
    private boolean mHasLoadedData;

    public static OfferSeriesImagesAndDetailFragment newInstance(DatabaseQuery databaseQuery) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "newInstance: databaseQuery = " + databaseQuery);
        }

        // Create a new fragment instance
        OfferSeriesImagesAndDetailFragment fragment = new OfferSeriesImagesAndDetailFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument bundle
        if (databaseQuery != null) {
            args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
        }

        // Default parameters
        Bundle args = getArguments();
        if (args == null) {}
        // Otherwise, set incoming parameters
        else {}

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {}
        // Otherwise restore state, overwriting any passed in parameters
        else {}

        // Get the smallest (portrait) width in dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float smallestWidthDp = Math.min(widthDp, heightDp);

        // Compute the height based on image aspect ratio 1080x760 @ 480dpi
        mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));

        // Default state
        mHasLoadedData = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }

        // Inflate the fragment layout into the container
        View fragmentView = inflater.inflate(R.layout.fragment_offer_series_detail_images_and_title,
                container, false);

        // Setup Views
        mViewPagerContainer = (RelativeLayout) fragmentView
                .findViewById(R.id.fragment_offer_series_images_and_title_viewpager_container);
        mViewPager = (JazzyViewPager) fragmentView
                .findViewById(R.id.fragment_offer_series_images_and_title_viewpager);
        mPagerDotContainer = (LinearLayout) fragmentView
                .findViewById(R.id.fragment_offer_series_images_and_title_dot_layout);
        mBottomGradient = fragmentView
                .findViewById(R.id.fragment_offer_series_images_and_title_bottom_gradient);
        mNameText = (TextView) fragmentView
                .findViewById(R.id.fragment_offer_series_images_and_title_poi_name_text);
        mDescText = (TextView) fragmentView
                .findViewById(R.id.fragment_offer_series_images_and_title_poi_desc_text);

        mNameText.setVisibility(View.INVISIBLE);
        mDescText.setVisibility(View.INVISIBLE);
        mPagerDotContainer.setVisibility(View.GONE);
        mBottomGradient.setVisibility(View.GONE);

        // Set pager height based on detail image aspect ratio
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mCalculatedImageHeightDp, displayMetrics));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewPagerContainer
                .getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = calculatedImageHeightPx;
        mViewPagerContainer.setLayoutParams(layoutParams);

        mViewPager.setOnPageChangeListener(this);
        mViewPager.setTransitionEffect(TransitionEffect.Standard);
        mViewPager.setFadeEnabled(false);

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(getActivity())
                .loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();

        // If this is the first creation, default state variables
        if (savedInstanceState == null) {
            mCurrentViewPagerTab = mViewPager.getCurrentItem();
        }
        // Otherwise, restore state
        else {
            mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);
        }

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=")
                    + " null");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onViewStateRestored: savedInstanceState "
                    + (savedInstanceState == null ? "==" : "!=") + " null");
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onSaveInstanceState");
        }
        outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
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

        if (mImageDetailFragmentPagerAdapter != null) {
            mImageDetailFragmentPagerAdapter.destroy();
        }
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onDestroy");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPageSelected: " + position);
        }

        mCurrentViewPagerTab = position;

        // Set the proper dot on, and the others off
        int pageCount = mPagerDotContainer.getChildCount();
        if (pageCount > 1) {
            for (int i = 0; i < pageCount; i++) {
                View pagerDot = mPagerDotContainer.getChildAt(i);
                if (pagerDot != null) {
                    pagerDot.setBackgroundResource(ImagePagerUtils.getPagerDotResId(
                            i == mCurrentViewPagerTab, PagerDotColor.WHITE));
                }
            }
        }
    }

    @Override
    public Picasso providePicasso() {
        return mPicasso;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoadFinished");
        }

        switch (loader.getId()) {
            case LOADER_ID_DATABASE_QUERY:
                // Let the parent class check for double loads
                if (checkIfDoubleOnLoadFinished()) {
                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "onLoadFinished: ignoring update due to double load");
                    }
                    return;
                }

                // Pull out data from Offer
                if (data != null && data.moveToFirst()) {
                    String offerSeriesObjectJson = data.getString(data
                            .getColumnIndex(OfferSeriesTable.COL_OFFER_SERIES_OBJECT_JSON));

                    OfferSeries offerSeries = GsonObject.fromJson(offerSeriesObjectJson, OfferSeries.class);
                    String desc = TextUtils.isEmpty(offerSeries.getLongDescription()) ? offerSeries
                            .getShortDescription() : offerSeries.getLongDescription();
                    String name = offerSeries.getDisplayName();

                    mNameText.setText(name);
                    mNameText.setVisibility(TextUtils.isEmpty(name) ? View.GONE : View.VISIBLE);
                    mDescText.setText(desc);
                    mDescText.setVisibility(TextUtils.isEmpty(desc) ? View.GONE : View.VISIBLE);

                    // Only load new images if the data hasn't been loaded before
                    if (!mHasLoadedData) {

                        // Try to load valid detail image URIs
                        List<Uri> validImageUris = new ArrayList<Uri>();
                        List<String> imageUrls = offerSeries.getDetailImageUrls();
                        if (imageUrls != null) {
                            for (String imageUrl : imageUrls) {
                                if (imageUrl != null) {
                                    // Only add URIs that can be parsed
                                    try {
                                        Uri imageUri = Uri.parse(imageUrl);
                                        validImageUris.add(imageUri);
                                    } catch (Exception e) {
                                        if (BuildConfig.DEBUG) {
                                            Log.e(TAG, "onLoadFinished: invalid image URL: " + imageUrl, e);
                                        }

                                        // Log the exception to crittercism
                                        Crittercism.logHandledException(e);
                                    }
                                }
                            }
                        }

                        // Create the pager adapter to bind images, if any are there
                        List<Uri> imageUrisToLoad = validImageUris.size() > 0 ? validImageUris
                                : createDefaultUriList();
                        mImageDetailFragmentPagerAdapter = new ImageDetailFragmentPagerAdapter(mViewPager,
                                getChildFragmentManager(), imageUrisToLoad);
                        mViewPager.setAdapter(mImageDetailFragmentPagerAdapter);

                        // Clear out the dot pager indicator and add new ones
                        mPagerDotContainer.removeAllViews();
                        int pageCount = mImageDetailFragmentPagerAdapter.getCount();
                        if (pageCount > 1) {
                            for (int i = 0; i < pageCount; i++) {
                                View pagerDot = ImagePagerUtils.createPagerDotView(mPagerDotContainer,
                                        i == mCurrentViewPagerTab, PagerDotColor.WHITE);
                                mPagerDotContainer.addView(pagerDot);
                            }
                            mPagerDotContainer.setVisibility(View.VISIBLE);
                            mBottomGradient.setVisibility(View.VISIBLE);
                        } else {
                            mPagerDotContainer.setVisibility(View.GONE);
                            mBottomGradient.setVisibility(View.VISIBLE);
                        }
                    }

                    // Track that the data has been loaded once
                    mHasLoadedData = true;
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onLoaderReset");
        }

        switch (loader.getId()) {
            case LOADER_ID_DATABASE_QUERY:
                // Data is not available anymore, delete reference
                mHasLoadedData = false;
                break;
            default:
                break;
        }
    }

    private List<Uri> createDefaultUriList() {
        List<Uri> defaultUriList = new ArrayList<Uri>();
        defaultUriList.add(null);
        return defaultUriList;
    }

}
