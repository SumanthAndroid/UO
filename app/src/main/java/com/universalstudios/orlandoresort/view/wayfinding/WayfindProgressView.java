package com.universalstudios.orlandoresort.view.wayfinding;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/8/16.
 * Class: WayfindProgressView
 * Class Description: This class is a custom view to show a dotted
 * line that is used to show progress.
 */
public class WayfindProgressView extends View {

    private int mProgressCount = 20;
    private int mCurrentPosition = 0;
    private Context mContext;

    private int mSelectedCircleRadius ;
    private int mCircleRadius;
    private int mLineLength = 300;
    private int mTotalLength; // Used for centering but not perfect

    private static final int CIRCLE_OFFSET_FROM_LINE = 4;

    public WayfindProgressView(Context context) {
        super(context);
        init(context);
    }

    public WayfindProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WayfindProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public WayfindProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCirlces(canvas);
    }

    public void setSelectedPosition(int position) {
        mCurrentPosition = position;
        invalidate();
    }

    public int getSelectedPosition() {
        return mCurrentPosition;
    }

    public void setIndicatorCount(int count) {
        this.mProgressCount = count;
        invalidate();
    }

    private void updateSizeValues(int startCount, int displayCount) {
        //Set larger circle values
        mSelectedCircleRadius = (getHeight() - getPaddingBottom() - getPaddingTop()) / 2;

        //Set smaller circle values
        mCircleRadius = (getHeight() - getPaddingBottom() - getPaddingTop()) / 6;

        //full width minus padding
        mLineLength = getWidth() - getPaddingLeft() - getPaddingRight();
        //current length minus the size of the smaller circles
        mLineLength -= (mCircleRadius * (displayCount - startCount - 1));
        //current length minus the size of the larger circle
        mLineLength -= (mSelectedCircleRadius + CIRCLE_OFFSET_FROM_LINE * 2);
        mLineLength =  mLineLength / ((displayCount - startCount) + 1);

        //Used for centering
        mTotalLength = ((displayCount - 1) * mLineLength) + ((displayCount) * mCircleRadius) + mSelectedCircleRadius;
        mTotalLength -= getPaddingLeft() + getPaddingRight();
    }


    private void drawCirlces(Canvas canvas) {
        if (mCurrentPosition >= mProgressCount) {
            mCurrentPosition = mProgressCount;
        }

        int displayCount = mProgressCount;
        int startCount = 0;
        if (mProgressCount > 8) {
            if (mCurrentPosition >= 4) {
                startCount = mCurrentPosition - 3;
            }
            displayCount = startCount + 8;
            if (displayCount > mProgressCount) {
                displayCount = mProgressCount;
            }
        }

        updateSizeValues(startCount, displayCount);
        int nextStartPosition = 0;
        if (mCurrentPosition == 0) {
            nextStartPosition = getHeight() / 2 /*+ ((getWidth() - mTotalLength) / 2) - 10*/;
        } else {
            nextStartPosition = getPaddingLeft() + 10 /*+ ((getWidth() - mTotalLength) / 2)*/;
        }

        for (int i = startCount; i < displayCount; i++) {
            Circle circle = new Circle();

            if (i == startCount && startCount != 0) {
                nextStartPosition = drawLine(nextStartPosition, circle, canvas);
            }

            if (i == mCurrentPosition) {
                nextStartPosition = drawSelectedCircle(nextStartPosition, circle, canvas);
            } else {
                nextStartPosition = drawCircle(nextStartPosition, circle, canvas);
            }

            if (i != mProgressCount - 1) {
                nextStartPosition = drawLine(nextStartPosition, circle, canvas);
            }
        }
    }

    private int drawLine(int start, Circle circle, Canvas canvas) {
        Paint linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth((int) (.35 * mCircleRadius));
        canvas.drawLine(start, getHeight() / 2, start + mLineLength, getHeight() / 2, linePaint);
        return start + mLineLength + (CIRCLE_OFFSET_FROM_LINE * 2) + (int) (.35 * mCircleRadius);
    }

    private int drawCircle(int start, Circle circle, Canvas canvas) {
        Paint paint = new Paint();
        circle.strokeSize = (int) (.35 * mCircleRadius);
        start += circle.strokeSize;
        circle.radius = mCircleRadius - circle.strokeSize;
        circle.startX = start;
        circle.startY = getHeight() / 2;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circle.strokeSize);

        canvas.drawCircle(circle.startX, circle.startY, circle.radius, paint);
        return start + circle.radius + circle.strokeSize + CIRCLE_OFFSET_FROM_LINE;
    }

    private int drawSelectedCircle(int start, Circle circle, Canvas canvas) {
        Paint paint = new Paint();
        circle.radius = mSelectedCircleRadius;
        circle.startX = start;
        circle.startY = getHeight() / 2;
        circle.strokeSize = (int) (.15 * circle.radius);
        circle.innerRadius = circle.radius - circle.strokeSize;

        //Draw Circle
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(circle.startX,
                circle.startY,
                circle.radius,
                paint);

        //Set Stroke
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circle.strokeSize);
        paint.setColor(Color.WHITE);

        canvas.drawCircle(circle.startX,
                circle.startY,
                circle.radius,
                paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(circle.strokeSize / 2);
        paint.setTextSize(circle.radius - (circle.radius * .3f));
        canvas.drawText(Integer.toString(mCurrentPosition + 1),
                circle.startX, (circle.startY + circle.radius / 2) - (paint.getTextSize() / 3), paint);
        return start + circle.radius + circle.strokeSize + CIRCLE_OFFSET_FROM_LINE;
    }

    private class Circle {
        int radius;
        int innerRadius;
        int strokeSize;
        int startX;
        int startY;
    }
}
