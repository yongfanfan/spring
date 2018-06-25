package com.dark.api.util;

public class WebUtil {

	public static boolean isMoblie(String userAgent) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android", "ipad" };
		if (userAgent != null) {
			for (String mobileAgent : mobileAgents) {
				if (userAgent.toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
}
