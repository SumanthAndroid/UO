package com.universalstudios.orlandoresort.controller.userinterface.drawer;

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
 * @deprecated Use {@link DrawerItemExpandableListAdapter} instead, it supports expandable rows
 *
 * @author Steven Byle
 */
@Deprecated
public class DrawerItemListAdapter extends BaseAdapter {

	private final List<DrawerItem> mDrawerItemList;

	public DrawerItemListAdapter(List<DrawerItem> drawerItemList) {
		super();
		mDrawerItemList = drawerItemList;
	}

	@Override
	public int getCount() {
		if (mDrawerItemList != null) {
			return mDrawerItemList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDrawerItemList != null && position < mDrawerItemList.size()) {
			return mDrawerItemList.get(position);
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
			convertView = inflater.inflate(R.layout.list_drawer_item, parent, false);

			holder = new ViewHolder();
			holder.titleText = (TextView) convertView.findViewById(R.id.list_drawer_item_title_text);
			holder.iconImage = (ImageView) convertView.findViewById(R.id.list_drawer_item_icon_image);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		DrawerItem drawerItem = (DrawerItem) getItem(position);
		Integer titleStringResId = drawerItem.getTitleStringResId();
		String title = drawerItem.getTitle();
		Integer iconDrawableResId = drawerItem.getIconDrawableResId();

		// Depending on way the title string was passed, use that
		if (titleStringResId != null) {
			holder.titleText.setText(titleStringResId);
		}
		else if (title != null) {
			holder.titleText.setText(title);
		}
		holder.iconImage.setImageResource(iconDrawableResId);

		return convertView;
	}

	private static class ViewHolder {
		TextView titleText;
		ImageView iconImage;
	}

}
