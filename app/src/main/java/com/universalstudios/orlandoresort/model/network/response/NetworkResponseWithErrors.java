package com.universalstudios.orlandoresort.model.network.response;

import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base class to hold response data from network calls made by {@link NetworkRequest} objects. All
 * response objects should extend from this class.
 *
 * @author Steven Byle
 */
public abstract class NetworkResponseWithErrors<T extends NetworkErrorResponse> extends NetworkResponse {

    private T networkErrorResponse;

    private Class inferredNetworkErrorResponseClass;

    public T getNetworkErrorResponse() {
        return networkErrorResponse;
    }

    public void setNetworkErrorResponse(T networkErrorResponse) {
        this.networkErrorResponse = networkErrorResponse;
    }

    public Class<? extends NetworkErrorResponse> getNetworkErrorResponseClass() {
        if (networkErrorResponse != null) {
            return networkErrorResponse.getClass();
        } else {
            if (inferredNetworkErrorResponseClass == null) {
                Type genericSuperclass = getClass().getGenericSuperclass();
                Type type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
                String className = type.toString().split(" ")[1];
                try {
                    inferredNetworkErrorResponseClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    inferredNetworkErrorResponseClass = null;
                }
            }
            return inferredNetworkErrorResponseClass;
        }
    }
}