package com.universalstudios.orlandoresort.model.network.response;

import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.universalstudios.orlandoresort.BuildConfig;

import java.io.Serializable;

/**
 * The base class for all Gson objects, which use annotations to denote their
 * JSON formats and field names.
 * 
 * @author Steven Byle
 */
public abstract class GsonObject implements Serializable {
	private static final String TAG = GsonObject.class.getSimpleName();

	@Override
	public String toString() {
		return toJsonString(true);
	}

	/**
	 * Convert this object to "raw" JSON, without pretty print formatting.
	 * 
	 * @return the object as JSON
	 */
	public String toJson() {
		return toJsonString(false);
	}

	/**
	 * Convert this object to JSON.
	 * 
	 * @param prettyPrint
	 *            if the JSON should be formatted in a human readable format
	 * 
	 * @return the object as JSON
	 */
	public String toJsonString(boolean prettyPrint) {
		GsonBuilder gsonBuilder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).serializeNulls();

		if (prettyPrint) {
			gsonBuilder.setPrettyPrinting();
		}

		return gsonBuilder.create().toJson(this);
	}

	/**
	 * Convenience method to parse a JSON {@link java.lang.String} into a an
	 * {@link java.lang.Object}.
	 * 
	 * @param jsonString
	 *            JSON string to be parsed
	 * @param classType
	 *            the class type that represents the JSON string
	 * @param <T>
	 *            the specific class type that represents the JSON string,
	 *            usually extended from {@link GsonObject}
	 * 
	 * @return the parsed {@link java.lang.Object}, or {@code null} if the
	 *         object could not be parsed
	 */
	public static <T> T fromJson(String jsonString, Class<T> classType) {
		if (jsonString != null) {
			try {
				return new Gson().fromJson(jsonString, classType);
			}
			catch (JsonSyntaxException e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "fromJson: invalid json", e);
				}

				// Log the exception to crittercism
				Crittercism.logHandledException(e);
			}
		}
		return null;
	}

}