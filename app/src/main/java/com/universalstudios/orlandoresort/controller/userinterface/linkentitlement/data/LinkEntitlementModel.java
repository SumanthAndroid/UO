package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.universalstudios.orlandoresort.BR;

import org.parceler.Parcel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tjudkins
 * @since 1/23/17
 */
@Parcel
public class LinkEntitlementModel extends BaseObservable {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({MEDIA_STATUS_VALID,
            MEDIA_STATUS_INVALID_MEDIA,
            MEDIA_STATUS_INVALID_SECOND_FACTOR,
            MEDIA_STATUS_INVALID_USAGE,
            MEDIA_STATUS_TOO_MANY_ENTITLEMENTS,
            MEDIA_STATUS_FORBIDDEN_TYPE,
            MEDIA_STATUS_LINKING_LOCKED
    })
    public @interface MediaStatus {}
    public static final String MEDIA_STATUS_VALID = "Valid";
    public static final String MEDIA_STATUS_INVALID_MEDIA = "InvalidMedia";
    public static final String MEDIA_STATUS_INVALID_SECOND_FACTOR = "InvalidSecondFactor";
    public static final String MEDIA_STATUS_INVALID_USAGE = "InvalidUsage";
    public static final String MEDIA_STATUS_TOO_MANY_ENTITLEMENTS = "TooManyEntitlements";
    public static final String MEDIA_STATUS_FORBIDDEN_TYPE = "ForbiddenType";
    public static final String MEDIA_STATUS_LINKING_LOCKED = "LinkingLocked";


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINK_TYPE_NAME,
            LINK_TYPE_SALES_PROGRAM})
    private @interface EntitlementLinkType {}

    public static final int LINK_TYPE_NAME = 1;
    public static final int LINK_TYPE_SALES_PROGRAM = 2;

    @Bindable
    String orderOrTicketNumber;
    @Bindable
    String firstName;
    @Bindable
    String lastName;
    @Bindable
    SalesProgram salesProgram;
    @Bindable
    @EntitlementLinkType int entitleLinkType;

    public String getOrderOrTicketNumber() {
        return orderOrTicketNumber;
    }

    public void setOrderOrTicketNumber(String orderOrTicketNumber) {
        this.orderOrTicketNumber = orderOrTicketNumber;
        notifyPropertyChanged(BR.orderOrTicketNumber);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    public @EntitlementLinkType int getEntitleLinkType() {
        return entitleLinkType;
    }

    public void setEntitleLinkType(@EntitlementLinkType int entitleLinkType) {
        this.entitleLinkType = entitleLinkType;
        notifyPropertyChanged(BR.entitleLinkType);
    }

    public SalesProgram getSalesProgram() {
        return salesProgram;
    }

    public void setSalesProgram(SalesProgram salesProgram) {
        this.salesProgram = salesProgram;
        notifyPropertyChanged(BR.salesProgram);
    }
}
