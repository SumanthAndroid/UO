package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import com.universalstudios.orlandoresort.controller.userinterface.checkout.binding.OrderConfirmationViewModel;

/**
 * Created by Tyler Ritchie on 2/6/17.
 */
public interface OrderConfirmationActionCallback {
    void onDobClicked();

    void onDobInfoPopupClicked();

    void onCreateAccountClicked();

    void onManageAccountClicked();

    void onCreatePinClicked(OrderConfirmationViewModel viewModel);

    void onCreatePinInfoClicked(OrderConfirmationViewModel viewModel);
}
