package com.universalstudios.orlandoresort.frommergeneedsrefactor.uo_network_library.view.fonts;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

@Deprecated
public class TextView extends android.widget.TextView {

	public TextView(Context context) {
		super(context);
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Don't style if eclipse is using the view in preview mode
		if (isInEditMode()) {
			return;
		}

		FontManager.getInstance().setFont(this, attrs);

		if(this.getText() != null) {
			this.setText(Html.fromHtml(this.getText().toString()));
		}
	}

	public void setFont(String fontPath) {
		FontManager.getInstance().setFont(this, fontPath);
	}

	public void setFont(int resId) {
		String fontPath = getContext().getString(resId);
		setFont(fontPath);
	}
}
