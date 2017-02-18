package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreference;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreferencesResult;
import com.universalstudios.orlandoresort.view.fonts.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestSeasonsAdapter extends RecyclerView.Adapter<GuestSeasonsAdapter.ViewHolder> implements CheckBox.OnCheckedChangeListener {
    private static final String TAG = GuestSeasonsAdapter.class.getSimpleName();

    private List<String> mSeasonsKeySet;
    private List<String> mSeasonDisplayValues;
    private GuestPreference mGuestPreference;

    private Map<String, Boolean> mChangedValues;


    public GuestSeasonsAdapter(GuestPreference guestPreference, GuestPreferencesResult dataset) {
        this.mSeasonsKeySet = new ArrayList<>(dataset.getSeasons().keySet());
        this.mGuestPreference = guestPreference;
        this.mSeasonDisplayValues = IceTicketUtils.getTridionConfig().getSeasonDisplayValues();
        this.mChangedValues = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_preference_checkbox,
                parent,
                false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String key = mSeasonsKeySet.get(position);
        final String displayValue = mSeasonDisplayValues.get(position);

        boolean checkValue = false;
        if (mGuestPreference != null && mGuestPreference.getVacationSeasonsResponse() != null) {
            checkValue = GuestPreference.isSeasonSelected(mGuestPreference.getVacationSeasonsResponse().get(key));
        }

        holder.checkBox.setTag(key);
        holder.checkBox.setText(displayValue);
        holder.checkBox.setChecked(checkValue);
        holder.checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public int getItemCount() {
        return mSeasonsKeySet.size();
    }

    protected Map<String, Boolean> getCheckboxValues() {
        return this.mChangedValues;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mChangedValues.put(buttonView.getTag().toString(), isChecked);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup container;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView.findViewById(R.id.item_preference_container);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_preference_checkbox);
        }

    }
}