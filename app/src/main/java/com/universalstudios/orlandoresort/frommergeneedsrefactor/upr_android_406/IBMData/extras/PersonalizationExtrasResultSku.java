package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItem;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceCardItemComponent;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tyler Ritchie on 10/13/16.
 */
@Parcel
public class PersonalizationExtrasResultSku extends GsonObject {

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("longDescription")
    String longDescription;

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("buyable")
    String buyable;

    @SerializedName("name")
    String name;

    @SerializedName("uniqueID")
    String uniqueID;

    @SerializedName("price")
    List<PersonalizationExtrasResultSkuPrice> personalizationExtrasResultSkuPrices;

    @SerializedName("attributes")
    List<PersonalizationExtrasResultAttribute> personalizationExtrasResultSkuAttributes;

    /**
     * This value does not come from the service,
     * the price/inventory service does not return the actual partNumber that was sent for combo tickets.
     * This value is used for the price/inventory call and set as the top-level partNumber in the orderItem
     *
     * TODO: This should be moved
     */
    String comboPartNumber;

    @NonNull
    public static List<PersonalizationExtrasResultSku> fromCommerceCardItems(@Nullable List<CommerceCardItem> commerceCardItems) {
        List<PersonalizationExtrasResultSku> extraSkus = new ArrayList<>();
        if (commerceCardItems != null) {
            for (CommerceCardItem commerceCardItem : commerceCardItems) {
                if (commerceCardItem != null) {
                    if (commerceCardItem.getComponents() != null) {
                        for (CommerceCardItemComponent component : commerceCardItem.getComponents()) {
                            if (component != null && !component.isItemTypeTicket()) {
                                PersonalizationExtrasResultSku extraSku = new PersonalizationExtrasResultSku();
                                extraSku.personalizationExtrasResultSkuPrices =
                                        Collections.singletonList(PersonalizationExtrasResultSkuPrice.fromCommerceCardItem(commerceCardItem));
                                extraSku.uniqueID = component.getId();
                                extraSku.longDescription = component.getLongDescription();
                                extraSku.shortDescription = component.getShortDescription();
                                extraSku.buyable = Boolean.toString(commerceCardItem.isBuyable());
                                // Component attributes must be first as they have the correct TCMIDS
                                extraSku.personalizationExtrasResultSkuAttributes =
                                        PersonalizationExtrasResultAttribute.fromCommerceAttributes(component.getAttributes());
                                extraSku.personalizationExtrasResultSkuAttributes
                                        .addAll(PersonalizationExtrasResultAttribute.fromCommerceAttributes(commerceCardItem.getAttributes()));
                                // Part numbers come from the CommerceCartItemComponent
                                extraSku.partNumber = component.getPartNumber();
                                // The price/inventory service does not return the actual partNumber that was sent for combo tickets
                                extraSku.comboPartNumber = commerceCardItem.getPartNumber();
                                extraSkus.add(extraSku);
                            }
                        }
                    }
                }
            }
        }
        return extraSkus;
    }

    public boolean getBuyable() {
        return Boolean.parseBoolean(buyable);
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getName() {
        return name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public List<PersonalizationExtrasResultAttribute> getPersonalizationExtrasResultSkuAttributes() {
        return personalizationExtrasResultSkuAttributes;
    }

    public List<PersonalizationExtrasResultSkuPrice> getPersonalizationExtrasResultSkuPrices() {
        return personalizationExtrasResultSkuPrices;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * This value does not come from the service,
     * the price/inventory service does not return the actual partNumber that was sent for combo tickets
     */
    public String getComboPartNumber() {
        return comboPartNumber;
    }

    public boolean isAge(@NonNull String age) {
        if (personalizationExtrasResultSkuAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isAge()) {
                    return age.equalsIgnoreCase(personalizationExtrasResultAttribute.getValues());
                }
            }
        }
        return false;
    }

    /**
     * Returns true if this entry lacks an age attribute
     */
    public boolean isAllAges() {
        if (personalizationExtrasResultSkuAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isAge()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if entry has specified attribute with specified value
     */
    public boolean hasAttributeValue(@NonNull String attribute, @NonNull String value) {
        if (personalizationExtrasResultSkuAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && attribute.equalsIgnoreCase(personalizationExtrasResultAttribute.getIdentifier())
                        && value.equalsIgnoreCase(personalizationExtrasResultAttribute.getValues())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if entry has specified display attribute value
     */
    public boolean hasDisplayValue(@NonNull String displayValue) {
        if (personalizationExtrasResultSkuAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isUiWizardDisplayName()
                        && displayValue.equalsIgnoreCase(personalizationExtrasResultAttribute.getValues())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isTier(@NonNull String tier) {
        if (personalizationExtrasResultSkuAttributes != null) {
            for (PersonalizationExtrasResultAttribute personalizationExtrasResultAttribute : personalizationExtrasResultSkuAttributes) {
                if (personalizationExtrasResultAttribute != null
                        && personalizationExtrasResultAttribute.isTier()) {
                    return tier.equalsIgnoreCase(personalizationExtrasResultAttribute.getValues());
                }
            }
        }
        return false;
    }

    public boolean isComboSku() {
        return !TextUtils.isEmpty(comboPartNumber);
    }

}
