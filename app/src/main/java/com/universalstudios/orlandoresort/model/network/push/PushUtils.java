
package com.universalstudios.orlandoresort.model.network.push;

import com.universalstudios.orlandoresort.BuildConfig;

/**
 * @author Steven Byle
 */
public class PushUtils {
	public static String getSenderId() {
		return BuildConfig.PUSH_SENDER_ID;
	}
}
