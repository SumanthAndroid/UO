package com.universalstudios.orlandoresort.controller.userinterface.wayfinding;

import java.lang.ref.WeakReference;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.network.domain.wayfinding.Path;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;

public class PathFragmentPagerAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = PathFragmentPagerAdapter.class.getSimpleName();

	private final SparseArray<WeakReference<Fragment>> mPagerFragments;
	private JazzyViewPager mJazzyViewPager;
	private final List<Path> mPaths;

	public PathFragmentPagerAdapter(JazzyViewPager jazzyViewPager, FragmentManager fm, List<Path> paths) {
		super(fm);

		mJazzyViewPager = jazzyViewPager;
		mPagerFragments = new SparseArray<WeakReference<Fragment>>();
		mPaths = paths;
	}

	@Override
	public Fragment getItem(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getItem: position = " + position);
		}
		Path path = mPaths.get(position);
		return PathFragment.newInstance(path.toJson(), position + 1, getCount());
	}

	@Override
	public int getCount() {
		return mPaths.size();
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
