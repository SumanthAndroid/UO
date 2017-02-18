package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Entitlements.ByDate.response.GetTicketsByDateResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.wallet.response.GetMyWalletResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Retrofit Service interface for commerce endpoints.
 * <p/>
 * Created by Jack Hughes on 9/19/16.
 */
public interface UniversalOrlandoCommerceServices {

    // Utilities

    /**
     * Method to get the seasonal tickets by a date range
     *
     * @param startDate
     *         a String for the formatted date of the start date
     * @param endDate
     *         a String for the formatted date of the end date
     *
     * @return the {@link GetTicketsByDateResponse} object (for synchronous calls)
     */
    @GET("/utils/utilities/entitlements/calendar/{startDate}/{endDate}")
    GetTicketsByDateResponse getTicketsByDate(@Path("startDate") String startDate, @Path("endDate") String endDate);

    /**
     * Method to get the seasonal tickets by a date range
     *
     * @param startDate
     *         a String for the formatted date of the start date
     * @param endDate
     *         a String for the formatted date of the end date
     */
    @GET("/utils/utilities/entitlements/calendar/{startDate}/{endDate}")
    void getTicketsByDate(@Path("startDate") String startDate, @Path("endDate") String endDate, Callback<GetTicketsByDateResponse> callback);

    // My Wallet

    @GET("/inv/orders/wallet")
    GetMyWalletResponse getMyWallet(@Header("Authorization") String authorization);

    @GET("/inv/orders/wallet")
    void getMyWallet(@Header("Authorization") String authorization, Callback<GetMyWalletResponse> callback);
}
