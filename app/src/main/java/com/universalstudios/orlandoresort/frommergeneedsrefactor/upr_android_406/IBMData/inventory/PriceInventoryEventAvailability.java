package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory;

import java.util.Map;

import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */

public class PriceInventoryEventAvailability extends GsonObject {

    private Map<String, PriceInventoryEventAvailabilityDate> priceInventoryEventAvailabilities;

    public void setPriceInventoryEventAvailabilities(Map<String, PriceInventoryEventAvailabilityDate> priceInventoryEventAvailabilities) {
        this.priceInventoryEventAvailabilities = priceInventoryEventAvailabilities;
    }

    public Map<String, PriceInventoryEventAvailabilityDate> getPriceInventoryEventAvailabilities() {
        return priceInventoryEventAvailabilities;
    }

}
