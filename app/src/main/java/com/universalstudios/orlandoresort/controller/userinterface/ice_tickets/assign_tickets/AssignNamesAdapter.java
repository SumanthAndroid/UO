package com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.assign_tickets;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.TravelPartyMember;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

import java.util.List;

public class AssignNamesAdapter extends ArrayAdapter<TravelPartyMember> {
	public AssignNamesAdapter(Context context, int resource) {
		super(context, resource);
	}

	public AssignNamesAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public AssignNamesAdapter(Context context, int resource, TravelPartyMember[] objects) {
		super(context, resource, objects);
	}

	public AssignNamesAdapter(Context context, int resource, int textViewResourceId, TravelPartyMember[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public AssignNamesAdapter(Context context, int resource, List<TravelPartyMember> objects) {
		super(context, resource, objects);
	}

	public AssignNamesAdapter(Context context, int resource, int textViewResourceId, List<TravelPartyMember> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, R.layout.item_assign_name);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, R.layout.item_assign_name);
	}

	private @NonNull View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		final View view;
		final TextView text;
		final ImageView icon;

		if (convertView == null) {
			view = inflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}
		text = (TextView) view.findViewById(R.id.assign_name_name);
		icon = (ImageView) view.findViewById(R.id.assign_name_increase);

		Resources resources = getContext().getResources();
		TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();
		if (position == getCount()-1) {
			text.setText(tridionConfig.getAddNewNameLabel());
			text.setTextColor(resources.getColor(R.color.text_primary));
			icon.setVisibility(View.VISIBLE);
		} else {
			text.setText(getItem(position).getFullName());
			text.setTextColor(resources.getColor(R.color.text_gray_darkest));
			icon.setVisibility(View.INVISIBLE);
		}

		return view;
	}

	@Override
	public int getCount() {
		int count = super.getCount();
		return count + 1;
	}
}
