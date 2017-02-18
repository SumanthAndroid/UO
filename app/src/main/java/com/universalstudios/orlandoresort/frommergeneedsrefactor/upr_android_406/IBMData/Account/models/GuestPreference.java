package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Parcel
public class GuestPreference extends GsonObject {
    private static final String SEASON_POSITIVE_VALUE = "Yes";
    private static final String SEASON_NEGATIVE_VALUE = "No";

    private final static Map<String, Integer> VALUE_LOOKUP_MAP;
    public final static Map<Integer, String> STRING_LOOKUP_MAP;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("Interested Slot 1", 0);
        map.put("Interested Slot 2", 1);
        map.put("Interested Slot 3", 2);
        map.put("Interested Slot 4", 3);
        map.put("Interested Slot 5", 4);
        VALUE_LOOKUP_MAP = Collections.unmodifiableMap(map);
    }

    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "Interested Slot 1");
        map.put(1, "Interested Slot 2");
        map.put(2, "Interested Slot 3");
        map.put(3, "Interested Slot 4");
        map.put(4, "Interested Slot 5");
        STRING_LOOKUP_MAP = Collections.unmodifiableMap(map);
    }


    @SerializedName("receiveUpdates")
    boolean receiveUpdates;

    @SerializedName("termsOfServices")
    boolean termsAndServices;

    @SerializedName("contactPermissions")
    UserContactPermissions contactPermissions;

    @SerializedName("emailFrequency")
    String emailFrequency;

    @SerializedName("communicationInterests")
    Map<String, String> communicationInterests;

    @SerializedName("typicalVacationSeasons")
    Map<String, String> vacationSeasonsResponse;


    public boolean getReceiveUpdates() {
        return receiveUpdates;
    }

    public void setReceiveUpdates(boolean receiveUpdates) {
        this.receiveUpdates = receiveUpdates;
    }

    public boolean getTermsAndServices() {
        return termsAndServices;
    }

    public void setTermsAndServices(boolean termsAndServices) {
        this.termsAndServices = termsAndServices;
    }

    public UserContactPermissions getContactPermissions() {
        return contactPermissions;
    }

    @EmailFrequencyType.StringValue
    public String getEmailFrequency() {
        return emailFrequency;
    }

    public void setContactPermissions(UserContactPermissions contactPermissions) {
        this.contactPermissions = contactPermissions;
    }

    public Map<String, String> getCommunicationInterests() {
        return communicationInterests;
    }
    public Map<String, Integer> getCommunicationInterestsValueMap() {
        Map<String, Integer> result = new HashMap<>();
        if (communicationInterests != null) {
            for (String key : communicationInterests.keySet()) {
                Integer value = VALUE_LOOKUP_MAP.get(communicationInterests.get(key));
                result.put(key, value);
            }
        }
        return result;
    }

    public Map<String, String>  getVacationSeasonsResponse() {
        return vacationSeasonsResponse;
    }

    public static boolean isSeasonSelected(String value){
        return SEASON_POSITIVE_VALUE.equalsIgnoreCase(value);
    }

    public static Integer getInterestIntValue(String value) {
        return VALUE_LOOKUP_MAP.get(value);
    }

    public static String getInterestStringValue(Integer value) {
        return STRING_LOOKUP_MAP.get(value);
    }
}