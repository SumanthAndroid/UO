package com.universalstudios.orlandoresort.model.network.response;

import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;

/**
 * Base class to hold error response data from network calls that receive non 2XX responses made by
 * {@link NetworkRequest} objects. All error response objects should extend from this class.
 *
 * @author Steven Byle
 */
public abstract class NetworkErrorResponse extends GsonObject {
}