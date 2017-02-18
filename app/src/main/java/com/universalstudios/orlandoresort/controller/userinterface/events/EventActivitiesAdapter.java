package com.universalstudios.orlandoresort.controller.userinterface.events;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventActivity;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * @author acampbell
 *
 */
public class EventActivitiesAdapter extends BaseAdapter {

    private static final String TAG = EventActivitiesAdapter.class.getSimpleName();

    private List<EventActivity> mActivities;

    private Picasso mPicasso;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;

    public EventActivitiesAdapter(Context context, List<EventActivity> activities) {
        super();
        mActivities = activities;

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(context).loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();
    }

    @Override
    public int getCount() {
        return mActivities.size();
    }

    @Override
    public Object getItem(int position) {
        return mActivities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mActivities.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_event_activity_item,
                    parent, false);
        }

        if(convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.listImage = (ImageView) convertView.findViewById(R.id.list_event_activity_item_image);
            holder.listImageNoImage = (ImageView) convertView
                    .findViewById(R.id.list_event_activity_item_no_image_logo);
            holder.titleTextView = (TextView) convertView
                    .findViewById(R.id.list_event_activity_item_title_textview);
            holder.descriptionTextView = (TextView) convertView
                    .findViewById(R.id.list_event_activity_item_description_textview);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EventActivity eventActivity = (EventActivity) getItem(position);
        try {
            holder.titleTextView.setText(eventActivity.getDisplayName());
            holder.descriptionTextView.setText(eventActivity.getShortDescription());
        } catch( Exception ex){
            Log.d(TAG, "holder has hut a null pointer exception");
        }

        // Load the list image
        String listImageUrl = eventActivity.getThumbnailImageUrl();
        if (listImageUrl != null && !listImageUrl.isEmpty()) {
            Uri listImageUri = null;
            try {
                listImageUri = Uri.parse(listImageUrl);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "bindView: invalid image URL: " + listImageUri, e);
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }

            if (listImageUri != null) {
                mPicasso.load(listImageUri).into(holder.listImage, new ListImageCallback(holder));
            }
        } else {
            holder.listImage.setVisibility(View.GONE);
            holder.listImageNoImage.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void destroy() {
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
    }

    public static class ViewHolder {

        public ImageView listImage;
        public ImageView listImageNoImage;
        public TextView titleTextView;
        public TextView descriptionTextView;
    }

    // Private static class using weak references to prevent leaking a context
    private static final class ListImageCallback implements Callback {

        private final WeakReference<ViewHolder> mViewHolder;

        public ListImageCallback(ViewHolder viewHolder) {
            mViewHolder = new WeakReference<ViewHolder>(viewHolder);
        }

        @Override
        public void onSuccess() {
            ViewHolder holder = mViewHolder.get();
            if (holder != null) {
                if (holder.listImage != null) {
                    holder.listImage.setVisibility(View.VISIBLE);
                }

                if (holder.listImageNoImage != null) {
                    holder.listImageNoImage.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError() {
            ViewHolder holder = mViewHolder.get();
            if (holder != null) {
                if (holder.listImage != null) {
                    holder.listImage.setVisibility(View.GONE);
                }
                if (holder.listImageNoImage != null) {
                    holder.listImageNoImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
