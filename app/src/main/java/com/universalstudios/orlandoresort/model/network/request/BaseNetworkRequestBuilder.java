package com.universalstudios.orlandoresort.model.network.request;

import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.ConcurrencyType;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest.Priority;

/**
 * Base request builder class used for common request parameters.
 * <p/>
 * Created by Jack Hughes on 9/19/16.
 */
public abstract class BaseNetworkRequestBuilder<T extends BaseNetworkRequestBuilder> {
    protected final String senderTag;
    protected Priority priority;
    protected ConcurrencyType concurrencyType;

    public BaseNetworkRequestBuilder(NetworkRequestSender networkRequestSender) {
        super();
        this.senderTag = networkRequestSender != null ? networkRequestSender.getSenderTag() : null;
    }

    /**
     * [OPTIONAL] Set a priority on the request. This is only necessary if there are multiple
     * requests being queued and sent at once. The requests will be sent in order, depending on
     * their priority level. By default requests are set to {@code Priority.NORMAL}.
     *
     * @param priority
     *         the {@link Priority}
     *
     * @return
     */
    public T setPriority(NetworkRequest.Priority priority) {
        this.priority = priority;
        return getThis();
    }

    /**
     * [OPTIONAL] Set if the request should be run asynchronously, and process requests at the same
     * time. By default requests are run synchronously, blocking the next request until it
     * completes.
     * <p/>
     * Note: All network requests occur off the of the main UI thread, this setting simply affects
     * whether subsequent requests will be blocked or not.
     *
     * @param concurrencyType
     *         the {@link ConcurrencyType}
     *         that specifies if the request should be asynchronous, otherwise it is synchronous
     *
     * @return
     */
    public T setConcurrencyType(ConcurrencyType concurrencyType) {
        this.concurrencyType = concurrencyType;
        return getThis();
    }

    protected abstract T getThis();

    public abstract NetworkRequest build();
}
