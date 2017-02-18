package com.universalstudios.orlandoresort.controller.userinterface.explore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.explore.list.ExploreListFragment;
import com.universalstudios.orlandoresort.controller.userinterface.explore.map.ExploreMapFragment;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;

import java.lang.ref.WeakReference;

public class ExploreFragmentPagerAdapter extends FragmentPagerAdapter {
	private static final String TAG = ExploreFragmentPagerAdapter.class.getSimpleName();

	public static final int PAGE_MAP = 1000;
	public static final int PAGE_LIST = 1001;

	private final SparseArray<WeakReference<Fragment>> mPagerFragments;
	private JazzyViewPager mJazzyViewPager;
	private DatabaseQuery mDatabaseQueryList;
	private DatabaseQuery mDatabaseQueryMap;
	private final ExploreType mExploreType;
	private final SparseIntArray mPageIds;
	private final String mSelectedPoiObjectJson;

	public ExploreFragmentPagerAdapter(JazzyViewPager jazzyViewPager, FragmentManager fm, DatabaseQuery databaseQueryList,
			DatabaseQuery databaseQueryMap, ExploreType exploreType, String selectedPoiObjectJson) {
		super(fm);

		mJazzyViewPager = jazzyViewPager;
		mPagerFragments = new SparseArray<WeakReference<Fragment>>();
		mDatabaseQueryList = databaseQueryList;
		mDatabaseQueryMap = databaseQueryMap;
		mExploreType = exploreType;
		mSelectedPoiObjectJson = selectedPoiObjectJson;

		switch (mExploreType) {
			case ISLANDS_OF_ADVENTURE:
			case UNIVERSAL_STUDIOS_FLORIDA:
			case CITY_WALK_ORLANDO:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_DINING:
			case UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING:
			case CITY_WALK_HOLLYWOOD:
			case VOLCANO_BAY:
			case RIDES:
			case WAIT_TIMES:
			case SHOWS:
			case DINING:
			case SHOPPING:
			case RENTAL_SERVICES:
			case SHOPPING_EXPRESS_PASS:
			case HOTELS:
			case FAVORITES:
			case ATTRACTIONS_EXPRESS_PASS:
			case ATTRACTIONS_PACKAGE_PICKUP:
			case WET_N_WILD:
			case UNIVERSAL_DINING:
			case UNIVERSAL_DINING_QUICK_SERVICE:
				// Pages with map and a list page
				mPageIds = new SparseIntArray(2);
				mPageIds.append(0, PAGE_MAP);
				mPageIds.append(1, PAGE_LIST);
				break;
			default:
				// Pages with only a map
				mPageIds = new SparseIntArray(1);
				mPageIds.append(0, PAGE_MAP);
				break;
		}
	}

	@Override
	public Fragment getItem(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getItem: position = " + position);
		}

		int pageId = getPageId(position);
		switch (pageId) {
			case PAGE_MAP:
				return ExploreMapFragment.newInstance(mDatabaseQueryMap, mExploreType, mSelectedPoiObjectJson, null);
			case PAGE_LIST:
				return ExploreListFragment.newInstance(mDatabaseQueryList, mExploreType, (Long) null);
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return mPageIds.size();
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

	public int getPageId(int position) {
		return mPageIds.get(position, -1);
	}

	public int getPagePosition(int id) {
		return mPageIds.indexOfValue(id);
	}

	public void setDatabaseQueryList(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "setDatabaseQueryList");
		}

		mDatabaseQueryList = databaseQuery;
	}

	public void setDatabaseQueryMap(DatabaseQuery databaseQuery) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "setDatabaseQueryMap");
		}

		mDatabaseQueryMap = databaseQuery;
	}

	// Release references to objects that are contained in contexts
	public void destroy() {
		mJazzyViewPager = null;
		mDatabaseQueryList = null;
		mDatabaseQueryMap = null;
	}
}
