package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceAttribute;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tritc412 on 10/13/16.
 */
@Parcel
public class PersonalizationExtrasResultAttribute extends GsonObject {

    private static final String ATTRIBUTE_IDENTIFIER_TCMID1 = "TCMID1";
    private static final String ATTRIBUTE_IDENTIFIER_TCMID2 = "TCMID2";
    private static final String ATTRIBUTE_IDENTIFIER_ITEM_TYPE = "ITEM_TYPE";
    private static final String ATTRIBUTE_IDENTIFIER_AGE = "AGE";
    private static final String ATTRIBUTE_VALUE_ADULT = "Adult";
    private static final String ATTRIBUTE_VALUE_CHILD = "Child";
    private static final String ATTRIBUTE_IDENTIFIER_UI_WIDGET = "UI_WIDGET";
    private static final String ATTRIBUTE_IDENTIFIER_UI_DEFINING_ATTRIBUTE = "UI_DEFINING_ATTRIBUTE";
    private static final String ATTRIBUTE_IDENTIFIER_UI_WIZARD_DISPLAY_NAME = "UI_WIZARD_DISPLAY_NAME";
    private static final String ATTRIBUTE_IDENTIFIER_MIN_QUANTITY = "MIN_QUANTITY";
    private static final String ATTRIBUTE_IDENTIFIER_MAX_QUANTITY= "MAX_QUANTITY";
    private static final String ATTRIBUTE_IDENTIFIER_SEAT_TIER = "SEAT_TIER";
    private static final String ATTRIBUTE_IDENTIFIER_STARTING_QUANTITY = "STARTING_QUANTITY";
    private static final String REGEX_STARTING_QUANTITY_VALUE = " \\| ";
    private static final int INDEX_STARTING_QUANTITY_VALUE_ADULT = 0;
    private static final int INDEX_STARTING_QUANTITY_VALUE_CHILD = 1;
    private static final String ATTRIBUTE_IDENTIFIER_ROLLING_AVAILABILITY = "ROLLING_AVAILABILITY";

    @SerializedName("name")
    String name;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("values")
    String values;

    @NonNull
    public static List<PersonalizationExtrasResultAttribute> fromCommerceAttributes(@Nullable List<CommerceAttribute> commerceAttributes) {
        List<PersonalizationExtrasResultAttribute> extrasAttributes = new ArrayList<>();
        if (commerceAttributes != null) {
            for (CommerceAttribute commerceAttribute : commerceAttributes) {
                if (commerceAttribute != null) {
                    PersonalizationExtrasResultAttribute extrasAttribute = new PersonalizationExtrasResultAttribute();
                    extrasAttribute.name = commerceAttribute.getName();
                    extrasAttribute.identifier = commerceAttribute.getName();
                    extrasAttribute.values = commerceAttribute.getValue();
                    extrasAttributes.add(extrasAttribute);
                }
            }
        }
        return extrasAttributes;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getValues() {
        return values;
    }

    public boolean isTcmId1() {
        return ATTRIBUTE_IDENTIFIER_TCMID1.equalsIgnoreCase(identifier);
    }

    public boolean isTcmId2() {
        return ATTRIBUTE_IDENTIFIER_TCMID2.equalsIgnoreCase(identifier);
    }

    public boolean isAge() {
        return ATTRIBUTE_IDENTIFIER_AGE.equalsIgnoreCase(identifier);
    }

    public boolean isItemType() {
        return ATTRIBUTE_IDENTIFIER_ITEM_TYPE.equalsIgnoreCase(identifier);
    }

    public boolean isUiWidget() {
        return ATTRIBUTE_IDENTIFIER_UI_WIDGET.equalsIgnoreCase(identifier);
    }

    public boolean isUiDefiningAttribute() {
        return ATTRIBUTE_IDENTIFIER_UI_DEFINING_ATTRIBUTE.equalsIgnoreCase(identifier);
    }

    public boolean isUiWizardDisplayName() {
        return ATTRIBUTE_IDENTIFIER_UI_WIZARD_DISPLAY_NAME.equalsIgnoreCase(identifier);
    }

    public boolean isMinQuantity() {
        return ATTRIBUTE_IDENTIFIER_MIN_QUANTITY.equalsIgnoreCase(identifier);
    }

    public boolean isMaxQuantity() {
        return ATTRIBUTE_IDENTIFIER_MAX_QUANTITY.equalsIgnoreCase(identifier);
    }

    public boolean isTier() {
        return ATTRIBUTE_IDENTIFIER_SEAT_TIER.equalsIgnoreCase(identifier);
    }

    public boolean isStartingQuantity() {
        return ATTRIBUTE_IDENTIFIER_STARTING_QUANTITY.equalsIgnoreCase(identifier);
    }

    /**
     * Convenience method for returning starting quantity based on age.
     * If only one starting quantity is present it is returned, regardless of the age. Make sure to call
     * {@link PersonalizationExtrasResultAttribute#isStartingQuantity()} first.
     *
     * @return starting quantity for age or null
     */
    @Nullable
    public Integer getStartingQuantityByAge(String age) {
        Integer quantity = null;
        if (!TextUtils.isEmpty(values)) {
            String[] quantities = values.split(REGEX_STARTING_QUANTITY_VALUE);
            if (quantities.length == 1) {
                quantity = NumberUtils.toInteger(quantities[INDEX_STARTING_QUANTITY_VALUE_ADULT]);
            } else if (quantities.length == 2) {
                if (ATTRIBUTE_VALUE_CHILD.equalsIgnoreCase(age)) {
                    quantity = NumberUtils.toInteger(quantities[INDEX_STARTING_QUANTITY_VALUE_CHILD]);
                } else {
                    quantity = NumberUtils.toInteger(quantities[INDEX_STARTING_QUANTITY_VALUE_ADULT]);
                }
            }
        }

        return quantity;
    }

    /**
     * Convenience method for checking if the attribute value is adult. Make sure to call
     * isAge() first to check that the attribute is an age attribute
     * @return true if adult
     */
    public boolean isAdult() {
        return ATTRIBUTE_VALUE_ADULT.equalsIgnoreCase(values);
    }

    /**
     * Convenience method for checking if the attribute value is child. Make sure to call
     * isAge() first to check that the attribute is an age attribute
     * @return true if adult
     */
    public boolean isChild() {
        return ATTRIBUTE_VALUE_CHILD.equalsIgnoreCase(values);
    }

    public boolean isRollingAvailability() {
        return ATTRIBUTE_IDENTIFIER_ROLLING_AVAILABILITY.equalsIgnoreCase(identifier);
    }
}
