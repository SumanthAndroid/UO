package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.models.CommerceMessage;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;
import com.universalstudios.orlandoresort.model.network.response.NetworkErrorResponse;

import java.util.ArrayList;

/**
 * Error object that gets return for an "add item" failure
 *
 * @author tjudkins
 * @since 10/26//16
 */
@Parcel
public class AddItemErrorResponse extends NetworkErrorResponse {
    public static final String TAG = AddItemErrorResponse.class.getSimpleName();

    @SerializedName("errors")
    ArrayList<AddItemError> errors;

    public ArrayList<AddItemError> getErrors() {
        return errors;
    }

    public boolean hasInventoryError() {
        boolean hasInventoryError = false;
        if(errors == null || errors.isEmpty()) {
            return hasInventoryError;
        }
        for (AddItemError error : errors) {
            if (error.isInventoryError()) {
                hasInventoryError = true;
                break;
            }
        }
        return hasInventoryError;
    }

    public boolean hasInsufficientInventoryError() {
        boolean hasInventoryError = false;
        if (errors != null) {
            for (AddItemError error : errors) {
                if (error.isInsufficientInventoryError()) {
                    hasInventoryError = true;
                    break;
                }
            }
        }
        return hasInventoryError;
    }

    public boolean isInventoryUnavailableError() {
        boolean hasInventoryError = false;
        if (errors != null) {
            for (AddItemError error : errors) {
                if (error.isInventoryUnavailableError()) {
                    hasInventoryError = true;
                    break;
                }
            }
        }
        return hasInventoryError;
    }

    public String getItemTypeMaxQuantityExceededError() {
        String errorString = null;
        if(errors == null || errors.isEmpty()) {
            return errorString;
        }
        for (AddItemError error : errors) {
            if (CommerceMessage.KEY_ERR_ITEM_TYPE_MAX_QTY_EXCEEDED.equalsIgnoreCase(error.getKey())) {
                errorString = error.getMessage();
                break;
            }
        }
        return errorString;
    }
}
