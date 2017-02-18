package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.databinding.Bindable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.view.binding.BaseObservableWithLayoutItem;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by kbojarski on 1/17/17.
 */

public class ViewProfileItem extends BaseObservableWithLayoutItem {

    @Retention(SOURCE)
    @IntDef({PROFILE_ITEM_PERSONAL, PROFILE_ITEM_CONTACT, PROFILE_ITEM_PASSWORD, PROFILE_ITEM_COMMUNICATION_PREFS, PROFILE_ITEM_INTERESTS, PROFILE_ITEM_ADDRESSES})
    public @interface ProfileItemType {

    }

    public static final int PROFILE_ITEM_PERSONAL = 0;
    public static final int PROFILE_ITEM_CONTACT = 1;
    public static final int PROFILE_ITEM_PASSWORD = 2;
    public static final int PROFILE_ITEM_COMMUNICATION_PREFS = 3;
    public static final int PROFILE_ITEM_INTERESTS = 4;
    public static final int PROFILE_ITEM_ADDRESSES = 5;

    @Bindable
    @ProfileItemType
    private int itemType;

    @Bindable
    @DrawableRes
    private int drawableResId;

    @Bindable
    private String title;

    @Bindable
    private String actionText;

    @Bindable
    private String header1;

    @Bindable
    private String detail1;

    @Bindable
    private String detail2;

    @Bindable
    private String header2;

    @Bindable
    private String detail3;

    @Bindable
    private String detail4;

    public ViewProfileItem(@ProfileItemType int itemType, int drawableResId, String title, String actionText) {
        this.itemType = itemType;
        this.drawableResId = drawableResId;
        this.title = title;
        this.actionText = actionText;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_view_profile;
    }


    @ProfileItemType
    public int getItemType() {
        return itemType;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public void setDrawableResId(int drawableResId) {
        this.drawableResId = drawableResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getHeader1() {
        return header1;
    }

    public void setHeader1(String header1) {
        this.header1 = header1;
    }

    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

    public String getHeader2() {
        return header2;
    }

    public void setHeader2(String header2) {
        this.header2 = header2;
    }

    public String getDetail3() {
        return detail3;
    }

    public void setDetail3(String detail3) {
        this.detail3 = detail3;
    }

    public String getDetail4() {
        return detail4;
    }

    public void setDetail4(String detail4) {
        this.detail4 = detail4;
    }

    public interface OnProfileActionItemClickedListener {
        void onProfileActionItemClicked(@ViewProfileItem.ProfileItemType int itemType);
    }
}
