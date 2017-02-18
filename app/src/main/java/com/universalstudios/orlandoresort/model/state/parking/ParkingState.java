package com.universalstudios.orlandoresort.model.state.parking;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Note: Because this object is persisted, it should not be changed, but can be
 * added to.
 * 
 * @author Steven Byle
 */
public class ParkingState extends GsonObject {

	@Deprecated
	@SerializedName("garage")
	private String garage;

	@SerializedName("section")
	private String section;

	@SerializedName("level")
	private String level;

	@SerializedName("row")
	private String row;

	@SerializedName("notes")
	private String notes;

	/**
	 * @return the garage
	 */
	public String getGarage() {
		return garage;
	}

	/**
	 * @param garage
	 *            the garage to set
	 */
	public void setGarage(String garage) {
		this.garage = garage;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the row
	 */
	public String getRow() {
		return row;
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(String row) {
		this.row = row;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

}