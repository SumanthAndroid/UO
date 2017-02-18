package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.ErrorHandling.ResponseDao;

import org.parceler.Parcel;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 9/14/16.
 * Class: SubmitPurchaseResponse
 * Class Description: Response class for submitting purchase data
 */
@Parcel
public class SubmitPurchaseResponse extends ResponseDao {
    public static final String TAG = "SubmitPurchaseResponse";
    public static final int HTTP_STATUS_CC_PROCESSING_ERROR = 408;

    @SerializedName("result")
    CardResult result;

    @SerializedName("message")
    String message;

    public CardResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
