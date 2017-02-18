package com.universalstudios.orlandoresort.view.binding;

import android.support.annotation.LayoutRes;

/**
 * @author tjudkins
 * @since 12/27/16
 */

public interface LayoutBinding {
    /**
     * Get the layout resource ID for an view that needs to be bound.
     * @return the resource ID of the layout
     */
    @LayoutRes int getLayoutId();
}
