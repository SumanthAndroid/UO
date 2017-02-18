package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.math.BigDecimal;

/**
 * Created by IBM_ADMIN on 2/6/2016.
 *
 */
public class DeliveryOption extends GsonObject {

    public static final String FIELD1_ADDRESS_REQUIRED = "addressRequired=1";
    public static final String FIELD1_IS_DOMESTIC = "isDomestic=1";
    public static final String FIELD1_IS_INTERNATIONAL = "isInternational=1";

    @SerializedName("field1")
    Object field1;

    @SerializedName("field2")
    Object field2;

    @SerializedName("id")
    String id;

    @SerializedName("code")
    String code;

    @SerializedName("description")
    String description;

    @SerializedName("currency")
    String currency;

    @SerializedName("amount")
    BigDecimal amount;

    @SerializedName("carrier")
    String carrier;

    @SerializedName("additionalDescription")
    String additionalDescription;

    /**
     *
     * @return
     * The field1
     */
    public Object getField1() {
        return field1;
    }

    /**
     *
     * @return
     * The field2
     */
    public Object getField2() {
        return field2;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     * The code
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     * The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return
     * The amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     *
     * @return
     * The carrier
     */
    public String getCarrier() {
        return carrier;
    }

    public String getAdditionalDescription() {
        return additionalDescription;
    }

    public boolean isShippingAddressRequired() {
        boolean required = false;
        if (getField1() instanceof String) {
            String field1 = (String) getField1();
            if (!TextUtils.isEmpty(field1)) {
                if (field1.contains(FIELD1_ADDRESS_REQUIRED)) {
                    required = true;
                }
            }
        }
        return required;
    }

    public boolean isDomestic() {
        boolean required = false;
        if (getField1() instanceof String) {
            String field1 = (String) getField1();
            if (!TextUtils.isEmpty(field1)) {
                if (field1.contains(FIELD1_IS_DOMESTIC)) {
                    required = true;
                }
            }
        }
        return required;
    }

    public boolean isInternational() {
        boolean required = false;
        if (getField1() instanceof String) {
            String field1 = (String) getField1();
            if (!TextUtils.isEmpty(field1)) {
                if (field1.contains(FIELD1_IS_INTERNATIONAL)) {
                    required = true;
                }
            }
        }
        return required;
    }

}
