package com.universalstudios.orlandoresort.view.custom_calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 7/21/16.
 * Class: CustomCalendarAdapter
 * Class Description: TODO: ALWAYS FILL OUT
 */
public class CustomCalendarAdapter extends RecyclerView.Adapter<CustomCalendarAdapter.CalendarCellViewHolder> {
    public static final String TAG = "CustomCalendarAdapter";

    public interface CalendarChangeListener {
        void onDateSelected(CalendarCellObject calendarCellObject);
    }

    private static final int VIEW_TYPE_DAY_HEADER = 1;
    private static final int VIEW_TYPE_DAY = 2;

    private List<CalendarCellObject> daysOfMonth = new ArrayList<>();
    private Context mContext;
    private CalendarChangeListener listener;

    private TridionConfig tridionConfig;

    public CustomCalendarAdapter(Context mContext, CalendarChangeListener listener) {
        this.mContext = mContext.getApplicationContext();
        this.listener = listener;
        tridionConfig = IceTicketUtils.getTridionConfig();
    }

    public void setDaysList(List<CalendarCellObject> days) {
        this.daysOfMonth = days;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 7) {
            return VIEW_TYPE_DAY_HEADER;
        }
        return VIEW_TYPE_DAY;
    }

    @Override
    public CalendarCellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell;
        if (viewType == VIEW_TYPE_DAY) {
            cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_calendar_cell, parent, false);
        } else {
            cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_calendar_header, parent, false);
        }
        return new CalendarCellViewHolder(cell);
    }

    @Override
    public void onBindViewHolder(CalendarCellViewHolder holder, final int position) {
        int backgroundColorValue = ContextCompat.getColor(mContext, R.color.custom_calendar_disabled);
        if (position < 7) {
            setTextToDayHeader(position, holder);
        } else {
            final int pos = position - 7;
            final CalendarCellObject dayObject = daysOfMonth.get(pos);
            if (dayObject != null) {
                holder.day.setText(Integer.toString(dayObject.day));
            }

            // If the context or context resources are null, we have a big problem...
            if (mContext == null || mContext.getResources() == null) {
                return;
            }

            // Background ID for the cell, determined by numerous factors below
            int textColorValue = Color.BLACK;

            // Set the cell to the disabled cell drawable if we are drawing "peak" cells
            if (dayObject.isOutOfMonth) {
                holder.itemView.setBackgroundResource(R.drawable.shape_calendar_cell_background_disabled);
                holder.day.setTextColor(mContext.getResources().getColor(R.color.custom_calendar_date_text_color_default));
            }
            // Assign the background ID based on the day's season type (CalendarCellSeasonState enum)
            else if (dayObject.computedSeasonState != null) {
                switch (dayObject.computedSeasonState) {
                    case VALUE:
                        backgroundColorValue = tridionConfig.getSCValueColor();
                        textColorValue = tridionConfig.getSCValueTextColor();
                        break;
                    case MIXED:
                        backgroundColorValue = tridionConfig.getSCMixedColor();
                        textColorValue = tridionConfig.getSCMixedTextColor();
                        break;
                    case REGULAR:
                        backgroundColorValue = tridionConfig.getSCRegularColor();
                        textColorValue = tridionConfig.getSCRegularTextColor();
                        break;
                    case PEAK:
                        backgroundColorValue = tridionConfig.getSCPeakColor();
                        textColorValue = tridionConfig.getSCPeakTextColor();
                        break;
                    case NONE:
                        holder.itemView.setBackgroundResource(R.drawable.shape_calendar_cell_background_disabled);
                        textColorValue = mContext.getResources().getColor(R.color.custom_calendar_date_text_color_default);
                        break;

                }
                // Set the background color to the determined color ID (case NONE was taken care of above)
                if (dayObject.computedSeasonState != CalendarCellSeasonState.NONE) {
                    holder.itemView.setBackgroundColor(backgroundColorValue);
                }
                // Set text color to color ID determined above
                holder.day.setTextColor(textColorValue);
            }
            // Finally, for the day we have selected, preserve the background color ID we retrieved above for the season, and
            // set the background color for our circle shape
            if (dayObject.isSelected) {
                LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.shape_calendar_selected_circle);
                GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.shape_calendar_selected_circle_background);
                if(dayObject.computedSeasonState == null){
                    if (null != gradientDrawable) {
                        gradientDrawable.setColor(mContext.getResources().getColor(R.color.dark_blue_background));
                    }
                    holder.day.setTextColor(mContext.getResources().getColor(R.color.text_white));
                } else {
                    gradientDrawable.setColor(backgroundColorValue);
                }

                // Set the circle background to the cell that has been selected
                holder.itemView.setBackground(layerDrawable);
            }

            // Day cells can be clicked if they aren't out of the month (peak days) and if they have an available season type
            if (!dayObject.isOutOfMonth && dayObject.computedSeasonState != CalendarCellSeasonState.NONE) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener && !dayObject.isSelected) {
                            // Select the selected date
                            listener.onDateSelected(dayObject);
                            dayObject.isSelected = true;
                            notifyItemChanged(position);
                            // Deselect the previously selected day if it exists
                            for (int dayPos = 0; dayPos < daysOfMonth.size(); dayPos++) {
                                if (dayPos+7 != position && daysOfMonth.get(dayPos).isSelected) {
                                    daysOfMonth.get(dayPos).isSelected = false;
                                    notifyItemChanged(dayPos+7);
                                }
                            }
                        }
                    }
                });
            } else {
                holder.itemView.setOnClickListener(null);
            }
        }
    }

    /**
     * Sets the text for the 7 day headers using the given position
     *
     * TODO: Replace strings below with Tridion strings
     *
     * @param position
     * @param holder
     */
    private void setTextToDayHeader(int position, CalendarCellViewHolder holder) {
        switch (position) {
            case 0:
                holder.day.setText(tridionConfig.getDaySunLabel());
                break;
            case 1:
                holder.day.setText(tridionConfig.getDayMonLabel());
                break;
            case 2:
                holder.day.setText(tridionConfig.getDayTueLabel());
                break;
            case 3:
                holder.day.setText(tridionConfig.getDayWedLabel());
                break;
            case 4:
                holder.day.setText(tridionConfig.getDayThrLabel());
                break;
            case 5:
                holder.day.setText(tridionConfig.getDayFriLabel());
                break;
            case 6:
                holder.day.setText(tridionConfig.getDaySatLabel());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size() + 7;
    }

    protected class CalendarCellViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        public CalendarCellViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day);
        }
    }
}
