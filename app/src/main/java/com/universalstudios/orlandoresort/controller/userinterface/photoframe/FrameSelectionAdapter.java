package com.universalstudios.orlandoresort.controller.userinterface.photoframe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrame;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/13/16.
 * Class: FrameSelectionAdapter
 * Class Description: Adapter to hold the frames available for selection
 */
public class FrameSelectionAdapter extends RecyclerView.Adapter<FrameSelectionAdapter.FrameSelectionViewHolder> {

    public interface PhotoFrameSelectionAdapterListener {
        void onFrameSelected(PhotoFrame frame);
    }

    public static final String TAG = "FrameSelectionAdapter";

    private List<PhotoFrame> mFrames;
    private Context mContext;
    private PhotoFrameSelectionAdapterListener mListener;

    public FrameSelectionAdapter(List<PhotoFrame> frames, Context context, PhotoFrameSelectionAdapterListener listener) {
        this.mFrames = frames;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public FrameSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photoframe, parent, false);
        return new FrameSelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FrameSelectionViewHolder holder, int position) {
        final PhotoFrame frame = mFrames.get(position);
        Picasso.with(mContext)
                .load(frame.getThumbnailUrl())
                .into(holder.mFrameImage);
        holder.mFrameTitle.setText(frame.getShortDescription());
        holder.mFrameSubTitle.setText(frame.getLongDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onFrameSelected(frame);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFrames.size();
    }

    protected class FrameSelectionViewHolder extends RecyclerView.ViewHolder {

        ImageView mFrameImage;
        TextView mFrameTitle;
        TextView mFrameSubTitle;

        public FrameSelectionViewHolder(View itemView) {
            super(itemView);
            mFrameImage = (ImageView) itemView.findViewById(R.id.ivFrameImage);
            mFrameTitle = (TextView) itemView.findViewById(R.id.tvFrameSelectionItem);
            mFrameSubTitle = (TextView) itemView.findViewById(R.id.tvFrameSelectionItemDetail);
        }
    }
}
