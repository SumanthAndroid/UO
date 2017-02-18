package com.universalstudios.orlandoresort.view.custom_calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;

/**
 * Adapter class for custom calendar legend for grid view.
 *
 * Created by Tyler Ritchie on 9/23/16.
 */
public class CalendarLegendAdapter extends BaseAdapter {

    private Context context;
    private CalendarLegendSeason[] seasonItems;
    LayoutInflater inflater;

    public CalendarLegendAdapter(Context context, CalendarLegendSeason[] seasonItems) {
        this.context = context;
        this.seasonItems = seasonItems;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_season_legend_cell, null);
        }
        CalendarLegendSeason legendSeason = seasonItems[position];

        TextView textHeader = (TextView) convertView.findViewById(R.id.calendar_season_legend_cell_text_header);
        textHeader.setText(legendSeason.getItemHeader());

        TextView textDesc = (TextView) convertView.findViewById(R.id.calendar_season_legend_cell_text_description);
        textDesc.setText(legendSeason.getItemDescription());

        View square = convertView.findViewById(R.id.calendar_season_legend_cell_square);
        square.setBackgroundColor(legendSeason.getColorValue());

        return convertView;
    }

    @Override
    public int getCount() {
        return seasonItems.length;
    }

    @Override
    public Object getItem(int position) {
        return seasonItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
