package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.account.address.AddressInfo;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.databinding.ItemAddAddressBinding;

import java.util.List;

/**
 * @author tjudkins
 * @since 10/1/16
 */

public class ProfileAddressSpinnerAdapter extends ArrayAdapter<AddressInfo> {

    public ProfileAddressSpinnerAdapter(Context context) {
        super(context, R.layout.item_add_address);
    }

    public ProfileAddressSpinnerAdapter(Context context, AddressInfo[] objects) {
        super(context, R.layout.item_add_address, objects);
    }

    public ProfileAddressSpinnerAdapter(Context context, int resource, List<AddressInfo> objects) {
        super(context, resource, objects);
    }

    public ProfileAddressSpinnerAdapter(Context context, List<AddressInfo> objects) {
        super(context, R.layout.item_add_address, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = createViewFromResource(position, convertView, parent);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final View view = createViewFromResource(position, convertView, parent);
        return view;
    }

    private @NonNull View createViewFromResource(int position,
                                                 @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ItemAddAddressBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_add_address, parent, false);
        Resources resources = getContext().getResources();
        if (position == getCount()-1) {
            binding.profileInfoAddress.setText(IceTicketUtils.getTridionConfig().getAddAddressLabel());
            binding.profileInfoAddress.setTextColor(resources.getColor(R.color.text_primary));
            binding.profileInfoIncrease.setVisibility(View.VISIBLE);
        } else {
            binding.profileInfoAddress.setText(getItem(position).toString());
            binding.profileInfoAddress.setTextColor(resources.getColor(R.color.text_gray_darkest));
            binding.profileInfoIncrease.setVisibility(View.INVISIBLE);
        }

        return binding.getRoot();
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count + 1;
    }
}
