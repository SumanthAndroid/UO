package com.universalstudios.orlandoresort.view.progress;

import org.parceler.Parcel;

/**
 * A step used in {@link StepProgressView}
 */
@Parcel
public class Step {
    String mText;
    String mTag;

    public Step() {
    }

    public Step(String text, String tag) {
        mTag = tag;
        mText = text;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
