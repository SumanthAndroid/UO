package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;
import com.universalstudios.orlandoresort.model.network.domain.appointments.CreateAppointmentTimeResponse;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersAdapter;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.TicketsAppointmentTable;

import java.lang.ref.WeakReference;

/**
 *
 * Created by GOKHAN on 8/4/2016.
 */
public class AppointmentTicketListCursorAdapter extends CursorAdapter implements StickyListHeadersAdapter {
    private static final String TAG = AppointmentTicketListCursorAdapter.class.getSimpleName();

    private static SparseIntArray sPoiHeaderStringResIdMap;
    private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private final String mImageSizeParam;
    private final Picasso mPicasso;
    private String createdAppointmentTicketObjStr,CreatedAppointmentTimeStr;
    private CreateAppointmentTimeResponse createdAppointmentTicketJson;
    private AppointmentTimes CreatedAppointmentTimeJson;
    private Boolean hasBeenRead;
    private Context mContext;


    public AppointmentTicketListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "AppointmentTicketListCursorAdapter constructor");
        }

        mContext = context;
        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
                CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);
        mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_search_image_dpi_shift));

        mPicasso = new Picasso.Builder(context)
                .loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader)
                .build();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.list_appointment_ticket_item, parent, false);

        SearchRowViewHolder holder = new SearchRowViewHolder();
        holder.displayNameText = (TextView) row.findViewById(R.id.list_appointment_ticket_item_display_name_text);
        holder.dateTimeText = (TextView) row.findViewById(R.id.list_appointment_item_date_time_text);
        holder.thumbnailImage = (ImageView) row.findViewById(R.id.list_appointment_ticket_item_image);
        holder.thumbnailImageNoImage = (ImageView) row.findViewById(R.id.list_appointment_ticket_item_no_image_logo);
        holder.rootContainer = row.findViewById(R.id.list_appointment_ticket_item_root_container);

        row.setTag(holder);


        return row;

    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {

        String appointmentTicket = cursor.getString(cursor.getColumnIndex(TicketsAppointmentTable.COL_APPOINTMENT_OBJECT_JSON));
        CreatedAppointmentTimeJson = GsonObject.fromJson(appointmentTicket, AppointmentTimes.class);

        createdAppointmentTicketObjStr = cursor.getString(cursor.getColumnIndex(TicketsAppointmentTable.COL_APPOINTMENT_TICKET_OBJECT_JSON));
        createdAppointmentTicketJson = GsonObject.fromJson(createdAppointmentTicketObjStr, CreateAppointmentTimeResponse.class);

        boolean hasBeenRead = (cursor.getInt(cursor.getColumnIndex(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.COL_HAS_BEEN_READ)) != 0);

        String ticketTime = getAppointmentTimeText(CreatedAppointmentTimeJson.getStartTime(),CreatedAppointmentTimeJson.getEndTime());

        // Apply data to view
        final SearchRowViewHolder holder = (SearchRowViewHolder) row.getTag();



// Set view state based on if the item has been read
        Resources r = context.getResources();
        holder.rootContainer.setBackgroundColor(
                hasBeenRead ? r.getColor(R.color.news_list_background_gray) : r.getColor(R.color.news_list_background_white));


        String thumbnailImageUrl =createdAppointmentTicketJson.getImageUrl();

        // Assume there is no image to start
        holder.thumbnailImage.setVisibility(View.GONE);
        holder.thumbnailImageNoImage.setVisibility(View.VISIBLE);

        // Load the search image
        if (thumbnailImageUrl != null && !thumbnailImageUrl.isEmpty()) {

            Uri thumbnailImageUri = null;
            try {
                thumbnailImageUri = Uri.parse(thumbnailImageUrl);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "bindView: invalid image URL: " + thumbnailImageUri, e);
                }

                // Log the exception to crittercism
                Crittercism.logHandledException(e);
            }

            if (thumbnailImageUri != null) {
                Uri imageUriToLoad = thumbnailImageUri.buildUpon()
                        .appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
                        .build();

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "bindView: imageUriToLoad = " + imageUriToLoad);
                }

                mPicasso.load(imageUriToLoad).into(holder.thumbnailImage, new SearchImageCallback(holder));
            }
        }


        String ticketDisplayName = CreatedAppointmentTimeJson.getTicketDisplayName();
        holder.displayNameText.setText(ticketDisplayName != null ? ticketDisplayName : "");
        holder.dateTimeText.setText(ticketTime !=null ? ticketTime : "");

    }

    private String getAppointmentTimeText(String startTime, String endTime){
        String appointmentTimeItem = "";
        if (!startTime.isEmpty()){
            if(!endTime.isEmpty()){
                appointmentTimeItem = startTime.substring(0,startTime.length()-3) + "-" + endTime;
            }
        }
        return appointmentTimeItem;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_appointment_ticket_section_header, parent, false);

            holder = new HeaderViewHolder();
            holder.headerTitleText = (TextView) convertView.findViewById(R.id.list_appointment_ticket_section_header_title_text);
            convertView.setTag(holder);
        }
        else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Cursor cursor = (Cursor) getItem(position);
        Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(TicketsAppointmentTable.COL_QUEUE_ENTITY_ID));
        String poiTypeName = CreatedAppointmentTimeJson.getQueueEntityType();

        AppointmentTimeTypeEnum enumval = AppointmentTimeTypeEnum.valueOf(poiTypeName);
