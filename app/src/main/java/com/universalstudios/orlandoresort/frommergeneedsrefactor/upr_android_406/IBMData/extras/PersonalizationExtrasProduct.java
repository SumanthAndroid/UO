package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItemComponent;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceGroup;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class PersonalizationExtrasProduct extends GsonObject {

    @StringDef({
            UI_CONTROL_QUANTITY,
            UI_CONTROL_DATE,
            UI_CONTROL_TIME,
            UI_CONTROL_TIER_BMG,
            UI_CONTROL_SKU,
            UI_CONTROL_CUSTOM_SKU_QUANTITY
    })
    public @interface UiControl {}
    public static final String UI_CONTROL_QUANTITY = "QUANTITY";
    public static final String UI_CONTROL_DATE = "DATE";
    public static final String UI_CONTROL_TIME = "TIME";
    public static final String UI_CONTROL_TIER_BMG = "TIER_BMG";
    public static final String UI_CONTROL_SKU = "SKU";
    /**
     * This step is a combination of the SKU and QUANTITY steps
     */
    public static final String UI_CONTROL_CUSTOM_SKU_QUANTITY = "SKU_QUANTITY";

    private static final String AGE_ALL_AGES = "ALL";
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy"; // Mon Dec 12 05:13:00 EST 2016

    @SerializedName("buyable")
    String buyable;

    @SerializedName("name")
    String name;

    @SerializedName("uniqueID")
    String uniqueId;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("attributes")
    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultAttributes;

    @SerializedName("skus")
    List<PersonalizationExtrasResultSku> personalizationExtrasResultSkus;

    @SerializedName("startDate")
    String startDate;

    @SerializedName("endDate")
    String endDate;

    @SerializedName("offer")
    PersonalizationExtrasResultOffer personalizationExtrasResultOffer;
    @NonNull
    public static PersonalizationExtrasProduct fromCommerceGroup(@NonNull CommerceGroup commerceGroup) {
        PersonalizationExtrasProduct extrasProduct = new PersonalizationExtrasProduct();
        extrasProduct.name = commerceGroup.getDescription();
        extrasProduct.partNumber = commerceGroup.getDescription();
        extrasProduct.startDate = commerceGroup.getStartDate();
        extrasProduct.endDate = commerceGroup.getEndDate();
        OUTER_LOOP: for (CommerceCardItem commerceCardItem : commerceGroup.getCardItems()) {
            if (commerceCardItem != null && commerceCardItem.getComponents() != null) {
                for (CommerceCardItemComponent component : commerceCardItem.getComponents()) {
                    if (component != null && !component.isItemTypeTicket()) {
                        // Component attributes must be first as they have the correct TCMIDS
                        extrasProduct.personalizationExtrasResultAttributes =
                                PersonalizationExtrasResultAttribute.fromCommerceAttributes(component.getAttributes());
                        extrasProduct.personalizationExtrasResultAttributes
                                .addAll(PersonalizationExtrasResultAttribute.fromCommerceAttributes(commerceCardItem.getAttributes()));
                        break OUTER_LOOP;
                    }
                }
            }
        }
        extrasProduct.personalizationExtrasResultSkus = PersonalizationExtrasResultSku.fromCommerceCardItems(commerceGroup.getCardItems());
        return extrasProduct;
    }

    public boolean getBuyable() {
        return Boolean.parseBoolean(buyable);
    }

    public String getName() {
        return name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public List<PersonalizationExtrasResultAttribute> getPersonalizationExtrasResultAttributes() {
        return personalizationExtrasResultAttributes;
    }

    public PersonalizationExtrasResultOffer getPersonalizationExtrasResultOffer() {
        return personalizationExtrasResultOffer;
    }

    public List<PersonalizationExtrasResultSku> getPersonalizationExtrasResultSkus() {
        return personalizationExtrasResultSkus;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    @Nullable
    public Date getStartDate() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        if (!TextUtils.isEmpty(startDate)) {
            try {
                return dateFormat.parse(startDate);
            } catch (ParseException e) {
                CrashAnalyticsUtils.logHandledException(e);
            }
        }
        return null;
    }

    @Nullable
    public Date getEndDate() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        if (!TextUtils.isEmpty(endDate)) {
            try {
                return dateFormat.parse(endDate);
            } catch (ParseException e) {
                CrashAnalyticsUtils.logHandledException(e);
            }
        }
        return null;
    }

    @Nullable
    public List<String> getUiControls() {
        List<String> result = new ArrayList<>();
        if (personalizationExtrasResultAttributes != null) {
            for (PersonalizationExtrasResultAttribute attribute : personalizationExtrasResultAttributes) {
                if (attribute != null && attribute.isUiWidget() && !TextUtils.isEmpty(attribute.getValues())) {
                    result.add(attribute.getValues());
                }
            }
        }

        if (result.size() > 0) {
            String[] split = result.get(0).split(" / ");
            List<String> steps = new ArrayList<>();
            // Replace SKU / QUANTITY steps with custom step
            for (int i = 0; i < split.length; i++) {
                if (UI_CONTROL_SKU.equals(split[i]) && i < split.length - 1
                        && UI_CONTROL_QUANTITY.equals(split[i+1])) {
                    steps.add(UI_CONTROL_CUSTOM_SKU_QUANTITY);
                    i++;
                } else {
                    steps.add(split[i]);
                }
            }
            // Remove the quantity step for combo products
            if (isComboProduct()) {
                Iterator<String> iterator = steps.iterator();
                while (iterator.hasNext()) {
                    String step = iterator.next();
                    if (UI_CONTROL_QUANTITY.equalsIgnoreCase(step)) {
                        iterator.remove();
                    }
                }
            }
            return steps;
        }
        return null;
    }

    /**
     * Returns the sku attribute that varies the child products if present
     */
    @Nullable
    public String getSkuDefiningAttribute() {
        String attributeValue = null;
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null
                                    && personalizationExtrasResultAttribute.isUiDefiningAttribute()
                                    && !TextUtils.isEmpty(personalizationExtrasResultAttribute.getValues())) {
                                attributeValue = personalizationExtrasResultAttribute.getValues();
                            }
                        }
                    }
                }
            }
        }

        return attributeValue;
    }

    /**
     * Returns the display names for the product sku options if present
     */
    @NonNull
    public List<String> getSkuSelectors() {
        List<String> skuOptions = new ArrayList<>();
        String attribute = getSkuDefiningAttribute();
        if (attribute != null && personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null
                                    && personalizationExtrasResultAttribute.isUiWizardDisplayName()
                                    && !TextUtils.isEmpty(personalizationExtrasResultAttribute.getValues())) {
                                if (!skuOptions.contains(personalizationExtrasResultAttribute.getValues())) {
                                    skuOptions.add(personalizationExtrasResultAttribute.getValues());
                                }
                            }
                        }
                    }
                }
            }
        }

        return skuOptions;
    }

    @Nullable
    public BigDecimal getMinPriceByAge(@NonNull String age) {
        BigDecimal result = null;
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null
                        && (AGE_ALL_AGES.equalsIgnoreCase(age) && personalizationExtrasResultSku.isAllAges()
                            || personalizationExtrasResultSku.isAge(age))) {
                    List<PersonalizationExtrasResultSkuPrice> personalizationExtrasResultSkuPrices =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuPrices();
                    if (personalizationExtrasResultSkuPrices != null) {
                        for (PersonalizationExtrasResultSkuPrice personalizationExtrasResultSkuPrice : personalizationExtrasResultSkuPrices) {
                            if (personalizationExtrasResultSkuPrice != null
                                    && personalizationExtrasResultSkuPrice.isDisplay()
                                    && !TextUtils.isEmpty(personalizationExtrasResultSkuPrice.getValue())) {
                                if (result == null) {
                                    try {
                                        result = new BigDecimal(personalizationExtrasResultSkuPrice.getValue());
                                    } catch (NumberFormatException e) {
                                        CrashAnalyticsUtils.logHandledException(e);
                                    }
                                } else {
                                    try {
                                        BigDecimal value = new BigDecimal(personalizationExtrasResultSkuPrice.getValue());
                                        result = result.min(value);
                                    } catch (NumberFormatException e) {
                                        CrashAnalyticsUtils.logHandledException(e);
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

    @Nullable
    public BigDecimal getMinPriceByAgeAndSku(@NonNull String age, @NonNull String attribute, @NonNull String sku) {
        BigDecimal result = null;
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null
                        && personalizationExtrasResultSku.hasAttributeValue(attribute, sku)
                        && (AGE_ALL_AGES.equalsIgnoreCase(age) && personalizationExtrasResultSku.isAllAges()
                        || personalizationExtrasResultSku.isAge(age))) {
                    List<PersonalizationExtrasResultSkuPrice> personalizationExtrasResultSkuPrices =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuPrices();
                    if (personalizationExtrasResultSkuPrices != null) {
                        for (PersonalizationExtrasResultSkuPrice personalizationExtrasResultSkuPrice : personalizationExtrasResultSkuPrices) {
                            if (personalizationExtrasResultSkuPrice != null
                                    && personalizationExtrasResultSkuPrice.isDisplay()
                                    && !TextUtils.isEmpty(personalizationExtrasResultSkuPrice.getValue())) {
                                if (result == null) {
                                    try {
                                        result = new BigDecimal(personalizationExtrasResultSkuPrice.getValue());
                                    } catch (NumberFormatException e) {
                                        CrashAnalyticsUtils.logHandledException(e);
                                    }
                                } else {
                                    try {
                                        BigDecimal value = new BigDecimal(personalizationExtrasResultSkuPrice.getValue());
                                        result = result.min(value);
                                    } catch (NumberFormatException e) {
                                        CrashAnalyticsUtils.logHandledException(e);
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

    @Nullable
    public Integer getMinQuantityByAge(@NonNull String age) {
        Integer quantity = null;
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && personalizationExtrasResultSku.isAge(age)) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null
                                    && personalizationExtrasResultAttribute.isMinQuantity()) {
                                try {
                                    quantity = Integer.valueOf(personalizationExtrasResultAttribute.getValues());
                                } catch (NumberFormatException e) {
                                    CrashAnalyticsUtils.logHandledException(e);
                                }
                            }
                        }
                    }
                }
            }
        }

        return quantity;
    }

    @Nullable
    public Integer getMaxQuantity() {
        Integer quantity = null;
        if (personalizationExtrasResultAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isMaxQuantity()) {
                    try {
                        quantity = Integer.valueOf(personalizationExtrasResultAttribute.getValues());
                    } catch (NumberFormatException e) {
                        CrashAnalyticsUtils.logHandledException(e);
                    }
                }
            }
        }

        return quantity;
    }

    @Nullable
    public String getAttributeValueForSkuDisplay(@NonNull String attribute, @NonNull String displayValue) {
        String value = null;
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && personalizationExtrasResultSku.hasDisplayValue(displayValue)) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null) {
                                if (attribute.equalsIgnoreCase(personalizationExtrasResultAttribute.getIdentifier())) {
                                    value = personalizationExtrasResultAttribute.getValues();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    @Nullable
    public String getSkuDisplayForAttributeValue(@NonNull String attribute, @NonNull String attributeValue) {
        String value = null;
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && personalizationExtrasResultSku.hasAttributeValue(attribute, attributeValue)) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                            personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null && personalizationExtrasResultAttribute.isUiWizardDisplayName()
                                    && !TextUtils.isEmpty(personalizationExtrasResultAttribute.getValues())) {
                                value = personalizationExtrasResultAttribute.getValues();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    /**
     * get unique age attribute definitions
     */
    @NonNull
    public HashMap<String, List<String>> getQuantitySelectors() {
        HashMap<String, List<String>> result = new HashMap<>();
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null) {
                    if (personalizationExtrasResultSku.isAllAges()) {
                        List<String> list = result.get(AGE_ALL_AGES);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(personalizationExtrasResultSku.getPartNumber());
                        result.put(AGE_ALL_AGES, list);
                    } else {
                        List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                                personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                        if (personalizationExtrasResultSkuAttributes != null) {
                            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                                if (personalizationExtrasResultAttribute != null
                                        && personalizationExtrasResultAttribute.isAge()
                                        && !TextUtils.isEmpty(personalizationExtrasResultAttribute.getValues())) {
                                    List<String> list = result.get(personalizationExtrasResultAttribute.getValues());
                                    if (list == null) {
                                        list = new ArrayList<>();
                                    }
                                    list.add(personalizationExtrasResultSku.getPartNumber());
                                    result.put(personalizationExtrasResultAttribute.getValues(), list);
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
     * get unique age attribute definitions
     */
    @NonNull
    public HashMap<String, List<String>> getComboQuantitySelectors() {
        HashMap<String, List<String>> result = new HashMap<>();
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null) {
                    if (personalizationExtrasResultSku.isAllAges()) {
                        List<String> list = result.get(AGE_ALL_AGES);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(personalizationExtrasResultSku.getComboPartNumber());
                        result.put(AGE_ALL_AGES, list);
                    } else {
                        List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes =
                                personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                        if (personalizationExtrasResultSkuAttributes != null) {
                            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                                if (personalizationExtrasResultAttribute != null
                                        && personalizationExtrasResultAttribute.isAge()
                                        && !TextUtils.isEmpty(personalizationExtrasResultAttribute.getValues())) {
                                    List<String> list = result.get(personalizationExtrasResultAttribute.getValues());
                                    if (list == null) {
                                        list = new ArrayList<>();
                                    }
                                    list.add(personalizationExtrasResultSku.getComboPartNumber());
                                    result.put(personalizationExtrasResultAttribute.getValues(), list);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @NonNull
    public List<String> getAllTcmIds() {
        HashSet<String> idSet = new HashSet<>();
        if (personalizationExtrasResultAttributes != null) {
            for (PersonalizationExtrasResultAttribute attribute : personalizationExtrasResultAttributes) {
                if (attribute != null && (attribute.isTcmId1() || attribute.isTcmId2())) {
                    idSet.add(attribute.getValues());
                }
            }
        }
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null
                        && personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes() != null) {
                    for (PersonalizationExtrasResultAttribute attribute : personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes() ) {
                        if (attribute != null && (attribute.isTcmId1() || attribute.isTcmId2())) {
                            idSet.add(attribute.getValues());
                        }
                    }
                }
            }
        }
        return new ArrayList<>(idSet);
    }

    @Nullable
    public String getTcmId1() {
        if (personalizationExtrasResultAttributes != null) {
            for (PersonalizationExtrasResultAttribute attribute : personalizationExtrasResultAttributes) {
                if (attribute != null && attribute.isTcmId1()) {
                    return attribute.getValues();
                }
            }
        }
        return null;
    }

    @Nullable
    public String getTcmId1(@NonNull String partNumber) {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && partNumber.equalsIgnoreCase(personalizationExtrasResultSku.getPartNumber())) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes = personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null && personalizationExtrasResultAttribute.isTcmId1()) {
                                return personalizationExtrasResultAttribute.getValues();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    public String getTcmId2(@NonNull String partNumber) {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && partNumber.equalsIgnoreCase(personalizationExtrasResultSku.getPartNumber())) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes = personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null && personalizationExtrasResultAttribute.isTcmId2()) {
                                return personalizationExtrasResultAttribute.getValues();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    public String getPartNumberForTier(@NonNull String tierName, @NonNull String age) {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null
                        && personalizationExtrasResultSku.isAge(age)
                        && personalizationExtrasResultSku.isTier(tierName)) {
                    return personalizationExtrasResultSku.getPartNumber();
                }
            }
        }

        return null;
    }

    public boolean isPartNumberAge(@NonNull String partNumber, @NonNull String age) {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null
                        && partNumber.equalsIgnoreCase(personalizationExtrasResultSku.getPartNumber())) {
                    return personalizationExtrasResultSku.isAge(age);
                }
            }
        }
        return false;
    }

    public boolean isPartNumberTier(@NonNull String partNumber, @NonNull String tier) {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null
                        && partNumber.equalsIgnoreCase(personalizationExtrasResultSku.getPartNumber())) {
                    return personalizationExtrasResultSku.isTier(tier);
                }
            }
        }
        return false;
    }

    public List<String> getTierList() {
        Set<String> list = new HashSet<>();
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null) {
                    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes
                            = personalizationExtrasResultSku.getPersonalizationExtrasResultSkuAttributes();
                    if (personalizationExtrasResultSkuAttributes != null) {
                        for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                            if (personalizationExtrasResultAttribute != null
                                    && personalizationExtrasResultAttribute.isTier()) {
                                list.add(personalizationExtrasResultAttribute.getValues());
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

    @Nullable
    public Integer getStartingQuantityByAge(String age) {
        Integer quantity = null;
        if (personalizationExtrasResultAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isStartingQuantity()) {
                    quantity = personalizationExtrasResultAttribute.getStartingQuantityByAge(age);
                }
            }
        }

        return quantity;
    }

    @Nullable
    public Integer getRollingAvailability() {
        Integer value = null;
        if (personalizationExtrasResultAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isRollingAvailability()
                        && !TextUtils.isEmpty(personalizationExtrasResultAttribute.getValues())) {
                    try {
                        value = Integer.parseInt(personalizationExtrasResultAttribute.getValues());
                    } catch (NumberFormatException e) {
                        CrashAnalyticsUtils.logHandledException(e);
                    }
                }
            }
        }
        return value;
    }

    public boolean isComboProduct() {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && personalizationExtrasResultSku.isComboSku()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAllAgesOnly() {
        if (personalizationExtrasResultSkus != null) {
            for (PersonalizationExtrasResultSku personalizationExtrasResultSku : personalizationExtrasResultSkus) {
                if (personalizationExtrasResultSku != null && !personalizationExtrasResultSku.isAllAges()) {
                    return false;
                }
            }
        }
        return true;
    }
}
