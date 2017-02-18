package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

/**
 * @author tjudkins
 * @author Tyler Ritchie
 * @since 10/19/16
 */

public class PersonalizationOffersPartNumber extends GsonObject {

    @SerializedName("productPartNumber")
    private String productPartNumber;

    @SerializedName("productItemType")
    private String productItemType;

    @SerializedName("skuPartNumber")
    private String skuPartNumber;

    public static class Builder {
        private PersonalizationOffersPartNumber model;

        public Builder() {
            super();
            model = new PersonalizationOffersPartNumber();
        }

        public Builder setProductPartNumber(String productPartNumber) {
            model.productPartNumber = productPartNumber;
            return this;
        }

        public Builder setProductItemType(String productItemType) {
            model.productItemType = productItemType;
            return this;
        }

        public Builder setSkuPartNumber(String skuPartNumber) {
            model.skuPartNumber = skuPartNumber;
            return this;
        }

        public PersonalizationOffersPartNumber build() {
            return model;
        }
    }
}
