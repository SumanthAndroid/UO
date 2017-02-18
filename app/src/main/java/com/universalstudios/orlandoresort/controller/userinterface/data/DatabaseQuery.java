/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.data;

import java.util.Arrays;

import android.net.Uri;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Class to hold information needed for a SQLite database query, commonly used
 * with a {@link android.content.ContentResolver} or a
 * {@link android.content.CursorLoader}.
 * 
 * @author Steven Byle
 */
public class DatabaseQuery extends GsonObject {

	private final String[] projection;
	private final String selection;
	private final String[] selectionArgs;
	private final String orderBy;
	private final String contentUriString;

	public DatabaseQuery(String contentUriString, String[] projection, String selection, String[] selectionArgs, String orderBy) {
		super();
		this.contentUriString = contentUriString;
		this.projection = projection;
		this.selection = selection;
		this.selectionArgs = selectionArgs;
		this.orderBy = orderBy;
	}

	/**
	 * @return the projection
	 */
	public String[] getProjection() {
		return projection;
	}

	/**
	 * @return the selection
	 */
	public String getSelection() {
		return selection;
	}

	/**
	 * @return the selectionArgs
	 */
	public String[] getSelectionArgs() {
		return selectionArgs;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @return the contentUri
	 */
	public String getContentUriString() {
		return contentUriString;
	}

	/**
	 * @return the contentUri
	 */
	public Uri getContentUri() {
		return Uri.parse(contentUriString);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentUriString == null) ? 0 : contentUriString.hashCode());
		result = prime * result + ((orderBy == null) ? 0 : orderBy.hashCode());
		result = prime * result + Arrays.hashCode(projection);
		result = prime * result + ((selection == null) ? 0 : selection.hashCode());
		result = prime * result + Arrays.hashCode(selectionArgs);
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
		DatabaseQuery other = (DatabaseQuery) obj;
		if (contentUriString == null) {
			if (other.contentUriString != null) {
				return false;
			}
		}
		else if (!contentUriString.equals(other.contentUriString)) {
			return false;
		}
		if (orderBy == null) {
			if (other.orderBy != null) {
				return false;
			}
		}
		else if (!orderBy.equals(other.orderBy)) {
			return false;
		}
		if (!Arrays.equals(projection, other.projection)) {
			return false;
		}
		if (selection == null) {
			if (other.selection != null) {
				return false;
			}
		}
		else if (!selection.equals(other.selection)) {
			return false;
		}
		if (!Arrays.equals(selectionArgs, other.selectionArgs)) {
			return false;
		}
		return true;
	}

}
