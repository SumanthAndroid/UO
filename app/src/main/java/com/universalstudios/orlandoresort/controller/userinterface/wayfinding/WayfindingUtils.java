/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.wayfinding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * 
 * 
 * @author Steven Byle
 */
public class WayfindingUtils {
	private static final String TAG = WayfindingUtils.class.getSimpleName();

	public static boolean openWayfindingPage(Context context, String poiObjectJson, Integer poiTypeId) {
		if (context == null || poiObjectJson == null || poiTypeId == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "openWayfindingPage: context or poiObjectJson or poiTypeId is null");
			}
			return false;
		}

		Bundle wayfindingActivityBundle = WayfindingActivity.newInstanceBundle(poiObjectJson, poiTypeId);
		context.startActivity(new Intent(context, WayfindingActivity.class).putExtras(wayfindingActivityBundle));
		return true;
	}

}
