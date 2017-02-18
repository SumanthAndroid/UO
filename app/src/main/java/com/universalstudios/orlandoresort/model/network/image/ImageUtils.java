/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.image;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * @author Steven Byle
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ImageUtils {
	private static final String TAG = ImageUtils.class.getSimpleName();

	private static Map<Integer, Integer> VALID_MAP_TILE_SIZES = new HashMap<Integer, Integer>();
	static {
		VALID_MAP_TILE_SIZES.put(DisplayMetrics.DENSITY_LOW, 192);
		VALID_MAP_TILE_SIZES.put(DisplayMetrics.DENSITY_MEDIUM, 256);
		VALID_MAP_TILE_SIZES.put(DisplayMetrics.DENSITY_HIGH, 384);
		VALID_MAP_TILE_SIZES.put(DisplayMetrics.DENSITY_XHIGH, 512);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			VALID_MAP_TILE_SIZES.put(DisplayMetrics.DENSITY_XXHIGH, 768);
		}
	}

	public static int getMapTileSizeInPx(int dpiSizeShift) {
		int closestDpi = getClosestDpi(VALID_MAP_TILE_SIZES.keySet(), dpiSizeShift);
		return VALID_MAP_TILE_SIZES.get(closestDpi);
	}

	private static Map<Integer, String> VALID_POI_IMAGE_SIZES = new HashMap<Integer, String>();
	static {
		VALID_POI_IMAGE_SIZES.put(DisplayMetrics.DENSITY_LOW, RequestQueryParams.Values.IMAGE_SIZE_EXTRA_SMALL);
		VALID_POI_IMAGE_SIZES.put(DisplayMetrics.DENSITY_MEDIUM, RequestQueryParams.Values.IMAGE_SIZE_SMALL);
		VALID_POI_IMAGE_SIZES.put(DisplayMetrics.DENSITY_HIGH, RequestQueryParams.Values.IMAGE_SIZE_MEDIUM);
		VALID_POI_IMAGE_SIZES.put(DisplayMetrics.DENSITY_XHIGH, RequestQueryParams.Values.IMAGE_SIZE_LARGE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			VALID_POI_IMAGE_SIZES.put(DisplayMetrics.DENSITY_XXHIGH, RequestQueryParams.Values.IMAGE_SIZE_EXTRA_LARGE);
		}
	}

	public static String getPoiImageSizeString(int dpiSizeShift) {
		int closestDpi = getClosestDpi(VALID_POI_IMAGE_SIZES.keySet(), dpiSizeShift);
		return VALID_POI_IMAGE_SIZES.get(closestDpi);
	}

	private static int getClosestDpi(Set<Integer> validDpiSizes, int dpiSizeShift) {
		return getClosestDpi(validDpiSizes.toArray(new Integer[0]), dpiSizeShift);
	}

	private static int getClosestDpi(final Integer[] validDpiSizes, int dpiSizeShift) {
		/**
		 * Valid DPIs
		 * 120 | 0.75 | ldpi
		 * 160 | 1.0  | mdpi
		 * 213 | 1.33 | tvdpi
		 * 240 | 1.5  | hdpi
		 * 384 | 2.0  | xhdpi
		 * 480 | 3.0  | xxhdpi
		 * 640 | 4.0  | xxxhdpi
		 */

		if (validDpiSizes.length == 0) {
			throw new IllegalArgumentException("validDpiSizes cannot be null");
		}
		else if (validDpiSizes.length == 1) {
			return validDpiSizes[0];
		}

		// Sort the DPIs in order
		Arrays.sort(validDpiSizes);
		final int MIN_DPI_SIZE = validDpiSizes[0];
		final int MAX_DPI_SIZE = validDpiSizes[validDpiSizes.length - 1];

		int deviceDpi = UniversalOrlandoApplication.getAppContext().getResources().getDisplayMetrics().densityDpi;

		// If the DPI is out of the DPI range, return the outer bound
		if (deviceDpi <= MIN_DPI_SIZE) {
			return shiftClosestDpi(0, validDpiSizes, dpiSizeShift);
		}
		else if (deviceDpi >= MAX_DPI_SIZE) {
			return shiftClosestDpi(validDpiSizes.length - 1, validDpiSizes, dpiSizeShift);
		}

		// Find if computed size matches one of the valid DPIs, otherwise store the diff
		int[] dpiAbsDiffs = new int[validDpiSizes.length];
		for (int i = 0; i < validDpiSizes.length; i++) {
			if (deviceDpi == validDpiSizes[i]) {
				return shiftClosestDpi(i, validDpiSizes, dpiSizeShift);
			}
			else {
				dpiAbsDiffs[i] = Math.abs(deviceDpi - validDpiSizes[i]);
			}
		}

		// Otherwise, find the tile size that closest matches the computed size
		int minAbsDiffIndex = 0;
		int minAbsDiff = dpiAbsDiffs[0];
		for (int i = 0; i < dpiAbsDiffs.length; i++) {
			// Find the closest DPI, preferring larger DPI in ties
			if (dpiAbsDiffs[i] < minAbsDiff
					|| (dpiAbsDiffs[i] == minAbsDiff && validDpiSizes[i] > validDpiSizes[minAbsDiffIndex])) {
				minAbsDiffIndex = i;
				minAbsDiff = dpiAbsDiffs[i];
			}
		}

		int closestDpiIndex = minAbsDiffIndex;
		int closestDpi = validDpiSizes[closestDpiIndex];

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "getClosestDpi: device dpi bucket = " + deviceDpi + " closest allowed dpi = " + closestDpi);
		}

		return shiftClosestDpi(closestDpiIndex, validDpiSizes, dpiSizeShift);
	}

	private static int shiftClosestDpi(int closestDpiIndex, final Integer[] validDpiSizes, int dpiSizeShift) {
		int closestDpi = validDpiSizes[closestDpiIndex];

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "shiftClosestDpi: closest allowed dpi = " + closestDpi);
		}

		if (dpiSizeShift != 0) {
			closestDpiIndex += dpiSizeShift;
			if (closestDpiIndex >= validDpiSizes.length) {
				closestDpiIndex = validDpiSizes.length - 1;
			}
			else if (closestDpiIndex < 0) {
				closestDpiIndex = 0;
			}
			closestDpi = validDpiSizes[closestDpiIndex];

			if (BuildConfig.DEBUG) {
				Log.i(TAG, "shiftClosestDpi: closest allowed dpi after shift = " + closestDpi);
			}
		}
		return closestDpi;
	}
}
