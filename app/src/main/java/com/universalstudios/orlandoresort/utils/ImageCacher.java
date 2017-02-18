package com.universalstudios.orlandoresort.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This image cacher only works if you use picasso to inject an imageview with the url path
 * @author jamestimberlake
 * @created 5/5/16.
 */
public class ImageCacher {

    public static void cacheImages(Picasso picasso, Context context, List<String> imageUrls){
        for (String imageUrl : imageUrls) {
            cacheImage(picasso, context, imageUrl);
        }
    }

    private static void cacheImage(Picasso picasso, Context context, String imageUrl){
        picasso.with(context).load(imageUrl).fetch();
    }

}
