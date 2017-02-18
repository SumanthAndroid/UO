package com.universalstudios.orlandoresort.model.network.domain.wallet;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import java.util.List;

/**
 * Created by kbojarski on 1/25/17.
 */

public class GetWalletCardsResponse extends NetworkResponse {
    @SerializedName("statusCode")
    String statusCode;

    @SerializedName("result")
    List<WalletFolioCard> result;

    @SerializedName("message")
    String message;

    public String getStatusCode() {
        return statusCode;
    }

    public List<WalletFolioCard> getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
