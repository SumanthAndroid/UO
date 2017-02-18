/**
 * 
 */
package com.universalstudios.orlandoresort.view.sectionedlistheaders;

import java.util.LinkedHashMap;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerSectionHeader;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.SectionHeaderAdapter;

/**
 * 
 * 
 * @author Steven Byle
 */
public class SectionedListAdapter extends BaseAdapter {
	private final static int VIEW_TYPE_SECTION_HEADER = 0;

	public final Map<DrawerSectionHeader, Adapter> mSections;
	public final SectionHeaderAdapter mSectionHeaderAdapter;

	public SectionedListAdapter(int sectionHeaderLayoutResId, int sectionHeaderTitleTextViewId) {
		mSections = new LinkedHashMap<DrawerSectionHeader, Adapter>();
		mSectionHeaderAdapter = new SectionHeaderAdapter(sectionHeaderLayoutResId, sectionHeaderTitleTextViewId);
	}

	public void addSection(DrawerSectionHeader sectionHeader, Adapter sectionAdapter) {
		mSections.put(sectionHeader, sectionAdapter);
		mSectionHeaderAdapter.add(sectionHeader);
	}

	@Override
	public Object getItem(int position) {
		for (DrawerSectionHeader sectionHeader : mSections.keySet()) {

			int sectionHeaderCount = sectionHeader.isHidden() ? 0 : 1;

			// Check if the position is the section header
			if (position == sectionHeaderCount - 1) {
				return sectionHeader;
			}

			// Check if the position is an item in the section
			Adapter sectionAdapter = mSections.get(sectionHeader);
			int sectionSize = sectionAdapter.getCount() + sectionHeaderCount;
			if (position < sectionSize) {
				return sectionAdapter.getItem(position - sectionHeaderCount);
			}

			// Otherwise, move to the next section
			position -= sectionSize;
		}
		return null;
	}

	@Override
	public int getCount() {
		// Add up all the items in the sections, plus one for each section header
		int total = 0;
		for (Adapter sectionAdapter : mSections.values()) {
			total += sectionAdapter.getCount();
		}
		for (DrawerSectionHeader sectionHeader : mSections.keySet()) {
			total += sectionHeader.isHidden() ? 0 : 1;
		}
		return total;
	}

	@Override
	public int getViewTypeCount() {
		// Add up all of the view types for headers and sections
		int viewTypeCount = mSectionHeaderAdapter.getViewTypeCount();
		for (Adapter sectionAdapter : mSections.values()) {
			viewTypeCount += sectionAdapter.getViewTypeCount();
		}
		return viewTypeCount;
	}

	@Override
	public int getItemViewType(int position) {
		int type = 1;
		for (DrawerSectionHeader sectionHeader : mSections.keySet()) {

			int sectionHeaderCount = sectionHeader.isHidden() ? 0 : 1;

			// Check if the position is the section header
			if (position == sectionHeaderCount - 1) {
				return VIEW_TYPE_SECTION_HEADER;
			}

			// Check if the position is an item in the section
			Adapter sectionAdapter = mSections.get(sectionHeader);
			int size = sectionAdapter.getCount() + sectionHeaderCount;
			if (position < size) {
				return type + sectionAdapter.getItemViewType(position - sectionHeaderCount);
			}

			// Otherwise, move to the next section
			position -= size;

			type += sectionAdapter.getViewTypeCount();
		}
		return -1;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) != VIEW_TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionNum = 0;
		for (DrawerSectionHeader sectionHeader : mSections.keySet()) {
			Adapter sectionAdapter = mSections.get(sectionHeader);

			int sectionHeaderCount = sectionHeader.isHidden() ? 0 : 1;
			int size = sectionAdapter.getCount() + sectionHeaderCount;

			// Check if the position is the section header
			if (position == sectionHeaderCount - 1) {
				return mSectionHeaderAdapter.getView(sectionNum, convertView, parent);
			}

			// Check if the position is an item in the section
			if (position < size) {
				return sectionAdapter.getView(position - sectionHeaderCount, convertView, parent);
			}

			// Otherwise, move to the next section
			position -= size;
			sectionNum++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		for (DrawerSectionHeader sectionHeader : mSections.keySet()) {

			int sectionHeaderCount = sectionHeader.isHidden() ? 0 : 1;

			// Check if the position is the section header
			if (position == sectionHeaderCount - 1) {
				return sectionHeader.hashCode();
			}

			// Check if the position is an item in the section
			Adapter sectionAdapter = mSections.get(sectionHeader);
			int sectionSize = sectionAdapter.getCount() + sectionHeaderCount;
			if (position < sectionSize) {
				return sectionAdapter.getItemId(position - sectionHeaderCount);
			}

			// Otherwise, move to the next section
			position -= sectionSize;
		}
		return -1;
	}

}
