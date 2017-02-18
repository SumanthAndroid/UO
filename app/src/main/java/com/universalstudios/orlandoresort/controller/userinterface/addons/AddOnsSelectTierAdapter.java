package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.fonts.RadioButton;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


public class AddOnsSelectTierAdapter extends RecyclerView.Adapter<AddOnsSelectTierAdapter.ViewHolder> {

    private List<SelectTierObject> mDataset;
    private ItemListener mListener;

    private int mSelected;

    public AddOnsSelectTierAdapter(List<SelectTierObject> items, ItemListener listener) {
        this.mDataset = items;
        this.mListener = listener;
        this.mSelected = -1;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_on_select_tier, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SelectTierObject currentItem = mDataset.get(position);

        holder.radioButton.setChecked(position == mSelected);

        holder.title.setText(currentItem.getDisplayName());
        BigDecimal adultPrice = currentItem.getAdultPrice();
        BigDecimal childPrice = currentItem.getChildPrice();

        if (adultPrice != null) {
            holder.adult.setText("Adult: " + NumberFormat.getCurrencyInstance().format(adultPrice));
        } else {
            holder.adult.setVisibility(View.GONE);
        }

        if (childPrice != null) {
            holder.child.setText("Child: " + NumberFormat.getCurrencyInstance().format(childPrice));
        } else {
            holder.child.setVisibility(View.GONE);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastSelected = mSelected;
                mSelected = holder.getAdapterPosition();
                notifyItemChanged(mSelected);
                notifyItemChanged(lastSelected);
                mListener.onItemClick(currentItem);
            }
        };

        holder.container.setOnClickListener(listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup container;
        RadioButton radioButton;
        TextView title;
        TextView adult;
        TextView child;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView.findViewById(R.id.item_add_on_select_tier_container);
            radioButton = (RadioButton) itemView.findViewById(R.id.item_add_on_select_tier_radio);
            title = (TextView) itemView.findViewById(R.id.item_add_on_select_tier_title);
            adult = (TextView) itemView.findViewById(R.id.item_add_on_select_tier_adult);
            child = (TextView) itemView.findViewById(R.id.item_add_on_select_tier_child);
        }
    }

    interface ItemListener {
        void onItemClick(SelectTierObject item);
    }
}
