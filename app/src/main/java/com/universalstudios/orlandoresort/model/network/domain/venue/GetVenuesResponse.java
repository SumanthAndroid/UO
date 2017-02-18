package com.universalstudios.orlandoresort.model.network.domain.venue;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class GetVenuesResponse extends NetworkResponse {

	@SerializedName("Results")
	List<Venue> venues;

	@SerializedName("TotalCount")
	Integer totalCount;

	@SerializedName("Pages")
	Integer pages;

	@SerializedName("PreviousPage")
	String previousPage;

	@SerializedName("NextPage")
	String nextPage;

	/**
	 * @return the venues
	 */
	public List<Venue> getVenues() {
		return venues;
	}

	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @return the pages
	 */
	public Integer getPages() {
		return pages;
	}

	/**
	 * @return the previousPage
	 */
	public String getPreviousPage() {
		return previousPage;
	}

	/**
	 * @return the nextPage
	 */
	public String getNextPage() {
		return nextPage;
	}

}