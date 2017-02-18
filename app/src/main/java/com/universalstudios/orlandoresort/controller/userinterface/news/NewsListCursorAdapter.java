package com.universalstudios.orlandoresort.controller.userinterface.news;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;

/**
 * 
 * 
 * @author Steven Byle
 */
public class NewsListCursorAdapter extends CursorAdapter implements ListAdapter {
	private static final String TAG = NewsListCursorAdapter.class.getSimpleName();

	private boolean mShowReadState;

	public NewsListCursorAdapter(Context context, Cursor cursor, boolean showReadState) {
		super(context, cursor, 0);
		mShowReadState = showReadState;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.list_news_item, parent, false);

		NewsRowViewHolder holder = new NewsRowViewHolder();
		holder.rootContainer = row.findViewById(R.id.list_news_item_root_container);
		holder.headingTextRead = (TextView) row.findViewById(R.id.list_news_item_heading_text_read);
		holder.headingTextUnread = (TextView) row.findViewById(R.id.list_news_item_heading_text_unread);
		holder.bodyText = (TextView) row.findViewById(R.id.list_news_item_body_text);
		holder.dateTextRead = (TextView) row.findViewById(R.id.list_news_item_date_text_read);
		holder.dateTextUnread = (TextView) row.findViewById(R.id.list_news_item_date_text_unread);

		row.setTag(holder);
		return row;
	}

	@Override
	public void bindView(View row, final Context context, Cursor cursor) {

		// Get data from database row
		String messageHeading = cursor.getString(cursor.getColumnIndex(NewsTable.COL_MESSAGE_HEADING));
		String messageBody = cursor.getString(cursor.getColumnIndex(NewsTable.COL_MESSAGE_BODY));
		Long startDate = cursor.getLong(cursor.getColumnIndex(NewsTable.COL_START_DATE_IN_MILLIS));
		boolean hasBeenRead = (cursor.getInt(cursor.getColumnIndex(NewsTable.COL_HAS_BEEN_READ)) != 0);

		String formattedStartDate = NewsUtils.getFormattedDate(startDate, context.getResources());

		// Set the text
		final NewsRowViewHolder holder = (NewsRowViewHolder) row.getTag();
		holder.headingTextRead.setText(messageHeading != null ? messageHeading : "");
		holder.headingTextUnread.setText(messageHeading != null ? messageHeading : "");
		holder.bodyText.setText(messageBody != null ? messageBody.replace('\n', ' ') : "");
		holder.dateTextRead.setText(formattedStartDate != null ? formattedStartDate : "");
		holder.dateTextUnread.setText(formattedStartDate != null ? formattedStartDate : "");

		// Set view state based on if the item has been read
		Resources r = context.getResources();

		// If not showing read state, assume every item is unread
		if (!mShowReadState) {
			hasBeenRead = false;
		}

		// Update the views based on the read state
		holder.rootContainer.setBackgroundColor(
				hasBeenRead ? r.getColor(R.color.news_list_background_gray) : r.getColor(R.color.news_list_background_white));

		holder.headingTextRead.setVisibility(hasBeenRead ? View.VISIBLE : View.GONE);
		holder.headingTextUnread.setVisibility(hasBeenRead ? View.GONE : View.VISIBLE);

		holder.dateTextRead.setVisibility(hasBeenRead ? View.VISIBLE : View.GONE);
		holder.dateTextUnread.setVisibility(hasBeenRead ? View.GONE : View.VISIBLE);
	}

	private static class NewsRowViewHolder {
		View rootContainer;
		TextView headingTextUnread;
		TextView headingTextRead;
		TextView bodyText;
		TextView dateTextUnread;
		TextView dateTextRead;
	}
}
