package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.pointofinterest.EventDate;
import com.universalstudios.orlandoresort.view.stickylistheaders.StickyListHeadersAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventScheduleAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private static final String FORMAT_DATE_HEADER = "MMMM y";
    private static final String FORMAT_DATE_PRIMARY = "EEE, MMM d";
    private static final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

    private Date mStartDate;
    private Calendar mCalendar;
    private List<EventDate> mEventDates;
    private int mDayCount;
    private String mPlaceholder;
    private String mFormatString;
    private DateFormat mHeaderDateFormat;
    private DateFormat mPrimaryDateFormat;

    @Deprecated
    public EventScheduleAdapter(List<EventDate> eventDates) {
        super();
        Context context = UniversalOrlandoApplication.getAppContext();
        mEventDates = eventDates;
        mPlaceholder = context.getString(R.string.event_schedule_closed);
        mFormatString = context.getString(R.string.event_schedule_open_date_format);
        mCalendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
        mPrimaryDateFormat = new SimpleDateFormat(FORMAT_DATE_PRIMARY, Locale.US);
        mPrimaryDateFormat.setTimeZone(DateTimeUtils.getParkTimeZone());
        mHeaderDateFormat = new SimpleDateFormat(FORMAT_DATE_HEADER, Locale.US);
        mHeaderDateFormat.setTimeZone(DateTimeUtils.getParkTimeZone());

        if (mEventDates != null) {
            Collections.sort(mEventDates, new EventDate.EventDateAscendingComparator());

            EventDate startDate = mEventDates.get(0);
            EventDate endDate = mEventDates.get(mEventDates.size() - 1);

            if (startDate.getStartDateUnix() != null && endDate.getEndDateUnix() != null) {
                mStartDate = new Date(startDate.getStartDateUnix() * 1000);

                mCalendar.setTimeInMillis(startDate.getStartDateUnix() * 1000);
                // Reset calendar to 12:00am
                mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                mCalendar.set(Calendar.MINUTE, 0);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);

                long startTime = mCalendar.getTimeInMillis();

                mCalendar.setTimeInMillis(endDate.getEndDateUnix() * 1000);
                // Reset calendar to 12:00am
                mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                mCalendar.set(Calendar.MINUTE, 0);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
                long endTime = mCalendar.getTimeInMillis();

                mDayCount = (int) Math.ceil((((double) endTime - (double) startTime) / MILLIS_IN_DAY));
                // Add one extra day to include the last day
                mDayCount++;
            }
        }
    }

    @Override
    public int getCount() {
        return mDayCount;
    }

    @Override
    public Object getItem(int position) {
        mCalendar.setTime(mStartDate);
        // Reset calendar to 12:00am
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        // Get date for this position
        mCalendar.add(Calendar.DATE, position);
        // Get event status for the date at this position
        String dateString = EventUtils.getEventStatusForDay(mCalendar, mEventDates, mCalendar.getTime(),
                mPlaceholder);
        // Format date range if placeholder was not used
        if (!mPlaceholder.equals(dateString)) {
            dateString = String.format(mFormatString, dateString);
        }

        return dateString;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_event_schedule_item,
                    parent, false);
            holder = new ViewHolder();
            holder.primaryTextView = (TextView) convertView
                    .findViewById(R.id.list_event_schedule_item_primary_text);
            holder.secondaryTextView = (TextView) convertView
                    .findViewById(R.id.list_event_schedule_item_primary_sub_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.primaryTextView.setText(getDate(position));
        holder.secondaryTextView.setText(getItem(position).toString());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_poi_section_header, parent, false);

            holder = new HeaderViewHolder();
            holder.headerTitleText = (TextView) convertView
                    .findViewById(R.id.list_poi_section_header_title_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.headerTitleText.setText(getHeaderString(position));

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getHeaderString(position).hashCode();
    }

    private String getHeaderString(int position) {
        mCalendar.setTime(mStartDate);
        mCalendar.add(Calendar.DATE, position);

        return mHeaderDateFormat.format(mCalendar.getTime());
    }

    private String getDate(int position) {
        mCalendar.setTime(mStartDate);
        mCalendar.add(Calendar.DATE, position);

        return mPrimaryDateFormat.format(mCalendar.getTime());
    }

    private static class ViewHolder {

        public TextView primaryTextView;
        public TextView secondaryTextView;
    }

    private static class HeaderViewHolder {

        TextView headerTitleText;
    }

}
