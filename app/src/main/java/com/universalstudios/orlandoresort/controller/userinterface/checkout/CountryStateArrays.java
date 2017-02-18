package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries.Country;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.ProvState;

/**
 * Created by tjudkins on 9/23/16.
 */

public class CountryStateArrays extends BaseObservable {

    public final ArrayList<Country> countries = new ArrayList<>();
    public final ArrayList<ProvState> states = new ArrayList<>();

    public CountryStateArrays(List<Country> countries, List<ProvState> states) {
        Country us = null;
        for (Country c : countries) {
            if (Country.COUNTRY_CODE_US.equals(c.getCode())) {
                us = c;
            } else {
                this.countries.add(c);
            }
        }
        Collections.sort(this.countries);
        // Make US the first country
        if (null != us) {
            this.countries.add(0, us);
        }

        for (ProvState s : states) {
            this.states.add(s);
        }
        Collections.sort(this.states);
    }

    @Bindable
    public @NonNull ArrayList<Country> getCountries() {
        return countries;
    }

    @Bindable
    public @NonNull ArrayList<ProvState> getStates() {
        return states;
    }

    /**
     * Find the position of the country given with the list of countries
     * @param countryCode
     * @return position of country in list
     */
    public int findCountryPosition(String countryCode) {
        int position = -1;
        for (int i=0; i < countries.size(); i++) {
            // Should only need to check code, but playing it safe since the services
            // don't see to be consistent
            if (countries.get(i).getDisplayName().equals(countryCode) ||
                    countries.get(i).getCode().equals(countryCode)) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * Find the position of the state given with the list of countries
     * @param stateName
     * @return position of state in list
     */
    public int findStatePosition(String stateName) {
        int position = -1;
        for (int i=0; i < states.size(); i++) {
            if (states.get(i).getDisplayName().equals(stateName) ||
                states.get(i).getCode().equals(stateName)) {
                position = i;
                break;
            }
        }
        return position;
    }
}