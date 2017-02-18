package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.lang.ref.WeakReference;
import java.util.List;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;

public class ImageDetailFragmentPagerAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = ImageDetailFragmentPagerAdapter.class.getSimpleName();

	private final SparseArray<WeakReference<Fragment>> mPagerFragments;
	private JazzyViewPager mJazzyViewPager;
	private final List<Uri> mDetailImageUris;
	private String mVideoImageUrl, mVideoUrl;

	public ImageDetailFragmentPagerAdapter(JazzyViewPager jazzyViewPager, FragmentManager fm, List<Uri> detailImageUris) {
		super(fm);

		mJazzyViewPager = jazzyViewPager;
		mPagerFragments = new SparseArray<WeakReference<Fragment>>();
		mDetailImageUris = detailImageUris;
	}

	public ImageDetailFragmentPagerAdapter(JazzyViewPager jazzyViewPager, FragmentManager fm, List<Uri> detailImageUris, String videoImageUrl, String videoUrl) {
		super(fm);
		mJazzyViewPager = jazzyViewPager;
		mPagerFragments = new SparseArray<WeakReference<Fragment>>();
		mDetailImageUris = detailImageUris;
		mVideoImageUrl = videoImageUrl;
		mVideoUrl = videoUrl;
	}

	@Override
	public Fragment getItem(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getItem: position = " + position);
		}

		if(position==0 && mVideoUrl != null){
			ImageDetailFragment imageDetailFragment =
					ImageDetailFragment.newInstance(mVideoImageUrl,mVideoUrl, position,true);
			return imageDetailFragment;


		}else {
			ImageDetailFragment imageDetailFragment =
					ImageDetailFragment.newInstance(mDetailImageUris.get(position), position, getCount());
			return imageDetailFragment;
		}
	}

	@Override
	public int getCount() {
		return mDetailImageUris.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "instantiateItem: position = " + position);
		}

		// Add reference to pager fragment to refer to later
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		mPagerFragments.put(position, new WeakReference <Fragment> (fragment));

		// Add reference for the view pager transitions
		mJazzyViewPager.setObjectForPosition(position, fragment);

		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "destroyItem: position = " + position);
		}

		// Remove reference of pager fragment
		mPagerFragments.remove(position);

		// Remove the reference for the view pager transitions
		mJazzyViewPager.removeObjectForPosition(position);

		super.destroyItem(container, position, object);
	}

	public Fragment getPagerFragment(int position) {
		WeakReference <Fragment> fragmentReference = mPagerFragments.get(position);
		if (fragmentReference != null) {
			return fragmentReference.get();
		}
		return null;
	}

	// Release references to objects that are contained in contexts
	public void destroy() {
		mJazzyViewPager = null;
	}
}
