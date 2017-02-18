package com.universalstudios.orlandoresort.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Project Name: Custom Slider
 * Created by kevinhaines on 7/11/16.
 * Class: CustomSlider
 * Class Description: Custom slider used in VQ
 */
public class CustomSlider extends View implements View.OnTouchListener{
    public static final String TAG = "CustomSlider";

    private static final int SQUARE_SIZE = 100;
    Paint linePaint = new Paint();

    protected ThumbDrawable mThumbCircle;
    private boolean mIsDragging = false;
    float mTouchProgressOffset;
    private float mTouchDownX;
    private int mScaledTouchSlop;
    private int mTouchLocation;
    private int mMax = 8;
    private int mProgress = 0;
    private OnSliderValueUpdatedListener mSliderValueListener;

    public CustomSlider(Context context) {
        super(context);
        init();
    }

    public CustomSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public CustomSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init () {
        createThumbDrawable();
        this.setOnTouchListener(this);
    }

    private void createThumbDrawable() {
        mThumbCircle = new ThumbDrawable();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawTrack(canvas);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        int size = 35;

        mThumbCircle.draw(canvas, getTotalOffsetLeft(), getCenterY(), size);
    }

    private int getCenterY() {
        return getMeasuredHeight() / 2;
    }

    private int getTotalOffsetLeft() {
        return getPaddingLeft();
    }

    @Override
    public int getPaddingLeft() {
        return super.getPaddingLeft() + SQUARE_SIZE / 2;
    }

    @Override
    public int getPaddingRight() {
        return super.getPaddingRight() + SQUARE_SIZE / 2;
    }

    private void drawTrack(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int offsetLeft = getPaddingLeft();
        int offsetRight = getPaddingRight();
        linePaint.setColor(Color.parseColor("#FFBFBFC4"));
        linePaint.setStrokeWidth(10);

        canvas.drawLine(offsetLeft, height/2, width - offsetRight, height/2, linePaint);
    }

    private class ThumbDrawable extends Drawable {
        Paint paint = new Paint();
        Paint tooltipPaint = new Paint();
        Paint strokePaint = new Paint();
        Paint textPaint = new Paint();
        int mSize;
        int mLeft;
        int centerY;

        public ThumbDrawable() {
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            strokePaint.setColor(Color.parseColor("#FFC8C8C8"));
            strokePaint.setStrokeWidth(4);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setAntiAlias(true);

            tooltipPaint.setColor(Color.BLUE);

            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(SQUARE_SIZE / 2);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }

        public void draw(Canvas canvas, int left, int top, int radius) {
            canvas.save();
            mSize = radius;
            mLeft = left;
            this.centerY = top;
            draw(canvas);
            canvas.restore();
        }

        @Override
        public void draw(Canvas canvas) {
            int left;
            if (mTouchLocation <= getPaddingLeft()) {
                left = getPaddingLeft();
            } else if (mTouchLocation >= CustomSlider.this.getMeasuredWidth() - getPaddingRight()) {
                left = CustomSlider.this.getMeasuredWidth() - getPaddingRight();
            } else {
                left = mTouchLocation;
            }
            canvas.drawCircle(left, centerY, mSize, paint);
            canvas.drawCircle(left, centerY, mSize, strokePaint);

            if (Build.VERSION.SDK_INT > 21) {
                canvas.drawRoundRect(left - (SQUARE_SIZE / 2), 0, left - (SQUARE_SIZE / 2) + SQUARE_SIZE, SQUARE_SIZE, 10, 10, tooltipPaint);
            } else {
                canvas.drawRect(left - (SQUARE_SIZE / 2), 0, left - (SQUARE_SIZE / 2) + SQUARE_SIZE, SQUARE_SIZE, tooltipPaint);
            }

            canvas.drawText(Integer.toString(getProgress() + 1), left, SQUARE_SIZE / 2 + 10, textPaint);

            Path path = new Path();
            path.moveTo(left  - (SQUARE_SIZE / 2) + 10, SQUARE_SIZE);
            path.lineTo(left  - (SQUARE_SIZE / 2) - 10 + SQUARE_SIZE, SQUARE_SIZE);
            path.lineTo(left  - (SQUARE_SIZE / 2) + SQUARE_SIZE - SQUARE_SIZE / 2, SQUARE_SIZE + 20);
            path.lineTo(left  - (SQUARE_SIZE / 2) + 10, SQUARE_SIZE);
            path.close();

            canvas.drawPath(path, tooltipPaint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    public int getProgress() {
        return mProgress;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer()) {
                    mTouchDownX = event.getX();
                } else {
                    setPressed(true);
                    if (mThumbCircle != null) {
                        invalidate(mThumbCircle.getBounds()); // This may be within the padding region
                    }
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    final float x = event.getX();
                    if (Math.abs(x - mTouchDownX) > mScaledTouchSlop) {
                        setPressed(true);
                        if (mThumbCircle != null) {
                            invalidate(mThumbCircle.getBounds()); // This may be within the padding region
                        }
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;
        if(mSliderValueListener != null) {
            /* Add one to account for the zero indexed progress value */
            mSliderValueListener.onSliderValueUpdated(mProgress+1);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        final int width = getWidth();
        final int available = width - getPaddingLeft() - getPaddingRight();
        final int x = (int) event.getX();
        float scale;
        float progress = 0;

        if (x < getPaddingLeft()) {
            scale = 0.0f;
        } else if (x > width - getPaddingRight()) {
            scale = 1.0f;
        } else {
            scale = (float)(x - getPaddingLeft()) / (float)available;
            progress = mTouchProgressOffset;
        }

        progress += scale * mMax;

        mTouchLocation = x;
        setProgress((int) progress);
        invalidate();
    }

    private void setProgress(int progress) {
        mProgress = progress;
    }


    public boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setOnSliderValueUpdatedListener(OnSliderValueUpdatedListener listener) {
        mSliderValueListener = listener;
    }

    public interface OnSliderValueUpdatedListener {
        void onSliderValueUpdated(int val);
    }

}
