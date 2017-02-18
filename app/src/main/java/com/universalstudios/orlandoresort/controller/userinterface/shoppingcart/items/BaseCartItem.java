package com.universalstudios.orlandoresort.controller.userinterface.shoppingcart.items;

import android.databinding.BaseObservable;
import android.support.annotation.LayoutRes;

/**
 * @author tjudkins
 * @since 10/12/16
 */

abstract public class BaseCartItem extends BaseObservable {

    abstract public @LayoutRes int getLayoutId();

}
