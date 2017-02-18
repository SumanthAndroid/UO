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

public class LinkEntitlementStep1ViewModel extends BaseObservable {

    private LinkEntitlementModel linkEntitlementModel;
    @Bindable
    private boolean showLoading;
    @Bindable
    boolean allRequiredFieldsComplete;

    public LinkEntitlementStep1ViewModel(LinkEntitlementModel linkEntitlementModel) {
        if (null != linkEntitlementModel) {
            this.linkEntitlementModel = linkEntitlementModel;
            checkAllRequiredFieldsValid();
        } else {
            this.linkEntitlementModel = new LinkEntitlementModel();
        }
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
        setAllRequiredFieldsComplete(!TextUtils.isEmpty(linkEntitlementModel.getOrderOrTicketNumber()));
    }

    private OnPropertyChangedCallback onFieldsChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            checkAllRequiredFieldsValid();
        }
    };

}
