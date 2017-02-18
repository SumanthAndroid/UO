/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalstudios.orlandoresort.view.buttons.FavoriteToggleButton;

/**
 * 
 * 
 * @author Steven Byle
 */
public class PoiViewHolder extends RecyclerView.ViewHolder {
	public ViewGroup rootLayout;
	public TextView displayNameText;
	public TextView venueNameText;
	public TextView extraInfoText;
	public ImageView listImage;
	public ImageView listImageNoImage;
	public LinearLayout circleBadgeRootLayout;
	public LinearLayout waitTimeLayout;
	public TextView waitTimeMinNumText;
	public FrameLayout showTimeLayout;
	public View showTimeBackgroundGray;
	public View showTimeBackgroundBlue;
	public TextView showTimeStartsText;
	public TextView showTimeOpensText;
	public TextView showTimeStartsTimeText;
	public TextView showTimeAmPmText;
	public LinearLayout guideMeLayout;
	public View guideMeDivider;
	public FavoriteToggleButton favoriteToggleButton;
	public Button locateButton;
	public LinearLayout closedLayout;
	public TextView closedText;
	public TextView closedWeatherText;
	public TextView closedTemporaryText;
	public TextView closedCapacityText;

	public PoiViewHolder(View itemView) {
		super(itemView);
	}
}
