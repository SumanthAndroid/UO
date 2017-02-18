package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.news.NewsUtils;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;

/**
 * {@link RecyclerView.Adapter} that wraps a {@link Cursor} to show news items with an option
 * to show all as a footer.
 */
public class HomeNewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor mCursor;
    private HomeNewsRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public HomeNewsRecyclerAdapter(Cursor cursor,
            HomeNewsRecyclerAdapter.OnItemClickListener onItemClickListener) {
        mCursor = cursor;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            // Add an item for the footer view
            return mCursor.getCount() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ViewAllViewHolder.LAYOUT_RES_ID;
        } else if (position >= 0) {
            return NewsItemViewHolder.LAYOUT_RES_ID;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NewsItemViewHolder.LAYOUT_RES_ID:
                return new NewsItemViewHolder(layoutInflater.inflate(NewsItemViewHolder.LAYOUT_RES_ID, parent, false));
            case ViewAllViewHolder.LAYOUT_RES_ID:
                return new ViewAllViewHolder(layoutInflater.inflate(ViewAllViewHolder.LAYOUT_RES_ID, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case NewsItemViewHolder.LAYOUT_RES_ID:
                // Let the cursor adapter bind the view for the news items
                if (mCursor != null) {
                    mCursor.moveToPosition(position);

                    // Get data from database row
                    String messageHeading = mCursor.getString(mCursor.getColumnIndex(NewsTable.COL_MESSAGE_HEADING));
                    String messageBody = mCursor.getString(mCursor.getColumnIndex(NewsTable.COL_MESSAGE_BODY));
                    Long startDate = mCursor.getLong(mCursor.getColumnIndex(NewsTable.COL_START_DATE_IN_MILLIS));
                    boolean hasBeenRead = (mCursor.getInt(mCursor.getColumnIndex(NewsTable.COL_HAS_BEEN_READ)) != 0);
                    final String newsObjectJson = mCursor.getString(mCursor.getColumnIndex(NewsTable.COL_NEWS_OBJECT_JSON));

                    // Set the text
                    NewsItemViewHolder holder = (NewsItemViewHolder) viewHolder;
                    holder.headingTextRead.setText(messageHeading != null ? messageHeading : "");
                    holder.headingTextUnread.setText(messageHeading != null ? messageHeading : "");
                    holder.bodyText.setText(messageBody != null ? messageBody.replace('\n', ' ') : "");

                    Resources res = holder.dateTextRead.getResources();
                    String formattedStartDate = NewsUtils.getFormattedDate(startDate, res);
                    holder.dateTextRead.setText(formattedStartDate != null ? formattedStartDate : "");
                    holder.dateTextUnread.setText(formattedStartDate != null ? formattedStartDate : "");

                    // Update the views based on the read state
                    holder.rootContainer.setBackgroundColor(res.getColor(R.color.news_list_background_white));

                    holder.headingTextRead.setVisibility(View.GONE);
                    holder.headingTextUnread.setVisibility(View.VISIBLE);

                    holder.dateTextRead.setVisibility(View.GONE);
                    holder.dateTextUnread.setVisibility(View.VISIBLE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                News news = GsonObject.fromJson(newsObjectJson, News.class);
                                mOnItemClickListener.onNewsItemClicked(news);
                            }
                        }
                    });
                }
                break;
            case ViewAllViewHolder.LAYOUT_RES_ID:
                // No data to bind for the view all item, just show it
                ViewAllViewHolder holder = (ViewAllViewHolder) viewHolder;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onNewsViewAllClicked();
                        }
                    }
                });
                break;
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        notifyDataSetChanged();
        return oldCursor;
    }

    static class NewsItemViewHolder extends RecyclerView.ViewHolder {
        public static final int LAYOUT_RES_ID = R.layout.list_news_item;
        View rootContainer;
        TextView headingTextUnread;
        TextView headingTextRead;
        TextView bodyText;
        TextView dateTextUnread;
        TextView dateTextRead;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            rootContainer = itemView.findViewById(R.id.list_news_item_root_container);
            headingTextRead = (TextView) itemView.findViewById(R.id.list_news_item_heading_text_read);
            headingTextUnread = (TextView) itemView.findViewById(R.id.list_news_item_heading_text_unread);
            bodyText = (TextView) itemView.findViewById(R.id.list_news_item_body_text);
            dateTextRead = (TextView) itemView.findViewById(R.id.list_news_item_date_text_read);
            dateTextUnread = (TextView) itemView.findViewById(R.id.list_news_item_date_text_unread);
        }
    }

    static class ViewAllViewHolder extends RecyclerView.ViewHolder {
        public static final int LAYOUT_RES_ID = R.layout.list_news_item_view_all;

        public ViewAllViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onNewsItemClicked(News news);
        void onNewsViewAllClicked();
    }
}
