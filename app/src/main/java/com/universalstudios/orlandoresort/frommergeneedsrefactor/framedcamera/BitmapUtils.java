package com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * @author jamestimberlake
 * @created 12/2/15.
 */
public class BitmapUtils {

    public static Bitmap mergeBitmaps(Bitmap bitmap1, Bitmap bitmap2, int desiredWidth, int desiredHeight){
        Paint paint1 = new Paint();
        Paint paint2 = new Paint();

        int width;
        int height;
        float scaleWidth;
        float scaleHeight;

        Bitmap mCBitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, bitmap1.getConfig());

        Canvas canvas = new Canvas(mCBitmap);

        /*width = bitmap1.getWidth();
        height = bitmap1.getHeight();
        scaleWidth = ((float) desiredWidth) / width;
        scaleHeight = ((float) desiredHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        canvas.drawBitmap(bitmap1,matrix,paint1);

        width = bitmap2.getWidth();
        height = bitmap2.getHeight();
        scaleWidth = ((float) desiredWidth) / width;
        scaleHeight = ((float) desiredHeight) / height;

        matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);/**/

        canvas.drawBitmap(getResizedBitmap(bitmap1, desiredWidth, desiredHeight),0,0,paint1);
        canvas.drawBitmap(getResizedBitmap(bitmap2, desiredWidth, desiredHeight), 0,0 ,paint2);

        bitmap1.recycle();
        bitmap2.recycle();

        return mCBitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
    }


}
