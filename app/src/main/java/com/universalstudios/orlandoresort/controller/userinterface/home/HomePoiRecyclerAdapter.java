package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.events.EventUtils;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;
import com.universalstudios.orlandoresort.controller.userinterface.explore.list.ListImageCallback;
import com.universalstudios.orlandoresort.controller.userinterface.pois.PoiUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Dining;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Event;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Gateway;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Lockers;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.PointOfInterest;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Ride;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.Shop;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.EventTimesTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenueLandsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;
import com.universalstudios.orlandoresort.view.buttons.FavoriteToggleButton;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that wraps a {@link Cursor} to show POI items with an option to show
 * all as a footer.
 */
public class HomePoiRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = HomePoiRecyclerAdapter.class.getSimpleName();

    private Cursor mCursor;
    private final HomePoiRecyclerAdapter.OnItemClickListener mOnItemClickListener;
    private final ExploreType mExploreType;
    private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private final Picasso mPicasso;
    private final String mImageSizeParam;
    private final ViewAllType mViewAllType;

    public HomePoiRecyclerAdapter(Context context, Cursor cursor, ExploreType exploreType,
            ViewAllType viewAllType, HomePoiRecyclerAdapter.OnItemClickListener onItemClickListener) {
        mCursor = cursor;
        mExploreType = exploreType;
        mViewAllType = viewAllType;
        mOnItemClickListener = onItemClickListener;

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
                CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(context)
                .loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader)
                .build();
        mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_list_image_dpi_shift));
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            // Add an item for the footer view
            return mCursor.getCount() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ViewAllViewHolder.LAYOUT_RES_ID;
        } else if (position >= 0) {
            return PoiItemViewHolder.LAYOUT_RES_ID;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case PoiItemViewHolder.LAYOUT_RES_ID:
                return new PoiItemViewHolder(layoutInflater.inflate(PoiItemViewHolder.LAYOUT_RES_ID, parent, false));
            case ViewAllViewHolder.LAYOUT_RES_ID:
                return new ViewAllViewHolder(layoutInflater.inflate(ViewAllViewHolder.LAYOUT_RES_ID, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case PoiItemViewHolder.LAYOUT_RES_ID:
                // Let the cursor adapter bind the view for the news items
                if (mCursor != null) {
                    mCursor.moveToPosition(position);
                    final Context context = viewHolder.itemView.getContext();

                    // Get data from database row
                    final Integer poiTypeId = mCursor.getInt(mCursor.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
                    final Long poiId = mCursor.getLong(mCursor.getColumnIndex(PointsOfInterestTable.COL_POI_ID));
                    String displayName = mCursor.getString(mCursor.getColumnIndex(PointsOfInterestTable.COL_ALIAS_DISPLAY_NAME));
                    final String poiObjectJson = mCursor.getString(mCursor.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
                    String listImageUrl = mCursor.getString(mCursor.getColumnIndex(PointsOfInterestTable.COL_LIST_IMAGE_URL));
                    boolean isRoutable = (mCursor.getInt(mCursor.getColumnIndex(PointsOfInterestTable.COL_IS_ROUTABLE)) != 0);
                    boolean isFavorite = (mCursor.getInt(mCursor.getColumnIndex(PointsOfInterestTable.COL_IS_FAVORITE)) != 0);
                    String venueName = mCursor.getString(mCursor.getColumnIndex(VenuesTable.COL_ALIAS_DISPLAY_NAME));
                    long venueId = mCursor.getLong(mCursor.getColumnIndex(VenuesTable.COL_VENUE_ID));
                    String venueLandName = mCursor.getString(mCursor.getColumnIndex(VenueLandsTable.COL_ALIAS_DISPLAY_NAME));
                    final String venueObjectJson = mCursor.getString(mCursor.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

                    // Ensure display name is not null
                    displayName = displayName != null ? displayName : "";

                    // Special header for hotels and water parks
                    if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_HOTEL) {
                        venueName = context.getString(R.string.poi_list_item_venue_name_hotel);
                    } else if (poiTypeId == PointsOfInterestTable.VAL_POI_TYPE_ID_WATERPARK) {
                        venueName = context.getString(R.string.poi_list_item_venue_name_waterpark);
                    }

                    // Special header format for Universal Studios Hollywood
                    if (venueId == VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD) {
                        if (!TextUtils.isEmpty(venueName) && !TextUtils.isEmpty(venueLandName)) {
                            venueName = context.getString(R.string.poi_venue_land_label_format, venueName, venueLandName);
                        }
                    }

                    // Apply data to view
                    final PoiItemViewHolder holder = (PoiItemViewHolder) viewHolder;

                    // Set timeline background if this is for the event timeline
                    if (mExploreType == ExploreType.EVENTS || mExploreType == ExploreType.UPCOMING_EVENTS) {
                        holder.itemView.setBackgroundResource(R.drawable.waypoint);
                    }

                    // Attach the relevant data to the guide me and favorite layouts
                    holder.guideMeLayout.setTag(R.id.key_view_tag_explore_list_poi_object_json, poiObjectJson);
                    holder.guideMeLayout.setTag(R.id.key_view_tag_explore_list_poi_type_id, poiTypeId);
                    holder.favoriteToggleButton.setTag(R.id.key_view_tag_explore_list_poi_object_json, poiObjectJson);
                    holder.favoriteToggleButton.setTag(R.id.key_view_tag_explore_list_poi_type_id, poiTypeId);
                    holder.locateButton.setTag(R.id.key_view_tag_explore_list_poi_object_json, poiObjectJson);
                    holder.locateButton.setTag(R.id.key_view_tag_explore_list_poi_type_id, poiTypeId);

                    holder.guideMeLayout.setContentDescription(context.getString(R.string.poi_list_item_guide_me_formatted_content_description, displayName));

                    // Bind the text
                    holder.displayNameText.setText(displayName);
                    holder.venueNameText.setText(venueName != null ? venueName.toUpperCase(Locale.US) : "");
                    holder.venueNameText.setVisibility(venueName != null ? View.VISIBLE : View.GONE);

                    // Show the favorite button
                    if (PoiUtils.isFavoriteEnabled(poiTypeId)) {
                        holder.favoriteToggleButton.setChecked(isFavorite);
                        holder.favoriteToggleButton.setVisibility(View.VISIBLE);
                    } else {
                        holder.favoriteToggleButton.setVisibility(View.GONE);
                    }

                    // Set the content description
                    holder.favoriteToggleButton.setContentDescriptionByPoiName(displayName);

                    // Assume there is no image to start
                    holder.listImage.setVisibility(View.GONE);
                    holder.listImageNoImage.setVisibility(View.VISIBLE);

                    // Load the list image
                    if (listImageUrl != null && !listImageUrl.isEmpty()) {
                        Uri listImageUri = null;
                        try {
                            listImageUri = Uri.parse(listImageUrl);
                        } catch (Exception e) {
                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, "onBindViewHolder: invalid image URL: " + listImageUrl, e);
                            }

                            // Log the exception to crittercism
                            Crittercism.logHandledException(e);
                        }

                        if (listImageUri != null) {
                            Uri imageUriToLoad = listImageUri.buildUpon()
                                    .appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
                                    .build();

                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "onBindViewHolder: imageUriToLoad = " + imageUriToLoad);
                            }

                            mPicasso.load(imageUriToLoad).into(holder.listImage, new ListImageCallback(holder));
                        }
                    } else {
                        holder.listImage.setVisibility(View.GONE);
                        holder.listImageNoImage.setVisibility(View.VISIBLE);
                    }

                    // Show / hide the guide me button
                    boolean isInPark = UniversalAppStateManager.getInstance().isInResortGeofence();
                    PoiUtils.updateGuideMeButton(isInPark, isRoutable, holder);

                    // Update the wait time / show time badge
                    int poiTypeIdInt = poiTypeId != null ? poiTypeId.intValue() : -1;
                    switch (poiTypeIdInt) {
                        case PointsOfInterestTable.VAL_POI_TYPE_ID_RIDE:
                            Integer waitTime = mCursor.getInt(mCursor.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
                            String hoursListJson = mCursor.getString(mCursor.getColumnIndex(VenuesTable.COL_HOURS_LIST_JSON));

                            List<VenueHours> venueHoursList = null;
                            if (hoursListJson != null) {
                                try {
                                    venueHoursList = new Gson().fromJson(hoursListJson, new TypeToken<List<VenueHours>>() {
                                    }.getType());
                                } catch (Exception e) {
                                    if (BuildConfig.DEBUG) {
                                        Log.e(TAG, "onBindViewHolder: exception trying to parse venue hours list from JSON", e);
                                    }

                                    // Log the exception to crittercism
                                    Crittercism.logHandledException(e);
                                }
                            }

                            Ride ride = (Ride) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            String opensAt = ride.getOpensAt();

                            PoiUtils.updatePoiCircleBadgeForRide(waitTime, opensAt, venueHoursList, holder);
                            break;
                        case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOW:
                        case PointsOfInterestTable.VAL_POI_TYPE_ID_PARADE:
                            waitTime = mCursor.getInt(mCursor.getColumnIndex(PointsOfInterestTable.COL_WAIT_TIME));
                            hoursListJson = mCursor.getString(mCursor.getColumnIndex(VenuesTable.COL_HOURS_LIST_JSON));
                            String showTimesJson = mCursor.getString(mCursor.getColumnIndex(PointsOfInterestTable.COL_SHOW_TIMES_JSON));

                            venueHoursList = null;
                            if (hoursListJson != null) {
                                try {
                                    venueHoursList = new Gson().fromJson(hoursListJson, new TypeToken<List<VenueHours>>() {
                                    }.getType());
                                } catch (Exception e) {
                                    if (BuildConfig.DEBUG) {
                                        Log.e(TAG, "onBindViewHolder: exception trying to parse venue hours list from JSON", e);
                                    }

                                    // Log the exception to crittercism
                                    Crittercism.logHandledException(e);
                                }
                            }

                            List<String> showTimesList = null;
                            if (showTimesJson != null) {
                                try {
                                    showTimesList = new Gson().fromJson(showTimesJson, new TypeToken<List<String>>() {
                                    }.getType());
                                } catch (Exception e) {
                                    if (BuildConfig.DEBUG) {
                                        Log.e(TAG, "onBindViewHolder: exception trying to parse show times list from JSON", e);
                                    }

                                    // Log the exception to crittercism
                                    Crittercism.logHandledException(e);
                                }
                            }

                            // First try to show a wait time, if it exists
                            PoiUtils.updatePoiCircleBadgeForRide(waitTime, null, venueHoursList, holder);

                            // If the badge was hidden, try showing a show time instead
                            if (holder.circleBadgeRootLayout.getVisibility() == View.GONE) {
                                PoiUtils.updatePoiCircleBadgeForShow(showTimesList, holder, context);
                            }
                            break;
                        case PointsOfInterestTable.VAL_POI_TYPE_ID_EVENT:
                            Event event = (Event) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            // Get start date from cursor, used for timeline events
                            if (event != null) {
                                if (mCursor.getColumnIndex(EventTimesTable.COL_START_DATE) != -1) {
                                    Long startDateUnix = mCursor.getLong(mCursor.getColumnIndex(EventTimesTable.COL_START_DATE));
                                    Long endDateUnix = mCursor.getLong(mCursor.getColumnIndex(EventTimesTable.COL_END_DATE));
                                    EventDate eventDate = new EventDate(startDateUnix, endDateUnix);
                                    if (event.getWaitTime() == null) {
                                        EventUtils.updatePoiCircleBadgeForEvent(Arrays.asList(eventDate), holder, mExploreType == ExploreType.FAVORITES);
                                    } else {
                                        PoiUtils.updatePoiCircleBadgeForRide(event.getWaitTime(), null, null, holder);
                                    }
                                }
                                // Get dates from event object used everywhere except timelines
                                else {
                                    if (event.getWaitTime() == null) {
                                        EventUtils.updatePoiCircleBadgeForEvent(event.getEventDates(), holder, mExploreType == ExploreType.FAVORITES);
                                    } else {
                                        PoiUtils.updatePoiCircleBadgeForRide(event.getWaitTime(), null, null, holder);
                                    }
                                }
                            } else {
                                holder.circleBadgeRootLayout.setVisibility(View.GONE);
                            }
                            break;
                        default:
                            holder.circleBadgeRootLayout.setVisibility(View.GONE);
                            break;
                    }

                    // Update extra line for certain POI types
                    int extraInfoStringResId = -1;
                    String extraInfoString = null;

                    switch (poiTypeIdInt) {
                        case PointsOfInterestTable.VAL_POI_TYPE_ID_DINING:
                            Dining dining = (Dining) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            Boolean hasHealthyOptions = dining.getHasHealthyOptions();
                            if (hasHealthyOptions != null && hasHealthyOptions.booleanValue()) {
                                extraInfoStringResId = R.string.poi_list_item_dining_has_healthy_options_info;
                            }
                            break;

                        case PointsOfInterestTable.VAL_POI_TYPE_ID_LOCKERS:
                            Lockers lockers = (Lockers) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            if (lockers.getIsFree()) {
                                extraInfoStringResId = R.string.poi_list_item_lockers_ride_info;
                            }

                            break;

                        case PointsOfInterestTable.VAL_POI_TYPE_ID_SHOP:
                            Shop shop = (Shop) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            Boolean sellsExpressPass = shop.getSellsExpressPass();
                            Boolean hasPickagePickup = shop.getHasPackagePickup();
                            if (sellsExpressPass != null && sellsExpressPass.booleanValue()) {
                                extraInfoStringResId = R.string.poi_list_item_shop_sells_express_pass_info;
                            } else if (hasPickagePickup != null && hasPickagePickup.booleanValue()) {
                                extraInfoStringResId = R.string.poi_list_item_shop_has_package_pickup_info;
                            }
                            break;

                        //Subtitle for rentals
                        case PointsOfInterestTable.VAL_POI_TYPE_ID_RENTALS:
                            extraInfoStringResId = R.string.advanced_filter_rental_type_extra_info;
                            break;

                        case PointsOfInterestTable.VAL_POI_TYPE_ID_GATEWAY:
                            Gateway gateway = (Gateway) PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                            extraInfoString = gateway.getGatewayType();
                            break;

                        default:
                            break;
                    }

                    // Set the extra text, or hide it
                    if (extraInfoStringResId != -1 || extraInfoString != null) {
                        if (extraInfoStringResId != -1) {
                            holder.extraInfoText.setText(extraInfoStringResId);
                        } else {
                            holder.extraInfoText.setText(extraInfoString);
                        }
                        holder.extraInfoText.setVisibility(View.VISIBLE);
                    } else {
                        holder.extraInfoText.setVisibility(View.GONE);
                    }

                    // Build a single content description that describes all UI elements in a specific order.
                    StringBuilder contentDescriptionBuilder = new StringBuilder();

                    // Add the attraction header to the content description
                    if (!TextUtils.isEmpty(venueName)) {
                        contentDescriptionBuilder.append(venueName).append("\n");
                    }
                    contentDescriptionBuilder.append(displayName).append("\n");

                    // Add circle badge content description
                    if (holder.circleBadgeRootLayout.getVisibility() == View.VISIBLE) {
                        CharSequence circleBadgeContentDescription = holder.circleBadgeRootLayout.getContentDescription();
                        if (circleBadgeContentDescription != null && circleBadgeContentDescription.length() > 0) {
                            contentDescriptionBuilder.append(circleBadgeContentDescription).append("\n");
                        }
                    }

                    // Set entire row's content description.
                    holder.itemView.setContentDescription(contentDescriptionBuilder.toString());

                    // Listen for various clicks
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onPoiItemClicked(poiObjectJson, poiTypeId, venueObjectJson);
                            }
                        }
                    });
                    holder.guideMeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onPoiItemGuideMeClicked(poiObjectJson, poiTypeId);
                            }
                        }
                    });
                    holder.favoriteToggleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                PointOfInterest poi = PointOfInterest.fromJson(poiObjectJson, poiTypeId);
                                // Update the favorite state off the main thread
                                boolean isFavorite = poi.getIsFavorite() != null && poi.getIsFavorite();
                                PoiUtils.updatePoiIsFavoriteInDatabase(
                                        v.getContext(), poi, !isFavorite, true);
                            }
                        }
                    });
                    holder.locateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onPoiItemLocateClicked(poiObjectJson, poiTypeId, poiId);
                            }
                        }
                    });

                    // Only show the locate button for Hollywood
                    if (BuildConfigUtils.isLocationFlavorHollywood()) {
                        holder.locateButton.setVisibility(View.VISIBLE);
                    } else {
                        holder.locateButton.setVisibility(View.GONE);
                    }
                }
                break;
            case ViewAllViewHolder.LAYOUT_RES_ID:
                ViewAllViewHolder holder = (ViewAllViewHolder) viewHolder;

                Integer viewAllTextResId;
                switch (mViewAllType) {
                    case POI:
                        viewAllTextResId = R.string.home_short_wait_times_view_all;
                        break;
                    case EVENT:
                        viewAllTextResId = R.string.home_upcoming_events_view_all;
                        break;
                    default:
                        viewAllTextResId = null;
                        break;
                }

                if (viewAllTextResId != null) {
                    holder.viewAllText.setText(viewAllTextResId);
                }

                holder.viewAllButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onViewAllClicked(mViewAllType);
                        }
                    }
                });
                break;
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        notifyDataSetChanged();
        return oldCursor;
    }

    public void destroy() {
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
    }

    static class PoiItemViewHolder extends PoiViewHolder {
        public static final int LAYOUT_RES_ID = R.layout.list_poi_item;
        RelativeLayout poiCardContainer;

        public PoiItemViewHolder(View itemView) {
            super(itemView);
            displayNameText = (TextView) itemView.findViewById(R.id.list_poi_item_display_name_text);
            venueNameText = (TextView) itemView.findViewById(R.id.list_poi_item_venue_name_text);
            extraInfoText = (TextView) itemView.findViewById(R.id.list_poi_item_extra_info_text);
            listImage = (ImageView) itemView.findViewById(R.id.list_poi_item_image);
            listImageNoImage = (ImageView) itemView.findViewById(R.id.list_poi_item_no_image_logo);
            guideMeLayout = (LinearLayout) itemView.findViewById(R.id.list_poi_item_guide_me_button_layout);
            guideMeDivider = itemView.findViewById(R.id.list_poi_item_guide_me_button_divider);
            favoriteToggleButton = (FavoriteToggleButton) itemView.findViewById(R.id.list_poi_item_favorite_toggle_button);
            locateButton = (Button) itemView.findViewById(R.id.list_poi_item_locate_button);
            circleBadgeRootLayout = (LinearLayout) itemView.findViewById(R.id.poi_item_circle_badge_root_container);
            waitTimeLayout = (LinearLayout) itemView.findViewById(R.id.poi_item_circle_badge_wait_time_layout);
            waitTimeMinNumText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_wait_time_num_text);
            showTimeLayout = (FrameLayout) itemView.findViewById(R.id.poi_item_circle_badge_show_time_layout);
            showTimeBackgroundGray = itemView.findViewById(R.id.poi_item_circle_badge_show_time_background_gray);
            showTimeBackgroundBlue = itemView.findViewById(R.id.poi_item_circle_badge_show_time_background_blue);
            showTimeStartsTimeText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_show_time_starts_time_text);
            showTimeStartsText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_show_time_starts_text);
            showTimeOpensText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_show_time_opens_text);
            showTimeAmPmText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_show_time_am_pm_text);
            closedLayout = (LinearLayout) itemView.findViewById(R.id.poi_item_circle_badge_closed_layout);
            closedText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_closed_text);
            closedWeatherText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_weather_text);
            closedTemporaryText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_temporary_text);
            closedCapacityText = (TextView) itemView.findViewById(R.id.poi_item_circle_badge_closed_capacity_text);

            // Overwrite the layout bounds for the card container, the width must be set or else the
            // relative layouts will not work with a horizontal layout manager
            poiCardContainer = (RelativeLayout) itemView.findViewById(R.id.list_poi_item_card_container);
            int widthPx = itemView.getResources().getDimensionPixelSize(R.dimen.explore_list_horizontal_card_width);
            FrameLayout.LayoutParams newLayoutParams = new FrameLayout.LayoutParams(widthPx, FrameLayout.LayoutParams.WRAP_CONTENT);
            poiCardContainer.setLayoutParams(newLayoutParams);

            // Remove all padding, it's set by the recycler view
            poiCardContainer.setPadding(0, 0, 0, 0);
        }
    }

    static class ViewAllViewHolder extends RecyclerView.ViewHolder {
        public static final int LAYOUT_RES_ID = R.layout.list_item_view_all;
        View viewAllButton;
        TextView viewAllText;

        public ViewAllViewHolder(View itemView) {
            super(itemView);
            viewAllButton = itemView.findViewById(R.id.list_item_view_all_button);
            viewAllText = (TextView) itemView.findViewById(R.id.list_item_view_all_text);
        }
    }

    public interface OnItemClickListener {
        void onPoiItemClicked(String poiObjectJson, int poiTypeId, String venueObjectJson);

        void onPoiItemGuideMeClicked(String poiObjectJson, int poiTypeId);

        void onPoiItemLocateClicked(String poiObjectJson, int poiTypeId, long poiId);

        void onViewAllClicked(ViewAllType viewAllType);
    }

    public enum ViewAllType {
        POI, EVENT
    }
}
