package com.universalstudios.orlandoresort.controller.userinterface.addons;

import java.math.BigDecimal;

/**
 * Created by kbojarski on 11/8/16.
 */

public class SelectTierObject {
    private String mDisplayName;
    private String mTierName;
    private BigDecimal mChildPrice;
    private BigDecimal mAdultPrice;
    private String mAdultPartNumber;
    private String mChildPartNumber;

    public SelectTierObject() {
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getTierName() {
        return mTierName;
    }

    public void setTierName(String mTitle) {
        this.mTierName = mTitle;
    }

    public BigDecimal getChildPrice() {
        return mChildPrice;
    }

    public void setChildPrice(BigDecimal mChildPrice) {
        this.mChildPrice = mChildPrice;
    }

    public BigDecimal getAdultPrice() {
        return mAdultPrice;
    }

    public void setAdultPrice(BigDecimal mAdultPrice) {
        this.mAdultPrice = mAdultPrice;
    }

    public String getAdultPartNumber() {
        return mAdultPartNumber;
    }

    public void setAdultPartNumber(String mAdultPartNumber) {
        this.mAdultPartNumber = mAdultPartNumber;
    }

    public String getChildPartNumber() {
        return mChildPartNumber;
    }

    public void setChildPartNumber(String mChildPartNumber) {
        this.mChildPartNumber = mChildPartNumber;
    }
}
