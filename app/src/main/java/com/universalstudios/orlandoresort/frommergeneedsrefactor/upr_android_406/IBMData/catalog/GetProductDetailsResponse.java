package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.crittercism.app.Crittercism;
import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class GetProductDetailsResponse extends NetworkResponse {
    private static final String IDENTIFIER_MAX_QUANTITY = "MAX_QUANTITY";
    private static final String IDENTIFIER_UI_WIDGET = "UI_WIDGET";
    private static final String IDENTIFIER_AGE = "AGE";
    private static final String IDENTIFIER_SEAT_TIER = "SEAT_TIER";
    private static final String IDENTIFIER_TCMID2 = "TCMID2";
    private static final String IDENTIFIER_TCMID1 = "TCMID1";
    private static final String AGE_ALL_AGES = "ALL";

    public static final String STEP_QUANTITY = "QUANTITY";
    public static final String STEP_DATE = "DATE";
    public static final String STEP_TIME = "TIME";
    public static final String STEP_TIER_BMG = "TIER_BMG";
    public static final String STEP_SKU = "SKU";
    /**
     * This step is a combination of the SKU and QUANTITY steps
     */
    public static final String STEP_CUSTOM_SKU_QUANTITY = "SKU_QUANTITY";

    @SerializedName("recordSetTotal")
    String recordSetTotal;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("resourceName")
    String resourceName;

    @SerializedName("recordSetComplete")
    String recordSetComplete;

    @SerializedName("recordSetStartNumber")
    String recordSetStartNumber;

    @SerializedName("CatalogEntryView")
    List<ProductDetailsCatalogEntry> productDetailsCatalogEntry;

    @SerializedName("recordSetCount")
    String recordSetCount;

    @SerializedName("MetaData")
    List<ProductDetailsMetaData> productDetailsMetaData;

    public List<ProductDetailsCatalogEntry> getProductDetailsCatalogEntry() {
        return productDetailsCatalogEntry;
    }

    public List<ProductDetailsMetaData> getProductDetailsMetaData() {
        return productDetailsMetaData;
    }

    public boolean getRecordSetComplete() {
        return Boolean.parseBoolean(recordSetComplete);
    }

    public Integer getRecordSetCount() {
        return NumberUtils.toInteger(recordSetCount);
    }

    public Integer getRecordSetStartNumber() {
        return NumberUtils.toInteger(recordSetStartNumber);
    }

    public Integer getRecordSetTotal() {
        return NumberUtils.toInteger(recordSetTotal);
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    /**
     * @return
     */
    public String getTitle() {
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    String name = detailsCatalogEntry.getName();
                    if (!TextUtils.isEmpty(name)) {
                        return name;
                    }
                }
            }
        }
        return "<Title>";
    }

    /**
     * @return
     */
    public String getShortDescription() {
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    String description = detailsCatalogEntry.getShortDescription();
                    if (!TextUtils.isEmpty(description)) {
                        return description;
                    }
                }
            }
        }
        return "<Short description>";
    }

    public String getLongDescription() {
        if (getProductDetailsCatalogEntry() != null && getProductDetailsCatalogEntry().size() > 0) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : getProductDetailsCatalogEntry()) {
                if (detailsCatalogEntry != null) {
                    String longDescription = detailsCatalogEntry.getLongDescription();
                    if (!TextUtils.isEmpty(longDescription)) {
                        return longDescription;
                    }
                }
            }
        }
        return "Long description";
    }

    /**
     * @return
     */
    public List<String> getUiControls() {
        List<String> result = new ArrayList<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null) {
                                if (IDENTIFIER_UI_WIDGET.equals(productDetailsAttribute.getIdentifier())) {
                                    if (productDetailsAttribute.getProductDetailsAttributesValues() != null) {
                                        List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                        for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                            if (productDetailsAttributesValue != null && !TextUtils.isEmpty(productDetailsAttributesValue.getValues())) {
                                                result.add(productDetailsAttributesValue.getValues());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (result.size() > 0) {
            String[] split = result.get(0).split(" / ");
            List<String> steps = new ArrayList<>();
            // Replace SKU / QUANTITY steps with custom step
            for (int i = 0; i < split.length; i++) {
                 if (STEP_SKU.equals(split[i]) && i < split.length - 1
                         && STEP_QUANTITY.equals(split[i+1])) {
                     steps.add(STEP_CUSTOM_SKU_QUANTITY);
                     i++;
                 } else {
                     steps.add(split[i]);
                 }
            }
            return steps;
        }

        return null;
    }

    /**
     * @return
     */
    public ArrayList<String> getSKUUniqueIDs() {
        ArrayList<String> result = new ArrayList<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsSku> skus = detailsCatalogEntry.getSkus();
                    if (skus != null) {
                        for (ProductDetailsSku sku : skus) {
                            result.add(sku.getSkuUniqueId());
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * get maximum max quantity attribute definitions
     *
     * @return
     */
    public Integer getMaxQuantity() {
        Integer result = null;
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null) {
                                if (IDENTIFIER_MAX_QUANTITY.equals(productDetailsAttribute.getIdentifier())) {
                                    if (productDetailsAttribute.getProductDetailsAttributesValues() != null) {
                                        List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                        for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                            if (productDetailsAttributesValue != null && !TextUtils.isEmpty(productDetailsAttributesValue.getValues())) {
                                                try {
                                                    return Integer.valueOf(productDetailsAttributesValue.getValues());
                                                } catch (NumberFormatException e) {
                                                    Crittercism.logHandledException(e);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get unique park attribute values
     */
    @Nonnull
    public List<String> getSkuParkSelectors() {
        List<String> parks = new ArrayList<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null && productDetailsAttribute.isPark()) {
                                List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                if (productDetailsAttributesValues != null) {
                                    for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                        if (productDetailsAttributesValue != null) {
                                            parks.add(productDetailsAttributesValue.getValues());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return parks;
    }

    /**
     * Get unique day attribute values
     */
    @Nonnull
    public List<String> getSkuDaySelectors() {
        List<String> days = new ArrayList<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null && productDetailsAttribute.isDays()) {
                                List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                if (productDetailsAttributesValues != null) {
                                    for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                        if (productDetailsAttributesValue != null) {
                                            days.add(productDetailsAttributesValue.getValues());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return days;
    }

    /**
     * Get unique ucw movie included attribute values
     */
    @Nonnull
    public List<String> getSkuUcwMovieIncludedSelectors() {
        List<String> selectors = new ArrayList<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null && productDetailsAttribute.isUcwMovieIncluded()) {
                                List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                if (productDetailsAttributesValues != null) {
                                    for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                        if (productDetailsAttributesValue != null) {
                                            selectors.add(detailsCatalogEntry.getShortDescription());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return selectors;
    }

    /**
     * Get ucw movie included attribute value map,
     * keys are the displayed text, while values are the attribute value
     */
    @Nonnull
    public Map<String, String> getSkuUcwMovieIncludedSelectorsMap() {
        Map<String, String> selectors = new HashMap<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null && productDetailsAttribute.isUcwMovieIncluded()) {
                                List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                if (productDetailsAttributesValues != null) {
                                    for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                        if (productDetailsAttributesValue != null) {
                                            selectors.put(detailsCatalogEntry.getShortDescription(),
                                                    productDetailsAttributesValue.getValues());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return selectors;
    }

    /**
     * get unique age attribute definitions
     *
     * @return
     */
    public HashMap<String, List<String>> getQuantitySelectors() {
        HashMap<String, List<String>> result = new HashMap<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && !detailsCatalogEntry.isAllAges()) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null) {
                                if (IDENTIFIER_AGE.equals(productDetailsAttribute.getIdentifier())) {
                                    if (productDetailsAttribute.getProductDetailsAttributesValues() != null) {
                                        List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                        for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                            if (productDetailsAttributesValue != null && !TextUtils.isEmpty(productDetailsAttributesValue.getValues())) {
                                                List<String> list = result.get(productDetailsAttributesValue.getValues());
                                                if (list == null) {
                                                    list = new ArrayList<>();
                                                }
                                                list.add(detailsCatalogEntry.getPartNumber());
                                                result.put(productDetailsAttributesValue.getValues(), list);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (detailsCatalogEntry != null) {
                        List<String> list = result.get(AGE_ALL_AGES);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(detailsCatalogEntry.getPartNumber());
                        result.put(AGE_ALL_AGES, list);
                    }
                }
            }
        }
        return result;
    }

    /**
     * get unique tier attribute definitions
     *
     * @return
     */
    public HashMap<String, List<String>> getTierPartNumberMap() {
        HashMap<String, List<String>> result = new HashMap<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null) {
                                if (IDENTIFIER_SEAT_TIER.equals(productDetailsAttribute.getIdentifier())) {
                                    if (productDetailsAttribute.getProductDetailsAttributesValues() != null) {
                                        List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                        for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                            if (productDetailsAttributesValue != null && !TextUtils.isEmpty(productDetailsAttributesValue.getValues())) {
                                                List<String> list = result.get(productDetailsAttributesValue.getValues());
                                                if (list == null) {
                                                    list = new ArrayList<>();
                                                }
                                                list.add(detailsCatalogEntry.getPartNumber());
                                                result.put(productDetailsAttributesValue.getValues(), list);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * get the minimum price by age
     *
     * @param age
     * @return
     */
    public BigDecimal getMinPriceByAge(String age) {
        Double result = null;
        if (AGE_ALL_AGES.equalsIgnoreCase(age) && productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && detailsCatalogEntry.isAllAges()) {
                    if (detailsCatalogEntry.getPriceList() != null) {
                        List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                        if (prices != null) {
                            for (CatalogEntryPrice price : prices) {
                                if (price != null) {
                                    if (result == null) {
                                        result = price.getDoubleValue();
                                    } else {
                                        result = Math.min(result, price.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && !TextUtils.isEmpty(age) && detailsCatalogEntry.isAge(age)) {
                    if (detailsCatalogEntry.getPriceList() != null) {
                        List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                        if (prices != null) {
                            for (CatalogEntryPrice price : prices) {
                                if (price != null) {
                                    if (result == null) {
                                        result = price.getDoubleValue();
                                    } else {
                                        result = Math.min(result, price.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result != null) {
            return BigDecimal.valueOf(result);
        } else {
            return null;
        }
    }

    /**
     * Get the minimum price by age and day
     */
    @Nullable
    public BigDecimal getMinPriceByAgeAndDay(String age, String day) {
        Double result = null;
        if (AGE_ALL_AGES.equalsIgnoreCase(age) && productDetailsCatalogEntry != null
                && !TextUtils.isEmpty(day)) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && detailsCatalogEntry.isAllAges()
                        && detailsCatalogEntry.isDay(day)) {
                    List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                    if (prices != null) {
                        for (CatalogEntryPrice price : prices) {
                            if (price != null) {
                                if (result == null) {
                                    result = price.getDoubleValue();
                                } else {
                                    result = Math.min(result, price.getDoubleValue());
                                }
                            }
                        }
                    }
                }
            }
        } else if (productDetailsCatalogEntry != null && !TextUtils.isEmpty(day)) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && !TextUtils.isEmpty(age) && detailsCatalogEntry.isAge(age)
                        && detailsCatalogEntry.isDay(day)) {
                    List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                    if (prices != null) {
                        for (CatalogEntryPrice price : prices) {
                            if (price != null) {
                                if (result == null) {
                                    result = price.getDoubleValue();
                                } else {
                                    result = Math.min(result, price.getDoubleValue());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result != null) {
            return BigDecimal.valueOf(result);
        } else {
            return null;
        }
    }

    /**
     * Get the minimum price by age and ucw movie included
     */
    @Nullable
    public BigDecimal getMinPriceByAgeUcwMovieIncluded(String age, String ucwMovieIncluded) {
        Double result = null;
        if (AGE_ALL_AGES.equalsIgnoreCase(age) && productDetailsCatalogEntry != null
                && !TextUtils.isEmpty(ucwMovieIncluded)) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && detailsCatalogEntry.isAllAges()
                        && detailsCatalogEntry.isUcwMovieIncluded(ucwMovieIncluded)) {
                    if (detailsCatalogEntry.getPriceList() != null) {
                        List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                        if (prices != null) {
                            for (CatalogEntryPrice price : prices) {
                                if (price != null) {
                                    if (result == null) {
                                        result = price.getDoubleValue();
                                    } else {
                                        result = Math.min(result, price.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (productDetailsCatalogEntry != null && !TextUtils.isEmpty(ucwMovieIncluded)
                && !TextUtils.isEmpty(age)) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && detailsCatalogEntry.isAge(age)
                        && detailsCatalogEntry.isUcwMovieIncluded(ucwMovieIncluded)) {
                    if (detailsCatalogEntry.getPriceList() != null) {
                        List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                        if (prices != null) {
                            for (CatalogEntryPrice price : prices) {
                                if (price != null) {
                                    if (result == null) {
                                        result = price.getDoubleValue();
                                    } else {
                                        result = Math.min(result, price.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result != null) {
            return BigDecimal.valueOf(result);
        } else {
            return null;
        }
    }

    /**
     * Get the minimum price by age and ucw movie included
     */
    @Nullable
    public BigDecimal getMinPriceByAgeAndPark(String age, String park) {
        Double result = null;
        if (AGE_ALL_AGES.equalsIgnoreCase(age) && productDetailsCatalogEntry != null
                && !TextUtils.isEmpty(park)) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && detailsCatalogEntry.isAllAges()
                        && detailsCatalogEntry.isPark(park)) {
                    if (detailsCatalogEntry.getPriceList() != null) {
                        List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                        if (prices != null) {
                            for (CatalogEntryPrice price : prices) {
                                if (price != null) {
                                    if (result == null) {
                                        result = price.getDoubleValue();
                                    } else {
                                        result = Math.min(result, price.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (productDetailsCatalogEntry != null && !TextUtils.isEmpty(age)
                && !TextUtils.isEmpty(park)) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null && detailsCatalogEntry.isAge(age)
                        && detailsCatalogEntry.isPark(park)) {
                    if (detailsCatalogEntry.getPriceList() != null) {
                        List<CatalogEntryPrice> prices = detailsCatalogEntry.getPriceList();
                        if (prices != null) {
                            for (CatalogEntryPrice price : prices) {
                                if (price != null) {
                                    if (result == null) {
                                        result = price.getDoubleValue();
                                    } else {
                                        result = Math.min(result, price.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result != null) {
            return BigDecimal.valueOf(result);
        } else {
            return null;
        }
    }

    public boolean isPartNumberAge(String partNumber, String age) {
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (partNumber.equalsIgnoreCase(detailsCatalogEntry.getPartNumber())) {
                    return detailsCatalogEntry.isAge(age);
                }
            }
        }
        return false;
    }

    public boolean isPartNumberTier(String partNumber, String tier) {
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (partNumber.equalsIgnoreCase(detailsCatalogEntry.getPartNumber())) {
                    return detailsCatalogEntry.isTier(tier);
                }
            }
        }
        return false;
    }

    public List<String> getTierList() {
        Set<String> list = new HashSet<>();
        if (productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry detailsCatalogEntry : productDetailsCatalogEntry) {
                if (detailsCatalogEntry != null) {
                    List<ProductDetailsAttributes> productDetailsAttributes = detailsCatalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null) {
                                if (IDENTIFIER_SEAT_TIER.equals(productDetailsAttribute.getIdentifier())) {
                                    if (productDetailsAttribute.getProductDetailsAttributesValues() != null) {
                                        List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                        for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                            list.add(productDetailsAttributesValue.getValues());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        List<String> result = new ArrayList<>();
        result.addAll(list);
        return result;
    }

    public String getPartNumberForTier(String tierName, String age) {
        if (getProductDetailsCatalogEntry() != null) {
            List<ProductDetailsCatalogEntry> catalogEntries = getProductDetailsCatalogEntry();
            for (ProductDetailsCatalogEntry catalogEntry : catalogEntries) {
                if (catalogEntry.isAge(age) && catalogEntry.isTier(tierName)) {
                    return catalogEntry.getPartNumber();
                }
            }
        }
        return null;
    }

    public String getTcmId2(String partNumber) {
        if (partNumber == null) {
            return null;
        }

        if (getProductDetailsCatalogEntry() != null) {
            List<ProductDetailsCatalogEntry> catalogEntries = getProductDetailsCatalogEntry();
            for (ProductDetailsCatalogEntry catalogEntry : catalogEntries) {
                if (partNumber.equals(catalogEntry.getPartNumber())) {
                    List<ProductDetailsAttributes> attributes = catalogEntry.getProductDetailsAttributes();
                    if (attributes != null) {
                        for (ProductDetailsAttributes attribute : attributes) {
                            if (attribute != null && IDENTIFIER_TCMID2.equals(attribute.getIdentifier())) {
                                List<ProductDetailsAttributesValues> attributesValues = attribute.getProductDetailsAttributesValues();
                                if (attributesValues != null) {
                                    for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                        return attributesValue.getValues();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getTcmId1() {
        List<ProductDetailsCatalogEntry> entries = getProductDetailsCatalogEntry();
        if (entries != null) {
            for (ProductDetailsCatalogEntry entry : entries) {
                if (entry != null) {
                    List<ProductDetailsAttributes> attributes = entry.getProductDetailsAttributes();
                    if (attributes != null) {
                        for (ProductDetailsAttributes attribute : attributes) {
                            if (attribute != null && IDENTIFIER_TCMID1.equals(attribute.getIdentifier())) {
                                List<ProductDetailsAttributesValues> attributesValues = attribute.getProductDetailsAttributesValues();
                                if (attributesValues != null) {
                                    for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                        return attributesValue.getValues();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get min quantity for specified age, 0 otherwise
     */
    @Nullable
    public Integer getMinQuantityByAge(String age) {
        Integer quantity = null;
        if (age != null && productDetailsCatalogEntry != null) {
            for (ProductDetailsCatalogEntry catalogEntry : productDetailsCatalogEntry) {
                if (catalogEntry != null && catalogEntry.isAge(age)) {
                    List<ProductDetailsAttributes> productDetailsAttributes = catalogEntry.getProductDetailsAttributes();
                    if (productDetailsAttributes != null) {
                        for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                            if (productDetailsAttribute != null && productDetailsAttribute.isMinQuantity()) {
                                List<ProductDetailsAttributesValues> productDetailsAttributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                                if (productDetailsAttributesValues != null) {
                                    for (ProductDetailsAttributesValues productDetailsAttributesValue : productDetailsAttributesValues) {
                                        if (productDetailsAttributesValue != null) {
                                            try {
                                                quantity = Integer.valueOf(productDetailsAttributesValue.getValues());
                                            } catch (NumberFormatException e) {
                                                Crittercism.logHandledException(e);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return quantity;
    }
}