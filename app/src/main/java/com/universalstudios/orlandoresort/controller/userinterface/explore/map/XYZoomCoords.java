/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

/**
 * 
 * 
 * @author Steven Byle
 */
public class XYZoomCoords {

	private int x, y, zoom;

	/**
	 * @param x
	 * @param y
	 * @param zoom
	 */
	public XYZoomCoords(int x, int y, int zoom) {
		super();
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

}
