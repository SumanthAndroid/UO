/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import java.lang.ref.WeakReference;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.view.imageview.TouchImageView;

/**
 * @author acampbell
 *
 */
public class DiningMenuDetailPagerAdapter extends PagerAdapter implements OnClickListener, OnTouchListener {

    private static final String TAG = DiningMenuDetailPagerAdapter.class.getSimpleName();

    private static final float MAX_ZOOM = 2;

    private Context mContext;
    private List<String> mMenuImages;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private Picasso mPicasso;
    private OnImageTouchedListener mListener;

    public interface OnImageTouchedListener {

        void onImageTouched();
    }

    public DiningMenuDetailPagerAdapter(OnImageTouchedListener listener, Context context,
            List<String> menuImages) {
        super();
        mMenuImages = menuImages;
        mContext = context;
        mListener = listener;

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(context).loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_dining_menu_page,
                container, false);

        ViewHolder holder = new ViewHolder();
        holder.position = position;
        holder.retryButton = view.findViewById(R.id.list_item_dining_menu_retry_button);
        holder.retryButton.setTag(holder);
        holder.retryButton.setOnClickListener(this);
        holder.errorContainter = view.findViewById(R.id.list_item_dining_menu_page_error_container);
        // Hide error message
        holder.errorContainter.setVisibility(View.GONE);
        holder.imageContainer = view.findViewById(R.id.list_item_dining_menu_page_image_container);
        holder.imageView = (TouchImageView) view.findViewById(R.id.list_item_dining_menu_page_imageview);
        holder.imageView.setMaxZoom(MAX_ZOOM);
        holder.imageView.setOnTouchListener(this);

        mPicasso.load(mMenuImages.get(position)).into(holder.imageView, new MenuImageCallback(holder));
        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mMenuImages.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(R.string.dining_menu_detail_page_format, position + 1, getCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_item_dining_menu_retry_button:
                ViewHolder holder = (ViewHolder) v.getTag();
                if (holder != null) {
                    // Attempt to reload image
                    mPicasso.load(mMenuImages.get(holder.position)).into(holder.imageView,
                            new MenuImageCallback(holder));
                } else {
                    Log.e(TAG, "onClick: Viewholder is null");
                }
                break;
        }
    }

    public void destroy() {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "destroy");
        }

        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
    }

    private static class ViewHolder {

        public View imageContainer;
        public TouchImageView imageView;
        public View errorContainter;
        public View retryButton;
        public int position;
    }

    private static final class MenuImageCallback implements Callback {

        private final WeakReference<ViewHolder> mViewHolder;

        public MenuImageCallback(ViewHolder holder) {
            mViewHolder = new WeakReference<ViewHolder>(holder);
        }

        @Override
        public void onSuccess() {
            if (BuildConfig.DEBUG) {
                Log.v(TAG, "onSuccess: Image Loaded successfully");
            }

            ViewHolder viewHolder = mViewHolder.get();
            if (viewHolder != null) {
                viewHolder.imageContainer.setVisibility(View.VISIBLE);
                viewHolder.errorContainter.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError() {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onError: Unable to load menu image");
            }

            ViewHolder viewHolder = mViewHolder.get();
            if (viewHolder != null) {
                viewHolder.imageContainer.setVisibility(View.GONE);
                viewHolder.errorContainter.setVisibility(View.VISIBLE);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mListener != null) {
            mListener.onImageTouched();
        }
        return false;
    }

}
