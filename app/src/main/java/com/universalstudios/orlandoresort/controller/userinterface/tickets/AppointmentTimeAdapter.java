package com.universalstudios.orlandoresort.controller.userinterface.tickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.domain.appointments.AppointmentTimes;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by GOKHAN on 7/26/2016.
 */
    public class AppointmentTimeAdapter extends ArrayAdapter {

    public static final boolean APPOINTMENT_TIME_DISABLED = false;
    public static final boolean APPOINTMENT_TIME_ENABLED = true;
    private Context context;
    private int layoutResourceId;
    private int selectedPosition = -1;
    List<AppointmentTimes> appointmentTimes;
    private final LinkedList<AppointmentTimes> grayedAppointmentTimes = new LinkedList<>();

        public AppointmentTimeAdapter(Context context, int layoutResourceId, List<AppointmentTimes> appointmentTimes) {
            super(context, layoutResourceId, appointmentTimes);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.appointmentTimes = appointmentTimes;
        }

    private void setSelectedPosition(int position){
        selectedPosition = position;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          //  View row = convertView;
            ViewHolder holder = null;

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(context);

                holder = new ViewHolder();

                gridView = inflater.inflate(R.layout.fragment_detail_queue_ticket_app_item, null);
                holder.appointmentTimeItemText = (TextView) gridView.findViewById(R.id.fragment_detail_appointment_time_item_view);
                holder.appointTimeAmPmText = (TextView) gridView.findViewById(R.id.fragment_detail_appointment_time_item_view2);
                gridView.setTag(holder);

                if(position == selectedPosition){
                    gridView.setBackgroundColor(getContext().getResources().getColor(R.color.action_bar_divider_white));
                }
            }else{
                gridView = convertView;
                holder = (ViewHolder) gridView.getTag();
            }

            if (!appointmentTimes.get(position).getStartTime().isEmpty()){
                String startTime = appointmentTimes.get(position).getStartTime();
                if(!appointmentTimes.get(position).getEndTime().isEmpty()){
                    String endTime = appointmentTimes.get(position).getEndTime();
                    String appointmentTimeItem = startTime.substring(0,startTime.length()-3) + "-" + endTime.substring(0,endTime.length()-3);
                    String appointmentTimeAmPm = endTime.substring(endTime.length()-2);
                    holder.appointmentTimeItemText.setText(appointmentTimeItem);
                    holder.appointTimeAmPmText.setText(appointmentTimeAmPm);
                }
            }

            if(isTimeGrayed(appointmentTimes.get(position))) {
                gridView.setAlpha(0.5f);
                gridView.setEnabled(APPOINTMENT_TIME_DISABLED);
            } else {
                gridView.setAlpha(1.0f);
                gridView.setEnabled(APPOINTMENT_TIME_ENABLED);
            }

            return gridView;
        }

        static class ViewHolder {
            TextView appointmentTimeItemText;
            TextView appointTimeAmPmText;
        }

    public void setGrayedAppointmentTimes(List<AppointmentTimes> times) {
        grayedAppointmentTimes.clear();
        grayedAppointmentTimes.addAll(times);
    }

    public boolean isTimeGrayed(AppointmentTimes time) {
        int size = grayedAppointmentTimes.size();
        for(int i = 0; i < size; i++) {
            if(time.getId() == grayedAppointmentTimes.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getItem(int position) {



        return super.getItem(position);


    }
}