package com.universalstudios.orlandoresort.controller.userinterface.wallet.binding;

import android.databinding.Bindable;
import android.support.annotation.DrawableRes;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlement;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementAttributes;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.WalletEntitlementDetails;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author tjudkins
 * @since 12/21/16
 */
public class WalletProductGroupViewModel extends BaseObservableWithLayoutItem {

    @Bindable
    String groupTitle;
    @Bindable
    String groupDescription;
    @Bindable
    String quantity;
    @Bindable
    @DrawableRes int icon;

    @Bindable
    List<WalletEntitlementModel> walletEntitlementModelList;

    public WalletProductGroupViewModel(WalletEntitlementDetails walletEntitlementDetails) {
        walletEntitlementModelList = new ArrayList<>();
        if (null != walletEntitlementDetails) {
            setGroupTitle(walletEntitlementDetails.getTicketName());
            setGroupDescription(walletEntitlementDetails.getEntitlementDescription());
            if (null != walletEntitlementDetails.getEntitlements()) {
                for (WalletEntitlement entitlement : walletEntitlementDetails.getEntitlements()) {
                    walletEntitlementModelList.add(new WalletEntitlementModel(walletEntitlementDetails, entitlement));
                }
            }
        }
        if (!walletEntitlementModelList.isEmpty()) {
            WalletEntitlementModel model = walletEntitlementModelList.get(0);
            if (null != model) {
                setQuantity(walletEntitlementModelList.size());
                setIcon(model.getIconResId());
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_wallet_product;
    }

    public List<WalletEntitlementModel> getWalletEntitlementModelList() {
        return walletEntitlementModelList;
    }

    private void setWalletEntitlementModelList(List<WalletEntitlementModel> walletEntitlementModelList) {
        this.walletEntitlementModelList = walletEntitlementModelList;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    private void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    private void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public int getIcon() {
        return icon;
    }

    private void setIcon(int icon) {
        this.icon = icon;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        String qtyText = String.format(Locale.US, "%s %d", IceTicketUtils.getTridionConfig().getQuantityLabel(), quantity);
        this.quantity = qtyText;
    }

    private void sortEntitlements(List<WalletEntitlement> entitlements) {
        if (null != entitlements) {
            Collections.sort(entitlements, new Comparator<WalletEntitlement>() {
                        @Override
                        public int compare(WalletEntitlement left, WalletEntitlement right) {
                            WalletEntitlementAttributes leftAttributes;
                            WalletEntitlementAttributes rightAttributes;

                            if (left != null) {
                                leftAttributes = left.getAttributes();
                            } else {
                                return -1;
                            }

                            if (right != null) {
                                rightAttributes = right.getAttributes();
                            } else {
                                return 1;
                            }

                            if (leftAttributes != null && WalletEntitlementAttributes.AGE_ADULT.equalsIgnoreCase(leftAttributes.getAge())) {
                                if (rightAttributes != null && WalletEntitlementAttributes.AGE_ADULT.equalsIgnoreCase(rightAttributes.getAge())) {
                                    return left.getEntitlementFirstName().compareTo(right.getEntitlementFirstName());
                                } else {
                                    return -1;
                                }
                            } else if (rightAttributes != null && WalletEntitlementAttributes.AGE_ADULT.equalsIgnoreCase(rightAttributes.getAge())) {
                                return 1;
                            } else {
                                return left.getEntitlementFirstName().compareTo(right.getEntitlementFirstName());
                            }
                        }
                    }
            );
        }
    }

    public interface WalletProductGroupCallback {
        void onGroupClicked(List<WalletEntitlementModel> walletEntitlementModels);
    }

}
