package com.universalstudios.orlandoresort.view;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.universalstudios.orlandoresort.R;

/**
 * Created by kbojarski on 7/7/16.
 */

public class TintUtils {

	public static Drawable tintDrawable(Drawable drawable, int color, Mode tintMode) {
		if (drawable != null) {
			Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
			DrawableCompat.setTintMode(wrappedDrawable, tintMode);
			DrawableCompat.setTint(wrappedDrawable, color);
			return wrappedDrawable;
		}
		return null;
	}

	public static Drawable tintDrawable(Drawable drawable, int color) {
		return tintDrawable(drawable, color, Mode.SRC_IN);
	}

	public static void tintImageView(int color, ImageView... imageViews) {
		for (ImageView imageView : imageViews) {
			tintDrawable(imageView.getDrawable(), color);
		}
	}

	public static void tintAllMenuItems(Menu menu, Context context) {
		int color = ContextCompat.getColor(context, R.color.menu_item_icon);
		for (int i = 0; i < menu.size(); ++i) {
			tintMenuItem(menu.getItem(i), color);
		}
	}

	private static void tintMenuItem(MenuItem menuItem, int color) {
		if (menuItem != null) {
			Drawable iconDrawable = menuItem.getIcon();
			if (iconDrawable != null) {
				tintDrawable(iconDrawable, color);
			}
		}
	}
}
