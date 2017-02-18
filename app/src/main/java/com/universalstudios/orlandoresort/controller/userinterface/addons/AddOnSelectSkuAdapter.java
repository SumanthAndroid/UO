package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;

import java.util.List;

/**
 * @author acampbell
 */
public class AddOnSelectSkuAdapter extends RecyclerView.Adapter<AddOnSelectSkuAdapter.ViewHolder> {

    private List<String> items;
    private ItemListener listener;
    private int selectedPosition;

    public AddOnSelectSkuAdapter(@NonNull List<String> items, @NonNull ItemListener listener, @Nullable String selectedItem) {
        this.items = items;
        this.listener = listener;
        selectedPosition = items.indexOf(selectedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_on_select_sku, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String item = items.get(position);
        holder.container.setSelected(selectedPosition == position);
        holder.title.setText(item);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastSelected = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(lastSelected);
                notifyItemChanged(selectedPosition);
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.item_add_on_select_sku_container);
            title = (TextView) itemView.findViewById(R.id.item_add_on_select_sku_title);
        }
    }

    interface ItemListener {
        void onItemClick(String item);
    }
}
