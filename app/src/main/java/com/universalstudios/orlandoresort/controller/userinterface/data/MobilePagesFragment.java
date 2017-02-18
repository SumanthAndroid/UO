package com.universalstudios.orlandoresort.controller.userinterface.data;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.tridion.MobilePage;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;

/**
 * Mobile pages fragment
 * Created by Nicholas Hanna on 12/5/2016.
 */
public abstract class MobilePagesFragment extends DatabaseQueryFragment {

    public static final String TAG = MobilePagesFragment.class.getSimpleName();

    public static final int LOADER_ID_MOBILE_PAGES = LoaderUtils.LOADER_ID_MOBILE_PAGES_FRAGMENT;

    private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";

    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;

    private Picasso mPicasso;

    private DatabaseQuery mDatabaseQuery;

    protected MobilePage mMobilePage = new MobilePage();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
                CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(getActivity())
                .debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();

        mDatabaseQuery = DatabaseQueryUtils.getMobilePageDatabaseQuery(getPageId());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle loaderArgs = new Bundle();

        LoaderUtils.initFragmentLoaderWithHandler(this, savedInstanceState, LOADER_ID_MOBILE_PAGES, loaderArgs);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case LOADER_ID_MOBILE_PAGES:
                return LoaderUtils.createCursorLoader(mDatabaseQuery);
            default:
                return super.onCreateLoader(id, args);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_MOBILE_PAGES:
                if(data != null && data.moveToFirst()) {
                    mMobilePage = mMobilePage.unwrap(data);
                }
                onMobilePageLoaded();
                break;
        }
    }

    public Picasso getPicasso() {
        if(BuildConfig.DEBUG && mPicasso == null) {
            Log.w(TAG, "Picasso is not setup yet");
        }
        return mPicasso;
    }

    public abstract String getPageId();
    public abstract void onMobilePageLoaded();
}
