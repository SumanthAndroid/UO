package com.universalstudios.orlandoresort.model.state.content;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tjudkins
 * @since 12/7/16
 */

public class TridionLabelSpecManager {

    private static Map<String, TridionLabelSpec> sMap;

    @NonNull
    private static Map<String, TridionLabelSpec> getMap() {
        if(sMap == null){
            sMap = new HashMap<>();
        }

        return sMap;
    }

    /**
     * Adds TridionLabelSpec sMap.
     * First parses the keys to get rid of tridion's overhead text
     * IE : "tcm:58-18779-16" gets parses to just 18779 using the
     * ID from the Commerce item
     *
     * @param map Raw HashMap returned from the server
     */
    public static void addToMap(Map<String, HashMap<String, String>> map) {
        Map<String, TridionLabelSpec> actualMap;
        actualMap = getMap();

        for (Map.Entry<String, HashMap<String, String>> entry : map.entrySet()) {
            String key = entry.getKey();
            String newKey = key.substring(7, 12);
            actualMap.put(newKey, new TridionLabelSpec(entry.getValue()));
        }
    }

    /**
     * Gets the spec for the given Id for a CommerceCardItem
     *
     * @param id the TCMID of the TridionLabelSpec
     * @return TridionLabelSpec for that card item
     */
    @NonNull
    public static TridionLabelSpec getSpecForId(String id) {
        TridionLabelSpec spec = null;
        if (!TextUtils.isEmpty(id)) {
            spec = getMap().get(id);
        }
        if (null == spec) {
            spec = new TridionLabelSpec(new HashMap<String, String>());
        }
        return spec;
    }

}
