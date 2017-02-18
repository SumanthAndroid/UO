package com.universalstudios.orlandoresort.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.universalstudios.orlandoresort.R;

import java.util.List;

/**
 *
 */
public abstract class SimpleSpinnerAdapter<T> extends ArrayAdapter<T> {
	@LayoutRes
	private static final int LAYOUT_RES_ID_SELECTED = R.layout.spinner_selected_item;

	@LayoutRes
	private static final int LAYOUT_RES_ID_DROPDOWN = R.layout.spinner_dropdown_item;

	@IdRes
	private static final int TEXTVIEW_RES_ID_SELECTED = android.R.id.text1;

	@IdRes
	private static final int TEXTVIEW_RES_ID_DROPDOWN = android.R.id.text1;

	protected List<T> mObjects;

	public SimpleSpinnerAdapter(Context context, List<T> objects) {
		super(context, LAYOUT_RES_ID_DROPDOWN, TEXTVIEW_RES_ID_DROPDOWN);
		mObjects = objects;
	}

	public abstract String getItemText(T object);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		if (convertView == null) {
			convertView = layoutInflater.inflate(LAYOUT_RES_ID_SELECTED, parent, false);
		}

		TextView textView = (TextView) convertView.findViewById(TEXTVIEW_RES_ID_SELECTED);
		String itemText = getItemText(getItem(position));
		textView.setText(itemText);

		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		if (convertView == null) {
			convertView = layoutInflater.inflate(LAYOUT_RES_ID_DROPDOWN, parent, false);
		}

		TextView textView = (TextView) convertView.findViewById(TEXTVIEW_RES_ID_DROPDOWN);
		String itemText = getItemText(getItem(position));
		textView.setText(itemText);

		return convertView;
	}

	@Override
	public T getItem(int position) {
		if (mObjects != null) {
			return mObjects.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		if (mObjects != null) {
			return mObjects.size();
		}
		return 0;
	}

	@Override
	public int getPosition(T item) {
		if (mObjects != null) {
			return mObjects.indexOf(item);
		}
		return -1;
	}
}