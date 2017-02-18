package com.universalstudios.orlandoresort.controller.userinterface.drawer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.BlankView;
import com.universalstudios.orlandoresort.view.TintUtils;

import java.util.ArrayList;
import java.util.List;

import static com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerItem.BLANK_DRAWABLE_RESOURCE_ID;

/**
 *
 *
 * @author Steven Byle
 */
public class DrawerItemExpandableListAdapter extends BaseExpandableListAdapter {
	private static final String TAG = DrawerItemExpandableListAdapter.class.getSimpleName();

	private static final int VIEW_TYPE_GROUP_COUNT = 2;
	private static final int VIEW_TYPE_GROUP_DRAWER_SECTION_HEADER = 0;
	private static final int VIEW_TYPE_GROUP_DRAWER_ITEM = 1;

	private static final int VIEW_TYPE_CHILD_COUNT = 1;
	private static final int VIEW_TYPE_CHILD_DRAWER_ITEM = 0;

	private final List<Object> mDrawerObjectList;
	private View mBlankView;

	public DrawerItemExpandableListAdapter() {
        super();
		mDrawerObjectList = new ArrayList<Object>();
	}

	public boolean addDrawerObject(Object drawerObject) {
		boolean success = mDrawerObjectList.add(drawerObject);
        if (success) {
            notifyDataSetChanged();
        }
        return success;
	}

	@Override
	public int getGroupCount() {
		return mDrawerObjectList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDrawerObjectList.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return getGroup(groupPosition).hashCode();
	}

	@Override
	public int getGroupTypeCount() {
		return VIEW_TYPE_GROUP_COUNT;
	}

	@Override
	public int getGroupType(int groupPosition) {
		Object group = getGroup(groupPosition);

		if (group instanceof DrawerSectionHeader) {
			return VIEW_TYPE_GROUP_DRAWER_SECTION_HEADER;
		}
		else if (group instanceof DrawerItem) {
			return VIEW_TYPE_GROUP_DRAWER_ITEM;
		}

		if (BuildConfig.DEBUG) {
			Log.w(TAG, "getGroupType: unknown group type");
		}
		return -1;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (BuildConfig.DEBUG) {
			//Log.d(TAG, "getGroupView: groupPosition = " + groupPosition + " isExpanded = " + isExpanded);
		}

		// Clear blank view so it won't be reused
		if (convertView instanceof BlankView) {
		    convertView = null;
		}

		int groupViewType = getGroupType(groupPosition);
		if (groupViewType == VIEW_TYPE_GROUP_DRAWER_SECTION_HEADER) {
			final DrawerSectionViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.list_drawer_section_header, parent, false);

				holder = new DrawerSectionViewHolder();
				holder.titleText = (TextView) convertView.findViewById(R.id.list_drawer_section_header_title_text);

				convertView.setTag(holder);
			}
			else {
				holder = (DrawerSectionViewHolder) convertView.getTag();
			}

			DrawerSectionHeader groupDrawerSectionHeader = (DrawerSectionHeader) getGroup(groupPosition);

			// Depending on way the title string was passed, use that
			Integer titleStringResId = groupDrawerSectionHeader.getTitleStringResId();
			String title = groupDrawerSectionHeader.getTitle();

			if (titleStringResId != null) {
				holder.titleText.setText(titleStringResId);
			}
			else if (title != null) {
				holder.titleText.setText(title);
			}

