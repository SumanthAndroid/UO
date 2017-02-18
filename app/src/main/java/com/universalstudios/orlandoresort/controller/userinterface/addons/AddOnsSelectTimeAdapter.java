package com.universalstudios.orlandoresort.controller.userinterface.addons;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.fonts.TextView;

import java.util.List;


public class AddOnsSelectTimeAdapter extends RecyclerView.Adapter<AddOnsSelectTimeAdapter.ViewHolder> {
    private static final String TAG = AddOnsSelectTimeAdapter.class.getSimpleName();
    private static final boolean FILL_BY_COLUMN_FIRST = false;

    private static final int MIN_ITEM_COUNT = 8;
    private static final int COLUMN_COUNT = 2;

    private List<String> mDataset;
    private ItemListener mListener;

    private int mSelectedItemPosition;

    public AddOnsSelectTimeAdapter(@NonNull List<String> items, ItemListener listener) {
        this.mDataset = items;
        this.mListener = listener;
        this.mSelectedItemPosition = -1;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_on_select_time, parent, false));
    }

    @Override
    public int getItemCount() {
        if (mDataset.size() > MIN_ITEM_COUNT) {
            // Allow columns to always be even
            return mDataset.size() + (mDataset.size() % 2);
        } else {
            return MIN_ITEM_COUNT;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //get proper position to populate the grid in the correct order
        final int translatePosition = FILL_BY_COLUMN_FIRST ? translatePosition(position): position;

        final String currentItem = mDataset.size() > translatePosition ? mDataset.get(translatePosition) : null;

        if (currentItem != null) {
            holder.title.setText(currentItem);
            holder.container.setEnabled(true);
        } else {
            holder.container.setEnabled(false);
        }

        holder.container.setSelected(holder.getAdapterPosition() == mSelectedItemPosition);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastLastSelected = mSelectedItemPosition;
                mSelectedItemPosition = holder.getAdapterPosition();
                notifyItemChanged(lastLastSelected);
                notifyItemChanged(mSelectedItemPosition);
                mListener.onItemClick(currentItem);
            }
        });
    }

    /**
     * grid is populated first by column, then row
     * <p>
     * a, b, c
     * d, e, f
     * g, h, i
     * <p>
     * translates to
     * <p>
     * a, d, g
     * b, e, h
     * c, f, i
     *
     * @param position
     * @return
     */
    private int translatePosition(int position) {
        int x = position % COLUMN_COUNT;
        int y = position / COLUMN_COUNT;
        int numRows = (int) Math.ceil((double) getItemCount() / COLUMN_COUNT);
        return x * numRows + y;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.item_add_on_select_time_container);
            title = (TextView) itemView.findViewById(R.id.item_add_on_select_time_title);
        }
    }

    interface ItemListener {
        void onItemClick(String item);
    }
}
