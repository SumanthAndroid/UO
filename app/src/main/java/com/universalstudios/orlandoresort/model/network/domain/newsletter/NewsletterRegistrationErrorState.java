package com.universalstudios.orlandoresort.model.network.domain.newsletter;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class NewsletterRegistrationErrorState extends GsonObject {

	@SerializedName("registration.Email")
	List<String> emailErrors;

	@SerializedName("registration.BirthDateMonth")
	List<String> birthMonthErrors;

	@SerializedName("registration.BirthDateYear")
	List<String> birthYearErrors;

	@SerializedName("registration.ZipCode")
	List<String> zipCodeErrors;

	public List<String> getEmailErrors() {
		return emailErrors;
	}

	public List<String> getBirthMonthErrors() {
		return birthMonthErrors;
	}

	public List<String> getBirthYearErrors() {
		return birthYearErrors;
	}

	public List<String> getZipCodeErrors() {
		return zipCodeErrors;
	}
}