			return convertView;
		}
		else if (groupViewType == VIEW_TYPE_GROUP_DRAWER_ITEM) {
			final DrawerItemViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.list_drawer_item, parent, false);

				holder = new DrawerItemViewHolder();
				holder.titleText = (TextView) convertView.findViewById(R.id.list_drawer_item_title_text);
				holder.iconImage = (ImageView) convertView.findViewById(R.id.list_drawer_item_icon_image);
				holder.expandCollapseImage = (ImageView) convertView.findViewById(R.id.list_drawer_item_expand_collapse_icon_image);
				holder.badgeText = (TextView) convertView.findViewById(R.id.list_drawer_item_number_badge_text);

				convertView.setTag(holder);
			}
			else {
				holder = (DrawerItemViewHolder) convertView.getTag();
			}

			DrawerItem groupDrawerItem = (DrawerItem) getGroup(groupPosition);
			Integer titleStringResId = groupDrawerItem.getTitleStringResId();
			String title = groupDrawerItem.getTitle();
			Integer iconDrawableResId = groupDrawerItem.getIconDrawableResId();
			Integer badgeNumber = groupDrawerItem.getBadgeNumber();
			Bitmap drawerIcon = groupDrawerItem.getDrawerIcon();

			// Hide drawer item if not visible
			if (!groupDrawerItem.isVisible()) {
				if (mBlankView == null) {
					mBlankView = new BlankView(parent.getContext());
					mBlankView.setLayoutParams(new LayoutParams(0, 0));
				}
				return mBlankView;
			}

			// Depending on way the title string was passed, use that
			if (titleStringResId != null) {
				holder.titleText.setText(titleStringResId);
			}
			else if (title != null) {
				holder.titleText.setText(title);
			}

			if (null != drawerIcon) {
				holder.iconImage.setVisibility(View.VISIBLE);
				holder.iconImage.setImageBitmap(drawerIcon);
			} else if (null != iconDrawableResId) {
				if(iconDrawableResId == BLANK_DRAWABLE_RESOURCE_ID) {
					holder.iconImage.setVisibility(View.INVISIBLE);
				} else {
					holder.iconImage.setVisibility(View.VISIBLE);
					holder.iconImage.setImageResource(iconDrawableResId);
				}
			} else {
				holder.iconImage.setVisibility(View.VISIBLE);
				holder.iconImage.setImageResource(R.drawable.menu_act_current_events);
			}

            TintUtils.tintImageView(ContextCompat.getColor(parent.getContext(), R.color.nav_drawer_icon),
					holder.iconImage);

			// Set the badge text if there is one set
			holder.badgeText.setVisibility(badgeNumber != null ? View.VISIBLE : View.GONE);
			holder.badgeText.setText(badgeNumber != null ? ("" + badgeNumber) : "");

			// If the group has children, show the expand/collapse arrow
			if (getChildrenCount(groupPosition) > 0) {
				if (isExpanded) {
                    Drawable collapseDrawable = ContextCompat.getDrawable(parent.getContext(), R.drawable.ic_group_collapse);
                    collapseDrawable = TintUtils.tintDrawable(collapseDrawable, ContextCompat.getColor(parent.getContext(),
                            R.color.nav_drawer_icon));
                    holder.expandCollapseImage.setImageDrawable(collapseDrawable);
				}
				else {Drawable expandDrawable = ContextCompat.getDrawable(parent.getContext(), R.drawable.ic_group_expand);
                    expandDrawable = TintUtils.tintDrawable(expandDrawable, ContextCompat.getColor(parent.getContext(),
                            R.color.nav_drawer_icon));
                    holder.expandCollapseImage.setImageDrawable(expandDrawable);
				}
				holder.expandCollapseImage.setVisibility(View.VISIBLE);
			}
			else {
				holder.expandCollapseImage.setVisibility(View.GONE);
			}

			return convertView;
		}

		if (BuildConfig.DEBUG) {
			Log.w(TAG, "getGroupView: returning a null group view");
		}
		return null;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Object group = getGroup(groupPosition);

		if (group instanceof DrawerItem) {
			List<DrawerItem> childDrawerItemList = ((DrawerItem) group).getChildDrawerItemList();
			if (childDrawerItemList != null) {
				return childDrawerItemList.size();
			}
		}
		return 0;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Object group = getGroup(groupPosition);

		if (group instanceof DrawerItem) {
			List<DrawerItem> childDrawerItemList = ((DrawerItem) group).getChildDrawerItemList();
			return childDrawerItemList.get(childPosition);
		}

		if (BuildConfig.DEBUG) {
			Log.w(TAG, "getChild: unknown group type");
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return getChild(groupPosition, childPosition).hashCode();
	}

	@Override
	public int getChildTypeCount() {
		return VIEW_TYPE_CHILD_COUNT;
	}

	@Override
	public int getChildType(int groupPosition, int childPosition) {
		Object child = getChild(groupPosition, childPosition);
		if (child instanceof DrawerItem) {
			return VIEW_TYPE_CHILD_DRAWER_ITEM;
		}

		if (BuildConfig.DEBUG) {
			Log.w(TAG, "getChildType: unknown child type");
		}
		return -1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (BuildConfig.DEBUG) {
			/*
			Log.d(TAG, "getChildView: groupPosition = " + groupPosition
					+ " childPosition = " + childPosition
					+ " isLastChild = " + isLastChild);
			 */
		}

		int childViewType = getChildType(groupPosition, childPosition);
		if (childViewType == VIEW_TYPE_CHILD_DRAWER_ITEM) {
			final ChildDrawerItemViewHolder holder;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.list_drawer_sub_item, parent, false);

				holder = new ChildDrawerItemViewHolder();
				holder.titleText = (TextView) convertView.findViewById(R.id.list_drawer_sub_item_title_text);
				holder.iconImage = (ImageView) convertView.findViewById(R.id.list_drawer_sub_item_icon_image);

				convertView.setTag(holder);
			}
			else {
				holder = (ChildDrawerItemViewHolder) convertView.getTag();
			}

			DrawerItem childDrawerItem = (DrawerItem) getChild(groupPosition, childPosition);
			Integer titleStringResId = childDrawerItem.getTitleStringResId();
			String title = childDrawerItem.getTitle();
			Integer iconDrawableResId = childDrawerItem.getIconDrawableResId();

			// Depending on way the title string was passed, use that
			if (titleStringResId != null) {
				holder.titleText.setText(titleStringResId);
			}
			else if (title != null) {
				holder.titleText.setText(title);
			}

			if(iconDrawableResId != null) {
				if(iconDrawableResId == BLANK_DRAWABLE_RESOURCE_ID) {
					holder.iconImage.setVisibility(View.INVISIBLE);
				} else {
					holder.iconImage.setImageResource(iconDrawableResId);
				}
			}
            Drawable drawable = ContextCompat.getDrawable(parent.getContext(), iconDrawableResId);
            drawable = TintUtils.tintDrawable(drawable, ContextCompat.getColor(parent.getContext(),
                    R.color.nav_drawer_icon));
            holder.iconImage.setImageDrawable(drawable);

			return convertView;
		}
		if (BuildConfig.DEBUG) {
			Log.w(TAG, "getChildView: returning a null child view");
		}
		return null;
	}

	public void clearAll() {
		mDrawerObjectList.clear();
		notifyDataSetChanged();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	private static class DrawerSectionViewHolder {
		TextView titleText;
	}

	private static class DrawerItemViewHolder {
		TextView titleText;
		TextView badgeText;
		ImageView iconImage;
		ImageView expandCollapseImage;
	}

	private static class ChildDrawerItemViewHolder {
		TextView titleText;
		ImageView iconImage;
	}
}