//        // Get the proper header title, fall back to amenities if there isn't a specific one
        int poiHeaderStringResId = getPoiHeaderStringResId(poiTypeId,enumval);
        holder.headerTitleText.setText(poiHeaderStringResId);

      // holder.headerTitleText.setText("RIDE TICKETS");

        return convertView;
    }

    private static class HeaderViewHolder {
        TextView headerTitleText;
    }

    @Override
    public long getHeaderId(int position) {
        Cursor cursor = (Cursor) getItem(position);
        Integer poiTypeId = cursor.getInt(cursor.getColumnIndex(TicketsAppointmentTable.COL_QUEUE_ENTITY_ID));
        String poiTypeName = CreatedAppointmentTimeJson.getQueueEntityType();

        AppointmentTimeTypeEnum enumval = AppointmentTimeTypeEnum.valueOf(poiTypeName);
        // Get the proper header title, fall back to amenities if there isn't a specific one
        int poiHeaderStringResId = getPoiHeaderStringResId(poiTypeId,enumval );
        return poiHeaderStringResId;
    }

    public void destroy() {
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
    }


    private int getPoiHeaderStringResId(int poiTypeId, AppointmentTimeTypeEnum poiTypeName) {
        if (sPoiHeaderStringResIdMap == null) {
            sPoiHeaderStringResIdMap = new SparseIntArray();
        }

        //String name = poiTypeName.toUpperCase();
        //AppointmentTimeTypeEnum enumval = AppointmentTimeTypeEnum.valueOf(poiTypeName);
        switch(poiTypeName) {

            case RIDES:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_RIDE = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_RIDE, R.string.poi_list_header_rides);
                break;

            case Rides:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_RIDE = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_RIDE, R.string.poi_list_header_rides);
                break;

            case DINING:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_DINING = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_DINING, R.string.poi_list_header_dining);
                break;
            case SHOWS:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_SHOW = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_SHOW, R.string.poi_list_header_shows);
                break;
            case Shows:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_SHOW = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_SHOW, R.string.poi_list_header_shows);
                break;

            case SHOPPING:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_SHOP = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_SHOP, R.string.poi_list_header_shopping);
                break;

            case HOTELS:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_HOTEL = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_HOTEL, R.string.poi_list_header_hotels);
                break;
            case WATERPARKS:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_WATERPARK = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_WATERPARK, R.string.poi_list_header_waterparks);
                break;
            case EVENTS:
                TicketsAppointmentTable.VAL_POI_TYPE_ID_EVENT = poiTypeId;
                sPoiHeaderStringResIdMap.put(UniversalOrlandoDatabaseTables.TicketsAppointmentTable.VAL_POI_TYPE_ID_EVENT, R.string.poi_list_header_events);
                break;


        }

        return sPoiHeaderStringResIdMap.get(poiTypeId);
    }



    private static class SearchRowViewHolder {
        View rootContainer;
        TextView displayNameText;
        TextView dateTimeText;
        ImageView thumbnailImage;
        ImageView thumbnailImageNoImage;
    }

    // Private static class using weak references to prevent leaking a context
    private static class SearchImageCallback implements Callback {
        private final WeakReference<SearchRowViewHolder> mSearchRowViewHolder;

        public SearchImageCallback(SearchRowViewHolder searchRowViewHolder) {
            mSearchRowViewHolder = new WeakReference<SearchRowViewHolder>(searchRowViewHolder);
        }

        @Override
        public void onSuccess() {
            SearchRowViewHolder holder = mSearchRowViewHolder.get();
            if (holder != null) {
                if (holder.thumbnailImage != null) {
                    holder.thumbnailImage.setVisibility(View.VISIBLE);
                }
                if (holder.thumbnailImageNoImage != null) {
                    holder.thumbnailImageNoImage.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError() {
            SearchRowViewHolder holder = mSearchRowViewHolder.get();
            if (holder != null) {
                if (holder.thumbnailImage != null) {
                    holder.thumbnailImage.setVisibility(View.GONE);
                }
                if (holder.thumbnailImageNoImage != null) {
                    holder.thumbnailImageNoImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
