package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.text.TextUtils;

import com.universalstudios.orlandoresort.BR;

import org.parceler.Transient;

/**
 * @author tjudkins
 * @since 1/23/17
 */

public class LinkEntitlementStep2ViewModel extends BaseObservable {

    private LinkEntitlementModel linkEntitlementModel;
    @Bindable
    private boolean showLoading;
    @Bindable
    boolean allRequiredFieldsComplete;

    public LinkEntitlementStep2ViewModel(LinkEntitlementModel linkEntitlementModel) {
        this.linkEntitlementModel = linkEntitlementModel;
        checkAllRequiredFieldsValid();
        this.linkEntitlementModel.addOnPropertyChangedCallback(onFieldsChangedCallback);
    }

    public LinkEntitlementModel getLinkEntitlementModel() {
        return linkEntitlementModel;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyPropertyChanged(BR.showLoading);
    }

    public boolean isAllRequiredFieldsComplete() {
        return allRequiredFieldsComplete;
    }

    public void setAllRequiredFieldsComplete(boolean allRequiredFieldsComplete) {
        this.allRequiredFieldsComplete = allRequiredFieldsComplete;
        notifyPropertyChanged(BR.allRequiredFieldsComplete);
    }

    private void checkAllRequiredFieldsValid() {
        boolean valid = true;
        if (TextUtils.isEmpty(linkEntitlementModel.getOrderOrTicketNumber())) {
            valid = false;
        }
        if (linkEntitlementModel.getEntitleLinkType() == LinkEntitlementModel.LINK_TYPE_NAME) {
            if (TextUtils.isEmpty(linkEntitlementModel.getFirstName())) {
                valid &= false;
            }
            if (TextUtils.isEmpty(linkEntitlementModel.getLastName())) {
                valid &= false;
            }
        } else {
            if (null == linkEntitlementModel.getSalesProgram()
                    || TextUtils.isEmpty(linkEntitlementModel.getSalesProgram().getProgramCode())) {
                valid &= false;
            }
        }
        setAllRequiredFieldsComplete(valid);
    }

    private OnPropertyChangedCallback onFieldsChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            checkAllRequiredFieldsValid();
        }
    };

}
