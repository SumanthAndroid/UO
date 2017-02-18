package com.universalstudios.orlandoresort.model.network.domain.pointofinterest.category;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class GetPoiCategoriesResponse extends NetworkResponse {

	@SerializedName("Categories")
	List<String> categories;

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

}