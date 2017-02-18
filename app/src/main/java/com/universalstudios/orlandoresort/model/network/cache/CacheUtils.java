/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.cache;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StatFs;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.io.File;

/**
 * 
 * 
 * @author Steven Byle
 */
public class CacheUtils {
	private static final String TAG = CacheUtils.class.getSimpleName();

	public static final String MAP_TILE_DISK_CACHE_NAME = "map-tile-disk-cache";
	public static final long MAP_TILE_DISK_CACHE_MAX_SIZE_BYTES = 50l * 1024 * 1024; // 50 MB
	public static final long MAP_TILE_DISK_CACHE_MIN_SIZE_BYTES = 10l * 1024 * 1024; // 10 MB

	public static final String POI_IMAGE_DISK_CACHE_NAME = "poi-image-disk-cache";
	public static final long POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES = 50l * 1024 * 1024; // 50 MB
	public static final long POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES = 10l * 1024 * 1024; // 10 MB

	public static final String WAYFINDING_DISK_CACHE_NAME = "wayfinding-image-disk-cache";
	public static final long WAYFINDING_DISK_CACHE_MAX_SIZE_BYTES = 10l * 1024 * 1024; // 10 MB
	public static final long WAYFINDING_DISK_CACHE_MIN_SIZE_BYTES = 5l * 1024 * 1024; // 5 MB

	public static final String INTERACTIVE_EXPERIENCE_DISK_CACHE_NAME = "interactive-experience-disk-cache";
	public static final long INTERACTIVE_EXPERIENCE_DISK_CACHE_MAX_SIZE_BYTES = 100l * 1024 * 1024; // 100 MB
	public static final long INTERACTIVE_EXPERIENCE_DISK_CACHE_MIN_SIZE_BYTES = 50l * 1024 * 1024; // 50 MB

	public static final String COMMERCE_DISK_CACHE_NAME = "commerce-image-disk-cache";
	public static final long COMMERCE_DISK_CACHE_MAX_SIZE_BYTES = 10l * 1024 * 1024; // 10 MB
	public static final long COMMERCE_DISK_CACHE_MIN_SIZE_BYTES = 5l * 1024 * 1024; // 5 MB


	public static synchronized File getOrCreateCacheDir(String cacheName) {

		File cacheDir = new File(UniversalOrlandoApplication.getAppContext().getCacheDir(), cacheName);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir;
	}

	public static synchronized boolean deleteCacheDir(String cacheName) {
		File cacheDir = new File(UniversalOrlandoApplication.getAppContext().getCacheDir(), cacheName);
		return deleteDir(cacheDir);
	}

	public static synchronized boolean deleteDir(File dir) {
		// Recursively wipe all the files in this directory
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
		}

		// The directory is now empty so delete it
		return dir != null ? dir.delete() : false;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static synchronized long calculateDiskCacheSize(File dir, long minSizeBytes, long maxSizeBytes) {
		long preferredSizeBytes = minSizeBytes;

		try {
			StatFs statFs = new StatFs(dir.getAbsolutePath());
			long available;

			// API 18 (4.3) and up
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
				available = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
			}
			// API 17 (4.2.2) and below
			else {
				available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
			}

			// Use no more than 8% of the total available space
			preferredSizeBytes = (available * 8) / 100;
		}
		catch (IllegalArgumentException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "calculateDiskCacheSize: exception trying to calculate available disk space", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
		}

		// Use the preferred size if it's smaller than the max, otherwise use the max
		long minOfPreferredAndMax = Math.min(preferredSizeBytes, maxSizeBytes);

		// Use the preferred or max if they are greater than the minimum, otherwise use the minimum
		long cacheSizeBytes = Math.max(minSizeBytes, minOfPreferredAndMax);

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "calculateDiskCacheSize: cache size in bytes = " + cacheSizeBytes);
		}

		return cacheSizeBytes;
	}
}
