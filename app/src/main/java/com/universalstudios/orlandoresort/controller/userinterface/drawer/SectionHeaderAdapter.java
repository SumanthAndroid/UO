/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.drawer;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SectionHeaderAdapter extends BaseAdapter {

	private final List<DrawerSectionHeader> mSectionHeaderList;
	private final int mLayoutResId, mTitleTextViewId;

	public SectionHeaderAdapter(List<DrawerSectionHeader> sectionHeaderList, int layoutResId, int titleTextViewId) {
		super();
		mSectionHeaderList = sectionHeaderList;
		mLayoutResId = layoutResId;
		mTitleTextViewId = titleTextViewId;
	}

	public SectionHeaderAdapter(int layoutResId, int titleTextViewId) {
		super();
		mSectionHeaderList = new ArrayList<DrawerSectionHeader>();
		mLayoutResId = layoutResId;
		mTitleTextViewId = titleTextViewId;
	}

	@Override
	public int getCount() {
		if (mSectionHeaderList != null) {
			return mSectionHeaderList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mSectionHeaderList != null) {
			return mSectionHeaderList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Object item = getItem(position);
		if (item != null) {
			return item.hashCode();
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(mLayoutResId, parent, false);

			holder = new ViewHolder();
			holder.titleText = (TextView) convertView.findViewById(mTitleTextViewId);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		DrawerSectionHeader sectionHeader = mSectionHeaderList.get(position);

		// Depending on way the title string was passed, use that
		Integer titleStringResId = sectionHeader.getTitleStringResId();
		String title = sectionHeader.getTitle();

		if (titleStringResId != null) {
			holder.titleText.setText(titleStringResId);
		}
		else if (title != null) {
			holder.titleText.setText(title);
		}

		return convertView;
	}

	public boolean add(DrawerSectionHeader sectionHeader) {
		boolean success = mSectionHeaderList.add(sectionHeader);
		if (success) {
			notifyDataSetInvalidated();
		}
		return success;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	private static class ViewHolder {
		TextView titleText;
	}

}
