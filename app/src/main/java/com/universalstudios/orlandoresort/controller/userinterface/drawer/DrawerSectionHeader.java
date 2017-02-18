/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.drawer;


/**
 * 
 * 
 * @author Steven Byle
 */
public class DrawerSectionHeader {

	private final String title;
	private final Integer titleStringResId;
	private final boolean isHidden;

	public DrawerSectionHeader() {
		super();
		title = null;
		titleStringResId = null;
		isHidden = true;
	}

	public DrawerSectionHeader(String title) {
		super();
		this.title = title;
		titleStringResId = null;
		isHidden = false;
	}

	public DrawerSectionHeader(Integer titleStringResId) {
		super();
		this.titleStringResId = titleStringResId;
		title = null;
		isHidden = false;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the titleStringResId
	 */
	public Integer getTitleStringResId() {
		return titleStringResId;
	}

	/**
	 * @return the isHidden
	 */
	public boolean isHidden() {
		return isHidden;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isHidden ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((titleStringResId == null) ? 0 : titleStringResId.hashCode());
		return result;
	}

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
		DrawerSectionHeader other = (DrawerSectionHeader) obj;
		if (isHidden != other.isHidden) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		}
		else if (!title.equals(other.title)) {
			return false;
		}
		if (titleStringResId == null) {
			if (other.titleStringResId != null) {
				return false;
			}
		}
		else if (!titleStringResId.equals(other.titleStringResId)) {
			return false;
		}
		return true;
	}

}
