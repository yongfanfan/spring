package com.dark.api.util;

import com.dark.common.domain.Constants;


public class PathManager {
	
	public static final String DIR_SEPARATOR = "/" ;

	/**
	 * 存放头像路径
	 * @return
	 */
	public static String getAvatarPath() {
		return "avatar" + DIR_SEPARATOR;
	}
	
	/**
	 * 客户端crash日志存放路径
	 * @param os
	 * @return
	 */
	public static String getAbsoluteLogPath(Integer os) {
		if (os == 0) {
			return Constants.RESOURCE_ROOT_PATH + "log" + DIR_SEPARATOR + "android" + DIR_SEPARATOR;
		} else {
			return Constants.RESOURCE_ROOT_PATH + "log" + DIR_SEPARATOR + "ios" + DIR_SEPARATOR;
		}
	}
}
