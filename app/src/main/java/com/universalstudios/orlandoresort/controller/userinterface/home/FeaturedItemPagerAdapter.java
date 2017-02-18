package com.universalstudios.orlandoresort.controller.userinterface.home;

import java.lang.ref.WeakReference;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;

public class FeaturedItemPagerAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = FeaturedItemPagerAdapter.class.getSimpleName();

	private final SparseArray<WeakReference<Fragment>> mPagerFragments;
	private JazzyViewPager mJazzyViewPager;
	private final List<FeaturedItem> mFeaturedItemIds;

	public FeaturedItemPagerAdapter(JazzyViewPager jazzyViewPager, FragmentManager fm, List<FeaturedItem> featuredItemIds) {
		super(fm);

		mJazzyViewPager = jazzyViewPager;
		mPagerFragments = new SparseArray<WeakReference<Fragment>>();
		mFeaturedItemIds = featuredItemIds;
	}

	@Override
	public Fragment getItem(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getItem: position = " + position);
		}
		FeaturedItem featuredItem = mFeaturedItemIds.get(position);
		return FeaturedItemFragment.newInstance(featuredItem);
	}

	@Override
	public int getCount() {
		return mFeaturedItemIds.size();
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
