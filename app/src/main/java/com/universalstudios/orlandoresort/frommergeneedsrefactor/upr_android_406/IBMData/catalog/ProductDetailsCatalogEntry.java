package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util.NumberUtils;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model class for CatalogGroupView.
 * <p>
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsCatalogEntry extends GsonObject {

    private static final String CHILD = "child";
    private static final String ADULT = "adult";

    private static final String IDENTIFIER_AGE = "AGE";
    private static final String IDENTIFIER_SEAT_TIER = "SEAT_TIER";

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("buyable")
    String buyable;

    @SerializedName("longDescription")
    String longDescription;

    @SerializedName("parentCategoryID")
    String parentCategoryId;

    @SerializedName("metaKeyword")
    String metaKeyword;

    @SerializedName("resourceId")
    String resourceId;

    @SerializedName("keyword")
    String keyword;

    @SerializedName("productType")
    String productType;

    @SerializedName("name")
    String name;

    @SerializedName("uniqueID")
    String uniqueId;

    @SerializedName("manufacturer")
    String manufacturer;

    @SerializedName("SKUs")
    List<ProductDetailsSku> skus;

    @SerializedName("numberOfSKUs")
    String numberOfSkus;

    @SerializedName("Attributes")
    List<ProductDetailsAttributes> productDetailsAttributes;

    @SerializedName("subscriptionType")
    String subscriptionType;

    @SerializedName("metaDescription")
    String metaDescription;

    @SerializedName("title")
    String title;

    @SerializedName("hasSingleSKU")
    String hasSingleSKU;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("storeID")
    String storeId;

    @SerializedName("fullImageAltDescription")
    String fullImageAltDescription;

    @SerializedName("Price")
    List<CatalogEntryPrice> priceList;

    public boolean getBuyable() {
        return Boolean.parseBoolean(buyable);
    }

    public String getFullImageAltDescription() {
        return fullImageAltDescription;
    }

    public boolean getHasSingleSKU() {
        return Boolean.parseBoolean(hasSingleSKU);
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfSkus() {
        return NumberUtils.toInteger(numberOfSkus);
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public List<ProductDetailsAttributes> getProductDetailsAttributes() {
        return productDetailsAttributes;
    }

    public String getProductType() {
        return productType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public List<ProductDetailsSku> getSkus() {
        return skus;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public String getTitle() {
        return title;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public List<CatalogEntryPrice> getPriceList() {
        return priceList;
    }

    public boolean isChild() {
        return isAge(CHILD);
    }

    public boolean isAdult() {
        return isAge(ADULT);
    }

    public boolean isAge(@NonNull String age) {
        List<ProductDetailsAttributes> productDetailsAttributes = getProductDetailsAttributes();
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null) {
                    if (IDENTIFIER_AGE.equals(productDetailsAttribute.getIdentifier())) {
                        List<ProductDetailsAttributesValues> attributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                        if (attributesValues != null) {
                            for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                if (age.equalsIgnoreCase(attributesValue.getValues())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isTier(String tierName) {
        List<ProductDetailsAttributes> productDetailsAttributes = getProductDetailsAttributes();
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null) {
                    if (IDENTIFIER_SEAT_TIER.equals(productDetailsAttribute.getIdentifier())) {
                        List<ProductDetailsAttributesValues> attributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                        if (attributesValues != null) {
                            for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                if (tierName.equalsIgnoreCase(attributesValue.getValues())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isUcwMovieIncluded(@NonNull String ucwMovieIncluded) {
        List<ProductDetailsAttributes> productDetailsAttributes = getProductDetailsAttributes();
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null) {
                    if (productDetailsAttribute.isUcwMovieIncluded()) {
                        List<ProductDetailsAttributesValues> attributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                        if (attributesValues != null) {
                            for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                if (attributesValue != null && ucwMovieIncluded.equalsIgnoreCase(attributesValue.getValues())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isDay(@NonNull String day) {
        List<ProductDetailsAttributes> productDetailsAttributes = getProductDetailsAttributes();
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null) {
                    if (productDetailsAttribute.isDays()) {
                        List<ProductDetailsAttributesValues> attributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                        if (attributesValues != null) {
                            for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                if (attributesValue != null && day.equalsIgnoreCase(attributesValue.getValues())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPark(@NonNull String park) {
        List<ProductDetailsAttributes> productDetailsAttributes = getProductDetailsAttributes();
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null) {
                    if (productDetailsAttribute.isPark()) {
                        List<ProductDetailsAttributesValues> attributesValues = productDetailsAttribute.getProductDetailsAttributesValues();
                        if (attributesValues != null) {
                            for (ProductDetailsAttributesValues attributesValue : attributesValues) {
                                if (attributesValue != null && park.equalsIgnoreCase(attributesValue.getValues())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isDiningEventType() {
        List<ProductDetailsAttributes> productDetailsAttributes = getProductDetailsAttributes();
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null && productDetailsAttribute.isDiningEventType()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if this entry lacks an age attribute
     */
    public boolean isAllAges() {
        if (productDetailsAttributes != null) {
            for (ProductDetailsAttributes productDetailsAttribute : productDetailsAttributes) {
                if (productDetailsAttribute != null) {
                    if (productDetailsAttribute.isAge()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}