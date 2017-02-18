package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.ErrorHandling;

import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import retrofit.RetrofitError;

/**
 * Created by ibm_admin on 1/22/2016.
 */
@Parcel
public class ResponseDao extends NetworkResponse {

    RetrofitError errorDao;

    public RetrofitError getError() {
        return errorDao;
    }

    public void setError(RetrofitError errorDao) {
        this.errorDao = errorDao;
    }
}
