package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventSeriesTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author acampbell
 *
 */
public class EventSeriesCarouselCursorAdapter extends CursorAdapter implements OnClickListener {

    private static final String TAG = EventSeriesCarouselCursorAdapter.class.getSimpleName();

    private OnEventSeriesCarouselChildClickListener mListener;
    private Picasso mPicasso;
    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private String mImageSizeParam;

    public EventSeriesCarouselCursorAdapter(OnEventSeriesCarouselChildClickListener lisetener,
            Context context, Cursor c) {
        super(context, c, 0);
        mListener = lisetener;

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
        mPicasso = new Picasso.Builder(context).loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();

        mImageSizeParam = ImageUtils.getPoiImageSizeString(
                context.getResources().getInteger(R.integer.poi_list_image_dpi_shift));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_event_series_carousel_item, parent, false);
        String eventSeriesObjectJson = cursor.getString(cursor
                .getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
        String venueObjectJson = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

        EventSeriesViewHolder holder = new EventSeriesViewHolder(view);
        holder.displayNameText = (TextView) view
                .findViewById(R.id.list_event_series_carousel_item_display_name_textview);
        holder.venueNameText = (TextView) view
                .findViewById(R.id.list_event_series_carousel_item_venue_name_textview);
        holder.descriptionTextView = (TextView) view
                .findViewById(R.id.list_event_series_carousel_item_description_textview);
        holder.eventDateTextView = (TextView) view
                .findViewById(R.id.list_event_series_carousel_item_event_date_textview);
        holder.descriptionTextView = (TextView) view
                .findViewById(R.id.list_event_series_carousel_item_description_textview);
        holder.listImage = (ImageView) view.findViewById(R.id.list_event_series_carousel_item_image);
        holder.listImageNoImage = (ImageView) view
                .findViewById(R.id.list_event_series_carousel_item_no_image_logo);

        // Circle badge
        holder.circleBadgeRootLayout = (LinearLayout) view
                .findViewById(R.id.poi_item_circle_badge_root_container);
        holder.waitTimeLayout = (LinearLayout) view.findViewById(R.id.poi_item_circle_badge_wait_time_layout);
        holder.waitTimeMinNumText = (TextView) view
                .findViewById(R.id.poi_item_circle_badge_wait_time_num_text);
        holder.showTimeLayout = (FrameLayout) view.findViewById(R.id.poi_item_circle_badge_show_time_layout);
        holder.showTimeBackgroundGray = view
                .findViewById(R.id.poi_item_circle_badge_show_time_background_gray);
        holder.showTimeBackgroundBlue = view
                .findViewById(R.id.poi_item_circle_badge_show_time_background_blue);
        holder.showTimeStartsTimeText = (TextView) view
                .findViewById(R.id.poi_item_circle_badge_show_time_starts_time_text);
        holder.showTimeStartsText = (TextView) view
                .findViewById(R.id.poi_item_circle_badge_show_time_starts_text);
        holder.showTimeOpensText = (TextView) view
                .findViewById(R.id.poi_item_circle_badge_show_time_opens_text);
        holder.showTimeAmPmText = (TextView) view
                .findViewById(R.id.poi_item_circle_badge_show_time_am_pm_text);
        holder.closedLayout = (LinearLayout) view.findViewById(R.id.poi_item_circle_badge_closed_layout);
        holder.closedText = (TextView) view.findViewById(R.id.poi_item_circle_badge_closed_text);
        holder.closedWeatherText = (TextView) view.findViewById(R.id.poi_item_circle_badge_weather_text);
        holder.closedTemporaryText = (TextView) view.findViewById(R.id.poi_item_circle_badge_temporary_text);
        holder.closedCapacityText = (TextView) view
                .findViewById(R.id.poi_item_circle_badge_closed_capacity_text);
        holder.eventDisabledView = view.findViewById(R.id.list_event_series_carousel_item_diabled_view);

        holder.eventRootContentRelativeLayout = (RelativeLayout) view
                .findViewById(R.id.list_event_series_carousel_item_root_content_relative_layout);
        holder.eventRootContentRelativeLayout.setTag(
                R.id.key_view_tag_event_series_list_event_series_object_json, eventSeriesObjectJson);
        holder.eventRootContentRelativeLayout.setTag(R.id.key_view_tag_event_series_list_venue_object_json,
                venueObjectJson);
        holder.eventRootContentRelativeLayout.setOnClickListener(this);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View row, final Context context, Cursor cursor) {
        EventSeriesViewHolder holder = (EventSeriesViewHolder) row.getTag();
        EventSeries eventSeries = GsonObject.fromJson(
                cursor.getString(cursor.getColumnIndex(EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON)),
                EventSeries.class);
        String heroImageUrl = cursor.getString(cursor.getColumnIndex(EventSeriesTable.COL_LIST_IMAGE_URL));
        String venueName = cursor.getString(cursor.getColumnIndex(VenuesTable.COL_ALIAS_DISPLAY_NAME));
        // Default venue name if null
        venueName = venueName == null ? context.getString(R.string.event_venue_name_default) : venueName;
        String displayName, description;
        List<EventDate> eventDates;
        long eventEndDateUnix = -1;
        boolean singleEvent = eventSeries.getEvents() != null && eventSeries.getEvents().size() == 1;
        Integer waitTime = null;

        // If event series has one event use it's information for the event series card
        if (singleEvent) {
            Event event = eventSeries.getEvents().get(0);
            displayName = event.getDisplayName();
            description = event.getShortDescription();
            eventDates = event.getEventDates();
            EventDate eventDate = EventUtils.getEndDate(event.getEventDates());
            if (eventDate != null && eventDate.getEndDateUnix() != null) {
                eventEndDateUnix = eventDate.getEndDateUnix();
            }
            waitTime = event.getWaitTime();
        } else {
            displayName = cursor.getString(cursor.getColumnIndex(EventSeriesTable.COL_ALIAS_DISPLAY_NAME));
            description = eventSeries.getShortDescription();
            eventDates = eventSeries.getEventDates();
            eventEndDateUnix = cursor.getLong(cursor.getColumnIndex(EventSeriesTimesTable.COL_END_DATE));
        }
        holder.displayNameText.setText(displayName);
        holder.venueNameText.setText(venueName);
        holder.descriptionTextView.setText(description);
        // Only use short date span style if dates span multiple days
        boolean shortStyle = EventUtils.eventDatesSpanMultipleDays(eventDates);
        holder.eventDateTextView.setText(EventUtils.getEventDateSpan(eventDates, shortStyle,
                eventSeries.getDatePlaceholder(), true));

        // Updated circle badge
        if (singleEvent) {
            if (waitTime == null) {
                EventUtils.updatePoiCircleBadgeForEvent(eventDates, holder, false);
            } else {
                PoiUtils.updatePoiCircleBadgeForRide(waitTime, null, null, holder);
            }
            // Hide circle badge if date passed
            if (holder.showTimeBackgroundGray.getVisibility() == View.VISIBLE) {
                holder.circleBadgeRootLayout.setVisibility(View.GONE);
            }
        } else {
            holder.circleBadgeRootLayout.setVisibility(View.GONE);
        }

        // Load event series hero image
        if (!TextUtils.isEmpty(heroImageUrl)) {
            Uri listImageUri = null;
            try {
                listImageUri = Uri.parse(heroImageUrl);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "bindView: invalid image URL: " + listImageUri, e);
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }

            if (listImageUri != null) {
                Uri imageUriToLoad = listImageUri.buildUpon()
                        .appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam).build();

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
                }

