package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * @author tjudkins
 * @since 1/23/17
 */
@Parcel
public class SalesProgram extends BaseObservable {

    @Bindable
    String name;
    @Bindable
    String programCode;

    @ParcelConstructor
    public SalesProgram(String name, String programCode) {
        this.name = name;
        this.programCode = programCode;
    }

    public String getName() {
        return name;
    }

    public String getProgramCode() {
        return programCode;
    }
}
