package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Class with type parameter to handle different types of values for a given key.
 *
 * Created by Tyler Ritchie on 11/15/16.
 */
public class GuestPreferenceValue<T> extends GsonObject {

    @SerializedName("id")
    String id;

    @SerializedName("value")
    T value;

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }
}
