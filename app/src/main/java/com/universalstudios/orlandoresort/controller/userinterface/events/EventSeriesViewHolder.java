package com.universalstudios.orlandoresort.controller.userinterface.events;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.controller.userinterface.explore.PoiViewHolder;

public class EventSeriesViewHolder extends PoiViewHolder {

    public TextView descriptionTextView;
    public TextView eventDateTextView;
    public View eventDisabledView;
    public RelativeLayout eventRootContentRelativeLayout;

    public EventSeriesViewHolder(View itemView) {
        super(itemView);
    }
}
