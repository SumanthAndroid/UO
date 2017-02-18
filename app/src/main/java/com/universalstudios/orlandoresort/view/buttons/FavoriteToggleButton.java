package com.universalstudios.orlandoresort.view.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ToggleButton;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;

public class FavoriteToggleButton extends ToggleButton{
	private static final String TAG = FavoriteToggleButton.class.getSimpleName();

	private String mPoiName;

	public FavoriteToggleButton(Context context) {
		this(context, null);
	}

	public FavoriteToggleButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FavoriteToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);

		// Update the content description since the checked state changed
		setContentDescriptionByPoiName(mPoiName);
	}

	public void setContentDescriptionByPoiName(String poiName) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "setContentDescriptionByPoiName");
		}

		// Hold a copy of the POI name
		mPoiName = (poiName != null ? poiName : "");

		int contentDescResId;
		if (isChecked()) {
			contentDescResId = R.string.favorite_toggle_button_on_formatted_content_description;
		}
		else {
			contentDescResId = R.string.favorite_toggle_button_off_formatted_content_description;
		}
		String contentDescription = getContext().getString(contentDescResId, mPoiName);
		setContentDescription(contentDescription);
	}
}
