package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 11/16/16.
 */
public class GuestPreferencesResult extends GsonObject {

    @SerializedName("termsOfServices")
    private List<GuestPreferenceValue<Boolean>> termsOfServices;

    @SerializedName("receiveUpdates")
    private List<GuestPreferenceValue<Boolean>> receiveUpdates;

    @SerializedName("interests")
    private Map<String, List<GuestPreferenceValue<String>>> interests;

    @SerializedName("seasons")
    private Map<String, List<GuestPreferenceValue<String>>> seasons;

    @SerializedName("emailFrequency")
    private List<GuestPreferenceValue<String>> emailFrequencies;

    @SerializedName("contactPermissions")
    private Map<String, List<GuestPreferenceValue<Boolean>>> contactPermissions;

    @SerializedName("selfSegment")
    private List<GuestPreferenceValue<String>> selfSegments;

    public Map<String, List<GuestPreferenceValue<String>>> getInterests() {
        return interests;
    }

    public List<GuestPreferenceValue<Boolean>> getTermsOfServices() {
        return termsOfServices;
    }

    public List<GuestPreferenceValue<Boolean>> getReceiveUpdates() {
        return receiveUpdates;
    }

    public Map<String, List<GuestPreferenceValue<String>>> getSeasons() {
        return seasons;
    }

    public List<GuestPreferenceValue<String>> getEmailFrequencies() {
        return emailFrequencies;
    }

    public Map<String, List<GuestPreferenceValue<Boolean>>> getContactPermissions() {
        return contactPermissions;
    }

    public List<GuestPreferenceValue<String>> getSelfSegments() {
        return selfSegments;
    }
}