                mPicasso.load(imageUriToLoad).into(holder.listImage, new HeroImageCallback(holder));
            }
        } else {
            holder.listImage.setVisibility(View.GONE);
            holder.listImageNoImage.setVisibility(View.VISIBLE);
        }

        // Update past events
        Date now = new Date();
        if (!cursor.isNull(cursor.getColumnIndex(EventSeriesTimesTable.COL_END_DATE))) {

            Date eventEndDate = new Date(eventEndDateUnix * 1000);
            // Disable event if start time has passed
            if (now.after(eventEndDate)) {
                holder.eventDisabledView.setVisibility(View.VISIBLE);
                holder.eventRootContentRelativeLayout.setClickable(false);
            } else {
                holder.eventDisabledView.setVisibility(View.GONE);
                holder.eventRootContentRelativeLayout.setClickable(true);
            }
        }
        // Events with unknown start dates should be enabled
        else {
            holder.eventDisabledView.setVisibility(View.GONE);
            holder.eventRootContentRelativeLayout.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.list_event_series_carousel_item_root_content_relative_layout:
                    String eventSeriesObjectJson = (String) v
                            .getTag(R.id.key_view_tag_event_series_list_event_series_object_json);
                    String venueObjectJson = (String) v
                            .getTag(R.id.key_view_tag_event_series_list_venue_object_json);
                    if (eventSeriesObjectJson != null) {
                        mListener.onEventCarouselChildClick(v, venueObjectJson, eventSeriesObjectJson);
                    }
                    break;
            }
        }
    }

    public void destroy() {
        mListener = null;
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
    }

    // Private static class using weak references to prevent leaking a context
    private static final class HeroImageCallback implements Callback {

        private final WeakReference<PoiViewHolder> mPoiViewHolder;

        public HeroImageCallback(PoiViewHolder poiViewHolder) {
            mPoiViewHolder = new WeakReference<PoiViewHolder>(poiViewHolder);
        }

        @Override
        public void onSuccess() {
            PoiViewHolder holder = mPoiViewHolder.get();
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
            PoiViewHolder holder = mPoiViewHolder.get();
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
