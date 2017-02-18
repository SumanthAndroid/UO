package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Tyler Ritchie on 10/12/16.
 */

@Parcel
public class ProductDetailsSku extends GsonObject {

    @SerializedName("SKUUniqueID")
    String skuUniqueId;

    @SerializedName("Attributes")
    List<ProductDetailsSkuAttributes> productDetailsSkuAttributes;

    @SerializedName("Price")
    List<ProductDetailsSkuPrice> productDetailsSkuPrice;

    public List<ProductDetailsSkuAttributes> getProductDetailsSkuAttributes() {
        return productDetailsSkuAttributes;
    }

    public List<ProductDetailsSkuPrice> getProductDetailsSkuPrice() {
        return productDetailsSkuPrice;
    }

    public String getSkuUniqueId() {
        return skuUniqueId;
    }


    public boolean isDay(@NonNull String day) {
        List<ProductDetailsSkuAttributes> productDetailsSkuAttributes = getProductDetailsSkuAttributes();
        if (productDetailsSkuAttributes != null) {
            for (ProductDetailsSkuAttributes productDetailsSkuAttribute : productDetailsSkuAttributes) {
                if (productDetailsSkuAttribute != null && productDetailsSkuAttribute.isDay()) {
                    List<ProductDetailsSkuAttributesValues> productDetailsSkuAttributesValues = productDetailsSkuAttribute.getProductDetailsSkuAttributesValues();
                    if (productDetailsSkuAttributesValues != null) {
                        for (ProductDetailsSkuAttributesValues productDetailsAttributesValue : productDetailsSkuAttributesValues) {
                            if (productDetailsAttributesValue != null) {
                                return day.equals(productDetailsAttributesValue.getValues());
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isAge(@NonNull String age) {
        List<ProductDetailsSkuAttributes> productDetailsSkuAttributes = getProductDetailsSkuAttributes();
        if (productDetailsSkuAttributes != null) {
            for (ProductDetailsSkuAttributes productDetailsSkuAttribute : productDetailsSkuAttributes) {
                if (productDetailsSkuAttribute != null && productDetailsSkuAttribute.isAge()) {
                    List<ProductDetailsSkuAttributesValues> productDetailsSkuAttributesValues = productDetailsSkuAttribute.getProductDetailsSkuAttributesValues();
                    if (productDetailsSkuAttributesValues != null) {
                        for (ProductDetailsSkuAttributesValues productDetailsAttributesValue : productDetailsSkuAttributesValues) {
                            if (productDetailsAttributesValue != null) {
                                return age.equals(productDetailsAttributesValue.getValues());
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
