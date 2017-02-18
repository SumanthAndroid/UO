package com.universalstudios.orlandoresort.controller.userinterface.tutorial;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * 
 * 
 * @author Steven Byle
 */
public class TutorialPage extends GsonObject {

	private final int imageResId;
	private final int titleResId;
	private final int messageResId;

	/**
	 * @param imageResId
	 * @param titleResId
	 * @param messageResId
	 */
	public TutorialPage(int imageResId, int titleResId, int messageResId) {
		super();
		this.imageResId = imageResId;
		this.titleResId = titleResId;
		this.messageResId = messageResId;
	}

	/**
	 * @return the imageResId
	 */
	public int getImageResId() {
		return imageResId;
	}

	/**
	 * @return the titleResId
	 */
	public int getTitleResId() {
		return titleResId;
	}

	/**
	 * @return the messageResId
	 */
	public int getMessageResId() {
		return messageResId;
	}

}
