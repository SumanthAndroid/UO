package com.universalstudios.orlandoresort.model.state.commerce;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import java.util.Date;

/**
 * Note: Because this object is persisted, fields should not be changed, but can be added.
 *
 * @author Steven Byle
 */
public class CommerceState extends GsonObject {

    // ************ PARSED STATE SET REMOTELY BY SERVICES ************

    @SerializedName("appValidForCommerce")
    private Boolean appValidForCommerce;

    // ************ NOT PARSED, ADDED STATE ************

    @SerializedName("dateOfLastCommerceEnabledSyncInMillis")
    private Long dateOfLastCommerceEnabledSyncInMillis;

    public Boolean getAppValidForCommerce() {
        return appValidForCommerce;
    }

    public void setAppValidForCommerce(Boolean appValidForCommerce) {
        this.appValidForCommerce = appValidForCommerce;
    }

    public Long getDateOfLastCommerceEnabledSyncInMillis() {
        return dateOfLastCommerceEnabledSyncInMillis;
    }

    public Date getDateOfLastCommerceEnabledSync() {
        if (dateOfLastCommerceEnabledSyncInMillis != null) {
            return new Date(dateOfLastCommerceEnabledSyncInMillis);
        } else {
            return null;
        }
    }

    public void setDateOfLastCommerceEnabledSyncInMillis(Long dateOfLastCommerceEnabledSyncInMillis) {
        this.dateOfLastCommerceEnabledSyncInMillis = dateOfLastCommerceEnabledSyncInMillis;
    }
}