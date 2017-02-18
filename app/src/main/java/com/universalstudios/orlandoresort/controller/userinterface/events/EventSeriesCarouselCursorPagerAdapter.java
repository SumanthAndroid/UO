package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;

/**
 * 
 * @author acampbell
 *
 */
public class EventSeriesCarouselCursorPagerAdapter extends PagerAdapter {

    private static final String TAG = EventSeriesCarouselCursorPagerAdapter.class.getSimpleName();

    private EventSeriesCarouselCursorAdapter mCursorAdapter;
    private Context mContext;

    public EventSeriesCarouselCursorPagerAdapter(OnEventSeriesCarouselChildClickListener lisetener,
            Context context, Cursor cursor) {
        super();
        mContext = context;
        mCursorAdapter = new EventSeriesCarouselCursorAdapter(lisetener, context, cursor);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mCursorAdapter.getView(position, null, container);
        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    public void swapCursor(Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext
                .getString(R.string.event_series_carousel_page_number_format, position + 1, getCount());
    }

    public void destroy() {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "destroy");
        }
        if (mCursorAdapter != null) {
            mCursorAdapter.destroy();
        }
    }
}
