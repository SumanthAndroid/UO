package com.universalstudios.orlandoresort.controller.userinterface.tutorial;

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

public class TutorialPageFragmentPagerAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = TutorialPageFragmentPagerAdapter.class.getSimpleName();

	private final SparseArray<WeakReference<Fragment>> mPagerFragments;
	private JazzyViewPager mJazzyViewPager;
	private final List<TutorialPage> mTutorialPages;

	public TutorialPageFragmentPagerAdapter(JazzyViewPager jazzyViewPager, FragmentManager fm, List<TutorialPage> tutorialPages) {
		super(fm);

		mJazzyViewPager = jazzyViewPager;
		mPagerFragments = new SparseArray<WeakReference<Fragment>>();
		mTutorialPages = tutorialPages;
	}

	@Override
	public Fragment getItem(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getItem: position = " + position);
		}
		TutorialPage tutorialPage = mTutorialPages.get(position);
		return TutorialPageFragment.newInstance(tutorialPage.getImageResId(), tutorialPage.getTitleResId(), tutorialPage.getMessageResId());
	}

	@Override
	public int getCount() {
		return mTutorialPages.size();
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
