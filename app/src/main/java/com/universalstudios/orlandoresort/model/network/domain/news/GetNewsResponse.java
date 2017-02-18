package com.universalstudios.orlandoresort.model.network.domain.news;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * 
 * 
 * @author Steven Byle
 */
@Parcel
public class GetNewsResponse extends NetworkResponse {

	@SerializedName("Results")
	List<News> results;

	/**
	 * @return the results
	 */
	public List<News> getResults() {
		return results;
	}

}