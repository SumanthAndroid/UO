/**
 *
 */
package com.universalstudios.orlandoresort.view.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.view.fonts.TextView;

/**
 * @author Steven Byle
 */
public class FilterTabButton extends LinearLayout implements OnClickListener {
	private static final String TAG = FilterTabButton.class.getSimpleName();

	private static final int[] ATTR_CHECKED = {R.attr.state_checked};
	private static final String KEY_STATE_OTHER_STATE = "KEY_STATE_OTHER_STATE";
	private static final String KEY_STATE_CHECKED = "KEY_STATE_CHECKED";
	private static final String KEY_STATE_IMG_RES_ID = "KEY_STATE_IMG_RES_ID";
	private static final String KEY_STATE_TEXT = "KEY_STATE_TEXT";

	private boolean mChecked;
	private int mImageDrawableResId;
	private String mText;
	private ImageView mImageView;
	private TextView mTextView;
	private OnClickListener mParentOnClickListener;

	public FilterTabButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Don't style if eclipse is using the view in preview mode
		if (isInEditMode()) {
			return;
		}

		// Get default start from the xml
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTabButton);
		mChecked = typedArray.getBoolean(R.styleable.CustomTabButton_state_checked, false);
		mImageDrawableResId = typedArray.getResourceId(R.styleable.CustomTabButton_img_src, -1);
		mText = typedArray.getString(R.styleable.CustomTabButton_text);
		typedArray.recycle();

		// Setup state for the tab button
		setSaveEnabled(true);
		setClickable(true);
		setOnClickListener(this);
		setBackgroundResource(R.drawable.state_list_explore_filter_tab_background);

		// Inflate the tab content into the parent tab
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.button_filter_tab_content, this, true);

		// Get references to the child views
		mImageView = (ImageView) findViewById(R.id.button_filter_tab_content_image);
		mTextView = (TextView) findViewById(R.id.button_filter_tab_content_text);

		// Update child views state
		updateImage();
		updateText();
		updateContentDescription();
		updateImageVisibility();
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

		if (mChecked) {
			mergeDrawableStates(drawableState, ATTR_CHECKED);
		}

		return drawableState;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_STATE_OTHER_STATE, super.onSaveInstanceState());

		// Save all state variables
		bundle.putBoolean(KEY_STATE_CHECKED, mChecked);
		bundle.putInt(KEY_STATE_IMG_RES_ID, mImageDrawableResId);
		bundle.putString(KEY_STATE_TEXT, mText);
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;

			// Restore all state variables
			mChecked = bundle.getBoolean(KEY_STATE_CHECKED);
			mImageDrawableResId = bundle.getInt(KEY_STATE_IMG_RES_ID);
			mText = bundle.getString(KEY_STATE_TEXT);

			// Update views
			updateImage();
			updateText();
			updateContentDescription();
			updateImageVisibility();
			refreshDrawableState();

			state = bundle.getParcelable(KEY_STATE_OTHER_STATE);
		}
		super.onRestoreInstanceState(state);
	}


	@Override
	public void setOnClickListener(OnClickListener l) {
		// Don't let the parent take the clicks, just forward them to it
		if (l != this) {
			mParentOnClickListener = l;
		} else {
			super.setOnClickListener(l);
		}
	}

	@Override
	public void onClick(View v) {
		setChecked(!mChecked);

		// Forward the parent click events
		if (mParentOnClickListener != null) {
			mParentOnClickListener.onClick(v);
		}
	}

	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;

			updateText();
			updateImageVisibility();
			updateContentDescription();
			refreshDrawableState();
		}
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setImageDrawableResId(int drawableResId) {
		if (mImageDrawableResId != drawableResId) {
			mImageDrawableResId = drawableResId;
			updateImage();
			updateImageVisibility();
		}
	}

	public int getImageDrawableResId() {
		return mImageDrawableResId;
	}

	public void setText(String text) {
		if (mText == null || !mText.equals(text)) {
			mText = text;
			updateText();
			updateContentDescription();
		}
	}

	public String getText() {
		return mText;
	}

	public int getTextLineCount() {
		return mTextView.getLineCount();
	}

	private void updateImageVisibility() {
		mImageView.setVisibility(mChecked && mImageDrawableResId != -1 ? View.VISIBLE : View.GONE);
	}

	private void updateImage() {
		if (mImageDrawableResId != -1) {
			Drawable drawable = ContextCompat.getDrawable(getContext(), mImageDrawableResId);

			mImageView.setImageDrawable(TintUtils.tintDrawable(drawable,
					ContextCompat.getColor(getContext(), R.color.explore_filter_tab_button_icon)));
		}
	}

	private void updateText() {
		if (mText != null) {
			mTextView.setText(mText);
		}

		if (mChecked) {
			mTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.explore_filter_tab_button_text_on));
		} else {
			mTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.explore_filter_tab_button_text_off));
		}
		mTextView.setVisibility(mText != null ? View.VISIBLE : View.GONE);
	}

	private void updateContentDescription() {
		int contentDescFormattedResId = mChecked ? R.string.filter_tab_selected_content_description_formatted
				: R.string.filter_tab_not_selected_content_description_formatted;
		Context context = mImageView.getContext();
		String fiterTabText = (mText != null) ? mText : "";
		String filterTabContentDesc = context.getString(contentDescFormattedResId, fiterTabText);
		setContentDescription(filterTabContentDesc);
	}

}
