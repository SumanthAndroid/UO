package com.universalstudios.orlandoresort.controller.userinterface.alerts;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WaitTimeListAdapter extends BaseAdapter {

	private final List<Integer> mWaitTimesInMinutes;

	public WaitTimeListAdapter(List<Integer> waitTimesInMinutes) {
		super();
		mWaitTimesInMinutes = waitTimesInMinutes;
	}

	@Override
	public int getCount() {
		if (mWaitTimesInMinutes != null) {
			return mWaitTimesInMinutes.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mWaitTimesInMinutes != null
				&& position < mWaitTimesInMinutes.size()
				&& position >= 0) {
			return mWaitTimesInMinutes.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Object item = getItem(position);
		if (item != null) {
			return item.hashCode();
		}
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.flip_wait_time_item, parent, false);

			holder = new ViewHolder();
			holder.minutesText = (TextView) convertView.findViewById(R.id.flip_wait_time_item_minutes_text);
			holder.upArrowImage = (ImageView) convertView.findViewById(R.id.flip_wait_time_item_up_arrow);
			holder.downArrowImage = (ImageView) convertView.findViewById(R.id.flip_wait_time_item_down_arrow);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		Integer waitTime = (Integer) getItem(position);

		// Bind the wait time to the view
		if (waitTime != null) {
			holder.minutesText.setText("" + waitTime);
		}

		// Set visibility of the arrows based on if the adjacent pages exist
		holder.downArrowImage.setVisibility(position + 1 < getCount() ? View.VISIBLE : View.GONE);
		holder.upArrowImage.setVisibility(position - 1 >= 0 ? View.VISIBLE : View.GONE);

		return convertView;
	}

	private static class ViewHolder {
		TextView minutesText;
		ImageView upArrowImage;
		ImageView downArrowImage;
	}

}
