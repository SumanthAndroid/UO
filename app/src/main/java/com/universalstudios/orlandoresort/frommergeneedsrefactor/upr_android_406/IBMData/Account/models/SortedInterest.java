package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models;

import android.support.annotation.NonNull;


public class SortedInterest implements Comparable {
    private String key;
    private Integer value;

    public SortedInterest(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int comp = 0;
        if (o instanceof SortedInterest) {
            SortedInterest other = (SortedInterest) o;
            //sort by value first
            comp = other.value.compareTo(this.value);

            //if values are the same, compare the keys
            if (comp == 0) {
                comp = this.key.compareTo(other.key);
            }
        }
        return comp;
    }
}