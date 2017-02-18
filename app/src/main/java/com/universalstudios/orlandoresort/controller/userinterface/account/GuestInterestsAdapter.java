package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.GuestPreference;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestInterestsAdapter extends RecyclerView.Adapter<GuestInterestsAdapter.ViewHolder> implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = GuestInterestsAdapter.class.getSimpleName();

    private List<String> mKeys;
    private Map<String, Integer> mProfileValues;
    private Map<String, Integer> mChangedValues;

    public GuestInterestsAdapter(List<String> keys, Map<String, Integer> profileValueMap ) {
        this.mKeys = keys;
        this.mProfileValues = profileValueMap;
        this.mChangedValues = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_preference_seekbar,
                parent,
                false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String key = mKeys.get(position);

        holder.title.setText(key);
        holder.seekbar.setTag(key);

        //subtract 1 for 0-index
        holder.seekbar.setMax(GuestPreference.STRING_LOOKUP_MAP.size() - 1);
        holder.seekbar.setProgress(mProfileValues.get(key));
        holder.seekbar.setOnSeekBarChangeListener(this);
    }

    @Override
    public int getItemCount() {
        return mKeys.size();
    }

    protected Map<String, Integer> getSeekBarValues(){
        return this.mChangedValues;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //empty
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //empty
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.mChangedValues.put(seekBar.getTag().toString(), seekBar.getProgress());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup container;
        TextView title;
        SeekBar seekbar;

        ViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView.findViewById(R.id.item_preference_container);
            title = (TextView) itemView.findViewById(R.id.item_preference_title);
            seekbar = (SeekBar) itemView.findViewById(R.id.item_preference_seek_bar);
        }
    }
}
