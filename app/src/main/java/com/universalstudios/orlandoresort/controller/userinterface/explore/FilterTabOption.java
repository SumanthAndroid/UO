/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore;

import android.os.Parcel;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.view.buttons.FilterTabButton;

/**
 * 
 * 
 * @author Steven Byle
 */
public class FilterTabOption extends GsonObject {

	private final String text;
	private final int imageDrawableResId;
	private final int id;
	private final boolean defaultCheckedState;
	private final String columnName;
	private final String[] columnValues;
	private boolean checked;
	private transient FilterTabButton filterTabButton;

	public FilterTabOption(String text, int imageDrawableResId, boolean defaultCheckedState, String columnName, String... columnValues) {
		super();
		this.text = text;
		this.imageDrawableResId = imageDrawableResId;
		this.defaultCheckedState = defaultCheckedState;
		this.columnName = columnName;
		this.columnValues = columnValues;
		this.checked = this.defaultCheckedState;
		id = Math.abs((text + imageDrawableResId).hashCode());
	}
	
	public FilterTabOption(Parcel in) {
	    super();
        this.text = in.readString();
        this.imageDrawableResId = in.readInt();
        this.id = in.readInt();
        this.defaultCheckedState = in.readInt() != 0 ? true : false;
        this.columnName = in.readString();
        this.columnValues = in.createStringArray();
        this.checked = in.readInt() != 0 ? true : false;
	}

	/**
	 * @return the columnValues
	 */
	public String[] getColumnValues() {
		return columnValues;
	}

	/**
	 * @return the filterTabButton
	 */
	public FilterTabButton getFilterTabButton() {
		return filterTabButton;
	}

	/**
	 * @param filterTabButton
	 *            the filterTabButton to set
	 */
	public void setFilterTabButton(FilterTabButton filterTabButton) {
		this.filterTabButton = filterTabButton;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the imageDrawableResId
	 */
	public int getImageDrawableResId() {
		return imageDrawableResId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the defaultCheckedState
	 */
	public boolean getDefaultCheckedState() {
		return defaultCheckedState;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
    
    /**
     * @return the checkedState
     */
    public boolean isChecked() {
        return checked;
    }
    
    /**
     * @param checkedState the checkedState to set
     */
    public void setChecked(boolean checkedState) {
        this.checked = checkedState;
    }

}
