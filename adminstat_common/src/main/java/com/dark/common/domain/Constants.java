package com.dark.common.domain;


public class Constants {
	
	//是否是调试模式
	public static Boolean IS_DEBUG;
	//资源物理根路径
	public static String RESOURCE_ROOT_PATH;
	//资源访问路径前缀
	public static String RESOURCE_ROOT_URL;
	
	public static final String RFC3339_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String OSS_ENDPOINT;
	
	//是否使用redis存储访问令牌
	public static Boolean TOKEN_STORE_USEREDIS;
	
	//是否使用oss存储文件
	public static Boolean OSS_USE;
	
	//redis host
	public static String REDIS_MASTER_HOST;
	public static String REDIS_MASTER_PASSWORD;
	
	public static String WEICHAT_APPID;
	public static String WEICHAT_APPSECRET;
	public static String CALLBACK_URL;
	public static int REQUEST_COUNT_DAY=600;//每天请求次数
	public static String LIUMI_HOST;
	public static String LIUMI_APPKEY;
	public static String LIUMI_APPSECRET;
	
	public static String TONGJI_HOST_HTTP;
	public static String TONGJI_HOST_HTTPS;
}
