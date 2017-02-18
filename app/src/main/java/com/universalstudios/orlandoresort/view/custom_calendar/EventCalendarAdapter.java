package com.universalstudios.orlandoresort.view.custom_calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventCalendarAdapter extends RecyclerView.Adapter<EventCalendarAdapter.CalendarCellViewHolder> {
    private static final String TAG = EventCalendarAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_DAY_HEADER = 1;
    private static final int VIEW_TYPE_DAY = 2;
    public static final int DAYS_IN_WEEK = 7;

    private Context mContext;
    private List<EventCalendarCell> mDaysOfMonth = new ArrayList<>();
    private CalendarChangeListener mListener;

    private TridionConfig mTridionConfig;

    private int mCurrentSelectedItem = -1;

    public EventCalendarAdapter(Context mContext, CalendarChangeListener listener) {
        this.mContext = mContext.getApplicationContext();
        this.mListener = listener;
        this.mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    public void setDaysList(List<EventCalendarCell> days) {
        this.mDaysOfMonth = days;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < DAYS_IN_WEEK) {
            return VIEW_TYPE_DAY_HEADER;
        }
        return VIEW_TYPE_DAY;
    }

    @Override
    public CalendarCellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell;
        if (viewType == VIEW_TYPE_DAY) {
            cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_calendar_cell, parent, false);
        } else {
            cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_calendar_header, parent, false);
        }
        return new CalendarCellViewHolder(cell);
    }

    @Override
    public void onBindViewHolder(final CalendarCellViewHolder holder, int position) {
        if (position < DAYS_IN_WEEK) {
            setTextToDayHeader(position, holder);
        } else {
            final EventCalendarCell eventCalendarCell = mDaysOfMonth.get(position - DAYS_IN_WEEK);

            if (eventCalendarCell != null) {
                holder.day.setText(Integer.toString(eventCalendarCell.getDayOfMonth()));
            }

            //can't do anything without day object
            if (eventCalendarCell == null) {
                return;
            }
            Date date = eventCalendarCell.getDate();
            boolean dateIsBeforeToday = false;
            if (date != null) {
                Date today = DateUtils.truncate(new Date(), Calendar.DATE);
                date = DateUtils.truncate(date, Calendar.DATE);
                dateIsBeforeToday = date.before(today);
            }
            // Days out of month and days before today should be disabled
            if (eventCalendarCell.isOutOfMonth() || dateIsBeforeToday) {
                holder.itemView.setBackgroundResource(R.drawable.shape_calendar_cell_background_disabled);
                holder.day.setTextColor(ContextCompat.getColor(mContext, R.color.custom_calendar_date_text_color_default));
                holder.itemView.setOnClickListener(null);
            } else {
                holder.day.setSelected(mCurrentSelectedItem == holder.getAdapterPosition());
                holder.day.setEnabled(eventCalendarCell.isAvailable());

                if (eventCalendarCell.isAvailable()) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int lastSelectedItem = mCurrentSelectedItem;
                            mCurrentSelectedItem = holder.getAdapterPosition();
                            notifyItemChanged(lastSelectedItem);
                            notifyItemChanged(mCurrentSelectedItem);
                            if (mListener != null) {
                                mListener.onDateSelected(mDaysOfMonth.get(holder.getAdapterPosition() - DAYS_IN_WEEK));
                            }
                        }
                    });
                } else {
                    holder.itemView.setOnClickListener(null);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDaysOfMonth.size() + DAYS_IN_WEEK;
    }

    void setSelectedItem(int selectedItem) {
        //add 7 to selected to account for day of week headers
        if (selectedItem != -1) {
            mCurrentSelectedItem = selectedItem + DAYS_IN_WEEK;
        } else {
            //don't try to select day of week header
            mCurrentSelectedItem = -1;
        }
        notifyItemChanged(mCurrentSelectedItem);
    }

    /**
     * Sets the text for the 7 day headers using the given position
     *
     * @param position
     * @param holder
     */
    public void setTextToDayHeader(int position, CalendarCellViewHolder holder) {
        switch (position) {
            case 0:
                holder.day.setText(mTridionConfig.getDaySunLabel());
                break;
            case 1:
                holder.day.setText(mTridionConfig.getDayMonLabel());
                break;
            case 2:
                holder.day.setText(mTridionConfig.getDayTueLabel());
                break;
            case 3:
                holder.day.setText(mTridionConfig.getDayWedLabel());
                break;
            case 4:
                holder.day.setText(mTridionConfig.getDayThrLabel());
                break;
            case 5:
                holder.day.setText(mTridionConfig.getDayFriLabel());
                break;
            case 6:
                holder.day.setText(mTridionConfig.getDaySatLabel());
                break;
        }
    }

    class CalendarCellViewHolder extends RecyclerView.ViewHolder {
        TextView day;

        CalendarCellViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day);
        }
    }

    interface CalendarChangeListener {
        void onDateSelected(EventCalendarCell calendarCellObject);
    }
}
