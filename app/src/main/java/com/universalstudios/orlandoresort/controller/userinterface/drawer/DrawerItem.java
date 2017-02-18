/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
public class DrawerItem {

	private final String title;
	private final Integer titleStringResId, iconDrawableResId;
	private Integer badgeNumber;
	private final List<DrawerItem> childDrawerItemList;
	private boolean visible;
    private String imageUrl;
    private Bitmap drawerIcon;
    private Context mContext;
    public Object tag;
    private WeakReference<DrawerItemExpandableListAdapter> adapterRef;
    public static int BLANK_DRAWABLE_RESOURCE_ID = -999999;

	public DrawerItem(Integer titleStringResId, Integer iconDrawableResId) {
		super();
		this.titleStringResId = titleStringResId;
		this.iconDrawableResId = iconDrawableResId;
		title = null;
		childDrawerItemList = new ArrayList<DrawerItem>();
		badgeNumber = null;
		visible = true;
	}

	public DrawerItem(String title, Integer iconDrawableResId) {
		super();
		this.title = title;
		this.iconDrawableResId = iconDrawableResId;
		titleStringResId = null;
		childDrawerItemList = new ArrayList<DrawerItem>();
		badgeNumber = null;
		visible = true;
	}

    public DrawerItem(String title, String imageUrl, Context context, Object tag, DrawerItemExpandableListAdapter adapter) {
        super();
        this.title = title;
        titleStringResId = null;
        childDrawerItemList = new ArrayList<>();
        iconDrawableResId = null;
        badgeNumber = null;
        visible = true;
        this.tag = tag;
        this.adapterRef = new WeakReference<>(adapter);
        this.imageUrl = imageUrl;
        this.mContext = context;
        new PreloadImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class PreloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap b = null;
            try {
               b = Picasso.with(mContext)
                        .load(imageUrl)
                        .get();
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            drawerIcon = bitmap;
            if (null != adapterRef && null != adapterRef.get()) {
                adapterRef.get().notifyDataSetChanged();
            }
        }
    }

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

    public Bitmap getDrawerIcon () {
        return drawerIcon;
    }

	/**
	 * @return the titleStringResId
	 */
	public Integer getTitleStringResId() {
		return titleStringResId;
	}

	/**
	 * @return the iconDrawableResId
	 */
	public Integer getIconDrawableResId() {
		return iconDrawableResId;
	}

	/**
	 * @return the childrenDrawerItems
	 */
	public List<DrawerItem> getChildDrawerItemList() {
		return childDrawerItemList;
	}

	/**
	 * @param childDrawerItem
	 * @return
	 */
	public boolean addChildDrawerItem(DrawerItem childDrawerItem) {
		return childDrawerItemList.add(childDrawerItem);
	}

	/**
	 * @return the badgeNumber
	 */
	public Integer getBadgeNumber() {
		return badgeNumber;
	}

	/**
	 * @param badgeNumber the badgeNumber to set
	 */
	public void setBadgeNumber(Integer badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible
     *            the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((badgeNumber == null) ? 0 : badgeNumber.hashCode());
        result = prime * result + ((childDrawerItemList == null) ? 0 : childDrawerItemList.hashCode());
        result = prime * result + ((iconDrawableResId == null) ? 0 : iconDrawableResId.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((titleStringResId == null) ? 0 : titleStringResId.hashCode());
        result = prime * result + (visible ? 1231 : 1237);
        return result;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DrawerItem other = (DrawerItem) obj;
        if (badgeNumber == null) {
            if (other.badgeNumber != null) {
                return false;
            }
        } else if (!badgeNumber.equals(other.badgeNumber)) {
            return false;
        }
        if (childDrawerItemList == null) {
            if (other.childDrawerItemList != null) {
                return false;
            }
        } else if (!childDrawerItemList.equals(other.childDrawerItemList)) {
            return false;
        }
        if (iconDrawableResId == null) {
            if (other.iconDrawableResId != null) {
                return false;
            }
        } else if (!iconDrawableResId.equals(other.iconDrawableResId)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (titleStringResId == null) {
            if (other.titleStringResId != null) {
                return false;
            }
        } else if (!titleStringResId.equals(other.titleStringResId)) {
            return false;
        }
        if (visible != other.visible) {
            return false;
        }
        return true;
    }
}
